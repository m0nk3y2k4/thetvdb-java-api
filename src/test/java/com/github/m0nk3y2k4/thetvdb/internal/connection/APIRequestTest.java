/*
 * Copyright (C) 2019 - 2022 thetvdb-java-api Authors and Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.m0nk3y2k4.thetvdb.internal.connection;

import static com.github.m0nk3y2k4.thetvdb.api.enumeration.FundingModel.CONTRACT;
import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_BAD_METHOD_ERROR;
import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_CONFLICT_ERROR;
import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_NOT_AUTHORIZED_ERROR;
import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_NOT_FOUND_ERROR;
import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_SERVICE_UNAVAILABLE;
import static com.github.m0nk3y2k4.thetvdb.internal.connection.APIRequest.ERR_SEND;
import static com.github.m0nk3y2k4.thetvdb.internal.connection.APIRequest.ERR_UNEXPECTED_RESPONSE;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.CONTENT_ENCODING;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.CONTENT_LENGTH;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.DELETE;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.HEAD;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.PUT;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.JSON_SUCCESS;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.createResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.createUnauthorizedResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.defaultAPIHttpHeaders;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.defaultAPIHttpHeadersWithAuthorization;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpError.error;
import static org.mockserver.model.HttpRequest.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.zip.GZIPOutputStream;

import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.APIKeyImpl;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APISession.Status;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APICommunicationException;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APINotAuthorizedException;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APIPreconditionException;
import com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders;
import com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod;
import com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter.WithHttpsMockServer;
import com.github.m0nk3y2k4.thetvdb.testutils.parameterized.NullAndEmptyStringSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.Header;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.verify.VerificationTimes;

@WithHttpsMockServer
class APIRequestTest {

    private static final String JSON_ERROR = "{\"message\":\"%s\"}";

    private static Stream<Arguments> getResponse_respondWithHTTPErrorCode_verifyExceptionHandling() {
        return Stream.of(
                Arguments.of("/test/unauthorized", HttpStatusCode.UNAUTHORIZED_401, APINotAuthorizedException.class,
                        String.format(API_NOT_AUTHORIZED_ERROR, HttpStatusCode.UNAUTHORIZED_401.reasonPhrase())),
                Arguments.of("/test/notFound", HttpStatusCode.NOT_FOUND_404, APIException.class,
                        String.format(API_NOT_FOUND_ERROR, HttpStatusCode.NOT_FOUND_404.reasonPhrase())),
                Arguments.of("/test/methodNotAllowed", HttpStatusCode.METHOD_NOT_ALLOWED_405, APIException.class,
                        String.format(API_BAD_METHOD_ERROR, HttpStatusCode.METHOD_NOT_ALLOWED_405.reasonPhrase())),
                Arguments.of("/test/conflict", HttpStatusCode.CONFLICT_409, APIException.class,
                        String.format(API_CONFLICT_ERROR, HttpStatusCode.CONFLICT_409.reasonPhrase())),
                Arguments.of("/test/unavailable", HttpStatusCode.SERVICE_UNAVAILABLE_503, APIException.class,
                        API_SERVICE_UNAVAILABLE),
                Arguments.of("/test/badGateway", HttpStatusCode.BAD_GATEWAY_502, APICommunicationException.class,
                        String.format(ERR_UNEXPECTED_RESPONSE, HttpStatusCode.BAD_GATEWAY_502.code(),
                                HttpStatusCode.BAD_GATEWAY_502.reasonPhrase()))
        );
    }

    private static APIRequest createAPIRequestWith(String resource, HttpRequestMethod method, APISession.Status status,
            RemoteAPI remoteAPI) {
        APIRequest request = new APIRequest(resource, method) {};
        Optional.ofNullable(status).map(x -> {
            APISession session = new APISession(new APIKeyImpl.Builder()
                    .apiKey(String.valueOf(Objects.hash(resource, method, status, remoteAPI)))
                    .fundingModel(CONTRACT)
                    .build());
            session.setStatus(status);
            return session;
        }).ifPresent(request::setSession);
        Optional.ofNullable(remoteAPI).ifPresent(request::setRemoteAPI);
        return request;
    }

    @ParameterizedTest(name = "[{index}] String \"{0}\" is not a valid resource")
    @NullAndEmptyStringSource
    void newAPIRequest_withoutResource_verifyParameterValidation(String resource) {
        assertThatIllegalArgumentException().isThrownBy(() -> new TestAPIRequest(resource, DELETE));
    }

    @ParameterizedTest(name = "[{index}] Value \"{0}\" is not a valid request method")
    @NullSource
    void newAPIRequest_withoutRequestMethod_verifyParameterValidation(HttpRequestMethod method) {
        assertThatIllegalArgumentException().isThrownBy(() -> new TestAPIRequest("/test/missingMethod", method));
    }

    @Test
    void send_missingRemoteEndpoint_verifyPreconditionsCheck() {
        final APIRequest request = new TestAPIRequest("/test/missingEndpoint", GET);
        assertThatExceptionOfType(APIPreconditionException.class).isThrownBy(request::send);
    }

    @Test
    void send_withoutSession_verifyHttpMethodInAPIRequest(MockServerClient client, RemoteAPI remoteAPI)
            throws Exception {
        final String resource = "/test/requestMethod";
        APIRequest request = createAPIRequestWith(resource, HEAD, null, remoteAPI);
        request.send();
        client.verify(request().withMethod(HEAD.getName()).withPath(resource), VerificationTimes.exactly(1));
    }

    @Test
    void send_withoutSession_verifyHttpHeadersInAPIRequest(MockServerClient client, RemoteAPI remoteAPI)
            throws Exception {
        final String resource = "/test/requestHeadersWithoutSession";
        APIRequest request = createAPIRequestWith(resource, GET, null, remoteAPI);
        request.send();
        client.verify(request().withMethod(GET.getName()).withPath(resource)
                        .withHeaders(defaultAPIHttpHeaders()),
                VerificationTimes.exactly(1));
    }

    @Test
    void send_withUninitializedSession_verifyHttpHeadersInAPIRequest(MockServerClient client, RemoteAPI remoteAPI)
            throws Exception {
        final String resource = "/test/requestHeadersWithUninitializedSession";
        APIRequest request = createAPIRequestWith(resource, DELETE, Status.NOT_AUTHORIZED, remoteAPI);
        request.send();
        client.verify(request().withMethod(DELETE.getName()).withPath(resource)
                        .withHeaders(defaultAPIHttpHeaders()),
                VerificationTimes.exactly(1));
    }

    @Test
    void send_withFullyInitializedSession_verifyHttpHeadersInAPIRequest(MockServerClient client, RemoteAPI remoteAPI)
            throws Exception {
        final String resource = "/test/requestHeadersWithUninitializedSession";
        APISession session = new APISession(CONTRACT_APIKEY);
        session.setStatus(Status.AUTHORIZED);
        session.setLanguage("en");
        session.setToken("Some.JSONWeb.Token");
        APIRequest request = new TestAPIRequest(resource, PUT);
        request.setRemoteAPI(remoteAPI);
        request.setSession(session);
        request.send();
        client.verify(request().withMethod(PUT.getName()).withPath(resource)
                        .withHeaders(defaultAPIHttpHeadersWithAuthorization()),
                VerificationTimes.exactly(1));
    }

    @Test
    void send_withSomeRequestPreparation_verifyPreparationIsApplied(MockServerClient client, RemoteAPI remoteAPI)
            throws Exception {
        final String resource = "/test/prepareRequest";
        final Header preparation = header("Prepared", "true");
        APISession session = new APISession(CONTRACT_APIKEY);
        session.setStatus(Status.NOT_AUTHORIZED);
        APIRequest request = new TestAPIRequest(resource, GET) {
            @Override
            void prepareRequest(@Nonnull HttpsURLConnection con) {
                con.setRequestProperty(preparation.getName().getValue(), preparation.getValues().get(0).getValue());
            }
        };
        request.setRemoteAPI(remoteAPI);
        request.setSession(session);
        request.send();
        client.verify(request().withMethod(GET.getName()).withPath(resource)
                        .withHeaders(defaultAPIHttpHeaders().withEntry(preparation)),
                VerificationTimes.exactly(1));
    }

    @Test
    void getResponse_respondWithHTTP200_verifyJSONContentParsed(RemoteAPI remoteAPI) throws Exception {
        final String resource = "/test/success";
        APIRequest request = createAPIRequestWith(resource, GET, Status.NOT_AUTHORIZED, remoteAPI);
        JsonNode response = request.send();
        assertThat(response).hasToString(JSON_SUCCESS);
    }

    @ParameterizedTest(name = "[{index}] Code {1} is mapped into \"{2}\" with error message \"{3}\"")
    @MethodSource
    void getResponse_respondWithHTTPErrorCode_verifyExceptionHandling(String resource, HttpStatusCode status,
            Class<?> expectedException, String expectedErrorMessage, MockServerClient client, RemoteAPI remoteAPI) {
        APIRequest request = createAPIRequestWith(resource, DELETE, Status.NOT_AUTHORIZED, remoteAPI);
        client.when(request(resource))
                .respond(createResponse(status, String.format(JSON_ERROR, status.reasonPhrase())));
        Throwable exception = catchThrowable(request::send);
        assertThat(exception).isInstanceOf(expectedException).hasMessageContaining(expectedErrorMessage);
    }

    @Test
    void getResponse_respondWithHTTP405ErrorCode_verifyAllowHeadersArePrependedToErrorMessage(MockServerClient client,
            RemoteAPI remoteAPI) {
        final String resource = "/test/methodErrorWithAllowHeader";
        APIRequest request = createAPIRequestWith(resource, PUT, null, remoteAPI);
        client.when(request(resource)).respond(
                createResponse(HttpStatusCode.METHOD_NOT_ALLOWED_405, String
                        .format(JSON_ERROR, HttpStatusCode.METHOD_NOT_ALLOWED_405.reasonPhrase()))
                        .withHeader(HttpHeaders.ALLOW, "GET", "POST", "DELETE"));
        Throwable exception = catchThrowable(request::send);
        assertThat(exception).isInstanceOf(APIException.class)
                .hasMessageContaining(" - Response Allow header: [DELETE, GET, POST]");
    }

    @Test
    void getResponse_respondWithoutConnectionErrorStream_verifyEmptyJsonNodeIsReturned(MockServerClient client,
            RemoteAPI remoteAPI) {
        final String resource = "/test/noErrorStream";
        // Request method HEAD causes mock server to return NULL as connections error stream
        APIRequest request = createAPIRequestWith(resource, HEAD, null, remoteAPI);
        client.when(request(resource)).respond(createUnauthorizedResponse());
        Throwable exception = catchThrowable(request::send);
        assertThat(exception).isInstanceOf(APINotAuthorizedException.class)
                .hasMessageContaining(API_NOT_AUTHORIZED_ERROR, "n/a");
    }

    private static void withGzipEncodedResponse(String resource, HttpStatusCode status, String content,
            MockServerClient client) throws IOException {
        try (ByteArrayOutputStream contentStream = new ByteArrayOutputStream();
             OutputStream compressed = new GZIPOutputStream(contentStream, true)) {
            compressed.write(content.getBytes(UTF_8));
            compressed.flush();
            byte[] compressedContent = contentStream.toByteArray();
            client.when(request(resource))
                    .respond(HttpResponse.response().withHeaders(
                                    header(CONTENT_LENGTH, compressedContent.length),
                                    header(CONTENT_ENCODING, "gzip"))
                            .withStatusCode(status.code()).withReasonPhrase(status.reasonPhrase())
                            .withBody(compressedContent));
        }
    }

    @Test
    void getResponse_respondHTTP200WithEncodedData_verifyDataIsDecompressedBeforeParsing(MockServerClient client,
            RemoteAPI remoteAPI) throws Exception {
        final String resource = "/test/success";
        APIRequest request = createAPIRequestWith(resource, DELETE, Status.NOT_AUTHORIZED, remoteAPI);
        withGzipEncodedResponse(resource, HttpStatusCode.OK_200, JSON_SUCCESS, client);
        JsonNode response = request.send();
        assertThat(response).hasToString(JSON_SUCCESS);
    }

    @Test
    void getResponse_terminateConnection_verifyExceptionHandling(MockServerClient client, RemoteAPI remoteAPI) {
        final String resource = "/test/terminated";
        APIRequest request = createAPIRequestWith(resource, PUT, Status.NOT_AUTHORIZED, remoteAPI);
        client.when(request(resource)).error(error().withDropConnection(true));
        APICommunicationException exception = catchThrowableOfType(request::send, APICommunicationException.class);
        assertThat(exception).hasMessageContaining(ERR_SEND, PUT.getName());
    }

    @Test
    void getResponse_respondHTTP405WithEncodedData_verifyDataIsDecompressedBeforeParsing(MockServerClient client,
            RemoteAPI remoteAPI) throws Exception {
        final String resource = "/test/notFound";
        APIRequest request = createAPIRequestWith(resource, DELETE, Status.NOT_AUTHORIZED, remoteAPI);
        withGzipEncodedResponse(resource, HttpStatusCode.NOT_FOUND_404, JSON_ERROR, client);
        Throwable exception = catchThrowable(request::send);
        assertThat(exception).isInstanceOf(APIException.class)
                .hasMessageContaining(API_NOT_FOUND_ERROR, HttpStatusCode.NOT_FOUND_404.reasonPhrase());
    }

    private static class TestAPIRequest extends APIRequest {
        private TestAPIRequest(String resource, HttpRequestMethod requestMethod) {super(resource, requestMethod);}
    }
}

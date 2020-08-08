package com.github.m0nk3y2k4.thetvdb.internal.connection;

import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_CONFLICT_ERROR;
import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_NOT_AUTHORIZED_ERROR;
import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_NOT_FOUND_ERROR;
import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_SERVICE_UNAVAILABLE;
import static com.github.m0nk3y2k4.thetvdb.internal.connection.APIRequest.ERR_SEND;
import static com.github.m0nk3y2k4.thetvdb.internal.connection.APIRequest.ERR_UNEXPECTED_RESPONSE;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.JSON_SUCCESS;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.createResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.defaultAPIHttpHeaders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpError.error;
import static org.mockserver.model.HttpRequest.request;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APICommunicationException;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APINotAuthorizedException;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APIPreconditionException;
import com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod;
import com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter.WithHttpsMockServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.Header;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.verify.VerificationTimes;

@WithHttpsMockServer
class APIRequestTest {

    private static final String JSON_ERROR = "{\"Error\":\"%s\"}";

    private static Stream<Arguments> getResponse_respondWithHTTPErrorCode_verifyExceptionHandling() {
        return Stream.of(
                Arguments.of("/unauthorized", HttpStatusCode.UNAUTHORIZED_401, APINotAuthorizedException.class,
                        String.format(API_NOT_AUTHORIZED_ERROR, HttpStatusCode.UNAUTHORIZED_401.reasonPhrase())),
                Arguments.of("/notFound", HttpStatusCode.NOT_FOUND_404, APIException.class,
                        String.format(API_NOT_FOUND_ERROR, HttpStatusCode.NOT_FOUND_404.reasonPhrase())),
                Arguments.of("/conflict", HttpStatusCode.CONFLICT_409, APIException.class,
                        String.format(API_CONFLICT_ERROR, HttpStatusCode.CONFLICT_409.reasonPhrase())),
                Arguments.of("/unavailable", HttpStatusCode.SERVICE_UNAVAILABLE_503, APIException.class,
                        API_SERVICE_UNAVAILABLE),
                Arguments.of("/methodNotAllowed", HttpStatusCode.METHOD_NOT_ALLOWED_405, APICommunicationException.class,
                        String.format(ERR_UNEXPECTED_RESPONSE, HttpStatusCode.METHOD_NOT_ALLOWED_405.code(), HttpStatusCode.METHOD_NOT_ALLOWED_405.reasonPhrase()))
        );
    }

    @ParameterizedTest(name = "[{index}] String \"{0}\" is not a valid resource")
    @NullAndEmptySource @ValueSource(strings = {"   "})
    void newAPIRequest_withoutResource_verifyParameterValidation(String resource) {
        assertThatIllegalArgumentException().isThrownBy(() -> new APIRequest(resource, HttpRequestMethod.DELETE) {});
    }

    @ParameterizedTest(name = "[{index}] Value \"{0}\" is not a valid request method")
    @NullSource
    void newAPIRequest_withoutRequestMethod_verifyParameterValidation(HttpRequestMethod method) {
        assertThatIllegalArgumentException().isThrownBy(() -> new APIRequest("/missingMethod", method) {});
    }

    @Test
    void send_missingRemoteEndpoint_verifyPreconditionsCheck() {
        final APIRequest request = new APIRequest("/missingEndpoint", HttpRequestMethod.GET) {};
        assertThatExceptionOfType(APIPreconditionException.class).isThrownBy(request::send);
    }

    @Test
    void send_withoutSession_verifyHttpMethodInAPIRequest(MockServerClient client, RemoteAPI remoteAPI) throws Exception {
        final String resource = "/requestMethod";
        APIRequest request = createAPIRequestWith(resource, HttpRequestMethod.HEAD, null, remoteAPI);
        request.send();
        client.verify(request().withMethod(HttpRequestMethod.HEAD.getName()).withPath(resource), VerificationTimes.exactly(1));
    }

    @Test
    void send_withoutSession_verifyHttpHeadersInAPIRequest(MockServerClient client, RemoteAPI remoteAPI) throws Exception {
        final String resource = "/requestHeadersWithoutSession";
        APIRequest request = createAPIRequestWith(resource, HttpRequestMethod.GET, null, remoteAPI);
        request.send();
        client.verify(request().withMethod(HttpRequestMethod.GET.getName()).withPath(resource)
                        .withHeaders(defaultAPIHttpHeaders(false)),
                VerificationTimes.exactly(1));
    }

    @Test
    void send_withUninitializedSession_verifyHttpHeadersInAPIRequest(MockServerClient client, RemoteAPI remoteAPI) throws Exception {
        final String resource = "/requestHeadersWithUninitializedSession";
        APIRequest request = createAPIRequestWith(resource, HttpRequestMethod.DELETE, APISession.Status.NOT_AUTHORIZED, remoteAPI);
        request.send();
        client.verify(request().withMethod(HttpRequestMethod.DELETE.getName()).withPath(resource)
                        .withHeaders(defaultAPIHttpHeaders(false)),
                VerificationTimes.exactly(1));
    }

    @Test
    void send_withFullyInitializedSession_verifyHttpHeadersInAPIRequest(MockServerClient client, RemoteAPI remoteAPI) throws Exception {
        final String resource = "/requestHeadersWithUninitializedSession";
        APISession session = new APISession("WIOD548W9DLOF32W5S4DFFW");
        session.setStatus(APISession.Status.AUTHORIZED);
        session.setLanguage("en");
        session.setToken("Some.JSONWeb.Token");
        APIRequest request = new APIRequest(resource, HttpRequestMethod.PUT) {};
        request.setRemoteAPI(remoteAPI);
        request.setSession(session);
        request.send();
        client.verify(request().withMethod(HttpRequestMethod.PUT.getName()).withPath(resource)
                        .withHeaders(defaultAPIHttpHeaders(true)),
                VerificationTimes.exactly(1));
    }

    @Test
    void send_withSomeRequestPreparation_verifyPreparationIsApplied(MockServerClient client, RemoteAPI remoteAPI) throws Exception {
        final String resource = "/prepareRequest";
        final Header preparation = header("Prepared", "true");
        APISession session = new APISession("47D5SF8WWF85K5LZ4GRTZ7512");
        session.setStatus(APISession.Status.NOT_AUTHORIZED);
        APIRequest request = new APIRequest(resource, HttpRequestMethod.GET) {
            @Override
            void prepareRequest(@Nonnull HttpsURLConnection con) {
                con.setRequestProperty(preparation.getName().getValue(), preparation.getValues().get(0).getValue());
            }
        };
        request.setRemoteAPI(remoteAPI);
        request.setSession(session);
        request.send();
        client.verify(request().withMethod(HttpRequestMethod.GET.getName()).withPath(resource)
                        .withHeaders(defaultAPIHttpHeaders(false).withEntry(preparation)),
                VerificationTimes.exactly(1));
    }

    @Test
    void getResponse_respondWithHTTP200_verifyJSONContentParsed(RemoteAPI remoteAPI) throws Exception {
        final String resource = "/success";
        APIRequest request = createAPIRequestWith(resource, HttpRequestMethod.GET, APISession.Status.NOT_AUTHORIZED, remoteAPI);
        JsonNode response = request.send();
        assertThat(response).hasToString(JSON_SUCCESS);
    }

    @ParameterizedTest(name = "[{index}] Code {1} is mapped into \"{2}\" with error message \"{3}\"")
    @MethodSource
    void getResponse_respondWithHTTPErrorCode_verifyExceptionHandling(String resource, HttpStatusCode status, Class<?> expectedException,
                String expectedErrorMessage, MockServerClient client, RemoteAPI remoteAPI) {
        APIRequest request = createAPIRequestWith(resource, HttpRequestMethod.DELETE, APISession.Status.NOT_AUTHORIZED, remoteAPI);
        client.when(request(resource)).respond(createResponse(status, String.format(JSON_ERROR, status.reasonPhrase())));
        Throwable exception = catchThrowable(request::send);
        assertThat(exception).isInstanceOf(expectedException).hasMessageContaining(expectedErrorMessage);
    }

    @Test
    void getResponse_terminateConnection_verifyExceptionHandling(MockServerClient client, RemoteAPI remoteAPI) {
        final String resource = "/terminated";
        APIRequest request = createAPIRequestWith(resource, HttpRequestMethod.PUT, APISession.Status.NOT_AUTHORIZED, remoteAPI);
        client.when(request(resource)).error(error().withDropConnection(true));
        APICommunicationException exception = catchThrowableOfType(request::send, APICommunicationException.class);
        assertThat(exception).hasMessageContaining(ERR_SEND, HttpRequestMethod.PUT.getName());
    }

    private APIRequest createAPIRequestWith(String resource, HttpRequestMethod method, APISession.Status status, RemoteAPI remoteAPI) {
        APIRequest request = new APIRequest(resource, method) {};
        Optional.ofNullable(status).map(x -> {
            APISession session = new APISession(String.valueOf(Objects.hash(resource, method, status, remoteAPI)));
            session.setStatus(status);
            return session;
        }).ifPresent(request::setSession);
        Optional.ofNullable(remoteAPI).ifPresent(request::setRemoteAPI);
        return request;
    }
}
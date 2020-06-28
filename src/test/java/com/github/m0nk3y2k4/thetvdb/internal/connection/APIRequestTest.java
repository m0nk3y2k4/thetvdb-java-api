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
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpError.error;
import static org.mockserver.model.HttpRequest.request;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APICommunicationException;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APINotAuthorizedException;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APIPreconditionException;
import com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod;
import com.github.m0nk3y2k4.thetvdb.junit.jupiter.WithHttpsMockServer;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.Header;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.verify.VerificationTimes;

@WithHttpsMockServer
class APIRequestTest {

    private static final String JSON_ERROR = "{\"Error\":\"%s\"}";

    @Test
    void newAPIRequest_withoutResource_verifyParameterValidation() {
        assertThatIllegalArgumentException().isThrownBy(() -> new APIRequest(null, HttpRequestMethod.GET) {});
        assertThatIllegalArgumentException().isThrownBy(() -> new APIRequest("   ", HttpRequestMethod.DELETE) {});
    }

    @Test
    void newAPIRequest_withoutRequestMethod_verifyParameterValidation() {
        assertThatIllegalArgumentException().isThrownBy(() -> new APIRequest("/missingMethod", null) {});
    }

    @Test
    void send_missingRemoteEndpoint_verifyPreconditionsCheck() {
        assertThatExceptionOfType(APIPreconditionException.class).isThrownBy(() -> new APIRequest("/missingEndpoint", HttpRequestMethod.GET) {}.send());
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
        assertThat(response.toString()).isEqualTo(JSON_SUCCESS);
    }

    @Test
    void getResponse_respondWithHTTP401_verifyExceptionHandling(MockServerClient client, RemoteAPI remoteAPI) {
        final String resource = "/unauthorized";
        final HttpStatusCode status = HttpStatusCode.UNAUTHORIZED_401;
        APIRequest request = createAPIRequestWith(resource, HttpRequestMethod.DELETE, APISession.Status.NOT_AUTHORIZED, remoteAPI);
        client.when(request(resource)).respond(createResponse(status, String.format(JSON_ERROR, status.reasonPhrase())));
        APINotAuthorizedException exception = catchThrowableOfType(request::send, APINotAuthorizedException.class);
        assertThat(exception).hasMessageContaining(API_NOT_AUTHORIZED_ERROR, status.reasonPhrase());
    }

    @Test
    void getResponse_respondWithHTTP404_verifyExceptionHandling(MockServerClient client, RemoteAPI remoteAPI) {
        final String resource = "/notFound";
        final HttpStatusCode status = HttpStatusCode.NOT_FOUND_404;
        APIRequest request = createAPIRequestWith(resource, HttpRequestMethod.POST, APISession.Status.NOT_AUTHORIZED, remoteAPI);
        client.when(request(resource)).respond(createResponse(status, String.format(JSON_ERROR, status.reasonPhrase())));
        APIException exception = catchThrowableOfType(request::send, APIException.class);
        assertThat(exception).hasMessageContaining(API_NOT_FOUND_ERROR, status.reasonPhrase());
    }

    @Test
    void getResponse_respondWithHTTP409_verifyExceptionHandling(MockServerClient client, RemoteAPI remoteAPI) {
        final String resource = "/conflict";
        final HttpStatusCode status = HttpStatusCode.CONFLICT_409;
        APIRequest request = createAPIRequestWith(resource, HttpRequestMethod.PUT, APISession.Status.NOT_AUTHORIZED, remoteAPI);
        client.when(request(resource)).respond(createResponse(status, String.format(JSON_ERROR, status.reasonPhrase())));
        APIException exception = catchThrowableOfType(request::send, APIException.class);
        assertThat(exception).hasMessageContaining(API_CONFLICT_ERROR, status.reasonPhrase());
    }

    @Test
    void getResponse_respondWithHTTP503_verifyExceptionHandling(MockServerClient client, RemoteAPI remoteAPI) {
        final String resource = "/unavailable";
        final HttpStatusCode status = HttpStatusCode.SERVICE_UNAVAILABLE_503;
        APIRequest request = createAPIRequestWith(resource, HttpRequestMethod.GET, APISession.Status.NOT_AUTHORIZED, remoteAPI);
        client.when(request(resource)).respond(createResponse(status, String.format(JSON_ERROR, status.reasonPhrase())));
        APIException exception = catchThrowableOfType(request::send, APIException.class);
        assertThat(exception).hasMessageContaining(API_SERVICE_UNAVAILABLE);
    }

    @Test
    void getResponse_respondWithHTTP405_verifyExceptionHandling(MockServerClient client, RemoteAPI remoteAPI) {
        final String resource = "/methodNotAllowed";
        final HttpStatusCode status = HttpStatusCode.METHOD_NOT_ALLOWED_405;
        APIRequest request = createAPIRequestWith(resource, HttpRequestMethod.DELETE, APISession.Status.NOT_AUTHORIZED, remoteAPI);
        client.when(request(resource)).respond(createResponse(status, String.format(JSON_ERROR, status.reasonPhrase())));
        APICommunicationException exception = catchThrowableOfType(request::send, APICommunicationException.class);
        assertThat(exception).hasMessageContaining(ERR_UNEXPECTED_RESPONSE, status.code(), status.reasonPhrase());
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
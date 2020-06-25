package com.github.m0nk3y2k4.thetvdb.internal.connection;

import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.DELETE;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.HEAD;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.POST;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.PUT;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.JSON_DATA;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.createUnauthorizedResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockserver.model.HttpRequest.request;

import java.util.function.Supplier;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.junit.jupiter.WithHttpsMockServer;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.matchers.Times;
import org.mockserver.verify.VerificationTimes;

@WithHttpsMockServer
class APIConnectionTest {

    private final APIConnection con;

    public APIConnectionTest(Supplier<RemoteAPI> remoteAPI) {
        con = new APIConnection("API-Key", remoteAPI);
    }

    @Test
    void newAPIConnection_withSimpleCredentials_verifySessionProperties() {
        final String apiKey = "SHJWSJDHUWH544SDASF2";
        APIConnection simpleConnection = new APIConnection(apiKey);
        assertThat(simpleConnection.getApiKey()).isEqualTo(apiKey);
        assertThat(simpleConnection.userAuthentication()).isFalse();
        assertThat(simpleConnection.getRemoteAPI()).isEqualTo(new RemoteAPI.Builder().build());
    }

    @Test
    void newAPIConnection_withSimpleCredentialsAndRemoteAPI_verifySessionProperties() {
        final String apiKey = "5DAS84FASWIJIS7466";
        final RemoteAPI remoteAPI = new RemoteAPI.Builder().protocol("http").host("myHost").port(3358).build();
        APIConnection simpleConnection = new APIConnection(apiKey, () -> remoteAPI);
        assertThat(simpleConnection.getApiKey()).isEqualTo(apiKey);
        assertThat(simpleConnection.userAuthentication()).isFalse();
        assertThat(simpleConnection.getRemoteAPI()).isEqualTo(remoteAPI);
    }

    @Test
    void newAPIConnection_withExtendedCredentials_verifySessionProperties() {
        final String apiKey = "UUZED71122WSAZDHA85A4";
        final String userKey = "unique_4555145247623";
        final String userName = "Sarah Connor";
        APIConnection extendedConnection = new APIConnection(apiKey, userKey, userName);
        assertThat(extendedConnection.getApiKey()).isEqualTo(apiKey);
        assertThat(extendedConnection.userAuthentication()).isTrue();
        assertThat(extendedConnection.getUserKey()).contains(userKey);
        assertThat(extendedConnection.getUserName()).contains(userName);
        assertThat(extendedConnection.getRemoteAPI()).isEqualTo(new RemoteAPI.Builder().build());
    }

    @Test
    void newAPIConnection_withExtendedCredentialsAndRemoteAPI_verifySessionProperties() {
        final String apiKey = "DAS7SAFRIIO85J6HH122";
        final String userKey = "unique_954872215412";
        final String userName = "Captain Kirk";
        final RemoteAPI remoteAPI = new RemoteAPI.Builder().protocol("https").host("someOtherHost").port(1678).build();
        APIConnection extendedConnection = new APIConnection(apiKey, userKey, userName, () -> remoteAPI);
        assertThat(extendedConnection.getApiKey()).isEqualTo(apiKey);
        assertThat(extendedConnection.userAuthentication()).isTrue();
        assertThat(extendedConnection.getUserKey()).contains(userKey);
        assertThat(extendedConnection.getUserName()).contains(userName);
        assertThat(extendedConnection.getRemoteAPI()).isEqualTo(remoteAPI);
    }

    @Test
    void sendGET_verifyHTTPMethodInRequest(MockServerClient client) throws Exception {
        final String resource = "/methodGET";
        con.sendGET(resource);
        client.verify(request().withMethod(GET.getName()).withPath(resource), VerificationTimes.once());
    }

    @Test
    void sendPOST_verifyHTTPMethodInRequest(MockServerClient client) throws Exception {
        final String resource = "/methodPOST";
        con.sendPOST(resource, JSON_DATA);
        client.verify(request().withMethod(POST.getName()).withPath(resource), VerificationTimes.once());
    }

    @Test
    void sendHEAD_verifyHTTPMethodInRequest(MockServerClient client) throws Exception {
        final String resource = "/methodHEAD";
        con.sendHEAD(resource);
        client.verify(request().withMethod(HEAD.getName()).withPath(resource), VerificationTimes.once());
    }

    @Test
    void sendDELETE_verifyHTTPMethodInRequest(MockServerClient client) throws Exception {
        final String resource = "/methodDELETE";
        con.sendDELETE(resource);
        client.verify(request().withMethod(DELETE.getName()).withPath(resource), VerificationTimes.once());
    }

    @Test
    void sendPUT_verifyHTTPMethodInRequest(MockServerClient client) throws Exception {
        final String resource = "/methodPUT";
        con.sendPUT(resource);
        client.verify(request().withMethod(PUT.getName()).withPath(resource), VerificationTimes.once());
    }

    @Test
    void setToken_verifyToken() throws Exception {
        final String token = "Header.Payload.Signature";
        con.setToken(token);
        assertThat(con.getToken()).contains(token);
    }

    @Test
    void setStatus_verifyStatus() {
        con.setStatus(APISession.Status.AUTHORIZED);
        assertThat(con.getSession()).extracting(APISession::getStatus).isEqualTo(APISession.Status.AUTHORIZED);
    }

    @Test
    void setLanguage_verifyLanguage() {
        final String language = "Klingon";
        con.setLanguage(language);
        assertThat(con.getSession()).extracting(APISession::getLanguage).isEqualTo(language);
    }

    @Test
    void sendRequest_abortAfterMaxRetryCount(MockServerClient client) {
        final String resource = "/retry";
        con.getSession().setStatus(APISession.Status.NOT_AUTHORIZED);       // Allow to trigger auto-authorization
        client.when(request(resource), Times.exactly(3)).respond(createUnauthorizedResponse());
        APIException exception = catchThrowableOfType(() -> con.sendGET(resource), APIException.class);
        assertThat(exception).hasMessageContaining("after 3 retries");
    }

    @Test
    void sendRequest_automaticAuthorizationSuccess(MockServerClient client) throws Exception {
        final String resource = "/autoAuthSuccess";
        con.getSession().setStatus(APISession.Status.NOT_AUTHORIZED);       // Allow to trigger auto-authorization
        client.when(request(resource), Times.once()).respond(createUnauthorizedResponse());
        con.sendGET(resource);
        client.verify(request(resource), VerificationTimes.exactly(2));
    }

    @Test
    void sendRequest_automaticAuthorizationFailed(MockServerClient client) {
        final String resource = "/autoAuthFailed";
        con.getSession().setStatus(APISession.Status.NOT_AUTHORIZED);       // Allow to trigger auto-authorization
        client.when(request(resource), Times.once()).respond(createUnauthorizedResponse());
        client.when(request("/login"), Times.once()).respond(createUnauthorizedResponse());
        APIException exception = catchThrowableOfType(() -> con.sendGET(resource), APIException.class);
        assertThat(exception).hasMessageContaining("authorization failed");
    }

}
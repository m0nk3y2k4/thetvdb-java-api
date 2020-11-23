/*
 * Copyright (C) 2019 - 2020 thetvdb-java-api Authors and Contributors
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

import static com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection.ERR_MAX_RETRY_EXCEEDED;
import static com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection.MAX_AUTHENTICATION_RETRY_COUNT;
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
import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APISession.Status;
import com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter.WithHttpsMockServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockserver.client.MockServerClient;
import org.mockserver.matchers.Times;
import org.mockserver.verify.VerificationTimes;

@WithHttpsMockServer
class APIConnectionTest {

    private static final String METHOD_RESOURCE = "/test/method";

    private final APIConnection con;

    APIConnectionTest(Supplier<RemoteAPI> remoteAPI) {
        con = new APIConnection("API-Key", remoteAPI);
    }

    private static Stream<Arguments> sendRequest_verifyHTTPMethodInRequest() {
        return Stream.of(
                Arguments.of((Request)con -> con.sendGET(METHOD_RESOURCE + GET), GET.getName()),
                Arguments.of((Request)con -> con.sendPOST(METHOD_RESOURCE + POST, JSON_DATA), POST.getName()),
                Arguments.of((Request)con -> con.sendHEAD(METHOD_RESOURCE + HEAD), HEAD.getName()),
                Arguments.of((Request)con -> con.sendDELETE(METHOD_RESOURCE + DELETE), DELETE.getName()),
                Arguments.of((Request)con -> con.sendPUT(METHOD_RESOURCE + PUT), PUT.getName())
        );
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

    @ParameterizedTest(name = "[{index}] Verifying {1} request")
    @MethodSource
    void sendRequest_verifyHTTPMethodInRequest(Request request, String httpMethod, MockServerClient client)
            throws Exception {
        request.send(con);
        client.verify(request().withMethod(httpMethod).withPath(METHOD_RESOURCE + httpMethod), VerificationTimes
                .once());
    }

    @Test
    void setToken_verifyToken() throws Exception {
        final String token = "Header.Payload.Signature";
        con.setToken(token);
        assertThat(con.getToken()).contains(token);
    }

    @Test
    void setStatus_verifyStatus() {
        con.setStatus(Status.AUTHORIZED);
        assertThat(con.getSession()).extracting(APISession::getStatus).isEqualTo(Status.AUTHORIZED);
    }

    @Test
    void setLanguage_verifyLanguage() {
        final String language = "Klingon";
        con.setLanguage(language);
        assertThat(con.getSession()).extracting(APISession::getLanguage).isEqualTo(language);
    }

    @Test
    void sendRequest_abortAfterMaxRetryCount(MockServerClient client) {
        final String resource = "/auth/retry";
        con.getSession().setStatus(Status.NOT_AUTHORIZED);       // Allow to trigger auto-authorization
        client.when(request(resource), Times.exactly(3)).respond(createUnauthorizedResponse());
        APIException exception = catchThrowableOfType(() -> con.sendGET(resource), APIException.class);
        assertThat(exception).hasMessageContaining(ERR_MAX_RETRY_EXCEEDED, MAX_AUTHENTICATION_RETRY_COUNT);
    }

    @Test
    void sendRequest_automaticAuthorizationSuccess(MockServerClient client) throws Exception {
        final String resource = "/auth/autoAuthSuccess";
        con.getSession().setStatus(Status.NOT_AUTHORIZED);       // Allow to trigger auto-authorization
        con.sendGET(resource);
        client.verify(request(resource), VerificationTimes.exactly(2));
    }

    @Test
    void sendRequest_automaticAuthorizationFailed(MockServerClient client) {
        final String resource = "/auth/autoAuthFailed";
        con.getSession().setStatus(Status.NOT_AUTHORIZED);       // Allow to trigger auto-authorization
        client.when(request("/login"), Times.once()).respond(createUnauthorizedResponse());
        APIException exception = catchThrowableOfType(() -> con.sendGET(resource), APIException.class);
        assertThat(exception).hasMessageContaining("authorization failed");
    }

    @FunctionalInterface
    private interface Request {
        void send(APIConnection con) throws APIException;
    }

}

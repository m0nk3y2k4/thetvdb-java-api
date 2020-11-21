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

package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.POST;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.createJWTResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonSchemaFromResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;

import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APISession.Status;
import com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI;
import com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter.WithHttpsMockServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockserver.client.MockServerClient;
import org.mockserver.verify.VerificationTimes;

@WithHttpsMockServer
class AuthenticationAPITest {

    private static RemoteAPI remote;

    @BeforeAll
    static void init(RemoteAPI remoteAPI) {
        remote = remoteAPI;
    }

    private static Stream<Arguments> login() {
        return Stream.of(
                Arguments.of(new Connection("JDUW78DDEF8Z3PSER54"), "login_apiKey.json"),
                Arguments.of(new Connection("PWLF4E5WKE89ZEJUESI", "unique_848763316974", "Emmett Brown"),
                        "login_userAuth.json")
        );
    }

    @ParameterizedTest(name = "[{index}] Login request with {0}")
    @MethodSource("login")
    void login_verifySuccessfullyLoggedIn(Connection con, String jsonSchemaName, MockServerClient client)
            throws Exception {
        AuthenticationAPI.login(con);
        client.verify(request("/login").withMethod(POST.getName())
                .withBody(jsonSchemaFromResource(jsonSchemaName)), VerificationTimes.once());
        assertThat(con.getToken()).isPresent();
        assertThat(con.getSessionStatus()).isEqualTo(Status.AUTHORIZED);
    }

    @Test
    void refreshSession(MockServerClient client) throws Exception {
        final String resource = "/refresh_token";
        Connection con = new Connection("75G8I5RFFEKR8E75GFF");
        client.when(request(resource).withMethod(GET.getName())).respond(createJWTResponse());
        AuthenticationAPI.refreshSession(con);
        client.verify(request(resource).withMethod(GET.getName()), VerificationTimes.once());
        assertThat(con.getToken()).isPresent();
        assertThat(con.getSessionStatus()).isEqualTo(Status.AUTHORIZED);
    }

    /**
     * Extends the regular API connection, providing access to the sessions current (login) status
     */
    private static final class Connection extends APIConnection {
        private Connection(String apiKey) {
            super(apiKey, () -> remote);
        }

        @SuppressWarnings("SameParameterValue")
        private Connection(String apiKey, String userKey, String userName) {
            super(apiKey, userKey, userName, () -> remote);
        }

        private Status getSessionStatus() {
            return super.getStatus();
        }

        @Override
        public String toString() {
            return String.format("[apiKey=%s, userKey=%s, userName=%s]", getApiKey(), getUserKey()
                    .orElse(null), getUserName().orElse(null));
        }
    }
}

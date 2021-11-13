/*
 * Copyright (C) 2019 - 2021 thetvdb-java-api Authors and Contributors
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

import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.POST;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.INVALID_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.SUBSCRIPTION_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonSchemaFromResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockserver.model.HttpRequest.request;

import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.api.APIKey;
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
class LoginAPITest {

    private static RemoteAPI remote;

    @BeforeAll
    static void init(RemoteAPI remoteAPI) {
        remote = remoteAPI;
    }

    private static Stream<Arguments> login() {
        return Stream.of(
                Arguments.of(CONTRACT_APIKEY, "login_apiKey.json"),
                Arguments.of(SUBSCRIPTION_APIKEY, "login_userAuth.json")
        );
    }

    @ParameterizedTest(name = "[{index}] Login request with {0}")
    @MethodSource("login")
    void login_verifySuccessfullyLoggedIn(APIKey apiKey, String jsonSchemaName, MockServerClient client)
            throws Exception {
        Connection con = new Connection(apiKey);
        LoginAPI.login(con);
        client.verify(request("/login").withMethod(POST.getName())
                .withBody(jsonSchemaFromResource(jsonSchemaName)), VerificationTimes.once());
        assertThat(con.getToken()).isPresent();
        assertThat(con.getSessionStatus()).isEqualTo(Status.AUTHORIZED);
    }

    @Test
    void login_withInvalidApiKey_exceptionThrown() {
        APIConnection con = new ConnectionWithInvalidApiKey();
        assertThatIllegalStateException().isThrownBy(() -> LoginAPI.login(con))
                .withMessage("For user subscription based authentication a PIN is required");
    }

    /**
     * Extends the regular API connection, providing access to the sessions current (login) status
     */
    private static class Connection extends APIConnection {
        private Connection(APIKey apiKey) {
            super(apiKey, remote);
        }

        private Status getSessionStatus() {
            return super.getStatus();
        }
    }

    /**
     * Specific API connection modified to return an invalid API key
     */
    private static final class ConnectionWithInvalidApiKey extends Connection {
        private ConnectionWithInvalidApiKey() {
            super(CONTRACT_APIKEY); // The super constructor will check the API key, so we have to use a valid one here
        }

        @Override
        public APIKey getApiKey() {
            return INVALID_APIKEY; // This key will be returned to LoginAPI#login()
        }
    }
}

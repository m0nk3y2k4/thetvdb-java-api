package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.POST;
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
                Arguments.of(new Connection("PWLF4E5WKE89ZEJUESI", "unique_848763316974", "Emmett Brown"), "login_userAuth.json")
        );
    }

    @ParameterizedTest(name = "[{index}] Login request with {0}")
    @MethodSource(value = "login")
    void login_verifySuccessfullyLoggedIn(Connection con, String jsonSchemaName, MockServerClient client) throws Exception {
        AuthenticationAPI.login(con);
        client.verify(request().withMethod(POST.getName()).withPath("/login")
                .withBody(jsonSchemaFromResource(jsonSchemaName)), VerificationTimes.once());
        assertThat(con.getToken()).isPresent();
        assertThat(con.getSessionStatus()).isEqualTo(Status.AUTHORIZED);
    }

    @Test
    void refreshSession(MockServerClient client) throws Exception {
        Connection con = new Connection("75G8I5RFFEKR8E75GFF");
        AuthenticationAPI.refreshSession(con);
        client.verify(request().withMethod(GET.getName()).withPath("/refresh_token"), VerificationTimes.once());
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
            return String.format("[apiKey=%s, userKey=%s, userName=%s]", getApiKey(), getUserKey().orElse(null), getUserName().orElse(null));
        }
    }
}


package com.github.m0nk3y2k4.thetvdb.api;

import static com.github.m0nk3y2k4.thetvdb.testutils.assertj.IntegrationTestAssertions.assertThat;

import com.github.m0nk3y2k4.thetvdb.testutils.annotation.IntegrationTestSuite;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@IntegrationTestSuite("Authentication")
class AuthenticationIT {

    @Test @Order(1)
    void login(TheTVDBApi api) {
        assertThat(api::login).as("/login").doesNotThrowAnyException();
    }

    @Test @Order(2)
    void refreshToken(TheTVDBApi api) {
        assertThat(api::refreshToken).as("/refresh_token").doesNotThrowAnyException();
    }
}

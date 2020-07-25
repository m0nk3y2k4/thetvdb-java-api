package com.github.m0nk3y2k4.thetvdb.internal.connection;

import static com.github.m0nk3y2k4.thetvdb.internal.connection.APISession.ERR_JWT_EMPTY;
import static com.github.m0nk3y2k4.thetvdb.internal.connection.APISession.ERR_JWT_INVALID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

class APISessionTest {

    private static Stream<Arguments> newAPISession_extendedWithMissingParameters_verifyParameterValidation() {
        return Stream.of(
                Arguments.of(null, "myUserKey", "HummaKavula"),
                Arguments.of("SZWD8F9N4JF5G", null, "FordPrefect"),
                Arguments.of("5DA323J3I42D", "myOtherUserKey", null)
        );
    }

    private static Stream<Arguments> setLanguage_verifyLanguage() {
        return Stream.of(Arguments.of(null, "en"), Arguments.of("es", "es"));
    }

    private static Stream<Arguments> setStatus_verifyStatus() {
        return Stream.of(
                Arguments.of(null, APISession.Status.NOT_AUTHORIZED),
                Arguments.of(APISession.Status.AUTHORIZED, APISession.Status.AUTHORIZED)
        );
    }

    private static Stream<Arguments> isInitialized_setStatusAndVerifyInitialized() {
        return Stream.of(
                Arguments.of(APISession.Status.NOT_AUTHORIZED, false),
                Arguments.of(APISession.Status.AUTHORIZATION_IN_PROGRESS, false),
                Arguments.of(APISession.Status.AUTHORIZED, true)
        );
    }

    @Test
    void newAPISession_withSimpleCredentials_verifyProperties() {
        final String apiKey = "ZS2F8EW2I5W8F3";
        APISession session = new APISession(apiKey);
        assertThat(session.getApiKey()).isEqualTo(apiKey);
        assertThat(session.getUserKey()).isEmpty();
        assertThat(session.getUserName()).isEmpty();
        assertThat(session.getLanguage()).isEqualTo("en");
        assertThat(session.getStatus()).isEqualTo(APISession.Status.NOT_AUTHORIZED);
        assertThat(session.getToken()).isEmpty();
    }

    @ParameterizedTest(name = "[{index}] Value {0} is not a valid API key")
    @NullSource
    void newAPISession_simpleWithMissingParameters_verifyParameterValidation(String apiKey) {
        assertThatIllegalArgumentException().isThrownBy(() -> new APISession(apiKey));
    }

    @Test
    void newAPISession_withExtendedCredentials_verifyProperties() {
        final String apiKey = "D7G51DAW8H5A87";
        final String userKey = "yetAnotherKey";
        final String userName = "ArthurDent";
        APISession session = new APISession(apiKey, userKey, userName);
        assertThat(session.getApiKey()).isEqualTo(apiKey);
        assertThat(session.getUserKey()).contains(userKey);
        assertThat(session.getUserName()).contains(userName);
        assertThat(session.getLanguage()).isEqualTo("en");
        assertThat(session.getStatus()).isEqualTo(APISession.Status.NOT_AUTHORIZED);
        assertThat(session.getToken()).isEmpty();
    }

    @ParameterizedTest(name = "[{index}] Verify unable to create session with [apiKey={0}, userKey={1}, userName={2}]")
    @MethodSource
    void newAPISession_extendedWithMissingParameters_verifyParameterValidation(String apiKey, String userKey, String userName) {
        assertThatIllegalArgumentException().isThrownBy(() -> new APISession(apiKey, userKey, userName));
    }

    @Test
    void setToken_verifyTokenAndAuthorization() throws Exception {
        final String token = "Some.JSONWeb.Token";
        APISession session = new APISession("OERIFH452DU");
        session.setStatus(APISession.Status.AUTHORIZATION_IN_PROGRESS);
        session.setToken(token);
        assertThat(session.getToken()).contains(token);
        assertThat(session.getStatus()).isEqualTo(APISession.Status.AUTHORIZED);
    }

    @Test
    void setToken_noToken_verifyParameterValidation() {
        APISession session = new APISession("OERIFH452DU");
        APIException exception = catchThrowableOfType(() -> session.setToken(null), APIException.class);
        assertThat(exception).hasMessageContaining(ERR_JWT_EMPTY);
    }

    @Test
    void setToken_invalidToken_verifyParameterValidation() {
        final String token = "SomeInvalidJSONWebToken";
        APISession session = new APISession("OERIFH452DU");
        APIException exception = catchThrowableOfType(() -> session.setToken(token), APIException.class);
        assertThat(exception).hasMessageContaining(ERR_JWT_INVALID, token);
    }

    @ParameterizedTest(name = "[{index}] Setting language to \"{0}\" results in \"{1}\"")
    @MethodSource
    void setLanguage_verifyLanguage(String language, String expected) {
        APISession session = new APISession("K512E3A7F8SWRT");
        session.setLanguage(language);
        assertThat(session.getLanguage()).isEqualTo(expected);
    }

    @ParameterizedTest(name = "[{index}] Setting status to \"{0}\" results in \"{1}\"")
    @MethodSource
    void setStatus_verifyStatus(APISession.Status status, APISession.Status expected) {
        APISession session = new APISession("LDIWQ532D7WQ");
        session.setStatus(status);
        assertThat(session.getStatus()).isEqualTo(expected);
    }

    @ParameterizedTest(name = "[{index}] Setting status to \"{0}\" results in isInitialized={1}")
    @MethodSource
    void isInitialized_setStatusAndVerifyInitialized(APISession.Status status, boolean expected) {
        APISession session = new APISession("32DLFO5W4F2S");
        session.setStatus(status);
        assertThat(session.isInitialized()).isEqualTo(expected);
    }

    @Test
    void userAuthentication_simpleAPISession_userAuthenticationDisabled() {
        APISession session = new APISession("D6F35PM127E8T");
        assertThat(session.userAuthentication()).isFalse();
    }

    @Test
    void userAuthentication_extendedAPISession_userAuthenticationEnabled() {
        APISession session = new APISession("32F6SHL5G4DL", "uSeRkEy", "Slartibartfast");
        assertThat(session.userAuthentication()).isTrue();
    }
}
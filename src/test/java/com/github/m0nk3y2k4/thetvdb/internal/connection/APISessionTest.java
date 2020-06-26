package com.github.m0nk3y2k4.thetvdb.internal.connection;

import static com.github.m0nk3y2k4.thetvdb.internal.connection.APISession.ERR_JWT_EMPTY;
import static com.github.m0nk3y2k4.thetvdb.internal.connection.APISession.ERR_JWT_INVALID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import org.junit.jupiter.api.Test;

class APISessionTest {

    @Test
    void newAPISession_simpleWithParameters_verifyParameterValidation() {
        final String apiKey = "ZS2F8EW2I5W8F3";
        APISession session = new APISession(apiKey);
        assertThat(session.getApiKey()).isEqualTo(apiKey);
        assertThat(session.getUserKey()).isEmpty();
        assertThat(session.getUserName()).isEmpty();
    }

    @Test
    void newAPISession_simpleWithMissingParameters_verifyParameterValidation() {
        assertThatIllegalArgumentException().isThrownBy(() -> new APISession(null));
    }

    @Test
    void newAPISession_extendedWithParameters_verifyParameterValidation() {
        final String apiKey = "D7G51DAW8H5A87";
        final String userKey = "yetAnotherKey";
        final String userName = "ArthurDent";
        APISession session = new APISession(apiKey, userKey, userName);
        assertThat(session.getApiKey()).isEqualTo(apiKey);
        assertThat(session.getUserKey()).contains(userKey);
        assertThat(session.getUserName()).contains(userName);
    }

    @Test
    void newAPISession_extendedWithMissingParameters_verifyParameterValidation() {
        assertThatIllegalArgumentException().isThrownBy(() -> new APISession(null, "myUserKey", "HummaKavula"));
        assertThatIllegalArgumentException().isThrownBy(() -> new APISession("SZWD8F9N4JF5G", null, "FordPrefect"));
        assertThatIllegalArgumentException().isThrownBy(() -> new APISession("5DA323J3I42D", "myOtherUserKey", null));
    }

    @Test
    void newAPISession_simpleWithParameters_verifyDefaultSettings() {
        final String apiKey = "OIE513DUWRSD5";
        APISession session = new APISession(apiKey);
        assertThat(session.getLanguage()).isEqualTo("en");
        assertThat(session.getStatus()).isEqualTo(APISession.Status.NOT_AUTHORIZED);
        assertThat(session.getToken()).isEmpty();
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

    @Test
    void setLanguage_verifyLanguage() {
        final String language = "es";
        APISession session = new APISession("K512E3A7F8SWRT");
        session.setLanguage(language);
        assertThat(session.getLanguage()).isEqualTo(language);
    }

    @Test
    void setLanguage_noLanguage_verifyDefaultLanguageIsSet() {
        APISession session = new APISession("93DAS5KJ4A5S");
        session.setLanguage(null);
        assertThat(session.getLanguage()).isEqualTo("en");
    }

    @Test
    void setStatus_verifyStatus() {
        APISession session = new APISession("LDIWQ532D7WQ");
        session.setStatus(APISession.Status.AUTHORIZATION_IN_PROGRESS);
        assertThat(session.getStatus()).isEqualTo(APISession.Status.AUTHORIZATION_IN_PROGRESS);
    }

    @Test
    void setStatus_noStatus_verifyDefaultStatusIsSet() {
        APISession session = new APISession("P5F4U5K2FDLDD");
        session.setStatus(null);
        assertThat(session.getStatus()).isEqualTo(APISession.Status.NOT_AUTHORIZED);
    }

    @Test
    void isInitialized_statusNOTAUTHORIZED_sessionIsNotInitialized() {
        APISession session = new APISession("32DLFO5W4F2S");
        session.setStatus(APISession.Status.NOT_AUTHORIZED);
        assertThat(session.isInitialized()).isFalse();
    }

    @Test
    void isInitialized_statusAUTHORIZATIONINPROGRESS_sessionIsNotInitialized() {
        APISession session = new APISession("LPFO5D7JU5TG1");
        session.setStatus(APISession.Status.AUTHORIZATION_IN_PROGRESS);
        assertThat(session.isInitialized()).isFalse();
    }

    @Test
    void isInitialized_statusAUTHORIZED_sessionIsInitialized() {
        APISession session = new APISession("X1G8Q4G5RFW6");
        session.setStatus(APISession.Status.AUTHORIZED);
        assertThat(session.isInitialized()).isTrue();
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
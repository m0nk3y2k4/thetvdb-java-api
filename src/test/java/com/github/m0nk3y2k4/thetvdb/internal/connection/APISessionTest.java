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

package com.github.m0nk3y2k4.thetvdb.internal.connection;

import static com.github.m0nk3y2k4.thetvdb.internal.connection.APISession.ERR_JWT_EMPTY;
import static com.github.m0nk3y2k4.thetvdb.internal.connection.APISession.ERR_JWT_INVALID;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.api.APIKey;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APISession.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

@SuppressWarnings("ConstantConditions")
class APISessionTest {

    private static Stream<Arguments> setLanguage_verifyLanguage() {
        return Stream.of(Arguments.of(null, "en"), Arguments.of("es", "es"));
    }

    private static Stream<Arguments> setStatus_verifyStatus() {
        return Stream.of(
                Arguments.of(null, Status.NOT_AUTHORIZED),
                Arguments.of(Status.AUTHORIZED, Status.AUTHORIZED)
        );
    }

    private static Stream<Arguments> isInitialized_setStatusAndVerifyInitialized() {
        return Stream.of(
                Arguments.of(Status.NOT_AUTHORIZED, false),
                Arguments.of(Status.AUTHORIZATION_IN_PROGRESS, false),
                Arguments.of(Status.AUTHORIZED, true)
        );
    }

    @Test
    void newAPISession_verifyProperties() {
        APISession session = new APISession(CONTRACT_APIKEY);
        assertThat(session.getApiKey()).isEqualTo(CONTRACT_APIKEY);
        assertThat(session.getLanguage()).isEqualTo("en");
        assertThat(session.getStatus()).isEqualTo(Status.NOT_AUTHORIZED);
        assertThat(session.getToken()).isEmpty();
    }

    @ParameterizedTest(name = "[{index}] Value {0} is not a valid API key")
    @NullSource
    void newAPISession_withInvalidApiKey_verifyParameterValidation(APIKey apiKey) {
        assertThatIllegalArgumentException().isThrownBy(() -> new APISession(apiKey));
    }

    @Test
    void setToken_verifyTokenAndAuthorization() throws Exception {
        final String token = "Some.JSONWeb.Token";
        APISession session = new APISession(CONTRACT_APIKEY);
        session.setStatus(Status.AUTHORIZATION_IN_PROGRESS);
        session.setToken(token);
        assertThat(session.getToken()).contains(token);
        assertThat(session.getStatus()).isEqualTo(Status.AUTHORIZED);
    }

    @Test
    void setToken_noToken_verifyParameterValidation() {
        APISession session = new APISession(CONTRACT_APIKEY);
        assertThatExceptionOfType(APIException.class).isThrownBy(() -> session.setToken(null))
                .withMessageContaining(ERR_JWT_EMPTY);
    }

    @Test
    void setToken_invalidToken_verifyParameterValidation() {
        final String token = "SomeInvalidJSONWebToken";
        APISession session = new APISession(CONTRACT_APIKEY);
        assertThatExceptionOfType(APIException.class).isThrownBy(() -> session.setToken(token))
                .withMessageContaining(ERR_JWT_INVALID, token);
    }

    @ParameterizedTest(name = "[{index}] Setting language to \"{0}\" results in \"{1}\"")
    @MethodSource
    void setLanguage_verifyLanguage(String language, String expected) {
        APISession session = new APISession(CONTRACT_APIKEY);
        session.setLanguage(language);
        assertThat(session.getLanguage()).isEqualTo(expected);
    }

    @ParameterizedTest(name = "[{index}] Setting status to \"{0}\" results in \"{1}\"")
    @MethodSource
    void setStatus_verifyStatus(APISession.Status status, APISession.Status expected) {
        APISession session = new APISession(CONTRACT_APIKEY);
        session.setStatus(status);
        assertThat(session.getStatus()).isEqualTo(expected);
    }

    @ParameterizedTest(name = "[{index}] Setting status to \"{0}\" results in isInitialized={1}")
    @MethodSource
    void isInitialized_setStatusAndVerifyInitialized(APISession.Status status, boolean expected) {
        APISession session = new APISession(CONTRACT_APIKEY);
        session.setStatus(status);
        assertThat(session.isInitialized()).isEqualTo(expected);
    }
}

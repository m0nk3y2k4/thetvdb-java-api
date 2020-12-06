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

package com.github.m0nk3y2k4.thetvdb.internal.api.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.api.Proxy;
import com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APIPreconditionException;
import com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod;
import com.github.m0nk3y2k4.thetvdb.testutils.assertj.TestTheTVDBAPICallAssert;
import com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter.WithHttpsMockServer;
import com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestTheTVDBAPICall;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;

@WithHttpsMockServer
class TheTVDBApiImplTest {

    private static HttpRequest verify(String path, HttpRequestMethod method) {
        return HttpRequest.request(path).withMethod(method.getName());
    }

    private static TheTVDBApi init(TheTVDBApi api) throws APIException {
        api.init("Header.Payload.Signature");
        return api;
    }

    @Test
    void createNewApi_withValidParameters_verifyNoExceptionIsThrown(Proxy remoteApi) {
        assertThatCode(() -> new TheTVDBApiImpl("W7T8IU7E5R7Z5F5")).doesNotThrowAnyException();
        assertThatCode(() -> new TheTVDBApiImpl("OPRIT75Z5EJE4W6", remoteApi)).doesNotThrowAnyException();
        assertThatCode(() -> new TheTVDBApiImpl("POW87F2S1G5J1S5", "unique_4257844", "Prince Valium"))
                .doesNotThrowAnyException();
        assertThatCode(() -> new TheTVDBApiImpl("QP2I456E1Z4OI3T", "unique_5847356", "Princess Vespa", remoteApi))
                .doesNotThrowAnyException();
    }

    @Nested
    @TestInstance(PER_CLASS)
    @DisplayName("Tests for the basic TheTVDBApi layout")
    class TheTVDBApiTest {

        private TheTVDBApi basicAPI;
        private TheTVDBApi userAuthApi;

        @BeforeAll
        void setUpAPIs(Proxy remoteAPI) throws Exception {
            basicAPI = init(new TheTVDBApiImpl("ZW7Z6L5GZ76545S", remoteAPI));
            userAuthApi = init(new TheTVDBApiImpl("47R8A5F8IU7RE6", "unique_65488745", "Lone Starr", remoteAPI));
        }

        //@DisableFormatting
        @BeforeAll
        void setUpRoutes(MockServerClient client) throws Exception {
            // ToDo: Setup some API routes on MockServer
        }

        private Stream<Arguments> withInvalidParameters() {
            return Stream.of(
                // ToDo: Create and return test arguments with invalid parameters
            );
        }

        private Stream<Arguments> withValidParameters() {
            return Stream.of(
                // ToDo: Create and return test arguments with valid parameters
            );
        }
        //@EnableFormatting

        @Disabled("New APIv4 implementation is still pending")
        @ParameterizedTest(name = "[{index}] Route TheTVDBApi.{0} rejected")
        @MethodSource("withInvalidParameters")
        <T> void invokeRoute_withInvalidParametersOrState_verifyParameterValidationAndPreconditionChecks(
                TestTheTVDBAPICall<T> route) {
            assertThat(catchThrowable(route::invoke)).isNotNull()
                    .isInstanceOfAny(IllegalArgumentException.class, APIPreconditionException.class);
        }

        @Disabled("New APIv4 implementation is still pending")
        @ParameterizedTest(name = "[{index}] Route TheTVDBApi.{0} successfully invoked")
        @MethodSource("withValidParameters")
        <T> void invokeRoute_withValidParameters_verifyResponse(TestTheTVDBAPICall<T> route, Object expected,
                MockServerClient client) throws Exception {
            TestTheTVDBAPICallAssert.assertThat(route).usingMockServer(client).matchesExpectation(expected);
        }

        @Test
        void init_withValidToken_verifyTokenSetOnSession(Proxy remoteAPI) throws Exception {
            final String token = "D78W4F5W.8F7WG4F.A69J7E";
            TheTVDBApi api = new TheTVDBApiImpl("ZWUD785K5H7F", remoteAPI);
            api.init(token);
            assertThat(api.getToken()).isNotEmpty().contains(token);
        }

        @Test
        void setLanguage_withValidLanguage_verifyLanguageIsSetInApiRequests(MockServerClient client, Proxy remoteAPI)
                throws Exception {
            // ToDo: Re-enable after APIv4 implementation is finished
//            final String language = "es";
//            TheTVDBApi api = init(new TheTVDBApiImpl("65SOWU45S4FAA", remoteAPI));
//            api.setLanguage(language);
//            api.getEpisodes(69845);
//            client.verify(HttpRequest.request("/series/69845/episodes")
//                    .withHeader(HttpHeaders.ACCEPT_LANGUAGE, language));
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    @DisplayName("Tests for the API's JSON layout")
    class JSONApiTest {

        private TheTVDBApi.JSON basicAPI;
        private TheTVDBApi.JSON userAuthApi;

        @BeforeAll
        void setUpAPIs(Proxy remoteAPI) throws Exception {
            basicAPI = init(new TheTVDBApiImpl("OIRE71D2V145FS", remoteAPI)).json();
            userAuthApi = init(new TheTVDBApiImpl("AAD66G72S3R74F", "unique_9878424", "Dark Helmet", remoteAPI)).json();
        }

        //@DisableFormatting
        @BeforeAll
        void setUpRoutes(MockServerClient client) throws Exception {
            // ToDo: Setup some API routes on MockServer
        }

        private Stream<Arguments> withInvalidParameters() {
            return Stream.of(
                // ToDo: Create and return test arguments with invalid parameters
            );
        }

        private Stream<Arguments> withValidParameters() {
            return Stream.of(
                // ToDo: Create and return test arguments with valid parameters
            );
        }
        //@EnableFormatting

        @Disabled("New APIv4 implementation is still pending")
        @ParameterizedTest(name = "[{index}] Route TheTVDBApi.{0} rejected")
        @MethodSource("withInvalidParameters")
        <T> void invokeRoute_withInvalidParametersOrState_verifyParameterValidationAndPreconditionChecks(
                TestTheTVDBAPICall<T> route) {
            assertThat(catchThrowable(route::invoke)).isNotNull()
                    .isInstanceOfAny(IllegalArgumentException.class, APIPreconditionException.class);
        }

        @Disabled("New APIv4 implementation is still pending")
        @ParameterizedTest(name = "[{index}] Route TheTVDBApi.{0} successfully invoked")
        @MethodSource("withValidParameters")
        <T> void invokeRoute_withValidParameters_verifyResponse(TestTheTVDBAPICall<T> route, Object expected)
                throws Exception {
            TestTheTVDBAPICallAssert.assertThat(route).matchesExpectation(expected);
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    @DisplayName("Tests for the API's Extended layout")
    class ExtendedApiTest {

        private TheTVDBApi.Extended basicAPI;
        private TheTVDBApi.Extended userAuthApi;

        @BeforeAll
        void setUpAPIs(Proxy remoteAPI) throws Exception {
            basicAPI = init(new TheTVDBApiImpl("IOE45S4R82S5", remoteAPI)).extended();
            userAuthApi = init(new TheTVDBApiImpl("D69L8F4N5X4W6R", "unique_5481157", "King Roland", remoteAPI))
                    .extended();
        }

        //@DisableFormatting
        @BeforeAll
        void setUpRoutes(MockServerClient client) throws Exception {
            // ToDo: Setup some API routes on MockServer
        }

        private Stream<Arguments> withInvalidParameters() {
            return Stream.of(
                // ToDo: Create and return test arguments with invalid parameters
            );
        }

        private Stream<Arguments> withValidParameters() {
            return Stream.of(
                // ToDo: Create and return test arguments with valid parameters
            );
        }
        //@EnableFormatting

        @Disabled("New APIv4 implementation is still pending")
        @ParameterizedTest(name = "[{index}] Route TheTVDBApi.{0} rejected")
        @MethodSource("withInvalidParameters")
        <T> void invokeRoute_withInvalidParametersOrState_verifyParameterValidationAndPreconditionChecks(
                TestTheTVDBAPICall<T> route) {
            assertThat(catchThrowable(route::invoke)).isNotNull()
                    .isInstanceOfAny(IllegalArgumentException.class, APIPreconditionException.class);
        }

        @Disabled("New APIv4 implementation is still pending")
        @ParameterizedTest(name = "[{index}] Route TheTVDBApi.{0} successfully invoked")
        @MethodSource("withValidParameters")
        <T> void invokeRoute_withValidParameters_verifyResponse(TestTheTVDBAPICall<T> route, Object expected,
                MockServerClient client) throws Exception {
            TestTheTVDBAPICallAssert.assertThat(route).usingMockServer(client).matchesExpectation(expected);
        }
    }
}

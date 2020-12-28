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

import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.POST;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.SUBSCRIPTION_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.params;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.ARTWORK;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.ARTWORKTYPE_LIST;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.ARTWORK_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.CHARACTER;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.EPISODE;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.GENRE;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.GENRE_LIST;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.MOVIE;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.PEOPLE;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SEASON;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIES;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIES_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIES_LIST;
import static com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestTheTVDBAPICall.route;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockserver.model.Parameter.param;

import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.api.Proxy;
import com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APIPreconditionException;
import com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders;
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
        assertThatCode(() -> new TheTVDBApiImpl(CONTRACT_APIKEY)).doesNotThrowAnyException();
        assertThatCode(() -> new TheTVDBApiImpl(CONTRACT_APIKEY, remoteApi)).doesNotThrowAnyException();
    }

    @Nested
    @TestInstance(PER_CLASS)
    @DisplayName("Tests for the basic TheTVDBApi layout")
    class TheTVDBApiTest {

        private TheTVDBApi basicAPI;
        private TheTVDBApi userAuthApi;

        @BeforeAll
        void setUpAPIs(Proxy remoteAPI) throws Exception {
            basicAPI = init(new TheTVDBApiImpl(CONTRACT_APIKEY, remoteAPI));
            userAuthApi = init(new TheTVDBApiImpl(SUBSCRIPTION_APIKEY, remoteAPI));
        }

        //@DisableFormatting
        @BeforeAll
        void setUpRoutes(MockServerClient client) throws Exception {
            client.when(request("/artwork-types", GET)).respond(jsonResponse(ARTWORKTYPE_LIST));
            client.when(request("/artwork/3447", GET)).respond(jsonResponse(ARTWORK));
            client.when(request("/artwork/9403/extended", GET)).respond(jsonResponse(ARTWORK_DETAILS));
            client.when(request("/characters/604784", GET)).respond(jsonResponse(CHARACTER));
            client.when(request("/episodes/141007", GET)).respond(jsonResponse(EPISODE));
            client.when(request("/genres", GET)).respond(jsonResponse(GENRE_LIST));
            client.when(request("/genres/47", GET)).respond(jsonResponse(GENRE));
            client.when(request("/movies/54394", GET)).respond(jsonResponse(MOVIE));
            client.when(request("/people/431071", GET)).respond(jsonResponse(PEOPLE));
            client.when(request("/seasons/34167", GET)).respond(jsonResponse(SEASON));
            client.when(request("/series", GET)).respond(jsonResponse(SERIES_LIST));
            client.when(request("/series", GET, param("value", "QuerySeries"))).respond(jsonResponse(SERIES_LIST));
            client.when(request("/series/2845", GET)).respond(jsonResponse(SERIES));
            client.when(request("/series/9041/extended", GET)).respond(jsonResponse(SERIES_DETAILS));
        }

        private Stream<Arguments> withInvalidParameters() {
            return Stream.of(
                // ToDo: Create and return test arguments with invalid parameters
            );
        }

        private Stream<Arguments> withValidParameters() {
            return Stream.of(
                    of(route(() -> basicAPI.init(), "init()"), verify("/login", POST)),
                    of(route(() -> basicAPI.login(), "login()"), verify("/login", POST)),
                    of(route(() -> basicAPI.getAllArtworkTypes(), "getAllArtworkTypes()"), ARTWORKTYPE_LIST),
                    of(route(() -> basicAPI.getArtwork(3447), "getArtwork()"), ARTWORK),
                    of(route(() -> basicAPI.getArtworkDetails(9403), "getArtworkDetails()"), ARTWORK_DETAILS),
                    of(route(() -> basicAPI.getCharacter(604784), "getCharacter()"), CHARACTER),
                    of(route(() -> basicAPI.getEpisode(141007), "getEpisode()"), EPISODE),
                    of(route(() -> basicAPI.getAllGenres(), "getAllGenres()"), GENRE_LIST),
                    of(route(() -> basicAPI.getGenre(47), "getGenre()"), GENRE),
                    of(route(() -> basicAPI.getMovie(54394), "getMovie()"), MOVIE),
                    of(route(() -> basicAPI.getPeople(431071), "getPeople()"), PEOPLE),
                    of(route(() -> basicAPI.getSeason(34167), "getSeason()"), SEASON),
                    of(route(() -> basicAPI.getAllSeries(null), "getAllSeries() without query parameters"), SERIES_LIST),
                    of(route(() -> basicAPI.getAllSeries(params("value", "QuerySeries")), "getAllSeries() with query parameters"), SERIES_LIST),
                    of(route(() -> basicAPI.getSeries(2845), "getSeries()"), SERIES),
                    of(route(() -> basicAPI.getSeriesDetails(9041), "getSeriesDetails()"), SERIES_DETAILS)
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

        @ParameterizedTest(name = "[{index}] Route TheTVDBApi.{0} successfully invoked")
        @MethodSource("withValidParameters")
        <T> void invokeRoute_withValidParameters_verifyResponse(TestTheTVDBAPICall<T> route, Object expected,
                MockServerClient client) throws Exception {
            TestTheTVDBAPICallAssert.assertThat(route).usingMockServer(client).matchesExpectation(expected);
        }

        @Test
        void init_withValidToken_verifyTokenSetOnSession(Proxy remoteAPI) throws Exception {
            final String token = "D78W4F5W.8F7WG4F.A69J7E";
            TheTVDBApi api = new TheTVDBApiImpl(CONTRACT_APIKEY, remoteAPI);
            api.init(token);
            assertThat(api.getToken()).isNotEmpty().contains(token);
        }

        @Test
        void setLanguage_withValidLanguage_verifyLanguageIsSetInApiRequests(MockServerClient client, Proxy remoteAPI)
                throws Exception {
            final String language = "es";
            TheTVDBApi api = init(new TheTVDBApiImpl(CONTRACT_APIKEY, remoteAPI));
            api.setLanguage(language);
            api.getArtwork(3447);
            client.verify(HttpRequest.request("/artwork/3447")
                    .withHeader(HttpHeaders.ACCEPT_LANGUAGE, language));
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
            basicAPI = init(new TheTVDBApiImpl(CONTRACT_APIKEY, remoteAPI)).json();
            userAuthApi = init(new TheTVDBApiImpl(SUBSCRIPTION_APIKEY, remoteAPI)).json();
        }

        //@DisableFormatting
        @BeforeAll
        void setUpRoutes(MockServerClient client) throws Exception {
            client.when(request("/artwork-types", GET)).respond(jsonResponse(ARTWORKTYPE_LIST));
            client.when(request("/artwork/6701", GET)).respond(jsonResponse(ARTWORK));
            client.when(request("/artwork/9100/extended", GET)).respond(jsonResponse(ARTWORK_DETAILS));
            client.when(request("/characters/94347", GET)).respond(jsonResponse(CHARACTER));
            client.when(request("/episodes/640796", GET)).respond(jsonResponse(EPISODE));
            client.when(request("/genres", GET)).respond(jsonResponse(GENRE_LIST));
            client.when(request("/genres/21", GET)).respond(jsonResponse(GENRE));
            client.when(request("/movies/61714", GET)).respond(jsonResponse(MOVIE));
            client.when(request("/people/3647", GET)).respond(jsonResponse(PEOPLE));
            client.when(request("/seasons/18322", GET)).respond(jsonResponse(SEASON));
            client.when(request("/series", GET)).respond(jsonResponse(SERIES_LIST));
            client.when(request("/series", GET, param("value", "QuerySeriesJson"))).respond(jsonResponse(SERIES_LIST));
            client.when(request("/series/5003", GET)).respond(jsonResponse(SERIES));
            client.when(request("/series/5842/extended", GET)).respond(jsonResponse(SERIES_DETAILS));
        }

        private Stream<Arguments> withInvalidParameters() {
            return Stream.of(
                // ToDo: Create and return test arguments with invalid parameters
            );
        }

        private Stream<Arguments> withValidParameters() {
            return Stream.of(
                    of(route(() -> basicAPI.getAllArtworkTypes(), "getAllArtworkTypes()"), ARTWORKTYPE_LIST),
                    of(route(() -> basicAPI.getArtwork(6701), "getArtwork()"), ARTWORK),
                    of(route(() -> basicAPI.getArtworkDetails(9100), "getArtworkDetails()"), ARTWORK_DETAILS),
                    of(route(() -> basicAPI.getCharacter(94347), "getCharacter()"), CHARACTER),
                    of(route(() -> basicAPI.getEpisode(640796), "getEpisode()"), EPISODE),
                    of(route(() -> basicAPI.getAllGenres(), "getAllGenres()"), GENRE_LIST),
                    of(route(() -> basicAPI.getGenre(21), "getGenre()"), GENRE),
                    of(route(() -> basicAPI.getMovie(61714), "getMovie()"), MOVIE),
                    of(route(() -> basicAPI.getPeople(3647), "getPeople()"), PEOPLE),
                    of(route(() -> basicAPI.getSeason(18322), "getSeason()"), SEASON),
                    of(route(() -> basicAPI.getAllSeries(null), "getAllSeries() without query parameters"), SERIES_LIST),
                    of(route(() -> basicAPI.getAllSeries(params("value", "QuerySeriesJson")), "getAllSeries() with query parameters"), SERIES_LIST),
                    of(route(() -> basicAPI.getSeries(5003), "getSeries()"), SERIES),
                    of(route(() -> basicAPI.getSeriesDetails(5842), "getSeriesDetails()"), SERIES_DETAILS)
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
            basicAPI = init(new TheTVDBApiImpl(CONTRACT_APIKEY, remoteAPI)).extended();
            userAuthApi = init(new TheTVDBApiImpl(SUBSCRIPTION_APIKEY, remoteAPI))
                    .extended();
        }

        //@DisableFormatting
        @BeforeAll
        void setUpRoutes(MockServerClient client) throws Exception {
            client.when(request("/artwork-types", GET)).respond(jsonResponse(ARTWORKTYPE_LIST));
            client.when(request("/artwork/7099", GET)).respond(jsonResponse(ARTWORK));
            client.when(request("/artwork/6471/extended", GET)).respond(jsonResponse(ARTWORK_DETAILS));
            client.when(request("/characters/66470", GET)).respond(jsonResponse(CHARACTER));
            client.when(request("/episodes/30619", GET)).respond(jsonResponse(EPISODE));
            client.when(request("/genres", GET)).respond(jsonResponse(GENRE_LIST));
            client.when(request("/genres/35", GET)).respond(jsonResponse(GENRE));
            client.when(request("/movies/90034", GET)).respond(jsonResponse(MOVIE));
            client.when(request("/people/9891", GET)).respond(jsonResponse(PEOPLE));
            client.when(request("/seasons/52270", GET)).respond(jsonResponse(SEASON));
            client.when(request("/series", GET)).respond(jsonResponse(SERIES_LIST));
            client.when(request("/series", GET, param("value", "QuerySeriesExtended"))).respond(jsonResponse(SERIES_LIST));
            client.when(request("/series/8131", GET)).respond(jsonResponse(SERIES));
            client.when(request("/series/5444/extended", GET)).respond(jsonResponse(SERIES_DETAILS));
        }

        private Stream<Arguments> withInvalidParameters() {
            return Stream.of(
                // ToDo: Create and return test arguments with invalid parameters
            );
        }

        private Stream<Arguments> withValidParameters() {
            return Stream.of(
                    of(route(() -> basicAPI.getAllArtworkTypes(), "getAllArtworkTypes()"), ARTWORKTYPE_LIST),
                    of(route(() -> basicAPI.getArtwork(7099), "getArtwork()"), ARTWORK),
                    of(route(() -> basicAPI.getArtworkDetails(6471), "getArtworkDetails()"), ARTWORK_DETAILS),
                    of(route(() -> basicAPI.getCharacter(66470), "getCharacter()"), CHARACTER),
                    of(route(() -> basicAPI.getEpisode(30619), "getEpisode()"), EPISODE),
                    of(route(() -> basicAPI.getAllGenres(), "getAllGenres()"), GENRE_LIST),
                    of(route(() -> basicAPI.getGenre(35), "getGenre()"), GENRE),
                    of(route(() -> basicAPI.getMovie(90034), "getMovie()"), MOVIE),
                    of(route(() -> basicAPI.getPeople(9891), "getPeople()"), PEOPLE),
                    of(route(() -> basicAPI.getSeason(52270), "getSeason()"), SEASON),
                    of(route(() -> basicAPI.getAllSeries(null), "getAllSeries() without query parameters"), SERIES_LIST),
                    of(route(() -> basicAPI.getAllSeries(params("value", "QuerySeriesExtended")), "getAllSeries() with query parameters"), SERIES_LIST),
                    of(route(() -> basicAPI.getSeries(8131), "getSeries()"), SERIES),
                    of(route(() -> basicAPI.getSeriesDetails(5444), "getSeriesDetails()"), SERIES_DETAILS)
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

        @ParameterizedTest(name = "[{index}] Route TheTVDBApi.{0} successfully invoked")
        @MethodSource("withValidParameters")
        <T> void invokeRoute_withValidParameters_verifyResponse(TestTheTVDBAPICall<T> route, Object expected,
                MockServerClient client) throws Exception {
            TestTheTVDBAPICallAssert.assertThat(route).usingMockServer(client).matchesExpectation(expected);
        }
    }
}

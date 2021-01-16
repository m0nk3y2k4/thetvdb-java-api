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
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.COMPANY;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.COMPANYTYPE_LIST;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.COMPANY_LIST;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.ENTITYTYPE_LIST;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.EPISODE;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.EPISODE_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.GENRE;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.GENRE_LIST;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.MOVIE;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.PEOPLE;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SEASON;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIES;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIES_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIES_LIST;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.TRANSLATION;
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
import com.github.m0nk3y2k4.thetvdb.api.constants.Query.Companies;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series;
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
            client.when(request("/companies", GET)).respond(jsonResponse(COMPANY_LIST));
            client.when(request("/companies", GET, param("value", "QueryCompanies"))).respond(jsonResponse(COMPANY_LIST));
            client.when(request("/companies", GET, param(Companies.PAGE, "1"))).respond(jsonResponse(COMPANY_LIST));
            client.when(request("/companies/241", GET)).respond(jsonResponse(COMPANY));
            client.when(request("/company-types", GET)).respond(jsonResponse(COMPANYTYPE_LIST));
            client.when(request("/entity-types", GET)).respond(jsonResponse(ENTITYTYPE_LIST));
            client.when(request("/episodes/141007", GET)).respond(jsonResponse(EPISODE));
            client.when(request("/episodes/37017/extended", GET)).respond(jsonResponse(EPISODE_DETAILS));
            client.when(request("/episodes/35744/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/genres", GET)).respond(jsonResponse(GENRE_LIST));
            client.when(request("/genres/47", GET)).respond(jsonResponse(GENRE));
            client.when(request("/movies/54394", GET)).respond(jsonResponse(MOVIE));
            client.when(request("/movies/69745/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/people/431071", GET)).respond(jsonResponse(PEOPLE));
            client.when(request("/seasons/34167", GET)).respond(jsonResponse(SEASON));
            client.when(request("/seasons/27478/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/series", GET)).respond(jsonResponse(SERIES_LIST));
            client.when(request("/series", GET, param("value", "QuerySeries"))).respond(jsonResponse(SERIES_LIST));
            client.when(request("/series", GET, param(Series.PAGE, "2"))).respond(jsonResponse(SERIES_LIST));
            client.when(request("/series/2845", GET)).respond(jsonResponse(SERIES));
            client.when(request("/series/9041/extended", GET)).respond(jsonResponse(SERIES_DETAILS));
            client.when(request("/series/6004/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
        }

        private Stream<Arguments> withInvalidParameters() {
            return Stream.of(
                    of(route(() -> basicAPI.getAllCompanies(-1), "getAllCompanies() with negative page parameter")),
                    of(route(() -> basicAPI.getAllSeries(-3), "getAllSeries() with negative page parameter"))
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
                    of(route(() -> basicAPI.getAllCompanies(null), "getAllCompanies() without query parameters"), COMPANY_LIST),
                    of(route(() -> basicAPI.getAllCompanies(params("value", "QueryCompanies")), "getAllCompanies() with query parameters"), COMPANY_LIST),
                    of(route(() -> basicAPI.getAllCompanies(1), "getAllCompanies() with page"), COMPANY_LIST),
                    of(route(() -> basicAPI.getCompany(241), "getCompany()"), COMPANY),
                    of(route(() -> basicAPI.getAllCompanyTypes(), "getAllCompanyTypes()"), COMPANYTYPE_LIST),
                    of(route(() -> basicAPI.getAllEntityTypes(), "getAllEntityTypes()"), ENTITYTYPE_LIST),
                    of(route(() -> basicAPI.getEpisode(141007), "getEpisode()"), EPISODE),
                    of(route(() -> basicAPI.getEpisodeDetails(37017), "getEpisodeDetails()"), EPISODE_DETAILS),
                    of(route(() -> basicAPI.getEpisodeTranslation(35744, "eng"), "getEpisodeTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllGenres(), "getAllGenres()"), GENRE_LIST),
                    of(route(() -> basicAPI.getGenre(47), "getGenre()"), GENRE),
                    of(route(() -> basicAPI.getMovie(54394), "getMovie()"), MOVIE),
                    of(route(() -> basicAPI.getMovieTranslation(69745, "eng"), "getMovieTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getPeople(431071), "getPeople()"), PEOPLE),
                    of(route(() -> basicAPI.getSeason(34167), "getSeason()"), SEASON),
                    of(route(() -> basicAPI.getSeasonTranslation(27478, "eng"), "getSeasonTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllSeries(null), "getAllSeries() without query parameters"), SERIES_LIST),
                    of(route(() -> basicAPI.getAllSeries(params("value", "QuerySeries")), "getAllSeries() with query parameters"), SERIES_LIST),
                    of(route(() -> basicAPI.getAllSeries(2), "getAllSeries() with page"), SERIES_LIST),
                    of(route(() -> basicAPI.getSeries(2845), "getSeries()"), SERIES),
                    of(route(() -> basicAPI.getSeriesDetails(9041), "getSeriesDetails()"), SERIES_DETAILS),
                    of(route(() -> basicAPI.getSeriesTranslation(6004, "eng"), "getSeriesTranslation()"), TRANSLATION)
            );
        }
        //@EnableFormatting

        @ParameterizedTest(name = "[{index}] Route TheTVDBApi.{0} rejected")
        @MethodSource("withInvalidParameters")
        <T> void invokeRoute_withInvalidParametersOrState_verifyParameterValidationAndPreconditionChecks(
                TestTheTVDBAPICall<T> route) {
            assertThat(catchThrowable(route::invoke))
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
            assertThat(api.getToken()).contains(token);
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
            client.when(request("/companies", GET)).respond(jsonResponse(COMPANY_LIST));
            client.when(request("/companies", GET, param("value", "QueryCompaniesJson"))).respond(jsonResponse(COMPANY_LIST));
            client.when(request("/companies/117", GET)).respond(jsonResponse(COMPANY));
            client.when(request("/company-types", GET)).respond(jsonResponse(COMPANYTYPE_LIST));
            client.when(request("/entity-types", GET)).respond(jsonResponse(ENTITYTYPE_LIST));
            client.when(request("/episodes/640796", GET)).respond(jsonResponse(EPISODE));
            client.when(request("/episodes/872404/extended", GET)).respond(jsonResponse(EPISODE_DETAILS));
            client.when(request("/episodes/379461/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/genres", GET)).respond(jsonResponse(GENRE_LIST));
            client.when(request("/genres/21", GET)).respond(jsonResponse(GENRE));
            client.when(request("/movies/61714", GET)).respond(jsonResponse(MOVIE));
            client.when(request("/movies/74810/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/people/3647", GET)).respond(jsonResponse(PEOPLE));
            client.when(request("/seasons/18322", GET)).respond(jsonResponse(SEASON));
            client.when(request("/seasons/67446/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/series", GET)).respond(jsonResponse(SERIES_LIST));
            client.when(request("/series", GET, param("value", "QuerySeriesJson"))).respond(jsonResponse(SERIES_LIST));
            client.when(request("/series/5003", GET)).respond(jsonResponse(SERIES));
            client.when(request("/series/5842/extended", GET)).respond(jsonResponse(SERIES_DETAILS));
            client.when(request("/series/8024/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
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
                    of(route(() -> basicAPI.getAllCompanies(null), "getAllCompanies() without query parameters"), COMPANY_LIST),
                    of(route(() -> basicAPI.getAllCompanies(params("value", "QueryCompaniesJson")), "getAllCompanies() with query parameters"), COMPANY_LIST),
                    of(route(() -> basicAPI.getCompany(117), "getCompany()"), COMPANY),
                    of(route(() -> basicAPI.getAllCompanyTypes(), "getAllCompanyTypes()"), COMPANYTYPE_LIST),
                    of(route(() -> basicAPI.getAllEntityTypes(), "getAllEntityTypes()"), ENTITYTYPE_LIST),
                    of(route(() -> basicAPI.getEpisode(640796), "getEpisode()"), EPISODE),
                    of(route(() -> basicAPI.getEpisodeDetails(872404), "getEpisodeDetails()"), EPISODE_DETAILS),
                    of(route(() -> basicAPI.getEpisodeTranslation(379461, "eng"), "getEpisodeTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllGenres(), "getAllGenres()"), GENRE_LIST),
                    of(route(() -> basicAPI.getGenre(21), "getGenre()"), GENRE),
                    of(route(() -> basicAPI.getMovie(61714), "getMovie()"), MOVIE),
                    of(route(() -> basicAPI.getMovieTranslation(74810, "eng"), "getMovieTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getPeople(3647), "getPeople()"), PEOPLE),
                    of(route(() -> basicAPI.getSeason(18322), "getSeason()"), SEASON),
                    of(route(() -> basicAPI.getSeasonTranslation(67446, "eng"), "getSeasonTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllSeries(null), "getAllSeries() without query parameters"), SERIES_LIST),
                    of(route(() -> basicAPI.getAllSeries(params("value", "QuerySeriesJson")), "getAllSeries() with query parameters"), SERIES_LIST),
                    of(route(() -> basicAPI.getSeries(5003), "getSeries()"), SERIES),
                    of(route(() -> basicAPI.getSeriesDetails(5842), "getSeriesDetails()"), SERIES_DETAILS),
                    of(route(() -> basicAPI.getSeriesTranslation(8024, "eng"), "getSeriesTranslation()"), TRANSLATION)
            );
        }
        //@EnableFormatting

        @Disabled("New APIv4 implementation is still pending")
        @ParameterizedTest(name = "[{index}] Route TheTVDBApi.{0} rejected")
        @MethodSource("withInvalidParameters")
        <T> void invokeRoute_withInvalidParametersOrState_verifyParameterValidationAndPreconditionChecks(
                TestTheTVDBAPICall<T> route) {
            assertThat(catchThrowable(route::invoke))
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
            client.when(request("/companies", GET)).respond(jsonResponse(COMPANY_LIST));
            client.when(request("/companies", GET, param("value", "QueryCompaniesExtended"))).respond(jsonResponse(COMPANY_LIST));
            client.when(request("/companies/64", GET)).respond(jsonResponse(COMPANY));
            client.when(request("/company-types", GET)).respond(jsonResponse(COMPANYTYPE_LIST));
            client.when(request("/entity-types", GET)).respond(jsonResponse(ENTITYTYPE_LIST));
            client.when(request("/episodes/30619", GET)).respond(jsonResponse(EPISODE));
            client.when(request("/episodes/47149/extended", GET)).respond(jsonResponse(EPISODE_DETAILS));
            client.when(request("/episodes/34771/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/genres", GET)).respond(jsonResponse(GENRE_LIST));
            client.when(request("/genres/35", GET)).respond(jsonResponse(GENRE));
            client.when(request("/movies/90034", GET)).respond(jsonResponse(MOVIE));
            client.when(request("/movies/46011/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/people/9891", GET)).respond(jsonResponse(PEOPLE));
            client.when(request("/seasons/52270", GET)).respond(jsonResponse(SEASON));
            client.when(request("/seasons/64714/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/series", GET)).respond(jsonResponse(SERIES_LIST));
            client.when(request("/series", GET, param("value", "QuerySeriesExtended"))).respond(jsonResponse(SERIES_LIST));
            client.when(request("/series/8131", GET)).respond(jsonResponse(SERIES));
            client.when(request("/series/5444/extended", GET)).respond(jsonResponse(SERIES_DETAILS));
            client.when(request("/series/6170/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
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
                    of(route(() -> basicAPI.getAllCompanies(null), "getAllCompanies() without query parameters"), COMPANY_LIST),
                    of(route(() -> basicAPI.getAllCompanies(params("value", "QueryCompaniesExtended")), "getAllCompanies() with query parameters"), COMPANY_LIST),
                    of(route(() -> basicAPI.getCompany(64), "getCompany()"), COMPANY),
                    of(route(() -> basicAPI.getAllCompanyTypes(), "getAllCompanyTypes()"), COMPANYTYPE_LIST),
                    of(route(() -> basicAPI.getAllEntityTypes(), "getAllEntityTypes()"), ENTITYTYPE_LIST),
                    of(route(() -> basicAPI.getEpisode(30619), "getEpisode()"), EPISODE),
                    of(route(() -> basicAPI.getEpisodeDetails(47149), "getEpisodeDetails()"), EPISODE_DETAILS),
                    of(route(() -> basicAPI.getEpisodeTranslation(34771, "eng"), "getEpisodeTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllGenres(), "getAllGenres()"), GENRE_LIST),
                    of(route(() -> basicAPI.getGenre(35), "getGenre()"), GENRE),
                    of(route(() -> basicAPI.getMovie(90034), "getMovie()"), MOVIE),
                    of(route(() -> basicAPI.getMovieTranslation(46011, "eng"), "getMovieTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getPeople(9891), "getPeople()"), PEOPLE),
                    of(route(() -> basicAPI.getSeason(52270), "getSeason()"), SEASON),
                    of(route(() -> basicAPI.getSeasonTranslation(64714, "eng"), "getSeasonTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllSeries(null), "getAllSeries() without query parameters"), SERIES_LIST),
                    of(route(() -> basicAPI.getAllSeries(params("value", "QuerySeriesExtended")), "getAllSeries() with query parameters"), SERIES_LIST),
                    of(route(() -> basicAPI.getSeries(8131), "getSeries()"), SERIES),
                    of(route(() -> basicAPI.getSeriesDetails(5444), "getSeriesDetails()"), SERIES_DETAILS),
                    of(route(() -> basicAPI.getSeriesTranslation(6170, "eng"), "getSeriesTranslation()"), TRANSLATION)
            );
        }
        //@EnableFormatting

        @Disabled("New APIv4 implementation is still pending")
        @ParameterizedTest(name = "[{index}] Route TheTVDBApi.{0} rejected")
        @MethodSource("withInvalidParameters")
        <T> void invokeRoute_withInvalidParametersOrState_verifyParameterValidationAndPreconditionChecks(
                TestTheTVDBAPICall<T> route) {
            assertThat(catchThrowable(route::invoke))
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

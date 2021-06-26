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

import static com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesSeasonType.ALTERNATE;
import static com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesSeasonType.DEFAULT;
import static com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesSeasonType.DVD;
import static com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesSeasonType.OFFICIAL;
import static com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesSeasonType.REGIONAL;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.POST;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.SUBSCRIPTION_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.params;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.ARTWORK;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.ARTWORKSTATUS_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.ARTWORKTYPE_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.ARTWORK_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.AWARDCATEGORY;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.AWARDCATEGORY_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.CHARACTER;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.COMPANY;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.COMPANYTYPE_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.COMPANY_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.CONTENTRATING_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.ENTITYTYPE_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.EPISODE;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.EPISODE_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.GENDER_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.GENRE;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.GENRE_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.LIST;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.LIST_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.LIST_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.MOVIE;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.MOVIE_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.MOVIE_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.PEOPLE;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.PEOPLETYPE_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.PEOPLE_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SEASON;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SEASONTYPE_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SEASON_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIES;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIESEPISODES;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIES_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIES_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SOURCETYPE_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.STATUS_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.TRANSLATION;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.TRANSLATIONS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.UPDATE_OVERVIEW;
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
import com.github.m0nk3y2k4.thetvdb.api.constants.Query.Lists;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query.Movies;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates;
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
            client.when(request("/artwork/statuses", GET)).respond(jsonResponse(ARTWORKSTATUS_OVERVIEW));
            client.when(request("/artwork/types", GET)).respond(jsonResponse(ARTWORKTYPE_OVERVIEW));
            client.when(request("/artwork/3447", GET)).respond(jsonResponse(ARTWORK));
            client.when(request("/artwork/9403/extended", GET)).respond(jsonResponse(ARTWORK_DETAILS));
            client.when(request("/awards/categories/830", GET)).respond(jsonResponse(AWARDCATEGORY));
            client.when(request("/awards/categories/574/extended", GET)).respond(jsonResponse(AWARDCATEGORY_DETAILS));
            client.when(request("/characters/604784", GET)).respond(jsonResponse(CHARACTER));
            client.when(request("/companies", GET, param("value", "QueryCompanies"))).respond(jsonResponse(COMPANY_OVERVIEW));
            client.when(request("/companies", GET, param(Companies.PAGE, "1"))).respond(jsonResponse(COMPANY_OVERVIEW));
            client.when(request("/companies/types", GET)).respond(jsonResponse(COMPANYTYPE_OVERVIEW));
            client.when(request("/companies/241", GET)).respond(jsonResponse(COMPANY));
            client.when(request("/content/ratings", GET)).respond(jsonResponse(CONTENTRATING_OVERVIEW));
            client.when(request("/entities/types", GET)).respond(jsonResponse(ENTITYTYPE_OVERVIEW));
            client.when(request("/episodes/141007", GET)).respond(jsonResponse(EPISODE));
            client.when(request("/episodes/37017/extended", GET)).respond(jsonResponse(EPISODE_DETAILS));
            client.when(request("/episodes/35744/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/lists/54047/translations/fra", GET)).respond(jsonResponse(TRANSLATIONS));
            client.when(request("/lists", GET, param("value", "QueryLists"))).respond(jsonResponse(LIST_OVERVIEW));
            client.when(request("/lists", GET, param(Lists.PAGE, "4"))).respond(jsonResponse(LIST_OVERVIEW));
            client.when(request("/lists/6740", GET)).respond(jsonResponse(LIST));
            client.when(request("/lists/7901/extended", GET)).respond(jsonResponse(LIST_DETAILS));
            client.when(request("/genders", GET)).respond(jsonResponse(GENDER_OVERVIEW));
            client.when(request("/genres", GET)).respond(jsonResponse(GENRE_OVERVIEW));
            client.when(request("/genres/47", GET)).respond(jsonResponse(GENRE));
            client.when(request("/movies/statuses", GET)).respond(jsonResponse(STATUS_OVERVIEW));
            client.when(request("/movies", GET, param("value", "QueryMovies"))).respond(jsonResponse(MOVIE_OVERVIEW));
            client.when(request("/movies", GET, param(Movies.PAGE, "3"))).respond(jsonResponse(MOVIE_OVERVIEW));
            client.when(request("/movies/54394", GET)).respond(jsonResponse(MOVIE));
            client.when(request("/movies/46994/extended", GET)).respond(jsonResponse(MOVIE_DETAILS));
            client.when(request("/movies/69745/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/people/types", GET)).respond(jsonResponse(PEOPLETYPE_OVERVIEW));
            client.when(request("/people/431071", GET)).respond(jsonResponse(PEOPLE));
            client.when(request("/people/574101/extended", GET)).respond(jsonResponse(PEOPLE_DETAILS));
            client.when(request("/seasons/34167", GET)).respond(jsonResponse(SEASON));
            client.when(request("/seasons/69761/extended", GET)).respond(jsonResponse(SEASON_DETAILS));
            client.when(request("/seasons/types", GET)).respond(jsonResponse(SEASONTYPE_OVERVIEW));
            client.when(request("/seasons/27478/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/series/statuses", GET)).respond(jsonResponse(STATUS_OVERVIEW));
            client.when(request("/series", GET, param("value", "QuerySeries"))).respond(jsonResponse(SERIES_OVERVIEW));
            client.when(request("/series", GET, param(Series.PAGE, "2"))).respond(jsonResponse(SERIES_OVERVIEW));
            client.when(request("/series/2845", GET)).respond(jsonResponse(SERIES));
            client.when(request("/series/9041/extended", GET)).respond(jsonResponse(SERIES_DETAILS));
            client.when(request("/series/3789/episodes/default", GET)).respond(jsonResponse(SERIESEPISODES));
            client.when(request("/series/7000/episodes/alternate", GET, param("value", "QuerySeriesEpisodes"))).respond(jsonResponse(SERIESEPISODES));
            client.when(request("/series/2147/episodes/regional", GET, param(Series.PAGE, "4"))).respond(jsonResponse(SERIESEPISODES));
            client.when(request("/series/6004/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/sources/types", GET)).respond(jsonResponse(SOURCETYPE_OVERVIEW));
            client.when(request("/updates", GET, param(Updates.SINCE, "16247601"), param("value", "QueryUpdates"))).respond(jsonResponse(UPDATE_OVERVIEW));
            client.when(request("/updates", GET, param(Updates.SINCE, "162365745"))).respond(jsonResponse(UPDATE_OVERVIEW));
        }

        private Stream<Arguments> withInvalidParameters() {
            return Stream.of(
                    of(route(() -> basicAPI.getAllCompanies(-1), "getAllCompanies() with negative page parameter")),
                    of(route(() -> basicAPI.getAllLists(-5), "getAllLists() with negative page parameter")),
                    of(route(() -> basicAPI.getAllMovies(-4), "getAllMovies() with negative page parameter")),
                    of(route(() -> basicAPI.getAllSeries(-3), "getAllSeries() with negative page parameter")),
                    of(route(() -> basicAPI.getSeriesEpisodes(41257, DVD, -6), "getSeriesEpisodes() with negative page parameter"))
            );
        }

        private Stream<Arguments> withValidParameters() {
            return Stream.of(
                    of(route(() -> basicAPI.init(), "init()"), verify("/login", POST)),
                    of(route(() -> basicAPI.login(), "login()"), verify("/login", POST)),
                    of(route(() -> basicAPI.getAllArtworkStatuses(), "getAllArtworkStatuses()"), ARTWORKSTATUS_OVERVIEW),
                    of(route(() -> basicAPI.getAllArtworkTypes(), "getAllArtworkTypes()"), ARTWORKTYPE_OVERVIEW),
                    of(route(() -> basicAPI.getArtwork(3447), "getArtwork()"), ARTWORK),
                    of(route(() -> basicAPI.getArtworkDetails(9403), "getArtworkDetails()"), ARTWORK_DETAILS),
                    of(route(() -> basicAPI.getAwardCategory(830), "getAwardCategory()"), AWARDCATEGORY),
                    of(route(() -> basicAPI.getAwardCategoryDetails(574), "getAwardCategoryDetails()"), AWARDCATEGORY_DETAILS),
                    of(route(() -> basicAPI.getCharacter(604784), "getCharacter()"), CHARACTER),
                    of(route(() -> basicAPI.getAllCompanies(params("value", "QueryCompanies")), "getAllCompanies() with query parameters"), COMPANY_OVERVIEW),
                    of(route(() -> basicAPI.getAllCompanies(1), "getAllCompanies() with page"), COMPANY_OVERVIEW),
                    of(route(() -> basicAPI.getCompany(241), "getCompany()"), COMPANY),
                    of(route(() -> basicAPI.getCompanyTypes(), "getCompanyTypes()"), COMPANYTYPE_OVERVIEW),
                    of(route(() -> basicAPI.getAllContentRatings(), "getAllContentRatings()"), CONTENTRATING_OVERVIEW),
                    of(route(() -> basicAPI.getEntityTypes(), "getEntityTypes()"), ENTITYTYPE_OVERVIEW),
                    of(route(() -> basicAPI.getEpisode(141007), "getEpisode()"), EPISODE),
                    of(route(() -> basicAPI.getEpisodeDetails(37017), "getEpisodeDetails()"), EPISODE_DETAILS),
                    of(route(() -> basicAPI.getEpisodeTranslation(35744, "eng"), "getEpisodeTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getListTranslation(54047, "fra"), "getListTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllLists(params("value", "QueryLists")), "getAllLists() with query parameters"), LIST_OVERVIEW),
                    of(route(() -> basicAPI.getAllLists(4), "getAllLists() with page"), LIST_OVERVIEW),
                    of(route(() -> basicAPI.getList(6740), "getList()"), LIST),
                    of(route(() -> basicAPI.getListDetails(7901), "getListDetails()"), LIST_DETAILS),
                    of(route(() -> basicAPI.getAllGenders(), "getAllGenders()"), GENDER_OVERVIEW),
                    of(route(() -> basicAPI.getAllGenres(), "getAllGenres()"), GENRE_OVERVIEW),
                    of(route(() -> basicAPI.getGenre(47), "getGenre()"), GENRE),
                    of(route(() -> basicAPI.getAllMovieStatuses(), "getAllMovieStatuses()"), STATUS_OVERVIEW),
                    of(route(() -> basicAPI.getAllMovies(params("value", "QueryMovies")), "getAllMovies() with query parameters"), MOVIE_OVERVIEW),
                    of(route(() -> basicAPI.getAllMovies(3), "getAllMovies() with page"), MOVIE_OVERVIEW),
                    of(route(() -> basicAPI.getMovie(54394), "getMovie()"), MOVIE),
                    of(route(() -> basicAPI.getMovieDetails(46994), "getMovieDetails()"), MOVIE_DETAILS),
                    of(route(() -> basicAPI.getMovieTranslation(69745, "eng"), "getMovieTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllPeopleTypes(), "getAllPeopleTypes()"), PEOPLETYPE_OVERVIEW),
                    of(route(() -> basicAPI.getPeople(431071), "getPeople()"), PEOPLE),
                    of(route(() -> basicAPI.getPeopleDetails(574101), "getPeopleDetails()"), PEOPLE_DETAILS),
                    of(route(() -> basicAPI.getSeason(34167), "getSeason()"), SEASON),
                    of(route(() -> basicAPI.getSeasonDetails(69761), "getSeasonDetails()"), SEASON_DETAILS),
                    of(route(() -> basicAPI.getSeasonTypes(), "getSeasonTypes()"), SEASONTYPE_OVERVIEW),
                    of(route(() -> basicAPI.getSeasonTranslation(27478, "eng"), "getSeasonTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllSeriesStatuses(), "getAllSeriesStatuses()"), STATUS_OVERVIEW),
                    of(route(() -> basicAPI.getAllSeries(params("value", "QuerySeries")), "getAllSeries() with query parameters"), SERIES_OVERVIEW),
                    of(route(() -> basicAPI.getAllSeries(2), "getAllSeries() with page"), SERIES_OVERVIEW),
                    of(route(() -> basicAPI.getSeries(2845), "getSeries()"), SERIES),
                    of(route(() -> basicAPI.getSeriesDetails(9041), "getSeriesDetails()"), SERIES_DETAILS),
                    of(route(() -> basicAPI.getSeriesEpisodes(3789, DEFAULT), "getSeriesEpisodes()"), SERIESEPISODES),
                    of(route(() -> basicAPI.getSeriesEpisodes(7000, ALTERNATE, params("value", "QuerySeriesEpisodes")), "getSeriesEpisodes() with query parameters"), SERIESEPISODES),
                    of(route(() -> basicAPI.getSeriesEpisodes(2147, REGIONAL, 4), "getSeriesEpisodes() with page"), SERIESEPISODES),
                    of(route(() -> basicAPI.getSeriesTranslation(6004, "eng"), "getSeriesTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllSourceTypes(), "getAllSourceTypes()"), SOURCETYPE_OVERVIEW),
                    of(route(() -> basicAPI.getUpdates(params(Updates.SINCE, "16247601", "value", "QueryUpdates")), "getUpdates() with query parameters"), UPDATE_OVERVIEW),
                    of(route(() -> basicAPI.getUpdates(162365745), "getUpdates() with Epoch time"), UPDATE_OVERVIEW)
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
            client.when(request("/artwork/statuses", GET)).respond(jsonResponse(ARTWORKSTATUS_OVERVIEW));
            client.when(request("/artwork/types", GET)).respond(jsonResponse(ARTWORKTYPE_OVERVIEW));
            client.when(request("/artwork/6701", GET)).respond(jsonResponse(ARTWORK));
            client.when(request("/artwork/9100/extended", GET)).respond(jsonResponse(ARTWORK_DETAILS));
            client.when(request("/awards/categories/411", GET)).respond(jsonResponse(AWARDCATEGORY));
            client.when(request("/awards/categories/623/extended", GET)).respond(jsonResponse(AWARDCATEGORY_DETAILS));
            client.when(request("/characters/94347", GET)).respond(jsonResponse(CHARACTER));
            client.when(request("/companies", GET, param("value", "QueryCompaniesJson"))).respond(jsonResponse(COMPANY_OVERVIEW));
            client.when(request("/companies/types", GET)).respond(jsonResponse(COMPANYTYPE_OVERVIEW));
            client.when(request("/companies/117", GET)).respond(jsonResponse(COMPANY));
            client.when(request("/content/ratings", GET)).respond(jsonResponse(CONTENTRATING_OVERVIEW));
            client.when(request("/entities/types", GET)).respond(jsonResponse(ENTITYTYPE_OVERVIEW));
            client.when(request("/episodes/640796", GET)).respond(jsonResponse(EPISODE));
            client.when(request("/episodes/872404/extended", GET)).respond(jsonResponse(EPISODE_DETAILS));
            client.when(request("/episodes/379461/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/lists/2400/translations/fra", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/lists", GET, param("value", "QueryListsJson"))).respond(jsonResponse(LIST_OVERVIEW));
            client.when(request("/lists/1151", GET)).respond(jsonResponse(LIST));
            client.when(request("/lists/3463/extended", GET)).respond(jsonResponse(LIST_DETAILS));
            client.when(request("/genders", GET)).respond(jsonResponse(GENDER_OVERVIEW));
            client.when(request("/genres", GET)).respond(jsonResponse(GENRE_OVERVIEW));
            client.when(request("/genres/21", GET)).respond(jsonResponse(GENRE));
            client.when(request("/movies/statuses", GET)).respond(jsonResponse(STATUS_OVERVIEW));
            client.when(request("/movies", GET, param("value", "QueryMoviesJson"))).respond(jsonResponse(MOVIE_OVERVIEW));
            client.when(request("/movies/61714", GET)).respond(jsonResponse(MOVIE));
            client.when(request("/movies/54801/extended", GET)).respond(jsonResponse(MOVIE_DETAILS));
            client.when(request("/movies/74810/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/people/types", GET)).respond(jsonResponse(PEOPLETYPE_OVERVIEW));
            client.when(request("/people/3647", GET)).respond(jsonResponse(PEOPLE));
            client.when(request("/people/6904/extended", GET)).respond(jsonResponse(PEOPLE_DETAILS));
            client.when(request("/seasons/18322", GET)).respond(jsonResponse(SEASON));
            client.when(request("/seasons/48874/extended", GET)).respond(jsonResponse(SEASON_DETAILS));
            client.when(request("/seasons/types", GET)).respond(jsonResponse(SEASONTYPE_OVERVIEW));
            client.when(request("/seasons/67446/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/series/statuses", GET)).respond(jsonResponse(STATUS_OVERVIEW));
            client.when(request("/series", GET, param("value", "QuerySeriesJson"))).respond(jsonResponse(SERIES_OVERVIEW));
            client.when(request("/series/5003", GET)).respond(jsonResponse(SERIES));
            client.when(request("/series/5842/extended", GET)).respond(jsonResponse(SERIES_DETAILS));
            client.when(request("/series/98043/episodes/official", GET, param("value", "QuerySeriesEpisodesJson"))).respond(jsonResponse(SERIESEPISODES));
            client.when(request("/series/8024/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/sources/types", GET)).respond(jsonResponse(SOURCETYPE_OVERVIEW));
            client.when(request("/updates", GET, param(Updates.SINCE, "16258740"), param("value", "QueryUpdatesJson"))).respond(jsonResponse(UPDATE_OVERVIEW));
        }

        private Stream<Arguments> withInvalidParameters() {
            return Stream.of(
                // ToDo: Create and return test arguments with invalid parameters
            );
        }

        private Stream<Arguments> withValidParameters() {
            return Stream.of(
                    of(route(() -> basicAPI.getAllArtworkStatuses(), "getAllArtworkStatuses()"), ARTWORKSTATUS_OVERVIEW),
                    of(route(() -> basicAPI.getAllArtworkTypes(), "getAllArtworkTypes()"), ARTWORKTYPE_OVERVIEW),
                    of(route(() -> basicAPI.getArtwork(6701), "getArtwork()"), ARTWORK),
                    of(route(() -> basicAPI.getArtworkDetails(9100), "getArtworkDetails()"), ARTWORK_DETAILS),
                    of(route(() -> basicAPI.getAwardCategory(411), "getAwardCategory()"), AWARDCATEGORY),
                    of(route(() -> basicAPI.getAwardCategoryDetails(623), "getAwardCategoryDetails()"), AWARDCATEGORY_DETAILS),
                    of(route(() -> basicAPI.getCharacter(94347), "getCharacter()"), CHARACTER),
                    of(route(() -> basicAPI.getAllCompanies(params("value", "QueryCompaniesJson")), "getAllCompanies() with query parameters"), COMPANY_OVERVIEW),
                    of(route(() -> basicAPI.getCompany(117), "getCompany()"), COMPANY),
                    of(route(() -> basicAPI.getCompanyTypes(), "getCompanyTypes()"), COMPANYTYPE_OVERVIEW),
                    of(route(() -> basicAPI.getAllContentRatings(), "getAllContentRatings()"), CONTENTRATING_OVERVIEW),
                    of(route(() -> basicAPI.getEntityTypes(), "getEntityTypes()"), ENTITYTYPE_OVERVIEW),
                    of(route(() -> basicAPI.getEpisode(640796), "getEpisode()"), EPISODE),
                    of(route(() -> basicAPI.getEpisodeDetails(872404), "getEpisodeDetails()"), EPISODE_DETAILS),
                    of(route(() -> basicAPI.getEpisodeTranslation(379461, "eng"), "getEpisodeTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getListTranslation(2400, "fra"), "getListTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllLists(params("value", "QueryListsJson")), "getAllLists() with query parameters"), LIST_OVERVIEW),
                    of(route(() -> basicAPI.getList(1151), "getList()"), LIST),
                    of(route(() -> basicAPI.getListDetails(3463), "getListDetails()"), LIST_DETAILS),
                    of(route(() -> basicAPI.getAllGenders(), "getAllGenders()"), GENDER_OVERVIEW),
                    of(route(() -> basicAPI.getAllGenres(), "getAllGenres()"), GENRE_OVERVIEW),
                    of(route(() -> basicAPI.getGenre(21), "getGenre()"), GENRE),
                    of(route(() -> basicAPI.getAllMovieStatuses(), "getAllMovieStatuses()"), STATUS_OVERVIEW),
                    of(route(() -> basicAPI.getAllMovies(params("value", "QueryMoviesJson")), "getAllMovies() with query parameters"), MOVIE_OVERVIEW),
                    of(route(() -> basicAPI.getMovie(61714), "getMovie()"), MOVIE),
                    of(route(() -> basicAPI.getMovieDetails(54801), "getMovieDetails()"), MOVIE_DETAILS),
                    of(route(() -> basicAPI.getMovieTranslation(74810, "eng"), "getMovieTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllPeopleTypes(), "getAllPeopleTypes()"), PEOPLETYPE_OVERVIEW),
                    of(route(() -> basicAPI.getPeople(3647), "getPeople()"), PEOPLE),
                    of(route(() -> basicAPI.getPeopleDetails(6904), "getPeopleDetails()"), PEOPLE_DETAILS),
                    of(route(() -> basicAPI.getSeason(18322), "getSeason()"), SEASON),
                    of(route(() -> basicAPI.getSeasonDetails(48874), "getSeasonDetails()"), SEASON_DETAILS),
                    of(route(() -> basicAPI.getSeasonTypes(), "getSeasonTypes()"), SEASONTYPE_OVERVIEW),
                    of(route(() -> basicAPI.getSeasonTranslation(67446, "eng"), "getSeasonTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllSeriesStatuses(), "getAllSeriesStatuses()"), STATUS_OVERVIEW),
                    of(route(() -> basicAPI.getAllSeries(params("value", "QuerySeriesJson")), "getAllSeries() with query parameters"), SERIES_OVERVIEW),
                    of(route(() -> basicAPI.getSeries(5003), "getSeries()"), SERIES),
                    of(route(() -> basicAPI.getSeriesDetails(5842), "getSeriesDetails()"), SERIES_DETAILS),
                    of(route(() -> basicAPI.getSeriesEpisodes(98043, OFFICIAL, params("value", "QuerySeriesEpisodesJson")), "getSeriesEpisodes() with query parameters"), SERIESEPISODES),
                    of(route(() -> basicAPI.getSeriesTranslation(8024, "eng"), "getSeriesTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllSourceTypes(), "getAllSourceTypes()"), SOURCETYPE_OVERVIEW),
                    of(route(() -> basicAPI.getUpdates(params(Updates.SINCE, "16258740", "value", "QueryUpdatesJson")), "getUpdates() with query parameters"), UPDATE_OVERVIEW)
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
            client.when(request("/artwork/statuses", GET)).respond(jsonResponse(ARTWORKSTATUS_OVERVIEW));
            client.when(request("/artwork/types", GET)).respond(jsonResponse(ARTWORKTYPE_OVERVIEW));
            client.when(request("/artwork/7099", GET)).respond(jsonResponse(ARTWORK));
            client.when(request("/artwork/6471/extended", GET)).respond(jsonResponse(ARTWORK_DETAILS));
            client.when(request("/awards/categories/355", GET)).respond(jsonResponse(AWARDCATEGORY));
            client.when(request("/awards/categories/495/extended", GET)).respond(jsonResponse(AWARDCATEGORY_DETAILS));
            client.when(request("/characters/66470", GET)).respond(jsonResponse(CHARACTER));
            client.when(request("/companies", GET, param("value", "QueryCompaniesExtended"))).respond(jsonResponse(COMPANY_OVERVIEW));
            client.when(request("/companies/types", GET)).respond(jsonResponse(COMPANYTYPE_OVERVIEW));
            client.when(request("/companies/64", GET)).respond(jsonResponse(COMPANY));
            client.when(request("/content/ratings", GET)).respond(jsonResponse(CONTENTRATING_OVERVIEW));
            client.when(request("/entities/types", GET)).respond(jsonResponse(ENTITYTYPE_OVERVIEW));
            client.when(request("/episodes/30619", GET)).respond(jsonResponse(EPISODE));
            client.when(request("/episodes/47149/extended", GET)).respond(jsonResponse(EPISODE_DETAILS));
            client.when(request("/episodes/34771/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/lists/64114/translations/fra", GET)).respond(jsonResponse(TRANSLATIONS));
            client.when(request("/lists", GET, param("value", "QueryListsExtended"))).respond(jsonResponse(LIST_OVERVIEW));
            client.when(request("/lists/4641", GET)).respond(jsonResponse(LIST));
            client.when(request("/lists/3169/extended", GET)).respond(jsonResponse(LIST_DETAILS));
            client.when(request("/genders", GET)).respond(jsonResponse(GENDER_OVERVIEW));
            client.when(request("/genres", GET)).respond(jsonResponse(GENRE_OVERVIEW));
            client.when(request("/genres/35", GET)).respond(jsonResponse(GENRE));
            client.when(request("/movies/statuses", GET)).respond(jsonResponse(STATUS_OVERVIEW));
            client.when(request("/movies", GET, param("value", "QueryMoviesExtended"))).respond(jsonResponse(MOVIE_OVERVIEW));
            client.when(request("/movies/90034", GET)).respond(jsonResponse(MOVIE));
            client.when(request("/movies/31101/extended", GET)).respond(jsonResponse(MOVIE_DETAILS));
            client.when(request("/movies/46011/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/people/types", GET)).respond(jsonResponse(PEOPLETYPE_OVERVIEW));
            client.when(request("/people/9891", GET)).respond(jsonResponse(PEOPLE));
            client.when(request("/people/1067/extended", GET)).respond(jsonResponse(PEOPLE_DETAILS));
            client.when(request("/seasons/52270", GET)).respond(jsonResponse(SEASON));
            client.when(request("/seasons/69714/extended", GET)).respond(jsonResponse(SEASON_DETAILS));
            client.when(request("/seasons/types", GET)).respond(jsonResponse(SEASONTYPE_OVERVIEW));
            client.when(request("/seasons/64714/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/series/statuses", GET)).respond(jsonResponse(STATUS_OVERVIEW));
            client.when(request("/series", GET, param("value", "QuerySeriesExtended"))).respond(jsonResponse(SERIES_OVERVIEW));
            client.when(request("/series/8131", GET)).respond(jsonResponse(SERIES));
            client.when(request("/series/5444/extended", GET)).respond(jsonResponse(SERIES_DETAILS));
            client.when(request("/series/5711/episodes/dvd", GET, param("value", "QuerySeriesEpisodesExtended"))).respond(jsonResponse(SERIESEPISODES));
            client.when(request("/series/6170/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/sources/types", GET)).respond(jsonResponse(SOURCETYPE_OVERVIEW));
            client.when(request("/updates", GET, param(Updates.SINCE, "16245743"), param("value", "QueryUpdatesExtended"))).respond(jsonResponse(UPDATE_OVERVIEW));
        }

        private Stream<Arguments> withInvalidParameters() {
            return Stream.of(
                // ToDo: Create and return test arguments with invalid parameters
            );
        }

        private Stream<Arguments> withValidParameters() {
            return Stream.of(
                    of(route(() -> basicAPI.getAllArtworkStatuses(), "getAllArtworkStatuses()"), ARTWORKSTATUS_OVERVIEW),
                    of(route(() -> basicAPI.getAllArtworkTypes(), "getAllArtworkTypes()"), ARTWORKTYPE_OVERVIEW),
                    of(route(() -> basicAPI.getArtwork(7099), "getArtwork()"), ARTWORK),
                    of(route(() -> basicAPI.getArtworkDetails(6471), "getArtworkDetails()"), ARTWORK_DETAILS),
                    of(route(() -> basicAPI.getAwardCategory(355), "getAwardCategory()"), AWARDCATEGORY),
                    of(route(() -> basicAPI.getAwardCategoryDetails(495), "getAwardCategoryDetails()"), AWARDCATEGORY_DETAILS),
                    of(route(() -> basicAPI.getCharacter(66470), "getCharacter()"), CHARACTER),
                    of(route(() -> basicAPI.getAllCompanies(params("value", "QueryCompaniesExtended")), "getAllCompanies() with query parameters"), COMPANY_OVERVIEW),
                    of(route(() -> basicAPI.getCompany(64), "getCompany()"), COMPANY),
                    of(route(() -> basicAPI.getCompanyTypes(), "getCompanyTypes()"), COMPANYTYPE_OVERVIEW),
                    of(route(() -> basicAPI.getAllContentRatings(), "getAllContentRatings()"), CONTENTRATING_OVERVIEW),
                    of(route(() -> basicAPI.getEntityTypes(), "getEntityTypes()"), ENTITYTYPE_OVERVIEW),
                    of(route(() -> basicAPI.getEpisode(30619), "getEpisode()"), EPISODE),
                    of(route(() -> basicAPI.getEpisodeDetails(47149), "getEpisodeDetails()"), EPISODE_DETAILS),
                    of(route(() -> basicAPI.getEpisodeTranslation(34771, "eng"), "getEpisodeTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getListTranslation(64114, "fra"), "getListTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllLists(params("value", "QueryListsExtended")), "getAllLists() with query parameters"), LIST_OVERVIEW),
                    of(route(() -> basicAPI.getList(4641), "getList()"), LIST),
                    of(route(() -> basicAPI.getListDetails(3169), "getListDetails()"), LIST_DETAILS),
                    of(route(() -> basicAPI.getAllGenders(), "getAllGenders()"), GENDER_OVERVIEW),
                    of(route(() -> basicAPI.getAllGenres(), "getAllGenres()"), GENRE_OVERVIEW),
                    of(route(() -> basicAPI.getGenre(35), "getGenre()"), GENRE),
                    of(route(() -> basicAPI.getAllMovieStatuses(), "getAllMovieStatuses()"), STATUS_OVERVIEW),
                    of(route(() -> basicAPI.getAllMovies(params("value", "QueryMoviesExtended")), "getAllMovies() with query parameters"), MOVIE_OVERVIEW),
                    of(route(() -> basicAPI.getMovie(90034), "getMovie()"), MOVIE),
                    of(route(() -> basicAPI.getMovieDetails(31101), "getMovieDetails()"), MOVIE_DETAILS),
                    of(route(() -> basicAPI.getMovieTranslation(46011, "eng"), "getMovieTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllPeopleTypes(), "getAllPeopleTypes()"), PEOPLETYPE_OVERVIEW),
                    of(route(() -> basicAPI.getPeople(9891), "getPeople()"), PEOPLE),
                    of(route(() -> basicAPI.getPeopleDetails(1067), "getPeopleDetails()"), PEOPLE_DETAILS),
                    of(route(() -> basicAPI.getSeason(52270), "getSeason()"), SEASON),
                    of(route(() -> basicAPI.getSeasonDetails(69714), "getSeasonDetails()"), SEASON_DETAILS),
                    of(route(() -> basicAPI.getSeasonTypes(), "getSeasonTypes()"), SEASONTYPE_OVERVIEW),
                    of(route(() -> basicAPI.getSeasonTranslation(64714, "eng"), "getSeasonTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllSeriesStatuses(), "getAllSeriesStatuses()"), STATUS_OVERVIEW),
                    of(route(() -> basicAPI.getAllSeries(params("value", "QuerySeriesExtended")), "getAllSeries() with query parameters"), SERIES_OVERVIEW),
                    of(route(() -> basicAPI.getSeries(8131), "getSeries()"), SERIES),
                    of(route(() -> basicAPI.getSeriesDetails(5444), "getSeriesDetails()"), SERIES_DETAILS),
                    of(route(() -> basicAPI.getSeriesEpisodes(5711, DVD, params("value", "QuerySeriesEpisodesExtended")), "getSeriesEpisodes() with query parameters"), SERIESEPISODES),
                    of(route(() -> basicAPI.getSeriesTranslation(6170, "eng"), "getSeriesTranslation()"), TRANSLATION),
                    of(route(() -> basicAPI.getAllSourceTypes(), "getAllSourceTypes()"), SOURCETYPE_OVERVIEW),
                    of(route(() -> basicAPI.getUpdates(params(Updates.SINCE, "16245743", "value", "QueryUpdatesExtended")), "getUpdates() with query parameters"), UPDATE_OVERVIEW)
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

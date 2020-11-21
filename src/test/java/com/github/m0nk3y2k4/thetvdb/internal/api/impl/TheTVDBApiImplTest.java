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

import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Movie.SINCE;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Search.IMDBID;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Search.NAME;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Search.ZAP2ITID;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series.ABSOLUTENUMBER;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series.AIREDEPISODE;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series.AIREDSEASON;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series.KEYS;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series.KEYTYPE;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series.PAGE;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series.RESOLUTION;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series.SUBKEY;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates.FROMTIME;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates.TOTIME;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Users.ITEMTYPE;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.DELETE;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.HEAD;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.POST;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.PUT;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.params;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.getHeadersFrom;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.ACTORS;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.EMPTY;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.EPISODE;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.EPISODES;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.FAVORITES;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.FAVORITES_EMPTY;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.IMAGEQUERYPARAMETERS;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.IMAGES;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.IMAGESUMMARY;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.LANGUAGE;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.LANGUAGES;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.MOVIE;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.MOVIEUPDATES;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.QUERYPARAMETERS;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.QUERYPARAMETERS_NESTED;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.RATINGS;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.SERIES;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.SERIESHEADER;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.SERIESSEARCH;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.SERIESSUMMARY;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.UPDATES;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.USER;
import static com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestTheTVDBAPICall.route;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.Parameter.param;

import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.api.Proxy;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APIPreconditionException;
import com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders;
import com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod;
import com.github.m0nk3y2k4.thetvdb.testutils.assertj.TestTheTVDBAPICallAssert;
import com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter.WithHttpsMockServer;
import com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestTheTVDBAPICall;
import org.junit.jupiter.api.BeforeAll;
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

    @Test
    void createNewApi_withValidParameters_verifyNoExceptionIsThrown(Proxy remoteApi) {
        assertThatCode(() -> new TheTVDBApiImpl("W7T8IU7E5R7Z5F5")).doesNotThrowAnyException();
        assertThatCode(() -> new TheTVDBApiImpl("OPRIT75Z5EJE4W6", remoteApi)).doesNotThrowAnyException();
        assertThatCode(() -> new TheTVDBApiImpl("POW87F2S1G5J1S5", "unique_4257844", "Prince Valium"))
                .doesNotThrowAnyException();
        assertThatCode(() -> new TheTVDBApiImpl("QP2I456E1Z4OI3T", "unique_5847356", "Princess Vespa", remoteApi))
                .doesNotThrowAnyException();
    }

    private HttpRequest verify(String path, HttpRequestMethod method) {
        return HttpRequest.request(path).withMethod(method.getName());
    }

    private TheTVDBApi init(TheTVDBApi api) throws APIException {
        api.init("Header.Payload.Signature");
        return api;
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

        //@formatter:off
        @BeforeAll
        void setUpRoutes(MockServerClient client) throws Exception {
            client.when(request("/episodes/8477", GET)).respond(jsonResponse(EPISODE));
            client.when(request("/languages", GET)).respond(jsonResponse(LANGUAGES));
            client.when(request("/languages/3184", GET)).respond(jsonResponse(LANGUAGE));
            client.when(request("/movies/64874", GET)).respond(jsonResponse(MOVIE));
            client.when(request("/movieupdates", GET, param(SINCE, "77"))).respond(jsonResponse(MOVIEUPDATES));
            client.when(request("/search/series", GET, param("value", "SearchSeries"))).respond(jsonResponse(SERIESSEARCH));
            client.when(request("/search/series", GET, param(NAME, "Name"))).respond(jsonResponse(SERIESSEARCH));
            client.when(request("/search/series", GET, param(IMDBID, "ImdbID"))).respond(jsonResponse(SERIESSEARCH));
            client.when(request("/search/series", GET, param(ZAP2ITID, "Zap2itID"))).respond(jsonResponse(SERIESSEARCH));
            client.when(request("/search/series/params", GET)).respond(jsonResponse(QUERYPARAMETERS));
            client.when(request("/series/97845", GET)).respond(jsonResponse(SERIES));
            client.when(request("/series/63118", HEAD)).respond(response().withHeaders(getHeadersFrom(SERIESHEADER)));
            client.when(request("/series/96587/actors", GET)).respond(jsonResponse(ACTORS));
            client.when(request("/series/54783/episodes", GET)).respond(jsonResponse(EPISODES));
            client.when(request("/series/42684/episodes", GET, param("value", "GetEpisodes"))).respond(jsonResponse(EPISODES));
            client.when(request("/series/69745/episodes", GET, param(PAGE, "4"))).respond(jsonResponse(EPISODES));
            client.when(request("/series/8745/episodes/query", GET)).respond(jsonResponse(EPISODES));
            client.when(request("/series/5748/episodes/query", GET, param("value", "QueryEpisodes"))).respond(jsonResponse(EPISODES));
            client.when(request("/series/6359/episodes/query", GET, param(AIREDSEASON, "2"))).respond(jsonResponse(EPISODES));
            client.when(request("/series/9912/episodes/query", GET, param(AIREDSEASON, "3"), param(PAGE, "2"))).respond(jsonResponse(EPISODES));
            client.when(request("/series/3635/episodes/query", GET, param(AIREDEPISODE, "21"))).respond(jsonResponse(EPISODES));
            client.when(request("/series/5933/episodes/query", GET, param(ABSOLUTENUMBER, "54"))).respond(jsonResponse(EPISODES));
            client.when(request("/series/57874/episodes/query/params", GET)).respond(jsonResponse(QUERYPARAMETERS_NESTED));
            client.when(request("/series/8111/episodes/summary", GET)).respond(jsonResponse(SERIESSUMMARY));
            client.when(request("/series/5433/filter", GET, param(KEYS, "Key"), param("value", "FilterSeries"))).respond(jsonResponse(SERIES));
            client.when(request("/series/9465/filter", GET, param(KEYS, "AnotherKey"))).respond(jsonResponse(SERIES));
            client.when(request("/series/35445/filter/params", GET)).respond(jsonResponse(QUERYPARAMETERS));
            client.when(request("/series/6232/images", GET)).respond(jsonResponse(IMAGESUMMARY));
            client.when(request("/series/32145/images/query", GET)).respond(jsonResponse(IMAGES));
            client.when(request("/series/98748/images/query", GET, param("value", "QueryImages"))).respond(jsonResponse(IMAGES));
            client.when(request("/series/34554/images/query", GET, param(KEYTYPE, "Type1"), param(RESOLUTION, "10x10"))).respond(jsonResponse(IMAGES));
            client.when(request("/series/58741/images/query", GET, param(KEYTYPE, "Type2"), param(RESOLUTION, "20x20"), param(SUBKEY, "Key2"))).respond(jsonResponse(IMAGES));
            client.when(request("/series/89467/images/query", GET, param(KEYTYPE, "Type3"))).respond(jsonResponse(IMAGES));
            client.when(request("/series/97345/images/query", GET, param(RESOLUTION, "30x30"))).respond(jsonResponse(IMAGES));
            client.when(request("/series/34964/images/query", GET, param(SUBKEY, "Key3"))).respond(jsonResponse(IMAGES));
            client.when(request("/series/88861/images/query/params", GET)).respond(jsonResponse(IMAGEQUERYPARAMETERS));
            client.when(request("/updated/query", GET, param(FROMTIME, "7"))).respond(jsonResponse(UPDATES));
            client.when(request("/updated/query", GET, param(FROMTIME, "54"))).respond(jsonResponse(UPDATES));
            client.when(request("/updated/query", GET, param(FROMTIME, "76"), param(TOTIME, "99"))).respond(jsonResponse(UPDATES));
            client.when(request("/updated/query/params", GET)).respond(jsonResponse(QUERYPARAMETERS));
            client.when(request("/user", GET)).respond(jsonResponse(USER));
            client.when(request("/user/favorites", GET)).respond(jsonResponse(FAVORITES));
            client.when(request("/user/favorites/3454", DELETE)).respond(jsonResponse(FAVORITES_EMPTY));
            client.when(request("/user/favorites/6452", PUT)).respond(jsonResponse(FAVORITES));
            client.when(request("/user/ratings", GET)).respond(jsonResponse(RATINGS));
            client.when(request("/user/ratings/query", GET)).respond(jsonResponse(RATINGS));
            client.when(request("/user/ratings/query", GET, param("value", "QueryRatings"))).respond(jsonResponse(RATINGS));
            client.when(request("/user/ratings/query", GET, param(ITEMTYPE, "Type"))).respond(jsonResponse(RATINGS));
            client.when(request("/user/ratings/query/params", GET)).respond(jsonResponse(QUERYPARAMETERS));
            client.when(request("/user/ratings/episode/42368/4", PUT)).respond(jsonResponse(RATINGS));
        }

        @SuppressWarnings("ConstantConditions")
        private Stream<Arguments> withInvalidParameters() {
            return Stream.of(
                    of(route(() -> basicAPI.searchSeriesByName(null), "searchSeriesByName() with NULL parameter")),
                    of(route(() -> basicAPI.searchSeriesByName("  "), "searchSeriesByName() with empty parameter")),
                    of(route(() -> basicAPI.searchSeriesByImdbId(null), "searchSeriesByImdbId() with NULL parameter")),
                    of(route(() -> basicAPI.searchSeriesByImdbId(" "), "searchSeriesByImdbId() with empty parameter")),
                    of(route(() -> basicAPI.searchSeriesByZap2itId(null), "searchSeriesByZap2itId() with NULL parameter")),
                    of(route(() -> basicAPI.searchSeriesByZap2itId("   "), "searchSeriesByZap2itId() with empty parameter")),
                    of(route(() -> basicAPI.filterSeries(2134, (QueryParameters) null), "filterSeries() with NULL query parameter")),
                    of(route(() -> basicAPI.filterSeries(4421, params("value", "FilterSeries")), "filterSeries() without mandatory query parameter")),
                    of(route(() -> basicAPI.filterSeries(3998, (String) null), "filterSeries() with NULL String parameter")),
                    of(route(() -> basicAPI.filterSeries(5373, ""), "filterSeries() with empty String parameter")),
                    of(route(() -> basicAPI.queryImages(7874, null, null), "queryImages() with NULL type and resolution parameter")),
                    of(route(() -> basicAPI.queryImages(64257, "    ", " "), "queryImages() with empty type and resolution parameter")),
                    of(route(() -> basicAPI.queryImages(36784, null, null, null), "queryImages() with NULL type, resolution and key parameter")),
                    of(route(() -> basicAPI.queryImages(98614, "    ", " ", "  "), "queryImages() with empty type, resolution and key parameter")),
                    of(route(() -> basicAPI.queryImagesByKeyType(3645, null), "queryImagesByKeyType() with NULL parameter")),
                    of(route(() -> basicAPI.queryImagesByKeyType(5487, ""), "queryImagesByKeyType() with empty parameter")),
                    of(route(() -> basicAPI.queryImagesByResolution(6978, null), "queryImagesByResolution() with NULL parameter")),
                    of(route(() -> basicAPI.queryImagesByResolution(3645, "  "), "queryImagesByResolution() with empty parameter")),
                    of(route(() -> basicAPI.queryImagesBySubKey(6311, null), "queryImagesBySubKey() with NULL parameter")),
                    of(route(() -> basicAPI.queryImagesBySubKey(9447, "   "), "queryImagesBySubKey() with empty parameter")),
                    of(route(() -> basicAPI.getUser(), "getUser() without authentication")),
                    of(route(() -> basicAPI.getFavorites(), "getFavorites() without authentication")),
                    of(route(() -> basicAPI.deleteFromFavorites(4874), "deleteFromFavorites() without authentication")),
                    of(route(() -> basicAPI.addToFavorites(6987), "addToFavorites() without authentication")),
                    of(route(() -> basicAPI.getRatings(), "getRatings() without authentication")),
                    of(route(() -> basicAPI.queryRatings(null), "queryRatings() without authentication")),
                    of(route(() -> basicAPI.queryRatingsByItemType("SomeType"), "queryRatingsByItemType() without authentication")),
                    of(route(() -> userAuthApi.queryRatingsByItemType(null), "queryRatingsByItemType() with NULL parameter")),
                    of(route(() -> userAuthApi.queryRatingsByItemType("    "), "queryRatingsByItemType() with empty parameter")),
                    of(route(() -> basicAPI.getAvailableRatingsQueryParameters(), "getAvailableRatingsQueryParameters() without authentication")),
                    of(route(() -> basicAPI.deleteFromRatings("Type", 4439), "deleteFromRatings() without authentication")),
                    of(route(() -> userAuthApi.deleteFromRatings(null, 8743), "deleteFromRatings() with NULL parameter")),
                    of(route(() -> userAuthApi.deleteFromRatings("", 7487), "deleteFromRatings() with empty parameter")),
                    of(route(() -> basicAPI.addToRatings("episode", 7432, 6), "addToRatings() without authentication")),
                    of(route(() -> userAuthApi.addToRatings(null, 54775, 8), "addToRatings() with NULL parameter")),
                    of(route(() -> userAuthApi.addToRatings(" ", 6998, 3), "addToRatings() with empty parameter"))
            );
        }

        private Stream<Arguments> withValidParameters() {
            return Stream.of(
                    of(route(() -> basicAPI.init(), "init()"), verify("/login", POST)),
                    of(route(() -> basicAPI.login(), "login()"), verify("/login", POST)),
                    of(route(() -> basicAPI.refreshToken(), "refreshToken()"), verify("/refresh_token", GET)),
                    of(route(() -> basicAPI.getEpisode(8477), "getEpisode()"), EPISODE),
                    of(route(() -> basicAPI.getAvailableLanguages(), "getAvailableLanguages()"), LANGUAGES),
                    of(route(() -> basicAPI.getLanguage(3184), "getLanguage()"), LANGUAGE),
                    of(route(() -> basicAPI.getMovie(64874), "getMovie()"), MOVIE),
                    of(route(() -> basicAPI.getMovieUpdates(77), "getMovieUpdates()"), MOVIEUPDATES),
                    of(route(() -> basicAPI.searchSeries(params("value", "SearchSeries")), "searchSeries()"), SERIESSEARCH),
                    of(route(() -> basicAPI.searchSeriesByName("Name"), "searchSeriesByName()"), SERIESSEARCH),
                    of(route(() -> basicAPI.searchSeriesByImdbId("ImdbID"), "searchSeriesByImdbId()"), SERIESSEARCH),
                    of(route(() -> basicAPI.searchSeriesByZap2itId("Zap2itID"), "searchSeriesByZap2itId()"), SERIESSEARCH),
                    of(route(() -> basicAPI.getAvailableSeriesSearchParameters(), "getAvailableSeriesSearchParameters()"), QUERYPARAMETERS),
                    of(route(() -> basicAPI.getSeries(97845), "getSeries()"), SERIES),
                    of(route(() -> basicAPI.getSeriesHeaderInformation(63118), "getSeriesHeaderInformation()"), SERIESHEADER),
                    of(route(() -> basicAPI.getActors(96587), "getActors()"), ACTORS),
                    of(route(() -> basicAPI.getEpisodes(54783), "getEpisodes()"), EPISODES),
                    of(route(() -> basicAPI.getEpisodes(42684, params("value", "GetEpisodes")), "getEpisodes() with query parameters"), EPISODES),
                    of(route(() -> basicAPI.getEpisodes(69745, 4), "getEpisodes() with page"), EPISODES),
                    of(route(() -> basicAPI.queryEpisodes(8745, null), "queryEpisodes() without query parameters"), EPISODES),
                    of(route(() -> basicAPI.queryEpisodes(5748, params("value", "QueryEpisodes")), "queryEpisodes() with query parameters"), EPISODES),
                    of(route(() -> basicAPI.queryEpisodesByAiredSeason(6359, 2), "queryEpisodesByAiredSeason()"), EPISODES),
                    of(route(() -> basicAPI.queryEpisodesByAiredSeason(9912, 3, 2), "queryEpisodesByAiredSeason() with page"), EPISODES),
                    of(route(() -> basicAPI.queryEpisodesByAiredEpisode(3635, 21), "queryEpisodesByAiredEpisode()"), EPISODES),
                    of(route(() -> basicAPI.queryEpisodesByAbsoluteNumber(5933, 54), "queryEpisodesByAbsoluteNumber()"), EPISODES),
                    of(route(() -> basicAPI.getAvailableEpisodeQueryParameters(57874), "getAvailableEpisodeQueryParameters()"), QUERYPARAMETERS_NESTED),
                    of(route(() -> basicAPI.getSeriesEpisodesSummary(8111), "getSeriesEpisodesSummary()"), SERIESSUMMARY),
                    of(route(() -> basicAPI.filterSeries(5433, params(KEYS, "Key", "value", "FilterSeries")), "filterSeries() with query parameters"), SERIES),
                    of(route(() -> basicAPI.filterSeries(9465, "AnotherKey"), "filterSeries() with keys"), SERIES),
                    of(route(() -> basicAPI.getAvailableSeriesFilterParameters(35445), "getAvailableSeriesFilterParameters()"), QUERYPARAMETERS),
                    of(route(() -> basicAPI.getSeriesImagesSummary(6232), "getSeriesImagesSummary()"), IMAGESUMMARY),
                    of(route(() -> basicAPI.queryImages(32145, null), "queryImages() without query parameters"), IMAGES),
                    of(route(() -> basicAPI.queryImages(98748, params("value", "QueryImages")), "queryImages() with query parameters"), IMAGES),
                    of(route(() -> basicAPI.queryImages(34554, "Type1", "10x10"), "queryImages() with type and resolution"), IMAGES),
                    of(route(() -> basicAPI.queryImages(58741, "Type2", "20x20", "Key2"), "queryImages() with type, resolution and key"), IMAGES),
                    of(route(() -> basicAPI.queryImagesByKeyType(89467, "Type3"), "queryImagesByKeyType()"), IMAGES),
                    of(route(() -> basicAPI.queryImagesByResolution(97345, "30x30"), "queryImagesByResolution()"), IMAGES),
                    of(route(() -> basicAPI.queryImagesBySubKey(34964, "Key3"), "queryImagesBySubKey()"), IMAGES),
                    of(route(() -> basicAPI.getAvailableImageQueryParameters(88861), "getAvailableImageQueryParameters()"), IMAGEQUERYPARAMETERS),
                    of(route(() -> basicAPI.queryLastUpdated(params(FROMTIME, "7")), "queryLastUpdated() with query parameters"), UPDATES),
                    of(route(() -> basicAPI.queryLastUpdated(54), "queryLastUpdated() with from time"), UPDATES),
                    of(route(() -> basicAPI.queryLastUpdated(76, 99), "queryLastUpdated() with from and to time"), UPDATES),
                    of(route(() -> basicAPI.getAvailableLastUpdatedQueryParameters(), "getAvailableLastUpdatedQueryParameters()"), QUERYPARAMETERS),
                    of(route(() -> userAuthApi.getUser(), "getUser()"), USER),
                    of(route(() -> userAuthApi.getFavorites(), "getFavorites()"), FAVORITES),
                    of(route(() -> userAuthApi.deleteFromFavorites(3454), "deleteFromFavorites()"), FAVORITES_EMPTY),
                    of(route(() -> userAuthApi.addToFavorites(6452), "addToFavorites()"), FAVORITES),
                    of(route(() -> userAuthApi.getRatings(), "getRatings()"), RATINGS),
                    of(route(() -> userAuthApi.queryRatings(null), "queryRatings() without query parameters"), RATINGS),
                    of(route(() -> userAuthApi.queryRatings(params("value", "QueryRatings")), "queryRatings() with query parameters"), RATINGS),
                    of(route(() -> userAuthApi.queryRatingsByItemType("Type"), "queryRatingsByItemType()"), RATINGS),
                    of(route(() -> userAuthApi.getAvailableRatingsQueryParameters(), "getAvailableRatingsQueryParameters()"), QUERYPARAMETERS),
                    of(route(() -> userAuthApi.deleteFromRatings("image", 7874), "deleteFromRatings()"), verify("/user/ratings/image/7874", DELETE)),
                    of(route(() -> userAuthApi.addToRatings("episode", 42368, 4), "addToRatings()"), RATINGS)
            );
        }
        //@formatter:on

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
            TheTVDBApi api = new TheTVDBApiImpl("ZWUD785K5H7F", remoteAPI);
            api.init(token);
            assertThat(api.getToken()).isNotEmpty().contains(token);
        }

        @Test
        void setLanguage_withValidLanguage_verifyLanguageIsSetInApiRequests(MockServerClient client, Proxy remoteAPI)
                throws Exception {
            final String language = "es";
            TheTVDBApi api = init(new TheTVDBApiImpl("65SOWU45S4FAA", remoteAPI));
            api.setLanguage(language);
            api.getEpisodes(69845);
            client.verify(HttpRequest.request("/series/69845/episodes")
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
            basicAPI = init(new TheTVDBApiImpl("OIRE71D2V145FS", remoteAPI)).json();
            userAuthApi = init(new TheTVDBApiImpl("AAD66G72S3R74F", "unique_9878424", "Dark Helmet", remoteAPI)).json();
        }

        //@formatter:off
        @BeforeAll
        void setUpRoutes(MockServerClient client) throws Exception {
            client.when(request("/episodes/5478", GET)).respond(jsonResponse(EPISODE));
            client.when(request("/languages", GET)).respond(jsonResponse(LANGUAGES));
            client.when(request("/languages/1547", GET)).respond(jsonResponse(LANGUAGE));
            client.when(request("/movies/74871", GET)).respond(jsonResponse(MOVIE));
            client.when(request("/movieupdates", GET, param(SINCE, "58"))).respond(jsonResponse(MOVIEUPDATES));
            client.when(request("/search/series", GET, param("value", "SearchSeriesJson"))).respond(jsonResponse(SERIESSEARCH));
            client.when(request("/search/series/params", GET)).respond(jsonResponse(QUERYPARAMETERS));
            client.when(request("/series/45741", GET)).respond(jsonResponse(SERIES));
            client.when(request("/series/64778", HEAD)).respond(response().withHeaders(getHeadersFrom(SERIESHEADER)));
            client.when(request("/series/32157/actors", GET)).respond(jsonResponse(ACTORS));
            client.when(request("/series/77412/episodes", GET)).respond(jsonResponse(EPISODES));
            client.when(request("/series/42684/episodes", GET, param("value", "GetEpisodesJson"))).respond(jsonResponse(EPISODES));
            client.when(request("/series/39834/episodes/query", GET)).respond(jsonResponse(EPISODES));
            client.when(request("/series/55612/episodes/query", GET, param("value", "QueryEpisodesJson"))).respond(jsonResponse(EPISODES));
            client.when(request("/series/64978/episodes/query/params", GET)).respond(jsonResponse(QUERYPARAMETERS_NESTED));
            client.when(request("/series/2344/episodes/summary", GET)).respond(jsonResponse(SERIESSUMMARY));
            client.when(request("/series/3145/filter", GET, param(KEYS, "Key"), param("value", "FilterSeriesJson"))).respond(jsonResponse(SERIES));
            client.when(request("/series/69745/filter/params", GET)).respond(jsonResponse(QUERYPARAMETERS));
            client.when(request("/series/4518/images", GET)).respond(jsonResponse(IMAGESUMMARY));
            client.when(request("/series/64475/images/query", GET)).respond(jsonResponse(IMAGES));
            client.when(request("/series/71121/images/query", GET, param("value", "QueryImagesJson"))).respond(jsonResponse(IMAGES));
            client.when(request("/series/36556/images/query/params", GET)).respond(jsonResponse(IMAGEQUERYPARAMETERS));
            client.when(request("/updated/query", GET, param(FROMTIME, "12"), param(TOTIME, "22"))).respond(jsonResponse(UPDATES));
            client.when(request("/updated/query/params", GET)).respond(jsonResponse(QUERYPARAMETERS));
            client.when(request("/user", GET)).respond(jsonResponse(USER));
            client.when(request("/user/favorites", GET)).respond(jsonResponse(FAVORITES));
            client.when(request("/user/favorites/5854", DELETE)).respond(jsonResponse(FAVORITES_EMPTY));
            client.when(request("/user/favorites/4295", PUT)).respond(jsonResponse(FAVORITES));
            client.when(request("/user/ratings", GET)).respond(jsonResponse(RATINGS));
            client.when(request("/user/ratings/query", GET)).respond(jsonResponse(RATINGS));
            client.when(request("/user/ratings/query", GET, param("value", "QueryRatingsJson"))).respond(jsonResponse(RATINGS));
            client.when(request("/user/ratings/query/params", GET)).respond(jsonResponse(QUERYPARAMETERS));
            client.when(request("/user/ratings/image/24514", DELETE)).respond(jsonResponse(EMPTY));
            client.when(request("/user/ratings/series/64874/3", PUT)).respond(jsonResponse(RATINGS));
        }

        @SuppressWarnings("ConstantConditions")
        private Stream<Arguments> withInvalidParameters() {
            return Stream.of(
                    of(route(() -> basicAPI.filterSeries(3659, null), "filterSeries() with NULL query parameter")),
                    of(route(() -> basicAPI.filterSeries(4421, params("value", "FilterSeriesJson")), "filterSeries() without mandatory query parameter")),
                    of(route(() -> basicAPI.queryLastUpdated(null), "queryLastUpdated() with NULL query parameter")),
                    of(route(() -> basicAPI.queryLastUpdated(params(TOTIME, "30")), "queryLastUpdated() without mandatory query parameter")),
                    of(route(() -> basicAPI.getUser(), "getUser() without authentication")),
                    of(route(() -> basicAPI.getFavorites(), "getFavorites() without authentication")),
                    of(route(() -> basicAPI.deleteFromFavorites(1745), "deleteFromFavorites() without authentication")),
                    of(route(() -> basicAPI.addToFavorites(6745), "addToFavorites() without authentication")),
                    of(route(() -> basicAPI.getRatings(), "getRatings() without authentication")),
                    of(route(() -> basicAPI.queryRatings(null), "queryRatings() without authentication")),
                    of(route(() -> basicAPI.getAvailableRatingsQueryParameters(), "getAvailableRatingsQueryParameters() without authentication")),
                    of(route(() -> basicAPI.deleteFromRatings("Type", 6966), "deleteFromRatings() without authentication")),
                    of(route(() -> userAuthApi.deleteFromRatings(null, 3647), "deleteFromRatings() with NULL parameter")),
                    of(route(() -> userAuthApi.deleteFromRatings(" ", 7391), "deleteFromRatings() with empty parameter")),
                    of(route(() -> basicAPI.addToRatings("series", 3487, 5), "addToRatings() without authentication")),
                    of(route(() -> userAuthApi.addToRatings(null, 64971, 6), "addToRatings() with NULL parameter")),
                    of(route(() -> userAuthApi.addToRatings("  ", 3323, 9), "addToRatings() with empty parameter"))
            );
        }

        private Stream<Arguments> withValidParameters() {
            return Stream.of(
                    of(route(() -> basicAPI.getEpisode(5478), "getEpisode()"), EPISODE),
                    of(route(() -> basicAPI.getAvailableLanguages(), "getAvailableLanguages()"), LANGUAGES),
                    of(route(() -> basicAPI.getLanguage(1547), "getLanguage()"), LANGUAGE),
                    of(route(() -> basicAPI.getMovie(74871), "getMovie()"), MOVIE),
                    of(route(() -> basicAPI.getMovieUpdates(58), "getMovieUpdates()"), MOVIEUPDATES),
                    of(route(() -> basicAPI.searchSeries(params("value", "SearchSeriesJson")), "searchSeries()"), SERIESSEARCH),
                    of(route(() -> basicAPI.getAvailableSeriesSearchParameters(), "getAvailableSeriesSearchParameters()"), QUERYPARAMETERS),
                    of(route(() -> basicAPI.getSeries(45741), "getSeries()"), SERIES),
                    of(route(() -> basicAPI.getSeriesHeaderInformation(64778), "getSeriesHeaderInformation()"), SERIESHEADER),
                    of(route(() -> basicAPI.getActors(32157), "getActors()"), ACTORS),
                    of(route(() -> basicAPI.getEpisodes(77412, null), "getEpisodes() without query parameters"), EPISODES),
                    of(route(() -> basicAPI.getEpisodes(42684, params("value", "GetEpisodesJson")), "getEpisodes() with query parameters"), EPISODES),
                    of(route(() -> basicAPI.queryEpisodes(39834, null), "queryEpisodes() without query parameters"), EPISODES),
                    of(route(() -> basicAPI.queryEpisodes(55612, params("value", "QueryEpisodesJson")), "queryEpisodes() with query parameters"), EPISODES),
                    of(route(() -> basicAPI.getAvailableEpisodeQueryParameters(64978), "getAvailableEpisodeQueryParameters()"), QUERYPARAMETERS_NESTED),
                    of(route(() -> basicAPI.getSeriesEpisodesSummary(2344), "getSeriesEpisodesSummary()"), SERIESSUMMARY),
                    of(route(() -> basicAPI.filterSeries(3145, params(KEYS, "Key", "value", "FilterSeriesJson")), "filterSeries()"), SERIES),
                    of(route(() -> basicAPI.getAvailableSeriesFilterParameters(69745), "getAvailableSeriesFilterParameters()"), QUERYPARAMETERS),
                    of(route(() -> basicAPI.getSeriesImagesSummary(4518), "getSeriesImagesSummary()"), IMAGESUMMARY),
                    of(route(() -> basicAPI.queryImages(64475, null), "queryImages() without query parameters"), IMAGES),
                    of(route(() -> basicAPI.queryImages(71121, params("value", "QueryImagesJson")), "queryImages() with query parameters"), IMAGES),
                    of(route(() -> basicAPI.getAvailableImageQueryParameters(36556), "getAvailableImageQueryParameters()"), IMAGEQUERYPARAMETERS),
                    of(route(() -> basicAPI.queryLastUpdated(params(FROMTIME, "12", TOTIME, "22")), "queryLastUpdated() with query parameters"), UPDATES),
                    of(route(() -> basicAPI.getAvailableLastUpdatedQueryParameters(), "getAvailableLastUpdatedQueryParameters()"), QUERYPARAMETERS),
                    of(route(() -> userAuthApi.getUser(), "getUser()"), USER),
                    of(route(() -> userAuthApi.getFavorites(), "getFavorites()"), FAVORITES),
                    of(route(() -> userAuthApi.deleteFromFavorites(5854), "deleteFromFavorites()"), FAVORITES_EMPTY),
                    of(route(() -> userAuthApi.addToFavorites(4295), "addToFavorites()"), FAVORITES),
                    of(route(() -> userAuthApi.getRatings(), "getRatings()"), RATINGS),
                    of(route(() -> userAuthApi.queryRatings(null), "queryRatings() without query parameters"), RATINGS),
                    of(route(() -> userAuthApi.queryRatings(params("value", "QueryRatingsJson")), "queryRatings() with query parameters"), RATINGS),
                    of(route(() -> userAuthApi.getAvailableRatingsQueryParameters(), "getAvailableRatingsQueryParameters()"), QUERYPARAMETERS),
                    of(route(() -> userAuthApi.deleteFromRatings("image", 24514), "deleteFromRatings()"), EMPTY),
                    of(route(() -> userAuthApi.addToRatings("series", 64874, 3), "addToRatings()"), RATINGS)
            );
        }
        //@formatter:on

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
            basicAPI = init(new TheTVDBApiImpl("IOE45S4R82S5", remoteAPI)).extended();
            userAuthApi = init(new TheTVDBApiImpl("D69L8F4N5X4W6R", "unique_5481157", "King Roland", remoteAPI))
                    .extended();
        }

        //@formatter:off
        @BeforeAll
        void setUpRoutes(MockServerClient client) throws Exception {
            client.when(request("/episodes/6341", GET)).respond(jsonResponse(EPISODE));
            client.when(request("/languages", GET)).respond(jsonResponse(LANGUAGES));
            client.when(request("/languages/1119", GET)).respond(jsonResponse(LANGUAGE));
            client.when(request("/movies/88122", GET)).respond(jsonResponse(MOVIE));
            client.when(request("/movieupdates", GET, param(SINCE, "16"))).respond(jsonResponse(MOVIEUPDATES));
            client.when(request("/search/series", GET, param("value", "SearchSeriesExtended"))).respond(jsonResponse(SERIESSEARCH));
            client.when(request("/search/series/params", GET)).respond(jsonResponse(QUERYPARAMETERS));
            client.when(request("/series/45748", GET)).respond(jsonResponse(SERIES));
            client.when(request("/series/48451", HEAD)).respond(response().withHeaders(getHeadersFrom(SERIESHEADER)));
            client.when(request("/series/12799/actors", GET)).respond(jsonResponse(ACTORS));
            client.when(request("/series/45744/episodes", GET)).respond(jsonResponse(EPISODES));
            client.when(request("/series/36945/episodes", GET, param("value", "GetEpisodesExtended"))).respond(jsonResponse(EPISODES));
            client.when(request("/series/6497/episodes/query", GET)).respond(jsonResponse(EPISODES));
            client.when(request("/series/3648/episodes/query", GET, param("value", "QueryEpisodesExtended"))).respond(jsonResponse(EPISODES));
            client.when(request("/series/56551/episodes/query/params", GET)).respond(jsonResponse(QUERYPARAMETERS_NESTED));
            client.when(request("/series/5996/episodes/summary", GET)).respond(jsonResponse(SERIESSUMMARY));
            client.when(request("/series/3647/filter", GET, param(KEYS, "Key"), param("value", "FilterSeriesExtended"))).respond(jsonResponse(SERIES));
            client.when(request("/series/94784/filter/params", GET)).respond(jsonResponse(QUERYPARAMETERS));
            client.when(request("/series/6323/images", GET)).respond(jsonResponse(IMAGESUMMARY));
            client.when(request("/series/15899/images/query", GET)).respond(jsonResponse(IMAGES));
            client.when(request("/series/11541/images/query", GET, param("value", "QueryImagesExtended"))).respond(jsonResponse(IMAGES));
            client.when(request("/series/34813/images/query/params", GET)).respond(jsonResponse(IMAGEQUERYPARAMETERS));
            client.when(request("/updated/query", GET, param(FROMTIME, "56"), param(TOTIME, "61"))).respond(jsonResponse(UPDATES));
            client.when(request("/updated/query/params", GET)).respond(jsonResponse(QUERYPARAMETERS));
            client.when(request("/user", GET)).respond(jsonResponse(USER));
            client.when(request("/user/favorites", GET)).respond(jsonResponse(FAVORITES));
            client.when(request("/user/favorites/8764", DELETE)).respond(jsonResponse(FAVORITES_EMPTY));
            client.when(request("/user/favorites/5541", PUT)).respond(jsonResponse(FAVORITES));
            client.when(request("/user/ratings", GET)).respond(jsonResponse(RATINGS));
            client.when(request("/user/ratings/query", GET)).respond(jsonResponse(RATINGS));
            client.when(request("/user/ratings/query", GET, param("value", "QueryRatingsExtended"))).respond(jsonResponse(RATINGS));
            client.when(request("/user/ratings/query/params", GET)).respond(jsonResponse(QUERYPARAMETERS));
            client.when(request("/user/ratings/image/79481/7", PUT)).respond(jsonResponse(RATINGS));
        }

        @SuppressWarnings("ConstantConditions")
        private Stream<Arguments> withInvalidParameters() {
            return Stream.of(
                    of(route(() -> basicAPI.filterSeries(2411, null), "filterSeries() with NULL query parameter")),
                    of(route(() -> basicAPI.filterSeries(2487, params("value", "FilterSeries")), "filterSeries() without mandatory query parameter")),
                    of(route(() -> basicAPI.queryLastUpdated(null), "queryLastUpdated() with NULL query parameter")),
                    of(route(() -> basicAPI.queryLastUpdated(params(TOTIME, "72")), "queryLastUpdated() without mandatory query parameter")),
                    of(route(() -> basicAPI.getUser(), "getUser() without authentication")),
                    of(route(() -> basicAPI.getFavorites(), "getFavorites() without authentication")),
                    of(route(() -> basicAPI.deleteFromFavorites(4643), "deleteFromFavorites() without authentication")),
                    of(route(() -> basicAPI.addToFavorites(1298), "addToFavorites() without authentication")),
                    of(route(() -> basicAPI.getRatings(), "getRatings() without authentication")),
                    of(route(() -> basicAPI.queryRatings(null), "queryRatings() without authentication")),
                    of(route(() -> basicAPI.getAvailableRatingsQueryParameters(), "getAvailableRatingsQueryParameters() without authentication")),
                    of(route(() -> basicAPI.deleteFromRatings("Type", 3548), "deleteFromRatings() without authentication")),
                    of(route(() -> userAuthApi.deleteFromRatings(null, 3913), "deleteFromRatings() with NULL parameter")),
                    of(route(() -> userAuthApi.deleteFromRatings(" ", 4395), "deleteFromRatings() with empty parameter")),
                    of(route(() -> basicAPI.addToRatings("image", 6433, 1), "addToRatings() without authentication")),
                    of(route(() -> userAuthApi.addToRatings(null, 83744, 3), "addToRatings() with NULL parameter")),
                    of(route(() -> userAuthApi.addToRatings("  ", 7441, 9), "addToRatings() with empty parameter"))
            );
        }

        private Stream<Arguments> withValidParameters() {
            return Stream.of(
                    of(route(() -> basicAPI.getEpisode(6341), "getEpisode()"), EPISODE),
                    of(route(() -> basicAPI.getAvailableLanguages(), "getAvailableLanguages()"), LANGUAGES),
                    of(route(() -> basicAPI.getLanguage(1119), "getLanguage()"), LANGUAGE),
                    of(route(() -> basicAPI.getMovie(88122), "getMovie()"), MOVIE),
                    of(route(() -> basicAPI.getMovieUpdates(16), "getMovieUpdates()"), MOVIEUPDATES),
                    of(route(() -> basicAPI.searchSeries(params("value", "SearchSeriesExtended")), "searchSeries()"), SERIESSEARCH),
                    of(route(() -> basicAPI.getAvailableSeriesSearchParameters(), "getAvailableSeriesSearchParameters()"), QUERYPARAMETERS),
                    of(route(() -> basicAPI.getSeries(45748), "getSeries()"), SERIES),
                    of(route(() -> basicAPI.getSeriesHeaderInformation(48451), "getSeriesHeaderInformation()"), SERIESHEADER),
                    of(route(() -> basicAPI.getActors(12799), "getActors()"), ACTORS),
                    of(route(() -> basicAPI.getEpisodes(45744, null), "getEpisodes() without query parameters"), EPISODES),
                    of(route(() -> basicAPI.getEpisodes(36945, params("value", "GetEpisodesExtended")), "getEpisodes() with query parameters"), EPISODES),
                    of(route(() -> basicAPI.queryEpisodes(6497, null), "queryEpisodes() without query parameters"), EPISODES),
                    of(route(() -> basicAPI.queryEpisodes(3648, params("value", "QueryEpisodesExtended")), "queryEpisodes() with query parameters"), EPISODES),
                    of(route(() -> basicAPI.getAvailableEpisodeQueryParameters(56551), "getAvailableEpisodeQueryParameters()"), QUERYPARAMETERS_NESTED),
                    of(route(() -> basicAPI.getSeriesEpisodesSummary(5996), "getSeriesEpisodesSummary()"), SERIESSUMMARY),
                    of(route(() -> basicAPI.filterSeries(3647, params(KEYS, "Key", "value", "FilterSeriesExtended")), "filterSeries() with query parameters"), SERIES),
                    of(route(() -> basicAPI.getAvailableSeriesFilterParameters(94784), "getAvailableSeriesFilterParameters()"), QUERYPARAMETERS),
                    of(route(() -> basicAPI.getSeriesImagesSummary(6323), "getSeriesImagesSummary()"), IMAGESUMMARY),
                    of(route(() -> basicAPI.queryImages(15899, null), "queryImages() without query parameters"), IMAGES),
                    of(route(() -> basicAPI.queryImages(11541, params("value", "QueryImagesExtended")), "queryImages() with query parameters"), IMAGES),
                    of(route(() -> basicAPI.getAvailableImageQueryParameters(34813), "getAvailableImageQueryParameters()"), IMAGEQUERYPARAMETERS),
                    of(route(() -> basicAPI.queryLastUpdated(params(FROMTIME, "56", TOTIME, "61")), "queryLastUpdated() with query parameters"), UPDATES),
                    of(route(() -> basicAPI.getAvailableLastUpdatedQueryParameters(), "getAvailableLastUpdatedQueryParameters()"), QUERYPARAMETERS),
                    of(route(() -> userAuthApi.getUser(), "getUser()"), USER),
                    of(route(() -> userAuthApi.getFavorites(), "getFavorites()"), FAVORITES),
                    of(route(() -> userAuthApi.deleteFromFavorites(8764), "deleteFromFavorites()"), FAVORITES_EMPTY),
                    of(route(() -> userAuthApi.addToFavorites(5541), "addToFavorites()"), FAVORITES),
                    of(route(() -> userAuthApi.getRatings(), "getRatings()"), RATINGS),
                    of(route(() -> userAuthApi.queryRatings(null), "queryRatings() without query parameters"), RATINGS),
                    of(route(() -> userAuthApi.queryRatings(params("value", "QueryRatingsExtended")), "queryRatings() with query parameters"), RATINGS),
                    of(route(() -> userAuthApi.getAvailableRatingsQueryParameters(), "getAvailableRatingsQueryParameters()"), QUERYPARAMETERS),
                    of(route(() -> userAuthApi.deleteFromRatings("episode", 6127), "deleteFromRatings()"), verify("/user/ratings/episode/6127", DELETE)),
                    of(route(() -> userAuthApi.addToRatings("image", 79481, 7), "addToRatings()"), RATINGS)
            );
        }
        //@formatter:on

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

/*
 * Copyright (C) 2019 - 2022 thetvdb-java-api Authors and Contributors
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
import static com.github.m0nk3y2k4.thetvdb.api.enumeration.UpdateAction.CREATE;
import static com.github.m0nk3y2k4.thetvdb.api.enumeration.UpdateEntityType.TRANSLATED_EPISODES;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.POST;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.params;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.ARTWORK;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.ARTWORKSTATUS_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.ARTWORKTYPE_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.ARTWORK_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.AWARD;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.AWARDCATEGORY;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.AWARDCATEGORY_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.AWARD_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.AWARD_OVERVIEW;
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
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.INSPIRATIONTYPE_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.LIST;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.LIST_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.LIST_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.MOVIE;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.MOVIE_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.MOVIE_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.PEOPLE;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.PEOPLETYPE_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.PEOPLE_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SEARCH_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SEASON;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SEASONTYPE_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SEASON_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SEASON_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIES;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIESEPISODES;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIESEPISODES_TRANSLATED;
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
import com.github.m0nk3y2k4.thetvdb.api.constants.Query.Episodes;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query.Lists;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query.Movies;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query.People;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query.Search;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query.Seasons;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.EpisodeMeta;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.MovieMeta;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.PeopleMeta;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.SearchType;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.SeasonMeta;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesMeta;
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

        private TheTVDBApi theTVDBApi;

        @BeforeAll
        void setUpAPI(Proxy remoteAPI) throws Exception {
            theTVDBApi = init(new TheTVDBApiImpl(CONTRACT_APIKEY, remoteAPI));
        }

        //@DisableFormatting
        @BeforeAll
        void setUpRoutes(MockServerClient client) throws Exception {
            client.when(request("/artwork/statuses", GET)).respond(jsonResponse(ARTWORKSTATUS_OVERVIEW));
            client.when(request("/artwork/types", GET)).respond(jsonResponse(ARTWORKTYPE_OVERVIEW));
            client.when(request("/artwork/3447", GET)).respond(jsonResponse(ARTWORK));
            client.when(request("/artwork/9403/extended", GET)).respond(jsonResponse(ARTWORK_DETAILS));
            client.when(request("/awards", GET)).respond(jsonResponse(AWARD_OVERVIEW));
            client.when(request("/awards/6361", GET)).respond(jsonResponse(AWARD));
            client.when(request("/awards/4470/extended", GET)).respond(jsonResponse(AWARD_DETAILS));
            client.when(request("/awards/categories/830", GET)).respond(jsonResponse(AWARDCATEGORY));
            client.when(request("/awards/categories/574/extended", GET)).respond(jsonResponse(AWARDCATEGORY_DETAILS));
            client.when(request("/characters/604784", GET)).respond(jsonResponse(CHARACTER));
            client.when(request("/companies", GET, param("value", "QueryCompanies"))).respond(jsonResponse(COMPANY_OVERVIEW));
            client.when(request("/companies", GET, param(Companies.PAGE, "1"))).respond(jsonResponse(COMPANY_OVERVIEW));
            client.when(request("/companies/types", GET)).respond(jsonResponse(COMPANYTYPE_OVERVIEW));
            client.when(request("/companies/241", GET)).respond(jsonResponse(COMPANY));
            client.when(request("/content/ratings", GET)).respond(jsonResponse(CONTENTRATING_OVERVIEW));
            client.when(request("/entities", GET)).respond(jsonResponse(ENTITYTYPE_OVERVIEW));
            client.when(request("/episodes/141007", GET)).respond(jsonResponse(EPISODE));
            client.when(request("/episodes/69784/extended", GET, param("value", "QueryEpisodeDetails"))).respond(jsonResponse(EPISODE_DETAILS));
            client.when(request("/episodes/37017/extended", GET)).respond(jsonResponse(EPISODE_DETAILS));
            client.when(request("/episodes/45704/extended", GET, param(Episodes.META, String.valueOf(EpisodeMeta.TRANSLATIONS)))).respond(jsonResponse(EPISODE_DETAILS));
            client.when(request("/episodes/35744/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/lists/54047/translations/fra", GET)).respond(jsonResponse(TRANSLATIONS));
            client.when(request("/lists", GET, param("value", "QueryLists"))).respond(jsonResponse(LIST_OVERVIEW));
            client.when(request("/lists", GET, param(Lists.PAGE, "4"))).respond(jsonResponse(LIST_OVERVIEW));
            client.when(request("/lists/6740", GET)).respond(jsonResponse(LIST));
            client.when(request("/lists/7901/extended", GET)).respond(jsonResponse(LIST_DETAILS));
            client.when(request("/genders", GET)).respond(jsonResponse(GENDER_OVERVIEW));
            client.when(request("/genres", GET)).respond(jsonResponse(GENRE_OVERVIEW));
            client.when(request("/genres/47", GET)).respond(jsonResponse(GENRE));
            client.when(request("/inspiration/types", GET)).respond(jsonResponse(INSPIRATIONTYPE_OVERVIEW));
            client.when(request("/movies/statuses", GET)).respond(jsonResponse(STATUS_OVERVIEW));
            client.when(request("/movies", GET, param("value", "QueryMovies"))).respond(jsonResponse(MOVIE_OVERVIEW));
            client.when(request("/movies", GET, param(Movies.PAGE, "3"))).respond(jsonResponse(MOVIE_OVERVIEW));
            client.when(request("/movies/54394", GET)).respond(jsonResponse(MOVIE));
            client.when(request("/movies/87416/extended", GET, param("value", "QueryMovieDetails"))).respond(jsonResponse(MOVIE_DETAILS));
            client.when(request("/movies/46994/extended", GET)).respond(jsonResponse(MOVIE_DETAILS));
            client.when(request("/movies/33657/extended", GET, param(Movies.META, String.valueOf(MovieMeta.TRANSLATIONS)))).respond(jsonResponse(MOVIE_DETAILS));
            client.when(request("/movies/69745/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/people/types", GET)).respond(jsonResponse(PEOPLETYPE_OVERVIEW));
            client.when(request("/people/431071", GET)).respond(jsonResponse(PEOPLE));
            client.when(request("/people/467845/extended", GET, param("value", "QueryPeopleDetails"))).respond(jsonResponse(PEOPLE_DETAILS));
            client.when(request("/people/574101/extended", GET)).respond(jsonResponse(PEOPLE_DETAILS));
            client.when(request("/people/800577/extended", GET, param(People.META, String.valueOf(PeopleMeta.TRANSLATIONS)))).respond(jsonResponse(PEOPLE_DETAILS));
            client.when(request("/people/471160/translations/por", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/search", GET, param(Search.Q, "SearchTerm"), param("value", "QuerySearch"))).respond(jsonResponse(SEARCH_OVERVIEW));
            client.when(request("/search", GET, param(Search.QUERY, "SearchTermOnly"))).respond(jsonResponse(SEARCH_OVERVIEW));
            client.when(request("/search", GET, param(Search.QUERY, "SearchTermAndType"), param(Search.TYPE, SearchType.SERIES.toString()))).respond(jsonResponse(SEARCH_OVERVIEW));
            client.when(request("/seasons", GET, param("value", "QuerySeasons"))).respond(jsonResponse(SEASON_OVERVIEW));
            client.when(request("/seasons", GET, param(Seasons.PAGE, "5"))).respond(jsonResponse(SEASON_OVERVIEW));
            client.when(request("/seasons/34167", GET)).respond(jsonResponse(SEASON));
            client.when(request("/seasons/68444/extended", GET, param("value", "QuerySeasonDetails"))).respond(jsonResponse(SEASON_DETAILS));
            client.when(request("/seasons/69761/extended", GET)).respond(jsonResponse(SEASON_DETAILS));
            client.when(request("/seasons/35047/extended", GET, param(Seasons.META, String.valueOf(SeasonMeta.TRANSLATIONS)))).respond(jsonResponse(SEASON_DETAILS));
            client.when(request("/seasons/types", GET)).respond(jsonResponse(SEASONTYPE_OVERVIEW));
            client.when(request("/seasons/27478/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/series/statuses", GET)).respond(jsonResponse(STATUS_OVERVIEW));
            client.when(request("/series", GET, param("value", "QuerySeries"))).respond(jsonResponse(SERIES_OVERVIEW));
            client.when(request("/series", GET, param(Series.PAGE, "2"))).respond(jsonResponse(SERIES_OVERVIEW));
            client.when(request("/series/2845", GET)).respond(jsonResponse(SERIES));
            client.when(request("/series/9342/artworks", GET, param("value", "QuerySeriesArtworks"))).respond(jsonResponse(SERIES_DETAILS));
            client.when(request("/series/8751/artworks", GET, param(Series.LANGUAGE, "rus"))).respond(jsonResponse(SERIES_DETAILS));
            client.when(request("/series/4577/extended", GET, param("value", "QuerySeriesDetails"))).respond(jsonResponse(SERIES_DETAILS));
            client.when(request("/series/9041/extended", GET)).respond(jsonResponse(SERIES_DETAILS));
            client.when(request("/series/1005/extended", GET, param(Series.META, String.valueOf(SeriesMeta.EPISODES)))).respond(jsonResponse(SERIES_DETAILS));
            client.when(request("/series/3789/episodes/default", GET)).respond(jsonResponse(SERIESEPISODES));
            client.when(request("/series/7000/episodes/alternate", GET, param("value", "QuerySeriesEpisodes"))).respond(jsonResponse(SERIESEPISODES));
            client.when(request("/series/2147/episodes/regional", GET, param(Series.SEASON, "4"))).respond(jsonResponse(SERIESEPISODES));
            client.when(request("/series/5481/episodes/dvd/eng", GET)).respond(jsonResponse(SERIESEPISODES_TRANSLATED));
            client.when(request("/series/6974/episodes/default/deu", GET, param("value", "QuerySeriesEpisodesTranslated"))).respond(jsonResponse(SERIESEPISODES_TRANSLATED));
            client.when(request("/series/6004/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/sources/types", GET)).respond(jsonResponse(SOURCETYPE_OVERVIEW));
            client.when(request("/updates", GET, param(Updates.SINCE, "16247601"), param("value", "QueryUpdates"))).respond(jsonResponse(UPDATE_OVERVIEW));
            client.when(request("/updates", GET, param(Updates.SINCE, "16236514"), param(Updates.PAGE, "3"))).respond(jsonResponse(UPDATE_OVERVIEW));
            client.when(request("/updates", GET, param(Updates.SINCE, "16239876"), param(Updates.TYPE, String.valueOf(TRANSLATED_EPISODES)), param(Updates.ACTION, String.valueOf(CREATE)), param(Updates.PAGE, "2"))).respond(jsonResponse(UPDATE_OVERVIEW));
        }

        @SuppressWarnings("ConstantConditions")
        private Stream<Arguments> withInvalidParameters() {
            return Stream.of(
                    of(route(() -> theTVDBApi.getAllCompanies(-1), "getAllCompanies() with negative page parameter")),
                    of(route(() -> theTVDBApi.getEpisodeDetails(87415, (EpisodeMeta)null), "getEpisodeDetails() with missing meta parameter")),
                    of(route(() -> theTVDBApi.getAllLists(-5), "getAllLists() with negative page parameter")),
                    of(route(() -> theTVDBApi.getAllMovies(-4), "getAllMovies() with negative page parameter")),
                    of(route(() -> theTVDBApi.getMovieDetails(98716, (MovieMeta)null), "getMovieDetails() with missing meta parameter")),
                    of(route(() -> theTVDBApi.getPeopleDetails(48710, (PeopleMeta)null), "getPeopleDetails() with missing meta parameter")),
                    of(route(() -> theTVDBApi.search(null), "search() with missing search term parameter")),
                    of(route(() -> theTVDBApi.search(null, null), "search() with missing search term and type parameter")),
                    of(route(() -> theTVDBApi.search("SearchTerm", null), "search() with missing type parameter")),
                    of(route(() -> theTVDBApi.getAllSeasons(-6), "getAllSeasons() with negative page parameter")),
                    of(route(() -> theTVDBApi.getSeasonDetails(97148, (SeasonMeta)null), "getSeasonDetails() with missing meta parameter")),
                    of(route(() -> theTVDBApi.getAllSeries(-3), "getAllSeries() with negative page parameter")),
                    of(route(() -> theTVDBApi.getSeriesArtworks(12407, (String)null), "getSeriesArtworks() with missing language parameter")),
                    of(route(() -> theTVDBApi.getSeriesDetails(68447, (SeriesMeta)null), "getSeriesDetails() with missing meta parameter")),
                    of(route(() -> theTVDBApi.getSeriesEpisodes(41257, DVD, -6), "getSeriesEpisodes() with negative season number parameter")),
                    of(route(() -> theTVDBApi.getUpdates(16237785, -8), "getUpdates() with negative page parameter")),
                    of(route(() -> theTVDBApi.getUpdates(16237871, null, CREATE, 3), "getUpdates() with missing type parameter")),
                    of(route(() -> theTVDBApi.getUpdates(16237945, TRANSLATED_EPISODES, null, 5), "getUpdates() with missing action parameter")),
                    of(route(() -> theTVDBApi.getUpdates(16238547, TRANSLATED_EPISODES, CREATE, -7), "getUpdates() with type, action and negative page parameter"))
            );
        }

        private Stream<Arguments> withValidParameters() {
            return Stream.of(
                    of(route(() -> theTVDBApi.init(), "init()"), verify("/login", POST)),
                    of(route(() -> theTVDBApi.login(), "login()"), verify("/login", POST)),
                    of(route(() -> theTVDBApi.getAllArtworkStatuses(), "getAllArtworkStatuses()"), ARTWORKSTATUS_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllArtworkTypes(), "getAllArtworkTypes()"), ARTWORKTYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getArtwork(3447), "getArtwork()"), ARTWORK),
                    of(route(() -> theTVDBApi.getArtworkDetails(9403), "getArtworkDetails()"), ARTWORK_DETAILS),
                    of(route(() -> theTVDBApi.getAllAwards(), "getAllAwards()"), AWARD_OVERVIEW),
                    of(route(() -> theTVDBApi.getAward(6361), "getAward()"), AWARD),
                    of(route(() -> theTVDBApi.getAwardDetails(4470), "getAwardDetails()"), AWARD_DETAILS),
                    of(route(() -> theTVDBApi.getAwardCategory(830), "getAwardCategory()"), AWARDCATEGORY),
                    of(route(() -> theTVDBApi.getAwardCategoryDetails(574), "getAwardCategoryDetails()"), AWARDCATEGORY_DETAILS),
                    of(route(() -> theTVDBApi.getCharacter(604784), "getCharacter()"), CHARACTER),
                    of(route(() -> theTVDBApi.getAllCompanies(params("value", "QueryCompanies")), "getAllCompanies() with query parameters"), COMPANY_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllCompanies(1), "getAllCompanies() with page"), COMPANY_OVERVIEW),
                    of(route(() -> theTVDBApi.getCompany(241), "getCompany()"), COMPANY),
                    of(route(() -> theTVDBApi.getCompanyTypes(), "getCompanyTypes()"), COMPANYTYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllContentRatings(), "getAllContentRatings()"), CONTENTRATING_OVERVIEW),
                    of(route(() -> theTVDBApi.getEntityTypes(), "getEntityTypes()"), ENTITYTYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getEpisode(141007), "getEpisode()"), EPISODE),
                    of(route(() -> theTVDBApi.getEpisodeDetails(69784, params("value", "QueryEpisodeDetails")), "getEpisodeDetails() with query parameters"), EPISODE_DETAILS),
                    of(route(() -> theTVDBApi.getEpisodeDetails(37017), "getEpisodeDetails() with episode ID"), EPISODE_DETAILS),
                    of(route(() -> theTVDBApi.getEpisodeDetails(45704, EpisodeMeta.TRANSLATIONS), "getEpisodeDetails() with episode ID and meta"), EPISODE_DETAILS),
                    of(route(() -> theTVDBApi.getEpisodeTranslation(35744, "eng"), "getEpisodeTranslation()"), TRANSLATION),
                    of(route(() -> theTVDBApi.getListTranslation(54047, "fra"), "getListTranslation()"), TRANSLATIONS),
                    of(route(() -> theTVDBApi.getAllLists(params("value", "QueryLists")), "getAllLists() with query parameters"), LIST_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllLists(4), "getAllLists() with page"), LIST_OVERVIEW),
                    of(route(() -> theTVDBApi.getList(6740), "getList()"), LIST),
                    of(route(() -> theTVDBApi.getListDetails(7901), "getListDetails()"), LIST_DETAILS),
                    of(route(() -> theTVDBApi.getAllGenders(), "getAllGenders()"), GENDER_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllGenres(), "getAllGenres()"), GENRE_OVERVIEW),
                    of(route(() -> theTVDBApi.getGenre(47), "getGenre()"), GENRE),
                    of(route(() -> theTVDBApi.getAllInspirationTypes(), "getAllInspirationTypes()"), INSPIRATIONTYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllMovieStatuses(), "getAllMovieStatuses()"), STATUS_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllMovies(params("value", "QueryMovies")), "getAllMovies() with query parameters"), MOVIE_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllMovies(3), "getAllMovies() with page"), MOVIE_OVERVIEW),
                    of(route(() -> theTVDBApi.getMovie(54394), "getMovie()"), MOVIE),
                    of(route(() -> theTVDBApi.getMovieDetails(87416, params("value", "QueryMovieDetails")), "getMovieDetails() with query parameters"), MOVIE_DETAILS),
                    of(route(() -> theTVDBApi.getMovieDetails(46994), "getMovieDetails() with movie ID"), MOVIE_DETAILS),
                    of(route(() -> theTVDBApi.getMovieDetails(33657, MovieMeta.TRANSLATIONS), "getMovieDetails() with movie ID and meta"), MOVIE_DETAILS),
                    of(route(() -> theTVDBApi.getMovieTranslation(69745, "eng"), "getMovieTranslation()"), TRANSLATION),
                    of(route(() -> theTVDBApi.getAllPeopleTypes(), "getAllPeopleTypes()"), PEOPLETYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getPeople(431071), "getPeople()"), PEOPLE),
                    of(route(() -> theTVDBApi.getPeopleDetails(467845, params("value", "QueryPeopleDetails")), "getPeopleDetails() with query parameters"), PEOPLE_DETAILS),
                    of(route(() -> theTVDBApi.getPeopleDetails(574101), "getPeopleDetails() with people ID"), PEOPLE_DETAILS),
                    of(route(() -> theTVDBApi.getPeopleDetails(800577, PeopleMeta.TRANSLATIONS), "getPeopleDetails() with people ID and meta"), PEOPLE_DETAILS),
                    of(route(() -> theTVDBApi.getPeopleTranslation(471160, "por"), "getPeopleTranslation()"), TRANSLATION),
                    of(route(() -> theTVDBApi.getSearchResults(params(Search.Q, "SearchTerm", "value", "QuerySearch")), "getSearchResults() with query parameters"), SEARCH_OVERVIEW),
                    of(route(() -> theTVDBApi.search("SearchTermOnly"), "search() with search term only"), SEARCH_OVERVIEW),
                    of(route(() -> theTVDBApi.search("SearchTermAndType", SearchType.SERIES), "search() with search term and type"), SEARCH_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllSeasons(params("value", "QuerySeasons")), "getAllSeasons() with query parameters"), SEASON_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllSeasons(5), "getAllSeasons() with page"), SEASON_OVERVIEW),
                    of(route(() -> theTVDBApi.getSeason(34167), "getSeason()"), SEASON),
                    of(route(() -> theTVDBApi.getSeasonDetails(68444, params("value", "QuerySeasonDetails")), "getSeasonDetails() with query parameters"), SEASON_DETAILS),
                    of(route(() -> theTVDBApi.getSeasonDetails(69761), "getSeasonDetails() with season ID"), SEASON_DETAILS),
                    of(route(() -> theTVDBApi.getSeasonDetails(35047, SeasonMeta.TRANSLATIONS), "getSeasonDetails() with season ID and meta"), SEASON_DETAILS),
                    of(route(() -> theTVDBApi.getSeasonTypes(), "getSeasonTypes()"), SEASONTYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getSeasonTranslation(27478, "eng"), "getSeasonTranslation()"), TRANSLATION),
                    of(route(() -> theTVDBApi.getAllSeriesStatuses(), "getAllSeriesStatuses()"), STATUS_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllSeries(params("value", "QuerySeries")), "getAllSeries() with query parameters"), SERIES_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllSeries(2), "getAllSeries() with page"), SERIES_OVERVIEW),
                    of(route(() -> theTVDBApi.getSeries(2845), "getSeries()"), SERIES),
                    of(route(() -> theTVDBApi.getSeriesArtworks(9342, params("value", "QuerySeriesArtworks")), "getSeriesArtworks() with query parameters"), SERIES_DETAILS),
                    of(route(() -> theTVDBApi.getSeriesArtworks(8751, "rus"), "getSeriesArtworks() with series ID and language"), SERIES_DETAILS),
                    of(route(() -> theTVDBApi.getSeriesDetails(4577, params("value", "QuerySeriesDetails")), "getSeriesDetails() with query parameters"), SERIES_DETAILS),
                    of(route(() -> theTVDBApi.getSeriesDetails(9041), "getSeriesDetails() with series ID"), SERIES_DETAILS),
                    of(route(() -> theTVDBApi.getSeriesDetails(1005, SeriesMeta.EPISODES), "getSeriesDetails() with series ID and meta"), SERIES_DETAILS),
                    of(route(() -> theTVDBApi.getSeriesEpisodes(3789, DEFAULT), "getSeriesEpisodes()"), SERIESEPISODES),
                    of(route(() -> theTVDBApi.getSeriesEpisodes(7000, ALTERNATE, params("value", "QuerySeriesEpisodes")), "getSeriesEpisodes() with query parameters"), SERIESEPISODES),
                    of(route(() -> theTVDBApi.getSeriesEpisodes(2147, REGIONAL, 4), "getSeriesEpisodes() with season number"), SERIESEPISODES),
                    of(route(() -> theTVDBApi.getSeriesEpisodesTranslated(5481, DVD, "eng"), "getSeriesEpisodesTranslated()"), SERIESEPISODES_TRANSLATED),
                    of(route(() -> theTVDBApi.getSeriesEpisodesTranslated(6974, DEFAULT, "deu", params("value", "QuerySeriesEpisodesTranslated")), "getSeriesEpisodesTranslated() with query parameters"), SERIESEPISODES_TRANSLATED),
                    of(route(() -> theTVDBApi.getSeriesTranslation(6004, "eng"), "getSeriesTranslation()"), TRANSLATION),
                    of(route(() -> theTVDBApi.getAllSourceTypes(), "getAllSourceTypes()"), SOURCETYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getUpdates(params(Updates.SINCE, "16247601", "value", "QueryUpdates")), "getUpdates() with query parameters"), UPDATE_OVERVIEW),
                    of(route(() -> theTVDBApi.getUpdates(16236514, 3), "getUpdates() with Epoch time and page"), UPDATE_OVERVIEW),
                    of(route(() -> theTVDBApi.getUpdates(16239876, TRANSLATED_EPISODES, CREATE, 2), "getUpdates() with Epoch time, type, action and page"), UPDATE_OVERVIEW)
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

        private TheTVDBApi.JSON theTVDBApi;

        @BeforeAll
        void setUpAPI(Proxy remoteAPI) throws Exception {
            theTVDBApi = init(new TheTVDBApiImpl(CONTRACT_APIKEY, remoteAPI)).json();
        }

        //@DisableFormatting
        @BeforeAll
        void setUpRoutes(MockServerClient client) throws Exception {
            client.when(request("/artwork/statuses", GET)).respond(jsonResponse(ARTWORKSTATUS_OVERVIEW));
            client.when(request("/artwork/types", GET)).respond(jsonResponse(ARTWORKTYPE_OVERVIEW));
            client.when(request("/artwork/6701", GET)).respond(jsonResponse(ARTWORK));
            client.when(request("/artwork/9100/extended", GET)).respond(jsonResponse(ARTWORK_DETAILS));
            client.when(request("/awards", GET)).respond(jsonResponse(AWARD_OVERVIEW));
            client.when(request("/awards/5744", GET)).respond(jsonResponse(AWARD));
            client.when(request("/awards/3697/extended", GET)).respond(jsonResponse(AWARD_DETAILS));
            client.when(request("/awards/categories/411", GET)).respond(jsonResponse(AWARDCATEGORY));
            client.when(request("/awards/categories/623/extended", GET)).respond(jsonResponse(AWARDCATEGORY_DETAILS));
            client.when(request("/characters/94347", GET)).respond(jsonResponse(CHARACTER));
            client.when(request("/companies", GET, param("value", "QueryCompaniesJson"))).respond(jsonResponse(COMPANY_OVERVIEW));
            client.when(request("/companies/types", GET)).respond(jsonResponse(COMPANYTYPE_OVERVIEW));
            client.when(request("/companies/117", GET)).respond(jsonResponse(COMPANY));
            client.when(request("/content/ratings", GET)).respond(jsonResponse(CONTENTRATING_OVERVIEW));
            client.when(request("/entities", GET)).respond(jsonResponse(ENTITYTYPE_OVERVIEW));
            client.when(request("/episodes/640796", GET)).respond(jsonResponse(EPISODE));
            client.when(request("/episodes/872404/extended", GET, param("value", "QueryEpisodeDetailsJson"))).respond(jsonResponse(EPISODE_DETAILS));
            client.when(request("/episodes/379461/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/lists/2400/translations/fra", GET)).respond(jsonResponse(TRANSLATIONS));
            client.when(request("/lists", GET, param("value", "QueryListsJson"))).respond(jsonResponse(LIST_OVERVIEW));
            client.when(request("/lists/1151", GET)).respond(jsonResponse(LIST));
            client.when(request("/lists/3463/extended", GET)).respond(jsonResponse(LIST_DETAILS));
            client.when(request("/genders", GET)).respond(jsonResponse(GENDER_OVERVIEW));
            client.when(request("/genres", GET)).respond(jsonResponse(GENRE_OVERVIEW));
            client.when(request("/genres/21", GET)).respond(jsonResponse(GENRE));
            client.when(request("/inspiration/types", GET)).respond(jsonResponse(INSPIRATIONTYPE_OVERVIEW));
            client.when(request("/movies/statuses", GET)).respond(jsonResponse(STATUS_OVERVIEW));
            client.when(request("/movies", GET, param("value", "QueryMoviesJson"))).respond(jsonResponse(MOVIE_OVERVIEW));
            client.when(request("/movies/61714", GET)).respond(jsonResponse(MOVIE));
            client.when(request("/movies/54801/extended", GET, param("value", "QueryMovieDetailsJson"))).respond(jsonResponse(MOVIE_DETAILS));
            client.when(request("/movies/74810/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/people/types", GET)).respond(jsonResponse(PEOPLETYPE_OVERVIEW));
            client.when(request("/people/3647", GET)).respond(jsonResponse(PEOPLE));
            client.when(request("/people/6904/extended", GET, param("value", "QueryPeopleDetailsJson"))).respond(jsonResponse(PEOPLE_DETAILS));
            client.when(request("/people/1504/translations/por", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/search", GET, param(Search.Q, "SearchTermJson"), param("value", "QuerySearchJson"))).respond(jsonResponse(SEARCH_OVERVIEW));
            client.when(request("/seasons", GET, param("value", "QuerySeasonsJson"))).respond(jsonResponse(SEASON_OVERVIEW));
            client.when(request("/seasons/18322", GET)).respond(jsonResponse(SEASON));
            client.when(request("/seasons/48874/extended", GET, param("value", "QuerySeasonDetailsJson"))).respond(jsonResponse(SEASON_DETAILS));
            client.when(request("/seasons/types", GET)).respond(jsonResponse(SEASONTYPE_OVERVIEW));
            client.when(request("/seasons/67446/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/series/statuses", GET)).respond(jsonResponse(STATUS_OVERVIEW));
            client.when(request("/series", GET, param("value", "QuerySeriesJson"))).respond(jsonResponse(SERIES_OVERVIEW));
            client.when(request("/series/5003", GET)).respond(jsonResponse(SERIES));
            client.when(request("/series/3661/artworks", GET, param("value", "QuerySeriesArtworksJson"))).respond(jsonResponse(SERIES_DETAILS));
            client.when(request("/series/5842/extended", GET, param("value", "QuerySeriesDetailsJson"))).respond(jsonResponse(SERIES_DETAILS));
            client.when(request("/series/98043/episodes/official", GET, param("value", "QuerySeriesEpisodesJson"))).respond(jsonResponse(SERIESEPISODES));
            client.when(request("/series/65660/episodes/regional/spa", GET, param("value", "QuerySeriesEpisodesTranslatedJson"))).respond(jsonResponse(SERIESEPISODES_TRANSLATED));
            client.when(request("/series/8024/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/sources/types", GET)).respond(jsonResponse(SOURCETYPE_OVERVIEW));
            client.when(request("/updates", GET, param(Updates.SINCE, "16258740"), param("value", "QueryUpdatesJson"))).respond(jsonResponse(UPDATE_OVERVIEW));
        }

        private Stream<Arguments> withValidParameters() {
            return Stream.of(
                    of(route(() -> theTVDBApi.getAllArtworkStatuses(), "getAllArtworkStatuses()"), ARTWORKSTATUS_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllArtworkTypes(), "getAllArtworkTypes()"), ARTWORKTYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getArtwork(6701), "getArtwork()"), ARTWORK),
                    of(route(() -> theTVDBApi.getArtworkDetails(9100), "getArtworkDetails()"), ARTWORK_DETAILS),
                    of(route(() -> theTVDBApi.getAllAwards(), "getAllAwards()"), AWARD_OVERVIEW),
                    of(route(() -> theTVDBApi.getAward(5744), "getAward()"), AWARD),
                    of(route(() -> theTVDBApi.getAwardDetails(3697), "getAwardDetails()"), AWARD_DETAILS),
                    of(route(() -> theTVDBApi.getAwardCategory(411), "getAwardCategory()"), AWARDCATEGORY),
                    of(route(() -> theTVDBApi.getAwardCategoryDetails(623), "getAwardCategoryDetails()"), AWARDCATEGORY_DETAILS),
                    of(route(() -> theTVDBApi.getCharacter(94347), "getCharacter()"), CHARACTER),
                    of(route(() -> theTVDBApi.getAllCompanies(params("value", "QueryCompaniesJson")), "getAllCompanies() with query parameters"), COMPANY_OVERVIEW),
                    of(route(() -> theTVDBApi.getCompany(117), "getCompany()"), COMPANY),
                    of(route(() -> theTVDBApi.getCompanyTypes(), "getCompanyTypes()"), COMPANYTYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllContentRatings(), "getAllContentRatings()"), CONTENTRATING_OVERVIEW),
                    of(route(() -> theTVDBApi.getEntityTypes(), "getEntityTypes()"), ENTITYTYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getEpisode(640796), "getEpisode()"), EPISODE),
                    of(route(() -> theTVDBApi.getEpisodeDetails(872404, params("value", "QueryEpisodeDetailsJson")), "getEpisodeDetails() with query parameters"), EPISODE_DETAILS),
                    of(route(() -> theTVDBApi.getEpisodeTranslation(379461, "eng"), "getEpisodeTranslation()"), TRANSLATION),
                    of(route(() -> theTVDBApi.getListTranslation(2400, "fra"), "getListTranslation()"), TRANSLATIONS),
                    of(route(() -> theTVDBApi.getAllLists(params("value", "QueryListsJson")), "getAllLists() with query parameters"), LIST_OVERVIEW),
                    of(route(() -> theTVDBApi.getList(1151), "getList()"), LIST),
                    of(route(() -> theTVDBApi.getListDetails(3463), "getListDetails()"), LIST_DETAILS),
                    of(route(() -> theTVDBApi.getAllGenders(), "getAllGenders()"), GENDER_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllGenres(), "getAllGenres()"), GENRE_OVERVIEW),
                    of(route(() -> theTVDBApi.getGenre(21), "getGenre()"), GENRE),
                    of(route(() -> theTVDBApi.getAllInspirationTypes(), "getAllInspirationTypes()"), INSPIRATIONTYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllMovieStatuses(), "getAllMovieStatuses()"), STATUS_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllMovies(params("value", "QueryMoviesJson")), "getAllMovies() with query parameters"), MOVIE_OVERVIEW),
                    of(route(() -> theTVDBApi.getMovie(61714), "getMovie()"), MOVIE),
                    of(route(() -> theTVDBApi.getMovieDetails(54801, params("value", "QueryMovieDetailsJson")), "getMovieDetails() with query parameters"), MOVIE_DETAILS),
                    of(route(() -> theTVDBApi.getMovieTranslation(74810, "eng"), "getMovieTranslation()"), TRANSLATION),
                    of(route(() -> theTVDBApi.getAllPeopleTypes(), "getAllPeopleTypes()"), PEOPLETYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getPeople(3647), "getPeople()"), PEOPLE),
                    of(route(() -> theTVDBApi.getPeopleDetails(6904, params("value", "QueryPeopleDetailsJson")), "getPeopleDetails() with query parameters"), PEOPLE_DETAILS),
                    of(route(() -> theTVDBApi.getPeopleTranslation(1504, "por"), "getPeopleTranslation()"), TRANSLATION),
                    of(route(() -> theTVDBApi.getSearchResults(params(Search.Q, "SearchTermJson", "value", "QuerySearchJson")), "getSearchResults() with query parameters"), SEARCH_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllSeasons(params("value", "QuerySeasonsJson")), "getAllSeasons() with query parameters"), SEASON_OVERVIEW),
                    of(route(() -> theTVDBApi.getSeason(18322), "getSeason()"), SEASON),
                    of(route(() -> theTVDBApi.getSeasonDetails(48874, params("value", "QuerySeasonDetailsJson")), "getSeasonDetails() with query parameters"), SEASON_DETAILS),
                    of(route(() -> theTVDBApi.getSeasonTypes(), "getSeasonTypes()"), SEASONTYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getSeasonTranslation(67446, "eng"), "getSeasonTranslation()"), TRANSLATION),
                    of(route(() -> theTVDBApi.getAllSeriesStatuses(), "getAllSeriesStatuses()"), STATUS_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllSeries(params("value", "QuerySeriesJson")), "getAllSeries() with query parameters"), SERIES_OVERVIEW),
                    of(route(() -> theTVDBApi.getSeries(5003), "getSeries()"), SERIES),
                    of(route(() -> theTVDBApi.getSeriesArtworks(3661, params("value", "QuerySeriesArtworksJson")), "getSeriesArtworks() with query parameters"), SERIES_DETAILS),
                    of(route(() -> theTVDBApi.getSeriesDetails(5842, params("value", "QuerySeriesDetailsJson")), "getSeriesDetails() with query parameters"), SERIES_DETAILS),
                    of(route(() -> theTVDBApi.getSeriesEpisodes(98043, OFFICIAL, params("value", "QuerySeriesEpisodesJson")), "getSeriesEpisodes() with query parameters"), SERIESEPISODES),
                    of(route(() -> theTVDBApi.getSeriesEpisodesTranslated(65660, REGIONAL, "spa", params("value", "QuerySeriesEpisodesTranslatedJson")), "getSeriesEpisodesTranslated() with query parameters"), SERIESEPISODES_TRANSLATED),
                    of(route(() -> theTVDBApi.getSeriesTranslation(8024, "eng"), "getSeriesTranslation()"), TRANSLATION),
                    of(route(() -> theTVDBApi.getAllSourceTypes(), "getAllSourceTypes()"), SOURCETYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getUpdates(params(Updates.SINCE, "16258740", "value", "QueryUpdatesJson")), "getUpdates() with query parameters"), UPDATE_OVERVIEW)
            );
        }
        //@EnableFormatting

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

        private TheTVDBApi.Extended theTVDBApi;

        @BeforeAll
        void setUpAPI(Proxy remoteAPI) throws Exception {
            theTVDBApi = init(new TheTVDBApiImpl(CONTRACT_APIKEY, remoteAPI)).extended();
        }

        //@DisableFormatting
        @BeforeAll
        void setUpRoutes(MockServerClient client) throws Exception {
            client.when(request("/artwork/statuses", GET)).respond(jsonResponse(ARTWORKSTATUS_OVERVIEW));
            client.when(request("/artwork/types", GET)).respond(jsonResponse(ARTWORKTYPE_OVERVIEW));
            client.when(request("/artwork/7099", GET)).respond(jsonResponse(ARTWORK));
            client.when(request("/artwork/6471/extended", GET)).respond(jsonResponse(ARTWORK_DETAILS));
            client.when(request("/awards", GET)).respond(jsonResponse(AWARD_OVERVIEW));
            client.when(request("/awards/3004", GET)).respond(jsonResponse(AWARD));
            client.when(request("/awards/5973/extended", GET)).respond(jsonResponse(AWARD_DETAILS));
            client.when(request("/awards/categories/355", GET)).respond(jsonResponse(AWARDCATEGORY));
            client.when(request("/awards/categories/495/extended", GET)).respond(jsonResponse(AWARDCATEGORY_DETAILS));
            client.when(request("/characters/66470", GET)).respond(jsonResponse(CHARACTER));
            client.when(request("/companies", GET, param("value", "QueryCompaniesExtended"))).respond(jsonResponse(COMPANY_OVERVIEW));
            client.when(request("/companies/types", GET)).respond(jsonResponse(COMPANYTYPE_OVERVIEW));
            client.when(request("/companies/64", GET)).respond(jsonResponse(COMPANY));
            client.when(request("/content/ratings", GET)).respond(jsonResponse(CONTENTRATING_OVERVIEW));
            client.when(request("/entities", GET)).respond(jsonResponse(ENTITYTYPE_OVERVIEW));
            client.when(request("/episodes/30619", GET)).respond(jsonResponse(EPISODE));
            client.when(request("/episodes/47149/extended", GET, param("value", "QueryEpisodeDetailsExtended"))).respond(jsonResponse(EPISODE_DETAILS));
            client.when(request("/episodes/34771/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/lists/64114/translations/fra", GET)).respond(jsonResponse(TRANSLATIONS));
            client.when(request("/lists", GET, param("value", "QueryListsExtended"))).respond(jsonResponse(LIST_OVERVIEW));
            client.when(request("/lists/4641", GET)).respond(jsonResponse(LIST));
            client.when(request("/lists/3169/extended", GET)).respond(jsonResponse(LIST_DETAILS));
            client.when(request("/genders", GET)).respond(jsonResponse(GENDER_OVERVIEW));
            client.when(request("/genres", GET)).respond(jsonResponse(GENRE_OVERVIEW));
            client.when(request("/genres/35", GET)).respond(jsonResponse(GENRE));
            client.when(request("/inspiration/types", GET)).respond(jsonResponse(INSPIRATIONTYPE_OVERVIEW));
            client.when(request("/movies/statuses", GET)).respond(jsonResponse(STATUS_OVERVIEW));
            client.when(request("/movies", GET, param("value", "QueryMoviesExtended"))).respond(jsonResponse(MOVIE_OVERVIEW));
            client.when(request("/movies/90034", GET)).respond(jsonResponse(MOVIE));
            client.when(request("/movies/31101/extended", GET, param("value", "QueryMovieDetailsExtended"))).respond(jsonResponse(MOVIE_DETAILS));
            client.when(request("/movies/46011/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/people/types", GET)).respond(jsonResponse(PEOPLETYPE_OVERVIEW));
            client.when(request("/people/9891", GET)).respond(jsonResponse(PEOPLE));
            client.when(request("/people/1067/extended", GET, param("value", "QueryPeopleDetailsExtended"))).respond(jsonResponse(PEOPLE_DETAILS));
            client.when(request("/people/8733/translations/por", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/search", GET, param(Search.Q, "SearchTermExtended"), param("value", "QuerySearchExtended"))).respond(jsonResponse(SEARCH_OVERVIEW));
            client.when(request("/seasons", GET, param("value", "QuerySeasonsExtended"))).respond(jsonResponse(SEASON_OVERVIEW));
            client.when(request("/seasons/52270", GET)).respond(jsonResponse(SEASON));
            client.when(request("/seasons/69714/extended", GET, param("value", "QuerySeasonDetailsExtended"))).respond(jsonResponse(SEASON_DETAILS));
            client.when(request("/seasons/types", GET)).respond(jsonResponse(SEASONTYPE_OVERVIEW));
            client.when(request("/seasons/64714/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/series/statuses", GET)).respond(jsonResponse(STATUS_OVERVIEW));
            client.when(request("/series", GET, param("value", "QuerySeriesExtended"))).respond(jsonResponse(SERIES_OVERVIEW));
            client.when(request("/series/8131", GET)).respond(jsonResponse(SERIES));
            client.when(request("/series/4713/artworks", GET, param("value", "QuerySeriesArtworksExtended"))).respond(jsonResponse(SERIES_DETAILS));
            client.when(request("/series/5444/extended", GET, param("value", "QuerySeriesDetailsExtended"))).respond(jsonResponse(SERIES_DETAILS));
            client.when(request("/series/5711/episodes/dvd", GET, param("value", "QuerySeriesEpisodesExtended"))).respond(jsonResponse(SERIESEPISODES));
            client.when(request("/series/2312/episodes/alternate/por", GET, param("value", "QuerySeriesEpisodesTranslatedExtended"))).respond(jsonResponse(SERIESEPISODES_TRANSLATED));
            client.when(request("/series/6170/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
            client.when(request("/sources/types", GET)).respond(jsonResponse(SOURCETYPE_OVERVIEW));
            client.when(request("/updates", GET, param(Updates.SINCE, "16245743"), param("value", "QueryUpdatesExtended"))).respond(jsonResponse(UPDATE_OVERVIEW));
        }

        private Stream<Arguments> withValidParameters() {
            return Stream.of(
                    of(route(() -> theTVDBApi.getAllArtworkStatuses(), "getAllArtworkStatuses()"), ARTWORKSTATUS_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllArtworkTypes(), "getAllArtworkTypes()"), ARTWORKTYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getArtwork(7099), "getArtwork()"), ARTWORK),
                    of(route(() -> theTVDBApi.getArtworkDetails(6471), "getArtworkDetails()"), ARTWORK_DETAILS),
                    of(route(() -> theTVDBApi.getAllAwards(), "getAllAwards()"), AWARD_OVERVIEW),
                    of(route(() -> theTVDBApi.getAward(3004), "getAward()"), AWARD),
                    of(route(() -> theTVDBApi.getAwardDetails(5973), "getAwardDetails()"), AWARD_DETAILS),
                    of(route(() -> theTVDBApi.getAwardCategory(355), "getAwardCategory()"), AWARDCATEGORY),
                    of(route(() -> theTVDBApi.getAwardCategoryDetails(495), "getAwardCategoryDetails()"), AWARDCATEGORY_DETAILS),
                    of(route(() -> theTVDBApi.getCharacter(66470), "getCharacter()"), CHARACTER),
                    of(route(() -> theTVDBApi.getAllCompanies(params("value", "QueryCompaniesExtended")), "getAllCompanies() with query parameters"), COMPANY_OVERVIEW),
                    of(route(() -> theTVDBApi.getCompany(64), "getCompany()"), COMPANY),
                    of(route(() -> theTVDBApi.getCompanyTypes(), "getCompanyTypes()"), COMPANYTYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllContentRatings(), "getAllContentRatings()"), CONTENTRATING_OVERVIEW),
                    of(route(() -> theTVDBApi.getEntityTypes(), "getEntityTypes()"), ENTITYTYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getEpisode(30619), "getEpisode()"), EPISODE),
                    of(route(() -> theTVDBApi.getEpisodeDetails(47149, params("value", "QueryEpisodeDetailsExtended")), "getEpisodeDetails() with query parameters"), EPISODE_DETAILS),
                    of(route(() -> theTVDBApi.getEpisodeTranslation(34771, "eng"), "getEpisodeTranslation()"), TRANSLATION),
                    of(route(() -> theTVDBApi.getListTranslation(64114, "fra"), "getListTranslation()"), TRANSLATIONS),
                    of(route(() -> theTVDBApi.getAllLists(params("value", "QueryListsExtended")), "getAllLists() with query parameters"), LIST_OVERVIEW),
                    of(route(() -> theTVDBApi.getList(4641), "getList()"), LIST),
                    of(route(() -> theTVDBApi.getListDetails(3169), "getListDetails()"), LIST_DETAILS),
                    of(route(() -> theTVDBApi.getAllGenders(), "getAllGenders()"), GENDER_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllGenres(), "getAllGenres()"), GENRE_OVERVIEW),
                    of(route(() -> theTVDBApi.getGenre(35), "getGenre()"), GENRE),
                    of(route(() -> theTVDBApi.getAllInspirationTypes(), "getAllInspirationTypes()"), INSPIRATIONTYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllMovieStatuses(), "getAllMovieStatuses()"), STATUS_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllMovies(params("value", "QueryMoviesExtended")), "getAllMovies() with query parameters"), MOVIE_OVERVIEW),
                    of(route(() -> theTVDBApi.getMovie(90034), "getMovie()"), MOVIE),
                    of(route(() -> theTVDBApi.getMovieDetails(31101, params("value", "QueryMovieDetailsExtended")), "getMovieDetails() with query parameters"), MOVIE_DETAILS),
                    of(route(() -> theTVDBApi.getMovieTranslation(46011, "eng"), "getMovieTranslation()"), TRANSLATION),
                    of(route(() -> theTVDBApi.getAllPeopleTypes(), "getAllPeopleTypes()"), PEOPLETYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getPeople(9891), "getPeople()"), PEOPLE),
                    of(route(() -> theTVDBApi.getPeopleDetails(1067, params("value", "QueryPeopleDetailsExtended")), "getPeopleDetails() with query parameters"), PEOPLE_DETAILS),
                    of(route(() -> theTVDBApi.getPeopleTranslation(8733, "por"), "getPeopleTranslation()"), TRANSLATION),
                    of(route(() -> theTVDBApi.getSearchResults(params(Search.Q, "SearchTermExtended", "value", "QuerySearchExtended")), "getSearchResults() with query parameters"), SEARCH_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllSeasons(params("value", "QuerySeasonsExtended")), "getAllSeasons() with query parameters"), SEASON_OVERVIEW),
                    of(route(() -> theTVDBApi.getSeason(52270), "getSeason()"), SEASON),
                    of(route(() -> theTVDBApi.getSeasonDetails(69714, params("value", "QuerySeasonDetailsExtended")), "getSeasonDetails() with query parameters"), SEASON_DETAILS),
                    of(route(() -> theTVDBApi.getSeasonTypes(), "getSeasonTypes()"), SEASONTYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getSeasonTranslation(64714, "eng"), "getSeasonTranslation()"), TRANSLATION),
                    of(route(() -> theTVDBApi.getAllSeriesStatuses(), "getAllSeriesStatuses()"), STATUS_OVERVIEW),
                    of(route(() -> theTVDBApi.getAllSeries(params("value", "QuerySeriesExtended")), "getAllSeries() with query parameters"), SERIES_OVERVIEW),
                    of(route(() -> theTVDBApi.getSeries(8131), "getSeries()"), SERIES),
                    of(route(() -> theTVDBApi.getSeriesArtworks(4713, params("value", "QuerySeriesArtworksExtended")), "getSeriesArtworks() with query parameters"), SERIES_DETAILS),
                    of(route(() -> theTVDBApi.getSeriesDetails(5444, params("value", "QuerySeriesDetailsExtended")), "getSeriesDetails() with query parameters"), SERIES_DETAILS),
                    of(route(() -> theTVDBApi.getSeriesEpisodes(5711, DVD, params("value", "QuerySeriesEpisodesExtended")), "getSeriesEpisodes() with query parameters"), SERIESEPISODES),
                    of(route(() -> theTVDBApi.getSeriesEpisodesTranslated(2312, ALTERNATE, "por", params("value", "QuerySeriesEpisodesTranslatedExtended")), "getSeriesEpisodesTranslated() with query parameters"), SERIESEPISODES_TRANSLATED),
                    of(route(() -> theTVDBApi.getSeriesTranslation(6170, "eng"), "getSeriesTranslation()"), TRANSLATION),
                    of(route(() -> theTVDBApi.getAllSourceTypes(), "getAllSourceTypes()"), SOURCETYPE_OVERVIEW),
                    of(route(() -> theTVDBApi.getUpdates(params(Updates.SINCE, "16245743", "value", "QueryUpdatesExtended")), "getUpdates() with query parameters"), UPDATE_OVERVIEW)
            );
        }
        //@EnableFormatting

        @ParameterizedTest(name = "[{index}] Route TheTVDBApi.{0} successfully invoked")
        @MethodSource("withValidParameters")
        <T> void invokeRoute_withValidParameters_verifyResponse(TestTheTVDBAPICall<T> route, Object expected,
                MockServerClient client) throws Exception {
            TestTheTVDBAPICallAssert.assertThat(route).usingMockServer(client).matchesExpectation(expected);
        }
    }
}

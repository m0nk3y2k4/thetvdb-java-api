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

package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series.AIR_DATE;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series.EPISODE_NUMBER;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series.SEASON;
import static com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesSeasonType.ABSOLUTE;
import static com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesSeasonType.ALTERNATE;
import static com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesSeasonType.DEFAULT;
import static com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesSeasonType.DVD;
import static com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesSeasonType.OFFICIAL;
import static com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesSeasonType.REGIONAL;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getAllSeries;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getAllSeriesStatuses;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getSeriesBase;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getSeriesEpisodes;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getSeriesEpisodesTranslated;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getSeriesExtended;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getSeriesTranslation;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.params;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIES;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIESEPISODES;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIES_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIES_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.STATUS_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.TRANSLATION;
import static com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestRemoteAPICall.route;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockserver.model.Parameter.param;

import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI;
import com.github.m0nk3y2k4.thetvdb.testutils.ResponseData;
import com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter.WithHttpsMockServer;
import com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestRemoteAPICall;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockserver.client.MockServerClient;

@WithHttpsMockServer
class SeriesAPITest {

    //@DisableFormatting
    @BeforeAll
    static void setUpRoutes(MockServerClient client) throws Exception {
        client.when(request("/series/statuses", GET)).respond(jsonResponse(STATUS_OVERVIEW));
        client.when(request("/series", GET)).respond(jsonResponse(SERIES_OVERVIEW));
        client.when(request("/series", GET, param("page", "3"))).respond(jsonResponse(SERIES_OVERVIEW));
        client.when(request("/series/100348", GET)).respond(jsonResponse(SERIES));
        client.when(request("/series/49710/extended", GET)).respond(jsonResponse(SERIES_DETAILS));
        client.when(request("/series/34879/extended", GET, param("meta", "episodes"))).respond(jsonResponse(SERIES_DETAILS));
        client.when(request("/series/58709/episodes/official", GET)).respond(jsonResponse(SERIESEPISODES));
        client.when(request("/series/69742/episodes/regional", GET, param(AIR_DATE, "1998-06-19"))).respond(jsonResponse(SERIESEPISODES));
        client.when(request("/series/89414/episodes/alternate", GET, param(SEASON, "2"), param(EPISODE_NUMBER, "8"))).respond(jsonResponse(SERIESEPISODES));
        client.when(request("/series/70204/episodes/default/nld", GET)).respond(jsonResponse(SERIES_DETAILS));
        client.when(request("/series/56347/episodes/regional/fra", GET, param("page", "9"))).respond(jsonResponse(SERIES_DETAILS));
        client.when(request("/series/69423/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
    }

    @SuppressWarnings("ConstantConditions")
    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                of(route(con -> getSeriesBase(con, 0), "getSeriesBase() with ZERO series ID")),
                of(route(con -> getSeriesBase(con, -2), "getSeriesBase() with negative series ID")),
                of(route(con -> getSeriesExtended(con, 0, null), "getSeriesExtended() with ZERO series ID")),
                of(route(con -> getSeriesExtended(con, -9, null), "getSeriesExtended() with negative series ID")),
                of(route(con -> getSeriesEpisodes(con, 0, ABSOLUTE, null), "getSeriesEpisodes() with ZERO series ID")),
                of(route(con -> getSeriesEpisodes(con, -4, REGIONAL, null), "getSeriesEpisodes() with negative series ID")),
                of(route(con -> getSeriesEpisodes(con, 548, null, null), "getSeriesEpisodes() without season-type")),
                of(route(con -> getSeriesEpisodes(con, 594, ALTERNATE, params(AIR_DATE, "21-10-19")), "getSeriesEpisodes() with invalid query parameters")),
                of(route(con -> getSeriesEpisodes(con, 612, DVD, params(EPISODE_NUMBER, "3")), "getSeriesEpisodes() with missing conditional query parameters")),
                of(route(con -> getSeriesEpisodesTranslated(con, 0, ALTERNATE, "eng", null), "getSeriesEpisodesTranslated() with ZERO series ID")),
                of(route(con -> getSeriesEpisodesTranslated(con, -6, DEFAULT, "por", null), "getSeriesEpisodesTranslated() with negative series ID")),
                of(route(con -> getSeriesEpisodesTranslated(con, 487, null, "spa", null), "getSeriesEpisodesTranslated() without season-type")),
                of(route(con -> getSeriesEpisodesTranslated(con, 559, DVD, "f", null), "getSeriesEpisodesTranslated() with invalid language code (1)")),
                of(route(con -> getSeriesEpisodesTranslated(con, 781, DVD, "ital", null), "getSeriesEpisodesTranslated() with invalid language code (2)")),
                of(route(con -> getSeriesTranslation(con, 0, "eng"), "getSeriesTranslation() with ZERO series ID")),
                of(route(con -> getSeriesTranslation(con, -5, "deu"), "getSeriesTranslation() with negative series ID")),
                of(route(con -> getSeriesTranslation(con, 785, "e"), "getSeriesTranslation() with invalid language code (1)")),
                of(route(con -> getSeriesTranslation(con, 6130, "span"), "getSeriesTranslation() with invalid language code (2)"))
        );
    }

    @SuppressWarnings("Convert2MethodRef")
    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                of(route(con -> getAllSeriesStatuses(con), "getAllSeriesStatuses()"), STATUS_OVERVIEW),
                of(route(con -> getAllSeries(con, null), "getAllSeries() without query parameters"), SERIES_OVERVIEW),
                of(route(con -> getAllSeries(con, params("page", "3")), "getAllSeries() with query parameters"), SERIES_OVERVIEW),
                of(route(con -> getSeriesBase(con, 100348), "getSeriesBase()"), SERIES),
                of(route(con -> getSeriesExtended(con, 49710, null), "getSeriesExtended() without query parameters"), SERIES_DETAILS),
                of(route(con -> getSeriesExtended(con, 34879, params("meta", "episodes")), "getSeriesExtended() with query parameters"), SERIES_DETAILS),
                of(route(con -> getSeriesEpisodes(con, 58709, OFFICIAL, null), "getSeriesEpisodes() without query parameters"), SERIESEPISODES),
                of(route(con -> getSeriesEpisodes(con, 69742, REGIONAL, params(AIR_DATE, "1998-06-19")), "getSeriesEpisodes() with query parameters"), SERIESEPISODES),
                of(route(con -> getSeriesEpisodes(con, 89414, ALTERNATE, params(SEASON, "2", EPISODE_NUMBER, "8")), "getSeriesEpisodes() with conditional query parameters"), SERIESEPISODES),
                of(route(con -> getSeriesEpisodesTranslated(con, 70204, DEFAULT, "nld", null), "getSeriesEpisodesTranslated() without query parameters"), SERIES_DETAILS),
                of(route(con -> getSeriesEpisodesTranslated(con, 56347, REGIONAL, "fra", params("page", "9")), "getSeriesEpisodesTranslated() with query parameters"), SERIES_DETAILS),
                of(route(con -> getSeriesTranslation(con, 69423, "eng"), "getSeriesTranslation()"), TRANSLATION)
        );
    }
    //@EnableFormatting

    @ParameterizedTest(name = "[{index}] Route SeriesAPI.{0} rejected")
    @MethodSource("withInvalidParameters")
    void invokeRoute_withInvalidParameters_verifyParameterValidation(TestRemoteAPICall route, RemoteAPI remoteAPI) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI)));
    }

    @ParameterizedTest(name = "[{index}] Route SeriesAPI.{0} successfully invoked")
    @MethodSource("withValidParameters")
    <T> void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, ResponseData<T> expected,
            RemoteAPI remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI))).isEqualTo(expected.getJson());
    }
}

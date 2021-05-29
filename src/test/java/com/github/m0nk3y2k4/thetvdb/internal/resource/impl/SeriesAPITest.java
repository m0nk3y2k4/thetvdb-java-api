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

package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getAllSeries;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getAllSeriesStatuses;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getSeriesBase;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getSeriesExtended;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getSeriesTranslation;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.params;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SERIES;
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
        client.when(request("/series", GET, param("page", "3"))).respond(jsonResponse(SERIES_OVERVIEW));
        client.when(request("/series/100348", GET)).respond(jsonResponse(SERIES));
        client.when(request("/series/49710/extended", GET)).respond(jsonResponse(SERIES_DETAILS));
        client.when(request("/series/69423/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
    }

    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                of(route(con -> getSeriesBase(con, 0), "getSeriesBase() with ZERO series ID")),
                of(route(con -> getSeriesBase(con, -2), "getSeriesBase() with negative series ID")),
                of(route(con -> getSeriesExtended(con, 0), "getSeriesExtended() with ZERO series ID")),
                of(route(con -> getSeriesExtended(con, -9), "getSeriesExtended() with negative series ID")),
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
                of(route(con -> getAllSeries(con, params("page", "3")), "getAllSeries()"), SERIES_OVERVIEW),
                of(route(con -> getSeriesBase(con, 100348), "getSeriesBase()"), SERIES),
                of(route(con -> getSeriesExtended(con, 49710), "getSeriesExtended()"), SERIES_DETAILS),
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

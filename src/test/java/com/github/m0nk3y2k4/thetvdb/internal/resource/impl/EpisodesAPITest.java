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

import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.EpisodesAPI.getEpisodeBase;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.EpisodesAPI.getEpisodeExtended;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.EpisodesAPI.getEpisodeTranslation;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.params;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.EPISODE;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.EPISODE_DETAILS;
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
class EpisodesAPITest {

    //@DisableFormatting
    @BeforeAll
    static void setUpRoutes(MockServerClient client) throws Exception {
        client.when(request("/episodes/78457", GET)).respond(jsonResponse(EPISODE));
        client.when(request("/episodes/96470/extended", GET)).respond(jsonResponse(EPISODE_DETAILS));
        client.when(request("/episodes/78403/extended", GET, param("meta", "translations"))).respond(jsonResponse(EPISODE_DETAILS));
        client.when(request("/episodes/67481/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
    }

    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                of(route(con -> getEpisodeBase(con, 0), "getEpisodeBase() with ZERO episode ID")),
                of(route(con -> getEpisodeBase(con, -7), "getEpisodeBase() with negative episode ID")),
                of(route(con -> getEpisodeExtended(con, 0, null), "getEpisodeExtended() with ZERO episode ID")),
                of(route(con -> getEpisodeExtended(con, -3, null), "getEpisodeExtended() with negative episode ID")),
                of(route(con -> getEpisodeTranslation(con, 0, "eng"), "getEpisodeTranslation() with ZERO episode ID")),
                of(route(con -> getEpisodeTranslation(con, -4, "deu"), "getEpisodeTranslation() with negative episode ID")),
                of(route(con -> getEpisodeTranslation(con, 8774, "e"), "getEpisodeTranslation() with invalid language code (1)")),
                of(route(con -> getEpisodeTranslation(con, 9647, "span"), "getEpisodeTranslation() with invalid language code (2)"))
        );
    }

    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                of(route(con -> getEpisodeBase(con, 78457), "getEpisodeBase()"), EPISODE),
                of(route(con -> getEpisodeExtended(con, 96470, null), "getEpisodeExtended() without query parameters"), EPISODE_DETAILS),
                of(route(con -> getEpisodeExtended(con, 78403, params("meta", "translations")), "getEpisodeExtended() with query parameters"), EPISODE_DETAILS),
                of(route(con -> getEpisodeTranslation(con, 67481, "eng"), "getEpisodeTranslation()"), TRANSLATION)
        );
    }
    //@EnableFormatting

    @ParameterizedTest(name = "[{index}] Route EpisodesAPI.{0} rejected")
    @MethodSource("withInvalidParameters")
    void invokeRoute_withInvalidParameters_verifyParameterValidation(TestRemoteAPICall route, RemoteAPI remoteAPI) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI)));
    }

    @ParameterizedTest(name = "[{index}] Route EpisodesAPI.{0} successfully invoked")
    @MethodSource("withValidParameters")
    <T> void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, ResponseData<T> expected,
            RemoteAPI remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI))).isEqualTo(expected.getJson());
    }
}

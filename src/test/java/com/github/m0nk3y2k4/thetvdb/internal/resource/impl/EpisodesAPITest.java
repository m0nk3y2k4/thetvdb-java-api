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

package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.EpisodesAPI.getEpisodeBase;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.EPISODE;
import static com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestRemoteAPICall.route;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.params.provider.Arguments.of;

import java.util.function.Supplier;
import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI;
import com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil;
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
    }

    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                of(route(con -> getEpisodeBase(con, 0), "getEpisodeBase() with ZERO episode ID")),
                of(route(con -> getEpisodeBase(con, -7), "getEpisodeBase() with negative episode ID"))
        );
    }

    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                of(route(con -> getEpisodeBase(con, 78457), "getEpisodeBase()"), EPISODE)
        );
    }
    //@EnableFormatting

    @ParameterizedTest(name = "[{index}] Route EpisodesAPI.{0} rejected")
    @MethodSource("withInvalidParameters")
    void invokeRoute_withInvalidParameters_verifyParameterValidation(TestRemoteAPICall route,
            Supplier<RemoteAPI> remoteAPI) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> route.invoke(new APIConnection("JFI9Z4E6J4Z961", remoteAPI)));
    }

    @ParameterizedTest(name = "[{index}] Route EpisodesAPI.{0} successfully invoked")
    @MethodSource("withValidParameters")
    void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, JSONTestUtil.JsonResource expected,
            Supplier<RemoteAPI> remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection("5Z76W73I10I40T", remoteAPI))).isEqualTo(expected.getJson());
    }
}

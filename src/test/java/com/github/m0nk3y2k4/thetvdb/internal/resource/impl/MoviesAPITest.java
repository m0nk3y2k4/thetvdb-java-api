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

import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.MoviesAPI.getMovieBase;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.MOVIE;
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
class MoviesAPITest {

    //@DisableFormatting
    @BeforeAll
    static void setUpRoutes(MockServerClient client) throws Exception {
        client.when(request("/movies/648730", GET)).respond(jsonResponse(MOVIE));
    }

    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                of(route(con -> getMovieBase(con, 0), "getMovieBase() with ZERO movie ID")),
                of(route(con -> getMovieBase(con, -4), "getMovieBase() with negative movie ID"))
        );
    }

    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                of(route(con -> getMovieBase(con, 648730), "getMovieBase()"), MOVIE)
        );
    }
    //@EnableFormatting

    @ParameterizedTest(name = "[{index}] Route MoviesAPI.{0} rejected")
    @MethodSource("withInvalidParameters")
    void invokeRoute_withInvalidParameters_verifyParameterValidation(TestRemoteAPICall route,
            Supplier<RemoteAPI> remoteAPI) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> route.invoke(new APIConnection("LW30K4SZ74POIU4", remoteAPI)));
    }

    @ParameterizedTest(name = "[{index}] Route MoviesAPI.{0} successfully invoked")
    @MethodSource("withValidParameters")
    void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, JSONTestUtil.JsonResource expected,
            Supplier<RemoteAPI> remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection("W740RD7Z6W47Z3QQ", remoteAPI))).isEqualTo(expected.getJson());
    }
}

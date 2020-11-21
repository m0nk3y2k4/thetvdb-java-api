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

import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Movie.SINCE;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.MoviesAPI.get;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.MoviesAPI.getMovieUpdates;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.params;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.MOVIE;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.MOVIEUPDATES;
import static com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestRemoteAPICall.route;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockserver.model.Parameter.param;

import java.util.function.Supplier;
import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.internal.api.impl.QueryParametersImpl;
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

    //@formatter:off
    @BeforeAll
    static void setUpRoutes(MockServerClient client) throws Exception {
        client.when(request("/movies/64875", GET)).respond(jsonResponse(MOVIE));
        client.when(request("/movieupdates", GET, param(SINCE, "52"))).respond(jsonResponse(MOVIEUPDATES));
    }

    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                of(route(con -> get(con, 0), "get() with ZERO movie ID")),
                of(route(con -> get(con, -3), "get() with negative movie ID")),
                of(route(con -> getMovieUpdates(con, null), "query() without query parameters")),
                of(route(con -> getMovieUpdates(con, new QueryParametersImpl()), "query() with empty query parameters")),
                of(route(con -> getMovieUpdates(con, params("optional", "4")), "query() without mandatory query parameters")),
                of(route(con -> getMovieUpdates(con, params(SINCE, "0")), "query() with ZERO query parameter value")),
                of(route(con -> getMovieUpdates(con, params(SINCE, "-5")), "query() with negative query parameter value")),
                of(route(con -> getMovieUpdates(con, params(SINCE, "yesterday")), "query() with NaN query parameter value"))
        );
    }

    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                of(route(con -> get(con, 64875), "get()"), MOVIE),
                of(route(con -> getMovieUpdates(con, params(SINCE, "52")), "getMovieUpdates()"), MOVIEUPDATES)
        );
    }
    //@formatter:on

    @ParameterizedTest(name = "[{index}] Route MoviesAPI.{0} rejected")
    @MethodSource("withInvalidParameters")
    void invokeRoute_withInvalidParameters_verifyParameterValidation(TestRemoteAPICall route,
            Supplier<RemoteAPI> remoteAPI) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> route.invoke(new APIConnection("U8W84D4QW56R4DS", remoteAPI)));
    }

    @ParameterizedTest(name = "[{index}] Route MoviesAPI.{0} successfully invoked")
    @MethodSource("withValidParameters")
    void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, JSONTestUtil.JsonResource expected,
            Supplier<RemoteAPI> remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection("7W8T91DF6Z7W6ER4K5Z", remoteAPI))).isEqualTo(expected.getJson());
    }
}

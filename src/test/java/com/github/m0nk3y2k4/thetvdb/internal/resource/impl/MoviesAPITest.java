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

import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.MoviesAPI.getAllMovieStatuses;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.MoviesAPI.getAllMovies;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.MoviesAPI.getMovieBase;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.MoviesAPI.getMovieExtended;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.MoviesAPI.getMovieTranslation;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.params;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.MOVIE;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.MOVIE_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.MOVIE_OVERVIEW;
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
class MoviesAPITest {

    //@DisableFormatting
    @BeforeAll
    static void setUpRoutes(MockServerClient client) throws Exception {
        client.when(request("/movies/statuses", GET)).respond(jsonResponse(STATUS_OVERVIEW));
        client.when(request("/movies", GET)).respond(jsonResponse(MOVIE_OVERVIEW));
        client.when(request("/movies", GET, param("page", "9"))).respond(jsonResponse(MOVIE_OVERVIEW));
        client.when(request("/movies/648730", GET)).respond(jsonResponse(MOVIE));
        client.when(request("/movies/95574/extended", GET)).respond(jsonResponse(MOVIE_DETAILS));
        client.when(request("/movies/54717/extended", GET, param("meta", "translations"))).respond(jsonResponse(MOVIE_DETAILS));
        client.when(request("/movies/57017/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
    }

    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                of(route(con -> getMovieBase(con, 0), "getMovieBase() with ZERO movie ID")),
                of(route(con -> getMovieBase(con, -4), "getMovieBase() with negative movie ID")),
                of(route(con -> getMovieExtended(con, 0, null), "getMovieExtended() with ZERO movie ID")),
                of(route(con -> getMovieExtended(con, -5, null), "getMovieExtended() with negative movie ID")),
                of(route(con -> getMovieTranslation(con, 0, "eng"), "getMovieTranslation() with ZERO movie ID")),
                of(route(con -> getMovieTranslation(con, -1, "deu"), "getMovieTranslation() with negative movie ID")),
                of(route(con -> getMovieTranslation(con, 5841, "e"), "getMovieTranslation() with invalid language code (1)")),
                of(route(con -> getMovieTranslation(con, 147, "span"), "getMovieTranslation() with invalid language code (2)"))
        );
    }

    @SuppressWarnings("Convert2MethodRef")
    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                of(route(con -> getAllMovieStatuses(con), "getAllMovieStatuses()"), STATUS_OVERVIEW),
                of(route(con -> getAllMovies(con, null), "getAllMovies() without query parameters"), MOVIE_OVERVIEW),
                of(route(con -> getAllMovies(con, params("page", "9")), "getAllMovies() with query parameters"), MOVIE_OVERVIEW),
                of(route(con -> getMovieBase(con, 648730), "getMovieBase()"), MOVIE),
                of(route(con -> getMovieExtended(con, 95574, null), "getMovieExtended() without query parameters"), MOVIE_DETAILS),
                of(route(con -> getMovieExtended(con, 54717, params("meta", "translations")), "getMovieExtended() with query parameters"), MOVIE_DETAILS),
                of(route(con -> getMovieTranslation(con, 57017, "eng"), "getMovieTranslation()"), TRANSLATION)
        );
    }
    //@EnableFormatting

    @ParameterizedTest(name = "[{index}] Route MoviesAPI.{0} rejected")
    @MethodSource("withInvalidParameters")
    void invokeRoute_withInvalidParameters_verifyParameterValidation(TestRemoteAPICall route, RemoteAPI remoteAPI) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI)));
    }

    @ParameterizedTest(name = "[{index}] Route MoviesAPI.{0} successfully invoked")
    @MethodSource("withValidParameters")
    <T> void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, ResponseData<T> expected,
            RemoteAPI remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI))).isEqualTo(expected.getJson());
    }
}

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

import static com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory.createQueryParameters;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Search.Q;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Search.QUERY;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SearchAPI.getSearchResults;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.params;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SEARCH_OVERVIEW;
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
class SearchAPITest {

    //@DisableFormatting
    @BeforeAll
    static void setUpRoutes(MockServerClient client) throws Exception {
        client.when(request("/search", GET, param(Q, "qSearchTerm"))).respond(jsonResponse(SEARCH_OVERVIEW));
        client.when(request("/search", GET, param(QUERY, "querySearchTerm"))).respond(jsonResponse(SEARCH_OVERVIEW));
    }

    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                of(route(con -> getSearchResults(con, null), "getSearchResults() without query parameters")),
                of(route(con -> getSearchResults(con, createQueryParameters()), "getSearchResults() with empty query parameters")),
                of(route(con -> getSearchResults(con, params("other", "value")), "getSearchResults() without mandatory query parameters")),
                of(route(con -> getSearchResults(con, params(Q, " ")), "getSearchResults() with empty \"q\" parameter value")),
                of(route(con -> getSearchResults(con, params(QUERY, " ")), "getSearchResults() with empty \"query\" parameter value"))
        );
    }

    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                of(route(con -> getSearchResults(con, params(Q, "qSearchTerm")), "getSearchResults() with \"q\" parameter"), SEARCH_OVERVIEW),
                of(route(con -> getSearchResults(con, params(QUERY, "querySearchTerm")), "getSearchResults() with \"query\" parameter"), SEARCH_OVERVIEW)
        );
    }
    //@EnableFormatting

    @ParameterizedTest(name = "[{index}] Route SearchAPI.{0} rejected")
    @MethodSource("withInvalidParameters")
    void invokeRoute_withInvalidParameters_verifyParameterValidation(TestRemoteAPICall route, RemoteAPI remoteAPI) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI)));
    }

    @ParameterizedTest(name = "[{index}] Route SearchAPI.{0} successfully invoked")
    @MethodSource("withValidParameters")
    <T> void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, ResponseData<T> expected,
            RemoteAPI remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI))).isEqualTo(expected.getJson());
    }
}

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

import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.ListsAPI.getAllLists;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.ListsAPI.getListBase;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.ListsAPI.getListExtended;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.ListsAPI.getListTranslation;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.params;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.LIST;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.LIST_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.LIST_OVERVIEW;
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
class ListsAPITest {

    //@DisableFormatting
    @BeforeAll
    static void setUpRoutes(MockServerClient client) throws Exception {
        client.when(request("/lists/39880/translations/fra", GET)).respond(jsonResponse(TRANSLATION));
        client.when(request("/lists", GET)).respond(jsonResponse(LIST_OVERVIEW));
        client.when(request("/lists", GET, param("page", "6"))).respond(jsonResponse(LIST_OVERVIEW));
        client.when(request("/lists/5771", GET)).respond(jsonResponse(LIST));
        client.when(request("/lists/2414/extended", GET)).respond(jsonResponse(LIST_DETAILS));
    }

    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                of(route(con -> getListTranslation(con, 0, "eng"), "getListTranslation() with ZERO List ID")),
                of(route(con -> getListTranslation(con, -3, "deu"), "getListTranslation() with negative List ID")),
                of(route(con -> getListTranslation(con, 448, "f"), "getListTranslation() with invalid language code (1)")),
                of(route(con -> getListTranslation(con, 230, "ital"), "getListTranslation() with invalid language code (2)")),
                of(route(con -> getListBase(con, 0), "getListBase() with ZERO list ID")),
                of(route(con -> getListBase(con, -4), "getListBase() with negative list ID")),
                of(route(con -> getListExtended(con, 0), "getListExtended() with ZERO list ID")),
                of(route(con -> getListExtended(con, -5), "getListExtended() with negative list ID"))
        );
    }

    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                of(route(con -> getListTranslation(con, 39880, "fra"), "getListTranslation()"), TRANSLATION),
                of(route(con -> getAllLists(con, null), "getAllLists() without query parameters"), LIST_OVERVIEW),
                of(route(con -> getAllLists(con, params("page", "6")), "getAllLists() with query parameters"), LIST_OVERVIEW),
                of(route(con -> getListBase(con, 5771), "getListBase()"), LIST),
                of(route(con -> getListExtended(con, 2414), "getListExtended()"), LIST_DETAILS)
        );
    }
    //@EnableFormatting

    @ParameterizedTest(name = "[{index}] Route ListsAPI.{0} rejected")
    @MethodSource("withInvalidParameters")
    void invokeRoute_withInvalidParameters_verifyParameterValidation(TestRemoteAPICall route, RemoteAPI remoteAPI) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI)));
    }

    @ParameterizedTest(name = "[{index}] Route ListsAPI.{0} successfully invoked")
    @MethodSource("withValidParameters")
    <T> void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, ResponseData<T> expected,
            RemoteAPI remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI))).isEqualTo(expected.getJson());
    }
}

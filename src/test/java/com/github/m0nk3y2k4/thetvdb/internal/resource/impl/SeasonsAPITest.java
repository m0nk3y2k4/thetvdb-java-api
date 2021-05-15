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

import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeasonsAPI.getSeasonBase;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeasonsAPI.getSeasonTranslation;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeasonsAPI.getSeasonTypes;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SEASON;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.SEASONTYPE_LIST;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.TRANSLATION;
import static com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestRemoteAPICall.route;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.params.provider.Arguments.of;

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
class SeasonsAPITest {

    //@DisableFormatting
    @BeforeAll
    static void setUpRoutes(MockServerClient client) throws Exception {
        client.when(request("/seasons/348109", GET)).respond(jsonResponse(SEASON));
        client.when(request("/seasons/types", GET)).respond(jsonResponse(SEASONTYPE_LIST));
        client.when(request("/seasons/47443/translations/eng", GET)).respond(jsonResponse(TRANSLATION));
    }

    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                of(route(con -> getSeasonBase(con, 0), "getSeasonBase() with ZERO season ID")),
                of(route(con -> getSeasonBase(con, -8), "getSeasonBase() with negative season ID")),
                of(route(con -> getSeasonTranslation(con, 0, "eng"), "getSeasonTranslation() with ZERO season ID")),
                of(route(con -> getSeasonTranslation(con, -3, "deu"), "getSeasonTranslation() with negative season ID")),
                of(route(con -> getSeasonTranslation(con, 2721, "e"), "getSeasonTranslation() with invalid language code (1)")),
                of(route(con -> getSeasonTranslation(con, 6741, "span"), "getSeasonTranslation() with invalid language code (2)"))
        );
    }

    @SuppressWarnings("Convert2MethodRef")
    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                of(route(con -> getSeasonBase(con, 348109), "getSeasonBase()"), SEASON),
                of(route(con -> getSeasonTypes(con), "getSeasonTypes()"), SEASONTYPE_LIST),
                of(route(con -> getSeasonTranslation(con, 47443, "eng"), "getSeasonTranslation()"), TRANSLATION)
        );
    }
    //@EnableFormatting

    @ParameterizedTest(name = "[{index}] Route SeasonsAPI.{0} rejected")
    @MethodSource("withInvalidParameters")
    void invokeRoute_withInvalidParameters_verifyParameterValidation(TestRemoteAPICall route, RemoteAPI remoteAPI) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI)));
    }

    @ParameterizedTest(name = "[{index}] Route SeasonsAPI.{0} successfully invoked")
    @MethodSource("withValidParameters")
    <T> void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, ResponseData<T> expected,
            RemoteAPI remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI))).isEqualTo(expected.getJson());
    }
}

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

import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.AwardsAPI.getAllAwards;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.AwardsAPI.getAwardBase;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.AwardsAPI.getAwardCategoryBase;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.AwardsAPI.getAwardCategoryExtended;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.AwardsAPI.getAwardExtended;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.AWARD;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.AWARDCATEGORY;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.AWARDCATEGORY_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.AWARD_DETAILS;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.AWARD_OVERVIEW;
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
class AwardsAPITest {

    //@DisableFormatting
    @BeforeAll
    static void setUpRoutes(MockServerClient client) throws Exception {
        client.when(request("/awards/categories/5501", GET)).respond(jsonResponse(AWARDCATEGORY));
        client.when(request("/awards/categories/6874/extended", GET)).respond(jsonResponse(AWARDCATEGORY_DETAILS));
        client.when(request("/awards", GET)).respond(jsonResponse(AWARD_OVERVIEW));
        client.when(request("/awards/87", GET)).respond(jsonResponse(AWARD));
        client.when(request("/awards/894/extended", GET)).respond(jsonResponse(AWARD_DETAILS));
    }

    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                of(route(con -> getAwardCategoryBase(con, 0), "getAwardCategoryBase() with ZERO award category ID")),
                of(route(con -> getAwardCategoryBase(con, -2), "getAwardCategoryBase() with negative award category ID")),
                of(route(con -> getAwardCategoryExtended(con, 0), "getAwardCategoryExtended() with ZERO award category ID")),
                of(route(con -> getAwardCategoryExtended(con, -3), "getAwardCategoryExtended() with negative award category ID")),
                of(route(con -> getAwardBase(con, 0), "getAwardBase() with ZERO award ID")),
                of(route(con -> getAwardBase(con, -4), "getAwardBase() with negative award ID")),
                of(route(con -> getAwardExtended(con, 0), "getAwardExtended() with ZERO award ID")),
                of(route(con -> getAwardExtended(con, -5), "getAwardExtended() with negative award ID"))
        );
    }

    @SuppressWarnings("Convert2MethodRef")
    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                of(route(con -> getAwardCategoryBase(con, 5501), "getAwardCategoryBase()"), AWARDCATEGORY),
                of(route(con -> getAwardCategoryExtended(con, 6874), "getAwardCategoryExtended()"), AWARDCATEGORY_DETAILS),
                of(route(con -> getAllAwards(con), "getAllAwards()"), AWARD_OVERVIEW),
                of(route(con -> getAwardBase(con, 87), "getAwardBase()"), AWARD),
                of(route(con -> getAwardExtended(con, 894), "getAwardExtended()"), AWARD_DETAILS)
        );
    }
    //@EnableFormatting

    @ParameterizedTest(name = "[{index}] Route AwardsAPI.{0} rejected")
    @MethodSource("withInvalidParameters")
    void invokeRoute_withInvalidParameters_verifyParameterValidation(TestRemoteAPICall route, RemoteAPI remoteAPI) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI)));
    }

    @ParameterizedTest(name = "[{index}] Route AwardsAPI.{0} successfully invoked")
    @MethodSource("withValidParameters")
    <T> void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, ResponseData<T> expected,
            RemoteAPI remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI))).isEqualTo(expected.getJson());
    }
}

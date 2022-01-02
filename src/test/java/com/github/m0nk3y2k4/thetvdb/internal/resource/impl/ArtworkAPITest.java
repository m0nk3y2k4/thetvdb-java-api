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

import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.ArtworkAPI.getAllArtworkStatuses;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.ArtworkAPI.getAllArtworkTypes;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.ArtworkAPI.getArtworkBase;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.ArtworkAPI.getArtworkExtended;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.ARTWORK;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.ARTWORKSTATUS_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.ARTWORKTYPE_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.ARTWORK_DETAILS;
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
class ArtworkAPITest {

    //@DisableFormatting
    @BeforeAll
    static void setUpRoutes(MockServerClient client) throws Exception {
        client.when(request("/artwork/statuses", GET)).respond(jsonResponse(ARTWORKSTATUS_OVERVIEW));
        client.when(request("/artwork/types", GET)).respond(jsonResponse(ARTWORKTYPE_OVERVIEW));
        client.when(request("/artwork/3340", GET)).respond(jsonResponse(ARTWORK));
        client.when(request("/artwork/1007/extended", GET)).respond(jsonResponse(ARTWORK_DETAILS));
    }

    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                of(route(con -> getArtworkBase(con, 0), "getArtworkBase() with ZERO artwork ID")),
                of(route(con -> getArtworkBase(con, -3), "getArtworkBase() with negative artwork ID")),
                of(route(con -> getArtworkExtended(con, 0), "getArtworkExtended() with ZERO artwork ID")),
                of(route(con -> getArtworkExtended(con, -9), "getArtworkExtended() with negative artwork ID"))
        );
    }

    @SuppressWarnings("Convert2MethodRef")
    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                of(route(con -> getAllArtworkStatuses(con), "getAllArtworkStatuses()"), ARTWORKSTATUS_OVERVIEW),
                of(route(con -> getAllArtworkTypes(con), "getAllArtworkTypes()"), ARTWORKTYPE_OVERVIEW),
                of(route(con -> getArtworkBase(con, 3340), "getArtworkBase()"), ARTWORK),
                of(route(con -> getArtworkExtended(con, 1007), "getArtworkExtended()"), ARTWORK_DETAILS)
        );
    }
    //@EnableFormatting

    @ParameterizedTest(name = "[{index}] Route ArtworkAPI.{0} rejected")
    @MethodSource("withInvalidParameters")
    void invokeRoute_withInvalidParameters_verifyParameterValidation(TestRemoteAPICall route, RemoteAPI remoteAPI) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI)));
    }

    @ParameterizedTest(name = "[{index}] Route ArtworkAPI.{0} successfully invoked")
    @MethodSource("withValidParameters")
    <T> void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, ResponseData<T> expected,
            RemoteAPI remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI))).isEqualTo(expected.getJson());
    }
}

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

import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.UserAPI.createUserFavorites;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.UserAPI.getUserFavorites;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.POST;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonSchemaFromResource;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.FAVORITES;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.NO_DATA;
import static com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestRemoteAPICall.route;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.params.provider.Arguments.of;

import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory;
import com.github.m0nk3y2k4.thetvdb.api.model.data.FavoriteRecord;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI;
import com.github.m0nk3y2k4.thetvdb.testutils.ResponseData;
import com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter.WithHttpsMockServer;
import com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestRemoteAPICall;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.verify.VerificationTimes;

@WithHttpsMockServer
class UserAPITest {

    //@DisableFormatting
    @BeforeAll
    static void setUpRoutes(MockServerClient client) throws Exception {
        client.when(request("/user/favorites", GET)).respond(jsonResponse(FAVORITES));
        client.when(request("/user/favorites", POST)).respond(jsonResponse(NO_DATA));
    }

    @SuppressWarnings("ConstantConditions")
    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                of(route(con -> createUserFavorites(con, null), "createUserFavorites() with missing favorite record"))
        );
    }

    @SuppressWarnings("Convert2MethodRef")
    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                of(route(con -> getUserFavorites(con), "getUserFavorites()"), FAVORITES)
        );
    }
    //@EnableFormatting

    @ParameterizedTest(name = "[{index}] Route UserAPI.{0} rejected")
    @MethodSource("withInvalidParameters")
    void invokeRoute_withInvalidParameters_verifyParameterValidation(TestRemoteAPICall route, RemoteAPI remoteAPI) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI)));
    }

    @ParameterizedTest(name = "[{index}] Route UserAPI.{0} successfully invoked")
    @MethodSource("withValidParameters")
    <T> void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, ResponseData<T> expected,
            RemoteAPI remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI))).isEqualTo(expected.getJson());
    }

    @Test
    void createUserFavorites_allPropertiesSet_verifyRequest(RemoteAPI remoteAPI, MockServerClient client)
            throws Exception {
        FavoriteRecord favoriteRecord = TheTVDBApiFactory.createFavoriteRecordBuilder()
                .series(67).movies(40).episodes(28).artwork(99).people(51).list(46).build();
        createUserFavorites(new APIConnection(CONTRACT_APIKEY, remoteAPI), favoriteRecord);
        client.verify(HttpRequest.request("/user/favorites").withMethod(POST.getName())
                .withBody(jsonSchemaFromResource("favoriterecord.json")), VerificationTimes.once());
    }
}

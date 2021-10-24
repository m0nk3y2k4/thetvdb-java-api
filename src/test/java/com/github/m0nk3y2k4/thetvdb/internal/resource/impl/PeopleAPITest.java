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

import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.PeopleAPI.getAllPeopleTypes;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.PeopleAPI.getPeopleBase;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.PeopleAPI.getPeopleExtended;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.PeopleAPI.getPeopleTranslation;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.params;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.PEOPLE;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.PEOPLETYPE_OVERVIEW;
import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.PEOPLE_DETAILS;
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
class PeopleAPITest {

    //@DisableFormatting
    @BeforeAll
    static void setUpRoutes(MockServerClient client) throws Exception {
        client.when(request("/people/types", GET)).respond(jsonResponse(PEOPLETYPE_OVERVIEW));
        client.when(request("/people/5404", GET)).respond(jsonResponse(PEOPLE));
        client.when(request("/people/8741/extended", GET)).respond(jsonResponse(PEOPLE_DETAILS));
        client.when(request("/people/7914/extended", GET, param("meta", "translations"))).respond(jsonResponse(PEOPLE_DETAILS));
        client.when(request("/people/2243/translations/rus", GET)).respond(jsonResponse(TRANSLATION));
    }

    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                of(route(con -> getPeopleBase(con, 0), "getPeopleBase() with ZERO people ID")),
                of(route(con -> getPeopleBase(con, -12), "getPeopleBase() with negative people ID")),
                of(route(con -> getPeopleExtended(con, 0, null), "getPeopleExtended() with ZERO people ID")),
                of(route(con -> getPeopleExtended(con, -4, null), "getPeopleExtended() with negative people ID")),
                of(route(con -> getPeopleTranslation(con, 0, "eng"), "getPeopleTranslation() with ZERO people ID")),
                of(route(con -> getPeopleTranslation(con, -1, "deu"), "getPeopleTranslation() with negative people ID")),
                of(route(con -> getPeopleTranslation(con, 5841, "e"), "getPeopleTranslation() with invalid language code (1)")),
                of(route(con -> getPeopleTranslation(con, 147, "span"), "getPeopleTranslation() with invalid language code (2)"))
        );
    }

    @SuppressWarnings("Convert2MethodRef")
    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                of(route(con -> getAllPeopleTypes(con), "getAllPeopleTypes()"), PEOPLETYPE_OVERVIEW),
                of(route(con -> getPeopleBase(con, 5404), "getPeopleBase()"), PEOPLE),
                of(route(con -> getPeopleExtended(con, 8741, null), "getPeopleExtended() without query parameters"), PEOPLE_DETAILS),
                of(route(con -> getPeopleExtended(con, 7914, params("meta", "translations")), "getPeopleExtended() with query parameters"), PEOPLE_DETAILS),
                of(route(con -> getPeopleTranslation(con, 2243, "rus"), "getPeopleTranslation()"), TRANSLATION)
        );
    }
    //@EnableFormatting

    @ParameterizedTest(name = "[{index}] Route PeopleAPI.{0} rejected")
    @MethodSource("withInvalidParameters")
    void invokeRoute_withInvalidParameters_verifyParameterValidation(TestRemoteAPICall route, RemoteAPI remoteAPI) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI)));
    }

    @ParameterizedTest(name = "[{index}] Route PeopleAPI.{0} successfully invoked")
    @MethodSource("withValidParameters")
    <T> void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, ResponseData<T> expected,
            RemoteAPI remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection(CONTRACT_APIKEY, remoteAPI))).isEqualTo(expected.getJson());
    }
}

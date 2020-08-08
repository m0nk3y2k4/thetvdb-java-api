package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SearchAPI.getAvailableSearchParameters;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SearchAPI.series;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.params;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.request;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.QUERYPARAMETERS;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.SERIESSEARCH;
import static com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestRemoteAPICall.route;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockserver.model.Parameter.param;

import java.util.function.Supplier;
import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI;
import com.github.m0nk3y2k4.thetvdb.junit.jupiter.WithHttpsMockServer;
import com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource;
import com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestRemoteAPICall;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockserver.client.MockServerClient;

@WithHttpsMockServer
class SearchAPITest {

    @BeforeAll
    static void setUpRoutes(MockServerClient client) throws Exception {
        client.when(request("/search/series", GET, param("name", "Some Series"))).respond(jsonResponse(SERIESSEARCH));
        client.when(request("/search/series/params", GET)).respond(jsonResponse(QUERYPARAMETERS));
    }

    @SuppressWarnings("Convert2MethodRef")
    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                of(route(con -> series(con, params("name", "Some Series")), "series()"), SERIESSEARCH),
                of(route(con -> getAvailableSearchParameters(con), "getAvailableSearchParameters()"), QUERYPARAMETERS)
        );
    }

    @ParameterizedTest(name = "[{index}] Route SearchAPI.{0} successfully invoked")
    @MethodSource(value = "withValidParameters")
    void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, JsonResource expected, Supplier<RemoteAPI> remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection("OW8H7D1D6SSIOIU7Z5", remoteAPI))).isEqualTo(expected.getJson());
    }
}
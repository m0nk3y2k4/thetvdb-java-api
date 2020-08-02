package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import static com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory.createQueryParameters;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SearchAPI.getAvailableSearchParameters;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SearchAPI.series;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.QUERYPARAMETERS;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.SERIESSEARCH;
import static com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestRemoteAPICall.route;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;

import java.util.Map;
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
        client.when(request("/search/series").withQueryStringParameter("name", "Some Series").withMethod(GET.getName())).respond(response().withBody(json(SERIESSEARCH.getJsonString())));
        client.when(request("/search/series/params").withMethod(GET.getName())).respond(response().withBody(json(QUERYPARAMETERS.getJsonString())));
    }

    @SuppressWarnings("Convert2MethodRef")
    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                Arguments.of(route(con -> series(con, createQueryParameters(Map.of("name", "Some Series"))), "series()"), SERIESSEARCH),
                Arguments.of(route(con -> getAvailableSearchParameters(con), "getAvailableSearchParameters()"), QUERYPARAMETERS)
        );
    }

    @ParameterizedTest(name = "[{index}] Route SearchAPI.{0} successfully invoked")
    @MethodSource(value = "withValidParameters")
    void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, JsonResource expected, Supplier<RemoteAPI> remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection("OW8H7D1D6SSIOIU7Z5", remoteAPI))).isEqualTo(expected.getJson());
    }
}
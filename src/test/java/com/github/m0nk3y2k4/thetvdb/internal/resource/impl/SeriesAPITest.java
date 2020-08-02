package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.filter;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.get;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getActors;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getEpisodes;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getEpisodesQueryParams;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getEpisodesSummary;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getFilterParams;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getHead;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getImages;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.getImagesQueryParams;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.queryEpisodes;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI.queryImages;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.HEAD;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.params;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.json;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.ACTORS;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.EPISODES;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.IMAGEQUERYPARAMETERS;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.IMAGES;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.IMAGESUMMARY;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.QUERYPARAMETERS;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.QUERYPARAMETERS_NESTED;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.SERIES;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.SERIESHEADER;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.SERIESSUMMARY;
import static com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestRemoteAPICall.route;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.NottableString.not;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.internal.api.impl.QueryParametersImpl;
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
class SeriesAPITest {

    @BeforeAll
    static void setUpRoutes(MockServerClient client) throws Exception {
        client.when(request("/series/84574").withMethod(GET.getName())).respond(response().withBody(json(SERIES)));
        client.when(request("/series/7451").withMethod(HEAD.getName())).respond(response().withHeaders(
                ((Map<String, String>)SERIESHEADER.getDTO()).entrySet().stream().map(e -> header(e.getKey(), e.getValue())).collect(Collectors.toList())));
        client.when(request("/series/36145/actors").withMethod(GET.getName())).respond(response().withBody(json(ACTORS)));
        client.when(request("/series/84674/episodes").withQueryStringParameter(not(".*"), not(".*")).withMethod(GET.getName())).respond(response().withBody(json(EPISODES)));
        client.when(request("/series/69547/episodes").withQueryStringParameter("page", "4").withMethod(GET.getName())).respond(response().withBody(json(EPISODES)));
        client.when(request("/series/3647/episodes/query").withQueryStringParameter(not(".*"), not(".*")).withMethod(GET.getName())).respond(response().withBody(json(EPISODES)));
        client.when(request("/series/1366/episodes/query").withQueryStringParameter("airedSeason", "2").withMethod(GET.getName())).respond(response().withBody(json(EPISODES)));
        client.when(request("/series/67457/episodes/query/params").withMethod(GET.getName())).respond(response().withBody(json(QUERYPARAMETERS)));
        client.when(request("/series/8457/episodes/summary").withMethod(GET.getName())).respond(response().withBody(json(SERIESSUMMARY)));
        client.when(request("/series/4877/filter").withQueryStringParameter("keys", "network,status").withMethod(GET.getName())).respond(response().withBody(json(SERIES)));
        client.when(request("/series/48123/filter/params").withMethod(GET.getName())).respond(response().withBody(json(QUERYPARAMETERS_NESTED)));
        client.when(request("/series/3124/images").withMethod(GET.getName())).respond(response().withBody(json(IMAGESUMMARY)));
        client.when(request("/series/32145/images/query").withQueryStringParameter(not(".*"), not(".*")).withMethod(GET.getName())).respond(response().withBody(json(IMAGES)));
        client.when(request("/series/98748/images/query").withQueryStringParameter("keyType", "fanart").withMethod(GET.getName())).respond(response().withBody(json(IMAGES)));
        client.when(request("/series/74585/images/query/params").withMethod(GET.getName())).respond(response().withBody(json(IMAGEQUERYPARAMETERS)));
    }

    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                Arguments.of(route(con -> get(con, 0), "get() with ZERO series ID")),
                Arguments.of(route(con -> get(con, -1), "get() with negative series ID")),
                Arguments.of(route(con -> getHead(con, 0), "getHead() with ZERO series ID")),
                Arguments.of(route(con -> getHead(con, -2), "getHead() with negative series ID")),
                Arguments.of(route(con -> getActors(con, 0), "getActors() with ZERO series ID")),
                Arguments.of(route(con -> getActors(con, -3), "getActors() with negative series ID")),
                Arguments.of(route(con -> getEpisodes(con, 0, null),"getEpisodes() with ZERO series ID")),
                Arguments.of(route(con -> getEpisodes(con, -4, null),"getEpisodes() with negative series ID")),
                Arguments.of(route(con -> queryEpisodes(con, 0, null), "queryEpisodes() with ZERO series ID")),
                Arguments.of(route(con -> queryEpisodes(con, -5, null), "queryEpisodes() with negative series ID")),
                Arguments.of(route(con -> getEpisodesQueryParams(con, 0),"getEpisodesQueryParams() with ZERO series ID")),
                Arguments.of(route(con -> getEpisodesQueryParams(con, -6),"getEpisodesQueryParams() with negative series ID")),
                Arguments.of(route(con -> getEpisodesSummary(con, 0), "getEpisodesSummary() with ZERO series ID")),
                Arguments.of(route(con -> getEpisodesSummary(con, -7), "getEpisodesSummary() with negative series ID")),
                Arguments.of(route(con -> filter(con, 0, null), "filter() with ZERO series ID")),
                Arguments.of(route(con -> filter(con, -8, null), "filter() with negative series ID")),
                Arguments.of(route(con -> filter(con, 48745, null), "filter() without query parameters")),
                Arguments.of(route(con -> filter(con, 65487, new QueryParametersImpl()), "filter() with empty query parameters")),
                Arguments.of(route(con -> getFilterParams(con, 0), "getFilterParams() with ZERO series ID")),
                Arguments.of(route(con -> getFilterParams(con, -9), "getFilterParams() with negative series ID")),
                Arguments.of(route(con -> getImages(con, 0), "getImages()with ZERO series ID")),
                Arguments.of(route(con -> getImages(con, -10), "getImages()with negative series ID")),
                Arguments.of(route(con -> queryImages(con, 0, null), "queryImages() with ZERO series ID")),
                Arguments.of(route(con -> queryImages(con, -11, null), "queryImages() with negative series ID")),
                Arguments.of(route(con -> getImagesQueryParams(con, 0), "getImagesQueryParams() with ZERO series ID")),
                Arguments.of(route(con -> getImagesQueryParams(con, -12), "getImagesQueryParams() with negative series ID"))
        );
    }

    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                Arguments.of(route(con -> get(con, 84574), "get()"), SERIES),
                Arguments.of(route(con -> getHead(con, 7451), "getHead()"), SERIESHEADER),
                Arguments.of(route(con -> getActors(con, 36145), "getActors()"), ACTORS),
                Arguments.of(route(con -> getEpisodes(con, 84674, null),"getEpisodes() without query parameters"), EPISODES),
                Arguments.of(route(con -> getEpisodes(con, 69547, params("page", "4")),"getEpisodes() with query parameters"), EPISODES),
                Arguments.of(route(con -> queryEpisodes(con, 3647, null), "queryEpisodes() without query parameters"), EPISODES),
                Arguments.of(route(con -> queryEpisodes(con, 1366, params("airedSeason", "2")), "queryEpisodes() with query parameters"), EPISODES),
                Arguments.of(route(con -> getEpisodesQueryParams(con, 67457),"getEpisodesQueryParams()"), QUERYPARAMETERS),
                Arguments.of(route(con -> getEpisodesSummary(con, 8457), "getEpisodesSummary()"), SERIESSUMMARY),
                Arguments.of(route(con -> filter(con, 4877, params("keys", "network,status")), "filter() with query parameters"), SERIES),
                Arguments.of(route(con -> getFilterParams(con, 48123), "getFilterParams()"), QUERYPARAMETERS_NESTED),
                Arguments.of(route(con -> getImages(con, 3124), "getImages()"), IMAGESUMMARY),
                Arguments.of(route(con -> queryImages(con, 32145, null), "queryImages() without query parameters"), IMAGES),
                Arguments.of(route(con -> queryImages(con, 98748, params("keyType", "fanart")), "queryImages() with query parameters"), IMAGES),
                Arguments.of(route(con -> getImagesQueryParams(con, 74585), "getImagesQueryParams()"), IMAGEQUERYPARAMETERS)
        );
    }

    @ParameterizedTest(name = "[{index}] Route SeriesAPI.{0} rejected")
    @MethodSource(value = "withInvalidParameters")
    void invokeRoute_WithInvalidParameters_verifyParameterValidation(TestRemoteAPICall route, Supplier<RemoteAPI> remoteAPI) {
        assertThatIllegalArgumentException().isThrownBy(() -> route.invoke(new APIConnection("946FG8I5P5E56E4", remoteAPI)));
    }

    @ParameterizedTest(name = "[{index}] Route SeriesAPI.{0} successfully invoked")
    @MethodSource(value = "withValidParameters")
    void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, JsonResource expected, Supplier<RemoteAPI> remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection("W1G5JW6W8974U66G1", remoteAPI))).isEqualTo(expected.getJson());
    }
}
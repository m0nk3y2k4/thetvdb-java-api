package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.UsersAPI.addToFavorites;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.UsersAPI.addToRatings;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.UsersAPI.deleteFromFavorites;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.UsersAPI.deleteFromRatings;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.UsersAPI.get;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.UsersAPI.getFavorites;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.UsersAPI.getRatings;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.UsersAPI.getRatingsQueryParams;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.UsersAPI.queryRatings;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.DELETE;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.PUT;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.params;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.json;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.FAVORITES;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.FAVORITES_EMPTY;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.QUERYPARAMETERS;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.RATINGS;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.USER;
import static com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestRemoteAPICall.route;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.NottableString.not;

import java.util.function.Supplier;
import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI;
import com.github.m0nk3y2k4.thetvdb.junit.jupiter.WithHttpsMockServer;
import com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil;
import com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestRemoteAPICall;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockserver.client.MockServerClient;

@WithHttpsMockServer
class UsersAPITest {

    @BeforeAll
    static void setUpRoutes(MockServerClient client) throws Exception {
        client.when(request("/user").withMethod(GET.getName())).respond(response().withBody(json(USER)));
        client.when(request("/user/favorites").withMethod(GET.getName())).respond(response().withBody(json(FAVORITES)));
        client.when(request("/user/favorites/4654").withMethod(PUT.getName())).respond(response().withBody(json(FAVORITES)));
        client.when(request("/user/favorites/2479").withMethod(DELETE.getName())).respond(response().withBody(json(FAVORITES_EMPTY)));
        client.when(request("/user/ratings").withMethod(GET.getName())).respond(response().withBody(json(RATINGS)));
        client.when(request("/user/ratings/query").withQueryStringParameter(not(".*"), not(".*")).withMethod(GET.getName())).respond(response().withBody(json(RATINGS)));
        client.when(request("/user/ratings/query").withQueryStringParameter("itemType", "series").withMethod(GET.getName())).respond(response().withBody(json(RATINGS)));
        client.when(request("/user/ratings/query/params").withMethod(GET.getName())).respond(response().withBody(json(QUERYPARAMETERS)));
        client.when(request("/user/ratings/episode/36544/9").withMethod(PUT.getName())).respond(response().withBody(json(RATINGS)));
        client.when(request("/user/ratings/image/9657").withMethod(DELETE.getName())).respond(response().withBody(json(RATINGS)));
    }

    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                Arguments.of(route(con -> deleteFromFavorites(con, 0), "deleteFromFavorites() with ZERO series ID")),
                Arguments.of(route(con -> deleteFromFavorites(con, -1), "deleteFromFavorites() with negative series ID")),
                Arguments.of(route(con -> addToFavorites(con, 0), "addToFavorites() with ZERO series ID")),
                Arguments.of(route(con -> addToFavorites(con, -2), "addToFavorites() with negative series ID")),
                Arguments.of(route(con -> deleteFromRatings(con, "xyz", 4857), "deleteFromRatings() with invalid item type")),
                Arguments.of(route(con -> deleteFromRatings(con, "series", 0), "deleteFromRatings() with ZERO item ID")),
                Arguments.of(route(con -> deleteFromRatings(con, "episode", -3), "deleteFromRatings() with negative item ID")),
                Arguments.of(route(con -> addToRatings(con, "zyx", 6597, 6), "addToRatings() with invalid item type")),
                Arguments.of(route(con -> addToRatings(con, "series", 0, 5), "addToRatings() with ZERO item ID")),
                Arguments.of(route(con -> addToRatings(con, "image", -4, 7), "addToRatings() with negative item ID")),
                Arguments.of(route(con -> addToRatings(con, "episode", 6774, 0), "addToRatings() with ZERO item rating")),
                Arguments.of(route(con -> addToRatings(con, "series", 1249, -5), "addToRatings() with negative item rating"))
        );
    }

    @SuppressWarnings("Convert2MethodRef")
    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                Arguments.of(route(con -> get(con), "get()"), USER),
                Arguments.of(route(con -> getFavorites(con), "getFavorites()"), FAVORITES),
                Arguments.of(route(con -> addToFavorites(con, 4654), "addToFavorites()"), FAVORITES),
                Arguments.of(route(con -> deleteFromFavorites(con, 2479), "deleteFromFavorites()"), FAVORITES_EMPTY),
                Arguments.of(route(con -> getRatings(con), "getRatings()"), RATINGS),
                Arguments.of(route(con -> queryRatings(con, null), "queryRatings() without query parameters"), RATINGS),
                Arguments.of(route(con -> queryRatings(con, params("itemType", "series")), "queryRatings() with query parameters"), RATINGS),
                Arguments.of(route(con -> getRatingsQueryParams(con), "getRatingsQueryParams()"), QUERYPARAMETERS),
                Arguments.of(route(con -> addToRatings(con, "episode", 36544, 9), "addToRatings()"), RATINGS),
                Arguments.of(route(con -> deleteFromRatings(con, "image", 9657), "deleteFromRatings()"), RATINGS)
        );
    }

    @ParameterizedTest(name = "[{index}] Route UsersAPI.{0} rejected")
    @MethodSource(value = "withInvalidParameters")
    void invokeRoute_WithInvalidParameters_verifyParameterValidation(TestRemoteAPICall route, Supplier<RemoteAPI> remoteAPI) {
        assertThatIllegalArgumentException().isThrownBy(() -> route.invoke(new APIConnection("97821R44O54ZT4W5", remoteAPI)));
    }

    @ParameterizedTest(name = "[{index}] Route UsersAPI.{0} successfully invoked")
    @MethodSource(value = "withValidParameters")
    void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, JSONTestUtil.JsonResource expected, Supplier<RemoteAPI> remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection("4S7TG7JU5Q687EE", remoteAPI))).isEqualTo(expected.getJson());
    }
}
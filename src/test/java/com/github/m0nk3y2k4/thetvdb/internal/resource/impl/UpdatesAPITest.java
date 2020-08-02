package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates.FROMTIME;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates.TOTIME;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.UpdatesAPI.getQueryParams;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.UpdatesAPI.query;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.params;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.json;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.QUERYPARAMETERS;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.UPDATES;
import static com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestRemoteAPICall.route;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.util.function.Supplier;
import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.internal.api.impl.QueryParametersImpl;
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
class UpdatesAPITest {

    @BeforeAll
    static void setUpRoutes(MockServerClient client) throws Exception {
        client.when(request("/updated/query").withQueryStringParameter(FROMTIME, "12").withMethod(GET.getName())).respond(response().withBody(json(UPDATES)));
        client.when(request("/updated/query/params").withMethod(GET.getName())).respond(response().withBody(json(QUERYPARAMETERS)));
    }

    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                Arguments.of(route(con -> query(con, null), "query() without query parameters")),
                Arguments.of(route(con -> query(con, new QueryParametersImpl()), "query() with empty query parameters")),
                Arguments.of(route(con -> query(con, params(TOTIME, "1")), "query() without mandatory query parameters")),
                Arguments.of(route(con -> query(con, params(FROMTIME, "0")), "query() with ZERO query parameter value")),
                Arguments.of(route(con -> query(con, params(FROMTIME, "-6")), "query() with negative query parameter value")),
                Arguments.of(route(con -> query(con, params(FROMTIME, "yesterday")), "query() with NaN query parameter value"))
        );
    }

    @SuppressWarnings("Convert2MethodRef")
    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                Arguments.of(route(con -> query(con, params(FROMTIME, "12")), "query()"), UPDATES),
                Arguments.of(route(con -> getQueryParams(con), "getQueryParams()"), QUERYPARAMETERS)
        );
    }

    @ParameterizedTest(name = "[{index}] Route UpdatesAPI.{0} rejected")
    @MethodSource(value = "withInvalidParameters")
    void invokeRoute_withInvalidParameters_verifyParameterValidation(TestRemoteAPICall route, Supplier<RemoteAPI> remoteAPI) {
        assertThatIllegalArgumentException().isThrownBy(() -> route.invoke(new APIConnection("7E58UZ8IE8T8U4", remoteAPI)));
    }

    @ParameterizedTest(name = "[{index}] Route UpdatesAPI.{0} successfully invoked")
    @MethodSource(value = "withValidParameters")
    void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, JSONTestUtil.JsonResource expected, Supplier<RemoteAPI> remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection("WUIR5EUZAO6354", remoteAPI))).isEqualTo(expected.getJson());
    }
}
package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.LanguagesAPI.get;
import static com.github.m0nk3y2k4.thetvdb.internal.resource.impl.LanguagesAPI.getAllAvailable;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.LANGUAGE;
import static com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource.LANGUAGES;
import static com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestRemoteAPICall.route;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;

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
class LanguagesAPITest {

    @BeforeAll
    static void setUpRoutes(MockServerClient client) throws Exception {
        client.when(request("/languages").withMethod(GET.getName())).respond(response().withBody(json(LANGUAGES.getJsonString())));
        client.when(request("/languages/4584").withMethod(GET.getName())).respond(response().withBody(json(LANGUAGE.getJsonString())));
    }

    private static Stream<Arguments> withInvalidParameters() {
        return Stream.of(
                Arguments.of(route(con -> get(con, 0), "get() with ZERO language ID")),
                Arguments.of(route(con -> get(con, -11), "get() with negative language ID"))
        );
    }

    @SuppressWarnings("Convert2MethodRef")
    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                Arguments.of(route(con -> get(con, 4584), "get()"), LANGUAGE),
                Arguments.of(route(con -> getAllAvailable(con), "getAllAvailable()"), LANGUAGES)
        );
    }

    @ParameterizedTest(name = "[{index}] Route LanguagesAPI.{0} rejected")
    @MethodSource(value = "withInvalidParameters")
    void invokeRoute_WithInvalidParameters_verifyParameterValidation(TestRemoteAPICall route, Supplier<RemoteAPI> remoteAPI) {
        assertThatIllegalArgumentException().isThrownBy(() -> route.invoke(new APIConnection("946FG8I5P5E56E4", remoteAPI)));
    }

    @ParameterizedTest(name = "[{index}] Route LanguagesAPI.{0} successfully invoked")
    @MethodSource(value = "withValidParameters")
    void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, JsonResource expected, Supplier<RemoteAPI> remoteAPI) throws Exception {
        assertThat(route.invoke(new APIConnection("F5I5W4T657I6KAA", remoteAPI))).isEqualTo(expected.getJson());
    }
}
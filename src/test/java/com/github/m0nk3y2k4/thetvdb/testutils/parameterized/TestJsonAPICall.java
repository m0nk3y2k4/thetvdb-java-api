package com.github.m0nk3y2k4.thetvdb.testutils.parameterized;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.util.functional.ThrowableFunctionalInterfaces.Supplier;

/**
 * Small helper class used to wrap calls of TheTVDB.com JSON layout API routes for easier parameterized JUnit testing
 * <p><br>
 * Accepts routes from the <i>{@link com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi.JSON}</i> interface. An additional textual
 * description can be added which will be displayed as the default String representation of this object. Provides a single method to
 * invoke the underlying API route and to return it's response.
 * <pre>{@code
 * @WithHttpsMockServer
 * class SomeJsonAPITest {
 *
 *     @BeforeAll
 *     static void setUpRoutes(MockServerClient client) throws Exception {
 *         client.when(request("/remote/api").withMethod(GET.getName())).respond(response().withBody(JSON_DATA));
 *     }
 *
 *     private static Stream<Arguments> withValidParameters() {
 *         return Stream.of(
 *                 Arguments.of(route(() -> SomeJsonAPI.getEpisode(8745), "getEpisode() with ID 8745"), JSON_DATA)
 *                 // ...here be some more routes
 *         );
 *     }
 *
 *     @ParameterizedTest(name = "[{index}] Route SomeJsonAPI.{0} successfully invoked")    // [1] Route SomeJsonAPI.getEpisode() with ID 8745 successfully invoked
 *     @MethodSource(value = "withValidParameters")
 *     void invokeRoute_withValidParameters_verifyResponse(TestJsonAPICall route, String expected, Supplier<RemoteAPI> remoteAPI) throws Exception {
 *         assertThat(route.invoke()).isEqualTo(expected);
 *     }
 * }</pre>
 */
public class TestJsonAPICall extends TestAPICall<Supplier<JsonNode, APIException>> {

    private TestJsonAPICall(Supplier<JsonNode, APIException> route, String description) {
        super(route, description);      // Use TestJsonAPICall#route instead
    }

    /**
     * Invokes the underlying JSON API route and returns it's result
     *
     * @return JSON response returned by the route invocation
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    public JsonNode invoke() throws APIException {
        return route.get();
    }

    /**
     * Creates a new JSON API call for the given route
     *
     * @param route The actual route to be invoked
     * @param description Textual description of the given route
     *
     * @return New JSON API call based on the given parameters
     */
    public static TestJsonAPICall route(Supplier<JsonNode, APIException> route, String description) {
        return new TestJsonAPICall(route, description);
    }
}

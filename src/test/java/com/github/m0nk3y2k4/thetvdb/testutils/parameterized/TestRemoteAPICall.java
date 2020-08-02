package com.github.m0nk3y2k4.thetvdb.testutils.parameterized;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.util.functional.ThrowableFunctionalInterfaces;

/**
 * Small helper class used to wrap calls to the remote API for easier parameterized JUnit testing
 * <p><br>
 * Accepts routes from classes within the <i>com.github.m0nk3y2k4.thetvdb.internal.resource.impl</i> package. An additional textual
 * description can be added which will be displayed as the default String representation of this object. Provides a single method to
 * invoke the underlying remote API route and to return it's response.
 * <pre>{@code
 * @WithHttpsMockServer
 * class SomeAPITest {
 *
 *     @BeforeAll
 *     static void setUpRoutes(MockServerClient client) throws Exception {
 *         client.when(request("/remote/api").withMethod(GET.getName())).respond(response().withBody(JSON_DATA));
 *     }
 *
 *     private static Stream<Arguments> withValidParameters() {
 *         return Stream.of(
 *                 Arguments.of(route(con -> SomeAPI.get(con, 8745), "get() with ID 8745"), JSON_DATA)
 *                 // ...here be some more routes
 *         );
 *     }
 *
 *     @ParameterizedTest(name = "[{index}] Route SomeAPI.{0} successfully invoked")    // [1] Route SomeAPI.get() with ID 8745 successfully invoked
 *     @MethodSource(value = "withValidParameters")
 *     void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, String expected, Supplier<RemoteAPI> remoteAPI) throws Exception {
 *         assertThat(route.invoke(new APIConnection("IWU57F5WF4", remoteAPI))).isEqualTo(expected);
 *     }
 * }</pre>
 */
public class TestRemoteAPICall {

    /** The actual API route represented by this object */
    private final ThrowableFunctionalInterfaces.Function<APIConnection, JsonNode, APIException> route;

    /** Textual description of this remote API call */
    private final String description;

    /**
     * Creates a new remote API call for the given route
     *
     * @param route The actual remote route to be invoked
     * @param description Textual description of the given remote route
     */
    private TestRemoteAPICall(ThrowableFunctionalInterfaces.Function<APIConnection, JsonNode, APIException> route, String description) {
        this.route = route;
        this.description = description;
    }

    /**
     * Invokes the underlying remote API route and returns it's result
     *
     * @param con API connection to be used for invocation
     *
     * @return JSON response returned by the remote route invocation
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    public JsonNode invoke(APIConnection con) throws APIException {
        return route.apply(con);
    }

    @Override
    public String toString() {
        return description;
    }

    /**
     * Creates a new remote API call for the given route
     *
     * @param route The actual remote route to be invoked
     * @param description Textual description of the given remote route
     *
     * @return New remote API call based on the given parameters
     */
    public static TestRemoteAPICall route(ThrowableFunctionalInterfaces.Function<APIConnection, JsonNode, APIException> route, String description) {
        return new TestRemoteAPICall(route, description);
    }
}

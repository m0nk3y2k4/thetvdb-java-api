package com.github.m0nk3y2k4.thetvdb.testutils.parameterized;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.util.functional.ThrowableFunctionalInterfaces.Supplier;

/**
 * Small helper class used to wrap calls of TheTVDB.com layout API routes for easier parameterized JUnit testing
 * <p><br>
 * Accepts routes from the <i>{@link com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi}</i> interface. An additional textual
 * description can be added which will be displayed as the default String representation of this object. Provides a single method to
 * invoke the underlying API route and to return it's response.
 * <pre>{@code
 * @WithHttpsMockServer
 * class SomeTheTVDBAPITest {
 *
 *     @BeforeAll
 *     static void setUpRoutes(MockServerClient client) throws Exception {
 *         client.when(request("/remote/api").withMethod(GET.getName())).respond(response().withBody(JSON_DATA));
 *     }
 *
 *     private static Stream<Arguments> withValidParameters() {
 *         return Stream.of(
 *                 Arguments.of(route(() -> SomeTheTVDBAPITest.getActors(3641), "getActors() with ID 3641"), JSON_DATA)
 *                 // ...here be some more routes
 *         );
 *     }
 *
 *     @ParameterizedTest(name = "[{index}] Route SomeTheTVDBAPITest.{0} successfully invoked")    // [1] Route SomeTheTVDBAPITest.getActors() with ID 3641 successfully invoked
 *     @MethodSource(value = "withValidParameters")
 *     void invokeRoute_withValidParameters_verifyResponse(TestTheTVDBAPICall route, String expected, Supplier<RemoteAPI> remoteAPI) throws Exception {
 *         assertThat(route.invoke()).isEqualTo(expected);
 *     }
 * }</pre>
 */
public class TestTheTVDBAPICall<T> extends TestAPICall<Supplier<T, APIException>> {

    private TestTheTVDBAPICall(Supplier<T, APIException> route, String description) {
        super(route, description);      // Use TestTheTVDBAPICall#route instead
    }

    /**
     * Invokes the underlying TheTVDB.com API route and returns it's result
     *
     * @return DTO response returned by the route invocation
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    public T invoke() throws APIException {
        return route.get();
    }

    /**
     * Creates a new TheTVDB.com API call for the given route
     *
     * @param route The actual route to be invoked
     * @param description Textual description of the given route
     *
     * @return New TheTVDB.com API call based on the given parameters
     */
    public static <T> TestTheTVDBAPICall<T> route(Supplier<T, APIException> route, String description) {
        return new TestTheTVDBAPICall<>(route, description);
    }
}

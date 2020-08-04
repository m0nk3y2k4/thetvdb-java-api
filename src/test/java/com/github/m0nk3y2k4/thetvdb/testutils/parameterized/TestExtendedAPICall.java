package com.github.m0nk3y2k4.thetvdb.testutils.parameterized;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.internal.util.functional.ThrowableFunctionalInterfaces.Supplier;

/**
 * Small helper class used to wrap calls of TheTVDB.com extended layout API routes for easier parameterized JUnit testing
 * <p><br>
 * Accepts routes from the <i>{@link com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi.Extended}</i> interface. An additional textual
 * description can be added which will be displayed as the default String representation of this object. Provides a single method to
 * invoke the underlying API route and to return it's response.
 * <pre>{@code
 * @WithHttpsMockServer
 * class SomeExtendedAPITest {
 *
 *     @BeforeAll
 *     static void setUpRoutes(MockServerClient client) throws Exception {
 *         client.when(request("/remote/api").withMethod(GET.getName())).respond(response().withBody(JSON_DATA));
 *     }
 *
 *     private static Stream<Arguments> withValidParameters() {
 *         return Stream.of(
 *                 Arguments.of(route(() -> SomeExtendedAPI.getLanguage(12), "getLanguage() with ID 12"), JSON_DATA)
 *                 // ...here be some more routes
 *         );
 *     }
 *
 *     @ParameterizedTest(name = "[{index}] Route SomeExtendedAPI.{0} successfully invoked")    // [1] Route SomeExtendedAPI.getLanguage() with ID 12 successfully invoked
 *     @MethodSource(value = "withValidParameters")
 *     void invokeRoute_withValidParameters_verifyResponse(TestExtendedAPICall route, String expected, Supplier<RemoteAPI> remoteAPI) throws Exception {
 *         assertThat(route.invoke()).isEqualTo(expected);
 *     }
 * }</pre>
 */
public class TestExtendedAPICall<T> extends TestAPICall<Supplier<APIResponse<T>, APIException>> {

    private TestExtendedAPICall(Supplier<APIResponse<T>, APIException> route, String description) {
        super(route, description);      // Use TestExtendedAPICall#route instead
    }

    /**
     * Invokes the underlying extended API route and returns it's result
     *
     * @return Extended API response returned by the route invocation
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    public APIResponse<T> invoke() throws APIException {
        return route.get();
    }

    /**
     * Creates a new extended API call for the given route
     *
     * @param route The actual route to be invoked
     * @param description Textual description of the given route
     *
     * @return New extended API call based on the given parameters
     */
    public static <T> TestExtendedAPICall<T> route(Supplier<APIResponse<T>, APIException> route, String description) {
        return new TestExtendedAPICall<>(route, description);
    }
}

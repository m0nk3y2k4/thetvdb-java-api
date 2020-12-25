/*
 * Copyright (C) 2019 - 2020 thetvdb-java-api Authors and Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.m0nk3y2k4.thetvdb.testutils.parameterized;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.util.functional.ThrowableFunctionalInterfaces.Procedure;
import com.github.m0nk3y2k4.thetvdb.internal.util.functional.ThrowableFunctionalInterfaces.Supplier;

/**
 * Small helper class used to wrap calls of TheTVDBAPI routes for easier parameterized JUnit testing
 * <p><br>
 * Accepts routes from the <i>{@link com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi}</i> interface. An additional textual
 * description can be added which will be displayed as the default String representation of this object. Provides a
 * single method to invoke the underlying API route and to return it's actual response value.
 * <pre><code>
 * {@literal @WithHttpsMockServer}
 * class SomeAPITest {
 *
 *     {@literal @BeforeAll}
 *     static void setUpRoutes(MockServerClient client) throws Exception {
 *         client.when(request("/remote/api/actors/3641").withMethod(GET.getName())).respond(response().withBody(ACTORS_JSON));
 *     }
 *
 *     private static Stream<Arguments> withValidParameters() {
 *         return Stream.of(
 *                 Arguments.of(route(() -> SomeTheTVDBAPITest.getActors(3641), "getActors() with ID 3641"), EXPECTED_ACTORS_DTO)
 *                 // ...here be some more routes
 *         );
 *     }
 *
 *     {@literal @ParameterizedTest}(name = "[{index}] Route SomeAPITest.{0} successfully invoked")    // [1] Route SomeAPITest.getActors() with ID 3641 successfully invoked
 *     {@literal @MethodSource}("withValidParameters")
 *     void invokeRoute_withValidParameters_verifyResponse(TestTheTVDBAPICall route, Object expectedDTO) throws Exception {
 *         // The following assert actually invokes the underlying route and compares its return value with the expected DTO
 *         TestTheTVDBAPICallAssert.assertThat(route).matchesExpectation(expectedDTO);
 *     }
 * }
 * </code></pre>
 * <p><br>
 * For void API routes returning no result/DTO the {@link TestTheTVDBAPICall.Void} wrapper can be used.
 *
 * @param <T> type of the wrapped routes actual return value
 */
public class TestTheTVDBAPICall<T> extends TestAPICall<Supplier<T, APIException>> {

    private TestTheTVDBAPICall(Supplier<T, APIException> route, String description) {
        super(route, description);      // Use TestTheTVDBAPICall#route(Supplier, String) instead
    }

    /**
     * Creates a new TheTVDBAPI call for the given route which forwards the routes actual return value
     *
     * @param route       The route to be invoked
     * @param description Textual description of the given route
     * @param <T>         type of the wrapped routes actual return value
     *
     * @return New TheTVDBAPI call based on the given parameters
     */
    public static <T> TestTheTVDBAPICall<T> route(Supplier<T, APIException> route, String description) {
        return new TestTheTVDBAPICall<>(route, description);
    }

    /**
     * Creates a new TheTVDBAPI call for the given void route, always returning {@code null} as invocation result
     *
     * @param route       The route to be invoked
     * @param description Textual description of the given route
     *
     * @return New TheTVDBAPI call based on the given parameters
     */
    public static TestTheTVDBAPICall.Void route(Procedure<APIException> route, String description) {
        return new TestTheTVDBAPICall.Void(route, description);
    }

    /**
     * Invokes the underlying TheTVDBAPI route and returns it's result. For instances of {@link TestTheTVDBAPICall.Void}
     * this method will always return a {@code null} value.
     *
     * @return DTO response returned by the route invocation or {@code null} for void routes
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public T invoke() throws APIException {
        return route.get();
    }

    /**
     * Small helper class used to wrap calls of TheTVDBAPI routes, that do not return any result, for easier
     * parameterized JUnit testing
     * <p><br>
     * Accepts routes from the <i>{@link com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi}</i> interface. An additional
     * textual description can be added which will be displayed as the default String representation of this object.
     * Provides a single method to invoke the underlying API route. The return value of the invocation will always be
     * {@code null} for instances of this class.
     * <pre><code>
     * class SomeAPITest {
     *
     *     private static Stream<Arguments> withValidParameters() {
     *         return Stream.of(
     *                 Arguments.of(route(() -> userAuthApi.deleteFromRatings("image", 7874), "deleteFromRatings()"), request("/user/ratings/image/7874").withMethod(DELETE.getName()))
     *                 // ...here be some more routes
     *         );
     *     }
     *
     *    {@literal @ParameterizedTest}(name = "[{index}] Route SomeAPITest.{0} successfully invoked")    // [1] Route SomeAPITest.deleteFromRatings() successfully invoked
     *    {@literal @MethodSource}("withValidParameters")
     *     void invokeRoute_withValidParameters_verifyResponse(TestTheTVDBAPICall route, HttpRequest verifyInvokedOnMockServer, MockServerClient client) throws Exception {
     *          // The following assert actually invokes the wrapped route and verifies that the expected HTTP request was received by the mock server
     *         TestTheTVDBAPICallAssert.assertThat(route).usingMockServer(client).matchesExpectation(verifyInvokedOnMockServer);
     *     }
     * }
     * </code></pre>
     */
    public static final class Void extends TestTheTVDBAPICall<Object> {

        private Void(Procedure<APIException> route, String description) {
            super(() -> {
                route.invoke();
                return null;
            }, description);       // Use TestTheTVDBAPICall#route(Procedure, String) instead
        }
    }
}

/*
 * Copyright (C) 2019 - 2021 thetvdb-java-api Authors and Contributors
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

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.util.functional.ThrowableFunctionalInterfaces.Function;

/**
 * Small helper class used to wrap calls to the remote API for easier parameterized JUnit testing
 * <p><br>
 * It accepts routes from classes within the <i>{@link com.github.m0nk3y2k4.thetvdb.internal.resource.impl}</i> package.
 * An additional textual description can be added which will be displayed as the default String representation of this
 * object. Provides a single method to invoke the underlying remote API route and to return its response.
 * <pre><code>
 * {@literal @WithHttpsMockServer}
 * class SomeAPITest {
 *
 *     {@literal @BeforeAll}
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
 *     {@literal @ParameterizedTest}(name = "[{index}] Route SomeAPI.{0} successfully invoked")    // [1] Route SomeAPI.get() with ID 8745 successfully invoked
 *     {@literal @MethodSource}("withValidParameters")
 *     void invokeRoute_withValidParameters_verifyResponse(TestRemoteAPICall route, String expected, Supplier<RemoteAPI> remoteAPI) throws Exception {
 *         assertThat(route.invoke(new APIConnection("IWU57F5WF4", remoteAPI))).isEqualTo(expected);
 *     }
 * }
 * </code></pre>
 */
public final class TestRemoteAPICall extends TestAPICall<Function<APIConnection, JsonNode, APIException>> {

    private TestRemoteAPICall(Function<APIConnection, JsonNode, APIException> route, String description) {
        super(route, description);      // Use TestRemoteAPICall#route instead
    }

    /**
     * Creates a new remote API-call for the given route
     *
     * @param route       The actual remote route to be invoked
     * @param description Textual description of the given remote route
     *
     * @return New remote API call based on the given parameters
     */
    public static TestRemoteAPICall route(Function<APIConnection, JsonNode, APIException> route, String description) {
        return new TestRemoteAPICall(route, description);
    }

    /**
     * Invokes the underlying remote API route and returns its result
     *
     * @param con API connection to be used for invocation
     *
     * @return JSON response returned by the remote route invocation
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public JsonNode invoke(APIConnection con) throws APIException {
        return route.apply(con);
    }
}


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

package com.github.m0nk3y2k4.thetvdb.testutils.assertj;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil;
import com.github.m0nk3y2k4.thetvdb.testutils.parameterized.TestTheTVDBAPICall;
import org.assertj.core.api.AbstractAssert;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.verify.VerificationTimes;

/**
 * Special assertion for {@link TestTheTVDBAPICall} objects, wrapping some API route invocation
 * <p><br>
 * Supports expectation matching for API routes, regardless of the actual API layout that is used. For wrapped non-void
 * routes a {@link JSONTestUtil.JsonResource} object is expected for matching. The assert will automatically try to
 * determine the layout used by the API route and will perform the matching operation accordingly.
 * <pre>{@code
 *     TheTVDBApi api = TheTVDBApiFactory.createApi("Some APIKey");
 *
 *     // Each assertion invokes the given (non-void) API route and matches the returned value with the given expectation (with automatic conversion)
 *
 *     // TheTVDBApi layout: expectation automatically converted to ACTORS.getDTO().getData()
 *     TestTheTVDBAPICallAssert.assertThat(route(() -> api.getActors(45), "Returning List<Actors>"))
 *              .matchesExpectation(JsonResource.ACTORS);
 *
 *     // Extended layout: expectation automatically converted to ACTORS.getDTO()
 *     TestTheTVDBAPICallAssert.assertThat(route(() -> api.extended().getActors(45), "Returning APIResponse<List<Actor>>"))
 *              .matchesExpectation(JsonResource.ACTORS);
 *
 *     // JSON layout: expectation automatically converted to ACTORS.getJson()
 *     TestTheTVDBAPICallAssert.assertThat(route(() -> api.json().getActors(45), "Returning JsonNode"))
 *              .matchesExpectation(JsonResource.ACTORS);
 * }</pre>
 * Unfortunately void API routes do not return any object which could be used for matching an expectation. However, you
 * may let the assert verify that a specific resource has been invoked on the mock server by passing a corresponding
 * HttpRequest object as expectation. For this to work the assert must get in contact with the mock server running in
 * the background. The server can be announced by setting a mock server reference to the assert first.
 * <pre>{@code
 *     @Test
 *     void voidApiRouteTest(MockServerClient client) throws Exception {        // Client can be injected when using the JUnit5 HttpsMockServerExtension
 *         TheTVDBApi api = TheTVDBApiFactory.createApi("Some APIKey");
 *
 *         // Make the mock server known to the assert with "usingMockServer(client)"
 *         TestTheTVDBAPICallAssert.assertThat(route(() -> api.login(), "Void route not returning any object"))
 *                .usingMockServer(client).matchesExpectation(HttpRequest.request("/api/login").withMethod("GET"));
 *     }
 * } </pre>
 *
 * @param <T> type of the wrapped routes actual return value
 */
public class TestTheTVDBAPICallAssert<T> extends AbstractAssert<TestTheTVDBAPICallAssert<T>, TestTheTVDBAPICall<T>> {

    /** Mock server used to verify resource invocations of void API routes. Has to be set via #usingMockServer first. */
    private MockServerClient client;

    private TestTheTVDBAPICallAssert(TestTheTVDBAPICall<T> actual) {
        super(actual, TestTheTVDBAPICallAssert.class);
    }

    /**
     * Creates a new instance of TestTheTVDBAPICallAssert
     *
     * @param actual The actual value
     * @param <T>    type of the wrapped routes actual return value
     *
     * @return The created assertion object
     */
    public static <T> TestTheTVDBAPICallAssert<T> assertThat(TestTheTVDBAPICall<T> actual) {
        return new TestTheTVDBAPICallAssert<>(actual);
    }

    /**
     * Use the given client to verify requests have been received by the mock server. Has to be called before matching a
     * HttpRequest expectation.
     *
     * @param client Client reference used for working with the mock server
     *
     * @return This assertion object
     */
    public TestTheTVDBAPICallAssert<T> usingMockServer(MockServerClient client) {
        this.client = client;
        return this;
    }

    /**
     * Invokes the actual API call and matches the given expectation which must be either
     * <ul>
     *     <li>A {@link JSONTestUtil.JsonResource} object for non-void API routes<br>
     *         The routes actual return value will be compared with the given object using automatic conversion</li>
     *     <li>A {@link HttpRequest} object for void API routes<br>
     *         After the route has been invoked the mock server will be asked to verify that a request matching the given
     *         object has been received exactly once</li>
     * </ul>
     *
     * @param expected The expected JsonResource or the HttpRequest to be verified
     *
     * @throws IOException  If an exception occurred while auto-converting a JsonResource object into it's JsonNode
     *                      representation
     * @throws APIException If an exception occurred while invoking the actual API call of this assertion
     */
    public void matchesExpectation(Object expected) throws IOException, APIException {
        isNotNull();

        if (isVoidCallInvocation()) {
            verifyMockServerRouteInvoked((HttpRequest)expected);
        } else {
            matchesJsonResourceExpectation((JSONTestUtil.JsonResource)expected);
        }
    }

    /**
     * Checks whether the actual API call represents a void API route
     *
     * @return True if the API call is an instance of {@link TestTheTVDBAPICall.Void}
     */
    private boolean isVoidCallInvocation() {
        return actual instanceof TestTheTVDBAPICall.Void;
    }

    /**
     * Invokes the actual API call and verifies that the given HttpRequest has been received exactly once by the mock
     * server
     *
     * @param request The HttpRequest to be verified it has been invoked once
     *
     * @throws APIException If an exception occurred while invoking the actual API call of this assertion
     */
    private void verifyMockServerRouteInvoked(HttpRequest request) throws APIException {
        if (client == null) {
            failWithMessage("Cannot verify HTTP request expectation due to missing mock server client. "
                    + "Please provide a valid mock server client via TestTheTVDBAPICallAssert#usingMockServer(client)");
        }

        actual.invoke();            // Ignore return value as it is always "null" for void methods

        client.verify(request, VerificationTimes.once());
    }

    /**
     * Invokes the actual API call and verifies that its return value matches the given JSON resource. The JSON resource
     * may be auto-converted based on the current API call layout before comparing the values.
     *
     * @param resource JSON resource which is expected to be returned by the invocation of the actual API call
     *
     * @throws IOException  If an exception occurred while auto-converting a JsonResource object into it's JsonNode
     *                      representation
     * @throws APIException If an exception occurred while invoking the actual API call of this assertion
     */
    private void matchesJsonResourceExpectation(JSONTestUtil.JsonResource resource) throws IOException, APIException {
        T result = actual.invoke();
        Object expected = buildExpectation(result, resource);

        if (!Objects.equals(result, expected)) {
            failWithActualExpectedAndMessage(result, expected, "Expected to be equal");
        }
    }

    /**
     * Auto-converts the given JSON resource to a representation matching the layout used by the API call. The layout
     * will be determined heuristically by analyzing the routes actual return value and compare it to the type of values
     * typically returned by a specific layout.
     *
     * @param result   The value returned by invoking the actual API call
     * @param resource JSON resource representing the value expected to be returned by the API call
     *
     * @return Representation of the given resource matching the used layout. This can either be a data object, an
     *         APIResponse DTO or a JSON representation.
     *
     * @throws IOException If an exception occurred while auto-converting a JsonResource object into it's JsonNode
     *                     representation
     */
    private Object buildExpectation(T result, JSONTestUtil.JsonResource resource) throws IOException {
        if (usingExtendedLayout(result)) {
            // Invocation of some (non-void) TheTVDBApi.Extended layout route -> These routes always return an APIResponse<DTO> object
            return resource.getDTO();
        } else if (usingJsonLayout(result)) {
            // Invocation of some (non-void) TheTVDBApi.JSON layout route -> These routes always return a JsonNode object
            return resource.getJson();
        } else {
            // Invocation of some (non-void) TheTVDBApi layout route -> These routes always return the actual content payload of the APIResponse<DTO>
            return resource.getDTO().getData();
        }
    }

    /**
     * Checks whether the given object is a typical return value for the {@link com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi.Extended}
     * layout
     *
     * @param result The value to check
     *
     * @return True if the given value represents a class that is typically returned by the invocation of Extended
     *         layout API routes
     */
    private boolean usingExtendedLayout(T result) {
        return Optional.ofNullable(result).map(Object::getClass).map(APIResponse.class::isAssignableFrom).orElse(false);
    }

    /**
     * Checks whether the given object is a typical return value for the {@link com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi.JSON}
     * layout
     *
     * @param result The value to check
     *
     * @return True if the given value represents a class that is typically returned by the invocation of JSON layout
     *         API routes
     */
    private boolean usingJsonLayout(T result) {
        return Optional.ofNullable(result).map(Object::getClass).map(JsonNode.class::isAssignableFrom).orElse(false);
    }
}

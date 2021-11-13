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

package com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;

/**
 * Meta-annotation used to assign a mock server instance to the test class
 * <p><br>
 * Enables the mock server JUnit5 extension and lets the server being started with some preconfigured settings like e.g.
 * specific port number. For test classes annotated with this meta-annotation a mock server instance will be provided
 * for the time of the test execution. If currently no mock server instance exists a new one will be created. Otherwise,
 * the existing instance will be provided. The mock server instance will be shut-down at the very end of the test
 * execution.
 * <p><br>
 * The mocked server can be accessed via injected {@link MockServerClient} parameter. Such clients may be injected into
 * the constructor, lifecycle methods and of course into the actual tests. The server will return an HTTP-200 for each
 * requested resource by default. This behavior can be overwritten by each test but will be reset before the next test
 * class is executed.
 * <pre><code>
 *     {@literal @Test}
 *     void mockResponseTest(MockServerClient client) {
 *         final String resource = "/someResource";
 *
 *         // Default response for all requests: HTTP-200
 *         assertThat(sendRequest(resource)).extracting(Response::getResponseCode).isEqualTo(200);
 *
 *         // Cover the default response for the next two requests to "resource" and return a HTTP-401 instead
 *         client.when(request(resource), Times.exactly(2)).respond(createUnauthorizedResponse());
 *         assertThat(sendRequest(resource)).extracting(Response::getResponseCode).isEqualTo(401);
 *         assertThat(sendRequest(resource)).extracting(Response::getResponseCode).isEqualTo(401);
 *
 *         // After two requests the specified HTTP-401 expectation expires and the default response is visible again
 *         assertThat(sendRequest(resource)).extracting(Response::getResponseCode).isEqualTo(200);
 *     }
 * </code></pre>
 * The list of recorded requests will be reset after each individual test. The mocked server has HTTPS enabled by
 * default.
 * <p><br>
 * Just like the mock server it is also possible to automatically inject a remote API instance either as {@link
 * com.github.m0nk3y2k4.thetvdb.api.Proxy Proxy}, {@link com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI
 * RemoteAPI} or {@link java.util.function.Supplier Supplier&lt;RemoteAPI&gt;} parameter. Valid injection points are
 * constructors, lifecycle methods and the actual tests.
 * <pre><code>
 * package com.github.m0nk3y2k4.thetvdb.foobar;
 *
 * import static com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter.WithHttpsMockServer;
 *
 * // Imports...
 *
 * {@literal @WithHttpsMockServer}
 * class SomeAPITestClass {
 *
 *     {@literal @Test}
 *     void testSomething(MockServerClient client, Supplier<RemoteAPI> remoteAPISupplier, Proxy remoteAPI) {
 *         client.when(...);        // Access to the local REST endpoint e.g. to add expectations or to verify requests
 *
 *         // Prepared remote API pointing to the server mock
 *         APIConnection con = new APIConnection("foo", remoteAPISupplier);
 *         TheTVDBApi api = TheTVDBApiFactory.createApi("bar", remoteAPI);
 *     }
 * }
 * </code></pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(MockServerExtension.class)
@ExtendWith(HttpsMockServerExtension.class)
@MockServerSettings(ports = WithHttpsMockServer.PORT, perTestSuite = true)
@Target(ElementType.TYPE)
public @interface WithHttpsMockServer {

    /** Protocol used by the server mock */
    String PROTOCOL = "https";

    /** Host that the server mock is running on */
    String HOST = "localhost";

    /** Port to which the server mock should listen to for incoming requests */
    int PORT = 8709;
}


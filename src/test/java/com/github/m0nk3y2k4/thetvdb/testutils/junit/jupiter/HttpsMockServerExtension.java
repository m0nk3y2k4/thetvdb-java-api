/*
 * Copyright (C) 2019 - 2022 thetvdb-java-api Authors and Contributors
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

import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.AUTHORIZATION;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.GET;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.POST;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.createJWTResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.createSuccessResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.createUnauthorizedResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.jsonSchemaFromResource;
import static com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter.WithHttpsMockServer.HOST;
import static com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter.WithHttpsMockServer.PORT;
import static com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter.WithHttpsMockServer.PROTOCOL;
import static org.mockserver.model.HttpRequest.request;

import javax.net.ssl.HttpsURLConnection;

import com.github.m0nk3y2k4.thetvdb.api.Proxy;
import com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.mockserver.client.MockServerClient;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.matchers.TimeToLive;
import org.mockserver.matchers.Times;
import org.mockserver.mock.Expectation;
import org.mockserver.model.ClearType;
import org.mockserver.socket.tls.KeyStoreFactory;

/**
 * JUnit5 extension adding some additional features to the regular
 * {@link org.mockserver.junit.jupiter.MockServerExtension MockServerExtension}
 * <p><br>
 * Provides a resolver to let preconfigured {@link RemoteAPI} method parameters being injected in test classes. Also
 * adds settings for handling HTTPS request as well as some default expectations which enable the mock server to
 * automatically answer any received HTTPS request with some default response. It will also take care of resetting
 * expectations and recorded requests on the server mock on a regular basis.
 *
 * @see WithHttpsMockServer
 */
class HttpsMockServerExtension implements ParameterResolver, BeforeAllCallback, AfterEachCallback {

    /** Priority for the mocks general default response behavior */
    private static final int PRIO_DEFAULT = -10;
    /** Priority for specific route default response behavior */
    private static final int PRIO_ROUTE = -9;

    /** Client for accessing the mocked server running in the background */
    private final MockServerClient client = new MockServerClient(HOST, PORT);

    /** Preconfigured remote API pointing to the actual server mock */
    private final RemoteAPI mockServerRemote = new RemoteAPI.Builder().protocol(PROTOCOL).host(HOST).port(PORT).build();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return Proxy.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return mockServerRemote;
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        // Only log warnings and more severe information
        ConfigurationProperties.logLevel("WARN");

        // Reset all expectations and recordings before each test class to avoid test classes interfering with each other
        client.reset();

        // Set some HTTPS related stuff
        client.withSecure(true);
        // Ensure all connection using HTTPS will use the SSL context defined by MockServer to allow dynamically generated certificates to be accepted
        HttpsURLConnection.setDefaultSSLSocketFactory(new KeyStoreFactory(new MockServerLogger()).sslContext()
                .getSocketFactory());

        // Simulate the default behavior of the real TheTVDB.com RESTful API: all routes except for /login require authentication
        client.upsert(new Expectation(request("/login").withMethod(POST.getName())
                .withBody(jsonSchemaFromResource("login.json")), Times.unlimited(), TimeToLive.unlimited(), PRIO_ROUTE)
                .thenRespond(createJWTResponse()).withId("LOGIN"));
        client.upsert(new Expectation(request("/refresh_token").withHeader(AUTHORIZATION)
                .withMethod(GET.getName()), Times.unlimited(), TimeToLive.unlimited(), PRIO_ROUTE)
                .thenRespond(createJWTResponse()).withId("REFRESH_TOKEN"));
        client.upsert(new Expectation(request("/.*")
                .withHeader(AUTHORIZATION), Times.unlimited(), TimeToLive.unlimited(), PRIO_DEFAULT)
                .thenRespond(createSuccessResponse()).withId("AUTHORIZED"));
        client.upsert(new Expectation(request("/.*"), Times.unlimited(), TimeToLive.unlimited(), PRIO_DEFAULT)
                .thenRespond(createUnauthorizedResponse()).withId("UNAUTHORIZED"));

        // Do not enforce authentication when requesting resources with a test-prefix
        client.upsert(new Expectation(request("/test/.*"), Times.unlimited(), TimeToLive.unlimited(), PRIO_ROUTE)
                .thenRespond(createSuccessResponse()).withId("TEST"));
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        // Clear all requests received by the mock server so far
        client.clear(request("/.*"), ClearType.LOG);
    }
}

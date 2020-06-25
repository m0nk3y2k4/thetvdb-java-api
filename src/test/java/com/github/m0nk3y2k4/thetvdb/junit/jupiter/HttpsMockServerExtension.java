package com.github.m0nk3y2k4.thetvdb.junit.jupiter;

import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.createJWTResponse;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.createSuccessResponse;
import static com.github.m0nk3y2k4.thetvdb.junit.jupiter.WithHttpsMockServer.HOST;
import static com.github.m0nk3y2k4.thetvdb.junit.jupiter.WithHttpsMockServer.PORT;
import static com.github.m0nk3y2k4.thetvdb.junit.jupiter.WithHttpsMockServer.PROTOCOL;
import static org.mockserver.model.HttpRequest.request;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.function.Supplier;

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
import org.mockserver.logging.MockServerLogger;
import org.mockserver.matchers.TimeToLive;
import org.mockserver.matchers.Times;
import org.mockserver.mock.Expectation;
import org.mockserver.model.ClearType;
import org.mockserver.socket.tls.KeyStoreFactory;

/**
 * JUnit5 extension adding some additional features to the regular {@link org.mockserver.junit.jupiter.MockServerExtension MockServerExtension}
 * <p><br>
 * Provides a resolver to let preconfigured {@link RemoteAPI} method parameters being injected in test classes.
 * Also adds settings for handling HTTPS reqeuest as well as some default expectations which enable the mock server
 * to automatically answer any received HTTPS request with some default response. It will also take care of resetting
 * expactations and recorded requests on the server mock on a regular basis.
 *
 * @see WithHttpsMockServer
 */
public class HttpsMockServerExtension implements ParameterResolver, BeforeAllCallback, AfterEachCallback {

    /** Priority for the mocks general default response behavior */
    private static final int PRIO_DEFAULT = -10;
    /** Priority for specific route default response behavior */
    private static final int PRIO_ROUTE = -9;

    /** Client for accessing the mocked server running in the background */
    private final MockServerClient client = new MockServerClient(HOST, PORT);

    /** Preconfigured remote API pointing to the actual server mock */
    private final RemoteAPI mockServerRemote = new RemoteAPI.Builder().protocol(PROTOCOL).host(HOST).port(PORT).build();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getParameter(parameterContext).isPresent();
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getParameter(parameterContext).orElseThrow(() -> new ParameterResolutionException("XX"));
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        // Reset all expactations and recordings before each test class to avoid test classes interfering with each other
        client.reset();

        // Set some HTTPS related stuff
        client.withSecure(true);
        // Ensure all connection using HTTPS will use the SSL context defined by MockServer to allow dynamically generated certificates to be accepted
        HttpsURLConnection.setDefaultSSLSocketFactory(new KeyStoreFactory(new MockServerLogger()).sslContext().getSocketFactory());

        // Setup some expectations describing the default behavior for specific resources
        client.upsert(new Expectation(request("/.*"), Times.unlimited(), TimeToLive.unlimited(), PRIO_DEFAULT).thenRespond(createSuccessResponse()));
        client.upsert(new Expectation(request("/login"), Times.unlimited(), TimeToLive.unlimited(), PRIO_ROUTE).thenRespond(createJWTResponse()));
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        // Clear all requests received by the mock server so far
        client.clear(request("/.*"), ClearType.LOG);
    }

    /**
     * Checks if the requested parameter can be resolved by this extension and returns an Optional representing the result of this check.
     *
     * @param parameterContext Context representing the parameter requested for injection
     *
     * @return Optional containing the requested parameter or an empty Optional in case the parameter can not be resolved by this extension
     */
    private Optional<Object> getParameter(ParameterContext parameterContext) {
        Class<?> parameterClass = parameterContext.getParameter().getType();
        if (Proxy.class.isAssignableFrom(parameterClass)) {
            return Optional.of(mockServerRemote);
        } else if (Supplier.class.isAssignableFrom(parameterClass) &&
                Proxy.class.isAssignableFrom((Class<?>)((ParameterizedType)parameterContext.getParameter()
                        .getParameterizedType()).getActualTypeArguments()[0])) {
            return Optional.of((Supplier<RemoteAPI>) () -> mockServerRemote);
        }
        return Optional.empty();
    }
}

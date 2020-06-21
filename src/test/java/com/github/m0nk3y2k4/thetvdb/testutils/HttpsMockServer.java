package com.github.m0nk3y2k4.thetvdb.testutils;

import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.CONTENT_LENGTH;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.OK_200;
import static org.mockserver.model.HttpStatusCode.UNAUTHORIZED_401;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.mock.Expectation;
import org.mockserver.model.Header;
import org.mockserver.model.HttpResponse;
import org.mockserver.socket.tls.KeyStoreFactory;

/**
 * Utilility class for mocking a HTTPS capable REST service
 * <p><br>
 * A dedicated JUnit 5 extension would not allow access to the actual server client held by the {@link MockServerExtension}.
 * However, as this client is needed in order to apply HTTPS related default settings as well as some default expectations
 * test class implementations which require a mocked REST endpoint have to extend this class.
 * <p><br>
 * The following steps are required in order to use a HTTPS capable local REST endpoint for testing:
 * <ol>
 *     <li>Let the test class extend this mock server class</li>
 *     <li>Annotate the test class with {@link WithMockServerExtension}</li>
 * </ol>
 * For example:
 * <pre>{@code
 * package com.github.m0nk3y2k4.thetvdb.foobar;
 *
 * import static com.github.m0nk3y2k4.thetvdb.testutils.HttpsMockServer.WithMockServerExtension;
 *
 * // Imports...
 *
 * @WithMockServerExtension
 * class SomeAPITestClass extends HttpsMockServer {
 *
 *     @Test
 *     void TestSomething(MockServerClient client) {
 *         client.when(...);        // Access to the local REST endpoint e.g. to add expectations or to verify requests
 *
 *         // Prepared remote API pointing to the server mock
 *         APIConnection con = new APIConnection("foo", () -> HttpsMockServer::remoteAPI);
 *         TheTVDBApi api = TheTVDBApiFactory.createApi("bar", remoteAPI());
 *     }
 * }
 * }</pre>
 */
public abstract class HttpsMockServer {

    /** Default connection settings for the server mock */
    private static final String PROTOCOL = "https";
    private static final String HOST = "localhost";
    private static final int PORT = 8709;

    /** Preconfigured remote API pointing to the actual server mock */
    private static final RemoteAPI MOCKSERVER_REMOTE = new RemoteAPI.Builder().protocol(PROTOCOL).host(HOST).port(PORT).build();

    /**
     * Meta-annotation used to assign a mock server instance to the test class
     * <p><br>
     * Enables the mock server JUnit5 extension and lets the server being started with some preconfigured settings like e.g. specific
     * port number. For test classes annotated with this meta-annotation a new mock server instance will be created during the
     * initalization of the test execution and will be shutdown again after all tests have been executed. The mocked server can
     * be accessed via injected {@link MockServerClient} parameter. Such clients may be injected into the constructor, lifecycle
     * methods and of course into the actual tests.
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @ExtendWith(MockServerExtension.class)
    @MockServerSettings(ports = {PORT})
    @Target({ElementType.TYPE})
    public @interface WithMockServerExtension {}

    /** JSON String representing a simple <i>Success</i> response content */
    protected static final String JSON_SUCCESS = new ObjectMapper().createObjectNode().put("Success", true).toString();
    /** JSON String representing a HTTP-401 <i>Not Authorized</i> response content */
    protected static final String JSON_ERROR_NOTAUTHORIZED = new ObjectMapper().createObjectNode().put("Error", "Not Authorized").toString();
    /** JSON String representing some dummy payload data e.g. to be used for POST requests */
    protected static final String JSON_DATA = new ObjectMapper().createObjectNode().put("Some", "JSON payload").toString();
    /** JSON String representing a dummy JWT response. It's not a real token but the content is valid with regards to the JWT format. */
    protected static final String JSON_JWT = new ObjectMapper().createObjectNode().put("token", "Header.Payload.Signature").toString();

    /** ID of the <i>/login</i> routes default expectation */
    protected static final String EXP_LOGIN = UUID.randomUUID().toString();

    /**
     * Setup some specific setting required for using HTTPS as remote API protocol. Will be triggered once for the whole test class
     * before the first test is executed.
     *
     * @param client Injected client for accessing the mock server
     */
    @BeforeAll
    static void initHttps(MockServerClient client) {
        // Ensure all connection using HTTPS will use the SSL context defined by MockServer to allow dynamically generated certificates to be accepted
        HttpsURLConnection.setDefaultSSLSocketFactory(new KeyStoreFactory(new MockServerLogger()).sslContext().getSocketFactory());
        client.withSecure(true);
    }

    /**
     * Setup some expectations describing the default behavior for specific resources. This behavior can be overwritten by a specific
     * test if required. However, it will be reset prior to the next test execution.
     *
     * @param client Injected client for accessing the mock server
     */
    @BeforeEach
    void initDefaultExpectations(MockServerClient client) {
        client.upsert(new Expectation(request("/login")).withId(EXP_LOGIN).thenRespond(response().withHeader(contentLenghth(JSON_JWT)).withBody(JSON_JWT)));
    }

    /**
     * Returns a preconfigured remote API endpoint pointing to the actual server mock
     *
     * @return Remote API endpoint pointing to the mocked server
     */
    protected static RemoteAPI remoteAPI() {
        return MOCKSERVER_REMOTE;
    }

    /**
     * Creates a new <i>"Content-Length"</i> header which can be added to some mock server response. Will invoke the
     * {@link String#length()} method of the given String in order to determine it's length.
     *
     * @param content The content of the mock server response
     *
     * @return Response header specifying the length of the given content String
     */
    protected static Header contentLenghth(@Nonnull String content) {
        Parameters.validateNotNull(content, "The content String must not be null");
        return contentLenghth(content.length());
    }

    /**
     * Creates a new <i>"Content-Length"</i> header which can be added to some mock server response.
     *
     * @param length The length of the response content
     *
     * @return Response header specifying the given content length
     */
    protected static Header contentLenghth(int length) {
        return header(CONTENT_LENGTH, length);
    }

    /**
     * Creates a simple HTTP-200 <i>"OK"</i> response. The response body contains some dummy JSON success message.
     *
     * @return New preconfigured HTTP response with a HTTP-200 status
     */
    protected static HttpResponse createSuccessResponse() {
        return response().withHeader(contentLenghth(JSON_SUCCESS)).withStatusCode(OK_200.code()).withReasonPhrase(OK_200.reasonPhrase())
                .withBody(JSON_SUCCESS);
    }

    /**
     * Creates a simple HTTP-401 <i>"Unauthorized"</i> response. The response body contains some dummy JSON authorization failure message.
     *
     * @return New preconfigured HTTP response with a HTTP-401 status
     */
    protected static HttpResponse createUnauthorizedResponse() {
        return response().withHeader(contentLenghth(JSON_ERROR_NOTAUTHORIZED)).withStatusCode(UNAUTHORIZED_401.code())
                .withReasonPhrase(UNAUTHORIZED_401.reasonPhrase()).withBody(JSON_ERROR_NOTAUTHORIZED);
    }

}

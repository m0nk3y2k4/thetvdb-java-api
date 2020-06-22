package com.github.m0nk3y2k4.thetvdb.testutils;

import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.ACCEPT;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.ACCEPT_LANGUAGE;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.AUTHORIZATION;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.CONTENT_LENGTH;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.CONTENT_TYPE;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.USER_AGENT;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.OK_200;
import static org.mockserver.model.HttpStatusCode.UNAUTHORIZED_401;
import static org.mockserver.model.NottableString.not;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.matchers.TimeToLive;
import org.mockserver.matchers.Times;
import org.mockserver.mock.Expectation;
import org.mockserver.model.Header;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;
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
 * <p><br>
 * The mock server is configured to return a <i>HTTP-200 OK</i> response for all requests by default. However, this behavior
 * can be overwritten by the specific tests if needed:
 * <pre>{@code
 *     @Test
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

    /** Priority for the mocks general default response behavior */
    private static final int PRIO_DEFAULT = -10;
    /** Priority for specific route default response behavior */
    private static final int PRIO_ROUTE = -9;

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
     * Setup some expectations describing the default behavior for specific resources. These expectations have negative priorities and
     * can be overwritten by a specific test if required.
     *
     * @param client Injected client for accessing the mock server
     */
    @BeforeAll
    static void initDefaultExpectations(MockServerClient client) {
        client.upsert(new Expectation(request("/.*"), Times.unlimited(), TimeToLive.unlimited(), PRIO_DEFAULT).thenRespond(createSuccessResponse()));
        client.upsert(new Expectation(request("/login"), Times.unlimited(), TimeToLive.unlimited(), PRIO_ROUTE).thenRespond(createJWTResponse()));
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
     * Returns matchers for the default HTTP headers which will be set when communicating with the remote API. Which haders are actually
     * set depends on whether the underlying API session has already been authorized or not. If <em>{@code withAuthorization}</em> is
     * set to TRUE, the returned array will contain matchers verifying that the authentication related headers exist and contain some reasonable
     * values. If set to FALSE the array will contain matchers verifying that no authentication related headers exist at all.
     *
     * @param withAuthorization Authorization related headers should be present or not
     *
     * @return Array of HTTP header matchers according to the given parameters
     */
    protected Header[] defaultAPIHttpHeaders(boolean withAuthorization) {
        return new Header[] {header(CONTENT_TYPE, "application/json; charset=utf-8"),
                header(ACCEPT, "application/json, application/vnd.thetvdb.v3.0.0"),
                header(USER_AGENT, "Mozilla/5.0"),
                withAuthorization ? header(AUTHORIZATION, "Bearer [A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$") : header(not(AUTHORIZATION)),
                withAuthorization ? header(ACCEPT_LANGUAGE, "^[a-z]{2}|[A-Z]{2}$") : header(not(ACCEPT_LANGUAGE))};
    }

    /**
     * Creates a simple HTTP-200 <i>"OK"</i> response. The response body contains some dummy JSON success message.
     *
     * @return New preconfigured HTTP response with a HTTP-200 status
     */
    protected static HttpResponse createSuccessResponse() {
        return createResponse(OK_200, JSON_SUCCESS);
    }

    /**
     * Creates a simple HTTP-200 <i>"OK"</i> response. The response body contains some dummy token in a valid JWT format.
     *
     * @return New preconfigured HTTP response with a HTTP-200 status
     */
    protected static HttpResponse createJWTResponse() {
        return createResponse(OK_200, JSON_JWT);
    }

    /**
     * Creates a simple HTTP-401 <i>"Unauthorized"</i> response. The response body contains some dummy JSON authorization failure message.
     *
     * @return New preconfigured HTTP response with a HTTP-401 status
     */
    protected static HttpResponse createUnauthorizedResponse() {
        return createResponse(UNAUTHORIZED_401, JSON_ERROR_NOTAUTHORIZED);
    }

    /**
     * Creates a simple response with the given HTTP <em>{@code status}</em> and the given <em>{@code content}</em> set to it's body.
     *
     * @param status The status of the response
     * @param content The content to be contained in the responses body section
     *
     * @return New preconfigured HTTP response with the given status and content
     */
    protected static HttpResponse createResponse(HttpStatusCode status, String content) {
        return response().withHeader(contentLenghth(content)).withStatusCode(status.code()).withReasonPhrase(status.reasonPhrase()).withBody(content);
    }

}

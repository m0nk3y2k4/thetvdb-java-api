package com.github.m0nk3y2k4.thetvdb.testutils;

import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.ACCEPT;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.ACCEPT_LANGUAGE;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.AUTHORIZATION;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.CONTENT_LENGTH;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.CONTENT_TYPE;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.USER_AGENT;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.OK_200;
import static org.mockserver.model.HttpStatusCode.UNAUTHORIZED_401;
import static org.mockserver.model.NottableString.not;

import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;
import org.mockserver.model.Header;
import org.mockserver.model.Headers;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.model.JsonSchemaBody;

/**
 * Utilility class providing useful methods for working with mock servers
 * <p><br>
 * This class offers quick access to functionality which is often required when working with mock servers. This includes prepared
 * JSON Strings, creation of common HTTP request headers as well as simple preconfigured responses for example to return a HTTP-200
 * or HTTP-401 response.
 */
public abstract class MockServerUtil {

    /** JSON String representing a simple <i>Success</i> response content */
    public static final String JSON_SUCCESS = "{\"Success\":true}";

    /** JSON String representing a HTTP-401 <i>Not Authorized</i> response content */
    public static final String JSON_ERROR_NOTAUTHORIZED = "{\"Error\":\"Not Authorized\"}";

    /** JSON String representing some dummy payload data e.g. to be used for POST requests */
    public static final String JSON_DATA = "{\"Some\":\"JSON payload\"}";

    /** JSON String representing a dummy JWT response. It's not a real token but the content is valid with regards to the JWT format. */
    public static final String JSON_JWT = "{\"token\":\"Header.Payload.Signature\"}";

    private MockServerUtil() {}         // Hidden constructor. Only static methods

    /**
     * Creates a new <i>"Content-Length"</i> header which can be added to some mock server response. Will invoke the
     * {@link String#length()} method of the given String in order to determine it's length.
     *
     * @param content The content of the mock server response
     *
     * @return Response header specifying the length of the given content String
     */
    public static Header contentLenghth(@Nonnull String content) {
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
    public static Header contentLenghth(int length) {
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
    public static Headers defaultAPIHttpHeaders(boolean withAuthorization) {
        return new Headers(header(CONTENT_TYPE, "application/json; charset=utf-8"),
                header(ACCEPT, "application/json, application/vnd.thetvdb.v3.0.0"),
                header(USER_AGENT, "Mozilla/5.0"),
                withAuthorization ? header(AUTHORIZATION, "Bearer [A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$") : header(not(AUTHORIZATION)),
                withAuthorization ? header(ACCEPT_LANGUAGE, "^[a-z]{2}|[A-Z]{2}$") : header(not(ACCEPT_LANGUAGE)));
    }

    /**
     * Creates a simple HTTP-200 <i>"OK"</i> response. The response body contains some dummy JSON success message.
     *
     * @return New preconfigured HTTP response with a HTTP-200 status
     */
    public static HttpResponse createSuccessResponse() {
        return createResponse(OK_200, JSON_SUCCESS);
    }

    /**
     * Creates a simple HTTP-200 <i>"OK"</i> response. The response body contains some dummy token in a valid JWT format.
     *
     * @return New preconfigured HTTP response with a HTTP-200 status
     */
    public static HttpResponse createJWTResponse() {
        return createResponse(OK_200, JSON_JWT);
    }

    /**
     * Creates a simple HTTP-401 <i>"Unauthorized"</i> response. The response body contains some dummy JSON authorization failure message.
     *
     * @return New preconfigured HTTP response with a HTTP-401 status
     */
    public static HttpResponse createUnauthorizedResponse() {
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
    public static HttpResponse createResponse(HttpStatusCode status, String content) {
        return response().withHeader(contentLenghth(content)).withStatusCode(status.code()).withReasonPhrase(status.reasonPhrase()).withBody(content);
    }

    /**
     * Creates a new JSON schema body based on the given schema resource file
     *
     * @param schemaName Name of the JSON schema resource file
     *
     * @return Mock server JSON schema body based on the given resource file
     */
    public static JsonSchemaBody jsonSchemaFromResource(String schemaName) {
        return JsonSchemaBody.jsonSchemaFromResource("json/schema/" + schemaName);
    }
}

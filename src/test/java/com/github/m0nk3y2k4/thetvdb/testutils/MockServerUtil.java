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

package com.github.m0nk3y2k4.thetvdb.testutils;

import static com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi.Version.API_VERSION;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.ACCEPT;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.ACCEPT_LANGUAGE;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.AUTHORIZATION;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.CONTENT_LENGTH;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.CONTENT_TYPE;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.USER_AGENT;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpStatusCode.OK_200;
import static org.mockserver.model.HttpStatusCode.UNAUTHORIZED_401;
import static org.mockserver.model.NottableString.not;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;
import org.mockserver.model.Header;
import org.mockserver.model.Headers;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.model.JsonBody;
import org.mockserver.model.JsonSchemaBody;
import org.mockserver.model.Parameter;

/**
 * Utility class providing useful methods for working with mock servers
 * <p><br>
 * This class offers quick access to functionality which is often required when working with mock servers. This includes
 * prepared JSON Strings, creation of common HTTP request headers as well as simple preconfigured responses for example
 * to return an HTTP-200 or HTTP-401 response.
 */
public final class MockServerUtil {

    /** JSON String representing a simple <i>Success</i> response content */
    public static final String JSON_SUCCESS = "{\"Success\":true}";

    /** JSON String representing an HTTP-401 <i>Not Authorized</i> response content */
    public static final String JSON_ERROR_NOTAUTHORIZED = "{\"message\":\"Not Authorized\"}";

    /** JSON String representing some dummy payload data e.g. to be used for POST requests */
    public static final String JSON_DATA = "{\"Some\":\"JSON payload\"}";

    /** JSON String representing a dummy JWT response (valid with regard to the JWT format) */
    public static final String JSON_JWT = "{\"token\":\"Header.Payload.Signature\"}";

    private MockServerUtil() {}         // Hidden constructor. Only static methods

    /**
     * Creates a new <i>"Content-Length"</i> header which can be added to some mock server response. Will invoke the
     * {@link String#length()} method of the given String in order to determine its length.
     *
     * @param content The content of the mock server response
     *
     * @return Response header specifying the length of the given content String
     */
    public static Header contentLength(@Nonnull CharSequence content) {
        Parameters.validateNotNull(content, "The content String must not be null");
        return contentLength(content.length());
    }

    /**
     * Creates a new <i>"Content-Length"</i> header which can be added to some mock server response.
     *
     * @param length The length of the response content
     *
     * @return Response header specifying the given content length
     */
    public static Header contentLength(int length) {
        return header(CONTENT_LENGTH, length);
    }

    /**
     * Returns matchers for the default HTTP headers which will be set when communicating with the remote API. Which
     * headers are actually set depends on whether the underlying API session has already been authorized or not. The
     * object returned by this method will contain matchers verifying the absence of any authentication related
     * headers.
     *
     * @return HTTP headers object including matchers verifying the absence of authorization headers
     *
     * @see #defaultAPIHttpHeadersWithAuthorization()
     */
    public static Headers defaultAPIHttpHeaders() {
        return defaultAPIHttpHeaders(false);
    }

    /**
     * Returns matchers for the default HTTP headers which will be set when communicating with the remote API. Which
     * headers are actually set depends on whether the underlying API session has already been authorized or not. The
     * object returned by this method will contain matchers verifying that the authentication related headers exist and
     * contain some reasonable values.
     *
     * @return HTTP headers object including matchers verifying the existence of authorization headers
     *
     * @see #defaultAPIHttpHeaders()
     */
    public static Headers defaultAPIHttpHeadersWithAuthorization() {
        return defaultAPIHttpHeaders(true);
    }

    /**
     * Returns matchers for the default HTTP headers which will be set when communicating with the remote API. Which
     * headers are actually set depends on whether the underlying API session has already been authorized or not. If
     * <em>{@code withAuthorization}</em> is set to TRUE, the returned object will contain matchers verifying that the
     * authentication related headers exist and contain some reasonable values. If set to FALSE the object will contain
     * matchers verifying the absence of any authentication related headers.
     *
     * @param withAuthorization Authorization related headers should be present or not
     *
     * @return HTTP headers object containing matchers according to the given parameters
     */
    private static Headers defaultAPIHttpHeaders(boolean withAuthorization) {
        return new Headers(header(CONTENT_TYPE, "application/json; charset=utf-8"),
                header(ACCEPT, "application/json, application/vnd.thetvdb." + API_VERSION),
                header(USER_AGENT, "Mozilla/5.0"),
                withAuthorization
                        ? header(AUTHORIZATION, "Bearer [A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$")
                        : header(not(AUTHORIZATION)),
                withAuthorization
                        ? header(ACCEPT_LANGUAGE, "^[a-z]{2}|[A-Z]{2}$")
                        : header(not(ACCEPT_LANGUAGE)));
    }

    /**
     * Tries to create a set of mock server headers from the given response object. For this, the responses {@link
     * ResponseData#getDTO()} method must return a {@link Map} as data object. The key/value pairs of this map will be
     * converted into their String representation and will be set as key/value pairs on the returned headers object.
     *
     * @param response The test response based on which the headers object should be created
     *
     * @return Headers object with key/value pairs from the given response object. In case the response DTO is not
     *         compatible an empty headers object without any keys or values will be returned.
     */
    public static <T> Headers getHeadersFrom(ResponseData<APIResponse<T>> response) {
        Headers headers = new Headers();
        if (response.getDTO().getData() instanceof Map) {
            Function<Map.Entry<?, ?>, Map.Entry<String, String>> toStringValues = e ->
                    new AbstractMap.SimpleImmutableEntry<>(Objects.toString(e.getKey(), null), Objects
                            .toString(e.getValue(), null));
            Consumer<Map.Entry<String, String>> addHeader = e -> headers.withEntry(e.getKey(), e.getValue());

            ((Map<?, ?>)response.getDTO().getData()).entrySet().stream().map(toStringValues).forEach(addHeader);
        }
        return headers;
    }

    /**
     * Creates a simple HTTP-200 <i>"OK"</i> response. The response body contains some dummy JSON success message.
     *
     * @return New preconfigured HTTP response with an HTTP-200 status
     */
    public static HttpResponse createSuccessResponse() {
        return createResponse(OK_200, JSON_SUCCESS);
    }

    /**
     * Creates a simple HTTP-200 <i>"OK"</i> response. The response body contains some dummy token in a valid JWT
     * format.
     *
     * @return New preconfigured HTTP response with an HTTP-200 status
     */
    public static HttpResponse createJWTResponse() {
        return createResponse(OK_200, JSON_JWT);
    }

    /**
     * Creates a simple HTTP-401 <i>"Unauthorized"</i> response. The response body contains some dummy JSON
     * authorization failure message.
     *
     * @return New preconfigured HTTP response with an HTTP-401 status
     */
    public static HttpResponse createUnauthorizedResponse() {
        return createResponse(UNAUTHORIZED_401, JSON_ERROR_NOTAUTHORIZED);
    }

    /**
     * Creates a simple response with the given HTTP <em>{@code status}</em> and the given <em>{@code content}</em> set
     * to it's body.
     *
     * @param status  The status of the response
     * @param content The content to be contained in the responses body section
     *
     * @return New preconfigured HTTP response with the given status and content
     */
    public static HttpResponse createResponse(HttpStatusCode status, String content) {
        return HttpResponse.response().withHeader(contentLength(content)).withStatusCode(status.code())
                .withReasonPhrase(status.reasonPhrase()).withBody(content);
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

    /**
     * Creates a new mock server HTTP-request for the given path and request method and without any query parameters
     *
     * @param path   The requests path
     * @param method The HTTP request method
     *
     * @return Mock server HTTP request based on the given parameters
     */
    public static HttpRequest request(String path, HttpRequestMethod method) {
        return request(path, method, Parameter.param(not(".*"), not(".*")));
    }

    /**
     * Creates a new mock server HTTP-request for the given path, request method and an optional set of query
     * parameters
     *
     * @param path       The requests path
     * @param method     The HTTP request method
     * @param parameters Optional query parameters
     *
     * @return Mock server HTTP request based on the given parameters
     */
    public static HttpRequest request(String path, HttpRequestMethod method, Parameter... parameters) {
        return HttpRequest.request(path).withMethod(method.getName()).withQueryStringParameters(parameters);
    }

    /**
     * Creates a new mock server HTTP response with a JSON body based on the given response object
     *
     * @param response Some response data test instance
     *
     * @return Mock server HTTP response with a JSON body based on the given response object
     *
     * @throws IOException If an I/O exception occurs while processing the response object
     */
    public static <T> HttpResponse jsonResponse(ResponseData<T> response) throws IOException {
        return HttpResponse.response().withBody(JsonBody.json(response.getJsonString()));
    }
}

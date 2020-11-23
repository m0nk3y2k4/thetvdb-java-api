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

package com.github.m0nk3y2k4.thetvdb.internal.connection;

import static com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi.Version.API_VERSION;
import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_BAD_METHOD_ERROR;
import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_CONFLICT_ERROR;
import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_NOT_FOUND_ERROR;
import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_SERVICE_UNAVAILABLE;
import static com.github.m0nk3y2k4.thetvdb.internal.connection.APISession.Status;
import static com.github.m0nk3y2k4.thetvdb.internal.connection.APISession.Status.NOT_AUTHORIZED;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.ACCEPT;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.ACCEPT_LANGUAGE;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.ALLOW;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.AUTHORIZATION;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.CONTENT_TYPE;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpHeaders.USER_AGENT;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APICommunicationException;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APINotAuthorizedException;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.AuthenticationAPI;
import com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Preconditions;

/**
 * Main class for handling communication with the <i>TheTVDB.com</i> REST remote service on a technical level
 * <p><br>
 * It provides options to invoke different types of HTTP requests to any given API route. It will also maintain
 * additional session information like API key, the JWT token and authentication status and will handle the Bearer
 * authentication for each request.
 * <p><br>
 * Although it is highly recommended to first off authenticate a newly created API connection manually via the {@link
 * com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi#login TheTVDBApi.login()} method, this API connection also provides some
 * automatic on-demand authorization. In case an attempt is made to call any of the API routes without previous
 * authorization, the connection will automatically try to resolve the HTTP-401 state by invoking the login() function
 * followed by a retry of requesting the original resource.
 */
public class APIConnection {

    /** Error message for max on-demand authentication retries exceeded */
    static final String ERR_MAX_RETRY_EXCEEDED = "Could not connect to API service after %d retries. Please check your API-Key.";

    /** Maximum number of retries for automatic on-demand authentication */
    static final int MAX_AUTHENTICATION_RETRY_COUNT = 3;

    /** Session used for API communication */
    private final APISession session;

    /** Remote endpoint used for API communication (default: <i>TheTVDB.com</i>) */
    private final RemoteAPI remoteAPI;

    /**
     * Creates a new <i>TheTVDB.com</i> API connection using the given <em>{@code apiKey}</em> for remote service
     * authentication. The given key must be a valid
     * <a href="https://www.thetvdb.com/member/api">TheTVDB.com API Key</a>.
     * <p><br>
     * <b>NOTE:</b> Objects created with this constructor <u>can not</u> be used for calls to the remote API's
     * <a href="https://api.thetvdb.com/swagger#/Users">/users</a> routes. These calls require extended authentication
     * using an additional <em>{@code userKey}</em> and <em>{@code userName}</em>.
     *
     * @param apiKey Valid <i>TheTVDB.com</i> API-Key
     *
     * @see #APIConnection(String, String, String) APIConnection(apiKey, userName, userKey)
     */
    public APIConnection(@Nonnull String apiKey) {
        this(new APISession(apiKey));
    }

    /**
     * Creates a new API connection using the given <em>{@code apiKey}</em> for remote service authentication. The given
     * key must be a valid <a href="https://www.thetvdb.com/member/api">TheTVDB.com API Key</a>. All outgoing
     * communication will be directed towards the given remote endpoint.
     * <p><br>
     * <b>NOTE:</b> Objects created with this constructor <u>can not</u> be used for calls to the remote API's
     * <a href="https://api.thetvdb.com/swagger#/Users">/users</a> routes. These calls require extended authentication
     * using an additional <em>{@code userKey}</em> and <em>{@code userName}</em>.
     *
     * @param apiKey Valid <i>TheTVDB.com</i> API-Key
     * @param remote Supplier providing the remote API endpoint to be used by this connection
     *
     * @see #APIConnection(String, String, String, Supplier) APIConnection(apiKey, userName, userKey, remote)
     */
    public APIConnection(@Nonnull String apiKey, @Nonnull Supplier<RemoteAPI> remote) {
        this(new APISession(apiKey), remote);
    }

    /**
     * Creates a new <i>TheTVDB.com</i> API connection using the given credentials for remote service authentication.
     * The given <em>{@code apiKey}</em> must be a valid
     * <a href="https://www.thetvdb.com/member/api">TheTVDB.com API Key</a>. The <em>{@code userKey}</em> and
     * <em>{@code userName}</em> must refer to a registered <i>TheTVDB.com</i> user account.
     *
     * @param apiKey   Valid <i>TheTVDB.com</i> API-Key
     * @param userKey  Valid <i>TheTVDB.com</i> user key (also referred to as "Unique ID")
     * @param userName Registered <i>TheTVDB.com</i> user name
     *
     * @see #APIConnection(String) APIConnection(apiKey)
     */
    public APIConnection(@Nonnull String apiKey, @Nonnull String userKey, @Nonnull String userName) {
        this(new APISession(apiKey, userKey, userName));
    }

    /**
     * Creates a new API connection using the given credentials for remote service authentication. The given <em>{@code
     * apiKey}</em> must be a valid
     * <a href="https://www.thetvdb.com/member/api">TheTVDB.com API Key</a>. The <em>{@code userKey}</em> and
     * <em>{@code userName}</em> must refer to a registered <i>TheTVDB.com</i> user account. All outgoing communication
     * will be directed towards the given remote endpoint.
     *
     * @param apiKey   Valid <i>TheTVDB.com</i> API-Key
     * @param userKey  Valid <i>TheTVDB.com</i> user key (also referred to as "Unique ID")
     * @param userName Registered <i>TheTVDB.com</i> user name
     * @param remote   Supplier providing the remote API endpoint to be used by this connection
     *
     * @see #APIConnection(String, Supplier) APIConnection(apiKey, remote)
     */
    public APIConnection(@Nonnull String apiKey, @Nonnull String userKey, @Nonnull String userName,
            @Nonnull Supplier<RemoteAPI> remote) {
        this(new APISession(apiKey, userKey, userName), remote);
    }

    /**
     * Creates a new <i>TheTVDB.com</i> API connection using the given <em>{@code session}</em> for remote service
     * authentication. The functionality offered by this API connection strongly depends on the current configuration of
     * the given session object.
     *
     * @param session Session used for API communication and authentication
     */
    private APIConnection(@Nonnull APISession session) {
        this(session, () -> new RemoteAPI.Builder().build());       // Default: regular TheTVDB.com remote
    }

    /**
     * Creates a new connection to the given <em>{@code remote}</em> API using the given <em>{@code session}</em> for
     * remote service authentication. The functionality offered by this API connection strongly depends on the current
     * configuration of the given session object.
     *
     * @param session Session used for API communication and authentication
     * @param remote  Specific remote communication endpoint (e.g. if the <i>TheTVDB.com</i> API should be accessed via
     *                proxy)
     */
    private APIConnection(@Nonnull APISession session, @Nonnull Supplier<RemoteAPI> remote) {
        Parameters.validateNotNull(session, "API session must not be NULL");
        Parameters.validateCondition(remoteSupplier -> Optional.ofNullable(remoteSupplier).map(Supplier::get)
                        .isPresent(), remote,
                new IllegalArgumentException("Remote endpoint for this connection needs to be specified"));

        this.session = session;
        this.remoteAPI = remote.get();
    }

    /**
     * Invokes a new <em>{@code GET}</em> request onto the given resource and returns the response as raw, unmodified
     * JSON. In case this connection has not yet been authorized an automated authorization attempt will be triggered.
     *
     * @param resource The remote API resource to be invoked
     *
     * @return The raw JSON response as received by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public JsonNode sendGET(@Nonnull String resource) throws APIException {
        return sendRequest(new GetRequest(resource));
    }

    /**
     * Invokes a new <em>{@code POST}</em> request onto the given resource and returns the response as raw, unmodified
     * JSON. In case this connection has not yet been authorized an automated authorization attempt will be triggered.
     *
     * @param resource The remote API resource to be invoked
     * @param data     The request payload to be pushed to the remote service
     *
     * @return The raw JSON response as received by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public JsonNode sendPOST(@Nonnull String resource, @Nonnull String data) throws APIException {
        return sendRequest(new PostRequest(resource, data));
    }

    /**
     * Invokes a new <em>{@code HEAD}</em> request onto the given resource and returns the response as raw, unmodified
     * JSON. In case this connection has not yet been authorized an automated authorization attempt will be triggered.
     *
     * @param resource The remote API resource to be invoked
     *
     * @return The raw JSON response as received by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public JsonNode sendHEAD(@Nonnull String resource) throws APIException {
        return sendRequest(new HeadRequest(resource));
    }

    /**
     * Invokes a new <em>{@code DELETE}</em> request onto the given resource and returns the response as raw, unmodified
     * JSON. In case this connection has not yet been authorized an automated authorization attempt will be triggered.
     *
     * @param resource The remote API resource to be invoked
     *
     * @return The raw JSON response as received by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public JsonNode sendDELETE(@Nonnull String resource) throws APIException {
        return sendRequest(new DeleteRequest(resource));
    }

    /**
     * Invokes a new <em>{@code PUT}</em> request onto the given resource and returns the response as raw, unmodified
     * JSON. In case this connection has not yet been authorized an automated authorization attempt will be triggered.
     *
     * @param resource The remote API resource to be invoked
     *
     * @return The raw JSON response as received by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public JsonNode sendPUT(@Nonnull String resource) throws APIException {
        return sendRequest(new PutRequest(resource));
    }

    /**
     * Sets the JWT token of the underlying session which will then be used further communication authentication
     *
     * @param token The new JWT session token issued by the remote service
     *
     * @throws APIException If the given token is <em>{@code null}</em>, an empty character sequence or does not match
     *                      the regular JWT format
     */
    public void setToken(@Nonnull String token) throws APIException {
        session.setToken(token);
    }

    /**
     * Sets the current status of the underlying session. If the given status parameter is <em>{@code null}</em> the
     * sessions status will be reset to {@link Status#NOT_AUTHORIZED}.
     *
     * @param status The new session status
     */
    public void setStatus(Status status) {
        session.setStatus(status);
    }

    /**
     * Returns the current {@link Status} of the underlying session. This status indicates that...
     * <ul>
     * <li>{@link Status#NOT_AUTHORIZED}: The session has not yet been initialized. API communication is restricted to login/refresh requests.</li>
     * <li>{@link Status#AUTHORIZATION_IN_PROGRESS}: The initialization of this session is currently in progress</li>
     * <li>{@link Status#AUTHORIZED}: The session is initialized and ready for further API communication</li>
     * </ul>
     *
     * @return The current status of the underlying session
     */
    protected Status getStatus() {
        return session.getStatus();
    }

    /**
     * Set the preferred language used for API communication. If available, search results will returned in this
     * language.
     *
     * @param language The preferred language of the data returned by the remote service
     */
    public void setLanguage(String language) {
        session.setLanguage(language);
    }

    /**
     * Returns the connections underlying API session
     *
     * @return API session associated with this connection
     */
    APISession getSession() {
        return session;
    }

    /**
     * Returns the endpoint which will be invoked for all remote API request
     *
     * @return Remote API associated with this connection
     */
    RemoteAPI getRemoteAPI() {
        return remoteAPI;
    }

    /**
     * Returns the API key related to this connection
     *
     * @return API key of this connection
     */
    public String getApiKey() {
        return session.getApiKey();
    }

    /**
     * Returns the user key related to this connection. If no value is present then this connection only supports
     * authorization via API Key.
     *
     * @return Optional user key for this connection
     */
    public Optional<String> getUserKey() {
        return session.getUserKey();
    }

    /**
     * Returns the user name related to this connection. If no value is present then this connection only supports
     * authorization via API Key.
     *
     * @return Optional user name for this connection
     */
    public Optional<String> getUserName() {
        return session.getUserName();
    }

    /**
     * Returns the JWT token that is currently used for Bearer authentication. Might be empty if authorization has not
     * yet been initialized.
     *
     * @return Current API session token or empty Optional if not yet authorized
     */
    public Optional<String> getToken() {
        return session.getToken();
    }

    /**
     * Checks whether user authentication is available or not. A distinct user authentication is optional and only
     * required for specific API calls (USER*).
     *
     * @return {@link Boolean#TRUE} if the underlying session supports user authentication or {@link Boolean#FALSE} if
     *         not.
     */
    public boolean userAuthentication() {
        return session.userAuthentication();
    }

    /**
     * Invokes the given request. If the remote service responds with a HTTP-401 status this method will automatically
     * try to authorize the underlying session. If the automated on-demand authentication was successful the given
     * request will be invoked again. Otherwise an exception will be thrown.
     *
     * @param request The request to be invoked
     *
     * @return Raw JSON response as received by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    private synchronized JsonNode sendRequest(APIRequest request) throws APIException {
        request.setSession(session);
        request.setRemoteAPI(remoteAPI);

        for (int retry = 0; retry < MAX_AUTHENTICATION_RETRY_COUNT; retry++) {
            try {
                return request.send();
            } catch (APINotAuthorizedException e) {
                // If the session is not yet authorized try to login or refresh the session
                authorizeSession();
            }
        }

        throw new APIException(String.format(ERR_MAX_RETRY_EXCEEDED, MAX_AUTHENTICATION_RETRY_COUNT));
    }

    /**
     * Tries to authorize the underlying API session in case it has not yet been initialized or the original
     * authorization has expired.
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if the underlying session is not in a proper state to initialize the
     *                      authorization
     */
    private void authorizeSession() throws APIException {
        switch (session.getStatus()) {
            case NOT_AUTHORIZED:
            case AUTHORIZED:
                AuthenticationAPI.login(this);    // Not yet authorized or authorization expired: Request a new token
                break;
            default:
                // Authorization is already in progress but could not be completed. Do not retry to authorize this session
                // again but abort processing and notify the calling instance that the session could not be authorized.
                session.setStatus(NOT_AUTHORIZED);
                throw new APIException("Remote API authorization failed: Please check your API key and login credentials");
        }
    }
}

/**
 * Abstract base class for API request handling
 * <p><br>
 * This class provides common functionality used in the context of HTTP request based API communication. It handles the
 * invocation of remote resources as well as processing of the received response data. This includes proper
 * configuration of HTTP connections (request headers), Bearer authentication, response state evaluation, parsing
 * response data and proper error handling.
 */
abstract class APIRequest {

    /** Messages for error/exception handling */
    static final String ERR_UNEXPECTED_RESPONSE = "Receiver returned an unexpected error: HTTP-%d \nOriginal API error message: %s";
    static final String ERR_SEND = "An exception occurred while sending %s request to API";

    /** Constants for API error handling */
    private static final String API_ERROR = "Error";

    /** Resource/Route to be called on remote service */
    private final String resource;

    /** HTTP request method to be used for this request */
    private final HttpRequestMethod requestMethod;

    /** Session for remote API authentication */
    private APISession session;

    /** Remote endpoint used for API communication */
    private RemoteAPI remoteAPI;

    /**
     * Creates a new request for the given resource using the given request method
     *
     * @param resource      The remote resource to be invoked
     * @param requestMethod The HTTP request method to be used for this request
     */
    APIRequest(@Nonnull String resource, @Nonnull HttpRequestMethod requestMethod) {
        Parameters.validateNotEmpty(resource, "API resource must not be NULL or empty");
        Parameters.validateNotNull(requestMethod, "HTTP request method must not be NULL");

        this.resource = resource;
        this.requestMethod = requestMethod;
    }

    /**
     * Disconnects the given connection if present
     *
     * @param con The connection to be disconnected
     */
    private static void disconnect(@CheckForNull HttpsURLConnection con) {
        Optional.ofNullable(con).ifPresent(HttpsURLConnection::disconnect);
    }

    /**
     * Should only be invoked in case of HTTP-405 status response. Fetches the error message from the connections
     * <b>error</b> stream and returns it. According to the HTTP-405 status code specification, the server MUST
     * generate an Allow header field in a 405 response containing a list of the target resource's currently supported
     * methods. These supported methods will - if available - be prepended to the end of the actual error message.
     *
     * @param con Fully initialized connection that has returned a HTTP-405 error status code
     *
     * @return Content from the error stream plus all available values from the responses Allow header as String
     *
     * @throws IOException Thrown if an I/O error occurs while creating the input stream or fetching the error data
     */
    private static String getBadMethodError(HttpsURLConnection con) throws IOException {
        String allowedMethods = con.getHeaderFields().getOrDefault(ALLOW, Collections.emptyList())
                .stream().sorted().collect(joining(", ", "[", "]"));
        return String.format("%s - Response Allow header: %s", getError(con), allowedMethods);
    }

    /**
     * Fetches the error message from the connections <b>error</b> stream and returns it
     *
     * @param con Fully initialized connection that has returned some HTTP error status code
     *
     * @return Content from the error stream as String
     *
     * @throws IOException Thrown if an I/O error occurs while creating the input stream or fetching the error data
     */
    private static String getError(@Nonnull HttpsURLConnection con) throws IOException {
        JsonNode response = parseResponse(con.getErrorStream());
        return response.has(API_ERROR) ? response.get(API_ERROR).asText("") : "n/a";
    }

    /**
     * Parses the data from the given input stream as JSON and returns it
     *
     * @param inputStream The input stream to parse the JSON from
     *
     * @return Content from the given input stream parsed as JSON object
     *
     * @throws IOException Thrown if an I/O error occurs while parsing the JSON data
     */
    private static JsonNode parseResponse(@CheckForNull InputStream inputStream) throws IOException {
        // According to the HTTP/1.1 specification, HEAD methods must not return a message-body in the response. In
        // order to harden the implementation we return an empty JsonNode instead of a null-value.
        InputStream checkedInputStream = Optional.ofNullable(inputStream)
                .orElseGet(() -> new ByteArrayInputStream("{}".getBytes(UTF_8)));

        ObjectMapper mapper = new ObjectMapper();
        try (JsonParser parser = mapper.getFactory().createParser(checkedInputStream)) {
            return mapper.readTree(parser);
        }
    }

    /**
     * Associates this request with an underlying communication session. Most of the requests require an initialized
     * session to be used for remote service authentication.
     *
     * @param session The API session bound to this request
     */
    void setSession(APISession session) {
        this.session = session;
    }

    /**
     * Specifies the remote endpoint (URI) to which the request should be addressed to. Typically this is the
     * <i>TheTVDB.com</i> RESTful API but It may also be set to some proxy for example.
     *
     * @param remote The remote endpoint used for API communication
     */
    void setRemoteAPI(@Nonnull RemoteAPI remote) {
        this.remoteAPI = remote;
    }

    /**
     * Performs the actual request invocation by opening a connection to an API resource, processing the actual response
     * data and finally closing the connection. Subclasses may hook into this process by implementing the {@link
     * #prepareRequest(HttpsURLConnection) prepareRequest(connection)} method which allows for additional request
     * specific preparation.
     *
     * @return Raw JSON as received by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    final JsonNode send() throws APIException {
        HttpsURLConnection con = null;

        try {
            // Create new HTTP connection to the remove service
            con = openConnection();

            // Special preparations for the request to send, e.g. specific connection settings, body for POST request,...
            prepareRequest(con);

            // Parse response from HTTP connection
            return getResponse(con);
        } catch (IOException ex) {
            throw new APICommunicationException(String.format(ERR_SEND, requestMethod), ex);
        } finally {
            disconnect(con);
        }
    }

    /**
     * Opens a new HTTPS connection to this requests resource URI using a specific request method. The returned
     * connection comes with some common configuration regarding content types and Bearer authentication.
     *
     * @return A preconfigured HTTPS connection pointing to some remote API endpoint
     *
     * @throws IOException Thrown in case of communication issues like malformed URL, invalid request method, etc.
     */
    @SuppressWarnings("java:S3655")    // Auth process guarantees that a session in status AUTHORIZED has a valid token
    private HttpsURLConnection openConnection() throws IOException {
        Preconditions.requireNonNull(remoteAPI, "No remote endpoint specified");
        Preconditions.requireNonEmpty(resource, "No API resource specified");
        Preconditions.requireNonNull(requestMethod, "No HTTP request method specified");

        HttpsURLConnection con = (HttpsURLConnection)remoteAPI.forResource(resource).openConnection();

        // POST, GET, DELETE, PUT,...
        con.setRequestMethod(requestMethod.getName());

        // Request properties for API
        con.setRequestProperty(CONTENT_TYPE, "application/json; charset=utf-8");
        con.setRequestProperty(ACCEPT, "application/json, application/vnd.thetvdb." + API_VERSION);
        con.setRequestProperty(USER_AGENT, "Mozilla/5.0");
        if (session != null && session.isInitialized()) {
            // If session has already been initialized, add token information and language key to each request
            con.setRequestProperty(AUTHORIZATION, "Bearer " + session.getToken().get());
            con.setRequestProperty(ACCEPT_LANGUAGE, session.getLanguage());
        }

        return con;
    }

    /**
     * Provides the option for additional request specific preparation. The default implementation does <b>not</b>
     * perform any additional preparations. However, subclasses may overwrite this method and use the given connection
     * in order to apply specific preparations like for example, pushing additional payload for <em>{@code POST}</em>
     * requests. Still, the provided connection will already contain some basic configuration that should be sufficient
     * for most of the request types without the need of any further preparations.
     *
     * @param con New HTTPS connection with some basic configuration already applied
     *
     * @throws IOException Thrown in case an I/O error occurred while performing request specific preparations
     */
    void prepareRequest(@Nonnull HttpsURLConnection con) throws IOException {
        // No default preparation. Overwrite this method in any sub-class to add type specific request preparations.
    }

    /**
     * Evaluates the status code and parses the response content accordingly. In case of HTTP-200 the content from the
     * responses <b>input</b> stream will be parsed and returned as raw JSON object. For other status codes the content
     * from the <b>error</b> stream will be parsed and mapped into a corresponding exception type or a general {@link
     * APICommunicationException} for unhandled status codes.
     *
     * @param con Fully initialized HTTPS connection pointing to some remote service endpoint
     *
     * @return Parsed response content as raw JSON in case of HTTP-200 status. For all other status codes an exception
     *         will be thrown
     *
     * @throws APIException Thrown if a response with a status code other than HTTP-200 was received
     * @throws IOException  Thrown if an error occurred connecting to the server
     */
    private JsonNode getResponse(@Nonnull HttpsURLConnection con) throws APIException, IOException {
        int responseCode = con.getResponseCode();

        switch (responseCode) {
            case HttpURLConnection.HTTP_OK:
                return getData(con);
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                throw new APINotAuthorizedException(getError(con));
            case HttpURLConnection.HTTP_NOT_FOUND:
                throw new APIException(API_NOT_FOUND_ERROR, getError(con));
            case HttpURLConnection.HTTP_BAD_METHOD:
                throw new APIException(API_BAD_METHOD_ERROR, getBadMethodError(con));
            case HttpURLConnection.HTTP_CONFLICT:
                throw new APIException(API_CONFLICT_ERROR, getError(con));
            case HttpURLConnection.HTTP_UNAVAILABLE:
                throw new APIException(API_SERVICE_UNAVAILABLE);
            default:
                throw new APICommunicationException(String
                        .format(ERR_UNEXPECTED_RESPONSE, responseCode, getError(con)));
        }
    }

    /**
     * Parses the data from the connections <b>input</b> stream as JSON and returns it
     *
     * @param con Fully initialized connection that has returned a HTTP-200 status
     *
     * @return Content from the input stream mapped as JSON object
     *
     * @throws IOException Thrown if an I/O error occurs while creating the input stream or parsing the JSON data
     */
    JsonNode getData(@Nonnull HttpsURLConnection con) throws IOException {
        return parseResponse(con.getInputStream());
    }
}

/**
 * Implementation for an API <em>{@code GET}</em> request
 * <p><br>
 * Invokes a specific remote service resource via the HTTP GET request method
 */
final class GetRequest extends APIRequest {

    /**
     * Creates a new <em>{@code GET}</em> request for the given resource
     *
     * @param resource Remote service resource to be invoked
     */
    GetRequest(@Nonnull String resource) {
        super(resource, HttpRequestMethod.GET);
    }
}

/**
 * Implementation for an API <em>{@code POST}</em> request
 * <p><br>
 * Invokes a specific remote service resource via the HTTP POST request method. This request type accepts additional
 * payload content to be pushed to the remote endpoint.
 */
final class PostRequest extends APIRequest {

    /** The requests payload to be pushed to the remote service */
    private final String data;

    /**
     * Creates a new <em>{@code POST}</em> request for the given resource
     *
     * @param resource Remote service resource to be invoked
     * @param data     The actual payload to be pushed to the remote service
     */
    PostRequest(@Nonnull String resource, @Nonnull String data) {
        super(resource, HttpRequestMethod.POST);
        this.data = data;
    }

    /**
     * Writes the requests payload into the output stream of the given connection
     *
     * @param con New HTTPS connection with some basic configuration already applied
     *
     * @throws IOException Thrown if an I/O error occurs while creating the output stream
     */
    @Override
    void prepareRequest(@Nonnull HttpsURLConnection con) throws IOException {
        // Write request body (payload) for POST request
        Preconditions.requireNonNull(data, "Request payload data is not set");

        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            os.write(data.getBytes(UTF_8));
            os.flush();
        }
    }
}

/**
 * Implementation for an API <em>{@code HEAD}</em> request
 * <p><br>
 * Invokes a specific remote service resource via the HTTP HEAD request method. This request type will not parse any
 * response stream content but only the returned connection headers, assembled into a JSON object.
 */
final class HeadRequest extends APIRequest {

    /**
     * Creates a new <em>{@code HEAD}</em> request for the given resource
     *
     * @param resource Remote service resource to be invoked
     */
    HeadRequest(@Nonnull String resource) {
        super(resource, HttpRequestMethod.HEAD);
    }

    /**
     * Assembles the response header fields of the given connection into the data node of a JSON object and returns it
     *
     * @param con Fully initialized HTTPS connection pointing to some remote service endpoint
     *
     * @return Artificial JSON object containing the responses header fields
     */
    @Override
    JsonNode getData(@Nonnull HttpsURLConnection con) {
        // Create JSON object from response header fields
        JsonNodeFactory factory = new ObjectMapper().getNodeFactory();
        ObjectNode data = factory.objectNode();

        for (Entry<String, List<String>> header : con.getHeaderFields().entrySet()) {
            if (header.getKey() == null) {
                continue;
            }

            String key = header.getKey();
            List<String> values = header.getValue();

            if (values.isEmpty()) {
                data.putNull(key);
            } else if (values.size() == 1) {
                data.put(key, values.get(0));
            } else {
                ArrayNode arrayNode = factory.arrayNode();
                values.forEach(arrayNode::add);
                data.set(key, arrayNode);
            }
        }

        return factory.objectNode().set("data", data);
    }
}

/**
 * Implementation for an API <em>{@code DELETE}</em> request
 * <p><br>
 * Invokes a specific remote service resource via the HTTP DELETE request method
 */
final class DeleteRequest extends APIRequest {

    /**
     * Creates a new <em>{@code DELETE}</em> request for the given resource
     *
     * @param resource Remote service resource to be invoked
     */
    DeleteRequest(@Nonnull String resource) {
        super(resource, HttpRequestMethod.DELETE);
    }
}

/**
 * Implementation for an API <em>{@code PUT}</em> request
 * <p><br>
 * Invokes a specific remote service resource via the HTTP PUT request method
 */
final class PutRequest extends APIRequest {

    /**
     * Creates a new <em>{@code PUT}</em> request for the given resource
     *
     * @param resource Remote service resource to be invoked
     */
    PutRequest(@Nonnull String resource) {
        super(resource, HttpRequestMethod.PUT);
    }
}

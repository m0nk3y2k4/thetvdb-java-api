package com.github.m0nk3y2k4.thetvdb.internal.connection;

import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_CONFLICT_ERROR;
import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_NOT_FOUND_ERROR;
import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_SERVICE_UNAVAILABLE;
import static com.github.m0nk3y2k4.thetvdb.internal.connection.APISession.Status.NOT_AUTHORIZED;
import static com.github.m0nk3y2k4.thetvdb.internal.connection.APISession.Status;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APICommunicationException;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APINotAuthorizedException;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.AuthenticationAPI;
import com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Preconditions;

public class APIConnection {

    private static final String ERR_MAX_RETRY_EXCEEDED = "Could not connect to API service after %d retries. Please check your API-Key.";

    private static final int MAX_RETRY_COUNT = 3;

    private final APISession session;

    public APIConnection(@Nonnull String apiKey) {
        this.session = new APISession(apiKey);
    }

    public APIConnection(@Nonnull String apiKey, @Nonnull String userKey, @Nonnull String userName) {
        this.session = new APISession(apiKey, userKey, userName);
    }

    public JsonNode sendGET(@Nonnull String resource) throws APIException {
        return sendRequest(new GetRequest(resource));
    }

    public JsonNode sendPOST(@Nonnull String resource, @Nonnull String data) throws APIException {
        return sendRequest(new PostRequest(resource, data));
    }

    public JsonNode sendHEAD(@Nonnull String resource) throws APIException {
        return sendRequest(new HeadRequest(resource));
    }

    public JsonNode sendDELETE(@Nonnull String resource) throws APIException {
        return sendRequest(new DeleteRequest(resource));
    }

    public JsonNode sendPUT(@Nonnull String resource) throws APIException {
        return sendRequest(new PutRequest(resource));
    }

    public void setToken(@Nonnull String token) throws APIException {
        session.setToken(token);
    }

    public void setStatus(Status status) {
        session.setStatus(status);
    }

    public void setLanguage(String language) {
        session.setLanguage(language);
    }

    public String getApiKey() {
        return session.getApiKey();
    }

    public Optional<String> getUserKey() {
        return session.getUserKey();
    }

    public Optional<String> getUserName() {
        return session.getUserName();
    }

    public Optional<String> getToken() {
        return session.getToken();
    }

    public boolean userAuthentication() {
        return session.userAuthentication();
    }

    private synchronized JsonNode sendRequest(APIRequest request) throws APIException {
        request.setSession(session);

        for (int retry = 0; retry < MAX_RETRY_COUNT ; retry++) {
            try {
                return request.send();
            } catch (APINotAuthorizedException e) {
                // If the session is not yet authorized try to login or refresh the session
                authorizeSession();
            }
        }

        throw new APIException(String.format(ERR_MAX_RETRY_EXCEEDED, MAX_RETRY_COUNT));
    }

    private void authorizeSession() throws APIException {
        switch (session.getStatus()) {
            case NOT_AUTHORIZED:
            case AUTHORIZED:
                AuthenticationAPI.login(this);              // Not yet authorized or authorization expired: Request a new token
                break;
            default:
                // Authorization is already in progress but could not be completed. Do not retry to authorize this session
                // again but abort processing and notify the calling instance that the session could not be authorized.
                session.setStatus(NOT_AUTHORIZED);
                throw new APIException("Remote API authorization failed: Please check your API key and login credentials");
        }
    }
}

abstract class APIRequest {

    /** Messages for error/exception handling */
    private static final String ERR_UNEXPECTED_RESPONSE = "Receiver returned an unexpected error: HTTP-%d \nOriginal API error message: %s";
    private static final String ERR_SEND = "An exception occurred while sending %s request to API";

    /** Constants for API error handling */
    private static final String API_ERROR = "Error";

    /** The API base URL */
    private static final String API_URL = "https://api.thetvdb.com";

    /** User agent */
    private static final String USER_AGENT = "Mozilla/5.0";

    /** Session for remote API authentication */
    private APISession session;

    /** Resource/Route to be called on remote service */
    private final String resource;

    /** HTTP request method to be used for this request*/
    private final HttpRequestMethod requestMethod;

    APIRequest(@Nonnull String resource, @Nonnull HttpRequestMethod requestMethod) {
        Parameters.validateNotEmpty(resource, "API resource must not be NULL or empty");
        Parameters.validateNotNull(requestMethod, "HTTP request method must not be NULL");

        this.resource = resource;
        this.requestMethod = requestMethod;
    }

    void setSession(@Nonnull APISession session) {
        this.session = session;
    }

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

    private HttpsURLConnection openConnection() throws IOException {
        Preconditions.requireNonEmpty(resource, "No API resource specified");
        Preconditions.requireNonNull(requestMethod, "No HTTP request method specified");

        HttpsURLConnection con = (HttpsURLConnection) new URL(API_URL + resource).openConnection();

        // POST, GET, DELETE, PUT,...
        con.setRequestMethod(requestMethod.getName());

        // Request properties for API
        con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("User-Agent", USER_AGENT);
        if (session != null && session.isInitialized()) {
            // If session has already been initialized, add token information and language key to each request
            con.setRequestProperty("Authorization", "Bearer " + session.getToken().get());
            con.setRequestProperty("Accept-Language", session.getLanguage());
        }

        return con;
    }

    private void disconnect(@CheckForNull HttpsURLConnection con) {
        if (con != null) {
            con.disconnect();
        }
    }

    void prepareRequest(@Nonnull HttpsURLConnection con) throws IOException {
        // No default preparation. Overwrite this method in any sub-class to add type specific request preparations.
    }

    JsonNode getResponse(@Nonnull HttpsURLConnection con) throws APIException, IOException {
        int responseCode = con.getResponseCode();

        switch (responseCode) {
            case HttpURLConnection.HTTP_OK:
                return parseResponse(con);
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                throw new APINotAuthorizedException(getError(con));
            case HttpURLConnection.HTTP_NOT_FOUND:
                throw new APIException(API_NOT_FOUND_ERROR, getError(con));
            case HttpURLConnection.HTTP_CONFLICT:
                throw new APIException(API_CONFLICT_ERROR, getError(con));
            case HttpURLConnection.HTTP_UNAVAILABLE:
                throw new APIException(API_SERVICE_UNAVAILABLE);
            default:
                throw new APICommunicationException(String.format(ERR_UNEXPECTED_RESPONSE, responseCode, getError(con)));
        }
    }

    private JsonNode parseResponse(@Nonnull HttpsURLConnection con) throws IOException {
        return parseResponse(con.getInputStream());
    }

    private String getError(@Nonnull HttpsURLConnection con) throws IOException {
        return parseResponse(con.getErrorStream()).get(API_ERROR).asText("");
    }

    private JsonNode parseResponse(@Nonnull InputStream inputStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (JsonParser parser = mapper.getFactory().createParser(inputStream)) {
            return mapper.readTree(parser);
        }
    }
}

final class GetRequest extends APIRequest {

    GetRequest(@Nonnull String resource) {
        super(resource, HttpRequestMethod.GET);
    }
}

final class PostRequest extends APIRequest {

    private final String data;

    PostRequest(@Nonnull String resource, @Nonnull String data) {
        super(resource, HttpRequestMethod.POST);
        this.data = data;
    }

    @Override
    void prepareRequest(@Nonnull HttpsURLConnection con) throws IOException {
        // Write request body (payload) for POST request
        Preconditions.requireNonNull(data, "Request payload data is not set");

        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            os.write(data.getBytes());
            os.flush();
        }
    }
}

final class HeadRequest extends APIRequest {

    HeadRequest(@Nonnull String resource) {
        super(resource, HttpRequestMethod.HEAD);
    }

    @Override
    JsonNode getResponse(@Nonnull HttpsURLConnection con) {
        // Create JSON object from response header fields
        JsonNodeFactory factory = new ObjectMapper().getNodeFactory();
        ObjectNode root = factory.objectNode();

        for (Entry<String, List<String>> header : con.getHeaderFields().entrySet()) {
            if (header.getKey() == null) {
                continue;
            }

            String key = header.getKey();
            List<String> values = header.getValue();

            if (values.isEmpty()) {
                root.putNull(key);
            } else if (values.size() == 1) {
                root.put(key, values.get(0));
            } else {
                ArrayNode arrayNode = factory.arrayNode();
                values.forEach(arrayNode::add);
                root.set(key, arrayNode);
            }
        }

        return root;
    }
}

final class DeleteRequest extends APIRequest {

    DeleteRequest(@Nonnull String resource) {
        super(resource, HttpRequestMethod.DELETE);
    }
}

final class PutRequest extends APIRequest {

    PutRequest(@Nonnull String resource) {
        super(resource, HttpRequestMethod.PUT);
    }
}

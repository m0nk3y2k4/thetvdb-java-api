package com.github.m0nk3y2k4.thetvdb.internal.connection;

import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_CONFLICT_ERROR;
import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_NOT_FOUND_ERROR;
import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_SERVICE_UNAVAILABLE;
import static com.github.m0nk3y2k4.thetvdb.internal.connection.APISession.Status.NOT_AUTHORIZED;
import static com.github.m0nk3y2k4.thetvdb.internal.connection.APISession.Status;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

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

    public String getApiKey() {
        return session.getApiKey();
    }

    public Optional<String> getUserKey() {
        return session.getUserKey();
    }

    public Optional<String> getUserName() {
        return session.getUserName();
    }

    public boolean userAuthentication() {
        return session.userAuthentication();
    }

    public void setStatus(Status status) {
        session.setStatus(status);
    }

    public void setToken(String token) {
        session.setToken(token);
    }

    public void setLanguage(String language) {
        this.session.setLanguage(language);
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
                AuthenticationAPI.login(this);              // Request a new token
                break;
            case AUTHORIZED:
                AuthenticationAPI.refreshSession(this);     // Refresh the existing token
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

    /** Constants for API error handling */
    private static final String API_ERROR = "Error";

    /** The API base URL */
    private static final String API_URL = "https://api.thetvdb.com";

    /** User agent */
    private static final String USER_AGENT = "Mozilla/5.0";

    private APISession session;
    protected HttpsURLConnection con;
    protected final String resource;

    public APIRequest(@Nonnull String resource) {
        this.resource = resource;
    }

    public void setSession(@Nonnull APISession session) {
        this.session = session;
    }

    protected HttpsURLConnection openConnection(@Nonnull String resource, @Nonnull String requestMethod) throws IOException {
        con = (HttpsURLConnection) new URL(API_URL + resource).openConnection();

        // POST or GET
        con.setRequestMethod(requestMethod);

        // Request properties for API
        con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("User-Agent", USER_AGENT);
        if (session != null && session.isInitialized()) {
            // If session has already been initialized, add token information and language key to each request
            con.setRequestProperty("Authorization", "Bearer " + session.getToken());
            con.setRequestProperty("Accept-Language", session.getLanguage());
        }

        return con;
    }

    protected void disconnect() {
        if (con != null) {
            con.disconnect();
        }
    }

    protected JsonNode getResponse() throws APIException, IOException {
        int responseCode = con.getResponseCode();

        switch (responseCode) {
            case HttpsURLConnection.HTTP_OK:
                return parseResponse();
            case HttpsURLConnection.HTTP_UNAUTHORIZED:
                throw new APINotAuthorizedException(getError());
            case HttpsURLConnection.HTTP_NOT_FOUND:
                throw new APIException(API_NOT_FOUND_ERROR, getError());
            case HttpsURLConnection.HTTP_CONFLICT:
                throw new APIException(API_CONFLICT_ERROR, getError());
            case HttpsURLConnection.HTTP_UNAVAILABLE:
                throw new APIException(API_SERVICE_UNAVAILABLE);
            default:
                throw new APICommunicationException(String.format(ERR_UNEXPECTED_RESPONSE, responseCode, getError()));
        }
    }

    private JsonNode parseResponse(InputStream inputStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (JsonParser parser = mapper.getFactory().createParser(inputStream)) {
            return mapper.readTree(parser);
        }
    }

    private JsonNode parseResponse() throws IOException {
        return parseResponse(con.getInputStream());
    }

    private String getError() throws IOException {
        return parseResponse(con.getErrorStream()).get(API_ERROR).asText("");
    }

    public abstract JsonNode send() throws APIException;
}

final class GetRequest extends APIRequest {

    /** Messages for error/exception handling */
    private static final String ERR_GET = "An exception occurred while sending GET request to API";

    public GetRequest(@Nonnull String resource) {
        super(resource);
    }

    @Override
    public JsonNode send() throws APIException {
        try {
            openConnection(resource, "GET");

            return getResponse();
        } catch (IOException ex) {
            throw new APICommunicationException(ERR_GET, ex);
        } finally {
            disconnect();
        }
    }
}

final class PostRequest extends APIRequest {
    /** Messages for error/exception handling */
    private static final String ERR_POST = "An exception occurred while sending POST request to API";

    private final String data;

    public PostRequest(@Nonnull String resource, @Nonnull String data) {
        super(resource);
        this.data = data;
    }

    @Override
    public JsonNode send() throws APIException {
        try {
            openConnection(resource, "POST");

            // POST data
            writeRequestBody(data);

            return getResponse();
        } catch (IOException ex) {
            throw new APICommunicationException(ERR_POST, ex);
        } finally {
            disconnect();
        }
    }

    private void writeRequestBody(@Nonnull String data) throws IOException {
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            os.write(data.getBytes());
            os.flush();
        }
    }
}

final class HeadRequest extends APIRequest {
    /** Messages for error/exception handling */
    private static final String ERR_HEAD = "An exception occurred while sending HEAD request to API";

    public HeadRequest(@Nonnull String resource) {
        super(resource);
    }

    @Override
    public JsonNode send() throws APIException {
        try {
            openConnection(resource, "HEAD");

            return createJsonFromHeaderFields();
        } catch (IOException ex) {
            throw new APICommunicationException(ERR_HEAD, ex);
        } finally {
            disconnect();
        }
    }

    private JsonNode createJsonFromHeaderFields() {
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
                values.stream().forEach(arrayNode::add);
                root.set(key, arrayNode);
            }
        }

        return root;
    }
}

final class DeleteRequest extends APIRequest {
        /** Messages for error/exception handling */
        private static final String ERR_DELETE = "An exception occurred while sending DELETE request to API";

        public DeleteRequest(@Nonnull String resource) {
            super(resource);
        }

        @Override
        public JsonNode send() throws APIException {
            try {
                openConnection(resource, "DELETE");

                return getResponse();
            } catch (IOException ex) {
                throw new APICommunicationException(ERR_DELETE, ex);
            } finally {
                disconnect();
            }
        }
}

final class PutRequest extends APIRequest {
    /** Messages for error/exception handling */
    private static final String ERR_PUT = "An exception occurred while sending PUT request to API";

    public PutRequest(@Nonnull String resource) {
        super(resource);
    }

    @Override
    public JsonNode send() throws APIException {
        try {
            openConnection(resource, "PUT");

            return getResponse();
        } catch (IOException ex) {
            throw new APICommunicationException(ERR_PUT, ex);
        } finally {
            disconnect();
        }
    }
}

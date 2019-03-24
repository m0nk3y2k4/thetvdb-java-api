package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APISession;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;

import java.util.regex.Pattern;

public final class AuthenticationAPI {

    /** Pattern for JSON Web Token validation */
    private static final Pattern JWT_PATTERN = Pattern.compile("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$");

    /** Error messages */
    private static final String ERR_JWT_EMPTY = "Remote API authorization failed: Service did not return any token";
    private static final String ERR_JWT_INVALID = "Remote API authorization failed: Invalid token format [%s]";

    /** Special functional interface to avoid exception handling in lambda expressions (Supplier<T> does not declare a throw-clause) */
    @FunctionalInterface
    private interface ThrowingSupplier<T> {
        T get() throws APIException;
    }

    private AuthenticationAPI() {}     // Private constructor. Only static methods

    public static void login(@Nonnull APIConnection con) throws APIException {
        ObjectNode authentication = new ObjectMapper().getNodeFactory().objectNode();

        authentication.put("apikey", con.getApiKey());
        if (con.userAuthentication()) {
            authentication.put("userkey", con.getUserKey().get());
            authentication.put("username", con.getUserName().get());
        }

        setToken(con, () -> con.sendPOST("/login", authentication.toString()));
    }

    public static void refreshSession(@Nonnull APIConnection con) throws APIException {
        setToken(con, () -> con.sendGET("/refresh_token"));
    }

    private static void setToken(APIConnection con, ThrowingSupplier<JsonNode> sendRequest) throws APIException {
        con.setStatus(APISession.Status.AUTHORIZATION_IN_PROGRESS);

        // Request token
        JsonNode response = sendRequest.get();              // Throws exception if authorization fails
        String token = response.get("token").asText();

        // Validate token - throws an exception if not a valid JWT
        validateJWT(token);

        // Set token on connection
        con.setToken(token);

        con.setStatus(APISession.Status.AUTHORIZED);
    }

    /**
     * Checks if the given token is a valid JSON Web Token
     *
     * @param token The token to check
     *
     * @throws APIException If the given token is <code>null</code>, an empty character sequence or does not match the regular JWT format
     */
    private static void validateJWT(String token) throws APIException {
        if (APIUtil.hasNoValue(token)) {
            throw new APIException(ERR_JWT_EMPTY);
        }

        if (!JWT_PATTERN.matcher(token).matches()) {
            throw new APIException(String.format(ERR_JWT_INVALID, token));
        }
    }
}

package com.github.m0nk3y2k4.thetvdb.resource.impl;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.m0nk3y2k4.thetvdb.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.connection.APISession;
import com.github.m0nk3y2k4.thetvdb.exception.APIException;

public final class AuthenticationAPI {

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
            authentication.put("userkey", con.getUserKey());
            authentication.put("username", con.getUserName());
        }

        setToken(con, () -> con.sendPOST("/login", authentication.toString()));
    }

    public static void refreshSession(@Nonnull APIConnection con) throws APIException {
        setToken(con, () -> con.sendGET("/refresh_token"));
    }

    private static void setToken(APIConnection con, ThrowingSupplier<JsonNode> sendRequest) throws APIException {
        con.setStatus(APISession.Status.AUTHORIZATION_IN_PROGRESS);

        JsonNode response = sendRequest.get();              // Throws exception if authorization fails
        String token = response.get("token").asText();
        con.setToken(token);

        con.setStatus(APISession.Status.AUTHORIZED);
    }
}

package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APISession;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.util.functional.ThrowableFunctionalInterfaces;

public final class AuthenticationAPI {

    private AuthenticationAPI() {}     // Private constructor. Only static methods

    public static void login(@Nonnull APIConnection con) throws APIException {
        ObjectNode authentication = new ObjectMapper().getNodeFactory().objectNode();

        authentication.put("apikey", con.getApiKey());
        if (con.userAuthentication()) {
            authentication.put("userkey", con.getUserKey().get());          // NOSONAR: Value presence already evaluated by enclosing condition
            authentication.put("username", con.getUserName().get());        // NOSONAR: Value presence already evaluated by enclosing condition
        }

        con.setStatus(APISession.Status.AUTHORIZATION_IN_PROGRESS);
        setToken(con, () -> con.sendPOST("/login", authentication.toString()));
        con.setStatus(APISession.Status.AUTHORIZED);
    }

    public static void refreshSession(@Nonnull APIConnection con) throws APIException {
        setToken(con, () -> con.sendGET("/refresh_token"));
    }

    private static void setToken(@Nonnull APIConnection con, @Nonnull ThrowableFunctionalInterfaces.Supplier<JsonNode, APIException> sendRequest) throws APIException {
        // Request token
        JsonNode response = sendRequest.get();              // Throws exception if authorization fails
        String token = response.get("token").asText();

        // Set token on connection
        con.setToken(token);
    }
}

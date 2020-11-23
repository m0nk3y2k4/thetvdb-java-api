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

package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APISession;
import com.github.m0nk3y2k4.thetvdb.internal.util.functional.ThrowableFunctionalInterfaces;

/**
 * Implementation of a connector for the remote API's <a href="https://api.thetvdb.com/swagger#/Authentication">Authentication</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used for obtaining and refreshing JWT tokens.
 * Acquired tokens will automatically be propagated to the given connection. Such authenticated/initialized connections
 * may then be used to invoke further remote API routes.
 */
public final class AuthenticationAPI {

    private AuthenticationAPI() {}     // Private constructor. Only static methods

    /**
     * Initializes the given connection by requesting a new token from the remote API. This token will be used for
     * authentication of all subsequent requests that are sent to the remote service using this connection. The
     * initialization will be performed based on the connections current authentication settings. It is strongly
     * recommended to call this route once in order to initialize a connection before using it to invoke other remote
     * routes. However, if an API call is made without proper initialization, an implicit login attempt will be
     * performed.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Authentication/post_login">
     * <b>[POST]</b> /login</a>
     *
     * @param con Connection to be used for API communication and to which the Issued JWT token should be propagated to
     *
     * @throws APIException If an exception with the remote API occurs, e.g. due to an IO error or if the API returned
     *                      an invalid or unparsable JWT token
     */
    public static void login(@Nonnull APIConnection con) throws APIException {
        ObjectNode authentication = new ObjectMapper().getNodeFactory().objectNode();

        authentication.put("apikey", con.getApiKey());
        if (con.userAuthentication()) {
            authentication.put("userkey", con.getUserKey().get());      // NOSONAR: evaluated by enclosing condition
            authentication.put("username", con.getUserName().get());    // NOSONAR: evaluated by enclosing condition
        }

        con.setStatus(APISession.Status.AUTHORIZATION_IN_PROGRESS);
        setToken(con, () -> con.sendPOST("/login", authentication.toString()));
        con.setStatus(APISession.Status.AUTHORIZED);
    }

    /**
     * Refreshes the current, valid JWT token of the given connection. This method can be used to extend the expiration
     * date (24 hours) of the current token without the need of a complete new login.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Authentication/get_refresh_token">
     * <b>[GET]</b> /refresh_token</a>
     *
     * @param con Connection to be used for API communication and for which the JWT token should be refreshed
     *
     * @throws APIException If an exception with the remote API occurs, e.g. due to an IO error or if the API returned
     *                      an invalid or unparsable JWT token
     */
    public static void refreshSession(@Nonnull APIConnection con) throws APIException {
        setToken(con, () -> con.sendGET("/refresh_token"));
    }

    /**
     * Invokes the given request and parses the JWT token from the responded JSON which will then be set to the given
     * connection.
     *
     * @param con         Connection to be used for API communication and to which the Issued JWT token should be
     *                    propagated to
     * @param sendRequest Prepared API request which returns a valid JWT token in it's payload
     *
     * @throws APIException If an exception with the remote API occurs, e.g. due to an IO error or if the API returned
     *                      an invalid or unparsable JWT token
     */
    private static void setToken(@Nonnull APIConnection con,
            @Nonnull ThrowableFunctionalInterfaces.Supplier<JsonNode, APIException> sendRequest) throws APIException {
        // Request token
        JsonNode response = sendRequest.get();              // Throws exception if authorization fails
        String token = response.get("token").asText();

        // Set token on connection
        con.setToken(token);
    }
}

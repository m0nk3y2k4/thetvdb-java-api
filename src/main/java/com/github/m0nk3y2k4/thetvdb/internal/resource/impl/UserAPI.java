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

package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.model.data.FavoriteRecord;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.resource.Resource;
import com.github.m0nk3y2k4.thetvdb.internal.util.json.APIJsonMapper;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Implementation of a connector for the remote API's
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Favorites">User</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used to access or change user data.
 */
public final class UserAPI extends Resource {

    private UserAPI() {}        // Private constructor. Only static methods

    /**
     * Returns some information about the current user as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/User%20info/getUserInfo">
     * <b>[GET]</b> /user</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing information about the current user
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getUserInfo(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource("/user"));
    }

    /**
     * Returns some information about a specific user as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/User%20info/getUserInfoById">
     * <b>[GET]</b> /user/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> user ID
     *
     * @return JSON object containing information about the specified user
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getUserInfo(@Nonnull APIConnection con, long id) throws APIException {
        return con.sendGET(createResource("/user/{id}", id));
    }

    /**
     * Returns the current favorites of this user as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Favorites/getUserFavorites">
     * <b>[GET]</b> /user/favorites</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing the current favorites of this user
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getUserFavorites(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource("/user/favorites"));
    }

    /**
     * Adds favorites to the current user and returns the success status of the operation as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Favorites/createUserFavorites">
     * <b>[POST]</b> /user/favorites</a>
     *
     * @param con       Initialized connection to be used for API communication
     * @param favRecord The user favorites record to create
     *
     * @return JSON object containing the success status of the operation
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode createUserFavorites(@Nonnull APIConnection con, @Nonnull FavoriteRecord favRecord)
            throws APIException {
        Parameters.validateNotNull(favRecord, "Favorite record must not be NULL");
        return con.sendPOST(createResource("/user/favorites"), APIJsonMapper.writeValue(favRecord));
    }
}

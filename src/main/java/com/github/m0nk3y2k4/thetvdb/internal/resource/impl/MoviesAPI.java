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

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.resource.QueryResource;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Implementation of a connector for the remote API's <a href="https://api.thetvdb.com/swagger#/Movies">Movies</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used to gather information about specific movies.
 */
public final class MoviesAPI extends QueryResource {

    /** Base URL path parameter for this endpoint */
    private static final String BASE = "/movies";

    private MoviesAPI() {}     // Private constructor. Only static methods

    /**
     * Returns detailed information for a specific movie as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Movies/get_movies_id">
     * <b>[GET]</b> /movies/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> movie ID
     *
     * @return JSON object containing detailed information for a specific movie
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      movie ID does not exist, etc.
     */
    public static JsonNode get(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(BASE, id));
    }

    /**
     * Returns a list of ID's of all movies, that have been updated since a specific time, as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Movies/get_movieupdates">
     * <b>[GET]</b> /movieupdates</a>
     *
     * @param con    Initialized connection to be used for API communication
     * @param params Object containing key/value pairs of query parameters. Has to specify a proper <em>{@code
     *               since}</em> parameter at least.
     *
     * @return JSON object containing the ID's of movies that have been updated beginning at the specified epoch time
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getMovieUpdates(@Nonnull APIConnection con, @CheckForNull QueryParameters params)
            throws APIException {
        Parameters.validateQueryParam(Query.Movie.SINCE, params, value -> value.matches("\\d+") && Long.valueOf(value)
                .compareTo(0L) > 0);
        return con.sendGET(createQueryResource("/movieupdates", null, params));
    }
}

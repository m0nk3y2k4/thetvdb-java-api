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
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.resource.QueryResource;

/**
 * Implementation of a connector for the remote API's <a href="https://api.thetvdb.com/swagger#/Search">Search</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used to search for a particular series.
 */
public final class SearchAPI extends QueryResource {

    /** Base URL path parameter for this endpoint */
    private static final String BASE = "/search";

    private SearchAPI() {}     // Private constructor. Only static methods

    /**
     * Returns series search results based on the given query parameters as raw JSON. The result contains basic
     * information of all series matching the query parameters.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series">
     * <b>[GET]</b> /search/series</a>
     *
     * @param con    Initialized connection to be used for API communication
     * @param params Object containing key/value pairs of query search parameters. For a complete list of possible
     *               search parameters see the API documentation or use {@link #getAvailableSearchParameters(APIConnection)
     *               getAvailableSearchParameters(con)}.
     *
     * @return JSON object containing the series search results
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no records are found that match your query.
     */
    public static JsonNode series(@Nonnull APIConnection con, QueryParameters params) throws APIException {
        return con.sendGET(createQueryResource(BASE, "/series", params));
    }

    /**
     * Returns possible query parameters, which can be used to search for series, as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series_params">
     * <b>[GET]</b> /search/series/params</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing possible parameters to query by in the series search route
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getAvailableSearchParameters(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource(BASE, "/series/params"));
    }
}

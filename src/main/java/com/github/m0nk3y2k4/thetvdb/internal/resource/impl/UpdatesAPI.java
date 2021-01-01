/*
 * Copyright (C) 2019 - 2021 thetvdb-java-api Authors and Contributors
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

import static com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters.isPositiveInteger;

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
 * Implementation of a connector for the remote API's <a href="https://api.thetvdb.com/swagger#/Updates">Updates</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used to fetch series that have been recently
 * updated.
 */
public final class UpdatesAPI extends QueryResource {

    /** Base URL path parameter for this endpoint */
    private static final String BASE = "/updated";

    private UpdatesAPI() {}     // Private constructor. Only static methods

    /**
     * Returns an array of series that have changed in a maximum of one week blocks since the provided <em>{@code
     * fromTime}</em> query parameter, as raw JSON. Note that the given query parameters must always contain a valid
     * <em>{@code fromTime}</em> Epoch timestamp key.
     * <p><br>
     * The user may specify an additional <em>{@code toTime}</em> query key to grab results for less than a week. Any
     * timespan larger than a week will be reduced down to one week automatically.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Updates/get_updated_query">
     * <b>[GET]</b> /updated/query</a>
     *
     * @param con    Initialized connection to be used for API communication
     * @param params Object containing key/value pairs of query parameters. For a complete list of possible query
     *               parameters see the API documentation or use {@link #getQueryParams(APIConnection)
     *               getQueryParams(con)}.
     *
     * @return JSON object containing a list of updated objects that match the given timeframe
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or no records exist for the given timespan.
     */
    public static JsonNode query(@Nonnull APIConnection con, @CheckForNull QueryParameters params) throws APIException {
        Parameters.validateQueryParam(Query.Updates.FROMTIME, params, isPositiveInteger());
        return con.sendGET(createQueryResource(BASE, "/query", params));
    }

    /**
     * Returns a list of valid parameters for querying series which have been updated lately, as raw JSON. These keys
     * are permitted to be used in {@link QueryParameters} objects when querying for recently updated series.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Updates/get_updated_query_params">
     * <b>[GET]</b> /updated/query/params</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing a list of possible parameters which may be used to query for last updated series
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getQueryParams(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource(BASE, "/query/params"));
    }
}

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
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.resource.Resource;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Implementation of a connector for the remote API's
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/series">series</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used for obtaining either basic, extended or
 * translated series information.
 */
public final class SeriesAPI extends Resource {

    /** Base URL path parameter for this endpoint */
    private static final String BASE = "/series";

    private SeriesAPI() {}      // Private constructor. Only static methods

    /**
     * Returns a list of available series as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/series/getAllSeries">
     * <b>[GET]</b> /series</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing an overview of available series
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getAllSeries(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(BASE);
    }

    /**
     * Returns basic information for a specific series record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/series/getSeriesBase">
     * <b>[GET]</b> /series/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> series ID
     *
     * @return JSON object containing basic information for a specific series record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc.
     */
    public static JsonNode getSeriesBase(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(BASE, id));
    }

    /**
     * Returns extended information for a specific series record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/series/getSeriesExtended">
     * <b>[GET]</b> /series/{id}/extended</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> series ID
     *
     * @return JSON object containing extended information for a specific series record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc.
     */
    public static JsonNode getSeriesExtended(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(BASE, id, "extended"));
    }
}

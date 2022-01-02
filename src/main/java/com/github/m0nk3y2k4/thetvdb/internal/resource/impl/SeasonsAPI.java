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
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Implementation of a connector for the remote API's
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Seasons">Seasons</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used for obtaining either basic, extended or
 * translated season information.
 */
public final class SeasonsAPI extends QueryResource {

    private SeasonsAPI() {}     // Private constructor. Only static methods

    /**
     * Returns an overview of seasons based on the given query parameters as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Seasons/getAllSeasons">
     * <b>[GET]</b> /seasons</a>
     *
     * @param con    Initialized connection to be used for API communication
     * @param params Object containing key/value pairs of query parameters
     *
     * @return JSON object containing a limited overview of seasons
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getAllSeasons(@Nonnull APIConnection con, QueryParameters params) throws APIException {
        return con.sendGET(createQueryResource("/seasons", params));
    }

    /**
     * Returns basic information for a specific season record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Seasons/getSeasonBase">
     * <b>[GET]</b> /seasons/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> season ID
     *
     * @return JSON object containing basic information for a specific season record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no season
     *                      record with the given ID exists, etc.
     */
    public static JsonNode getSeasonBase(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/seasons/{id}", id));
    }

    /**
     * Returns extended information for a specific season record based on the given query parameters as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Seasons/getSeasonExtended">
     * <b>[GET]</b> /seasons/{id}/extended</a>
     *
     * @param con    Initialized connection to be used for API communication
     * @param id     The <i>TheTVDB.com</i> season ID
     * @param params Object containing key/value pairs of query parameters
     *
     * @return JSON object containing extended information for a specific season record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no season
     *                      record with the given ID exists, etc.
     */
    // ToDo: Currently no query parameters are declared in the documentation but should be supported according to GitHub issue. Check again after the next API update.
    public static JsonNode getSeasonExtended(@Nonnull APIConnection con, long id, QueryParameters params)
            throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createQueryResource("/seasons/{id}/extended", params, id));
    }

    /**
     * Returns an overview of available season types as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Seasons/getSeasonTypes">
     * <b>[GET]</b> /seasons/types</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing an overview of available season types
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getSeasonTypes(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource("/seasons/types"));
    }

    /**
     * Returns a translation record for a specific season as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Seasons/getSeasonTranslation">
     * <b>[GET]</b> /seasons/{id}/translations/{language}</a>
     *
     * @param con      Initialized connection to be used for API communication
     * @param id       The <i>TheTVDB.com</i> season ID
     * @param language The 2- or 3-character language code
     *
     * @return JSON object containing a translation record for a specific season
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no season
     *                      translation record exists for the given ID and language, etc.
     */
    public static JsonNode getSeasonTranslation(@Nonnull APIConnection con, long id, @Nonnull String language)
            throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        Parameters.validatePathParam(PATH_LANGUAGE, language, LANGUAGE_VALIDATOR);
        return con.sendGET(createResource("/seasons/{id}/translations/{language}", id, language));
    }
}

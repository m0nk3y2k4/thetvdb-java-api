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

import static com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters.isValidLanguageCode;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.resource.QueryResource;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Implementation of a connector for the remote API's
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/People">People</a> and
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/People%20Types">People Types</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used for obtaining either basic, extended or
 * translated people information.
 */
public final class PeopleAPI extends QueryResource {

    private PeopleAPI() {}      // Private constructor. Only static methods

    /**
     * Returns an overview of available people types as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://thetvdb.github.io/v4-api/#/People%20Types/getAllPeopleTypes">
     * <b>[GET]</b> /people/types</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing an overview of available people types
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getAllPeopleTypes(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource("/people/types"));
    }

    /**
     * Returns basic information for a specific people record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://thetvdb.github.io/v4-api/#/People/getPeopleBase">
     * <b>[GET]</b> /people/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> people ID
     *
     * @return JSON object containing basic information for a specific people record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no people
     *                      record with the given ID exists, etc.
     */
    public static JsonNode getPeopleBase(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/people/{id}", id));
    }

    /**
     * Returns extended information for a specific people record based on the given query parameters as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://thetvdb.github.io/v4-api/#/People/getPeopleExtended">
     * <b>[GET]</b> /people/{id}/extended</a>
     *
     * @param con    Initialized connection to be used for API communication
     * @param id     The <i>TheTVDB.com</i> people ID
     * @param params Object containing key/value pairs of query parameters
     *
     * @return JSON object containing extended information for a specific people record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no people
     *                      record with the given ID exists, etc.
     */
    public static JsonNode getPeopleExtended(@Nonnull APIConnection con, long id, QueryParameters params)
            throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createQueryResource("/people/{id}/extended", params, id));
    }

    /**
     * Returns a translation record for a specific people as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://thetvdb.github.io/v4-api/#/People/getPeopleTranslation">
     * <b>[GET]</b> /people/{id}/translations/{language}</a>
     *
     * @param con      Initialized connection to be used for API communication
     * @param id       The <i>TheTVDB.com</i> people ID
     * @param language The 2- or 3-character language code
     *
     * @return JSON object containing a translation record for a specific people
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no people
     *                      translation record exists for the given ID and language, etc.
     */
    public static JsonNode getPeopleTranslation(@Nonnull APIConnection con, long id, @Nonnull String language)
            throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        Parameters.validatePathParam(PATH_LANGUAGE, language, isValidLanguageCode());
        return con.sendGET(createResource("/people/{id}/translations/{language}", id, language));
    }
}

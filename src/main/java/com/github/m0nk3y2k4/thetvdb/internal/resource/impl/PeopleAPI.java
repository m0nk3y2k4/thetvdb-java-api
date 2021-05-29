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

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.resource.Resource;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Implementation of a connector for the remote API's
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/people">people</a>
 * and
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/people-types">people-types</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used for obtaining either basic, extended or
 * translated people information.
 */
public final class PeopleAPI extends Resource {

    private PeopleAPI() {}      // Private constructor. Only static methods

    /**
     * Returns an overview of available people types as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/people-types/getAllPeopleTypes">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/people/getPeopleBase">
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
     * Returns extended information for a specific people record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/people/getPeopleExtended">
     * <b>[GET]</b> /people/{id}/extended</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> people ID
     *
     * @return JSON object containing extended information for a specific people record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no people
     *                      record with the given ID exists, etc.
     */
    public static JsonNode getPeopleExtended(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/people/{id}/extended", id));
    }
}

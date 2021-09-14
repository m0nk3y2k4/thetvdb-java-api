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
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.resource.QueryResource;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Implementation of a connector for the remote API's
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Lists">Lists</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used for obtaining either basic, extended or
 * translated list information as well as an overview of available list records.
 */
public final class ListsAPI extends QueryResource {

    private ListsAPI() {}      // Private constructor. Only static methods

    /**
     * Returns a translation record for a specific list as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Lists/getListTranslation">
     * <b>[GET]</b> /lists/{id}/translations/{language}</a>
     *
     * @param con      Initialized connection to be used for API communication
     * @param id       The <i>TheTVDB.com</i> list ID
     * @param language The 2- or 3-character language code
     *
     * @return JSON object containing a translation record for a specific list
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no list
     *                      translation record exists for the given ID and language, etc.
     */
    public static JsonNode getListTranslation(@Nonnull APIConnection con, long id, @Nonnull String language)
            throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        Parameters.validatePathParam(PATH_LANGUAGE, language, LANGUAGE_VALIDATOR);
        return con.sendGET(createResource("/lists/{id}/translations/{language}", id, language));
    }

    /**
     * Returns an overview of lists based on the given query parameters as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Lists/getAllLists">
     * <b>[GET]</b> /lists</a>
     *
     * @param con    Initialized connection to be used for API communication
     * @param params Object containing key/value pairs of query parameters
     *
     * @return JSON object containing a limited overview of lists
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getAllLists(@Nonnull APIConnection con, QueryParameters params) throws APIException {
        return con.sendGET(createQueryResource("/lists", params));
    }

    /**
     * Returns basic information for a specific list record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Lists/getList">
     * <b>[GET]</b> /lists/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> list ID
     *
     * @return JSON object containing basic information for a specific list record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      list ID does not exist, etc.
     */
    public static JsonNode getListBase(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/lists/{id}", id));
    }

    /**
     * Returns extended information for a specific list record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Lists/getListExtended">
     * <b>[GET]</b> /lists/{id}/extended</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> list ID
     *
     * @return JSON object containing extended information for a specific list record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no list
     *                      record with the given ID exists, etc.
     */
    public static JsonNode getListExtended(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/lists/{id}/extended", id));
    }
}

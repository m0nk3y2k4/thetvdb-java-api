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
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.resource.Resource;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Implementation of a connector for the remote API's
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Award%20Categories">Award Categories</a> and
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Awards">Awards</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used for obtaining either basic or extended award
 * and award category information as well as overviews of available records for these types.
 */
public final class AwardsAPI extends Resource {

    private AwardsAPI() {}        // Private constructor. Only static methods

    /**
     * Returns basic information for a specific award category as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Award%20Categories/getAwardCategory">
     * <b>[GET]</b> /awards/categories/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> award category ID
     *
     * @return JSON object containing basic information for a specific award category
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no award
     *                      category record with the given ID exists, etc.
     */
    public static JsonNode getAwardCategoryBase(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/awards/categories/{id}", id));
    }

    /**
     * Returns extended information for a specific award category as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Award%20Categories/getAwardCategoryExtended">
     * <b>[GET]</b> /awards/categories/{id}/extended</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> award category ID
     *
     * @return JSON object containing extended information for a specific award category
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no award
     *                      category record with the given ID exists, etc.
     */
    public static JsonNode getAwardCategoryExtended(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/awards/categories/{id}/extended", id));
    }

    /**
     * Returns an overview of available awards as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Awards/getAllAwards">
     * <b>[GET]</b> /awards</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing an overview of available awards
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getAllAwards(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource("/awards"));
    }

    /**
     * Returns basic information for a specific award record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Awards/getAward">
     * <b>[GET]</b> /awards/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> award ID
     *
     * @return JSON object containing basic information for a specific award record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no award
     *                      record with the given ID exists, etc.
     */
    public static JsonNode getAwardBase(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/awards/{id}", id));
    }

    /**
     * Returns extended information for a specific award record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Awards/getAwardExtended">
     * <b>[GET]</b> /awards/{id}/extended</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> award ID
     *
     * @return JSON object containing extended information for a specific award record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no award
     *                      record with the given ID exists, etc.
     */
    public static JsonNode getAwardExtended(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/awards/{id}/extended", id));
    }
}

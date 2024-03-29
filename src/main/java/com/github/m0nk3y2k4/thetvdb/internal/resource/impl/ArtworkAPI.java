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
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Artwork">Artwork</a>,
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Artwork%20Statuses">Artwork Statuses</a> and
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Artwork%20Types">Artwork Types</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used for obtaining either basic, extended or
 * translated artwork information as well as an overview of available artwork status and artwork type records.
 */
public final class ArtworkAPI extends Resource {

    private ArtworkAPI() {}     // Private constructor. Only static methods

    /**
     * Returns an overview of available artwork statuses as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Artwork%20Statuses/getAllArtworkStatuses">
     * <b>[GET]</b> /artwork/statuses</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing an overview of available artwork statuses
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getAllArtworkStatuses(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource("/artwork/statuses"));
    }

    /**
     * Returns an overview of available artwork types as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Artwork%20Types/getAllArtworkTypes">
     * <b>[GET]</b> /artwork/types</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing an overview of available artwork types
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getAllArtworkTypes(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource("/artwork/types"));
    }

    /**
     * Returns basic information for a specific artwork record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Artwork/getArtworkBase">
     * <b>[GET]</b> /artwork/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> artwork ID
     *
     * @return JSON object containing basic information for a specific artwork record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no
     *                      artwork record with the given ID exists, etc.
     */
    public static JsonNode getArtworkBase(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/artwork/{id}", id));
    }

    /**
     * Returns extended information for a specific artwork record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Artwork/getArtworkExtended">
     * <b>[GET]</b> /artwork/{id}/extended</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> artwork ID
     *
     * @return JSON object containing extended information for a specific artwork record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no
     *                      artwork record with the given ID exists, etc.
     */
    public static JsonNode getArtworkExtended(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/artwork/{id}/extended", id));
    }
}

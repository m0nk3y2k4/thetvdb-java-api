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
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.0.0#/episodes">episodes</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used for obtaining either basic, extended or
 * translated episode information.
 */
public final class EpisodesAPI extends Resource {

    private EpisodesAPI() {}        // Private constructor. Only static methods

    /**
     * Returns basic information for a specific episode record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.0.0#/episodes/getEpisodeBase">
     * <b>[GET]</b> /episodes/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> episode ID
     *
     * @return JSON object containing basic information for a specific episode record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no
     *                      episode record with the given ID exists, etc.
     */
    public static JsonNode getEpisodeBase(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/episodes/{id}", id));
    }

    /**
     * Returns extended information for a specific episode record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.0.0#/episodes/getEpisodeExtended">
     * <b>[GET]</b> /episodes/{id}/extended</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> episode ID
     *
     * @return JSON object containing extended information for a specific episode record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no
     *                      episode record with the given ID exists, etc.
     */
    public static JsonNode getEpisodeExtended(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/episodes/{id}/extended", id));
    }

    /**
     * Returns a translation record for a specific episode as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.0.0#/episodes/getEpisodeTranslation">
     * <b>[GET]</b> /episodes/{id}/translations/{language}</a>
     *
     * @param con      Initialized connection to be used for API communication
     * @param id       The <i>TheTVDB.com</i> episode ID
     * @param language The 2- or 3-character language code
     *
     * @return JSON object containing a translation record for a specific episode
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no
     *                      episode translation record exists for the given ID and language, etc.
     */
    public static JsonNode getEpisodeTranslation(@Nonnull APIConnection con, long id, @Nonnull String language)
            throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        Parameters.validatePathParam(PATH_LANGUAGE, language, LANGUAGE_VALIDATOR);
        return con.sendGET(createResource("/episodes/{id}/translations/{language}", id, language));
    }
}

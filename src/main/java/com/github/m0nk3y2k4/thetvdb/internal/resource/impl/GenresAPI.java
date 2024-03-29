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
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Genres">Genres</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used for obtaining specific genre information.
 */
public final class GenresAPI extends Resource {

    private GenresAPI() {}      // Private constructor. Only static methods

    /**
     * Returns an overview of available genres as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Genres/getAllGenres">
     * <b>[GET]</b> /genres</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing an overview of available genres
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getAllGenres(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource("/genres"));
    }

    /**
     * Returns information for a specific genre record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Genres/getGenreBase">
     * <b>[GET]</b> /genres/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> genre ID
     *
     * @return JSON object containing information for a specific genre record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      genre ID does not exist, etc.
     */
    public static JsonNode getGenreBase(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/genres/{id}", id));
    }
}

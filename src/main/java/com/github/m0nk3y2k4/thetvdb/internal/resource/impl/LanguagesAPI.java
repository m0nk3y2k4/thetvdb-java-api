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
 * Implementation of a connector for the remote API's <a href="https://api.thetvdb.com/swagger#/Languages">Languages</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used to retrieve available languages and related
 * information.
 */
public final class LanguagesAPI extends Resource {

    /** Base URL path parameter for this endpoint */
    private static final String BASE = "/languages";

    private LanguagesAPI() {}     // Private constructor. Only static methods

    /**
     * Returns an overview of all supported languages as raw JSON. These language abbreviations can be used to set the
     * preferred language for the communication with the remote service.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages">
     * <b>[GET]</b> /languages</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing all languages that are supported by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getAllAvailable(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(BASE);
    }

    /**
     * Returns further language information for a given language ID as raw JSON. The language abbreviation can be used
     * to set the preferred language for the communication with the remote service.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages_id">
     * <b>[GET]</b> /languages/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The ID of the language
     *
     * @return JSON object containing detailed language information
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if the given language ID does not exist.
     */
    public static JsonNode get(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(BASE, id));
    }
}

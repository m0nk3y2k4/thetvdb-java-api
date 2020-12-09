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

/**
 * Implementation of a connector for the remote API's
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/artwork-types">artwork-types</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used for obtaining an overview of available
 * artwork type records.
 */
public final class ArtworkTypesAPI {

    /** Base URL path parameter for this endpoint */
    private static final String BASE = "/artwork-types";

    private ArtworkTypesAPI() {}        // Private constructor. Only static methods

    /**
     * Returns a list of available artwork types as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/artwork-types/getAllArtworkTypes">
     * <b>[GET]</b> /artwork-types</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing an overview of available artwork types
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getAllArtworkTypes(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(BASE);
    }
}

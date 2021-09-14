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

import static com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters.isPositiveInteger;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.resource.QueryResource;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Implementation of a connector for the remote API's
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Updates">Updates</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used for obtaining an overview of recently updated
 * records.
 */
public final class UpdatesAPI extends QueryResource {

    private UpdatesAPI() {}        // Private constructor. Only static methods

    /**
     * Returns an overview of recently updated entities based on the given query parameters as raw JSON. Note that the
     * given query parameters must always contain a valid <em>{@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates#SINCE}</em>
     * Epoch timestamp key.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Updates/updates">
     * <b>[GET]</b> /updates</a>
     *
     * @param con    Initialized connection to be used for API communication
     * @param params Object containing key/value pairs of query parameters
     *
     * @return JSON object containing an overview of recently updated entities
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getUpdates(@Nonnull APIConnection con, QueryParameters params) throws APIException {
        Parameters.validateQueryParam(Query.Updates.SINCE, params, isPositiveInteger());
        return con.sendGET(createQueryResource("/updates", params));
    }
}

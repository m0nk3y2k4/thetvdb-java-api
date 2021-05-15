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
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies">movies</a>
 * and
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movie-statuses">movie-statuses</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used for obtaining either basic, extended or
 * translated movie information as well as an overview of available movie and movie status records.
 */
public final class MoviesAPI extends QueryResource {

    private MoviesAPI() {}      // Private constructor. Only static methods

    /**
     * Returns a list of available movie statuses as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movie-statuses/getAllMovieStatuses">
     * <b>[GET]</b> /movies/statuses</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing an overview of available movie statuses
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getAllMovieStatuses(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource("/movies/statuses"));
    }

    /**
     * Returns a list of movies based on the given query parameters as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies/getAllMovie">
     * <b>[GET]</b> /movies</a>
     *
     * @param con    Initialized connection to be used for API communication
     * @param params Object containing key/value pairs of query parameters
     *
     * @return JSON object containing a limited overview of movies
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getAllMovies(@Nonnull APIConnection con, QueryParameters params) throws APIException {
        return con.sendGET(createQueryResource("/movies", params));
    }

    /**
     * Returns basic information for a specific movie record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies/getMovieBase">
     * <b>[GET]</b> /movies/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> movie ID
     *
     * @return JSON object containing basic information for a specific movie record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      movie ID does not exist, etc.
     */
    public static JsonNode getMovieBase(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/movies/{id}", id));
    }

    /**
     * Returns extended information for a specific movie record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies/getMovieExtended">
     * <b>[GET]</b> /movies/{id}/extended</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> movie ID
     *
     * @return JSON object containing extended information for a specific movie record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no movie
     *                      record with the given ID exists, etc.
     */
    public static JsonNode getMovieExtended(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/movies/{id}/extended", id));
    }

    /**
     * Returns a translation record for a specific movie as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies/getMovieTranslation">
     * <b>[GET]</b> /movies/{id}/translations/{language}</a>
     *
     * @param con      Initialized connection to be used for API communication
     * @param id       The <i>TheTVDB.com</i> movie ID
     * @param language The 2- or 3-character language code
     *
     * @return JSON object containing a translation record for a specific movie
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no movie
     *                      translation record exists for the given ID and language, etc.
     */
    public static JsonNode getMovieTranslation(@Nonnull APIConnection con, long id, @Nonnull String language)
            throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        Parameters.validatePathParam(PATH_LANGUAGE, language, LANGUAGE_VALIDATOR);
        return con.sendGET(createResource("/movies/{id}/translations/{language}", id, language));
    }
}

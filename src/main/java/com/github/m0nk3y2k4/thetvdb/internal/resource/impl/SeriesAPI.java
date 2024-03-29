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

import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series.AIR_DATE;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series.EPISODE_NUMBER;
import static com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series.SEASON;
import static com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters.isValidDate;
import static com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters.isValidLanguageCode;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesSeasonType;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.resource.QueryResource;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Implementation of a connector for the remote API's
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Series">Series</a> and
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Series%20Statuses">Series Statuses</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used for obtaining either basic, extended or
 * translated series information.
 */
public final class SeriesAPI extends QueryResource {

    /** Identifiers for dynamic URL path parameters */
    private static final String PATH_SEASONTYPE = "season-type";

    /** Pattern used to validate the conformity of optional 'airDate' query parameter value */
    private static final Pattern AIR_DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    /** Validator for the dynamic <em>{@code season-type}</em> URL path parameter */
    private static final Predicate<SeriesSeasonType> SEASONTYPE_VALIDATOR = Objects::nonNull;

    /** Validator for the optional 'airDate' query parameter value */
    private static final Predicate<String> AIR_DATE_VALIDATOR = isValidDate("yyyy-MM-dd")
            .and(value -> AIR_DATE_PATTERN.matcher(value).matches());

    private SeriesAPI() {}      // Private constructor. Only static methods

    /**
     * Returns an overview of available series statuses as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series%20Statuses/getAllSeriesStatuses">
     * <b>[GET]</b> /series/statuses</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing an overview of available series statuses
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getAllSeriesStatuses(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource("/series/statuses"));
    }

    /**
     * Returns an overview of series based on the given query parameters as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series/getAllSeries">
     * <b>[GET]</b> /series</a>
     *
     * @param con    Initialized connection to be used for API communication
     * @param params Object containing key/value pairs of query parameters
     *
     * @return JSON object containing a limited overview of series
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getAllSeries(@Nonnull APIConnection con, QueryParameters params) throws APIException {
        return con.sendGET(createQueryResource("/series", params));
    }

    /**
     * Returns basic information for a specific series record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesBase">
     * <b>[GET]</b> /series/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> series ID
     *
     * @return JSON object containing basic information for a specific series record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no series
     *                      record with the given ID exists, etc.
     */
    public static JsonNode getSeriesBase(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/series/{id}", id));
    }

    /**
     * Returns extended information for a specific series record including custom artworks based on the given query
     * parameters as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesArtworks">
     * <b>[GET]</b> /series/{id}/artworks</a>
     *
     * @param con    Initialized connection to be used for API communication
     * @param id     The <i>TheTVDB.com</i> series ID
     * @param params Object containing key/value pairs of query parameters
     *
     * @return JSON object containing extended information incl. language/type based artworks for a specific series
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no series
     *                      record with the given ID exists, etc.
     */
    public static JsonNode getSeriesArtworks(@Nonnull APIConnection con, long id, QueryParameters params)
            throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createQueryResource("/series/{id}/artworks", params, id));
    }

    /**
     * Returns extended information for a specific series record based on the given query parameters as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesExtended">
     * <b>[GET]</b> /series/{id}/extended</a>
     *
     * @param con    Initialized connection to be used for API communication
     * @param id     The <i>TheTVDB.com</i> series ID
     * @param params Object containing key/value pairs of query parameters
     *
     * @return JSON object containing extended information for a specific series record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no series
     *                      record with the given ID exists, etc.
     */
    public static JsonNode getSeriesExtended(@Nonnull APIConnection con, long id, QueryParameters params)
            throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createQueryResource("/series/{id}/extended", params, id));
    }

    /**
     * Returns the episodes of a particular series based on the given query parameters as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesEpisodes">
     * <b>[GET]</b> /series/{id}/episodes/{season-type}</a>
     *
     * @param con        Initialized connection to be used for API communication
     * @param id         The <i>TheTVDB.com</i> series ID
     * @param seasonType The type of season for which episodes should be returned
     * @param params     Object containing key/value pairs of query parameters
     *
     * @return JSON object containing a limited overview of a series episodes
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no series
     *                      record with the given ID exists, etc.
     */
    public static JsonNode getSeriesEpisodes(@Nonnull APIConnection con, long id, @Nonnull SeriesSeasonType seasonType,
            QueryParameters params) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        Parameters.validatePathParam(PATH_SEASONTYPE, seasonType, SEASONTYPE_VALIDATOR);
        Parameters.validateOptionalQueryParam(AIR_DATE, params, AIR_DATE_VALIDATOR);
        Parameters.validateOptionalQueryParam(EPISODE_NUMBER, params, episodeNumber -> {
            Parameters.validateMandatoryQueryParam(SEASON, params); // If episodeNumber is not null then season must be present
            return true;
        });
        return con.sendGET(createQueryResource("/series/{id}/episodes/{season-type}", params, id, seasonType));
    }

    /**
     * Returns basic information for a specific series record based on the given query parameters as raw JSON. The
     * contained episodes will be translated to the given language.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesSeasonEpisodesTranslated">
     * <b>[GET]</b> /series/{id}/episodes/{season-type}/{lang}</a>
     *
     * @param con        Initialized connection to be used for API communication
     * @param id         The <i>TheTVDB.com</i> series ID
     * @param seasonType The type of season for which episodes should be returned
     * @param language   The 2- or 3-character language code
     * @param params     Object containing key/value pairs of query parameters
     *
     * @return JSON object containing basic information incl. translated episodes for a specific series
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no series
     *                      record with the given ID exists, invalid language code, etc.
     */
    public static JsonNode getSeriesEpisodesTranslated(@Nonnull APIConnection con, long id,
            @Nonnull SeriesSeasonType seasonType, @Nonnull String language, QueryParameters params)
            throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        Parameters.validatePathParam(PATH_SEASONTYPE, seasonType, SEASONTYPE_VALIDATOR);
        Parameters.validatePathParam(PATH_LANGUAGE, language, isValidLanguageCode());
        return con.sendGET(createQueryResource("/series/{id}/episodes/{season-type}/{language}", params, id, seasonType, language));
    }

    /**
     * Returns a collection of series based on the given filter parameters as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesFilter">
     * <b>[GET]</b> /series/filter</a>
     *
     * @param con    Initialized connection to be used for API communication
     * @param params Object containing key/value pairs of query parameters
     *
     * @return JSON object containing a collection of series based on the given filter parameters
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getSeriesFilter(@Nonnull APIConnection con, QueryParameters params) throws APIException {
        Parameters.validateFilterQueryParams(params);
        return con.sendGET(createQueryResource("/series/filter", params));
    }

    /**
     * Returns a translation record for a specific series as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesTranslation">
     * <b>[GET]</b> /series/{id}/translations/{language}</a>
     *
     * @param con      Initialized connection to be used for API communication
     * @param id       The <i>TheTVDB.com</i> series ID
     * @param language The 2- or 3-character language code
     *
     * @return JSON object containing a translation record for a specific series
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no series
     *                      translation record exists for the given ID and language, etc.
     */
    public static JsonNode getSeriesTranslation(@Nonnull APIConnection con, long id, @Nonnull String language)
            throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        Parameters.validatePathParam(PATH_LANGUAGE, language, isValidLanguageCode());
        return con.sendGET(createResource("/series/{id}/translations/{language}", id, language));
    }
}

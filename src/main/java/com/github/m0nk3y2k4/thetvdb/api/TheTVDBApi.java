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

package com.github.m0nk3y2k4.thetvdb.api;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Artwork;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ArtworkType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Character;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Episode;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Genre;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Movie;
import com.github.m0nk3y2k4.thetvdb.api.model.data.People;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Season;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Series;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesDetails;

// ToDo: Revise JDoc once APIv4 implementation is finished

/**
 * Main interface of the <i>TheTVDB</i> API connector.
 * <p><br>
 * This interface provides access to all available routes of the remote <i>TheTVDB.com</i> REST API. Routes which accept
 * additional optional and mandatory query parameters can either be invoked with a given set of {@link QueryParameters}
 * or via some predefined shortcut-methods. These shortcut-methods will accept certain values as direct method
 * parameters which will then be forwarded to the REST API as regular URL query parameters. Please note that
 * shortcut-methods exist for most of the common query scenarios but maybe not for all. In case of more complex query
 * setups the user has to take care of creating a properly configured <em>{@code QueryParameters}</em> object, which is
 * slightly more effort than using the shortcut-methods but gives the user unlimited configuration options.
 * <p><br>
 * In order to create a new API instance the {@link com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory TheTVDBApiFactory}
 * should be used. This factory also provides additional helper methods, for example to easily create new <em>{@code
 * QueryParameters}</em>.
 * <p><br>
 * To cover a wide range of possible applications, this API connector provides multiple layouts in order to allow an
 * easy integration regardless of your actual project requirements. It gives you the option to use prefabbed DTO's which
 * will be parsed from the actual JSON returned by the remote service. In case you need advanced exception handling or
 * you prefer to parse the JSON into your own data models (or don't want to parse it at all), other API layouts will
 * provide you with extended API response DTO's or even with the raw JSON. The following API layouts are currently
 * available:
 * <ul>
 * <li>{@link TheTVDBApi}<br>
 * This is probably the most common layout. It provides various shortcut-methods and automatically maps the received
 * JSON <b><i>data</i></b> content into simple Java DTO's (at least for more complex response data). The user does not
 * have to worry about JSON parsing but can simply work with the returned DTO's like he works with every other Java
 * object. However, these objects do only contain the actually requested data and will not include any additional
 * contextual information that may be returned by the remote service (e.g. Pagination information, additional
 * validation or error data). Furthermore they will only provide access to properties that are
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.0.0">formally declared by the
 * API</a> (version {@value Version#API_VERSION}).</li>
 * <li>{@link Extended}<br>
 * This layout may be used for slightly advance API integration. Like the common layout it'll take care of parsing
 * the received JSON into Java DTO's but it will also provide access to any additional contextual information.
 * Methods of this layout will always return a single {@link APIResponse} object which consists of the actual data,
 * parsed as DTO, as well as all additional information which is available in the given context, like additional
 * error or pagination information. This layout does not provide any shortcut-methods.</li>
 * <li>{@link JSON}<br>
 * This layout may be used if you do not want any post-processing being applied to the actual remote service response
 * data. All methods within this layout will return the raw, unmodified JSON data as it was received from the API.
 * This might be useful if you prefer to map the JSON data yourself, want to use your own Java data models or if you
 * don't want to parse the JSON data at all (but forward it to some other service for example). It would also be the
 * preferred layout in case you need access to additional (e.g. experimental) properties that are not yet officially
 * declared by the formal API description. This layout does not provide any shortcut-methods though.</li>
 * </ul>
 * <p><br>
 * Once an API instance has been created, the additional layouts can be accessed via the {@link #extended()} or
 * {@link #json()} method.
 */
public interface TheTVDBApi {

    /**
     * Initializes the current API session by requesting a new token from the remote API. This token will be used for
     * authentication of all requests that are sent to the remote service by this API instance. The initialization will
     * be performed based on the constructor parameters used to create this API instance. Actually this method will do
     * the same as {@link #login()}.
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    void init() throws APIException;

    /**
     * Initializes the current API with the given token. This token will be used for authentication of all requests that
     * are sent to the remote service by this API instance. The given string must be a valid Base64 encoded token in the
     * regular JWT format <i>"{header}.{payload}.{signature}"</i>.
     * <p><br>
     * If the given token is (or becomes) expired it will be replaced by a new JWT automatically. The new token will be
     * requested from the remove service based on the constructor parameters used to create this API instance.
     *
     * @param token JSON Web Token to be used for remote API communication/authorization
     *
     * @throws APIException If the given string does not match the JSON Web Token format
     */
    void init(@Nonnull String token) throws APIException;

    /**
     * Returns the JSON Web Token used for authentication of all requests that are sent to the remote service by this
     * API instance. If the current API has not yet been initialized an empty <i>Optional</i> instance will be
     * returned.
     *
     * @return The JWT used by this API or an empty <i>Optional</i> if the API has not been initialized
     */
    Optional<String> getToken();

    /**
     * Sets the preferred language to be used for communication with the remote service. Some of the API calls might use
     * this setting in order to only return results that match the given language. If available, the data returned by
     * the remote API will be translated to the given language. The default language code is <b>"en"</b>.
     *
     * @param languageCode The language in which the results are to be returned
     */
    void setLanguage(String languageCode);

    /**
     * Initializes the current API session by requesting a new token from the remote API. This token will be used for
     * authentication of all requests that are sent to the remote service by this API instance. The initialization will
     * be performed based on the constructor parameters used to create this API instance. It is recommended to
     * login/initialize the session before making the first API call. However, if an API call is made without proper
     * initialization, an implicit login will be performed.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/login/post_login">
     * <b>[POST]</b> /login</a>
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    void login() throws APIException;

    /**
     * Refreshes the current, valid JWT session token. This method can be used to extend the expiration date (24 hours)
     * of the current session token without the need of a complete new login.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/login/get_refresh_token">
     * <b>[GET]</b> /refresh_token</a>
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    void refreshToken() throws APIException;

    /**
     * Returns a list of available artwork types mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/artwork-types/getAllArtworkTypes">
     * <b>[GET]</b> /artwork-types</a>
     *
     * @return List of available artwork types mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getArtworkTypes()
     * @see Extended#getArtworkTypes()
     */
    List<ArtworkType> getArtworkTypes() throws APIException;

    /**
     * Returns basic information for a specific artwork mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/artwork/getArtworkBase">
     * <b>[GET]</b> /artwork/{id}</a>
     *
     * @param artworkId The <i>TheTVDB.com</i> artwork ID
     *
     * @return Basic artwork information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if the given artwork ID does not exist.
     * @see JSON#getArtwork(long) TheTVDBApi.JSON.getArtwork(artworkId)
     * @see Extended#getArtwork(long) TheTVDBApi.Extended.getArtwork(artworkId)
     */
    Artwork getArtwork(long artworkId) throws APIException;

    /**
     * Returns information for a specific character mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/characters/getCharacterBase">
     * <b>[GET]</b> /characters/{id}</a>
     *
     * @param characterId The <i>TheTVDB.com</i> character ID
     *
     * @return Character information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if the given character ID does not exist.
     * @see JSON#getCharacter(long) TheTVDBApi.JSON.getCharacter(characterId)
     * @see Extended#getCharacter(long) TheTVDBApi.Extended.getCharacter(characterId)
     */
    Character getCharacter(long characterId) throws APIException;

    /**
     * Returns basic information for a specific episode mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/episodes/getEpisodeBase">
     * <b>[GET]</b> /episodes/{id}</a>
     *
     * @param episodeId The <i>TheTVDB.com</i> episode ID
     *
     * @return Basic episode information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if the given episode ID does not exist.
     * @see JSON#getEpisode(long) TheTVDBApi.JSON.getEpisode(episodeId)
     * @see Extended#getEpisode(long) TheTVDBApi.Extended.getEpisode(episodeId)
     */
    Episode getEpisode(long episodeId) throws APIException;

    /**
     * Returns a list of available genres mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/genres/getAllGenres">
     * <b>[GET]</b> /genres</a>
     *
     * @return List of available genres mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getGenres()
     * @see Extended#getGenres()
     */
    List<Genre> getGenres() throws APIException;

    /**
     * Returns information for a specific genre mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/genres/getGenreBase">
     * <b>[GET]</b> /genres/{id}</a>
     *
     * @param genreId The <i>TheTVDB.com</i> genre ID
     *
     * @return Genre information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if the given genre ID does not exist.
     * @see JSON#getGenre(long) TheTVDBApi.JSON.getGenre(genreId)
     * @see Extended#getGenre(long) TheTVDBApi.Extended.getGenre(genreId)
     */
    Genre getGenre(long genreId) throws APIException;

    /**
     * Returns basic information for a specific movie mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/movies/getMovieBase">
     * <b>[GET]</b> /movies/{id}</a>
     *
     * @param movieId The <i>TheTVDB.com</i> movie ID
     *
     * @return Basic movie information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if the given movie ID does not exist.
     * @see JSON#getMovie(long) TheTVDBApi.JSON.getMovie(movieId)
     * @see Extended#getMovie(long) TheTVDBApi.Extended.getMovie(movieId)
     */
    Movie getMovie(long movieId) throws APIException;

    /**
     * Returns basic information for a specific people mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/people/getPeopleBase">
     * <b>[GET]</b> /people/{id}</a>
     *
     * @param peopleId The <i>TheTVDB.com</i> people ID
     *
     * @return Basic people information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if the given people ID does not exist.
     * @see JSON#getPeople(long) TheTVDBApi.JSON.getPeople(peopleId)
     * @see Extended#getPeople(long) TheTVDBApi.Extended.getPeople(peopleId)
     */
    People getPeople(long peopleId) throws APIException;

    /**
     * Returns basic information for a specific season mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/seasons/getSeasonBase">
     * <b>[GET]</b> /seasons/{id}</a>
     *
     * @param seasonId The <i>TheTVDB.com</i> season ID
     *
     * @return Basic season information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if the given season ID does not exist.
     * @see JSON#getSeason(long) TheTVDBApi.JSON.getSeason(seasonId)
     * @see Extended#getSeason(long) TheTVDBApi.Extended.getSeason(seasonId)
     */
    Season getSeason(long seasonId) throws APIException;

    /**
     * Returns a list of series based on the given query parameters mapped as Java DTO. The list contains basic
     * information of all series matching the query parameters.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/series/getAllSeries">
     * <b>[GET]</b> /series</a>
     *
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return List of series mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#querySeries(QueryParameters) TheTVDBApi.JSON.querySeries(queryParameters)
     * @see Extended#querySeries(QueryParameters) TheTVDBApi.Extended.querySeries(queryParameters)
     */
    List<Series> querySeries(QueryParameters queryParameters) throws APIException;

    /**
     * Returns basic information for a specific series mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/series/getSeriesBase">
     * <b>[GET]</b> /series/{id}</a>
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     *
     * @return Basic series information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if the given series ID does not exist.
     * @see JSON#getSeries(long) TheTVDBApi.JSON.getSeries(seriesId)
     * @see Extended#getSeries(long) TheTVDBApi.Extended.getSeries(seriesId)
     */
    Series getSeries(long seriesId) throws APIException;

    /**
     * Returns detailed information for a specific series mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/series/getSeriesExtended">
     * <b>[GET]</b> /series/{id}/extended</a>
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     *
     * @return Detailed series information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if the given series ID does not exist.
     * @see JSON#getSeriesDetails(long) TheTVDBApi.JSON.getSeriesDetails(seriesId)
     * @see Extended#getSeriesDetails(long) TheTVDBApi.Extended.getSeriesDetails(seriesId)
     */
    SeriesDetails getSeriesDetails(long seriesId) throws APIException;

    /**
     * Provides access to the API's {@link JSON JSON} layout.
     * <p><br>
     * In this layout, all methods will return the raw, unmodified JSON as received from the remove service.
     *
     * @return Instance representing the the API's <em>{@code JSON}</em> layout
     */
    JSON json();

    /**
     * Provides access to the API's {@link Extended Extended} layout.
     * <p><br>
     * In this layout, all methods will return a single {@link APIResponse} object, containing the actual request data,
     * mapped as DTO, as well as all additional information that is available in the corresponding context.
     *
     * @return Instance representing the the API's <em>{@code Extended}</em> layout
     */
    Extended extended();

    /**
     * Interface representing the API's <em>{@code JSON}</em> layout.
     * <p><br>
     * This layout may be used if you do not want any post-processing being applied to the actual remote service
     * response data. All methods within this layout will return the raw, unmodified JSON data as it was received from
     * the API. This might be useful if you prefer to map the JSON data yourself, want to use your own Java data models
     * or if you don't want to parse the JSON data at all (but forward it to some other service for example). This
     * layout does not provide any shortcut-methods though.
     *
     * @see #json()
     */
    interface JSON {

        /**
         * Returns a list of available artwork types as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/artwork-types/getAllArtworkTypes">
         * <b>[GET]</b> /artwork-types</a>
         *
         * @return JSON object containing a list of available artwork types
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getArtworkTypes() TheTVDBApi.getArtworkTypes()
         * @see Extended#getArtworkTypes()
         */
        JsonNode getArtworkTypes() throws APIException;

        /**
         * Returns basic information for a specific artwork as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/artwork/getArtworkBase">
         * <b>[GET]</b> /artwork/{id}</a>
         *
         * @param artworkId The <i>TheTVDB.com</i> artwork ID
         *
         * @return JSON object containing basic information for a specific artwork
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given artwork ID does not exist.
         * @see TheTVDBApi#getArtwork(long) TheTVDBApi.getArtwork(artworkId)
         * @see Extended#getArtwork(long) TheTVDBApi.Extended.getArtwork(artworkId)
         */
        JsonNode getArtwork(long artworkId) throws APIException;

        /**
         * Returns information for a specific character as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/characters/getCharacterBase">
         * <b>[GET]</b> /characters/{id}</a>
         *
         * @param characterId The <i>TheTVDB.com</i> character ID
         *
         * @return JSON object containing information for a specific character
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given character ID does not exist.
         * @see TheTVDBApi#getCharacter(long) TheTVDBApi.getCharacter(characterId)
         * @see Extended#getCharacter(long) TheTVDBApi.Extended.getCharacter(characterId)
         */
        JsonNode getCharacter(long characterId) throws APIException;

        /**
         * Returns basic information for a specific episode as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/episodes/getEpisodeBase">
         * <b>[GET]</b> /episodes/{id}</a>
         *
         * @param episodeId The <i>TheTVDB.com</i> episode ID
         *
         * @return JSON object containing basic information for a specific episode
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given episode ID does not exist.
         * @see TheTVDBApi#getEpisode(long) TheTVDBApi.getEpisode(episodeId)
         * @see Extended#getEpisode(long) TheTVDBApi.Extended.getEpisode(episodeId)
         */
        JsonNode getEpisode(long episodeId) throws APIException;

        /**
         * Returns a list of available genres as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/genres/getAllGenres">
         * <b>[GET]</b> /genres</a>
         *
         * @return JSON object containing a list of available genres
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getGenres() TheTVDBApi.getGenres()
         * @see Extended#getGenres()
         */
        JsonNode getGenres() throws APIException;

        /**
         * Returns information for a specific genre as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/genres/getGenreBase">
         * <b>[GET]</b> /genres/{id}</a>
         *
         * @param genreId The <i>TheTVDB.com</i> genre ID
         *
         * @return JSON object containing information for a specific genre
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given genre ID does not exist.
         * @see TheTVDBApi#getGenre(long) TheTVDBApi.getGenre(genreId)
         * @see Extended#getGenre(long) TheTVDBApi.Extended.getGenre(genreId)
         */
        JsonNode getGenre(long genreId) throws APIException;

        /**
         * Returns basic information for a specific movie as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/movies/getMovieBase">
         * <b>[GET]</b> /movies/{id}</a>
         *
         * @param movieId The <i>TheTVDB.com</i> movie ID
         *
         * @return JSON object containing basic information for a specific movie
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given movie ID does not exist.
         * @see TheTVDBApi#getMovie(long) TheTVDBApi.getMovie(movieId)
         * @see Extended#getMovie(long) TheTVDBApi.Extended.getMovie(movieId)
         */
        JsonNode getMovie(long movieId) throws APIException;

        /**
         * Returns basic information for a specific people as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/people/getPeopleBase">
         * <b>[GET]</b> /people/{id}</a>
         *
         * @param peopleId The <i>TheTVDB.com</i> people ID
         *
         * @return JSON object containing basic information for a specific people
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given people ID does not exist.
         * @see TheTVDBApi#getPeople(long) TheTVDBApi.getPeople(peopleId)
         * @see Extended#getPeople(long) TheTVDBApi.Extended.getPeople(peopleId)
         */
        JsonNode getPeople(long peopleId) throws APIException;

        /**
         * Returns basic information for a specific season as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/seasons/getSeasonBase">
         * <b>[GET]</b> /seasons/{id}</a>
         *
         * @param seasonId The <i>TheTVDB.com</i> season ID
         *
         * @return JSON object containing basic information for a specific season
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given season ID does not exist.
         * @see TheTVDBApi#getSeason(long) TheTVDBApi.getSeason(seasonId)
         * @see Extended#getSeason(long) TheTVDBApi.Extended.getSeason(seasonId)
         */
        JsonNode getSeason(long seasonId) throws APIException;

        /**
         * Returns a list of series based on the given query parameters as raw JSON. The list contains basic information
         * of all series matching the query parameters.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/series/getAllSeries">
         * <b>[GET]</b> /series</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing a list of series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#querySeries(QueryParameters) TheTVDBApi.querySeries(queryParameters)
         * @see Extended#querySeries(QueryParameters) TheTVDBApi.Extended.querySeries(queryParameters)
         */
        JsonNode querySeries(QueryParameters queryParameters) throws APIException;

        /**
         * Returns basic information for a specific series as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/series/getSeriesBase">
         * <b>[GET]</b> /series/{id}</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return JSON object containing basic information for a specific series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given series ID does not exist.
         * @see TheTVDBApi#getSeries(long) TheTVDBApi.getSeries(seriesId)
         * @see Extended#getSeries(long) TheTVDBApi.Extended.getSeries(seriesId)
         */
        JsonNode getSeries(long seriesId) throws APIException;

        /**
         * Returns detailed information for a specific series as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/series/getSeriesExtended">
         * <b>[GET]</b> /series/{id}/extended</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return JSON object containing detailed information for a specific series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given series ID does not exist.
         * @see TheTVDBApi#getSeriesDetails(long) TheTVDBApi.getSeriesDetails(seriesId)
         * @see Extended#getSeriesDetails(long) TheTVDBApi.Extended.getSeriesDetails(seriesId)
         */
        JsonNode getSeriesDetails(long seriesId) throws APIException;
    }

    /**
     * Interface representing the API's <em>{@code Extended}</em> layout.
     * <p><br>
     * This layout may be used for slightly advance API integration. Like the common layout it'll take care of parsing
     * the received JSON into Java DTO's but it will also provide access to any additional contextual information.
     * Methods of this layout will always return a single {@link APIResponse} object which consists of the actual data,
     * parsed as DTO, as well as all additional information which is available in the given context, like additional
     * error or pagination information. This layout does not provide any shortcut-methods.
     *
     * @see #extended()
     */
    interface Extended {

        /**
         * Returns a response object containing a list of available artwork types mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/artwork-types/getAllArtworkTypes">
         * <b>[GET]</b> /artwork-types</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getArtworkTypes()
         * @see TheTVDBApi#getArtworkTypes() TheTVDBApi.getArtworkTypes()
         */
        APIResponse<List<ArtworkType>> getArtworkTypes() throws APIException;

        /**
         * Returns a response object containing basic information for a specific artwork mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/artwork/getArtworkBase">
         * <b>[GET]</b> /artwork/{id}</a>
         *
         * @param artworkId The <i>TheTVDB.com</i> artwork ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given artwork ID does not exist.
         * @see JSON#getArtwork(long) TheTVDBApi.JSON.getArtwork(artworkId)
         * @see TheTVDBApi#getArtwork(long) TheTVDBApi.getArtwork(artworkId)
         */
        APIResponse<Artwork> getArtwork(long artworkId) throws APIException;

        /**
         * Returns a response object containing information for a specific character mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/characters/getCharacterBase">
         * <b>[GET]</b> /characters/{id}</a>
         *
         * @param characterId The <i>TheTVDB.com</i> character ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given character ID does not exist.
         * @see JSON#getCharacter(long) TheTVDBApi.JSON.getCharacter(characterId)
         * @see TheTVDBApi#getCharacter(long) TheTVDBApi.getCharacter(characterId)
         */
        APIResponse<Character> getCharacter(long characterId) throws APIException;

        /**
         * Returns a response object containing basic information for a specific episode mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/episodes/getEpisodeBase">
         * <b>[GET]</b> /episodes/{id}</a>
         *
         * @param episodeId The <i>TheTVDB.com</i> episode ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given episode ID does not exist.
         * @see JSON#getEpisode(long) TheTVDBApi.JSON.getEpisode(episodeId)
         * @see TheTVDBApi#getEpisode(long) TheTVDBApi.getEpisode(episodeId)
         */
        APIResponse<Episode> getEpisode(long episodeId) throws APIException;

        /**
         * Returns a response object containing a list of available genres mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/genres/getAllGenres">
         * <b>[GET]</b> /genres</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getGenres()
         * @see TheTVDBApi#getGenres() TheTVDBApi.getGenres()
         */
        APIResponse<List<Genre>> getGenres() throws APIException;

        /**
         * Returns a response object containing information for a specific genre mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/genres/getGenreBase">
         * <b>[GET]</b> /genres/{id}</a>
         *
         * @param genreId The <i>TheTVDB.com</i> genre ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given genre ID does not exist.
         * @see JSON#getGenre(long) TheTVDBApi.JSON.getGenre(genreId)
         * @see TheTVDBApi#getGenre(long) TheTVDBApi.getGenre(genreId)
         */
        APIResponse<Genre> getGenre(long genreId) throws APIException;

        /**
         * Returns a response object containing basic information for a specific movie mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/movies/getMovieBase">
         * <b>[GET]</b> /movies/{id}</a>
         *
         * @param movieId The <i>TheTVDB.com</i> movie ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given movie ID does not exist.
         * @see JSON#getMovie(long) TheTVDBApi.JSON.getMovie(movieId)
         * @see TheTVDBApi#getMovie(long) TheTVDBApi.getMovie(movieId)
         */
        APIResponse<Movie> getMovie(long movieId) throws APIException;

        /**
         * Returns a response object containing basic information for a specific people mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/people/getPeopleBase">
         * <b>[GET]</b> /people/{id}</a>
         *
         * @param peopleId The <i>TheTVDB.com</i> people ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given people ID does not exist.
         * @see JSON#getPeople(long) TheTVDBApi.JSON.getPeople(peopleId)
         * @see TheTVDBApi#getPeople(long) TheTVDBApi.getPeople(peopleId)
         */
        APIResponse<People> getPeople(long peopleId) throws APIException;

        /**
         * Returns a response object containing basic information for a specific season mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/seasons/getSeasonBase">
         * <b>[GET]</b> /seasons/{id}</a>
         *
         * @param seasonId The <i>TheTVDB.com</i> season ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given season ID does not exist.
         * @see JSON#getSeason(long) TheTVDBApi.JSON.getSeason(seasonId)
         * @see TheTVDBApi#getSeason(long) TheTVDBApi.getSeason(seasonId)
         */
        APIResponse<Season> getSeason(long seasonId) throws APIException;

        /**
         * Returns a response object containing a list of series based on the given query parameters mapped as Java DTO.
         * The list contains basic information of all series matching the query parameters.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/series/getAllSeries">
         * <b>[GET]</b> /series</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#querySeries(QueryParameters) TheTVDBApi.JSON.querySeries(queryParameters)
         * @see TheTVDBApi#querySeries(QueryParameters) TheTVDBApi.querySeries(queryParameters)
         */
        APIResponse<List<Series>> querySeries(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing basic information for a specific series mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/series/getSeriesBase">
         * <b>[GET]</b> /series/{id}</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given series ID does not exist.
         * @see JSON#getSeries(long) TheTVDBApi.JSON.getSeries(seriesId)
         * @see TheTVDBApi#getSeries(long) TheTVDBApi.getSeries(seriesId)
         */
        APIResponse<Series> getSeries(long seriesId) throws APIException;

        /**
         * Returns a response object containing detailed information for a specific series mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/series/getSeriesExtended">
         * <b>[GET]</b> /series/{id}/extended</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given series ID does not exist.
         * @see JSON#getSeriesDetails(long) TheTVDBApi.JSON.getSeriesDetails(seriesId)
         * @see TheTVDBApi#getSeriesDetails(long) TheTVDBApi.getSeriesDetails(seriesId)
         */
        APIResponse<SeriesDetails> getSeriesDetails(long seriesId) throws APIException;
    }

    /**
     * Specifies the version of the <i>TheTVDB.com</i> remote API to be used by this connector
     */
    final class Version {
        /** Version of the <i>TheTVDB.com</i> remote API used by this connector */
        public static final String API_VERSION = "v4.0.0";

        /** Constant class. Should not be instantiated */
        private Version() {}
    }
}

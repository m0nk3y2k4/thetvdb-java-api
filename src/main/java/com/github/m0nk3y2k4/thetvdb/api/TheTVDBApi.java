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
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Actor;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Episode;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Image;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ImageQueryParameter;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ImageSummary;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Language;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Movie;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Rating;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Series;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesSearchResult;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesSummary;
import com.github.m0nk3y2k4.thetvdb.api.model.data.User;

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
 * <a href="https://api.thetvdb.com/swagger">formally declared by the API</a> (version {@value Version#API_VERSION})
 * .</li>
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
     * the remote API will be translated to the given language. The default language code is <b>"en"</b>. For a list of
     * supported languages see {@link #getAvailableLanguages()}.
     *
     * @param languageCode The language in which the results are to be returned
     *
     * @see #getAvailableLanguages()
     */
    void setLanguage(String languageCode);

    /**
     * Initializes the current API session by requesting a new token from the remote API. This token will be used for
     * authentication of all requests that are sent to the remote service by this API instance. The initialization will
     * be performed based on the constructor parameters used to create this API instance. It is recommended to
     * login/initialize the session before making the first API call. However, if an API call is made without proper
     * initialization, an implicit login will be performed.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Authentication/post_login">
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
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Authentication/get_refresh_token">
     * <b>[GET]</b> /refresh_token</a>
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    void refreshToken() throws APIException;

    /**
     * Returns the full information for a given episode id as mapped Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Episodes/get_episodes_id">
     * <b>[GET]</b> /episodes/{id}</a>
     *
     * @param episodeId The ID of the episode
     *
     * @return Mapped Java DTO containing the full episode information based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      episode ID does not exist, etc.
     * @see JSON#getEpisode(long) TheTVDBApi.JSON.getEpisode(episodeId)
     * @see Extended#getEpisode(long) TheTVDBApi.Extended.getEpisode(episodeId)
     */
    Episode getEpisode(long episodeId) throws APIException;

    /**
     * Returns a list of all supported languages mapped as Java DTO. These language abbreviations can be used to set the
     * preferred language for the communication with the remote service (see {@link #setLanguage(String)}.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages">
     * <b>[GET]</b> /languages</a>
     *
     * @return List of available languages mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAvailableLanguages()
     * @see Extended#getAvailableLanguages()
     */
    List<Language> getAvailableLanguages() throws APIException;

    /**
     * Returns further language information for a given language ID mapped as Java DTO. The language abbreviation can be
     * used to set the preferred language for the communication with the remote service (see {@link
     * #setLanguage(String)}.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages_id">
     * <b>[GET]</b> /languages/{id}</a>
     *
     * @param languageId The ID of the language
     *
     * @return Mapped Java DTO containing detailed language information based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if the given language ID does not exist.
     * @see JSON#getLanguage(long) TheTVDBApi.JSON.getLanguage(languageId)
     * @see Extended#getLanguage(long) TheTVDBApi.Extended.getLanguage(languageId)
     */
    Language getLanguage(long languageId) throws APIException;

    /**
     * Returns detailed information for a specific movie mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Movies/get_movies_id">
     * <b>[GET]</b> /movies/{id}</a>
     *
     * @param movieId The <i>TheTVDB.com</i> movie ID
     *
     * @return Detailed information for a specific movie mapped as Java DTO based on the JSON data returned by the
     *         remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      movie ID does not exist, etc.
     * @see JSON#getMovie(long) TheTVDBApi.JSON.getMovie(movieId)
     * @see Extended#getMovie(long) TheTVDBApi.Extended.getMovie(movieId)
     */
    Movie getMovie(long movieId) throws APIException;

    /**
     * Returns a list of ID's of all movies that have been updated since the given epoch timestamp.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Movies/get_movieupdates">
     * <b>[GET]</b> /movieupdates</a>
     *
     * @param since Epoch time to start your date range
     *
     * @return A list of updated movies ID's beginning at the given <em>{@code since}</em> time, based on the JSON data
     *         returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getMovieUpdates(long) TheTVDBApi.JSON.getMovieUpdates(since)
     * @see Extended#getMovieUpdates(long) TheTVDBApi.Extended.getMovieUpdates(since)
     */
    List<Long> getMovieUpdates(long since) throws APIException;

    /**
     * Returns a list of series search results based on the given query parameters mapped as Java DTO. The list contains
     * basic information of all series matching the query parameters.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series">
     * <b>[GET]</b> /search/series</a>
     *
     * @param queryParameters Object containing key/value pairs of query search parameters. For a complete list of
     *                        possible search parameters see the API documentation or use {@link
     *                        #getAvailableSeriesSearchParameters()}.
     *
     * @return List of series search results mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no records are found that match your query.
     * @see JSON#searchSeries(QueryParameters) TheTVDBApi.JSON.searchSeries(queryParameters)
     * @see Extended#searchSeries(QueryParameters) TheTVDBApi.Extended.searchSeries(queryParameters)
     */
    List<SeriesSearchResult> searchSeries(QueryParameters queryParameters) throws APIException;

    /**
     * Search for series by name. Returns a list of series search results mapped as Java DTO. The search results contain
     * basic information of all series matching the given name. This is a shortcut-method for {@link
     * #searchSeries(QueryParameters) searchSeries(queryParameters)} with a single "name" query parameter.
     *
     * @param name The name of the series to search for
     *
     * @return List of series search results mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no records are found that match your query.
     */
    List<SeriesSearchResult> searchSeriesByName(@Nonnull String name) throws APIException;

    /**
     * Search for series by IMDB-Id. Returns a list of series search results mapped as Java DTO. The search results
     * contain basic information of all series matching the given IMDB-Id. This is a shortcut-method for {@link
     * #searchSeries(QueryParameters) searchSeries(queryParameters)} with a single "imdbId" query parameter.
     *
     * @param imdbId The IMDB-Id of the series to search for
     *
     * @return List of series search results mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no records are found that match your query.
     */
    List<SeriesSearchResult> searchSeriesByImdbId(@Nonnull String imdbId) throws APIException;

    /**
     * Search for series by Zap2it-Id. Returns a list of series search results mapped as Java DTO. The search results
     * contain basic information of all series matching the given Zap2it-Id. This is a shortcut-method for {@link
     * #searchSeries(QueryParameters) searchSeries(queryParameters)} with a single "zap2itId" query parameter.
     *
     * @param zap2itId The Zap2it-Id of the series to search for
     *
     * @return List of series search results mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no records are found that match your query.
     */
    List<SeriesSearchResult> searchSeriesByZap2itId(@Nonnull String zap2itId) throws APIException;

    /**
     * Returns possible query parameters, which can be used to search for series, mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series_params">
     * <b>[GET]</b> /search/series/params</a>
     *
     * @return List of possible parameters to query by in the series search route
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAvailableSeriesSearchParameters()
     * @see Extended#getAvailableSeriesSearchParameters()
     * @see JSON#searchSeries(QueryParameters) TheTVDBApi.JSON.searchSeries(queryParams)
     * @see Extended#searchSeries(QueryParameters) TheTVDBApi.Extended.searchSeries(queryParams)
     * @see #searchSeries(QueryParameters) searchSeries(queryParams)
     */
    List<String> getAvailableSeriesSearchParameters() throws APIException;

    /**
     * Returns detailed information for a specific series mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id">
     * <b>[GET]</b> /series/{id}</a>
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     *
     * @return Detailed information for a specific series mapped as Java DTO based on the JSON data returned by the
     *         remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc.
     * @see JSON#getSeries(long) TheTVDBApi.JSON.getSeries(seriesId)
     * @see Extended#getSeries(long) TheTVDBApi.Extended.getSeries(seriesId)
     */
    Series getSeries(long seriesId) throws APIException;

    /**
     * Returns header information for a specific series as key/value pairs. Good for getting the Last-Updated header to
     * find out when the series was last modified.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/head_series_id">
     * <b>[HEAD]</b> /series/{id}</a>
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     *
     * @return HTML header information returned by the remote service mapped as key/value pairs
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc.
     * @see JSON#getSeriesHeaderInformation(long) TheTVDBApi.JSON.getSeriesHeaderInformation(seriesId)
     * @see Extended#getSeriesHeaderInformation(long) TheTVDBApi.Extended.getSeriesHeaderInformation(seriesId)
     */
    Map<String, String> getSeriesHeaderInformation(long seriesId) throws APIException;

    /**
     * Returns a list of actors for a specific series mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_actors">
     * <b>[GET]</b> /series/{id}/actors</a>
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     *
     * @return List of actors mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc.
     * @see JSON#getActors(long) TheTVDBApi.JSON.getActors(seriesId)
     * @see Extended#getActors(long) TheTVDBApi.Extended.getActors(seriesId)
     */
    List<Actor> getActors(long seriesId) throws APIException;

    /**
     * Returns all episodes of a specific series mapped as Java DTO. Results will be paginated with 100 results per
     * page. Use <em>{@code queryParameters}</em> to select a specific result page.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes">
     * <b>[GET]</b> /series/{id}/episodes</a>
     *
     * @param seriesId        The <i>TheTVDB.com</i> series ID
     * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
     *                        parameters see the API documentation.
     *
     * @return List of episodes mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc.
     * @see JSON#getEpisodes(long, QueryParameters) TheTVDBApi.JSON.getEpisodes(seriesId, queryParameters)
     * @see Extended#getEpisodes(long, QueryParameters) TheTVDBApi.Extended.getEpisodes(seriesId, queryParameters)
     */
    List<Episode> getEpisodes(long seriesId, QueryParameters queryParameters) throws APIException;

    /**
     * Returns the first 100 episodes of a specific series mapped as Java DTO. Note that this method is deterministic
     * and will always return the <b>first</b> result page of the available episodes. This is a shortcut-method for
     * {@link #getEpisodes(long, QueryParameters) getEpisodes(seriesId, queryParameters)} with an empty query
     * parameter.
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     *
     * @return List of episodes mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc.
     * @see #getEpisodes(long, long) getEpisodes(seriesId, page)
     */
    List<Episode> getEpisodes(long seriesId) throws APIException;

    /**
     * Returns a list of episodes of a specific series mapped as Java DTO. The result list will contain 100 episodes at
     * most. For series with more episodes use the <em>{@code page}</em> parameter to browse to a specific result page.
     * This is a shortcut-method for {@link #getEpisodes(long, QueryParameters) getEpisodes(seriesId, queryParameters)}
     * with a single "page" query parameter.
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     * @param page     The result page to be returned
     *
     * @return List of episodes mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc.
     * @see #getEpisodes(long) getEpisodes(seriesId)
     */
    List<Episode> getEpisodes(long seriesId, long page) throws APIException;

    /**
     * Returns all matching episodes of a specific series mapped as Java DTO. Results will be paginated. Note that this
     * method is deterministic and will always return the <b>first</b> result page of the available episodes.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query">
     * <b>[GET]</b> /series/{id}/episodes/query</a>
     *
     * @param seriesId        The <i>TheTVDB.com</i> series ID
     * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
     *                        query parameters see the API documentation or use {@link #getAvailableEpisodeQueryParameters(long)
     *                        getAvailableEpisodeQueryParameters(seriesId)}.
     *
     * @return List of episodes matching the query parameters, mapped as Java DTO's based on the JSON data returned by
     *         the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc. or if no records are found that match your query.
     * @see JSON#queryEpisodes(long, QueryParameters) TheTVDBApi.JSON.queryEpisodes(seriesId, queryParameters)
     * @see Extended#queryEpisodes(long, QueryParameters) TheTVDBApi.Extended.queryEpisodes(seriesId,
     *         queryParameters)
     */
    List<Episode> queryEpisodes(long seriesId, QueryParameters queryParameters) throws APIException;

    /**
     * Returns all episodes of a specific series and season mapped as Java DTO. Results will be paginated. Note that
     * this method is deterministic and will always return the <b>first</b> result page of the available episodes. This
     * is a shortcut-method for {@link #queryEpisodes(long, QueryParameters) queryEpisodes(seriesId, queryParameters)}
     * with a single "airedSeason" query parameter.
     *
     * @param seriesId    The <i>TheTVDB.com</i> series ID
     * @param airedSeason The number of the aired season to query for
     *
     * @return List of episodes for a specific season, mapped as Java DTO's based on the JSON data returned by the
     *         remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc. or if no records are found that match your query.
     * @see #queryEpisodesByAiredSeason(long, long, long) queryEpisodesByAiredSeason(seriesId, airedSeason, page)
     */
    List<Episode> queryEpisodesByAiredSeason(long seriesId, long airedSeason) throws APIException;

    /**
     * Returns all episodes of a specific series and season mapped as Java DTO. Results will be paginated. For seasons
     * with a high number of episodes use the <em>{@code page}</em> parameter to browse to a specific result page. This
     * is a shortcut-method for {@link #queryEpisodes(long, QueryParameters) queryEpisodes(seriesId, queryParameters)}
     * with a "airedSeason" and "page" query parameter.
     *
     * @param seriesId    The <i>TheTVDB.com</i> series ID
     * @param airedSeason The number of the aired season to query for
     * @param page        The result page to be returned
     *
     * @return List of episodes for a specific season, mapped as Java DTO's based on the JSON data returned by the
     *         remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc. or if no records are found that match your query.
     * @see #queryEpisodesByAiredSeason(long, long) queryEpisodesByAiredSeason(seriesId, airedSeason)
     */
    List<Episode> queryEpisodesByAiredSeason(long seriesId, long airedSeason, long page) throws APIException;

    /**
     * Returns all episodes of a specific series, matching the <em>{@code airedEpisode}</em> parameter, mapped as Java
     * DTO. Results will be paginated. This is a shortcut-method for {@link #queryEpisodes(long, QueryParameters)
     * queryEpisodes(seriesId, queryParameters)} with a single "airedEpisode" query parameter.
     * <p><br>
     * Note that an aired episode number might be associated with a specific season. If the series consists of more than
     * one season this method will return the matching aired episodes from all the seasons. Use {@link
     * #queryEpisodesByAbsoluteNumber(long, long)} in order to query for a single episode.
     *
     * @param seriesId     The <i>TheTVDB.com</i> series ID
     * @param airedEpisode The number of the aired episode to query for
     *
     * @return List of episodes for a specific season and aired episode number, mapped as Java DTO's based on the JSON
     *         data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc. or if no records are found that match your query.
     * @see #queryEpisodesByAbsoluteNumber(long, long) queryEpisodesByAbsoluteNumber(seriesId, absoluteNumber)
     */
    List<Episode> queryEpisodesByAiredEpisode(long seriesId, long airedEpisode) throws APIException;

    /**
     * Returns a specific episode of a series, mapped as Java DTO. Results will be paginated. This is a shortcut-method
     * for {@link #queryEpisodes(long, QueryParameters) queryEpisodes(seriesId, queryParameters)} with a single
     * "absoluteNumber" query parameter.
     * <p><br>
     * Note that (unlike an aired episode number) an absolute episode number should most likely be unique throughout all
     * episodes of a specific series. So in most cases the returned list will consist of only one element. However, as
     * the remote API doesn't give any guarantees that querying with an "absoluteNumber" parameter always returns one
     * episode record
     * <b>at most</b> this method will return all episode data as received from the remote service.
     *
     * @param seriesId       The <i>TheTVDB.com</i> series ID
     * @param absoluteNumber The absolute number of the episode to query for (this is not the episode ID!)
     *
     * @return List of episodes for an absolute episode number, mapped as Java DTO's based on the JSON data returned by
     *         the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc. or if no records are found that match your query.
     * @see #getEpisode(long) getEpisode(episodeId)
     */
    List<Episode> queryEpisodesByAbsoluteNumber(long seriesId, long absoluteNumber) throws APIException;

    /**
     * Returns a list of keys which are valid parameters for querying episodes, as plain Strings. These keys are
     * permitted to be used in {@link QueryParameters} objects when querying for specific episodes of a series.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query_params">
     * <b>[GET]</b> /series/{id}/episodes/query/params</a>
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     *
     * @return List of allowed keys to be used for querying episodes, based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc.
     * @see #queryEpisodes(long, QueryParameters) queryEpisodes(seriesId, queryParameters)
     * @see JSON#getAvailableEpisodeQueryParameters(long) TheTVDBApi.JSON.getAvailableEpisodeQueryParameters(seriesId)
     * @see Extended#getAvailableEpisodeQueryParameters(long) TheTVDBApi.Extended.getAvailableEpisodeQueryParameters(seriesId)
     */
    List<String> getAvailableEpisodeQueryParameters(long seriesId) throws APIException;

    /**
     * Returns a summary of the episodes and seasons available for a series, mapped as Java DTO.
     * <br>
     * <b>Note:</b> Season "0" is for all episodes that are considered to be specials.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_summary">
     * <b>[GET]</b> /series/{id}/episodes/summary</a>
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     *
     * @return A summary of the episodes and seasons available for the given series, mapped as Java DTO based on the
     *         JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc.
     * @see JSON#getSeriesEpisodesSummary(long) TheTVDBApi.JSON.getSeriesEpisodesSummary(seriesId)
     * @see Extended#getSeriesEpisodesSummary(long) TheTVDBApi.Extended.getSeriesEpisodesSummary(seriesId)
     */
    SeriesSummary getSeriesEpisodesSummary(long seriesId) throws APIException;

    /**
     * Returns a filtered series record based on the given parameters, mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_filter">
     * <b>[GET]</b> /series/{id}/filter</a>
     *
     * @param seriesId        The <i>TheTVDB.com</i> series ID
     * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
     *                        query parameters see the API documentation or use {@link #getAvailableSeriesFilterParameters(long)
     *                        getAvailableSeriesFilterParameters(seriesId)}.
     *
     * @return A filtered series record, mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc.
     * @see JSON#filterSeries(long, QueryParameters) TheTVDBApi.JSON.filterSeries(seriesId, queryParameters)
     * @see Extended#filterSeries(long, QueryParameters) TheTVDBApi.Extended.filterSeries(seriesId, queryParameters)
     */
    Series filterSeries(long seriesId, QueryParameters queryParameters) throws APIException;

    /**
     * Returns a series records, filtered by the supplied comma-separated list of keys, mapped as Java DTO. This is a
     * shortcut-method for {@link #filterSeries(long, QueryParameters) filterSeries(seriesId, queryParameters)} with a
     * single "keys" query parameter.
     *
     * @param seriesId   The <i>TheTVDB.com</i> series ID
     * @param filterKeys Comma-separated list of keys to filter by
     *
     * @return A filtered series record, mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc.
     */
    Series filterSeries(long seriesId, @Nonnull String filterKeys) throws APIException;

    /**
     * Returns a list of keys which are valid parameters for filtering series, as plain Strings. These keys are
     * permitted to be used in {@link QueryParameters} objects when filtering for a specific series.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_filter_params">
     * <b>[GET]</b> /series/{id}/filter/params</a>
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     *
     * @return A list of keys to filter by, based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc.
     * @see #filterSeries(long, QueryParameters) filterSeries(seriesId, queryParameters)
     * @see JSON#getAvailableSeriesFilterParameters(long) TheTVDBApi.JSON.getAvailableSeriesFilterParameters(seriesId)
     * @see Extended#getAvailableSeriesFilterParameters(long) TheTVDBApi.Extended.getAvailableSeriesFilterParameters(seriesId)
     */
    List<String> getAvailableSeriesFilterParameters(long seriesId) throws APIException;

    /**
     * Returns a summary of the images types and counts available for a particular series, mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images">
     * <b>[GET]</b> /series/{id}/images</a>
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     *
     * @return A summary of the image types and counts available for the given series, mapped as Java DTO based on the
     *         JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc.
     * @see JSON#getSeriesImagesSummary(long) TheTVDBApi.JSON.getSeriesImagesSummary(seriesId)
     * @see Extended#getSeriesImagesSummary(long) TheTVDBApi.Extended.getSeriesImagesSummary(seriesId)
     */
    ImageSummary getSeriesImagesSummary(long seriesId) throws APIException;

    /**
     * Returns the matching result of querying images for a specific series, mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query">
     * <b>[GET]</b> /series/{id}/images/query</a>
     *
     * @param seriesId        The <i>TheTVDB.com</i> series ID
     * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
     *                        query parameters see the API documentation or use {@link #getAvailableImageQueryParameters(long)
     *                        getAvailableImageQueryParameters(seriesId)}.
     *
     * @return List of images that matched the query, mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc. or if no records are found that match your query.
     * @see JSON#queryImages(long, QueryParameters) TheTVDBApi.JSON.queryImages(seriesId, queryParameters)
     * @see Extended#queryImages(long, QueryParameters) TheTVDBApi.Extended.queryImages(seriesId, queryParameters)
     */
    List<Image> queryImages(long seriesId, QueryParameters queryParameters) throws APIException;

    /**
     * Returns all images for a specific series, matching the given parameters, mapped as Java DTO. This is a
     * shortcut-method for {@link #queryImages(long, QueryParameters) queryImages(seriesId, queryParameters)} with a
     * "keyType" and "resolution" query parameter.
     * <p><br>
     * Note: For more details regarding valid values for the method specific query parameters see the API documentation
     * or use {@link #getAvailableImageQueryParameters(long) getAvailableImageQueryParameters(seriesId)}
     *
     * @param seriesId   The <i>TheTVDB.com</i> series ID
     * @param keyType    Type of image you're querying for (fanart, poster, etc.)
     * @param resolution Resolution to filter by (1280x1024, for example)
     *
     * @return List of images that matched the given parameters, mapped as Java DTO's based on the JSON data returned by
     *         the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc. or if no records are found that match your query.
     * @see #queryImages(long, String, String, String) queryImages(seriesId, keyType, resolution, subKey)
     */
    List<Image> queryImages(long seriesId, @Nonnull String keyType, @Nonnull String resolution) throws APIException;

    /**
     * Returns all images for a specific series, matching the given parameters, mapped as Java DTO. This is a
     * shortcut-method for {@link #queryImages(long, QueryParameters) queryImages(seriesId, queryParameters)} with a
     * "keyType", a "resolution" and a "subKey" query parameter.
     * <p><br>
     * Note: For more details regarding valid values for the method specific query parameters see the API documentation
     * or use {@link #getAvailableImageQueryParameters(long) getAvailableImageQueryParameters(seriesId)}
     *
     * @param seriesId   The <i>TheTVDB.com</i> series ID
     * @param keyType    Type of image you're querying for (fanart, poster, etc.)
     * @param resolution Resolution to filter by (1280x1024, for example)
     * @param subKey     Subkey for the other method query parameters
     *
     * @return List of images that matched the given parameters, mapped as Java DTO's based on the JSON data returned by
     *         the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc. or if no records are found that match your query.
     * @see #queryImages(long, String, String) queryImages(seriesId, keyType, resolution)
     */
    List<Image> queryImages(long seriesId, @Nonnull String keyType, @Nonnull String resolution, @Nonnull String subKey)
            throws APIException;

    /**
     * Returns all images of a specific type for a series, mapped as Java DTO. This is a shortcut-method for {@link
     * #queryImages(long, QueryParameters) queryImages(seriesId, queryParameters)} with a single "keyType" query
     * parameter.
     * <p><br>
     * Note: For more details regarding valid values for the method specific query parameters see the API documentation
     * or use {@link #getAvailableImageQueryParameters(long) getAvailableImageQueryParameters(seriesId)}
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     * @param keyType  Type of image you're querying for (fanart, poster, etc.)
     *
     * @return List of images of the given key type, mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc. or if no records are found that match your query.
     */
    List<Image> queryImagesByKeyType(long seriesId, @Nonnull String keyType) throws APIException;

    /**
     * Returns all images of a specific resolution for a series, mapped as Java DTO. This is a shortcut-method for
     * {@link #queryImages(long, QueryParameters) queryImages(seriesId, queryParameters)} with a single "resolution"
     * query parameter.
     * <p><br>
     * Note: For more details regarding valid values for the method specific query parameters see the API documentation
     * or use {@link #getAvailableImageQueryParameters(long) getAvailableImageQueryParameters(seriesId)}
     *
     * @param seriesId   The <i>TheTVDB.com</i> series ID
     * @param resolution Resolution to filter by (1280x1024, for example)
     *
     * @return List of images with the given resolution, mapped as Java DTO's based on the JSON data returned by the
     *         remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc. or if no records are found that match your query.
     */
    List<Image> queryImagesByResolution(long seriesId, @Nonnull String resolution) throws APIException;

    /**
     * Returns all images of a specific sub key for a series, mapped as Java DTO. This is a shortcut-method for {@link
     * #queryImages(long, QueryParameters) queryImages(seriesId, queryParameters)} with a single "subKey" query
     * parameter.
     * <p><br>
     * Note: For more details regarding valid values for the method specific query parameters see the API documentation
     * or use {@link #getAvailableImageQueryParameters(long) getAvailableImageQueryParameters(seriesId)}
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     * @param subKey   Subkey to query for
     *
     * @return List of images matching the given sub key, mapped as Java DTO's based on the JSON data returned by the
     *         remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc. or if no records are found that match your query.
     */
    List<Image> queryImagesBySubKey(long seriesId, @Nonnull String subKey) throws APIException;

    /**
     * Returns a list of valid parameters for querying a series images, mapped as Java DTO. Unlike other routes,
     * querying for a series images may be restricted to certain combinations of query keys. The allowed combinations
     * are clustered in the single {@link ImageQueryParameter} objects returned by this method.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query_params">
     * <b>[GET]</b> /series/{id}/images/query/params</a>
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     *
     * @return A list of possible parameters which may be used to query a series images, mapped as Java DTO's based on
     *         the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given
     *                      series ID does not exist, etc.
     * @see #queryImages(long, QueryParameters) queryImages(seriesId, queryParameters)
     * @see JSON#getAvailableImageQueryParameters(long) TheTVDBApi.JSON.getAvailableImageQueryParameters(seriesId)
     * @see Extended#getAvailableImageQueryParameters(long) TheTVDBApi.Extended.getAvailableImageQueryParameters(seriesId)
     */
    List<ImageQueryParameter> getAvailableImageQueryParameters(long seriesId) throws APIException;

    /**
     * Returns a map of series that have changed in a maximum of one week blocks since the provided <em>{@code
     * fromTime}</em> query parameter. The key/value pairs of the returned map represent a <i>TheTVDB.com</i> series ID
     * (key) and when it was updated the last time (value) as Epoch time. Note that the given query parameters must
     * always contain a valid <em>{@code fromTime}</em> Epoch timestamp key.
     * <p><br>
     * The user may specify an additional <em>{@code toTime}</em> query key to grab results for less than a week. Any
     * timespan larger than a week will be reduced down to one week automatically.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Updates/get_updated_query">
     * <b>[GET]</b> /updated/query</a>
     *
     * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
     *                        query parameters see the API documentation or use {@link #getAvailableLastUpdatedQueryParameters()}.
     *
     * @return A map of updated objects that match the given timeframe, based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or no records exist for the given timespan.
     * @see JSON#queryLastUpdated(QueryParameters) TheTVDBApi.JSON.queryLastUpdated(queryParameters)
     * @see Extended#queryLastUpdated(QueryParameters) TheTVDBApi.Extended.queryLastUpdated(queryParameters)
     */
    Map<Long, Long> queryLastUpdated(QueryParameters queryParameters) throws APIException;

    /**
     * Returns a map of series that have changed in the (one) week since the provided <em>{@code fromTime}</em> query
     * parameter. The key/value pairs of the returned map represent a <i>TheTVDB.com</i> series ID (key) and when it was
     * updated the last time (value) as Epoch time. This is a shortcut-method for {@link
     * #queryLastUpdated(QueryParameters) queryLastUpdated(queryParameters)} with a single "fromTime" query parameter.
     *
     * @param fromTime Epoch time to start your date range
     *
     * @return A map of updated objects beginning at the given <em>{@code fromTime}</em>, based on the JSON data
     *         returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or no records exist for the given timespan.
     * @see #queryLastUpdated(long, long) queryLastUpdated(fromTime, toTime)
     */
    Map<Long, Long> queryLastUpdated(long fromTime) throws APIException;

    /**
     * Returns a map of series that have changed in between the given timeframe, but with a maximum of one week,
     * starting at the provided <em>{@code fromTime}</em> query parameter. The <em>{@code toTime}</em> parameter may be
     * specified to grab results for less than a week. Any timespan larger than a week will be reduced down to one week
     * automatically. The key/value pairs of the returned map represent a <i>TheTVDB.com</i> series ID (key) and when it
     * was updated the last time (value) as Epoch time. This is a shortcut-method for {@link
     * #queryLastUpdated(QueryParameters) queryLastUpdated(queryParameters)} with a "fromTime" and a "toTime" query
     * parameter.
     *
     * @param fromTime Epoch time to start your date range
     * @param toTime   Epoch time to end your date range. Must not be greater than one week from <em>{@code
     *                 fromTime}</em>.
     *
     * @return A map of updated objects matching the given timeframe, based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or no records exist for the given timespan.
     * @see #queryLastUpdated(long) queryLastUpdated(fromTime)
     */
    Map<Long, Long> queryLastUpdated(long fromTime, long toTime) throws APIException;

    /**
     * Returns a list of valid parameters for querying series which have been updated lately, as plain Strings. These
     * keys are permitted to be used in {@link QueryParameters} objects when querying for recently updated series.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Updates/get_updated_query_params">
     * <b>[GET]</b> /updated/query/params</a>
     *
     * @return A list of possible parameters which may be used to query for last updated series, based on the JSON data
     *         returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see #queryLastUpdated(QueryParameters) queryLastUpdated(queryParameters)
     * @see JSON#getAvailableLastUpdatedQueryParameters()
     * @see Extended#getAvailableLastUpdatedQueryParameters()
     */
    List<String> getAvailableLastUpdatedQueryParameters() throws APIException;

    /**
     * Returns basic information about the currently authenticated user, mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user">
     * <b>[GET]</b> /user</a>
     *
     * @return Basic user information, mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no information exists for the current user
     * @see JSON#getUser()
     * @see Extended#getUser()
     */
    User getUser() throws APIException;

    /**
     * Returns a list of favorite series for a given user, as plain Strings. Will be an empty list if no favorites
     * exist.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_favorites">
     * <b>[GET]</b> /user/favorites</a>
     *
     * @return The user favorites, based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no information exists for the current user
     * @see JSON#getFavorites()
     * @see Extended#getFavorites()
     */
    List<String> getFavorites() throws APIException;

    /**
     * Deletes the given series ID from the user’s favorite’s list and returns the updated list as plain Strings.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/delete_user_favorites_id">
     * <b>[DELETE]</b> /user/favorites/{id}</a>
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     *
     * @return Updated list of user favorites, based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no information exists for the current user or the requested record
     *                      could not be deleted
     * @see #addToFavorites(long) addToFavorites(seriesId)
     * @see JSON#deleteFromFavorites(long) TheTVDBApi.JSON.deleteFromFavorites(seriesId)
     * @see Extended#deleteFromFavorites(long) TheTVDBApi.Extended.deleteFromFavorites(seriesId)
     */
    List<String> deleteFromFavorites(long seriesId) throws APIException;

    /**
     * Adds the supplied series ID to the user’s favorite’s list and returns the updated list as plain Strings.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/put_user_favorites_id">
     * <b>[PUT]</b> /user/favorites/{id}</a>
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     *
     * @return Updated list of user favorites, based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no information exists for the current user or the requested record
     *                      could not be updated
     * @see #deleteFromFavorites(long) deleteFromFavorites(seriesId)
     * @see JSON#addToFavorites(long) TheTVDBApi.JSON.addToFavorites(seriesId)
     * @see Extended#addToFavorites(long) TheTVDBApi.Extended.addToFavorites(seriesId)
     */
    List<String> addToFavorites(long seriesId) throws APIException;

    /**
     * Returns a list of ratings for the given user, mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings">
     * <b>[GET]</b> /user/ratings</a>
     *
     * @return List of user ratings, mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no information exists for the current user
     * @see JSON#getRatings()
     * @see Extended#getRatings()
     */
    List<Rating> getRatings() throws APIException;

    /**
     * Returns a list of ratings for a given user that match the query, mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings_query">
     * <b>[GET]</b> /user/ratings/query</a>
     *
     * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
     *                        query parameters see the API documentation or use {@link #getAvailableRatingsQueryParameters()}.
     *
     * @return List of user ratings that match the given query, mapped as Java DTO's based on the JSON data returned by
     *         the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no information exists for the current user
     * @see JSON#queryRatings(QueryParameters) TheTVDBApi.JSON.queryRatings(queryParameters)
     * @see Extended#queryRatings(QueryParameters) TheTVDBApi.Extended.queryRatings(queryParameters)
     */
    List<Rating> queryRatings(QueryParameters queryParameters) throws APIException;

    /**
     * Returns a list of ratings for a given user that match the <em>{@code itemType}</em> parameter, mapped as Java
     * DTO. This is a shortcut-method for {@link #queryRatings(QueryParameters) queryRatings(queryParameters)} with a
     * single "itemType" query parameter.
     *
     * @param itemType Item to query. Can be either 'series', 'episode', or 'banner'.
     *
     * @return List of user ratings with the given item type, mapped as Java DTO's based on the JSON data returned by
     *         the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    List<Rating> queryRatingsByItemType(@Nonnull String itemType) throws APIException;

    /**
     * Returns a list of valid parameters for querying user ratings, as plain Strings. These keys are permitted to be
     * used in {@link QueryParameters} objects when querying for ratings.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings_query_params">
     * <b>[GET]</b> /user/ratings/query/params</a>
     *
     * @return A list of possible parameters which may be used to query for user ratings, based on the JSON data
     *         returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no information exists for the current user
     * @see #queryRatings(QueryParameters) queryRatings(queryParameters)
     * @see JSON#getAvailableRatingsQueryParameters()
     * @see Extended#getAvailableRatingsQueryParameters()
     */
    List<String> getAvailableRatingsQueryParameters() throws APIException;

    /**
     * Deletes a given rating of a given type.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/delete_user_ratings_itemType_itemId">
     * <b>[DELETE]</b> /user/ratings/{itemType}/{itemId}</a>
     *
     * @param itemType Item to update. Can be either 'series', 'episode', or 'image'.
     * @param itemId   ID of the ratings record that you wish to delete
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no rating is found that matches your given parameters
     * @see #addToRatings(String, long, long) addToRatings(itemType, itemId, itemRating)
     * @see JSON#deleteFromRatings(String, long) TheTVDBApi.JSON.deleteFromRatings(itemType, itemId)
     * @see Extended#deleteFromRatings(String, long) TheTVDBApi.Extended.deleteFromRatings(itemType, itemId)
     */
    void deleteFromRatings(@Nonnull String itemType, long itemId) throws APIException;

    /**
     * Updates a given rating of a given type and returns the modified rating, mapped as Java DTO. If no rating exists
     * yet, a new rating will be created.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/put_user_ratings_itemType_itemId_itemRating">
     * <b>[PUT]</b> /user/ratings/{itemType}/{itemId}/{itemRating}</a>
     *
     * @param itemType   Item to update. Can be either 'series', 'episode', or 'image'.
     * @param itemId     ID of the ratings record that you wish to modify
     * @param itemRating The updated rating number
     *
     * @return The modified rating (whether it was added or updated), mapped as Java DTO based on the JSON data returned
     *         by the remote service
     *         <br>
     *         <b>Note:</b> It seems that the data returned by the remote service for this route is quite unreliable!
     *         It might not always return the modified rating but an empty data array instead.
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no rating is found that matches your given parameters
     * @see #deleteFromRatings(String, long) deleteFromRatings(itemType, itemId)
     * @see JSON#addToRatings(String, long, long) TheTVDBApi.JSON.addToRatings(itemType, itemId, itemRating)
     * @see Extended#addToRatings(String, long, long) TheTVDBApi.Extended.addToRatings(itemType, itemId, itemRating)
     */
    List<Rating> addToRatings(@Nonnull String itemType, long itemId, long itemRating) throws APIException;

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
         * Returns the full information for a given episode id as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Episodes/get_episodes_id">
         * <b>[GET]</b> /episodes/{id}</a>
         *
         * @param episodeId The ID of the episode
         *
         * @return JSON object containing the full episode information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given episode ID does not exist, etc.
         * @see TheTVDBApi#getEpisode(long) TheTVDBApi.getEpisode(episodeId)
         * @see Extended#getEpisode(long) TheTVDBApi.Extended.getEpisode(episodeId)
         */
        JsonNode getEpisode(long episodeId) throws APIException;

        /**
         * Returns an overview of all supported languages as raw JSON. These language abbreviations can be used to set
         * the preferred language for the communication with the remote service (see {@link #setLanguage(String)}.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages">
         * <b>[GET]</b> /languages</a>
         *
         * @return JSON object containing all languages that are supported by the remote service
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAvailableLanguages() TheTVDBApi.getAvailableLanguages()
         * @see Extended#getAvailableLanguages()
         */
        JsonNode getAvailableLanguages() throws APIException;

        /**
         * Returns further language information for a given language ID as raw JSON. The language abbreviation can be
         * used to set the preferred language for the communication with the remote service (see {@link
         * #setLanguage(String)}.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages_id">
         * <b>[GET]</b> /languages/{id}</a>
         *
         * @param languageId The ID of the language
         *
         * @return JSON object containing detailed language information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given language ID does not exist.
         * @see TheTVDBApi#getLanguage(long) TheTVDBApi.getLanguage(languageId)
         * @see Extended#getLanguage(long) TheTVDBApi.Extended.getLanguage(languageId)
         */
        JsonNode getLanguage(long languageId) throws APIException;

        /**
         * Returns detailed information for a specific movie as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Movies/get_movies_id">
         * <b>[GET]</b> /movies/{id}</a>
         *
         * @param movieId The <i>TheTVDB.com</i> movie ID
         *
         * @return JSON object containing detailed information for a specific movie
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given movie ID does not exist, etc.
         * @see TheTVDBApi#getMovie(long) TheTVDBApi.getMovie(movieId)
         * @see Extended#getMovie(long) TheTVDBApi.Extended.getMovie(movieId)
         */
        JsonNode getMovie(long movieId) throws APIException;

        /**
         * Returns a list of ID's of all movies, that have been updated since the given epoch timestamp, as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Movies/get_movieupdates">
         * <b>[GET]</b> /movieupdates</a>
         *
         * @param since Epoch time to start your date range
         *
         * @return JSON object containing the ID's of all movies that have been updated beginning at the given
         *         <em>{@code since}</em> time
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getMovieUpdates(long) TheTVDBApi.getMovieUpdates(since)
         * @see Extended#getMovieUpdates(long) TheTVDBApi.Extended.getMovieUpdates(since)
         */
        JsonNode getMovieUpdates(long since) throws APIException;

        /**
         * Returns series search results based on the given query parameters as raw JSON. The result contains basic
         * information of all series matching the query parameters.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series">
         * <b>[GET]</b> /search/series</a>
         *
         * @param queryParameters Object containing key/value pairs of query search parameters. For a complete list of
         *                        possible search parameters see the API documentation or use {@link
         *                        #getAvailableSeriesSearchParameters()}.
         *
         * @return JSON object containing the series search results
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no records are found that match your query.
         * @see TheTVDBApi#searchSeries(QueryParameters) TheTVDBApi.searchSeries(queryParameters)
         * @see Extended#searchSeries(QueryParameters) TheTVDBApi.Extended.searchSeries(queryParameters)
         */
        JsonNode searchSeries(QueryParameters queryParameters) throws APIException;

        /**
         * Returns possible query parameters, which can be used to search for series, as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series_params">
         * <b>[GET]</b> /search/series/params</a>
         *
         * @return JSON object containing possible parameters to query by in the series search route
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAvailableSeriesSearchParameters() TheTVDBApi.getAvailableSeriesSearchParameters()
         * @see Extended#getAvailableSeriesSearchParameters()
         * @see #searchSeries(QueryParameters) searchSeries(queryParams)
         * @see TheTVDBApi#searchSeries(QueryParameters) TheTVDBApi.searchSeries(queryParams)
         * @see Extended#searchSeries(QueryParameters) TheTVDBApi.Extended.searchSeries(queryParams)
         */
        JsonNode getAvailableSeriesSearchParameters() throws APIException;

        /**
         * Returns detailed information for a specific series as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id">
         * <b>[GET]</b> /series/{id}</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return JSON object containing detailed information for a specific series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see TheTVDBApi#getSeries(long) TheTVDBApi.getSeries(seriesId)
         * @see Extended#getSeries(long) TheTVDBApi.Extended.getSeries(seriesId)
         */
        JsonNode getSeries(long seriesId) throws APIException;

        /**
         * Returns header information for a specific series as raw JSON. Good for getting the Last-Updated header to
         * find out when the series was last modified.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/head_series_id">
         * <b>[HEAD]</b> /series/{id}</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return Artificial JSON object based on the HTML header information returned by the remote service
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see TheTVDBApi#getSeriesHeaderInformation(long) TheTVDBApi.getSeriesHeaderInformation(seriesId)
         * @see Extended#getSeriesHeaderInformation(long) TheTVDBApi.Extended.getSeriesHeaderInformation(seriesId)
         */
        JsonNode getSeriesHeaderInformation(long seriesId) throws APIException;

        /**
         * Returns the actors for a specific series as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_actors">
         * <b>[GET]</b> /series/{id}/actors</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return JSON object containing the actors for a specific series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see TheTVDBApi#getActors(long) TheTVDBApi.getActors(seriesId)
         * @see Extended#getActors(long) TheTVDBApi.Extended.getActors(seriesId)
         */
        JsonNode getActors(long seriesId) throws APIException;

        /**
         * Returns all episodes of a specific series as raw JSON. Results will be paginated with 100 results per page.
         * Use <em>{@code queryParameters}</em> to select a specific result page.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes">
         * <b>[GET]</b> /series/{id}/episodes</a>
         *
         * @param seriesId        The <i>TheTVDB.com</i> series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
         *                        parameters see the API documentation.
         *
         * @return JSON object containing a single result page of episodes
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see TheTVDBApi#getEpisodes(long, QueryParameters) TheTVDBApi.getEpisodes(seriesId, queryParameters)
         * @see Extended#getEpisodes(long, QueryParameters) TheTVDBApi.Extended.getEpisodes(seriesId,
         *         queryParameters)
         */
        JsonNode getEpisodes(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns all matching episodes of a specific series as raw JSON. Results will be paginated. Use <em>{@code
         * queryParameters}</em> to filter for specific episodes or to select a specific result page.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query">
         * <b>[GET]</b> /series/{id}/episodes/query</a>
         *
         * @param seriesId        The <i>TheTVDB.com</i> series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
         *                        query parameters see the API documentation or use {@link #getAvailableEpisodeQueryParameters(long)
         *                        getAvailableEpisodeQueryParameters(seriesId)}.
         *
         * @return JSON object containing a single result page of queried episode records
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc. or if no records are found that match your query.
         * @see TheTVDBApi#queryEpisodes(long, QueryParameters) TheTVDBApi.queryEpisodes(seriesId, queryParameters)
         * @see Extended#queryEpisodes(long, QueryParameters) TheTVDBApi.Extended.queryEpisodes(seriesId,
         *         queryParameters)
         */
        JsonNode queryEpisodes(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a list of keys which are valid parameters for querying episodes, as raw JSON. These keys are
         * permitted to be used in {@link QueryParameters} objects when querying for specific episodes of a series.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query_params">
         * <b>[GET]</b> /series/{id}/episodes/query/params</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return JSON object containing all allowed keys to be used for querying episodes
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see #queryEpisodes(long, QueryParameters) queryEpisodes(seriesId, queryParameters)
         * @see TheTVDBApi#getAvailableEpisodeQueryParameters(long) TheTVDBApi.getAvailableEpisodeQueryParameters(seriesId)
         * @see Extended#getAvailableEpisodeQueryParameters(long) TheTVDBApi.Extended.getAvailableEpisodeQueryParameters(seriesId)
         */
        JsonNode getAvailableEpisodeQueryParameters(long seriesId) throws APIException;

        /**
         * Returns a summary of the episodes and seasons available for a series, as raw JSON.
         * <br>
         * <b>Note:</b> Season "0" is for all episodes that are considered to be specials.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_summary">
         * <b>[GET]</b> /series/{id}/episodes/summary</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return JSON object containing a summary of the episodes and seasons available for the given series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see TheTVDBApi#getSeriesEpisodesSummary(long) TheTVDBApi.getSeriesEpisodesSummary(seriesId)
         * @see Extended#getSeriesEpisodesSummary(long) TheTVDBApi.Extended.getSeriesEpisodesSummary(seriesId)
         */
        JsonNode getSeriesEpisodesSummary(long seriesId) throws APIException;

        /**
         * Returns a filtered series record based on the given parameters, as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_filter">
         * <b>[GET]</b> /series/{id}/filter</a>
         *
         * @param seriesId        The <i>TheTVDB.com</i> series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
         *                        query parameters see the API documentation or use {@link #getAvailableSeriesFilterParameters(long)
         *                        getAvailableSeriesFilterParameters(seriesId)}.
         *
         * @return JSON object containing a filtered series record
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see TheTVDBApi#filterSeries(long, QueryParameters) TheTVDBApi.filterSeries(seriesId, queryParameters)
         * @see Extended#filterSeries(long, QueryParameters) TheTVDBApi.Extended.filterSeries(seriesId,
         *         queryParameters)
         */
        JsonNode filterSeries(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a list of keys which are valid parameters for filtering series, as raw JSON. These keys are permitted
         * to be used in {@link QueryParameters} objects when filtering for a specific series.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_filter_params">
         * <b>[GET]</b> /series/{id}/filter/params</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return JSON object containing a list of all keys allowed to filter by
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see #filterSeries(long, QueryParameters) filterSeries(seriesId, queryParameters)
         * @see TheTVDBApi#getAvailableSeriesFilterParameters(long) TheTVDBApi.getAvailableSeriesFilterParameters(seriesId)
         * @see Extended#getAvailableSeriesFilterParameters(long) TheTVDBApi.Extended.getAvailableSeriesFilterParameters(seriesId)
         */
        JsonNode getAvailableSeriesFilterParameters(long seriesId) throws APIException;

        /**
         * Returns a summary of the images types and counts available for a particular series, as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images">
         * <b>[GET]</b> /series/{id}/images</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return JSON object containing a summary of the image types and counts available for the given series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see TheTVDBApi#getSeriesImagesSummary(long) TheTVDBApi.getSeriesImagesSummary(seriesId)
         * @see Extended#getSeriesImagesSummary(long) TheTVDBApi.Extended.getSeriesImagesSummary(seriesId)
         */
        JsonNode getSeriesImagesSummary(long seriesId) throws APIException;

        /**
         * Returns the matching result of querying images for a specific series, as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query">
         * <b>[GET]</b> /series/{id}/images/query</a>
         *
         * @param seriesId        The <i>TheTVDB.com</i> series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
         *                        query parameters see the API documentation or use {@link #getAvailableImageQueryParameters(long)
         *                        getAvailableImageQueryParameters(seriesId)}.
         *
         * @return JSON object containing images that matched the query
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc. or if no records are found that match your query.
         * @see TheTVDBApi#queryImages(long, QueryParameters) TheTVDBApi.queryImages(seriesId, queryParameters)
         * @see Extended#queryImages(long, QueryParameters) TheTVDBApi.Extended.queryImages(seriesId,
         *         queryParameters)
         */
        JsonNode queryImages(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a list of valid parameters for querying a series images, as raw JSON. Unlike other routes, querying
         * for a series images may be restricted to certain combinations of query keys. The allowed combinations are
         * clustered in the data array of the returned JSON object.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query_params">
         * <b>[GET]</b> /series/{id}/images/query/params</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return JSON object containing a list of possible parameters which may be used to query a series images
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see #queryImages(long, QueryParameters) queryImages(seriesId, queryParameters)
         * @see TheTVDBApi#getAvailableImageQueryParameters(long) TheTVDBApi.getAvailableImageQueryParameters(seriesId)
         * @see Extended#getAvailableImageQueryParameters(long) TheTVDBApi.Extended.getAvailableImageQueryParameters(seriesId)
         */
        JsonNode getAvailableImageQueryParameters(long seriesId) throws APIException;

        /**
         * Returns an array of series that have changed in a maximum of one week blocks since the provided <em>{@code
         * fromTime}</em> query parameter, as raw JSON. Note that the given query parameters must always contain some
         * valid <em>{@code fromTime}</em> Epoch timestamp key.
         * <p><br>
         * The user may specify an additional <em>{@code toTime}</em> query key to grab results for less than a week.
         * Any timespan larger than a week will be reduced down to one week automatically.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Updates/get_updated_query">
         * <b>[GET]</b> /updated/query</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
         *                        query parameters see the API documentation or use {@link #getAvailableLastUpdatedQueryParameters()}.
         *
         * @return JSON object containing a list of updated objects that match the given timeframe
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or no records exist for the given timespan.
         * @see TheTVDBApi#queryLastUpdated(QueryParameters) TheTVDBApi.queryLastUpdated(queryParameters)
         * @see Extended#queryLastUpdated(QueryParameters) TheTVDBApi.Extended.queryLastUpdated(queryParameters)
         */
        JsonNode queryLastUpdated(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a list of valid parameters for querying series which have been updated lately, as raw JSON. These
         * keys are permitted to be used in {@link QueryParameters} objects when querying for recently updated series.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Updates/get_updated_query_params">
         * <b>[GET]</b> /updated/query/params</a>
         *
         * @return JSON object containing a list of possible parameters which may be used to query for last updated
         *         series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see #queryLastUpdated(QueryParameters) queryLastUpdated(queryParameters)
         * @see TheTVDBApi#getAvailableLastUpdatedQueryParameters()
         * @see Extended#getAvailableLastUpdatedQueryParameters()
         */
        JsonNode getAvailableLastUpdatedQueryParameters() throws APIException;

        /**
         * Returns basic information about the currently authenticated user, as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user">
         * <b>[GET]</b> /user</a>
         *
         * @return JSON object containing basic user information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no information exists for the current user
         * @see TheTVDBApi#getUser()
         * @see Extended#getUser()
         */
        JsonNode getUser() throws APIException;

        /**
         * Returns an array of favorite series for a given user, as raw JSON. Will be a blank array if no favorites
         * exist.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_favorites">
         * <b>[GET]</b> /user/favorites</a>
         *
         * @return JSON object containing the user favorites
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no information exists for the current user
         * @see TheTVDBApi#getFavorites()
         * @see Extended#getFavorites()
         */
        JsonNode getFavorites() throws APIException;

        /**
         * Deletes the given series ID from the user’s favorite’s list and returns the updated list as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/delete_user_favorites_id">
         * <b>[DELETE]</b> /user/favorites/{id}</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return JSON object containing the updated list of user favorites
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no information exists for the current user or the
         *                      requested record could not be deleted
         * @see #addToFavorites(long) addToFavorites(seriesId)
         * @see TheTVDBApi#deleteFromFavorites(long) TheTVDBApi.deleteFromFavorites(seriesId)
         * @see Extended#deleteFromFavorites(long) TheTVDBApi.Extended.deleteFromFavorites(seriesId)
         */
        JsonNode deleteFromFavorites(long seriesId) throws APIException;

        /**
         * Adds the supplied series ID to the user’s favorite’s list and returns the updated list as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/put_user_favorites_id">
         * <b>[PUT]</b> /user/favorites/{id}</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return JSON object containing the updated list of user favorites
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no information exists for the current user or the
         *                      requested record could not be updated
         * @see #deleteFromFavorites(long) deleteFromFavorites(seriesId)
         * @see TheTVDBApi#addToFavorites(long) TheTVDBApi.addToFavorites(seriesId)
         * @see Extended#addToFavorites(long) TheTVDBApi.Extended.addToFavorites(seriesId)
         */
        JsonNode addToFavorites(long seriesId) throws APIException;

        /**
         * Returns a list of ratings for the given user, as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings">
         * <b>[GET]</b> /user/ratings</a>
         *
         * @return JSON object containing a list of user ratings
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no information exists for the current user
         * @see TheTVDBApi#getRatings()
         * @see Extended#getRatings()
         */
        JsonNode getRatings() throws APIException;

        /**
         * Returns a list of ratings for a given user that match the query, as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings_query">
         * <b>[GET]</b> /user/ratings/query</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
         *                        query parameters see the API documentation or use {@link #getAvailableRatingsQueryParameters()}.
         *
         * @return JSON object containing a list of user ratings that match the given query
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no information exists for the current user
         * @see TheTVDBApi#queryRatings(QueryParameters) TheTVDBApi.queryRatings(queryParameters)
         * @see Extended#queryRatings(QueryParameters) TheTVDBApi.Extended.queryRatings(queryParameters)
         */
        JsonNode queryRatings(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a list of valid parameters for querying user ratings, as raw JSON. These keys are permitted to be
         * used in {@link QueryParameters} objects when querying for ratings.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings_query_params">
         * <b>[GET]</b> /user/ratings/query/params</a>
         *
         * @return JSON object containing a list of possible parameters which may be used to query for user ratings
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no information exists for the current user
         * @see #queryRatings(QueryParameters) queryRatings(queryParameters)
         * @see TheTVDBApi#getAvailableRatingsQueryParameters()
         * @see Extended#getAvailableRatingsQueryParameters()
         */
        JsonNode getAvailableRatingsQueryParameters() throws APIException;

        /**
         * Deletes a given rating of a given type.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/delete_user_ratings_itemType_itemId">
         * <b>[DELETE]</b> /user/ratings/{itemType}/{itemId}</a>
         *
         * @param itemType Item to update. Can be either 'series', 'episode', or 'image'.
         * @param itemId   ID of the ratings record that you wish to delete
         *
         * @return JSON object as returned by the remote service (probably containing an empty data block)
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no rating is found that matches your given parameters
         * @see #addToRatings(String, long, long) addToRatings(itemType, itemId, itemRating)
         * @see TheTVDBApi#deleteFromRatings(String, long) TheTVDBApi.deleteFromRatings(itemType, itemId)
         * @see Extended#deleteFromRatings(String, long) TheTVDBApi.Extended.deleteFromRatings(itemType, itemId)
         */
        JsonNode deleteFromRatings(@Nonnull String itemType, long itemId) throws APIException;

        /**
         * Updates a given rating of a given type and returns the modified rating, mapped as raw JSON. If no rating
         * exists yet, a new rating will be created.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/put_user_ratings_itemType_itemId_itemRating">
         * <b>[PUT]</b> /user/ratings/{itemType}/{itemId}/{itemRating}</a>
         *
         * @param itemType   Item to update. Can be either 'series', 'episode', or 'image'.
         * @param itemId     ID of the ratings record that you wish to modify
         * @param itemRating The updated rating number
         *
         * @return JSON object containing the modified rating (whether it was added or updated)
         *         <br>
         *         <b>Note:</b> It seems that the data returned by the remote service for this route is quite
         *         unreliable! It might not always return the modified rating but an empty data array instead.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no rating is found that matches your given parameters
         * @see #deleteFromRatings(String, long) deleteFromRatings(itemType, itemId)
         * @see TheTVDBApi#addToRatings(String, long, long) TheTVDBApi.addToRatings(itemType, itemId, itemRating)
         * @see Extended#addToRatings(String, long, long) TheTVDBApi.Extended.addToRatings(itemType, itemId,
         *         itemRating)
         */
        JsonNode addToRatings(@Nonnull String itemType, long itemId, long itemRating) throws APIException;
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
         * Returns a response object containing the full information for a given episode id as mapped Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Episodes/get_episodes_id">
         * <b>[GET]</b> /episodes/{id}</a>
         *
         * @param episodeId The ID of the episode
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given episode ID does not exist, etc.
         * @see JSON#getEpisode(long) TheTVDBApi.JSON.getEpisode(episodeId)
         * @see TheTVDBApi#getEpisode(long) TheTVDBApi.getEpisode(episodeId)
         */
        APIResponse<Episode> getEpisode(long episodeId) throws APIException;

        /**
         * Returns a response object containing a list of all supported languages mapped as Java DTO. These language
         * abbreviations can be used to set the preferred language for the communication with the remote service (see
         * {@link #setLanguage(String)}.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages">
         * <b>[GET]</b> /languages</a>
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAvailableLanguages()
         * @see TheTVDBApi#getAvailableLanguages() TheTVDBApi.getAvailableLanguages()
         */
        APIResponse<List<Language>> getAvailableLanguages() throws APIException;

        /**
         * Returns a response object containing further language information for a given language ID mapped as Java DTO.
         * The language abbreviation can be used to set the preferred language for the communication with the remote
         * service (see {@link #setLanguage(String)}.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages_id">
         * <b>[GET]</b> /languages/{id}</a>
         *
         * @param languageId The ID of the language
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if the given language ID does not exist.
         * @see JSON#getLanguage(long) TheTVDBApi.JSON.getLanguage(languageId)
         * @see TheTVDBApi#getLanguage(long) TheTVDBApi.getLanguage(languageId)
         */
        APIResponse<Language> getLanguage(long languageId) throws APIException;

        /**
         * Returns a response object containing detailed information for a specific movie mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Movies/get_movies_id">
         * <b>[GET]</b> /movies/{id}</a>
         *
         * @param movieId The <i>TheTVDB.com</i> movie ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given movie ID does not exist, etc.
         * @see JSON#getMovie(long) TheTVDBApi.JSON.getMovie(movieId)
         * @see TheTVDBApi#getMovie(long) TheTVDBApi.getMovie(movieId)
         */
        APIResponse<Movie> getMovie(long movieId) throws APIException;

        /**
         * Returns a response object containing a list of ID's of all movies, that have been updated since the given
         * epoch timestamp, mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Movies/get_movieupdates">
         * <b>[GET]</b> /movieupdates</a>
         *
         * @param since Epoch time to start your date range
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getMovieUpdates(long) TheTVDBApi.JSON.getMovieUpdates(since)
         * @see TheTVDBApi#getMovieUpdates(long) TheTVDBApi.getMovieUpdates(since)
         */
        APIResponse<List<Long>> getMovieUpdates(long since) throws APIException;

        /**
         * Returns a response object containing a list of series search results based on the given query parameters
         * mapped as Java DTO. The list contains basic information of all series matching the query parameters.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series">
         * <b>[GET]</b> /search/series</a>
         *
         * @param queryParameters Object containing key/value pairs of query search parameters. For a complete list of
         *                        possible search parameters see the API documentation or use {@link
         *                        #getAvailableSeriesSearchParameters()}.
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no records are found that match your query.
         * @see JSON#searchSeries(QueryParameters) TheTVDBApi.JSON.searchSeries(queryParameters)
         * @see TheTVDBApi#searchSeries(QueryParameters) TheTVDBApi.searchSeries(queryParameters)
         */
        APIResponse<List<SeriesSearchResult>> searchSeries(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing possible query parameters, which can be used to search for series,
         * mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series_params">
         * <b>[GET]</b> /search/series/params</a>
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAvailableSeriesSearchParameters()
         * @see TheTVDBApi#getAvailableSeriesSearchParameters() TheTVDBApi.getAvailableSeriesSearchParameters()
         * @see JSON#searchSeries(QueryParameters) TheTVDBApi.JSON.searchSeries(queryParams)
         * @see TheTVDBApi#searchSeries(QueryParameters) TheTVDBApi.searchSeries(queryParams)
         */
        APIResponse<List<String>> getAvailableSeriesSearchParameters() throws APIException;

        /**
         * Returns a response object containing detailed information for a specific series mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id">
         * <b>[GET]</b> /series/{id}</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see JSON#getSeries(long) TheTVDBApi.JSON.getSeries(seriesId)
         * @see TheTVDBApi#getSeries(long) TheTVDBApi.getSeries(seriesId)
         */
        APIResponse<Series> getSeries(long seriesId) throws APIException;

        /**
         * Returns a response object containing header information for a specific series as key/value pairs. Good for
         * getting the Last-Updated header to find out when the series was last modified.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/head_series_id">
         * <b>[HEAD]</b> /series/{id}</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see JSON#getSeriesHeaderInformation(long) TheTVDBApi.JSON.getSeriesHeaderInformation(seriesId)
         * @see TheTVDBApi#getSeriesHeaderInformation(long) TheTVDBApi.getSeriesHeaderInformation(seriesId)
         */
        APIResponse<Map<String, String>> getSeriesHeaderInformation(long seriesId) throws APIException;

        /**
         * Returns a response object containing a list of actors for a specific series mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_actors">
         * <b>[GET]</b> /series/{id}/actors</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see JSON#getActors(long) TheTVDBApi.JSON.getActors(seriesId)
         * @see TheTVDBApi#getActors(long) TheTVDBApi.getActors(seriesId)
         */
        APIResponse<List<Actor>> getActors(long seriesId) throws APIException;

        /**
         * Returns a response object containing all episodes of a specific series mapped as Java DTO. Results will be
         * paginated with 100 results per page. Use <em>{@code queryParameters}</em> to select a specific result page.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes">
         * <b>[GET]</b> /series/{id}/episodes</a>
         *
         * @param seriesId        The <i>TheTVDB.com</i> series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
         *                        parameters see the API documentation.
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see JSON#getEpisodes(long, QueryParameters) TheTVDBApi.JSON.getEpisodes(seriesId, queryParameters)
         * @see TheTVDBApi#getEpisodes(long, QueryParameters) TheTVDBApi.getEpisodes(seriesId, queryParameters)
         */
        APIResponse<List<Episode>> getEpisodes(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing all matching episodes of a specific series mapped as Java DTO. Results
         * will be paginated. Note that this method is deterministic and will always return the <b>first</b> result page
         * of the available episodes.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query">
         * <b>[GET]</b> /series/{id}/episodes/query</a>
         *
         * @param seriesId        The <i>TheTVDB.com</i> series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
         *                        query parameters see the API documentation or use {@link #getAvailableEpisodeQueryParameters(long)
         *                        getAvailableEpisodeQueryParameters(seriesId)}.
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc. or if no records are found that match your query.
         * @see JSON#queryEpisodes(long, QueryParameters) TheTVDBApi.JSON.queryEpisodes(seriesId, queryParameters)
         * @see TheTVDBApi#queryEpisodes(long, QueryParameters) TheTVDBApi.queryEpisodes(seriesId, queryParameters)
         */
        APIResponse<List<Episode>> queryEpisodes(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a list of keys which are valid parameters for querying episodes, as
         * plain Strings. These keys are permitted to be used in {@link QueryParameters} objects when querying for
         * specific episodes of a series.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query_params">
         * <b>[GET]</b> /series/{id}/episodes/query/params</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see #queryEpisodes(long, QueryParameters) queryEpisodes(seriesId, queryParameters)
         * @see JSON#getAvailableEpisodeQueryParameters(long) TheTVDBApi.JSON.getAvailableEpisodeQueryParameters(seriesId)
         * @see TheTVDBApi#getAvailableEpisodeQueryParameters(long) TheTVDBApi.getAvailableEpisodeQueryParameters(seriesId)
         */
        APIResponse<List<String>> getAvailableEpisodeQueryParameters(long seriesId) throws APIException;

        /**
         * Returns a response object containing a summary of the episodes and seasons available for a series, mapped as
         * Java DTO.
         * <br>
         * <b>Note:</b> Season "0" is for all episodes that are considered to be specials.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_summary">
         * <b>[GET]</b> /series/{id}/episodes/summary</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see JSON#getSeriesEpisodesSummary(long) TheTVDBApi.JSON.getSeriesEpisodesSummary(seriesId)
         * @see TheTVDBApi#getSeriesEpisodesSummary(long) TheTVDBApi.getSeriesEpisodesSummary(seriesId)
         */
        APIResponse<SeriesSummary> getSeriesEpisodesSummary(long seriesId) throws APIException;

        /**
         * Returns a response object containing a filtered series record based on the given parameters, mapped as Java
         * DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_filter">
         * <b>[GET]</b> /series/{id}/filter</a>
         *
         * @param seriesId        The <i>TheTVDB.com</i> series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
         *                        query parameters see the API documentation or use {@link #getAvailableSeriesFilterParameters(long)
         *                        getAvailableSeriesFilterParameters(seriesId)}.
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see JSON#filterSeries(long, QueryParameters) TheTVDBApi.JSON.filterSeries(seriesId, queryParameters)
         * @see TheTVDBApi#filterSeries(long, QueryParameters) TheTVDBApi.filterSeries(seriesId, queryParameters)
         */
        APIResponse<Series> filterSeries(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a list of keys which are valid parameters for filtering series, as plain
         * Strings. These keys are permitted to be used in {@link QueryParameters} objects when filtering for a specific
         * series.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_filter_params">
         * <b>[GET]</b> /series/{id}/filter/params</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see #filterSeries(long, QueryParameters) filterSeries(seriesId, queryParameters)
         * @see JSON#getAvailableSeriesFilterParameters(long) TheTVDBApi.JSON.getAvailableSeriesFilterParameters(seriesId)
         * @see TheTVDBApi#getAvailableSeriesFilterParameters(long) TheTVDBApi.getAvailableSeriesFilterParameters(seriesId)
         */
        APIResponse<List<String>> getAvailableSeriesFilterParameters(long seriesId) throws APIException;

        /**
         * Returns a response object containing a summary of the images types and counts available for a particular
         * series, mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images">
         * <b>[GET]</b> /series/{id}/images</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see JSON#getSeriesImagesSummary(long) TheTVDBApi.JSON.getSeriesImagesSummary(seriesId)
         * @see TheTVDBApi#getSeriesImagesSummary(long) TheTVDBApi.getSeriesImagesSummary(seriesId)
         */
        APIResponse<ImageSummary> getSeriesImagesSummary(long seriesId) throws APIException;

        /**
         * Returns a response object containing the matching result of querying images for a specific series, mapped as
         * Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query">
         * <b>[GET]</b> /series/{id}/images/query</a>
         *
         * @param seriesId        The <i>TheTVDB.com</i> series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
         *                        query parameters see the API documentation or use {@link #getAvailableImageQueryParameters(long)
         *                        getAvailableImageQueryParameters(seriesId)}.
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc. or if no records are found that match your query.
         * @see JSON#queryImages(long, QueryParameters) TheTVDBApi.JSON.queryImages(seriesId, queryParameters)
         * @see TheTVDBApi#queryImages(long, QueryParameters) TheTVDBApi.queryImages(seriesId, queryParameters)
         */
        APIResponse<List<Image>> queryImages(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a list of valid parameters for querying a series images, mapped as Java
         * DTO. Unlike other routes, querying for a series images may be restricted to certain combinations of query
         * keys. The allowed combinations are clustered in the single {@link ImageQueryParameter} objects of the
         * returned API responses data object.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query_params">
         * <b>[GET]</b> /series/{id}/images/query/params</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the
         *                      given series ID does not exist, etc.
         * @see #queryImages(long, QueryParameters) queryImages(seriesId, queryParameters)
         * @see JSON#getAvailableImageQueryParameters(long) TheTVDBApi.JSON.getAvailableImageQueryParameters(seriesId)
         * @see TheTVDBApi#getAvailableImageQueryParameters(long) TheTVDBApi.getAvailableImageQueryParameters(seriesId)
         */
        APIResponse<List<ImageQueryParameter>> getAvailableImageQueryParameters(long seriesId) throws APIException;

        /**
         * Returns a response object containing a map of series that have changed in a maximum of one week blocks since
         * the provided <em>{@code fromTime}</em> query parameter, as plain Strings. The key/value pairs of the returned
         * data object's map represent a <i>TheTVDB.com</i> series ID (key) and when it was updated the last time
         * (value) as Epoch time. Note that the given query parameters must always contain a valid <em>{@code
         * fromTime}</em> Epoch timestamp key.
         * <p><br>
         * The user may specify an additional <em>{@code toTime}</em> query key to grab results for less than a week.
         * Any timespan larger than a week will be reduced down to one week automatically.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Updates/get_updated_query">
         * <b>[GET]</b> /updated/query</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
         *                        query parameters see the API documentation or use {@link #getAvailableLastUpdatedQueryParameters()}.
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or no records exist for the given timespan.
         * @see JSON#queryLastUpdated(QueryParameters) TheTVDBApi.JSON.queryLastUpdated(queryParameters)
         * @see TheTVDBApi#queryLastUpdated(QueryParameters) TheTVDBApi.queryLastUpdated(queryParameters)
         */
        APIResponse<Map<Long, Long>> queryLastUpdated(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a list of valid parameters for querying series which have been updated
         * lately, as plain Strings. These keys are permitted to be used in {@link QueryParameters} objects when
         * querying for recently updated series.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Updates/get_updated_query_params">
         * <b>[GET]</b> /updated/query/params</a>
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see #queryLastUpdated(QueryParameters) queryLastUpdated(queryParameters)
         * @see JSON#getAvailableLastUpdatedQueryParameters()
         * @see TheTVDBApi#getAvailableLastUpdatedQueryParameters()
         */
        APIResponse<List<String>> getAvailableLastUpdatedQueryParameters() throws APIException;

        /**
         * Returns a response object containing basic information about the currently authenticated user, mapped as Java
         * DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user">
         * <b>[GET]</b> /user</a>
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no information exists for the current user
         * @see JSON#getUser()
         * @see TheTVDBApi#getUser()
         */
        APIResponse<User> getUser() throws APIException;

        /**
         * Returns a response object containing a list of favorite series for a given user, as plain Strings. The data
         * object of the returned response will contain an empty list if no favorites exist.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_favorites">
         * <b>[GET]</b> /user/favorites</a>
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no information exists for the current user
         * @see JSON#getFavorites()
         * @see TheTVDBApi#getFavorites()
         */
        APIResponse<List<String>> getFavorites() throws APIException;

        /**
         * Deletes the given series ID from the user’s favorite’s list and returns a response object containing the
         * updated list as plain Strings.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/delete_user_favorites_id">
         * <b>[DELETE]</b> /user/favorites/{id}</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no information exists for the current user or the
         *                      requested record could not be deleted
         * @see #addToFavorites(long) addToFavorites(seriesId)
         * @see JSON#deleteFromFavorites(long) TheTVDBApi.JSON.deleteFromFavorites(seriesId)
         * @see TheTVDBApi#deleteFromFavorites(long) TheTVDBApi.deleteFromFavorites(seriesId)
         */
        APIResponse<List<String>> deleteFromFavorites(long seriesId) throws APIException;

        /**
         * Adds the supplied series ID to the user’s favorite’s list and returns a response object containing the
         * updated list as plain Strings.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/put_user_favorites_id">
         * <b>[PUT]</b> /user/favorites/{id}</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no information exists for the current user or the
         *                      requested record could not be updated
         * @see #deleteFromFavorites(long) deleteFromFavorites(seriesId)
         * @see JSON#addToFavorites(long) TheTVDBApi.JSON.addToFavorites(seriesId)
         * @see TheTVDBApi#addToFavorites(long) TheTVDBApi.addToFavorites(seriesId)
         */
        APIResponse<List<String>> addToFavorites(long seriesId) throws APIException;

        /**
         * Returns a response object containing a list of ratings for the given user, mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings">
         * <b>[GET]</b> /user/ratings</a>
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no information exists for the current user
         * @see JSON#getRatings()
         * @see TheTVDBApi#getRatings()
         */
        APIResponse<List<Rating>> getRatings() throws APIException;

        /**
         * Returns a response object containing a list of ratings for a given user that match the query, mapped as Java
         * DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings_query">
         * <b>[GET]</b> /user/ratings/query</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible
         *                        query parameters see the API documentation or use {@link #getAvailableRatingsQueryParameters()}.
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no information exists for the current user
         * @see JSON#queryRatings(QueryParameters) TheTVDBApi.JSON.queryRatings(queryParameters)
         * @see TheTVDBApi#queryRatings(QueryParameters) TheTVDBApi.queryRatings(queryParameters)
         */
        APIResponse<List<Rating>> queryRatings(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a list of valid parameters for querying user ratings, as plain Strings.
         * These keys are permitted to be used in {@link QueryParameters} objects when querying for ratings.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings_query_params">
         * <b>[GET]</b> /user/ratings/query/params</a>
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no information exists for the current user
         * @see #queryRatings(QueryParameters) queryRatings(queryParameters)
         * @see JSON#getAvailableRatingsQueryParameters()
         * @see TheTVDBApi#getAvailableRatingsQueryParameters()
         */
        APIResponse<List<String>> getAvailableRatingsQueryParameters() throws APIException;

        /**
         * Deletes a given rating of a given type.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/delete_user_ratings_itemType_itemId">
         * <b>[DELETE]</b> /user/ratings/{itemType}/{itemId}</a>
         *
         * @param itemType Item to update. Can be either 'series', 'episode', or 'image'.
         * @param itemId   ID of the ratings record that you wish to delete
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no rating is found that matches your given parameters
         * @see #addToRatings(String, long, long) addToRatings(itemType, itemId, itemRating)
         * @see JSON#deleteFromRatings(String, long) TheTVDBApi.JSON.deleteFromRatings(itemType, itemId)
         * @see TheTVDBApi#deleteFromRatings(String, long) TheTVDBApi.deleteFromRatings(itemType, itemId)
         */
        void deleteFromRatings(@Nonnull String itemType, long itemId) throws APIException;

        /**
         * Updates a given rating of a given type and return a response object containing the modified rating, mapped as
         * Java DTO. If no rating exists yet, a new rating will be created.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/put_user_ratings_itemType_itemId_itemRating">
         * <b>[PUT]</b> /user/ratings/{itemType}/{itemId}/{itemRating}</a>
         *
         * @param itemType   Item to update. Can be either 'series', 'episode', or 'image'.
         * @param itemId     ID of the ratings record that you wish to modify
         * @param itemRating The updated rating number
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error
         *         and paging information. Please note that not all API routes provide additional information so this
         *         type of data might be empty.
         *         <br>
         *         <b>Note:</b> It seems that the data returned by the remote service for this route is quite
         *         unreliable! It might not always return the modified rating but an empty data array instead.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no rating is found that matches your given parameters
         * @see #deleteFromRatings(String, long) deleteFromRatings(itemType, itemId)
         * @see JSON#addToRatings(String, long, long) TheTVDBApi.JSON.addToRatings(itemType, itemId, itemRating)
         * @see TheTVDBApi#addToRatings(String, long, long) TheTVDBApi.addToRatings(itemType, itemId, itemRating)
         */
        APIResponse<List<Rating>> addToRatings(@Nonnull String itemType, long itemId, long itemRating)
                throws APIException;
    }

    /**
     * Specifies the version of the <i>TheTVDB.com</i> remote API to be used by this connector
     */
    final class Version {
        /** Version of the <i>TheTVDB.com</i> remote API used by this connector */
        public static final String API_VERSION = "v3.0.0";

        /** Constant class. Should not be instantiated */
        private Version() {}
    }
}

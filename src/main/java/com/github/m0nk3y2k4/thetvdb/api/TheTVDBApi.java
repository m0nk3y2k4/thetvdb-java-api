package com.github.m0nk3y2k4.thetvdb.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.api.model.data.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Main interface of the <i>TheTDVB</i> API connector.
 * <p/>
 * This interface provides access to all available routes of the remote <i>TheTVDB</i> REST API. Routes which accept additional optional and mandatory
 * query parameters can either be invoked with a given set of {@link QueryParameters} or via some predefined shortcut-methods. These shortcut-methods
 * will accept certain values as direct method parameters which will then be forwarded to the REST API as regular URL query parameters. Please note
 * that shortcut-methods exist for most of the common query scenarios but maybe not for all. In case of more complex query setups the user has to
 * take care of creating a properly configured <code>QueryParameters</code> object, which is slightly more effort than using the shortcut-methods
 * but gives the user unlimited configuration options.
 * <p/>
 * In order to create a new API instance the {@link com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory TheTVDBApiFactory} should be used. This factory
 * also provides additional helper methods, for example to easily create new <code>QueryParameters</code>.
 * <p/>
 * To cover a wide range of possible applications, this API connector provides multiple layouts in order to allow an easy integration regardless
 * of your actual project requirements. It gives you the option to use prefabbed DTO's which will be parsed from the actual JSON returned by the
 * remote service. In case you need advanced exception handling or you prefer to parse the JSON into your own data models (or don't want to parse
 * it at all), other API layouts will provide you with extended API response DTO's or even with the raw JSON. The following API layouts are currently
 * available:
 * <ul>
 * <li>{@link TheTVDBApi}</li>
 * This is probably the most common layout. It provides various shortcut-methods and automatically maps the received JSON <b><i>data</i></b> content
 * into simple Java DTO's (at least for more complex response data). The user does not have to worry about JSON parsing but can simply work with the
 * returned DTO's like he works with every other Java object. However, these objects do only contain the actually requested data and will not include
 * any additional contextual informations that may be returned by the remote service (e.g. Pagination information, additional validation or error data).
 * Furthermore they will only provide access to properties that are <a href="https://api.thetvdb.com/swagger">formally declared by the API</a>
 * (version v3.0.0).
 * <li>{@link Extended}</li>
 * This layout may be used for slightly advance API integration. Like the common layout it'll take care of parsing the recieved JSON into Java DTO's
 * but it will also provide access to any additional contextual information. Methods of this layout will always return a single {@link APIResponse}
 * object which consists of the actual data, parsed as DTO, as well as all additional information which is available in the given context, like
 * additional error or pagination information. This layout does not provide any shortcut-methods.
 * <li>{@link JSON}</li>
 * This layout may be used if you do not want any post-processing being applied to the actual remote service response data. All methods within this
 * layout will return the raw, unmodified JSON data as it was received from the API. This might be useful if you prefer to map the JSON data yourself,
 * want to use your own Java data models or if you don't want to parse the JSON data at all (but forward it to some other service for example). It would
 * also be the preferred layout in case you need access to additional (e.g. experimental) properties that are not yet officially declared by the formal
 * API description. This layout does not provide any shortcut-methods though.
 * </ul>
 * <p/>
 * Once an API instance has been created, the additional layouts can be accessed via the {@link #extended()} or {@link #json()} method.
 */
public interface TheTVDBApi {

    /**
     * Initializes the current API session by requesting a new token from the remote API. This token will be used for authentication of all requests that are sent to the
     * remote service by this API instance. The initialization will be performed based on the constructor parameters used to create this API instance. Actually this method
     * will do the same as {@link #login()}.
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    void init() throws APIException;

    /**
     * Initializes the current API with the given token. This token will be used for authentication of all requests that are sent to the remote service by this API instance.
     * The given string must be a valid Base64 encoded token in the regular JWT format <i>"{header}.{payload}.{signature}"</i>.
     * <p/>
     * If the given token is (or becomes) expired it will be replaced by a new JWT automatically. The new token will be requested from the remove service based
     * on the constructor parameters used to create this API instance.
     *
     * @param token JSON Web Token to be used for remote API communication/authorization
     *
     * @throws APIException If the given string does not match the JSON Web Token format
     */
    void init(@Nonnull String token) throws APIException;

    /**
     * Returns the JSON Web Token used for authentication of all requests that are sent to the remote service by this API instance. If the current API has not yet been
     * initialized an empty <i>Optional</i> instance will be returned.
     *
     * @return The JWT used by this API or an empty <i>Optional</i> if the API has not been initialized
     */
    Optional<String> getToken();

    /**
     * Sets the preferred language to be used for communication with the remote service. Some of the API calls might use this setting in order to only return results that
     * match the given language. If available, the data returned by the remote API will be translated to the given language. The default language code is <b>"en"</b>. For a list
     * of supported languages see {@link #getAvailableLanguages()}.
     *
     * @see #getAvailableLanguages()
     *
     * @param languageCode The language in which the results are to be returned
     */
    void setLanguage(String languageCode);

    /**
     * Initializes the current API session by requesting a new token from the remote API. This token will be used for authentication of all requests that are sent to the
     * remote service by this API instance. The initialization will be performed based on the constructor parameters used to create this API instance. It is recommended to
     * login/initialize the session before making the first API call. However, if an API call is made without proper initialization, an implicit login will be performed.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Authentication/post_login"><b>[POST]</b> /login</a>
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    void login() throws APIException;

    /**
     * Refreshes the current, valid JWT session token. This method can be used to extend the expiration date (24 hours) of the current session token without the need of a
     * complete new login. This method will be called automatically if an API call is made using an expired JWT session token.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Authentication/get_refresh_token"><b>[GET]</b> /refresh_token</a>
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    void refreshToken() throws APIException;

    /**
     * Returns the full information for a given episode id as mapped Java DTO.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Episodes/get_episodes_id"><b>[GET]</b> /episodes/{id}</a>
     *
     * @see JSON#getEpisode(long) TheTVDBApi.JSON.getEpisode(episodeId)
     * @see Extended#getEpisode(long) TheTVDBApi.Extended.getEpisode(episodeId)
     *
     * @param episodeId The ID of the episode
     *
     * @return Mapped Java DTO containing the full episode information based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    Episode getEpisode(long episodeId) throws APIException;

    /**
     * Returns a list of all supported languages mapped as Java DTO. These language abbreviations can be used to set the preferred language
     * for the communication with the remote service (see {@link #setLanguage(String)}.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages"><b>[GET]</b> /languages</a>
     *
     * @see JSON#getAvailableLanguages()
     * @see Extended#getAvailableLanguages()
     *
     * @return List of available languages mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Language> getAvailableLanguages() throws APIException;

    /**
     * Returns further language information for a given language ID mapped as Java DTO. The language abbreviation can be used to set the preferred language
     * for the communication with the remote service (see {@link #setLanguage(String)}.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages_id"><b>[GET]</b> /languages/{id}</a>
     *
     * @see JSON#getLanguage(long) TheTVDBApi.JSON.getLanguage(languageId)
     * @see Extended#getLanguage(long) TheTVDBApi.Extended.getLanguage(languageId)
     *
     * @param languageId The ID of the language
     *
     * @return Mapped Java DTO containing detailed language information based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
     *                      if the given language ID does not exist.
     */
    Language getLanguage(long languageId) throws APIException;

    /**
     * Returns a list of series search results based on the given query parameters mapped as Java DTO. The list contains basic information
     * of all series matching the query parameters.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series"><b>[GET]</b> /search/series</a>
     *
     * @see JSON#searchSeries(QueryParameters) TheTVDBApi.JSON.searchSeries(queryParameters)
     * @see Extended#searchSeries(QueryParameters) TheTVDBApi.Extended.searchSeries(queryParameters)
     *
     * @param queryParameters Object containing key/value pairs of query search parameters. For a complete list of possible search parameters
     *                        see the API documentation or use {@link #getAvailableSeriesSearchParameters()}.
     *
     * @return List of series search results mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
     *                      if no records are found that match your query.
     */
    List<SeriesSearchResult> searchSeries(QueryParameters queryParameters) throws APIException;

    /**
     * Search for series by name. Returns a list of series search results mapped as Java DTO. The search results contain basic information
     * of all series matching the given name. This is a shortcut-method for {@link #searchSeries(QueryParameters) searchSeries(queryParameters)}
     * with a single "name" query parameter.
     *
     * @param name The name of the series to search for
     *
     * @return List of series search results mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
     *                      if no records are found that match your query.
     */
    List<SeriesSearchResult> searchSeriesByName(@Nonnull String name) throws APIException;

    /**
     * Search for series by IMDB-Id. Returns a list of series search results mapped as Java DTO. The search results contain basic information
     * of all series matching the given IMDB-Id. This is a shortcut-method for {@link #searchSeries(QueryParameters) searchSeries(queryParameters)}
     * with a single "imdbId" query parameter.
     *
     * @param imdbId The IMDB-Id of the series to search for
     *
     * @return List of series search results mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
     *                      if no records are found that match your query.
     */
    List<SeriesSearchResult> searchSeriesByImdbId(@Nonnull String imdbId) throws APIException;

    /**
     * Search for series by Zap2it-Id. Returns a list of series search results mapped as Java DTO. The search results contain basic information
     * of all series matching the given Zap2it-Id. This is a shortcut-method for {@link #searchSeries(QueryParameters) searchSeries(queryParameters)}
     * with a single "zap2itId" query parameter.
     *
     * @param zap2itId The Zap2it-Id of the series to search for
     *
     * @return List of series search results mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
     *                      if no records are found that match your query.
     */
    List<SeriesSearchResult> searchSeriesByZap2itId(@Nonnull String zap2itId) throws APIException;

    /**
     * Returns possible query parameters, which can be used to search for series, mapped as Java DTO.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series_params"><b>[GET]</b> /search/series/params</a>
     *
     * @see JSON#getAvailableSeriesSearchParameters()
     * @see Extended#getAvailableSeriesSearchParameters()
     * @see JSON#searchSeries(QueryParameters) TheTVDBApi.JSON.searchSeries(queryParams)
     * @see Extended#searchSeries(QueryParameters) TheTVDBApi.Extended.searchSeries(queryParams)
     * @see #searchSeries(QueryParameters) searchSeries(queryParams)
     *
     * @return List of possible parameters to query by in the series search
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<String> getAvailableSeriesSearchParameters() throws APIException;

    /**
     * Returns detailed information for a specific series mapped as Java DTO.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id"><b>[GET]</b> /series/{id}</a>
     *
     * @see JSON#getSeries(long) TheTVDBApi.JSON.getSeries(seriesId)
     * @see Extended#getSeries(long) TheTVDBApi.Extended.getSeries(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return Detailed information for a specific series mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    Series getSeries(long seriesId) throws APIException;

    /**
     * Returns header information for a specific series as key/value pairs. Good for getting the Last-Updated header to find out
     * when the series was last modified.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/head_series_id"><b>[HEAD]</b> /series/{id}</a>
     *
     * @see JSON#getSeriesHeaderInformation(long) TheTVDBApi.JSON.getSeriesHeaderInformation(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return HTML header information returned by the remote service mapped as key/value pairs
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    Map<String, String> getSeriesHeaderInformation(long seriesId) throws APIException;

    /**
     * Returns a list of actors for a specific series mapped as Java DTO.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_actors"><b>[GET]</b> /series/{id}/actors</a>
     *
     * @see JSON#getActors(long) TheTVDBApi.JSON.getActors(seriesId)
     * @see Extended#getActors(long) TheTVDBApi.Extended.getActors(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return List of actors mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Actor> getActors(long seriesId) throws APIException;

    /**
     * Returns all episodes of a specific series mapped as Java DTO. Results will be paginated with 100 results per page.
     * Use <code>queryParameters</code> to select a specific result page.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes"><b>[GET]</b> /series/{id}/episodes</a>
     *
     * @see JSON#getEpisodes(long, QueryParameters) TheTVDBApi.JSON.getEpisodes(seriesId, queryParameters)
     * @see Extended#getEpisodes(long, QueryParameters) TheTVDBApi.Extended.getEpisodes(seriesId, queryParameters)
     *
     * @param seriesId The TheTVDB series ID
     * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible parameters
     *                        see the API documentation.
     *
     * @return List of episodes mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Episode> getEpisodes(long seriesId, QueryParameters queryParameters) throws APIException;

    /**
     * Returns the first 100 episodes of a specific series mapped as Java DTO. Note that this method is deterministic and
     * will always return the <b>first</b> result page of the available episodes. This is a shortcut-method for
     * {@link #getEpisodes(long, QueryParameters) getEpisodes(seriesId, queryParameters)} with an empty query parameter.
     *
     * @see #getEpisodes(long, long) getEpisodes(seriesId, page)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return List of episodes mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Episode> getEpisodes(long seriesId) throws APIException;

    /**
     * Returns a list of episodes of a specific series mapped as Java DTO. The result list will contain 100 episodes at most. For
     * series with more episodes use the <code>page</code> parameter to browse to a specific result page. This is a shortcut-method for
     * {@link #getEpisodes(long, QueryParameters) getEpisodes(seriesId, queryParameters)} with a single "page" query parameter.
     *
     * @see #getEpisodes(long) getEpisodes(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     * @param page The result page to be returned
     *
     * @return List of episodes mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Episode> getEpisodes(long seriesId, long page) throws APIException;

    /**
     * Returns all matching episodes of a specific series mapped as Java DTO. Results will be paginated. Note that this method
     * is deterministic and will always return the <b>first</b> result page of the available episodes.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query"><b>[GET]</b> /series/{id}/episodes/query</a>
     *
     * @see JSON#queryEpisodes(long, QueryParameters) TheTVDBApi.JSON.queryEpisodes(seriesId, queryParameters)
     * @see Extended#queryEpisodes(long, QueryParameters) TheTVDBApi.Extended.queryEpisodes(seriesId, queryParameters)
     *
     * @param seriesId The TheTVDB series ID
     * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible query parameters
     *                        see the API documentation or use {@link #getAvailableEpisodeQueryParameters(long) getAvailableEpisodeQueryParameters(seriesId)}.
     *
     * @return List of episodes matching the query parameters, mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Episode> queryEpisodes(long seriesId, QueryParameters queryParameters) throws APIException;

    /**
     * Returns all episodes of a specific series and season mapped as Java DTO. Results will be paginated. Note that this method
     * is deterministic and will always return the <b>first</b> result page of the available episodes. This is a shortcut-method for
     * {@link #queryEpisodes(long, QueryParameters) queryEpisodes(seriesId, queryParameters)} with a single "airedSeason" query parameter.
     *
     * @see #queryEpisodesByAiredSeason(long, long, long) queryEpisodesByAiredSeason(seriesId, airedSeason, page)
     *
     * @param seriesId The TheTVDB series ID
     * @param airedSeason The number of the aired season to query for
     *
     * @return List of episodes for a specific season, mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Episode> queryEpisodesByAiredSeason(long seriesId, long airedSeason) throws APIException;

    /**
     * Returns all episodes of a specific series and season mapped as Java DTO. Results will be paginated. For seasons with
     * a high number of episodes use the <code>page</code> parameter to browse to a specific result page. This is a shortcut-method for
     * {@link #queryEpisodes(long, QueryParameters) queryEpisodes(seriesId, queryParameters)} with a "airedSeason" and "page" query parameter.
     *
     * @see #queryEpisodesByAiredSeason(long, long) queryEpisodesByAiredSeason(seriesId, airedSeason)
     *
     * @param seriesId The TheTVDB series ID
     * @param airedSeason The number of the aired season to query for
     * @param page The result page to be returned
     *
     * @return List of episodes for a specific season, mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Episode> queryEpisodesByAiredSeason(long seriesId, long airedSeason, long page) throws APIException;

    /**
     * Returns all episodes of a specific series, matching the <code>airedEpisode</code> parameter, mapped as Java DTO. Results will be paginated.
     * This is a shortcut-method for {@link #queryEpisodes(long, QueryParameters) queryEpisodes(seriesId, queryParameters)} with a single "airedEpisode"
     * query parameter.
     * <p/>
     * Note that an aired episode number might be associated with a specific season. If the series consists of more than one season this method will return the matching aired episodes
     * from all the seasons. Use {@link #queryEpisodesByAbsoluteNumber(long, long)} in order to query for a single episode.
     *
     * @see #queryEpisodesByAbsoluteNumber(long, long) queryEpisodesByAbsoluteNumber(seriesId, absoluteNumber)
     *
     * @param seriesId The TheTVDB series ID
     * @param airedEpisode The number of the aired episode to query for
     *
     * @return List of episodes for a specific season and aired episode number, mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Episode> queryEpisodesByAiredEpisode(long seriesId, long airedEpisode) throws APIException;

    /**
     * Returns a specific episode of a series, mapped as Java DTO. Results will be paginated.
     * This is a shortcut-method for {@link #queryEpisodes(long, QueryParameters) queryEpisodes(seriesId, queryParameters)} with a single "absoluteNumber"
     * query parameter.
     * <p/>
     * Note that (unlike an aired episode number) an absolute episode number should most likely be unique throughout all episodes of a specific series. So in most cases the returned
     * list will consist of only one element. However, as the remote API doesn't give any guarantees that querying with an "absoluteNumber" parameter always returns one episode record
     * <b>at most</b> this method will return all episode data as received from the remote service.
     *
     * @see #getEpisode(long) getEpisode(episodeId)
     *
     * @param seriesId The TheTVDB series ID
     * @param absoluteNumber The absolute number of the episode to query for (this is not the episode ID!)
     *
     * @return List of episodes for an absolute episode number, mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Episode> queryEpisodesByAbsoluteNumber(long seriesId, long absoluteNumber) throws APIException;

    /**
     * Returns a list of keys which are valid parameters for querying episodes, as plain Strings. These keys are permitted to be used in
     * {@link QueryParameters} objects when querying for specific episodes of a series.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query_params"><b>[GET]</b> /series/{id}/episodes/query/params</a>
     *
     * @see #queryEpisodes(long, QueryParameters) queryEpisodes(seriesId, queryParameters)
     * @see JSON#getAvailableEpisodeQueryParameters(long) TheTVDBApi.JSON.getAvailableEpisodeQueryParameters(seriesId)
     * @see Extended#getAvailableEpisodeQueryParameters(long) TheTVDBApi.Extended.getAvailableEpisodeQueryParameters(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return List of allowed keys to be used for querying episodes, based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<String> getAvailableEpisodeQueryParameters(long seriesId) throws APIException;

    /**
     * Returns a summary of the episodes and seasons available for a series, mapped as Java DTO.
     * <br/>
     * <b>Note:</b> Season "0" is for all episodes that are considered to be specials.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_summary"><b>[GET]</b> /series/{id}/episodes/summary</a>
     *
     * @see JSON#getSeriesEpisodesSummary(long) TheTVDBApi.JSON.getSeriesEpisodesSummary(seriesId)
     * @see Extended#getSeriesEpisodesSummary(long) TheTVDBApi.Extended.getSeriesEpisodesSummary(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return A summary of the episodes and seasons avaialable for the given series, mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    SeriesSummary getSeriesEpisodesSummary(long seriesId) throws APIException;

    /**
     * Returns a filtered series record based on the given parameters, mapped as Java DTO.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_filter"><b>[GET]</b> /series/{id}/filter</a>
     *
     * @see JSON#filterSeries(long, QueryParameters) TheTVDBApi.JSON.filterSeries(seriesId, queryParameters)
     * @see Extended#filterSeries(long, QueryParameters) TheTVDBApi.Extended.filterSeries(seriesId, queryParameters)
     *
     * @param seriesId The TheTVDB series ID
     * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible query parameters
     *                        see the API documentation or use {@link #getAvailableSeriesFilterParameters(long)} getAvailableSeriesFilterParameters(seriesId)}.
     *
     * @return A filtered series record, mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    Series filterSeries(long seriesId, QueryParameters queryParameters) throws APIException;

    /**
     * Returns a series records, filtered by the supplied comma-separated list of keys, mapped as Java DTO. This is a shortcut-method for
     * {@link #filterSeries(long, QueryParameters) filterSeries(seriesId, queryParameters)} with a single "keys" query parameter.
     *
     * @param seriesId The TheTVDB series ID
     * @param filterKeys Comma-separated list of keys to filter by
     *
     * @return A filtered series record, mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    Series filterSeries(long seriesId, @Nonnull String filterKeys) throws APIException;

    /**
     * Returns a list of keys which are valid parameters for filtering series, as plain Strings. These keys are permitted to be used in
     * {@link QueryParameters} objects when filtering for a specific series.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_filter_params"><b>[GET]</b> /series/{id}/filter/params</a>
     *
     * @see #filterSeries(long, QueryParameters) filterSeries(seriesId, queryParameters)
     * @see JSON#getAvailableSeriesFilterParameters(long) TheTVDBApi.JSON.getAvailableSeriesFilterParameters(seriesId)
     * @see Extended#getAvailableSeriesFilterParameters(long) TheTVDBApi.Extended.getAvailableSeriesFilterParameters(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return A list of keys to filter by, based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<String> getAvailableSeriesFilterParameters(long seriesId) throws APIException;

    /**
     * Returns a summary of the images types and counts available for a particular series, mapped as Java DTO.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images"><b>[GET]</b> /series/{id}/images</a>
     *
     * @see JSON#getSeriesImagesSummary(long) TheTVDBApi.JSON.getSeriesImagesSummary(seriesId)
     * @see Extended#getSeriesImagesSummary(long) TheTVDBApi.Extended.getSeriesImagesSummary(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return A summary of the image types and counts available for the given series, mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    ImageSummary getSeriesImagesSummary(long seriesId) throws APIException;

    /**
     * Returns the matching result of querying images for a specific series, mapped as Java DTO.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query"><b>[GET]</b> /series/{id}/images/query</a>
     *
     * @see JSON#queryImages(long, QueryParameters) TheTVDBApi.JSON.queryImages(seriesId, queryParameters)
     * @see Extended#queryImages(long, QueryParameters) TheTVDBApi.Extended.queryImages(seriesId, queryParameters)
     *
     * @param seriesId The TheTVDB series ID
     * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible query parameters
     *                        see the API documentation or use {@link #getAvailableImageQueryParameters(long)} getAvailableImageQueryParameters(seriesId)}.
     *
     * @return List of images that matched the query, mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Image> queryImages(long seriesId, QueryParameters queryParameters) throws APIException;

    /**
     * Returns all images for a specific series, matching the given parameters, mapped as Java DTO. This is a shortcut-method for
     * {@link #queryImages(long, QueryParameters) queryImages(seriesId, queryParameters)} with a "keyType" and "resolution" query parameter.
     * <p/>
     * Note: For more details regarding valid values for the method specific query parameters see the API documentation or use
     * {@link #getAvailableImageQueryParameters(long)} getAvailableImageQueryParameters(seriesId)}
     *
     * @see #queryImages(long, String, String, String) queryImages(seriesId, keyType, resolution, subKey)
     *
     * @param seriesId The TheTVDB series ID
     * @param keyType Type of image you're querying for (fanart, poster, etc.)
     * @param resolution Resolution to filter by (1280x1024, for example)
     *
     * @return List of images that matched the given parameters, mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Image> queryImages(long seriesId, @Nonnull String keyType, @Nonnull String resolution) throws APIException;

    /**
     * Returns all images for a specific series, matching the given parameters, mapped as Java DTO. This is a shortcut-method for
     * {@link #queryImages(long, QueryParameters) queryImages(seriesId, queryParameters)} with a "keyType", a "resolution" and a "subKey" query parameter.
     * <p/>
     * Note: For more details regarding valid values for the method specific query parameters see the API documentation or use
     * {@link #getAvailableImageQueryParameters(long)} getAvailableImageQueryParameters(seriesId)}
     *
     * @see #queryImages(long, String, String) queryImages(seriesId, keyType, resolution)
     *
     * @param seriesId The TheTVDB series ID
     * @param keyType Type of image you're querying for (fanart, poster, etc.)
     * @param resolution Resolution to filter by (1280x1024, for example)
     * @param subKey Subkey for the other method query parameters
     *
     * @return List of images that matched the given parameters, mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Image> queryImages(long seriesId, @Nonnull String keyType, @Nonnull String resolution, @Nonnull String subKey) throws APIException;

    /**
     * Returns all images of a specific type for a series, mapped as Java DTO. This is a shortcut-method for
     * {@link #queryImages(long, QueryParameters) queryImages(seriesId, queryParameters)} with a single "keyType" query parameter.
     * <p/>
     * Note: For more details regarding valid values for the method specific query parameters see the API documentation or use
     * {@link #getAvailableImageQueryParameters(long)} getAvailableImageQueryParameters(seriesId)}
     *
     * @param seriesId The TheTVDB series ID
     * @param keyType Type of image you're querying for (fanart, poster, etc.)
     *
     * @return List of images of the given key type, mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Image> queryImagesByKeyType(long seriesId, @Nonnull String keyType) throws APIException;

    /**
     * Returns all images of a specific resolution for a series, mapped as Java DTO. This is a shortcut-method for
     * {@link #queryImages(long, QueryParameters) queryImages(seriesId, queryParameters)} with a single "resolution" query parameter.
     * <p/>
     * Note: For more details regarding valid values for the method specific query parameters see the API documentation or use
     * {@link #getAvailableImageQueryParameters(long)} getAvailableImageQueryParameters(seriesId)}
     *
     * @param seriesId The TheTVDB series ID
     * @param resolution Resolution to filter by (1280x1024, for example)
     *
     * @return List of images with the given resolution, mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Image> queryImagesByResolution(long seriesId, @Nonnull String resolution) throws APIException;

    /**
     * Returns all images of a specific sub key for a series, mapped as Java DTO. This is a shortcut-method for
     * {@link #queryImages(long, QueryParameters) queryImages(seriesId, queryParameters)} with a single "subKey" query parameter.
     * <p/>
     * Note: For more details regarding valid values for the method specific query parameters see the API documentation or use
     * {@link #getAvailableImageQueryParameters(long)} getAvailableImageQueryParameters(seriesId)}
     *
     * @param seriesId The TheTVDB series ID
     * @param subKey Subkey to query for
     *
     * @return List of images matching the given sub key, mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Image> queryImagesBySubKey(long seriesId, @Nonnull String subKey) throws APIException;

    /**
     * Returns a list of valid parameters for querying a series images, mapped as Java DTO. Unlike other routes, querying for a series images may be resticted
     * to certain combinations of query keys. The allowed combinations are clustered in the single {@link ImageQueryParameter} objects returned by this method.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query_params"><b>[GET]</b> /series/{id}/images/query/params</a>
     *
     * @see #queryImages(long, QueryParameters) queryImages(seriesId, queryParameters)
     * @see JSON#getAvailableImageQueryParameters(long) TheTVDBApi.JSON.getAvailableImageQueryParameters(seriesId)
     * @see Extended#getAvailableImageQueryParameters(long) TheTVDBApi.Extended.getAvailableImageQueryParameters(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return A list of possible parameters which may be used to query a series images, mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<ImageQueryParameter> getAvailableImageQueryParameters(long seriesId) throws APIException;

    /**
     * Returns a map of series that have changed in a maximum of one week blocks since the provided <code>fromTime</code> query parameter. The key/value pairs of the
     * returned map represent a TheTVDB series ID (key) and when it was updated the last time (value) as Epoch time. Note that the given query parameters must
     * always contain a valid <code>fromTime</code> Epoch timestamp key.
     * <p/>
     * The user may specify an additional <code>toTime</code> query key to grab results for less than a week. Any timespan larger than a week will be reduced
     * down to one week automatically.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Updates/get_updated_query"><b>[GET]</b> /updated/query</a>
     *
     * @see JSON#queryLastUpdated(QueryParameters) TheTVDBApi.JSON.queryLastUpdated(queryParameters)
     * @see Extended#queryLastUpdated(QueryParameters) TheTVDBApi.Extended.queryLastUpdated(queryParameters)
     *
     * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible query parameters
     *                        see the API documentation or use {@link #getAvailableLastUpdatedQueryParameters()}.
     *
     * @return A map of updated objects that match the given timeframe, based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    Map<Long, Long> queryLastUpdated(QueryParameters queryParameters) throws APIException;

    /**
     * Returns a map of series that have changed in the (one) week since the provided <code>fromTime</code> query parameter. The key/value pairs
     * of the returned map represent a TheTVDB series ID (key) and when it was updated the last time (value) as Epoch time. This is a shortcut-method for
     * {@link #queryLastUpdated(QueryParameters) queryLastUpdated(queryParameters)} with a single "fromTime" query parameter.
     *
     * @see #queryLastUpdated(String, String) queryLastUpdated(fromTime, toTime)
     *
     * @param fromTime Epoch time to start your date range
     *
     * @return A map of updated objects beginning at the given <code>fromTime</code>, based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    Map<Long, Long> queryLastUpdated(@Nonnull String fromTime) throws APIException;

    /**
     * Returns a map of series that have changed inbetween the given timeframe, but with a maximum of one week, starting at the provided <code>fromTime</code>
     * query parameter. The <code>toTime</code> parameter may be specified to grab results for less than a week. Any timespan larger than a week will be
     * reduced down to one week automatically. The key/value pairs of the returned map represent a TheTVDB series ID (key) and when it was updated the last
     * time (value) as Epoch time. This is a shortcut-method for {@link #queryLastUpdated(QueryParameters) queryLastUpdated(queryParameters)} with a "fromTime"
     * and a "toTime" query parameter.
     *
     * @see #queryLastUpdated(String) queryLastUpdated(fromTime)
     *
     * @param fromTime Epoch time to start your date range
     * @param toTime Epoch time to end your date range. Must not be greater than one week from <code>fromTime</code>.
     *
     * @return A map of updated objects matching the given timeframe, based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    Map<Long, Long> queryLastUpdated(@Nonnull String fromTime, @Nonnull String toTime) throws APIException;

    /**
     * Returns a list of valid parameters for querying series which have been updated lately, as plain Strings. These keys are permitted to be used in
     * {@link QueryParameters} objects when querying for recently updated series.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Updates/get_updated_query_params"><b>[GET]</b> /updated/query/params</a>
     *
     * @see #queryLastUpdated(QueryParameters) queryLastUpdated(queryParameters)
     * @see JSON#getAvailableLastUpdatedQueryParameters()
     * @see Extended#getAvailableLastUpdatedQueryParameters()
     *
     * @return A list of possible parameters which may be used to query for last updated series, based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<String> getAvailableLastUpdatedQueryParameters() throws APIException;

    /**
     * Returns basic information about the currently authenticated user, mapped as Java DTO.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user"><b>[GET]</b> /user</a>
     *
     * @see JSON#getUser()
     * @see Extended#getUser()
     *
     * @return Basic user information, mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    User getUser() throws APIException;

    /**
     * Returns a list of favorite series for a given user, as plain Strings. Will be an empty list if no favorites exist.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_favorites"><b>[GET]</b> /user/favorites</a>
     *
     * @see JSON#getFavorites()
     * @see Extended#getFavorites()
     *
     * @return The user favorites, based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<String> getFavorites() throws APIException;

    /**
     * Deletes the given series ID from the user’s favorite’s list and returns the updated list as plain Strings.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/delete_user_favorites_id"><b>[DELETE]</b> /user/favorites/{id}</a>
     *
     * @see #addToFavorites(long) addToFavorites(seriesId)
     * @see JSON#deleteFromFavorites(long) TheTVDBApi.JSON.deleteFromFavorites(seriesId)
     * @see Extended#deleteFromFavorites(long) TheTVDBApi.Extended.deleteFromFavorites(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return Updated list of user favorites, based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<String> deleteFromFavorites(long seriesId) throws APIException;

    /**
     * Adds the supplied series ID to the user’s favorite’s list and returns the updated list as plain Strings.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/put_user_favorites_id"><b>[PUT]</b> /user/favorites/{id}</a>
     *
     * @see #deleteFromFavorites(long) deleteFromFavorites(seriesId)
     * @see JSON#addToFavorites(long) TheTVDBApi.JSON.addToFavorites(seriesId)
     * @see Extended#addToFavorites(long) TheTVDBApi.Extended.addToFavorites(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return Updated list of user favorites, based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<String> addToFavorites(long seriesId) throws APIException;

    /**
     * Returns a list of ratings for the given user, mapped as Java DTO.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings"><b>[GET]</b> /user/ratings</a>
     *
     * @see JSON#getRatings()
     * @see Extended#getRatings()
     *
     * @return List of user ratings, mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Rating> getRatings() throws APIException;

    /**
     * Returns a list of ratings for a given user that match the query, mapped as Java DTO.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings_query"><b>[GET]</b> /user/ratings/query</a>
     *
     * @see JSON#queryRatings(QueryParameters) TheTVDBApi.JSON.queryRatings(queryParameters)
     * @see Extended#queryRatings(QueryParameters) TheTVDBApi.Extended.queryRatings(queryParameters)
     *
     * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible query parameters
     *                        see the API documentation or use {@link #getAvailableRatingsQueryParameters()}.
     *
     * @return List of user ratings that match the given query, mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Rating> queryRatings(QueryParameters queryParameters) throws APIException;

    /**
     * Returns a list of ratings for a given user that match the <code>itemType</code> parameter, mapped as Java DTO. This is a shortcut-method for
     * {@link #queryRatings(QueryParameters) queryRatings(queryParameters)} with a single "itemType" query parameter.
     *
     * @param itemType Item to query. Can be either 'series', 'episode', or 'banner'.
     *
     * @return List of user ratings with the given item type, mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Rating> queryRatingsByItemType(@Nonnull String itemType) throws APIException;

    /**
     * Returns a list of valid parameters for querying user ratings, as plain Strings. These keys are permitted to be used in {@link QueryParameters}
     * objects when querying for ratings.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings_query_params"><b>[GET]</b> /user/ratings/query/params</a>
     *
     * @see #queryRatings(QueryParameters) queryRatings(queryParameters)
     * @see JSON#getAvailableRatingsQueryParameters()
     * @see Extended#getAvailableRatingsQueryParameters()
     *
     * @return A list of possible parameters which may be used to query for user ratings, based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<String> getAvailableRatingsQueryParameters() throws APIException;

    /**
     * Deletes a given rating of a given type.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/delete_user_ratings_itemType_itemId"><b>[DELETE]</b> /user/ratings/{itemType}/{itemId}</a>
     *
     * @see #addToRatings(String, long, long) addToRatings(itemType, itemId, itemRating)
     * @see JSON#deleteFromRatings(String, long) TheTVDBApi.JSON.deleteFromRatings(itemType, itemId)
     * @see Extended#deleteFromRatings(String, long) TheTVDBApi.Extended.deleteFromRatings(itemType, itemId)
     *
     * @param itemType Item to update. Can be either 'series', 'episode', or 'image'.
     * @param itemId ID of the ratings record that you wish to delete
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    void deleteFromRatings(@Nonnull String itemType, long itemId) throws APIException;

    /**
     * Updates a given rating of a given type and returns the modified rating, mapped as Java DTO. If no rating exists yet, a new rating
     * will be created.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/put_user_ratings_itemType_itemId_itemRating"><b>[PUT]</b> /user/ratings/{itemType}/{itemId}/{itemRating}</a>
     *
     * @see #deleteFromRatings(String, long) deleteFromRatings(itemType, itemId)
     * @see JSON#addToRatings(String, long, long) TheTVDBApi.JSON.addToRatings(itemType, itemId, itemRating)
     * @see Extended#addToRatings(String, long, long) TheTVDBApi.Extended.addToRatings(itemType, itemId, itemRating)
     *
     * @param itemType Item to update. Can be either 'series', 'episode', or 'image'.
     * @param itemId ID of the ratings record that you wish to modify
     * @param itemRating The updated rating number
     *
     * @return The modified rating (whether it was added or updated), mapped as Java DTO based on the JSON data returned by the remote service
     *         <br/>
     *         <b>Note:</b> It seems that the data returned by the remote service for this route is quite unreliable! It might not always return the
     *         modified rating but an empty data array instead.
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Rating> addToRatings(@Nonnull String itemType, long itemId, long itemRating) throws APIException;

    /**
     * Provides access to the API's {@link JSON JSON} layout.
     * <p/>
     * In this layout, all methods will return the raw, unmodified JSON as received from the remove service.
     *
     * @return Instance representing the the API's <code>JSON</code> layout
     */
    JSON json();

    /**
     * Provides access to the API's {@link Extended Extended} layout.
     * <p/>
     * In this layout, all methods will return a single {@link APIResponse} object, containing the actual request data, mapped as DTO, as well as
     * all additional information that is available in the corresponding context.
     *
     * @return Instance representing the the API's <code>Extended</code> layout
     */
    Extended extended();

    /**
     * Interface representing the API's <code>JSON</code> layout.
     * <p/>
     * This layout may be used if you do not want any post-processing being applied to the actual remote service response data. All methods within this
     * layout will return the raw, unmodified JSON data as it was received from the API. This might be useful if you prefer to map the JSON data yourself,
     * want to use your own Java data models or if you don't want to parse the JSON data at all (but forward it to some other service for example). This layout
     * does not provide any shortcut-methods though.
     *
     * @see #json()
     */
    interface JSON {

        /**
         * Returns the full information for a given episode id as raw JSON.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Episodes/get_episodes_id"><b>[GET]</b> /episodes/{id}</a>
         *
         * @see TheTVDBApi#getEpisode(long) TheTVDBApi.getEpisode(episodeId)
         * @see Extended#getEpisode(long) TheTVDBApi.Extended.getEpisode(episodeId)
         *
         * @param episodeId The ID of the episode
         *
         * @return JSON object containing the full episode information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getEpisode(long episodeId) throws APIException;

        /**
         * Returns an overview of all supported languages as raw JSON. These language abbreviations can be used to set the preferred language
         * for the communication with the remote service (see {@link #setLanguage(String)}.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages"><b>[GET]</b> /languages</a>
         *
         * @see TheTVDBApi#getAvailableLanguages() TheTVDBApi.getAvailableLanguages()
         * @see Extended#getAvailableLanguages()
         *
         * @return JSON object containing all languages that are supported by the remote service
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getAvailableLanguages() throws APIException;

        /**
         * Returns further language information for a given language ID as raw JSON. The language abbreviation can be used to set the preferred language
         * for the communication with the remote service (see {@link #setLanguage(String)}.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages_id"><b>[GET]</b> /languages/{id}</a>
         *
         * @see TheTVDBApi#getLanguage(long) TheTVDBApi.getLanguage(languageId)
         * @see Extended#getLanguage(long) TheTVDBApi.Extended.getLanguage(languageId)
         *
         * @param languageId The ID of the language
         *
         * @return JSON object containing detailed language information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
         *                      if the given language ID does not exist.
         */
        JsonNode getLanguage(long languageId) throws APIException;

        /**
         * Returns series search results based on the given query parameters as raw JSON. The result contains basic information of all series
         * matching the query parameters.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series"><b>[GET]</b> /search/series</a>
         *
         * @see TheTVDBApi#searchSeries(QueryParameters) TheTVDBApi.searchSeries(queryParameters)
         * @see Extended#searchSeries(QueryParameters) TheTVDBApi.Extended.searchSeries(queryParameters)
         *
         * @param queryParameters Object containing key/value pairs of query search parameters. For a complete list of possible search parameters
         *                        see the API documentation or use {@link #getAvailableSeriesSearchParameters()}.
         *
         * @return JSON object containing the series search results
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
         *                      if no records are found that match your query.
         */
        JsonNode searchSeries(QueryParameters queryParameters) throws APIException;

        /**
         * Returns detailed information for a specific series as raw JSON.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id"><b>[GET]</b> /series/{id}</a>
         *
         * @see TheTVDBApi#getSeries(long) TheTVDBApi.getSeries(seriesId)
         * @see Extended#getSeries(long) TheTVDBApi.Extended.getSeries(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return JSON object containing detailed information for a specific series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getSeries(long seriesId) throws APIException;


        /**
         * Returns possible query parameters, which can be used to search for series, as raw JSON.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series_params"><b>[GET]</b> /search/series/params</a>
         *
         * @see TheTVDBApi#getAvailableSeriesSearchParameters() TheTVDBApi.getAvailableSeriesSearchParameters()
         * @see Extended#getAvailableSeriesSearchParameters()
         * @see #searchSeries(QueryParameters) searchSeries(queryParams)
         * @see TheTVDBApi#searchSeries(QueryParameters) TheTVDBApi.searchSeries(queryParams)
         * @see Extended#searchSeries(QueryParameters) TheTVDBApi.Extended.searchSeries(queryParams)
         *
         * @return JSON object containing possible parameters to query by in the series search
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getAvailableSeriesSearchParameters() throws APIException;

        /**
         * Returns header information for a specific series as raw JSON. Good for getting the Last-Updated header to find out
         * when the series was last modified.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/head_series_id"><b>[HEAD]</b> /series/{id}</a>
         *
         * @see TheTVDBApi#getSeriesHeaderInformation(long) TheTVDBApi.getSeriesHeaderInformation(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return Artificial JSON object based on the HTML header information returned by the remote service
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getSeriesHeaderInformation(long seriesId) throws APIException;

        /**
         * Returns the actors for a specific series as raw JSON.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_actors"><b>[GET]</b> /series/{id}/actors</a>
         *
         * @see TheTVDBApi#getActors(long) TheTVDBApi.getActors(seriesId)
         * @see Extended#getActors(long) TheTVDBApi.Extended.getActors(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return JSON object containing the actors for a specific series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getActors(long seriesId) throws APIException;

        /**
         * Returns all episodes of a specific series as raw JSON. Results will be paginated with 100 results per page.
         * Use <code>queryParameters</code> to select a specific result page.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes"><b>[GET]</b> /series/{id}/episodes</a>
         *
         * @see TheTVDBApi#getEpisodes(long, QueryParameters) TheTVDBApi.getEpisodes(seriesId, queryParameters)
         * @see Extended#getEpisodes(long, QueryParameters) TheTVDBApi.Extended.getEpisodes(seriesId, queryParameters)
         *
         * @param seriesId The TheTVDB series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible parameters
         *                        see the API documentation.
         *
         * @return JSON object containing a single result page of episodes
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getEpisodes(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns all matching episodes of a specific series as raw JSON. Results will be paginated. Use <code>queryParameters</code> to filter
         * for specific episodes or to select a specific result page.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query"><b>[GET]</b> /series/{id}/episodes/query</a>
         *
         * @see TheTVDBApi#queryEpisodes(long, QueryParameters) TheTVDBApi.queryEpisodes(seriesId, queryParameters)
         * @see Extended#queryEpisodes(long, QueryParameters) TheTVDBApi.Extended.queryEpisodes(seriesId, queryParameters)
         *
         * @param seriesId The TheTVDB series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible query parameters
         *                        see the API documentation or use {@link #getAvailableEpisodeQueryParameters(long) getAvailableEpisodeQueryParameters(seriesId)}.
         *
         * @return JSON object containing a single result page of queried episode records
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode queryEpisodes(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a list of keys which are valid parameters for querying episodes, as raw JSON. These keys are permitted to be used in
         * {@link QueryParameters} objects when querying for specific episodes of a series.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query_params"><b>[GET]</b> /series/{id}/episodes/query/params</a>
         *
         * @see #queryEpisodes(long, QueryParameters) queryEpisodes(seriesId, queryParameters)
         * @see TheTVDBApi#getAvailableEpisodeQueryParameters(long) TheTVDBApi.getAvailableEpisodeQueryParameters(seriesId)
         * @see Extended#getAvailableEpisodeQueryParameters(long) TheTVDBApi.Extended.getAvailableEpisodeQueryParameters(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return JSON object containing all allowed keys to be used for querying episodes
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getAvailableEpisodeQueryParameters(long seriesId) throws APIException;

        /**
         * Returns a summary of the episodes and seasons available for a series, as raw JSON.
         * <br/>
         * <b>Note:</b> Season "0" is for all episodes that are considered to be specials.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_summary"><b>[GET]</b> /series/{id}/episodes/summary</a>
         *
         * @see TheTVDBApi#getSeriesEpisodesSummary(long) TheTVDBApi.getSeriesEpisodesSummary(seriesId)
         * @see Extended#getSeriesEpisodesSummary(long) TheTVDBApi.Extended.getSeriesEpisodesSummary(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return JSON object containing a summary of the episodes and seasons avaialable for the given series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getSeriesEpisodesSummary(long seriesId) throws APIException;

        /**
         * Returns a filtered series record based on the given parameters, as raw JSON.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_filter"><b>[GET]</b> /series/{id}/filter</a>
         *
         * @see TheTVDBApi#filterSeries(long, QueryParameters) TheTVDBApi.filterSeries(seriesId, queryParameters)
         * @see Extended#filterSeries(long, QueryParameters) TheTVDBApi.Extended.filterSeries(seriesId, queryParameters)
         *
         * @param seriesId The TheTVDB series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible query parameters
         *                        see the API documentation or use {@link #getAvailableSeriesFilterParameters(long)} getAvailableSeriesFilterParameters(seriesId)}.
         *
         * @return JSON object containing a filtered series record
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode filterSeries(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a list of keys which are valid parameters for filtering series, as raw JSON. These keys are permitted to be used in
         * {@link QueryParameters} objects when filtering for a specific series.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_filter_params"><b>[GET]</b> /series/{id}/filter/params</a>
         *
         * @see #filterSeries(long, QueryParameters) filterSeries(seriesId, queryParameters)
         * @see TheTVDBApi#getAvailableSeriesFilterParameters(long) TheTVDBApi.getAvailableSeriesFilterParameters(seriesId)
         * @see Extended#getAvailableSeriesFilterParameters(long) TheTVDBApi.Extended.getAvailableSeriesFilterParameters(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return JSON object containing a list of all keys allowed to filter by
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getAvailableSeriesFilterParameters(long seriesId) throws APIException;

        /**
         * Returns a summary of the images types and counts available for a particular series, as raw JSON.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images"><b>[GET]</b> /series/{id}/images</a>
         *
         * @see TheTVDBApi#getSeriesImagesSummary(long) TheTVDBApi.getSeriesImagesSummary(seriesId)
         * @see Extended#getSeriesImagesSummary(long) TheTVDBApi.Extended.getSeriesImagesSummary(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return JSON object containing a summary of the image types and counts available for the given series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getSeriesImagesSummary(long seriesId) throws APIException;

        /**
         * Returns the matching result of querying images for a specific series, as raw JSON.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query"><b>[GET]</b> /series/{id}/images/query</a>
         *
         * @see TheTVDBApi#queryImages(long, QueryParameters) TheTVDBApi.queryImages(seriesId, queryParameters)
         * @see Extended#queryImages(long, QueryParameters) TheTVDBApi.Extended.queryImages(seriesId, queryParameters)
         *
         * @param seriesId The TheTVDB series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible query parameters
         *                        see the API documentation or use {@link #getAvailableImageQueryParameters(long)} getAvailableImageQueryParameters(seriesId)}.
         *
         * @return JSON object containing images that matched the query
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode queryImages(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a list of valid parameters for querying a series images, as raw JSON. Unlike other routes, querying for a series images may be resticted
         * to certain combinations of query keys. The allowed combinations are clustered in the data array of the returned JSON object.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query_params"><b>[GET]</b> /series/{id}/images/query/params</a>
         *
         * @see #queryImages(long, QueryParameters) queryImages(seriesId, queryParameters)
         * @see TheTVDBApi#getAvailableImageQueryParameters(long) TheTVDBApi.getAvailableImageQueryParameters(seriesId)
         * @see Extended#getAvailableImageQueryParameters(long) TheTVDBApi.Extended.getAvailableImageQueryParameters(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return JSON object containing a list of possible parameters which may be used to query a series images
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getAvailableImageQueryParameters(long seriesId) throws APIException;

        /**
         * Returns an array of series that have changed in a maximum of one week blocks since the provided <code>fromTime</code> query parameter, as raw JSON. Note
         * that the given query parameters must always contain a valid <code>fromTime</code> Epoch timestamp key.
         * <p/>
         * The user may specify an additional <code>toTime</code> query key to grab results for less than a week. Any timespan larger than a week will be reduced
         * down to one week automatically.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Updates/get_updated_query"><b>[GET]</b> /updated/query</a>
         *
         * @see TheTVDBApi#queryLastUpdated(QueryParameters) TheTVDBApi.queryLastUpdated(queryParameters)
         * @see Extended#queryLastUpdated(QueryParameters) TheTVDBApi.Extended.queryLastUpdated(queryParameters)
         *
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible query parameters
         *                        see the API documentation or use {@link #getAvailableLastUpdatedQueryParameters()}.
         *
         * @return JSON object containing a list of updated objects that match the given timeframe
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode queryLastUpdated(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a list of valid parameters for querying series which have been updated lately, as raw JSON. These keys are permitted to be used in
         * {@link QueryParameters} objects when querying for recently updated series.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Updates/get_updated_query_params"><b>[GET]</b> /updated/query/params</a>
         *
         * @see #queryLastUpdated(QueryParameters) queryLastUpdated(queryParameters)
         * @see TheTVDBApi#getAvailableLastUpdatedQueryParameters()
         * @see Extended#getAvailableLastUpdatedQueryParameters()
         *
         * @return JSON object containing a list of possible parameters which may be used to query for last updated series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getAvailableLastUpdatedQueryParameters() throws APIException;

        /**
         * Returns basic information about the currently authenticated user, as raw JSON.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user"><b>[GET]</b> /user</a>
         *
         * @see TheTVDBApi#getUser()
         * @see Extended#getUser()
         *
         * @return JSON object containing basic user information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getUser() throws APIException;

        /**
         * Returns an array of favorite series for a given user, as raw JSON. Will be a blank array if no favorites exist.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_favorites"><b>[GET]</b> /user/favorites</a>
         *
         * @see TheTVDBApi#getFavorites()
         * @see Extended#getFavorites()
         *
         * @return JSON object containing the user favorites
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getFavorites() throws APIException;

        /**
         * Deletes the given series ID from the user’s favorite’s list and returns the updated list as raw JSON.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/delete_user_favorites_id"><b>[DELETE]</b> /user/favorites/{id}</a>
         *
         * @see #addToFavorites(long) addToFavorites(seriesId)
         * @see TheTVDBApi#deleteFromFavorites(long) TheTVDBApi.deleteFromFavorites(seriesId)
         * @see Extended#deleteFromFavorites(long) TheTVDBApi.Extended.deleteFromFavorites(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return JSON object containing the updated list of user favorites
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode deleteFromFavorites(long seriesId) throws APIException;

        /**
         * Adds the supplied series ID to the user’s favorite’s list and returns the updated list as raw JSON.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/put_user_favorites_id"><b>[PUT]</b> /user/favorites/{id}</a>
         *
         * @see #deleteFromFavorites(long) deleteFromFavorites(seriesId)
         * @see TheTVDBApi#addToFavorites(long) TheTVDBApi.addToFavorites(seriesId)
         * @see Extended#addToFavorites(long) TheTVDBApi.Extended.addToFavorites(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return JSON object containing the updated list of user favorites
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode addToFavorites(long seriesId) throws APIException;

        /**
         * Returns a list of ratings for the given user, as raw JSON.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings"><b>[GET]</b> /user/ratings</a>
         *
         * @see TheTVDBApi#getRatings()
         * @see Extended#getRatings()
         *
         * @return JSON object containing a list of user ratings
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getRatings() throws APIException;

        /**
         * Returns a list of ratings for a given user that match the query, as raw JSON.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings_query"><b>[GET]</b> /user/ratings/query</a>
         *
         * @see TheTVDBApi#queryRatings(QueryParameters) TheTVDBApi.queryRatings(queryParameters)
         * @see Extended#queryRatings(QueryParameters) TheTVDBApi.Extended.queryRatings(queryParameters)
         *
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible query parameters
         *                        see the API documentation or use {@link #getAvailableRatingsQueryParameters()}.
         *
         * @return JSON object containing a list of user ratings that match the given query
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode queryRatings(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a list of valid parameters for querying user ratings, as raw JSON. These keys are permitted to be used in {@link QueryParameters}
         * objects when querying for ratings.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings_query_params"><b>[GET]</b> /user/ratings/query/params</a>
         *
         * @see #queryRatings(QueryParameters) queryRatings(queryParameters)
         * @see TheTVDBApi#getAvailableRatingsQueryParameters()
         * @see Extended#getAvailableRatingsQueryParameters()
         *
         * @return JSON object containing a list of possible parameters which may be used to query for user ratings
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getAvailableRatingsQueryParameters() throws APIException;

        /**
         * Deletes a given rating of a given type.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/delete_user_ratings_itemType_itemId"><b>[DELETE]</b> /user/ratings/{itemType}/{itemId}</a>
         *
         * @see #addToRatings(String, long, long) addToRatings(itemType, itemId, itemRating)
         * @see TheTVDBApi#deleteFromRatings(String, long) TheTVDBApi.deleteFromRatings(itemType, itemId)
         * @see Extended#deleteFromRatings(String, long) TheTVDBApi.Extended.deleteFromRatings(itemType, itemId)
         *
         * @param itemType Item to update. Can be either 'series', 'episode', or 'image'.
         * @param itemId ID of the ratings record that you wish to delete
         *
         * @return JSON object as returned by the remote service (probably containing an empty data block)
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode deleteFromRatings(@Nonnull String itemType, long itemId) throws APIException;

        /**
         * Updates a given rating of a given type and returns the modified rating, mapped as raw JSON. If no rating exists yet, a new rating
         * will be created.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/put_user_ratings_itemType_itemId_itemRating"><b>[PUT]</b> /user/ratings/{itemType}/{itemId}/{itemRating}</a>
         *
         * @see #deleteFromRatings(String, long) deleteFromRatings(itemType, itemId)
         * @see TheTVDBApi#addToRatings(String, long, long) TheTVDBApi.addToRatings(itemType, itemId, itemRating)
         * @see Extended#addToRatings(String, long, long) TheTVDBApi.Extended.addToRatings(itemType, itemId, itemRating)
         *
         * @param itemType Item to update. Can be either 'series', 'episode', or 'image'.
         * @param itemId ID of the ratings record that you wish to modify
         * @param itemRating The updated rating number
         *
         * @return JSON object containing the modified rating (whether it was added or updated)
         *         <br/>
         *         <b>Note:</b> It seems that the data returned by the remote service for this route is quite unreliable! It might not always return the
         *         modified rating but an empty data array instead.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode addToRatings(@Nonnull String itemType, long itemId, long itemRating) throws APIException;
    }

    /**
     * Interface representing the API's <code>Extended</code> layout.
     * <p/>
     * This layout may be used for slightly advance API integration. Like the common layout it'll take care of parsing the recieved JSON into Java DTO's
     * but it will also provide access to any additional contextual information. Methods of this layout will always return a single {@link APIResponse}
     * object which consists of the actual data, parsed as DTO, as well as all additional information which is available in the given context, like
     * additional error or pagination information. This layout does not provide any shortcut-methods.
     *
     * @see #extended()
     */
    interface Extended {

        /**
         * Returns a response object containing the full information for a given episode id as mapped Java DTO.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Episodes/get_episodes_id"><b>[GET]</b> /episodes/{id}</a>
         *
         * @see JSON#getEpisode(long) TheTVDBApi.JSON.getEpisode(episodeId)
         * @see TheTVDBApi#getEpisode(long) TheTVDBApi.getEpisode(episodeId)
         *
         * @param episodeId The ID of the episode
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<Episode> getEpisode(long episodeId) throws APIException;

        /**
         * Returns a response object containing a list of all supported languages mapped as Java DTO. These language abbreviations can be used to set the preferred language
         * for the communication with the remote service (see {@link #setLanguage(String)}.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages"><b>[GET]</b> /languages</a>
         *
         * @see JSON#getAvailableLanguages()
         * @see TheTVDBApi#getAvailableLanguages() TheTVDBApi.getAvailableLanguages()
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<List<Language>> getAvailableLanguages() throws APIException;

        /**
         * Returns a response object containing further language information for a given language ID mapped as Java DTO. The language abbreviation can be used to set the preferred language
         * for the communication with the remote service (see {@link #setLanguage(String)}.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages_id"><b>[GET]</b> /languages/{id}</a>
         *
         * @see JSON#getLanguage(long) TheTVDBApi.JSON.getLanguage(languageId)
         * @see TheTVDBApi#getLanguage(long) TheTVDBApi.getLanguage(languageId)
         *
         * @param languageId The ID of the language
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
         *                      if the given language ID does not exist.
         */
        APIResponse<Language> getLanguage(long languageId) throws APIException;

        /**
         * Returns a response object containing a list of series search results based on the given query parameters mapped as Java DTO. The list contains
         * basic information of all series matching the query parameters.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series"><b>[GET]</b> /search/series</a>
         *
         * @see JSON#searchSeries(QueryParameters) TheTVDBApi.JSON.searchSeries(queryParameters)
         * @see TheTVDBApi#searchSeries(QueryParameters) TheTVDBApi.searchSeries(queryParameters)
         *
         * @param queryParameters Object containing key/value pairs of query search parameters. For a complete list of possible search parameters
         *                        see the API documentation or use {@link #getAvailableSeriesSearchParameters()}.
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
         *                      if no records are found that match your query.
         */
        APIResponse<List<SeriesSearchResult>> searchSeries(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing possible query parameters, which can be used to search for series, mapped as Java DTO.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series_params"><b>[GET]</b> /search/series/params</a>
         *
         * @see JSON#getAvailableSeriesSearchParameters()
         * @see TheTVDBApi#getAvailableSeriesSearchParameters() TheTVDBApi.getAvailableSeriesSearchParameters()
         * @see JSON#searchSeries(QueryParameters) TheTVDBApi.JSON.searchSeries(queryParams)
         * @see TheTVDBApi#searchSeries(QueryParameters) TheTVDBApi.searchSeries(queryParams)
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<List<String>> getAvailableSeriesSearchParameters() throws APIException;

        /**
         * Returns a response object containing detailed information for a specific series mapped as Java DTO.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id"><b>[GET]</b> /series/{id}</a>
         *
         * @see JSON#getSeries(long) TheTVDBApi.JSON.getSeries(seriesId)
         * @see TheTVDBApi#getSeries(long) TheTVDBApi.getSeries(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<Series> getSeries(long seriesId) throws APIException;

        /**
         * Returns a response object containing a list of actors for a specific series mapped as Java DTO.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_actors"><b>[GET]</b> /series/{id}/actors</a>
         *
         * @see JSON#getActors(long) TheTVDBApi.JSON.getActors(seriesId)
         * @see TheTVDBApi#getActors(long) TheTVDBApi.getActors(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<List<Actor>> getActors(long seriesId) throws APIException;

        /**
         * Returns a response object containing all episodes of a specific series mapped as Java DTO. Results will be paginated with 100 results per page.
         * Use <code>queryParameters</code> to select a specific result page.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes"><b>[GET]</b> /series/{id}/episodes</a>
         *
         * @see JSON#getEpisodes(long, QueryParameters) TheTVDBApi.JSON.getEpisodes(seriesId, queryParameters)
         * @see TheTVDBApi#getEpisodes(long, QueryParameters) TheTVDBApi.getEpisodes(seriesId, queryParameters)
         *
         * @param seriesId The TheTVDB series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible parameters
         *                        see the API documentation.
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<List<Episode>> getEpisodes(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing all matching episodes of a specific series mapped as Java DTO. Results will be paginated. Note that this method
         * is deterministic and will always return the <b>first</b> result page of the available episodes.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query"><b>[GET]</b> /series/{id}/episodes/query</a>
         *
         * @see JSON#queryEpisodes(long, QueryParameters) TheTVDBApi.JSON.queryEpisodes(seriesId, queryParameters)
         * @see TheTVDBApi#queryEpisodes(long, QueryParameters) TheTVDBApi.queryEpisodes(seriesId, queryParameters)
         *
         * @param seriesId The TheTVDB series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible query parameters
         *                        see the API documentation or use {@link #getAvailableEpisodeQueryParameters(long) getAvailableEpisodeQueryParameters(seriesId)}.
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<List<Episode>> queryEpisodes(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a list of keys which are valid parameters for querying episodes, as plain Strings. These keys are permitted to be used
         * in {@link QueryParameters} objects when querying for specific episodes of a series.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query_params"><b>[GET]</b> /series/{id}/episodes/query/params</a>
         *
         * @see #queryEpisodes(long, QueryParameters) queryEpisodes(seriesId, queryParameters)
         * @see JSON#getAvailableEpisodeQueryParameters(long) TheTVDBApi.JSON.getAvailableEpisodeQueryParameters(seriesId)
         * @see TheTVDBApi#getAvailableEpisodeQueryParameters(long) TheTVDBApi.getAvailableEpisodeQueryParameters(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<List<String>> getAvailableEpisodeQueryParameters(long seriesId) throws APIException;

        /**
         * Returns a response object containing a summary of the episodes and seasons available for a series, mapped as Java DTO.
         * <br/>
         * <b>Note:</b> Season "0" is for all episodes that are considered to be specials.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_summary"><b>[GET]</b> /series/{id}/episodes/summary</a>
         *
         * @see JSON#getSeriesEpisodesSummary(long) TheTVDBApi.JSON.getSeriesEpisodesSummary(seriesId)
         * @see TheTVDBApi#getSeriesEpisodesSummary(long) TheTVDBApi.getSeriesEpisodesSummary(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<SeriesSummary> getSeriesEpisodesSummary(long seriesId) throws APIException;

        /**
         * Returns a response object containing a filtered series record based on the given parameters, mapped as Java DTO.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_filter"><b>[GET]</b> /series/{id}/filter</a>
         *
         * @see JSON#filterSeries(long, QueryParameters) TheTVDBApi.JSON.filterSeries(seriesId, queryParameters)
         * @see TheTVDBApi#filterSeries(long, QueryParameters) TheTVDBApi.filterSeries(seriesId, queryParameters)
         *
         * @param seriesId The TheTVDB series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible query parameters
         *                        see the API documentation or use {@link #getAvailableSeriesFilterParameters(long)} getAvailableSeriesFilterParameters(seriesId)}.
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<Series> filterSeries(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a list of keys which are valid parameters for filtering series, as plain Strings. These keys are permitted
         * to be used in {@link QueryParameters} objects when filtering for a specific series.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_filter_params"><b>[GET]</b> /series/{id}/filter/params</a>
         *
         * @see #filterSeries(long, QueryParameters) filterSeries(seriesId, queryParameters)
         * @see JSON#getAvailableSeriesFilterParameters(long) TheTVDBApi.JSON.getAvailableSeriesFilterParameters(seriesId)
         * @see TheTVDBApi#getAvailableSeriesFilterParameters(long) TheTVDBApi.getAvailableSeriesFilterParameters(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<List<String>> getAvailableSeriesFilterParameters(long seriesId) throws APIException;

        /**
         * Returns a response object containing a summary of the images types and counts available for a particular series, mapped as Java DTO.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images"><b>[GET]</b> /series/{id}/images</a>
         *
         * @see JSON#getSeriesImagesSummary(long) TheTVDBApi.JSON.getSeriesImagesSummary(seriesId)
         * @see TheTVDBApi#getSeriesImagesSummary(long) TheTVDBApi.getSeriesImagesSummary(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<ImageSummary> getSeriesImagesSummary(long seriesId) throws APIException;

        /**
         * Returns a response object containing the matching result of querying images for a specific series, mapped as Java DTO.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query"><b>[GET]</b> /series/{id}/images/query</a>
         *
         * @see JSON#queryImages(long, QueryParameters) TheTVDBApi.JSON.queryImages(seriesId, queryParameters)
         * @see TheTVDBApi#queryImages(long, QueryParameters) TheTVDBApi.queryImages(seriesId, queryParameters)
         *
         * @param seriesId The TheTVDB series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible query parameters
         *                        see the API documentation or use {@link #getAvailableImageQueryParameters(long)} getAvailableImageQueryParameters(seriesId)}.
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<List<Image>> queryImages(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a list of valid parameters for querying a series images, mapped as Java DTO. Unlike other routes, querying for
         * a series images may be resticted to certain combinations of query keys. The allowed combinations are clustered in the single {@link ImageQueryParameter}
         * objects of the returned API responses data object.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query_params"><b>[GET]</b> /series/{id}/images/query/params</a>
         *
         * @see #queryImages(long, QueryParameters) queryImages(seriesId, queryParameters)
         * @see JSON#getAvailableImageQueryParameters(long) TheTVDBApi.JSON.getAvailableImageQueryParameters(seriesId)
         * @see TheTVDBApi#getAvailableImageQueryParameters(long) TheTVDBApi.getAvailableImageQueryParameters(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<List<ImageQueryParameter>> getAvailableImageQueryParameters(long seriesId) throws APIException;

        /**
         * Returns a response object containing a map of series that have changed in a maximum of one week blocks since the provided <code>fromTime</code>
         * query parameter, as plain Strings. The key/value pairs of the returned data object's map represent a TheTVDB series ID (key) and when it was updated
         * the last time (value) as Epoch time. Note that the given query parameters must always contain a valid <code>fromTime</code> Epoch timestamp key.
         * <p/>
         * The user may specify an additional <code>toTime</code> query key to grab results for less than a week. Any timespan larger than a week will be reduced
         * down to one week automatically.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Updates/get_updated_query"><b>[GET]</b> /updated/query</a>
         *
         * @see JSON#queryLastUpdated(QueryParameters) TheTVDBApi.JSON.queryLastUpdated(queryParameters)
         * @see TheTVDBApi#queryLastUpdated(QueryParameters) TheTVDBApi.queryLastUpdated(queryParameters)
         *
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible query parameters
         *                        see the API documentation or use {@link #getAvailableLastUpdatedQueryParameters()}.
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<Map<Long, Long>> queryLastUpdated(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a list of valid parameters for querying series which have been updated lately, as plain Strings. These keys
         * are permitted to be used in {@link QueryParameters} objects when querying for recently updated series.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Updates/get_updated_query_params"><b>[GET]</b> /updated/query/params</a>
         *
         * @see #queryLastUpdated(QueryParameters) queryLastUpdated(queryParameters)
         * @see JSON#getAvailableLastUpdatedQueryParameters()
         * @see TheTVDBApi#getAvailableLastUpdatedQueryParameters()
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<List<String>> getAvailableLastUpdatedQueryParameters() throws APIException;

        /**
         * Returns a response object containing basic information about the currently authenticated user, mapped as Java DTO.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user"><b>[GET]</b> /user</a>
         *
         * @see JSON#getUser()
         * @see TheTVDBApi#getUser()
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<User> getUser() throws APIException;

        /**
         * Returns a response object containing a list of favorite series for a given user, as plain Strings. The data object of the returned response
         * will contain an empty list if no favorites exist.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_favorites"><b>[GET]</b> /user/favorites</a>
         *
         * @see JSON#getFavorites()
         * @see TheTVDBApi#getFavorites()
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<List<String>> getFavorites() throws APIException;

        /**
         * Deletes the given series ID from the user’s favorite’s list and returns a response object containing the updated list as plain Strings.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/delete_user_favorites_id"><b>[DELETE]</b> /user/favorites/{id}</a>
         *
         * @see #addToFavorites(long) addToFavorites(seriesId)
         * @see JSON#deleteFromFavorites(long) TheTVDBApi.JSON.deleteFromFavorites(seriesId)
         * @see TheTVDBApi#deleteFromFavorites(long) TheTVDBApi.deleteFromFavorites(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<List<String>> deleteFromFavorites(long seriesId) throws APIException;

        /**
         * Adds the supplied series ID to the user’s favorite’s list and returns a response object containing the updated list as plain Strings.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/put_user_favorites_id"><b>[PUT]</b> /user/favorites/{id}</a>
         *
         * @see #deleteFromFavorites(long) deleteFromFavorites(seriesId)
         * @see JSON#addToFavorites(long) TheTVDBApi.JSON.addToFavorites(seriesId)
         * @see TheTVDBApi#addToFavorites(long) TheTVDBApi.addToFavorites(seriesId)
         *
         * @param seriesId The TheTVDB series ID
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<List<String>> addToFavorites(long seriesId) throws APIException;

        /**
         * Returns a response object containing a list of ratings for the given user, mapped as Java DTO.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings"><b>[GET]</b> /user/ratings</a>
         *
         * @see JSON#getRatings()
         * @see TheTVDBApi#getRatings()
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<List<Rating>> getRatings() throws APIException;

        /**
         * Returns a response object containing a list of ratings for a given user that match the query, mapped as Java DTO.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings_query"><b>[GET]</b> /user/ratings/query</a>
         *
         * @see JSON#queryRatings(QueryParameters) TheTVDBApi.JSON.queryRatings(queryParameters)
         * @see TheTVDBApi#queryRatings(QueryParameters) TheTVDBApi.queryRatings(queryParameters)
         *
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible query parameters
         *                        see the API documentation or use {@link #getAvailableRatingsQueryParameters()}.
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<List<Rating>> queryRatings(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a list of valid parameters for querying user ratings, as plain Strings. These keys are permitted to be
         * used in {@link QueryParameters} objects when querying for ratings.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings_query_params"><b>[GET]</b> /user/ratings/query/params</a>
         *
         * @see #queryRatings(QueryParameters) queryRatings(queryParameters)
         * @see JSON#getAvailableRatingsQueryParameters()
         * @see TheTVDBApi#getAvailableRatingsQueryParameters()
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<List<String>> getAvailableRatingsQueryParameters() throws APIException;

        /**
         * Deletes a given rating of a given type.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/delete_user_ratings_itemType_itemId"><b>[DELETE]</b> /user/ratings/{itemType}/{itemId}</a>
         *
         * @see #addToRatings(String, long, long) addToRatings(itemType, itemId, itemRating)
         * @see JSON#deleteFromRatings(String, long) TheTVDBApi.JSON.deleteFromRatings(itemType, itemId)
         * @see TheTVDBApi#deleteFromRatings(String, long) TheTVDBApi.deleteFromRatings(itemType, itemId)
         *
         * @param itemType Item to update. Can be either 'series', 'episode', or 'image'.
         * @param itemId ID of the ratings record that you wish to delete
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        void deleteFromRatings(@Nonnull String itemType, long itemId) throws APIException;

        /**
         * Updates a given rating of a given type and return a response object containing the modified rating, mapped as Java DTO. If no rating exists
         * yet, a new rating will be created.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/put_user_ratings_itemType_itemId_itemRating"><b>[PUT]</b> /user/ratings/{itemType}/{itemId}/{itemRating}</a>
         *
         * @see #deleteFromRatings(String, long) deleteFromRatings(itemType, itemId)
         * @see JSON#addToRatings(String, long, long) TheTVDBApi.JSON.addToRatings(itemType, itemId, itemRating)
         * @see TheTVDBApi#addToRatings(String, long, long) TheTVDBApi.addToRatings(itemType, itemId, itemRating)
         *
         * @param itemType Item to update. Can be either 'series', 'episode', or 'image'.
         * @param itemId ID of the ratings record that you wish to modify
         * @param itemRating The updated rating number
         *
         * @return Extended API response containing the actually requested data as well as optional, additional error and paging information. Please note
         *         that not all API routes provide additional information so this type of data might be empty.
         *         <br/>
         *         <b>Note:</b> It seems that the data returned by the remote service for this route is quite unreliable! It might not always return the
         *         modified rating but an empty data array instead.
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        APIResponse<List<Rating>> addToRatings(@Nonnull String itemType, long itemId, long itemRating) throws APIException;
    }
}

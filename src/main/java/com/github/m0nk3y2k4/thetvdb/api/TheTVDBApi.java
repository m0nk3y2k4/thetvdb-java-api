package com.github.m0nk3y2k4.thetvdb.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.model.data.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

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
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Authentication/post_login">/login</a>
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    void login() throws APIException;

    /**
     * Refreshes the current, valid JWT session token. This method can be used to extend the expiration date (24 hours) of the current session token without the need of a
     * complete new login. This method will be called automatically if an API call is made using an expired JWT session token.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Authentication/get_refresh_token">/refresh_token</a>
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    void refreshToken() throws APIException;

    /**
     * Returns the full information for a given episode id as mapped Java object.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Episodes/get_episodes_id">/episodes/{id}</a>
     *
     * @see JSON#getEpisode(long) TheTVDBApi.JSON.getEpisode(episodeId)
     *
     * @param episodeId The ID of the episode
     *
     * @return Mapped Java object containing the full episode information based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    Episode getEpisode(long episodeId) throws APIException;

    /**
     * Returns a list of all supported languages mapped as Java object. These language abbreviations can be used to set the preferred language
     * for the communication with the remote service (see {@link #setLanguage(String)}.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages">/languages</a>
     *
     * @see JSON#getAvailableLanguages()
     *
     * @return List of available languages mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Language> getAvailableLanguages() throws APIException;

    /**
     * Returns further language information for a given language ID mapped as Java object. The language abbreviation can be used to set the preferred language
     * for the communication with the remote service (see {@link #setLanguage(String)}.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages_id">/languages/{id}</a>
     *
     * @see JSON#getLanguage(long) TheTVDBApi.JSON.getLanguage(languageId)
     *
     * @param languageId The ID of the language
     *
     * @return Mapped Java object containing detailed language information based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    Language getLanguage(long languageId) throws APIException;

    /**
     * Returns a list of series search results based on the given query parameters mapped as Java object. The list contains basic information
     * of all series matching the query parameters.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series">/search/series</a>
     *
     * @see JSON#searchSeries(QueryParameters) TheTVDBApi.JSON.searchSeries(queryParameters)
     *
     * @param queryParameters Object containing key/value pairs of query search parameters. For a complete list of possible search parameters
     *                        see the API documentation or use {@link #getAvailableSeriesSearchParameters()}.
     *
     * @return List of series search results mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<SeriesSearchResult> searchSeries(QueryParameters queryParameters) throws APIException;

    /**
     * Search for series by name. Returns a list of series search results mapped as Java object. The search results contain basic information
     * of all series matching the given name. This is a shortcut-method for {@link #searchSeries(QueryParameters)} with a single "name" query parameter.
     *
     * @param name The name of the series to search for
     *
     * @return List of series search results mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<SeriesSearchResult> searchSeriesByName(@Nonnull String name) throws APIException;

    /**
     * Search for series by IMDB-Id. Returns a list of series search results mapped as Java object. The search results contain basic information
     * of all series matching the given IMDB-Id. This is a shortcut-method for {@link #searchSeries(QueryParameters)} with a single "imdbId" query parameter.
     *
     * @param imdbId The IMDB-Id of the series to search for
     *
     * @return List of series search results mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<SeriesSearchResult> searchSeriesByImdbId(@Nonnull String imdbId) throws APIException;

    /**
     * Search for series by Zap2it-Id. Returns a list of series search results mapped as Java object. The search results contain basic information
     * of all series matching the given Zap2it-Id. This is a shortcut-method for {@link #searchSeries(QueryParameters)} with a single "zap2itId" query parameter.
     *
     * @param zap2itId The Zap2it-Id of the series to search for
     *
     * @return List of series search results mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<SeriesSearchResult> searchSeriesByZap2itId(@Nonnull String zap2itId) throws APIException;

    /**
     * Returns possible query parameters, which can be used to search for series, mapped as Java object.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series_params">/search/series/params</a>
     *
     * @see JSON#getAvailableSeriesSearchParameters()
     * @see JSON#searchSeries(QueryParameters) TheTVDBApi.JSON.searchSeries(queryParams)
     * @see #searchSeries(QueryParameters) searchSeries(queryParams)
     *
     * @return List of possible parameters to query by in the series search
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<String> getAvailableSeriesSearchParameters() throws APIException;

    /**
     * Returns detailed information for a specific series mapped as Java object.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id">/series/{id}</a>
     *
     * @see JSON#getSeries(long) TheTVDBApi.JSON.getSeries(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return Detailed information for a specific series mapped as Java object based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    Series getSeries(long seriesId) throws APIException;

    /**
     * Returns header information for a specific series as key/value pairs. Good for getting the Last-Updated header to find out
     * when the series was last modified.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/head_series_id">/series/{id}</a>
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
     * Returns a list of actors for a specific series mapped as Java objects.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_actors">/series/{id}/actors</a>
     *
     * @see JSON#getActors(long) TheTVDBApi.JSON.getActors(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return List of actors mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Actor> getActors(long seriesId) throws APIException;

    /**
     * Returns all episodes of a specific series mapped as Java objects. Results will be paginated with 100 results per page.
     * Use <code>queryParameters</code> to select a specific result page.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes">/series/{id}/episodes</a>
     *
     * @see JSON#getEpisodes(long, QueryParameters) TheTVDBApi.JSON.getEpisodes(seriesId, queryParameters)
     *
     * @param seriesId The TheTVDB series ID
     * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible search parameters
     *                        see the API documentation.
     *
     * @return List of episodes mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Episode> getEpisodes(long seriesId, QueryParameters queryParameters) throws APIException;

    /**
     * Returns the first 100 episodes of a specific series mapped as Java objects. Note that this method is deterministic and
     * will always return the <b>first</b> result page of the available episodes. This is a shortcut-method for {@link #getEpisodes(long, QueryParameters)}
     * with an empty query parameter.
     *
     * @see #getEpisodes(long, long) getEpisodes(seriesId, page)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return List of episodes mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Episode> getEpisodes(long seriesId) throws APIException;

    /**
     * Returns a list of episodes of a specific series mapped as Java objects. The result list will contain 100 episodes at most. For
     * series with more episodes use the <code>page</code> parameter to browse to a specific result page. This is a shortcut-method for
     * {@link #getEpisodes(long, QueryParameters)} with a single "page" query parameter.
     *
     * @see #getEpisodes(long) getEpisodes(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     * @param page The result page to be returned
     *
     * @return List of episodes mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Episode> getEpisodes(long seriesId, long page) throws APIException;

    /**
     * Returns all matching episodes of a specific series mapped as Java objects. Results will be paginated. Note that this method
     * is deterministic and will always return the <b>first</b> result page of the available episodes.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query">/series/{id}/episodes/query</a>
     *
     * @see JSON#queryEpisodes(long, QueryParameters) TheTVDBApi.JSON.queryEpisodes(seriesId, queryParameters)
     *
     * @param seriesId The TheTVDB series ID
     * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible search parameters
     *                        see the API documentation.
     *
     * @return List of episodes matching the query parameters, mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Episode> queryEpisodes(long seriesId, QueryParameters queryParameters) throws APIException;

    /**
     * Returns all episodes of a specific series and season mapped as Java objects. Results will be paginated. Note that this method
     * is deterministic and will always return the <b>first</b> result page of the available episodes. This is a shortcut-method for {@link JSON#queryEpisodes(long, QueryParameters)}
     * with a single "airedSeason" query parameter.
     *
     * @see #queryEpisodesByAiredSeason(long, long, long) queryEpisodesByAiredSeason(seriesId, airedSeason, page)
     *
     * @param seriesId The TheTVDB series ID
     * @param airedSeason The number of the aired season to query for
     *
     * @return List of episodes for a specific season, mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Episode> queryEpisodesByAiredSeason(long seriesId, long airedSeason) throws APIException;

    /**
     * Returns all episodes of a specific series and season mapped as Java objects. Results will be paginated. For seasons with
     * a high number of episodes use the <code>page</code> parameter to browse to a specific result page. This is a shortcut-method for {@link JSON#queryEpisodes(long, QueryParameters)}
     * with a "airedSeason" and "page" query parameter.
     *
     * @see #queryEpisodesByAiredSeason(long, long) queryEpisodesByAiredSeason(seriesId, airedSeason)
     *
     * @param seriesId The TheTVDB series ID
     * @param airedSeason The number of the aired season to query for
     * @param page The result page to be returned
     *
     * @return List of episodes for a specific season, mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Episode> queryEpisodesByAiredSeason(long seriesId, long airedSeason, long page) throws APIException;

    /**
     * Returns all episodes of a specific series, matching the <code>airedEpisode</code> parameter, mapped as Java objects. Results will be paginated.
     * This is a shortcut-method for {@link JSON#queryEpisodes(long, QueryParameters)} with a single "airedEpisode" query parameter.
     * <p/>
     * Note that an aired episode number might be associated with a specific season. If the series consists of more than one season this method will return the matching aired episodes
     * from all the seasons. Use {@link #queryEpisodesByAbsoluteNumber(long, long)} in order to query for a single episode.
     *
     * @see #queryEpisodesByAbsoluteNumber(long, long) queryEpisodesByAbsoluteNumber(seriesId, absoluteNumber)
     *
     * @param seriesId The TheTVDB series ID
     * @param airedEpisode The number of the aired episode to query for
     *
     * @return List of episodes for a specific season and aired episode number, mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Episode> queryEpisodesByAiredEpisode(long seriesId, long airedEpisode) throws APIException;

    /**
     * Returns a specific episode of a series, mapped as Java object. Results will be paginated.
     * This is a shortcut-method for {@link JSON#queryEpisodes(long, QueryParameters)} with a single "absoluteNumber" query parameter.
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
     * @return List of episodes for an absolute episode number, mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
     */
    List<Episode> queryEpisodesByAbsoluteNumber(long seriesId, long absoluteNumber) throws APIException;

    List<String> getAvailableEpisodeQueryParameters(long seriesId) throws APIException;

    SeriesSummary getSeriesEpisodesSummary(long seriesId) throws APIException;

    Series filterSeries(long seriesId, QueryParameters queryParameters) throws APIException;

    Series filterSeries(long seriesId, @Nonnull String filterKeys) throws APIException;

    List<String> getAvailableSeriesFilterParameters(long seriesId) throws APIException;

    ImageSummary getSeriesImagesSummary(long seriesId) throws APIException;

    List<Image> queryImages(long seriesId, QueryParameters queryParameters) throws APIException;

    List<Image> queryImages(long seriesId, String keyType, String resolution) throws APIException;

    List<Image> queryImages(long seriesId, String keyType, String resolution, String subKey) throws APIException;

    List<Image> queryImagesByKeyType(long seriesId, String keyType) throws APIException;

    List<Image> queryImagesByResolution(long seriesId, String resolution) throws APIException;

    List<Image> queryImagesBySubKey(long seriesId, String subKey) throws APIException;

    List<ImageQueryParameter> getAvailableImageQueryParameters(long seriesId) throws APIException;

    Map<Long, Long> queryLastUpdated(QueryParameters queryParameters) throws APIException;

    Map<Long, Long> queryLastUpdated(@Nonnull String fromTime) throws APIException;

    Map<Long, Long> queryLastUpdated(@Nonnull String fromTime, @Nonnull String toTime) throws APIException;

    List<String> getAvailableLastUpdatedQueryParameters() throws APIException;

    User getUser() throws APIException;

    List<String> getFavorites() throws APIException;

    List<String> deleteFromFavorites(long seriesId) throws APIException;

    List<String> addToFavorites(long seriesId) throws APIException;

    List<Rating> getRatings() throws APIException;

    List<Rating> queryRatings(QueryParameters queryParameters) throws APIException;

    List<Rating> queryRatingsByItemType(@Nonnull String itemType) throws APIException;

    List<String> getAvailableRatingsQueryParameters() throws APIException;

    void deleteFromRatings(@Nonnull String itemType, long itemId) throws APIException;

    List<Rating> addToRatings(@Nonnull String itemType, long itemId, long itemRating) throws APIException;

    JSON json();

    interface JSON {

        /**
         * Returns the full information for a given episode id as raw JSON.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Episodes/get_episodes_id">/episodes/{id}</a>
         *
         * @see TheTVDBApi#getEpisode(long) TheTVDBApi.getEpisode(episodeId)
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
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages">/languages</a>
         *
         * @see TheTVDBApi#getAvailableLanguages()
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
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages_id">/languages/{id}</a>
         *
         * @see TheTVDBApi#getLanguage(long) TheTVDBApi.getLanguage(languageId)
         *
         * @param languageId The ID of the language
         *
         * @return JSON object containing detailed language information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode getLanguage(long languageId) throws APIException;

        /**
         * Returns series search results based on the given query parameters as raw JSON. The result contains basic information of all series
         * matching the query parameters.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series">/search/series</a>
         *
         * @see TheTVDBApi#searchSeries(QueryParameters) TheTVDBApi.searchSeries(queryParameters)
         *
         * @param queryParameters Object containing key/value pairs of query search parameters. For a complete list of possible search parameters
         *                        see the API documentation or use {@link #getAvailableSeriesSearchParameters()}.
         *
         * @return JSON object containing the series search results
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode searchSeries(QueryParameters queryParameters) throws APIException;

        /**
         * Returns detailed information for a specific series as raw JSON.
         * <p/>
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id">/series/{id}</a>
         *
         * @see TheTVDBApi#getSeries(long) TheTVDBApi.getSeries(seriesId)
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
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series_params">/search/series/params</a>
         *
         * @see TheTVDBApi#getAvailableSeriesSearchParameters()
         * @see #searchSeries(QueryParameters) searchSeriesJSON(queryParams)
         * @see TheTVDBApi#searchSeries(QueryParameters) TheTVDBApi.searchSeries(queryParams)
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
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/head_series_id">/series/{id}</a>
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
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_actors">/series/{id}/actors</a>
         *
         * @see TheTVDBApi#getActors(long) TheTVDBApi.getActors(seriesId)
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
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes">/series/{id}/episodes</a>
         *
         * @see TheTVDBApi#getEpisodes(long, QueryParameters) TheTVDBApi.getEpisodes(seriesId, queryParameters)
         *
         * @param seriesId The TheTVDB series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible search parameters
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
         * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query">/series/{id}/episodes/query</a>
         *
         * @see TheTVDBApi#queryEpisodes(long, QueryParameters) TheTVDBApi.queryEpisodes(seriesId, queryParameters)
         *
         * @param seriesId The TheTVDB series ID
         * @param queryParameters Object containing key/value pairs of query parameters. For a complete list of possible search parameters
         *                        see the API documentation.
         *
         * @return JSON object containing a single result page of queried episode records
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc.
         */
        JsonNode queryEpisodes(long seriesId, QueryParameters queryParameters) throws APIException;

        JsonNode getAvailableEpisodeQueryParameters(long seriesId) throws APIException;

        JsonNode getSeriesEpisodesSummary(long seriesId) throws APIException;

        JsonNode filterSeries(long seriesId, QueryParameters queryParameters) throws APIException;

        JsonNode getAvailableSeriesFilterParameters(long seriesId) throws APIException;

        JsonNode getSeriesImagesSummary(long seriesId) throws APIException;

        JsonNode queryImages(long seriesId, QueryParameters queryParameters) throws APIException;

        JsonNode getAvailableImageQueryParameters(long seriesId) throws APIException;

        JsonNode queryLastUpdated(QueryParameters queryParameters) throws APIException;

        JsonNode getAvailableLastUpdatedQueryParameters() throws APIException;

        JsonNode getUser() throws APIException;

        JsonNode getFavorites() throws APIException;

        JsonNode deleteFromFavorites(long seriesId) throws APIException;

        JsonNode addToFavorites(long seriesId) throws APIException;

        JsonNode getRatings() throws APIException;

        JsonNode queryRatings(QueryParameters queryParameters) throws APIException;

        JsonNode getAvailableRatingsQueryParameters() throws APIException;

        JsonNode deleteFromRatings(@Nonnull String itemType, long itemId) throws APIException;

        JsonNode addToRatings(@Nonnull String itemType, long itemId, long itemRating) throws APIException;
    }
}

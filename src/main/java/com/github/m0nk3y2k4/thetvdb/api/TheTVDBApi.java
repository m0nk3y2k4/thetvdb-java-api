package com.github.m0nk3y2k4.thetvdb.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.model.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public interface TheTVDBApi {

    /**
     * Initializes the current API session by requesting a new token from the remote API. This token will be used for authentication of all requests that are sent to the
     * remote service by this API instance. The initialization will be performed based on the constructor parameters used to create this API instance. Actually this method
     * will do the same as {@link #login()}.
     *
     * @throws APIException
     */
    void init() throws APIException;

    /**
     * Sets the preferred language to be used for communication with the remote service. Some of the API calls might use this setting in order to only return results that
     * match the given language. If available, the data returned by the remote API will translated to the given language. The default language code is <b>"en"</b>. For a list
     * of supported languages see {@link #getAvailableLanguages()}.
     *
     * @see #getAvailableLanguages()
     *
     * @param languageCode
     */
    void setLanguage(String languageCode);

    /**
     * Initializes the current API session by requesting a new token from the remote API. This token will be used for authentication of all requests that are sent to the
     * remote service by this API instance. The initialization will be performed based on the constructor parameters used to create this API instance. It is recommended to
     * login/initialize the session before making the first API call. However, if an API call is made without proper initialization, an implicit login will be performed.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Authentication/post_login">/login</a>
     *
     * @throws APIException
     */
    void login() throws APIException;

    /**
     * Refreshes the current, valid JWT session token. This method can be used to extend the expiration date (24 hours) of the current session token without the need of a
     * complete new login. This method will be called automatically if an API call is made using an expired JWT session token.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Authentication/get_refresh_token">/refresh_token</a>
     *
     * @throws APIException
     */
    void refreshToken() throws APIException;

    /**
     * Returns the full information for a given episode id as raw JSON.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Episodes/get_episodes_id">/episods/{id}</a>
     *
     * @see #getEpisode(long) getEpisode(episodeId)
     *
     * @param episodeId The ID of the episode
     *
     * @return JSON object containing the full episode information
     *
     * @throws APIException
     */
    JsonNode getEpisodeJSON(long episodeId) throws APIException;

    /**
     * Returns the full information for a given episode id as mapped Java object.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Episodes/get_episodes_id">/episods/{id}</a>
     *
     * @see #getEpisodeJSON(long) getEpisodeJSON(episodeId)
     *
     * @param episodeId The ID of the episode
     *
     * @return Mapped Java object containing the full episode information based on the JSON data returned by the remote service
     *
     * @throws APIException
     */
    Episode getEpisode(long episodeId) throws APIException;

    /**
     * Returns an overview of all supported languages as raw JSON. These language abbreviations can be used to set the preferred language
     * for the communication with the remote service (see {@link #setLanguage(String)}.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages">/languages</a>
     *
     * @see #getAvailableLanguages()
     *
     * @return JSON object containing all languages that are supported by the remote service
     *
     * @throws APIException
     */
    JsonNode getAvailableLanguagesJSON() throws APIException;

    /**
     * Returns a list of all supported languages mapped as Java object. These language abbreviations can be used to set the preferred language
     * for the communication with the remote service (see {@link #setLanguage(String)}.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages">/languages</a>
     *
     * @see #getAvailableLanguagesJSON()
     *
     * @return List of available languages mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException
     */
    List<Language> getAvailableLanguages() throws APIException;

    /**
     * Returns further language information for a given language ID as raw JSON. The language abbreviation can be used to set the preferred language
     * for the communication with the remote service (see {@link #setLanguage(String)}.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages_id">/languages/{id}</a>
     *
     * @see #getLanguage(long) getLanguage(languageId)
     *
     * @param languageId The ID of the language
     *
     * @return JSON object containing detailed language information
     *
     * @throws APIException
     */
    JsonNode getLanguageJSON(long languageId) throws APIException;

    /**
     * Returns further language information for a given language ID mapped as Java object. The language abbreviation can be used to set the preferred language
     * for the communication with the remote service (see {@link #setLanguage(String)}.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Languages/get_languages_id">/languages/{id}</a>
     *
     * @see #getLanguageJSON(long) getLanguageJSON(languageId)
     *
     * @param languageId The ID of the language
     *
     * @return Mapped Java object containing detailed language information based on the JSON data returned by the remote service
     *
     * @throws APIException
     */
    Language getLanguage(long languageId) throws APIException;

    /**
     * Returns series search results based on the given query parameters as raw JSON. The result contains basic information of all series
     * matching the query parameters.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series">/search/series</a>
     *
     * @see #searchSeries(Map) searchSeries(queryParameters)
     *
     * @param queryParameters Map containing key/value pairs of query search parameters. For a complete list of possible search parameters
     *                        see the API documentation or use {@link #getAvailableSeriesSearchParameters()}.
     *
     * @return JSON object containing the series search results
     *
     * @throws APIException
     */
    JsonNode searchSeriesJSON(Map<String, String> queryParameters) throws APIException;

    /**
     * Returns a list of series search results based on the given query parameters mapped as Java object. The list contains basic information
     * of all series matching the query parameters.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series">/search/series</a>
     *
     * @see #searchSeriesJSON(Map) searchSeriesJSON(queryParameters)
     *
     * @param queryParameters Map containing key/value pairs of query search parameters. For a complete list of possible search parameters
     *                        see the API documentation or use {@link #getAvailableSeriesSearchParameters()}.
     *
     * @return List of series search results mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException
     */
    List<SeriesAbstract> searchSeries(Map<String, String> queryParameters) throws APIException;

    /**
     * Search for series by name. Returns a list of series search results mapped as Java object. The search results contain basic information
     * of all series matching the given name. This is a shortcut-method for {@link #searchSeries(Map)} with a single "name" query parameter.
     *
     * @param name The name of the series to search for
     *
     * @return List of series search results mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException
     */
    List<SeriesAbstract> searchSeriesByName(@Nonnull String name) throws APIException;

    /**
     * Search for series by IMDB-Id. Returns a list of series search results mapped as Java object. The search results contain basic information
     * of all series matching the given IMDB-Id. This is a shortcut-method for {@link #searchSeries(Map)} with a single "imdbId" query parameter.
     *
     * @param imdbId The IMDB-Id of the series to search for
     *
     * @return List of series search results mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException
     */
    List<SeriesAbstract> searchSeriesByImdbId(@Nonnull String imdbId) throws APIException;

    /**
     * Search for series by Zap2it-Id. Returns a list of series search results mapped as Java object. The search results contain basic information
     * of all series matching the given Zap2it-Id. This is a shortcut-method for {@link #searchSeries(Map)} with a single "zap2itId" query parameter.
     *
     * @param zap2itId The Zap2it-Id of the series to search for
     *
     * @return List of series search results mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException
     */
    List<SeriesAbstract> searchSeriesByZap2itId(@Nonnull String zap2itId) throws APIException;

    /**
     * Returns possible query parameters, which can be used to search for series, as raw JSON.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series_params">/search/series/params</a>
     *
     * @see #getAvailableSeriesSearchParameters()
     * @see #searchSeriesJSON(Map) searchSeriesJSON(queryParams)
     * @see #searchSeries(Map) searchSeries(queryParams)
     *
     * @return JSON object containing possible parameters to query by in the series search
     *
     * @throws APIException
     */
    JsonNode getAvailableSeriesSearchParametersJSON() throws APIException;

    /**
     * Returns possible query parameters, which can be used to search for series, mapped as Java object.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series_params">/search/series/params</a>
     *
     * @see #getAvailableSeriesSearchParametersJSON()
     * @see #searchSeriesJSON(Map) searchSeriesJSON(queryParams)
     * @see #searchSeries(Map) searchSeries(queryParams)
     *
     * @return List of possible parameters to query by in the series search
     *
     * @throws APIException
     */
    List<String> getAvailableSeriesSearchParameters() throws APIException;

    /**
     * Returns detailed information for a specific series as raw JSON.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id">/series/{id}</a>
     *
     * @see #getSeries(long) getSeries(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return JSON object containing detailed information for a specific series
     *
     * @throws APIException
     */
    JsonNode getSeriesJSON(long seriesId) throws APIException;

    /**
     * Returns detailed information for a specific series mapped as Java object.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id">/series/{id}</a>
     *
     * @see #getSeriesJSON(long) getSeriesJSON(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return Detailed information for a specific series mapped as Java object based on the JSON data returned by the remote service
     *
     * @throws APIException
     */
    Series getSeries(long seriesId) throws APIException;

    /**
     * Returns header information for a specific series as raw JSON. Good for getting the Last-Updated header to find out
     * when the series was last modified.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/head_series_id">/series/{id}</a>
     *
     * @see #getSeriesHeaderInformation(long) getSeriesHeaderInformation(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return Artificial JSON object based on the HTML header information returned by the remote service
     *
     * @throws APIException
     */
    JsonNode getSeriesHeaderInformationJSON(long seriesId) throws APIException;

    /**
     * Returns header information for a specific series as key/value pairs. Good for getting the Last-Updated header to find out
     * when the series was last modified.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/head_series_id">/series/{id}</a>
     *
     * @see #getSeriesHeaderInformationJSON(long) getSeriesHeaderInformationJSON(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return HTML header information returned by the remote service mapped as key/value pairs
     *
     * @throws APIException
     */
    Map<String, String> getSeriesHeaderInformation(long seriesId) throws APIException;

    /**
     * Returns the actors for a specific series as raw JSON.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_actors">/series/{id}/actors</a>
     *
     * @see #getActors(long) getActors(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return JSON object containing the actors for a specific series
     *
     * @throws APIException
     */
    JsonNode getActorsJSON(long seriesId) throws APIException;

    /**
     * Returns a list of actors for a specific series mapped as Java objects.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_actors">/series/{id}/actors</a>
     *
     * @see #getActorsJSON(long) getActorsJSON(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return List of actors mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException
     */
    List<Actor> getActors(long seriesId) throws APIException;

    /**
     * Returns basic information for all episodes of a specific series as raw JSON. Results will be paginated with 100 results per page.
     * Use <code>queryParameters</code> to select a specific result page.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes">/series/{id}/episodes</a>
     *
     * @see #getEpisodes(long, Map) getEpisodes(seriesId, queryParameters)
     *
     * @param seriesId The TheTVDB series ID
     * @param queryParameters Map containing key/value pairs of query parameters. For a complete list of possible search parameters
     *                        see the API documentation.
     *
     * @return JSON object containing a single result page of episodes
     *
     * @throws APIException
     */
    JsonNode getEpisodesJSON(long seriesId, Map<String, String> queryParameters) throws APIException;

    /**
     * Returns a list of basic information for all episodes of a specific series mapped as Java objects. Results will be paginated with 100 results per page.
     * Use <code>queryParameters</code> to select a specific result page.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes">/series/{id}/episodes</a>
     *
     * @see #getEpisodesJSON(long, Map) getEpisodesJSON(seriesId, queryParameters)
     *
     * @param seriesId The TheTVDB series ID
     * @param queryParameters Map containing key/value pairs of query parameters. For a complete list of possible search parameters
     *                        see the API documentation.
     *
     * @return List of basic episode information mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException
     */
    List<EpisodeAbstract> getEpisodes(long seriesId, Map<String, String> queryParameters) throws APIException;

    /**
     * Returns a list of basic information for the first 100 episodes of a specific series mapped as Java objects. Note that this method is deterministic and
     * will always return the <b>first</b> result page of the available episodes. This is a shortcut-method for {@link #getEpisodes(long, Map)} with an empty query
     * parameter map.
     *
     * @see #getEpisodes(long, long) getEpisodes(seriesId, page)
     *
     * @param seriesId The TheTVDB series ID
     *
     * @return List of basic episode information mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException
     */
    List<EpisodeAbstract> getEpisodes(long seriesId) throws APIException;

    /**
     * Returns a list of basic information for all episodes of a specific series mapped as Java objects. The result list will contain 100 episodes at most. For
     * series with more episodes use the <code>page</code> parameter to browse to a specific result page. This is a shortcut-method for
     * {@link #getEpisodes(long, Map)} with a single "page" query parameter.
     *
     * @see #getEpisodes(long) getEpisodes(seriesId)
     *
     * @param seriesId The TheTVDB series ID
     * @param page The result page to be returned
     *
     * @return List of basic episode information mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException
     */
    List<EpisodeAbstract> getEpisodes(long seriesId, long page) throws APIException;

    /**
     * Returns basic information for all matching episodes of a specific series as raw JSON. Results will be paginated. Use <code>queryParameters</code> to filter
     * for specific episodes or to select a specific result page.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query">/series/{id}/episodes/query</a>
     *
     * @see #queryEpisodes(long, Map) queryEpisodes(seriesId, queryParameters)
     *
     * @param seriesId The TheTVDB series ID
     * @param queryParameters Map containing key/value pairs of query parameters. For a complete list of possible search parameters
     *                        see the API documentation.
     *
     * @return JSON object containing a single result page of queried episode records
     *
     * @throws APIException
     */
    JsonNode queryEpisodesJSON(long seriesId, Map<String, String> queryParameters) throws APIException;

    /**
     * Returns a list of basic information for all matching episodes of a specific series mapped as Java objects. Results will be paginated. Note that this method
     * is deterministic and will always return the <b>first</b> result page of the available episodes.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query">/series/{id}/episodes/query</a>
     *
     * @see #queryEpisodesJSON(long, Map) queryEpisodesJSON(seriesId, queryParameters)
     *
     * @param seriesId The TheTVDB series ID
     * @param queryParameters Map containing key/value pairs of query parameters. For a complete list of possible search parameters
     *                        see the API documentation.
     *
     * @return List of basic episode information matching the query parameters, mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException
     */
    List<EpisodeAbstract> queryEpisodes(long seriesId, Map<String, String> queryParameters) throws APIException;

    /**
     * Returns a list of basic information for all episodes of a specific series and season mapped as Java objects. Results will be paginated. Note that this method
     * is deterministic and will always return the <b>first</b> result page of the available episodes. This is a shortcut-method for {@link #queryEpisodesJSON(long, Map)}
     * with a single "airedSeason" query parameter.
     *
     * @see #queryEpisodesByAiredSeason(long, long, long) queryEpisodesByAiredSeason(seriesId, airedSeason, page)
     *
     * @param seriesId The TheTVDB series ID
     * @param airedSeason The number of the aired season to query for
     *
     * @return List of basic episode information for a specific season, mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException
     */
    List<EpisodeAbstract> queryEpisodesByAiredSeason(long seriesId, long airedSeason) throws APIException;

    /**
     * Returns a list of basic information for all episodes of a specific series and season mapped as Java objects. Results will be paginated. For seasons with
     * a high number of episodes use the <code>page</code> parameter to browse to a specific result page. This is a shortcut-method for {@link #queryEpisodesJSON(long, Map)}
     * with a "airedSeason" and "page" query parameter.
     *
     * @see #queryEpisodesByAiredSeason(long, long) queryEpisodesByAiredSeason(seriesId, airedSeason)
     *
     * @param seriesId The TheTVDB series ID
     * @param airedSeason The number of the aired season to query for
     * @param page The result page to be returned
     *
     * @return List of basic episode information for a specific season, mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException
     */
    List<EpisodeAbstract> queryEpisodesByAiredSeason(long seriesId, long airedSeason, long page) throws APIException;

    /**
     * Returns a list of basic information for all episodes of a specific series, matching the <code>airedEpisode</code> parameter, mapped as Java objects. Results will be paginated.
     * This is a shortcut-method for {@link #queryEpisodesJSON(long, Map)} with a single "airedEpisode" query parameter.
     * <p/>
     * Note that an aired episode number might be associated with a specific season. If the series consists of more than one season this method will return the matching aired episodes
     * from all the seasons. Use {@link #queryEpisodesByAbsoluteNumber(long, long)} in order to query for a single episode.
     *
     * @see #queryEpisodesByAbsoluteNumber(long, long) queryEpisodesByAbsoluteNumber(seriesId, absoluteNumber)
     *
     * @param seriesId The TheTVDB series ID
     * @param airedEpisode The number of the aired episode to query for
     *
     * @return List of basic episode information for a specific season and aired episode number, mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException
     */
    List<EpisodeAbstract> queryEpisodesByAiredEpisode(long seriesId, long airedEpisode) throws APIException;

    /**
     * Returns basic information for a specific episode of a series, mapped as Java object. Results will be paginated.
     * This is a shortcut-method for {@link #queryEpisodesJSON(long, Map)} with a single "absoluteNumber" query parameter.
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
     * @return List of basic episode information for an absolute episode number, mapped as Java objects based on the JSON data returned by the remote service
     *
     * @throws APIException
     */
    List<EpisodeAbstract> queryEpisodesByAbsoluteNumber(long seriesId, long absoluteNumber) throws APIException;

    JsonNode getAvailableEpisodeQueryParametersJSON(long seriesId) throws APIException;

    List<String> getAvailableEpisodeQueryParameters(long seriesId) throws APIException;

    JsonNode getSeriesEpisodesSummaryJSON(long seriesId) throws APIException;

    SeriesSummary getSeriesEpisodesSummary(long seriesId) throws APIException;

    JsonNode filterSeriesJSON(long seriesId, Map<String, String> queryParameters) throws APIException;

    Series filterSeries(long seriesId, Map<String, String> queryParameters) throws APIException;

    Series filterSeries(long seriesId, @Nonnull String filterKeys) throws APIException;

    JsonNode getAvailableFilterParametersJSON(long seriesId) throws APIException;

    List<String> getAvailableFilterParameters(long seriesId) throws APIException;

    JsonNode getSeriesImagesSummaryJSON(long seriesId) throws APIException;

    ImageSummary getSeriesImagesSummary(long seriesId) throws APIException;

    JsonNode queryImagesJSON(long seriesId, Map<String, String> queryParameters) throws APIException;

    List<Image> queryImages(long seriesId, Map<String, String> queryParameters) throws APIException;

    List<Image> queryImages(long seriesId, String keyType, String resolution) throws APIException;

    List<Image> queryImages(long seriesId, String keyType, String resolution, String subKey) throws APIException;

    List<Image> queryImagesByKeyType(long seriesId, String keyType) throws APIException;

    List<Image> queryImagesByResolution(long seriesId, String resolution) throws APIException;

    List<Image> queryImagesBySubKey(long seriesId, String subKey) throws APIException;

    JsonNode getAvailableImageQueryParametersJSON(long seriesId) throws APIException;

    List<ImageQueryParameter> getAvailableImageQueryParameters(long seriesId) throws APIException;

    JsonNode queryLastUpdatedJSON(Map<String, String> queryParameters) throws APIException;

    Map<Long, Long> queryLastUpdated(Map<String, String> queryParameters) throws APIException;

    Map<Long, Long> queryLastUpdated(@Nonnull String fromTime) throws APIException;

    Map<Long, Long> queryLastUpdated(@Nonnull String fromTime, @Nonnull String toTime) throws APIException;

    JsonNode getAvailableLastUpdatedQueryParametersJSON() throws APIException;

    List<String> getAvailableLastUpdatedQueryParameters() throws APIException;

    JsonNode getUserJSON() throws APIException;

    User getUser() throws APIException;

    JsonNode getFavoritesJSON() throws APIException;

    List<String> getFavorites() throws APIException;

    JsonNode deleteFromFavoritesJSON(long seriesId) throws APIException;

    List<String> deleteFromFavorites(long seriesId) throws APIException;

    JsonNode addToFavoritesJSON(long seriesId) throws APIException;

    List<String> addToFavorites(long seriesId) throws APIException;

    JsonNode getRatingsJSON() throws APIException;

    List<Rating> getRatings() throws APIException;

    JsonNode queryRatingsJSON(Map<String, String> queryParameters) throws APIException;

    List<Rating> queryRatings(Map<String, String> queryParameters) throws APIException;

    List<Rating> queryRatingsByItemType(@Nonnull String itemType) throws APIException;

    JsonNode getAvailableRatingsQueryParametersJSON() throws APIException;

    List<String> getAvailableRatingsQueryParameters() throws APIException;

    JsonNode deleteFromRatingsJSON(@Nonnull String itemType, long itemId) throws APIException;

    void deleteFromRatings(@Nonnull String itemType, long itemId) throws APIException;

    JsonNode addToRatingsJSON(@Nonnull String itemType, long itemId, long itemRating) throws APIException;

    List<Rating> addToRatings(@Nonnull String itemType, long itemId, long itemRating) throws APIException;
}
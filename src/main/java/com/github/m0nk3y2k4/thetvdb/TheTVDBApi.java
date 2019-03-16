package com.github.m0nk3y2k4.thetvdb;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.resource.impl.AuthenticationAPI;
import com.github.m0nk3y2k4.thetvdb.resource.impl.EpisodesAPI;
import com.github.m0nk3y2k4.thetvdb.resource.impl.LanguagesAPI;
import com.github.m0nk3y2k4.thetvdb.resource.impl.SearchAPI;
import com.github.m0nk3y2k4.thetvdb.resource.impl.SeriesAPI;
import com.github.m0nk3y2k4.thetvdb.resource.impl.UpdatesAPI;
import com.github.m0nk3y2k4.thetvdb.resource.impl.UsersAPI;
import com.github.m0nk3y2k4.thetvdb.shared.model.Actor;
import com.github.m0nk3y2k4.thetvdb.shared.model.Episode;
import com.github.m0nk3y2k4.thetvdb.shared.model.EpisodeAbstract;
import com.github.m0nk3y2k4.thetvdb.shared.model.Image;
import com.github.m0nk3y2k4.thetvdb.shared.model.ImageQueryParameter;
import com.github.m0nk3y2k4.thetvdb.shared.model.ImageSummary;
import com.github.m0nk3y2k4.thetvdb.shared.model.Language;
import com.github.m0nk3y2k4.thetvdb.shared.model.Rating;
import com.github.m0nk3y2k4.thetvdb.shared.model.Series;
import com.github.m0nk3y2k4.thetvdb.shared.model.SeriesAbstract;
import com.github.m0nk3y2k4.thetvdb.shared.model.SeriesSummary;
import com.github.m0nk3y2k4.thetvdb.shared.model.User;
import com.github.m0nk3y2k4.thetvdb.shared.util.APIUtil;
import com.github.m0nk3y2k4.thetvdb.shared.util.JsonDeserializer;

/**
 * Objects of this class represent the public interface/entry point for the usage of TheTVDB-API. It provides methods for all sorts of API calls throughout the
 * different API routes. Responses will either be returned as raw, untouch JSON (as received by the remote REST service) or as mapped Java-Objects.
 *
 * @author m0nk3y
 */
public class TheTVDBApi {

    /** The actual connection to the remote API */
    private final APIConnection con;

    /**
     * Creates a new TheTVDBAPI instance. The given <code>apiKey</code> must be a valid <a href="https://www.thetvdb.com/member/api">TheTVDB API Key</a> which will
     * be used for remote service authentication. To authenticate and generate a new session token use the {@link #init()} or {@link #login()} methods right after
     * creating a new instance of this API.
     * <p/>
     * <b>NOTE:</b> Objects created with this constructor <u>can not</u> be used for calls to the remote API's <a href="https://api.thetvdb.com/swagger#/Users">/users</a>
     * routes. These call require extended authentication using an additional <code>userName</code> and <code>userKey</code>.
     *
     * @see #TheTVDBApi(String, String, String) TheTVDBApi(apiKey, userName, userKey)
     *
     * @param apiKey Valid TheTVDB API-Key
     */
    public TheTVDBApi(@Nonnull String apiKey) {
        if (!APIUtil.hasValue(apiKey)) {
            throw new IllegalArgumentException("APIKey must not be null or empty!");
        }

        this.con = new APIConnection(apiKey);
    }

    /**
     * Creates a new TheTVDBAPI instance. The given <code>apiKey</code> must be a valid <a href="https://www.thetvdb.com/member/api">TheTVDB API Key</a>. The <code>userName</code>
     * and <code>userKey</code> must refer to a registered TheTVDB user account. The given parameters will be used for the initial remote service authentication. To authenticate
     * and generate a new session token use the {@link #init()} or {@link #login()} methods right after creating a new instance of this API.
     *
     * @param apiKey Valid TheTVDB API-Key
     * @param userName Registered TheTVDB user name
     * @param userKey The password for user login
     */
    public TheTVDBApi(@Nonnull String apiKey, @Nonnull String userName, @Nonnull String userKey) {
        if (!APIUtil.hasValue(apiKey, userKey, userName)) {
            throw new IllegalArgumentException("APIKey/UserKey/UserName must not be null or empty!");
        }

        this.con = new APIConnection(apiKey, userKey, userName);
    }

    /**
     * Initializes the current API session by requesting a new token from the remote API. This token will be used for authentication of all requests that are sent to the
     * remote service by this API instance. The initialization will be performed based on the constructor parameters used to create this API instance. Actually this method
     * will do the same as {@link #login()}.
     *
     * @throws APIException
     */
    public void init() throws APIException {
        this.login();
    }

    /**
     * Sets the preferred language to be used for communication with the remote service. Some of the API calls might use this setting in order to only return results that
     * match the given language. If available, the data returned by the remote API will translated to the given language. The default language code is <b>"en"</b>. For a list
     * of supported languages see {@link #getAvailableLanguages()}.
     *
     * @see #getAvailableLanguages()
     *
     * @param languageCode
     */
    public void setLanguage(String languageCode) {
        con.setLanguage(languageCode);
    }

    /**
     * Initializes the current API session by requesting a new token from the remote API. This token will be used for authentication of all requests that are sent to the
     * remote service by this API instance. The initialization will be performed based on the constructor parameters used to create this API instance. It is recommended to
     * login/initialize the session before making the first API call. However, if an API call is made without proper initialization, an implicit login will be performed.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Authentication/post_login">/login</a>
     *
     * @throws APIException
     */
    public void login() throws APIException {
        AuthenticationAPI.login(con);
    }

    /**
     * Refreshes the current, valid JWT session token. This method can be used to extend the expiration date (24 hours) of the current session token without the need of a
     * complete new login. This method will be called automatically if an API call is made using an expired JWT session token.
     * <p/>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Authentication/get_refresh_token">/refresh_token</a>
     *
     * @throws APIException
     */
    public void refreshToken() throws APIException {
        AuthenticationAPI.refreshSession(con);
    }

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
    public JsonNode getEpisodeJSON(long episodeId) throws APIException {
        return EpisodesAPI.get(con, episodeId);
    }

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
    public Episode getEpisode(long episodeId) throws APIException {
        return JsonDeserializer.createEpisode(getEpisodeJSON(episodeId));
    }

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
    public JsonNode getAvailableLanguagesJSON() throws APIException {
        return LanguagesAPI.getAllAvailable(con);
    }

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
    public List<Language> getAvailableLanguages() throws APIException {
        return JsonDeserializer.createLanguageList(getAvailableLanguagesJSON());
    }

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
    public JsonNode getLanguageJSON(long languageId) throws APIException {
        return LanguagesAPI.get(con, languageId);
    }

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
    public Language getLanguage(long languageId) throws APIException {
        return JsonDeserializer.createLanguage(getLanguageJSON(languageId));
    }

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
    public JsonNode searchSeriesJSON(Map<String, String> queryParameters) throws APIException {
        return SearchAPI.series(con, queryParameters);
    }

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
    public List<SeriesAbstract> searchSeries(Map<String, String> queryParameters) throws APIException {
        return JsonDeserializer.createSeriesAbstract(searchSeriesJSON(queryParameters));
    }

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
    public List<SeriesAbstract> searchSeriesByName(@Nonnull String name) throws APIException {
        validateNotEmpty(name);
        return JsonDeserializer.createSeriesAbstract(searchSeriesJSON(Map.of(SearchAPI.QUERY_NAME, name)));
    }

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
    public List<SeriesAbstract> searchSeriesByImdbId(@Nonnull String imdbId) throws APIException {
        validateNotEmpty(imdbId);
        return JsonDeserializer.createSeriesAbstract(searchSeriesJSON(Map.of(SearchAPI.QUERY_IMDBID, imdbId)));
    }

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
    public List<SeriesAbstract> searchSeriesByZap2itId(@Nonnull String zap2itId) throws APIException {
        validateNotEmpty(zap2itId);
        return JsonDeserializer.createSeriesAbstract(searchSeriesJSON(Map.of(SearchAPI.QUERY_ZAP2ITID, zap2itId)));
    }

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
    public JsonNode getAvailableSeriesSearchParametersJSON() throws APIException {
        return SearchAPI.getAvailableSearchParameters(con);
    }

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
    public List<String> getAvailableSeriesSearchParameters() throws APIException {
        return JsonDeserializer.createQueryParameters(getAvailableSeriesSearchParametersJSON());
    }

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
    public JsonNode getSeriesJSON(long seriesId) throws APIException {
        return SeriesAPI.get(con, seriesId);
    }

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
    public Series getSeries(long seriesId) throws APIException {
        return JsonDeserializer.createSeries(getSeriesJSON(seriesId));
    }

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
    public JsonNode getSeriesHeaderInformationJSON(long seriesId) throws APIException {
        return SeriesAPI.getHead(con, seriesId);
    }

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
    public Map<String, String> getSeriesHeaderInformation(long seriesId) throws APIException {
        return JsonDeserializer.createSeriesHeader(getSeriesHeaderInformationJSON(seriesId));
    }

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
    public JsonNode getActorsJSON(long seriesId) throws APIException {
        return SeriesAPI.getActors(con, seriesId);
    }

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
    public List<Actor> getActors(long seriesId) throws APIException {
        return JsonDeserializer.createActors(getActorsJSON(seriesId));
    }

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
    public JsonNode getEpisodesJSON(long seriesId, Map<String, String> queryParameters) throws APIException {
        return SeriesAPI.getEpisodes(con, seriesId, queryParameters);
    }

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
    public List<EpisodeAbstract> getEpisodes(long seriesId, Map<String, String> queryParameters) throws APIException {
        return JsonDeserializer.createEpisodeAbstract(getEpisodesJSON(seriesId, queryParameters));
    }

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
    public List<EpisodeAbstract> getEpisodes(long seriesId) throws APIException {
        return JsonDeserializer.createEpisodeAbstract(getEpisodesJSON(seriesId, Collections.emptyMap()));
    }

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
    public List<EpisodeAbstract> getEpisodes(long seriesId, long page) throws APIException {
        return JsonDeserializer.createEpisodeAbstract(getEpisodesJSON(seriesId, Map.of(SeriesAPI.QUERY_PAGE, String.valueOf(page))));
    }

    public JsonNode queryEpisodesJSON(long seriesId, Map<String, String> queryParameters) throws APIException {
        return SeriesAPI.queryEpisodes(con, seriesId, queryParameters);
    }

    public List<EpisodeAbstract> queryEpisodes(long seriesId, Map<String, String> queryParameters) throws APIException {
        return JsonDeserializer.createEpisodeAbstract(queryEpisodesJSON(seriesId, queryParameters));
    }

    public List<EpisodeAbstract> queryEpisodesByAiredSeason(long seriesId, long airedSeason) throws APIException {
        return JsonDeserializer.createEpisodeAbstract(queryEpisodesJSON(seriesId, Map.of(SeriesAPI.QUERY_AIREDSEASON, String.valueOf(airedSeason))));
    }

    public List<EpisodeAbstract> queryEpisodesByAiredSeason(long seriesId, long airedSeason, long page) throws APIException {
        return JsonDeserializer.createEpisodeAbstract(queryEpisodesJSON(seriesId, Map.of(SeriesAPI.QUERY_AIREDSEASON, String.valueOf(airedSeason), SeriesAPI.QUERY_PAGE, String.valueOf(page))));
    }

    public List<EpisodeAbstract> queryEpisodesByAiredEpisode(long seriesId, long airedEpisode) throws APIException {
        return JsonDeserializer.createEpisodeAbstract(queryEpisodesJSON(seriesId, Map.of(SeriesAPI.QUERY_AIREDEPISODE, String.valueOf(airedEpisode))));
    }

    public List<EpisodeAbstract> queryEpisodesByAbsoluteNumber(long seriesId, long absoluteNumber) throws APIException {
        return JsonDeserializer.createEpisodeAbstract(queryEpisodesJSON(seriesId, Map.of(SeriesAPI.QUERY_ABSOLUTENUMBER, String.valueOf(absoluteNumber))));
    }

    public JsonNode getAvailableEpisodeQueryParametersJSON(long seriesId) throws APIException {
        return SeriesAPI.getEpisodesQueryParams(con, seriesId);
    }

    public List<String> getAvailableEpisodeQueryParameters(long seriesId) throws APIException {
        return JsonDeserializer.createQueryParameters(getAvailableEpisodeQueryParametersJSON(seriesId));
    }

    public JsonNode getSeriesEpisodesSummaryJSON(long seriesId) throws APIException {
        return SeriesAPI.getEpisodesSummary(con, seriesId);
    }

    public SeriesSummary getSeriesEpisodesSummary(long seriesId) throws APIException {
        return JsonDeserializer.createSeriesSummary(getSeriesEpisodesSummaryJSON(seriesId));
    }

    public JsonNode filterSeriesJSON(long seriesId, Map<String, String> queryParameters) throws APIException {
        return SeriesAPI.filter(con, seriesId, queryParameters);
    }

    public Series filterSeries(long seriesId, Map<String, String> queryParameters) throws APIException {
        return JsonDeserializer.createSeries(filterSeriesJSON(seriesId, queryParameters));
    }

    public Series filterSeries(long seriesId, @Nonnull String filterKeys) throws APIException {
        validateNotEmpty(filterKeys);
        return JsonDeserializer.createSeries(filterSeriesJSON(seriesId, Map.of(SeriesAPI.QUERY_KEYS, filterKeys)));
    }

    public JsonNode getAvailableFilterParametersJSON(long seriesId) throws APIException {
        return SeriesAPI.getFilterParams(con, seriesId);
    }

    public List<String> getAvailableFilterParameters(long seriesId) throws APIException {
        return JsonDeserializer.createQueryParameters(getAvailableFilterParametersJSON(seriesId));
    }

    public JsonNode getSeriesImagesSummaryJSON(long seriesId) throws APIException {
        return SeriesAPI.getImages(con, seriesId);
    }

    public ImageSummary getSeriesImagesSummary(long seriesId) throws APIException {
        return JsonDeserializer.createSeriesImageSummary(getSeriesImagesSummaryJSON(seriesId));
    }

    public JsonNode queryImagesJSON(long seriesId, Map<String, String> queryParameters) throws APIException {
        return SeriesAPI.queryImages(con, seriesId, queryParameters);
    }

    public List<Image> queryImages(long seriesId, Map<String, String> queryParameters) throws APIException {
        return JsonDeserializer.createImages(queryImagesJSON(seriesId, queryParameters));
    }

    public List<Image> queryImages(long seriesId, String keyType, String resolution) throws APIException {
        validateNotEmpty(keyType, resolution);
        return JsonDeserializer.createImages(queryImagesJSON(seriesId, Map.of(SeriesAPI.QUERY_KEYTYPE, keyType, SeriesAPI.QUERY_RESOLUTION, resolution)));
    }

    public List<Image> queryImages(long seriesId, String keyType, String resolution, String subKey) throws APIException {
        validateNotEmpty(keyType, resolution, subKey);
        return JsonDeserializer.createImages(queryImagesJSON(seriesId, Map.of(SeriesAPI.QUERY_KEYTYPE, keyType, SeriesAPI.QUERY_RESOLUTION, resolution, SeriesAPI.QUERY_SUBKEY, subKey)));
    }

    public List<Image> queryImagesByKeyType(long seriesId, String keyType) throws APIException {
        validateNotEmpty(keyType);
        return JsonDeserializer.createImages(queryImagesJSON(seriesId, Map.of(SeriesAPI.QUERY_KEYTYPE, keyType)));
    }

    public List<Image> queryImagesByResolution(long seriesId, String resolution) throws APIException {
        validateNotEmpty(resolution);
        return JsonDeserializer.createImages(queryImagesJSON(seriesId, Map.of(SeriesAPI.QUERY_RESOLUTION, resolution)));
    }

    public List<Image> queryImagesBySubKey(long seriesId, String subKey) throws APIException {
        validateNotEmpty(subKey);
        return JsonDeserializer.createImages(queryImagesJSON(seriesId, Map.of(SeriesAPI.QUERY_SUBKEY, subKey)));
    }

    public JsonNode getAvailableImageQueryParametersJSON(long seriesId) throws APIException {
        return SeriesAPI.getImagesQueryParams(con, seriesId);
    }

    public List<ImageQueryParameter> getAvailableImageQueryParameters(long seriesId) throws APIException {
        return JsonDeserializer.createQueryParameterList(getAvailableImageQueryParametersJSON(seriesId));
    }

    public JsonNode queryLastUpdatedJSON(Map<String, String> queryParameters) throws APIException {
        return UpdatesAPI.query(con, queryParameters);
    }

    public Map<Long, Long> queryLastUpdated(Map<String, String> queryParameters) throws APIException {
        return JsonDeserializer.createUpdated(queryLastUpdatedJSON(queryParameters));
    }

    public Map<Long, Long> queryLastUpdated(@Nonnull String fromTime) throws APIException {
        validateNotEmpty(fromTime);
        return JsonDeserializer.createUpdated(queryLastUpdatedJSON(Map.of(UpdatesAPI.QUERY_FROMTIME, fromTime)));
    }

    public Map<Long, Long> queryLastUpdated(@Nonnull String fromTime, @Nonnull String toTime) throws APIException {
        validateNotEmpty(fromTime, toTime);
        return JsonDeserializer.createUpdated(queryLastUpdatedJSON(Map.of(UpdatesAPI.QUERY_FROMTIME, fromTime, UpdatesAPI.QUERY_TOTIME, toTime)));
    }

    public JsonNode getAvailableLastUpdatedQueryParametersJSON() throws APIException {
        return UpdatesAPI.getQueryParams(con);
    }

    public List<String> getAvailableLastUpdatedQueryParameters() throws APIException {
        return JsonDeserializer.createQueryParameters(getAvailableLastUpdatedQueryParametersJSON());
    }

    public JsonNode getUserJSON() throws APIException {
        validateUserAuthentication();
        return UsersAPI.get(con);
    }

    public User getUser() throws APIException {
        return JsonDeserializer.createUser(getUserJSON());
    }

    public JsonNode getFavoritesJSON() throws APIException {
        validateUserAuthentication();
        return UsersAPI.getFavorites(con);
    }

    public List<String> getFavorites() throws APIException {
        return JsonDeserializer.createFavorites(getFavoritesJSON());
    }

    public JsonNode deleteFromFavoritesJSON(long seriesId) throws APIException {
        validateUserAuthentication();
        return UsersAPI.deleteFromFavorites(con, seriesId);
    }

    public List<String> deleteFromFavorites(long seriesId) throws APIException {
        return JsonDeserializer.createFavorites(deleteFromFavoritesJSON(seriesId));
    }

    public JsonNode addToFavoritesJSON(long seriesId) throws APIException {
        validateUserAuthentication();
        return UsersAPI.addToFavorites(con, seriesId);
    }

    public List<String> addToFavorites(long seriesId) throws APIException {
        return JsonDeserializer.createFavorites(addToFavoritesJSON(seriesId));
    }

    public JsonNode getRatingsJSON() throws APIException {
        validateUserAuthentication();
        return UsersAPI.getRatings(con);
    }

    public List<Rating> getRatings() throws APIException {
        return JsonDeserializer.createRatings(getRatingsJSON());
    }

    public JsonNode queryRatingsJSON(Map<String, String> queryParameters) throws APIException {
        validateUserAuthentication();
        return UsersAPI.queryRatings(con, queryParameters);
    }

    public List<Rating> queryRatings(Map<String, String> queryParameters) throws APIException {
        return JsonDeserializer.createRatings(queryRatingsJSON(queryParameters));
    }

    public List<Rating> queryRatingsByItemType(@Nonnull String itemType) throws APIException {
        validateNotEmpty(itemType);
        return JsonDeserializer.createRatings(queryRatingsJSON(Map.of(UsersAPI.QUERY_ITEMTYPE, itemType)));
    }

    public JsonNode getAvailableRatingsQueryParametersJSON() throws APIException {
        validateUserAuthentication();
        return UsersAPI.getRatingsQueryParams(con);
    }

    public List<String> getAvailableRatingsQueryParameters() throws APIException {
        return JsonDeserializer.createQueryParameters(getAvailableRatingsQueryParametersJSON());
    }

    public JsonNode deleteFromRatingsJSON(@Nonnull String itemType, long itemId) throws APIException {
        validateNotEmpty(itemType);
        validateUserAuthentication();
        return UsersAPI.deleteFromRatings(con, itemType, itemId);
    }

    public void deleteFromRatings(@Nonnull String itemType, long itemId) throws APIException {
        deleteFromRatingsJSON(itemType, itemId);
    }

    public JsonNode addToRatingsJSON(@Nonnull String itemType, long itemId, long itemRating) throws APIException {
        validateNotEmpty(itemType);
        validateUserAuthentication();
        return UsersAPI.addToRatings(con, itemType, itemId, itemRating);
    }

    public List<Rating> addToRatings(@Nonnull String itemType, long itemId, long itemRating) throws APIException {
        return JsonDeserializer.createRatings(addToRatingsJSON(itemType, itemId, itemRating));
    }

    private void validateNotEmpty(String... params) {
        if (!APIUtil.hasValue(params)) {
            throw new IllegalArgumentException("Method parameters must not be null or empty!");
        }
    }

    private void validateUserAuthentication() {
        if (!con.userAuthentication()) {
            throw new IllegalArgumentException("API call requires userKey/userName to be set!");
        }
    }
}
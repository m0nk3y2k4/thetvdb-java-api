package com.github.m0nk3y2k4.thetvdb.internal.api.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory;
import com.github.m0nk3y2k4.thetvdb.api.Proxy;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Actor;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Episode;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Image;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ImageQueryParameter;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ImageSummary;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Language;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Rating;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Series;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesSearchResult;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesSummary;
import com.github.m0nk3y2k4.thetvdb.api.model.data.User;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APIPreconditionException;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.AuthenticationAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.EpisodesAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.LanguagesAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SearchAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.UpdatesAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.UsersAPI;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;
import com.github.m0nk3y2k4.thetvdb.internal.util.json.JsonDeserializer;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Implementation of the {@link TheTVDBApi} API layout. It provides methods for all sorts of API calls throughout the different API routes. Responses will
 * be returned as mapped Java DTO objects.
 */
public class TheTVDBApiImpl implements TheTVDBApi {

    /** Wrapper API: Consolidates TheTVDBApi calls which return raw JSON */
    private final JSON jsonApi = new JSONApi();

    /** Wrapper API: Consolidates TheTVDBApi calls which return extended response information */
    private final Extended extendedApi = new ExtendedApi();

    /** The actual connection to the remote API */
    private final APIConnection con;

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid <a href="https://www.thetvdb.com/member/api">TheTVDB.com API Key</a>
     * as it will be used for remote service authentication. To authenticate and generate a new session token use the {@link #init()} or {@link #login()}
     * method right after creating a new instance of this API.
     * <p><br>
     * <b>NOTE:</b> Objects created with this constructor <u>can not</u> be used for calls to the remote API's <a href="https://api.thetvdb.com/swagger#/Users">/users</a>
     * routes. These calls require extended authentication using an additional <em>{@code userKey}</em> and <em>{@code userName}</em>.
     *
     * @see #TheTVDBApiImpl(String, String, String) TheTVDBApiImpl(apiKey, userKey, userName)
     *
     * @param apiKey Valid <i>TheTVDB.com</i> API-Key
     */
    public TheTVDBApiImpl(@Nonnull String apiKey) {
        this.con = new APIConnection(apiKey);
    }

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid <a href="https://www.thetvdb.com/member/api">TheTVDB.com API Key</a>
     * as it will be used for remote service authentication. To authenticate and generate a new session token use the {@link #init()} or {@link #login()}
     * method right after creating a new instance of this API. All communication to the remote API will be forwarded to the given <em>{@code proxy}</em>.
     * <p><br>
     * <b>NOTE:</b> Objects created with this constructor <u>can not</u> be used for calls to the remote API's <a href="https://api.thetvdb.com/swagger#/Users">/users</a>
     * routes. These calls require extended authentication using an additional <em>{@code userKey}</em> and <em>{@code userName}</em>.
     *
     * @see #TheTVDBApiImpl(String, String, String, Proxy) TheTVDBApiImpl(apiKey, userKey, userName, proxy)
     *
     * @param apiKey Valid <i>TheTVDB.com</i> API-Key
     * @param proxy The proxy service to be used for remote API communication
     */
    public TheTVDBApiImpl(@Nonnull String apiKey, @Nonnull Proxy proxy) {
        Parameters.validateNotNull(proxy, "Proxy must not be NULL");
        this.con = new APIConnection(apiKey, () -> new RemoteAPI.Builder().from(proxy).build());
    }

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid <a href="https://www.thetvdb.com/member/api">TheTVDB.com API Key</a>.
     * The <em>{@code userKey}</em> and <em>{@code userName}</em> must refer to a registered <i>TheTVDB.com</i> user account. The given parameters will be used for the
     * initial remote service authentication. To authenticate and generate a new session token use the {@link #init()} or {@link #login()} method right after
     * creating a new instance of this API.
     *
     * @param apiKey Valid <i>TheTVDB.com</i> API-Key
     * @param userKey Valid <i>TheTVDB.com</i> user key (also referred to as "Unique ID")
     * @param userName Registered <i>TheTVDB.com</i> user name
     */
    public TheTVDBApiImpl(@Nonnull String apiKey, @Nonnull String userKey, @Nonnull String userName) {
        this.con = new APIConnection(apiKey, userKey, userName);
    }

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid <a href="https://www.thetvdb.com/member/api">TheTVDB.com API Key</a>.
     * The <em>{@code userKey}</em> and <em>{@code userName}</em> must refer to a registered <i>TheTVDB.com</i> user account. The given parameters will be used for the
     * initial remote service authentication. To authenticate and generate a new session token use the {@link #init()} or {@link #login()} method right after
     * creating a new instance of this API. All communication to the remote API will be forwarded to the given <em>{@code proxy}</em>.
     *
     * @param apiKey Valid <i>TheTVDB.com</i> API-Key
     * @param userKey Valid <i>TheTVDB.com</i> user key (also referred to as "Unique ID")
     * @param userName Registered <i>TheTVDB.com</i> user name
     * @param proxy The proxy service to be used for remote API communication
     */
    public TheTVDBApiImpl(@Nonnull String apiKey, @Nonnull String userKey, @Nonnull String userName, @Nonnull Proxy proxy) {
        Parameters.validateNotNull(proxy, "Proxy must not be NULL");
        this.con = new APIConnection(apiKey, userKey, userName, () -> new RemoteAPI.Builder().from(proxy).build());
    }

    @Override
    public void init() throws APIException {
        this.login();
    }

    @Override
    public void init(@Nonnull String token) throws APIException {
        con.setToken(token);
    }

    @Override
    public Optional<String> getToken() {
        return con.getToken();
    }

    @Override
    public void setLanguage(String languageCode) {
        con.setLanguage(languageCode);
    }

    @Override
    public void login() throws APIException {
        AuthenticationAPI.login(con);
    }

    @Override
    public void refreshToken() throws APIException {
        AuthenticationAPI.refreshSession(con);
    }

    @Override
    public Episode getEpisode(long episodeId) throws APIException {
        return extended().getEpisode(episodeId).getData();
    }

    @Override
    public List<Language> getAvailableLanguages() throws APIException {
        return extended().getAvailableLanguages().getData();
    }

    @Override
    public Language getLanguage(long languageId) throws APIException {
        return extended().getLanguage(languageId).getData();
    }

    @Override
    public List<SeriesSearchResult> searchSeries(QueryParameters queryParameters) throws APIException {
        return extended().searchSeries(queryParameters).getData();
    }

    @Override
    public List<SeriesSearchResult> searchSeriesByName(@Nonnull String name) throws APIException {
        validateNotEmpty(name);
        return searchSeries(query(Map.of(Query.Search.NAME, name)));
    }

    @Override
    public List<SeriesSearchResult> searchSeriesByImdbId(@Nonnull String imdbId) throws APIException {
        validateNotEmpty(imdbId);
        return searchSeries(query(Map.of(Query.Search.IMDBID, imdbId)));
    }

    @Override
    public List<SeriesSearchResult> searchSeriesByZap2itId(@Nonnull String zap2itId) throws APIException {
        validateNotEmpty(zap2itId);
        return searchSeries(query(Map.of(Query.Search.ZAP2ITID, zap2itId)));
    }

    @Override
    public List<String> getAvailableSeriesSearchParameters() throws APIException {
        return extended().getAvailableSeriesSearchParameters().getData();
    }

    @Override
    public Series getSeries(long seriesId) throws APIException {
        return extended().getSeries(seriesId).getData();
    }

    @Override
    public Map<String, String> getSeriesHeaderInformation(long seriesId) throws APIException {
        return extended().getSeriesHeaderInformation(seriesId).getData();
    }

    @Override
    public List<Actor> getActors(long seriesId) throws APIException {
        return extended().getActors(seriesId).getData();
    }

    @Override
    public List<Episode> getEpisodes(long seriesId, QueryParameters queryParameters) throws APIException {
        return extended().getEpisodes(seriesId, queryParameters).getData();
    }

    @Override
    public List<Episode> getEpisodes(long seriesId) throws APIException {
        return getEpisodes(seriesId, emptyQuery());
    }

    @Override
    public List<Episode> getEpisodes(long seriesId, long page) throws APIException {
        return getEpisodes(seriesId, query(Map.of(Query.Series.PAGE, String.valueOf(page))));
    }

    @Override
    public List<Episode> queryEpisodes(long seriesId, QueryParameters queryParameters) throws APIException {
        return extended().queryEpisodes(seriesId, queryParameters).getData();
    }

    @Override
    public List<Episode> queryEpisodesByAiredSeason(long seriesId, long airedSeason) throws APIException {
        return queryEpisodes(seriesId, query(Map.of(Query.Series.AIREDSEASON, String.valueOf(airedSeason))));
    }

    @Override
    public List<Episode> queryEpisodesByAiredSeason(long seriesId, long airedSeason, long page) throws APIException {
        return queryEpisodes(seriesId, query(Map.of(Query.Series.AIREDSEASON, String.valueOf(airedSeason), Query.Series.PAGE, String.valueOf(page))));
    }

    @Override
    public List<Episode> queryEpisodesByAiredEpisode(long seriesId, long airedEpisode) throws APIException {
        return queryEpisodes(seriesId, query(Map.of(Query.Series.AIREDEPISODE, String.valueOf(airedEpisode))));
    }

    @Override
    public List<Episode> queryEpisodesByAbsoluteNumber(long seriesId, long absoluteNumber) throws APIException {
        return queryEpisodes(seriesId, query(Map.of(Query.Series.ABSOLUTENUMBER, String.valueOf(absoluteNumber))));
    }

    @Override
    public List<String> getAvailableEpisodeQueryParameters(long seriesId) throws APIException {
        return extended().getAvailableEpisodeQueryParameters(seriesId).getData();
    }

    @Override
    public SeriesSummary getSeriesEpisodesSummary(long seriesId) throws APIException {
        return extended().getSeriesEpisodesSummary(seriesId).getData();
    }

    @Override
    public Series filterSeries(long seriesId, QueryParameters queryParameters) throws APIException {
        return extended().filterSeries(seriesId, queryParameters).getData();
    }

    @Override
    public Series filterSeries(long seriesId, @Nonnull String filterKeys) throws APIException {
        validateNotEmpty(filterKeys);
        return filterSeries(seriesId, query(Map.of(Query.Series.KEYS, filterKeys)));
    }

    @Override
    public List<String> getAvailableSeriesFilterParameters(long seriesId) throws APIException {
        return extended().getAvailableSeriesFilterParameters(seriesId).getData();
    }

    @Override
    public ImageSummary getSeriesImagesSummary(long seriesId) throws APIException {
        return extended().getSeriesImagesSummary(seriesId).getData();
    }

    @Override
    public List<Image> queryImages(long seriesId, QueryParameters queryParameters) throws APIException {
        return extended().queryImages(seriesId, queryParameters).getData();
    }

    @Override
    public List<Image> queryImages(long seriesId, @Nonnull String keyType, @Nonnull String resolution) throws APIException {
        validateNotEmpty(keyType, resolution);
        return queryImages(seriesId, query(Map.of(Query.Series.KEYTYPE, keyType, Query.Series.RESOLUTION, resolution)));
    }

    @Override
    public List<Image> queryImages(long seriesId, @Nonnull String keyType, @Nonnull String resolution, @Nonnull String subKey) throws APIException {
        validateNotEmpty(keyType, resolution, subKey);
        return queryImages(seriesId, query(Map.of(Query.Series.KEYTYPE, keyType, Query.Series.RESOLUTION, resolution, Query.Series.SUBKEY, subKey)));
    }

    @Override
    public List<Image> queryImagesByKeyType(long seriesId, @Nonnull String keyType) throws APIException {
        validateNotEmpty(keyType);
        return queryImages(seriesId, query(Map.of(Query.Series.KEYTYPE, keyType)));
    }

    @Override
    public List<Image> queryImagesByResolution(long seriesId, @Nonnull String resolution) throws APIException {
        validateNotEmpty(resolution);
        return queryImages(seriesId, query(Map.of(Query.Series.RESOLUTION, resolution)));
    }

    @Override
    public List<Image> queryImagesBySubKey(long seriesId, @Nonnull String subKey) throws APIException {
        validateNotEmpty(subKey);
        return queryImages(seriesId, query(Map.of(Query.Series.SUBKEY, subKey)));
    }

    @Override
    public List<ImageQueryParameter> getAvailableImageQueryParameters(long seriesId) throws APIException {
        return extended().getAvailableImageQueryParameters(seriesId).getData();
    }

    @Override
    public Map<Long, Long> queryLastUpdated(QueryParameters queryParameters) throws APIException {
        return extended().queryLastUpdated(queryParameters).getData();
    }

    @Override
    public Map<Long, Long> queryLastUpdated(@Nonnull String fromTime) throws APIException {
        validateNotEmpty(fromTime);
        return queryLastUpdated(query(Map.of(Query.Updates.FROMTIME, fromTime)));
    }

    @Override
    public Map<Long, Long> queryLastUpdated(@Nonnull String fromTime, @Nonnull String toTime) throws APIException {
        validateNotEmpty(fromTime, toTime);
        return queryLastUpdated(query(Map.of(Query.Updates.FROMTIME, fromTime, Query.Updates.TOTIME, toTime)));
    }

    @Override
    public List<String> getAvailableLastUpdatedQueryParameters() throws APIException {
        return extended().getAvailableLastUpdatedQueryParameters().getData();
    }

    @Override
    public User getUser() throws APIException {
        return extended().getUser().getData();
    }

    @Override
    public List<String> getFavorites() throws APIException {
        return extended().getFavorites().getData();
    }

    @Override
    public List<String> deleteFromFavorites(long seriesId) throws APIException {
        return extended().deleteFromFavorites(seriesId).getData();
    }

    @Override
    public List<String> addToFavorites(long seriesId) throws APIException {
        return extended().addToFavorites(seriesId).getData();
    }

    @Override
    public List<Rating> getRatings() throws APIException {
        return extended().getRatings().getData();
    }

    @Override
    public List<Rating> queryRatings(QueryParameters queryParameters) throws APIException {
        return extended().queryRatings(queryParameters).getData();
    }

    @Override
    public List<Rating> queryRatingsByItemType(@Nonnull String itemType) throws APIException {
        validateNotEmpty(itemType);
        return queryRatings(query(Map.of(Query.Users.ITEMTYPE, itemType)));
    }

    @Override
    public List<String> getAvailableRatingsQueryParameters() throws APIException {
        return extended().getAvailableRatingsQueryParameters().getData();
    }

    @Override
    public void deleteFromRatings(@Nonnull String itemType, long itemId) throws APIException {
        extended().deleteFromRatings(itemType, itemId);
    }

    @Override
    public List<Rating> addToRatings(@Nonnull String itemType, long itemId, long itemRating) throws APIException {
        return extended().addToRatings(itemType, itemId, itemRating).getData();
    }

    @Override
    public JSON json() {
        return jsonApi;
    }

    @Override
    public Extended extended() {
        return extendedApi;
    }

    /**
     * Implementation of the {@link TheTVDBApi.JSON} API layout. It provides methods for all sorts of API calls throughout the different API routes. Responses will
     * be returned as raw, untouched JSON as it has been received by the remote REST service.
     */
    private class JSONApi implements JSON {

        /**
         * Creates a new instance of the {@link TheTVDBApi.JSON} API layout
         */
        private JSONApi() {}

        @Override
        public JsonNode getEpisode(long episodeId) throws APIException {
            return EpisodesAPI.get(con, episodeId);
        }

        @Override
        public JsonNode getAvailableLanguages() throws APIException {
            return LanguagesAPI.getAllAvailable(con);
        }

        @Override
        public JsonNode getLanguage(long languageId) throws APIException {
            return LanguagesAPI.get(con, languageId);
        }

        @Override
        public JsonNode searchSeries(QueryParameters queryParameters) throws APIException {
            return SearchAPI.series(con, queryParameters);
        }

        @Override
        public JsonNode getAvailableSeriesSearchParameters() throws APIException {
            return SearchAPI.getAvailableSearchParameters(con);
        }

        @Override
        public JsonNode getSeries(long seriesId) throws APIException {
            return SeriesAPI.get(con, seriesId);
        }

        @Override
        public JsonNode getSeriesHeaderInformation(long seriesId) throws APIException {
            return SeriesAPI.getHead(con, seriesId);
        }

        @Override
        public JsonNode getActors(long seriesId) throws APIException {
            return SeriesAPI.getActors(con, seriesId);
        }

        @Override
        public JsonNode getEpisodes(long seriesId, QueryParameters queryParameters) throws APIException {
            return SeriesAPI.getEpisodes(con, seriesId, queryParameters);
        }

        @Override
        public JsonNode queryEpisodes(long seriesId, QueryParameters queryParameters) throws APIException {
            return SeriesAPI.queryEpisodes(con, seriesId, queryParameters);
        }

        @Override
        public JsonNode getAvailableEpisodeQueryParameters(long seriesId) throws APIException {
            return SeriesAPI.getEpisodesQueryParams(con, seriesId);
        }

        @Override
        public JsonNode getSeriesEpisodesSummary(long seriesId) throws APIException {
            return SeriesAPI.getEpisodesSummary(con, seriesId);
        }

        @Override
        public JsonNode filterSeries(long seriesId, QueryParameters queryParameters) throws APIException {
            return SeriesAPI.filter(con, seriesId, queryParameters);
        }

        @Override
        public JsonNode getAvailableSeriesFilterParameters(long seriesId) throws APIException {
            return SeriesAPI.getFilterParams(con, seriesId);
        }

        @Override
        public JsonNode getSeriesImagesSummary(long seriesId) throws APIException {
            return SeriesAPI.getImages(con, seriesId);
        }

        @Override
        public JsonNode queryImages(long seriesId, QueryParameters queryParameters) throws APIException {
            return SeriesAPI.queryImages(con, seriesId, queryParameters);
        }

        @Override
        public JsonNode getAvailableImageQueryParameters(long seriesId) throws APIException {
            return SeriesAPI.getImagesQueryParams(con, seriesId);
        }

        @Override
        public JsonNode queryLastUpdated(QueryParameters queryParameters) throws APIException {
            return UpdatesAPI.query(con, queryParameters);
        }

        @Override
        public JsonNode getAvailableLastUpdatedQueryParameters() throws APIException {
            return UpdatesAPI.getQueryParams(con);
        }

        @Override
        public JsonNode getUser() throws APIException {
            validateUserAuthentication();
            return UsersAPI.get(con);
        }

        @Override
        public JsonNode getFavorites() throws APIException {
            validateUserAuthentication();
            return UsersAPI.getFavorites(con);
        }

        @Override
        public JsonNode deleteFromFavorites(long seriesId) throws APIException {
            validateUserAuthentication();
            return UsersAPI.deleteFromFavorites(con, seriesId);
        }

        @Override
        public JsonNode addToFavorites(long seriesId) throws APIException {
            validateUserAuthentication();
            return UsersAPI.addToFavorites(con, seriesId);
        }

        @Override
        public JsonNode getRatings() throws APIException {
            validateUserAuthentication();
            return UsersAPI.getRatings(con);
        }

        @Override
        public JsonNode queryRatings(QueryParameters queryParameters) throws APIException {
            validateUserAuthentication();
            return UsersAPI.queryRatings(con, queryParameters);
        }

        @Override
        public JsonNode getAvailableRatingsQueryParameters() throws APIException {
            validateUserAuthentication();
            return UsersAPI.getRatingsQueryParams(con);
        }

        @Override
        public JsonNode deleteFromRatings(@Nonnull String itemType, long itemId) throws APIException {
            validateNotEmpty(itemType);
            validateUserAuthentication();
            return UsersAPI.deleteFromRatings(con, itemType, itemId);
        }

        @Override
        public JsonNode addToRatings(@Nonnull String itemType, long itemId, long itemRating) throws APIException {
            validateNotEmpty(itemType);
            validateUserAuthentication();
            return UsersAPI.addToRatings(con, itemType, itemId, itemRating);
        }

        /**
         * Verifies that the underlying connection to the remote API is associated with a valid user authentication
         */
        private void validateUserAuthentication() {
            if (!con.userAuthentication()) {
                throw new APIPreconditionException("API call requires userKey/userName to be set!");
            }
        }
    }

    /**
     * Implementation of the {@link TheTVDBApi.Extended} API layout. It provides methods for all sorts of API calls throughout the different API routes. Responses will
     * be returned as wrapped {@link APIResponse APIResponse&lt;DTO&gt;} objects containing additional error and paging information.
     */
    private class ExtendedApi implements Extended {

        /**
         * Creates a new instance of the {@link TheTVDBApi.Extended} API layout
         */
        private ExtendedApi() {}

        @Override
        public APIResponse<Episode> getEpisode(long episodeId) throws APIException {
            return JsonDeserializer.mapEpisode(json().getEpisode(episodeId));
        }

        @Override
        public APIResponse<List<Language>> getAvailableLanguages() throws APIException {
            return JsonDeserializer.mapLanguages(json().getAvailableLanguages());
        }

        @Override
        public APIResponse<Language> getLanguage(long languageId) throws APIException {
            return JsonDeserializer.mapLanguage(json().getLanguage(languageId));
        }

        @Override
        public APIResponse<List<SeriesSearchResult>> searchSeries(QueryParameters queryParameters) throws APIException {
            return JsonDeserializer.mapSeriesSearchResult(json().searchSeries(queryParameters));
        }

        @Override
        public APIResponse<List<String>> getAvailableSeriesSearchParameters() throws APIException {
            return JsonDeserializer.mapQueryParameters(json().getAvailableSeriesSearchParameters());
        }

        @Override
        public APIResponse<Series> getSeries(long seriesId) throws APIException {
            return JsonDeserializer.mapSeries(json().getSeries(seriesId));
        }

        @Override
        public APIResponse<Map<String, String>> getSeriesHeaderInformation(long seriesId) throws APIException {
            return JsonDeserializer.mapSeriesHeader(json().getSeriesHeaderInformation(seriesId));
        }

        @Override
        public APIResponse<List<Actor>> getActors(long seriesId) throws APIException {
            return JsonDeserializer.mapActors(json().getActors(seriesId));
        }

        @Override
        public APIResponse<List<Episode>> getEpisodes(long seriesId, QueryParameters queryParameters) throws APIException {
            return JsonDeserializer.mapEpisodes(json().getEpisodes(seriesId, queryParameters));
        }

        @Override
        public APIResponse<List<Episode>> queryEpisodes(long seriesId, QueryParameters queryParameters) throws APIException {
            return JsonDeserializer.mapEpisodes(json().queryEpisodes(seriesId, queryParameters));
        }

        @Override
        public APIResponse<List<String>> getAvailableEpisodeQueryParameters(long seriesId) throws APIException {
            return JsonDeserializer.mapQueryParameters(json().getAvailableEpisodeQueryParameters(seriesId));
        }

        @Override
        public APIResponse<SeriesSummary> getSeriesEpisodesSummary(long seriesId) throws APIException {
            return JsonDeserializer.mapSeriesSummary(json().getSeriesEpisodesSummary(seriesId));
        }

        @Override
        public APIResponse<Series> filterSeries(long seriesId, QueryParameters queryParameters) throws APIException {
            return JsonDeserializer.mapSeries(json().filterSeries(seriesId, queryParameters));
        }

        @Override
        public APIResponse<List<String>> getAvailableSeriesFilterParameters(long seriesId) throws APIException {
            return JsonDeserializer.mapQueryParameters(json().getAvailableSeriesFilterParameters(seriesId));
        }

        @Override
        public APIResponse<ImageSummary> getSeriesImagesSummary(long seriesId) throws APIException {
            return JsonDeserializer.mapSeriesImageSummary(json().getSeriesImagesSummary(seriesId));
        }

        @Override
        public APIResponse<List<Image>> queryImages(long seriesId, QueryParameters queryParameters) throws APIException {
            return JsonDeserializer.mapImages(json().queryImages(seriesId, queryParameters));
        }

        @Override
        public APIResponse<List<ImageQueryParameter>> getAvailableImageQueryParameters(long seriesId) throws APIException {
            return JsonDeserializer.mapImageQueryParameters(json().getAvailableImageQueryParameters(seriesId));
        }

        @Override
        public APIResponse<Map<Long, Long>> queryLastUpdated(QueryParameters queryParameters) throws APIException {
            return JsonDeserializer.mapUpdates(json().queryLastUpdated(queryParameters));
        }

        @Override
        public APIResponse<List<String>> getAvailableLastUpdatedQueryParameters() throws APIException {
            return JsonDeserializer.mapQueryParameters(json().getAvailableLastUpdatedQueryParameters());
        }

        @Override
        public APIResponse<User> getUser() throws APIException {
            return JsonDeserializer.mapUser(json().getUser());
        }

        @Override
        public APIResponse<List<String>> getFavorites() throws APIException {
            return JsonDeserializer.mapFavorites(json().getFavorites());
        }

        @Override
        public APIResponse<List<String>> deleteFromFavorites(long seriesId) throws APIException {
            return JsonDeserializer.mapFavorites(json().deleteFromFavorites(seriesId));
        }

        @Override
        public APIResponse<List<String>> addToFavorites(long seriesId) throws APIException {
            return JsonDeserializer.mapFavorites(json().addToFavorites(seriesId));
        }

        @Override
        public APIResponse<List<Rating>> getRatings() throws APIException {
            return JsonDeserializer.mapRatings(json().getRatings());
        }

        @Override
        public APIResponse<List<Rating>> queryRatings(QueryParameters queryParameters) throws APIException {
            return JsonDeserializer.mapRatings(json().queryRatings(queryParameters));
        }

        @Override
        public APIResponse<List<String>> getAvailableRatingsQueryParameters() throws APIException {
            return JsonDeserializer.mapQueryParameters(json().getAvailableRatingsQueryParameters());
        }

        @Override
        public void deleteFromRatings(@Nonnull String itemType, long itemId) throws APIException {
            json().deleteFromRatings(itemType, itemId);
        }

        @Override
        public APIResponse<List<Rating>> addToRatings(@Nonnull String itemType, long itemId, long itemRating) throws APIException {
            return JsonDeserializer.mapRatings(json().addToRatings(itemType, itemId, itemRating));
        }
    }

    /**
     * Validates that none of the given method String parameters is NULL or empty
     *
     * @param params Array of method String parameter to check
     *
     * @throws IllegalArgumentException If at least one of the given parameters is NULL or empty
     */
    private void validateNotEmpty(String... params) {
        Parameters.validateCondition(APIUtil::hasValue, params, new IllegalArgumentException("Method parameters must not be NULL or empty!"));
    }

    /**
     * Creates an empty query parameters object with no individual parameters set
     *
     * @return Empty query parameters object
     */
    private QueryParameters emptyQuery() {
        return TheTVDBApiFactory.createQueryParameters();
    }

    /**
     * Creates a new query parameters object with preset parameters based on the given map of key/value pairs
     *
     * @param parameters Map of parameter key/value pairs. For each entry in the map an appropriate parameter will be added
     *                   in the object returned by this method
     *
     * @return New query parameters object with a preset collection of individual query parameters
     */
    private QueryParameters query(@Nonnull Map<String, String> parameters) {
        return TheTVDBApiFactory.createQueryParameters(parameters);
    }
}
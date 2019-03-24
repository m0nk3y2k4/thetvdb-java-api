package com.github.m0nk3y2k4.thetvdb.internal.api.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query;
import com.github.m0nk3y2k4.thetvdb.api.model.*;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.AuthenticationAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.EpisodesAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.LanguagesAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SearchAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.UpdatesAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.UsersAPI;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;
import com.github.m0nk3y2k4.thetvdb.internal.util.JsonDeserializer;

/**
 * Objects of this class represent the public interface/entry point for the usage of TheTVDB-API. It provides methods for all sorts of API calls throughout the
 * different API routes. Responses will either be returned as raw, untouch JSON (as received by the remote REST service) or as mapped Java-Objects.
 */
public class TheTVDBApiImpl implements TheTVDBApi {

    /** The actual connection to the remote API */
    private final APIConnection con;

    /**
     * Creates a new TheTVDBAPI instance. The given <code>apiKey</code> must be a valid <a href="https://www.thetvdb.com/member/api">TheTVDB API Key</a> which will
     * be used for remote service authentication. To authenticate and generate a new session token use the {@link #init()} or {@link #login()} methods right after
     * creating a new instance of this API.
     * <p/>
     * <b>NOTE:</b> Objects created with this constructor <u>can not</u> be used for calls to the remote API's <a href="https://api.thetvdb.com/swagger#/Users">/users</a>
     * routes. These calls require extended authentication using an additional <code>userKey</code> and <code>userName</code>.
     *
     * @see #TheTVDBApiImpl(String, String, String) TheTVDBApiImpl(apiKey, userKey, userName)
     *
     * @param apiKey Valid TheTVDB API-Key
     */
    public TheTVDBApiImpl(@Nonnull String apiKey) {
        if (!APIUtil.hasValue(apiKey)) {
            throw new IllegalArgumentException("APIKey must not be null or empty!");
        }

        this.con = new APIConnection(apiKey);
    }

    /**
     * Creates a new TheTVDBAPI instance. The given <code>apiKey</code> must be a valid <a href="https://www.thetvdb.com/member/api">TheTVDB API Key</a>. The <code>userKey</code>
     * and <code>userName</code> must refer to a registered TheTVDB user account. The given parameters will be used for the initial remote service authentication. To authenticate
     * and generate a new session token use the {@link #init()} or {@link #login()} methods right after creating a new instance of this API.
     *
     * @param apiKey Valid TheTVDB API-Key
     * @param userKey Valid TheTVDB user key (also referred to as "Unique ID")
     * @param userName Registered TheTVDB user name
     */
    public TheTVDBApiImpl(@Nonnull String apiKey, @Nonnull String userKey, @Nonnull String userName) {
        if (!APIUtil.hasValue(apiKey, userKey, userName)) {
            throw new IllegalArgumentException("APIKey/UserKey/UserName must not be null or empty!");
        }

        this.con = new APIConnection(apiKey, userKey, userName);
    }

    @Override
    public void init() throws APIException {
        this.login();
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
    public JsonNode getEpisodeJSON(long episodeId) throws APIException {
        return EpisodesAPI.get(con, episodeId);
    }

    @Override
    public Episode getEpisode(long episodeId) throws APIException {
        return JsonDeserializer.createEpisode(getEpisodeJSON(episodeId));
    }

    @Override
    public JsonNode getAvailableLanguagesJSON() throws APIException {
        return LanguagesAPI.getAllAvailable(con);
    }

    @Override
    public List<Language> getAvailableLanguages() throws APIException {
        return JsonDeserializer.createLanguageList(getAvailableLanguagesJSON());
    }

    @Override
    public JsonNode getLanguageJSON(long languageId) throws APIException {
        return LanguagesAPI.get(con, languageId);
    }

    @Override
    public Language getLanguage(long languageId) throws APIException {
        return JsonDeserializer.createLanguage(getLanguageJSON(languageId));
    }

    @Override
    public JsonNode searchSeriesJSON(QueryParameters queryParameters) throws APIException {
        return SearchAPI.series(con, queryParameters);
    }

    @Override
    public List<SeriesAbstract> searchSeries(QueryParameters queryParameters) throws APIException {
        return JsonDeserializer.createSeriesAbstract(searchSeriesJSON(queryParameters));
    }

    @Override
    public List<SeriesAbstract> searchSeriesByName(@Nonnull String name) throws APIException {
        validateNotEmpty(name);
        return JsonDeserializer.createSeriesAbstract(searchSeriesJSON(query(Map.of(Query.Search.NAME, name))));
    }

    @Override
    public List<SeriesAbstract> searchSeriesByImdbId(@Nonnull String imdbId) throws APIException {
        validateNotEmpty(imdbId);
        return JsonDeserializer.createSeriesAbstract(searchSeriesJSON(query(Map.of(Query.Search.IMDBID, imdbId))));
    }

    @Override
    public List<SeriesAbstract> searchSeriesByZap2itId(@Nonnull String zap2itId) throws APIException {
        validateNotEmpty(zap2itId);
        return JsonDeserializer.createSeriesAbstract(searchSeriesJSON(query(Map.of(Query.Search.ZAP2ITID, zap2itId))));
    }

    @Override
    public JsonNode getAvailableSeriesSearchParametersJSON() throws APIException {
        return SearchAPI.getAvailableSearchParameters(con);
    }

    @Override
    public List<String> getAvailableSeriesSearchParameters() throws APIException {
        return JsonDeserializer.createQueryParameters(getAvailableSeriesSearchParametersJSON());
    }

    @Override
    public JsonNode getSeriesJSON(long seriesId) throws APIException {
        return SeriesAPI.get(con, seriesId);
    }

    @Override
    public Series getSeries(long seriesId) throws APIException {
        return JsonDeserializer.createSeries(getSeriesJSON(seriesId));
    }

    @Override
    public JsonNode getSeriesHeaderInformationJSON(long seriesId) throws APIException {
        return SeriesAPI.getHead(con, seriesId);
    }

    @Override
    public Map<String, String> getSeriesHeaderInformation(long seriesId) throws APIException {
        return JsonDeserializer.createSeriesHeader(getSeriesHeaderInformationJSON(seriesId));
    }

    @Override
    public JsonNode getActorsJSON(long seriesId) throws APIException {
        return SeriesAPI.getActors(con, seriesId);
    }

    @Override
    public List<Actor> getActors(long seriesId) throws APIException {
        return JsonDeserializer.createActors(getActorsJSON(seriesId));
    }

    @Override
    public JsonNode getEpisodesJSON(long seriesId, QueryParameters queryParameters) throws APIException {
        return SeriesAPI.getEpisodes(con, seriesId, queryParameters);
    }

    @Override
    public List<EpisodeAbstract> getEpisodes(long seriesId, QueryParameters queryParameters) throws APIException {
        return JsonDeserializer.createEpisodeAbstract(getEpisodesJSON(seriesId, queryParameters));
    }

    @Override
    public List<EpisodeAbstract> getEpisodes(long seriesId) throws APIException {
        return JsonDeserializer.createEpisodeAbstract(getEpisodesJSON(seriesId, emptyQuery()));
    }

    @Override
    public List<EpisodeAbstract> getEpisodes(long seriesId, long page) throws APIException {
        return JsonDeserializer.createEpisodeAbstract(getEpisodesJSON(seriesId, query(Map.of(Query.Series.PAGE, String.valueOf(page)))));
    }

    @Override
    public JsonNode queryEpisodesJSON(long seriesId, QueryParameters queryParameters) throws APIException {
        return SeriesAPI.queryEpisodes(con, seriesId, queryParameters);
    }

    @Override
    public List<EpisodeAbstract> queryEpisodes(long seriesId, QueryParameters queryParameters) throws APIException {
        return JsonDeserializer.createEpisodeAbstract(queryEpisodesJSON(seriesId, queryParameters));
    }

    @Override
    public List<EpisodeAbstract> queryEpisodesByAiredSeason(long seriesId, long airedSeason) throws APIException {
        return JsonDeserializer.createEpisodeAbstract(queryEpisodesJSON(seriesId, query(Map.of(Query.Series.AIREDSEASON, String.valueOf(airedSeason)))));
    }

    @Override
    public List<EpisodeAbstract> queryEpisodesByAiredSeason(long seriesId, long airedSeason, long page) throws APIException {
        return JsonDeserializer.createEpisodeAbstract(queryEpisodesJSON(seriesId, query(Map.of(Query.Series.AIREDSEASON, String.valueOf(airedSeason), Query.Series.PAGE, String.valueOf(page)))));
    }

    @Override
    public List<EpisodeAbstract> queryEpisodesByAiredEpisode(long seriesId, long airedEpisode) throws APIException {
        return JsonDeserializer.createEpisodeAbstract(queryEpisodesJSON(seriesId, query(Map.of(Query.Series.AIREDEPISODE, String.valueOf(airedEpisode)))));
    }

    @Override
    public List<EpisodeAbstract> queryEpisodesByAbsoluteNumber(long seriesId, long absoluteNumber) throws APIException {
        return JsonDeserializer.createEpisodeAbstract(queryEpisodesJSON(seriesId, query(Map.of(Query.Series.ABSOLUTENUMBER, String.valueOf(absoluteNumber)))));
    }

    @Override
    public JsonNode getAvailableEpisodeQueryParametersJSON(long seriesId) throws APIException {
        return SeriesAPI.getEpisodesQueryParams(con, seriesId);
    }

    @Override
    public List<String> getAvailableEpisodeQueryParameters(long seriesId) throws APIException {
        return JsonDeserializer.createQueryParameters(getAvailableEpisodeQueryParametersJSON(seriesId));
    }

    @Override
    public JsonNode getSeriesEpisodesSummaryJSON(long seriesId) throws APIException {
        return SeriesAPI.getEpisodesSummary(con, seriesId);
    }

    @Override
    public SeriesSummary getSeriesEpisodesSummary(long seriesId) throws APIException {
        return JsonDeserializer.createSeriesSummary(getSeriesEpisodesSummaryJSON(seriesId));
    }

    @Override
    public JsonNode filterSeriesJSON(long seriesId, QueryParameters queryParameters) throws APIException {
        return SeriesAPI.filter(con, seriesId, queryParameters);
    }

    @Override
    public Series filterSeries(long seriesId, QueryParameters queryParameters) throws APIException {
        return JsonDeserializer.createSeries(filterSeriesJSON(seriesId, queryParameters));
    }

    @Override
    public Series filterSeries(long seriesId, @Nonnull String filterKeys) throws APIException {
        validateNotEmpty(filterKeys);
        return JsonDeserializer.createSeries(filterSeriesJSON(seriesId, query(Map.of(Query.Series.KEYS, filterKeys))));
    }

    @Override
    public JsonNode getAvailableFilterParametersJSON(long seriesId) throws APIException {
        return SeriesAPI.getFilterParams(con, seriesId);
    }

    @Override
    public List<String> getAvailableFilterParameters(long seriesId) throws APIException {
        return JsonDeserializer.createQueryParameters(getAvailableFilterParametersJSON(seriesId));
    }

    @Override
    public JsonNode getSeriesImagesSummaryJSON(long seriesId) throws APIException {
        return SeriesAPI.getImages(con, seriesId);
    }

    @Override
    public ImageSummary getSeriesImagesSummary(long seriesId) throws APIException {
        return JsonDeserializer.createSeriesImageSummary(getSeriesImagesSummaryJSON(seriesId));
    }

    @Override
    public JsonNode queryImagesJSON(long seriesId, QueryParameters queryParameters) throws APIException {
        return SeriesAPI.queryImages(con, seriesId, queryParameters);
    }

    @Override
    public List<Image> queryImages(long seriesId, QueryParameters queryParameters) throws APIException {
        return JsonDeserializer.createImages(queryImagesJSON(seriesId, queryParameters));
    }

    @Override
    public List<Image> queryImages(long seriesId, String keyType, String resolution) throws APIException {
        validateNotEmpty(keyType, resolution);
        return JsonDeserializer.createImages(queryImagesJSON(seriesId, query(Map.of(Query.Series.KEYTYPE, keyType, Query.Series.RESOLUTION, resolution))));
    }

    @Override
    public List<Image> queryImages(long seriesId, String keyType, String resolution, String subKey) throws APIException {
        validateNotEmpty(keyType, resolution, subKey);
        return JsonDeserializer.createImages(queryImagesJSON(seriesId, query(Map.of(Query.Series.KEYTYPE, keyType, Query.Series.RESOLUTION, resolution, Query.Series.SUBKEY, subKey))));
    }

    @Override
    public List<Image> queryImagesByKeyType(long seriesId, String keyType) throws APIException {
        validateNotEmpty(keyType);
        return JsonDeserializer.createImages(queryImagesJSON(seriesId, query(Map.of(Query.Series.KEYTYPE, keyType))));
    }

    @Override
    public List<Image> queryImagesByResolution(long seriesId, String resolution) throws APIException {
        validateNotEmpty(resolution);
        return JsonDeserializer.createImages(queryImagesJSON(seriesId, query(Map.of(Query.Series.RESOLUTION, resolution))));
    }

    @Override
    public List<Image> queryImagesBySubKey(long seriesId, String subKey) throws APIException {
        validateNotEmpty(subKey);
        return JsonDeserializer.createImages(queryImagesJSON(seriesId, query(Map.of(Query.Series.SUBKEY, subKey))));
    }

    @Override
    public JsonNode getAvailableImageQueryParametersJSON(long seriesId) throws APIException {
        return SeriesAPI.getImagesQueryParams(con, seriesId);
    }

    @Override
    public List<ImageQueryParameter> getAvailableImageQueryParameters(long seriesId) throws APIException {
        return JsonDeserializer.createQueryParameterList(getAvailableImageQueryParametersJSON(seriesId));
    }

    @Override
    public JsonNode queryLastUpdatedJSON(QueryParameters queryParameters) throws APIException {
        return UpdatesAPI.query(con, queryParameters);
    }

    @Override
    public Map<Long, Long> queryLastUpdated(QueryParameters queryParameters) throws APIException {
        return JsonDeserializer.createUpdated(queryLastUpdatedJSON(queryParameters));
    }

    @Override
    public Map<Long, Long> queryLastUpdated(@Nonnull String fromTime) throws APIException {
        validateNotEmpty(fromTime);
        return JsonDeserializer.createUpdated(queryLastUpdatedJSON(query(Map.of(Query.Updates.FROMTIME, fromTime))));
    }

    @Override
    public Map<Long, Long> queryLastUpdated(@Nonnull String fromTime, @Nonnull String toTime) throws APIException {
        validateNotEmpty(fromTime, toTime);
        return JsonDeserializer.createUpdated(queryLastUpdatedJSON(query(Map.of(Query.Updates.FROMTIME, fromTime, Query.Updates.TOTIME, toTime))));
    }

    @Override
    public JsonNode getAvailableLastUpdatedQueryParametersJSON() throws APIException {
        return UpdatesAPI.getQueryParams(con);
    }

    @Override
    public List<String> getAvailableLastUpdatedQueryParameters() throws APIException {
        return JsonDeserializer.createQueryParameters(getAvailableLastUpdatedQueryParametersJSON());
    }

    @Override
    public JsonNode getUserJSON() throws APIException {
        validateUserAuthentication();
        return UsersAPI.get(con);
    }

    @Override
    public User getUser() throws APIException {
        return JsonDeserializer.createUser(getUserJSON());
    }

    @Override
    public JsonNode getFavoritesJSON() throws APIException {
        validateUserAuthentication();
        return UsersAPI.getFavorites(con);
    }

    @Override
    public List<String> getFavorites() throws APIException {
        return JsonDeserializer.createFavorites(getFavoritesJSON());
    }

    @Override
    public JsonNode deleteFromFavoritesJSON(long seriesId) throws APIException {
        validateUserAuthentication();
        return UsersAPI.deleteFromFavorites(con, seriesId);
    }

    @Override
    public List<String> deleteFromFavorites(long seriesId) throws APIException {
        return JsonDeserializer.createFavorites(deleteFromFavoritesJSON(seriesId));
    }

    @Override
    public JsonNode addToFavoritesJSON(long seriesId) throws APIException {
        validateUserAuthentication();
        return UsersAPI.addToFavorites(con, seriesId);
    }

    @Override
    public List<String> addToFavorites(long seriesId) throws APIException {
        return JsonDeserializer.createFavorites(addToFavoritesJSON(seriesId));
    }

    @Override
    public JsonNode getRatingsJSON() throws APIException {
        validateUserAuthentication();
        return UsersAPI.getRatings(con);
    }

    @Override
    public List<Rating> getRatings() throws APIException {
        return JsonDeserializer.createRatings(getRatingsJSON());
    }

    @Override
    public JsonNode queryRatingsJSON(QueryParameters queryParameters) throws APIException {
        validateUserAuthentication();
        return UsersAPI.queryRatings(con, queryParameters);
    }

    @Override
    public List<Rating> queryRatings(QueryParameters queryParameters) throws APIException {
        return JsonDeserializer.createRatings(queryRatingsJSON(queryParameters));
    }

    @Override
    public List<Rating> queryRatingsByItemType(@Nonnull String itemType) throws APIException {
        validateNotEmpty(itemType);
        return JsonDeserializer.createRatings(queryRatingsJSON(query(Map.of(Query.Users.ITEMTYPE, itemType))));
    }

    @Override
    public JsonNode getAvailableRatingsQueryParametersJSON() throws APIException {
        validateUserAuthentication();
        return UsersAPI.getRatingsQueryParams(con);
    }

    @Override
    public List<String> getAvailableRatingsQueryParameters() throws APIException {
        return JsonDeserializer.createQueryParameters(getAvailableRatingsQueryParametersJSON());
    }

    @Override
    public JsonNode deleteFromRatingsJSON(@Nonnull String itemType, long itemId) throws APIException {
        validateNotEmpty(itemType);
        validateUserAuthentication();
        return UsersAPI.deleteFromRatings(con, itemType, itemId);
    }

    @Override
    public void deleteFromRatings(@Nonnull String itemType, long itemId) throws APIException {
        deleteFromRatingsJSON(itemType, itemId);
    }

    @Override
    public JsonNode addToRatingsJSON(@Nonnull String itemType, long itemId, long itemRating) throws APIException {
        validateNotEmpty(itemType);
        validateUserAuthentication();
        return UsersAPI.addToRatings(con, itemType, itemId, itemRating);
    }

    @Override
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
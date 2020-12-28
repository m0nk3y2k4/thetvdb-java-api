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

package com.github.m0nk3y2k4.thetvdb.internal.api.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory;
import com.github.m0nk3y2k4.thetvdb.api.APIKey;
import com.github.m0nk3y2k4.thetvdb.api.Proxy;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Artwork;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ArtworkDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ArtworkType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.AwardCategory;
import com.github.m0nk3y2k4.thetvdb.api.model.data.AwardCategoryDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Character;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Company;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Episode;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Genre;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Movie;
import com.github.m0nk3y2k4.thetvdb.api.model.data.People;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Season;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Series;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesDetails;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.ArtworkAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.ArtworkTypesAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.AwardCategoriesAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.CharactersAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.CompaniesAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.EpisodesAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.GenresAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.LoginAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.MoviesAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.PeopleAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeasonsAPI;
import com.github.m0nk3y2k4.thetvdb.internal.resource.impl.SeriesAPI;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;
import com.github.m0nk3y2k4.thetvdb.internal.util.json.APIJsonMapper;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

// ToDo: Revise JDoc once APIv4 implementation is finished

/**
 * Implementation of the {@link TheTVDBApi} API layout. It provides methods for all sorts of API calls throughout the
 * different API routes. Responses will be returned as mapped Java DTO objects.
 */
public class TheTVDBApiImpl implements TheTVDBApi {

    /** Wrapper API: Consolidates TheTVDBApi calls which return raw JSON */
    private final JSON jsonApi = new JSONApi();

    /** Wrapper API: Consolidates TheTVDBApi calls which return extended response information */
    private final Extended extendedApi = new ExtendedApi();

    /** The actual connection to the remote API */
    private final APIConnection con;

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid
     * <a target="_blank" href="https://www.thetvdb.com/dashboard/account/apikey">TheTVDB.com v4 API Key</a> as it will
     * be used for remote service authentication. To authenticate and generate a new session token use the {@link
     * #init()} or {@link #login()} method right after creating a new instance of this API.
     *
     * @param apiKey Valid <i>TheTVDB.com</i> v4 API-Key
     */
    public TheTVDBApiImpl(@Nonnull APIKey apiKey) {
        this.con = new APIConnection(apiKey);
    }

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid
     * <a target="_blank" href="https://www.thetvdb.com/dashboard/account/apikey">TheTVDB.com v4 API Key</a> as it will
     * be used for remote service authentication. To authenticate and generate a new session token use the {@link
     * #init()} or {@link #login()} method right after creating a new instance of this API. All communication to the
     * remote API will be forwarded to the given <em>{@code proxy}</em>.
     *
     * @param apiKey Valid <i>TheTVDB.com</i> v4 API-Key
     * @param proxy  The proxy service to be used for remote API communication
     */
    public TheTVDBApiImpl(@Nonnull APIKey apiKey, @Nonnull Proxy proxy) {
        Parameters.validateNotNull(proxy, "Proxy must not be NULL");
        this.con = new APIConnection(apiKey, new RemoteAPI.Builder().from(proxy).build());
    }

    /**
     * Validates that none of the given method String parameters is NULL or empty
     *
     * @param params Array of method String parameter to check
     *
     * @throws IllegalArgumentException If at least one of the given parameters is NULL or empty
     */
    private static void validateNotEmpty(String... params) {
        Parameters.validateCondition(APIUtil::hasValue, params,
                new IllegalArgumentException("Method parameters must not be NULL or empty!"));
    }

    /**
     * Validates that the given method {@code page} parameters is not negative
     *
     * @param page The method {@code page} parameter to check
     *
     * @throws IllegalArgumentException If the given parameter is a negative numerical value
     */
    private static void validatePage(long page) {
        Parameters.validateCondition(value -> value >= 0, page,
                new IllegalArgumentException("Page value must not be negative!"));
    }

    /**
     * Creates an empty query parameters object with no individual parameters set
     *
     * @return Empty query parameters object
     */
    private static QueryParameters emptyQuery() {
        return TheTVDBApiFactory.createQueryParameters();
    }

    /**
     * Creates a new query parameters object with preset parameters based on the given map of key/value pairs
     *
     * @param parameters Map of parameter key/value pairs. For each entry in the map an appropriate parameter will be
     *                   added in the object returned by this method
     *
     * @return New query parameters object with a preset collection of individual query parameters
     */
    private static QueryParameters query(@Nonnull Map<String, String> parameters) {
        return TheTVDBApiFactory.createQueryParameters(parameters);
    }

    @Override
    public void init() throws APIException {
        login();
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
        LoginAPI.login(con);
    }

    @Override
    public void refreshToken() throws APIException {
        // ToDo: Adjust to new APIv4 login mechanics
//        AuthenticationAPI.refreshSession(con);
    }

    @Override
    public List<ArtworkType> getAllArtworkTypes() throws APIException {
        return extended().getAllArtworkTypes().getData();
    }

    @Override
    public Artwork getArtwork(long artworkId) throws APIException {
        return extended().getArtwork(artworkId).getData();
    }

    @Override
    public ArtworkDetails getArtworkDetails(long artworkId) throws APIException {
        return extended().getArtworkDetails(artworkId).getData();
    }

    @Override
    public AwardCategory getAwardCategory(long awardCategoryId) throws APIException {
        return extended().getAwardCategory(awardCategoryId).getData();
    }

    @Override
    public AwardCategoryDetails getAwardCategoryDetails(long awardCategoryId) throws APIException {
        return extended().getAwardCategoryDetails(awardCategoryId).getData();
    }

    @Override
    public Character getCharacter(long characterId) throws APIException {
        return extended().getCharacter(characterId).getData();
    }

    @Override
    public List<Company> getAllCompanies(QueryParameters queryParameters) throws APIException {
        return extended().getAllCompanies(queryParameters).getData();
    }

    @Override
    public List<Company> getAllCompanies(long page) throws APIException {
        validatePage(page);
        return getAllCompanies(query(Map.of(Query.Companies.PAGE, String.valueOf(page))));
    }

    @Override
    public Company getCompany(long companyId) throws APIException {
        return extended().getCompany(companyId).getData();
    }

    @Override
    public Episode getEpisode(long episodeId) throws APIException {
        return extended().getEpisode(episodeId).getData();
    }

    @Override
    public List<Genre> getAllGenres() throws APIException {
        return extended().getAllGenres().getData();
    }

    @Override
    public Genre getGenre(long genreId) throws APIException {
        return extended().getGenre(genreId).getData();
    }

    @Override
    public Movie getMovie(long movieId) throws APIException {
        return extended().getMovie(movieId).getData();
    }

    @Override
    public People getPeople(long peopleId) throws APIException {
        return extended().getPeople(peopleId).getData();
    }

    @Override
    public Season getSeason(long seasonId) throws APIException {
        return extended().getSeason(seasonId).getData();
    }

    @Override
    public List<Series> getAllSeries(QueryParameters queryParameters) throws APIException {
        return extended().getAllSeries(queryParameters).getData();
    }

    @Override
    public List<Series> getAllSeries(long page) throws APIException {
        validatePage(page);
        return getAllSeries(query(Map.of(Query.Series.PAGE, String.valueOf(page))));
    }

    @Override
    public Series getSeries(long seriesId) throws APIException {
        return extended().getSeries(seriesId).getData();
    }

    @Override
    public SeriesDetails getSeriesDetails(long seriesId) throws APIException {
        return extended().getSeriesDetails(seriesId).getData();
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
     * Implementation of the {@link TheTVDBApi.JSON} API layout. It provides methods for all sorts of API calls
     * throughout the different API routes. Responses will be returned as raw, untouched JSON as it has been received by
     * the remote REST service.
     */
    private class JSONApi implements JSON {

        @Override
        public JsonNode getAllArtworkTypes() throws APIException {
            return ArtworkTypesAPI.getAllArtworkTypes(con);
        }

        @Override
        public JsonNode getArtwork(long artworkId) throws APIException {
            return ArtworkAPI.getArtworkBase(con, artworkId);
        }

        @Override
        public JsonNode getArtworkDetails(long artworkId) throws APIException {
            return ArtworkAPI.getArtworkExtended(con, artworkId);
        }

        @Override
        public JsonNode getAwardCategory(long awardCategoryId) throws APIException {
            return AwardCategoriesAPI.getAwardCategoryBase(con, awardCategoryId);
        }

        @Override
        public JsonNode getAwardCategoryDetails(long awardCategoryId) throws APIException {
            return AwardCategoriesAPI.getAwardCategoryExtended(con, awardCategoryId);
        }

        @Override
        public JsonNode getCharacter(long characterId) throws APIException {
            return CharactersAPI.getCharacterBase(con, characterId);
        }

        @Override
        public JsonNode getAllCompanies(QueryParameters queryParameters) throws APIException {
            return CompaniesAPI.getAllCompanies(con, queryParameters);
        }

        @Override
        public JsonNode getCompany(long companyId) throws APIException {
            return CompaniesAPI.getCompany(con, companyId);
        }

        @Override
        public JsonNode getEpisode(long episodeId) throws APIException {
            return EpisodesAPI.getEpisodeBase(con, episodeId);
        }

        @Override
        public JsonNode getAllGenres() throws APIException {
            return GenresAPI.getAllGenres(con);
        }

        @Override
        public JsonNode getGenre(long genreId) throws APIException {
            return GenresAPI.getGenreBase(con, genreId);
        }

        @Override
        public JsonNode getMovie(long movieId) throws APIException {
            return MoviesAPI.getMovieBase(con, movieId);
        }

        @Override
        public JsonNode getPeople(long peopleId) throws APIException {
            return PeopleAPI.getPeopleBase(con, peopleId);
        }

        @Override
        public JsonNode getSeason(long seasonId) throws APIException {
            return SeasonsAPI.getSeasonBase(con, seasonId);
        }

        @Override
        public JsonNode getAllSeries(QueryParameters queryParameters) throws APIException {
            return SeriesAPI.getAllSeries(con, queryParameters);
        }

        @Override
        public JsonNode getSeries(long seriesId) throws APIException {
            return SeriesAPI.getSeriesBase(con, seriesId);
        }

        @Override
        public JsonNode getSeriesDetails(long seriesId) throws APIException {
            return SeriesAPI.getSeriesExtended(con, seriesId);
        }
    }

    /**
     * Implementation of the {@link TheTVDBApi.Extended} API layout. It provides methods for all sorts of API calls
     * throughout the different API routes. Responses will be returned as wrapped {@link APIResponse
     * APIResponse&lt;DTO&gt;} objects containing additional error and paging information.
     */
    private class ExtendedApi implements Extended {

        @Override
        public APIResponse<List<ArtworkType>> getAllArtworkTypes() throws APIException {
            return APIJsonMapper.readValue(json().getAllArtworkTypes(), new TypeReference<>() {});
        }

        @Override
        public APIResponse<Artwork> getArtwork(long artworkId) throws APIException {
            return APIJsonMapper.readValue(json().getArtwork(artworkId), new TypeReference<>() {});
        }

        @Override
        public APIResponse<ArtworkDetails> getArtworkDetails(long artworkId) throws APIException {
            return APIJsonMapper.readValue(json().getArtworkDetails(artworkId), new TypeReference<>() {});
        }

        @Override
        public APIResponse<AwardCategory> getAwardCategory(long awardCategoryId) throws APIException {
            return APIJsonMapper.readValue(json().getAwardCategory(awardCategoryId), new TypeReference<>() {});
        }

        @Override
        public APIResponse<AwardCategoryDetails> getAwardCategoryDetails(long awardCategoryId) throws APIException {
            return APIJsonMapper.readValue(json().getAwardCategoryDetails(awardCategoryId), new TypeReference<>() {});
        }

        @Override
        public APIResponse<Character> getCharacter(long characterId) throws APIException {
            return APIJsonMapper.readValue(json().getCharacter(characterId), new TypeReference<>() {});
        }

        @Override
        public APIResponse<List<Company>> getAllCompanies(QueryParameters queryParameters) throws APIException {
            return APIJsonMapper.readValue(json().getAllCompanies(queryParameters), new TypeReference<>() {});
        }

        @Override
        public APIResponse<Company> getCompany(long companyId) throws APIException {
            return APIJsonMapper.readValue(json().getCompany(companyId), new TypeReference<>() {});
        }

        @Override
        public APIResponse<Episode> getEpisode(long episodeId) throws APIException {
            return APIJsonMapper.readValue(json().getEpisode(episodeId), new TypeReference<>() {});
        }

        @Override
        public APIResponse<List<Genre>> getAllGenres() throws APIException {
            return APIJsonMapper.readValue(json().getAllGenres(), new TypeReference<>() {});
        }

        @Override
        public APIResponse<Genre> getGenre(long genreId) throws APIException {
            return APIJsonMapper.readValue(json().getGenre(genreId), new TypeReference<>() {});
        }

        @Override
        public APIResponse<Movie> getMovie(long movieId) throws APIException {
            return APIJsonMapper.readValue(json().getMovie(movieId), new TypeReference<>() {});
        }

        @Override
        public APIResponse<People> getPeople(long peopleId) throws APIException {
            return APIJsonMapper.readValue(json().getPeople(peopleId), new TypeReference<>() {});
        }

        @Override
        public APIResponse<Season> getSeason(long seasonId) throws APIException {
            return APIJsonMapper.readValue(json().getSeason(seasonId), new TypeReference<>() {});
        }

        @Override
        public APIResponse<List<Series>> getAllSeries(QueryParameters queryParameters) throws APIException {
            return APIJsonMapper.readValue(json().getAllSeries(queryParameters), new TypeReference<>() {});
        }

        @Override
        public APIResponse<Series> getSeries(long seriesId) throws APIException {
            return APIJsonMapper.readValue(json().getSeries(seriesId), new TypeReference<>() {});
        }

        @Override
        public APIResponse<SeriesDetails> getSeriesDetails(long seriesId) throws APIException {
            return APIJsonMapper.readValue(json().getSeriesDetails(seriesId), new TypeReference<>() {});
        }
    }
}

/*
 * Copyright (C) 2019 - 2021 thetvdb-java-api Authors and Contributors
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

import java.util.Collection;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesSeasonType;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Artwork;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ArtworkDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ArtworkStatus;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ArtworkType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Award;
import com.github.m0nk3y2k4.thetvdb.api.model.data.AwardCategory;
import com.github.m0nk3y2k4.thetvdb.api.model.data.AwardCategoryDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.AwardDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Character;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Company;
import com.github.m0nk3y2k4.thetvdb.api.model.data.CompanyType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ContentRating;
import com.github.m0nk3y2k4.thetvdb.api.model.data.EntityType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.EntityUpdate;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Episode;
import com.github.m0nk3y2k4.thetvdb.api.model.data.EpisodeDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.FCList;
import com.github.m0nk3y2k4.thetvdb.api.model.data.FCListDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Gender;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Genre;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Movie;
import com.github.m0nk3y2k4.thetvdb.api.model.data.MovieDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.People;
import com.github.m0nk3y2k4.thetvdb.api.model.data.PeopleDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.PeopleType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Season;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeasonDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeasonType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Series;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesEpisodes;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SourceType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Status;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Translation;

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
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/login/post_login">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/login/get_refresh_token">
     * <b>[GET]</b> /refresh_token</a>
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    void refreshToken() throws APIException;

    /**
     * Returns a collection of available artwork statuses mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/artwork-statuses/getAllArtworkStatuses">
     * <b>[GET]</b> /artwork/statuses</a>
     *
     * @return Collection of available artwork statuses mapped as Java DTO's based on the JSON data returned by the
     *         remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAllArtworkStatuses()
     * @see Extended#getAllArtworkStatuses()
     */
    Collection<ArtworkStatus> getAllArtworkStatuses() throws APIException;

    /**
     * Returns a collection of available artwork types mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/artwork-types/getAllArtworkTypes">
     * <b>[GET]</b> /artwork/types</a>
     *
     * @return Collection of available artwork types mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAllArtworkTypes()
     * @see Extended#getAllArtworkTypes()
     */
    Collection<ArtworkType> getAllArtworkTypes() throws APIException;

    /**
     * Returns basic information for a specific artwork mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/artwork/getArtworkBase">
     * <b>[GET]</b> /artwork/{id}</a>
     *
     * @param artworkId The <i>TheTVDB.com</i> artwork ID
     *
     * @return Basic artwork information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no artwork record with the given ID exists.
     * @see JSON#getArtwork(long) TheTVDBApi.JSON.getArtwork(artworkId)
     * @see Extended#getArtwork(long) TheTVDBApi.Extended.getArtwork(artworkId)
     */
    Artwork getArtwork(long artworkId) throws APIException;

    /**
     * Returns detailed information for a specific artwork mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/artwork/getArtworkExtended">
     * <b>[GET]</b> /artwork/{id}/extended</a>
     *
     * @param artworkId The <i>TheTVDB.com</i> artwork ID
     *
     * @return Detailed artwork information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no artwork record with the given ID exists.
     * @see JSON#getArtworkDetails(long) TheTVDBApi.JSON.getArtworkDetails(artworkId)
     * @see Extended#getArtworkDetails(long) TheTVDBApi.Extended.getArtworkDetails(artworkId)
     */
    ArtworkDetails getArtworkDetails(long artworkId) throws APIException;

    /**
     * Returns basic information for a specific award category mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/award-categories/getAwardCategory">
     * <b>[GET]</b> /awards/categories/{id}</a>
     *
     * @param awardCategoryId The <i>TheTVDB.com</i> award category ID
     *
     * @return Basic award category information mapped as Java DTO based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no award category record with the given ID exists.
     * @see JSON#getAwardCategory(long) TheTVDBApi.JSON.getAwardCategory(awardCategoryId)
     * @see Extended#getAwardCategory(long) TheTVDBApi.Extended.getAwardCategory(awardCategoryId)
     */
    AwardCategory getAwardCategory(long awardCategoryId) throws APIException;

    /**
     * Returns detailed information for a specific award category mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/award-categories/getAwardCategoryExtended">
     * <b>[GET]</b> /awards/categories/{id}/extended</a>
     *
     * @param awardCategoryId The <i>TheTVDB.com</i> award category ID
     *
     * @return Detailed award category information mapped as Java DTO based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no award category record with the given ID exists.
     * @see JSON#getAwardCategoryDetails(long) TheTVDBApi.JSON.getAwardCategoryDetails(awardCategoryId)
     * @see Extended#getAwardCategoryDetails(long) TheTVDBApi.Extended.getAwardCategoryDetails(awardCategoryId)
     */
    AwardCategoryDetails getAwardCategoryDetails(long awardCategoryId) throws APIException;

    /**
     * Returns a collection of available awards mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/awards/getAllAwards">
     * <b>[GET]</b> /awards</a>
     *
     * @return Collection of available awards mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAllAwards()
     * @see Extended#getAllAwards()
     */
    Collection<Award> getAllAwards() throws APIException;

    /**
     * Returns basic information for a specific award mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/awards/getAward">
     * <b>[GET]</b> /awards/{id}</a>
     *
     * @param awardId The <i>TheTVDB.com</i> award ID
     *
     * @return Basic award information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no award record with the given ID exists.
     * @see JSON#getAward(long) TheTVDBApi.JSON.getAward(awardId)
     * @see Extended#getAward(long) TheTVDBApi.Extended.getAward(awardId)
     */
    Award getAward(long awardId) throws APIException;

    /**
     * Returns detailed information for a specific award mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/awards/getAwardExtended">
     * <b>[GET]</b> /awards/{id}/extended</a>
     *
     * @param awardId The <i>TheTVDB.com</i> award ID
     *
     * @return Detailed award information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no award record with the given ID exists.
     * @see JSON#getAwardDetails(long) TheTVDBApi.JSON.getAwardDetails(awardId)
     * @see Extended#getAwardDetails(long) TheTVDBApi.Extended.getAwardDetails(awardId)
     */
    AwardDetails getAwardDetails(long awardId) throws APIException;

    /**
     * Returns information for a specific character mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/characters/getCharacterBase">
     * <b>[GET]</b> /characters/{id}</a>
     *
     * @param characterId The <i>TheTVDB.com</i> character ID
     *
     * @return Character information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no character record with the given ID exists.
     * @see JSON#getCharacter(long) TheTVDBApi.JSON.getCharacter(characterId)
     * @see Extended#getCharacter(long) TheTVDBApi.Extended.getCharacter(characterId)
     */
    Character getCharacter(long characterId) throws APIException;

    /**
     * Returns a collection of companies based on the given query parameters mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/companies/getAllCompanies">
     * <b>[GET]</b> /companies</a>
     *
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return Collection of companies mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAllCompanies(QueryParameters) TheTVDBApi.JSON.getAllCompanies(queryParameters)
     * @see Extended#getAllCompanies(QueryParameters) TheTVDBApi.Extended.getAllCompanies(queryParameters)
     */
    Collection<Company> getAllCompanies(QueryParameters queryParameters) throws APIException;

    /**
     * Returns a limited collection of companies mapped as Java DTO. Due to the large amount of available companies, the
     * result will be paginated. Use the <em>{@code page}</em> parameter to browse to a specific result page. This is a
     * shortcut-method for {@link #getAllCompanies(QueryParameters) getAllCompanies(queryParameters)} with a single
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Companies#PAGE} query parameter.
     *
     * @param page The result page to be returned (zero-based)
     *
     * @return Collection of companies mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    Collection<Company> getAllCompanies(long page) throws APIException;

    /**
     * Returns a collection of available company types mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/companies/getCompanyTypes">
     * <b>[GET]</b> /companies/types</a>
     *
     * @return Collection of available company types mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getCompanyTypes()
     * @see Extended#getCompanyTypes()
     */
    Collection<CompanyType> getCompanyTypes() throws APIException;

    /**
     * Returns information for a specific company mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/companies/getCompany">
     * <b>[GET]</b> /companies/{id}</a>
     *
     * @param companyId The <i>TheTVDB.com</i> company ID
     *
     * @return Company information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no company record with the given ID exists.
     * @see JSON#getCompany(long) TheTVDBApi.JSON.getCompany(companyId)
     * @see Extended#getCompany(long) TheTVDBApi.Extended.getCompany(companyId)
     */
    Company getCompany(long companyId) throws APIException;

    /**
     * Returns a collection of available content ratings mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/content-ratings/getAllContentRatings">
     * <b>[GET]</b> /content/ratings</a>
     *
     * @return Collection of available content ratings mapped as Java DTO's based on the JSON data returned by the
     *         remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAllContentRatings()
     * @see Extended#getAllContentRatings()
     */
    Collection<ContentRating> getAllContentRatings() throws APIException;

    /**
     * Returns a collection of available entity types mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/entity-types/getEntityTypes">
     * <b>[GET]</b> /entities/types</a>
     *
     * @return Collection of available entity types mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getEntityTypes()
     * @see Extended#getEntityTypes()
     */
    Collection<EntityType> getEntityTypes() throws APIException;

    /**
     * Returns basic information for a specific episode mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/episodes/getEpisodeBase">
     * <b>[GET]</b> /episodes/{id}</a>
     *
     * @param episodeId The <i>TheTVDB.com</i> episode ID
     *
     * @return Basic episode information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no episode record with the given ID exists.
     * @see JSON#getEpisode(long) TheTVDBApi.JSON.getEpisode(episodeId)
     * @see Extended#getEpisode(long) TheTVDBApi.Extended.getEpisode(episodeId)
     */
    Episode getEpisode(long episodeId) throws APIException;

    /**
     * Returns detailed information for a specific episode mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/episodes/getEpisodeExtended">
     * <b>[GET]</b> /episodes/{id}/extended</a>
     *
     * @param episodeId The <i>TheTVDB.com</i> episode ID
     *
     * @return Detailed episode information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no episode record with the given ID exists.
     * @see JSON#getEpisodeDetails(long) TheTVDBApi.JSON.getEpisodeDetails(episodeId)
     * @see Extended#getEpisodeDetails(long) TheTVDBApi.Extended.getEpisodeDetails(episodeId)
     */
    EpisodeDetails getEpisodeDetails(long episodeId) throws APIException;

    /**
     * Returns a translation record for a specific episode mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/episodes/getEpisodeTranslation">
     * <b>[GET]</b> /episodes/{id}/translations/{language}</a>
     *
     * @param episodeId The <i>TheTVDB.com</i> episode ID
     * @param language  The 2- or 3-character language code
     *
     * @return Episode translation record mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no episode translation record exists for the given ID and language.
     * @see JSON#getEpisodeTranslation(long, String) TheTVDBApi.JSON.getEpisodeTranslation(episodeId, language)
     * @see Extended#getEpisodeTranslation(long, String) TheTVDBApi.Extended.getEpisodeTranslation(episodeId,
     *         language)
     */
    Translation getEpisodeTranslation(long episodeId, @Nonnull String language) throws APIException;

    /**
     * Returns a translation record for a specific list mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/lists/getListTranslation">
     * <b>[GET]</b> /lists/{id}/translations/{language}</a>
     *
     * @param listId   The <i>TheTVDB.com</i> list ID
     * @param language The 2- or 3-character language code
     *
     * @return List translation record mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no list translation record exists for the given ID and language.
     * @see JSON#getListTranslation(long, String) TheTVDBApi.JSON.getListTranslation(listId, language)
     * @see Extended#getListTranslation(long, String) TheTVDBApi.Extended.getListTranslation(listId, language)
     */
    Translation getListTranslation(long listId, @Nonnull String language) throws APIException;

    /**
     * Returns a collection of lists based on the given query parameters mapped as Java DTO. The collection contains
     * basic information of all lists matching the query parameters.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/lists/getAllLists">
     * <b>[GET]</b> /lists</a>
     *
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return Collection of lists mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAllLists(QueryParameters) TheTVDBApi.JSON.getAllLists(queryParameters)
     * @see Extended#getAllLists(QueryParameters) TheTVDBApi.Extended.getAllLists(queryParameters)
     */
    Collection<FCList> getAllLists(QueryParameters queryParameters) throws APIException;

    /**
     * Returns a limited collection of lists mapped as Java DTO. Due to the large amount of available lists, the result
     * will be paginated. Use the <em>{@code page}</em> parameter to browse to a specific result page. This is a
     * shortcut-method for {@link #getAllLists(QueryParameters) getAllLists(queryParameters)} with a single {@value
     * com.github.m0nk3y2k4.thetvdb.api.constants.Query.Lists#PAGE} query parameter.
     *
     * @param page The result page to be returned (zero-based)
     *
     * @return Collection of lists mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    Collection<FCList> getAllLists(long page) throws APIException;

    /**
     * Returns basic information for a specific list mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/lists/getList">
     * <b>[GET]</b> /lists/{id}</a>
     *
     * @param listId The <i>TheTVDB.com</i> list ID
     *
     * @return Basic list information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no list record with the given ID exists.
     * @see JSON#getList(long) TheTVDBApi.JSON.getList(listId)
     * @see Extended#getList(long) TheTVDBApi.Extended.getList(listId)
     */
    FCList getList(long listId) throws APIException;

    /**
     * Returns detailed information for a specific list mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/lists/getListExtended">
     * <b>[GET]</b> /lists/{id}/extended</a>
     *
     * @param listId The <i>TheTVDB.com</i> list ID
     *
     * @return Detailed list information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no list record with the given ID exists.
     * @see JSON#getListDetails(long) TheTVDBApi.JSON.getListDetails(listId)
     * @see Extended#getListDetails(long) TheTVDBApi.Extended.getListDetails(listId)
     */
    FCListDetails getListDetails(long listId) throws APIException;

    /**
     * Returns a collection of available genders mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/genders/getAllGenders">
     * <b>[GET]</b> /genders</a>
     *
     * @return Collection of available genders mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAllGenders()
     * @see Extended#getAllGenders()
     */
    Collection<Gender> getAllGenders() throws APIException;

    /**
     * Returns a collection of available genres mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/genres/getAllGenres">
     * <b>[GET]</b> /genres</a>
     *
     * @return Collection of available genres mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAllGenres()
     * @see Extended#getAllGenres()
     */
    Collection<Genre> getAllGenres() throws APIException;

    /**
     * Returns information for a specific genre mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/genres/getGenreBase">
     * <b>[GET]</b> /genres/{id}</a>
     *
     * @param genreId The <i>TheTVDB.com</i> genre ID
     *
     * @return Genre information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no genre record with the given ID exists.
     * @see JSON#getGenre(long) TheTVDBApi.JSON.getGenre(genreId)
     * @see Extended#getGenre(long) TheTVDBApi.Extended.getGenre(genreId)
     */
    Genre getGenre(long genreId) throws APIException;

    /**
     * Returns a collection of available movie statuses mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movie-statuses/getAllMovieStatuses">
     * <b>[GET]</b> /movies/statuses</a>
     *
     * @return Collection of available movie statuses mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAllMovieStatuses()
     * @see Extended#getAllMovieStatuses()
     */
    Collection<Status> getAllMovieStatuses() throws APIException;

    /**
     * Returns a collection of movies based on the given query parameters mapped as Java DTO. The collection contains
     * basic information of all movies matching the query parameters.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies/getAllMovie">
     * <b>[GET]</b> /movies</a>
     *
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return Collection of movies mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAllMovies(QueryParameters) TheTVDBApi.JSON.getAllMovies(queryParameters)
     * @see Extended#getAllMovies(QueryParameters) TheTVDBApi.Extended.getAllMovies(queryParameters)
     */
    Collection<Movie> getAllMovies(QueryParameters queryParameters) throws APIException;

    /**
     * Returns a limited collection of movies mapped as Java DTO. Due to the large amount of available movies, the
     * result will be paginated. Use the <em>{@code page}</em> parameter to browse to a specific result page. This is a
     * shortcut-method for {@link #getAllMovies(QueryParameters) getAllMovies(queryParameters)} with a single {@value
     * com.github.m0nk3y2k4.thetvdb.api.constants.Query.Movies#PAGE} query parameter.
     *
     * @param page The result page to be returned (zero-based)
     *
     * @return Collection of movies mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    Collection<Movie> getAllMovies(long page) throws APIException;

    /**
     * Returns basic information for a specific movie mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies/getMovieBase">
     * <b>[GET]</b> /movies/{id}</a>
     *
     * @param movieId The <i>TheTVDB.com</i> movie ID
     *
     * @return Basic movie information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no movie record with the given ID exists.
     * @see JSON#getMovie(long) TheTVDBApi.JSON.getMovie(movieId)
     * @see Extended#getMovie(long) TheTVDBApi.Extended.getMovie(movieId)
     */
    Movie getMovie(long movieId) throws APIException;

    /**
     * Returns detailed information for a specific movie mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies/getMovieExtended">
     * <b>[GET]</b> /movies/{id}/extended</a>
     *
     * @param movieId The <i>TheTVDB.com</i> movie ID
     *
     * @return Detailed movie information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no movie record with the given ID exists.
     * @see JSON#getMovieDetails(long) TheTVDBApi.JSON.getMovieDetails(movieId)
     * @see Extended#getMovieDetails(long) TheTVDBApi.Extended.getMovieDetails(movieId)
     */
    MovieDetails getMovieDetails(long movieId) throws APIException;

    /**
     * Returns a translation record for a specific movie mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies/getMovieTranslation">
     * <b>[GET]</b> /movies/{id}/translations/{language}</a>
     *
     * @param movieId  The <i>TheTVDB.com</i> movie ID
     * @param language The 2- or 3-character language code
     *
     * @return Movie translation record mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no movie translation record exists for the given ID and language.
     * @see JSON#getMovieTranslation(long, String) TheTVDBApi.JSON.getMovieTranslation(movieId, language)
     * @see Extended#getMovieTranslation(long, String) TheTVDBApi.Extended.getMovieTranslation(movieId, language)
     */
    Translation getMovieTranslation(long movieId, @Nonnull String language) throws APIException;

    /**
     * Returns a collection of available people types mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/people-types/getAllPeopleTypes">
     * <b>[GET]</b> /people/types</a>
     *
     * @return Collection of available people types mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAllPeopleTypes()
     * @see Extended#getAllPeopleTypes()
     */
    Collection<PeopleType> getAllPeopleTypes() throws APIException;

    /**
     * Returns basic information for a specific people mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/people/getPeopleBase">
     * <b>[GET]</b> /people/{id}</a>
     *
     * @param peopleId The <i>TheTVDB.com</i> people ID
     *
     * @return Basic people information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no people record with the given ID exists.
     * @see JSON#getPeople(long) TheTVDBApi.JSON.getPeople(peopleId)
     * @see Extended#getPeople(long) TheTVDBApi.Extended.getPeople(peopleId)
     */
    People getPeople(long peopleId) throws APIException;

    /**
     * Returns detailed information for a specific people mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/people/getPeopleExtended">
     * <b>[GET]</b> /people/{id}/extended</a>
     *
     * @param peopleId The <i>TheTVDB.com</i> people ID
     *
     * @return Detailed people information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no people record with the given ID exists.
     * @see JSON#getPeopleDetails(long) TheTVDBApi.JSON.getPeopleDetails(peopleId)
     * @see Extended#getPeopleDetails(long) TheTVDBApi.Extended.getPeopleDetails(peopleId)
     */
    PeopleDetails getPeopleDetails(long peopleId) throws APIException;

    /**
     * Returns basic information for a specific season mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/seasons/getSeasonBase">
     * <b>[GET]</b> /seasons/{id}</a>
     *
     * @param seasonId The <i>TheTVDB.com</i> season ID
     *
     * @return Basic season information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no season record with the given ID exists.
     * @see JSON#getSeason(long) TheTVDBApi.JSON.getSeason(seasonId)
     * @see Extended#getSeason(long) TheTVDBApi.Extended.getSeason(seasonId)
     */
    Season getSeason(long seasonId) throws APIException;

    /**
     * Returns detailed information for a specific season mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/seasons/getSeasonExtended">
     * <b>[GET]</b> /seasons/{id}/extended</a>
     *
     * @param seasonId The <i>TheTVDB.com</i> season ID
     *
     * @return Detailed season information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no season record with the given ID exists.
     * @see JSON#getSeasonDetails(long) TheTVDBApi.JSON.getSeasonDetails(seasonId)
     * @see Extended#getSeasonDetails(long) TheTVDBApi.Extended.getSeasonDetails(seasonId)
     */
    SeasonDetails getSeasonDetails(long seasonId) throws APIException;

    /**
     * Returns a collection of available season types mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/seasons/getSeasonTypes">
     * <b>[GET]</b> /seasons/types</a>
     *
     * @return Collection of available season types mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getSeasonTypes()
     * @see Extended#getSeasonTypes()
     */
    Collection<SeasonType> getSeasonTypes() throws APIException;

    /**
     * Returns a translation record for a specific season mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/seasons/getSeasonTranslation">
     * <b>[GET]</b> /seasons/{id}/translations/{language}</a>
     *
     * @param seasonId The <i>TheTVDB.com</i> season ID
     * @param language The 2- or 3-character language code
     *
     * @return Season translation record mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no season translation record exists for the given ID and language.
     * @see JSON#getSeasonTranslation(long, String) TheTVDBApi.JSON.getSeasonTranslation(seasonId, language)
     * @see Extended#getSeasonTranslation(long, String) TheTVDBApi.Extended.getSeasonTranslation(seasonId, language)
     */
    Translation getSeasonTranslation(long seasonId, @Nonnull String language) throws APIException;

    /**
     * Returns a collection of available series statuses mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series-statuses/getAllSeriesStatuses">
     * <b>[GET]</b> /series/statuses</a>
     *
     * @return Collection of available series statuses mapped as Java DTO's based on the JSON data returned by the
     *         remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAllSeriesStatuses()
     * @see Extended#getAllSeriesStatuses()
     */
    Collection<Status> getAllSeriesStatuses() throws APIException;

    /**
     * Returns a collection of series based on the given query parameters mapped as Java DTO. The collection contains
     * basic information of all series matching the query parameters.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series/getAllSeries">
     * <b>[GET]</b> /series</a>
     *
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return Collection of series mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAllSeries(QueryParameters) TheTVDBApi.JSON.getAllSeries(queryParameters)
     * @see Extended#getAllSeries(QueryParameters) TheTVDBApi.Extended.getAllSeries(queryParameters)
     */
    Collection<Series> getAllSeries(QueryParameters queryParameters) throws APIException;

    /**
     * Returns a limited collection of series mapped as Java DTO. Due to the large amount of available series, the
     * result will be paginated. Use the <em>{@code page}</em> parameter to browse to a specific result page. This is a
     * shortcut-method for {@link #getAllSeries(QueryParameters) getAllSeries(queryParameters)} with a single {@value
     * com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series#PAGE} query parameter.
     *
     * @param page The result page to be returned (zero-based)
     *
     * @return Collection of series mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    Collection<Series> getAllSeries(long page) throws APIException;

    /**
     * Returns basic information for a specific series mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series/getSeriesBase">
     * <b>[GET]</b> /series/{id}</a>
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     *
     * @return Basic series information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no series record with the given ID exists.
     * @see JSON#getSeries(long) TheTVDBApi.JSON.getSeries(seriesId)
     * @see Extended#getSeries(long) TheTVDBApi.Extended.getSeries(seriesId)
     */
    Series getSeries(long seriesId) throws APIException;

    /**
     * Returns detailed information for a specific series mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series/getSeriesExtended">
     * <b>[GET]</b> /series/{id}/extended</a>
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     *
     * @return Detailed series information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no series record with the given ID exists.
     * @see JSON#getSeriesDetails(long) TheTVDBApi.JSON.getSeriesDetails(seriesId)
     * @see Extended#getSeriesDetails(long) TheTVDBApi.Extended.getSeriesDetails(seriesId)
     */
    SeriesDetails getSeriesDetails(long seriesId) throws APIException;

    /**
     * Returns the episodes of a particular series based on the given query parameters mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series/getSeriesEpisodes">
     * <b>[GET]</b> /series/{id}/episodes/{season-type}</a>
     *
     * @param seriesId        The <i>TheTVDB.com</i> series ID
     * @param seasonType      The type of season for which episodes should be returned
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return A series episodes mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no series record with the given ID exists.
     * @see JSON#getSeriesEpisodes(long, SeriesSeasonType, QueryParameters) TheTVDBApi.JSON.getSeriesEpisodes(seriesId,
     *         seasonType, queryParameters)
     * @see Extended#getSeriesEpisodes(long, SeriesSeasonType, QueryParameters) TheTVDBApi.Extended.getSeriesEpisodes(seriesId,
     *         seasonType, queryParameters)
     */
    SeriesEpisodes getSeriesEpisodes(long seriesId, SeriesSeasonType seasonType, QueryParameters queryParameters)
            throws APIException;

    /**
     * Returns a limited overview of episodes of a particular series mapped as Java DTO. Since some series have many
     * episodes, the result will be paginated. This is a shortcut-method for {@link #getSeriesEpisodes(long,
     * SeriesSeasonType, QueryParameters) getAllSeries(seriesId, seasonType, queryParameters)} which will always return
     * the first page only what may be convenient for series with few episodes.
     *
     * @param seriesId   The <i>TheTVDB.com</i> series ID
     * @param seasonType The type of season for which episodes should be returned
     *
     * @return First page of a series episodes mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no series record with the given ID exists.
     * @see #getSeriesEpisodes(long, SeriesSeasonType, long) getSeriesEpisodes(seriesId, seasonType, page)
     */
    SeriesEpisodes getSeriesEpisodes(long seriesId, SeriesSeasonType seasonType) throws APIException;

    /**
     * Returns a limited overview of episodes of a particular series mapped as Java DTO. Since some series have many
     * episodes, the result will be paginated. Use the <em>{@code page}</em> parameter to browse to a specific result
     * page. This is a shortcut-method for {@link #getSeriesEpisodes(long, SeriesSeasonType, QueryParameters)
     * getAllSeries(seriesId, seasonType, queryParameters)} with a single {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series#PAGE}
     * query parameter.
     *
     * @param seriesId   The <i>TheTVDB.com</i> series ID
     * @param seasonType The type of season for which episodes should be returned
     * @param page       The result page to be returned (zero-based)
     *
     * @return A series episodes mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no series record with the given ID exists.
     * @see #getSeriesEpisodes(long, SeriesSeasonType) getSeriesEpisodes(seriesId, seasonType)
     */
    SeriesEpisodes getSeriesEpisodes(long seriesId, SeriesSeasonType seasonType, long page) throws APIException;

    /**
     * Returns a translation record for a specific series mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series/getSeriesTranslation">
     * <b>[GET]</b> /series/{id}/translations/{language}</a>
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     * @param language The 2- or 3-character language code
     *
     * @return Series translation record mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no series translation record exists for the given ID and language.
     * @see JSON#getSeriesTranslation(long, String) TheTVDBApi.JSON.getSeriesTranslation(seriesId, language)
     * @see Extended#getSeriesTranslation(long, String) TheTVDBApi.Extended.getSeriesTranslation(seriesId, language)
     */
    Translation getSeriesTranslation(long seriesId, @Nonnull String language) throws APIException;

    /**
     * Returns a collection of available source types mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/source-types/getAllSourceTypes">
     * <b>[GET]</b> /sources/types</a>
     *
     * @return Collection of available source types mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAllSourceTypes()
     * @see Extended#getAllSourceTypes()
     */
    Collection<SourceType> getAllSourceTypes() throws APIException;

    /**
     * Returns a collection of recently updated entities based on the given query parameters mapped as Java DTO. The
     * collection contains basic information of all entities matching the query parameters.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/updates/updates">
     * <b>[GET]</b> /updates</a>
     *
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return Collection of updated entities mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getUpdates(QueryParameters) TheTVDBApi.JSON.getUpdates(queryParameters)
     * @see Extended#getUpdates(QueryParameters) TheTVDBApi.Extended.getUpdates(queryParameters)
     */
    Collection<EntityUpdate> getUpdates(QueryParameters queryParameters) throws APIException;

    /**
     * Returns a collection of entities which have been updated since the given Epoch time mapped as Java DTO. This is a
     * shortcut-method for {@link #getUpdates(QueryParameters) getUpdates(queryParameters)} with a single {@value
     * com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates#SINCE} query parameter.
     *
     * @param since UNIX Epoch time (GMT) in seconds. Must be in the past.
     *
     * @return Collection of entities updated since the given Epoch time mapped as Java DTO's based on the JSON data
     *         returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or the timestamp lies in the future.
     */
    Collection<EntityUpdate> getUpdates(long since) throws APIException;

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
         * Returns a collection of available artwork statuses as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/artwork-statuses/getAllArtworkStatuses">
         * <b>[GET]</b> /artwork/statuses</a>
         *
         * @return JSON object containing the available artwork statuses
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAllArtworkStatuses() TheTVDBApi.getAllArtworkStatuses()
         * @see Extended#getAllArtworkStatuses()
         */
        JsonNode getAllArtworkStatuses() throws APIException;

        /**
         * Returns a collection of available artwork types as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/artwork-types/getAllArtworkTypes">
         * <b>[GET]</b> /artwork/types</a>
         *
         * @return JSON object containing the available artwork types
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAllArtworkTypes() TheTVDBApi.getAllArtworkTypes()
         * @see Extended#getAllArtworkTypes()
         */
        JsonNode getAllArtworkTypes() throws APIException;

        /**
         * Returns basic information for a specific artwork as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/artwork/getArtworkBase">
         * <b>[GET]</b> /artwork/{id}</a>
         *
         * @param artworkId The <i>TheTVDB.com</i> artwork ID
         *
         * @return JSON object containing basic information for a specific artwork
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no artwork record with the given ID exists.
         * @see TheTVDBApi#getArtwork(long) TheTVDBApi.getArtwork(artworkId)
         * @see Extended#getArtwork(long) TheTVDBApi.Extended.getArtwork(artworkId)
         */
        JsonNode getArtwork(long artworkId) throws APIException;

        /**
         * Returns detailed information for a specific artwork as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/artwork/getArtworkExtended">
         * <b>[GET]</b> /artwork/{id}/extended</a>
         *
         * @param artworkId The <i>TheTVDB.com</i> artwork ID
         *
         * @return JSON object containing detailed information for a specific artwork
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no artwork record with the given ID exists.
         * @see TheTVDBApi#getArtworkDetails(long) TheTVDBApi.getArtworkDetails(artworkId)
         * @see Extended#getArtworkDetails(long) TheTVDBApi.Extended.getArtworkDetails(artworkId)
         */
        JsonNode getArtworkDetails(long artworkId) throws APIException;

        /**
         * Returns basic information for a specific award category as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/award-categories/getAwardCategory">
         * <b>[GET]</b> /awards/categories/{id}</a>
         *
         * @param awardCategoryId The <i>TheTVDB.com</i> award category ID
         *
         * @return JSON object containing basic information for a specific award category
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no award category record with the given ID exists.
         * @see TheTVDBApi#getAwardCategory(long) TheTVDBApi.getAwardCategory(awardCategoryId)
         * @see Extended#getAwardCategory(long) TheTVDBApi.Extended.getAwardCategory(awardCategoryId)
         */
        JsonNode getAwardCategory(long awardCategoryId) throws APIException;

        /**
         * Returns detailed information for a specific award category as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/award-categories/getAwardCategoryExtended">
         * <b>[GET]</b> /awards/categories/{id}/extended</a>
         *
         * @param awardCategoryId The <i>TheTVDB.com</i> award category ID
         *
         * @return JSON object containing detailed information for a specific award category
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no award category record with the given ID exists.
         * @see TheTVDBApi#getAwardCategoryDetails(long) TheTVDBApi.getAwardCategoryDetails(awardCategoryId)
         * @see Extended#getAwardCategoryDetails(long) TheTVDBApi.Extended.getAwardCategoryDetails(awardCategoryId)
         */
        JsonNode getAwardCategoryDetails(long awardCategoryId) throws APIException;

        /**
         * Returns a collection of available awards as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/awards/getAllAwards">
         * <b>[GET]</b> /awards</a>
         *
         * @return JSON object containing the available awards
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAllAwards() TheTVDBApi.getAllAwards()
         * @see Extended#getAllAwards()
         */
        JsonNode getAllAwards() throws APIException;

        /**
         * Returns basic information for a specific award as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/awards/getAward">
         * <b>[GET]</b> /awards/{id}</a>
         *
         * @param awardId The <i>TheTVDB.com</i> award ID
         *
         * @return JSON object containing basic information for a specific award
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no award record with the given ID exists.
         * @see TheTVDBApi#getAward(long) TheTVDBApi.getAward(awardId)
         * @see Extended#getAward(long) TheTVDBApi.Extended.getAward(awardId)
         */
        JsonNode getAward(long awardId) throws APIException;

        /**
         * Returns detailed information for a specific award as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/awards/getAwardExtended">
         * <b>[GET]</b> /awards/{id}/extended</a>
         *
         * @param awardId The <i>TheTVDB.com</i> award ID
         *
         * @return JSON object containing detailed information for a specific award
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no award record with the given ID exists.
         * @see TheTVDBApi#getAwardDetails(long) TheTVDBApi.getAwardDetails(awardId)
         * @see Extended#getAwardDetails(long) TheTVDBApi.Extended.getAwardDetails(awardId)
         */
        JsonNode getAwardDetails(long awardId) throws APIException;

        /**
         * Returns information for a specific character as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/characters/getCharacterBase">
         * <b>[GET]</b> /characters/{id}</a>
         *
         * @param characterId The <i>TheTVDB.com</i> character ID
         *
         * @return JSON object containing information for a specific character
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no character record with the given ID exists.
         * @see TheTVDBApi#getCharacter(long) TheTVDBApi.getCharacter(characterId)
         * @see Extended#getCharacter(long) TheTVDBApi.Extended.getCharacter(characterId)
         */
        JsonNode getCharacter(long characterId) throws APIException;

        /**
         * Returns a collection of companies based on the given query parameters as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/companies/getAllCompanies">
         * <b>[GET]</b> /companies</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing the companies
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAllCompanies(QueryParameters) TheTVDBApi.getAllCompanies(queryParameters)
         * @see Extended#getAllCompanies(QueryParameters) TheTVDBApi.Extended.getAllCompanies(queryParameters)
         */
        JsonNode getAllCompanies(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a collection of available company types as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/companies/getCompanyTypes">
         * <b>[GET]</b> /companies/types</a>
         *
         * @return JSON object containing the available company types
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getCompanyTypes() TheTVDBApi.getCompanyTypes()
         * @see Extended#getCompanyTypes()
         */
        JsonNode getCompanyTypes() throws APIException;

        /**
         * Returns information for a specific company as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/companies/getCompany">
         * <b>[GET]</b> /companies/{id}</a>
         *
         * @param companyId The <i>TheTVDB.com</i> company ID
         *
         * @return JSON object containing information for a specific company
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no company record with the given ID exists.
         * @see TheTVDBApi#getCompany(long) TheTVDBApi.getCompany(companyId)
         * @see Extended#getCompany(long) TheTVDBApi.Extended.getCompany(companyId)
         */
        JsonNode getCompany(long companyId) throws APIException;

        /**
         * Returns a collection of available content ratings as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/content-ratings/getAllContentRatings">
         * <b>[GET]</b> /content/ratings</a>
         *
         * @return JSON object containing the available content ratings
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAllContentRatings() TheTVDBApi.getAllContentRatings()
         * @see Extended#getAllContentRatings()
         */
        JsonNode getAllContentRatings() throws APIException;

        /**
         * Returns a collection of available entity types as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/entity-types/getEntityTypes">
         * <b>[GET]</b> /entities/types</a>
         *
         * @return JSON object containing the available entity types
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getEntityTypes() TheTVDBApi.getEntityTypes()
         * @see Extended#getEntityTypes()
         */
        JsonNode getEntityTypes() throws APIException;

        /**
         * Returns basic information for a specific episode as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/episodes/getEpisodeBase">
         * <b>[GET]</b> /episodes/{id}</a>
         *
         * @param episodeId The <i>TheTVDB.com</i> episode ID
         *
         * @return JSON object containing basic information for a specific episode
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no episode record with the given ID exists.
         * @see TheTVDBApi#getEpisode(long) TheTVDBApi.getEpisode(episodeId)
         * @see Extended#getEpisode(long) TheTVDBApi.Extended.getEpisode(episodeId)
         */
        JsonNode getEpisode(long episodeId) throws APIException;

        /**
         * Returns detailed information for a specific episode as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/episodes/getEpisodeExtended">
         * <b>[GET]</b> /episodes/{id}/extended</a>
         *
         * @param episodeId The <i>TheTVDB.com</i> episode ID
         *
         * @return JSON object containing detailed information for a specific episode
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no episode record with the given ID exists.
         * @see TheTVDBApi#getEpisodeDetails(long) TheTVDBApi.getEpisodeDetails(episodeId)
         * @see Extended#getEpisodeDetails(long) TheTVDBApi.Extended.getEpisodeDetails(episodeId)
         */
        JsonNode getEpisodeDetails(long episodeId) throws APIException;

        /**
         * Returns a translation record for a specific episode as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/episodes/getEpisodeTranslation">
         * <b>[GET]</b> /episodes/{id}/translations/{language}</a>
         *
         * @param episodeId The <i>TheTVDB.com</i> episode ID
         * @param language  The 2- or 3-character language code
         *
         * @return JSON object containing a translation record for a specific episode
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no episode translation record exists for the given ID and
         *                      language.
         * @see TheTVDBApi#getEpisodeTranslation(long, String) TheTVDBApi.getEpisodeTranslation(episodeId, language)
         * @see Extended#getEpisodeTranslation(long, String) TheTVDBApi.Extended.getEpisodeTranslation(episodeId,
         *         language)
         */
        JsonNode getEpisodeTranslation(long episodeId, @Nonnull String language) throws APIException;

        /**
         * Returns a translation record for a specific list as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/lists/getListTranslation">
         * <b>[GET]</b> /lists/{id}/translations/{language}</a>
         *
         * @param listId   The <i>TheTVDB.com</i> list ID
         * @param language The 2- or 3-character language code
         *
         * @return JSON object containing a translation record for a specific list
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no list translation record exists for the given ID and
         *                      language.
         * @see TheTVDBApi#getListTranslation(long, String) TheTVDBApi.getListTranslation(listId, language)
         * @see Extended#getListTranslation(long, String) TheTVDBApi.Extended.getListTranslation(listId, language)
         */
        JsonNode getListTranslation(long listId, @Nonnull String language) throws APIException;

        /**
         * Returns a collection of lists based on the given query parameters as raw JSON. It contains basic information
         * of all lists matching the query parameters.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/lists/getAllLists">
         * <b>[GET]</b> /lists</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing the lists
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAllLists(QueryParameters) TheTVDBApi.getAllLists(queryParameters)
         * @see Extended#getAllLists(QueryParameters) TheTVDBApi.Extended.getAllLists(queryParameters)
         */
        JsonNode getAllLists(QueryParameters queryParameters) throws APIException;

        /**
         * Returns basic information for a specific list as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/lists/getList">
         * <b>[GET]</b> /lists/{id}</a>
         *
         * @param listId The <i>TheTVDB.com</i> list ID
         *
         * @return JSON object containing basic information for a specific list
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no list record with the given ID exists.
         * @see TheTVDBApi#getList(long) TheTVDBApi.getList(listId)
         * @see Extended#getList(long) TheTVDBApi.Extended.getList(listId)
         */
        JsonNode getList(long listId) throws APIException;

        /**
         * Returns detailed information for a specific list as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/lists/getListExtended">
         * <b>[GET]</b> /lists/{id}/extended</a>
         *
         * @param listId The <i>TheTVDB.com</i> list ID
         *
         * @return JSON object containing detailed information for a specific list
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no list record with the given ID exists.
         * @see TheTVDBApi#getListDetails(long) TheTVDBApi.getListDetails(listId)
         * @see Extended#getListDetails(long) TheTVDBApi.Extended.getListDetails(listId)
         */
        JsonNode getListDetails(long listId) throws APIException;

        /**
         * Returns a collection of available genders as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/genders/getAllGenders">
         * <b>[GET]</b> /genders</a>
         *
         * @return JSON object containing the available genders
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAllGenders() TheTVDBApi.getAllGenders()
         * @see Extended#getAllGenders()
         */
        JsonNode getAllGenders() throws APIException;

        /**
         * Returns a collection of available genres as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/genres/getAllGenres">
         * <b>[GET]</b> /genres</a>
         *
         * @return JSON object containing the available genres
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAllGenres() TheTVDBApi.getAllGenres()
         * @see Extended#getAllGenres()
         */
        JsonNode getAllGenres() throws APIException;

        /**
         * Returns information for a specific genre as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/genres/getGenreBase">
         * <b>[GET]</b> /genres/{id}</a>
         *
         * @param genreId The <i>TheTVDB.com</i> genre ID
         *
         * @return JSON object containing information for a specific genre
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no genre record with the given ID exists.
         * @see TheTVDBApi#getGenre(long) TheTVDBApi.getGenre(genreId)
         * @see Extended#getGenre(long) TheTVDBApi.Extended.getGenre(genreId)
         */
        JsonNode getGenre(long genreId) throws APIException;

        /**
         * Returns a collection of available movie statuses as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movie-statuses/getAllMovieStatuses">
         * <b>[GET]</b> /movies/statuses</a>
         *
         * @return JSON object containing the available movie statuses
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAllMovieStatuses() TheTVDBApi.getAllMovieStatuses()
         * @see Extended#getAllMovieStatuses()
         */
        JsonNode getAllMovieStatuses() throws APIException;

        /**
         * Returns a collection of movies based on the given query parameters as raw JSON. It contains basic information
         * of all movies matching the query parameters.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies/getAllMovie">
         * <b>[GET]</b> /movies</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing the movies
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAllMovies(QueryParameters) TheTVDBApi.getAllMovies(queryParameters)
         * @see Extended#getAllMovies(QueryParameters) TheTVDBApi.Extended.getAllMovies(queryParameters)
         */
        JsonNode getAllMovies(QueryParameters queryParameters) throws APIException;

        /**
         * Returns basic information for a specific movie as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies/getMovieBase">
         * <b>[GET]</b> /movies/{id}</a>
         *
         * @param movieId The <i>TheTVDB.com</i> movie ID
         *
         * @return JSON object containing basic information for a specific movie
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no movie record with the given ID exists.
         * @see TheTVDBApi#getMovie(long) TheTVDBApi.getMovie(movieId)
         * @see Extended#getMovie(long) TheTVDBApi.Extended.getMovie(movieId)
         */
        JsonNode getMovie(long movieId) throws APIException;

        /**
         * Returns detailed information for a specific movie as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies/getMovieExtended">
         * <b>[GET]</b> /movies/{id}/extended</a>
         *
         * @param movieId The <i>TheTVDB.com</i> movie ID
         *
         * @return JSON object containing detailed information for a specific movie
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no movie record with the given ID exists.
         * @see TheTVDBApi#getMovieDetails(long) TheTVDBApi.getMovieDetails(movieId)
         * @see Extended#getMovieDetails(long) TheTVDBApi.Extended.getMovieDetails(movieId)
         */
        JsonNode getMovieDetails(long movieId) throws APIException;

        /**
         * Returns a translation record for a specific movie as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies/getMovieTranslation">
         * <b>[GET]</b> /movies/{id}/translations/{language}</a>
         *
         * @param movieId  The <i>TheTVDB.com</i> movie ID
         * @param language The 2- or 3-character language code
         *
         * @return JSON object containing a translation record for a specific movie
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no movie translation record exists for the given ID and
         *                      language.
         * @see TheTVDBApi#getMovieTranslation(long, String) TheTVDBApi.getMovieTranslation(movieId, language)
         * @see Extended#getMovieTranslation(long, String) TheTVDBApi.Extended.getMovieTranslation(movieId,
         *         language)
         */
        JsonNode getMovieTranslation(long movieId, @Nonnull String language) throws APIException;

        /**
         * Returns a collection of available people types as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/people-types/getAllPeopleTypes">
         * <b>[GET]</b> /people/types</a>
         *
         * @return JSON object containing the available people types
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAllPeopleTypes() TheTVDBApi.getAllPeopleTypes()
         * @see Extended#getAllPeopleTypes()
         */
        JsonNode getAllPeopleTypes() throws APIException;

        /**
         * Returns basic information for a specific people as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/people/getPeopleBase">
         * <b>[GET]</b> /people/{id}</a>
         *
         * @param peopleId The <i>TheTVDB.com</i> people ID
         *
         * @return JSON object containing basic information for a specific people
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no people record with the given ID exists.
         * @see TheTVDBApi#getPeople(long) TheTVDBApi.getPeople(peopleId)
         * @see Extended#getPeople(long) TheTVDBApi.Extended.getPeople(peopleId)
         */
        JsonNode getPeople(long peopleId) throws APIException;

        /**
         * Returns detailed information for a specific people as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/people/getPeopleExtended">
         * <b>[GET]</b> /people/{id}/extended</a>
         *
         * @param peopleId The <i>TheTVDB.com</i> people ID
         *
         * @return JSON object containing detailed information for a specific people
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no people record with the given ID exists.
         * @see TheTVDBApi#getPeopleDetails(long) TheTVDBApi.getPeopleDetails(peopleId)
         * @see Extended#getPeopleDetails(long) TheTVDBApi.Extended.getPeopleDetails(peopleId)
         */
        JsonNode getPeopleDetails(long peopleId) throws APIException;

        /**
         * Returns basic information for a specific season as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/seasons/getSeasonBase">
         * <b>[GET]</b> /seasons/{id}</a>
         *
         * @param seasonId The <i>TheTVDB.com</i> season ID
         *
         * @return JSON object containing basic information for a specific season
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no season record with the given ID exists.
         * @see TheTVDBApi#getSeason(long) TheTVDBApi.getSeason(seasonId)
         * @see Extended#getSeason(long) TheTVDBApi.Extended.getSeason(seasonId)
         */
        JsonNode getSeason(long seasonId) throws APIException;

        /**
         * Returns detailed information for a specific season as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/seasons/getSeasonExtended">
         * <b>[GET]</b> /seasons/{id}/extended</a>
         *
         * @param seasonId The <i>TheTVDB.com</i> season ID
         *
         * @return JSON object containing detailed information for a specific season
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no season record with the given ID exists.
         * @see TheTVDBApi#getSeasonDetails(long) TheTVDBApi.getSeasonDetails(seasonId)
         * @see Extended#getSeasonDetails(long) TheTVDBApi.Extended.getSeasonDetails(seasonId)
         */
        JsonNode getSeasonDetails(long seasonId) throws APIException;

        /**
         * Returns a collection of available season types as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/seasons/getSeasonTypes">
         * <b>[GET]</b> /seasons/types</a>
         *
         * @return JSON object containing the available season types
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getSeasonTypes() TheTVDBApi.getSeasonTypes()
         * @see Extended#getSeasonTypes()
         */
        JsonNode getSeasonTypes() throws APIException;

        /**
         * Returns a translation record for a specific season as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/seasons/getSeasonTranslation">
         * <b>[GET]</b> /seasons/{id}/translations/{language}</a>
         *
         * @param seasonId The <i>TheTVDB.com</i> season ID
         * @param language The 2- or 3-character language code
         *
         * @return JSON object containing a translation record for a specific season
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no season translation record exists for the given ID and
         *                      language.
         * @see TheTVDBApi#getSeasonTranslation(long, String) TheTVDBApi.getSeasonTranslation(seasonId, language)
         * @see Extended#getSeasonTranslation(long, String) TheTVDBApi.Extended.getSeasonTranslation(seasonId,
         *         language)
         */
        JsonNode getSeasonTranslation(long seasonId, @Nonnull String language) throws APIException;

        /**
         * Returns a collection of available series statuses as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series-statuses/getAllSeriesStatuses">
         * <b>[GET]</b> /series/statuses</a>
         *
         * @return JSON object containing the available series statuses
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAllSeriesStatuses() TheTVDBApi.getAllSeriesStatuses()
         * @see Extended#getAllSeriesStatuses()
         */
        JsonNode getAllSeriesStatuses() throws APIException;

        /**
         * Returns a collection of series based on the given query parameters as raw JSON. It contains basic information
         * of all series matching the query parameters.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series/getAllSeries">
         * <b>[GET]</b> /series</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing the series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAllSeries(QueryParameters) TheTVDBApi.getAllSeries(queryParameters)
         * @see Extended#getAllSeries(QueryParameters) TheTVDBApi.Extended.getAllSeries(queryParameters)
         */
        JsonNode getAllSeries(QueryParameters queryParameters) throws APIException;

        /**
         * Returns basic information for a specific series as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series/getSeriesBase">
         * <b>[GET]</b> /series/{id}</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return JSON object containing basic information for a specific series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no series record with the given ID exists.
         * @see TheTVDBApi#getSeries(long) TheTVDBApi.getSeries(seriesId)
         * @see Extended#getSeries(long) TheTVDBApi.Extended.getSeries(seriesId)
         */
        JsonNode getSeries(long seriesId) throws APIException;

        /**
         * Returns detailed information for a specific series as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series/getSeriesExtended">
         * <b>[GET]</b> /series/{id}/extended</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return JSON object containing detailed information for a specific series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no series record with the given ID exists.
         * @see TheTVDBApi#getSeriesDetails(long) TheTVDBApi.getSeriesDetails(seriesId)
         * @see Extended#getSeriesDetails(long) TheTVDBApi.Extended.getSeriesDetails(seriesId)
         */
        JsonNode getSeriesDetails(long seriesId) throws APIException;

        /**
         * Returns the episodes of a particular series based on the given query parameters as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series/getSeriesEpisodes">
         * <b>[GET]</b> /series/{id}/episodes/{season-type}</a>
         *
         * @param seriesId        The <i>TheTVDB.com</i> series ID
         * @param seasonType      The type of season for which episodes should be returned
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing a series episodes
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no series record with the given ID exists.
         * @see TheTVDBApi#getSeriesEpisodes(long, SeriesSeasonType, QueryParameters)
         *         TheTVDBApi.getSeriesEpisodes(seriesId, seasonType, queryParameters)
         * @see Extended#getSeriesEpisodes(long, SeriesSeasonType, QueryParameters) TheTVDBApi.Extended.getSeriesEpisodes(seriesId,
         *         seasonType, queryParameters)
         */
        JsonNode getSeriesEpisodes(long seriesId, SeriesSeasonType seasonType, QueryParameters queryParameters)
                throws APIException;

        /**
         * Returns a translation record for a specific series as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series/getSeriesTranslation">
         * <b>[GET]</b> /series/{id}/translations/{language}</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         * @param language The 2- or 3-character language code
         *
         * @return JSON object containing a translation record for a specific series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no series translation record exists for the given ID and
         *                      language.
         * @see TheTVDBApi#getSeriesTranslation(long, String) TheTVDBApi.getSeriesTranslation(seriesId, language)
         * @see Extended#getSeriesTranslation(long, String) TheTVDBApi.Extended.getSeriesTranslation(seriesId,
         *         language)
         */
        JsonNode getSeriesTranslation(long seriesId, @Nonnull String language) throws APIException;

        /**
         * Returns a collection of available source types as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/source-types/getAllSourceTypes">
         * <b>[GET]</b> /sources/types</a>
         *
         * @return JSON object containing the available source types
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAllSourceTypes() TheTVDBApi.getAllSourceTypes()
         * @see Extended#getAllSourceTypes()
         */
        JsonNode getAllSourceTypes() throws APIException;

        /**
         * Returns a collection of recently updated entities based on the given query parameters as raw JSON. It
         * contains basic information of all entities matching the query parameters.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/updates/updates">
         * <b>[GET]</b> /updates</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing the updated entities
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getUpdates(QueryParameters) TheTVDBApi.getUpdates(queryParameters)
         * @see Extended#getUpdates(QueryParameters) TheTVDBApi.Extended.getUpdates(queryParameters)
         */
        JsonNode getUpdates(QueryParameters queryParameters) throws APIException;
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
         * Returns a response object containing a collection of available artwork statuses mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/artwork-statuses/getAllArtworkStatuses">
         * <b>[GET]</b> /artwork/statuses</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAllArtworkStatuses()
         * @see TheTVDBApi#getAllArtworkStatuses() TheTVDBApi.getAllArtworkStatuses()
         */
        APIResponse<Collection<ArtworkStatus>> getAllArtworkStatuses() throws APIException;

        /**
         * Returns a response object containing a collection of available artwork types mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/artwork-types/getAllArtworkTypes">
         * <b>[GET]</b> /artwork/types</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAllArtworkTypes()
         * @see TheTVDBApi#getAllArtworkTypes() TheTVDBApi.getAllArtworkTypes()
         */
        APIResponse<Collection<ArtworkType>> getAllArtworkTypes() throws APIException;

        /**
         * Returns a response object containing basic information for a specific artwork mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/artwork/getArtworkBase">
         * <b>[GET]</b> /artwork/{id}</a>
         *
         * @param artworkId The <i>TheTVDB.com</i> artwork ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no artwork record with the given ID exists.
         * @see JSON#getArtwork(long) TheTVDBApi.JSON.getArtwork(artworkId)
         * @see TheTVDBApi#getArtwork(long) TheTVDBApi.getArtwork(artworkId)
         */
        APIResponse<Artwork> getArtwork(long artworkId) throws APIException;

        /**
         * Returns a response object containing detailed information for a specific artwork mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/artwork/getArtworkExtended">
         * <b>[GET]</b> /artwork/{id}/extended</a>
         *
         * @param artworkId The <i>TheTVDB.com</i> artwork ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no artwork record with the given ID exists.
         * @see JSON#getArtworkDetails(long) TheTVDBApi.JSON.getArtworkDetails(artworkId)
         * @see TheTVDBApi#getArtworkDetails(long) TheTVDBApi.getArtworkDetails(artworkId)
         */
        APIResponse<ArtworkDetails> getArtworkDetails(long artworkId) throws APIException;

        /**
         * Returns a response object containing basic information for a specific award category mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/award-categories/getAwardCategory">
         * <b>[GET]</b> /awards/categories/{id}</a>
         *
         * @param awardCategoryId The <i>TheTVDB.com</i> award category ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no award category record with the given ID exists.
         * @see JSON#getAwardCategory(long) TheTVDBApi.JSON.getAwardCategory(awardCategoryId)
         * @see TheTVDBApi#getAwardCategory(long) TheTVDBApi.getAwardCategory(awardCategoryId)
         */
        APIResponse<AwardCategory> getAwardCategory(long awardCategoryId) throws APIException;

        /**
         * Returns a response object containing detailed information for a specific award category mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/award-categories/getAwardCategoryExtended">
         * <b>[GET]</b> /awards/categories/{id}/extended</a>
         *
         * @param awardCategoryId The <i>TheTVDB.com</i> award category ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no award category record with the given ID exists.
         * @see JSON#getAwardCategoryDetails(long) TheTVDBApi.JSON.getAwardCategoryDetails(awardCategoryId)
         * @see TheTVDBApi#getAwardCategoryDetails(long) TheTVDBApi.getAwardCategoryDetails(awardCategoryId)
         */
        APIResponse<AwardCategoryDetails> getAwardCategoryDetails(long awardCategoryId) throws APIException;

        /**
         * Returns a response object containing a collection of available awards mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/awards/getAllAwards">
         * <b>[GET]</b> /awards</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAllAwards()
         * @see TheTVDBApi#getAllAwards() TheTVDBApi.getAllAwards()
         */
        APIResponse<Collection<Award>> getAllAwards() throws APIException;

        /**
         * Returns a response object containing basic information for a specific award mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/awards/getAward">
         * <b>[GET]</b> /awards/{id}</a>
         *
         * @param awardId The <i>TheTVDB.com</i> award ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no award record with the given ID exists.
         * @see JSON#getAward(long) TheTVDBApi.JSON.getAward(awardId)
         * @see TheTVDBApi#getAward(long) TheTVDBApi.getAward(awardId)
         */
        APIResponse<Award> getAward(long awardId) throws APIException;

        /**
         * Returns a response object containing detailed information for a specific award mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/awards/getAwardExtended">
         * <b>[GET]</b> /awards/{id}/extended</a>
         *
         * @param awardId The <i>TheTVDB.com</i> award ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no award record with the given ID exists.
         * @see JSON#getAwardDetails(long) TheTVDBApi.JSON.getAwardDetails(awardId)
         * @see TheTVDBApi#getAwardDetails(long) TheTVDBApi.getAwardDetails(awardId)
         */
        APIResponse<AwardDetails> getAwardDetails(long awardId) throws APIException;

        /**
         * Returns a response object containing information for a specific character mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/characters/getCharacterBase">
         * <b>[GET]</b> /characters/{id}</a>
         *
         * @param characterId The <i>TheTVDB.com</i> character ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no character record with the given ID exists.
         * @see JSON#getCharacter(long) TheTVDBApi.JSON.getCharacter(characterId)
         * @see TheTVDBApi#getCharacter(long) TheTVDBApi.getCharacter(characterId)
         */
        APIResponse<Character> getCharacter(long characterId) throws APIException;

        /**
         * Returns a response object containing a collection of companies based on the given query parameters mapped as
         * Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/companies/getAllCompanies">
         * <b>[GET]</b> /companies</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAllCompanies(QueryParameters) TheTVDBApi.JSON.getAllCompanies(queryParameters)
         * @see TheTVDBApi#getAllCompanies(QueryParameters) TheTVDBApi.getAllCompanies(queryParameters)
         */
        APIResponse<Collection<Company>> getAllCompanies(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a collection of available company types mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/companies/getCompanyTypes">
         * <b>[GET]</b> /companies/types</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getCompanyTypes()
         * @see TheTVDBApi#getCompanyTypes() TheTVDBApi.getCompanyTypes()
         */
        APIResponse<Collection<CompanyType>> getCompanyTypes() throws APIException;

        /**
         * Returns a response object containing information for a specific company mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/companies/getCompany">
         * <b>[GET]</b> /companies/{id}</a>
         *
         * @param companyId The <i>TheTVDB.com</i> company ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no company record with the given ID exists.
         * @see JSON#getCompany(long) TheTVDBApi.JSON.getCompany(companyId)
         * @see TheTVDBApi#getCompany(long) TheTVDBApi.getCompany(companyId)
         */
        APIResponse<Company> getCompany(long companyId) throws APIException;

        /**
         * Returns a response object containing a collection of available content ratings mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/content-ratings/getAllContentRatings">
         * <b>[GET]</b> /content/ratings</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAllContentRatings()
         * @see TheTVDBApi#getAllContentRatings() TheTVDBApi.getAllContentRatings()
         */
        APIResponse<Collection<ContentRating>> getAllContentRatings() throws APIException;

        /**
         * Returns a response object containing a collection of available entity types mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/entity-types/getEntityTypes">
         * <b>[GET]</b> /entities/types</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getEntityTypes()
         * @see TheTVDBApi#getEntityTypes() TheTVDBApi.getEntityTypes()
         */
        APIResponse<Collection<EntityType>> getEntityTypes() throws APIException;

        /**
         * Returns a response object containing basic information for a specific episode mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/episodes/getEpisodeBase">
         * <b>[GET]</b> /episodes/{id}</a>
         *
         * @param episodeId The <i>TheTVDB.com</i> episode ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no episode record with the given ID exists.
         * @see JSON#getEpisode(long) TheTVDBApi.JSON.getEpisode(episodeId)
         * @see TheTVDBApi#getEpisode(long) TheTVDBApi.getEpisode(episodeId)
         */
        APIResponse<Episode> getEpisode(long episodeId) throws APIException;

        /**
         * Returns a response object containing detailed information for a specific episode mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/episodes/getEpisodeExtended">
         * <b>[GET]</b> /episodes/{id}/extended</a>
         *
         * @param episodeId The <i>TheTVDB.com</i> episode ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no episode record with the given ID exists.
         * @see JSON#getEpisodeDetails(long) TheTVDBApi.JSON.getEpisodeDetails(episodeId)
         * @see TheTVDBApi#getEpisodeDetails(long) TheTVDBApi.getEpisodeDetails(episodeId)
         */
        APIResponse<EpisodeDetails> getEpisodeDetails(long episodeId) throws APIException;

        /**
         * Returns a response object containing a translation record for a specific episode mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/episodes/getEpisodeTranslation">
         * <b>[GET]</b> /episodes/{id}/translations/{language}</a>
         *
         * @param episodeId The <i>TheTVDB.com</i> episode ID
         * @param language  The 2- or 3-character language code
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no episode translation record exists for the given ID and
         *                      language.
         * @see JSON#getEpisodeTranslation(long, String) TheTVDBApi.JSON.getEpisodeTranslation(episodeId, language)
         * @see TheTVDBApi#getEpisodeTranslation(long, String) TheTVDBApi.getEpisodeTranslation(episodeId, language)
         */
        APIResponse<Translation> getEpisodeTranslation(long episodeId, @Nonnull String language) throws APIException;

        /**
         * Returns a response object containing a translation record for a specific list mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/lists/getListTranslation">
         * <b>[GET]</b> /lists/{id}/translations/{language}</a>
         *
         * @param listId   The <i>TheTVDB.com</i> list ID
         * @param language The 2- or 3-character language code
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no list translation record exists for the given ID and
         *                      language.
         * @see JSON#getListTranslation(long, String) TheTVDBApi.JSON.getListTranslation(listId, language)
         * @see TheTVDBApi#getListTranslation(long, String) TheTVDBApi.getListTranslation(listId, language)
         */
        APIResponse<Translation> getListTranslation(long listId, @Nonnull String language) throws APIException;

        /**
         * Returns a response object containing a collection of lists based on the given query parameters mapped as Java
         * DTO. The collection contains basic information of all lists matching the query parameters.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/lists/getAllLists">
         * <b>[GET]</b> /lists</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAllLists(QueryParameters) TheTVDBApi.JSON.getAllLists(queryParameters)
         * @see TheTVDBApi#getAllLists(QueryParameters) TheTVDBApi.getAllLists(queryParameters)
         */
        APIResponse<Collection<FCList>> getAllLists(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing basic information for a specific list mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/lists/getList">
         * <b>[GET]</b> /lists/{id}</a>
         *
         * @param listId The <i>TheTVDB.com</i> list ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no list record with the given ID exists.
         * @see JSON#getList(long) TheTVDBApi.JSON.getList(listId)
         * @see TheTVDBApi#getList(long) TheTVDBApi.getList(listId)
         */
        APIResponse<FCList> getList(long listId) throws APIException;

        /**
         * Returns a response object containing detailed information for a specific list mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/lists/getListExtended">
         * <b>[GET]</b> /lists/{id}/extended</a>
         *
         * @param listId The <i>TheTVDB.com</i> list ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no list record with the given ID exists.
         * @see JSON#getListDetails(long) TheTVDBApi.JSON.getListDetails(listId)
         * @see TheTVDBApi#getListDetails(long) TheTVDBApi.getListDetails(listId)
         */
        APIResponse<FCListDetails> getListDetails(long listId) throws APIException;

        /**
         * Returns a response object containing a collection of available genders mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/genders/getAllGenders">
         * <b>[GET]</b> /genders</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAllGenders()
         * @see TheTVDBApi#getAllGenders() TheTVDBApi.getAllGenders()
         */
        APIResponse<Collection<Gender>> getAllGenders() throws APIException;

        /**
         * Returns a response object containing a collection of available genres mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/genres/getAllGenres">
         * <b>[GET]</b> /genres</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAllGenres()
         * @see TheTVDBApi#getAllGenres() TheTVDBApi.getAllGenres()
         */
        APIResponse<Collection<Genre>> getAllGenres() throws APIException;

        /**
         * Returns a response object containing information for a specific genre mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/genres/getGenreBase">
         * <b>[GET]</b> /genres/{id}</a>
         *
         * @param genreId The <i>TheTVDB.com</i> genre ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no genre record with the given ID exists.
         * @see JSON#getGenre(long) TheTVDBApi.JSON.getGenre(genreId)
         * @see TheTVDBApi#getGenre(long) TheTVDBApi.getGenre(genreId)
         */
        APIResponse<Genre> getGenre(long genreId) throws APIException;

        /**
         * Returns a response object containing a collection of available movie statuses mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movie-statuses/getAllMovieStatuses">
         * <b>[GET]</b> /movies/statuses</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAllMovieStatuses()
         * @see TheTVDBApi#getAllMovieStatuses() TheTVDBApi.getAllMovieStatuses()
         */
        APIResponse<Collection<Status>> getAllMovieStatuses() throws APIException;

        /**
         * Returns a response object containing a collection of movies based on the given query parameters mapped as
         * Java DTO. The collection contains basic information of all movies matching the query parameters.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies/getAllMovie">
         * <b>[GET]</b> /movies</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAllMovies(QueryParameters) TheTVDBApi.JSON.getAllMovies(queryParameters)
         * @see TheTVDBApi#getAllMovies(QueryParameters) TheTVDBApi.getAllMovies(queryParameters)
         */
        APIResponse<Collection<Movie>> getAllMovies(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing basic information for a specific movie mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies/getMovieBase">
         * <b>[GET]</b> /movies/{id}</a>
         *
         * @param movieId The <i>TheTVDB.com</i> movie ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no movie record with the given ID exists.
         * @see JSON#getMovie(long) TheTVDBApi.JSON.getMovie(movieId)
         * @see TheTVDBApi#getMovie(long) TheTVDBApi.getMovie(movieId)
         */
        APIResponse<Movie> getMovie(long movieId) throws APIException;

        /**
         * Returns a response object containing detailed information for a specific movie mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies/getMovieExtended">
         * <b>[GET]</b> /movies/{id}/extended</a>
         *
         * @param movieId The <i>TheTVDB.com</i> movie ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no movie record with the given ID exists.
         * @see JSON#getMovieDetails(long) TheTVDBApi.JSON.getMovieDetails(movieId)
         * @see TheTVDBApi#getMovieDetails(long) TheTVDBApi.getMovieDetails(movieId)
         */
        APIResponse<MovieDetails> getMovieDetails(long movieId) throws APIException;

        /**
         * Returns a response object containing a translation record for a specific movie mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies/getMovieTranslation">
         * <b>[GET]</b> /movies/{id}/translations/{language}</a>
         *
         * @param movieId  The <i>TheTVDB.com</i> movie ID
         * @param language The 2- or 3-character language code
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no movie translation record exists for the given ID and
         *                      language.
         * @see JSON#getMovieTranslation(long, String) TheTVDBApi.JSON.getMovieTranslation(movieId, language)
         * @see TheTVDBApi#getMovieTranslation(long, String) TheTVDBApi.getMovieTranslation(movieId, language)
         */
        APIResponse<Translation> getMovieTranslation(long movieId, @Nonnull String language) throws APIException;

        /**
         * Returns a response object containing a collection of available people types mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/people-types/getAllPeopleTypes">
         * <b>[GET]</b> /people/types</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAllPeopleTypes()
         * @see TheTVDBApi#getAllPeopleTypes() TheTVDBApi.getAllPeopleTypes()
         */
        APIResponse<Collection<PeopleType>> getAllPeopleTypes() throws APIException;

        /**
         * Returns a response object containing basic information for a specific people mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/people/getPeopleBase">
         * <b>[GET]</b> /people/{id}</a>
         *
         * @param peopleId The <i>TheTVDB.com</i> people ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no people record with the given ID exists.
         * @see JSON#getPeople(long) TheTVDBApi.JSON.getPeople(peopleId)
         * @see TheTVDBApi#getPeople(long) TheTVDBApi.getPeople(peopleId)
         */
        APIResponse<People> getPeople(long peopleId) throws APIException;

        /**
         * Returns a response object containing detailed information for a specific people mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/people/getPeopleExtended">
         * <b>[GET]</b> /people/{id}/extended</a>
         *
         * @param peopleId The <i>TheTVDB.com</i> people ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no people record with the given ID exists.
         * @see JSON#getPeopleDetails(long) TheTVDBApi.JSON.getPeopleDetails(peopleId)
         * @see TheTVDBApi#getPeopleDetails(long) TheTVDBApi.getPeopleDetails(peopleId)
         */
        APIResponse<PeopleDetails> getPeopleDetails(long peopleId) throws APIException;

        /**
         * Returns a response object containing basic information for a specific season mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/seasons/getSeasonBase">
         * <b>[GET]</b> /seasons/{id}</a>
         *
         * @param seasonId The <i>TheTVDB.com</i> season ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no season record with the given ID exists.
         * @see JSON#getSeason(long) TheTVDBApi.JSON.getSeason(seasonId)
         * @see TheTVDBApi#getSeason(long) TheTVDBApi.getSeason(seasonId)
         */
        APIResponse<Season> getSeason(long seasonId) throws APIException;

        /**
         * Returns a response object containing detailed information for a specific season mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/seasons/getSeasonExtended">
         * <b>[GET]</b> /seasons/{id}/extended</a>
         *
         * @param seasonId The <i>TheTVDB.com</i> season ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no season record with the given ID exists.
         * @see JSON#getSeasonDetails(long) TheTVDBApi.JSON.getSeasonDetails(seasonId)
         * @see TheTVDBApi#getSeasonDetails(long) TheTVDBApi.getSeasonDetails(seasonId)
         */
        APIResponse<SeasonDetails> getSeasonDetails(long seasonId) throws APIException;

        /**
         * Returns a response object containing a collection of available season types mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/seasons/getSeasonTypes">
         * <b>[GET]</b> /seasons/types</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getSeasonTypes()
         * @see TheTVDBApi#getSeasonTypes() TheTVDBApi.getSeasonTypes()
         */
        APIResponse<Collection<SeasonType>> getSeasonTypes() throws APIException;

        /**
         * Returns a response object containing a translation record for a specific season mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/seasons/getSeasonTranslation">
         * <b>[GET]</b> /seasons/{id}/translations/{language}</a>
         *
         * @param seasonId The <i>TheTVDB.com</i> season ID
         * @param language The 2- or 3-character language code
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no season translation record exists for the given ID and
         *                      language.
         * @see JSON#getSeasonTranslation(long, String) TheTVDBApi.JSON.getSeasonTranslation(seasonId, language)
         * @see TheTVDBApi#getSeasonTranslation(long, String) TheTVDBApi.getSeasonTranslation(seasonId, language)
         */
        APIResponse<Translation> getSeasonTranslation(long seasonId, @Nonnull String language) throws APIException;

        /**
         * Returns a response object containing a collection of available series statuses mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series-statuses/getAllSeriesStatuses">
         * <b>[GET]</b> /series/statuses</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAllSeriesStatuses()
         * @see TheTVDBApi#getAllSeriesStatuses() TheTVDBApi.getAllSeriesStatuses()
         */
        APIResponse<Collection<Status>> getAllSeriesStatuses() throws APIException;

        /**
         * Returns a response object containing a collection of series based on the given query parameters mapped as
         * Java DTO. The collection contains basic information of all series matching the query parameters.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series/getAllSeries">
         * <b>[GET]</b> /series</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAllSeries(QueryParameters) TheTVDBApi.JSON.getAllSeries(queryParameters)
         * @see TheTVDBApi#getAllSeries(QueryParameters) TheTVDBApi.getAllSeries(queryParameters)
         */
        APIResponse<Collection<Series>> getAllSeries(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing basic information for a specific series mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series/getSeriesBase">
         * <b>[GET]</b> /series/{id}</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no series record with the given ID exists.
         * @see JSON#getSeries(long) TheTVDBApi.JSON.getSeries(seriesId)
         * @see TheTVDBApi#getSeries(long) TheTVDBApi.getSeries(seriesId)
         */
        APIResponse<Series> getSeries(long seriesId) throws APIException;

        /**
         * Returns a response object containing detailed information for a specific series mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series/getSeriesExtended">
         * <b>[GET]</b> /series/{id}/extended</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no series record with the given ID exists.
         * @see JSON#getSeriesDetails(long) TheTVDBApi.JSON.getSeriesDetails(seriesId)
         * @see TheTVDBApi#getSeriesDetails(long) TheTVDBApi.getSeriesDetails(seriesId)
         */
        APIResponse<SeriesDetails> getSeriesDetails(long seriesId) throws APIException;

        /**
         * Returns the episodes of a particular series based on the given query parameters mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series/getSeriesEpisodes">
         * <b>[GET]</b> /series/{id}/episodes/{season-type}</a>
         *
         * @param seriesId        The <i>TheTVDB.com</i> series ID
         * @param seasonType      The type of season for which episodes should be returned
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no series record with the given ID exists.
         * @see JSON#getSeriesEpisodes(long, SeriesSeasonType, QueryParameters) TheTVDBApi.JSON.getSeriesEpisodes(seriesId,
         *         seasonType, queryParameters)
         * @see TheTVDBApi#getSeriesEpisodes(long, SeriesSeasonType, QueryParameters)
         *         TheTVDBApi.getSeriesEpisodes(seriesId, seasonType, queryParameters)
         */
        APIResponse<SeriesEpisodes> getSeriesEpisodes(long seriesId, SeriesSeasonType seasonType,
                QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a translation record for a specific series mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series/getSeriesTranslation">
         * <b>[GET]</b> /series/{id}/translations/{language}</a>
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         * @param language The 2- or 3-character language code
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no series translation record exists for the given ID and
         *                      language.
         * @see JSON#getSeriesTranslation(long, String) TheTVDBApi.JSON.getSeriesTranslation(seriesId, language)
         * @see TheTVDBApi#getSeriesTranslation(long, String) TheTVDBApi.getSeriesTranslation(seriesId, language)
         */
        APIResponse<Translation> getSeriesTranslation(long seriesId, @Nonnull String language) throws APIException;

        /**
         * Returns a response object containing a collection of available source types mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/source-types/getAllSourceTypes">
         * <b>[GET]</b> /sources/types</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAllSourceTypes()
         * @see TheTVDBApi#getAllSourceTypes() TheTVDBApi.getAllSourceTypes()
         */
        APIResponse<Collection<SourceType>> getAllSourceTypes() throws APIException;

        /**
         * Returns a response object containing a collection of recently updated entities based on the given query
         * parameters mapped as Java DTO. The collection contains basic information of all entities matching the query
         * parameters.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/updates/updates">
         * <b>[GET]</b> /updates</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getUpdates(QueryParameters) TheTVDBApi.JSON.getUpdates(queryParameters)
         * @see TheTVDBApi#getUpdates(QueryParameters) TheTVDBApi.getUpdates(queryParameters)
         */
        APIResponse<Collection<EntityUpdate>> getUpdates(QueryParameters queryParameters) throws APIException;
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

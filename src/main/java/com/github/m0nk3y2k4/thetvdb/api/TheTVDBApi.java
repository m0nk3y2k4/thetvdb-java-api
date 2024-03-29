/*
 * Copyright (C) 2019 - 2022 thetvdb-java-api Authors and Contributors
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
import com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.EpisodeMeta;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.MovieMeta;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.PeopleMeta;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.SearchType;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.SeasonMeta;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesMeta;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesSeasonType;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.UpdateAction;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.UpdateEntityType;
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
import com.github.m0nk3y2k4.thetvdb.api.model.data.EntityTranslation;
import com.github.m0nk3y2k4.thetvdb.api.model.data.EntityType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.EntityUpdate;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Episode;
import com.github.m0nk3y2k4.thetvdb.api.model.data.EpisodeDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.FCList;
import com.github.m0nk3y2k4.thetvdb.api.model.data.FCListDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.FavoriteRecord;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Favorites;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Gender;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Genre;
import com.github.m0nk3y2k4.thetvdb.api.model.data.InspirationType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Movie;
import com.github.m0nk3y2k4.thetvdb.api.model.data.MovieDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.People;
import com.github.m0nk3y2k4.thetvdb.api.model.data.PeopleDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.PeopleType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SearchResult;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Season;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeasonDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeasonType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Series;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesEpisodes;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SourceType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Status;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Translations;
import com.github.m0nk3y2k4.thetvdb.api.model.data.UserInfo;

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
 * should be used. This factory also provides additional helper methods, for example to easily create new
 * <em>{@code QueryParameters}</em>.
 * <p><br>
 * To cover a wide range of possible applications, this API connector provides multiple layouts in order to allow an
 * easy integration regardless of your actual project requirements. It gives you the option to use prefabbed DTO's which
 * will be parsed from the actual JSON returned by the remote service. In case you need advanced exception handling, or
 * you prefer to parse the JSON into your own data models (or don't want to parse it at all), other API layouts will
 * provide you with extended API response DTO's or even with the raw JSON. The following API layouts are currently
 * available:
 * <ul>
 * <li>{@link TheTVDBApi}<br>
 * This is probably the most common layout. It provides various shortcut-methods and automatically maps the received
 * JSON <b><i>data</i></b> content into simple Java DTO's. The user does not have to worry about JSON parsing but can
 * simply work with the returned DTO's like he works with every other Java object. However, these objects do only
 * contain the actually requested data and will not include any additional contextual information that may be returned
 * by the remote service (e.g. Pagination information, additional validation or error data). They will provide access to
 * all properties that are formally declared by the <a target="_blank" href="https://thetvdb.github.io/v4-api/">API
 * documentation</a> (version {@value Version#API_VERSION}).</li>
 * <li>{@link Extended}<br>
 * This layout may be used for slightly advance API integration. Like the common layout it'll take care of parsing
 * the received JSON into Java DTO's but it will also provide access to any additional contextual information.
 * Methods of this layout will always return a single {@link APIResponse} object which consists of the actual data,
 * parsed as DTO, as well as all additional information which is available in the given context, like additional
 * status or pagination information. This layout does not provide any shortcut-methods.</li>
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
     * Sets the preferred language to be used for communication with the remote service. Some API calls might use this
     * setting in order to only return results that match the given language. If available, the data returned by the
     * remote API will be translated to the given language. The default language code is <b>"en"</b>.
     *
     * @param languageCode The language in which the results are to be returned
     */
    // ToDo: Check if this is still supported by the remote API
    void setLanguage(String languageCode);

    /**
     * Initializes the current API session by requesting a new token from the remote API. This token will be used for
     * authentication of all requests that are sent to the remote service by this API instance. The initialization will
     * be performed based on the constructor parameters used to create this API instance. It is recommended to log
     * in/initialize the session before making the first API call. However, if an API call is made without proper
     * initialization, an implicit login will be performed.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Login/post_login">
     * <b>[POST]</b> /login</a>
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    void login() throws APIException;

    /**
     * Returns a collection of available artwork statuses mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Artwork%20Statuses/getAllArtworkStatuses">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Artwork%20Types/getAllArtworkTypes">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Artwork/getArtworkBase">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Artwork/getArtworkExtended">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Award%20Categories/getAwardCategory">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Award%20Categories/getAwardCategoryExtended">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Awards/getAllAwards">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Awards/getAward">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Awards/getAwardExtended">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Characters/getCharacterBase">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Companies/getAllCompanies">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Companies/getCompanyTypes">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Companies/getCompany">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Content%20Ratings/getAllContentRatings">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Entity%20Types/getEntityTypes">
     * <b>[GET]</b> /entities</a>
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Episodes/getEpisodeBase">
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
     * Returns detailed information for a specific episode based on the given query parameters mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Episodes/getEpisodeExtended">
     * <b>[GET]</b> /episodes/{id}/extended</a>
     *
     * @param episodeId       The <i>TheTVDB.com</i> episode ID
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return Detailed episode information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no episode record with the given ID exists.
     * @see JSON#getEpisodeDetails(long, QueryParameters) TheTVDBApi.JSON.getEpisodeDetails(episodeId,
     *         queryParameters)
     * @see Extended#getEpisodeDetails(long, QueryParameters) TheTVDBApi.Extended.getEpisodeDetails(episodeId,
     *         queryParameters)
     */
    EpisodeDetails getEpisodeDetails(long episodeId, QueryParameters queryParameters) throws APIException;

    /**
     * Returns detailed information for a specific episode mapped as Java DTO. This is a shortcut-method for
     * {@link #getEpisodeDetails(long, QueryParameters) getEpisodeDetails(episodeId, queryParameters)} with no
     * additional query parameters.
     *
     * @param episodeId The <i>TheTVDB.com</i> episode ID
     *
     * @return Detailed episode information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no episode record with the given ID exists.
     */
    EpisodeDetails getEpisodeDetails(long episodeId) throws APIException;

    /**
     * Returns detailed information as well as the requested additional meta information for a specific episode mapped
     * as Java DTO. This is a shortcut-method for
     * {@link #getEpisodeDetails(long, QueryParameters) getEpisodeDetails(episodeId, queryParameters)} with a single
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Episodes#META} query parameter.
     *
     * @param episodeId The <i>TheTVDB.com</i> episode ID
     * @param meta      Additional information to be included in the response
     *
     * @return Detailed episode information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no episode record with the given ID exists.
     */
    EpisodeDetails getEpisodeDetails(long episodeId, @Nonnull EpisodeMeta meta) throws APIException;

    /**
     * Returns a translation record for a specific episode mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Episodes/getEpisodeTranslation">
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
    EntityTranslation getEpisodeTranslation(long episodeId, @Nonnull String language) throws APIException;

    /**
     * Returns a collection of translation records for a specific list mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Lists/getListTranslation">
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
    Translations<EntityTranslation> getListTranslation(long listId, @Nonnull String language) throws APIException;

    /**
     * Returns a collection of lists based on the given query parameters mapped as Java DTO. The collection contains
     * basic information of all lists matching the query parameters.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Lists/getAllLists">
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
     * shortcut-method for {@link #getAllLists(QueryParameters) getAllLists(queryParameters)} with a single
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Lists#PAGE} query parameter.
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Lists/getList">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Lists/getListExtended">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Genders/getAllGenders">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Genres/getAllGenres">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Genres/getGenreBase">
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
     * Returns a collection of available inspiration types mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/InspirationTypes/getAllInspirationTypes">
     * <b>[GET]</b> /inspiration/types</a>
     *
     * @return Collection of available inspiration types mapped as Java DTO's based on the JSON data returned by the
     *         remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAllInspirationTypes()
     * @see Extended#getAllInspirationTypes()
     */
    Collection<InspirationType> getAllInspirationTypes() throws APIException;

    /**
     * Returns a collection of available movie statuses mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Movie%20Statuses/getAllMovieStatuses">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Movies/getAllMovie">
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
     * shortcut-method for {@link #getAllMovies(QueryParameters) getAllMovies(queryParameters)} with a single
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Movies#PAGE} query parameter.
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Movies/getMovieBase">
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
     * Returns detailed information for a specific movie based on the given query parameters mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Movies/getMovieExtended">
     * <b>[GET]</b> /movies/{id}/extended</a>
     *
     * @param movieId         The <i>TheTVDB.com</i> movie ID
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return Detailed movie information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no movie record with the given ID exists.
     * @see JSON#getMovieDetails(long, QueryParameters) TheTVDBApi.JSON.getMovieDetails(movieId, queryParameters)
     * @see Extended#getMovieDetails(long, QueryParameters) TheTVDBApi.Extended.getMovieDetails(movieId,
     *         queryParameters)
     */
    MovieDetails getMovieDetails(long movieId, QueryParameters queryParameters) throws APIException;

    /**
     * Returns a collection of movies based on the given filter parameters mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Movies/getMoviesFilter">
     * <b>[GET]</b> /movies/filter</a>
     *
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return Collection of movies mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getMoviesFiltered(QueryParameters) TheTVDBApi.JSON.getMoviesFiltered(queryParameters)
     * @see Extended#getMoviesFiltered(QueryParameters) TheTVDBApi.Extended.getMoviesFiltered(queryParameters)
     */
    Collection<Movie> getMoviesFiltered(QueryParameters queryParameters) throws APIException;

    /**
     * Returns detailed information for a specific movie mapped as Java DTO. This is a shortcut-method for
     * {@link #getMovieDetails(long, QueryParameters) getMovieDetails(movieId, queryParameters)} with no additional
     * query parameters.
     *
     * @param movieId The <i>TheTVDB.com</i> movie ID
     *
     * @return Detailed movie information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no movie record with the given ID exists.
     */
    MovieDetails getMovieDetails(long movieId) throws APIException;

    /**
     * Returns detailed information as well as the requested additional meta information for a specific movie based on
     * the given query parameters mapped as Java DTO. This is a shortcut-method for
     * {@link #getMovieDetails(long, QueryParameters) getMovieDetails(movieId, queryParameters)} with a single
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Movies#META} query parameter.
     *
     * @param movieId The <i>TheTVDB.com</i> movie ID
     * @param meta    Additional information to be included in the response
     *
     * @return Detailed movie information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no movie record with the given ID exists.
     */
    MovieDetails getMovieDetails(long movieId, @Nonnull MovieMeta meta) throws APIException;

    /**
     * Returns a translation record for a specific movie mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Movies/getMovieTranslation">
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
    EntityTranslation getMovieTranslation(long movieId, @Nonnull String language) throws APIException;

    /**
     * Returns a collection of available people types mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/People%20Types/getAllPeopleTypes">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/People/getPeopleBase">
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
     * Returns detailed information for a specific people based on the given query parameters mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/People/getPeopleExtended">
     * <b>[GET]</b> /people/{id}/extended</a>
     *
     * @param peopleId        The <i>TheTVDB.com</i> people ID
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return Detailed people information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no people record with the given ID exists.
     * @see JSON#getPeopleDetails(long, QueryParameters) TheTVDBApi.JSON.getPeopleDetails(peopleId, queryParameters)
     * @see Extended#getPeopleDetails(long, QueryParameters) TheTVDBApi.Extended.getPeopleDetails(peopleId,
     *         queryParameters)
     */
    PeopleDetails getPeopleDetails(long peopleId, QueryParameters queryParameters) throws APIException;

    /**
     * Returns detailed information for a specific people mapped as Java DTO. This is a shortcut-method for
     * {@link #getPeopleDetails(long, QueryParameters) getPeopleDetails(peopleId, queryParameters)} with no additional
     * query parameters.
     *
     * @param peopleId The <i>TheTVDB.com</i> people ID
     *
     * @return Detailed people information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no people record with the given ID exists.
     */
    PeopleDetails getPeopleDetails(long peopleId) throws APIException;

    /**
     * Returns detailed information as well as the requested additional meta information for a specific people based on
     * the given query parameters mapped as Java DTO. This is a shortcut-method for
     * {@link #getPeopleDetails(long, QueryParameters) getPeopleDetails(peopleId, queryParameters)} with a single
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.People#META} query parameter.
     *
     * @param peopleId The <i>TheTVDB.com</i> people ID
     * @param meta     Additional information to be included in the response
     *
     * @return Detailed people information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no people record with the given ID exists.
     */
    PeopleDetails getPeopleDetails(long peopleId, @Nonnull PeopleMeta meta) throws APIException;

    /**
     * Returns a translation record for a specific people mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/People/getPeopleTranslation">
     * <b>[GET]</b> /people/{id}/translations/{language}</a>
     *
     * @param peopleId The <i>TheTVDB.com</i> people ID
     * @param language The 2- or 3-character language code
     *
     * @return People translation record mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no people translation record exists for the given ID and language.
     * @see JSON#getPeopleTranslation(long, String) TheTVDBApi.JSON.getPeopleTranslation(peopleId, language)
     * @see Extended#getPeopleTranslation(long, String) TheTVDBApi.Extended.getPeopleTranslation(peopleId, language)
     */
    EntityTranslation getPeopleTranslation(long peopleId, @Nonnull String language) throws APIException;

    /**
     * Returns a collection of search results based on the given query parameters mapped as Java DTO. Note that the
     * given query parameters must either contain a valid
     * <em>{@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Search#Q}</em> or
     * <em>{@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Search#QUERY}</em> search term key.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Search/getSearchResults">
     * <b>[GET]</b> /search</a>
     *
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return Collection of search results mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getSearchResults(QueryParameters) TheTVDBApi.JSON.getSearchResults(queryParameters)
     * @see Extended#getSearchResults(QueryParameters) TheTVDBApi.Extended.getSearchResults(queryParameters)
     */
    Collection<SearchResult> getSearchResults(QueryParameters queryParameters) throws APIException;

    /**
     * Returns a collection of search results based on the given search term mapped as Java DTO. The returned data will
     * contain all entities that match the given search criteria. This is a shortcut-method for
     * {@link #getSearchResults(QueryParameters) getSearchResults(queryParameters)} with a single
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Search#QUERY} query parameter.
     *
     * @param searchTerm The search string for which the database should be queried
     *
     * @return Collection of search results mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see #search(String, SearchType) search(searchTerm, entityType)
     */
    Collection<SearchResult> search(@Nonnull String searchTerm) throws APIException;

    /**
     * Returns a collection of search results based on the given search term mapped as Java DTO. The returned data will
     * be restricted to the specified entity type and contain results that match the given search criteria. This is a
     * shortcut-method for {@link #getSearchResults(QueryParameters) getSearchResults(queryParameters)} with
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Search#QUERY} and
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Search#TYPE} query parameters.
     *
     * @param searchTerm The search string for which the database should be queried
     * @param entityType Only search for results of this particular entity type
     *
     * @return Collection of search results mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see #search(String) search(searchTerm)
     */
    Collection<SearchResult> search(@Nonnull String searchTerm, @Nonnull SearchType entityType) throws APIException;

    /**
     * Returns a collection of seasons based on the given query parameters mapped as Java DTO. The collection contains
     * basic information of all seasons matching the query parameters.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Seasons/getAllSeasons">
     * <b>[GET]</b> /seasons</a>
     *
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return Collection of seasons mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getAllSeasons(QueryParameters) TheTVDBApi.JSON.getAllSeasons(queryParameters)
     * @see Extended#getAllSeasons(QueryParameters) TheTVDBApi.Extended.getAllSeasons(queryParameters)
     */
    Collection<Season> getAllSeasons(QueryParameters queryParameters) throws APIException;

    /**
     * Returns a limited collection of seasons mapped as Java DTO. Due to the large amount of available seasons, the
     * result will be paginated. Use the <em>{@code page}</em> parameter to browse to a specific result page. This is a
     * shortcut-method for {@link #getAllSeasons(QueryParameters) getAllSeasons(queryParameters)} with a single
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Seasons#PAGE} query parameter.
     *
     * @param page The result page to be returned (zero-based)
     *
     * @return Collection of seasons mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    Collection<Season> getAllSeasons(long page) throws APIException;

    /**
     * Returns basic information for a specific season mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Seasons/getSeasonBase">
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
     * Returns detailed information for a specific season based on the given query parameters mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Seasons/getSeasonExtended">
     * <b>[GET]</b> /seasons/{id}/extended</a>
     *
     * @param seasonId        The <i>TheTVDB.com</i> season ID
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return Detailed season information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no season record with the given ID exists.
     * @see JSON#getSeasonDetails(long, QueryParameters) TheTVDBApi.JSON.getSeasonDetails(seasonId, queryParameters)
     * @see Extended#getSeasonDetails(long, QueryParameters) TheTVDBApi.Extended.getSeasonDetails(seasonId,
     *         queryParameters)
     */
    SeasonDetails getSeasonDetails(long seasonId, QueryParameters queryParameters) throws APIException;

    /**
     * Returns detailed information for a specific season mapped as Java DTO. This is a shortcut-method for
     * {@link #getSeasonDetails(long, QueryParameters) getSeasonDetails(seasonId, queryParameters)} with no additional
     * query parameters.
     *
     * @param seasonId The <i>TheTVDB.com</i> season ID
     *
     * @return Detailed season information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no season record with the given ID exists.
     */
    SeasonDetails getSeasonDetails(long seasonId) throws APIException;

    /**
     * Returns detailed information as well as the requested additional meta information for a specific season based on
     * the given query parameters mapped as Java DTO. This is a shortcut-method for
     * {@link #getSeasonDetails(long, QueryParameters) getSeasonDetails(seasonId, queryParameters)} with a single
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Seasons#META} query parameter.
     *
     * @param seasonId The <i>TheTVDB.com</i> season ID
     * @param meta     Additional information to be included in the response
     *
     * @return Detailed season information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no season record with the given ID exists.
     */
    SeasonDetails getSeasonDetails(long seasonId, @Nonnull SeasonMeta meta) throws APIException;

    /**
     * Returns a collection of available season types mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Seasons/getSeasonTypes">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Seasons/getSeasonTranslation">
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
    EntityTranslation getSeasonTranslation(long seasonId, @Nonnull String language) throws APIException;

    /**
     * Returns a collection of available series statuses mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series%20Statuses/getAllSeriesStatuses">
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series/getAllSeries">
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
     * shortcut-method for {@link #getAllSeries(QueryParameters) getAllSeries(queryParameters)} with a single
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series#PAGE} query parameter.
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
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesBase">
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
     * Returns detailed information for a specific series including custom artworks based on the given query parameters
     * mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesArtworks">
     * <b>[GET]</b> /series/{id}/artworks</a>
     *
     * @param seriesId        The <i>TheTVDB.com</i> series ID
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return Detailed series information incl. language/type based artworks mapped as Java DTO based on the JSON data
     *         returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no series record with the given ID exists.
     * @see JSON#getSeriesArtworks(long, QueryParameters) TheTVDBApi.JSON.getSeriesArtworks(seriesId,
     *         queryParameters)
     * @see Extended#getSeriesArtworks(long, QueryParameters) TheTVDBApi.Extended.getSeriesArtworks(seriesId,
     *         queryParameters)
     */
    SeriesDetails getSeriesArtworks(long seriesId, QueryParameters queryParameters) throws APIException;

    /**
     * Returns detailed information for a specific series including custom artworks based on the given query parameters
     * mapped as Java DTO. This is a shortcut-method for
     * {@link #getSeriesArtworks(long, QueryParameters) getSeriesArtworks(seriesId, queryParameters)} with a single
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series#LANGUAGE} query parameter.
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     * @param language The 2- or 3-character language code
     *
     * @return Detailed series information incl. language/type based artworks mapped as Java DTO based on the JSON data
     *         returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no series record with the given ID exists.
     */
    SeriesDetails getSeriesArtworks(long seriesId, @Nonnull String language) throws APIException;

    /**
     * Returns detailed information for a specific series based on the given query parameters mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesExtended">
     * <b>[GET]</b> /series/{id}/extended</a>
     *
     * @param seriesId        The <i>TheTVDB.com</i> series ID
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return Detailed series information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no series record with the given ID exists.
     * @see JSON#getSeriesDetails(long, QueryParameters) TheTVDBApi.JSON.getSeriesDetails(seriesId, queryParameters)
     * @see Extended#getSeriesDetails(long, QueryParameters) TheTVDBApi.Extended.getSeriesDetails(seriesId,
     *         queryParameters)
     */
    SeriesDetails getSeriesDetails(long seriesId, QueryParameters queryParameters) throws APIException;

    /**
     * Returns detailed information for a specific series mapped as Java DTO. This is a shortcut-method for
     * {@link #getSeriesDetails(long, QueryParameters) getSeriesDetails(seriesId, queryParameters)} with no additional
     * query parameters.
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     *
     * @return Detailed series information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no series record with the given ID exists.
     */
    SeriesDetails getSeriesDetails(long seriesId) throws APIException;

    /**
     * Returns detailed information as well as the requested additional meta information for a specific series based on
     * the given query parameters mapped as Java DTO. This is a shortcut-method for
     * {@link #getSeriesDetails(long, QueryParameters) getSeriesDetails(seriesId, queryParameters)} with a single
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series#META} query parameter.
     *
     * @param seriesId The <i>TheTVDB.com</i> series ID
     * @param meta     Additional information to be included in the response
     *
     * @return Detailed series information mapped as Java DTO based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no series record with the given ID exists.
     */
    SeriesDetails getSeriesDetails(long seriesId, @Nonnull SeriesMeta meta) throws APIException;

    /**
     * Returns the episodes of a particular series based on the given query parameters mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesEpisodes">
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
     * @see JSON#getSeriesEpisodes(long, SeriesSeasonType, QueryParameters)
     *         TheTVDBApi.JSON.getSeriesEpisodes(seriesId, seasonType, queryParameters)
     * @see Extended#getSeriesEpisodes(long, SeriesSeasonType, QueryParameters)
     *         TheTVDBApi.Extended.getSeriesEpisodes(seriesId, seasonType, queryParameters)
     */
    SeriesEpisodes getSeriesEpisodes(long seriesId, @Nonnull SeriesSeasonType seasonType,
            QueryParameters queryParameters) throws APIException;

    /**
     * Returns a limited overview of episodes of a particular series mapped as Java DTO. Since some series have many
     * episodes, the result will be paginated. This is a shortcut-method for
     * {@link #getSeriesEpisodes(long, SeriesSeasonType, QueryParameters) getSeriesEpisodes(seriesId, seasonType,
     * queryParameters)} which will always return the first page only, what may be convenient for series with few
     * episodes.
     *
     * @param seriesId   The <i>TheTVDB.com</i> series ID
     * @param seasonType The type of season for which episodes should be returned
     *
     * @return First page of a series episodes mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no series record with the given ID exists.
     * @see #getSeriesEpisodes(long, SeriesSeasonType, long) getSeriesEpisodes(seriesId, seasonType, seasonNumber)
     */
    SeriesEpisodes getSeriesEpisodes(long seriesId, @Nonnull SeriesSeasonType seasonType) throws APIException;

    /**
     * Returns a limited overview of episodes of a particular series season mapped as Java DTO. Since some seasons have
     * many episodes, the result will be paginated. This is a shortcut-method for
     * {@link #getSeriesEpisodes(long, SeriesSeasonType, QueryParameters) getSeriesEpisodes(seriesId, seasonType,
     * queryParameters)} with a single {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series#SEASON} query
     * parameter.
     *
     * @param seriesId     The <i>TheTVDB.com</i> series ID
     * @param seasonType   The type of season for which episodes should be returned
     * @param seasonNumber Number of the season for which episodes should be returned ('0' = all seasons)
     *
     * @return A series seasons episodes mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no series record with the given ID exists.
     * @see #getSeriesEpisodes(long, SeriesSeasonType) getSeriesEpisodes(seriesId, seasonType)
     */
    SeriesEpisodes getSeriesEpisodes(long seriesId, @Nonnull SeriesSeasonType seasonType, long seasonNumber)
            throws APIException;

    /**
     * Returns basic information for a specific series based on the given query parameters mapped as Java DTO. The
     * contained episodes will be translated to the given language.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesSeasonEpisodesTranslated">
     * <b>[GET]</b> /series/{id}/episodes/{season-type}/{lang}</a>
     *
     * @param seriesId        The <i>TheTVDB.com</i> series ID
     * @param seasonType      The type of season for which episodes should be returned
     * @param language        The 2- or 3-character language code
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return Basic series information with translated episodes mapped as Java DTO based on the JSON data returned by
     *         the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no series record with the given ID exists or the language code is
     *                      invalid.
     * @see JSON#getSeriesEpisodesTranslated(long, SeriesSeasonType, String, QueryParameters)
     *         TheTVDBApi.JSON.getSeriesEpisodesTranslated(seriesId, seasonType, language, queryParameters)
     * @see Extended#getSeriesEpisodesTranslated(long, SeriesSeasonType, String, QueryParameters)
     *         TheTVDBApi.Extended.getSeriesEpisodesTranslated(seriesId, seasonType, language, queryParameters)
     */
    Series getSeriesEpisodesTranslated(long seriesId, @Nonnull SeriesSeasonType seasonType, @Nonnull String language,
            QueryParameters queryParameters) throws APIException;

    /**
     * Returns basic information for a specific series mapped as Java DTO. The contained episodes will be translated to
     * the given language. This is a shortcut-method for
     * {@link #getSeriesEpisodesTranslated(long, SeriesSeasonType, String, QueryParameters)
     * getSeriesEpisodesTranslated(seriesId, seasonType, language, queryParameters)}  with no additional query
     * parameters.
     *
     * @param seriesId   The <i>TheTVDB.com</i> series ID
     * @param seasonType The type of season for which episodes should be returned
     * @param language   The 2- or 3-character language code
     *
     * @return Basic series information with translated episodes mapped as Java DTO's based on the JSON data returned by
     *         the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or if no series record with the given ID exists or the language code is
     *                      invalid.
     */
    Series getSeriesEpisodesTranslated(long seriesId, @Nonnull SeriesSeasonType seasonType, @Nonnull String language)
            throws APIException;

    /**
     * Returns a collection of series based on the given filter parameters mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesFilter">
     * <b>[GET]</b> /series/filter</a>
     *
     * @param queryParameters Object containing key/value pairs of query parameters
     *
     * @return Collection of series mapped as Java DTO's based on the JSON data returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getSeriesFiltered(QueryParameters) TheTVDBApi.JSON.getSeriesFiltered(queryParameters)
     * @see Extended#getSeriesFiltered(QueryParameters) TheTVDBApi.Extended.getSeriesFiltered(queryParameters)
     */
    Collection<Series> getSeriesFiltered(QueryParameters queryParameters) throws APIException;

    /**
     * Returns a translation record for a specific series mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesTranslation">
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
    EntityTranslation getSeriesTranslation(long seriesId, @Nonnull String language) throws APIException;

    /**
     * Returns a collection of available source types mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Source%20Types/getAllSourceTypes">
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
     * collection contains basic information of all entities matching the query parameters. Note that the given query
     * parameters must always contain a valid
     * <em>{@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates#SINCE}</em> Epoch timestamp key.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Updates/updates">
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
     * Returns a collection of entities which have been updated since the given Epoch time mapped as Java DTO. Due to
     * the possibly large amount of updates, the result will be paginated. Use the <em>{@code page}</em> parameter to
     * browse to a specific result page. This is a shortcut-method for
     * {@link #getUpdates(QueryParameters) getUpdates(queryParameters)} with
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates#SINCE} and
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates#PAGE} query parameters.
     *
     * @param since UNIX Epoch time (GMT) in seconds. Must be in the past.
     * @param page  The result page to be returned (zero-based)
     *
     * @return Collection of entities updated since the given Epoch time mapped as Java DTO's based on the JSON data
     *         returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or the timestamp lies in the future.
     * @see #getUpdates(long, UpdateEntityType, UpdateAction, long) getUpdates(since, type, action, page)
     */
    Collection<EntityUpdate> getUpdates(long since, long page) throws APIException;

    /**
     * Returns a collection of entities which have been updated since the given Epoch time, restricted to a specific
     * type of entities and some particular update action, mapped as Java DTO. Due to the possibly large amount of
     * updates, the result will be paginated. Use the <em>{@code page}</em> parameter to browse to a specific result
     * page. This is a shortcut-method for {@link #getUpdates(QueryParameters) getUpdates(queryParameters)} with
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates#SINCE},
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates#TYPE},
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates#ACTION} and
     * {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates#PAGE} query parameters.
     *
     * @param since  UNIX Epoch time (GMT) in seconds. Must be in the past.
     * @param type   The type of entity to be restricted to
     * @param action The type of update action to be restricted to
     * @param page   The result page to be returned (zero-based)
     *
     * @return Collection of entities changed since the given Epoch time mapped as Java DTO's based on the JSON data
     *         returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc. or the timestamp lies in the future.
     * @see #getUpdates(long, long) getUpdates(since, page)
     */
    Collection<EntityUpdate> getUpdates(long since, @Nonnull UpdateEntityType type, @Nonnull UpdateAction action,
            long page) throws APIException;

    /**
     * Returns some information about the current user mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/User%20info/getUserInfo">
     * <b>[GET]</b> /user</a>
     *
     * @return Information about the current user mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getUserInfo()
     * @see Extended#getUserInfo()
     */
    UserInfo getUserInfo() throws APIException;

    /**
     * Returns some information about a specific user mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/User%20info/getUserInfoById">
     * <b>[GET]</b> /user/{id}</a>
     *
     * @param userId The <i>TheTVDB.com</i> user ID
     *
     * @return Information about the specified user mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getUserInfo(long) TheTVDBApi.JSON.getUserInfo(userId)
     * @see Extended#getUserInfo(long) TheTVDBApi.Extended.getUserInfo(userId)
     */
    UserInfo getUserInfo(long userId) throws APIException;

    /**
     * Returns the current favorites of this user mapped as Java DTO.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Favorites/getUserFavorites">
     * <b>[GET]</b> /user/favorites</a>
     *
     * @return The current favorites of this user mapped as Java DTO's based on the JSON data returned by the remote
     *         service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#getUserFavorites()
     * @see Extended#getUserFavorites()
     */
    Favorites getUserFavorites() throws APIException;

    /**
     * Adds the given entities to the users list of favorites. To create a new favorite record, use the
     * {@link TheTVDBApiFactory#createFavoriteRecordBuilder() factory method} to retrieve a corresponding builder
     * instance.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank"
     * href="https://thetvdb.github.io/v4-api/#/Favorites/createUserFavorites">
     * <b>[POST]</b> /user/favorites</a>
     *
     * @param favoriteRecord Record containing one or multiple favorite entities
     *
     * @return Void placeholder object containing no actual data
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     * @see JSON#createUserFavorites(FavoriteRecord) TheTVDBApi.JSON.createUserFavorites(favoriteRecord)
     * @see Extended#createUserFavorites(FavoriteRecord) TheTVDBApi.Extended.createUserFavorites(favoriteRecord)
     * @see TheTVDBApiFactory#createFavoriteRecordBuilder()
     */
    Void createUserFavorites(@Nonnull FavoriteRecord favoriteRecord) throws APIException;

    /**
     * Provides access to the API's {@link JSON JSON} layout.
     * <p><br>
     * In this layout, all methods will return the raw, unmodified JSON as received from the remove service.
     *
     * @return Instance representing the API's <em>{@code JSON}</em> layout
     */
    JSON json();

    /**
     * Provides access to the API's {@link Extended Extended} layout.
     * <p><br>
     * In this layout, all methods will return a single {@link APIResponse} object, containing the actual request data,
     * mapped as DTO, as well as all additional information that is available in the corresponding context.
     *
     * @return Instance representing the API's <em>{@code Extended}</em> layout
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Artwork%20Statuses/getAllArtworkStatuses">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Artwork%20Types/getAllArtworkTypes">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Artwork/getArtworkBase">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Artwork/getArtworkExtended">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Award%20Categories/getAwardCategory">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Award%20Categories/getAwardCategoryExtended">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Awards/getAllAwards">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Awards/getAward">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Awards/getAwardExtended">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Characters/getCharacterBase">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Companies/getAllCompanies">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Companies/getCompanyTypes">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Companies/getCompany">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Content%20Ratings/getAllContentRatings">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Entity%20Types/getEntityTypes">
         * <b>[GET]</b> /entities</a>
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Episodes/getEpisodeBase">
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
         * Returns detailed information for a specific episode based on the given query parameters as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Episodes/getEpisodeExtended">
         * <b>[GET]</b> /episodes/{id}/extended</a>
         *
         * @param episodeId       The <i>TheTVDB.com</i> episode ID
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing detailed information for a specific episode
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no episode record with the given ID exists.
         * @see TheTVDBApi#getEpisodeDetails(long, QueryParameters) TheTVDBApi.getEpisodeDetails(episodeId,
         *         queryParameters)
         * @see Extended#getEpisodeDetails(long, QueryParameters) TheTVDBApi.Extended.getEpisodeDetails(episodeId,
         *         queryParameters)
         */
        JsonNode getEpisodeDetails(long episodeId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a translation record for a specific episode as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Episodes/getEpisodeTranslation">
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
         * Returns a collection of translation records for a specific list as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Lists/getListTranslation">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Lists/getAllLists">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Lists/getList">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Lists/getListExtended">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Genders/getAllGenders">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Genres/getAllGenres">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Genres/getGenreBase">
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
         * Returns a collection of available inspiration types as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/InspirationTypes/getAllInspirationTypes">
         * <b>[GET]</b> /inspiration/types</a>
         *
         * @return JSON object containing the available inspiration types
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAllInspirationTypes() TheTVDBApi.getAllInspirationTypes()
         * @see Extended#getAllInspirationTypes()
         */
        JsonNode getAllInspirationTypes() throws APIException;

        /**
         * Returns a collection of available movie statuses as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Movie%20Statuses/getAllMovieStatuses">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Movies/getAllMovie">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Movies/getMovieBase">
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
         * Returns detailed information for a specific movie based on the given query parameters as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Movies/getMovieExtended">
         * <b>[GET]</b> /movies/{id}/extended</a>
         *
         * @param movieId         The <i>TheTVDB.com</i> movie ID
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing detailed information for a specific movie
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no movie record with the given ID exists.
         * @see TheTVDBApi#getMovieDetails(long, QueryParameters) TheTVDBApi.getMovieDetails(movieId,
         *         queryParameters)
         * @see Extended#getMovieDetails(long, QueryParameters) TheTVDBApi.Extended.getMovieDetails(movieId,
         *         queryParameters)
         */
        JsonNode getMovieDetails(long movieId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a collection of movies based on the given filter parameters as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Movies/getMoviesFilter">
         * <b>[GET]</b> /movies/filter</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing the movies
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getMoviesFiltered(QueryParameters) TheTVDBApi.getMoviesFiltered(queryParameters)
         * @see Extended#getMoviesFiltered(QueryParameters) TheTVDBApi.Extended.getMoviesFiltered(queryParameters)
         */
        JsonNode getMoviesFiltered(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a translation record for a specific movie as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Movies/getMovieTranslation">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/People%20Types/getAllPeopleTypes">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/People/getPeopleBase">
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
         * Returns detailed information for a specific people based on the given query parameters as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/People/getPeopleExtended">
         * <b>[GET]</b> /people/{id}/extended</a>
         *
         * @param peopleId        The <i>TheTVDB.com</i> people ID
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing detailed information for a specific people
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no people record with the given ID exists.
         * @see TheTVDBApi#getPeopleDetails(long, QueryParameters) TheTVDBApi.getPeopleDetails(peopleId,
         *         queryParameters)
         * @see Extended#getPeopleDetails(long, QueryParameters) TheTVDBApi.Extended.getPeopleDetails(peopleId,
         *         queryParameters)
         */
        JsonNode getPeopleDetails(long peopleId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a translation record for a specific people as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/People/getPeopleTranslation">
         * <b>[GET]</b> /people/{id}/translations/{language}</a>
         *
         * @param peopleId The <i>TheTVDB.com</i> people ID
         * @param language The 2- or 3-character language code
         *
         * @return JSON object containing a translation record for a specific people
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no people translation record exists for the given ID and
         *                      language.
         * @see TheTVDBApi#getPeopleTranslation(long, String) TheTVDBApi.getPeopleTranslation(peopleId, language)
         * @see Extended#getPeopleTranslation(long, String) TheTVDBApi.Extended.getPeopleTranslation(peopleId,
         *         language)
         */
        JsonNode getPeopleTranslation(long peopleId, @Nonnull String language) throws APIException;

        /**
         * Returns a collection of search results based on the given query parameters as raw JSON. Note that the given
         * query parameters must either contain a valid
         * <em>{@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Search#Q}</em> or
         * <em>{@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Search#QUERY}</em> search term key.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Search/getSearchResults">
         * <b>[GET]</b> /search</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing the search results
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getSearchResults(QueryParameters) TheTVDBApi.getSearchResults(queryParameters)
         * @see Extended#getSearchResults(QueryParameters) TheTVDBApi.Extended.getSearchResults(queryParameters)
         */
        JsonNode getSearchResults(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a collection of seasons based on the given query parameters as raw JSON. It contains basic
         * information of all seasons matching the query parameters.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Seasons/getAllSeasons">
         * <b>[GET]</b> /seasons</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing the seasons
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getAllSeasons(QueryParameters) TheTVDBApi.getAllSeasons(queryParameters)
         * @see Extended#getAllSeasons(QueryParameters) TheTVDBApi.Extended.getAllSeasons(queryParameters)
         */
        JsonNode getAllSeasons(QueryParameters queryParameters) throws APIException;

        /**
         * Returns basic information for a specific season as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Seasons/getSeasonBase">
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
         * Returns detailed information for a specific season based on the given query parameters as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Seasons/getSeasonExtended">
         * <b>[GET]</b> /seasons/{id}/extended</a>
         *
         * @param seasonId        The <i>TheTVDB.com</i> season ID
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing detailed information for a specific season
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no season record with the given ID exists.
         * @see TheTVDBApi#getSeasonDetails(long, QueryParameters) TheTVDBApi.getSeasonDetails(seasonId,
         *         queryParameters)
         * @see Extended#getSeasonDetails(long, QueryParameters) TheTVDBApi.Extended.getSeasonDetails(seasonId,
         *         queryParameters)
         */
        JsonNode getSeasonDetails(long seasonId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a collection of available season types as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Seasons/getSeasonTypes">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Seasons/getSeasonTranslation">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series%20Statuses/getAllSeriesStatuses">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series/getAllSeries">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesBase">
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
         * Returns detailed information for a specific series including custom artworks based on the given query
         * parameters as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesArtworks">
         * <b>[GET]</b> /series/{id}/artworks</a>
         *
         * @param seriesId        The <i>TheTVDB.com</i> series ID
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing detailed information for a specific series incl. language/type based artworks
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no series record with the given ID exists.
         * @see TheTVDBApi#getSeriesArtworks(long, QueryParameters) TheTVDBApi.getSeriesArtworks(seriesId,
         *         queryParameters)
         * @see Extended#getSeriesArtworks(long, QueryParameters) TheTVDBApi.Extended.getSeriesArtworks(seriesId,
         *         queryParameters)
         */
        JsonNode getSeriesArtworks(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns detailed information for a specific series based on the given query parameters as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesExtended">
         * <b>[GET]</b> /series/{id}/extended</a>
         *
         * @param seriesId        The <i>TheTVDB.com</i> series ID
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing detailed information for a specific series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no series record with the given ID exists.
         * @see TheTVDBApi#getSeriesDetails(long, QueryParameters) TheTVDBApi.getSeriesDetails(seriesId,
         *         queryParameters)
         * @see Extended#getSeriesDetails(long, QueryParameters) TheTVDBApi.Extended.getSeriesDetails(seriesId,
         *         queryParameters)
         */
        JsonNode getSeriesDetails(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns the episodes of a particular series based on the given query parameters as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesEpisodes">
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
         * @see Extended#getSeriesEpisodes(long, SeriesSeasonType, QueryParameters)
         *         TheTVDBApi.Extended.getSeriesEpisodes(seriesId, seasonType, queryParameters)
         */
        JsonNode getSeriesEpisodes(long seriesId, @Nonnull SeriesSeasonType seasonType, QueryParameters queryParameters)
                throws APIException;

        /**
         * Returns basic information for a specific series based on the given query parameters as raw JSON. The
         * contained episodes will be translated to the given language.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesSeasonEpisodesTranslated">
         * <b>[GET]</b> /series/{id}/episodes/{season-type}/{lang}</a>
         *
         * @param seriesId        The <i>TheTVDB.com</i> series ID
         * @param seasonType      The type of season for which episodes should be returned
         * @param language        The 2- or 3-character language code
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing basic information incl. translated episodes for a specific series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no series record with the given ID exists or the language
         *                      code is invalid.
         * @see TheTVDBApi#getSeriesEpisodesTranslated(long, SeriesSeasonType, String, QueryParameters)
         *         TheTVDBApi.getSeriesEpisodesTranslated(seriesId, seasonType, language, queryParameters)
         * @see Extended#getSeriesEpisodesTranslated(long, SeriesSeasonType, String, QueryParameters)
         *         TheTVDBApi.Extended.getSeriesEpisodesTranslated(seriesId, seasonType, language, queryParameters)
         */
        JsonNode getSeriesEpisodesTranslated(long seriesId, @Nonnull SeriesSeasonType seasonType,
                @Nonnull String language, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a collection of series based on the given filter parameters as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesFilter">
         * <b>[GET]</b> /series/filter</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return JSON object containing the series
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getSeriesFiltered(QueryParameters) TheTVDBApi.getSeriesFiltered(queryParameters)
         * @see Extended#getSeriesFiltered(QueryParameters) TheTVDBApi.Extended.getSeriesFiltered(queryParameters)
         */
        JsonNode getSeriesFiltered(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a translation record for a specific series as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesTranslation">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Source%20Types/getAllSourceTypes">
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
         * contains basic information of all entities matching the query parameters. Note that the given query
         * parameters must always contain a valid
         * <em>{@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates#SINCE}</em> Epoch timestamp key.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Updates/updates">
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

        /**
         * Returns some information about the current user as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/User%20info/getUserInfo">
         * <b>[GET]</b> /user</a>
         *
         * @return JSON object containing information about the current user
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getUserInfo() TheTVDBApi.getUserInfo()
         * @see Extended#getUserInfo()
         */
        JsonNode getUserInfo() throws APIException;

        /**
         * Returns some information about a specific user as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/User%20info/getUserInfoById">
         * <b>[GET]</b> /user/{id}</a>
         *
         * @param userId The <i>TheTVDB.com</i> user ID
         *
         * @return JSON object containing information about the specified user
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getUserInfo(long) TheTVDBApi.getUserInfo(userId)
         * @see Extended#getUserInfo(long) TheTVDBApi.Extended.getUserInfo(userId)
         */
        JsonNode getUserInfo(long userId) throws APIException;

        /**
         * Returns the current favorites of this user as raw JSON.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Favorites/getUserFavorites">
         * <b>[GET]</b> /user/favorites</a>
         *
         * @return JSON object containing the current favorites of this user
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#getUserFavorites() TheTVDBApi.getUserFavorites()
         * @see Extended#getUserFavorites()
         */
        JsonNode getUserFavorites() throws APIException;

        /**
         * Adds the given entities to the users list of favorites and returns the success status of the operation as raw
         * JSON. To create a new favorite record, use the
         * {@link TheTVDBApiFactory#createFavoriteRecordBuilder() factory method} to retrieve a corresponding builder
         * instance.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Favorites/createUserFavorites">
         * <b>[POST]</b> /user/favorites</a>
         *
         * @param favoriteRecord Record containing one or multiple favorite entities
         *
         * @return JSON object containing the success status of the operation
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see TheTVDBApi#createUserFavorites(FavoriteRecord) TheTVDBApi.createUserFavorites(favoriteRecord)
         * @see Extended#createUserFavorites(FavoriteRecord) TheTVDBApi.Extended.createUserFavorites(favoriteRecord)
         * @see TheTVDBApiFactory#createFavoriteRecordBuilder()
         */
        JsonNode createUserFavorites(@Nonnull FavoriteRecord favoriteRecord) throws APIException;
    }

    /**
     * Interface representing the API's <em>{@code Extended}</em> layout.
     * <p><br>
     * This layout may be used for slightly advance API integration. Like the common layout it'll take care of parsing
     * the received JSON into Java DTO's, but it will also provide access to any additional contextual information.
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Artwork%20Statuses/getAllArtworkStatuses">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Artwork%20Types/getAllArtworkTypes">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Artwork/getArtworkBase">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Artwork/getArtworkExtended">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Award%20Categories/getAwardCategory">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Award%20Categories/getAwardCategoryExtended">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Awards/getAllAwards">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Awards/getAward">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Awards/getAwardExtended">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Characters/getCharacterBase">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Companies/getAllCompanies">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Companies/getCompanyTypes">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Companies/getCompany">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Content%20Ratings/getAllContentRatings">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Entity%20Types/getEntityTypes">
         * <b>[GET]</b> /entities</a>
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Episodes/getEpisodeBase">
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
         * Returns a response object containing detailed information for a specific episode based on the given query
         * parameters mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Episodes/getEpisodeExtended">
         * <b>[GET]</b> /episodes/{id}/extended</a>
         *
         * @param episodeId       The <i>TheTVDB.com</i> episode ID
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no episode record with the given ID exists.
         * @see JSON#getEpisodeDetails(long, QueryParameters) TheTVDBApi.JSON.getEpisodeDetails(episodeId,
         *         queryParameters)
         * @see TheTVDBApi#getEpisodeDetails(long, QueryParameters) TheTVDBApi.getEpisodeDetails(episodeId,
         *         queryParameters)
         */
        APIResponse<EpisodeDetails> getEpisodeDetails(long episodeId, QueryParameters queryParameters)
                throws APIException;

        /**
         * Returns a response object containing a translation record for a specific episode mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Episodes/getEpisodeTranslation">
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
        APIResponse<EntityTranslation> getEpisodeTranslation(long episodeId, @Nonnull String language)
                throws APIException;

        /**
         * Returns a response object containing a collection of translation records for a specific list mapped as Java
         * DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Lists/getListTranslation">
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
        APIResponse<Translations<EntityTranslation>> getListTranslation(long listId, @Nonnull String language)
                throws APIException;

        /**
         * Returns a response object containing a collection of lists based on the given query parameters mapped as Java
         * DTO. The collection contains basic information of all lists matching the query parameters.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Lists/getAllLists">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Lists/getList">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Lists/getListExtended">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Genders/getAllGenders">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Genres/getAllGenres">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Genres/getGenreBase">
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
         * Returns a response object containing a collection of available inspiration types mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/InspirationTypes/getAllInspirationTypes">
         * <b>[GET]</b> /inspiration/types</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAllInspirationTypes()
         * @see TheTVDBApi#getAllInspirationTypes() TheTVDBApi.getAllInspirationTypes()
         */
        APIResponse<Collection<InspirationType>> getAllInspirationTypes() throws APIException;

        /**
         * Returns a response object containing a collection of available movie statuses mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Movie%20Statuses/getAllMovieStatuses">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Movies/getAllMovie">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Movies/getMovieBase">
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
         * Returns a response object containing detailed information for a specific movie based on the given query
         * parameters mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Movies/getMovieExtended">
         * <b>[GET]</b> /movies/{id}/extended</a>
         *
         * @param movieId         The <i>TheTVDB.com</i> movie ID
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no movie record with the given ID exists.
         * @see JSON#getMovieDetails(long, QueryParameters) TheTVDBApi.JSON.getMovieDetails(movieId,
         *         queryParameters)
         * @see TheTVDBApi#getMovieDetails(long, QueryParameters) TheTVDBApi.getMovieDetails(movieId,
         *         queryParameters)
         */
        APIResponse<MovieDetails> getMovieDetails(long movieId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a collection of movies based on the given filter parameters mapped as
         * Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Movies/getMoviesFilter">
         * <b>[GET]</b> /movies/filter</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getMoviesFiltered(QueryParameters) TheTVDBApi.JSON.getMoviesFiltered(queryParameters)
         * @see TheTVDBApi#getMoviesFiltered(QueryParameters) TheTVDBApi.getMoviesFiltered(queryParameters)
         */
        APIResponse<Collection<Movie>> getMoviesFiltered(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a translation record for a specific movie mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Movies/getMovieTranslation">
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
        APIResponse<EntityTranslation> getMovieTranslation(long movieId, @Nonnull String language) throws APIException;

        /**
         * Returns a response object containing a collection of available people types mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/People%20Types/getAllPeopleTypes">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/People/getPeopleBase">
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
         * Returns a response object containing detailed information for a specific people based on the given query
         * parameters mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/People/getPeopleExtended">
         * <b>[GET]</b> /people/{id}/extended</a>
         *
         * @param peopleId        The <i>TheTVDB.com</i> people ID
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no people record with the given ID exists.
         * @see JSON#getPeopleDetails(long, QueryParameters) TheTVDBApi.JSON.getPeopleDetails(peopleId,
         *         queryParameters)
         * @see TheTVDBApi#getPeopleDetails(long, QueryParameters) TheTVDBApi.getPeopleDetails(peopleId,
         *         queryParameters)
         */
        APIResponse<PeopleDetails> getPeopleDetails(long peopleId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a translation record for a specific people mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/People/getPeopleTranslation">
         * <b>[GET]</b> /people/{id}/translations/{language}</a>
         *
         * @param peopleId The <i>TheTVDB.com</i> people ID
         * @param language The 2- or 3-character language code
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no people translation record exists for the given ID and
         *                      language.
         * @see JSON#getPeopleTranslation(long, String) TheTVDBApi.JSON.getPeopleTranslation(peopleId, language)
         * @see TheTVDBApi#getPeopleTranslation(long, String) TheTVDBApi.getPeopleTranslation(peopleId, language)
         */
        APIResponse<EntityTranslation> getPeopleTranslation(long peopleId, @Nonnull String language)
                throws APIException;

        /**
         * Returns a response object containing a collection of search results based on the given query parameters
         * mapped as Java DTO. Note that the given query parameters must either contain a valid
         * <em>{@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Search#Q}</em> or
         * <em>{@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Search#QUERY}</em> search term key.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Search/getSearchResults">
         * <b>[GET]</b> /search</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getSearchResults(QueryParameters) TheTVDBApi.JSON.getSearchResults(queryParameters)
         * @see TheTVDBApi#getSearchResults(QueryParameters) TheTVDBApi.getSearchResults(queryParameters)
         */
        APIResponse<Collection<SearchResult>> getSearchResults(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a collection of seasons based on the given query parameters mapped as
         * Java DTO. The collection contains basic information of all seasons matching the query parameters.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Seasons/getAllSeasons">
         * <b>[GET]</b> /seasons</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getAllSeasons(QueryParameters) TheTVDBApi.JSON.getAllSeasons(queryParameters)
         * @see TheTVDBApi#getAllSeasons(QueryParameters) TheTVDBApi.getAllSeasons(queryParameters)
         */
        APIResponse<Collection<Season>> getAllSeasons(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing basic information for a specific season mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Seasons/getSeasonBase">
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
         * Returns a response object containing detailed information for a specific season based on the given query
         * parameters mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Seasons/getSeasonExtended">
         * <b>[GET]</b> /seasons/{id}/extended</a>
         *
         * @param seasonId        The <i>TheTVDB.com</i> season ID
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no season record with the given ID exists.
         * @see JSON#getSeasonDetails(long, QueryParameters) TheTVDBApi.JSON.getSeasonDetails(seasonId,
         *         queryParameters)
         * @see TheTVDBApi#getSeasonDetails(long, QueryParameters) TheTVDBApi.getSeasonDetails(seasonId,
         *         queryParameters)
         */
        APIResponse<SeasonDetails> getSeasonDetails(long seasonId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a collection of available season types mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Seasons/getSeasonTypes">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Seasons/getSeasonTranslation">
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
        APIResponse<EntityTranslation> getSeasonTranslation(long seasonId, @Nonnull String language)
                throws APIException;

        /**
         * Returns a response object containing a collection of available series statuses mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series%20Statuses/getAllSeriesStatuses">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series/getAllSeries">
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
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesBase">
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
         * Returns a response object containing detailed information for a specific series including custom artworks
         * based on the given query parameters mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesArtworks">
         * <b>[GET]</b> /series/{id}/artworks</a>
         *
         * @param seriesId        The <i>TheTVDB.com</i> series ID
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no series record with the given ID exists.
         * @see JSON#getSeriesArtworks(long, QueryParameters) TheTVDBApi.JSON.getSeriesArtworks(seriesId,
         *         queryParameters)
         * @see TheTVDBApi#getSeriesArtworks(long, QueryParameters) TheTVDBApi.getSeriesArtworks(seriesId,
         *         queryParameters)
         */
        APIResponse<SeriesDetails> getSeriesArtworks(long seriesId, QueryParameters queryParameters)
                throws APIException;

        /**
         * Returns a response object containing detailed information for a specific series based on the given query
         * parameters mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesExtended">
         * <b>[GET]</b> /series/{id}/extended</a>
         *
         * @param seriesId        The <i>TheTVDB.com</i> series ID
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no series record with the given ID exists.
         * @see JSON#getSeriesDetails(long, QueryParameters) TheTVDBApi.JSON.getSeriesDetails(seriesId,
         *         queryParameters)
         * @see TheTVDBApi#getSeriesDetails(long, QueryParameters) TheTVDBApi.getSeriesDetails(seriesId,
         *         queryParameters)
         */
        APIResponse<SeriesDetails> getSeriesDetails(long seriesId, QueryParameters queryParameters) throws APIException;

        /**
         * Returns the episodes of a particular series based on the given query parameters mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesEpisodes">
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
         * @see JSON#getSeriesEpisodes(long, SeriesSeasonType, QueryParameters)
         *         TheTVDBApi.JSON.getSeriesEpisodes(seriesId, seasonType, queryParameters)
         * @see TheTVDBApi#getSeriesEpisodes(long, SeriesSeasonType, QueryParameters)
         *         TheTVDBApi.getSeriesEpisodes(seriesId, seasonType, queryParameters)
         */
        APIResponse<SeriesEpisodes> getSeriesEpisodes(long seriesId, @Nonnull SeriesSeasonType seasonType,
                QueryParameters queryParameters) throws APIException;

        /**
         * Returns basic information for a specific series based on the given query parameters mapped as Java DTO. The
         * contained episodes will be translated to the given language.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesSeasonEpisodesTranslated">
         * <b>[GET]</b> /series/{id}/episodes/{season-type}/{lang}</a>
         *
         * @param seriesId        The <i>TheTVDB.com</i> series ID
         * @param seasonType      The type of season for which episodes should be returned
         * @param language        The 2- or 3-character language code
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc. or if no series record with the given ID exists or the language
         *                      code is invalid.
         * @see JSON#getSeriesEpisodesTranslated(long, SeriesSeasonType, String, QueryParameters)
         *         TheTVDBApi.JSON.getSeriesEpisodesTranslated(seriesId, seasonType, language, queryParameters)
         * @see TheTVDBApi#getSeriesEpisodesTranslated(long, SeriesSeasonType, String, QueryParameters)
         *         TheTVDBApi.getSeriesEpisodesTranslated(seriesId, seasonType, language, queryParameters)
         */
        APIResponse<Series> getSeriesEpisodesTranslated(long seriesId, @Nonnull SeriesSeasonType seasonType,
                @Nonnull String language, QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a collection of series based on the given filter parameters mapped as
         * Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesFilter">
         * <b>[GET]</b> /series/filter</a>
         *
         * @param queryParameters Object containing key/value pairs of query parameters
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getSeriesFiltered(QueryParameters) TheTVDBApi.JSON.getSeriesFiltered(queryParameters)
         * @see TheTVDBApi#getSeriesFiltered(QueryParameters) TheTVDBApi.getSeriesFiltered(queryParameters)
         */
        APIResponse<Collection<Series>> getSeriesFiltered(QueryParameters queryParameters) throws APIException;

        /**
         * Returns a response object containing a translation record for a specific series mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Series/getSeriesTranslation">
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
        APIResponse<EntityTranslation> getSeriesTranslation(long seriesId, @Nonnull String language)
                throws APIException;

        /**
         * Returns a response object containing a collection of available source types mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Source%20Types/getAllSourceTypes">
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
         * parameters. Note that the given query parameters must always contain a valid
         * <em>{@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates#SINCE}</em> Epoch timestamp key.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Updates/updates">
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

        /**
         * Returns a response object containing some information about the current user mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/User%20info/getUserInfo">
         * <b>[GET]</b> /user</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getUserInfo()
         * @see TheTVDBApi#getUserInfo() TheTVDBApi.getUserInfo()
         */
        APIResponse<UserInfo> getUserInfo() throws APIException;

        /**
         * Returns a response object containing some information about a specific user mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/User%20info/getUserInfoById">
         * <b>[GET]</b> /user/{id}</a>
         *
         * @param userId The <i>TheTVDB.com</i> user ID
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getUserInfo(long) TheTVDBApi.JSON.getUserInfo(userId)
         * @see TheTVDBApi#getUserInfo(long) TheTVDBApi.getUserInfo(userId)
         */
        APIResponse<UserInfo> getUserInfo(long userId) throws APIException;

        /**
         * Returns a response object containing the current favorites of this user mapped as Java DTO.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Favorites/getUserFavorites">
         * <b>[GET]</b> /user/favorites</a>
         *
         * @return Extended API response containing the actually requested data as well as additional status
         *         information
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#getUserFavorites()
         * @see TheTVDBApi#getUserFavorites() TheTVDBApi.getUserFavorites()
         */
        APIResponse<Favorites> getUserFavorites() throws APIException;

        /**
         * Adds the given entities to the users list of favorites and returns a response object containing the success
         * status of the operation. To create a new favorite record, use the
         * {@link TheTVDBApiFactory#createFavoriteRecordBuilder() factory method} to retrieve a corresponding builder
         * instance.
         * <p><br>
         * <i>Corresponds to remote API route:</i> <a target="_blank"
         * href="https://thetvdb.github.io/v4-api/#/Favorites/createUserFavorites">
         * <b>[POST]</b> /user/favorites</a>
         *
         * @param favoriteRecord Record containing one or multiple favorite entities
         *
         * @return Extended API response containing the success status of the operation
         *
         * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error,
         *                      resource not found, etc.
         * @see JSON#createUserFavorites(FavoriteRecord) TheTVDBApi.JSON.createUserFavorites(favoriteRecord)
         * @see TheTVDBApi#createUserFavorites(FavoriteRecord) TheTVDBApi.createUserFavorites(favoriteRecord)
         * @see TheTVDBApiFactory#createFavoriteRecordBuilder()
         */
        APIResponse<Void> createUserFavorites(@Nonnull FavoriteRecord favoriteRecord) throws APIException;
    }

    /**
     * Specifies the version of the <i>TheTVDB.com</i> remote API to be used by this connector
     */
    final class Version {
        /** Version of the <i>TheTVDB.com</i> remote API used by this connector */
        public static final String API_VERSION = "v4.6.2";

        /** Constant class. Should not be instantiated */
        private Version() {}
    }
}

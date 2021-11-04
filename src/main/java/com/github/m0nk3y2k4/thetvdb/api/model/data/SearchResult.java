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

package com.github.m0nk3y2k4.thetvdb.api.model.data;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Interface representing a <em>{@code SearchResult}</em> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all search result data which was returned by the remote service in
 * JSON format. Methods returning collection-based values will return an empty collection in case no corresponding data
 * was received.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface SearchResult {

    /**
     * Get the value of the {<em>{@code data.objectID}</em>} JSON property
     *
     * @return The <em>{@code objectID}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in SearchResult but returned in JSON. Check again after the next API update.
    @Nullable
    String getObjectID();

    /**
     * Get the value of the {<em>{@code data.aliases}</em>} JSON property
     *
     * @return The <em>{@code aliases}</em> property from the received JSON
     */
    List<String> getAliases();

    /**
     * Get the value of the {<em>{@code data.companies}</em>} JSON property
     *
     * @return The <em>{@code companies}</em> property from the received JSON
     */
    List<String> getCompanies();

    /**
     * Get the value of the {<em>{@code data.companyType}</em>} JSON property
     *
     * @return The <em>{@code companyType}</em> property from the received JSON
     */
    @Nullable
    String getCompanyType();

    /**
     * Get the value of the {<em>{@code data.country}</em>} JSON property
     *
     * @return The <em>{@code country}</em> property from the received JSON
     */
    @Nullable
    String getCountry();

    /**
     * Get the value of the {<em>{@code data.director}</em>} JSON property
     *
     * @return The <em>{@code director}</em> property from the received JSON
     */
    @Nullable
    String getDirector();

    /**
     * Get the value of the {<em>{@code data.extended_title}</em>} JSON property
     *
     * @return The <em>{@code extended_title}</em> property from the received JSON
     */
    @Nullable
    String getExtendedTitle();

    /**
     * Get the value of the {<em>{@code data.genres}</em>} JSON property
     *
     * @return The <em>{@code genres}</em> property from the received JSON
     */
    List<String> getGenres();

    /**
     * Get the value of the {<em>{@code data.studios}</em>} JSON property
     *
     * @return The <em>{@code studios}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in SearchResult but returned in JSON. Check again after the next API update.
    @Nullable
    List<String> getStudios();

    /**
     * Get the value of the {<em>{@code data.id}</em>} JSON property
     *
     * @return The <em>{@code id}</em> property from the received JSON
     */
    @Nullable
    String getId();

    /**
     * Get the value of the {<em>{@code data.image_url}</em>} JSON property
     *
     * @return The <em>{@code image_url}</em> property from the received JSON
     */
    @Nullable
    String getImageUrl();

    /**
     * Get the value of the {<em>{@code data.name}</em>} JSON property
     *
     * @return The <em>{@code name}</em> property from the received JSON
     */
    @Nullable
    String getName();

    /**
     * Get the value of the {<em>{@code data.first_air_time}</em>} JSON property
     *
     * @return The <em>{@code first_air_time}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in SearchResult but returned in JSON. Check again after the next API update.
    @Nullable
    String getFirstAirTime();

    /**
     * Get the value of the {<em>{@code data.name_translated}</em>} JSON property
     *
     * @return The <em>{@code name_translated}</em> property from the received JSON
     */
    Translations<SearchResultTranslation> getNameTranslated();

    /**
     * Get the value of the {<em>{@code data.officialList}</em>} JSON property
     *
     * @return The <em>{@code officialList}</em> property from the received JSON
     */
    @Nullable
    String getOfficialList();

    /**
     * Get the value of the {<em>{@code data.overview}</em>} JSON property
     *
     * @return The <em>{@code overview}</em> property from the received JSON
     */
    @Nullable
    String getOverview();

    /**
     * Get the value of the {<em>{@code data.overview_translated}</em>} JSON property
     *
     * @return The <em>{@code overview_translated}</em> property from the received JSON
     */
    Translations<SearchResultTranslation> getOverviewTranslated();

    /**
     * Get the value of the {<em>{@code data.posters}</em>} JSON property
     *
     * @return The <em>{@code posters}</em> property from the received JSON
     */
    List<String> getPosters();

    /**
     * Get the value of the {<em>{@code data.primary_language}</em>} JSON property
     *
     * @return The <em>{@code primary_language}</em> property from the received JSON
     */
    @Nullable
    String getPrimaryLanguage();

    /**
     * Get the value of the {<em>{@code data.primary_type}</em>} JSON property
     *
     * @return The <em>{@code primary_type}</em> property from the received JSON
     */
    @Nullable
    String getPrimaryType();

    /**
     * Get the value of the {<em>{@code data.status}</em>} JSON property
     *
     * @return The <em>{@code status}</em> property from the received JSON
     */
    @Nullable
    String getStatus();

    /**
     * Get the value of the {<em>{@code data.translationsWithLang}</em>} JSON property
     *
     * @return The <em>{@code translationsWithLang}</em> property from the received JSON
     */
    List<String> getTranslationsWithLang();

    /**
     * Get the value of the {<em>{@code data.tvdb_id}</em>} JSON property
     *
     * @return The <em>{@code tvdb_id}</em> property from the received JSON
     */
    @Nullable
    String getTvdbId();

    /**
     * Get the value of the {<em>{@code data.type}</em>} JSON property
     *
     * @return The <em>{@code type}</em> property from the received JSON
     */
    @Nullable
    String getType();

    /**
     * Get the value of the {<em>{@code data.year}</em>} JSON property
     *
     * @return The <em>{@code year}</em> property from the received JSON
     */
    @Nullable
    Long getYear();

    /**
     * Get the value of the {<em>{@code data.slug}</em>} JSON property
     *
     * @return The <em>{@code slug}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in SearchResult but returned in JSON. Check again after the next API update.
    @Nullable
    String getSlug();

    /**
     * Get the value of the {<em>{@code data.thumbnail}</em>} JSON property
     *
     * @return The <em>{@code thumbnail}</em> property from the received JSON
     */
    @Nullable
    String getThumbnail();

    /**
     * Get the value of the {<em>{@code data.poster}</em>} JSON property
     *
     * @return The <em>{@code poster}</em> property from the received JSON
     */
    @Nullable
    String getPoster();

    /**
     * Get the value of the {<em>{@code data.translations}</em>} JSON property
     *
     * @return The <em>{@code translations}</em> property from the received JSON
     */
    Translations<SearchResultTranslation> getTranslations();

    /**
     * Get the value of the {<em>{@code data.is_official}</em>} JSON property
     *
     * @return The <em>{@code is_official}</em> property from the received JSON
     */
    @Nullable
    Boolean isOfficial();

    /**
     * Get the value of the {<em>{@code data.remote_ids}</em>} JSON property
     *
     * @return The <em>{@code remote_ids}</em> property from the received JSON
     */
    List<RemoteId> getRemoteIds();

    /**
     * Get the value of the {<em>{@code data.network}</em>} JSON property
     *
     * @return The <em>{@code network}</em> property from the received JSON
     */
    @Nullable
    String getNetwork();

    /**
     * Get the value of the {<em>{@code data.title}</em>} JSON property
     *
     * @return The <em>{@code title}</em> property from the received JSON
     */
    @Nullable
    String getTitle();

    /**
     * Get the value of the {<em>{@code data.overviews}</em>} JSON property
     *
     * @return The <em>{@code overviews}</em> property from the received JSON
     */
    Translations<SearchResultTranslation> getOverviews();
}

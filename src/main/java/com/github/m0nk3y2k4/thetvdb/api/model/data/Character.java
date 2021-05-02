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
 * Interface representing a
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/Character">Character</a>
 * data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all character related data which was returned by the remote service
 * in JSON format. Methods returning collection-based values will return an empty collection in case no corresponding
 * data was received.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface Character {

    /**
     * Get the value of the {<em>{@code data.id}</em>} JSON property
     *
     * @return The <em>{@code id}</em> property from the received JSON
     */
    @Nullable
    Long getId();

    /**
     * Get the value of the {<em>{@code data.name}</em>} JSON property
     *
     * @return The <em>{@code name}</em> property from the received JSON
     */
    @Nullable
    String getName();

    /**
     * Get the value of the {<em>{@code data.peopleId}</em>} JSON property
     *
     * @return The <em>{@code peopleId}</em> property from the received JSON
     */
    @Nullable
    Long getPeopleId();

    /**
     * Get the value of the {<em>{@code data.seriesId}</em>} JSON property
     *
     * @return The <em>{@code seriesId}</em> property from the received JSON
     */
    @Nullable
    Long getSeriesId();

    /**
     * Get the value of the {<em>{@code data.movieId}</em>} JSON property
     *
     * @return The <em>{@code movieId}</em> property from the received JSON
     */
    @Nullable
    Long getMovieId();

    /**
     * Get the value of the {<em>{@code data.episodeId}</em>} JSON property
     *
     * @return The <em>{@code episodeId}</em> property from the received JSON
     */
    @Nullable
    Long getEpisodeId();

    /**
     * Get the value of the {<em>{@code data.type}</em>} JSON property
     *
     * @return The <em>{@code type}</em> property from the received JSON
     */
    @Nullable
    Long getType();

    /**
     * Get the value of the {<em>{@code data.image}</em>} JSON property
     *
     * @return The <em>{@code image}</em> property from the received JSON
     */
    @Nullable
    String getImage();

    /**
     * Get the value of the {<em>{@code data.sort}</em>} JSON property
     *
     * @return The <em>{@code sort}</em> property from the received JSON
     */
    @Nullable
    Long getSort();

    /**
     * Get the value of the {<em>{@code data.isFeatured}</em>} JSON property
     *
     * @return The <em>{@code isFeatured}</em> property from the received JSON
     */
    @Nullable
    Boolean isFeatured();

    /**
     * Get the value of the {<em>{@code data.url}</em>} JSON property
     *
     * @return The <em>{@code url}</em> property from the received JSON
     */
    @Nullable
    String getUrl();

    /**
     * Get the value of the {<em>{@code data.nameTranslations}</em>} JSON property
     *
     * @return The <em>{@code nameTranslations}</em> property from the received JSON
     */
    List<String> getNameTranslations();

    /**
     * Get the value of the {<em>{@code data.overviewTranslations}</em>} JSON property
     *
     * @return The <em>{@code overviewTranslations}</em> property from the received JSON
     */
    List<String> getOverviewTranslations();

    /**
     * Get the value of the {<em>{@code data.aliases}</em>} JSON property
     *
     * @return The <em>{@code aliases}</em> property from the received JSON
     */
    List<Alias> getAliases();

    /**
     * Get the value of the {<em>{@code data.peopleType}</em>} JSON property
     *
     * @return The <em>{@code peopleType}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in Character but returned in JSON. Check again after the next API update.
    @Nullable
    String getPeopleType();

    /**
     * Get the value of the {<em>{@code data.personName}</em>} JSON property
     *
     * @return The <em>{@code personName}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in Character but returned in JSON. Check again after the next API update.
    @Nullable
    String getPersonName();
}

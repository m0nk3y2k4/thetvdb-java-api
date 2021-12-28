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
 * Interface representing a <em>{@code EpisodeBaseRecord}</em> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to basic episode related data which was returned by the remote service
 * in JSON format. Methods returning collection-based values will return an empty collection in case no corresponding
 * data was received.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface Episode {

    /**
     * Get the value of the {<em>{@code data.id}</em>} JSON property
     *
     * @return The <em>{@code id}</em> property from the received JSON
     */
    @Nullable
    Long getId();

    /**
     * Get the value of the {<em>{@code data.seriesId}</em>} JSON property
     *
     * @return The <em>{@code seriesId}</em> property from the received JSON
     */
    @Nullable
    Long getSeriesId();

    /**
     * Get the value of the {<em>{@code data.name}</em>} JSON property
     *
     * @return The <em>{@code name}</em> property from the received JSON
     */
    @Nullable
    String getName();

    /**
     * Get the value of the {<em>{@code data.aired}</em>} JSON property
     *
     * @return The <em>{@code aired}</em> property from the received JSON
     */
    @Nullable
    String getAired();

    /**
     * Get the value of the {<em>{@code data.runtime}</em>} JSON property
     *
     * @return The <em>{@code runtime}</em> property from the received JSON
     */
    @Nullable
    Long getRuntime();

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
     * Get the value of the {<em>{@code data.image}</em>} JSON property
     *
     * @return The <em>{@code image}</em> property from the received JSON
     */
    @Nullable
    String getImage();

    /**
     * Get the value of the {<em>{@code data.imageType}</em>} JSON property
     *
     * @return The <em>{@code imageType}</em> property from the received JSON
     */
    @Nullable
    Long getImageType();

    /**
     * Get the value of the {<em>{@code data.isMovie}</em>} JSON property mapped as boolean
     *
     * @return The <em>{@code isMovie}</em> property from the received JSON mapped as boolean
     */
    @Nullable
    Boolean isMovie();

    /**
     * Get the value of the {<em>{@code data.seasons}</em>} JSON property
     *
     * @return The <em>{@code seasons}</em> property from the received JSON
     */
    List<Season> getSeasons();

    /**
     * Get the value of the {<em>{@code data.number}</em>} JSON property
     *
     * @return The <em>{@code number}</em> property from the received JSON
     */
    @Nullable
    Long getNumber();

    /**
     * Get the value of the {<em>{@code data.seasonNumber}</em>} JSON property
     *
     * @return The <em>{@code seasonNumber}</em> property from the received JSON
     */
    @Nullable
    Long getSeasonNumber();

    /**
     * Get the value of the {<em>{@code data.lastUpdated}</em>} JSON property
     *
     * @return The <em>{@code lastUpdated}</em> property from the received JSON
     */
    @Nullable
    String getLastUpdated();

    /**
     * Get the value of the {<em>{@code data.finaleType}</em>} JSON property
     *
     * @return The <em>{@code finaleType}</em> property from the received JSON
     */
    @Nullable
    String getFinaleType();

    /**
     * Get the value of the {<em>{@code data.overview}</em>} JSON property
     *
     * @return The <em>{@code overview}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in EpisodeBaseRecord but returned in JSON. Check again after the next API update.
    @Nullable
    String getOverview();

    /**
     * Get the value of the {<em>{@code data.seasonName}</em>} JSON property
     *
     * @return The <em>{@code seasonName}</em> property from the received JSON
     */
    @Nullable
    String getSeasonName();
}

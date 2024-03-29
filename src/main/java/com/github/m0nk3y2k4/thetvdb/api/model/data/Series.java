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

package com.github.m0nk3y2k4.thetvdb.api.model.data;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Interface representing a <em>{@code SeriesBaseRecord}</em> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to basic series related data which was returned by the remote service
 * in JSON format. Methods returning collection-based values will return an empty collection in case no corresponding
 * data was received.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface Series {

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
     * Get the value of the {<em>{@code data.slug}</em>} JSON property
     *
     * @return The <em>{@code slug}</em> property from the received JSON
     */
    @Nullable
    String getSlug();

    /**
     * Get the value of the {<em>{@code data.image}</em>} JSON property
     *
     * @return The <em>{@code image}</em> property from the received JSON
     */
    @Nullable
    String getImage();

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
     * Get the value of the {<em>{@code data.firstAired}</em>} JSON property
     *
     * @return The <em>{@code firstAired}</em> property from the received JSON
     */
    @Nullable
    String getFirstAired();

    /**
     * Get the value of the {<em>{@code data.lastAired}</em>} JSON property
     *
     * @return The <em>{@code lastAired}</em> property from the received JSON
     */
    @Nullable
    String getLastAired();

    /**
     * Get the value of the {<em>{@code data.nextAired}</em>} JSON property
     *
     * @return The <em>{@code nextAired}</em> property from the received JSON
     */
    @Nullable
    String getNextAired();

    /**
     * Get the value of the {<em>{@code data.score}</em>} JSON property
     *
     * @return The <em>{@code score}</em> property from the received JSON
     */
    @Nullable
    Double getScore();

    /**
     * Get the value of the {<em>{@code data.status}</em>} JSON property
     *
     * @return The <em>{@code status}</em> property from the received JSON
     */
    @Nullable
    Status getStatus();

    /**
     * Get the value of the {<em>{@code data.originalCountry}</em>} JSON property
     *
     * @return The <em>{@code originalCountry}</em> property from the received JSON
     */
    @Nullable
    String getOriginalCountry();

    /**
     * Get the value of the {<em>{@code data.originalLanguage}</em>} JSON property
     *
     * @return The <em>{@code originalLanguage}</em> property from the received JSON
     */
    @Nullable
    String getOriginalLanguage();

    /**
     * Get the value of the {<em>{@code data.country}</em>} JSON property
     *
     * @return The <em>{@code country}</em> property from the received JSON
     */
    @Nullable
    String getCountry();

    /**
     * Get the value of the {<em>{@code data.defaultSeasonType}</em>} JSON property
     *
     * @return The <em>{@code defaultSeasonType}</em> property from the received JSON
     */
    @Nullable
    Long getDefaultSeasonType();

    /**
     * Get the value of the {<em>{@code data.isOrderRandomized}</em>} JSON property
     *
     * @return The <em>{@code isOrderRandomized}</em> property from the received JSON
     */
    @Nullable
    Boolean isOrderRandomized();

    /**
     * Get the value of the {<em>{@code data.lastUpdated}</em>} JSON property
     *
     * @return The <em>{@code lastUpdated}</em> property from the received JSON
     */
    @Nullable
    String getLastUpdated();

    /**
     * Get the value of the {<em>{@code data.averageRuntime}</em>} JSON property
     *
     * @return The <em>{@code averageRuntime}</em> property from the received JSON
     */
    @Nullable
    Long getAverageRuntime();

    /**
     * Get the value of the {<em>{@code data.overview}</em>} JSON property
     *
     * @return The <em>{@code overview}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in SeriesBaseRecord but returned in JSON. Check again after the next API update.
    @Nullable
    String getOverview();

    /**
     * Get the value of the {<em>{@code data.episodes}</em>} JSON property
     * <p><br>
     * <b>Note:</b>
     * The content of this field strongly depends on the context in which the object is retrieved, meaning that the
     * returned list will only contain elements in specific cases like
     * <ul>
     *     <li>
     *         Retrieving translated episode data via the <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Series/getSeriesSeasonEpisodesTranslated">
     *             <b>[GET]</b> /series/{id}/episodes/{season-type}/{lang}</a> endpoint
     *     </li>
     *     <li>
     *         Explicitly requesting episode data via the <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Series/getSeriesExtended">
     *             <b>[GET]</b> /series/{id}/extended</a> endpoint. See
     *             {@link com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesMeta#EPISODES SeriesMeta.EPISODES}.
     *     </li>
     * </ul>
     *
     * @return The <em>{@code episodes}</em> property from the received JSON
     */
    List<Episode> getEpisodes();
}

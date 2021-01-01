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
 * Interface representing an <i>Episode</i> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all episode related data which was returned by the remote service in
 * JSON format. Please note that, as the remote service declares all of the properties to be optional, most of the
 * methods are marked as {@link Nullable}. Methods returning collection-based values however will return an empty
 * collection in case no corresponding data was received.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface Episode {

    /**
     * Get the value of the {<em>{@code data.absoluteNumber}</em>} JSON property
     *
     * @return The <em>{@code absoluteNumber}</em> property from the received JSON
     */
    @Nullable
    Long getAbsoluteNumber();

    /**
     * Get the value of the {<em>{@code data.airedEpisodeNumber}</em>} JSON property
     *
     * @return The <em>{@code airedEpisodeNumber}</em> property from the received JSON
     */
    @Nullable
    Long getAiredEpisodeNumber();

    /**
     * Get the value of the {<em>{@code data.airedSeason}</em>} JSON property
     *
     * @return The <em>{@code airedSeason}</em> property from the received JSON
     */
    @Nullable
    Long getAiredSeason();

    /**
     * Get the value of the {<em>{@code data.airsAfterSeason}</em>} JSON property
     *
     * @return The <em>{@code airsAfterSeason}</em> property from the received JSON
     */
    @Nullable
    Long getAirsAfterSeason();

    /**
     * Get the value of the {<em>{@code data.airsBeforeEpisode}</em>} JSON property
     *
     * @return The <em>{@code airsBeforeEpisode}</em> property from the received JSON
     */
    @Nullable
    Long getAirsBeforeEpisode();

    /**
     * Get the value of the {<em>{@code data.airsBeforeSeason}</em>} JSON property
     *
     * @return The <em>{@code airsBeforeSeason}</em> property from the received JSON
     */
    @Nullable
    Long getAirsBeforeSeason();

    /**
     * Get the value of the {<em>{@code data.director}</em>} JSON property
     *
     * @return The <em>{@code director}</em> property from the received JSON
     *
     * @deprecated Will be removed in future API release. Use {@link #getDirectors()} instead.
     */
    @Deprecated(since = "0.0.1", forRemoval = true)
    @Nullable
    String getDirector();

    /**
     * Get all values of the {<em>{@code data.directors}</em>} JSON property in a list
     *
     * @return The <em>{@code directors}</em> property from the received JSON as list
     */
    List<String> getDirectors();

    /**
     * Get the value of the {<em>{@code data.dvdChapter}</em>} JSON property
     *
     * @return The <em>{@code dvdChapter}</em> property from the received JSON
     */
    @Nullable
    Long getDvdChapter();

    /**
     * Get the value of the {<em>{@code data.dvdDiscid}</em>} JSON property
     *
     * @return The <em>{@code dvdDiscid}</em> property from the received JSON
     */
    @Nullable
    String getDvdDiscid();

    /**
     * Get the value of the {<em>{@code data.dvdEpisodeNumber}</em>} JSON property
     *
     * @return The <em>{@code dvdEpisodeNumber}</em> property from the received JSON
     */
    @Nullable
    Long getDvdEpisodeNumber();

    /**
     * Get the value of the {<em>{@code data.dvdSeason}</em>} JSON property
     *
     * @return The <em>{@code dvdSeason}</em> property from the received JSON
     */
    @Nullable
    Long getDvdSeason();

    /**
     * Get the value of the {<em>{@code data.episodeName}</em>} JSON property
     *
     * @return The <em>{@code episodeName}</em> property from the received JSON
     */
    @Nullable
    String getEpisodeName();

    /**
     * Get the value of the {<em>{@code data.filename}</em>} JSON property
     *
     * @return The <em>{@code filename}</em> property from the received JSON
     */
    @Nullable
    String getFilename();

    /**
     * Get the value of the {<em>{@code data.firstAired}</em>} JSON property
     *
     * @return The <em>{@code firstAired}</em> property from the received JSON
     */
    @Nullable
    String getFirstAired();

    /**
     * Get all values of the {<em>{@code data.guestStars}</em>} JSON property in a list
     *
     * @return The <em>{@code guestStars}</em> property from the received JSON as list
     */
    List<String> getGuestStars();

    /**
     * Get the value of the {<em>{@code data.id}</em>} JSON property
     *
     * @return The <em>{@code id}</em> property from the received JSON
     */
    @Nullable
    Long getId();

    /**
     * Get the value of the {<em>{@code data.imdbId}</em>} JSON property
     *
     * @return The <em>{@code imdbId}</em> property from the received JSON
     */
    @Nullable
    String getImdbId();

    /**
     * Get the value of the {<em>{@code data.lastUpdated}</em>} JSON property
     *
     * @return The <em>{@code lastUpdated}</em> property from the received JSON
     */
    @Nullable
    Long getLastUpdated();

    /**
     * Get the value of the {<em>{@code data.lastUpdatedBy}</em>} JSON property
     *
     * @return The <em>{@code lastUpdatedBy}</em> property from the received JSON
     */
    @Nullable
    String getLastUpdatedBy();

    /**
     * Get the value of the {<em>{@code data.overview}</em>} JSON property
     *
     * @return The <em>{@code overview}</em> property from the received JSON
     */
    @Nullable
    String getOverview();

    /**
     * Get the value of the {<em>{@code data.productionCode}</em>} JSON property
     *
     * @return The <em>{@code productionCode}</em> property from the received JSON
     */
    @Nullable
    String getProductionCode();

    /**
     * Get the value of the {<em>{@code data.seriesId}</em>} JSON property
     *
     * @return The <em>{@code seriesId}</em> property from the received JSON
     */
    @Nullable
    String getSeriesId();

    /**
     * Get the value of the {<em>{@code data.showUrl}</em>} JSON property
     *
     * @return The <em>{@code showUrl}</em> property from the received JSON
     */
    @Nullable
    String getShowUrl();

    /**
     * Get the value of the {<em>{@code data.siteRating}</em>} JSON property
     *
     * @return The <em>{@code siteRating}</em> property from the received JSON
     */
    @Nullable
    Double getSiteRating();

    /**
     * Get the value of the {<em>{@code data.siteRatingCount}</em>} JSON property
     *
     * @return The <em>{@code siteRatingCount}</em> property from the received JSON
     */
    @Nullable
    Long getSiteRatingCount();

    /**
     * Get the value of the {<em>{@code data.thumbAdded}</em>} JSON property
     *
     * @return The <em>{@code thumbAdded}</em> property from the received JSON
     */
    @Nullable
    String getThumbAdded();

    /**
     * Get the value of the {<em>{@code data.thumbAuthor}</em>} JSON property
     *
     * @return The <em>{@code thumbAuthor}</em> property from the received JSON
     */
    @Nullable
    Long getThumbAuthor();

    /**
     * Get the value of the {<em>{@code data.thumbHeight}</em>} JSON property
     *
     * @return The <em>{@code thumbHeight}</em> property from the received JSON
     */
    @Nullable
    String getThumbHeight();

    /**
     * Get the value of the {<em>{@code data.thumbWidth}</em>} JSON property
     *
     * @return The <em>{@code thumbWidth}</em> property from the received JSON
     */
    @Nullable
    String getThumbWidth();

    /**
     * Get all values of the {<em>{@code data.writers}</em>} JSON property in a list
     *
     * @return The <em>{@code writers}</em> property from the received JSON as list
     */
    List<String> getWriters();
}

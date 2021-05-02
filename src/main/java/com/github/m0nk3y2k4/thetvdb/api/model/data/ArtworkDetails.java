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

import javax.annotation.Nullable;

/**
 * Interface representing an
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/ArtworkExtendedRecord">ArtworkExtendedRecord</a>
 * data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all artwork related data which was returned by the remote service in
 * JSON format.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface ArtworkDetails {

    /**
     * Get the value of the {<em>{@code data.episodeId}</em>} JSON property
     *
     * @return The <em>{@code episodeId}</em> property from the received JSON
     */
    @Nullable
    Long getEpisodeId();

    /**
     * Get the value of the {<em>{@code data.height}</em>} JSON property
     *
     * @return The <em>{@code height}</em> property from the received JSON
     */
    @Nullable
    Long getHeight();

    /**
     * Get the value of the {<em>{@code data.id}</em>} JSON property
     *
     * @return The <em>{@code id}</em> property from the received JSON
     */
    @Nullable
    Long getId();

    /**
     * Get the value of the {<em>{@code data.image}</em>} JSON property
     *
     * @return The <em>{@code image}</em> property from the received JSON
     */
    @Nullable
    String getImage();

    /**
     * Get the value of the {<em>{@code data.language}</em>} JSON property
     *
     * @return The <em>{@code language}</em> property from the received JSON
     */
    @Nullable
    String getLanguage();

    /**
     * Get the value of the {<em>{@code data.movieId}</em>} JSON property
     *
     * @return The <em>{@code movieId}</em> property from the received JSON
     */
    @Nullable
    Long getMovieId();

    /**
     * Get the value of the {<em>{@code data.networkId}</em>} JSON property
     *
     * @return The <em>{@code networkId}</em> property from the received JSON
     */
    @Nullable
    Long getNetworkId();

    /**
     * Get the value of the {<em>{@code data.peopleId}</em>} JSON property
     *
     * @return The <em>{@code peopleId}</em> property from the received JSON
     */
    @Nullable
    Long getPeopleId();

    /**
     * Get the value of the {<em>{@code data.score}</em>} JSON property
     *
     * @return The <em>{@code score}</em> property from the received JSON
     */
    @Nullable
    Double getScore();

    /**
     * Get the value of the {<em>{@code data.seasonId}</em>} JSON property
     *
     * @return The <em>{@code seasonId}</em> property from the received JSON
     */
    @Nullable
    Long getSeasonId();

    /**
     * Get the value of the {<em>{@code data.seriesId}</em>} JSON property
     *
     * @return The <em>{@code seriesId}</em> property from the received JSON
     */
    @Nullable
    Long getSeriesId();

    /**
     * Get the value of the {<em>{@code data.seriesPeopleId}</em>} JSON property
     *
     * @return The <em>{@code seriesPeopleId}</em> property from the received JSON
     */
    @Nullable
    Long getSeriesPeopleId();

    /**
     * Get the value of the {<em>{@code data.thumbnail}</em>} JSON property
     *
     * @return The <em>{@code thumbnail}</em> property from the received JSON
     */
    @Nullable
    String getThumbnail();

    /**
     * Get the value of the {<em>{@code data.thumbnailHeight}</em>} JSON property
     *
     * @return The <em>{@code thumbnailHeight}</em> property from the received JSON
     */
    @Nullable
    Long getThumbnailHeight();

    /**
     * Get the value of the {<em>{@code data.thumbnailWidth}</em>} JSON property
     *
     * @return The <em>{@code thumbnailWidth}</em> property from the received JSON
     */
    @Nullable
    Long getThumbnailWidth();

    /**
     * Get the value of the {<em>{@code data.type}</em>} JSON property
     *
     * @return The <em>{@code type}</em> property from the received JSON
     */
    @Nullable
    Long getType();

    /**
     * Get the value of the {<em>{@code data.updatedAt}</em>} JSON property
     *
     * @return The <em>{@code updatedAt}</em> property from the received JSON
     */
    @Nullable
    Long getUpdatedAt();

    /**
     * Get the value of the {<em>{@code data.width}</em>} JSON property
     *
     * @return The <em>{@code width}</em> property from the received JSON
     */
    @Nullable
    Long getWidth();
}

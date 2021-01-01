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
import java.util.Optional;
import java.util.OptionalLong;

/**
 * Interface representing a
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.0.0#/EpisodeExtendedRecord">EpisodeExtendedRecord</a>
 * data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all episode related data which was returned by the remote service in
 * JSON format. Properties that are declared to be nullable in the remote service documentation will be returned as Java
 * Optionals. Methods returning collection-based values will return an empty collection in case no corresponding data
 * was received.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface EpisodeDetails {

    /**
     * Get the value of the {<em>{@code data.id}</em>} JSON property
     *
     * @return The <em>{@code id}</em> property from the received JSON
     */
    Long getId();

    /**
     * Get the value of the {<em>{@code data.seriesId}</em>} JSON property
     *
     * @return The <em>{@code seriesId}</em> property from the received JSON
     */
    Long getSeriesId();

    /**
     * Get the value of the {<em>{@code data.name}</em>} JSON property
     *
     * @return The <em>{@code name}</em> property from the received JSON
     */
    Optional<String> getName();

    /**
     * Get the value of the {<em>{@code data.aired}</em>} JSON property
     *
     * @return The <em>{@code aired}</em> property from the received JSON
     */
    Optional<String> getAired();

    /**
     * Get the value of the {<em>{@code data.runtime}</em>} JSON property
     *
     * @return The <em>{@code runtime}</em> property from the received JSON
     */
    OptionalLong getRuntime();

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
    Optional<String> getImage();

    /**
     * Get the value of the {<em>{@code data.imageType}</em>} JSON property
     *
     * @return The <em>{@code imageType}</em> property from the received JSON
     */
    OptionalLong getImageType();

    /**
     * Get the value of the {<em>{@code data.isMovie}</em>} JSON property
     *
     * @return The <em>{@code isMovie}</em> property from the received JSON
     */
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
    OptionalLong getNumber();

    /**
     * Get the value of the {<em>{@code data.seasonNumber}</em>} JSON property
     *
     * @return The <em>{@code seasonNumber}</em> property from the received JSON
     */
    OptionalLong getSeasonNumber();

    /**
     * Get the value of the {<em>{@code data.productionCode}</em>} JSON property
     *
     * @return The <em>{@code productionCode}</em> property from the received JSON
     */
    Optional<String> getProductionCode();

    /**
     * Get the value of the {<em>{@code data.airsAfterSeason}</em>} JSON property
     *
     * @return The <em>{@code airsAfterSeason}</em> property from the received JSON
     */
    OptionalLong getAirsAfterSeason();

    /**
     * Get the value of the {<em>{@code data.airsBeforeSeason}</em>} JSON property
     *
     * @return The <em>{@code airsBeforeSeason}</em> property from the received JSON
     */
    OptionalLong getAirsBeforeSeason();

    /**
     * Get the value of the {<em>{@code data.airsBeforeEpisode}</em>} JSON property
     *
     * @return The <em>{@code airsBeforeEpisode}</em> property from the received JSON
     */
    OptionalLong getAirsBeforeEpisode();

    /**
     * Get the value of the {<em>{@code data.network}</em>} JSON property
     *
     * @return The <em>{@code network}</em> property from the received JSON
     */
    Network getNetwork();

    /**
     * Get the value of the {<em>{@code data.awards}</em>} JSON property
     *
     * @return The <em>{@code awards}</em> property from the received JSON
     */
    List<Award> getAwards();

    /**
     * Get the value of the {<em>{@code data.characters}</em>} JSON property
     *
     * @return The <em>{@code characters}</em> property from the received JSON
     */
    List<Character> getCharacters();

    /**
     * Get the value of the {<em>{@code data.contentRatings}</em>} JSON property
     *
     * @return The <em>{@code contentRatings}</em> property from the received JSON
     */
    List<ContentRating> getContentRatings();

    /**
     * Get the value of the {<em>{@code data.remoteIds}</em>} JSON property
     *
     * @return The <em>{@code remoteIds}</em> property from the received JSON
     */
    List<RemoteId> getRemoteIds();

    /**
     * Get the value of the {<em>{@code data.tagOptions}</em>} JSON property
     *
     * @return The <em>{@code tagOptions}</em> property from the received JSON
     */
    List<TagOption> getTagOptions();

    /**
     * Get the value of the {<em>{@code data.trailers}</em>} JSON property
     *
     * @return The <em>{@code trailers}</em> property from the received JSON
     */
    List<Trailer> getTrailers();
}
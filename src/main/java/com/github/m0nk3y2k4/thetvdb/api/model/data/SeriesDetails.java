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
import java.util.Optional;

import javax.annotation.Nullable;

/**
 * Interface representing a <em>{@code SeriesExtendedRecord}</em> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all series related data which was returned by the remote service in
 * JSON format. Methods returning collection-based values will return an empty collection in case no corresponding data
 * was received.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface SeriesDetails extends Series {

    /**
     * Get the value of the {<em>{@code data.artworks}</em>} JSON property
     *
     * @return The <em>{@code artworks}</em> property from the received JSON
     */
    List<ArtworkDetails> getArtworks();

    /**
     * Get the value of the {<em>{@code data.genres}</em>} JSON property
     *
     * @return The <em>{@code genres}</em> property from the received JSON
     */
    List<Genre> getGenres();

    /**
     * Get the value of the {<em>{@code data.trailers}</em>} JSON property
     *
     * @return The <em>{@code trailers}</em> property from the received JSON
     */
    List<Trailer> getTrailers();

    /**
     * Get the value of the {<em>{@code data.lists}</em>} JSON property
     *
     * @return The <em>{@code lists}</em> property from the received JSON
     */
    List<FCList> getLists();

    /**
     * Get the value of the {<em>{@code data.remoteIds}</em>} JSON property
     *
     * @return The <em>{@code remoteIds}</em> property from the received JSON
     */
    List<RemoteId> getRemoteIds();

    /**
     * Get the value of the {<em>{@code data.characters}</em>} JSON property
     *
     * @return The <em>{@code characters}</em> property from the received JSON
     */
    List<Character> getCharacters();

    /**
     * Get the value of the {<em>{@code data.airsDays}</em>} JSON property
     *
     * @return The <em>{@code airsDays}</em> property from the received JSON
     */
    @Nullable
    SeriesAirsDays getAirsDays();

    /**
     * Get the value of the {<em>{@code data.abbreviation}</em>} JSON property
     *
     * @return The <em>{@code abbreviation}</em> property from the received JSON
     */
    @Nullable
    String getAbbreviation();

    /**
     * Get the value of the {<em>{@code data.airsTime}</em>} JSON property
     *
     * @return The <em>{@code airsTime}</em> property from the received JSON
     */
    @Nullable
    String getAirsTime();

    /**
     * Get the value of the {<em>{@code data.seasons}</em>} JSON property
     *
     * @return The <em>{@code seasons}</em> property from the received JSON
     */
    List<Season> getSeasons();

    /**
     * Get the value of the {<em>{@code data.companies}</em>} JSON property
     *
     * @return The <em>{@code companies}</em> property from the received JSON
     */
    List<Company> getCompanies();

    /**
     * Get the value of the {<em>{@code data.translations}</em>} JSON property
     * <p><br>
     * <b>Note:</b> Field will only be present if these data is explicitly requested. See {@link
     * com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesMeta#TRANSLATIONS SeriesMeta.TRANSLATIONS}.
     *
     * @return The <em>{@code translations}</em> property from the received JSON
     */
    Optional<MetaTranslations> getTranslations();

    /**
     * Get the value of the {<em>{@code data.originalNetwork}</em>} JSON property
     *
     * @return The <em>{@code originalNetwork}</em> property from the received JSON
     */
    @Nullable
    Company getOriginalNetwork();

    /**
     * Get the value of the {<em>{@code data.latestNetwork}</em>} JSON property
     *
     * @return The <em>{@code latestNetwork}</em> property from the received JSON
     */
    @Nullable
    Company getLatestNetwork();

    /**
     * Get the value of the {<em>{@code data.tags}</em>} JSON property
     *
     * @return The <em>{@code tags}</em> property from the received JSON
     */
    List<TagOption> getTags();

    /**
     * Get the value of the {<em>{@code data.contentRatings}</em>} JSON property
     *
     * @return The <em>{@code contentRatings}</em> property from the received JSON
     */
    List<ContentRating> getContentRatings();

    /**
     * Get the value of the {<em>{@code data.seasonTypes}</em>} JSON property
     *
     * @return The <em>{@code seasonTypes}</em> property from the received JSON
     */
    List<SeasonType> getSeasonTypes();
}

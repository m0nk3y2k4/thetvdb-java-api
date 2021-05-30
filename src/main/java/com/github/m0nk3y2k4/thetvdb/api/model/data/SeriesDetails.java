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
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/SeriesExtendedRecord">SeriesExtendedRecord</a>
 * data transfer object.
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
    List<Artwork> getArtworks();

    /**
     * Get the value of the {<em>{@code data.networks}</em>} JSON property
     *
     * @return The <em>{@code networks}</em> property from the received JSON
     */
    List<Network> getNetworks();

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
    // ToDo: Field is currently not declared in SeriesExtendedRecord but returned in JSON. Check again after the next API update.
    List<Company> getCompanies();

    /**
     * Get the value of the {<em>{@code data.airsTimeUTC}</em>} JSON property
     *
     * @return The <em>{@code airsTimeUTC}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in SeriesExtendedRecord but returned in JSON. Check again after the next API update.
    @Nullable
    String getAirsTimeUTC();

    /**
     * Get the value of the {<em>{@code data.translations}</em>} JSON property
     *
     * @return The <em>{@code translations}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in SeriesExtendedRecord but returned in JSON. Check again after the next API update.
    @Nullable
    Translations getTranslations();
}

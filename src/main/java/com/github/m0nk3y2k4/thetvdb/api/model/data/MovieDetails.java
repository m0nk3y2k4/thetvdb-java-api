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
 * Interface representing a <em>{@code MovieExtendedRecord}</em> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all movie related data which was returned by the remote service in
 * JSON format. Methods returning collection-based values will return an empty collection in case no corresponding data
 * was received.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface MovieDetails extends Movie {

    /**
     * Get the value of the {<em>{@code data.artworks}</em>} JSON property
     *
     * @return The <em>{@code artworks}</em> property from the received JSON
     */
    List<Artwork> getArtworks();

    /**
     * Get the value of the {<em>{@code data.audioLanguages}</em>} JSON property
     *
     * @return The <em>{@code audioLanguages}</em> property from the received JSON
     */
    List<String> getAudioLanguages();

    /**
     * Get the value of the {<em>{@code data.awards}</em>} JSON property
     *
     * @return The <em>{@code awards}</em> property from the received JSON
     */
    List<Award> getAwards();

    /**
     * Get the value of the {<em>{@code data.boxOffice}</em>} JSON property
     *
     * @return The <em>{@code boxOffice}</em> property from the received JSON
     */
    @Nullable
    String getBoxOffice();

    /**
     * Get the value of the {<em>{@code data.budget}</em>} JSON property
     *
     * @return The <em>{@code budget}</em> property from the received JSON
     */
    @Nullable
    String getBudget();

    /**
     * Get the value of the {<em>{@code data.characters}</em>} JSON property
     *
     * @return The <em>{@code characters}</em> property from the received JSON
     */
    List<Character> getCharacters();

    /**
     * Get the value of the {<em>{@code data.companies}</em>} JSON property
     *
     * @return The <em>{@code companies}</em> property from the received JSON
     */
    Companies getCompanies();

    /**
     * Get the value of the {<em>{@code data.contentRatings}</em>} JSON property
     *
     * @return The <em>{@code contentRatings}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in MovieExtendedRecord but returned in JSON. Check again after the next API update.
    List<ContentRating> getContentRatings();

    /**
     * Get the value of the {<em>{@code data.lists}</em>} JSON property
     *
     * @return The <em>{@code lists}</em> property from the received JSON
     */
    List<FCList> getLists();

    /**
     * Get the value of the {<em>{@code data.genres}</em>} JSON property
     *
     * @return The <em>{@code genres}</em> property from the received JSON
     */
    List<Genre> getGenres();

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
     * Get the value of the {<em>{@code data.releases}</em>} JSON property
     *
     * @return The <em>{@code releases}</em> property from the received JSON
     */
    List<Release> getReleases();

    /**
     * Get the value of the {<em>{@code data.remoteIds}</em>} JSON property
     *
     * @return The <em>{@code remoteIds}</em> property from the received JSON
     */
    List<RemoteId> getRemoteIds();

    /**
     * Get the value of the {<em>{@code data.studios}</em>} JSON property
     *
     * @return The <em>{@code studios}</em> property from the received JSON
     */
    List<Studio> getStudios();

    /**
     * Get the value of the {<em>{@code data.subtitleLanguages}</em>} JSON property
     *
     * @return The <em>{@code subtitleLanguages}</em> property from the received JSON
     */
    List<String> getSubtitleLanguages();

    /**
     * Get the value of the {<em>{@code data.tagOptions}</em>} JSON property
     *
     * @return The <em>{@code tagOptions}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in MovieExtendedRecord but returned in JSON. Check again after the next API update.
    List<TagOption> getTagOptions();

    /**
     * Get the value of the {<em>{@code data.trailers}</em>} JSON property
     *
     * @return The <em>{@code trailers}</em> property from the received JSON
     */
    List<Trailer> getTrailers();
}

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

/**
 * Interface representing a <em>{@code SeasonExtendedRecord}</em> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all season related data which was returned by the remote service in
 * JSON format. Methods returning collection-based values will return an empty collection in case no corresponding data
 * was received.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface SeasonDetails extends Season {

    /**
     * Get the value of the {<em>{@code data.artwork}</em>} JSON property
     *
     * @return The <em>{@code artwork}</em> property from the received JSON
     */
    List<Artwork> getArtwork();

    /**
     * Get the value of the {<em>{@code data.episodes}</em>} JSON property
     *
     * @return The <em>{@code episodes}</em> property from the received JSON
     */
    List<Episode> getEpisodes();

    /**
     * Get the value of the {<em>{@code data.trailers}</em>} JSON property
     *
     * @return The <em>{@code trailers}</em> property from the received JSON
     */
    List<Trailer> getTrailers();

    /**
     * Get the value of the {<em>{@code data.tagOptions}</em>} JSON property
     *
     * @return The <em>{@code tagOptions}</em> property from the received JSON
     */
    List<TagOption> getTagOptions();

    /**
     * Get the value of the {<em>{@code data.translations}</em>} JSON property
     *
     * @return The <em>{@code translations}</em> property from the received JSON
     */
    Optional<MetaTranslations> getTranslations();
}

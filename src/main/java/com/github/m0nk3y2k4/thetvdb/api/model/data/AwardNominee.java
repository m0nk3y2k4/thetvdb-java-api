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

import java.util.Optional;

/**
 * Interface representing an
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/AwardNomineeBaseRecord">AwardNomineeBaseRecord</a>
 * data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all award nominee related data which was returned by the remote
 * service in JSON format. Properties that are declared to be nullable in the remote service documentation will be
 * returned as Java Optionals.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface AwardNominee {

    /**
     * Get the value of the {<em>{@code <enclosing>.character}</em>} JSON property
     *
     * @return The <em>{@code character}</em> property from the received JSON
     */
    Character getCharacter();

    /**
     * Get the value of the {<em>{@code <enclosing>.details}</em>} JSON property
     *
     * @return The <em>{@code details}</em> property from the received JSON
     */
    Optional<String> getDetails();

    /**
     * Get the value of the {<em>{@code <enclosing>.episode}</em>} JSON property
     *
     * @return The <em>{@code episode}</em> property from the received JSON
     */
    Episode getEpisode();

    /**
     * Get the value of the {<em>{@code <enclosing>.id}</em>} JSON property
     *
     * @return The <em>{@code id}</em> property from the received JSON
     */
    Long getId();

    /**
     * Get the value of the {<em>{@code <enclosing>.isWinner}</em>} JSON property
     *
     * @return The <em>{@code isWinner}</em> property from the received JSON
     */
    Boolean isWinner();

    /**
     * Get the value of the {<em>{@code <enclosing>.movie}</em>} JSON property
     *
     * @return The <em>{@code movie}</em> property from the received JSON
     */
    Movie getMovie();

    /**
     * Get the value of the {<em>{@code <enclosing>.series}</em>} JSON property
     *
     * @return The <em>{@code series}</em> property from the received JSON
     */
    Series getSeries();

    /**
     * Get the value of the {<em>{@code <enclosing>.year}</em>} JSON property
     *
     * @return The <em>{@code year}</em> property from the received JSON
     */
    Optional<String> getYear();
}

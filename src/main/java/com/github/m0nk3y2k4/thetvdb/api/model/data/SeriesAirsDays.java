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

import javax.annotation.Nullable;

/**
 * Interface representing a <em>{@code SeriesAirsDays}</em> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all series airs days related data which was returned by the remote
 * service in JSON format.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface SeriesAirsDays {

    /**
     * Get the value of the {<em>{@code <enclosing>.monday}</em>} JSON property
     *
     * @return The <em>{@code monday}</em> property from the received JSON
     */
    @Nullable
    Boolean onMonday();

    /**
     * Get the value of the {<em>{@code <enclosing>.tuesday}</em>} JSON property
     *
     * @return The <em>{@code tuesday}</em> property from the received JSON
     */
    @Nullable
    Boolean onTuesday();

    /**
     * Get the value of the {<em>{@code <enclosing>.wednesday}</em>} JSON property
     *
     * @return The <em>{@code wednesday}</em> property from the received JSON
     */
    @Nullable
    Boolean onWednesday();

    /**
     * Get the value of the {<em>{@code <enclosing>.thursday}</em>} JSON property
     *
     * @return The <em>{@code thursday}</em> property from the received JSON
     */
    @Nullable
    Boolean onThursday();

    /**
     * Get the value of the {<em>{@code <enclosing>.friday}</em>} JSON property
     *
     * @return The <em>{@code friday}</em> property from the received JSON
     */
    @Nullable
    Boolean onFriday();

    /**
     * Get the value of the {<em>{@code <enclosing>.saturday}</em>} JSON property
     *
     * @return The <em>{@code saturday}</em> property from the received JSON
     */
    @Nullable
    Boolean onSaturday();

    /**
     * Get the value of the {<em>{@code <enclosing>.sunday}</em>} JSON property
     *
     * @return The <em>{@code sunday}</em> property from the received JSON
     */
    @Nullable
    Boolean onSunday();
}

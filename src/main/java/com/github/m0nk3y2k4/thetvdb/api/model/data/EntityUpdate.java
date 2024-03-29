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
 * Interface representing a <em>{@code EntityUpdate}</em> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all entity update related data which was returned by the remote
 * service in JSON format.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface EntityUpdate {

    /**
     * Get the value of the {<em>{@code data.entityType}</em>} JSON property
     *
     * @return The <em>{@code entityType}</em> property from the received JSON
     */
    @Nullable
    String getEntityType();

    /**
     * Get the value of the {<em>{@code data.method}</em>} JSON property
     *
     * @return The <em>{@code method}</em> property from the received JSON
     */
    @Nullable
    String getMethod();

    /**
     * Get the value of the {<em>{@code data.recordId}</em>} JSON property
     *
     * @return The <em>{@code recordId}</em> property from the received JSON
     */
    @Nullable
    Long getRecordId();

    /**
     * Get the value of the {<em>{@code data.timeStamp}</em>} JSON property
     *
     * @return The <em>{@code timeStamp}</em> property from the received JSON
     */
    @Nullable
    Long getTimeStamp();

    /**
     * Get the value of the {<em>{@code data.seriesId}</em>} JSON property
     *
     * @return The <em>{@code seriesId}</em> property from the received JSON
     */
    @Nullable
    Long getSeriesId();

    /**
     * Get the value of the {<em>{@code data.mergeToId}</em>} JSON property
     *
     * @return The <em>{@code mergeToId}</em> property from the received JSON
     */
    @Nullable
    Long getMergeToId();

    /**
     * Get the value of the {<em>{@code data.mergeToEntityType}</em>} JSON property
     *
     * @return The <em>{@code mergeToEntityType}</em> property from the received JSON
     */
    @Nullable
    String getMergeToEntityType();
}

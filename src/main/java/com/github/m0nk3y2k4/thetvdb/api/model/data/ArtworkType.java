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
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/ArtworkType">ArtworkType</a>
 * data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all artwork type related data which was returned by the remote
 * service in JSON format.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface ArtworkType {

    /**
     * Get the value of the {<em>{@code data.id}</em>} JSON property
     *
     * @return The <em>{@code id}</em> property from the received JSON
     */
    @Nullable
    Long getId();

    /**
     * Get the value of the {<em>{@code data.name}</em>} JSON property
     *
     * @return The <em>{@code name}</em> property from the received JSON
     */
    @Nullable
    String getName();

    /**
     * Get the value of the {<em>{@code data.recordType}</em>} JSON property
     *
     * @return The <em>{@code recordType}</em> property from the received JSON
     */
    @Nullable
    String getRecordType();

    /**
     * Get the value of the {<em>{@code data.slug}</em>} JSON property
     *
     * @return The <em>{@code slug}</em> property from the received JSON
     */
    @Nullable
    String getSlug();

    /**
     * Get the value of the {<em>{@code data.imageFormat}</em>} JSON property
     *
     * @return The <em>{@code imageFormat}</em> property from the received JSON
     */
    @Nullable
    String getImageFormat();

    /**
     * Get the value of the {<em>{@code data.width}</em>} JSON property
     *
     * @return The <em>{@code width}</em> property from the received JSON
     */
    @Nullable
    Long getWidth();

    /**
     * Get the value of the {<em>{@code data.height}</em>} JSON property
     *
     * @return The <em>{@code height}</em> property from the received JSON
     */
    @Nullable
    Long getHeight();

    /**
     * Get the value of the {<em>{@code data.thumbWidth}</em>} JSON property
     *
     * @return The <em>{@code thumbWidth}</em> property from the received JSON
     */
    @Nullable
    Long getThumbWidth();

    /**
     * Get the value of the {<em>{@code data.thumbHeight}</em>} JSON property
     *
     * @return The <em>{@code thumbHeight}</em> property from the received JSON
     */
    @Nullable
    Long getThumbHeight();
}

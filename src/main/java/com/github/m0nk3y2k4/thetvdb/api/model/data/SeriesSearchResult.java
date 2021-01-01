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
 * Interface representing a <i>Series search result</i> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all series search result related data which was returned by the
 * remote service in JSON format. Please note that, as the remote service declares all of the properties to be optional,
 * most of the methods are marked as {@link Nullable}. Methods returning collection-based values however will return an
 * empty collection in case no corresponding data was received.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface SeriesSearchResult {

    /**
     * Get all values of the {<em>{@code data.aliases}</em>} JSON property in a list
     *
     * @return The <em>{@code aliases}</em> property from the received JSON as list
     */
    List<String> getAliases();

    /**
     * Get the value of the {<em>{@code data.banner}</em>} JSON property
     *
     * @return The <em>{@code banner}</em> property from the received JSON
     */
    @Nullable
    String getBanner();

    /**
     * Get the value of the {<em>{@code data.firstAired}</em>} JSON property
     *
     * @return The <em>{@code firstAired}</em> property from the received JSON
     */
    @Nullable
    String getFirstAired();

    /**
     * Get the value of the {<em>{@code data.id}</em>} JSON property
     *
     * @return The <em>{@code id}</em> property from the received JSON
     */
    @Nullable
    Long getId();

    /**
     * Get the value of the {<em>{@code data.image}</em>} JSON property. Only available for route
     * <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series"><b>[GET]</b> /search/series</a>.
     *
     * @return The <em>{@code image}</em> property from the received JSON
     */
    @Nullable
    String getImage();

    /**
     * Get the value of the {<em>{@code data.network}</em>} JSON property
     *
     * @return The <em>{@code network}</em> property from the received JSON
     */
    @Nullable
    String getNetwork();

    /**
     * Get the value of the {<em>{@code data.overview}</em>} JSON property
     *
     * @return The <em>{@code overview}</em> property from the received JSON
     */
    @Nullable
    String getOverview();

    /**
     * Get the value of the {<em>{@code data.poster}</em>} JSON property. Only available for route
     * <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series"><b>[GET]</b> /search/series</a>.
     *
     * @return The <em>{@code poster}</em> property from the received JSON
     */
    @Nullable
    String getPoster();

    /**
     * Get the value of the {<em>{@code data.seriesName}</em>} JSON property
     *
     * @return The <em>{@code seriesName}</em> property from the received JSON
     */
    @Nullable
    String getSeriesName();

    /**
     * Get the value of the {<em>{@code data.slug}</em>} JSON property
     *
     * @return The <em>{@code slug}</em> property from the received JSON
     */
    @Nullable
    String getSlug();

    /**
     * Get the value of the {<em>{@code data.status}</em>} JSON property
     *
     * @return The <em>{@code status}</em> property from the received JSON
     */
    @Nullable
    String getStatus();
}

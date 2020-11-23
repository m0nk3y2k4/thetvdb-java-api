/*
 * Copyright (C) 2019 - 2020 thetvdb-java-api Authors and Contributors
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

package com.github.m0nk3y2k4.thetvdb.api.model;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

/**
 * Interface for extended remote API responses. Some API routes will not only return the actually requested data but
 * will also report additional information with regards to error and pagination handling.
 * <p><br>
 * <b>Note:</b><br>
 * The type and amount of additional information returned by the remote service depends on the actual API route. Some
 * will only return the requested records, other will also report additional error information in case invalid filter
 * parameters have been used by the client. API calls that support pagination might also add some additional information
 * which can be used to navigate through the "pages".
 * <p><br>
 *
 * @param <T> The actual payload DTO based on the data returned by the remote service
 */
public interface APIResponse<T> {

    /**
     * Returns the actually requested records mapped as Java DTO. Resolves to the value of the {<em>{@code data}</em>}
     * JSON property.
     *
     * @return Data returned by the API call
     */
    @Nullable
    T getData();

    /**
     * Returns an Optional representing the additional error information returned by the remote service. If the request
     * was fully compliant or if the requested resource does not support additional error reporting the returned
     * Optional might be empty. Resolves to the value of the {<em>{@code errors}</em>} JSON property.
     *
     * @return Optional containing additional error details or empty Optional if no errors occurred or error reporting
     *         is not supported by the requested resource
     */
    Optional<Errors> getErrors();

    /**
     * Returns an Optional representing the additional paging information returned by the remote service. If the
     * requested resource does not support pagination the returned Optional might be empty. Resolves to the value of the
     * {<em>{@code links}</em>} JSON property.
     *
     * @return Optional containing additional paging information or empty Optional if pagination is not supported by the
     *         requested resource
     */
    Optional<Links> getLinks();

    /**
     * Interface representing optional soft errors that might occur while requesting data from the remote service
     * <p><br>
     * Please note that, as the remote service declares all of the properties to be optional, most of the methods are
     * marked as {@link Nullable}. Methods returning collection-based values however will return an empty collection in
     * case no corresponding data was received.
     */
    interface Errors {

        /**
         * Returns invalid filters passed to the route. Resolves to the values of the {<em>{@code
         * errors.invalidFilters}</em>} JSON property.
         *
         * @return The <em>{@code invalidFilters}</em> property from the received JSON as list
         */
        List<String> getInvalidFilters();

        /**
         * Returns invalid language or translation missing. Resolves to the value of the {<em>{@code
         * errors.invalidLanguage}</em>} JSON property.
         *
         * @return The <em>{@code invalidLanguage}</em> property from the received JSON
         */
        @Nullable
        String getInvalidLanguage();

        /**
         * Returns invalid query params passed to the route. Resolves to the values of the {<em>{@code
         * errors.invalidQueryParams}</em>} JSON property.
         *
         * @return The <em>{@code invalidQueryParams}</em> property from the received JSON as list
         */
        List<String> getInvalidQueryParams();
    }

    /**
     * Interface representing optional paging information for remote service requests supporting pagination
     * <p><br>
     * Please note that, as the remote service declares all of the properties to be optional, most of the methods are
     * marked as {@link Nullable}. Methods returning collection-based values however will return an empty collection in
     * case no corresponding data was received.
     */
    interface Links {

        /**
         * Get the number of the first available page. Resolves to the value of the {<em>{@code links.first}</em>} JSON
         * property.
         *
         * @return The number of the first available page
         */
        @Nullable
        Integer getFirst();

        /**
         * Get the number of the last available page. Resolves to the value of the {<em>{@code links.last}</em>} JSON
         * property.
         *
         * @return The number of the last available page
         */
        @Nullable
        Integer getLast();

        /**
         * Get the number of the next page (if available). Resolves to the value of the {<em>{@code links.next}</em>}
         * JSON property.
         *
         * @return The number of the next page
         */
        @Nullable
        Integer getNext();

        /**
         * Get the number of the previous page (if available). Resolves to the value of the {<em>{@code
         * links.previous}</em>} JSON property.
         *
         * @return The number of the previous page
         */
        @Nullable
        Integer getPrevious();
    }
}

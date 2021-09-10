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

package com.github.m0nk3y2k4.thetvdb.api.model;

import java.util.Optional;

/**
 * Interface for extended remote API responses. All API routes will return additional status information together with
 * the actually requested data. Some routes that return huge amounts of data may also provide additional links which can
 * be used for pagination.
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
    T getData();

    /**
     * Returns additional processing status information. Resolves to the value of the {<em>{@code status}</em>} JSON
     * property.
     *
     * @return Related status information for this response
     */
    String getStatus();

    /**
     * Returns optional URLs for pagination, for example a link to the previous/next page. This kind of information is
     * not provided by all endpoints but only for specific ones. The corresponding Getters will return an empty Optional
     * if no link information is available (the actual {@link Links Links} object will <b>always</b> be present).
     * Resolves to the value of the {<em>{@code links}</em>} JSON property.
     *
     * @return Optional links for pagination if present
     */
    Links getLinks();

    /**
     * Interface representing optional paging information for most remote service requests that support pagination
     * <p><br>
     * The single methods of this interface represent the different links which might or might not be provided by some
     * endpoint. If no information is available for a particular link an empty Optional will be returned.
     */
    interface Links {

        /**
         * Link to the previous page. Resolves to the value of the {<em>{@code links.prev}</em>} JSON property.
         *
         * @return Link to the previous page or an empty Optional if there is no previous page or link information are
         *         not available for this endpoint
         */
        Optional<String> getPrevious();

        /**
         * Link to the current page. Resolves to the value of the {<em>{@code links.self}</em>} JSON property.
         *
         * @return Link to the page that has actually been requested or an empty Optional if link information are not
         *         available for this endpoint
         */
        Optional<String> getSelf();

        /**
         * Link to the next page. Resolves to the value of the {<em>{@code links.next}</em>} JSON property.
         *
         * @return Link to the next page or an empty Optional if there are no further pages or link information are not
         *         available for this endpoint
         */
        Optional<String> getNext();
    }
}

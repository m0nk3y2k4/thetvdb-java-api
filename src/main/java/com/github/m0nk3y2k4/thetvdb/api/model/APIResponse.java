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

/**
 * Interface for extended remote API responses. All API routes will return additional status information together with
 * the actually requested data.
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
}

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

package com.github.m0nk3y2k4.thetvdb.api.constants;

/**
 * Collection of query parameter constants used throughout the various <i>TheTVDB.com</i> routes.
 * <ul>
 *     <li>{@link Series}</li>
 * </ul>
 * The parameters of this class may be used to create own {@link com.github.m0nk3y2k4.thetvdb.api.QueryParameters QueryParameters} objects.
 */
public final class Query {

    /** Constant class. Should not be instantiated */
    private Query() {}

    /**
     * Collection of query parameters for API route <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/series">/series</a>
     */
    public static final class Series {

        /** Query parameter "<i>page</i>" - Page of results to fetch. Defaults to page 1 if not provided */
        public static final String PAGE = "page";

        private Series() {}     // Private constructor. Only constants in this class
    }
}

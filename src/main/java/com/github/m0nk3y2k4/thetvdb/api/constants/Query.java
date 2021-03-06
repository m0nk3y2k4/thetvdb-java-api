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

package com.github.m0nk3y2k4.thetvdb.api.constants;

/**
 * Collection of query parameter constants used throughout the various <i>TheTVDB.com</i> routes.
 * <ul>
 *     <li>{@link Companies}</li>
 *     <li>{@link Movies}</li>
 *     <li>{@link Lists}</li>
 *     <li>{@link Series}</li>
 *     <li>{@link Updates}</li>
 * </ul>
 * The parameters of this class may be used to create own {@link com.github.m0nk3y2k4.thetvdb.api.QueryParameters QueryParameters} objects.
 */
public final class Query {

    /** Constant class. Should not be instantiated */
    private Query() {}

    /**
     * Collection of query parameters for API endpoint <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/companies">/companies</a>
     */
    public static final class Companies {

        /** Query parameter "<i>page</i>" - Page of results to fetch. Defaults to page 0 if not provided. */
        public static final String PAGE = "page";

        private Companies() {}     // Private constructor. Only constants in this class
    }

    /**
     * Collection of query parameters for API endpoint <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/movies">/movies</a>
     */
    public static final class Movies {

        /** Query parameter "<i>page</i>" - Page of results to fetch. Defaults to page 0 if not provided. */
        public static final String PAGE = "page";

        private Movies() {}     // Private constructor. Only constants in this class
    }

    /**
     * Collection of query parameters for API endpoint <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/lists">/lists</a>
     */
    public static final class Lists {

        /** Query parameter "<i>page</i>" - Page of results to fetch. Defaults to page 0 if not provided. */
        public static final String PAGE = "page";

        private Lists() {}     // Private constructor. Only constants in this class
    }

    /**
     * Collection of query parameters for API endpoint <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series">/series</a>
     */
    public static final class Series {

        /** Query parameter "<i>page</i>" - Page of results to fetch. Defaults to page 0 if not provided. */
        public static final String PAGE = "page";

        private Series() {}     // Private constructor. Only constants in this class
    }

    /**
     * Collection of query parameters for API endpoint <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/updates">/updates</a>
     */
    public static final class Updates {

        /** Query parameter "<i>since</i>" - UNIX Epoch time in seconds. Remote service uses GMT as reference. */
        public static final String SINCE = "since";

        private Updates() {}     // Private constructor. Only constants in this class
    }
}

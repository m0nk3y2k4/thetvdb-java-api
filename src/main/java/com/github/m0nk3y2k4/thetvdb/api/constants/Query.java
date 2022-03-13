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

package com.github.m0nk3y2k4.thetvdb.api.constants;

/**
 * Collection of query parameter constants used throughout the various <i>TheTVDB.com</i> routes.
 * <ul>
 *     <li>{@link Companies}</li>
 *     <li>{@link Episodes}</li>
 *     <li>{@link Movies}</li>
 *     <li>{@link Lists}</li>
 *     <li>{@link People}</li>
 *     <li>{@link Search}</li>
 *     <li>{@link Seasons}</li>
 *     <li>{@link Series}</li>
 *     <li>{@link Updates}</li>
 *     <li>{@link Filter}</li>
 * </ul>
 * The parameters of this class may be used to create own {@link com.github.m0nk3y2k4.thetvdb.api.QueryParameters QueryParameters} objects.
 */
public final class Query {

    /** Constant class. Should not be instantiated */
    private Query() {}

    /**
     * Collection of query parameters for API endpoint <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Companies">Companies</a>
     */
    public static final class Companies {

        /** Query parameter "<i>page</i>" - Page of results to fetch. Defaults to page 0 if not provided. */
        public static final String PAGE = "page";

        private Companies() {}     // Private constructor. Only constants in this class
    }

    /**
     * Collection of query parameters for API endpoint <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Episodes">Episodes</a>
     */
    public static final class Episodes {

        /** Query parameter "<i>meta</i>" - Used to request additional information. */
        public static final String META = "meta";

        private Episodes() {}     // Private constructor. Only constants in this class
    }

    /**
     * Collection of query parameters for API endpoint <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Movies">Movies</a>
     */
    public static final class Movies {

        /** Query parameter "<i>meta</i>" - Used to request additional information. */
        public static final String META = "meta";

        /** Query parameter "<i>page</i>" - Page of results to fetch. Defaults to page 0 if not provided. */
        public static final String PAGE = "page";

        private Movies() {}     // Private constructor. Only constants in this class
    }

    /**
     * Collection of query parameters for API endpoint <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Lists">Lists</a>
     */
    public static final class Lists {

        /** Query parameter "<i>page</i>" - Page of results to fetch. Defaults to page 0 if not provided. */
        public static final String PAGE = "page";

        private Lists() {}     // Private constructor. Only constants in this class
    }

    /**
     * Collection of query parameters for API endpoint <a target="_blank" href="https://thetvdb.github.io/v4-api/#/People">People</a>
     */
    public static final class People {

        /** Query parameter "<i>meta</i>" - Used to request additional information. */
        public static final String META = "meta";

        private People() {}     // Private constructor. Only constants in this class
    }

    /**
     * Collection of query parameters for API endpoint <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Search">Search</a>
     */
    public static final class Search {

        /** Query parameter "<i>company</i>" - Restrict results to a specific company (original network, studio, etc.) */
        public static final String COMPANY = "company";
        /** Query parameter "<i>country</i>" - Restrict results to a specific country of origin (3-char country code) */
        public static final String COUNTRY = "country";
        /** Query parameter "<i>director</i>" - Restrict movie results to a specific director (full name) */
        public static final String DIRECTOR = "director";
        /** Query parameter "<i>language</i>" - Restrict results to a specific primary language (3-char language code) */
        public static final String LANGUAGE = "language";
        /** Query parameter "<i>limit</i>" - Limit search results to a specific amount */
        public static final String LIMIT = "limit";
        /** Query parameter "<i>network</i>" - Restrict results to a specific network. Used for TV and TV movies */
        public static final String NETWORK = "network";
        /** Query parameter "<i>offset</i>" - Offset results (max. 500 results returned per request) */
        public static final String OFFSET = "offset";
        /** Query parameter "<i>primaryType</i>" - Restrict results to a specific type of company (full name) */
        public static final String PRIMARY_TYPE = "primaryType";
        /** Query parameter "<i>q</i>" - Synonym for the {@value QUERY} parameter */
        public static final String Q = "q";
        /** Query parameter "<i>query</i>" - The actual search term to query for */
        public static final String QUERY = "query";
        /** Query parameter "<i>remote_id</i>" - Search for a specific remote id, e.g. IMDB or EIDR id */
        public static final String REMOTE_ID = "remote_id";
        /** Query parameter "<i>type</i>" - Restrict results to a specific entity type */
        public static final String TYPE = "type";
        /** Query parameter "<i>year</i>" - Restrict results to a year for movie|series */
        public static final String YEAR = "year";

        private Search() {}     // Private constructor. Only constants in this class
    }

    /**
     * Collection of query parameters for API endpoint <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Seasons">Seasons</a>
     */
    public static final class Seasons {

        /** Query parameter "<i>meta</i>" - Used to request additional information. */
        public static final String META = "meta";
        /** Query parameter "<i>page</i>" - Page of results to fetch. Defaults to page 0 if not provided. */
        public static final String PAGE = "page";

        private Seasons() {}     // Private constructor. Only constants in this class
    }

    /**
     * Collection of query parameters for API endpoint <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Series">Series</a>
     */
    public static final class Series {

        /** Query parameter "<i>meta</i>" - Used to request additional information. */
        public static final String META = "meta";
        /** Query parameter "<i>page</i>" - Page of results to fetch. Defaults to page 0 if not provided. */
        public static final String PAGE = "page";
        /** Query parameter "<i>season</i>" - Restrict results to a specific season. Defaults to 0 if not provided. */
        public static final String SEASON = "season";
        /** Query parameter "<i>episodeNumber</i>" - Restrict results to a specific episode. Defaults to 0 if not provided. */
        public static final String EPISODE_NUMBER = "episodeNumber";
        /** Query parameter "<i>airDate</i>" - Air date of the episode, format is yyyy-MM-dd. */
        public static final String AIR_DATE = "airDate";
        /** Query parameter "<i>lang</i>" - Restrict artworks to a specific language, e.g. eng, spa */
        public static final String LANGUAGE = "lang";
        /** Query parameter "<i>type</i>" - Restrict artworks to a specific type */
        public static final String TYPE = "type";

        private Series() {}     // Private constructor. Only constants in this class
    }

    /**
     * Collection of query parameters for API endpoint <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Updates">Updates</a>
     */
    public static final class Updates {

        /** Query parameter "<i>action</i>" - Restrict results to a specific update action like creation or deletion */
        public static final String ACTION = "action";
        /** Query parameter "<i>page</i>" - Page of results to fetch. Defaults to page 0 if not provided. */
        public static final String PAGE = "page";
        /** Query parameter "<i>since</i>" - UNIX Epoch time in seconds. Remote service uses GMT as reference. */
        public static final String SINCE = "since";
        /** Query parameter "<i>type</i>" - Restrict results to a specific entity type */
        public static final String TYPE = "type";

        private Updates() {}     // Private constructor. Only constants in this class
    }

    /**
     * Collection of query parameters for API filter endpoints
     */
    public static final class Filter {

        /** Query parameter "<i>company</i>" - Filter for production company */
        public static final String COMPANY = "company";
        /** Query parameter "<i>contentRating</i>" - Filter for content rating id based on a country */
        public static final String CONTENT_RATING = "contentRating";
        /** Query parameter "<i>country</i>" - Filter for country of origin */
        public static final String COUNTRY = "country";
        /** Query parameter "<i>genre</i>" - Filter for genre */
        public static final String GENRE = "genre";
        /** Query parameter "<i>lang</i>" - Filter for original language */
        public static final String LANGUAGE = "lang";
        /** Query parameter "<i>sort</i>" - Sort filter results */
        public static final String SORT = "sort";
        /** Query parameter "<i>status</i>" - Filter for status */
        public static final String STATUS = "status";
        /** Query parameter "<i>year</i>" - Filter for release year */
        public static final String YEAR = "year";

        private Filter() {}     // Private constructor. Only constants in this class
    }
}

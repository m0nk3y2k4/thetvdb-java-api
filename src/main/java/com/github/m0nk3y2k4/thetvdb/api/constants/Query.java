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
 *     <li>{@link Search}</li>
 *     <li>{@link Series}</li>
 *     <li>{@link Updates}</li>
 *     <li>{@link Users}</li>
 * </ul>
 * The parameters of this class may be used to create own {@link com.github.m0nk3y2k4.thetvdb.api.QueryParameters QueryParameters} objects.
 */
public final class Query {

    /** Constant class. Should not be instantiated */
    private Query() {}

    /**
     * Collection of query parameters for API route <a href="https://api.thetvdb.com/swagger#/Movies">/movies</a>
     */
    public static final class Movie {

        /** Query parameter "<i>since</i>" - Epoch time to start your date range */
        public static final String SINCE = "since";

        private Movie() {}     // Private constructor. Only constants in this class
    }

    /**
     * Collection of query parameters for API route <a href="https://api.thetvdb.com/swagger#!/Search">/search</a>
     */
    public static final class Search {

        /** Query parameter "<i>name</i>" - Name of the series to search for */
        public static final String NAME = "name";

        /** Query parameter "<i>imdbId</i>" - IMDB id of the series */
        public static final String IMDBID = "imdbId";

        /** Query parameter "<i>zap2itId</i>" - Zap2it ID of the series to search for */
        public static final String ZAP2ITID = "zap2itId";

        /** Query parameter "<i>slug</i>" - Slug from site URL of series (<a href="https://www.thetvdb.com/series/$SLUG">https://www.thetvdb.com/series/$SLUG</a>) */
        public static final String SLUG = "slug";

        private Search() {}     // Private constructor. Only constants in this class
    }

    /**
     * Collection of query parameters for API route <a href="https://api.thetvdb.com/swagger#!/Series">/series</a>
     */
    public static final class Series {

        /** Query parameter "<i>page</i>" - Page of results to fetch. Defaults to page 1 if not provided */
        public static final String PAGE = "page";

        /** Query parameter "<i>absoluteNumber</i>" - Absolute number of the episode */
        public static final String ABSOLUTENUMBER = "absoluteNumber";

        /** Query parameter "<i>airedSeason</i>" - Aired season number */
        public static final String AIREDSEASON = "airedSeason";

        /** Query parameter "<i>airedEpisode</i>" - Aired episode number */
        public static final String AIREDEPISODE = "airedEpisode";

        /** Query parameter "<i>dvdSeason</i>" - DVD season number */
        public static final String DVDSEASON = "dvdSeason";

        /** Query parameter "<i>dvdEpisode</i>" - DVD episode number */
        public static final String DVDEPISODE = "dvdEpisode";

        /** Query parameter "<i>imdbId</i>" - IMDB id of the series */
        public static final String IMDBID = "imdbId";

        /** Query parameter "<i>keys</i>" - Comma-separated list of keys to filter by */
        public static final String KEYS = "keys";

        /** Query parameter "<i>keyType</i>" - Type of image you're querying for (fanart, poster, etc.) */
        public static final String KEYTYPE = "keyType";

        /** Query parameter "<i>resolution</i>" - Resolution to filter by (1280x1024, for example) */
        public static final String RESOLUTION = "resolution";

        /** Query parameter "<i>subKey</i>" - Subkey for the above query keys */
        public static final String SUBKEY = "subKey";

        private Series() {}     // Private constructor. Only constants in this class
    }

    /**
     * Collection of query parameters for API route <a href="https://api.thetvdb.com/swagger#!/Updates">/updates</a>
     */
    public static final class Updates {

        /** Query parameter "<i>fromTime</i>" - Epoch time to start your date range */
        public static final String FROMTIME = "fromTime";

        /** Query parameter "<i>toTime</i>" - Epoch time to end your date range. Must be one week from {@link #FROMTIME} */
        public static final String TOTIME = "toTime";

        private Updates() {}        // Private constructor. Only constants in this class
    }

    /**
     * Collection of query parameters for API route <a href="https://api.thetvdb.com/swagger#!/Users">/users</a>
     */
    public static final class Users {

        /** Query parameter "<i>itemType</i>" - Item to query. Can be either 'series', 'episode', or 'banner' */
        public static final String ITEMTYPE = "itemType";

        private Users() {}      // Private constructor. Only constants in this class
    }
}

package com.github.m0nk3y2k4.thetvdb.api.constants;

/**
 * Collction of query parameter constants used throughout the various TheTVDB routes
 */
public final class Query {

    /** Constant classes should not be instantiated */
    private Query() {}

    /**
     * Collection of query parameters for API route <a href="https://api.thetvdb.com/swagger#!/Search">/search</a>
     */
    public static final class Search {

        private Search() {}     // Private constructor. Only constants in this class

        /** Query parameter "<i>name</i>" - Name of the series to search for */
        public static final String NAME = "name";

        /** Query parameter "<i>imdbId</i>" - IMDB id of the series */
        public static final String IMDBID = "imdbId";

        /** Query parameter "<i>zap2itId</i>" - Zap2it ID of the series to search for */
        public static final String ZAP2ITID = "zap2itId";
    }

    /**
     * Collection of query parameters for API route <a href="https://api.thetvdb.com/swagger#!/Series">/series</a>
     */
    public static final class Series {

        private Series() {}     // Private constructor. Only constants in this class

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

        /** Query parameter "<i>keyType</i>" - Type of image you're querying for (fanart, poster, etc. See <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query_params">/images/query/params</a> for more details) */
        public static final String KEYTYPE = "keyType";

        /** Query parameter "<i>resolution</i>" - Resolution to filter by (1280x1024, for example) */
        public static final String RESOLUTION = "resolution";

        /** Query parameter "<i>subKey</i>" - Subkey for the above query keys. See <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query_params">/images/query/params</a> for more information */
        public static final String SUBKEY = "subKey";
    }

    /**
     * Collection of query parameters for API route <a href="https://api.thetvdb.com/swagger#!/Updates">/updates</a>
     */
    public static final class Updates {

        private Updates() {}        // Private constructor. Only constants in this class

        /** Query parameter "<i>fromTime</i>" - Epoch time to start your date range */
        public static final String FROMTIME = "fromTime";

        /** Query parameter "<i>toTime</i>" - Epoch time to end your date range. Must be one week from {@link #FROMTIME} */
        public static final String TOTIME = "toTime";
    }

    /**
     * Collection of query parameters for API route <a href="https://api.thetvdb.com/swagger#!/Users">/users</a>
     */
    public static final class Users {

        private Users() {}      // Private constructor. Only constants in this class

        /** Query parameter "<i>itemType</i>" - Item to query. Can be either 'series', 'episode', or 'banner' */
        public static final String ITEMTYPE = "itemType";
    }
}
package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.resource.QueryResource;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Implementation of a connector for the remote API's <a href="https://api.thetvdb.com/swagger#/Series">Series</a> endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used to gather information about a specific series.
 */
public final class SeriesAPI extends QueryResource {

    /** Base URL path parameter for this endpoint */
    private static final String BASE = "/series";

    private SeriesAPI() {}     // Private constructor. Only static methods

    /**
     * Returns detailed information for a specific series as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id"><b>[GET]</b> /series/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id The <i>TheTVDB.com</i> series ID
     *
     * @return JSON object containing detailed information for a specific series
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given series ID does
     *                      not exist, etc.
     */
    public static JsonNode get(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(BASE, id));
    }

    /**
     * Returns header information for a specific series as raw JSON. Good for getting the Last-Updated header to find out
     * when the series was last modified.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/head_series_id"><b>[HEAD]</b> /series/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id The <i>TheTVDB.com</i> series ID
     *
     * @return Artificial JSON object based on the HTML header information returned by the remote service
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given series ID does
     *                      not exist, etc.
     */
    public static JsonNode getHead(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendHEAD(createResource(BASE, id));
    }

    /**
     * Returns the actors for a specific series as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_actors"><b>[GET]</b> /series/{id}/actors</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id The <i>TheTVDB.com</i> series ID
     *
     * @return JSON object containing the actors for a specific series
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given series ID does
     *                      not exist, etc.
     */
    public static JsonNode getActors(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(id, "/actors"));
    }

    /**
     * Returns all episodes of a specific series as raw JSON. Results will be paginated with 100 results per page.
     * Use <em>{@code params}</em> to select a specific result page.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes"><b>[GET]</b> /series/{id}/episodes</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id The <i>TheTVDB.com</i> series ID
     * @param params Object containing key/value pairs of query parameters. For a complete list of possible parameters
     *               see the API documentation.
     *
     * @return JSON object containing a single result page of episodes
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given series ID does
     *                      not exist, etc.
     */
    public static JsonNode getEpisodes(@Nonnull APIConnection con, long id, @CheckForNull QueryParameters params) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createQueryResource(id, "/episodes", params));
    }

    /**
     * Returns all matching episodes of a specific series as raw JSON. Results will be paginated. Use <em>{@code params}</em> to filter
     * for specific episodes or to select a specific result page.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query"><b>[GET]</b> /series/{id}/episodes/query</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id The <i>TheTVDB.com</i> series ID
     * @param params Object containing key/value pairs of query parameters. For a complete list of possible query parameters
     *               see the API documentation or use {@link #getEpisodesQueryParams(APIConnection, long) getEpisodesQueryParams(con, id)}.
     *
     * @return JSON object containing a single result page of queried episode records
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given series ID does
     *                      not exist, etc. or if no records are found that match your query.
     */
    public static JsonNode queryEpisodes(@Nonnull APIConnection con, long id, @CheckForNull QueryParameters params) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createQueryResource(id, "/episodes/query", params));
    }

    /**
     * Returns a list of keys which are valid parameters for querying episodes, as raw JSON. These keys are permitted to be used in
     * {@link QueryParameters} objects when querying for specific episodes of a series.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query_params"><b>[GET]</b> /series/{id}/episodes/query/params</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id The <i>TheTVDB.com</i> series ID
     *
     * @return JSON object containing all allowed keys to be used for querying episodes
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given series ID does
     *                      not exist, etc.
     */
    public static JsonNode getEpisodesQueryParams(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(id, "/episodes/query/params"));
    }

    /**
     * Returns a summary of the episodes and seasons available for a series, as raw JSON.
     * <br>
     * <b>Note:</b> Season "0" is for all episodes that are considered to be specials.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_summary"><b>[GET]</b> /series/{id}/episodes/summary</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id The <i>TheTVDB.com</i> series ID
     *
     * @return JSON object containing a summary of the episodes and seasons avaialable for the given series
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given series ID does
     *                      not exist, etc.
     */
    public static JsonNode getEpisodesSummary(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(id, "/episodes/summary"));
    }

    /**
     * Returns a filtered series record based on the given parameters, as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_filter"><b>[GET]</b> /series/{id}/filter</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id The <i>TheTVDB.com</i> series ID
     * @param params Object containing key/value pairs of query parameters. For a complete list of possible query parameters
     *               see the API documentation or use {@link #getFilterParams(APIConnection, long) getFilterParams(con, seriesId)}.
     *
     * @return JSON object containing a filtered series record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given series ID does
     *                      not exist, etc.
     */
    public static JsonNode filter(@Nonnull APIConnection con, long id, @CheckForNull QueryParameters params) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        Parameters.validateQueryParam(Query.Series.KEYS, params);
        return con.sendGET(createQueryResource(id, "/filter", params));
    }

    /**
     * Returns a list of keys which are valid parameters for filtering series, as raw JSON. These keys are permitted to be used in
     * {@link QueryParameters} objects when filtering for a specific series.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_filter_params"><b>[GET]</b> /series/{id}/filter/params</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id The <i>TheTVDB.com</i> series ID
     *
     * @return JSON object containing a list of all keys allowed to filter by
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given series ID does
     *                      not exist, etc.
     */
    public static JsonNode getFilterParams(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(id, "/filter/params"));
    }

    /**
     * Returns a summary of the images types and counts available for a particular series, as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images"><b>[GET]</b> /series/{id}/images</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id The <i>TheTVDB.com</i> series ID
     *
     * @return JSON object containing a summary of the image types and counts available for the given series
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given series ID does
     *                      not exist, etc.
     */
    public static JsonNode getImages(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(id, "/images"));
    }

    /**
     * Returns the matching result of querying images for a specific series, as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query"><b>[GET]</b> /series/{id}/images/query</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id The <i>TheTVDB.com</i> series ID
     * @param params Object containing key/value pairs of query parameters. For a complete list of possible query parameters
     *               see the API documentation or use {@link #getImagesQueryParams(APIConnection, long) getImagesQueryParams(con, seriesId)}.
     *
     * @return JSON object containing images that matched the query
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given series ID does
     *                      not exist, etc. or if no records are found that match your query.
     */
    public static JsonNode queryImages(@Nonnull APIConnection con, long id, @CheckForNull QueryParameters params) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createQueryResource(id, "/images/query", params));
    }

    /**
     * Returns a list of valid parameters for querying a series images, as raw JSON. Unlike other routes, querying for a series images may be resticted
     * to certain combinations of query keys. The allowed combinations are clustered in the data array of the returned JSON object.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query_params"><b>[GET]</b> /series/{id}/images/query/params</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id The <i>TheTVDB.com</i> series ID
     *
     * @return JSON object containing a list of possible parameters which may be used to query a series images
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given series ID does
     *                      not exist, etc.
     */
    public static JsonNode getImagesQueryParams(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(id, "/images/query/params"));
    }

    /**
     * Creates a new resource string consisting of the <i>Series</i> endpoints base URL path parameter, the given <em>{@code id}</em> and
     * the <em>{@code specific}</em> URL path paramter in the following format: <b><code>/BASE/id/specific</code></b>
     *
     * @param id Series ID to be added to the resource String
     * @param specific Specific URL path parameter representing the actual route to be invoked
     *
     * @return Composed resource String based on the given parameters
     */
    private static String createResource(long id, @Nonnull String specific) {
        return createResource(BASE, "/" + id + specific);
    }

    /**
     * Creates a new query resource string consisting of the <i>Series</i> endpoints base URL path parameter, the given <em>{@code id}</em> and
     * <em>{@code specific}</em> URL path paramter and additional query parametrs in the following format:
     * <b><code>/BASE/id/specific?query1=value1&amp;query2=value2&amp;...</code></b>
     *
     * @param id Series ID to be added to the resource String
     * @param specific Specific URL path parameter representing the actual route to be invoked
     * @param params Set of query parameters to be added to the very end of the resource String
     *
     * @return Composed query resource String based on the given parameters
     */
    private static String createQueryResource(long id, @Nonnull String specific, @CheckForNull QueryParameters params) {
        return createQueryResource(BASE, "/" + id + specific, params);
    }
}

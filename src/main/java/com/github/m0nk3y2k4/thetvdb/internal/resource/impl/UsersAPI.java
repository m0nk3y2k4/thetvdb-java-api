package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import java.util.Arrays;
import java.util.function.Predicate;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.resource.QueryResource;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Implementation of a connector for the remote API's <a href="https://api.thetvdb.com/swagger#/Users">Users</a> endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used to handle user data.
 */
public final class UsersAPI extends QueryResource {

    /** Base URL path parameter for this endpoint */
    private static final String BASE = "/user";

    /** Validator for the ratings dynamic <em>{@code itemType}</em> URL path parameter */
    private static final Predicate<String> ITEMTYPE_VALIDATOR = value -> Arrays.asList("series", "episode", "image").contains(value);

    /** Identifiers for the ratings dynamic URL path parameters */
    private static final String PATH_ITEMTYPE = "itemType";
    private static final String PATH_ITEMID = "itemId";
    private static final String PATH_ITEMRATING = "itemRating";

    private UsersAPI() {}     // Private constructor. Only static methods

    /**
     * Returns basic information about the currently authenticated user, as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user"><b>[GET]</b> /user</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing basic user information
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
     *                      if no information exists for the current user
     */
    public static JsonNode get(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(BASE);
    }

    /**
     * Returns an array of favorite series for a given user, as raw JSON. Will be a blank array if no favorites exist.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_favorites"><b>[GET]</b> /user/favorites</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing the user favorites
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
     *                      if no information exists for the current user
     */
    public static JsonNode getFavorites(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource(BASE, "/favorites"));
    }

    /**
     * Deletes the given series ID from the user’s favorite’s list and returns the updated list as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/delete_user_favorites_id"><b>[DELETE]</b> /user/favorites/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id The <i>TheTVDB.com</i> series ID
     *
     * @return JSON object containing the updated list of user favorites
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
     *                      if no information exists for the current user or the requested record could not be deleted
     */
    public static JsonNode deleteFromFavorites(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendDELETE(createResource(BASE, "/favorites", id));
    }

    /**
     * Adds the supplied series ID to the user’s favorite’s list and returns the updated list as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/put_user_favorites_id"><b>[PUT]</b> /user/favorites/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id The <i>TheTVDB.com</i> series ID
     *
     * @return JSON object containing the updated list of user favorites
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
     *                      if no information exists for the current user or the requested record could not be updated
     */
    public static JsonNode addToFavorites(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendPUT(createResource(BASE, "/favorites", id));
    }

    /**
     * Returns a list of ratings for the given user, as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings"><b>[GET]</b> /user/ratings</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing a list of user ratings
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
     *                      if no information exists for the current user
     */
    public static JsonNode getRatings(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource(BASE, "/ratings"));
    }

    /**
     * Returns a list of ratings for a given user that match the query, as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings_query"><b>[GET]</b> /user/ratings/query</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param params Object containing key/value pairs of query parameters. For a complete list of possible query parameters
     *               see the API documentation or use {@link #getRatingsQueryParams(APIConnection) getRatingsQueryParams(con)}.
     *
     * @return JSON object containing a list of user ratings that match the given query
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
     *                      if no information exists for the current user
     */
    public static JsonNode queryRatings(@Nonnull APIConnection con, @CheckForNull QueryParameters params) throws APIException {
        return con.sendGET(createQueryResource(BASE, "/ratings/query", params));
    }

    /**
     * Returns a list of valid parameters for querying user ratings, as raw JSON. These keys are permitted to be used in {@link QueryParameters}
     * objects when querying for ratings.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/get_user_ratings_query_params"><b>[GET]</b> /user/ratings/query/params</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing a list of possible parameters which may be used to query for user ratings
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
     *                      if no information exists for the current user
     */
    public static JsonNode getRatingsQueryParams(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource(BASE, "/ratings/query/params"));
    }

    /**
     * Deletes a given rating of a given type.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/delete_user_ratings_itemType_itemId"><b>[DELETE]</b> /user/ratings/{itemType}/{itemId}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param itemType Item to update. Can be either 'series', 'episode', or 'image'.
     * @param itemId ID of the ratings record that you wish to delete
     *
     * @return JSON object as returned by the remote service (probably containing an empty data block)
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
     *                      if no rating is found that matches your given parameters
     */
    public static JsonNode deleteFromRatings(@Nonnull APIConnection con, @Nonnull String itemType, long itemId) throws APIException {
        Parameters.validatePathParam(PATH_ITEMTYPE, itemType, ITEMTYPE_VALIDATOR);
        Parameters.validatePathParam(PATH_ITEMID, itemId, ID_VALIDATOR);
        return con.sendDELETE(createResource(BASE, "/ratings", itemType, itemId));
    }

    /**
     * Updates a given rating of a given type and returns the modified rating, mapped as raw JSON. If no rating exists yet, a new rating
     * will be created.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Users/put_user_ratings_itemType_itemId_itemRating"><b>[PUT]</b> /user/ratings/{itemType}/{itemId}/{itemRating}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param itemType Item to update. Can be either 'series', 'episode', or 'image'.
     * @param itemId ID of the ratings record that you wish to modify
     * @param itemRating The updated rating number
     *
     * @return JSON object containing the modified rating (whether it was added or updated)
     *         <br>
     *         <b>Note:</b> It seems that the data returned by the remote service for this route is quite unreliable! It might not always return the
     *         modified rating but an empty data array instead.
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource not found, etc. or
     *                      if no rating is found that matches your given parameters
     */
    public static JsonNode addToRatings(@Nonnull APIConnection con, @Nonnull String itemType, long itemId, long itemRating) throws APIException {
        Parameters.validatePathParam(PATH_ITEMTYPE, itemType, ITEMTYPE_VALIDATOR);
        Parameters.validatePathParam(PATH_ITEMID, itemId, ID_VALIDATOR);
        Parameters.validatePathParam(PATH_ITEMRATING, itemRating, ID_VALIDATOR);
        return con.sendPUT(createResource(BASE, "/ratings", itemType, itemId, itemRating));
    }
}

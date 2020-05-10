package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import java.util.Arrays;
import java.util.function.Predicate;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.resource.validation.PathValidator;
import com.github.m0nk3y2k4.thetvdb.internal.resource.QueryResource;

public final class UsersAPI extends QueryResource {

    private static final String BASE = "/user";

    private static final Predicate<String> ITEMTYPE_VALIDATOR = value -> Arrays.asList("series", "episode", "image").contains(value);

    private static final String PATH_ITEMTYPE = "itemType";
    private static final String PATH_ITEMID = "itemId";
    private static final String PATH_ITEMRATING = "itemRating";

    private UsersAPI() {}     // Private constructor. Only static methods

    public static JsonNode get(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(BASE);
    }

    public static JsonNode getFavorites(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource(BASE, "/favorites"));
    }

    public static JsonNode deleteFromFavorites(@Nonnull APIConnection con, long id) throws APIException {
        PathValidator.requiresPathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendDELETE(createResource(BASE, "/favorites", id));
    }

    public static JsonNode addToFavorites(@Nonnull APIConnection con, long id) throws APIException {
        PathValidator.requiresPathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendPUT(createResource(BASE, "/favorites", id));
    }

    public static JsonNode getRatings(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource(BASE, "/ratings"));
    }

    public static JsonNode queryRatings(@Nonnull APIConnection con, @CheckForNull QueryParameters params) throws APIException {
        return con.sendGET(createQueryResource(BASE, "/ratings/query", params));
    }

    public static JsonNode getRatingsQueryParams(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource(BASE, "/ratings/query/params"));
    }

    public static JsonNode deleteFromRatings(@Nonnull APIConnection con, @Nonnull String itemType, long itemId) throws APIException {
        PathValidator.requiresPathParam(PATH_ITEMTYPE, itemType, ITEMTYPE_VALIDATOR);
        PathValidator.requiresPathParam(PATH_ITEMID, itemId, ID_VALIDATOR);
        return con.sendDELETE(createResource(BASE, "/ratings", itemType, itemId));
    }

    public static JsonNode addToRatings(@Nonnull APIConnection con, @Nonnull String itemType, long itemId, long itemRating) throws APIException {
        PathValidator.requiresPathParam(PATH_ITEMTYPE, itemType, ITEMTYPE_VALIDATOR);
        PathValidator.requiresPathParam(PATH_ITEMID, itemId, ID_VALIDATOR);
        PathValidator.requiresPathParam(PATH_ITEMRATING, itemRating, ID_VALIDATOR);
        return con.sendPUT(createResource(BASE, "/ratings", itemType, itemId, itemRating));
    }
}

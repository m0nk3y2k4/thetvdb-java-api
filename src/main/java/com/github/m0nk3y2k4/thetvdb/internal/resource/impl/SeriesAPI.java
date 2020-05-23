package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.resource.QueryResource;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

public final class SeriesAPI extends QueryResource {

    private static final String BASE = "/series";

    private SeriesAPI() {}     // Private constructor. Only static methods

    public static JsonNode get(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(BASE, id));
    }

    public static JsonNode getHead(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendHEAD(createResource(BASE, id));
    }

    public static JsonNode getActors(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(id, "/actors"));
    }

    public static JsonNode getEpisodes(@Nonnull APIConnection con, long id, @CheckForNull QueryParameters params) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(id, "/episodes", params));
    }

    public static JsonNode queryEpisodes(@Nonnull APIConnection con, long id, @CheckForNull QueryParameters params) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(id, "/episodes/query", params));
    }

    public static JsonNode getEpisodesQueryParams(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(id, "/episodes/query/params"));
    }

    public static JsonNode getEpisodesSummary(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(id, "/episodes/summary"));
    }

    public static JsonNode filter(@Nonnull APIConnection con, long id, @CheckForNull QueryParameters params) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        Parameters.validateQueryParam(Query.Series.KEYS, params);
        return con.sendGET(createResource(id, "/filter", params));
    }

    public static JsonNode getFilterParams(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(id, "/filter/params"));
    }

    public static JsonNode getImages(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(id, "/images"));
    }

    public static JsonNode queryImages(@Nonnull APIConnection con, long id, @CheckForNull QueryParameters params) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(id, "/images/query", params));
    }

    public static JsonNode getImagesQueryParams(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(id, "/images/query/params"));
    }

    private static String createResource(long id, @Nonnull String specific) {
        return createResource(id, specific, null);
    }

    private static String createResource(long id, @Nonnull String specific, @CheckForNull QueryParameters params) {
        String baseWithId = BASE + "/" + id;
        if (params != null) {
            return createQueryResource(baseWithId, specific, params);
        }
        return createResource(baseWithId, specific);
    }
}

package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import java.util.Map;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.resource.QueryResource;

public final class SearchAPI extends QueryResource {

    private static final String BASE = "/search/series";

    private SearchAPI() {}     // Private constructor. Only static methods

    public static JsonNode series(@Nonnull APIConnection con, Map<String, String> params) throws APIException {
        return con.sendGET(createQueryResource(BASE, params));
    }

    public static JsonNode getAvailableSearchParameters(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource(BASE, "/params"));
    }
}

package com.github.m0nk3y2k4.thetvdb.resource.impl;

import java.util.Map;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.resource.QueryResource;
import com.github.m0nk3y2k4.thetvdb.resource.validation.ParamValidator;

public final class UpdatesAPI extends QueryResource {

    private static final String BASE = "/updated/query";

    public static final String QUERY_FROMTIME = "fromTime";
    public static final String QUERY_TOTIME = "toTime";

    private UpdatesAPI() {}     // Private constructor. Only static methods

    public static JsonNode query(@Nonnull APIConnection con, Map<String, String> params) throws APIException {
        ParamValidator.requiresQueryParam(QUERY_FROMTIME, params, value -> value.matches("\\d+") && Long.valueOf(value).compareTo(0L) > 0);
        return con.sendGET(createQueryResource(BASE, params));
    }

    public static JsonNode getQueryParams(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource(BASE, "/params"));
    }
}

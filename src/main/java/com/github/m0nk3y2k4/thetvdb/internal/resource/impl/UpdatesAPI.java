package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.resource.validation.ParamValidator;
import com.github.m0nk3y2k4.thetvdb.internal.resource.QueryResource;

public final class UpdatesAPI extends QueryResource {

    private static final String BASE = "/updated/query";

    private UpdatesAPI() {}     // Private constructor. Only static methods

    public static JsonNode query(@Nonnull APIConnection con, QueryParameters params) throws APIException {
        ParamValidator.requiresQueryParam(Query.Updates.FROMTIME, params, value -> value.matches("\\d+") && Long.valueOf(value).compareTo(0L) > 0);
        return con.sendGET(createQueryResource(BASE, params));
    }

    public static JsonNode getQueryParams(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource(BASE, "/params"));
    }
}

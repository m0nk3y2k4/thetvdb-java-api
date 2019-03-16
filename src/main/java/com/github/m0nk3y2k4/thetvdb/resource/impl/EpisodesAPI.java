package com.github.m0nk3y2k4.thetvdb.resource.impl;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.resource.Resource;
import com.github.m0nk3y2k4.thetvdb.resource.validation.ParamValidator;

public final class EpisodesAPI extends Resource {

    private static final String BASE = "/episodes";

    private EpisodesAPI() {}     // Private constructor. Only static methods

    public static JsonNode get(@Nonnull APIConnection con, long id) throws APIException {
        ParamValidator.requiresPathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(BASE, id));
    }
}

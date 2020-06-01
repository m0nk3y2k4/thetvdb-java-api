package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.resource.Resource;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Implementation of a connector for the remote API's <a href="https://api.thetvdb.com/swagger#/Episodes">Episodes</a> endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used for fetching information about a specific episode.
 */
public final class EpisodesAPI extends Resource {

    /** Base URL path parameter for this endpoint */
    private static final String BASE = "/episodes";

    private EpisodesAPI() {}     // Private constructor. Only static methods

    /**
     * Returns the full information for a given episode id as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Episodes/get_episodes_id"><b>[GET]</b> /episodes/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id The ID of the episode to fetch
     *
     * @return JSON object containing the full episode information
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, the given episode ID does not exist, etc.
     */
    public static JsonNode get(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource(BASE, id));
    }
}

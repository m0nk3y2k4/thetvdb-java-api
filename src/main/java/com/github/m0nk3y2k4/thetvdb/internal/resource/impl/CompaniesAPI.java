/*
 * Copyright (C) 2019 - 2021 thetvdb-java-api Authors and Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.m0nk3y2k4.thetvdb.internal.resource.impl;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.resource.QueryResource;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Implementation of a connector for the remote API's
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/companies">companies</a>
 * endpoint.
 * <p><br>
 * Provides static access to all routes of this endpoint which may be used for obtaining specific company information.
 */
public final class CompaniesAPI extends QueryResource {

    private CompaniesAPI() {}       // Private constructor. Only static methods

    /**
     * Returns a list of companies based on the given query parameters as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/companies/getAllCompanies">
     * <b>[GET]</b> /companies</a>
     *
     * @param con    Initialized connection to be used for API communication
     * @param params Object containing key/value pairs of query parameters
     *
     * @return JSON object containing a limited overview of companies
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getAllCompanies(@Nonnull APIConnection con, QueryParameters params) throws APIException {
        return con.sendGET(createQueryResource("/companies", params));
    }

    /**
     * Returns a list of available company types as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/companies/getCompanyTypes">
     * <b>[GET]</b> /companies/types</a>
     *
     * @param con Initialized connection to be used for API communication
     *
     * @return JSON object containing an overview of available company types
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    public static JsonNode getCompanyTypes(@Nonnull APIConnection con) throws APIException {
        return con.sendGET(createResource("/companies/types"));
    }

    /**
     * Returns information for a specific company record as raw JSON.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/companies/getCompany">
     * <b>[GET]</b> /companies/{id}</a>
     *
     * @param con Initialized connection to be used for API communication
     * @param id  The <i>TheTVDB.com</i> company ID
     *
     * @return JSON object containing information for a specific company record
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, no
     *                      company record with the given ID exists, etc.
     */
    public static JsonNode getCompany(@Nonnull APIConnection con, long id) throws APIException {
        Parameters.validatePathParam(PATH_ID, id, ID_VALIDATOR);
        return con.sendGET(createResource("/companies/{id}", id));
    }
}

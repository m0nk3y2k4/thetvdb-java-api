/*
 * Copyright (C) 2019 - 2020 thetvdb-java-api Authors and Contributors
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

package com.github.m0nk3y2k4.thetvdb.internal.api.impl;

import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory;
import com.github.m0nk3y2k4.thetvdb.api.Proxy;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.internal.connection.APIConnection;
import com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

// ToDo: Rework JDoc once APIv4 implementation is finished

/**
 * Implementation of the {@link TheTVDBApi} API layout. It provides methods for all sorts of API calls throughout the
 * different API routes. Responses will be returned as mapped Java DTO objects.
 */
public class TheTVDBApiImpl implements TheTVDBApi {

    /** Wrapper API: Consolidates TheTVDBApi calls which return raw JSON */
    private final JSON jsonApi = new JSONApi();

    /** Wrapper API: Consolidates TheTVDBApi calls which return extended response information */
    private final Extended extendedApi = new ExtendedApi();

    /** The actual connection to the remote API */
    private final APIConnection con;

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid
     * <a href="https://www.thetvdb.com/member/api">TheTVDB.com API Key</a> as it will be used for remote service
     * authentication. To authenticate and generate a new session token use the {@link #init()} or {@link #login()}
     * method right after creating a new instance of this API.
     * <p><br>
     * <b>NOTE:</b> Objects created with this constructor <u>can not</u> be used for calls to the remote API's
     * <a href="https://api.thetvdb.com/swagger#/Users">/users</a> routes. These calls require extended authentication
     * using an additional <em>{@code userKey}</em> and <em>{@code userName}</em>.
     *
     * @param apiKey Valid <i>TheTVDB.com</i> API-Key
     *
     * @see #TheTVDBApiImpl(String, String, String) TheTVDBApiImpl(apiKey, userKey, userName)
     */
    public TheTVDBApiImpl(@Nonnull String apiKey) {
        this.con = new APIConnection(apiKey);
    }

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid
     * <a href="https://www.thetvdb.com/member/api">TheTVDB.com API Key</a> as it will be used for remote service
     * authentication. To authenticate and generate a new session token use the {@link #init()} or {@link #login()}
     * method right after creating a new instance of this API. All communication to the remote API will be forwarded to
     * the given <em>{@code proxy}</em>.
     * <p><br>
     * <b>NOTE:</b> Objects created with this constructor <u>can not</u> be used for calls to the remote API's
     * <a href="https://api.thetvdb.com/swagger#/Users">/users</a> routes. These calls require extended authentication
     * using an additional <em>{@code userKey}</em> and <em>{@code userName}</em>.
     *
     * @param apiKey Valid <i>TheTVDB.com</i> API-Key
     * @param proxy  The proxy service to be used for remote API communication
     *
     * @see #TheTVDBApiImpl(String, String, String, Proxy) TheTVDBApiImpl(apiKey, userKey, userName, proxy)
     */
    public TheTVDBApiImpl(@Nonnull String apiKey, @Nonnull Proxy proxy) {
        Parameters.validateNotNull(proxy, "Proxy must not be NULL");
        this.con = new APIConnection(apiKey, () -> new RemoteAPI.Builder().from(proxy).build());
    }

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid
     * <a href="https://www.thetvdb.com/member/api">TheTVDB.com API Key</a>. The <em>{@code userKey}</em> and
     * <em>{@code userName}</em> must refer to a registered <i>TheTVDB.com</i> user account. The given parameters will
     * be used for the initial remote service authentication. To authenticate and generate a new session token use the
     * {@link #init()} or {@link #login()} method right after creating a new instance of this API.
     *
     * @param apiKey   Valid <i>TheTVDB.com</i> API-Key
     * @param userKey  Valid <i>TheTVDB.com</i> user key (also referred to as "Unique ID")
     * @param userName Registered <i>TheTVDB.com</i> user name
     */
    public TheTVDBApiImpl(@Nonnull String apiKey, @Nonnull String userKey, @Nonnull String userName) {
        this.con = new APIConnection(apiKey, userKey, userName);
    }

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid
     * <a href="https://www.thetvdb.com/member/api">TheTVDB.com API Key</a>. The <em>{@code userKey}</em> and
     * <em>{@code userName}</em> must refer to a registered <i>TheTVDB.com</i> user account. The given parameters will
     * be used for the initial remote service authentication. To authenticate and generate a new session token use the
     * {@link #init()} or {@link #login()} method right after creating a new instance of this API. All communication to
     * the remote API will be forwarded to the given <em>{@code proxy}</em>.
     *
     * @param apiKey   Valid <i>TheTVDB.com</i> API-Key
     * @param userKey  Valid <i>TheTVDB.com</i> user key (also referred to as "Unique ID")
     * @param userName Registered <i>TheTVDB.com</i> user name
     * @param proxy    The proxy service to be used for remote API communication
     */
    public TheTVDBApiImpl(@Nonnull String apiKey, @Nonnull String userKey, @Nonnull String userName,
            @Nonnull Proxy proxy) {
        Parameters.validateNotNull(proxy, "Proxy must not be NULL");
        this.con = new APIConnection(apiKey, userKey, userName, () -> new RemoteAPI.Builder().from(proxy).build());
    }

    /**
     * Validates that none of the given method String parameters is NULL or empty
     *
     * @param params Array of method String parameter to check
     *
     * @throws IllegalArgumentException If at least one of the given parameters is NULL or empty
     */
    private static void validateNotEmpty(String... params) {
        Parameters.validateCondition(APIUtil::hasValue, params,
                new IllegalArgumentException("Method parameters must not be NULL or empty!"));
    }

    /**
     * Creates an empty query parameters object with no individual parameters set
     *
     * @return Empty query parameters object
     */
    private static QueryParameters emptyQuery() {
        return TheTVDBApiFactory.createQueryParameters();
    }

    /**
     * Creates a new query parameters object with preset parameters based on the given map of key/value pairs
     *
     * @param parameters Map of parameter key/value pairs. For each entry in the map an appropriate parameter will be
     *                   added in the object returned by this method
     *
     * @return New query parameters object with a preset collection of individual query parameters
     */
    private static QueryParameters query(@Nonnull Map<String, String> parameters) {
        return TheTVDBApiFactory.createQueryParameters(parameters);
    }

    @Override
    public void init() throws APIException {
        login();
    }

    @Override
    public void init(@Nonnull String token) throws APIException {
        con.setToken(token);
    }

    @Override
    public Optional<String> getToken() {
        return con.getToken();
    }

    @Override
    public void setLanguage(String languageCode) {
        con.setLanguage(languageCode);
    }

    @Override
    public void login() throws APIException {
        // ToDo: Adjust to new APIv4 login mechanics
//        AuthenticationAPI.login(con);
    }

    @Override
    public void refreshToken() throws APIException {
        // ToDo: Adjust to new APIv4 login mechanics
//        AuthenticationAPI.refreshSession(con);
    }

    @Override
    public JSON json() {
        return jsonApi;
    }

    @Override
    public Extended extended() {
        return extendedApi;
    }

    /**
     * Implementation of the {@link TheTVDBApi.JSON} API layout. It provides methods for all sorts of API calls
     * throughout the different API routes. Responses will be returned as raw, untouched JSON as it has been received by
     * the remote REST service.
     */
    private class JSONApi implements JSON {

    }

    /**
     * Implementation of the {@link TheTVDBApi.Extended} API layout. It provides methods for all sorts of API calls
     * throughout the different API routes. Responses will be returned as wrapped {@link APIResponse
     * APIResponse&lt;DTO&gt;} objects containing additional error and paging information.
     */
    private class ExtendedApi implements Extended {

    }
}

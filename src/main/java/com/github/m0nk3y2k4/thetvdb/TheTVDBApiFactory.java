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

package com.github.m0nk3y2k4.thetvdb;

import java.util.Map;

import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.api.Proxy;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.QueryParametersImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.TheTVDBApiImpl;
import com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI;

/**
 * Factory used to create new API instances
 * <p><br>
 * Provides methods to create new {@link TheTVDBApi} instances with a given set of initialization parameters. Also
 * offers factory methods for creating additional objects that are commonly used in the context of working with the
 * API.
 */
public class TheTVDBApiFactory {

    private TheTVDBApiFactory() {}      // Hidden constructor. Only static methods

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid
     * <a href="https://www.thetvdb.com/member/api">TheTVDB.com API Key</a> which will be used for remote service
     * authentication. To authenticate and generate a new session token use the {@link TheTVDBApi#init()} or {@link
     * TheTVDBApi#login()} methods right after creating a new instance of this API.
     * <p><br>
     * <b>NOTE:</b> Objects created with this constructor <u>can not</u> be used for calls to the remote API's
     * <a href="https://api.thetvdb.com/swagger#/Users">/users</a> routes. These calls require extended authentication
     * using an additional <em>{@code userKey}</em> and <em>{@code userName}</em>.
     *
     * @param apiKey Valid <i>TheTVDB.com</i> API-Key
     *
     * @return A new TheTVDBApi instance using the given API key for authentication
     *
     * @see #createApi(String, String, String) createApi(apiKey, userName, userKey)
     */
    public static TheTVDBApi createApi(@Nonnull String apiKey) {
        return new TheTVDBApiImpl(apiKey);
    }

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid
     * <a href="https://www.thetvdb.com/member/api">TheTVDB.com API Key</a> which will be used for remote service
     * authentication. To authenticate and generate a new session token use the {@link TheTVDBApi#init()} or {@link
     * TheTVDBApi#login()} methods right after creating a new instance of this API. All communication to the remote API
     * will be forwarded to the given <em>{@code proxy}</em>.
     * <p><br>
     * <b>NOTE:</b> Objects created with this constructor <u>can not</u> be used for calls to the remote API's
     * <a href="https://api.thetvdb.com/swagger#/Users">/users</a> routes. These calls require extended authentication
     * using an additional <em>{@code userKey}</em> and <em>{@code userName}</em>.
     *
     * @param apiKey Valid <i>TheTVDB.com</i> API-Key
     * @param proxy  The proxy service to be used for remote API communication
     *
     * @return A new TheTVDBApi instance using the given API key for authentication
     *
     * @see #createApi(String, String, String, Proxy) createApi(apiKey, userName, userKey, proxy)
     */
    public static TheTVDBApi createApi(@Nonnull String apiKey, @Nonnull Proxy proxy) {
        return new TheTVDBApiImpl(apiKey, proxy);
    }

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid
     * <a href="https://www.thetvdb.com/member/api">TheTVDB.com API Key</a>. The <em>{@code userKey}</em> and
     * <em>{@code userName}</em> must refer to a registered <i>TheTVDB.com</i> user account. The given parameters will
     * be used for the initial remote service authentication. To authenticate and generate a new session token use the
     * {@link TheTVDBApi#init()} or {@link TheTVDBApi#login()} methods right after creating a new instance of this API.
     *
     * @param apiKey   Valid <i>TheTVDB.com</i> API-Key
     * @param userKey  Valid <i>TheTVDB.com</i> user key (also referred to as "Unique ID")
     * @param userName Registered <i>TheTVDB.com</i> user name
     *
     * @return A new TheTVDBApi instance using the given API key, user key and user name for authentication
     */
    public static TheTVDBApi createApi(@Nonnull String apiKey, @Nonnull String userKey, @Nonnull String userName) {
        return new TheTVDBApiImpl(apiKey, userKey, userName);
    }

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid
     * <a href="https://www.thetvdb.com/member/api">TheTVDB.com API Key</a>. The <em>{@code userKey}</em> and
     * <em>{@code userName}</em> must refer to a registered <i>TheTVDB.com</i> user account. The given parameters will
     * be used for the initial remote service authentication. To authenticate and generate a new session token use the
     * {@link TheTVDBApi#init()} or {@link TheTVDBApi#login()} methods right after creating a new instance of this API.
     * All communication to the remote API will be forwarded to the given <em>{@code proxy}</em>.
     *
     * @param apiKey   Valid <i>TheTVDB.com</i> API-Key
     * @param userKey  Valid <i>TheTVDB.com</i> user key (also referred to as "Unique ID")
     * @param userName Registered <i>TheTVDB.com</i> user name
     * @param proxy    The proxy service to be used for remote API communication
     *
     * @return A new TheTVDBApi instance using the given API key, user key and user name for authentication
     */
    public static TheTVDBApi createApi(@Nonnull String apiKey, @Nonnull String userKey, @Nonnull String userName,
            @Nonnull Proxy proxy) {
        return new TheTVDBApiImpl(apiKey, userKey, userName, proxy);
    }

    /**
     * Creates a new query parameter objects to be used with some of the API calls. The returned object is empty and
     * does not contain any preset query parameters. Those parameters have to be added manually by using the {@link
     * QueryParameters#addParameter(String, String) QueryParameters#addParameter(key, value)} method.
     *
     * @return A new, empty query parameter object
     */
    public static QueryParameters createQueryParameters() {
        return new QueryParametersImpl();
    }

    /**
     * Creates a new query parameter objects to be used with some of the API calls. The returned object is contain a
     * preset collection of query parameters based on the given map of key/value pairs. Additional parameters may be
     * added manually to this object by using the {@link QueryParameters#addParameter(String, String)
     * QueryParameters#addParameter(key, value)} method.
     *
     * @param parameters Map containing key/value pairs of query parameters to be initially added to the object returned
     *                   by this method. Might be empty but not <em>{@code null}</em>.
     *
     * @return A new query parameter object with a preset collection of individual parameters
     */
    public static QueryParameters createQueryParameters(@Nonnull Map<String, String> parameters) {
        return new QueryParametersImpl(parameters);
    }

    /**
     * Creates a new proxy object based on the given parameters. A proxy may be provided when creating a new TheTVDBApi
     * instance in order to forward all communication towards this proxy instead of directly communicating with the
     * remote <i>TheTVDB.com</i> API.
     *
     * @param protocol The protocol used by the proxy
     * @param host     The host name of the proxy
     * @param port     The port number to be used for communication
     *
     * @return New immutable proxy object based on the given parameters
     */
    public static Proxy createProxy(@Nonnull String protocol, @Nonnull String host, int port) {
        return new RemoteAPI.Builder().protocol(protocol).host(host).port(port).build();
    }
}

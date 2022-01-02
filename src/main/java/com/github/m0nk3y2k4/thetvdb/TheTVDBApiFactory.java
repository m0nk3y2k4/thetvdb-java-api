/*
 * Copyright (C) 2019 - 2022 thetvdb-java-api Authors and Contributors
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

import com.github.m0nk3y2k4.thetvdb.api.APIKey;
import com.github.m0nk3y2k4.thetvdb.api.Proxy;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.FundingModel;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.APIKeyImpl;
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
public final class TheTVDBApiFactory {

    private TheTVDBApiFactory() {}      // Hidden constructor. Only static methods

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid
     * <a target="_blank" href="https://www.thetvdb.com/dashboard/account/apikey">TheTVDB.com v4 API Key</a> which will
     * be used for remote service authentication. To authenticate and generate a new session token use the {@link
     * TheTVDBApi#init()} or {@link TheTVDBApi#login()} methods right after creating a new instance of this API.
     *
     * @param apiKey Valid <i>TheTVDB.com</i> v4 API-Key
     *
     * @return A new TheTVDBApi instance using the given API key for authentication
     *
     * @see TheTVDBApiFactory#createAPIKey(String) createAPIKey(apiKey)
     * @see TheTVDBApiFactory#createAPIKey(String, String) createAPIKey(apiKey, pin)
     */
    public static TheTVDBApi createApi(@Nonnull APIKey apiKey) {
        return new TheTVDBApiImpl(apiKey);
    }

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid
     * <a target="_blank" href="https://www.thetvdb.com/dashboard/account/apikey">TheTVDB.com v4 API Key</a> which will
     * be used for remote service authentication. To authenticate and generate a new session token use the {@link
     * TheTVDBApi#init()} or {@link TheTVDBApi#login()} methods right after creating a new instance of this API. All
     * communication to the remote API will be forwarded to the given <em>{@code proxy}</em>.
     *
     * @param apiKey Valid <i>TheTVDB.com</i> v4 API-Key
     * @param proxy  The proxy service to be used for remote API communication
     *
     * @return A new TheTVDBApi instance using the given API key for authentication forwarding all communication to the
     *         given proxy
     *
     * @see TheTVDBApiFactory#createAPIKey(String) createAPIKey(apiKey)
     * @see TheTVDBApiFactory#createAPIKey(String, String) createAPIKey(apiKey, pin)
     */
    public static TheTVDBApi createApi(@Nonnull APIKey apiKey, @Nonnull Proxy proxy) {
        return new TheTVDBApiImpl(apiKey, proxy);
    }

    /**
     * Creates a new query parameter objects to be used with some API calls. The returned object is empty and does not
     * contain any preset query parameters. Those parameters have to be added manually by using the {@link
     * QueryParameters#addParameter(String, String) QueryParameters#addParameter(key, value)} method.
     *
     * @return A new, empty query parameter object
     */
    public static QueryParameters createQueryParameters() {
        return new QueryParametersImpl();
    }

    /**
     * Creates a new query parameter objects to be used with some API calls. The returned object contains a preset
     * collection of query parameters based on the given map of key/value pairs. Additional parameters may be added
     * manually to this object by using the {@link QueryParameters#addParameter(String, String)
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
     * remote <i>TheTVDB.com</i> API. The returned proxy object represents a URI in the following format:
     * <em>{@code "protocol://host:port"}</em>.
     *
     * @param protocol The protocol used by the proxy
     * @param host     The host name of the proxy
     * @param port     The port number to be used for communication
     *
     * @return New immutable proxy object based on the given parameters
     *
     * @see TheTVDBApiFactory#createProxy(String, String, String, int) createProxy(protocol, host, path, port)
     */
    public static Proxy createProxy(@Nonnull String protocol, @Nonnull String host, int port) {
        return new RemoteAPI.Builder().protocol(protocol).host(host).port(port).build();
    }

    /**
     * Creates a new proxy object based on the given parameters. A proxy may be provided when creating a new TheTVDBApi
     * instance in order to forward all communication towards this proxy instead of directly communicating with the
     * remote <i>TheTVDB.com</i> API. The returned proxy object represents a URI in the following format:
     * <em>{@code "protocol://host:port/path"}</em>.
     *
     * @param protocol The protocol used by the proxy
     * @param host     The host name of the proxy
     * @param path     The path component with a leading '/'. Will be appended to the authority component.
     * @param port     The port number to be used for communication
     *
     * @return New immutable proxy object based on the given parameters
     *
     * @see TheTVDBApiFactory#createProxy(String, String, int) createProxy(protocol, host, port)
     */
    public static Proxy createProxy(@Nonnull String protocol, @Nonnull String host, @Nonnull String path, int port) {
        return new RemoteAPI.Builder().from(createProxy(protocol, host, port)).path(path).build();
    }

    /**
     * Creates a new APIKey instance based on the given value. This type of key will typically be issued based on a
     * specific contract and is often publicly available. If you have negotiated your own contract with
     * <i>TheTVDB.com</i> the API key should be available on your
     * <a target="_blank" href="https://www.thetvdb.com/dashboard/account/apikey">TheTVDB.com dashboard</a> under the
     * "{@code v4 API Keys}" section. For keys that have been issued based on an end-user subscription you will have to
     * authenticate with an {@link #createAPIKey(String, String) additional PIN}. The returned API key can be used for
     * creating new TheTVDBApi instances.
     *
     * @param apiKey Valid <i>TheTVDB.com</i> v4 API-Key
     *
     * @return API key used to create a new TheTVDBApi instance
     *
     * @see TheTVDBApiFactory#createApi(APIKey)
     * @see TheTVDBApiFactory#createApi(APIKey, Proxy)
     * @see TheTVDBApiFactory#createAPIKey(String, String) createAPIKey(apiKey, pin)
     */
    public static APIKey createAPIKey(@Nonnull String apiKey) {
        return new APIKeyImpl.Builder().apiKey(apiKey).fundingModel(FundingModel.CONTRACT).build();
    }

    /**
     * Creates a new APIKey instance based on the given parameters. This method is to be used for private keys that have
     * been issued based on an end-user subscription. This type of key requires an additional PIN for authentication.
     * Both, the API key and the PIN are available on your
     * <a target="_blank" href="https://www.thetvdb.com/dashboard/account/apikey">TheTVDB.com dashboard</a>
     * under the "{@code v4 API Keys}" section. The returned API key can be used to create a new TheTVDBApi instance.
     *
     * @param apiKey Valid <i>TheTVDB.com</i> v4 API-Key
     * @param pin    Additional PIN necessary for end-user subscription based authentication
     *
     * @return API key used to create a new TheTVDBApi instance
     *
     * @see TheTVDBApiFactory#createApi(APIKey)
     * @see TheTVDBApiFactory#createApi(APIKey, Proxy)
     * @see TheTVDBApiFactory#createAPIKey(String) createAPIKey(apiKey)
     */
    public static APIKey createAPIKey(@Nonnull String apiKey, @Nonnull String pin) {
        return new APIKeyImpl.Builder().apiKey(apiKey).pin(pin).fundingModel(FundingModel.SUBSCRIPTION).build();
    }
}

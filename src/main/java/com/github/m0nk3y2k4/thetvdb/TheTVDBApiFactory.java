package com.github.m0nk3y2k4.thetvdb;

import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.QueryParametersImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.TheTVDBApiImpl;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Factory used to create new API instances
 * <p><br>
 * Provides methods to create new {@link TheTVDBApi} instances with a given set of initialization parameters. Also offers factory methods
 * for creating additional objects that are commonly used in the context of working with the API.
 */
public class TheTVDBApiFactory {

    private TheTVDBApiFactory() {}      // Hidden constructor. Only static methods

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid <a href="https://www.thetvdb.com/member/api">TheTVDB API Key</a> which will
     * be used for remote service authentication. To authenticate and generate a new session token use the {@link TheTVDBApi#init()} or {@link TheTVDBApi#login()}
     * methods right after creating a new instance of this API.
     * <p><br>
     * <b>NOTE:</b> Objects created with this constructor <u>can not</u> be used for calls to the remote API's <a href="https://api.thetvdb.com/swagger#/Users">/users</a>
     * routes. These calls require extended authentication using an additional <em>{@code userKey}</em> and <em>{@code userName}</em>.
     *
     * @see #createApi(String, String, String) createApi(apiKey, userName, userKey)
     *
     * @param apiKey Valid TheTVDB API-Key
     *
     * @return A new TheTVDBApi instance using the given API key for authentication
     */
    public static TheTVDBApi createApi(@Nonnull String apiKey) {
        return new TheTVDBApiImpl(apiKey);
    }

    /**
     * Creates a new TheTVDBApi instance. The given <em>{@code apiKey}</em> must be a valid <a href="https://www.thetvdb.com/member/api">TheTVDB API Key</a>. The <em>{@code userKey}</em>
     * and <em>{@code userName}</em> must refer to a registered TheTVDB user account. The given parameters will be used for the initial remote service authentication. To authenticate
     * and generate a new session token use the {@link TheTVDBApi#init()} or {@link TheTVDBApi#login()} methods right after creating a new instance of this API.
     *
     * @param apiKey Valid TheTVDB API-Key
     * @param userKey Valid TheTVDB user key (also referred to as "Unique ID")
     * @param userName Registered TheTVDB user name
     *
     * @return A new TheTVDBApi instance using the given API key, user key and user name for authentication
     */
    public static TheTVDBApi createApi(@Nonnull String apiKey, @Nonnull String userKey, @Nonnull String userName) {
        return new TheTVDBApiImpl(apiKey, userKey, userName);
    }

    /**
     * Creates a new query parameter objects to be used with some of the API calls. The returned object is empty and does
     * not contain any preset query parameters. Those parameters have to be added manually by using the
     * {@link QueryParameters#addParameter(String, String) QueryParameters#addParameter(key, value)} method.
     *
     * @return A new, empty query parameter object
     */
    public static QueryParameters createQueryParameters() {
        return new QueryParametersImpl();
    }

    /**
     * Creates a new query parameter objects to be used with some of the API calls. The returned object is contain a preset
     * collection of query parameters based on the given map of key/value pairs. Additional parameters may be added manually
     * to this object by using the {@link QueryParameters#addParameter(String, String) QueryParameters#addParameter(key, value)} method.
     *
     * @param parameters Map containing key/value pairs of query parameters to be initially added to the object returned by this
     *                   method. Might be empty but not <em>{@code null}</em>.
     *
     * @return A new query parameter object with a preset collection of individual parameters
     */
    public static QueryParameters createQueryParameters(@Nonnull Map<String, String> parameters) {
        return new QueryParametersImpl(parameters);
    }
}

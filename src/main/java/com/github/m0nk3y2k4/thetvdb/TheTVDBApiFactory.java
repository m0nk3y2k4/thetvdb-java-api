package com.github.m0nk3y2k4.thetvdb;

import com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.TheTVDBApiImpl;

import javax.annotation.Nonnull;

public class TheTVDBApiFactory {

    /**
     * Creates a new TheTVDBApi instance. The given <code>apiKey</code> must be a valid <a href="https://www.thetvdb.com/member/api">TheTVDB API Key</a> which will
     * be used for remote service authentication. To authenticate and generate a new session token use the {@link TheTVDBApi#init()} or {@link TheTVDBApi#login()}
     * methods right after creating a new instance of this API.
     * <p/>
     * <b>NOTE:</b> Objects created with this constructor <u>can not</u> be used for calls to the remote API's <a href="https://api.thetvdb.com/swagger#/Users">/users</a>
     * routes. These call require extended authentication using an additional <code>userName</code> and <code>userKey</code>.
     *
     * @see #createApi(String, String, String) createApi(apiKey, userName, userKey)
     *
     * @param apiKey Valid TheTVDB API-Key
     */
    public static TheTVDBApi createApi(@Nonnull String apiKey) {
        return new TheTVDBApiImpl(apiKey);
    }

    /**
     * Creates a new TheTVDBApi instance. The given <code>apiKey</code> must be a valid <a href="https://www.thetvdb.com/member/api">TheTVDB API Key</a>. The <code>userName</code>
     * and <code>userKey</code> must refer to a registered TheTVDB user account. The given parameters will be used for the initial remote service authentication. To authenticate
     * and generate a new session token use the {@link TheTVDBApi#init()} or {@link TheTVDBApi#login()} methods right after creating a new instance of this API.
     *
     * @param apiKey Valid TheTVDB API-Key
     * @param userName Registered TheTVDB user name
     * @param userKey The password for user login
     */
    public static TheTVDBApi createApi(@Nonnull String apiKey, @Nonnull String userName, @Nonnull String userKey) {
        return new TheTVDBApiImpl(apiKey, userName, userKey);
    }
}

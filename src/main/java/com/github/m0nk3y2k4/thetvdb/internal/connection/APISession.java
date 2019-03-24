package com.github.m0nk3y2k4.thetvdb.internal.connection;

import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

/**
 * Session used for remote API communication. All connections to the TheTVDB API are backed by an instance of this class. These sessions
 * contain all information required for client authentication on the remote service, locale settings as well as session tokens used for
 * remote service communication.
 */
public final class APISession {

    /**
     * Represents the different states of a session. By default, sessions are not authorized for general API communication. Only
     * login/refresh requests may be allowed. During the execution of these kind of requests, the session authorization is in progress.
     * In case the login/refresh were successful the session is authorized and ready for general API communication.
     */
    public enum Status {NOT_AUTHORIZED, AUTHORIZATION_IN_PROGRESS, AUTHORIZED}

    /** Accept english by default if no language was specified */
    private static final String DEFAULT_LANGUAGE = "en";

    /** The API key used to request a session token */
    private final String apiKey;

    /** Optional userKey for authentication */
    private final String userKey;

    /** Optional userName for authentication */
    private final String userName;

    /** Token valid for this session */
    private volatile String token;

    /** The preferred language for API communication based on this session */
    private String language = DEFAULT_LANGUAGE;

    /** The current status of this session in terms of authorization */
    private Status status = Status.NOT_AUTHORIZED;

    /**
     * Creates a new API session with the given API key. The <code>apiKey</code> must be a valid <a href="https://www.thetvdb.com/member/api">TheTVDB API Key</a> which will
     * be used for remote service authentication.
     * <p/>
     * <b>NOTE:</b> Sessions created with this constructor <u>can not</u> be used for calls to the remote API's <a href="https://api.thetvdb.com/swagger#/Users">/users</a>
     * routes. These calls require extended authentication using an additional <code>userKey</code> and <code>userName</code>.
     *
     * @see APISession#APISession(String, String, String) APISession(apiKey, userKey, userName)
     *
     * @param apiKey The API key used to request a session token
     */
    public APISession(@Nonnull String apiKey) {
        Objects.requireNonNull(apiKey, "API key must not be NULL or empty!");

        this.apiKey = apiKey;
        this.userKey = null;
        this.userName = null;
    }

    /**
     * Creates a new API session. The given <code>apiKey</code> must be a valid <a href="https://www.thetvdb.com/member/api">TheTVDB API Key</a>. The <code>userKey</code>
     * and <code>userName</code> must refer to a registered TheTVDB user account. The given parameters will be used for the initial remote service authentication. Sessions
     * created with this constructor can be used for calls to the remote API's <a href="https://api.thetvdb.com/swagger#/Users">/users</a> routes.
     *
     * @see APISession#APISession(String) APISession(apiKey)
     *
     * @param apiKey The API key used to request a session token
     * @param userKey User key for authentication (also referred to as "Unique ID")
     * @param userName User name for authentication
     */
    public APISession(@Nonnull String apiKey, @Nonnull String userKey, @Nonnull String userName) {
        Objects.requireNonNull(apiKey, "API key must not be NULL or empty!");
        Objects.requireNonNull(userKey, "User key must not be NULL or empty!");
        Objects.requireNonNull(userName, "User name must not be NULL or empty!");

        this.apiKey = apiKey;
        this.userKey = userKey;
        this.userName = userName;
    }

    /**
     * Returns the API key of this session
     *
     * @return API key of the session
     */
    public String getApiKey() {
        return this.apiKey;
    }

    /**
     * Returns the user key used for authentication. If set to <code>null</code> user authentication will be skipped.
     *
     * @return The user key
     */
    public Optional<String> getUserKey() {
        return Optional.ofNullable(userKey);
    }

    /**
     * Returns the user name used for authentication. If set to <code>null</code> user authentication will be skipped.
     *
     * @return The user name
     */
    public Optional<String> getUserName() {
        return Optional.ofNullable(userName);
    }

    /**
     * Sets the token of this session
     *
     * @param token The new session token
     */
    public void setToken(@Nonnull String token) {
        Objects.requireNonNull(token, "Token must not be NULL or empty!");

        this.token = token;
    }

    /**
     * Returns the current session token
     *
     * @return Current API session token
     */
    public String getToken() {
        return token;
    }

    /**
     * Set the preferred language used for API communication. Search results will be based on this language.
     *
     * @param language The language for API communication
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Returns the language code currently set for this session
     *
     * @return The language used for API communication
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the current status of this session
     *
     * @param status The new session status
     */
    public void setStatus(Status status) { this.status = status; }
    /**
     * Returns the current {@link Status} of this session. This status indicates that...
     * <p/>
     * <p>{@link Status#NOT_AUTHORIZED}: The session has not yet been initialized. API communication is restricted to login/refresh requests.</p>
     * <p>{@link Status#AUTHORIZATION_IN_PROGRESS}: The initialization of this session is currently in progress</p>
     * <p>{@link Status#AUTHORIZED}: The session is initialized and ready for further API communication</p>
     *
     * @return The current status of this session
     */
    public Status getStatus() { return status; }

    /**
     * Check if this session has already been initialized
     *
     * @return {@link Boolean#TRUE} if the session is initialized or {@link Boolean#FALSE} if the session is not yet initialized.
     */
    public Boolean isInitialized() {
        return getStatus() == Status.AUTHORIZED;
    }

    /**
     * Checks whether user authentication is available or not. User authentication is optional and only required for specific API calls (USER*).
     *
     * @return {@link Boolean#TRUE} if both, userKey and userName are not empty or {@link Boolean#FALSE} if not.
     */
    public Boolean userAuthentication() {
        return APIUtil.hasValue(userKey) && APIUtil.hasValue(userName);
    }
}

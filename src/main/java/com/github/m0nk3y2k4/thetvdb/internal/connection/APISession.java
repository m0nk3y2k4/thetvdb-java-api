package com.github.m0nk3y2k4.thetvdb.internal.connection;

import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;

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

    public APISession(String apiKey) {
        this(apiKey, null, null);
    }

    public APISession(String apiKey, String userKey, String userName) {
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
    public String getUserKey() {
        return userKey;
    }

    /**
     * Returns the user name used for authentication. If set to <code>null</code> user authentication will be skipped.
     *
     * @return The user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the token of this session
     *
     * @param token The new session token
     */
    public void setToken(String token) {
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

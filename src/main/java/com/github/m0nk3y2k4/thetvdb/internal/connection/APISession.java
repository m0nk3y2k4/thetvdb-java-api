package com.github.m0nk3y2k4.thetvdb.internal.connection;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Session used for remote API communication.
 * <p><br>
 * All connections to the TheTVDB API are backed by an instance of this class. These sessions
 * contain all information required for client authentication on the remote service, locale settings as well as session tokens used for
 * remote service communication.
 */
public final class APISession {

    /** Pattern for JSON Web Token validation */
    private static final Pattern JWT_PATTERN = Pattern.compile("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$");

    /** JWT related error messages */
    private static final String ERR_JWT_EMPTY = "Remote API authorization failed: Token must not be NULL or empty";
    private static final String ERR_JWT_INVALID = "Remote API authorization failed: Invalid token format [%s]";

    /**
     * Represents the different states of a session.
     * <p><br>
     * By default, sessions are not authorized for general API communication. Only
     * login/refresh requests may be allowed. During the execution of these kind of requests, the session authorization is in progress.
     * In case the login/refresh was successful the session is authorized and ready for general API communication.
     */
    public enum Status {
        /** Session authorization is pending */
        NOT_AUTHORIZED,

        /** Session authorization is currently in progress */
        AUTHORIZATION_IN_PROGRESS,

        /** Session authorization completed successfully */
        AUTHORIZED
    }

    /** Accept english by default if no language was specified */
    private static final String DEFAULT_LANGUAGE = "en";

    /** The API key used to request a session token */
    private final String apiKey;

    /** Optional userKey for authentication */
    private final String userKey;

    /** Optional userName for authentication */
    private final String userName;

    /** The JWT token for this session, issued by the remote service */
    private volatile String token;

    /** The preferred language for API communication based on this session */
    private String language = DEFAULT_LANGUAGE;

    /** The current status of this session in terms of authorization */
    private Status status = Status.NOT_AUTHORIZED;

    /**
     * Creates a new API session with the given API key. The <em>{@code apiKey}</em> must be a valid <a href="https://www.thetvdb.com/member/api">TheTVDB API Key</a> which will
     * be used for remote service authentication.
     * <p><br>
     * <b>NOTE:</b> Sessions created with this constructor <u>can not</u> be used for calls to the remote API's <a href="https://api.thetvdb.com/swagger#/Users">/users</a>
     * routes. These calls require extended authentication using an additional <em>{@code userKey}</em> and <em>{@code userName}</em>.
     *
     * @see APISession#APISession(String, String, String) APISession(apiKey, userKey, userName)
     *
     * @param apiKey The API key used to request a session token
     */
    APISession(@Nonnull String apiKey) {
        Parameters.validateNotEmpty(apiKey, "API key must not be NULL or empty!");

        this.apiKey = apiKey;
        this.userKey = null;
        this.userName = null;
    }

    /**
     * Creates a new API session. The given <em>{@code apiKey}</em> must be a valid <a href="https://www.thetvdb.com/member/api">TheTVDB API Key</a>. The <em>{@code userKey}</em>
     * and <em>{@code userName}</em> must refer to a registered TheTVDB user account. The given parameters will be used for the initial remote service authentication. Sessions
     * created with this constructor can be used for calls to the remote API's <a href="https://api.thetvdb.com/swagger#/Users">/users</a> routes.
     *
     * @see APISession#APISession(String) APISession(apiKey)
     *
     * @param apiKey The API key used to request a session token
     * @param userKey User key for authentication (also referred to as "Unique ID")
     * @param userName User name for authentication
     */
    APISession(@Nonnull String apiKey, @Nonnull String userKey, @Nonnull String userName) {
        Parameters.validateNotEmpty(apiKey, "API key must not be NULL or empty!");
        Parameters.validateNotEmpty(userKey, "User key must not be NULL or empty!");
        Parameters.validateNotEmpty(userName, "User name must not be NULL or empty!");

        this.apiKey = apiKey;
        this.userKey = userKey;
        this.userName = userName;
    }

    /**
     * Returns the API key of this session
     *
     * @return API key of this session
     */
    String getApiKey() {
        return this.apiKey;
    }

    /**
     * Returns the user key used for authentication. If no value is present then user authentication will be skipped.
     *
     * @return Optional user key for this session
     */
    Optional<String> getUserKey() {
        return Optional.ofNullable(userKey);
    }

    /**
     * Returns the user name used for authentication. If no value is present then user authentication will be skipped.
     *
     * @return Optional user name for this session
     */
    Optional<String> getUserName() {
        return Optional.ofNullable(userName);
    }

    /**
     * Returns the current session token. Might be empty if the session has not yet been initialized.
     *
     * @return Current API session token or empty Optional if not yet authorized
     */
    Optional<String> getToken() {
        return Optional.ofNullable(token);
    }

    /**
     * Sets the JWT token for this session which will then be used further communication authentication
     *
     * @param token The new JWT session token issued by the remote service
     *
     * @throws APIException If the given token is <em>{@code null}</em>, an empty character sequence or does not match the regular JWT format
     */
    void setToken(@Nonnull String token) throws APIException {
        // Validate token - throws an exception if not a valid JWT
        validateJWT(token);

        this.token = token;
        this.status = Status.AUTHORIZED;
    }

    /**
     * Set the preferred language used for API communication. If available, search results will be returned in this language. If the given
     * language parameter is <em>{@code null}</em> the sessions language will be reset to {@link #DEFAULT_LANGUAGE}.
     *
     * @param language The preferred language of the data returned by the remote service
     */
    void setLanguage(String language) {
        this.language = language != null ? language : DEFAULT_LANGUAGE;
    }

    /**
     * Returns the language code currently set for this session
     *
     * @return The preferred language used for API communication
     */
    String getLanguage() {
        return language;
    }

    /**
     * Sets the current status of this session. If the given status parameter is <em>{@code null}</em> the sessions
     * status will be reset to {@link Status#NOT_AUTHORIZED}.
     *
     * @param status The new session status
     */
    void setStatus(Status status) {
        this.status = status != null ? status : Status.NOT_AUTHORIZED;
    }

    /**
     * Returns the current {@link Status} of this session. This status indicates that...
     * <ul>
     * <li>{@link Status#NOT_AUTHORIZED}: The session has not yet been initialized. API communication is restricted to login/refresh requests.</li>
     * <li>{@link Status#AUTHORIZATION_IN_PROGRESS}: The initialization of this session is currently in progress</li>
     * <li>{@link Status#AUTHORIZED}: The session is initialized and ready for further API communication</li>
     * </ul>
     *
     * @return The current status of this session
     */
    Status getStatus() { return status; }

    /**
     * Check if this session has already been initialized
     *
     * @return {@link Boolean#TRUE} if the session is initialized or {@link Boolean#FALSE} if the session is not yet initialized.
     */
    Boolean isInitialized() {
        return getStatus() == Status.AUTHORIZED;
    }

    /**
     * Checks whether user authentication is available or not. A distinct user authentication is optional and only required for specific API calls (USER*).
     *
     * @return {@link Boolean#TRUE} if both, userKey and userName are <b>not</b> empty or {@link Boolean#FALSE} if not.
     */
    Boolean userAuthentication() {
        return APIUtil.hasValue(userKey) && APIUtil.hasValue(userName);
    }

    /**
     * Checks if the given token is a valid JSON Web Token
     *
     * @param token The token to check
     *
     * @throws APIException If the given token is <em>{@code null}</em>, an empty character sequence or does not match the regular JWT format
     */
    private static void validateJWT(String token) throws APIException {
        if (APIUtil.hasNoValue(token)) {
            throw new APIException(ERR_JWT_EMPTY);
        }

        if (!JWT_PATTERN.matcher(token).matches()) {
            throw new APIException(String.format(ERR_JWT_INVALID, token));
        }
    }
}

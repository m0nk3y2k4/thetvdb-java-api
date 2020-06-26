package com.github.m0nk3y2k4.thetvdb.api.exception;

import javax.annotation.Nonnull;

/**
 * Main type for all checked exceptions related to the API functionality. This includes specific errors as defined by the remote service
 * as well as general problems like communication issues or malformed API responses.
 */
public class APIException extends Exception {

    /** Indicates that an attempt to communicate with some API route was made without proper authorization */
    public static final String API_NOT_AUTHORIZED_ERROR = "Missing authorization or expired JWT token (HTTP-401). Original API error message: %s";

    /** Indicates that the requested resource is unknown for the remote service (e.g. wrong seriesId) */
    public static final String API_NOT_FOUND_ERROR = "Requested resource could not be found (HTTP-404). Original API error message: %s";

    /** Indicates that a requested record could not be updated or deleted */
    public static final String API_CONFLICT_ERROR = "Update request caused a conflict (HTTP-409). Original API error message: %s";

    /** Indicates that the remote service is currently unavailable e.g. due to maintenance or server issues */
    public static final String API_SERVICE_UNAVAILABLE = "API Service is currently unavailable. Please try again later.";

    /** Indicates that a malformed JSON response was received from the remote service */
    public static final String API_JSON_PARSE_ERROR = "Error while parsing JSON from response";

    /**
     * Creates a new checked API exception with the given error message
     *
     * @param message Brief error message describing the general problem
     */
    public APIException(String message) {
        super(message);
    }

    /**
     * Creates a new checked API exception with an extended error message
     *
     * @param message Brief error message describing the general problem. This message may contain a single <i>%s</i> conversion which will be
     *                automatically replaced with the given <em>{@code details}</em> message text.
     * @param details A more detailed error description
     */
    public APIException(@Nonnull String message, @Nonnull String details) {
        super(String.format(message, details));
    }

    /**
     * Creates a nested checked API exception wrapping some other root exception
     *
     * @param message Brief error message describing the general problem
     * @param ex The original root exception
     */
    public APIException(String message, Throwable ex) {
        super(message, ex);
    }
}

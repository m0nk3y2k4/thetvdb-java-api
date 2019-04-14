package com.github.m0nk3y2k4.thetvdb.api.exception;

import javax.annotation.Nonnull;

public class APIException extends Exception {

    protected static final String API_NOT_AUTHORIZED_ERROR = "Missing authorization or expired JWT token (HTTP-401). Original API error message: %s";

    public static final String API_NOT_FOUND_ERROR = "Requested resource could not be found (HTTP-404). Original API error message: %s";
    public static final String API_CONFLICT_ERROR = "Update request caused a conflict (HTTP-409). Original API error message: %s";
    public static final String API_SERVICE_UNAVAILABLE = "API Service is currently unavailable. Please try again later.";
    public static final String API_JSON_PARSE_ERROR = "Error while parsing JSON from response";

    public APIException(String message) {
        super(message);
    }

    public APIException(@Nonnull String message, @Nonnull String details) {
        super(String.format(message, details));
    }

    public APIException(String message, Throwable ex) {
        super(message, ex);
    }
}

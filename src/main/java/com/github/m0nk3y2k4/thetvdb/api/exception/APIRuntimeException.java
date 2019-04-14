package com.github.m0nk3y2k4.thetvdb.api.exception;

import javax.annotation.Nonnull;

public class APIRuntimeException extends RuntimeException {

    protected static final String API_VALIDATION_ERROR = "Request could not be processed due to invalid/missing parameters: %s";

    public APIRuntimeException(String message) {
        super(message);
    }

    public APIRuntimeException(@Nonnull String message, @Nonnull String details) {
        super(String.format(message, details));
    }

    public APIRuntimeException(String message, Exception ex) {
        super(message, ex);
    }
}

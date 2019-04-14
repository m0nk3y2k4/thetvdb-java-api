package com.github.m0nk3y2k4.thetvdb.internal.exception;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIRuntimeException;

import javax.annotation.Nonnull;

public final class APIValidationException extends APIRuntimeException {

    public APIValidationException(@Nonnull String error) {
        super(API_VALIDATION_ERROR, error);
    }
}

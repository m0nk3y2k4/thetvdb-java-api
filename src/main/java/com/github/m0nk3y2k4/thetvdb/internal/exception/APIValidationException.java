package com.github.m0nk3y2k4.thetvdb.internal.exception;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;

import javax.annotation.Nonnull;

public final class APIValidationException extends APIException {

    public APIValidationException(@Nonnull String error) {
        super(API_VALIDATION_ERROR, error);
    }
}

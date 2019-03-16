package com.github.m0nk3y2k4.thetvdb.exception;

import javax.annotation.Nonnull;

public final class APIValidationException extends APIException {

    private static final long serialVersionUID = -3945888794316685512L;

    public APIValidationException(@Nonnull String error) {
        super(API_VALIDATION_ERROR, error);
    }
}

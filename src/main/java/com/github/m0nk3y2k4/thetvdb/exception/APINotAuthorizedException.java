package com.github.m0nk3y2k4.thetvdb.exception;

import javax.annotation.Nonnull;

public final class APINotAuthorizedException extends APIException {

    private static final long serialVersionUID = -4671220715589081295L;

    public APINotAuthorizedException(@Nonnull String error) {
        super(API_NOT_AUTHORIZED_ERROR, error);
    }
}

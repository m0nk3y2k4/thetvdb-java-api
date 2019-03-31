package com.github.m0nk3y2k4.thetvdb.internal.exception;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;

import javax.annotation.Nonnull;

public final class APINotAuthorizedException extends APIException {

    public APINotAuthorizedException(@Nonnull String error) {
        super(API_NOT_AUTHORIZED_ERROR, error);
    }
}

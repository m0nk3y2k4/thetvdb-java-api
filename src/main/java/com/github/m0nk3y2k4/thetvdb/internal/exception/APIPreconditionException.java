package com.github.m0nk3y2k4.thetvdb.internal.exception;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIRuntimeException;

import javax.annotation.Nonnull;

public class APIPreconditionException extends APIRuntimeException {

    public APIPreconditionException(@Nonnull String error) {
        super(API_PRECONDITION_ERROR, error);
    }
}

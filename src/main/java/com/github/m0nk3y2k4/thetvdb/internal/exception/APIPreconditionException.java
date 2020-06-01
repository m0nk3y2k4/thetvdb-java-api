package com.github.m0nk3y2k4.thetvdb.internal.exception;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIRuntimeException;

import javax.annotation.Nonnull;

/**
 * Exceptions of this type represent a failed precondition check. Such checks are carried out throughout processing to check
 * whether all the necessary prerequisites for proper processing of the task at hand have been met. Such exceptions typically
 * indicate an implementation error rather than incorrect use of the API.
 */
public class APIPreconditionException extends APIRuntimeException {

    /**
     * Creates a new API precondition exception with the given error message.
     *
     * @param error Brief error message describing the unmet precondition. Will be appended to some basic exception specific error text.
     */
    public APIPreconditionException(@Nonnull String error) {
        super(API_PRECONDITION_ERROR, error);
    }
}

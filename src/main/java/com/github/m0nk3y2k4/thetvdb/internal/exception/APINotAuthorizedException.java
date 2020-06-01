package com.github.m0nk3y2k4.thetvdb.internal.exception;

import javax.annotation.Nonnull;

/**
 * Specific type of {@link APICommunicationException} which handles HTTP-401 responses received from the remote API. Such response
 * may be returned by the remote service in case of requesting a resource using an uninitialized session, which lacks proper authentication.
 */
public final class APINotAuthorizedException extends APICommunicationException {

    /**
     * Creates a new API missing authorization exception with the given error message.
     *
     * @param error Brief error message describing the authorization issue. Will be appended to some basic exception specific error text.
     */
    public APINotAuthorizedException(@Nonnull String error) {
        super(API_NOT_AUTHORIZED_ERROR, error);
    }
}

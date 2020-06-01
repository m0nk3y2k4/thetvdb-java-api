package com.github.m0nk3y2k4.thetvdb.internal.exception;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;

import javax.annotation.Nonnull;

/**
 * Exception indicating problems while communicating with the remote API. This includes general IO issues as well as checked communication
 * level errors returned by the API as declared in the interface description.
 * <p><br>
 * The only checked response error that is <b>not</b> covered by this exception is HTTP-401 which is handled by a more distinctive exception
 * type: {@link APINotAuthorizedException}
 */
public class APICommunicationException extends APIException {

    /**
     * Creates a new API communication exception with the given error message
     *
     * @param message Brief error message describing the problem
     */
    public APICommunicationException(String message) {
        super(message);
    }

    /**
     * Creates a new API communication exception with an extended error message
     *
     * @param message Brief error message describing the problem. This message may contain a single <i>%s</i> conversion which will be
     *                automatically replaced with the given <em>{@code details}</em> message text.
     * @param details A more detailed error description
     */
    public APICommunicationException(@Nonnull String message, @Nonnull String details) {
        super(message, details);
    }

    /**
     * Creates a nested API communication exception wrapping some other root exception
     *
     * @param message Brief error message describing the problem
     * @param ex The original root exception
     */
    public APICommunicationException(String message, Throwable ex) {
        super(message, ex);
    }
}

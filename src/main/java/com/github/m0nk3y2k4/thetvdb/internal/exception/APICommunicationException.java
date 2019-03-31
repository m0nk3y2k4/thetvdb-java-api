package com.github.m0nk3y2k4.thetvdb.internal.exception;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;

public final class APICommunicationException extends APIException {

    public APICommunicationException(String message) {
        super(message);
    }

    public APICommunicationException(String message, Throwable ex) {
        super(message, ex);
    }
}

package com.github.m0nk3y2k4.thetvdb.exception;

public final class APICommunicationException extends APIException {

    private static final long serialVersionUID = 138391498917155704L;

    public APICommunicationException(String message) {
        super(message);
    }

    public APICommunicationException(String message, Throwable ex) {
        super(message, ex);
    }
}

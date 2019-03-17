package com.github.m0nk3y2k4.thetvdb.api.exception;

public class APIRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -1929651549448587150L;

    public APIRuntimeException(String message, Exception ex) {
        super(message, ex);
    }
}

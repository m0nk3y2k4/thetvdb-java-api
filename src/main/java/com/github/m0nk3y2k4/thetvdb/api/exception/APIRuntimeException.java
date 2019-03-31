package com.github.m0nk3y2k4.thetvdb.api.exception;

public class APIRuntimeException extends RuntimeException {

    public APIRuntimeException(String message, Exception ex) {
        super(message, ex);
    }
}

package com.github.m0nk3y2k4.thetvdb.api.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class APIRuntimeExceptionTest {

    @Test
    void newAPIRuntimeException_withSimpleMessage_verifyProperties() {
        final String message = "Some simple exception message";
        APIRuntimeException exception = new APIRuntimeException(message);
        assertThat(exception).hasMessage(message);
    }

    @Test
    void newAPIRuntimeException_withDetailedMessage_verifyProperties() {
        final String message = "Detailed exception message: %s";
        final String details = "Some details";
        APIRuntimeException exception = new APIRuntimeException(message, details);
        assertThat(exception).hasMessage(message, details);
    }

    @Test
    void newAPIRuntimeException_withWrappedException_verifyProperties() {
        final String message = "Some wrapped exception";
        final Exception cause = new Exception();
        APIRuntimeException exception = new APIRuntimeException(message, cause);
        assertThat(exception).hasMessage(message).hasCause(cause);
    }

    @Test
    void verifyIsRuntimeException() {
        APIRuntimeException exception = new APIRuntimeException("Message");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
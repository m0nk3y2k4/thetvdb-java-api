package com.github.m0nk3y2k4.thetvdb.api.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class APIExceptionTest {

    @Test
    void newAPIException_withSimpleMessage_verifyProperties() {
        final String message = "Some simple exception message";
        APIException exception = new APIException(message);
        assertThat(exception).hasMessage(message);
    }

    @Test
    void newAPIException_withDetailedMessage_verifyProperties() {
        final String message = "Detailed exception message: %s";
        final String deatils = "Some details";
        APIException exception = new APIException(message, deatils);
        assertThat(exception).hasMessage(message, deatils);
    }

    @Test
    void newAPIException_withWrappedException_verifyProperties() {
        final String message = "Some wrapped exception";
        final Exception cause = new Exception();
        APIException exception = new APIException(message, cause);
        assertThat(exception).hasMessage(message);
        assertThat(exception).hasCause(cause);
    }

    @Test
    void verifyIsCheckedException() {
        APIException exception = new APIException("Message");
        assertThat(exception).isInstanceOf(Exception.class);
    }
}
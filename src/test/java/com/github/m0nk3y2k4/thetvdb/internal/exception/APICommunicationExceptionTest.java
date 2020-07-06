package com.github.m0nk3y2k4.thetvdb.internal.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class APICommunicationExceptionTest {

    @Test
    void newAPICommunicationException_withSimpleMessage_verifyProperties() {
        final String message = "Some simple exception message";
        APICommunicationException exception = new APICommunicationException(message);
        assertThat(exception).hasMessage(message);
    }

    @Test
    void newAPICommunicationException_withDetailedMessage_verifyProperties() {
        final String message = "Detailed exception message: %s";
        final String deatils = "Some details";
        APICommunicationException exception = new APICommunicationException(message, deatils);
        assertThat(exception).hasMessage(message, deatils);
    }

    @Test
    void newAPICommunicationException_withWrappedException_verifyProperties() {
        final String message = "Some wrapped exception";
        final Exception cause = new Exception();
        APICommunicationException exception = new APICommunicationException(message, cause);
        assertThat(exception).hasMessage(message).hasCause(cause);
    }

    @Test
    void verifyIsCheckedException() {
        APICommunicationException exception = new APICommunicationException("Message");
        assertThat(exception).isInstanceOf(Exception.class);
    }
}
package com.github.m0nk3y2k4.thetvdb.internal.exception;

import static com.github.m0nk3y2k4.thetvdb.api.exception.APIRuntimeException.API_PRECONDITION_ERROR;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class APIPreconditionExceptionTest {

    @Test
    void newAPIPreconditionException_withSimpleMessage_verifyProperties() {
        final String message = "XYZ is not set";
        APIPreconditionException exception = new APIPreconditionException(message);
        assertThat(exception).hasMessageContaining(API_PRECONDITION_ERROR, message);
    }

    @Test
    void verifyIsRuntimeException() {
        APIPreconditionException exception = new APIPreconditionException("Message");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
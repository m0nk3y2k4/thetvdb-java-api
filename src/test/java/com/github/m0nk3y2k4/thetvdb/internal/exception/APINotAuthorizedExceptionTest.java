package com.github.m0nk3y2k4.thetvdb.internal.exception;

import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_NOT_AUTHORIZED_ERROR;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class APINotAuthorizedExceptionTest {

    @Test
    void newAPINotAuthorizedException_withSimpleMessage_verifyProperties() {
        final String message = "Some authorization error";
        APINotAuthorizedException exception = new APINotAuthorizedException(message);
        assertThat(exception).hasMessage(API_NOT_AUTHORIZED_ERROR, message);
    }

    @Test
    void verifyIsCheckedException() {
        APINotAuthorizedException exception = new APINotAuthorizedException("Message");
        assertThat(exception).isInstanceOf(Exception.class);
    }
}
package com.github.m0nk3y2k4.thetvdb.internal.util.validation;

import static com.github.m0nk3y2k4.thetvdb.api.exception.APIRuntimeException.API_PRECONDITION_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.github.m0nk3y2k4.thetvdb.internal.exception.APIPreconditionException;
import org.junit.jupiter.api.Test;

class PreconditionsTest {

    @Test
    void requires_conditionMatched_noExceptionThrown() {
        assertDoesNotThrow(() -> Preconditions.requires(age -> age > 18, 42, new APIPreconditionException("Should not be thrown")));
    }

    @Test
    void requires_conditionNotMatched_exceptionRethrown() {
        final APIPreconditionException exception = new APIPreconditionException("Not old enough!");
        APIPreconditionException thrown = catchThrowableOfType(() -> Preconditions.requires(age -> age > 18, 14, exception),
                APIPreconditionException.class);
        assertThat(thrown).isEqualTo(exception);
    }

    @Test
    void requireNonNull_parameterIsNotNull_noExceptionThrown() {
        assertDoesNotThrow(() -> Preconditions.requireNonNull("   ", "Keep it to yourself"));
    }

    @Test
    void requireNonNull_parameterIsNull_exceptionThrown() {
        final String message = "I said no null-values!";
        APIPreconditionException thrown = catchThrowableOfType(() -> Preconditions.requireNonNull(null, message),
                APIPreconditionException.class);
        assertThat(thrown).hasMessage(API_PRECONDITION_ERROR, message);
    }

    @Test
    void requireNonEmpty_parameterIsNotEmpty_noExceptionThrown() {
        assertDoesNotThrow(() -> Preconditions.requireNonEmpty("Neither null nor empty", "Never thrown..."));
    }

    @Test
    void requireNonEmpty_parameterIsNull_exceptionThrown() {
        final String message = "Grrr...";
        APIPreconditionException thrown = catchThrowableOfType(() -> Preconditions.requireNonEmpty(null, message),
                APIPreconditionException.class);
        assertThat(thrown).hasMessage(API_PRECONDITION_ERROR, message);
    }

    @Test
    void requireNonEmpty_parameterIsEmpty_exceptionThrown() {
        final String message = "No empty values, please";
        APIPreconditionException thrown = catchThrowableOfType(() -> Preconditions.requireNonEmpty("      ", message),
                APIPreconditionException.class);
        assertThat(thrown).hasMessage(API_PRECONDITION_ERROR, message);
    }
}
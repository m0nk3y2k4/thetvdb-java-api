package com.github.m0nk3y2k4.thetvdb.internal.util.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.QueryParametersImpl;
import org.junit.jupiter.api.Test;

class ParametersTest {

    @Test
    void validateCondition_conditionMatched_noExceptionThrown() {
        assertDoesNotThrow(() -> Parameters.validateCondition(Objects::nonNull, "I am not null!",
                new IllegalArgumentException("Should not be thrown")));
    }

    @Test
    void validateCondition_conditionNotMatched_exceptionRethrown() {
        final IllegalArgumentException exception = new IllegalArgumentException("Should not be null!");
        IllegalArgumentException thrown = catchThrowableOfType(() -> Parameters.validateCondition(Objects::nonNull, null, exception),
                IllegalArgumentException.class);
        assertThat(thrown).isEqualTo(exception);
    }

    @Test
    void validateNotNull_parameterIsNotNull_noExceptionThrown() {
        assertDoesNotThrow(() -> Parameters.validateNotNull(7, "Error in case of null-value"));
    }

    @Test
    void validateNotNull_parameterIsNull_exceptionThrown() {
        final String validationFailedMessage = "Value must not be null!";
        IllegalArgumentException exception = catchThrowableOfType(() -> Parameters.validateNotNull(null, validationFailedMessage),
                IllegalArgumentException.class);
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage(validationFailedMessage);
    }

    @Test
    void validateNotEmpty_withNonEmptyString_noExceptionThrown() {
        assertDoesNotThrow(() -> Parameters.validateNotEmpty("Not empty!", "Error in case of empty string"));
    }

    @Test
    void validateNotEmpty_withNullString_exceptionThrown() {
        final String validationFailedMessage = "String must not be null!";
        IllegalArgumentException exception = catchThrowableOfType(() -> Parameters.validateNotEmpty((String)null, validationFailedMessage),
                IllegalArgumentException.class);
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage(validationFailedMessage);
    }

    @Test
    void validateNotEmpty_withEmptyString_exceptionThrown() {
        final String validationFailedMessage = "String must not be empty!";
        IllegalArgumentException exception = catchThrowableOfType(() -> Parameters.validateNotEmpty("   ", validationFailedMessage),
                IllegalArgumentException.class);
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage(validationFailedMessage);
    }

    @Test
    void validateNotEmpty_withNonEmptyOptional_noExceptionThrown() {
        assertDoesNotThrow(() -> Parameters.validateNotEmpty(Optional.of("Some String"), "Error in case of empty Optional"));
    }

    @Test
    void validateNotEmpty_withEmptyOptional_exceptionThrown() {
        final String validationFailedMessage = "Optional must not be empty!";
        IllegalArgumentException exception = catchThrowableOfType(() -> Parameters.validateNotEmpty(Optional.empty(), validationFailedMessage),
                IllegalArgumentException.class);
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage(validationFailedMessage);
    }

    @Test
    void validateNotEmpty_withOptionalContainingNullString_exceptionThrown() {
        final String validationFailedMessage = "String must not be null!";
        IllegalArgumentException exception = catchThrowableOfType(() -> Parameters.validateNotEmpty(Optional.ofNullable(null), validationFailedMessage),
                IllegalArgumentException.class);
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage(validationFailedMessage);
    }

    @Test
    void validateNotEmpty_withOptionalContainingEmptyString_exceptionThrown() {
        final String validationFailedMessage = "String must not be empty!";
        IllegalArgumentException exception = catchThrowableOfType(() -> Parameters.validateNotEmpty(Optional.of(" "), validationFailedMessage),
                IllegalArgumentException.class);
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage(validationFailedMessage);
    }

    @Test
    void validatePathParam_withValidPathParameter_noExceptionThrown() {
        assertDoesNotThrow(() -> Parameters.validatePathParam("ValidPathParam", "en", p -> p.length() <= 2));
    }

    @Test
    void validatePathParam_withNullPathParameter_exceptionThrown() {
        final String paramName = "NullPathParam";
        IllegalArgumentException exception = catchThrowableOfType(() -> Parameters.validatePathParam(paramName, null, x -> true),
                IllegalArgumentException.class);
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("[%s] is required", paramName);
    }

    @Test
    void validatePathParam_withInvalidPathParameter_exceptionThrown() {
        final String paramName = "InvalidPathParam";
        IllegalArgumentException exception = catchThrowableOfType(() -> Parameters.validatePathParam(paramName, 12, x -> x < 10),
                IllegalArgumentException.class);
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("[%s] is set to an invalid value", paramName);
    }

    @Test
    void validateQueryParam_withValidQueryParameter_noExceptionThrown() {
        final String queryParamName = "department";
        final QueryParameters queryParameters = new QueryParametersImpl(Map.of(queryParamName, "Sales"));
        assertDoesNotThrow(() -> Parameters.validateQueryParam(queryParamName, queryParameters));
    }

    @Test
    void validateQueryParam_withMissingMandatoryQueryParameter_exceptionThrown() {
        final String queryParamName = "company";
        final QueryParameters queryParameters = new QueryParametersImpl(Map.of("city", "Bespin"));
        IllegalArgumentException exception = catchThrowableOfType(() -> Parameters.validateQueryParam(queryParamName, queryParameters),
                IllegalArgumentException.class);
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("[%s] is required but is not set", queryParamName);
    }

    @Test
    void validateQueryParam_withMandatoryQueryParameterSetToNull_exceptionThrown() {
        final String queryParamName = "region";
        final QueryParameters queryParameters = new QueryParametersWithDisabledValueChecks(queryParamName, null);
        IllegalArgumentException exception = catchThrowableOfType(() -> Parameters.validateQueryParam(queryParamName, queryParameters),
                IllegalArgumentException.class);
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("[%s] must not be empty", queryParamName);
    }

    @Test
    void validateQueryParam_withMandatoryQueryParameterBeingEmpty_exceptionThrown() {
        final String queryParamName = "gender";
        final QueryParameters queryParameters = new QueryParametersWithDisabledValueChecks(queryParamName, "        ");
        IllegalArgumentException exception = catchThrowableOfType(() -> Parameters.validateQueryParam(queryParamName, queryParameters),
                IllegalArgumentException.class);
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("[%s] must not be empty", queryParamName);
    }

    @Test
    void validateQueryParam_withInvalidQueryParameter_exceptionThrown() {
        final String queryParamName = "languageCode";
        final String queryParamValue = "french";
        IllegalArgumentException exception = catchThrowableOfType(() -> Parameters.validateQueryParam(queryParamName,
                new QueryParametersImpl(Map.of(queryParamName, queryParamValue)), code -> code.length() <= 2), IllegalArgumentException.class);
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[%s] is set to an invalid value: %s", queryParamName, queryParamValue);
    }

    /**
     * Special implementation of {@link QueryParameters} allowing empty/null parameter values. Needed to test
     * the corresponding validation methods.
     */
    private static class QueryParametersWithDisabledValueChecks extends QueryParametersImpl {

        protected QueryParametersWithDisabledValueChecks(@Nonnull String key, @CheckForNull String value) {
            this.addParameter(key, value);
        }

        @Override
        public QueryParameters addParameter(@Nonnull String key, @CheckForNull String value) {
            Parameters.validateNotNull(key, "Parameter key must not be NULL");
            params.put(key, value);
            return this;
        }
    }
}
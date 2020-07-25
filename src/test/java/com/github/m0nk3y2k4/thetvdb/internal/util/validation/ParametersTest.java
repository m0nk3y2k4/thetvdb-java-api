package com.github.m0nk3y2k4.thetvdb.internal.util.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.QueryParametersImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ParametersTest {

    private static Stream<Arguments> validateCondition_withConditionNotMatched_exceptionRethrown() {
        return Stream.of(
                Arguments.of((Predicate<Integer>)age -> age > 21, 19, new IllegalArgumentException("No booze for you!")),
                Arguments.of((Predicate<Object>)Objects::nonNull, null, new IllegalArgumentException("Should not be null!"))
        );
    }

    private static Stream<Optional<?>> validateNotEmptyOptional_withInvalidOptional_exceptionThrown() {
        return Stream.of(Optional.empty(), Optional.ofNullable(null), Optional.of(" "));
    }

    @Test
    void validateCondition_happyDay() {
        assertDoesNotThrow(() -> Parameters.validateCondition(Objects::nonNull, "I am not null!", new IllegalArgumentException("Should not be thrown")));
    }

    @ParameterizedTest(name = "[{index}] Value \"{1}\" is invalid as it does not match the condition")
    @MethodSource
    <T> void validateCondition_withConditionNotMatched_exceptionRethrown(Predicate<T> predicate, T value, RuntimeException exception) {
        IllegalArgumentException thrown = catchThrowableOfType(() -> Parameters.validateCondition(predicate, value, exception), IllegalArgumentException.class);
        assertThat(thrown).isEqualTo(exception);
    }

    @Test
    void validateNotNull_happyDay() {
        assertDoesNotThrow(() -> Parameters.validateNotNull(7, "Error in case of null-value"));
    }

    @ParameterizedTest(name = "[{index}] String \"{0}\" is null")
    @NullSource
    void validateNotNull_withNullValue_exceptionThrown(String obj) {
        final String validationFailedMessage = "Value must not be null!";
        IllegalArgumentException exception = catchThrowableOfType(() -> Parameters.validateNotNull(obj, validationFailedMessage), IllegalArgumentException.class);
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage(validationFailedMessage);
    }

    @Test
    void validateNotEmptyString_happyDay() {
        assertDoesNotThrow(() -> Parameters.validateNotEmpty("Not empty!", "Error in case of empty string"));
    }

    @ParameterizedTest(name = "[{index}] String \"{0}\" is null or empty")
    @NullAndEmptySource @ValueSource(strings = {"      "})
    void validateNotEmptyString_withNullOrEmptyString_exceptionThrown(String obj) {
        final String validationFailedMessage = "String is null or empty!";
        IllegalArgumentException exception = catchThrowableOfType(() -> Parameters.validateNotEmpty(obj, validationFailedMessage),
                IllegalArgumentException.class);
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage(validationFailedMessage);
    }

    @Test
    void validateNotEmptyOptional_happyDay() {
        assertDoesNotThrow(() -> Parameters.validateNotEmpty(Optional.of("Some String"), "Error in case of empty Optional"));
    }

    @ParameterizedTest(name = "[{index}] \"{0}\" contains null or empty value")
    @MethodSource
    void validateNotEmptyOptional_withInvalidOptional_exceptionThrown(Optional<String> obj) {
        final String validationFailedMessage = "Optional is null or empty!";
        IllegalArgumentException exception = catchThrowableOfType(() -> Parameters.validateNotEmpty(obj, validationFailedMessage),
                IllegalArgumentException.class);
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage(validationFailedMessage);
    }

    @Test
    void validatePathParam_happyDay() {
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
    void validateQueryParam_happyDay() {
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

    @ParameterizedTest(name = "[{index}] Parameter \"{0}\" is null or empty")
    @NullAndEmptySource @ValueSource(strings = {"   "})
    void validateQueryParam_withInvalidMandatoryQueryParameter_exceptionThrown(String queryParamNameValue) {
        final String queryParamName = "region";
        final QueryParameters queryParameters = new QueryParametersWithDisabledValueChecks(queryParamName, queryParamNameValue);
        IllegalArgumentException exception = catchThrowableOfType(() -> Parameters.validateQueryParam(queryParamName, queryParameters),
                IllegalArgumentException.class);
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("[%s] must not be empty", queryParamName);
    }

    @Test
    void validateQueryParam_withInvalidQueryParameter_exceptionThrown() {
        final String queryParamName = "languageCode";
        final String queryParamValue = "french";
        final QueryParameters params = new QueryParametersImpl(Map.of(queryParamName, queryParamValue));
        IllegalArgumentException exception = catchThrowableOfType(() -> Parameters.validateQueryParam(queryParamName, params,
                code -> code.length() <= 2), IllegalArgumentException.class);
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
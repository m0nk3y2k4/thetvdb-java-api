/*
 * Copyright (C) 2019 - 2021 thetvdb-java-api Authors and Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.m0nk3y2k4.thetvdb.internal.util.validation;

import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.api.APIKey;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.FundingModel;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.QueryParametersImpl;
import com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil;
import com.github.m0nk3y2k4.thetvdb.testutils.parameterized.NullAndEmptyStringSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings({"ConstantConditions", "ObviousNullCheck"})
class ParametersTest {

    private static Stream<Arguments> validateCondition_withConditionNotMatched_exceptionRethrown() {
        return Stream.of(
                Arguments.of((Predicate<Integer>)age -> age > 21, 19,
                        new IllegalArgumentException("No booze for you!")),
                Arguments.of((Predicate<Object>)Objects::nonNull, null,
                        new IllegalArgumentException("Should not be null!"))
        );
    }

    private static Stream<Optional<?>> validateNotEmptyOptional_withInvalidOptional_exceptionThrown() {
        return Stream.of(Optional.empty(), Optional.ofNullable(null), Optional.of(" "));
    }

    @Test
    void validateCondition_happyDay() {
        assertDoesNotThrow(() -> Parameters
                .validateCondition(Objects::nonNull, "I am not null!", new IllegalArgumentException("Should not be thrown")));
    }

    @ParameterizedTest(name = "[{index}] Value \"{1}\" is invalid as it does not match the condition")
    @MethodSource
    <T> void validateCondition_withConditionNotMatched_exceptionRethrown(Predicate<T> predicate, T value,
            RuntimeException exception) {
        assertThatIllegalArgumentException().isThrownBy(() -> Parameters.validateCondition(predicate, value, exception))
                .isSameAs(exception);
    }

    @Test
    void validateNotNull_happyDay() {
        assertDoesNotThrow(() -> Parameters.validateNotNull(7, "Error in case of null-value"));
    }

    @Test
    void validateNotNull_withNullValue_exceptionThrown() {
        final String validationFailedMessage = "Value must not be null!";
        assertThatIllegalArgumentException().isThrownBy(() -> Parameters.validateNotNull(null, validationFailedMessage))
                .withMessage(validationFailedMessage);
    }

    @Test
    void validateNotEmptyString_happyDay() {
        assertDoesNotThrow(() -> Parameters.validateNotEmpty("Not empty!", "Error in case of empty string"));
    }

    @ParameterizedTest(name = "[{index}] String \"{0}\" is null or empty")
    @NullAndEmptyStringSource
    void validateNotEmptyString_withNullOrEmptyString_exceptionThrown(String obj) {
        final String validationFailedMessage = "String is null or empty!";
        assertThatIllegalArgumentException().isThrownBy(() -> Parameters.validateNotEmpty(obj, validationFailedMessage))
                .withMessage(validationFailedMessage);
    }

    @Test
    void validateNotEmptyOptional_happyDay() {
        assertDoesNotThrow(() -> Parameters
                .validateNotEmpty(Optional.of("Some String"), "Error in case of empty Optional"));
    }

    @ParameterizedTest(name = "[{index}] \"{0}\" contains null or empty value")
    @MethodSource
    void validateNotEmptyOptional_withInvalidOptional_exceptionThrown(Optional<String> obj) {
        final String validationFailedMessage = "Optional is null or empty!";
        assertThatIllegalArgumentException().isThrownBy(() -> Parameters.validateNotEmpty(obj, validationFailedMessage))
                .withMessage(validationFailedMessage);
    }

    @Test
    void validatePathParam_happyDay() {
        assertDoesNotThrow(() -> Parameters.validatePathParam("ValidPathParam", "en", p -> p.length() <= 2));
    }

    @Test
    void validatePathParam_withNullPathParameter_exceptionThrown() {
        final String paramName = "NullPathParam";
        assertThatIllegalArgumentException().isThrownBy(() -> Parameters.validatePathParam(paramName, null, x -> true))
                .withMessageContaining("[%s] is required", paramName);
    }

    @Test
    void validatePathParam_withInvalidPathParameter_exceptionThrown() {
        final String paramName = "InvalidPathParam";
        assertThatIllegalArgumentException().isThrownBy(() -> Parameters.validatePathParam(paramName, 12, x -> x < 10))
                .withMessageContaining("[%s] is set to an invalid value", paramName);
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
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Parameters.validateQueryParam(queryParamName, queryParameters))
                .withMessageContaining("[%s] is required but is not set", queryParamName);
    }

    @ParameterizedTest(name = "[{index}] Parameter \"{0}\" is null or empty")
    @NullAndEmptyStringSource
    void validateQueryParam_withInvalidMandatoryQueryParameter_exceptionThrown(String queryParamNameValue) {
        final String queryParamName = "region";
        final QueryParameters queryParameters = new QueryParametersWithDisabledValueChecks()
                .addParameter(queryParamName, queryParamNameValue);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Parameters.validateQueryParam(queryParamName, queryParameters))
                .withMessageContaining("[%s] must not be empty", queryParamName);
    }

    @Test
    void validateQueryParam_withInvalidQueryParameter_exceptionThrown() {
        final String queryParamName = "languageCode";
        final String queryParamValue = "french";
        final QueryParameters params = new QueryParametersImpl(Map.of(queryParamName, queryParamValue));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Parameters.validateQueryParam(queryParamName, params, code -> code.length() <= 2))
                .withMessageContaining("[%s] is set to an invalid value: %s", queryParamName, queryParamValue);
    }

    @ParameterizedTest(name = "[{index}] \"{0}\" is a positive numerical integer")
    @ValueSource(strings = {"3", "104"})
    void isPositiveInteger_withPositiveIntegerValues_returnsFalse(String value) {
        assertThat(Parameters.isPositiveInteger().test(value)).isTrue();
    }

    @ParameterizedTest(name = "[{index}] \"{0}\" is not a positive numerical integer")
    @NullSource
    @ValueSource(strings = {"", "  ", "NaN", "25.3", "-7", "0", "3 "})
    void isPositiveInteger_withNonPositiveIntegerValues_returnsFalse(String value) {
        assertThat(Parameters.isPositiveInteger().test(value)).isFalse();
    }

    @Test
    void validateApiKey_happyDay() {
        assertDoesNotThrow(() -> Parameters.validateApiKey(CONTRACT_APIKEY));
    }

    @ParameterizedTest(name = "[{index}] Cannot create API key for \"{0}\"")
    @NullAndEmptyStringSource
    void validateApiKey_withInvalidApiKeyProperty_exceptionThrown(String apiKey) {
        APIKey key = APITestUtil.apiKey(apiKey, "PIN", FundingModel.CONTRACT);
        assertThatIllegalArgumentException().isThrownBy(() -> Parameters.validateApiKey(key))
                .withMessageContaining("Invalid key: the apiKey property must not be empty");
    }

    @ParameterizedTest(name = "[{index}] Cannot create user subscription API key with pin \"{0}\"")
    @NullAndEmptyStringSource
    void validate_withInvalidPinProperty_exceptionIsThrown(String pin) {
        APIKey key = APITestUtil.apiKey("GGS78FJSD5R7", pin, FundingModel.SUBSCRIPTION);
        assertThatIllegalArgumentException().isThrownBy(() -> Parameters.validateApiKey(key))
                .withMessageContaining("Invalid key: for user subscription based authentication a PIN is required");
    }

    /**
     * Special implementation of {@link QueryParameters} allowing empty/null parameter values. Needed to test the
     * corresponding validation methods.
     */
    private static class QueryParametersWithDisabledValueChecks extends QueryParametersImpl {

        @Override
        public QueryParameters addParameter(@Nonnull String key, @CheckForNull String value) {
            Parameters.validateNotNull(key, "Parameter key must not be NULL");
            params.put(key, value);
            return this;
        }
    }
}

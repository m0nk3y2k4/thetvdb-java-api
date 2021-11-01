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
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Collection;
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
import com.github.m0nk3y2k4.thetvdb.testutils.parameterized.NullAndEmptyCollectionSource;
import com.github.m0nk3y2k4.thetvdb.testutils.parameterized.NullAndEmptyStringSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings({"ConstantConditions", "ObviousNullCheck"})
class ParametersTest {

    private static Stream<Arguments> violatedConditions() {
        return Stream.of(
                Arguments.of((Predicate<Integer>)age -> age > 21, 19,
                        new IllegalArgumentException("No booze for you!")),
                Arguments.of((Predicate<Object>)Objects::nonNull, null,
                        new IllegalArgumentException("Should not be null!"))
        );
    }

    private static Stream<Optional<?>> emptyOptionals() {
        return Stream.of(Optional.empty(), Optional.ofNullable(null), Optional.of(" "));
    }

    private static Stream<Arguments> validQueryParameterCombinations() {
        return Stream.of(
                Arguments.of(Optional.of("Name1"), Optional.empty()),
                Arguments.of(Optional.empty(), Optional.of("Name2")),
                Arguments.of(Optional.of("Name1"), Optional.of("Name2"))
        );
    }

    private static Stream<Arguments> validDateStrings() {
        return Stream.of(
                Arguments.of("2017-11-06", "yyyy-MM-dd"),
                Arguments.of("10/19/21", "M/d/yyyy"),
                Arguments.of("16.4.19", "dd.MM.yyyy")
        );
    }

    @Test
    void validateCondition_happyDay() {
        assertDoesNotThrow(() -> Parameters
                .validateCondition(Objects::nonNull, "I am not null!", new IllegalArgumentException("Should not be thrown")));
    }

    @ParameterizedTest(name = "[{index}] Value \"{1}\" is invalid as it does not match the condition")
    @MethodSource("violatedConditions")
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
    void validateNotEmptyCollection_happyDay() {
        assertDoesNotThrow(() -> Parameters.validateNotEmpty(singleton("Non-empty collection"), "Never thrown..."));
    }

    @ParameterizedTest(name = "[{index}] Collection \"{0}\" is null or empty")
    @NullAndEmptyCollectionSource
    void validateNotEmptyCollection_withNullOrEmptyCollection_exceptionThrown(Collection<?> obj) {
        final String validationFailedMessage = "Collection is null or empty";
        assertThatIllegalArgumentException().isThrownBy(() -> Parameters.validateNotEmpty(obj, validationFailedMessage))
                .withMessage(validationFailedMessage);
    }

    @Test
    void validateNotEmptyOptional_happyDay() {
        assertDoesNotThrow(() -> Parameters
                .validateNotEmpty(Optional.of("Some String"), "Error in case of empty Optional"));
    }

    @ParameterizedTest(name = "[{index}] \"{0}\" contains null or empty value")
    @MethodSource("emptyOptionals")
    void validateNotEmptyOptional_withInvalidOptional_exceptionThrown(Optional<String> obj) {
        final String validationFailedMessage = "Optional is null or empty!";
        assertThatIllegalArgumentException().isThrownBy(() -> Parameters.validateNotEmpty(obj, validationFailedMessage))
                .withMessage(validationFailedMessage);
    }

    @ParameterizedTest(name = "[{index}] \"{0}\" is not a negative integer")
    @ValueSource(longs = {0, 3})
    void validateNotNegative_happyDay(long value) {
        assertDoesNotThrow(() -> Parameters.validateNotNegative(value, "Error in case of negative integer"));
    }

    @Test
    void validateNotNegative_withNegativeValue_exceptionThrown() {
        final String validationFailedMessage = "Value is a negative integer!";
        assertThatIllegalArgumentException().isThrownBy(() -> Parameters.validateNotNegative(-1, validationFailedMessage))
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

    @ParameterizedTest(name = "[{index}] With parameters \"{0}\" and \"{1}\"")
    @MethodSource("validQueryParameterCombinations")
    void validateEitherMandatoryQueryParam_withMultipleQueryParameters_happyDay(Optional<String> paramName1,
            Optional<String> paramName2) {
        final QueryParameters queryParameters = new QueryParametersImpl();
        paramName1.ifPresent(name -> queryParameters.addParameter(name, "Value1"));
        paramName2.ifPresent(name -> queryParameters.addParameter(name, "Value2"));
        assertDoesNotThrow(() -> Parameters
                .validateEitherMandatoryQueryParam(paramName1.orElse("K1"), paramName2.orElse("K2"), queryParameters));
    }

    @Test
    void validateEitherMandatoryQueryParam_withNoneOfMultipleQueryParametersPresent_exceptionThrown() {
        final String queryParamName1 = "name";
        final String queryParamName2 = "id";
        final QueryParameters queryParameters = new QueryParametersImpl(Map.of("phone", "555-176014"));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Parameters.validateEitherMandatoryQueryParam(queryParamName1, queryParamName2, queryParameters))
                .withMessageContaining("None of query parameters [%s, %s] is set", queryParamName1, queryParamName2);
    }

    @Test
    void validateMandatoryQueryParam_withSingleQueryParameter_happyDay() {
        final String queryParamName = "department";
        final QueryParameters queryParameters = new QueryParametersImpl(Map.of(queryParamName, "Sales"));
        assertDoesNotThrow(() -> Parameters.validateMandatoryQueryParam(queryParamName, queryParameters));
    }

    @Test
    void validateMandatoryQueryParam_withNoSingleQueryParameterPresent_exceptionThrown() {
        final String queryParamName = "company";
        final QueryParameters queryParameters = new QueryParametersImpl(Map.of("city", "Bespin"));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Parameters.validateMandatoryQueryParam(queryParamName, queryParameters))
                .withMessageContaining("[%s] is required but is not set", queryParamName);
    }

    @ParameterizedTest(name = "[{index}] Parameter \"{0}\" is null or empty")
    @NullAndEmptyStringSource
    void validateMandatoryQueryParam_withEmptySingleQueryParameter_exceptionThrown(String queryParamNameValue) {
        final String queryParamName = "region";
        final QueryParameters queryParameters = new QueryParametersWithDisabledValueChecks()
                .addParameter(queryParamName, queryParamNameValue);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Parameters.validateMandatoryQueryParam(queryParamName, queryParameters))
                .withMessageContaining("[%s] must not be empty", queryParamName);
    }

    @Test
    void validateMandatoryQueryParam_withInvalidSingleQueryParameter_exceptionThrown() {
        final String queryParamName = "languageCode";
        final String queryParamValue = "french";
        final QueryParameters params = new QueryParametersImpl(Map.of(queryParamName, queryParamValue));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Parameters.validateMandatoryQueryParam(queryParamName, params, code -> code.length() <= 2))
                .withMessageContaining("[%s] is set to an invalid value: %s", queryParamName, queryParamValue);
    }

    @Test
    void validateOptionalQueryParam_withValidQueryParameterPresent_happyDay() {
        final String queryParamName = "airDate";
        final QueryParameters queryParameters = new QueryParametersImpl(Map.of(queryParamName, "1998-01-26"));
        assertDoesNotThrow(() -> Parameters.validateOptionalQueryParam(queryParamName, queryParameters, Parameters.isValidDate("yyyy-MM-dd")));
    }

    @Test
    void validateOptionalQueryParam_withNoQueryParameterPresent_noValidationPerformed() {
        assertDoesNotThrow(() -> Parameters.validateOptionalQueryParam("season", new QueryParametersImpl(), s -> false));
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

    @ParameterizedTest(name = "[{index}] \"{0}\" matches date pattern <{1}>")
    @MethodSource("validDateStrings")
    void isValidDate_withValidDateValues_returnsTrue(String value, String pattern) {
        assertThat(Parameters.isValidDate(pattern).test(value)).isTrue();
    }

    @ParameterizedTest(name = "[{index}] \"{0}\" does not match date pattern <yyyy-MM-dd>")
    @NullSource
    @ValueSource(strings = {"", "  ", "abc", "21-02-30", "2022-15-28", "18-07-2019", "2020/04/31"})
    void isValidDate_withInvalidDateValues_returnsFalse(String value) {
        assertThat(Parameters.isValidDate("yyyy-MM-dd").test(value)).isFalse();
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

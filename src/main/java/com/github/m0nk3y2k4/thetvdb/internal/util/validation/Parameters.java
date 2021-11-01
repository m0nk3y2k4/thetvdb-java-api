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

import static java.util.stream.Collectors.toList;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.api.APIKey;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.FundingModel;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;

/**
 * Collection of simple checks to be used for method parameter validation
 * <p><br>
 * The default behavior for parameter checks provided by this class is to throw an {@link IllegalArgumentException} in
 * case the given arguments do not match the requirements. Some of the methods may support extended control over the
 * actual type of exception that will be thrown by allowing the calling instance to provide it's onw runtime exception
 * instance.
 * <p><br>
 * Please note that this class should primarily be used for method parameter validation as the default <em>{@code
 * IllegalArgumentException}</em> typically indicates that resolving the failed validation lies within the
 * responsibility of the calling instance. To check for additional, non-method parameter related preconditions use
 * {@link Preconditions}.
 */
public final class Parameters {

    /** Pattern for numeric integer String matcher */
    private static final Pattern NUMERIC_INTEGER = Pattern.compile("\\d+");

    private Parameters() {}     // Hidden constructor. Only static methods

    /**
     * Checks the condition and throws the given runtime exception in case the condition is not met.
     *
     * @param condition The condition to check for
     * @param value     The value to be checked against the condition
     * @param exception The exception to be thrown in case the condition is not met
     * @param <T>       the type of the value to check
     * @param <X>       type of the runtime exception to be thrown
     */
    public static <T, X extends RuntimeException> void validateCondition(Predicate<T> condition, T value, X exception) {
        if (!condition.test(value)) {
            throw exception;
        }
    }

    /**
     * Checks that the given <em>{@code obj}</em> is not <i>null</i>. Otherwise, an exception with the given error
     * message will be thrown.
     *
     * @param obj     The object to check
     * @param message Error message to be propagated to the exception in case of a failed validation
     *
     * @throws IllegalArgumentException If the given <em>{@code obj}</em> is <i>null</i>
     */
    public static void validateNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks that the given String is neither <i>null</i> nor empty. Otherwise, an exception with the given error
     * message will be thrown.
     *
     * @param obj     The String to check
     * @param message Error message to be propagated to the exception in case of a failed validation
     *
     * @throws IllegalArgumentException If the given String is either <i>null</i> or empty
     */
    public static void validateNotEmpty(String obj, String message) {
        if (APIUtil.hasNoValue(obj)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks that the given Optional contains a non-empty String. Otherwise, an exception with the given error message
     * will be thrown.
     *
     * @param obj     The String optional to check
     * @param message Error message to be propagated to the exception in case of a failed validation
     *
     * @throws IllegalArgumentException If no value is present or the value is an empty String
     */
    public static void validateNotEmpty(@Nonnull Optional<String> obj, String message) {
        validateNotEmpty(obj.orElse(null), message);
    }

    /**
     * Checks that the given Collection is neither <i>null</i> nor empty. Otherwise, an exception with the given error
     * message will be thrown.
     *
     * @param obj     The Collection to check
     * @param message Error message to be propagated to the exception in case of a failed validation
     *
     * @throws IllegalArgumentException If the given Collection is either <i>null</i> or contains no elements
     */
    public static void validateNotEmpty(Collection<?> obj, String message) {
        if (obj == null || obj.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks that the given value is equal or greater zero. Otherwise, an exception with the given error message will
     * be thrown.
     *
     * @param value   The numeric value to check
     * @param message Error message to be propagated to the exception in case of a failed validation
     *
     * @throws IllegalArgumentException If the given value is negative
     */
    public static void validateNotNegative(long value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks that a given URL path parameter name is not empty and the corresponding value matches the given condition.
     * Otherwise an exception will be thrown.
     *
     * @param paramName  The name of the URL path parameter to check
     * @param paramValue The corresponding value of the path parameter
     * @param condition  Condition to be matched by the given parameter value
     * @param <T>        Type of the parameter value to check
     *
     * @throws IllegalArgumentException If the given parameter name is <i>null</i> or the parameter value does not match
     *                                  the given condition
     */
    public static <T> void validatePathParam(String paramName, T paramValue, Predicate<T> condition) {
        validateNotNull(paramValue, String.format("Path parameter [%s] is required but is not set", paramName));
        validateCondition(condition, paramValue, new IllegalArgumentException(String
                .format("Path parameter [%s] is set to an invalid value: %s", paramName, paramValue)));
    }

    /**
     * Checks if the given <em>{@code params}</em> query parameter collection contains <u>at least one</u> of the given
     * parameters and that they are not empty. Otherwise, an exception will be thrown. Any matching parameter that is
     * present will be validated (not only the first one found).
     *
     * @param paramName1 The name of the 1st URL query parameter to check for
     * @param paramName2 The name of the 2nd URL query parameter to check for
     * @param params     Query parameters object that should contain the given parameter
     *
     * @throws IllegalArgumentException If the parameter collection does not contain any of the given parameters or if a
     *                                  parameter exists but contains no value
     */
    public static void validateEitherMandatoryQueryParam(String paramName1, String paramName2, QueryParameters params) {
        List<String> existingParams = Stream.of(paramName1, paramName2)
                .filter(name -> containsQueryParameter(name, params)).collect(toList());
        validateNotEmpty(existingParams, String.format("None of query parameters [%s, %s] is set", paramName1, paramName2));
        existingParams.forEach(paramName -> validateMandatoryQueryParam(paramName, params));
    }

    /**
     * Checks if the given <em>{@code params}</em> query parameter collection contains a non-empty <em>{@code
     * paramName}</em> parameter. Otherwise, an exception will be thrown.
     *
     * @param paramName The name of the URL query parameter to check for
     * @param params    Query parameters object that should contain the given parameter
     *
     * @throws IllegalArgumentException If the parameter collection does not contain a non-empty parameter with the
     *                                  given name
     */
    public static void validateMandatoryQueryParam(String paramName, QueryParameters params) {
        validateMandatoryQueryParam(paramName, params, s -> true);
    }

    /**
     * Checks if the given <em>{@code params}</em> query parameter collection contains a non-empty <em>{@code
     * paramName}</em> parameter which matches the given condition. Otherwise, an exception will be thrown.
     *
     * @param paramName The name of the URL query parameter to check for
     * @param params    Query parameters object that should contain the given parameter
     * @param condition Condition to be matched by the query parameter value
     *
     * @throws IllegalArgumentException If the parameter collection does not contain a non-empty parameter with the
     *                                  given name or the parameter does not match the given condition
     */
    public static void validateMandatoryQueryParam(String paramName, QueryParameters params,
            Predicate<String> condition) {
        validateCondition(query -> containsQueryParameter(paramName, query), params, new IllegalArgumentException(
                String.format("Query parameter [%s] is required but is not set", paramName)));
        Optional<String> paramValue = params.getParameterValue(paramName);
        validateNotEmpty(paramValue, String.format("Value for query parameter [%s] must not be empty", paramName));
        validateCondition(condition, paramValue.get(),       // NOSONAR: evaluated by upstream validation
                new IllegalArgumentException(String
                        .format("Value for query parameter [%s] is set to an invalid value: %s", paramName,
                                paramValue.get())));
    }

    /**
     * Checks if a <em>{@code paramName}</em> parameter is present in the given <em>{@code params}</em> query parameter
     * collection, and if so, verifies that its value is not empty and matches the given condition. Otherwise, an
     * exception will be thrown. If no such parameter exists this method returns instantly.
     *
     * @param paramName The name of the URL query parameter to check for
     * @param params    Query parameters object to check for the given parameter
     * @param condition Condition to be matched by the parameter value in case the query parameter is present
     *
     * @throws IllegalArgumentException If the parameter collection does contain a parameter with the given name but its
     *                                  value is empty or does not match the given condition
     */
    public static void validateOptionalQueryParam(String paramName, QueryParameters params,
            Predicate<String> condition) {
        if (containsQueryParameter(paramName, params)) {
            validateMandatoryQueryParam(paramName, params, condition);
        }
    }

    /**
     * Checks if the given <em>{@code params}</em> query parameter collection contains a <em>{@code paramName}</em>
     * parameter.
     *
     * @param paramName The name of the URL query parameter to check for
     * @param params    Query parameters object that should contain the given parameter
     *
     * @return Returns TRUE if the parameter collection contains a parameter with the given name
     */
    public static boolean containsQueryParameter(String paramName, QueryParameters params) {
        return Optional.ofNullable(params).map(p -> p.containsParameter(paramName)).orElse(false);
    }

    /**
     * Provides a predicate used to check whether a String represents a positive (greater zero) numerical integer.
     *
     * @return String predicate to check for a positive numerical integer
     */
    public static Predicate<String> isPositiveInteger() {
        return value -> APIUtil.hasValue(value) && NUMERIC_INTEGER.matcher(value).matches()
                && Long.valueOf(value).compareTo(0L) > 0;
    }

    /**
     * Provides a predicate used to check whether a String represents a valid date according to the given pattern.
     * <p><br>
     * Although strict parsing will be used, this predicate may consider date Strings to be valid, even if they do not
     * exactly match the given <em>{@code pattern}</em>, i.e. for 2-digit years. Further information can be found {@link
     * SimpleDateFormat SimpleDateFormat documentation}.
     *
     * @param pattern The pattern describing the date format
     *
     * @return String predicate to check for a valid date
     */
    public static Predicate<String> isValidDate(String pattern) {
        return value -> Optional.ofNullable(value)
                .map(date -> {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
                    dateFormat.setLenient(false);
                    return dateFormat.parse(date, new ParsePosition(0));
                }).isPresent();
    }

    /**
     * Checks whether all required properties of the given API key are set
     * <p><br>
     * The keys <em>{@code apiKey}</em> property must not be null or empty and for subscription based API keys an
     * additional PIN is required for authentication.
     *
     * @param key The API key to validate
     */
    public static void validateApiKey(APIKey key) {
        validateNotEmpty(key.getApiKey(), "Invalid key: the apiKey property must not be empty");
        if (key.getFundingModel() == FundingModel.SUBSCRIPTION) {
            validateNotEmpty(key.getPin(), "Invalid key: for user subscription based authentication a PIN is required");
        }
    }
}

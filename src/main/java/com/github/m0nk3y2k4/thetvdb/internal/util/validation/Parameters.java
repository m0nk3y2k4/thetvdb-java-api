package com.github.m0nk3y2k4.thetvdb.internal.util.validation;

import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Collection of simple checks to be used for method parameter validation
 * <p>
 * The default behavior for parameter checks provided by this class is to throw an {@link IllegalArgumentException} in case the given
 * arguments do not match the requirements. Some of the methods may support extended control over the actual type of exception that
 * will be thrown by allowing the calling instance to privide it's onw runtime exception instance.
 * <p>
 * Please note that this class should primarily be used for method parameter validation as the default <code>IllegalAgrumentException</code>
 * typically indicates that resolving the failed validation lies within the responsibility of the calling instance. To check for additional,
 * non-method parameter related preconditions use {@link Preconditions}.
 */
public final class Parameters {

    private Parameters() {}     // Hidden constructor. Only static methods

    /**
     * Checks the condition and throws the given runtime exception in case the condition is not met.
     *
     * @param condition The condition to check for
     * @param value The value to be checked against the codition
     * @param exception The exception to be thrown in case the condition is not met
     *
     * @param <T> the type of the value to check
     * @param <X> type of the runtime exception to be thrown
     */
    public static <T, X extends RuntimeException> void validateCondition (Predicate<T> condition, T value, X exception) {
        if (!condition.test(value)) {
            throw exception;
        }
    }

    /**
     * Checks that the given <code>obj</code> is not <i>null</i>. Otherwise an exception with the given error message
     * will be thrown.
     *
     * @param obj The object to check
     * @param message Error message to be propagated to the exception in case of a failed validation
     *
     * @throws IllegalArgumentException If the given <code>obj</code> is <i>null</i>
     */
    public static void validateNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks that the given String is neither <i>null</i> nor empty. Otherwise an exception with the given error message
     * will be thrown.
     *
     * @param obj The String to check
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
     * Checks that the given Optional contains a non-empty String. Otherwise an exception with the given error message
     * will be thrown.
     *
     * @param obj The String optional to check
     * @param message Error message to be propagated to the exception in case of a failed validation
     *
     * @throws IllegalArgumentException If no value is present or the value is an empty String
     */
    public static void validateNotEmpty(@Nonnull Optional<String> obj, String message) {
        Parameters.validateNotEmpty(obj.orElse(null), message);
    }

    /**
     * Checks that a given URL path parameter name is not empty and the corresponding value matches the given condition. Otherwise an
     * exception will be thrown.
     *
     * @param paramName The name of the URL path parameter to check
     * @param paramValue The corresponding value of the path parameter
     * @param condition Condition to be matched by the given parameter value
     *
     * @param <T> Type of the parameter value to check
     *
     * @throws IllegalArgumentException If the given parameter name is <i>null</i> or the parameter value does not match the given condition
     */
    public static <T> void validatePathParam(String paramName, T paramValue, Predicate<T> condition) {
        Parameters.validateNotNull(paramName, String.format("Path parameter [%s] is required but is not set", paramName));
        Parameters.validateCondition(condition, paramValue,
                new IllegalArgumentException(String.format("Path parameter [%s] is set to an invalid value: %s", paramName, paramValue)));
    }

    /**
     * Checks if the given <code>params</code> query parameter collection contains a non-empty <code>paramName</code> parameter. Otherwise an
     * exception will be thrown.
     *
     * @param paramName The name of the URL query parameter to check for
     * @param params Query parameters object that should contain the given parameter
     *
     * @throws IllegalArgumentException If the parameter collection does not contain a non-empty parameter with the given name
     */
    public static void validateQueryParam(String paramName, QueryParameters params) {
        Parameters.validateQueryParam(paramName, params, s -> true);
    }

    /**
     * Checks if the given <code>params</code> query parameter collection contains a non-empty <code>paramName</code> parameter which matches the
     * given condition. Otherwise an exception will be thrown.
     *
     * @param paramName The name of the URL query parameter to check for
     * @param params Query parameters object that should contain the given parameter
     * @param condition Condition to be matched by the query parameter value
     *
     * @throws IllegalArgumentException If the parameter collection does not contain a non-empty parameter with the given name or the parameter
     *                                  does not match the given condition
     */
    public static void validateQueryParam(String paramName, QueryParameters params, Predicate<String> condition) {
        Predicate<QueryParameters> containsMandatoryParam = query -> Optional.ofNullable(query).map(p -> p.containsParameter(paramName)).orElse(false);
        Parameters.validateCondition(containsMandatoryParam, params,
                new IllegalArgumentException(String.format("Query parameter [%s] is required but is not set", paramName)));
        Optional<String> paramValue = params.getParameterValue(paramName);
        Parameters.validateNotEmpty(paramValue, String.format("Value for query parameter [%s] must not be empty", paramName));
        Parameters.validateCondition(condition, paramValue.get(),       // NOSONAR: Value presence is already evaluated by upstream validation
                new IllegalArgumentException(String.format("Value for query parameter [%s] is set to an invalid value: %s", paramName, paramValue.get())));
    }
}

package com.github.m0nk3y2k4.thetvdb.internal.resource.validation;

import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APIValidationException;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Predicate;

public final class QueryValidator {

    private QueryValidator() {}      // Private constructor, only static methods

    public static void requiresQueryParam(@Nonnull String paramName, QueryParameters params) {
        requiresQueryParam(paramName, params, s -> true);
    }

    public static void requiresQueryParam(@Nonnull String paramName, QueryParameters params, @Nonnull Predicate<String> valueValidator) {
        boolean containsParameter = Optional.ofNullable(params).map(p -> p.containsParameter(paramName)).orElse(false);
        if (!containsParameter) {
            throw new APIValidationException(String.format("Query parameter [%s] is required but is not set", paramName));
        }

        Optional<String> paramValue = params.getParameterValue(paramName);
        if (!paramValue.isPresent()) {
            throw new APIValidationException(String.format("Value for query parameter [%s] must not be empty", paramName));
        }

        if (!valueValidator.test(paramValue.get())) {
            throw new APIValidationException(String.format("Value for query parameter [%s] is set to an invalid value: %s", paramName, paramValue));
        }
    }
}

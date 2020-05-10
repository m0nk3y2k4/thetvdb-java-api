package com.github.m0nk3y2k4.thetvdb.internal.resource.validation;

import com.github.m0nk3y2k4.thetvdb.internal.exception.APIValidationException;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public final class PathValidator {

    private PathValidator() {}       // Private constructor, only static methods

    public static void requiresPathParam(@Nonnull String paramName, Object paramValue) {
        requiresPathParam(paramName, paramValue, validate -> true);
    }

    public static <T> void requiresPathParam(@Nonnull String paramName, T paramValue, @Nonnull Predicate<T> valueValidator) {
        if (paramValue == null) {
            throw new APIValidationException(String.format("Path parameter [%s] is required but is not set", paramName));
        }

        if (!valueValidator.test(paramValue)) {
            throw new APIValidationException(String.format("Path parameter [%s] is set to an invalid value: %s", paramName, paramValue));
        }
    }
}

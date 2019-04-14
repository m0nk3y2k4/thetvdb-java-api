package com.github.m0nk3y2k4.thetvdb.internal.resource.validation;

import com.github.m0nk3y2k4.thetvdb.internal.exception.APIValidationException;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;

public final class ObjectValidator {

    private ObjectValidator(){}     // Private constructor, only static methods

    public static void requireNonNull(Object obj, String message) {
        if (obj == null) {
            throw new APIValidationException(message);
        }
    }

    public static void requireNonEmpty(String obj, String message) {
        if (APIUtil.hasNoValue(obj)) {
            throw new APIValidationException(message);
        }
    }
}

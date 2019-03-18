package com.github.m0nk3y2k4.thetvdb.internal.resource.validation;

import java.util.Map;
import java.util.function.Function;

import com.github.m0nk3y2k4.thetvdb.internal.exception.APIValidationException;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;

public final class ParamValidator {

    public static final void requiresPathParam(String paramName, Object paramValue) throws APIValidationException {
        requiresPathParam(paramName, paramValue, s -> true);
    }

    public static final void requiresPathParam(String paramName, Object paramValue, Function<Object, Boolean> valueValidator) throws APIValidationException {
        if (paramValue == null) {
            throw new APIValidationException(String.format("Path parameter [%s] is required but is not set", paramName));
        }

        if (!valueValidator.apply(paramValue)) {
            throw new APIValidationException(String.format("Path parameter [%s] is set to an invalid value: %s", paramName, paramValue));
        }
    }

    public static final void requiresQueryParam(String paramName, Map<String, String> params) throws APIValidationException {
        requiresQueryParam(paramName, params, s -> true);
    }

    public static final void requiresQueryParam(String paramName, Map<String, String> params, Function<String, Boolean> valueValidator) throws APIValidationException {
        if (!params.containsKey(paramName)) {
            throw new APIValidationException(String.format("Query parameter [%s] is required but is not set", paramName));
        }

        if (!APIUtil.hasValue(params.get(paramName))) {
            throw new APIValidationException(String.format("Value for query parameter [%s] must not be empty", paramName));
        }

        String paramValue = params.get(paramName);
        if (!valueValidator.apply(paramValue)) {
            throw new APIValidationException(String.format("Value for query parameter [%s] is set to an invalid value: %s", paramName, paramValue));
        }
    }
}
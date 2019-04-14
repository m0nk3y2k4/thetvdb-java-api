package com.github.m0nk3y2k4.thetvdb.internal.resource.validation;

import com.github.m0nk3y2k4.thetvdb.internal.exception.APIValidationException;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;

import java.util.Arrays;
import java.util.List;

public final class ConnectionValidator {

    private ConnectionValidator() {}     // Private constructor, only static methods

    private static final List<String> VALID_REQUEST_METHODS = Arrays.asList("GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE", "TRACE");

    public static void validateResource(String resource) {
        if (APIUtil.hasNoValue(resource)) {
            throw new APIValidationException("No API resource specified");
        }
    }

    public static void validateRequestMethod(String requestMethod) {
        if (APIUtil.hasNoValue(requestMethod)) {
            throw new APIValidationException("No HTTP request method specified");
        }

        if (!VALID_REQUEST_METHODS.contains(requestMethod.toUpperCase())) {
            throw new APIValidationException(String.format("Specified HTTP request method [%s] is not in the list of supported methods %s", requestMethod, VALID_REQUEST_METHODS.toString()));
        }
    }

    public static void validatePayload(String data) {
        if (data == null) {
            throw new APIValidationException("Request payload data is not set");
        }
    }
}

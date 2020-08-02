package com.github.m0nk3y2k4.thetvdb.testutils;

import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.QueryParametersImpl;

/**
 * Utility class providing general methods for API JUnit testing
 * <p><br>
 * This class offers quick access to commonly used functionality for general API testing, like for example
 * the creation of simple query parameters.
 */
public class APITestUtil {

    /**
     * Creates a new query parameter representing the given key/value pair
     *
     * @param key The query parameter key
     * @param value The query parameter value
     *
     * @return New query parameter representing a single key/value pair
     */
    public static QueryParameters params(String key, String value) {
        return new QueryParametersImpl().addParameter(key, value);
    }
}

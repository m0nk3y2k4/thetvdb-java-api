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
     * Creates a new query parameter with the given key/value pair
     *
     * @param k1 The query parameter key
     * @param v1 The query parameter value
     *
     * @return New query parameter representing a single key/value pair
     */
    public static QueryParameters params(String k1, String v1) {
        return new QueryParametersImpl().addParameter(k1, v1);
    }

    /**
     * Creates a new query parameter with the given key/value pairs
     *
     * @param k1 The 1st query parameter key
     * @param v1 The 1st query parameter value
     * @param k2 The 2nd query parameter key
     * @param v2 The 2nd query parameter value
     *
     * @return New query parameter representing two key/value pairs
     */
    public static QueryParameters params(String k1, String v1, String k2, String v2) {
        return params(k1, v1).addParameter(k2, v2);
    }
}

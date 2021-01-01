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

package com.github.m0nk3y2k4.thetvdb.testutils;

import static com.github.m0nk3y2k4.thetvdb.api.enumeration.FundingModel.CONTRACT;
import static com.github.m0nk3y2k4.thetvdb.api.enumeration.FundingModel.SUBSCRIPTION;

import com.github.m0nk3y2k4.thetvdb.api.APIKey;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.APIKeyImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.QueryParametersImpl;

/**
 * Utility class providing general methods for API JUnit testing
 * <p><br>
 * This class offers quick access to commonly used functionality for general API testing, like for example the creation
 * of simple query parameters.
 */
public final class APITestUtil {

    public static final APIKey CONTRACT_APIKEY = new APIKeyImpl.Builder().key("contract-unit-test-api-key")
            .fundingModel(CONTRACT).build();

    public static final APIKey SUBSCRIPTION_APIKEY = new APIKeyImpl.Builder().key("subscription-unit-test-api-key")
            .fundingModel(SUBSCRIPTION).build();

    private APITestUtil() {}

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

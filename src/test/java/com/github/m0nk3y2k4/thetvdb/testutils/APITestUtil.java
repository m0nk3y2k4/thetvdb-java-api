/*
 * Copyright (C) 2019 - 2022 thetvdb-java-api Authors and Contributors
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

import java.util.Optional;

import com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory;
import com.github.m0nk3y2k4.thetvdb.api.APIKey;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.FundingModel;
import com.github.m0nk3y2k4.thetvdb.api.model.data.FavoriteRecord;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.APIKeyImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.QueryParametersImpl;

/**
 * Utility class providing general methods for API JUnit testing
 * <p><br>
 * This class offers quick access to commonly used functionality for general API testing, like for example the creation
 * of simple query parameters.
 */
public final class APITestUtil {

    /** A contract-based API key */
    public static final APIKey CONTRACT_APIKEY = new APIKeyImpl.Builder().apiKey("contract-unit-test-api-key")
            .fundingModel(CONTRACT).build();

    /** A subscription-based API key */
    public static final APIKey SUBSCRIPTION_APIKEY = new APIKeyImpl.Builder().apiKey("subscription-unit-test-api-key")
            .pin("my-secret-pin").fundingModel(SUBSCRIPTION).build();

    /** An invalid API key with empty PIN parameter */
    public static final APIKey INVALID_APIKEY = apiKey("E9U4D7D4T7FS", null, SUBSCRIPTION);

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

    /**
     * Creates a new API key with the given values. Unlike keys created via the {@link APIKeyImpl.Builder builder}, no
     * further validation of the given values will be performed which enables the client to generate effectively invalid
     * keys.
     *
     * @param apiKey       The actual API key
     * @param pin          Additional PIN for end-user subscription based authentication
     * @param fundingModel The funding model on which this key has been issued on
     *
     * @return New API key based on the given parameters
     */
    public static APIKey apiKey(String apiKey, String pin, FundingModel fundingModel) {
        return new TestAPIKey(apiKey, pin, fundingModel);
    }

    /**
     * Creates a new favorite record with only the series ID being set
     *
     * @param seriesId Some arbitrary favorite series ID
     *
     * @return New favorite record with the given ID set to be a favorite series
     */
    public static FavoriteRecord favoriteRecord(int seriesId) {
        return TheTVDBApiFactory.createFavoriteRecordBuilder().series(seriesId).build();
    }

    /**
     * Basic APIKey implementation without parameter validation
     */
    private static final class TestAPIKey implements APIKey {

        /** The actual API key */
        private final String apiKey;

        /** Additional PIN for end-user subscription based authentication */
        private final String pin;

        /** The funding model on which this key has been issued on */
        private final FundingModel fundingModel;

        /**
         * Default constructor to create a new, non-validating API key
         *
         * @param apiKey       The actual API key
         * @param pin          Additional PIN for end-user subscription based authentication
         * @param fundingModel The funding model on which this key has been issued on
         */
        private TestAPIKey(String apiKey, String pin, FundingModel fundingModel) {
            this.apiKey = apiKey;
            this.pin = pin;
            this.fundingModel = fundingModel;
        }

        @Override
        public String getApiKey() {
            return apiKey;
        }

        @Override
        public Optional<String> getPin() {
            return Optional.ofNullable(pin);
        }

        @Override
        public FundingModel getFundingModel() {
            return fundingModel;
        }
    }
}

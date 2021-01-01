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

package com.github.m0nk3y2k4.thetvdb.api;

import com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.FundingModel;

/**
 * Interface representing a <i>TheTVDB.com</i> v4 API key to be used for remote API authentication. Such API keys may be
 * issued either based on a negotiated contract with <i>TheTVDB.com</i> or based on an end-user subscription. Whenever a
 * new instance of this interface is created you will have to provide which of the aforementioned funding models applies
 * to the API key that is to be used. Instances of this interface might be created via the {@link TheTVDBApiFactory}.
 */
public interface APIKey {

    /**
     * Returns the actual API key String value.
     *
     * @return The <i>TheTVDB.com</i> v4 API key
     */
    String getKey();

    /**
     * Returns the funding model based on which the API-Key was issued.
     *
     * @return The related funding model for this API key
     */
    FundingModel getFundingModel();
}

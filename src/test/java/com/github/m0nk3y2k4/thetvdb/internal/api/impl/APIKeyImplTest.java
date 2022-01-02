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

package com.github.m0nk3y2k4.thetvdb.internal.api.impl;

import static com.github.m0nk3y2k4.thetvdb.api.enumeration.FundingModel.CONTRACT;
import static com.github.m0nk3y2k4.thetvdb.api.enumeration.FundingModel.SUBSCRIPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.github.m0nk3y2k4.thetvdb.testutils.parameterized.EmptyStringSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

class APIKeyImplTest {

    @Test
    void build_withValidParameters_apiKeyInstanceCreated() {
        assertDoesNotThrow(() -> new APIKeyImpl.Builder().apiKey("IW85Z9U4E0E4").fundingModel(CONTRACT));
        assertDoesNotThrow(() -> new APIKeyImpl.Builder().apiKey("O80Z4RHZ7").pin("NOT_NEEDED").fundingModel(CONTRACT));
        assertDoesNotThrow(() -> new APIKeyImpl.Builder().apiKey("W778FT4F71J").pin("PIN2").fundingModel(SUBSCRIPTION));
    }

    @ParameterizedTest(name = "[{index}] Cannot create API key for \"{0}\"")
    @EmptyStringSource
    void validate_withEmptyApiKey_exceptionIsThrown(String apiKey) {
        APIKeyImplBuilder key = new APIKeyImpl.Builder().apiKey(apiKey).fundingModel(CONTRACT);
        assertThatIllegalArgumentException().isThrownBy(key::build)
                .withMessageContaining("Invalid key: the apiKey property must not be empty");
    }

    @Test
    void validate_withMissingPin_exceptionIsThrown() {
        APIKeyImplBuilder key = new APIKeyImpl.Builder().apiKey("IW5G0J46S1H7F").fundingModel(SUBSCRIPTION);
        assertThatIllegalArgumentException().isThrownBy(key::build)
                .withMessageContaining("Invalid key: for user subscription based authentication a PIN is required");
    }

    @ParameterizedTest(name = "[{index}] Cannot create user subscription API key with pin \"{0}\"")
    @EmptyStringSource
    void validate_withEmptyPin_exceptionIsThrown(String pin) {
        APIKeyImplBuilder key = new APIKeyImpl.Builder().apiKey("GGS78FJSD5R7").pin(pin).fundingModel(SUBSCRIPTION);
        assertThatIllegalArgumentException().isThrownBy(key::build)
                .withMessageContaining("Invalid key: for user subscription based authentication a PIN is required");
    }

    @Test
    void staticBuilderClass_newInstance_extendsDTOBuilder() {
        assertThat(new APIKeyImpl.Builder()).isInstanceOf(APIKeyImplBuilder.class);
    }
}

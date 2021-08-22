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

package com.github.m0nk3y2k4.thetvdb.internal.util.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.testutils.ResponseData;
import com.github.m0nk3y2k4.thetvdb.testutils.parameterized.ResponseDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

class APIJsonMapperTest {

    @Test
    void readObject_withInvalidJSON_throwsAPIException() {
        assertThatExceptionOfType(APIException.class).isThrownBy(() ->
                APIJsonMapper.readValue(new ObjectMapper().readTree(
                                "{\"data\": {\"id\": \"NaN\"}, \"status\": \"success\"}"),
                        ResponseData.ARTWORK.getType()));
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @ResponseDataSource
    <T> void readValue_fromResource_verifyJsonIsParsedProperly(ResponseData<APIResponse<T>> resource) throws Exception {
        assertThat(APIJsonMapper.readValue(resource.getJson(), resource.getType())).isEqualTo(resource.getDTO());
    }
}

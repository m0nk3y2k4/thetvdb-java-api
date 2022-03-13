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

package com.github.m0nk3y2k4.thetvdb.internal.util.json.deser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse.Links;
import com.github.m0nk3y2k4.thetvdb.testutils.ResponseData;
import com.github.m0nk3y2k4.thetvdb.testutils.json.Data;
import com.github.m0nk3y2k4.thetvdb.testutils.parameterized.ResponseDataSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class APIResponseDeserializerTest {

    //@DisableFormatting
    private static final String MISSING_DATA_PROPERTY =
            "\"status\": \"success\"";

    private static final String MISSING_STATUS_PROPERTY =
            "\"data\": {\"content\": \"Some content\"}";

    private static final String MISSING_LINKS_PROPERTY =
            "\"data\": {\"content\": \"Some content\"}," +
            "\"status\": \"success\"";

    private static final String NULL_DATA_PROPERTY =
            "\"data\": null," +
            "\"status\": \"success\"";

    private static final String NULL_STATUS_PROPERTY =
            "\"data\": {\"content\": \"Some content\"}," +
            "\"status\": null";

    private static final String NULL_LINKS_PROPERTY =
            "\"data\": {\"content\": \"Some content\"}," +
            "\"status\": \"success\"," +
            "\"links\": null";

    private static final String EMPTY_LINKS_PROPERTY =
            "\"data\": {\"content\": \"Some content\"}," +
                    "\"status\": \"success\"," +
                    "\"links\": {}";
    //@EnableFormatting

    private final APIResponseDeserializer<Data, IOException> functionalDeserializer =
            new APIResponseDeserializer<>(dataNode -> new ObjectMapper().readValue(dataNode.toString(), Data.class));

    @ParameterizedTest(name = "[{index}] Parsing JSON [{0}] throws IllegalArgumentException")
    @ValueSource(strings = {MISSING_DATA_PROPERTY, MISSING_STATUS_PROPERTY})
    void deserialize_withMandatoryPropertyMissing_throwsIllegalArgumentException(String jsonContent)
            throws Exception {
        JsonParser jsonParser = new JsonFactory().createParser("{" + jsonContent + "}");
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                functionalDeserializer.deserialize(jsonParser, null));
    }

    @ParameterizedTest(name = "[{index}] Parsing JSON [{0}] does not throw any exception")
    @ValueSource(strings = {NULL_DATA_PROPERTY, NULL_STATUS_PROPERTY})
    void deserialize_withMandatoryPropertyNull_isParsedWithoutThrowingAnyException(String jsonContent)
            throws Exception {
        JsonParser jsonParser = new JsonFactory().createParser("{" + jsonContent + "}");
        assertThatNoException().isThrownBy(() -> functionalDeserializer.deserialize(jsonParser, null));
    }

    @ParameterizedTest(name = "[{index}] Optional fields in JSON [{0}] are mapped to empty object")
    @ValueSource(strings = {MISSING_LINKS_PROPERTY, NULL_LINKS_PROPERTY, EMPTY_LINKS_PROPERTY})
    void deserialize_withOptionalPropertyMissingOrNull_mappedToEmptyObject(String jsonContent) throws Exception {
        JsonParser jsonParser = new JsonFactory().createParser("{" + jsonContent + "}");
        APIResponse<Data> result = functionalDeserializer.deserialize(jsonParser, null);

        assertThat(result.getLinks()).extracting(Links::getPrevious, Links::getSelf, Links::getNext)
                .containsOnly(Optional.empty());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @ResponseDataSource(names = "DATA")
    <T> void deserialize_withFullJSON_verifyJsonIsParsedProperly(ResponseData<T> response) throws Exception {
        JsonParser jsonParser = new JsonFactory().createParser(response.getUrl());
        APIResponse<Data> result = functionalDeserializer.deserialize(jsonParser, null);

        assertThat(result).isNotNull().isEqualTo(response.getDTO());
    }
}

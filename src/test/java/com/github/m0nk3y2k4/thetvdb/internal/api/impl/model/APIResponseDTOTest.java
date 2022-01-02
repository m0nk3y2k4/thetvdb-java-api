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

package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.m0nk3y2k4.thetvdb.testutils.json.Data;
import org.junit.jupiter.api.Test;

class APIResponseDTOTest {

    @Test
    void toString_withNullDTOs_verifyDefaultValuesInStringRepresentation() {
        assertThat(new APIResponseDTO.Builder<Data>().build()).asString().isEqualTo("Data: [], Errors: [], Links: []");
    }

    @Test
    void toString_withEmptyDTOs_verifyDefaultValuesInStringRepresentation() {
        assertThat(new APIResponseDTO.Builder<Data>().data(new Data())
                .errors(new APIResponseDTO.ErrorsDTO.Builder().build())
                .links(new APIResponseDTO.LinksDTO.Builder().build())
                .build()).asString().isEqualTo(
                "Data: [], Errors: [Filters: [], Language: , QueryParams: []], Links: [First: , Last: , Next: , Previous: ]");
    }

    @Test
    void toString_withFilledDTOs_verifyStringRepresentation() {
        assertThat(new APIResponseDTO.Builder<Data>().data(Data.with("Content"))
                .errors(new APIResponseDTO.ErrorsDTO.Builder()
                        .addInvalidFilters("Fx", "fulter").invalidLanguage("cg").addInvalidQueryParams("itemKey", "q")
                        .build())
                .links(new APIResponseDTO.LinksDTO.Builder()
                        .first(1).last(12).next(5).previous(3).build())
                .build()).asString().isEqualTo(
                "Data: [Content], " +
                        "Errors: [Filters: [Fx, fulter], Language: cg, QueryParams: [itemKey, q]], " +
                        "Links: [First: 1, Last: 12, Next: 5, Previous: 3]");
    }
}

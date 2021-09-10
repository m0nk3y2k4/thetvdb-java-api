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

package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.APIResponseDTO.LinksDTO;
import com.github.m0nk3y2k4.thetvdb.testutils.json.Data;
import org.junit.jupiter.api.Test;

class APIResponseDTOTest {

    @Test
    void toString_withEmptyDTOs_verifyDefaultValuesInStringRepresentation() {
        assertThat(new APIResponseDTO.Builder<Data>().data(new Data())
                .status("")
                .links(new LinksDTO.Builder().build())
                .build())
                .asString().isEqualTo("Data: [], Status: [], Links: [Previous: , Self: , Next: ]");
    }

    @Test
    void toString_withFilledDTOs_verifyStringRepresentation() {
        assertThat(new APIResponseDTO.Builder<Data>().data(Data.with("Content"))
                .status("Success")
                .links(new LinksDTO.Builder()
                        .previous("Prev")
                        .self("Self")
                        .next("Next")
                        .build())
                .build())
                .asString()
                .isEqualTo("Data: [Content], Status: [Success], Links: [Previous: Prev, Self: Self, Next: Next]");
    }

    @Test
    void staticBuilderClass_newApiResponseInstance_extendsDTOBuilder() {
        assertThat(new APIResponseDTO.Builder<>()).isInstanceOf(APIResponseDTOBuilder.class);
    }

    @Test
    void staticBuilderClass_newLinksInstance_extendsDTOBuilder() {
        assertThat(new LinksDTO.Builder()).isInstanceOf(LinksDTOBuilder.class);
    }
}

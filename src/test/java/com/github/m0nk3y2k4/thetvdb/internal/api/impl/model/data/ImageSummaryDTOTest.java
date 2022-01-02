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

package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ImageSummaryDTOTest {

    @Test
    void toString_withCounts_verifyStringRepresentation() {
        assertThat(new ImageSummaryDTO.Builder().fanartCount(12L).posterCount(4L).seasonCount(2L).seasonwideCount(14L)
                .seriesCount(6L).build()).asString()
                .isEqualTo("[Fanart] 12, [Poster] 4, [Season] 2, [Seasonwide] 14, [Series] 6");
    }

    @Test
    void toString_withoutCounts_verifyDefaultValuesInStringRepresentation() {
        assertThat(new ImageSummaryDTO.Builder()
                .build()).asString().isEqualTo("[Fanart] 0, [Poster] 0, [Season] 0, [Seasonwide] 0, [Series] 0");
    }
}

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

package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ImageDTOTest {

    private static Stream<Arguments> ratingsInfo() {
        return Stream.of(
                Arguments.of(Collections.emptyMap(), null, null),
                Arguments.of(Map.of("count", 847), null, 847),
                Arguments.of(Map.of("average", 5), 5D, null),
                Arguments.of(Map.of("average", 2, "count", 65), 2D, 65)
        );
    }

    @ParameterizedTest(name = "[{index}] Based on RatingsInfo object \"{0}\" values are: Average={1}, Count={2}")
    @MethodSource("ratingsInfo")
    void ratingsInfo_withDifferentSettings_verifyRatingAverageAndRatingCount(Map<String, Integer> ratingsInfo,
            Double expectedAverage, Integer expectedCount) {
        ImageDTO dto = new ImageDTO.Builder().ratingsInfo(ratingsInfo).build();
        assertThat(dto.getRatingAverage()).isEqualTo(expectedAverage);
        assertThat(dto.getRatingCount()).isEqualTo(expectedCount);
    }

    @Test
    void toString_withKeyTypeAndFileName_verifyStringRepresentation() {
        assertThat(new ImageDTO.Builder().keyType("fanart").fileName("series/296762/backgrounds/62088681.jpg").build())
                .asString().isEqualTo("[fanart] series/296762/backgrounds/62088681.jpg");
    }
}

/*
 * Copyright (C) 2019 - 2020 thetvdb-java-api Authors and Contributors
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

class SeriesSummaryDTOTest {

    @Test
    void toString_withSeriesAndEpisodes_verifyStringRepresentation() {
        assertThat(new SeriesSummaryDTO.Builder().addAiredSeasons("1", "2", "3", "4").airedEpisodes("54")
                .addDvdSeasons("1", "2").dvdEpisodes("23")
                .build()).asString().isEqualTo("[Seasons/Episodes] Aired: [1, 2, 3, 4]/54, DVD: [1, 2]/23");
    }
}
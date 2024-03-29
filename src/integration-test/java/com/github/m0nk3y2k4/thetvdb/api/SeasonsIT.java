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

package com.github.m0nk3y2k4.thetvdb.api;

import static com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory.createQueryParameters;
import static com.github.m0nk3y2k4.thetvdb.testutils.assertj.IntegrationTestAssertions.assertThat;

import com.github.m0nk3y2k4.thetvdb.testutils.annotation.IntegrationTestSuite;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@IntegrationTestSuite("Seasons")
class SeasonsIT {

    @Test
    @Order(1)
    void getAllSeasons(TheTVDBApi api) {
        assertThat(() -> api.getAllSeasons(createQueryParameters())).as("/seasons").doesNotThrowAnyException();
    }

    @Test
    @Order(2)
    void getSeason(TheTVDBApi api) {
        assertThat(() -> api.getSeason(1733210)).as("/seasons/669028").doesNotThrowAnyException();
    }

    @Test
    @Order(3)
    void getSeasonDetails(TheTVDBApi api) {
        assertThat(() -> api.getSeasonDetails(669028)).as("/seasons/1733210/extended").doesNotThrowAnyException();
    }

    @Test
    @Order(4)
    void getSeasonTypes(TheTVDBApi api) {
        assertThat(api::getSeasonTypes).as("/seasons/types").doesNotThrowAnyException();
    }

    @Test
    @Order(5)
    void getSeasonTranslation(TheTVDBApi api) {
        assertThat(() -> api.getSeasonTranslation(750521, "deu")).as("/seasons/750521/translations/deu")
                .doesNotThrowAnyException();
    }
}

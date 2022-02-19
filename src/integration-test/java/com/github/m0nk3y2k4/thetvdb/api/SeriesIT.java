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
import static com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesSeasonType.DEFAULT;
import static com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesSeasonType.OFFICIAL;
import static com.github.m0nk3y2k4.thetvdb.testutils.assertj.IntegrationTestAssertions.assertThat;

import com.github.m0nk3y2k4.thetvdb.testutils.annotation.IntegrationTestSuite;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@IntegrationTestSuite("Series")
class SeriesIT {

    @Test
    @Order(1)
    void getAllSeriesStatuses(TheTVDBApi api) {
        assertThat(api::getAllSeriesStatuses).as("/series/statuses").doesNotThrowAnyException();
    }

    @Test
    @Order(2)
    void getAllSeries(TheTVDBApi api) {
        assertThat(() -> api.getAllSeries(createQueryParameters())).as("/series").doesNotThrowAnyException();
    }

    @Test
    @Order(3)
    void getSeries(TheTVDBApi api) {
        assertThat(() -> api.getSeries(280619)).as("/series/280619").doesNotThrowAnyException();
    }

    @Test
    @Order(4)
    void getSeriesArtworks(TheTVDBApi api) {
        assertThat(() -> api.getSeriesArtworks(273181, "deu")).as("/series/273181/artworks?language=deu")
                .doesNotThrowAnyException();
    }

    @Test
    @Order(5)
    void getSeriesDetails(TheTVDBApi api) {
        assertThat(() -> api.getSeriesDetails(292157)).as("/series/292157/extended").doesNotThrowAnyException();
    }

    @Test
    @Order(6)
    void getSeriesEpisodes(TheTVDBApi api) {
        assertThat(() -> api.getSeriesEpisodes(71470, OFFICIAL)).as("/series/71470/episodes/official")
                .doesNotThrowAnyException();
    }

    @Test
    @Order(7)
    void getSeriesEpisodesTranslated(TheTVDBApi api) {
        assertThat(() -> api.getSeriesEpisodesTranslated(360893, DEFAULT, "fra")).as("/series/360893/episodes/default/fra")
                .doesNotThrowAnyException();
    }

    @Test
    @Order(8)
    void getSeriesTranslation(TheTVDBApi api) {
        assertThat(() -> api.getSeriesTranslation(361753, "nld")).as("/series/361753/translations/nld")
                .doesNotThrowAnyException();
    }
}

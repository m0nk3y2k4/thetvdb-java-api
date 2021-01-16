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

package com.github.m0nk3y2k4.thetvdb.api;

import static com.github.m0nk3y2k4.thetvdb.testutils.assertj.IntegrationTestAssertions.assertThat;

import com.github.m0nk3y2k4.thetvdb.testutils.annotation.IntegrationTestSuite;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@IntegrationTestSuite("Movies")
class MoviesIT {

    @Disabled("Movies endpoint is currently broken on TheTVDB.com side, constantly returning HTTP-404")
    @Test
    @Order(2)
    void getMovie(TheTVDBApi api) {
        assertThat(() -> api.getMovie(78743)).as("/movies/78743").doesNotThrowAnyException();
    }

    @Test
    @Order(4)
    void getMovieTranslation(TheTVDBApi api) {
        assertThat(() -> api.getMovieTranslation(733, "ita")).as("/movies/67143/translations/ita")
                .doesNotThrowAnyException();
    }
}

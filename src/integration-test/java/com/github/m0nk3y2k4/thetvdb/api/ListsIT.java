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

@IntegrationTestSuite("Lists")
class ListsIT {

    @Test
    @Order(1)
    void getListTranslation(TheTVDBApi api) {
        assertThat(() -> api.getListTranslation(4, "fra")).as("/movies/4/translations/fra")
                .doesNotThrowAnyException();
    }

    @Test
    @Order(2)
    void getAllLists(TheTVDBApi api) {
        assertThat(() -> api.getAllLists(createQueryParameters())).as("/lists").doesNotThrowAnyException();
    }

    @Test
    @Order(3)
    void getList(TheTVDBApi api) {
        assertThat(() -> api.getList(56)).as("/lists/56").doesNotThrowAnyException();
    }

    @Test
    @Order(4)
    void getListDetails(TheTVDBApi api) {
        assertThat(() -> api.getListDetails(58)).as("/lists/58/extended").doesNotThrowAnyException();
    }
}

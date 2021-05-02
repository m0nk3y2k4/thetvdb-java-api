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
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@IntegrationTestSuite("Awards")
class AwardsIT {

    @Test
    @Order(1)
    void getAwardCategory(TheTVDBApi api) {
        assertThat(() -> api.getAwardCategory(1)).as("/awards/categories/1").doesNotThrowAnyException();
    }

    @Test
    @Order(2)
    void getAwardCategoryDetails(TheTVDBApi api) {
        assertThat(() -> api.getAwardCategoryDetails(10)).as("/awards/categories/10/extended")
                .doesNotThrowAnyException();
    }
}

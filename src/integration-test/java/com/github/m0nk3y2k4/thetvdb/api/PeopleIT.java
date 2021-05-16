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

@IntegrationTestSuite("People")
class PeopleIT {

    @Test
    @Order(1)
    void getAllPeopleTypes(TheTVDBApi api) {
        assertThat(api::getAllPeopleTypes).as("/people/types").doesNotThrowAnyException();
    }

    @Test
    @Order(2)
    void getPeople(TheTVDBApi api) {
        assertThat(() -> api.getPeople(294100)).as("/people/294100").doesNotThrowAnyException();
    }

    @Test
    @Order(3)
    void getPeopleDetails(TheTVDBApi api) {
        assertThat(() -> api.getPeopleDetails(254786)).as("/people/254786/extended").doesNotThrowAnyException();
    }
}

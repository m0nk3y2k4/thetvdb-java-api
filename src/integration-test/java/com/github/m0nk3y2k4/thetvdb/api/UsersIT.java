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

import static com.github.m0nk3y2k4.thetvdb.testutils.assertj.IntegrationTestAssertions.assertThat;

import com.github.m0nk3y2k4.thetvdb.testutils.annotation.IntegrationTestSuite;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@IntegrationTestSuite("Users")
class UsersIT {

    @Test
    @Order(1)
    void getUser(TheTVDBApi api) {
        assertThat(api::getUser).as("/user").doesNotThrowAnyException();
    }

    @Test
    @Order(2)
    void getFavorites(TheTVDBApi api) {
        assertThat(api::getFavorites).as("/user/favorites").doesNotThrowAnyException();
    }

    @Test
    @Order(3)
    void deleteFromFavorites(TheTVDBApi api) {
        assertThat(() -> api.deleteFromFavorites(328711)).as("/user/favorites/328711[Delete]")
                .doesNotThrowAnyException();
    }

    @Test
    @Order(4)
    void addToFavorites(TheTVDBApi api) {
        assertThat(() -> api.addToFavorites(328711)).as("/user/favorites/328711[Add]").doesNotThrowAnyException();
    }

    @Test
    @Order(5)
    void getRatings(TheTVDBApi api) {
        assertThat(api::getRatings).as("/user/ratings").doesNotThrowAnyException();
    }

    @Disabled("Seems this route is currently broken on the TheTVDB.com side")
    @Test
    @Order(6)
    void queryRatingsByItemType(TheTVDBApi api) {
        assertThat(() -> api.queryRatingsByItemType("series")).as("/user/ratings/query").doesNotThrowAnyException();
    }

    @Test
    @Order(7)
    void getAvailableRatingsQueryParameters(TheTVDBApi api) {
        assertThat(api::getAvailableRatingsQueryParameters).as("/user/ratings/query/params").doesNotThrowAnyException();
    }

    @Test
    @Order(8)
    void deleteFromRatings(TheTVDBApi api) {
        assertThat(() -> api.deleteFromRatings("image", 1101207)).as("/user/ratings/image/1101207[Delete]")
                .doesNotThrowAnyException();
    }

    @Test
    @Order(9)
    void addToRatings(TheTVDBApi api) {
        assertThat(() -> api.addToRatings("image", 1101207, 8)).as("/user/ratings/image/1101207/8[Add]")
                .doesNotThrowAnyException();
    }
}

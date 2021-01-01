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
import static java.util.stream.Collectors.joining;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import com.github.m0nk3y2k4.thetvdb.testutils.annotation.IntegrationTestSuite;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@IntegrationTestSuite("Updates")
class UpdatesIT {

    @Test
    @Order(1)
    void queryLastUpdated(TheTVDBApi api) {
        final long fromTime = Instant.now().minus(2, ChronoUnit.DAYS).getEpochSecond();
        final long toTime = Instant.now().minus(2, ChronoUnit.DAYS).plus(30, ChronoUnit.MINUTES).getEpochSecond();
        assertThat(() -> {
            Map<Long, Long> lastUpdated = api.queryLastUpdated(fromTime, toTime);
            return lastUpdated.entrySet().stream().map(u -> u.getKey() + ":" + u.getValue())
                    .collect(joining(", ", "[id:date] ", ""));
        }).as("/updated/query").doesNotThrowAnyException();
    }

    @Test
    @Order(2)
    void getAvailableLastUpdatedQueryParameters(TheTVDBApi api) {
        assertThat(api::getAvailableLastUpdatedQueryParameters).as("/updated/query/params").doesNotThrowAnyException();
    }
}

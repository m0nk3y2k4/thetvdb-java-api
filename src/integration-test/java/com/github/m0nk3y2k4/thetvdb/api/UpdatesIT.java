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

    @Test @Order(1)
    void queryLastUpdated(TheTVDBApi api) {
        final long fromTime = Instant.now().minus(2, ChronoUnit.DAYS).getEpochSecond();
        final long toTime = Instant.now().minus(2, ChronoUnit.DAYS).plus(30, ChronoUnit.MINUTES).getEpochSecond();
        assertThat(() -> {
            Map<Long, Long> lastUpdated = api.queryLastUpdated(fromTime, toTime);
            return lastUpdated.entrySet().stream().map(u -> u.getKey() + ":" + u.getValue()).collect(joining(", ", "[id:date] ", ""));
        }).as("/updated/query").doesNotThrowAnyException();
    }

    @Test @Order(2)
    void getAvailableLastUpdatedQueryParameters(TheTVDBApi api) {
        assertThat(api::getAvailableLastUpdatedQueryParameters).as("/updated/query/params").doesNotThrowAnyException();
    }
}

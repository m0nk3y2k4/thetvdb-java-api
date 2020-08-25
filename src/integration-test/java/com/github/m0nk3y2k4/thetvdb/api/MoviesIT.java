package com.github.m0nk3y2k4.thetvdb.api;

import static com.github.m0nk3y2k4.thetvdb.testutils.assertj.IntegrationTestAssertions.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.github.m0nk3y2k4.thetvdb.testutils.annotation.IntegrationTestSuite;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@IntegrationTestSuite("Movies")
class MoviesIT {

    @Test @Order(1)
    void getMovie(TheTVDBApi api) {
        assertThat(() -> api.getMovie(2559)).as("/movies/2559").doesNotThrowAnyException();
    }

    @Test @Order(2)
    void getMovieUpdates(TheTVDBApi api) {
        final long since = Instant.now().minus(2, ChronoUnit.DAYS).getEpochSecond();
        assertThat(() -> api.getMovieUpdates(since)).as("/movieupdates").doesNotThrowAnyException();
    }
}

package com.github.m0nk3y2k4.thetvdb.api;

import static com.github.m0nk3y2k4.thetvdb.testutils.assertj.IntegrationTestAssertions.assertThat;

import com.github.m0nk3y2k4.thetvdb.testutils.annotation.IntegrationTestSuite;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@IntegrationTestSuite("Episodes")
class EpisodesIT {

    @Test @Order(1)
    void getEpisodes(TheTVDBApi api) {
        assertThat(() -> api.getEpisode(6647385)).as("/episodes/6647385").doesNotThrowAnyException();
    }
}

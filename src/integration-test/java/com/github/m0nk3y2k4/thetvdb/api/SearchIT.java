package com.github.m0nk3y2k4.thetvdb.api;

import static com.github.m0nk3y2k4.thetvdb.testutils.assertj.IntegrationTestAssertions.assertThat;

import com.github.m0nk3y2k4.thetvdb.testutils.annotation.IntegrationTestSuite;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@IntegrationTestSuite("Search")
class SearchIT {

    @Test @Order(1)
    void searchSeriesByName(TheTVDBApi api) {
        assertThat(() -> api.searchSeriesByName("Simpsons")).as("/search/series").doesNotThrowAnyException();
    }

    @Test @Order(2)
    void getAvailableSeriesSearchParameters(TheTVDBApi api) {
        assertThat(api::getAvailableSeriesSearchParameters).as("/search/series/params").doesNotThrowAnyException();
    }
}

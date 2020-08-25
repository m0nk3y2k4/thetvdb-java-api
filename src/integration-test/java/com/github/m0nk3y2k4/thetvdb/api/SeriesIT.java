package com.github.m0nk3y2k4.thetvdb.api;

import static com.github.m0nk3y2k4.thetvdb.testutils.assertj.IntegrationTestAssertions.assertThat;

import java.util.Map;
import java.util.stream.Collectors;

import com.github.m0nk3y2k4.thetvdb.testutils.annotation.IntegrationTestSuite;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@IntegrationTestSuite("Series")
class SeriesIT {

    @Test @Order(1)
    void getSeries(TheTVDBApi api) {
        assertThat(() -> api.getSeries(296762)).as("/series/296762").doesNotThrowAnyException();
    }

    @Test @Order(2)
    void getSeriesHeaderInformation(TheTVDBApi api) {
        assertThat(() -> {
            Map<String, String> seriesHeader = api.getSeriesHeaderInformation(296762);
            return seriesHeader.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(", "));
        }).as("/series/296762[Head]").doesNotThrowAnyException();
    }

    @Test @Order(3)
    void getActors(TheTVDBApi api) {
        assertThat(() -> api.getActors(296762)).as("/series/296762/actors").doesNotThrowAnyException();
    }

    @Test @Order(4)
    void getEpisodes(TheTVDBApi api) {
        assertThat(() -> api.getEpisodes(296762)).as("/series/296762/episodes").doesNotThrowAnyException();
    }

    @Test @Order(5)
    void queryEpisodesByAiredEpisode(TheTVDBApi api) {
        assertThat(() -> api.queryEpisodesByAiredEpisode(296762, 2)).as("/series/296762/episodes/query").doesNotThrowAnyException();
    }

    @Test @Order(6)
    void getAvailableEpisodeQueryParameters(TheTVDBApi api) {
        assertThat(() -> api.getAvailableEpisodeQueryParameters(296762)).as("/series/296762/episodes/query/params").doesNotThrowAnyException();
    }

    @Test @Order(7)
    void getSeriesEpisodesSummary(TheTVDBApi api) {
        assertThat(() -> api.getSeriesEpisodesSummary(296762)).as("/series/296762/episodes/summary").doesNotThrowAnyException();
    }

    @Test @Order(8)
    void filterSeries(TheTVDBApi api) {
        assertThat(() -> api.filterSeries(296762, "network").getNetwork()).as("/series/296762/filter").doesNotThrowAnyException();
    }

    @Test @Order(9)
    void getAvailableSeriesFilterParameters(TheTVDBApi api) {
        assertThat(() -> api.getAvailableSeriesFilterParameters(296762)).as("/series/296762/filter/params").doesNotThrowAnyException();
    }

    @Test @Order(10)
    void getSeriesImagesSummary(TheTVDBApi api) {
        assertThat(() -> api.getSeriesImagesSummary(296762)).as("/series/296762/images").doesNotThrowAnyException();
    }

    @Test @Order(11)
    void queryImages(TheTVDBApi api) {
        assertThat(() -> api.queryImages(296762, "fanart", "1920x1080")).as("/series/296762/images/query").doesNotThrowAnyException();
    }

    @Test @Order(12)
    void getAvailableImageQueryParameters(TheTVDBApi api) {
        assertThat(() -> api.getAvailableImageQueryParameters(296762)).as("/series/296762/images/query/params").doesNotThrowAnyException();
    }
}

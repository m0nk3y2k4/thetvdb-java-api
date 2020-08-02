package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SeriesSummaryDTOTest {

    @Test
    void toString_withSeriesAndEpisodes_verifyStringRepresentation() {
        assertThat(new SeriesSummaryDTO.Builder().addAiredSeasons("1", "2", "3", "4").airedEpisodes("54")
                .addDvdSeasons("1", "2").dvdEpisodes("23")
                .build()).asString().isEqualTo("[Seasons/Episodes] Aired: [1, 2, 3, 4]/54, DVD: [1, 2]/23");
    }
}
package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ImageSummaryDTOTest {

    @Test
    void toString_withCounts_verifyStringRepresentation() {
        assertThat(new ImageSummaryDTO.Builder().fanartCount(12L).posterCount(4L).seasonCount(2L).seasonwideCount(14L).seriesCount(6L)
                .build()).asString().isEqualTo("[Fanart] 12, [Poster] 4, [Season] 2, [Seasonwide] 14, [Series] 6");
    }

    @Test
    void toString_withoutCounts_verifyDefaultValuesInStringRepresentation() {
        assertThat(new ImageSummaryDTO.Builder()
                .build()).asString().isEqualTo("[Fanart] 0, [Poster] 0, [Season] 0, [Seasonwide] 0, [Series] 0");
    }
}
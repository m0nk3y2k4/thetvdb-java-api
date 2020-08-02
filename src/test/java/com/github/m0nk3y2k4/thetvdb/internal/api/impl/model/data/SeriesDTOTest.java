package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SeriesDTOTest {

    @Test
    void toString_withSeriesName_verifyStringRepresentation() {
        assertThat(new SeriesDTO.Builder().seriesName("The Expanse").build()).asString().isEqualTo("The Expanse");
    }
}
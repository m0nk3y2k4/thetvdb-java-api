package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EpisodeDTOTest {

    @Test
    void toString_withEpisodeName_verifyStringRepresentation() {
        assertThat(new EpisodeDTO.Builder().episodeName("In the Pale Moonlight").build()).asString().isEqualTo("In the Pale Moonlight");
    }
}
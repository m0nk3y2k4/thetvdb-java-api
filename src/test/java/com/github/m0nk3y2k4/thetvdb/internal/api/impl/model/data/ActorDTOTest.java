package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ActorDTOTest {

    @Test
    void toString_withName_verifyStringRepresentation() {
        assertThat(new ActorDTO.Builder().name("Christopher Lloyd").build()).asString().isEqualTo("Christopher Lloyd");
    }
}
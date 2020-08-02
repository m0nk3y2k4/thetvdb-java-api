package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UserDTOTest {

    @Test
    void toString_withUserName_verifyStringRepresentation() {
        assertThat(new UserDTO.Builder().userName("Ben Kenobi").build()).asString().isEqualTo("Ben Kenobi");
    }
}
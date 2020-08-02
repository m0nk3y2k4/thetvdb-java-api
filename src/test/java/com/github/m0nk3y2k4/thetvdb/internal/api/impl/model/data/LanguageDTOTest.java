package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LanguageDTOTest {

    @Test
    void toString_withName_verifyStringRepresentation() {
        assertThat(new LanguageDTO.Builder().name("English").build()).asString().isEqualTo("English");
    }
}
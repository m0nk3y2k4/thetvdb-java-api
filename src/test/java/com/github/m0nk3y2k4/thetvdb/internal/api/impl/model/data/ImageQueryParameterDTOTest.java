package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ImageQueryParameterDTOTest {

    @Test
    void toString_withKeyTypeAndResolution_verifyStringRepresentation() {
        assertThat(new ImageQueryParameterDTO.Builder().keyType("fanart").addResolution("1080x1920").build()).asString()
                .isEqualTo("fanart [1080x1920]");
    }
}
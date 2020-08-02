package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RatingDTOTest {

    @Test
    void toString_withTypeAndItemIdAndRating_verifyStringRepresentation() {
        assertThat(new RatingDTO.Builder().ratingType("image").ratingItemId(1101207L).rating(8.0).build()).asString()
                .isEqualTo("[image] 1101207: 8.0");
    }
}
package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Rating;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import org.immutables.value.Value.Immutable;

@Immutable
@WithHiddenImplementation
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = RatingDTO.Builder.class)
public abstract class RatingDTO implements Rating {

    @Override
    public String toString() {
        return String.format("[%s] %s: %s", getRatingType(), getRatingItemId(), getRating());
    }

    public static class Builder extends RatingDTOBuilder {}
}

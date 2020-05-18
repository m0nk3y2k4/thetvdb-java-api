package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesSearchResult;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import org.immutables.value.Value.Immutable;

@Immutable
@WithHiddenImplementation
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = SeriesSearchResultDTO.Builder.class)
public abstract class SeriesSearchResultDTO implements SeriesSearchResult {

    @Override
    public String toString() {
        return getSeriesName();
    }

    public static class Builder extends SeriesSearchResultDTOBuilder {}
}

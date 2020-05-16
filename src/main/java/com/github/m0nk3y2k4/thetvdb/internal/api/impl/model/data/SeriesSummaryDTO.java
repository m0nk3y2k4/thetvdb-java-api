package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesSummary;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;

@Immutable
@Style(visibility = Style.ImplementationVisibility.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesSummaryDTOBuilder.class)
public abstract class SeriesSummaryDTO implements SeriesSummary {

    @Override
    public String toString() {
        return String.format("[Seasons/Episodes] Aired: %s/%s, DVD: %s/%s", getAiredSeasons(), getAiredEpisodes(), getDvdSeasons(), getDvdEpisodes());
    }
}

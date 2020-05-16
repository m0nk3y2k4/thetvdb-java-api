package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ImageSummary;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;

import javax.annotation.Nullable;

@Immutable
@Style(visibility = Style.ImplementationVisibility.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ImageSummaryDTOBuilder.class)
public abstract class ImageSummaryDTO implements ImageSummary {

    @Override @JsonProperty("fanart")
    @Nullable public abstract Long getFanartCount();

    @Override @JsonProperty("poster")
    @Nullable public abstract Long getPosterCount();

    @Override @JsonProperty("season")
    @Nullable public abstract Long getSeasonCount();

    @Override @JsonProperty("seasonwide")
    @Nullable public abstract Long getSeasonwideCount();

    @Override @JsonProperty("series")
    @Nullable public abstract Long getSeriesCount();

    @Override
    public String toString() {
        final String def = "0";
        return String.format("[Fanart] %s, [Poster] %s, [Season] %s, [Seasonwide] %s, [Series] %s",
                APIUtil.toString(this::getFanartCount, def),
                APIUtil.toString(this::getPosterCount, def),
                APIUtil.toString(this::getSeasonCount, def),
                APIUtil.toString(this::getSeasonwideCount, def),
                APIUtil.toString(this::getSeriesCount, def));
    }
}

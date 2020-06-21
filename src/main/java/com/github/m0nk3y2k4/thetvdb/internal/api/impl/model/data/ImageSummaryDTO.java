package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ImageSummary;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;
import org.immutables.value.Value.Immutable;

/**
 * DTO implementation of the {@link ImageSummary} interface
 * <p><br>
 * Objects of this class reflect the data received by the remote service and are immutable so that their content can
 * not be changed once an instance has been created. New objects of this class may be created by using the corresponding
 * {@link Builder}.
 */
@Immutable
@WithHiddenImplementation
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = ImageSummaryDTO.Builder.class)
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

    /**
     * Builder used to create a new immutable {@link ImageSummaryDTO} implementation
     * <p><br>
     * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link ImageSummaryDTO} instance
     * based on these properties. New builders may be initialized with some existing DTO instance, which presets the builders properties
     * to the values of the given DTO, still retaining the option to make additional changes before actually building a new immutable object.
     */
    public static class Builder extends ImageSummaryDTOBuilder {}
}

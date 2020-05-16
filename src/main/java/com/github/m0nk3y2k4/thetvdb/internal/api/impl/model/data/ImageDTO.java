package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Image;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

@Immutable
@Style(visibility = Style.ImplementationVisibility.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ImageDTOBuilder.class)
public abstract class ImageDTO implements Image {

    @Override
    @Nullable public Double getRatingAverage() {
        return Optional.ofNullable(getRatingsInfo()).map(ri -> ri.get("average")).map(Integer.class::cast).map(Double::valueOf).orElse(null);
    }

    @Override
    @Nullable public Integer getRatingCount() {
        return Optional.ofNullable(getRatingsInfo()).map(ri -> ri.get("count")).map(Integer.class::cast).orElse(null);
    }

    @JsonProperty("ratingsInfo")
    abstract Map<String,Object> getRatingsInfo();

    @Override
    public String toString() {
        return String.format("[%s] %s", getKeyType(), getFileName());
    }
}

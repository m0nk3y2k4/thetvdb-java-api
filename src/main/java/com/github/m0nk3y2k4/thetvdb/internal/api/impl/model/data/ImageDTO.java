package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Image;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import org.immutables.value.Value.Immutable;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

/**
 * DTO implementation of the {@link Image} interface
 * <p>
 * Objects of this class reflect the data received by the remote service and are immutable so that their content can
 * not be changed once an instance has been created. New objects of this class may be created by using the corresponding
 * {@link Builder}.
 */
@Immutable
@WithHiddenImplementation
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = ImageDTO.Builder.class)
public abstract class ImageDTO implements Image {

    @Override
    @Nullable public Double getRatingAverage() {
        return Optional.ofNullable(getRatingsInfo()).map(ri -> ri.get("average")).map(Integer.class::cast).map(Double::valueOf).orElse(null);
    }

    @Override
    @Nullable public Integer getRatingCount() {
        return Optional.ofNullable(getRatingsInfo()).map(ri -> ri.get("count")).map(Integer.class::cast).orElse(null);
    }

    /**
     * Wraps the content of the Images' nested <code>ratingsInfo</code> JSON node in order to flatten the hierarchy of this DTO.
     *
     * @see #getRatingAverage()
     * @see #getRatingCount()
     *
     * @return Content of <code>ratingsInfo</code> as key/value pairs
     */
    @JsonProperty("ratingsInfo")
    abstract Map<String,Object> getRatingsInfo();

    @Override
    public String toString() {
        return String.format("[%s] %s", getKeyType(), getFileName());
    }

    /**
     * Builder used to create a new immutable {@link ImageDTO} implementation
     * <p>
     * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link ImageDTO} instance
     * based on these properties. New builders may be initialized with some existing DTO instance, which presets the builders properties
     * to the values of the given DTO, still retaining the option to make additional changes before actually building a new immutable object.
     */
    public static class Builder extends ImageDTOBuilder {}
}

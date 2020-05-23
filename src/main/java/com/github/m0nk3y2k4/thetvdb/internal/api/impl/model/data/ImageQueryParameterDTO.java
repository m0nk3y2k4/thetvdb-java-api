package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ImageQueryParameter;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import org.immutables.value.Value.Immutable;

/**
 * DTO implementation of the {@link ImageQueryParameter} interface
 * <p/>
 * Objects of this class reflect the data received by the remote service and are immutable so that their content can
 * not be changed once an instance has been created. New objects of this class may be created by using the corresponding
 * {@link Builder}.
 */
@Immutable
@WithHiddenImplementation
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = ImageQueryParameterDTO.Builder.class)
public abstract class ImageQueryParameterDTO implements ImageQueryParameter {

    @Override
    public String toString() {
        return String.format("%s %s", getKeyType(), getResolution());
    }

    /**
     * Builder used to create a new immutable {@link ImageQueryParameterDTO} implementation
     * <p/>
     * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link ImageQueryParameterDTO} instance
     * based on these properties. New builders may be initialized with some existing DTO instance, which presets the builders properties
     * to the values of the given DTO, still retaining the option to make additional changes before actually building a new immutable object.
     */
    public static class Builder extends ImageQueryParameterDTOBuilder {}
}

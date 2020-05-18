package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ImageQueryParameter;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import org.immutables.value.Value.Immutable;

@Immutable
@WithHiddenImplementation
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = ImageQueryParameterDTO.Builder.class)
public abstract class ImageQueryParameterDTO implements ImageQueryParameter {

    @Override
    public String toString() {
        return String.format("%s %s", getKeyType(), getResolution());
    }

    public static class Builder extends ImageQueryParameterDTOBuilder {}
}

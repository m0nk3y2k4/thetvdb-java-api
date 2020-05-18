package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import org.immutables.value.Value.Immutable;

@Immutable
@WithHiddenImplementation
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = APIResponseDTO.Builder.class)
public abstract class APIResponseDTO<T> implements APIResponse<T> {

    @Override
    public String toString() {
        return String.format("Data: [%s], Errors: [%s], Links: [%s]",
                APIUtil.toString(this::getData),
                APIUtil.toString(this::getErrors),
                APIUtil.toString(this::getLinks));
    }

    public static class Builder<T> extends APIResponseDTOBuilder<T> {}

    @Immutable
    @WithHiddenImplementation
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonDeserialize(builder = JSONErrorsDTO.Builder.class)
    public abstract static class JSONErrorsDTO implements JSONErrors {

        @Override
        public String toString() {
            return String.format("Filters: [%s], Laguage: %s, QueryParams: [%s]",
                    APIUtil.toString(this::getInvalidFilters),
                    APIUtil.toString(this::getInvalidLanguage),
                    APIUtil.toString(this::getInvalidQueryParams));
        }

        public static class Builder extends JSONErrorsDTOBuilder {}
    }

    @Immutable
    @WithHiddenImplementation
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonDeserialize(builder = LinksDTO.Builder.class)
    public abstract static class LinksDTO implements Links {

        @Override
        public String toString() {
            return String.format("First: %s, Last: %s, Next: %s, Previous: %s",
                    APIUtil.toString(this::getFirst),
                    APIUtil.toString(this::getLast),
                    APIUtil.toString(this::getNext),
                    APIUtil.toString(this::getPrevious));
        }

        public static class Builder extends LinksDTOBuilder {}
    }
}

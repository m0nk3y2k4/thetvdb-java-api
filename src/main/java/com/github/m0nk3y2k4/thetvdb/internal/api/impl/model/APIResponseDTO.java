package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import org.immutables.value.Value.Immutable;

/**
 * DTO implementation of the {@link APIResponse} interface
 * <p>
 * Objects of this class reflect the data received by the remote service and are immutable so that their content can
 * not be changed once an instance has been created. New objects of this class may be created by using the corresponding
 * {@link Builder}.
 */
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

    /**
     * Builder used to create a new immutable {@link APIResponseDTO} implementation
     * <p>
     * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link APIResponseDTO} instance
     * based on these properties. New builders may be initialized with some existing DTO instance, which presets the builders properties
     * to the values of the given DTO, still retaining the option to make additional changes before actually building a new immutable object.
     */
    public static class Builder<T> extends APIResponseDTOBuilder<T> {}

    /**
     * DTO implementation of the {@link JSONErrors} interface
     * <p>
     * Objects of this class reflect the data received by the remote service and are immutable so that their content can
     * not be changed once an instance has been created. New objects of this class may be created by using the corresponding
     * {@link Builder}.
     */
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

        /**
         * Builder used to create a new immutable {@link JSONErrorsDTO} implementation
         * <p>
         * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link JSONErrorsDTO} instance
         * based on these properties. New builders may be initialized with some existing DTO instance, which presets the builders properties
         * to the values of the given DTO, still retaining the option to make additional changes before actually building a new immutable object.
         */
        public static class Builder extends JSONErrorsDTOBuilder {}
    }

    /**
     * DTO implementation of the {@link Links} interface
     * <p>
     * Objects of this class reflect the data received by the remote service and are immutable so that their content can
     * not be changed once an instance has been created. New objects of this class may be created by using the corresponding
     * {@link Builder}.
     */
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

        /**
         * Builder used to create a new immutable {@link LinksDTO} implementation
         * <p>
         * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link LinksDTO} instance
         * based on these properties. New builders may be initialized with some existing DTO instance, which presets the builders properties
         * to the values of the given DTO, still retaining the option to make additional changes before actually building a new immutable object.
         */
        public static class Builder extends LinksDTOBuilder {}
    }
}

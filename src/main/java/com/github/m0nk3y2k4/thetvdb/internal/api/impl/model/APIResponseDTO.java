/*
 * Copyright (C) 2019 - 2022 thetvdb-java-api Authors and Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model;

import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;
import org.immutables.value.Value.Immutable;

/**
 * DTO implementation of the {@link APIResponse} interface
 * <p><br>
 * Objects of this class reflect the data received from the remote service and are immutable so that their content can
 * not be changed once an instance has been created. New objects of this class may be created by using the corresponding
 * {@link Builder}.
 */
@Immutable
@WithHiddenImplementation
@JsonDeserialize(builder = APIResponseDTO.Builder.class)
public abstract class APIResponseDTO<T> implements APIResponse<T> {

    @Override
    public String toString() {
        return String.format("Data: [%s], Status: [%s], Links: [%s]",
                APIUtil.toString(this::getData),
                APIUtil.toString(this::getStatus),
                APIUtil.toString(this::getLinks));
    }

    /**
     * Builder used to create a new immutable {@link APIResponseDTO} implementation
     * <p><br>
     * This builder provides a fluent API for setting certain object properties and creating a new immutable
     * {@link APIResponseDTO} instance based on these properties. New builders may be initialized with some existing DTO
     * instance, which presets the builders properties to the values of the given DTO, still retaining the option to
     * make additional changes before actually building a new immutable object.
     */
    public static class Builder<T> extends APIResponseDTOBuilder<T> {}

    /**
     * DTO implementation of the {@link Links} interface
     * <p><br>
     * Objects of this class reflect the data received by the remote service and are immutable so that their content can
     * not be changed once an instance has been created. New objects of this class may be created by using the
     * corresponding {@link Builder}.
     */
    @Immutable
    @WithHiddenImplementation
    @JsonDeserialize(builder = LinksDTO.Builder.class)
    public abstract static class LinksDTO implements Links {

        @Override
        @JsonAlias("prev")
        public abstract Optional<String> getPrevious();

        @Override
        @JsonAlias("total_items")
        public abstract OptionalInt getTotalItems();

        @Override
        @JsonAlias("page_size")
        public abstract OptionalInt getPageSize();

        @Override
        public String toString() {
            return String.format("Previous: %s, Self: %s, Next: %s, Total items: %s, Page size: %s",
                    APIUtil.toString(this::getPrevious),
                    APIUtil.toString(this::getSelf),
                    APIUtil.toString(this::getNext),
                    APIUtil.toString(this::getTotalItems),
                    APIUtil.toString(this::getPageSize));
        }

        /**
         * Builder used to create a new immutable {@link LinksDTO} implementation
         * <p><br>
         * This builder provides a fluent API for setting certain object properties and creating a new immutable
         * {@link LinksDTO} instance based on these properties. New builders may be initialized with some existing DTO
         * instance, which presets the builders properties to the values of the given DTO, still retaining the option to
         * make additional changes before actually building a new immutable object.
         */
        public static class Builder extends LinksDTOBuilder {}
    }
}

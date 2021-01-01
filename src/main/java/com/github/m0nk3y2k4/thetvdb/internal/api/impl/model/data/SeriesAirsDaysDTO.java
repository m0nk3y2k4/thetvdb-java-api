/*
 * Copyright (C) 2019 - 2021 thetvdb-java-api Authors and Contributors
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

package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesAirsDays;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.APIDataModel;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import org.immutables.value.Value.Immutable;

/**
 * DTO implementation of the {@link SeriesAirsDays} interface
 * <p><br>
 * Objects of this class reflect the data received by the remote service and are immutable so that their content can not
 * be changed once an instance has been created. New objects of this class may be created by using the corresponding
 * {@link Builder}.
 */
@Immutable
@APIDataModel
@WithHiddenImplementation
@JsonDeserialize(builder = SeriesAirsDaysDTO.Builder.class)
public abstract class SeriesAirsDaysDTO implements SeriesAirsDays {

    @Override
    @JsonProperty("monday")
    public abstract Boolean onMonday();

    @Override
    @JsonProperty("tuesday")
    public abstract Boolean onTuesday();

    @Override
    @JsonProperty("wednesday")
    public abstract Boolean onWednesday();

    @Override
    @JsonProperty("thursday")
    public abstract Boolean onThursday();

    @Override
    @JsonProperty("friday")
    public abstract Boolean onFriday();

    @Override
    @JsonProperty("saturday")
    public abstract Boolean onSaturday();

    @Override
    @JsonProperty("sunday")
    public abstract Boolean onSunday();

    /**
     * Builder used to create a new immutable {@link SeriesAirsDaysDTO} implementation
     * <p><br>
     * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link
     * SeriesAirsDaysDTO} instance based on these properties. New builders may be initialized with some existing DTO
     * instance, which presets the builders properties to the values of the given DTO, still retaining the option to
     * make additional changes before actually building a new immutable object.
     */
    public static class Builder extends SeriesAirsDaysDTOBuilder {}
}

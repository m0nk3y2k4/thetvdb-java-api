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

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesAirsDays;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.APIDataModel;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import org.immutables.value.Value.Immutable;

/**
 * DTO implementation of the {@link SeriesAirsDays} interface
 * <p><br>
 * Objects of this class reflect the data received from the remote service and are immutable so that their content can
 * not be changed once an instance has been created. New objects of this class may be created by using the corresponding
 * {@link Builder}.
 */
@Immutable
@APIDataModel
@WithHiddenImplementation
@JsonDeserialize(builder = SeriesAirsDaysDTO.Builder.class)
public abstract class SeriesAirsDaysDTO implements SeriesAirsDays {

    @Override
    @Nullable
    @JsonAlias("monday")
    public abstract Boolean onMonday();

    @Override
    @Nullable
    @JsonAlias("tuesday")
    public abstract Boolean onTuesday();

    @Override
    @Nullable
    @JsonAlias("wednesday")
    public abstract Boolean onWednesday();

    @Override
    @Nullable
    @JsonAlias("thursday")
    public abstract Boolean onThursday();

    @Override
    @Nullable
    @JsonAlias("friday")
    public abstract Boolean onFriday();

    @Override
    @Nullable
    @JsonAlias("saturday")
    public abstract Boolean onSaturday();

    @Override
    @Nullable
    @JsonAlias("sunday")
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

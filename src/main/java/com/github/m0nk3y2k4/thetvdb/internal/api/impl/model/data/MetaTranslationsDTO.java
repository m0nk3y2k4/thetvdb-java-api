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

package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.data.EntityTranslation;
import com.github.m0nk3y2k4.thetvdb.api.model.data.MetaTranslations;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Translations;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.APIDataModel;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

/**
 * DTO implementation of the {@link MetaTranslations} interface
 * <p><br>
 * Objects of this class reflect the data received from the remote service and are immutable so that their content can
 * not be changed once an instance has been created. New objects of this class may be created by using the corresponding
 * {@link Builder}.
 */
@Immutable
@APIDataModel
@WithHiddenImplementation
@JsonDeserialize(builder = MetaTranslationsDTO.Builder.class)
public abstract class MetaTranslationsDTO implements MetaTranslations {

    @Default
    @Override
    @JsonSetter(nulls = Nulls.SKIP)
    public Translations<EntityTranslation> getNameTranslations() {
        return new TranslationsDTO.Builder<EntityTranslation>().build();
    }

    @Default
    @Override
    @JsonSetter(nulls = Nulls.SKIP)
    public Translations<EntityTranslation> getOverviewTranslations() {
        return new TranslationsDTO.Builder<EntityTranslation>().build();
    }

    /**
     * Builder used to create a new immutable {@link MetaTranslationsDTO} implementation
     * <p><br>
     * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link
     * MetaTranslationsDTO} instance based on these properties. New builders may be initialized with some existing DTO
     * instance, which presets the builders properties to the values of the given DTO, still retaining the option to
     * make additional changes before actually building a new immutable object.
     */
    public static class Builder extends MetaTranslationsDTOBuilder {}
}

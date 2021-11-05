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

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.data.RemoteId;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SearchResult;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SearchResultTranslation;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Translations;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.APIDataModel;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import com.github.m0nk3y2k4.thetvdb.internal.util.json.converter.SearchResultConverter;
import com.github.m0nk3y2k4.thetvdb.internal.util.json.converter.TranslationsConverter;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

/**
 * DTO implementation of the {@link SearchResult} interface
 * <p><br>
 * Objects of this class reflect the data received from the remote service and are immutable so that their content can
 * not be changed once an instance has been created. New objects of this class may be created by using the corresponding
 * {@link Builder}.
 */
@Immutable
@APIDataModel
@WithHiddenImplementation
@JsonDeserialize(builder = SearchResultDTO.Builder.class)
public abstract class SearchResultDTO implements SearchResult {

    @Override
    @Nullable
    @JsonAlias("extended_title")
    // ToDo: Property is declared as "extendedTitle" in API documentation but send as "extended_title" in JSON. Check again after next API update.
    public abstract String getExtendedTitle();

    @Override
    @Nullable
    @JsonAlias("image_url")
    // ToDo: Property is declared as "imageUrl" in API documentation but send as "image_url" in JSON. Check again after next API update.
    public abstract String getImageUrl();

    @Default
    @Override
    @JsonAlias("name_translated")
    @JsonSetter(nulls = Nulls.SKIP)
    @JsonDeserialize(converter = SearchResultConverter.TranslationString.class)
    // ToDo: Property is declared as "nameTranslated" in API documentation but send as "name_translated" in JSON. Check again after next API update.
    public Translations<SearchResultTranslation> getNameTranslated() {
        return new TranslationsDTO.Builder<SearchResultTranslation>().build();
    }

    @Default
    @Override
    @JsonAlias("overview_translated")
    @JsonSetter(nulls = Nulls.SKIP)
    @JsonDeserialize(converter = TranslationsConverter.class, contentConverter = SearchResultConverter.TranslationListItem.class)
    public Translations<SearchResultTranslation> getOverviewTranslated() {
        return new TranslationsDTO.Builder<SearchResultTranslation>().build();
    }

    @Override
    @Nullable
    @JsonAlias("primary_language")
    // ToDo: Property is declared as "primaryLanguage" in API documentation but send as "primary_language" in JSON. Check again after next API update.
    public abstract String getPrimaryLanguage();

    @Override
    @Nullable
    @JsonAlias("primary_type")
    // ToDo: Property is declared as "primaryType" in API documentation but send as "primary_type" in JSON. Check again after next API update.
    public abstract String getPrimaryType();

    @Override
    @Nullable
    @JsonAlias("tvdb_id")
    public abstract String getTvdbId();

    @Override
    @JsonAlias("remote_ids")
    // ToDo: Property is declared as "remoteIds" in API documentation but send as "remote_ids" in JSON. Check again after next API update.
    public abstract List<RemoteId> getRemoteIds();

    @Override
    @Nullable
    @JsonAlias("first_air_time")
    public abstract String getFirstAirTime();

    @Override
    @Nullable
    @JsonAlias("is_official")
    public abstract Boolean isOfficial();

    @Default
    @Override
    @JsonSetter(nulls = Nulls.SKIP)
    @JsonDeserialize(converter = SearchResultConverter.TranslationObject.class)
    public Translations<SearchResultTranslation> getTranslations() {
        return new TranslationsDTO.Builder<SearchResultTranslation>().build();
    }

    @Default
    @Override
    @JsonSetter(nulls = Nulls.SKIP)
    @JsonDeserialize(converter = SearchResultConverter.TranslationObject.class)
    public Translations<SearchResultTranslation> getOverviews() {
        return new TranslationsDTO.Builder<SearchResultTranslation>().build();
    }

    /**
     * Builder used to create a new immutable {@link SearchResultDTO} implementation
     * <p><br>
     * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link
     * SearchResultDTO} instance based on these properties. New builders may be initialized with some existing DTO
     * instance, which presets the builders properties to the values of the given DTO, still retaining the option to
     * make additional changes before actually building a new immutable object.
     */
    public static class Builder extends SearchResultDTOBuilder {}
}

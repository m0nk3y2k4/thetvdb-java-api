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
import java.util.Optional;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.data.RemoteId;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SearchResult;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.APIDataModel;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import com.github.m0nk3y2k4.thetvdb.internal.util.json.converter.SearchResultConverter;
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

    /**
     * Creates a new immutable {@link SearchResult.Translation} object with the given values
     *
     * @param language    The corresponding language code
     * @param translation The actual translation
     *
     * @return New immutable search result translation DTO with the given translation information
     */
    public static Translation createTranslationDTO(Optional<String> language, Optional<String> translation) {
        return new SearchResultTranslationDTOBuilder().language(language).translation(translation).build();
    }

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

    @Override
    @JsonAlias("name_translated")
    @JsonDeserialize(converter = SearchResultConverter.TranslationString.class)
    // ToDo: Property is declared as "nameTranslated" in API documentation but send as "name_translated" in JSON. Check again after next API update.
    public abstract List<Translation> getNameTranslated();

    @Override
    @JsonAlias("overview_translated")
    @JsonDeserialize(contentConverter = SearchResultConverter.TranslationListItem.class)
    public abstract List<Translation> getOverviewTranslated();

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

    @Override
    @JsonDeserialize(converter = SearchResultConverter.TranslationObject.class)
    public abstract List<Translation> getTranslations();

    @Override
    @JsonDeserialize(converter = SearchResultConverter.TranslationObject.class)
    public abstract List<Translation> getOverviews();

    /**
     * Builder used to create a new immutable {@link SearchResultDTO} implementation
     * <p><br>
     * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link
     * SearchResultDTO} instance based on these properties. New builders may be initialized with some existing DTO
     * instance, which presets the builders properties to the values of the given DTO, still retaining the option to
     * make additional changes before actually building a new immutable object.
     */
    public static class Builder extends SearchResultDTOBuilder {}

    /**
     * DTO implementation of the {@link SearchResult.Translation} interface
     * <p><br>
     * Objects of this class reflect the data received by the remote service and are immutable so that their content can
     * not be changed once an instance has been created. New objects of this class may be created by using the the
     * {@link SearchResultDTO#createTranslationDTO(Optional, Optional) createTranslationDTO(language, translation)} util
     * method.
     */
    @Immutable
    @WithHiddenImplementation
    public abstract static class SearchResultTranslationDTO implements Translation {

    }
}

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

import static com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO.PeopleCategory.ACTORS;
import static com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO.PeopleCategory.DIRECTORS;
import static com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO.PeopleCategory.PRODUCERS;
import static com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO.PeopleCategory.WRITERS;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Movie;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;
import org.immutables.value.Value.Auxiliary;
import org.immutables.value.Value.Immutable;

/**
 * DTO implementation of the {@link Movie} interface
 * <p><br>
 * Objects of this class reflect the data received by the remote service and are immutable so that their content can not
 * be changed once an instance has been created. New objects of this class may be created by using the corresponding
 * {@link Builder}.
 */
@Immutable
@WithHiddenImplementation
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = MovieDTO.Builder.class)
public abstract class MovieDTO implements Movie {

    /**
     * Simple enum representing the different categories of people associated with this movie
     */
    public enum PeopleCategory {
        ACTORS, DIRECTORS, PRODUCERS, WRITERS;

        /**
         * Return the enum name in lower-case. Will be used by the JSON deserializer in order to resolve a JSON property
         * name to a specific enum.
         *
         * @return The enum name in its corresponding JSON property notation
         */
        @JsonValue
        public String getJsonName() {
            return name().toLowerCase();
        }
    }

    @Override
    public List<People> getActors() {
        return getPeople(ACTORS);
    }

    @Override
    public List<People> getDirectors() {
        return getPeople(DIRECTORS);
    }

    @Override
    public List<People> getProducers() {
        return getPeople(PRODUCERS);
    }

    @Override
    public List<People> getWriters() {
        return getPeople(WRITERS);
    }

    @Override
    @JsonProperty("release_dates")
    public abstract List<ReleaseDate> getReleaseDates();

    @Override
    @JsonProperty("remoteids")
    public abstract List<RemoteId> getRemoteIds();

    /**
     * Map representing the JSON's inline "people" node
     *
     * @return People from the JSON mapped by their corresponding category
     */
    @Auxiliary
    abstract Map<PeopleCategory, List<People>> getPeople();

    /**
     * Returns a list of people associated to the given category. If no people are present for the given category an
     * empty list will be returned.
     *
     * @param category The category of people to be returned
     *
     * @return List of corresponding people or an empty list of no people are associated with the given category
     */
    private List<People> getPeople(PeopleCategory category) {
        return unmodifiableList(getPeople().getOrDefault(category, emptyList()));
    }

    /**
     * Returns the name of the movie based on its primary translation. Typically this is the english movie name.
     *
     * @return Optional representing the name of this movie or an empty Optional if no primary translation is available
     */
    private Optional<String> getName() {
        Predicate<Translation> isPrimary = t -> Optional.ofNullable(t.isPrimary()).orElse(false);
        return getTranslations().stream().filter(isPrimary).map(Translation::getName).findFirst();
    }

    @Override
    public String toString() {
        return APIUtil.toString(this::getName, "n/a");
    }

    /**
     * Builder used to create a new immutable {@link MovieDTO} implementation
     * <p><br>
     * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link
     * MovieDTO} instance based on these properties. New builders may be initialized with some existing DTO instance,
     * which presets the builders properties to the values of the given DTO, still retaining the option to make
     * additional changes before actually building a new immutable object.
     */
    public static class Builder extends MovieDTOBuilder {}

    /**
     * DTO implementation of the {@link Artwork} interface
     * <p><br>
     * Objects of this class reflect the data received by the remote service and are immutable so that their content can
     * not be changed once an instance has been created. New objects of this class may be created by using the
     * corresponding {@link Builder}.
     */
    @Immutable
    @WithHiddenImplementation
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonDeserialize(builder = ArtworkDTO.Builder.class)
    public abstract static class ArtworkDTO implements Artwork {

        @Override
        @JsonProperty("artwork_type")
        @Nullable
        public abstract String getArtworkType();

        @Override
        @JsonProperty("is_primary")
        @Nullable
        public abstract Boolean isPrimary();

        @Override
        @JsonProperty("thumb_url")
        @Nullable
        public abstract String getThumbUrl();

        @Override
        public String toString() {
            return String.format("[%s] %s: %dx%d", getId(), getArtworkType(), getWidth(), getHeight());
        }

        /**
         * Builder used to create a new immutable {@link ArtworkDTO} implementation
         * <p><br>
         * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link
         * ArtworkDTO} instance based on these properties. New builders may be initialized with some existing DTO
         * instance, which presets the builders properties to the values of the given DTO, still retaining the option to
         * make additional changes before actually building a new immutable object.
         */
        public static class Builder extends ArtworkDTOBuilder {}
    }

    /**
     * DTO implementation of the {@link Genre} interface
     * <p><br>
     * Objects of this class reflect the data received by the remote service and are immutable so that their content can
     * not be changed once an instance has been created. New objects of this class may be created by using the
     * corresponding {@link Builder}.
     */
    @Immutable
    @WithHiddenImplementation
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonDeserialize(builder = GenreDTO.Builder.class)
    public abstract static class GenreDTO implements Genre {

        @Override
        public String toString() {
            return this.getName();
        }

        /**
         * Builder used to create a new immutable {@link GenreDTO} implementation
         * <p><br>
         * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link
         * GenreDTO} instance based on these properties. New builders may be initialized with some existing DTO
         * instance, which presets the builders properties to the values of the given DTO, still retaining the option to
         * make additional changes before actually building a new immutable object.
         */
        public static class Builder extends GenreDTOBuilder {}
    }

    /**
     * DTO implementation of the {@link ReleaseDate} interface
     * <p><br>
     * Objects of this class reflect the data received by the remote service and are immutable so that their content can
     * not be changed once an instance has been created. New objects of this class may be created by using the
     * corresponding {@link Builder}.
     */
    @Immutable
    @WithHiddenImplementation
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonDeserialize(builder = ReleaseDateDTO.Builder.class)
    public abstract static class ReleaseDateDTO implements ReleaseDate {

        @Override
        public String toString() {
            return String.format("[%s] %s: %s", getCountry(), getType(), getDate());
        }

        /**
         * Builder used to create a new immutable {@link ReleaseDateDTO} implementation
         * <p><br>
         * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link
         * ReleaseDateDTO} instance based on these properties. New builders may be initialized with some existing DTO
         * instance, which presets the builders properties to the values of the given DTO, still retaining the option to
         * make additional changes before actually building a new immutable object.
         */
        public static class Builder extends ReleaseDateDTOBuilder {}
    }

    /**
     * DTO implementation of the {@link RemoteId} interface
     * <p><br>
     * Objects of this class reflect the data received by the remote service and are immutable so that their content can
     * not be changed once an instance has been created. New objects of this class may be created by using the
     * corresponding {@link Builder}.
     */
    @Immutable
    @WithHiddenImplementation
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonDeserialize(builder = RemoteIdDTO.Builder.class)
    public abstract static class RemoteIdDTO implements RemoteId {

        @Override
        @JsonProperty("source_id")
        @Nullable
        public abstract Long getSourceId();

        @Override
        @JsonProperty("source_name")
        @Nullable
        public abstract String getSourceName();

        @Override
        @JsonProperty("source_url")
        @Nullable
        public abstract String getSourceUrl();

        @Override
        public String toString() {
            return getSourceName();
        }

        /**
         * Builder used to create a new immutable {@link RemoteIdDTO} implementation
         * <p><br>
         * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link
         * RemoteIdDTO} instance based on these properties. New builders may be initialized with some existing DTO
         * instance, which presets the builders properties to the values of the given DTO, still retaining the option to
         * make additional changes before actually building a new immutable object.
         */
        public static class Builder extends RemoteIdDTOBuilder {}
    }

    /**
     * DTO implementation of the {@link Trailer} interface
     * <p><br>
     * Objects of this class reflect the data received by the remote service and are immutable so that their content can
     * not be changed once an instance has been created. New objects of this class may be created by using the
     * corresponding {@link Builder}.
     */
    @Immutable
    @WithHiddenImplementation
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonDeserialize(builder = TrailerDTO.Builder.class)
    public abstract static class TrailerDTO implements Trailer {

        @Override
        public String toString() {
            return this.getName();
        }

        /**
         * Builder used to create a new immutable {@link TrailerDTO} implementation
         * <p><br>
         * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link
         * TrailerDTO} instance based on these properties. New builders may be initialized with some existing DTO
         * instance, which presets the builders properties to the values of the given DTO, still retaining the option to
         * make additional changes before actually building a new immutable object.
         */
        public static class Builder extends TrailerDTOBuilder {}
    }

    /**
     * DTO implementation of the {@link Translation} interface
     * <p><br>
     * Objects of this class reflect the data received by the remote service and are immutable so that their content can
     * not be changed once an instance has been created. New objects of this class may be created by using the
     * corresponding {@link Builder}.
     */
    @Immutable
    @WithHiddenImplementation
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonDeserialize(builder = TranslationDTO.Builder.class)
    public abstract static class TranslationDTO implements Translation {

        @Override
        @JsonProperty("is_primary")
        @Nullable
        public abstract Boolean isPrimary();

        @Override
        @JsonProperty("language_code")
        @Nullable
        public abstract String getLanguageCode();

        @Override
        public String toString() {
            return this.getName();
        }

        /**
         * Builder used to create a new immutable {@link TranslationDTO} implementation
         * <p><br>
         * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link
         * TranslationDTO} instance based on these properties. New builders may be initialized with some existing DTO
         * instance, which presets the builders properties to the values of the given DTO, still retaining the option to
         * make additional changes before actually building a new immutable object.
         */
        public static class Builder extends TranslationDTOBuilder {}
    }

    /**
     * DTO implementation of the {@link People} interface
     * <p><br>
     * Objects of this class reflect the data received by the remote service and are immutable so that their content can
     * not be changed once an instance has been created. New objects of this class may be created by using the
     * corresponding {@link Builder}.
     */
    @Immutable
    @WithHiddenImplementation
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonDeserialize(builder = PeopleDTO.Builder.class)
    public abstract static class PeopleDTO implements People {

        @Override
        @JsonProperty("imdb_id")
        @Nullable
        public abstract String getImdbId();

        @Override
        @JsonProperty("is_featured")
        @Nullable
        public abstract Boolean isFeatured();

        @Override
        @JsonProperty("people_facebook")
        @Nullable
        public abstract String getPeopleFacebook();

        @Override
        @JsonProperty("people_id")
        @Nullable
        public abstract String getPeopleId();

        @Override
        @JsonProperty("people_image")
        @Nullable
        public abstract String getPeopleImage();

        @Override
        @JsonProperty("people_instagram")
        @Nullable
        public abstract String getPeopleInstagram();

        @Override
        @JsonProperty("people_twitter")
        @Nullable
        public abstract String getPeopleTwitter();

        @Override
        @JsonProperty("role_image")
        @Nullable
        public abstract String getRoleImage();

        @Override
        public String toString() {
            return this.getName();
        }

        /**
         * Builder used to create a new immutable {@link PeopleDTO} implementation
         * <p><br>
         * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link
         * PeopleDTO} instance based on these properties. New builders may be initialized with some existing DTO
         * instance, which presets the builders properties to the values of the given DTO, still retaining the option to
         * make additional changes before actually building a new immutable object.
         */
        public static class Builder extends PeopleDTOBuilder {}
    }
}

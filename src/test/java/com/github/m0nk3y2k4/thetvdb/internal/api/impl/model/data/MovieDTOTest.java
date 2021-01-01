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

import static com.github.m0nk3y2k4.thetvdb.api.model.data.Movie.People;
import static com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO.ArtworkDTO;
import static com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO.GenreDTO;
import static com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO.PeopleCategory;
import static com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO.PeopleCategory.ACTORS;
import static com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO.PeopleCategory.DIRECTORS;
import static com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO.PeopleCategory.PRODUCERS;
import static com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO.PeopleCategory.WRITERS;
import static com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO.PeopleDTO;
import static com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO.ReleaseDateDTO;
import static com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO.RemoteIdDTO;
import static com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO.TrailerDTO;
import static com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO.TranslationDTO;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.api.model.data.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class MovieDTOTest {

    private static Stream<Arguments> people() {
        return Stream.of(
                Arguments.of(ACTORS, (Function<Movie, List<People>>)Movie::getActors),
                Arguments.of(DIRECTORS, (Function<Movie, List<People>>)Movie::getDirectors),
                Arguments.of(PRODUCERS, (Function<Movie, List<People>>)Movie::getProducers),
                Arguments.of(WRITERS, (Function<Movie, List<People>>)Movie::getWriters)
        );
    }

    @ParameterizedTest(name = "[{index}] Provides matching lower-case JSON name for {0}")
    @EnumSource(PeopleCategory.class)
    void peopleCategories_verifyJsonNames(PeopleCategory category) {
        assertThat(category.getJsonName()).isEqualTo(category.name().toLowerCase());
    }

    @ParameterizedTest(name = "[{index}] Returns correct values for getting people of category {0}")
    @MethodSource("people")
    void getPeople_withDifferentSettings_verifyReturnValue(PeopleCategory category,
            Function<Movie, List<People>> getPeople) {
        List<People> peopleOfCategory = Collections.singletonList(new PeopleDTO.Builder().name("Someone").build());
        Movie movieWithPeopleForCategory = new MovieDTO.Builder().putPeople(category, peopleOfCategory).build();
        Movie movieWithoutPeopleForCategory = new MovieDTO.Builder().build();

        assertThat(getPeople.apply(movieWithPeopleForCategory)).as("Returns a list of people for an existing category")
                .containsExactlyInAnyOrderElementsOf(peopleOfCategory);
        assertThat(getPeople.apply(movieWithoutPeopleForCategory))
                .as("Returns an empty list for a non-existing category").isNotNull().isEmpty();
    }

    @Test
    void toString_withPrimaryTranslationsAvailable_verifyStringRepresentation() {
        assertThat(new MovieDTO.Builder().addTranslations(
                new TranslationDTO.Builder().isPrimary(true).name("Léon: The Professional").build()).build())
                .asString().isEqualTo("Léon: The Professional");
    }

    @Test
    void toString_withNoPrimaryTranslationsAvailable_verifyDefaultValuesInStringRepresentation() {
        assertThat(new MovieDTO.Builder().addTranslations(
                new TranslationDTO.Builder().isPrimary(false).name("Passengers").build()).build())
                .asString().isEqualTo("n/a");
    }

    @Test
    void toString_withNoTranslationsAvailable_verifyDefaultValuesInStringRepresentation() {
        assertThat(new MovieDTO.Builder().build()).asString().isEqualTo("n/a");
    }

    @Nested
    @DisplayName("Tests for the movies \"Artwork\" DTO")
    class ArtworkTest {

        @Test
        void toString_withIdAndTypeAndWidthAndHeight_verifyStringRepresentation() {
            assertThat(new ArtworkDTO.Builder().id("817").artworkType("Poster").width(680L).height(1000L).build())
                    .asString().isEqualTo("[817] Poster: 680x1000");
        }
    }

    @Nested
    @DisplayName("Tests for the movies \"Genre\" DTO")
    class GenreTest {

        @Test
        void toString_withName_verifyStringRepresentation() {
            assertThat(new GenreDTO.Builder().name("Drama").build()).asString().isEqualTo("Drama");
        }
    }

    @Nested
    @DisplayName("Tests for the movies \"ReleaseDate\" DTO")
    class ReleaseDateTest {

        @Test
        void toString_withCountryAndTypeAndDate_verifyStringRepresentation() {
            assertThat(new ReleaseDateDTO.Builder().country("global").type("release_date").date("1990-04-05").build())
                    .asString().isEqualTo("[global] release_date: 1990-04-05");
        }
    }

    @Nested
    @DisplayName("Tests for the movies \"RemoteId\" DTO")
    class RemoteIdTest {

        @Test
        void toString_withSourceName_verifyStringRepresentation() {
            assertThat(new RemoteIdDTO.Builder().sourceName("IMDB").build()).asString().isEqualTo("IMDB");
        }
    }

    @Nested
    @DisplayName("Tests for the movies \"Trailer\" DTO")
    class TrailerTest {

        @Test
        void toString_withName_verifyStringRepresentation() {
            assertThat(new TrailerDTO.Builder().name("TENET - Official Trailer").build()).asString()
                    .isEqualTo("TENET - Official Trailer");
        }
    }

    @Nested
    @DisplayName("Tests for the movies \"Translation\" DTO")
    class TranslationTest {

        @Test
        void toString_withName_verifyStringRepresentation() {
            assertThat(new TranslationDTO.Builder().name("Blade Runner").build()).asString().isEqualTo("Blade Runner");
        }
    }

    @Nested
    @DisplayName("Tests for the movies \"People\" DTO")
    class PeopleTest {

        @Test
        void toString_withName_verifyStringRepresentation() {
            assertThat(new PeopleDTO.Builder().name("Sam Rockwell").build()).asString().isEqualTo("Sam Rockwell");
        }
    }
}

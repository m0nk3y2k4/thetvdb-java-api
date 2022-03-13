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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.api.model.data.FavoriteRecord;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APIPreconditionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class FavoriteRecordDTOTest {

    private static Stream<FavoriteRecordDTO.Builder> builderWithMissingProperties() {
        return Stream.of(
                new FavoriteRecordDTO.Builder(),
                new FavoriteRecordDTO.Builder().series(0).movies(0).episodes(0).artwork(0).people(0).list(0)
        );
    }

    @Test
    void publicBuilderInterface_allPropertiesAreSet_correctValuesAreReturned() {
        final int series = 12;
        final int movies = 73;
        final int episodes = 32;
        final int artwork = 87;
        final int people = 94;
        final int list = 55;

        FavoriteRecord.FavoriteRecordBuilder builder = new FavoriteRecordDTO.Builder();
        FavoriteRecordDTO fav = (FavoriteRecordDTO)builder
                .series(series).movies(movies).episodes(episodes).artwork(artwork).people(people).list(list).build();

        assertThat(fav.getSeries()).isEqualTo(series);
        assertThat(fav.getMovies()).isEqualTo(movies);
        assertThat(fav.getEpisodes()).isEqualTo(episodes);
        assertThat(fav.getArtwork()).isEqualTo(artwork);
        assertThat(fav.getPeople()).isEqualTo(people);
        assertThat(fav.getList()).isEqualTo(list);
    }

    @Test
    void getSeries_notSet_defaultValueReturned() {
        assertThat(new FavoriteRecordDTO.Builder().episodes(15).build().getSeries()).isZero();
    }

    @Test
    void getMovies_notSet_defaultValueReturned() {
        assertThat(new FavoriteRecordDTO.Builder().list(69).build().getMovies()).isZero();
    }

    @Test
    void getEpisodes_notSet_defaultValueReturned() {
        assertThat(new FavoriteRecordDTO.Builder().series(26).build().getEpisodes()).isZero();
    }

    @Test
    void getArtwork_notSet_defaultValueReturned() {
        assertThat(new FavoriteRecordDTO.Builder().movies(71).build().getArtwork()).isZero();
    }

    @Test
    void getPeople_notSet_defaultValueReturned() {
        assertThat(new FavoriteRecordDTO.Builder().artwork(20).build().getPeople()).isZero();
    }

    @Test
    void getList_notSet_defaultValueReturned() {
        assertThat(new FavoriteRecordDTO.Builder().people(88).build().getList()).isZero();
    }

    @ParameterizedTest
    @MethodSource("builderWithMissingProperties")
    void validate_noPropertiesAreSet_exceptionIsThrown(FavoriteRecordDTO.Builder builder) {
        assertThatExceptionOfType(APIPreconditionException.class)
                .isThrownBy(builder::build)
                .withMessageContaining("No favorites set! Please provide at least one ID.");
    }
}

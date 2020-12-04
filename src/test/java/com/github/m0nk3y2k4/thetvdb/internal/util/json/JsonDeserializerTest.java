/*
 * Copyright (C) 2019 - 2020 thetvdb-java-api Authors and Contributors
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

package com.github.m0nk3y2k4.thetvdb.internal.util.json;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class JsonDeserializerTest {

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "ARTWORK")
    void mapArtwork(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapArtwork(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "ARTWORKTYPE_LIST")
    void mapArtworkTypesOverview(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapArtworkTypesOverview(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "CHARACTER")
    void mapCharacter(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapCharacter(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "EPISODE")
    void mapEpisode(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapEpisode(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "GENRE")
    void mapGenre(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapGenre(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "GENRE_LIST")
    void mapGenresOverview(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapGenresOverview(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "MOVIE")
    void mapMovie(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapMovie(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "PEOPLE")
    void mapPeople(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapPeople(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "SEASON")
    void mapSeason(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapSeason(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "SERIES")
    void mapSeries(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapSeries(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "SERIES_DETAILS")
    void mapSeriesDetails(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapSeriesDetails(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "SERIES_LIST")
    void mapSeriesOverview(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapSeriesOverview(resource.getJson())).isEqualTo(resource.getDTO());
    }
}

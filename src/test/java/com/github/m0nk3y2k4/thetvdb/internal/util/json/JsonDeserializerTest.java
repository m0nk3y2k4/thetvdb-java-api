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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class JsonDeserializerTest {

    @Test
    void mapObject_withInvalidJSON_throwsAPIException() {
        assertThatExceptionOfType(APIException.class).isThrownBy(() ->
                JsonDeserializer.mapArtwork(new ObjectMapper().readTree(
                        "{\"data\": {\"Invalid\": \"Content\"}, \"status\": \"success\"}")));
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = {"ARTWORK", "ARTWORK_MIN"})
    void mapArtwork_fromJSONResource_verifyJsonIsParsedProperly(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapArtwork(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "ARTWORKTYPE_LIST")
    void mapArtworkTypesOverview_fromJSONResource_verifyJsonIsParsedProperly(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapArtworkTypesOverview(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = {"CHARACTER", "CHARACTER_MIN"})
    void mapCharacter_fromJSONResource_verifyJsonIsParsedProperly(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapCharacter(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = {"EPISODE", "EPISODE_MIN"})
    void mapEpisode_fromJSONResource_verifyJsonIsParsedProperly(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapEpisode(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "GENRE")
    void mapGenre_fromJSONResource_verifyJsonIsParsedProperly(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapGenre(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "GENRE_LIST")
    void mapGenresOverview_fromJSONResource_verifyJsonIsParsedProperly(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapGenresOverview(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = {"MOVIE", "MOVIE_MIN"})
    void mapMovie_fromJSONResource_verifyJsonIsParsedProperly(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapMovie(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = {"PEOPLE", "PEOPLE_MIN"})
    void mapPeople_fromJSONResource_verifyJsonIsParsedProperly(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapPeople(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = {"SEASON", "SEASON_MIN"})
    void mapSeason_fromJSONResource_verifyJsonIsParsedProperly(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapSeason(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = {"SERIES", "SERIES_MIN"})
    void mapSeries_fromJSONResource_verifyJsonIsParsedProperly(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapSeries(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = {"SERIES_DETAILS", "SERIES_DETAILS_MIN"})
    void mapSeriesDetails_fromJSONResource_verifyJsonIsParsedProperly(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapSeriesDetails(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "SERIES_LIST")
    void mapSeriesOverview_fromJSONResource_verifyJsonIsParsedProperly(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapSeriesOverview(resource.getJson())).isEqualTo(resource.getDTO());
    }
}

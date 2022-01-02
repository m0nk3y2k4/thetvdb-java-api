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

package com.github.m0nk3y2k4.thetvdb.internal.util.json;

import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_JSON_PARSE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class JsonDeserializerTest {

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = {"QUERYPARAMETERS", "QUERYPARAMETERS_NESTED"})
    void mapQueryParameters(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapQueryParameters(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = {"FAVORITES", "FAVORITES_EMPTY"})
    void mapFavorites(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapFavorites(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "SERIESHEADER")
    void mapSeriesHeader(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapSeriesHeader(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "SERIESSEARCH")
    void mapSeriesSearchResult(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapSeriesSearchResult(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "SERIES")
    void mapSeries(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapSeries(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "EPISODE")
    void mapEpisode(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapEpisode(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "EPISODES")
    void mapEpisodes(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapEpisodes(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "LANGUAGES")
    void mapLanguages(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapLanguages(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "LANGUAGE")
    void mapLanguage(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapLanguage(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "MOVIE")
    void mapMovie(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapMovie(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "MOVIEUPDATES")
    void mapMovieUpdates(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapMovieUpdates(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "ACTORS")
    void mapActors(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapActors(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "SERIESSUMMARY")
    void mapSeriesSummary(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapSeriesSummary(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "IMAGEQUERYPARAMETERS")
    void mapImageQueryParameters(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapImageQueryParameters(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "IMAGESUMMARY")
    void mapSeriesImageSummary(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapSeriesImageSummary(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "IMAGES")
    void mapImages(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapImages(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "UPDATES")
    void mapUpdates(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapUpdates(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "RATINGS")
    void mapRatings(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapRatings(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = "USER")
    void mapUser(JsonResource resource) throws Exception {
        assertThat(JsonDeserializer.mapUser(resource.getJson())).isEqualTo(resource.getDTO());
    }

    @Test
    void jsonParserException() throws Exception {
        JsonNode userJson = JsonResource.USER.getJson();
        assertThatExceptionOfType(APIException.class).isThrownBy(() -> JsonDeserializer.mapRatings(userJson))
                .withMessage(API_JSON_PARSE_ERROR);
    }
}

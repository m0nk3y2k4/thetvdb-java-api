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

package com.github.m0nk3y2k4.thetvdb.testutils.json;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Artwork;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ArtworkType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Character;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Episode;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Genre;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Movie;
import com.github.m0nk3y2k4.thetvdb.api.model.data.People;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Season;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Series;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesDetails;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.APIResponseDTO;

/**
 * Provides access to prefabbed JSON data used for unit testing
 * <p><br>
 * Test utility class offering easy access to predefined JSON files and associated Java DTO's. Resource files and DTO's
 * are aligned in terms of content. In other words, the DTO is the Java data type representation of some JSON resource
 * file. For an easier usage corresponding DTO's and resource files are consolidated into enumerations. For example:
 * <pre>{@code
 *     JsonParser seriesJSON = new JsonFactory().createParser(JsonResource.SERIES.getUrl());    // JSON resource file via URI
 *     APIResponse<Series> seriesDTO = parse(seriesJSON);
 *
 *     assertThat(seriesDTO).isEqualTo(JsonResource.SERIES.getDTO());      // Resource file as Java object
 * }</pre>
 */
public final class JSONTestUtil {

    public enum JsonResource {
        ARTWORK("artwork", JSONTestUtil::artwork, "Single artwork JSON response"),
        ARTWORKTYPE_LIST("artworktype_list", JSONTestUtil::artworkType, "List of artwork types JSON response"),
        CHARACTER("character", JSONTestUtil::character, "Single character JSON response"),
        DATA("data", JSONTestUtil::data, "Full JSON response with data and status node"),
        EPISODE("episode", JSONTestUtil::episode, "Single episode JSON response"),
        GENRE("genre", JSONTestUtil::genre, "Single genre JSON response"),
        GENRE_LIST("genre_list", JSONTestUtil::genreList, "List of genres JSON response"),
        MOVIE("movie", JSONTestUtil::movie, "Single movie JSON response"),
        PEOPLE("people", JSONTestUtil::people, "Single people JSON response"),
        SEASON("season", JSONTestUtil::season, "Single season JSON response"),
        SERIES("series", JSONTestUtil::series, "Single series JSON response"),
        SERIES_DETAILS("series_extended", JSONTestUtil::seriesDetails, "Single extended series JSON response"),
        SERIES_LIST("series_list", JSONTestUtil::seriesList, "List of series JSON response");

        private final String fileName;
        private final Supplier<APIResponse<?>> dtoSupplier;
        private final String description;

        JsonResource(String fileName, Supplier<APIResponse<?>> dtoSupplier, String description) {
            this.fileName = fileName;
            this.dtoSupplier = dtoSupplier;
            this.description = description;
        }

        /**
         * Creates a new JsonNode representation of this JSON resource
         *
         * @return This resource mapped as JsonNode object
         *
         * @throws IOException If a low-level I/O problem (unexpected end-of-input, network error) occurs
         */
        public JsonNode getJson() throws IOException {
            return new ObjectMapper().readTree(getUrl());
        }

        /**
         * Returns this resources JSON content as String
         *
         * @return This resource as JSON String
         *
         * @throws IOException If an I/O exception occurs
         */
        public String getJsonString() throws IOException {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(getUrl().openStream(), UTF_8))) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }

        /**
         * Returns the uniform resource locator (URL) for this JSON resource
         *
         * @return URL referencing this JSON resource
         */
        public URL getUrl() {
            return ClassLoader.getSystemResource("json/example/" + fileName + ".json");
        }

        /**
         * Returns a DTO representation of this JSON resource
         *
         * @return DTO representation of this JSON resource
         */
        public APIResponse<?> getDTO() {
            return dtoSupplier.get();
        }

        @Override
        public String toString() {
            return description;
        }
    }

    private JSONTestUtil() {}

    /**
     * Creates a new basic artwork APIResponse DTO with default values set
     *
     * @return New basic artwork APIResponse DTO prefilled with default values
     */
    public static APIResponse<Artwork> artwork() {
        return createAPIResponse(null);
    }

    /**
     * Creates a new artwork type APIResponse DTO with default values set
     *
     * @return New artwork type APIResponse DTO prefilled with default values
     */
    public static APIResponse<List<ArtworkType>> artworkType() {
        return createAPIResponse(null);
    }

    /**
     * Creates a new character APIResponse DTO with default values set
     *
     * @return New character APIResponse DTO prefilled with default values
     */
    public static APIResponse<Character> character() {
        return createAPIResponse(null);
    }

    /**
     * Creates a new full APIResponse DTO containing data and status, both prefilled with default values
     *
     * @return New APIResponse DTO prefilled with default values
     */
    public static APIResponse<Data> data() {
        return createAPIResponse(Data.with("Some content"));
    }

    /**
     * Creates a new episode APIResponse DTO with default values set
     *
     * @return New episode APIResponse DTO prefilled with default values
     */
    public static APIResponse<Episode> episode() {
        return createAPIResponse(null);
    }

    /**
     * Creates a new genre APIResponse DTO with default values set
     *
     * @return New genre APIResponse DTO prefilled with default values
     */
    public static APIResponse<Genre> genre() {
        return createAPIResponse(null);
    }

    /**
     * Creates a new genre overview APIResponse DTO with default values set
     *
     * @return New genre overview APIResponse DTO prefilled with default values
     */
    public static APIResponse<List<Genre>> genreList() {
        return createAPIResponse(null);
    }

    /**
     * Creates a new movie APIResponse DTO with default values set
     *
     * @return New movie APIResponse DTO prefilled with default values
     */
    public static APIResponse<Movie> movie() {
        return createAPIResponse(null);
    }

    /**
     * Creates a new people APIResponse DTO with default values set
     *
     * @return New people APIResponse DTO prefilled with default values
     */
    public static APIResponse<People> people() {
        return createAPIResponse(null);
    }

    /**
     * Creates a new season APIResponse DTO with default values set
     *
     * @return New season APIResponse DTO prefilled with default values
     */
    public static APIResponse<Season> season() {
        return createAPIResponse(null);
    }

    /**
     * Creates a new series APIResponse DTO with default values set
     *
     * @return New series APIResponse DTO prefilled with default values
     */
    public static APIResponse<Series> series() {
        return createAPIResponse(null);
    }

    /**
     * Creates a new series details APIResponse DTO with default values set
     *
     * @return New series details APIResponse DTO prefilled with default values
     */
    public static APIResponse<SeriesDetails> seriesDetails() {
        return createAPIResponse(null);
    }

    /**
     * Creates a new series overview APIResponse DTO with default values set
     *
     * @return New series overview APIResponse DTO prefilled with default values
     */
    public static APIResponse<List<Series>> seriesList() {
        return createAPIResponse(null);
    }

    /**
     * Creates a new APIResponse DTO with the given <em>{@code data}</em> and some additional default values
     *
     * @param data The actual payload DTO of the response
     * @param <T>  The API response payload type
     *
     * @return New APIResponse DTO prefilled with the given object and some additional default values
     */
    public static <T> APIResponse<T> createAPIResponse(T data) {
        return new APIResponseDTO.Builder<T>()
                .data(data)
                .status("success")
                .build();
    }
}

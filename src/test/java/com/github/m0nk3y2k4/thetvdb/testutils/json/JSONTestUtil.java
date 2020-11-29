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
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
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
        DATA("data", JSONTestUtil::data, "Full JSON response with data and status node");

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
     * Creates a new full APIResponse DTO containing data and status, both prefilled with default values
     *
     * @return New APIResponse DTO prefilled with default values
     */
    public static APIResponse<Data> data() {
        return new APIResponseDTO.Builder<Data>()
                .data(Data.with("Some content"))
                .status("success")
                .build();
    }
}

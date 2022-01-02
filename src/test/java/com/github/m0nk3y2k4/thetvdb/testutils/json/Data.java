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

package com.github.m0nk3y2k4.thetvdb.testutils.json;

import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a simple JSON "data" node object
 * <p><br>
 * May be used for testing JSON deserialization/parsing. The actual JSON should have the following structure:
 * <pre>{@code
 * {
 *     "data": {
 *         "content": "Some content"
 *     }
 * }
 * }</pre>
 */
public final class Data {

    @JsonProperty("content")
    private String content;

    public static Data with(String content) {
        Data data = new Data();
        data.setContent(content);
        return data;
    }

    private String getContent() {
        return content;
    }

    private void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Data data = (Data)obj;
        return content.equals(data.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return Optional.ofNullable(content).orElse("");
    }
}

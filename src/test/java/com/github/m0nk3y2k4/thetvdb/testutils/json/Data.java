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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static Data with(String content) {
        Data d = new Data();
        d.setContent(content);
        return d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Data data = (Data) o;
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

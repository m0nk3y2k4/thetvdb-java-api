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

package com.github.m0nk3y2k4.thetvdb.internal.util.json.deser;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

/**
 * Wrapper class for Jackson {@link StringCollectionDeserializer} objects
 * <p><br>
 * These wrappers are used as a workaround for Jackson/Immutables not supporting proper handling of nullable lists in
 * JSON data. Empty lists ({@code "list": []}) will automatically be deserialized into an empty {@link Collection
 * Collection&lt;T&gt;} with no elements.
 * <br>
 * However, {@code null} values ({@code "list": null}) will be parsed as Java {@code null}. Unfortunately, the {@code
 * Immutables} library used by this API does not support
 * <a target="_bank" href="https://immutables.github.io/immutable.html#nulls-in-collection">nullable collections</a>
 * (only by wrapping the collection into a Java Optional which can be considered as bad practice). On the other hand,
 * Jackson currently doesn't support skipping or ignoring {@code null} values during JSON <b>de</b>serialization.
 * <p><br>
 * Although I would consider this a bad practice either, the remote API seems to occasionally respond {@code null}
 * values for JSON list properties which leads to the mentioned incompatibility with the Immutables DTO builders.
 * <p><br>
 * However, as using {@code null} values for list properties is still valid JSON, it seems reasonable to harden this
 * Java connector in order to also support such scenarios. This can be achieved by using this wrapper around
 * String-collection based JSON deserializers. It supports only lists of simple String-values. For all other list
 * content types please use the more general {@link NullableCollectionWrapper}.
 */
public final class NullableStringCollectionWrapper extends ContainerDeserializerBase<Collection<String>> implements ContextualDeserializer {

    /** The wrapped StringCollectionDeserializer instance */
    private final StringCollectionDeserializer deser;

    /**
     * Creates a new instance of this wrapper based on the properties of the given deserializer
     *
     * @param deser JSON deserializer to be wrapped by this object
     */
    NullableStringCollectionWrapper(@Nonnull StringCollectionDeserializer deser) {
        super(deser.getContentType(), deser.getContentDeserializer(), null);
        this.deser = deser;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
        // Just to be on the safe side. Technically it will always return a StringCollectionDeserializer.
        return CollectionDeserializerModifier.wrapCollectionDeserializers(deser.createContextual(ctxt, property));
    }

    @Override
    public JsonDeserializer<Object> getContentDeserializer() {
        return deser.getContentDeserializer();
    }

    @Override
    public Collection<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return deser.deserialize(p, ctxt);
    }

    /**
     * Method that can be called to determine the value to be used for representing null values (values deserialized
     * when JSON token is {@link com.fasterxml.jackson.core.JsonToken#VALUE_NULL}). Returns an empty collection. This
     * method may be called once, or multiple times, depending on what getNullAccessPattern() returns.
     *
     * @param ctxt Context that can be used to access information about this deserialization activity
     *
     * @return An unmodifiable collection with no elements
     */
    @Override
    public Collection<String> getNullValue(DeserializationContext ctxt) {
        // StringCollectionDeserializer returns null-value by default what leads to a NPE in DTO builders
        return Collections.emptyList();
    }
}

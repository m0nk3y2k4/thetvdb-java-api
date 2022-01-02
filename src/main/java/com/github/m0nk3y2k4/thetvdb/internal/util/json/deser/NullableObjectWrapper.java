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

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.BuilderBasedDeserializer;

/**
 * Wrapper class for Jackson {@link BuilderBasedDeserializer} objects
 * <p><br>
 * Objects of this class are used to extend the capability of the underlying deserializer to handle non-existing/NULL
 * JSON properties. The default implementation will dereference such properties as NULL. Using a wrapped deserializer
 * though, a new, plain Immutable DTO object will be created instead.
 * <p><br>
 * However, as there is no real data to be mapped within the JSON, it's necessary that the referred Immutable DTO
 * supports building new instances without previously setting any object properties. That means that <b>all</b> DTO
 * properties need either to be <u>nullable or Java Optionals</u>. Otherwise, the creation of a "blank" instance of the
 * Immutable will fail due to mandatory properties not being set.
 * <p><br>
 * If the corresponding property exists within the JSON and is not 'null' the actual deserialization is simply relayed
 * to the wrapped deserializer instance.
 */
public final class NullableObjectWrapper extends BuilderBasedDeserializer {

    /**
     * Creates a new instance of this class wrapping the given deserializer
     *
     * @param deser Builder-based JSON deserializer to be wrapped by this object
     */
    NullableObjectWrapper(BuilderBasedDeserializer deser) {
        super(deser);
    }

    /**
     * Method that can be called to determine the value to be used for representing null values (values deserialized
     * when JSON token is {@link com.fasterxml.jackson.core.JsonToken#VALUE_NULL}). Returns a new, plain instance of the
     * DTO class handled by this builder-based deserializer. This method may be called once, or multiple times,
     * depending on what getNullAccessPattern() returns.
     *
     * @param ctxt Context that can be used to access information about this deserialization activity
     *
     * @return A new DTO object with all properties being empty/NULL
     */
    @Override
    public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
        try {
            return finishBuild(ctxt, getValueInstantiator().createUsingDefault(ctxt)); // Basically this does the same as: new Builder().build()
        } catch (IOException e) {
            throw JsonMappingException.from(ctxt, String.format("Could not create instance of class: %s", _targetType.getRawClass()), e);
        }
    }
}

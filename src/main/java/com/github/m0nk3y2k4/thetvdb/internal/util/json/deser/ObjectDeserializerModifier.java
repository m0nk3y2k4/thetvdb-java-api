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

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.BuilderBasedDeserializer;

/**
 * JSON deserialization modifier used to participate in constructing {@code JsonDeserializer} instances for JSON object
 * properties.
 * <p><br>
 * Objects of this class are used to alter some aspects of deserialization process, especially with regard to the
 * handling of nullable objects.
 */
class ObjectDeserializerModifier extends BeanDeserializerModifier {

    /**
     * Tucks the given {@code JsonDeserializer} into a corresponding wrapper class if necessary. Wrapping will be
     * performed based on the actual type of the deserializer.
     * <ul>
     *     <li>{@link com.fasterxml.jackson.databind.deser.BuilderBasedDeserializer}: New {@link NullableObjectWrapper} instance</li>
     * </ul>
     * JsonDeserializers other than the aforementioned will <u>not</u> be wrapped but will simply be returned unchanged.
     *
     * @param deserializer JSON deserializer instance that shall be wrapped if necessary
     *
     * @return New wrapper instance for the listed deserializer types or the (unwrapped) provided deserializer itself
     */
    @SuppressWarnings("java:S1452")  // Generic wildcard type is part of the superclass contract
    static JsonDeserializer<?> wrapObjectDeserializer(JsonDeserializer<?> deserializer) {
        if (deserializer instanceof BuilderBasedDeserializer) {
            return new NullableObjectWrapper((BuilderBasedDeserializer)deserializer);
        } else {
            return deserializer;        // No wrapping
        }
    }

    @Override
    public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc,
            JsonDeserializer<?> deserializer) {
        return wrapObjectDeserializer(deserializer);
    }
}

/*
 * Copyright (C) 2019 - 2021 thetvdb-java-api Authors and Contributors
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

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CollectionDeserializerModifierTest {

    @Mock
    private JavaType collectionType;

    @Mock
    private JsonDeserializer<Object> valueDeser;

    @Mock
    private TypeDeserializer valueTypeDeser;

    @Mock
    private ValueInstantiator valueInstantiator;

    @Test
    void modifyCollectionDeserializer_withStringCollectionDeserializer_wrappedIntoNullableStringCollectionWrapper() {
        JsonDeserializer<?> deserializer = new StringCollectionDeserializer(collectionType, valueDeser, valueInstantiator);
        assertThat(new CollectionDeserializerModifier().modifyCollectionDeserializer(null, null, null, deserializer))
                .isInstanceOf(NullableStringCollectionWrapper.class);
    }

    @Test
    void modifyCollectionDeserializer_withCollectionDeserializer_wrappedIntoNullableCollectionWrapper() {
        JsonDeserializer<?> deserializer =
                new CollectionDeserializer(collectionType, valueDeser, valueTypeDeser, valueInstantiator);
        assertThat(new CollectionDeserializerModifier().modifyCollectionDeserializer(null, null, null, deserializer))
                .isInstanceOf(NullableCollectionWrapper.class);
    }

    @Test
    void modifyCollectionDeserializer_withUnsupportedDeserializerType_notWrapped() {
        JsonDeserializer<?> deserializer = new UUIDDeserializer();
        assertThat(new CollectionDeserializerModifier().modifyCollectionDeserializer(null, null, null, deserializer))
                .isEqualTo(deserializer);
    }
}

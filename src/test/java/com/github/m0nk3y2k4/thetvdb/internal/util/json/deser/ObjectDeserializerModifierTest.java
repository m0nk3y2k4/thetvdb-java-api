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

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.BuilderBasedDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ObjectDeserializerModifierTest {

    @InjectMocks
    private ObjectDeserializerModifier modifier;

    @Test
    void modifyDeserializer_withBuilderBasedDeserializer_wrappedIntoNullableObjectWrapper() {
        BeanDeserializerBuilder builder = mock(BeanDeserializerBuilder.class);
        doReturn(mock(ValueInstantiator.class)).when(builder).getValueInstantiator();

        BeanDescription beanDesc = mock(BeanDescription.class);
        doReturn(new JsonFormat.Value()).when(beanDesc).findExpectedFormat(null);

        JsonDeserializer<?> deserializer = new BuilderBasedDeserializer(builder, beanDesc, mock(JavaType.class),
                mock(BeanPropertyMap.class), emptyMap(), emptySet(), true, emptySet(), false);

        assertThat(modifier.modifyDeserializer(null, null, deserializer))
                .isInstanceOf(NullableObjectWrapper.class);
    }

    @Test
    void modifyDeserializer_withUnsupportedDeserializerType_notWrapped() {
        JsonDeserializer<?> deserializer = new UUIDDeserializer();
        assertThat(modifier.modifyDeserializer(null, null, deserializer))
                .isEqualTo(deserializer);
    }
}

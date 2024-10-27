/*
 * Copyright (C) 2019 - 2024 thetvdb-java-api Authors and Contributors
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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.BuilderBasedDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse.Links;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.APIResponseDTO.LinksDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.TestInstantiationException;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NullableObjectWrapperTest {

    private final NullableObjectWrapper wrapper = new NullableObjectWrapper(createBuilderBasedDeserializerMock());
    @Mock
    private DeserializationContext context;

    private static BuilderBasedDeserializer createBuilderBasedDeserializerMock() {
        try {
            LinksDTO.Builder dtoBuilder = new LinksDTO.Builder();
            JavaType targetType = SimpleType.constructUnsafe(LinksDTO.class);
            AnnotatedMethod buildMethod = new AnnotatedMethod(null, dtoBuilder.getClass()
                    .getDeclaredMethod("build"), null, null);

            ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
            doReturn(dtoBuilder).when(valueInstantiator).createUsingDefault(any(DeserializationContext.class));

            BeanDeserializerBuilder builder = mock(BeanDeserializerBuilder.class);
            doReturn(buildMethod).when(builder).getBuildMethod();
            doReturn(valueInstantiator).when(builder).getValueInstantiator();

            BeanDescription beanDesc = mock(BeanDescription.class);
            doReturn(new JsonFormat.Value()).when(beanDesc).findExpectedFormat();

            return new BuilderBasedDeserializer(builder, beanDesc, targetType, mock(BeanPropertyMap.class), emptyMap(),
                    emptySet(), true, emptySet(), false);
        } catch (NoSuchMethodException | IOException e) {
            throw new TestInstantiationException("Failed to setup test class", e);
        }
    }

    @Test
    void getNullValue_builderSuccessfullyInstantiated_properNonNullInstanceReturned() throws Exception {
        assertThat(wrapper.getNullValue(context)).isInstanceOf(Links.class);
    }

    @Test
    void getNullValue_builderInstantiationFailed_throwsJSONMappingException() throws Exception {
        doThrow(new IOException()).when(wrapper.getValueInstantiator()).createUsingDefault(context);

        assertThatExceptionOfType(JsonMappingException.class).isThrownBy(() -> wrapper.getNullValue(context))
                .withMessage("Could not create instance of class: %s", LinksDTO.class);
    }
}

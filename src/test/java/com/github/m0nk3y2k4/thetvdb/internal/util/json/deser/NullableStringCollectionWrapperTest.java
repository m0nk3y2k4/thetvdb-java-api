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

package com.github.m0nk3y2k4.thetvdb.internal.util.json.deser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Unfortunately StringCollectionDeserializer is final so we cannot simply mock it
@ExtendWith(MockitoExtension.class)
class NullableStringCollectionWrapperTest {

    @Mock
    private DeserializationContext ctxt;

    @Mock
    private JsonDeserializer<Object> valueDeser;

    @Mock
    private ValueInstantiator valueInstantiator;

    private NullableStringCollectionWrapper wrapper;

    @BeforeEach
    void setUp(@Mock JavaType collectionType) {
        wrapper = new NullableStringCollectionWrapper(
                new StringCollectionDeserializer(collectionType, valueDeser, valueInstantiator));
    }

    @Test
    void createContextual_returningStringCollectionDeserializer_wrapReturnedDeserializer() throws Exception {
        PropertyMetadata meta = when(mock(PropertyMetadata.class).getContentNulls()).thenReturn(Nulls.SET).getMock();
        BeanProperty property = when(mock(BeanProperty.class).getMetadata()).thenReturn(meta).getMock();
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0)).when(ctxt)
                .handleSecondaryContextualization(any(), any(), any());

        assertThat(wrapper.createContextual(ctxt, property)).isInstanceOf(NullableStringCollectionWrapper.class);
    }

    @Test
    void getContentDeserializer_invokeActualDeserializer_forwardResult() {
        assertThat(wrapper.getContentDeserializer()).isSameAs(valueDeser);
    }

    @Test
    void deserialize_invokeActualDeserializer_forwardResult() throws Exception {
        doReturn(Collections.emptyList()).when(valueInstantiator).createUsingDefault(ctxt);

        JsonParser p = new JsonFactory().createParser("[]");
        p.nextToken();

        assertThat(wrapper.deserialize(p, ctxt)).isSameAs(Collections.emptyList());
    }

    @Test
    void getNullValue_verifyEmptyCollectionIsReturned() {
        assertThat(wrapper.getNullValue(ctxt)).isNotNull().isEmpty();
    }
}

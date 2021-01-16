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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Collection;
import java.util.Collections;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NullableCollectionWrapperTest {

    @Mock
    private DeserializationContext ctxt;

    @Mock
    private CollectionDeserializer deserializer;

    private NullableCollectionWrapper wrapper;

    @BeforeEach
    void setUp() {
        wrapper = new NullableCollectionWrapper(deserializer);
    }

    @Test
    void createContextual_invokeActualDeserializer_wrapReturnedCollectionDeserializer(@Mock BeanProperty property)
            throws Exception {
        doReturn(mock(CollectionDeserializer.class)).when(deserializer).createContextual(ctxt, property);
        assertThat(wrapper.createContextual(ctxt, property)).isInstanceOf(NullableCollectionWrapper.class);
    }

    @Test
    void getContentDeserializer_invokeActualDeserializer_forwardResult(@Mock JsonDeserializer<Object> ctntDeser) {
        doReturn(ctntDeser).when(deserializer).getContentDeserializer();
        assertThat(wrapper.getContentDeserializer()).isSameAs(ctntDeser);
    }

    @Test
    void deserialize_invokeActualDeserializer_forwardResult(@Mock JsonParser p) throws Exception {
        Collection<Object> result = Collections.emptyList();
        doReturn(result).when(deserializer).deserialize(p, ctxt);
        assertThat(wrapper.deserialize(p, ctxt)).isSameAs(result);
    }

    @Test
    void getNullValue_verifyEmptyCollectionIsReturned() {
        assertThat(wrapper.getNullValue(ctxt)).isEmpty();
    }
}

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

package com.github.m0nk3y2k4.thetvdb.internal.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ResourceTest {

    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    private static Stream<Arguments> createBaseResource_verifyResourceString() {
        return Stream.of(
                Arguments.of("/users", EMPTY_OBJECT_ARRAY, "/users"),
                Arguments.of("/orders", new Object[]{1, 2, 3}, "/orders/1/2/3"),
                Arguments.of("/shipment", new Object[]{"pending", "today"}, "/shipment/pending/today")
        );
    }

    private static Stream<Arguments> createSpecificResource_verifyResourceString() {
        return Stream.of(
                Arguments.of("/invoices", null, EMPTY_OBJECT_ARRAY, "/invoices"),
                Arguments.of("/invoices", null, new Object[]{1, 2}, "/invoices/1/2"),
                Arguments.of("/invoices", "/pending", EMPTY_OBJECT_ARRAY, "/invoices/pending"),
                Arguments.of("/invoices", "/sent", new Object[]{"customer", "atari"}, "/invoices/sent/customer/atari")
        );
    }

    @ParameterizedTest(name = "[{index}] Resource String for base \"{0}\" and path parameters {1} is: \"{2}\"")
    @MethodSource
    void createBaseResource_verifyResourceString(String base, Object[] pathParams, String expected) {
        assertThat(Resource.createResource(base, pathParams)).isEqualTo(expected);
    }

    @ParameterizedTest(name = "[{index}] Resource String for base \"{0}\", specific \"{1}\" and path parameters {2} is: \"{3}\"")
    @MethodSource
    void createSpecificResource_verifyResourceString(String base, String specific, Object[] pathParams,
            String expected) {
        assertThat(Resource.createResource(base, specific, pathParams)).isEqualTo(expected);
    }

    @Test
    void createNewQueryResource_happyDay() {
        assertThatCode(TestResource::new).doesNotThrowAnyException();
    }

    private static final class TestResource extends Resource {}
}

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

package com.github.m0nk3y2k4.thetvdb.internal.util;

import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.JSON_SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class APIUtilTest {

    private static final String SOME_STRING = "This is some String value";

    private static Stream<Arguments> hasValue() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(new String[]{"Only", "non-empty", "Strings"}, true),
                Arguments.of(new String[]{"Ok", null}, false),
                Arguments.of(new String[]{"So", "close", "    "}, false)
        );
    }

    private static Stream<Arguments> hasNoValue() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of(new String[]{"booh!", "AH!!"}, false),
                Arguments.of(new String[]{null, "some value"}, true),
                Arguments.of(new String[]{" ", "another value"}, true)
        );
    }

    private static Stream<Arguments> toString_withoutDefaultValue() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of((Supplier<?>)() -> null, ""),
                Arguments.of((Supplier<Collection<Integer>>)() -> List.of(4, 3, 2, 1), "4, 3, 2, 1"),
                Arguments.of((Supplier<Optional<String>>)() -> Optional.of(SOME_STRING), SOME_STRING),
                Arguments.of((Supplier<Optional<?>>)Optional::empty, ""),
                Arguments.of((Supplier<SomeObject>)() -> new SomeObject(SOME_STRING), SOME_STRING),
                Arguments.of((Supplier<String>)() -> SOME_STRING, SOME_STRING)
        );
    }

    private static Stream<Arguments> toString_withDefaultValue() {
        return Stream.of(
                Arguments.of(null, SOME_STRING, SOME_STRING),
                Arguments.of((Supplier<?>)() -> null, SOME_STRING, SOME_STRING),
                Arguments.of((Supplier<Collection<Integer>>)() -> List.of(1, 2, 3, 4), "", "1, 2, 3, 4"),
                Arguments.of((Supplier<Optional<String>>)() -> Optional.of(SOME_STRING), "", SOME_STRING),
                Arguments.of((Supplier<Optional<?>>)Optional::empty, SOME_STRING, SOME_STRING),
                Arguments.of((Supplier<SomeObject>)() -> new SomeObject(SOME_STRING), "", SOME_STRING),
                Arguments.of((Supplier<String>)() -> SOME_STRING, "", SOME_STRING)
        );
    }

    @ParameterizedTest
    @MethodSource
    void hasValue(String[] values, boolean expected) {
        assertThat(APIUtil.hasValue(values)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource
    void hasNoValue(String[] values, boolean expected) {
        assertThat(APIUtil.hasNoValue(values)).isEqualTo(expected);
    }

    @Test
    void prettyPrint_simpleJSONNode() throws Exception {
        //@formatter:off
        assertThat(APIUtil.prettyPrint(new ObjectMapper().readTree(JSON_SUCCESS))).isEqualTo(
                "{\n" +
                "  \"Success\" : true\n" +
                "}"
        );
        //@formatter:on
    }

    @ParameterizedTest
    @MethodSource
    void toString_withoutDefaultValue(Supplier<?> valueSupplier, String expected) {
        assertThat(APIUtil.toString(valueSupplier)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource
    void toString_withDefaultValue(Supplier<?> valueSupplier, String nullDefault, String expected) {
        assertThat(APIUtil.toString(valueSupplier, nullDefault)).isEqualTo(expected);
    }

    private static final class SomeObject {
        private final String value;

        @SuppressWarnings("SameParameterValue")
        SomeObject(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}

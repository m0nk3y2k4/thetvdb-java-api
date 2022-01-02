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

package com.github.m0nk3y2k4.thetvdb.internal.util;

import static com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil.BracketType.ANGLE;
import static com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil.BracketType.BRACES;
import static com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil.BracketType.BRACKETS;
import static com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil.BracketType.PARENTHESES;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.JSON_SUCCESS;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil.BracketType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
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

    private static Stream<Arguments> valueSupplierWithoutDefaultValue() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of((Supplier<?>)() -> null, ""),
                Arguments.of((Supplier<Collection<Integer>>)() -> List.of(4, 3, 2, 1), "4, 3, 2, 1"),
                Arguments.of((Supplier<Optional<String>>)() -> Optional.of(SOME_STRING), SOME_STRING),
                Arguments.of((Supplier<Optional<?>>)Optional::empty, ""),
                Arguments.of((Supplier<OptionalInt>)() -> OptionalInt.of(26), "26"),
                Arguments.of((Supplier<OptionalInt>)OptionalInt::empty, ""),
                Arguments.of((Supplier<SomeObject>)() -> new SomeObject(SOME_STRING), SOME_STRING),
                Arguments.of((Supplier<String>)() -> SOME_STRING, SOME_STRING)
        );
    }

    private static Stream<Arguments> valueSupplierWithDefaultValue() {
        return Stream.of(
                Arguments.of(null, SOME_STRING, SOME_STRING),
                Arguments.of((Supplier<?>)() -> null, SOME_STRING, SOME_STRING),
                Arguments.of((Supplier<Collection<Integer>>)() -> List.of(1, 2, 3, 4), "", "1, 2, 3, 4"),
                Arguments.of((Supplier<Optional<String>>)() -> Optional.of(SOME_STRING), "", SOME_STRING),
                Arguments.of((Supplier<Optional<?>>)Optional::empty, SOME_STRING, SOME_STRING),
                Arguments.of((Supplier<OptionalInt>)() -> OptionalInt.of(26), SOME_STRING, "26"),
                Arguments.of((Supplier<OptionalInt>)OptionalInt::empty, SOME_STRING, SOME_STRING),
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
    void convert_withEmptyMap_returnsEmptyMap() {
        Map<String, Long> mapToConvert = emptyMap();
        Map<String, String> convertedMap = APIUtil.convert(mapToConvert, Objects::toString);
        assertThat(convertedMap).isEmpty();
    }

    @Test
    void convert_withMapContainingMultipleValues_valuesAreProperlyMappedIncludingNull() {
        Map<String, OptionalInt> mapToConvert = new HashMap<>();
        mapToConvert.put("Key1", OptionalInt.of(12));
        mapToConvert.put("Key2", null);
        Map<String, Integer> convertedMap = APIUtil.convert(mapToConvert, OptionalInt::getAsInt);
        assertThat(convertedMap).contains(new SimpleEntry<>("Key1", 12), new SimpleEntry<>("Key2", null));
    }

    @Test
    void prettyPrint_simpleJSONNode() throws Exception {
        //@DisableFormatting
        assertThat(APIUtil.prettyPrint(new ObjectMapper().readTree(JSON_SUCCESS))).isEqualTo(
                "{\n" +
                "  \"Success\" : true\n" +
                "}"
        );
        //@EnableFormatting
    }

    @ParameterizedTest
    @MethodSource("valueSupplierWithoutDefaultValue")
    void toString_withoutDefaultValue(Supplier<?> valueSupplier, String expected) {
        assertThat(APIUtil.toString(valueSupplier)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("valueSupplierWithDefaultValue")
    void toString_withDefaultValue(Supplier<?> valueSupplier, String nullDefault, String expected) {
        assertThat(APIUtil.toString(valueSupplier, nullDefault)).isEqualTo(expected);
    }

    @ParameterizedTest
    @EnumSource(value = BracketType.class, mode = EnumSource.Mode.EXCLUDE, names = "PARENTHESES")
    void removeEnclosingBrackets_RoundBrackets_VerifyOtherBracketTypesNotBeingRemoved(BracketType type) {
        String value = type.opening + "value" + type.closing;
        assertThat(APIUtil.removeEnclosingBrackets(value, PARENTHESES)).isEqualTo(value);
    }

    @ParameterizedTest
    @EnumSource(value = BracketType.class, mode = EnumSource.Mode.EXCLUDE, names = "BRACKETS")
    void removeEnclosingBrackets_SquareBrackets_VerifyOtherBracketTypesNotBeingRemoved(BracketType type) {
        String value = type.opening + "value" + type.closing;
        assertThat(APIUtil.removeEnclosingBrackets(value, BRACKETS)).isEqualTo(value);
    }

    @ParameterizedTest
    @EnumSource(value = BracketType.class, mode = EnumSource.Mode.EXCLUDE, names = "BRACES")
    void removeEnclosingBrackets_CurlyBrackets_VerifyOtherBracketTypesNotBeingRemoved(BracketType type) {
        String value = type.opening + "value" + type.closing;
        assertThat(APIUtil.removeEnclosingBrackets(value, BRACES)).isEqualTo(value);
    }

    @ParameterizedTest
    @EnumSource(value = BracketType.class, mode = EnumSource.Mode.EXCLUDE, names = "ANGLE")
    void removeEnclosingBrackets_AngleBrackets_VerifyOtherBracketTypesNotBeingRemoved(BracketType type) {
        String value = type.opening + "value" + type.closing;
        assertThat(APIUtil.removeEnclosingBrackets(value, ANGLE)).isEqualTo(value);
    }

    @ParameterizedTest
    @EnumSource(BracketType.class)
    void removeEnclosingBrackets_AllTypesWithOpeningBracket_VerifyBracketsNotBeingRemoved(BracketType type) {
        String value = "WithOpeningBracketsOnly";
        assertThat(APIUtil.removeEnclosingBrackets(type.opening + value, type)).isEqualTo(value);
    }

    @ParameterizedTest
    @EnumSource(BracketType.class)
    void removeEnclosingBrackets_AllTypesWithClosingBracket_VerifyBracketsNotBeingRemoved(BracketType type) {
        String value = "WithClosingBracketsOnly";
        assertThat(APIUtil.removeEnclosingBrackets(value + type.closing, type)).isEqualTo(value);
    }

    @ParameterizedTest
    @EnumSource(BracketType.class)
    void removeEnclosingBrackets_AllTypesWithOpeningAndClosingBracket_VerifyBracketsNotBeingRemoved(BracketType type) {
        String value = "WithOpeningAndClosingBrackets";
        assertThat(APIUtil.removeEnclosingBrackets(type.opening + value + type.closing, type)).isEqualTo(value);
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

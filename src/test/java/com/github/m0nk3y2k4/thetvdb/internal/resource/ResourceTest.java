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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ResourceTest {

    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    private static Stream<Arguments> withInvalidWildcardParameters() {
        return Stream.of(
                Arguments.of("/noWildcard", new Object[]{14L}),
                Arguments.of("/{id}/nullValue/{language}", new Object[]{32L, null}),
                Arguments.of("/{id}/tooFew/{language}", new Object[]{12L}),
                Arguments.of("/{language}/tooMany", new Object[]{"eng", 7L})
        );
    }

    private static Stream<Arguments> validResourceParameters() {
        return Stream.of(
                Arguments.of("/withNoWildcards", EMPTY_OBJECT_ARRAY, "/withNoWildcards"),
                Arguments.of("/with/no/wildcards", EMPTY_OBJECT_ARRAY, "/with/no/wildcards"),
                Arguments.of("/with/{id}", new Object[]{15L}, "/with/15"),
                Arguments.of("/with/{id}/wildcard", new Object[]{21L}, "/with/21/wildcard"),
                Arguments.of("/with/{id}/and/{language}", new Object[]{2L, "eng"}, "/with/2/and/eng")
        );
    }

    @ParameterizedTest(name = "[{index}] With path: <{0}>")
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "/", "/path/", "/pa.th", "/p ath"})
    void createResource_withInvalidPath_verifyParameterValidation(String path) {
        assertThatIllegalArgumentException().isThrownBy(() -> Resource.createResource(path));
    }

    @ParameterizedTest(name = "[{index}] With path <{0}> and wildcard parameters {1}")
    @MethodSource("withInvalidWildcardParameters")
    void createResource_withInvalidWildcardParameters_verifyParameterValidation(String path, Object[] params) {
        assertThatIllegalArgumentException().isThrownBy(() -> Resource.createResource(path, params));
    }

    @ParameterizedTest(name = "[{index}] With path <{0}> and parameters {1} returns: {2}")
    @MethodSource("validResourceParameters")
    void createResource_verifyReturnedParameterString(String path, Object[] params, String expected) {
        assertThat(Resource.createResource(path, params)).isEqualTo(expected);
    }

    @Test
    void createNewQueryResource_happyDay() {
        assertThatCode(TestResource::new).doesNotThrowAnyException();
    }

    private static final class TestResource extends Resource {}
}

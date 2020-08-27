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

package com.github.m0nk3y2k4.thetvdb.internal.util.validation;

import static com.github.m0nk3y2k4.thetvdb.api.exception.APIRuntimeException.API_PRECONDITION_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.internal.exception.APIPreconditionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class PreconditionsTest {

    private static Stream<Arguments> requires_conditionNotMatched_exceptionRethrown() {
        return Stream.of(
                Arguments.of((Predicate<Integer>)age -> age > 18, 14, new APIPreconditionException("Not old enough!")),
                Arguments.of((Predicate<Object>)Objects::nonNull, null, new APIPreconditionException("No null values allowed!"))
        );
    }

    @Test
    void requires_happyDay() {
        assertDoesNotThrow(() -> Preconditions.requires(age -> age > 18, 42, new APIPreconditionException("Should not be thrown")));
    }

    @ParameterizedTest(name = "[{index}] Value \"{1}\" is invalid as it does not match the condition")
    @MethodSource
    <T> void requires_conditionNotMatched_exceptionRethrown(Predicate<T> predicate, T value, RuntimeException exception) {
        APIPreconditionException thrown = catchThrowableOfType(() -> Preconditions.requires(predicate, value, exception), APIPreconditionException.class);
        assertThat(thrown).isEqualTo(exception);
    }

    @Test
    void requireNonNull_happyDay() {
        assertDoesNotThrow(() -> Preconditions.requireNonNull("   ", "Keep it to yourself"));
    }

    @ParameterizedTest(name = "[{index}] String \"{0}\" is null")
    @NullSource
    void requireNonNull_withNullValue_exceptionThrown(String obj) {
        final String message = "I said no null-values!";
        APIPreconditionException thrown = catchThrowableOfType(() -> Preconditions.requireNonNull(obj, message), APIPreconditionException.class);
        assertThat(thrown).hasMessage(API_PRECONDITION_ERROR, message);
    }

    @Test
    void requireNonEmpty_happyDay() {
        assertDoesNotThrow(() -> Preconditions.requireNonEmpty("Neither null nor empty", "Never thrown..."));
    }

    @ParameterizedTest(name = "[{index}] String \"{0}\" is null or empty")
    @NullAndEmptySource @ValueSource(strings = {"      "})
    void requireNonEmpty_withNullOrEmptyValue_exceptionThrown(String obj) {
        final String message = "Grrr...";
        APIPreconditionException thrown = catchThrowableOfType(() -> Preconditions.requireNonEmpty(obj, message), APIPreconditionException.class);
        assertThat(thrown).hasMessage(API_PRECONDITION_ERROR, message);
    }
}
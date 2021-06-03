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

package com.github.m0nk3y2k4.thetvdb.internal.util.http;

import static com.github.m0nk3y2k4.thetvdb.api.constants.Path.Series.SeasonType.ALTERNATE;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.URLPathTokenType.ID;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.URLPathTokenType.LANGUAGE;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.URLPathTokenType.LITERAL;
import static com.github.m0nk3y2k4.thetvdb.internal.util.http.URLPathTokenType.SEASON_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.testutils.parameterized.NullAndEmptyStringSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class URLPathTokenTypeTest {

    private static Stream<Arguments> forToken() {
        return Stream.of(
                Arguments.of("{id}", ID),
                Arguments.of("{language}", LANGUAGE),
                Arguments.of("{season-type}", SEASON_TYPE),
                Arguments.of("nonWildcardToken", LITERAL)
        );
    }

    private static Stream<Arguments> wildcardTypesWithCompatibleReplacementParameters() {
        return Stream.of(
                Arguments.of(ID, 4712L),
                Arguments.of(LANGUAGE, "eng"),
                Arguments.of(SEASON_TYPE, ALTERNATE)
        );
    }

    private static Stream<Arguments> wildcardTypesWithIncompatibleReplacementParameters() {
        return Stream.of(
                Arguments.of(ID, null),
                Arguments.of(ID, "Some String"),
                Arguments.of(LANGUAGE, null),
                Arguments.of(LANGUAGE, 4711L),
                Arguments.of(SEASON_TYPE, null),
                Arguments.of(SEASON_TYPE, "DVD String")
        );
    }

    @ParameterizedTest(name = "[{index}] With token \"{0}\" being resolved to {1}")
    @MethodSource("forToken")
    void forToken_withValidTokens_verifyResolvedTokenType(String token, URLPathTokenType expected) {
        assertThat(URLPathTokenType.forToken(token)).isEqualTo(expected);
    }

    @ParameterizedTest(name = "[{index}] With invalid token: \"{0}\"")
    @NullAndEmptyStringSource
    @ValueSource(strings = {"{unknown}", "{ID}", "{123}", "{i-d}", "{_language}"})
    void forToken_withInvalidTokens_verifyIllegalArgumentExceptionIsThrown(String token) {
        assertThatIllegalArgumentException().isThrownBy(() -> URLPathTokenType.forToken(token));
    }

    @ParameterizedTest(name = "[{index}] Parameter <{1}> is a valid wildcard replacement for token type {0}")
    @MethodSource("wildcardTypesWithCompatibleReplacementParameters")
    void checkParameterCompatibility_withCompatibleParameterTypes_verifyNoExceptionIsThrown(
            URLPathTokenType type, Object parameter) {
        assertThatCode(() -> type.checkParameterCompatibility(parameter)).doesNotThrowAnyException();
    }

    @Test
    void checkParameterCompatibility_withTypeNotSupportingWildcardReplacements_verifyExceptionIsThrown() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> LITERAL.checkParameterCompatibility("Does not support wildcard replacements"))
                .withMessageMatching("Tokens of type LITERAL don't support wildcard replacements");
    }

    @ParameterizedTest(name = "[{index}] Type {0} is not compatible with parameter <{1}>")
    @MethodSource("wildcardTypesWithIncompatibleReplacementParameters")
    void checkParameterCompatibility_withIncompatibleParameterTypes_verifyIllegalArgumentExceptionIsThrown(
            URLPathTokenType type, Object parameter) {
        assertThatIllegalArgumentException().isThrownBy(() -> type.checkParameterCompatibility(parameter))
                .withMessageMatching("Wildcard replacements for \\{[a-z\\-_]+} must be of type <[A-Za-z0-9.$]+>");
    }

    @ParameterizedTest(name = "[{index}] Type {0} is wildcard token")
    @EnumSource(value = URLPathTokenType.class, mode = EnumSource.Mode.EXCLUDE, names = "LITERAL")
    void isWildcardToken_withWildcardTokens_returnsTrue(URLPathTokenType type) {
        assertThat(type.isWildcardToken()).isTrue();
    }

    @ParameterizedTest(name = "[{index}] Type {0} is no wildcard token")
    @EnumSource(value = URLPathTokenType.class, names = "LITERAL")
    void isWildcardToken_withNonWildcardTokens_returnsFalse(URLPathTokenType type) {
        assertThat(type.isWildcardToken()).isFalse();
    }
}

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

package com.github.m0nk3y2k4.thetvdb.internal.util.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

class URLPathTokenTest {

    @Test
    void getToken_withWildcardAndLiteralTokens_verifyTokenOrPathParameterIsReturned() {
        URLPathToken idWildcard = new URLPathToken("{id}");
        URLPathToken languageWildcard = new URLPathToken("{language}");
        URLPathToken literal = new URLPathToken("episodes");

        idWildcard.replaceWildcard(57487L);
        languageWildcard.replaceWildcard("eng");

        assertThat(idWildcard.getToken()).isEqualTo("57487");
        assertThat(languageWildcard.getToken()).isEqualTo("eng");
        assertThat(literal.getToken()).isEqualTo("episodes");
    }

    @Test
    void getToken_withWildcardThatHasNotBeenReplaced_verifyExceptionIsThrown() {
        URLPathToken wildcard = new URLPathToken("{language}");
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(wildcard::getToken)
                .withMessage("No actual parameter value set for wildcard token");
    }

    @Test
    void isWildcardToken_withDifferentTokens_verifyTokenTypeWildcardFlagIsReturned() {
        assertThat(new URLPathToken("{id}").isWildcardToken()).isTrue();
        assertThat(new URLPathToken("series").isWildcardToken()).isFalse();
    }
}

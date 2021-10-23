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

package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class SearchResultDTOTest {

    @Test
    void staticBuilderClass_newInstance_extendsDTOBuilder() {
        assertThat(new SearchResultDTO.Builder()).isInstanceOf(SearchResultDTOBuilder.class);
    }

    @Test
    void createTranslationDTO_withNonEmptyValues_newDTOWithValuesIsCreated() {
        Optional<String> language = Optional.of("Language");
        Optional<String> translation = Optional.of("Translation");
        assertThat(SearchResultDTO.createTranslationDTO(language, translation)).satisfies(t -> {
            assertThat(t.getLanguage()).isEqualTo(language);
            assertThat(t.getTranslation()).isEqualTo(translation);
        });
    }
}

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

package com.github.m0nk3y2k4.thetvdb.internal.util.json.converter;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.github.m0nk3y2k4.thetvdb.api.model.data.SearchResult.Translation;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SearchResultDTO;
import com.github.m0nk3y2k4.thetvdb.internal.util.json.converter.SearchResultConverter.TranslationListItem;
import com.github.m0nk3y2k4.thetvdb.internal.util.json.converter.SearchResultConverter.TranslationObject;
import com.github.m0nk3y2k4.thetvdb.internal.util.json.converter.SearchResultConverter.TranslationString;
import com.github.m0nk3y2k4.thetvdb.testutils.parameterized.EmptyStringSource;
import com.github.m0nk3y2k4.thetvdb.testutils.parameterized.NullAndEmptyStringSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SearchResultConverterTest {

    @ParameterizedTest(name = "[{index}] With translation String: {0}")
    @ValueSource(strings = {"{\"eng\": \"The Skipper 3\"}", "\"eng\": \"The Skipper 3\"", "eng: The Skipper 3"})
    void convertTranslationString_WithAndWithoutEnclosingBracketsAndQuotes_TranslationProperlyMapped(String value) {
        List<Translation> translations = new TranslationString().convert(value);
        assertThat(translations).hasSize(1).allSatisfy(t -> {
            assertThat(t.getLanguage()).contains("eng");
            assertThat(t.getTranslation()).contains("The Skipper 3");
        });
    }

    @ParameterizedTest(name = "[{index}] With null or empty value: {0}")
    @NullAndEmptyStringSource
    @ValueSource(strings = "{}")
    void convertTranslationString_WithNoDataAtAll_NotMapped(String value) {
        assertThat(new TranslationString().convert(value)).isEmpty();
    }

    @ParameterizedTest(name = "[{index}] With separator but no translation data: {0}")
    @ValueSource(strings = {"{\"\": \"\"}", "\"\": \"\"", "\" \": \" \"", ": ", " : "})
    void convertTranslationString_WithSeparatorButNoActualTranslationData_MappedToEmptyTranslation(String value) {
        List<Translation> translations = new TranslationString().convert(value);
        assertThat(translations).hasSize(1).allSatisfy(t -> {
            assertThat(t.getLanguage()).isEmpty();
            assertThat(t.getTranslation()).isEmpty();
        });
    }

    @Test
    void convertTranslationString_WithNoTranslationDelimiter_MapWholeValueAsTranslation() {
        String value = "Name translation with no delimiter";
        List<Translation> translations = new TranslationString().convert(value);
        assertThat(translations).hasSize(1).allSatisfy(t -> {
            assertThat(t.getLanguage()).isEmpty();
            assertThat(t.getTranslation()).contains(value);
        });
    }

    @Test
    void convertTranslationString_WithMultipleTranslations_ProperlyMapped() {
        String value = "{\"eng\": \"Translation1\", \"eng\": \"Translation2\", \"spa\": \"Translation3\"}";
        List<Translation> translations = new TranslationString().convert(value);
        assertThat(translations).containsExactlyInAnyOrder(
                SearchResultDTO.createTranslationDTO(Optional.of("eng"), Optional.of("Translation1")),
                SearchResultDTO.createTranslationDTO(Optional.of("eng"), Optional.of("Translation2")),
                SearchResultDTO.createTranslationDTO(Optional.of("spa"), Optional.of("Translation3"))
        );
    }

    @ParameterizedTest(name = "[{index}] With empty translation String: {0}")
    @EmptyStringSource
    @ValueSource(strings = {": ", " : "})
    void convertTranslationListItem_WithNoActualData_MappedToEmptyTranslation(String value) {
        Translation translation = new TranslationListItem().convert(value);
        assertThat(translation).satisfies(t -> {
            assertThat(t.getLanguage()).isEmpty();
            assertThat(t.getTranslation()).isEmpty();
        });
    }

    @Test
    void convertTranslationListItem_WithNoTranslationDelimiter_MapWholeValueAsTranslation() {
        String value = "Overview translation with no delimiter";
        Translation translation = new TranslationListItem().convert(value);
        assertThat(translation).satisfies(t -> {
            assertThat(t.getLanguage()).isEmpty();
            assertThat(t.getTranslation()).contains(value);
        });
    }

    @Test
    void convertTranslationListItem_WithMultipleTranslations_ProperlyMapped() {
        Translation french = new TranslationListItem().convert("fra: Translation1");
        Translation spanish = new TranslationListItem().convert("spa: Translation2");
        Translation portuguese = new TranslationListItem().convert("por: Translation3");
        assertThat(french).isEqualTo(SearchResultDTO.createTranslationDTO(Optional.of("fra"), Optional.of("Translation1")));
        assertThat(spanish).isEqualTo(SearchResultDTO.createTranslationDTO(Optional.of("spa"), Optional.of("Translation2")));
        assertThat(portuguese).isEqualTo(SearchResultDTO.createTranslationDTO(Optional.of("por"), Optional.of("Translation3")));
    }

    @Test
    void convertTranslationObject_WithNoActualData_MappedToEmptyTranslation() {
        List<Translation> translations = new TranslationObject().convert(emptyMap());
        assertThat(translations).isEmpty();
    }

    @ParameterizedTest(name = "[{index}] With empty translation String: {0}")
    @NullAndEmptyStringSource
    void convertTranslationListItem_WithNoActualTranslationData_MappedToEmptyTranslation(String value) {
        List<Translation> translations = new TranslationObject().convert(singletonMap("eng", value));
        assertThat(translations).hasSize(1).allSatisfy(t -> {
            assertThat(t.getLanguage()).contains("eng");
            assertThat(t.getTranslation()).isEmpty();
        });
    }

    @Test
    void convertTranslationObject_WithMultipleTranslations_ProperlyMapped() {
        Map<String, String> translationMap = Map.of("por", "Translation1", "fra", "Translation2", "spa", "Translation3");
        List<Translation> translations = new TranslationObject().convert(translationMap);
        assertThat(translations).hasSize(3).containsExactlyInAnyOrder(
                SearchResultDTO.createTranslationDTO(Optional.of("por"), Optional.of("Translation1")),
                SearchResultDTO.createTranslationDTO(Optional.of("fra"), Optional.of("Translation2")),
                SearchResultDTO.createTranslationDTO(Optional.of("spa"), Optional.of("Translation3"))
        );
    }
}

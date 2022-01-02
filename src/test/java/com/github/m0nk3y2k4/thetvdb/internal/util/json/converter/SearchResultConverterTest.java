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

package com.github.m0nk3y2k4.thetvdb.internal.util.json.converter;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import com.github.m0nk3y2k4.thetvdb.api.model.data.SearchResultTranslation;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Translations;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SearchResultTranslationDTO;
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
        Translations<SearchResultTranslation> translations = new TranslationString().convert(value);
        assertThat(translations.getAllTranslations()).hasSize(1).allSatisfy(t -> {
            assertThat(t.getLanguage()).contains("eng");
            assertThat(t.getTranslation()).contains("The Skipper 3");
        });
    }

    @ParameterizedTest(name = "[{index}] With null or empty value: {0}")
    @NullAndEmptyStringSource
    @ValueSource(strings = "{}")
    void convertTranslationString_WithNoDataAtAll_NotMapped(String value) {
        assertThat(new TranslationString().convert(value).getAllTranslations()).isEmpty();
    }

    @ParameterizedTest(name = "[{index}] With separator but no translation data: {0}")
    @ValueSource(strings = {"{\"\": \"\"}", "\"\": \"\"", "\" \": \" \"", ": ", " : "})
    void convertTranslationString_WithSeparatorButNoActualTranslationData_MappedToEmptyTranslation(String value) {
        Translations<SearchResultTranslation> translations = new TranslationString().convert(value);
        assertThat(translations.getAllTranslations()).hasSize(1).allSatisfy(t -> {
            assertThat(t.getLanguage()).isNull();
            assertThat(t.getTranslation()).isNull();
        });
    }

    @Test
    void convertTranslationString_WithNoTranslationDelimiter_MapWholeValueAsTranslation() {
        String value = "Name translation with no delimiter";
        Translations<SearchResultTranslation> translations = new TranslationString().convert(value);
        assertThat(translations.getAllTranslations()).hasSize(1).allSatisfy(t -> {
            assertThat(t.getLanguage()).isNull();
            assertThat(t.getTranslation()).contains(value);
        });
    }

    @Test
    void convertTranslationString_WithMultipleTranslations_ProperlyMapped() {
        String value = "{\"eng\": \"Translation1\", \"eng\": \"Translation2\", \"spa\": \"Translation3\"}";
        Translations<SearchResultTranslation> translations = new TranslationString().convert(value);
        assertThat(translations.getAllTranslations()).containsExactlyInAnyOrder(
                new SearchResultTranslationDTO.Builder().language("eng").translation("Translation1").build(),
                new SearchResultTranslationDTO.Builder().language("eng").translation("Translation2").build(),
                new SearchResultTranslationDTO.Builder().language("spa").translation("Translation3").build()
        );
    }

    @ParameterizedTest(name = "[{index}] With empty translation String: {0}")
    @EmptyStringSource
    @ValueSource(strings = {": ", " : "})
    void convertTranslationListItem_WithNoActualData_MappedToEmptyTranslation(String value) {
        SearchResultTranslation translation = new TranslationListItem().convert(value);
        assertThat(translation).satisfies(t -> {
            assertThat(t.getLanguage()).isNull();
            assertThat(t.getTranslation()).isNull();
        });
    }

    @Test
    void convertTranslationListItem_WithNoTranslationDelimiter_MapWholeValueAsTranslation() {
        String value = "Overview translation with no delimiter";
        SearchResultTranslation translation = new TranslationListItem().convert(value);
        assertThat(translation).satisfies(t -> {
            assertThat(t.getLanguage()).isNull();
            assertThat(t.getTranslation()).contains(value);
        });
    }

    @Test
    void convertTranslationListItem_WithMultipleTranslations_ProperlyMapped() {
        SearchResultTranslation french = new TranslationListItem().convert("fra: Translation1");
        SearchResultTranslation spanish = new TranslationListItem().convert("spa: Translation2");
        SearchResultTranslation portuguese = new TranslationListItem().convert("por: Translation3");
        assertThat(french.getLanguage()).isEqualTo("fra");
        assertThat(french.getTranslation()).isEqualTo("Translation1");
        assertThat(spanish.getLanguage()).isEqualTo("spa");
        assertThat(spanish.getTranslation()).isEqualTo("Translation2");
        assertThat(portuguese.getLanguage()).isEqualTo("por");
        assertThat(portuguese.getTranslation()).isEqualTo("Translation3");
    }

    @Test
    void convertTranslationObject_WithNoActualData_MappedToEmptyTranslation() {
        Translations<SearchResultTranslation> translations = new TranslationObject().convert(emptyMap());
        assertThat(translations.getAllTranslations()).isEmpty();
    }

    @ParameterizedTest(name = "[{index}] With empty translation String: {0}")
    @NullAndEmptyStringSource
    void convertTranslationListItem_WithNoActualTranslationData_MapOnlyLanguage(String value) {
        Translations<SearchResultTranslation> translations = new TranslationObject().convert(singletonMap("eng", value));
        assertThat(translations.getAllTranslations()).hasSize(1).allSatisfy(t -> {
            assertThat(t.getLanguage()).contains("eng");
            assertThat(t.getTranslation()).isNull();
        });
    }

    @Test
    void convertTranslationObject_WithMultipleTranslations_ProperlyMapped() {
        Map<String, String> translationMap = Map.of("por", "Translation1", "fra", "Translation2", "spa", "Translation3");
        Translations<SearchResultTranslation> translations = new TranslationObject().convert(translationMap);
        assertThat(translations.getAllTranslations()).hasSize(3).containsExactlyInAnyOrder(
                new SearchResultTranslationDTO.Builder().language("por").translation("Translation1").build(),
                new SearchResultTranslationDTO.Builder().language("fra").translation("Translation2").build(),
                new SearchResultTranslationDTO.Builder().language("spa").translation("Translation3").build()
        );
    }
}

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

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.github.m0nk3y2k4.thetvdb.api.model.data.EntityTranslation;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SearchResultTranslation;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Translations;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.EntityTranslationDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SearchResultTranslationDTO;
import org.junit.jupiter.api.Test;

class TranslationsConverterTest {

    @Test
    void convert_WithTwoEntityTranslationItems_ItemsAreProperlyMapped() {
        List<EntityTranslation> translationItems = List.of(
                new EntityTranslationDTO.Builder().language("ita").name("Una traduzione").isPrimary(false).build(),
                new EntityTranslationDTO.Builder().language("rus").name("Перевод").isPrimary(true).build()
        );

        Translations<EntityTranslation> translations = new TranslationsConverter<EntityTranslation>()
                .convert(translationItems);

        assertThat(translations.getAllTranslations()).containsExactlyInAnyOrderElementsOf(translationItems);
    }

    @Test
    void convert_WithSingleSearchResultTranslationItem_ItemsAreProperlyMapped() {
        List<SearchResultTranslation> translationItems = singletonList(
                new SearchResultTranslationDTO.Builder().language("jpn").translation("翻訳").build()
        );

        Translations<SearchResultTranslation> translations = new TranslationsConverter<SearchResultTranslation>()
                .convert(translationItems);

        assertThat(translations.getAllTranslations()).containsExactlyInAnyOrderElementsOf(translationItems);
    }
}

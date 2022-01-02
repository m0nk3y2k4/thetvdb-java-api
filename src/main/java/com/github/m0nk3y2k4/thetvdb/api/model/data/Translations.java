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

package com.github.m0nk3y2k4.thetvdb.api.model.data;

import java.util.List;
import java.util.Optional;

/**
 * Wrapper interface for collections of different kinds of translation data transfer objects.
 * <p><br>
 * Objects of this class wrap around a list of specific translation DTO's and provide convenience functionality to
 * simplify working with translations, like for example direct access to the translation for a specific language.
 *
 * @param <T> The type of translations held by this object
 */
public interface Translations<T extends Translated> {

    /**
     * Returns all available translation information
     *
     * @return List of all translations available
     */
    List<T> getAllTranslations();

    /**
     * Returns an Optional containing translation information for the given language or an empty Optional if no such
     * information is present.
     *
     * @param language The 2- or 3-character language code for which the translation information should be returned
     *
     * @return Optional containing the requested translation information or an empty Optional if no translation is
     *         available for the given language
     */
    Optional<T> get(String language);
}

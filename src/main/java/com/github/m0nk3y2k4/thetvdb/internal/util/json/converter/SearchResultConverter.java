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

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SearchResultTranslation;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Translations;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SearchResultTranslationDTO;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil.BracketType;

/**
 * Contains multiple JSON converters used in conjunction with deserialization of {@link
 * com.github.m0nk3y2k4.thetvdb.api.model.data.SearchResult} objects.
 * <p><br>
 * Nested converters from this class may be used to convert data from Jackson-bound intermediate types into actual
 * property types. This is basically a two-step deserialization; Jackson binds data into a suitable intermediate type
 * and the converter then builds actual property type.
 */
public final class SearchResultConverter {

    /** Marker that separates the language abbreviation and the actual translation inside the received JSON String */
    private static final String TRANSLATION_DELIMITER = ":";

    /** Wrapper class. Should not be instantiated */
    private SearchResultConverter() {}  // Provides only shared functionality for the actual converter implementations

    /**
     * Extracts the language (code) part from the given value. If the value contains no translation delimiter or the
     * language section contains no data, an empty Optional will be returned.
     *
     * @param value The String value to extract the language from
     *
     * @return Optional representing the language part of the given value
     */
    @SuppressWarnings("MethodOnlyUsedFromInnerClass")
    private static Optional<String> extractLanguage(String value) {
        return getDelimiterIndex(value).map(idx -> value.substring(0, idx)).filter(APIUtil::hasValue);
    }

    /**
     * Extracts the translation part from the given value. If the value contains no translation delimiter the whole
     * String will be returned.
     *
     * @param value The String value to extract the translation from
     *
     * @return Optional representing the translation part of the given value or an empty Optional if not translation
     *         data is available
     */
    @SuppressWarnings("MethodOnlyUsedFromInnerClass")
    private static Optional<String> extractTranslation(String value) {
        Optional<Integer> delimiterIndex = getDelimiterIndex(value);
        if (delimiterIndex.isPresent()) {
            return delimiterIndex.map(idx -> idx + 1).map(value::substring).filter(APIUtil::hasValue);
        }
        return Optional.of(value).filter(APIUtil::hasValue); // Return whole String if it contains no delimiter
    }

    /**
     * Returns an Optional representing the position of the translation delimiter inside the given String. Returns an
     * empty Optional if the String contains no delimiter.
     *
     * @param value The String value to be checked for the occurrence of a translation delimiter
     *
     * @return Index of the translation delimiter inside the given String or an empty Optional if the String contains no
     *         delimiter.
     */
    private static Optional<Integer> getDelimiterIndex(String value) {
        return Optional.of(value.indexOf(TRANSLATION_DELIMITER)).filter(idx -> idx >= 0);
    }

    /**
     * Converts the given String value into a corresponding translation object using the given extractors to separate
     * language and translation information.
     *
     * @param value          String containing the single translation
     * @param getLanguage    Helper to extract the language part from the translation String
     * @param getTranslation Helper to extract the translation part from the translation String
     *
     * @return Translation object converted from the given translation String
     */
    @SuppressWarnings({"TypeMayBeWeakened", "MethodOnlyUsedFromInnerClass"})
    private static SearchResultTranslation convert(String value, Extractor getLanguage, Extractor getTranslation) {
        return new SearchResultTranslationDTO.Builder()
                .language(Optional.ofNullable(value).flatMap(getLanguage).map(String::trim).orElse(null))
                .translation(Optional.ofNullable(value).flatMap(getTranslation).map(String::trim).orElse(null))
                .build();
    }

    /**
     * Simple interface representing a {@link Function} that accepts a String value and returns an Optional.
     * <p><br>
     * Used as a synonym to hide implementation details and increase readability.
     */
    @FunctionalInterface
    private interface Extractor extends Function<String, Optional<String>> {

        /**
         * Creates a new extractor that combines the functionality of this one with some additional post-processing
         * operations.
         *
         * @param after Operation to be performed on the output created by this extractor
         *
         * @return New extractor with additional post-processing functionality
         */
        default Extractor andThen(UnaryOperator<Optional<String>> after) {
            return s -> after.apply(apply(s));
        }
    }

    /**
     * Used to convert multi-translation Strings into a list of immutable translation objects.
     * <p><br>
     * The actual translations will be transmitted in one single String value and may be enclosed in braces and contain
     * additional quote characters. The language code and the actual translation will be separated by a {@value
     * TRANSLATION_DELIMITER} with multiple translations being comma-separated. For example:
     * <pre>{@code
     * { \"por\": \"Rei Naresuan 3\", \"eng\": \"King Naresuan 3\" }
     * }</pre>
     * Using this converter, the actual language and translation information will be mapped into a list of corresponding
     * {@link SearchResultTranslation} objects with unnecessary characters being stripped of.
     */
    public static final class TranslationString extends StdConverter<String, Translations<SearchResultTranslation>> {

        /** Pattern for matching escaped quote characters */
        @SuppressWarnings("RegExpRedundantEscape")
        private static final Pattern ESCAPED_QUOTE = Pattern.compile("\\\"");
        /** Pattern for splitting the single translation elements */
        private static final Pattern ELEMENT_SEPARATOR = Pattern.compile(",");

        /**
         * Extends the given extractor by some functionality to remove escaped quote characters from the extracted
         * String.
         *
         * @param extract The base extractor that should be extended
         *
         * @return A new extractor that combines the functionality of the given extractor with some additional quote
         *         character stripping logic
         */
        private static Extractor removeQuotes(Extractor extract) {
            return extract.andThen(value -> value.map(ESCAPED_QUOTE::matcher)
                    .map(matcher -> matcher.replaceAll("")).filter(APIUtil::hasValue));
        }

        /**
         * Converts a single multi-translation JSON String into a list of corresponding {@link SearchResultTranslation}
         * objects with unnecessary characters being stripped of.
         *
         * @param value The String value as received in the JSON response
         *
         * @return List of translation objects based on the given JSON data
         */
        @Override
        public Translations<SearchResultTranslation> convert(@Nullable String value) {
            return new TranslationsConverter<SearchResultTranslation>().convert(Optional.ofNullable(value)
                    .map(s -> APIUtil.removeEnclosingBrackets(s, BracketType.BRACES))
                    .filter(APIUtil::hasValue)
                    .map(ELEMENT_SEPARATOR::split)
                    .map(Arrays::asList)
                    .map(translations -> translations.stream()
                            .map(translation -> SearchResultConverter.convert(translation,
                                    removeQuotes(SearchResultConverter::extractLanguage),
                                    removeQuotes(SearchResultConverter::extractTranslation)))
                            .collect(Collectors.toList()))
                    .orElseGet(Collections::emptyList));
        }
    }

    /**
     * Used to convert the single items of a JSON translation String array to an immutable translation object.
     * <p><br>
     * The actual data will be transmitted as an array of String values with each value representing a single
     * translation. The language code and the actual translation will be separated by a {@value TRANSLATION_DELIMITER}.
     * For example:
     * <pre>{@code
     * [ "por: Rei Naresuan 3", "eng: King Naresuan 3" ]
     * }</pre>
     * Using this converter, the actual language and translation information will be mapped into a corresponding {@link
     * SearchResultTranslation} object.
     */
    public static final class TranslationListItem extends StdConverter<String, SearchResultTranslation> {

        /**
         * Converts the single items of a JSON translation String array to a corresponding {@link
         * SearchResultTranslation} object.
         *
         * @param value Single item from the received JSON String array
         *
         * @return Translation object based on the given JSON data
         */
        @Override
        public SearchResultTranslation convert(@Nullable String value) {
            return Optional.ofNullable(value)
                    .map(v -> SearchResultConverter.convert(v,
                            SearchResultConverter::extractLanguage,
                            SearchResultConverter::extractTranslation))
                    .orElse(null);
        }
    }

    /**
     * Used to convert the single properties of a JSON translation object into a list of immutable translation objects.
     * <p><br>
     * The actual data will be transmitted as distinct JSON object with each property representing a single translation.
     * The property's name is the language code whereas it's value represents the actual translation. For example:
     * <pre>{@code
     * { "por": "Rei Naresuan 3", "eng": "King Naresuan 3" }
     * }</pre>
     * Using this converter, the actual language and translation information will be mapped into a list of corresponding
     * {@link SearchResultTranslation} objects.
     */
    public static final class TranslationObject extends StdConverter<Map<String, String>, Translations<SearchResultTranslation>> {

        /**
         * Converts a JSON translation object into a list of corresponding {@link SearchResultTranslation} object with
         * the objects properties representing the languages keys and their values representing the actual translation.
         *
         * @param translations Translation JSON object in its default Jackson-bound intermediate type (property name
         *                     &lt;&gt; value)
         *
         * @return List of translation objects based on the given Map from the JSON data
         */
        @Override
        public Translations<SearchResultTranslation> convert(Map<String, String> translations) {
            return new TranslationsConverter<SearchResultTranslation>().convert(translations.entrySet().stream()
                    .map(e -> SearchResultConverter.convert("",
                            s -> Optional.ofNullable(e.getKey()).filter(APIUtil::hasValue),
                            s -> Optional.ofNullable(e.getValue()).filter(APIUtil::hasValue))
                    ).collect(Collectors.toList()));
        }
    }
}

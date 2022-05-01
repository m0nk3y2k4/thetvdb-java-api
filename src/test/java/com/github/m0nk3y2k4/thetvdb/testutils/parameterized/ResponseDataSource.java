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

package com.github.m0nk3y2k4.thetvdb.testutils.parameterized;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.platform.commons.PreconditionViolationException;
import org.junit.platform.commons.util.Preconditions;

/**
 * Custom test data source annotation for {@link ResponseDataProvider}
 * <p><br>
 * Use this annotation to get access to a set of predefined
 * {@link com.github.m0nk3y2k4.thetvdb.testutils.ResponseData ResponseData&lt;T&gt;} test objects in parameterized JUnit
 * tests. Without further configuration all available test objects will be applied. To consider only specific
 * {@code ResponseData} instances, use one of the following modes in combination with the <em>{@code names}</em>
 * parameter:
 * <ul>
 *     <li>{@link Mode#INCLUDE} - Select only those test objects whose names are supplied via the {@link #names()} attribute</li>
 *     <li>{@link Mode#EXCLUDE} - Select all declared test objects except those supplied via the {@link #names} attribute</li>
 *     <li>{@link Mode#MATCH_ALL} - Select only those test objects whose names match all patterns supplied via the {@link #names} attribute</li>
 *     <li>{@link Mode#MATCH_ANY} - Select only those test objects whose names match any pattern supplied via the {@link #names} attribute</li>
 * </ul>
 * If no {@code Mode} is specified {@link Mode#INCLUDE} will be used as default. The provided names must match the
 * property names of the corresponding test object constant, for example {@code "ARTWORK"}.
 * <pre><code>
 *     {@literal @ResponseDataSource}
 *     void testAll(ResponseData data) {
 *         // Runs the test with all existing predefined response test data objects
 *     }
 *
 *     {@literal @ResponseDataSource}(names = "PEOPLE")
 *     void testPeopleFull(ResponseData data) {
 *         // Runs the test only with the ResponseData.PEOPLE test object
 *     }
 *
 *     {@literal @ResponseDataSource}(mode = Mode.EXCLUDE, names = {"PEOPLE", "PEOPLE_MIN"})
 *     void testAllExceptPeople(ResponseData data) {
 *         // Runs the test with all existing response test data objects except for PEOPLE and PEOPLE_MIN
 *     }
 *
 *     {@literal @ResponseDataSource}(mode = Mode.MATCH_ANY, names = "ARTWORK.*")
 *     void testArtworkStuff(ResponseData data) {
 *         // Runs the test with all artwork related test data objects, e.g. ARTWORK, ARTWORKTYPE_OVERVIEW, ...
 *     }
 * </code></pre>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(ResponseDataProvider.class)
public @interface ResponseDataSource {

    /**
     * The response test data instances selection mode
     */
    @SuppressWarnings("unused")
    enum Mode {
        /** Select only those test objects whose names are supplied via the {@link #names()} attribute */
        INCLUDE(Mode::validateNames, (name, names) -> names.contains(name)),

        /** Select all declared test objects except those supplied via the {@link #names} attribute */
        EXCLUDE(Mode::validateNames, (name, names) -> !names.contains(name)),

        /** Select only those test objects whose names match all patterns supplied via the {@link #names} attribute */
        MATCH_ALL(Mode::validatePatterns, (name, patterns) -> patterns.stream().allMatch(name::matches)),

        /** Select only those test objects whose names match any pattern supplied via the {@link #names} attribute */
        MATCH_ANY(Mode::validatePatterns, (name, patterns) -> patterns.stream().anyMatch(name::matches));

        /** To check whether the provided names are valid patterns or map to an actual predefined test data object */
        private final Mode.Validator validator;

        /** To determine whether a specific test data object is to be in- or excluded */
        private final BiPredicate<String, Collection<String>> selector;

        /**
         * Creates a new mode using the given parameters
         *
         * @param validator To check whether the provided names are valid patterns or map to an actual predefined test
         *                  data object
         * @param selector  To determine whether a specific test data object is to be in- or excluded
         */
        Mode(Mode.Validator validator, BiPredicate<String, Collection<String>> selector) {
            this.validator = validator;
            this.selector = selector;
        }

        /**
         * Default validator for {@link #INCLUDE} and {@link #EXCLUDE} mode. Checks whether the given
         * <em>{@code names}</em> all resolve to an existing response test data instance.
         *
         * @param source      Reference to the corresponding JUnit test annotation
         * @param testObjects List of available test data objects
         * @param names       List of names of test data objects to be in- or excluded
         */
        private static void validateNames(ResponseDataSource source, Collection<Field> testObjects,
                Collection<String> names) {
            Set<String> allNames = testObjects.stream().map(Field::getName).collect(Collectors.toSet());
            Preconditions.condition(allNames.containsAll(names), () ->
                    String.format("Invalid test object name(s) in %s. Valid names include: %s", source, allNames));
        }

        /**
         * Default validator for {@link #MATCH_ALL} and {@link #MATCH_ANY} mode. Checks whether the given
         * <em>{@code patterns}</em> are valid regular expressions.
         *
         * @param source      Reference to the corresponding JUnit test annotation
         * @param testObjects List of available test data objects
         * @param names       List of test data object naming patterns
         */
        private static void validatePatterns(ResponseDataSource source, Collection<Field> testObjects,
                Iterable<String> names) {
            try {
                names.forEach(Pattern::compile);
            } catch (PatternSyntaxException ex) {
                throw new PreconditionViolationException("Pattern compilation failed for a regular expression supplied in " + source, ex);
            }
        }

        /**
         * Check whether the provided names are valid patterns or map to an actual predefined test data object,
         * depending on the selected {@code Mode}
         *
         * @param source      Reference to the corresponding JUnit test annotation
         * @param testObjects List of available test data objects
         * @param names       List of names or naming patterns of test data objects
         */
        void validate(ResponseDataSource source, Collection<Field> testObjects, Collection<String> names) {
            this.validator.validate(source, testObjects, names);
        }

        /**
         * Determines whether a specific test data object is to be in- or excluded from JUnit test execution
         *
         * @param testObject One of the available test data objects
         * @param names      List of names or naming patterns of test data objects to be in- or excluded
         *
         * @return TRUE if the given <em>{@code testObject}</em> is to be included in the current JUnit test execution
         *         or FALSE if it has to be excluded from it
         */
        boolean isSelected(Member testObject, Collection<String> names) {
            return this.selector.test(testObject.getName(), names);
        }

        @FunctionalInterface
        private interface Validator {
            void validate(ResponseDataSource jsonSource, Collection<Field> testObjects, Collection<String> names);
        }
    }

    /** The names of test objects to provide, or regular expressions to select the names of test objects to provide */
    String[] names() default {};

    /** The test data object selection mode */
    Mode mode() default Mode.INCLUDE;
}

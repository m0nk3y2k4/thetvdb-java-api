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

package com.github.m0nk3y2k4.thetvdb.internal.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * General utility class providing a collection of useful little helper methods.
 * <p><br>
 * Contains various non-specific helper methods used to process common tasks like checking for (non)empty Strings or
 * converting values into a String representation.
 */
public final class APIUtil {

    /** Some stateless unconfigured JSON object mapper */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /** JSON pretty printer with fix LF linefeed */
    private static final PrettyPrinter UNIX_LINEFEED_PRINTER = new DefaultPrettyPrinter()
            .withObjectIndenter(new DefaultIndenter().withLinefeed("\n"));

    private APIUtil() {}     // Hidden constructor. Only static methods

    /**
     * Checks if all the given Strings are non-empty character sequences
     *
     * @param strings The strings to check
     *
     * @return {@link Boolean#FALSE} if at least one of the given Strings is <em>{@code null}</em> or contains only
     *         blanks.
     */
    public static boolean hasValue(String... strings) {
        if (strings == null) {
            return false;
        }
        return Arrays.stream(strings).noneMatch(s -> s == null || s.trim().isEmpty());
    }

    /**
     * Checks if at least one of the given strings is an empty character sequence
     *
     * @param strings The strings to check
     *
     * @return {@link Boolean#TRUE} if at least one of the given Strings is <em>{@code null}</em> or contains only
     *         blanks.
     */
    public static boolean hasNoValue(String... strings) {
        return !hasValue(strings);
    }

    /**
     * Converts a Maps values from one type to another using the given converter function.
     *
     * @param map       The input map that should be converted
     * @param converter Function to convert values from the input to the output format
     * @param <T>       The type of values of the input map
     * @param <U>       The new type of values of the returned map
     *
     * @return A new Map with its values being converted based on the given function
     */
    public static <T, U> Map<String, U> convert(Map<String, T> map, Function<T, U> converter) {
        BiConsumer<HashMap<String, U>, ? super Entry<String, T>> accumulator = (out, entry) ->
                out.put(entry.getKey(), Optional.ofNullable(entry.getValue()).map(converter).orElse(null));
        return map.entrySet().stream().collect(HashMap::new, accumulator, HashMap::putAll); // Supports NULL values
    }

    /**
     * Serializes the given JSON object as String using a pretty print output with indentation to produce an
     * easy-to-read String representation of the JSON object.
     *
     * @param obj The JSON object to be printed in a pretty fashion
     *
     * @return Easy-to-read String representation of the given JSON object
     *
     * @throws JsonProcessingException If an error occurred while processing the JSON object
     */
    public static String prettyPrint(@Nonnull JsonNode obj) throws JsonProcessingException {
        return MAPPER.writer(UNIX_LINEFEED_PRINTER).writeValueAsString(obj);
    }

    /**
     * Converts the value returned by the given <em>{@code valueSupplier}</em> into a new String. The output depends on
     * the actual type argument &lt;T&gt; of the given supplier. If the supplied value is unconvertible an empty String
     * will be returned instead.
     *
     * @param valueSupplier The supplier whose {@link Supplier#get()} output should be converted into a String
     * @param <T>           The type of results supplied by the given supplier
     *
     * @return Supplier output value converted to a String representation or an empty String
     */
    public static <T> String toString(Supplier<T> valueSupplier) {
        return toString(valueSupplier, "");
    }

    /**
     * Converts the value returned by the given <em>{@code valueSupplier}</em> into a new String. The output depends on
     * the actual type argument &lt;T&gt; of the given supplier. If the supplied value is unconvertible the value of
     * <em>{@code nullDefault}</em> will be returned instead.
     *
     * @param valueSupplier The supplier whose {@link Supplier#get()} output should be converted into a String
     * @param nullDefault   The fallback default value to be returned by this method if the supplied value could not be
     *                      converted
     * @param <T>           The type of results supplied by the given supplier
     *
     * @return Supplier output value converted to a String representation or <em>{@code nullDefault}</em>
     */
    public static <T> String toString(Supplier<T> valueSupplier, String nullDefault) {
        Optional<T> optValue = Optional.ofNullable(valueSupplier).map(Supplier::get);
        if (optValue.isPresent()) {
            T value = optValue.get();
            if (value instanceof Collection<?>) {
                return ((Collection<?>)value).stream().map(Object::toString).collect(Collectors.joining(", "));
            } else if (value instanceof Optional<?>) {
                return ((Optional<?>)value).map(Object::toString).orElse(nullDefault);
            } else {
                return value.toString();
            }
        }
        return nullDefault;
    }

    public enum BracketType {
        /** Parentheses a.k.a. round brackets: '( )' */
        PARENTHESES("(", ")"),
        /** Brackets a.k.a. square brackets: '[ ]' */
        BRACKETS("[", "]"),
        /** Braces a.k.a. curly brackets: '{ }' */
        BRACES("{", "}"),
        /** Angle brackets a.k.a. chevrons: '&lt; &gt;' */
        ANGLE("<", ">");

        /** Opening bracket for this type */
        final String opening;
        /** Closing bracket for this type */
        final String closing;

        /**
         * Creates a new bracket type constant
         *
         * @param opening The opening bracket for this type
         * @param closing The closing bracket for this type
         */
        BracketType(String opening, String closing) {
            this.opening = opening;
            this.closing = closing;
        }
    }

    /**
     * Removes leading and tailing brackets of a specific type from the given String value if present. Only the first
     * and the last occurrence will be removed. Also supports Strings that only contain an opening but no closing
     * bracket and vice versa.
     *
     * @param value       The value to remove the brackets from
     * @param bracketType The type of brackets to be removed
     *
     * @return String value without leading and tailing brackets
     */
    public static String removeEnclosingBrackets(String value, BracketType bracketType) {
        return value.substring(
                value.startsWith(bracketType.opening) ? 1 : 0,
                value.endsWith(bracketType.closing) ? value.length() - 1 : value.length()
        );
    }
}

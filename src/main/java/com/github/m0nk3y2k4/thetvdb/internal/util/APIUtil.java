package com.github.m0nk3y2k4.thetvdb.internal.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * General utility class providing a collection of useful little helper methods.
 * <p><br>
 * Contains various non-specific helper methods used to process common tasks like checking for (non)empty Strings or converting
 * values into a String representation.
 */
public final class APIUtil {

    /** Some stateless unconfigured JSON object mapper */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private APIUtil() {}     // Hidden constructor. Only static methods

    /**
     * Checks if all of the given Strings are non-empty character sequences
     *
     * @param strings The strings to check
     *
     * @return {@link Boolean#FALSE} if at least one of the given Strings is <em>{@code null}</em> or contains only blanks.
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
     * @return {@link Boolean#TRUE} if at least one of the given Strings is <em>{@code null}</em> or contains only blanks.
     */
    public static boolean hasNoValue(String... strings) {
        return !hasValue(strings);
    }

    /**
     * Serializes the given JSON object as String using a pretty print output with indentation to produce an easy-to-read
     * String representation of the JSON object.
     *
     * @param obj The JSON object to be printed in a pretty fashion
     *
     * @return Easy-to-read String representation of the given JSON object
     * @throws JsonProcessingException If an error occurred while processing the JSON object
     */
    public static String prettyPrint(@Nonnull JsonNode obj) throws JsonProcessingException {
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    /**
     * Converts the value returned by the given <em>{@code valueSupplier}</em> into a new String. The output depends on the
     * actual type argument &lt;T&gt; of the given supplier. If the supplied value is unconvertible an empty String will be
     * returned instead.
     *
     * @param valueSupplier The supplier whose {@link Supplier#get()} output should be converted into a String
     * @param <T> The type of results supplied by the given supplier
     *
     * @return Supplier output value converted to a String representation or an empty String
     */
    public static <T> String toString(Supplier<T> valueSupplier) {
        return toString(valueSupplier, "");
    }

    /**
     * Converts the value returned by the given <em>{@code valueSupplier}</em> into a new String. The output depends on the
     * actual type argument &lt;T&gt; of the given supplier. If the supplied value is unconvertible the value of
     * <em>{@code nullDefault}</em> will be returned instead.
     *
     * @param valueSupplier The supplier whose {@link Supplier#get()} output should be converted into a String
     * @param nullDefault The fallback default value to be returned by this method if the supplied value could not be converted
     * @param <T> The type of results supplied by the given supplier
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
}

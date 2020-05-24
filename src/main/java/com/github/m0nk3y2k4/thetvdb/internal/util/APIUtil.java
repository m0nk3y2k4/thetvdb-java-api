package com.github.m0nk3y2k4.thetvdb.internal.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class APIUtil {

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

    public static String prettyPrint(@Nonnull JsonNode obj) throws IOException {
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    public static <T> String toString(Supplier<T> valueSupplier) {
        return toString(valueSupplier, "");
    }

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

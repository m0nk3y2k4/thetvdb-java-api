package com.github.m0nk3y2k4.thetvdb.internal.util;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class APIUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private APIUtil() {}     // Hidden constructor. Only static methods

    /**
     * Checks if all of the given Strings are non-empty character sequences
     * @param strings The strings to check
     *
     * @return {@link Boolean#FALSE} if at least one of the given Strings is <code>null</code> or contains only blanks.
     */
    public static Boolean hasValue(String... strings) {
        if (strings.length == 0) {
            return false;
        }

        for (String s : strings) {
            if (s == null || s.trim().isEmpty())
            return false;
        }

        return true;
    }

    /**
     * Checks if at least one of the given strings is an empty character sequence
     * @param strings The strings to check
     *
     * @return {@link Boolean#TRUE} if at least one of the given Strings is <code>null</code> or contains only blanks.
     */
    public static Boolean hasNoValue(String... strings) {
        return !hasValue(strings);
    }

    public static String prettyPrint(@Nonnull JsonNode obj) throws IOException {
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }
}

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

package com.github.m0nk3y2k4.thetvdb.internal.util.http;

import static java.util.Objects.nonNull;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.api.enumeration.SeriesSeasonType;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Representation of the different types of URL path parameter tokens.
 * <p><br>
 * Maintains distinct enumerations for each supported wildcard token as well as some enumeration representing an
 * unspecified literal token. The following wildcard tokens are currently supported.
 * <ul>
 *     <li><em>{@code {id}}</em> - placeholder for some {@link Long} ID value</li>
 *     <li><em>{@code {language}}</em> - placeholder for a language abbreviation String</li>
 * </ul>
 */
public enum URLPathTokenType {
    /** Type representing a <b>{@code {id}}</b> wildcard token */
    ID("id", Long.class),

    /** Type representing a <b>{@code {language}}</b> wildcard token */
    LANGUAGE("language", String.class),

    /** Type representing a <b>{@code {season-type}}</b> wildcard token */
    SEASON_TYPE("season-type", SeriesSeasonType.class),

    /** Type representing a literal token which is not a wildcard */
    LITERAL(null, null);

    /** Pattern used to identify wildcard String tokens */
    private static final Pattern URL_WILDCARD = Pattern.compile("\\{([A-Za-z0-9\\-_]*)}");

    /** The wildcard identifier String */
    private final String wildcardID;

    /** The type of replacements parameters supported by this (wildcard) token */
    private final Class<?> parameterType;

    /**
     * Creates a new token type enumeration with the given wildcard ID and corresponding parameter type class
     *
     * @param wildcardID    The wildcard identifier String
     * @param parameterType The type of replacements parameters supported by this (wildcard) token
     */
    URLPathTokenType(String wildcardID, Class<?> parameterType) {
        this.wildcardID = wildcardID;
        this.parameterType = parameterType;
    }

    /**
     * Returns the corresponding Enum for a given String token. Tokens starting and ending with a curly bracket will be
     * considered to be a wildcard token, for example {@code {wildcardID}}. If no matching Enum exists for this wildcard
     * ID an exception will be thrown. All tokens that do <b>not</b> match the described pattern will be recognized as
     * non-wildcard tokens and will be resolved to {@link #LITERAL}, which means that they do not require (and support!)
     * any parameter replacements.
     *
     * @param token The non-empty path parameter token as String value
     *
     * @return Token type matching the given String token
     *
     * @throws IllegalArgumentException If the given token is recognized as wildcard but no Enum exists which matches
     *                                  the wildcard ID (name in between the curly brackets)
     */
    static URLPathTokenType forToken(@Nonnull String token) {
        Parameters.validateNotEmpty(token, "Token must be a non-empty String value");
        Matcher wildcardToken = URL_WILDCARD.matcher(token);

        if (!wildcardToken.matches()) {
            return LITERAL;
        }

        String id = wildcardToken.replaceAll("$1");
        return Arrays.stream(values()).filter(URLPathTokenType::isWildcardToken).filter(t -> t.wildcardID.equals(id))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(
                        String.format("Unsupported URL path wildcard: %s", token)));
    }

    /**
     * Checks whether the given path parameter object is compatible with this token type. This is only supported for
     * wildcard tokens. It's within the responsibility of the calling instance to <b>not</b> invoke this method on
     * {@link #LITERAL} types as it will raise an {@link UnsupportedOperationException}.
     *
     * @param parameter The actual path parameter that should be used to replace a wildcard token
     *
     * @throws UnsupportedOperationException If this method is invoked on a non-wildcard token type
     * @throws IllegalArgumentException      If the given parameter value is {@code null} or is not compatible with the
     *                                       tokens actual parameter type
     */
    void checkParameterCompatibility(@Nonnull Object parameter) {
        Parameters.validateCondition(URLPathTokenType::isWildcardToken, this, new UnsupportedOperationException(
                String.format("Tokens of type %s don't support wildcard replacements", this)));
        Parameters.validateCondition(
                param -> nonNull(param) && parameterType.isAssignableFrom(param.getClass()),
                parameter, new IllegalArgumentException(String.format(
                        "Wildcard replacements for {%s} must be of type <%s>", wildcardID, parameterType.getName())));
    }

    /**
     * Returns whether this type represents a wildcard token or not
     *
     * @return TRUE if this token type is a wildcard token or FALSE if it's a literal
     */
    boolean isWildcardToken() {
        return this != LITERAL;
    }
}

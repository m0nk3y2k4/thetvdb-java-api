/*
 * Copyright (C) 2019 - 2020 thetvdb-java-api Authors and Contributors
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

import java.util.Optional;

import javax.annotation.Nonnull;

/**
 * Objects of this class represent a single remote API URL path token.
 * <p><br>
 * The path component (the part between the URLs authority and query component) of some <i>TheTVDB.com</i> remote API
 * endpoint may be composed of several individual path tokens, separated by a {@code "/"}. These tokens may either
 * represent some existing resource on the remote service or may just be wildcards which have to be replaced by some
 * actual parameter values.
 * <p><br>
 * For example, the following path component consists of four tokens, two of them being wildcard tokens: {@code
 * /movies/{id}/translations/{language}}.<br> The tokens <i>"movies"</i> and <i>"translations"</i> are called {@code
 * literal} tokens which are just fine as they are. The two tokens <i>"{id}"</i> and <i>"{language}"</i> are only
 * placeholders which have to be replaced by some other, contextual values before connecting to this URL.
 * <p><br>
 * Instances of this class help processing these different kind of tokens by offering the ability to maintain all token
 * related information in one single object. This is especially useful for wildcard tokens with regards to binding a
 * wildcard and it's specific replacement altogether in one object. Using this class also provides a certain degree of
 * type-safety as it will detect unknown wildcards, verify the compatibility of replacement parameters and prohibits
 * further usage of wildcard tokens which have not yet been properly replaced by some actual path parameter value. It
 * also takes care of literal tokens not being accidentally replaced by some other value.
 */
public final class URLPathToken {

    /** The type of token represented by this object (e.g. literal, wildcard,...) */
    private final URLPathTokenType type;

    /** The initial String token (ATTENTION: this may also be a wildcard token String!) */
    private final String token;

    /** The actual path parameter (only applicable for wildcard tokens) */
    private String parameter;

    /**
     * Creates a new instance of this class based on the given String token
     *
     * @param token Non-empty String token to be wrapped by this instance
     */
    public URLPathToken(@Nonnull String token) {
        this.type = URLPathTokenType.forToken(token);
        this.token = token;
    }

    /**
     * Returns the actual token as String value. Please note that for wildcard tokens an actual URL path parameter has
     * to be set before invoking this method. Wildcard replacement parameters can be applied via the {@link
     * #replaceWildcard(Object) replaceWildcard(pathParameter)} method.
     *
     * @return The URL path token as String
     *
     * @throws IllegalStateException If this method is invoked on a wildcard token on which no actual path parameter has
     *                               been set yet
     */
    public String getToken() {
        return isWildcardToken() ? Optional.ofNullable(parameter).orElseThrow(() ->
                new IllegalStateException("No actual parameter value set for wildcard token")) : token;
    }

    /**
     * Sets the actual path parameter for this token. This operation is only supported for wildcard tokens.
     *
     * @param pathParameter The actual path parameter replacing the preliminary wildcard
     *
     * @throws UnsupportedOperationException If this method is invoked on a non-wildcard token
     * @throws IllegalArgumentException      If the given parameter value is {@code null} or is not compatible with the
     *                                       tokens actual parameter type
     */
    public void replaceWildcard(@Nonnull Object pathParameter) {
        type.checkParameterCompatibility(pathParameter);
        parameter = String.valueOf(pathParameter);
    }

    /**
     * Returns whether this token represents some wildcard or not
     *
     * @return TRUE if this is a wildcard token or FALSE if it's a literal token
     */
    public boolean isWildcardToken() {
        return type.isWildcardToken();
    }
}

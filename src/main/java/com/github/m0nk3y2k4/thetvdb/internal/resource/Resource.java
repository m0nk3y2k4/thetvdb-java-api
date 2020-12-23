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

package com.github.m0nk3y2k4.thetvdb.internal.resource;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.internal.util.http.URLPathToken;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * General implementation for remote API resources.
 * <p><br>
 * Provides general functionality for the creation of URL path parameter based resource strings.
 */
public abstract class Resource {

    /** Validator for the common dynamic <em>{@code id}</em> URL path parameter */
    @SuppressWarnings("java:S4276")    // Validators must be of type Predicate<T> to comply with the general validation
    protected static final Predicate<Long> ID_VALIDATOR = value -> value > 0;

    /** Identifiers for common dynamic URL path parameters */
    protected static final String PATH_ID = "id";

    /** Pattern used to validate the conformity of path Strings */
    private static final Pattern API_PATH = Pattern.compile("/([A-Za-z0-9{}\\-_]+/)*([A-Za-z0-9{}\\-_]+)");

    protected Resource() {}

    /**
     * Creates a new resource String based on the given parameters. The <em>{@code path}</em> parameter may contain
     * certain wildcards. <b>If</b> the given path contains wildcards then a set of corresponding replacement parameters
     * has to be provided. For a list of supported wildcards see {@link com.github.m0nk3y2k4.thetvdb.internal.util.http.URLPathTokenType
     * URLPathTokenType}.
     *
     * @param path               URL path String with or without wildcards
     * @param pathWildcardParams Additional path parameters used used to replace wildcards within the path String (will
     *                           be replaced in the order of their appearance)
     *
     * @return Composed resource String based on the given parameters
     */
    protected static String createResource(@Nonnull String path, Object... pathWildcardParams) {
        Parameters.validateNotEmpty(path, "Path must be a non-empty String value");
        Parameters.validateCondition(p -> API_PATH.matcher(p).matches(), path,
                new IllegalArgumentException(String.format("Invalid API path: %s", path)));
        Parameters.validateCondition(params -> Arrays.stream(params).allMatch(Objects::nonNull), pathWildcardParams,
                new IllegalArgumentException("Wildcard parameters must not be null"));

        List<URLPathToken> tokens = Collections.list(new StringTokenizer(path, "/")).stream()
                .map(token -> (String)token).map(URLPathToken::new).collect(Collectors.toList());

        List<URLPathToken> wildcardTokens = tokens.stream().filter(URLPathToken::isWildcardToken)
                .collect(Collectors.toList());
        if (wildcardTokens.size() != pathWildcardParams.length) {
            throw new IllegalArgumentException(
                    String.format("Parameter count mismatch: %d wildcard parameters were provided but found %d wildcard tokens in path [%s]",
                            pathWildcardParams.length, wildcardTokens.size(), path));
        }
        Iterator<Object> replacements = List.of(pathWildcardParams).iterator();
        wildcardTokens.forEach(token -> token.replaceWildcard(replacements.next()));

        return "/" + tokens.stream().map(URLPathToken::getToken).collect(Collectors.joining("/"));
    }
}

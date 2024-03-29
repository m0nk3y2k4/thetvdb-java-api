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

package com.github.m0nk3y2k4.thetvdb.internal.resource;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIRuntimeException;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;

/**
 * Specialized implementation for resources containing additional URL query parameters.
 * <p><br>
 * Provides general functionality for the creation of query resource strings. Only non-empty parameters will be
 * considered. Parameter values will be encoded automatically.
 */
public abstract class QueryResource extends Resource {

    protected QueryResource() {}

    /**
     * Creates a new query resource String based on the given parameters. It consists of the provided
     * <em>{@code path}</em> parameter prepended by the given query parameters in the following format:
     * <b>{@code /path?query1=value1&query2=value2&...}</b>
     * <p><br>
     * The <em>{@code path}</em> parameter may contain certain wildcards. <b>If</b> the given path contains wildcards
     * then a set of corresponding replacement parameters has to be provided. For a list of supported wildcards see
     * {@link com.github.m0nk3y2k4.thetvdb.internal.util.http.URLPathTokenType URLPathTokenType}.
     *
     * @param path               URL path String with or without wildcards
     * @param queryParams        Set of query parameters to be added to the very end of the resource String
     * @param pathWildcardParams Additional path parameters used to replace wildcards within the path String (will be
     *                           replaced in the order of their appearance)
     *
     * @return Composed query resource String based on the given parameters
     */
    protected static String createQueryResource(@Nonnull String path, @CheckForNull QueryParameters queryParams,
            Object... pathWildcardParams) {
        return createResource(path, pathWildcardParams) + createQuery(queryParams);
    }

    /**
     * Creates a URL query snippet based on the given parameters. Only non-empty key/value pairs will be considered and
     * the values will be encoded automatically. The returned snippet will start with a <i>'?'</i> character followed by
     * the actual parameter mappings. If the given object contains no valid query parameters an empty String will be
     * returned.
     *
     * @param queryParams Set of query parameters based on which the URL snippet should be created
     *
     * @return URL query snippet or empty String if the given object didn't contain any valid parameters
     */
    private static String createQuery(@CheckForNull QueryParameters queryParams) {
        List<QueryParameters.Parameter> validParams = Optional.ofNullable(queryParams)
                .orElse(TheTVDBApiFactory.createQueryParameters())
                .stream().filter(QueryResource::isValidQueryParameter).collect(Collectors.toList());

        if (!validParams.isEmpty()) {
            // .../resource?param1=value1&param2=value2&...
            return validParams.stream().map(e -> e.getKey() + "=" + encode(e.getValue()))
                    .collect(Collectors.joining("&", "?", ""));
        }

        return "";
    }

    /**
     * Checks if both, the key and the value of the given query parameter are non-empty Strings
     *
     * @param param The query parameter to check
     *
     * @return True if both, the key and the value are not empty. False if at least one of them is empty.
     */
    private static boolean isValidQueryParameter(@Nonnull QueryParameters.Parameter param) {
        return APIUtil.hasValue(param.getKey(), param.getValue());
    }

    /**
     * URL encodes the given <em>{@code value}</em> if necessary so that it may be used inside a resource String
     *
     * @param value The value to be encoded
     *
     * @return The encoded value
     */
    private static String encode(@Nonnull String value) {
        try {
            return URLEncoder.encode(value, Charset.defaultCharset().name());
        } catch (UnsupportedEncodingException ex) {
            throw new APIRuntimeException("Encoding [" + Charset.defaultCharset().name() + "] is not supported", ex);
        }
    }
}

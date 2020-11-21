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

import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

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

    protected Resource() {}

    /**
     * Creates a new resource string consisting of the given <em>{@code base}</em> URL path parameter prepended by some
     * optional route specific path parameters in the following format: <b><code>/BASE/param1/param2/...</code></b>
     *
     * @param base       Base URL path parameter which identifies a particular endpoint
     * @param pathParams Optional additional path parameters to be prepended to the end of the resource string
     *
     * @return Composed resource String based on the given parameters
     */
    protected static String createResource(@Nonnull String base, Object... pathParams) {
        return createResource(base, null, pathParams);
    }

    /**
     * Creates a new resource string consisting of the given <em>{@code base}</em> and <em>{@code specific}</em> URL
     * path parameters prepended by some optional additional route path parameters in the following format:
     * <b><code>/BASE/specific/param1/param2/...</code></b>
     *
     * @param base       Base URL path parameter which identifies a particular endpoint
     * @param specific   Specific URL path parameter representing the actual route to be invoked
     * @param pathParams Optional additional path parameters to be prepended to the end of the resource string
     *
     * @return Composed resource String based on the given parameters
     */
    protected static String createResource(@Nonnull String base, @CheckForNull String specific, Object... pathParams) {
        StringBuilder resourceBuilder = new StringBuilder(base);

        // Append specific URL path parameter if present
        Optional.ofNullable(specific).ifPresent(resourceBuilder::append);

        // Append additional URL path parameters
        for (Object param : pathParams) {
            resourceBuilder.append("/").append(param);
        }

        return resourceBuilder.toString();
    }
}

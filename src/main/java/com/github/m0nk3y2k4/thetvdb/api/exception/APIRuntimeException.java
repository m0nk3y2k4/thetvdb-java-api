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

package com.github.m0nk3y2k4.thetvdb.api.exception;

import javax.annotation.Nonnull;

/**
 * Unchecked runtime exception for API related issues. Exceptions of this type will mostly be used by the internal
 * implementation to indicate failed validations, invalid settings or unsupported character encoding.
 */
public class APIRuntimeException extends RuntimeException {

    /** Indicates a failed precondition check */
    public static final String API_PRECONDITION_ERROR = "Precondition check failed. Object is not in a proper state to process the requested task: %s";

    /**
     * Creates a new unchecked API runtime exception with the given error message
     *
     * @param message Brief error message describing the general problem
     */
    public APIRuntimeException(String message) {
        super(message);
    }

    /**
     * Creates a new unchecked API runtime exception with an extended error message
     *
     * @param message Brief error message describing the general problem. This message may contain a single <i>%s</i>
     *                conversion which will be automatically replaced with the given <em>{@code details}</em> message
     *                text.
     * @param details A more detailed error description
     */
    public APIRuntimeException(@Nonnull String message, @Nonnull String details) {
        super(String.format(message, details));
    }

    /**
     * Creates a nested unchecked API runtime exception wrapping some other root exception
     *
     * @param message Brief error message describing the general problem
     * @param ex      The original root exception
     */
    public APIRuntimeException(String message, Exception ex) {
        super(message, ex);
    }
}

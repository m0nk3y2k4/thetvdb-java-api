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

package com.github.m0nk3y2k4.thetvdb.internal.exception;

import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;

/**
 * Exception indicating problems while communicating with the remote API. This includes general IO issues as well as
 * checked communication level errors returned by the API as declared in the interface description.
 * <p><br>
 * The only checked response error that is <b>not</b> covered by this exception is HTTP-401 which is handled by a more
 * distinctive exception type: {@link APINotAuthorizedException}
 */
public class APICommunicationException extends APIException {

    /**
     * Creates a new API communication exception with the given error message
     *
     * @param message Brief error message describing the problem
     */
    public APICommunicationException(String message) {
        super(message);
    }

    /**
     * Creates a new API communication exception with an extended error message
     *
     * @param message Brief error message describing the problem. This message may contain a single <i>%s</i> conversion
     *                which will be automatically replaced with the given <em>{@code details}</em> message text.
     * @param details A more detailed error description
     */
    public APICommunicationException(@Nonnull String message, @Nonnull String details) {
        super(message, details);
    }

    /**
     * Creates a nested API communication exception wrapping some other root exception
     *
     * @param message Brief error message describing the problem
     * @param cause   The original root exception
     */
    public APICommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}

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

/**
 * Specific type of {@link APICommunicationException} which handles HTTP-401 responses received from the remote API.
 * Such response may be returned by the remote service in case of requesting a resource using an uninitialized session,
 * which lacks proper authentication.
 */
public final class APINotAuthorizedException extends APICommunicationException {

    /**
     * Creates a new API missing authorization exception with the given error message.
     *
     * @param error Brief error message describing the authorization issue. Will be appended to some basic exception
     *              specific error text.
     */
    public APINotAuthorizedException(@Nonnull String error) {
        super(API_NOT_AUTHORIZED_ERROR, error);
    }
}

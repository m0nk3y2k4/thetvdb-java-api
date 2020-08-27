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

import com.github.m0nk3y2k4.thetvdb.api.exception.APIRuntimeException;

/**
 * Exceptions of this type represent a failed precondition check. Such checks are carried out throughout processing to check
 * whether all the necessary prerequisites for proper processing of the task at hand have been met. Such exceptions typically
 * indicate an implementation error rather than incorrect use of the API.
 */
public class APIPreconditionException extends APIRuntimeException {

    /**
     * Creates a new API precondition exception with the given error message.
     *
     * @param error Brief error message describing the unmet precondition. Will be appended to some basic exception specific error text.
     */
    public APIPreconditionException(@Nonnull String error) {
        super(API_PRECONDITION_ERROR, error);
    }
}

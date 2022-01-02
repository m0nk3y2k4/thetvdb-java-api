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

package com.github.m0nk3y2k4.thetvdb.internal.exception;

import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_NOT_AUTHORIZED_ERROR;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class APINotAuthorizedExceptionTest {

    @Test
    void newAPINotAuthorizedException_withSimpleMessage_verifyProperties() {
        final String message = "Some authorization error";
        APINotAuthorizedException exception = new APINotAuthorizedException(message);
        assertThat(exception).hasMessage(API_NOT_AUTHORIZED_ERROR, message);
    }

    @Test
    void verifyIsCheckedException() {
        APINotAuthorizedException exception = new APINotAuthorizedException("Message");
        assertThat(exception).isInstanceOf(Exception.class);
    }
}

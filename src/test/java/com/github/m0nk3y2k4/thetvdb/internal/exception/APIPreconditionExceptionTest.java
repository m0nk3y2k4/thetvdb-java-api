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

import static com.github.m0nk3y2k4.thetvdb.api.exception.APIRuntimeException.API_PRECONDITION_ERROR;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class APIPreconditionExceptionTest {

    @Test
    void newAPIPreconditionException_withSimpleMessage_verifyProperties() {
        final String message = "XYZ is not set";
        APIPreconditionException exception = new APIPreconditionException(message);
        assertThat(exception).hasMessageContaining(API_PRECONDITION_ERROR, message);
    }

    @Test
    void verifyIsRuntimeException() {
        APIPreconditionException exception = new APIPreconditionException("Message");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
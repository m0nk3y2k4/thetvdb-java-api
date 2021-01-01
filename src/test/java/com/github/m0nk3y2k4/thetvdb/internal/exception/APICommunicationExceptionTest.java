/*
 * Copyright (C) 2019 - 2021 thetvdb-java-api Authors and Contributors
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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class APICommunicationExceptionTest {

    @Test
    void newAPICommunicationException_withSimpleMessage_verifyProperties() {
        final String message = "Some simple exception message";
        APICommunicationException exception = new APICommunicationException(message);
        assertThat(exception).hasMessage(message);
    }

    @Test
    void newAPICommunicationException_withDetailedMessage_verifyProperties() {
        final String message = "Detailed exception message: %s";
        final String details = "Some details";
        APICommunicationException exception = new APICommunicationException(message, details);
        assertThat(exception).hasMessage(message, details);
    }

    @Test
    void newAPICommunicationException_withWrappedException_verifyProperties() {
        final String message = "Some wrapped exception";
        final Exception cause = new Exception();
        APICommunicationException exception = new APICommunicationException(message, cause);
        assertThat(exception).hasMessage(message).hasCause(cause);
    }

    @Test
    void verifyIsCheckedException() {
        APICommunicationException exception = new APICommunicationException("Message");
        assertThat(exception).isInstanceOf(Exception.class);
    }
}

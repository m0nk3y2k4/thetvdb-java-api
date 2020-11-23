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

package com.github.m0nk3y2k4.thetvdb.testutils.assertj;

import java.util.Optional;
import java.util.function.Function;

import javax.annotation.CheckForNull;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;
import org.assertj.core.api.AbstractAssert;

/**
 * Special abstract assertion for integration tests, wrapping some API route invocation
 * <p><br>
 * Invokes a <i>TheTVDB.com</i> remote API route and verifies that the invocation was successful. Prints the description
 * text of this assertion to the standard console-out stream. For non-void routes the object returned by the remote API
 * will be converted to a proper String representation and will be printed to the standard console-out too.
 * <pre>{@code
 *     IntegrationTestAssertions.assertThat(api::login).as("/login").doesNotThrowAnyException();
 *     // [STDOUT] /login............................................OK
 *
 *     IntegrationTestAssertions.assertThat(api::getAvailableSeriesSearchParameters).as("/search/series/params").doesNotThrowAnyException();
 *     // [STDOUT] /search/series/params.............................OK        (name, imdbId, zap2itId, slug)
 * }</pre>
 *
 * @param <T> type of the wrapped route
 */
public abstract class AbstractIntegrationTestAssert<T> extends AbstractAssert<AbstractIntegrationTestAssert<T>, T> {

    /** Cut response String representation to not spam the maven output */
    private static final int MAX_OUTPUT_CHARACTERS = 180;

    AbstractIntegrationTestAssert(T actual) {
        super(actual, AbstractIntegrationTestAssert.class);
    }

    /**
     * Invoke the actual <i>TheTVDB.com</i> API route and verify that it did not throw any exception. Print description
     * text and API response (if available) to the standard console-out.
     */
    public void doesNotThrowAnyException() {
        final Function<Object, String> toString = obj -> APIUtil.toString(() -> obj);
        final Function<String, String> trimOutput = s -> s.length() > MAX_OUTPUT_CHARACTERS ?
                s.substring(0, MAX_OUTPUT_CHARACTERS).concat("...") : s;

        try {
            isNotNull();
            printOutput(execute().map(toString).map(trimOutput).orElse(null));
        } catch (APIException ex) {
            failWithMessage("Expected remote API invocation to not throw any exceptions but was [%s]", ex.getMessage());
        }
    }

    /**
     * Prints the description text and the given response String to the standard console-out stream, applying some
     * formatting first.
     *
     * @param result The response value from the remote API in its String representation
     */
    private void printOutput(@CheckForNull String result) {
        String testOutput = String.format("%-50s%s", descriptionText(), "OK").replace(" ", ".");
        if (result != null) {
            testOutput = String.format("%-60s(%s)", testOutput, result);
        }
        System.out.println(testOutput);
    }

    /**
     * Invokes the actual <i>TheTVDB.com</i> API route and returns its response value (for non-void routes).
     *
     * @return Optional containing the response value from the remote API - if available
     *
     * @throws APIException If the invocation of the API route failed
     */
    abstract Optional<?> execute() throws APIException;
}

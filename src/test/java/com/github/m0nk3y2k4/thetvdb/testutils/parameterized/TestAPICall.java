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

package com.github.m0nk3y2k4.thetvdb.testutils.parameterized;

/**
 * Small helper class used to wrap calls to some remote or API routes for easier parameterized JUnit testing
 * <p><br>
 * Accepted routes depend on the actual implementation that is used. Please check out the documentation of the implementing classes
 * for further details and examples. An additional textual description can be added which will be displayed as the default String
 * representation of this object. Provides a single method to invoke the underlying remote API route and to return it's response.
 */
public abstract class TestAPICall<T> {

    /** The actual API route represented by this object */
    protected final T route;

    /** Textual description of this remote API call */
    private final String description;

    /**
     * Creates a new API call referencing a given route with a given description
     *
     * @param route The route represented by this API call
     * @param description Textual description of this API call
     */
    protected TestAPICall(T route, String description) {
        this.route = route;
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}

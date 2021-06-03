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

package com.github.m0nk3y2k4.thetvdb.api.constants;

/**
 * Collection of path parameter constants used throughout the various <i>TheTVDB.com</i> routes.
 * <ul>
 *     <li>{@link Series}</li>
 * </ul>
 * The parameter values provided by this class are to be used when invoking certain API routes containing path
 * parameters with a fix set of valid parameter values.
 */
public final class Path {

    /** Constant class. Should not be instantiated */
    private Path() {}

    /**
     * Collection of path parameters for API endpoint <a target="_blank" href="https://app.swaggerhub.com/apis-docs/thetvdb/tvdb-api_v_4/4.3.2#/series">/series</a>
     */
    public static final class Series {

        /** Path parameter "<i>season-type</i>" - Used for fetching series episodes of a specified season type. */
        public enum SeasonType {
            /** Returns the episodes in the series default season type */
            DEFAULT("default"),
            OFFICIAL("official"),
            DVD("dvd"),
            ABSOLUTE("absolute"),
            ALTERNATE("alternate"),
            REGIONAL("regional");

            /** The path parameter value */
            private final String value;

            /**
             * Creates a new <i>season-type</i> path parameter constant
             *
             * @param value The path parameter value
             */
            SeasonType(String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return value;
            }
        }

        private Series() {}     // Private constructor. Only constants in this class
    }
}

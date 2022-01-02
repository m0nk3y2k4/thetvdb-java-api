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

package com.github.m0nk3y2k4.thetvdb.api.enumeration;

/**
 * Constants for the {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Search#TYPE} query parameter of the APIs
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Search/getSearchResults">
 * <b>[GET]</b> /search</a> route.
 * <p><br>
 * Used to restrict results to a specific entity type.
 *
 * @see com.github.m0nk3y2k4.thetvdb.api.constants.Query.Search#TYPE
 * @see com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi#search(String, SearchType) TheTVDBApi.search(searchTerm,
 *         entityType)
 */
public enum SearchType {
    /** Restrict search results to movies only */
    MOVIE("movie"),
    /** Restrict search results to series only */
    SERIES("series"),
    /** Restrict search results to series episodes only */
    EPISODE("episode"),
    /** Restrict search results to persons only */
    PERSON("person"),
    /** Restrict search results to companies only */
    COMPANY("company");

    /** The query parameter value */
    private final String value;

    /**
     * Creates a new <i>search type</i> query parameter constant
     *
     * @param value The query parameter value
     */
    SearchType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

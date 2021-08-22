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

package com.github.m0nk3y2k4.thetvdb.api.enumeration;

/**
 * Constants for the "<i>season-type</i>" path parameter of the APIs
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/series/getSeriesEpisodes">
 * <b>[GET]</b> /series/{id}/episodes/{season-type}</a> route.
 * <p><br>
 * Used for fetching series episodes of a specified season type.
 *
 * @see com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi#getSeriesEpisodes(long, SeriesSeasonType,
 *         com.github.m0nk3y2k4.thetvdb.api.QueryParameters) TheTVDBApi.getSeriesEpisodes(seriesId, seasonType,
 *         queryParameters)
 */
public enum SeriesSeasonType {
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
    SeriesSeasonType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

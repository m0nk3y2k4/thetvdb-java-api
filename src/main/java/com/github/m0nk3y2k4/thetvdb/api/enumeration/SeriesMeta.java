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
 * Constants for the {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series#META} query parameter of the APIs
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Series/getSeriesExtended">
 * <b>[GET]</b> /series/{id}/extended</a> route.
 * <p><br>
 * Used to request additional data which is usually not included in the default JSON response but may be of use under
 * certain circumstances. For example, it may be used to instruct the remote service to instantly enhance its response
 * with extended translation data so that no separate API calls are required in order to retrieve this kind of
 * information.
 *
 * @see com.github.m0nk3y2k4.thetvdb.api.constants.Query.Series#META
 * @see com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi#getSeriesDetails(long, SeriesMeta)
 *         TheTVDBApi.getSeriesDetails(seriesId, meta)
 */
public enum SeriesMeta {
    /** Request detailed episode information to be included in the JSON response */
    EPISODES("episodes"),
    /** Request additional translation information to be included in the JSON response */
    TRANSLATIONS("translations");

    /** The query parameter value */
    private final String value;

    /**
     * Creates a new <i>SeriesMeta</i> query parameter constant
     *
     * @param value The query parameter value
     */
    SeriesMeta(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

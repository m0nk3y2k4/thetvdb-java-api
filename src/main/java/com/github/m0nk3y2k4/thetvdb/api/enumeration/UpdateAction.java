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
 * Constants for the {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates#ACTION} query parameter of the
 * APIs
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Updates/updates">
 * <b>[GET]</b> /updates</a> route.
 * <p><br>
 * Used to restrict results to a specific change action like creation, update or deletion.
 * <p>
 *
 * @see com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates#ACTION
 * @see com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi#getUpdates(long, UpdateEntityType, UpdateAction, long)
 *         TheTVDBApi.getUpdates(since, type, action, page)
 */
public enum UpdateAction {
    /** Only list newly created entities */
    CREATE("create"), // ToDo: Has been removed with last API update but still working. Check again after next update.
    /** Only list deleted entities */
    DELETE("delete"),
    /** List updated/changed entities */
    UPDATE("update");

    /** The query parameter value */
    private final String action;

    /**
     * Creates a new <i>update action</i> query parameter constant
     *
     * @param action The query parameter value
     */
    UpdateAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return action;
    }
}

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

package com.github.m0nk3y2k4.thetvdb.api.model.data;

import javax.annotation.Nullable;

/**
 * Super interface for objects containing any kind of translation information.
 * <p><br>
 * Only provides access to the most basic translation information. The main purpose of this interface is to allow the
 * implementation of common logic that is able to process different kinds of translation DTO's.
 */
@FunctionalInterface
public interface Translated {

    /**
     * Returns the 2- or 3-character language code
     *
     * @return The language code from the received JSON
     */
    @Nullable
    String getLanguage();
}

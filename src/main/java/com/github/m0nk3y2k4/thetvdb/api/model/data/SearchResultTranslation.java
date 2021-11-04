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

package com.github.m0nk3y2k4.thetvdb.api.model.data;

import javax.annotation.Nullable;

/**
 * Interface representing the various translation information in the context of a <em>{@code SearchResult}</em> data
 * transfer object.
 * <p><br>
 * The methods of this class provide easy access to all translation data which was returned by the remote service in
 * JSON format.
 * <p><br>
 * For <em>{@code SearchResult}</em> objects, the various translations are returned in quite different fashions, for
 * example as one big translation String or as List of translations or even as dedicated JSON translation objects. All
 * of them will be mapped into this unified form in order to facilitate the work with these translations.
 */
public interface SearchResultTranslation extends Translated {

    /**
     * Returns the actual translation
     *
     * @return The actual translation from the received JSON
     */
    @Nullable
    String getTranslation();
}

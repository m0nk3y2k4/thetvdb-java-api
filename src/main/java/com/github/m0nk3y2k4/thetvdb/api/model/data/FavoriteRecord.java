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

import com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory;

/**
 * Interface representing a <em>{@code FavoriteRecord}</em> data transfer object.
 * <p><br>
 * Objects of this class can be used to create/add new favorites for the authenticated user. A new instance can be
 * {@link FavoriteRecordBuilder build} using the {@link TheTVDBApiFactory#createFavoriteRecordBuilder()} util method.
 */
public interface FavoriteRecord {

    /**
     * Builder class used to create new instances of the {@link FavoriteRecord} interface.
     * <p><br>
     * All properties are optional which means they can be omitted. However, please make sure to set <u>at least one</u>
     * property before invoking the {@link #build()} method to create a new instance.
     *
     * @see TheTVDBApiFactory#createFavoriteRecordBuilder()
     */
    interface FavoriteRecordBuilder {

        /**
         * Adds the given series to the users favorites
         *
         * @param seriesId The <i>TheTVDB.com</i> series ID
         *
         * @return This builder for use in a chained invocation
         */
        FavoriteRecordBuilder series(int seriesId);

        /**
         * Adds the given movie to the users favorites
         *
         * @param movieId The <i>TheTVDB.com</i> movie ID
         *
         * @return This builder for use in a chained invocation
         */
        FavoriteRecordBuilder movies(int movieId);

        /**
         * Adds the given episode to the users favorites
         *
         * @param episodeId The <i>TheTVDB.com</i> episode ID
         *
         * @return This builder for use in a chained invocation
         */
        FavoriteRecordBuilder episodes(int episodeId);

        /**
         * Adds the given artwork to the users favorites
         *
         * @param artworkId The <i>TheTVDB.com</i> artwork ID
         *
         * @return This builder for use in a chained invocation
         */
        FavoriteRecordBuilder artwork(int artworkId);

        /**
         * Adds the given people to the users favorites
         *
         * @param peopleId The <i>TheTVDB.com</i> people ID
         *
         * @return This builder for use in a chained invocation
         */
        FavoriteRecordBuilder people(int peopleId);

        /**
         * Adds the given list to the users favorites
         *
         * @param listId The <i>TheTVDB.com</i> list ID
         *
         * @return This builder for use in a chained invocation
         */
        FavoriteRecordBuilder list(int listId);

        /**
         * Builds a new {@link FavoriteRecord}
         *
         * @return An immutable instance of FavoriteRecord
         */
        FavoriteRecord build();
    }
}

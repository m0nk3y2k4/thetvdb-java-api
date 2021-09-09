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
 * Constants for the {@value com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates#TYPE} query parameter of the APIs
 * <a target="_blank" href="https://thetvdb.github.io/v4-api/#/Updates/updates">
 * <b>[GET]</b> /updates</a> route.
 * <p><br>
 * Used to restrict results to a specific entity type.
 * <p>
 *
 * @see com.github.m0nk3y2k4.thetvdb.api.constants.Query.Updates#TYPE
 * @see com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi#getUpdates(long, UpdateEntityType, UpdateAction, long)
 *         TheTVDBApi.getUpdates(since, type, action, page)
 */
public enum UpdateEntityType {
    /** Restrict update records to artwork entities */
    ARTWORK("artwork"),
    /** Restrict update records to artwork type entities */
    ARTWORK_TYPES("artworktypes"),
    /** Restrict update records to award entities */
    AWARDS("awards"),
    /** Restrict update records to award category entities */
    AWARD_CATEGORIES("award_categories"),
    /** Restrict update records to award nominee entities */
    AWARD_NOMINEES("award_nominees"),
    /** Restrict update records to company entities */
    COMPANIES("companies"),
    /** Restrict update records to company type entities */
    COMPANY_TYPES("company_types"),
    /** Restrict update records to content rating entities */
    CONTENT_RATINGS("content_ratings"),
    /** Restrict update records to country entities */
    COUNTRIES("countries"),
    /** Restrict update records to entity type entities */
    ENTITY_TYPES("entity_types"),
    /** Restrict update records to episode entities */
    EPISODES("episodes"),
    /** Restrict update records to genre entities */
    GENRES("genres"),
    /** Restrict update records to language entities */
    LANGUAGES("languages"),
    /** Restrict update records to list entities */
    LISTS("lists"),
    /** Restrict update records to movie entities */
    MOVIES("movies"),
    /** Restrict update records to movie genre entities */
    MOVIE_GENRES("movie_genres"),
    /** Restrict update records to movie status entities */
    MOVIE_STATUS("movie_status"),
    /** Restrict update records to people entities */
    PEOPLE("people"),
    /** Restrict update records to people type entities */
    PEOPLE_TYPES("peopletypes"),
    /** Restrict update records to season entities */
    SEASONS("seasons"),
    /** Restrict update records to season type entities */
    SEASON_TYPES("seasontypes"),
    /** Restrict update records to series entities */
    SERIES("series"),
    /** Restrict update records to series people entities */
    SERIES_PEOPLE("seriespeople"),
    /** Restrict update records to source type entities */
    SOURCE_TYPES("sourcetypes"),
    /** Restrict update records to tag entities */
    TAGS("tags"),
    /** Restrict update records to tag option entities */
    TAG_OPTIONS("tag_options"),
    /** Restrict update records to character translation entities */
    TRANSLATED_CHARACTERS("translatedcharacters"),
    /** Restrict update records to company translation entities */
    TRANSLATED_COMPANIES("translatedcompanies"),
    /** Restrict update records to episode translation entities */
    TRANSLATED_EPISODES("translatedepisodes"),
    /** Restrict update records to list translation entities */
    TRANSLATED_LISTS("translatedlists"),
    /** Restrict update records to movie translation entities */
    TRANSLATED_MOVIES("translatedmovies"),
    /** Restrict update records to people translation entities */
    TRANSLATED_PEOPLE("translatedpeople"),
    /** Restrict update records to season translation entities */
    TRANSLATED_SEASONS("translatedseasons"),
    /** Restrict update records to series translation entities */
    TRANSLATED_SERIES("translatedseries");

    /** The query parameter value */
    private final String entityType;

    /**
     * Creates a new <i>update type</i> query parameter constant
     *
     * @param entityType The query parameter value
     */
    UpdateEntityType(String entityType) {
        this.entityType = entityType;
    }

    @Override
    public String toString() {
        return entityType;
    }
}

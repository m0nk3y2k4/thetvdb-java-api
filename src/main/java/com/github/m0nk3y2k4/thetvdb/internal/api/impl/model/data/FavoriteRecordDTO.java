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

package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import java.util.stream.IntStream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.m0nk3y2k4.thetvdb.api.model.data.FavoriteRecord;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APIPreconditionException;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Preconditions;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;

/**
 * DTO implementation of the {@link FavoriteRecord} interface
 * <p><br>
 * Objects of this class reflect the data to be sent to the remote service and are immutable so that their content can
 * not be changed once an instance has been created. New objects of this class may be created by using the corresponding
 * {@link FavoriteRecordDTO.Builder}.
 */
@Immutable
@WithHiddenImplementation
public abstract class FavoriteRecordDTO implements FavoriteRecord {

    /**
     * Get the ID of the series to be added to the users favorites
     *
     * @return The series ID or 0 if not set
     */
    @JsonInclude(Include.NON_DEFAULT)
    public abstract int getSeries();

    /**
     * Get the ID of the movie to be added to the users favorites
     *
     * @return The movie ID or 0 if not set
     */
    @JsonInclude(Include.NON_DEFAULT)
    public abstract int getMovies();

    /**
     * Get the ID of the episode to be added to the users favorites
     *
     * @return The episode ID or 0 if not set
     */
    @JsonInclude(Include.NON_DEFAULT)
    public abstract int getEpisodes();

    /**
     * Get the ID of the artwork to be added to the users favorites
     *
     * @return The artwork ID or 0 if not set
     */
    @JsonInclude(Include.NON_DEFAULT)
    public abstract int getArtwork();

    /**
     * Get the ID of the people to be added to the users favorites
     *
     * @return The people ID or 0 if not set
     */
    @JsonInclude(Include.NON_DEFAULT)
    public abstract int getPeople();

    /**
     * Get the ID of the list to be added to the users favorites
     *
     * @return The list ID or 0 if not set
     */
    @JsonInclude(Include.NON_DEFAULT)
    public abstract int getList();

    /**
     * Checks whether all necessary parameters have been set. If not an exception will be thrown. This method will
     * automatically be invoked while creating a new immutable instance via the {@link Builder#build()} method.
     *
     * @throws APIPreconditionException If no favorite ID has been set using the builder (at least one ID is required)
     */
    @Check
    protected void validate() {
        Preconditions.requires(fav -> IntStream.of(
                                fav.getSeries(),
                                fav.getMovies(),
                                fav.getEpisodes(),
                                fav.getArtwork(),
                                fav.getPeople(),
                                fav.getList())
                        .anyMatch(id -> id > 0),
                this, new APIPreconditionException("No favorites set! Please provide at least one ID."));
    }

    /**
     * Builder used to create a new immutable {@link FavoriteRecordDTO} implementation
     * <p><br>
     * This builder provides a fluent API for setting certain object properties and creating a new immutable
     * {@link FavoriteRecordDTO} instance based on these properties. New builders may be initialized with some existing
     * DTO instance, which presets the builders properties to the values of the given DTO, still retaining the option to
     * make additional changes before actually building a new immutable object.
     */
    public static class Builder extends FavoriteRecordDTOBuilder implements FavoriteRecord.FavoriteRecordBuilder {}
}

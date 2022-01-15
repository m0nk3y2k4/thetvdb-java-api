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

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

/**
 * Interface representing a <em>{@code PeopleExtendedRecord}</em> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all people related data which was returned by the remote service in
 * JSON format. Methods returning collection-based values will return an empty collection in case no corresponding data
 * was received.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface PeopleDetails extends People {

    /**
     * Get the value of the {<em>{@code data.awards}</em>} JSON property
     *
     * @return The <em>{@code awards}</em> property from the received JSON
     */
    List<Award> getAwards();

    /**
     * Get the value of the {<em>{@code data.biographies}</em>} JSON property
     *
     * @return The <em>{@code biographies}</em> property from the received JSON
     */
    List<Biography> getBiographies();

    /**
     * Get the value of the {<em>{@code data.birth}</em>} JSON property
     *
     * @return The <em>{@code birth}</em> property from the received JSON
     */
    @Nullable
    String getBirth();

    /**
     * Get the value of the {<em>{@code data.birthPlace}</em>} JSON property
     *
     * @return The <em>{@code birthPlace}</em> property from the received JSON
     */
    @Nullable
    String getBirthPlace();

    /**
     * Get the value of the {<em>{@code data.characters}</em>} JSON property
     *
     * @return The <em>{@code characters}</em> property from the received JSON
     */
    List<Character> getCharacters();

    /**
     * Get the value of the {<em>{@code data.death}</em>} JSON property
     *
     * @return The <em>{@code death}</em> property from the received JSON
     */
    @Nullable
    String getDeath();

    /**
     * Get the value of the {<em>{@code data.gender}</em>} JSON property
     *
     * @return The <em>{@code gender}</em> property from the received JSON
     */
    @Nullable
    Long getGender();

    /**
     * Get the value of the {<em>{@code data.slug}</em>} JSON property
     *
     * @return The <em>{@code slug}</em> property from the received JSON
     */
    @Nullable
    String getSlug();

    /**
     * Get the value of the {<em>{@code data.races}</em>} JSON property
     *
     * @return The <em>{@code races}</em> property from the received JSON
     */
    List<Race> getRaces();

    /**
     * Get the value of the {<em>{@code data.remoteIds}</em>} JSON property
     *
     * @return The <em>{@code remoteIds}</em> property from the received JSON
     */
    List<RemoteId> getRemoteIds();

    /**
     * Get the value of the {<em>{@code data.tagOptions}</em>} JSON property
     *
     * @return The <em>{@code tagOptions}</em> property from the received JSON
     */
    List<TagOption> getTagOptions();

    /**
     * Get the value of the {<em>{@code data.translations}</em>} JSON property
     * <p><br>
     * <b>Note:</b> Field will only be present if these data is explicitly requested. See {@link
     * com.github.m0nk3y2k4.thetvdb.api.enumeration.PeopleMeta#TRANSLATIONS PeopleMeta.TRANSLATIONS}.
     *
     * @return The <em>{@code translations}</em> property from the received JSON
     */
    // ToDo: This field seems to be always included in the JSON response even without meta=translations, but with all properties being null. Check occasionally.
    // ToDo: Field is currently not declared in PeopleExtendedRecord but returned in JSON. Check again after the next API update.
    Optional<MetaTranslations> getTranslations();
}

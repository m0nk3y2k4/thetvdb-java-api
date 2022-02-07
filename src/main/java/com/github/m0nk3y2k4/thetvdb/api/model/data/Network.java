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

import javax.annotation.Nullable;

/**
 * Interface representing a <em>{@code NetworkBaseRecord}</em> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to basic network related data which was returned by the remote service
 * in JSON format.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface Network {

    /**
     * Get the value of the {<em>{@code <enclosing>.id}</em>} JSON property
     *
     * @return The <em>{@code id}</em> property from the received JSON
     */
    @Nullable
    Long getId();

    /**
     * Get the value of the {<em>{@code <enclosing>.name}</em>} JSON property
     *
     * @return The <em>{@code name}</em> property from the received JSON
     */
    @Nullable
    String getName();

    /**
     * Get the value of the {<em>{@code <enclosing>.slug}</em>} JSON property
     *
     * @return The <em>{@code slug}</em> property from the received JSON
     */
    @Nullable
    String getSlug();

    /**
     * Get the value of the {<em>{@code <enclosing>.abbreviation}</em>} JSON property
     *
     * @return The <em>{@code abbreviation}</em> property from the received JSON
     */
    @Nullable
    String getAbbreviation();

    /**
     * Get the value of the {<em>{@code <enclosing>.country}</em>} JSON property
     *
     * @return The <em>{@code country}</em> property from the received JSON
     */
    @Nullable
    String getCountry();


    /**
     * Get the value of the {<em>{@code <enclosing>.nameTranslations}</em>} JSON property
     *
     * @return The <em>{@code nameTranslations}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in NetworkBaseRecord but returned in JSON. Check again after the next API update.
    List<String> getNameTranslations();

    /**
     * Get the value of the {<em>{@code <enclosing>.overviewTranslations}</em>} JSON property
     *
     * @return The <em>{@code overviewTranslations}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in NetworkBaseRecord but returned in JSON. Check again after the next API update.
    List<String> getOverviewTranslations();

    /**
     * Get the value of the {<em>{@code <enclosing>.aliases}</em>} JSON property
     *
     * @return The <em>{@code aliases}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in NetworkBaseRecord but returned in JSON. Check again after the next API update.
    List<Alias> getAliases();

    /**
     * Get the value of the {<em>{@code <enclosing>.primaryCompanyType}</em>} JSON property
     *
     * @return The <em>{@code primaryCompanyType}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in NetworkBaseRecord but returned in JSON. Check again after the next API update.
    @Nullable
    Long getPrimaryCompanyType();

    /**
     * Get the value of the {<em>{@code <enclosing>.activeDate}</em>} JSON property
     *
     * @return The <em>{@code activeDate}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in NetworkBaseRecord but returned in JSON. Check again after the next API update.
    @Nullable
    String getActiveDate();

    /**
     * Get the value of the {<em>{@code <enclosing>.inactiveDate}</em>} JSON property
     *
     * @return The <em>{@code inactiveDate}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in NetworkBaseRecord but returned in JSON. Check again after the next API update.
    @Nullable
    String getInactiveDate();

    /**
     * Get the value of the {<em>{@code <enclosing>.companyType}</em>} JSON property
     *
     * @return The <em>{@code companyType}</em> property from the received JSON
     */
    // ToDo: Field is currently not declared in NetworkBaseRecord but returned in JSON. Check again after the next API update.
    @Nullable
    CompanyType getCompanyType();
}
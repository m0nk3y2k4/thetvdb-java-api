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

package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Company;
import com.github.m0nk3y2k4.thetvdb.api.model.data.CompanyType;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.APIDataModel;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import org.immutables.value.Value.Auxiliary;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

/**
 * DTO implementation of the {@link Company} interface
 * <p><br>
 * Objects of this class reflect the data received by the remote service and are immutable so that their content can not
 * be changed once an instance has been created. New objects of this class may be created by using the corresponding
 * {@link Builder}.
 */
@Immutable
@APIDataModel
@WithHiddenImplementation
@JsonDeserialize(builder = CompanyDTO.Builder.class)
public abstract class CompanyDTO implements Company {

    @Nullable
    @Override
    @Derived
    public CompanyType getCompanyType() {
        CompanyTypeDTOBuilder companyType = new CompanyTypeDTO.Builder();
        Optional.ofNullable(getCompanyTypesJson().get("companyTypeId")).map(Object::toString).map(Long::valueOf)
                .ifPresent(companyType::id);
        Optional.ofNullable(getCompanyTypesJson().get("companyTypeName")).map(Object::toString)
                .ifPresent(companyType::name);
        return companyType.build();
    }

    /**
     * Helper method used for mapping JSON data into a {@link CompanyType} model
     * <p><br>
     * For some unknown reason the names for the company type properties inside the Company JSON differ from the
     * property names used in the company types overview. In order to reuse the existing type model some extended JSON
     * mapping is required, based on the data returned by this method.
     *
     * @return List of key/value pairs representing the JSONs 'companyType' node properties
     */
    @Auxiliary
    @JsonProperty("companyType")
    abstract Map<String, Object> getCompanyTypesJson();

    /**
     * Builder used to create a new immutable {@link CompanyDTO} implementation
     * <p><br>
     * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link
     * CompanyDTO} instance based on these properties. New builders may be initialized with some existing DTO instance,
     * which presets the builders properties to the values of the given DTO, still retaining the option to make
     * additional changes before actually building a new immutable object.
     */
    public static class Builder extends CompanyDTOBuilder {}
}

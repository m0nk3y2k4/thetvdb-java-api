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

package com.github.m0nk3y2k4.thetvdb.internal.api.impl;

import com.github.m0nk3y2k4.thetvdb.api.APIKey;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;

/**
 * Implementation of the {@link APIKey} interface
 * <p><br>
 * Objects of this class represent a specific API key and are immutable so that their content can not be changed once an
 * instance has been created. New objects of this class may be created by using the corresponding
 * {@link APIKeyImpl.Builder}.
 */
@Immutable
@WithHiddenImplementation
public abstract class APIKeyImpl implements APIKey {

    /**
     * Checks whether all required properties are set for this API key
     * <p><br>
     * This method prevents the client from creating invalid API key objects that are practically unusable for
     * <i>TheTVDB.com</i> API authentication due to invalid or missing data.
     */
    @Check
    protected void validate() {
        Parameters.validateApiKey(this);
    }

    /**
     * Builder used to create a new immutable {@link APIKeyImpl} implementation
     * <p><br>
     * This builder provides a fluent API for setting certain object properties and creating a new immutable
     * {@link APIKeyImpl} instance based on these properties. New builders may be initialized with some existing APIKey
     * instance, which presets the builders properties to the values of the given key, still retaining the option to
     * make additional changes before actually building a new immutable object.
     */
    public static class Builder extends APIKeyImplBuilder {}
}

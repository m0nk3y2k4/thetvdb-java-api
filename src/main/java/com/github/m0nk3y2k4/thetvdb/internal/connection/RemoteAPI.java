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

package com.github.m0nk3y2k4.thetvdb.internal.connection;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.api.Proxy;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import org.immutables.value.Value.Immutable;

/**
 * Object representing the technical remote endpoint of the RESTful remote API. It defines the remote service from which
 * the individual resources will be requested. Without further initialization, objects of this class will represent the
 * <i>TheTVDB.com</i> remote API service by default. However, new instances can also be configured to point to an
 * endpoint other than the actual <i>TheTVDB.com</i> API, e.g. if you want to forward the API communication via a proxy
 * service.
 * <p><br>
 * Objects of this class represent a specific remote and are immutable so that their content can not be changed once an
 * instance has been created. New objects of this class may be created by using the corresponding
 * {@link RemoteAPI.Builder}.
 */
@Immutable
@WithHiddenImplementation
public abstract class RemoteAPI implements Proxy {

    /** The default communication protocol */
    static final String HTTPS = "https";

    /** The <i>TheTVDB.com</i> API base host */
    static final String THE_TVDB_DOT_COM = "api4.thetvdb.com";

    /** Path component of the <i>TheTVDB.com</i> v4 API */
    static final String API_V4 = "/v4";

    /** The default HTTPS port number */
    static final int HTTPS_DEFAULT = 443;

    /**
     * Creates a new remote pointing to the actual <i>TheTVDB.com</i> API.
     *
     * @return New RemoteAPI instance pointing to the actual <i>TheTVDB.com</i> API
     */
    public static RemoteAPI getDefault() {
        return new Builder().protocol(HTTPS).host(THE_TVDB_DOT_COM).port(HTTPS_DEFAULT).path(API_V4).build();
    }

    /**
     * Creates a new Uniform Resource Identifier for the given <em>{@code resource}</em> of the remote API represented
     * by this object.
     *
     * @param resource The remote resource to create a new URI for
     *
     * @return The URI for the given resource of this remote API
     *
     * @throws MalformedURLException If an unknown protocol is used or the port is a negative number other than -1
     */
    public URL forResource(@Nonnull String resource) throws MalformedURLException {
        return new URL(getProtocol(), getHost(), getPort(), getPath().orElse("") + resource);
    }

    /**
     * Builder used to create a new immutable {@link RemoteAPI} implementation
     * <p><br>
     * This builder provides a fluent API for setting certain object properties and creating a new immutable
     * {@link RemoteAPI} instance based on these properties. New builders may be initialized with some existing API
     * instance or even a {@link Proxy}, which presets the builders properties to the values of the given object, still
     * retaining the option to make additional changes before actually building a new immutable instance.
     */
    public static class Builder extends RemoteAPIBuilder {}
}

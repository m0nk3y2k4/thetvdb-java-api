/*
 * Copyright (C) 2019 - 2020 thetvdb-java-api Authors and Contributors
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

package com.github.m0nk3y2k4.thetvdb.api;

import com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory;

/**
 * Interface representing a proxy service to be used for communication with the remote API. Such proxies may be provided when creating a new
 * API instance in order to forward the all the communication towards this proxy rather than directly communicating with the the actual
 * <i>TheTVDB.com</i> remote API. The latter one will be the default behavior if no specific proxy is set during the API instantiation.
 * Instances of this interface might be created via the {@link TheTVDBApiFactory}.
 */
public interface Proxy {

    /**
     * Returns the name of the communication protocol used for this proxy.
     *
     * @return The protocol name used by this proxy
     */
    String getProtocol();

    /**
     * Returns the host name of this proxy.
     *
     * @return The host name of this proxy
     */
    String getHost();

    /**
     * Returns the port number used for communication with this proxy.
     *
     * @return The port number of this proxy
     */
    int getPort();
}

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

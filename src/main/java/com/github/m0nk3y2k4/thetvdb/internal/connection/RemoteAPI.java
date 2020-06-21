package com.github.m0nk3y2k4.thetvdb.internal.connection;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.api.Proxy;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.WithHiddenImplementation;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

/**
 * Object representing the technical remote endpoint of the RESTful remote API. It defines the remote service from which
 * the individual resources will be requested. Without further initialization, objects of this class will represent the
 * <i>TheTVDB.com</i> remote API service by default. However, new instances can also be configured to point to an endpoint
 * other than the actual <i>TheTVDB.com</i> API, e.g. if you want to forward the API communication via a proxy service.
 * <p><br>
 * Objects of this class represent a specific remote and are immutable so that their content can not be changed once an instance
 * has been created. New objects of this class may be created by using the corresponding {@link RemoteAPI.Builder}.
 */
@Immutable
@WithHiddenImplementation
public abstract class RemoteAPI implements Proxy {

    /** The default communication protocol */
    private static final String HTTPS = "https";

    /** The <i>TheTVDB.com</i> API base host */
    private static final String THE_TVDB_DOT_COM = "api.thetvdb.com";

    /** The default HTTPS port number */
    private static final int HTTPS_DEFAULT = 443;

    /**
     * Returns the name of the communication protocol used for this remote. The default value is <em>{@value #HTTPS}</em>.
     *
     * @return The protocol name used by this remote
     */
    @Override @Default
    public String getProtocol() {
        return HTTPS;
    }

    /**
     * Returns the name of the remote host. The default value is <em>{@value #THE_TVDB_DOT_COM}</em>.
     *
     * @return The host name of the remote service
     */
    @Override @Default
    public String getHost() {
        return THE_TVDB_DOT_COM;
    }

    /**
     * Returns the port number used for communication with the remote. The default value is <em>{@value #HTTPS_DEFAULT}</em>.
     *
     * @return The port number of this remote
     */
    @Override @Default
    public int getPort() {
        return HTTPS_DEFAULT;
    }

    /**
     * Creates a new Uniform Resource Identifier for the given <em>{@code resource}</em> of the remote API represented by this object.
     *
     * @param resource The remote resource to create a new URI for
     *
     * @return The URI for the given resource of this remote API
     *
     * @throws MalformedURLException If an unknown protocol is used or the port is a negative number other than -1
     */
    public URL forResource(@Nonnull String resource) throws MalformedURLException {
        return new URL(getProtocol(), getHost(), getPort(), resource);
    }

    /**
     * Builder used to create a new immutable {@link RemoteAPI} implementation
     * <p><br>
     * This builder provides a fluent API for setting certain object properties and creating a new immutable {@link RemoteAPI} instance
     * based on these properties. New builders may be initialized with some existing API instance or even a {@link Proxy}, which presets
     * the builders properties to the values of the given object, still retaining the option to make additional changes before actually
     * building a new immutable instance.
     */
    public static class Builder extends RemoteAPIBuilder {}
}

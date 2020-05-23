package com.github.m0nk3y2k4.thetvdb.internal.util.http;

/**
 * Simple enumeration for HTTP request methods to be used when calling the remote API REST service
 */
public enum HttpRequestMethod {

    /** HTTP request methods supported by this API */
    GET, POST, HEAD, PUT, DELETE;

    /**
     * The String representation of this HTTP request method
     *
     * @return HTTP request method as String
     */
    public String getName() {
        return this.toString();
    }
}

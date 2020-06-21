package com.github.m0nk3y2k4.thetvdb.internal.util.http;

/**
 * Constants enumerating common HTTP headers
 */
public final class HttpHeaders {

    /** <i>Accept</i> header according to <a href="https://tools.ietf.org/html/rfc1945#appendix-D.2.1">RFC1945</a> */
    public static final String ACCEPT = "Accept";

    /** <i>Accept-Language</i> header according to <a href="https://tools.ietf.org/html/rfc1945#appendix-D.2.4">RFC1945</a> */
    public static final String ACCEPT_LANGUAGE = "Accept-Language";

    /** <i>Authorization</i> header according to <a href="https://tools.ietf.org/html/rfc1945#section-10.2">RFC1945</a> */
    public static final String AUTHORIZATION = "Authorization";

    /** <i>Content-Length</i> header according to <a href="https://tools.ietf.org/html/rfc1945#section-10.4">RFC1945</a> */
    public static final String CONTENT_LENGTH = "Content-Length";

    /** <i>Content-Type</i> header according to <a href="https://tools.ietf.org/html/rfc1945#section-10.5">RFC1945</a> */
    public static final String CONTENT_TYPE = "Content-Type";

    /** <i>User-Agent</i> header according to <a href="https://tools.ietf.org/html/rfc1945#section-10.15">RFC1945</a> */
    public static final String USER_AGENT = "User-Agent";

    private HttpHeaders() {}        // Hidden constructor. Only constants
}

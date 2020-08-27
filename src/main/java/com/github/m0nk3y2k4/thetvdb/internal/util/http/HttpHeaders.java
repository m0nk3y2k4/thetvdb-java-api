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

    /** <i>Allow</i> header according to <a href="https://tools.ietf.org/html/rfc1945#section-10.1">RFC1945</a> */
    public static final String ALLOW = "Allow";

    private HttpHeaders() {}        // Hidden constructor. Only constants
}

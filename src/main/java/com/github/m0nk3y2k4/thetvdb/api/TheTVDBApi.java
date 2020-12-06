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

import java.util.Optional;

import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;

// ToDo: Revise JDoc once APIv4 implementation is finished

/**
 * Main interface of the <i>TheTVDB</i> API connector.
 * <p><br>
 * This interface provides access to all available routes of the remote <i>TheTVDB.com</i> REST API. Routes which accept
 * additional optional and mandatory query parameters can either be invoked with a given set of {@link QueryParameters}
 * or via some predefined shortcut-methods. These shortcut-methods will accept certain values as direct method
 * parameters which will then be forwarded to the REST API as regular URL query parameters. Please note that
 * shortcut-methods exist for most of the common query scenarios but maybe not for all. In case of more complex query
 * setups the user has to take care of creating a properly configured <em>{@code QueryParameters}</em> object, which is
 * slightly more effort than using the shortcut-methods but gives the user unlimited configuration options.
 * <p><br>
 * In order to create a new API instance the {@link com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory TheTVDBApiFactory}
 * should be used. This factory also provides additional helper methods, for example to easily create new <em>{@code
 * QueryParameters}</em>.
 * <p><br>
 * To cover a wide range of possible applications, this API connector provides multiple layouts in order to allow an
 * easy integration regardless of your actual project requirements. It gives you the option to use prefabbed DTO's which
 * will be parsed from the actual JSON returned by the remote service. In case you need advanced exception handling or
 * you prefer to parse the JSON into your own data models (or don't want to parse it at all), other API layouts will
 * provide you with extended API response DTO's or even with the raw JSON. The following API layouts are currently
 * available:
 * <ul>
 * <li>{@link TheTVDBApi}<br>
 * This is probably the most common layout. It provides various shortcut-methods and automatically maps the received
 * JSON <b><i>data</i></b> content into simple Java DTO's (at least for more complex response data). The user does not
 * have to worry about JSON parsing but can simply work with the returned DTO's like he works with every other Java
 * object. However, these objects do only contain the actually requested data and will not include any additional
 * contextual information that may be returned by the remote service (e.g. Pagination information, additional
 * validation or error data). Furthermore they will only provide access to properties that are
 * <a href="https://api.thetvdb.com/swagger">formally declared by the API</a> (version {@value Version#API_VERSION})
 * .</li>
 * <li>{@link Extended}<br>
 * This layout may be used for slightly advance API integration. Like the common layout it'll take care of parsing
 * the received JSON into Java DTO's but it will also provide access to any additional contextual information.
 * Methods of this layout will always return a single {@link APIResponse} object which consists of the actual data,
 * parsed as DTO, as well as all additional information which is available in the given context, like additional
 * error or pagination information. This layout does not provide any shortcut-methods.</li>
 * <li>{@link JSON}<br>
 * This layout may be used if you do not want any post-processing being applied to the actual remote service response
 * data. All methods within this layout will return the raw, unmodified JSON data as it was received from the API.
 * This might be useful if you prefer to map the JSON data yourself, want to use your own Java data models or if you
 * don't want to parse the JSON data at all (but forward it to some other service for example). It would also be the
 * preferred layout in case you need access to additional (e.g. experimental) properties that are not yet officially
 * declared by the formal API description. This layout does not provide any shortcut-methods though.</li>
 * </ul>
 * <p><br>
 * Once an API instance has been created, the additional layouts can be accessed via the {@link #extended()} or
 * {@link #json()} method.
 */
public interface TheTVDBApi {

    /**
     * Initializes the current API session by requesting a new token from the remote API. This token will be used for
     * authentication of all requests that are sent to the remote service by this API instance. The initialization will
     * be performed based on the constructor parameters used to create this API instance. Actually this method will do
     * the same as {@link #login()}.
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    void init() throws APIException;

    /**
     * Initializes the current API with the given token. This token will be used for authentication of all requests that
     * are sent to the remote service by this API instance. The given string must be a valid Base64 encoded token in the
     * regular JWT format <i>"{header}.{payload}.{signature}"</i>.
     * <p><br>
     * If the given token is (or becomes) expired it will be replaced by a new JWT automatically. The new token will be
     * requested from the remove service based on the constructor parameters used to create this API instance.
     *
     * @param token JSON Web Token to be used for remote API communication/authorization
     *
     * @throws APIException If the given string does not match the JSON Web Token format
     */
    void init(@Nonnull String token) throws APIException;

    /**
     * Returns the JSON Web Token used for authentication of all requests that are sent to the remote service by this
     * API instance. If the current API has not yet been initialized an empty <i>Optional</i> instance will be
     * returned.
     *
     * @return The JWT used by this API or an empty <i>Optional</i> if the API has not been initialized
     */
    Optional<String> getToken();

    /**
     * Sets the preferred language to be used for communication with the remote service. Some of the API calls might use
     * this setting in order to only return results that match the given language. If available, the data returned by
     * the remote API will be translated to the given language. The default language code is <b>"en"</b>.
     *
     * @param languageCode The language in which the results are to be returned
     */
    void setLanguage(String languageCode);

    /**
     * Initializes the current API session by requesting a new token from the remote API. This token will be used for
     * authentication of all requests that are sent to the remote service by this API instance. The initialization will
     * be performed based on the constructor parameters used to create this API instance. It is recommended to
     * login/initialize the session before making the first API call. However, if an API call is made without proper
     * initialization, an implicit login will be performed.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Authentication/post_login">
     * <b>[POST]</b> /login</a>
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    void login() throws APIException;

    /**
     * Refreshes the current, valid JWT session token. This method can be used to extend the expiration date (24 hours)
     * of the current session token without the need of a complete new login.
     * <p><br>
     * <i>Corresponds to remote API route:</i> <a href="https://api.thetvdb.com/swagger#!/Authentication/get_refresh_token">
     * <b>[GET]</b> /refresh_token</a>
     *
     * @throws APIException If an exception with the remote API occurs, e.g. authentication failure, IO error, resource
     *                      not found, etc.
     */
    void refreshToken() throws APIException;

    /**
     * Provides access to the API's {@link JSON JSON} layout.
     * <p><br>
     * In this layout, all methods will return the raw, unmodified JSON as received from the remove service.
     *
     * @return Instance representing the the API's <em>{@code JSON}</em> layout
     */
    JSON json();

    /**
     * Provides access to the API's {@link Extended Extended} layout.
     * <p><br>
     * In this layout, all methods will return a single {@link APIResponse} object, containing the actual request data,
     * mapped as DTO, as well as all additional information that is available in the corresponding context.
     *
     * @return Instance representing the the API's <em>{@code Extended}</em> layout
     */
    Extended extended();

    /**
     * Interface representing the API's <em>{@code JSON}</em> layout.
     * <p><br>
     * This layout may be used if you do not want any post-processing being applied to the actual remote service
     * response data. All methods within this layout will return the raw, unmodified JSON data as it was received from
     * the API. This might be useful if you prefer to map the JSON data yourself, want to use your own Java data models
     * or if you don't want to parse the JSON data at all (but forward it to some other service for example). This
     * layout does not provide any shortcut-methods though.
     *
     * @see #json()
     */
    interface JSON {
        // ToDo: Add some methods
    }

    /**
     * Interface representing the API's <em>{@code Extended}</em> layout.
     * <p><br>
     * This layout may be used for slightly advance API integration. Like the common layout it'll take care of parsing
     * the received JSON into Java DTO's but it will also provide access to any additional contextual information.
     * Methods of this layout will always return a single {@link APIResponse} object which consists of the actual data,
     * parsed as DTO, as well as all additional information which is available in the given context, like additional
     * error or pagination information. This layout does not provide any shortcut-methods.
     *
     * @see #extended()
     */
    interface Extended {
        // ToDo: Add some methods
    }

    /**
     * Specifies the version of the <i>TheTVDB.com</i> remote API to be used by this connector
     */
    final class Version {
        /** Version of the <i>TheTVDB.com</i> remote API used by this connector */
        public static final String API_VERSION = "v4.0.0";

        /** Constant class. Should not be instantiated */
        private Version() {}
    }
}

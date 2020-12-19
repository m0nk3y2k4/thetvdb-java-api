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

package com.github.m0nk3y2k4.thetvdb.testutils;

import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Optional;
import java.util.Properties;

/**
 * Configuration used for authentication at the <i>TheTVDB.com</i> RESTful remote API
 * <p><br>
 * This is wrapper class which provides easy access to the system properties holding the authentication information to
 * be used for remote API integration tests.
 */
public final class IntegrationTestConfiguration {

    /** System property holding the <i>TheTVDB.com</i> API-Key to be used for authentication */
    private static final String INTEGRATION_THETVDB_COM_APIKEY = "integration.thetvdb.com.v3.apikey";

    /** System property holding the <i>TheTVDB.com</i> user key to be used for authentication */
    private static final String INTEGRATION_THETVDB_COM_USERKEY = "integration.thetvdb.com.v3.userkey";

    /** System property holding the <i>TheTVDB.com</i> user name to be used for authentication */
    private static final String INTEGRATION_THETVDB_COM_USERNAME = "integration.thetvdb.com.v3.username";

    /** Singleton instance */
    private static final IntegrationTestConfiguration INSTANCE = new IntegrationTestConfiguration();

    /** Set of authentication properties */
    private Properties config;

    private IntegrationTestConfiguration() {}       // Private constructor due to Singleton-pattern

    /**
     * Tries to load the authentication properties from the "thetvdbapi.properties" resource file. If no such
     * configuration is available, the properties have to be provided directly via VM/maven arguments.
     *
     * @return Instance representing the authentication settings found in the properties resource file
     */
    public static synchronized IntegrationTestConfiguration loadConfiguration() {
        if (INSTANCE.config == null) {
            INSTANCE.config = new Properties();
            try (InputStream theTVDBApiProps = IntegrationTestConfiguration.class.getClassLoader()
                    .getResourceAsStream("thetvdbapi.properties")) {
                if (theTVDBApiProps != null) {
                    INSTANCE.config.load(theTVDBApiProps);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    /**
     * Fetches the API-Key from the resource properties file or from the VM system properties
     *
     * @return The API-Key to be used for <i>TheTVDB.com</i> remote API authentication
     *
     * @throws InvalidPropertiesFormatException If the API-Key was neither provided via the "thetvdbapi.properties" file
     *                                          nor as a system property
     */
    public String getApiKey() throws InvalidPropertiesFormatException {
        return getProperty(INTEGRATION_THETVDB_COM_APIKEY);
    }

    /**
     * Fetches the user key from the resource properties file or from the VM system properties
     *
     * @return The user key to be used for <i>TheTVDB.com</i> remote API authentication
     *
     * @throws InvalidPropertiesFormatException If the user key was neither provided via the "thetvdbapi.properties"
     *                                          file nor as a system property
     */
    public String getUserKey() throws InvalidPropertiesFormatException {
        return getProperty(INTEGRATION_THETVDB_COM_USERKEY);
    }

    /**
     * Fetches the user name from the resource properties file or from the VM system properties
     *
     * @return The user name to be used for <i>TheTVDB.com</i> remote API authentication
     *
     * @throws InvalidPropertiesFormatException If the user name was neither provided via the "thetvdbapi.properties"
     *                                          file nor as a system property
     */
    public String getUserName() throws InvalidPropertiesFormatException {
        return getProperty(INTEGRATION_THETVDB_COM_USERNAME);
    }

    /**
     * Fetches the given property from the "thetvdbapi.properties" resource file. If the given property is not present,
     * load it from the system properties.
     *
     * @param key The key of the property to be returned
     *
     * @return Property value for the given key
     *
     * @throws InvalidPropertiesFormatException If the requested property is neither set in the "thetvdbapi.properties"
     *                                          file nor available as a system property
     */
    private String getProperty(String key) throws InvalidPropertiesFormatException {
        return Optional.ofNullable(config.getProperty(key, System.getProperty(key))).orElseThrow(() ->
                new InvalidPropertiesFormatException(String
                        .format("Failed to lookup property <%s>. Please provide valid settings via the \"thetvdbapi.properties\" file!", key)));
    }
}

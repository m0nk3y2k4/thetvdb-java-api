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

package com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter;

import java.util.InvalidPropertiesFormatException;
import java.util.Locale;
import java.util.Optional;

import com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.FundingModel;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.APIKeyImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.TheTVDBApiImpl;
import com.github.m0nk3y2k4.thetvdb.testutils.IntegrationTestConfiguration;
import com.github.m0nk3y2k4.thetvdb.testutils.annotation.IntegrationTestSuite;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JUnit5 extension providing some useful functionality for running integration tests against the actual
 * <i>TheTVDB.com</i> RESTful API
 * <p><br>
 * Provides a resolver to let a preconfigured {@link TheTVDBApi} method parameter being injected in test classes. Also
 * prints the name of the test suite - if provided via the {@link IntegrationTestSuite} annotation - to the standard
 * output stream.
 *
 * @see IntegrationTestSuite
 */
public class IntegrationTestExtension implements ParameterResolver, BeforeAllCallback {

    /** Logger for verbose integration test information */
    private static final Logger LOG = LoggerFactory.getLogger(IntegrationTestExtension.class);

    /** API instance referring to the actual <i>TheTVDB.com</i> RESTful remote API */
    private static final TheTVDBApi API = createConfigurationBasedApi();

    /**
     * Creates a new API instance which uses the actual <i>TheTVDB.com</i> RESTful API as remote endpoint. The returned
     * API will be pre-configured with the values from the following system properties:
     * <ul>
     *     <li>integration.thetvdb.com.apikey - The <i>TheTVDB.com</i> v4 API-Key to be used for authentication</li>
     *     <li>integration.thetvdb.com.fundingmodel - The funding model based on which the API-Key was issued</li>
     * </ul>
     * All of these properties are mandatory and must be set. Otherwise the initialization of this extension will fail. The
     * properties may either be set directly as VM/maven arguments (e.g. -Dintegration.thetvdb.com.apikey=SOMEAPIKEY) or via
     * a properties file in the resource folder. For the latter one please see the "thetvdbapi.properties.sample" file inside
     * the resource folder.
     *
     * @return A new <i>TheTVDB.com</i> API instance preconfigured with the system property values for authentication
     */
    private static TheTVDBApi createConfigurationBasedApi() {
        IntegrationTestConfiguration testConfig = IntegrationTestConfiguration.loadConfiguration();
        try {
            Optional<String> pin = testConfig.getPin();
            FundingModel fundingModel = pin.isPresent() ? FundingModel.SUBSCRIPTION : FundingModel.CONTRACT;
            return new TheTVDBApiImpl(new APIKeyImpl.Builder().apiKey(testConfig.getApiKey()).pin(pin)
                    .fundingModel(fundingModel).build());
        } catch (InvalidPropertiesFormatException configurationException) {
            throw new ExtensionConfigurationException("Failed to initialize extension", configurationException);
        }
    }

    /**
     * Pretty-prints the <i>value</i> attribute of the given annotation to the maven console
     *
     * @param suite Meta-annotation of an integration test class
     */
    private static void printSuiteName(IntegrationTestSuite suite) {
        String name = suite.value().toUpperCase(Locale.ROOT);
        LOG.info("+-------------------------------------------+");
        LOG.info(String.format("|%-43s|", String.format("%" + (name.length() + (43 - name.length()) / 2) + "s", name)));
        LOG.info("+-------------------------------------------+");
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return TheTVDBApi.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return API;
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        extensionContext.getTestClass()
                .map(clazz -> clazz.getAnnotation(IntegrationTestSuite.class))
                .ifPresent(IntegrationTestExtension::printSuiteName);
    }
}

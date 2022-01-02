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

package com.github.m0nk3y2k4.thetvdb;

import static com.github.m0nk3y2k4.thetvdb.testutils.APITestUtil.CONTRACT_APIKEY;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import com.github.m0nk3y2k4.thetvdb.api.APIKey;
import com.github.m0nk3y2k4.thetvdb.api.Proxy;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.enumeration.FundingModel;
import com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI;
import org.junit.jupiter.api.Test;

class TheTVDBApiFactoryTest {

    @Test
    void createApi_withApiKey_verifyApiIsCreated() {
        assertThat(TheTVDBApiFactory.createApi(CONTRACT_APIKEY)).isNotNull();
    }

    @Test
    void createApi_withApiKeyAndProxy_verifyApiIsCreated() {
        assertThat(TheTVDBApiFactory.createApi(CONTRACT_APIKEY, RemoteAPI.getDefault())).isNotNull();
    }

    @Test
    void createQueryParameters_verifyThatNoActualQueryParametersArePresent() {
        QueryParameters parameters = TheTVDBApiFactory.createQueryParameters();
        assertThat(parameters).isEmpty();
    }

    @Test
    void createQueryParameters_withValidParameterMap_verifyQueryParameters() {
        final Map<String, String> parameterMap = Map.of("SomeKey", "X", "AnotherKey", "Y");
        QueryParameters parameters = TheTVDBApiFactory.createQueryParameters(parameterMap);
        assertThat(parameterMap).hasSize(parameters.size());
        parameterMap.forEach((key, value) -> assertThat(parameters.getParameterValue(key)).contains(value));
    }

    @Test
    void createProxy_withoutPath_verifyProxySettings() {
        final String protocol = "https";
        final String host = "sub1.proxy.com";
        final int port = 9584;
        Proxy proxy = TheTVDBApiFactory.createProxy(protocol, host, port);
        assertThat(proxy.getProtocol()).isEqualTo(protocol);
        assertThat(proxy.getHost()).isEqualTo(host);
        assertThat(proxy.getPath()).isEmpty();
        assertThat(proxy.getPort()).isEqualTo(port);
    }

    @Test
    void createProxy_withPath_verifyProxySettings() {
        final String protocol = "https";
        final String host = "sub2.proxy.com";
        final String path = "/path";
        final int port = 6487;
        Proxy proxy = TheTVDBApiFactory.createProxy(protocol, host, path, port);
        assertThat(proxy.getProtocol()).isEqualTo(protocol);
        assertThat(proxy.getHost()).isEqualTo(host);
        assertThat(proxy.getPath()).contains(path);
        assertThat(proxy.getPort()).isEqualTo(port);
    }

    @Test
    void createAPIKey_contractBased_verifyAPIKeyProperties() {
        final String key = "FH7IK2H4S3Z4J";
        APIKey apiKey = TheTVDBApiFactory.createAPIKey(key);
        assertThat(apiKey.getApiKey()).isEqualTo(key);
        assertThat(apiKey.getPin()).isEmpty();
        assertThat(apiKey.getFundingModel()).isEqualTo(FundingModel.CONTRACT);
    }

    @Test
    void createAPIKey_subscriptionBased_verifyAPIKeyProperties() {
        final String key = "UG9OD4S0U4E56";
        final String pin = "PIN";
        APIKey apiKey = TheTVDBApiFactory.createAPIKey(key, pin);
        assertThat(apiKey.getApiKey()).isEqualTo(key);
        assertThat(apiKey.getPin()).contains(pin);
        assertThat(apiKey.getFundingModel()).isEqualTo(FundingModel.SUBSCRIPTION);
    }
}

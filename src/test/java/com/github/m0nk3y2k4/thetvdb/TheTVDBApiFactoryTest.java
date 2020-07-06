package com.github.m0nk3y2k4.thetvdb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import com.github.m0nk3y2k4.thetvdb.api.Proxy;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI;
import org.junit.jupiter.api.Test;

class TheTVDBApiFactoryTest {

    @Test
    void createApi_withSimpleCredentials_verifyApiIsCreated() {
        assertThat(TheTVDBApiFactory.createApi("WD8G6LFD64J3DO")).isNotNull();
    }

    @Test
    void createApi_withSimpleCredentialsAndProxy_verifyApiIsCreated() {
        assertThat(TheTVDBApiFactory.createApi("PT83JW4T6I4WT", new RemoteAPI.Builder().build())).isNotNull();
    }

    @Test
    void createApi_withExtendedCredentials_verifyApiIsCreated() {
        assertThat(TheTVDBApiFactory.createApi("ORIFKSK75DSZ", "unique_8472577536", "Tony Stark")).isNotNull();
    }

    @Test
    void createApi_withExtendedCredentialsAndProxy_verifyApiIsCreated() {
        assertThat(TheTVDBApiFactory.createApi("OIFFAO26G21JU7F", "unique_6617881334", "Carol Danvers",
                new RemoteAPI.Builder().build())).isNotNull();
    }

    @Test
    void createQueryParameters() {
        QueryParameters parameters = TheTVDBApiFactory.createQueryParameters();
        assertThat(parameters).isNotNull().isEmpty();
    }

    @Test
    void createQueryParameters_withValidParameterMap_verifyQueryParameters() {
        final Map<String, String> parameterMap = Map.of("SomeKey", "X", "AnotherKey", "Y");
        QueryParameters parameters = TheTVDBApiFactory.createQueryParameters(parameterMap);
        assertThat(parameterMap).hasSize(parameters.size());
        parameterMap.forEach((key, value) -> assertThat(parameters.getParameterValue(key)).isPresent().contains(value));
    }

    @Test
    void createProxy_withValidSettings_verifyProxySettings() {
        final String protocol = "https";
        final String host = "sub.proxy.com";
        final int port = 9584;
        Proxy proxy = TheTVDBApiFactory.createProxy(protocol, host, port);
        assertThat(proxy.getProtocol()).isEqualTo(protocol);
        assertThat(proxy.getHost()).isEqualTo(host);
        assertThat(proxy.getPort()).isEqualTo(port);
    }
}
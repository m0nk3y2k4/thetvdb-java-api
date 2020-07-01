package com.github.m0nk3y2k4.thetvdb.internal.connection;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RemoteAPITest {

    private static Stream<Arguments> buildNewRemoteAPI_withSpecificOrDefaultSettings_verifyAPISettings() {
        return Stream.of(
                Arguments.of(new RemoteAPI.Builder().protocol("ftp").build(), "ftp", RemoteAPI.THE_TVDB_DOT_COM, RemoteAPI.HTTPS_DEFAULT),
                Arguments.of(new RemoteAPI.Builder().host("some.host.com").build(), RemoteAPI.HTTPS, "some.host.com", RemoteAPI.HTTPS_DEFAULT),
                Arguments.of(new RemoteAPI.Builder().port(8457).build(), RemoteAPI.HTTPS, RemoteAPI.THE_TVDB_DOT_COM, 8457),
                Arguments.of(new RemoteAPI.Builder().build(), RemoteAPI.HTTPS, RemoteAPI.THE_TVDB_DOT_COM, RemoteAPI.HTTPS_DEFAULT)
        );
    }

    @ParameterizedTest(name = "{index} Remote API \"{0}\" has settings [protocol={1}, host={2}, port={3}]")
    @MethodSource
    void buildNewRemoteAPI_withSpecificOrDefaultSettings_verifyAPISettings(RemoteAPI remote, String protocol, String host, int port) {
        assertThat(remote.getProtocol()).isEqualTo(protocol);
        assertThat(remote.getHost()).isEqualTo(host);
        assertThat(remote.getPort()).isEqualTo(port);
    }

    @Test
    void forResource() throws Exception {
        final URL remote = new URL("http", "localhost", 3654, "/someRemoteResource");
        URL url = new RemoteAPI.Builder().protocol(remote.getProtocol()).host(remote.getHost()).port(remote.getPort()).build().forResource(remote.getFile());
        assertThat(url).isEqualTo(remote);
    }
}
package com.github.m0nk3y2k4.thetvdb.internal.connection;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;

import org.junit.jupiter.api.Test;

class RemoteAPITest {

    @Test
    void buildNewRemoteAPI_withSpecificProtocol_verifyAPISettings() {
        final String protocol = "ftp";
        RemoteAPI remote = new RemoteAPI.Builder().protocol(protocol).build();
        assertThat(remote.getProtocol()).isEqualTo(protocol);
        assertThat(remote.getHost()).isEqualTo(RemoteAPI.THE_TVDB_DOT_COM);
        assertThat(remote.getPort()).isEqualTo(RemoteAPI.HTTPS_DEFAULT);
    }

    @Test
    void buildNewRemoteAPI_withSpecificHost_verifyAPISettings() {
        final String host = "some.host.com";
        RemoteAPI remote = new RemoteAPI.Builder().host(host).build();
        assertThat(remote.getProtocol()).isEqualTo(RemoteAPI.HTTPS);
        assertThat(remote.getHost()).isEqualTo(host);
        assertThat(remote.getPort()).isEqualTo(RemoteAPI.HTTPS_DEFAULT);
    }

    @Test
    void buildNewRemoteAPI_withSpecificPort_verifyAPISettings() {
        final int port = 8457;
        RemoteAPI remote = new RemoteAPI.Builder().port(port).build();
        assertThat(remote.getProtocol()).isEqualTo(RemoteAPI.HTTPS);
        assertThat(remote.getHost()).isEqualTo(RemoteAPI.THE_TVDB_DOT_COM);
        assertThat(remote.getPort()).isEqualTo(port);
    }

    @Test
    void buildNewRemoteAPI_withoutSettings_verifyAPISettings() {
        RemoteAPI remote = new RemoteAPI.Builder().build();
        assertThat(remote.getProtocol()).isEqualTo(RemoteAPI.HTTPS);
        assertThat(remote.getHost()).isEqualTo(RemoteAPI.THE_TVDB_DOT_COM);
        assertThat(remote.getPort()).isEqualTo(RemoteAPI.HTTPS_DEFAULT);
    }

    @Test
    void forResource() throws Exception {
        final URL remote = new URL("http", "localhost", 3654, "/someRemoteResource");
        URL url = new RemoteAPI.Builder().protocol(remote.getProtocol()).host(remote.getHost()).port(remote.getPort()).build().forResource(remote.getFile());
        assertThat(url).isEqualTo(remote);
    }
}
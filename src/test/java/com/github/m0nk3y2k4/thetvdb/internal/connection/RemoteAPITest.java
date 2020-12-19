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

package com.github.m0nk3y2k4.thetvdb.internal.connection;

import static com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI.API_V4;
import static com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI.HTTPS;
import static com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI.HTTPS_DEFAULT;
import static com.github.m0nk3y2k4.thetvdb.internal.connection.RemoteAPI.THE_TVDB_DOT_COM;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;

import org.junit.jupiter.api.Test;

class RemoteAPITest {

    @Test
    void getDefault_verifyAPISettings() {
        RemoteAPI remote = RemoteAPI.getDefault();
        assertThat(remote.getProtocol()).isEqualTo(HTTPS);
        assertThat(remote.getHost()).isEqualTo(THE_TVDB_DOT_COM);
        assertThat(remote.getPort()).isEqualTo(HTTPS_DEFAULT);
        assertThat(remote.getPath()).contains(API_V4);
    }

    @Test
    void forResource_withoutPath_checkUrl() throws Exception {
        final URL remote = new URL("http", "localhost", 3654, "/someRemoteResource");
        URL url = new RemoteAPI.Builder().protocol(remote.getProtocol()).host(remote.getHost()).port(remote.getPort())
                .build().forResource(remote.getFile());
        assertThat(url).isEqualTo(remote);
    }

    @Test
    void forResource_withPath_checkUrl() throws Exception {
        final String path = "/path";
        final String resource = "/someRemoteResource";
        final URL remote = new URL("http", "localhost", 3654, path + resource);
        URL url = new RemoteAPI.Builder().protocol(remote.getProtocol()).host(remote.getHost()).path(path)
                .port(remote.getPort()).build().forResource(resource);
        assertThat(url).isEqualTo(remote);
    }
}

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

import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.POST;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.JSON_DATA;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.JSON_SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockserver.model.HttpRequest.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APIPreconditionException;
import com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter.WithHttpsMockServer;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.verify.VerificationTimes;

@SuppressWarnings("ConstantConditions")
@WithHttpsMockServer
class PostRequestTest {

    @Test
    void send_verifyHttpMethodIsSet(MockServerClient client, RemoteAPI remoteAPI) throws Exception {
        final String resource = "/test/postRequestMethod";
        PostRequest request = new PostRequest(resource, JSON_DATA);
        request.setRemoteAPI(remoteAPI);
        request.send();
        client.verify(request(resource).withMethod(POST.getName()), VerificationTimes.once());
    }

    @Test
    void send_withoutData_verifyPreparationPreconditionCheck(RemoteAPI remoteAPI) {
        final String resource = "/test/postRequestNoData";
        PostRequest request = new PostRequest(resource, null);
        request.setRemoteAPI(remoteAPI);
        APIPreconditionException exception = catchThrowableOfType(request::send, APIPreconditionException.class);
        assertThat(exception).hasMessageContaining("data is not set");
    }

    @Test
    void send_withData_verifyOutputIsSet(MockServerClient client, RemoteAPI remoteAPI) throws Exception {
        final String resource = "/test/postRequestOutput";
        final String json = "{\"Value\":\"Some content of the POST-request\"}";
        PostRequest request = new PostRequest(resource, json);
        request.setRemoteAPI(remoteAPI);
        request.send();
        client.verify(request(resource).withBody(json), VerificationTimes.once());
    }

    @Test
    void send_withData_verifyJSONContentParsed(RemoteAPI remoteAPI) throws Exception {
        PostRequest request = new PostRequest("/test/postRequestContent", JSON_DATA);
        request.setRemoteAPI(remoteAPI);
        JsonNode response = request.send();
        assertThat(response).hasToString(JSON_SUCCESS);
    }
}

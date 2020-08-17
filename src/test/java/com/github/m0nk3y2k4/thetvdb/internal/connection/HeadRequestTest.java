package com.github.m0nk3y2k4.thetvdb.internal.connection;

import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.HEAD;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.OK_200;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter.WithHttpsMockServer;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.verify.VerificationTimes;

@WithHttpsMockServer
class HeadRequestTest {

    @Test
    void send_verifyHttpMethodIsSet(MockServerClient client, RemoteAPI remoteAPI) throws Exception {
        final String resource = "/test/headRequestMethod";
        HeadRequest request = new HeadRequest(resource);
        request.setRemoteAPI(remoteAPI);
        request.send();
        client.verify(request(resource).withMethod(HEAD.getName()), VerificationTimes.once());
    }

    @Test
    void send_verifyJSONContentParsed(MockServerClient client, RemoteAPI remoteAPI) throws Exception {
        final String resource = "/test/headRequestContent";
        HeadRequest request = new HeadRequest(resource);
        request.setRemoteAPI(remoteAPI);
        client.when(request(resource).withMethod(HEAD.getName())).respond(response().withStatusCode(OK_200.code())
                .withHeaders(header("Some-Header", "singleValue")));
        JsonNode dataNode = request.send().get("data");
        assertThat(dataNode).isNotNull();
        assertThat(dataNode.get("Some-Header").textValue()).isEqualTo("singleValue");
    }

    @Test
    void getData_withResponseHeaders_verifyAllTypesOfHeadersAreProperlyParsed() {
        HeadRequest request = new HeadRequest("/test/headRequestParseHeaders");
        HttpsURLConnection connection = mock(HttpsURLConnection.class);
        Map<String, List<String>> headers = new HashMap<>();
        headers.put(null, emptyList());
        headers.put("Empty-Header", emptyList());
        headers.put("Single-Header", singletonList("value"));
        headers.put("List-Header", List.of("X", "Y", "Z"));
        doReturn(headers).when(connection).getHeaderFields();
        JsonNode dataNode = request.getData(connection).get("data");
        assertThat(dataNode).isNotNull().hasSize(3);
        assertThat(dataNode.get("Empty-Header").isNull()).isTrue();
        assertThat(dataNode.get("Single-Header").textValue()).isEqualTo("value");
        assertThat(dataNode.get("List-Header")).extracting(JsonNode::textValue).containsExactlyInAnyOrderElementsOf(List.of("X", "Y", "Z"));
    }
}

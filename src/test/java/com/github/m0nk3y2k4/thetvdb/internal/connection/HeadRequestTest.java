package com.github.m0nk3y2k4.thetvdb.internal.connection;

import static com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod.HEAD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.OK_200;

import java.util.List;

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
        final String headerKey = "Some-Header";
        final String headerValue = "singleValue";
        final String listHeaderKey = "List-Header";
        final List<String> listHeaderValues = List.of("X", "Y", "Z");
        HeadRequest request = new HeadRequest(resource);
        request.setRemoteAPI(remoteAPI);
        client.when(request(resource).withMethod(HEAD.getName())).respond(response().withStatusCode(OK_200.code())
                .withHeaders(header(headerKey, headerValue), header(listHeaderKey, listHeaderValues)));
        JsonNode dataNode = request.send().get("data");
        assertThat(dataNode).isNotNull();
        assertThat(dataNode.get(headerKey).textValue()).isEqualTo(headerValue);
        assertThat(dataNode.get(listHeaderKey)).extracting(JsonNode::textValue).containsExactlyInAnyOrderElementsOf(listHeaderValues);
    }
}

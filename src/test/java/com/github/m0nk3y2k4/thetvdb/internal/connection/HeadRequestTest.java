package com.github.m0nk3y2k4.thetvdb.internal.connection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod;
import com.github.m0nk3y2k4.thetvdb.junit.jupiter.WithHttpsMockServer;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.verify.VerificationTimes;

@WithHttpsMockServer
class HeadRequestTest {

    @Test
    void send_verifyHttpMethodIsSet(MockServerClient client, RemoteAPI remoteAPI) throws Exception {
        final String resource = "/headRequestMethod";
        HeadRequest request = new HeadRequest(resource);
        request.setRemoteAPI(remoteAPI);
        request.send();
        client.verify(request(resource).withMethod(HttpRequestMethod.HEAD.getName()), VerificationTimes.once());
    }

    @Test
    void send_verifyJSONContentParsed(MockServerClient client, RemoteAPI remoteAPI) throws Exception {
        final String resource = "/headRequestContent";
        final String headerKey = "Some-Header";
        final String headerValue = "singleValue";
        final String listHeaderKey = "List-Header";
        final List<String> listHeaderValues = List.of("X", "Y", "Z");
        HeadRequest request = new HeadRequest(resource);
        request.setRemoteAPI(remoteAPI);
        client.when(request(resource).withMethod(HttpRequestMethod.HEAD.getName())).respond(response().withStatusCode(HttpStatusCode.OK_200.code())
                .withHeaders(header(headerKey, headerValue), header(listHeaderKey, listHeaderValues)));
        JsonNode response = request.send();
        assertThat(response.get(headerKey).textValue()).isEqualTo(headerValue);
        assertThat(response.get(listHeaderKey)).extracting(JsonNode::textValue).containsExactlyInAnyOrderElementsOf(listHeaderValues);
    }
}

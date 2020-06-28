package com.github.m0nk3y2k4.thetvdb.internal.connection;

import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.JSON_SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod;
import com.github.m0nk3y2k4.thetvdb.junit.jupiter.WithHttpsMockServer;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.verify.VerificationTimes;

@WithHttpsMockServer
public class PutRequestTest {

    @Test
    void send_verfiyHttpMethodIsSet(MockServerClient client, RemoteAPI remoteAPI) throws Exception {
        final String resource = "/putRequestMethod";
        PutRequest request = new PutRequest(resource);
        request.setRemoteAPI(remoteAPI);
        request.send();
        client.verify(request(resource).withMethod(HttpRequestMethod.PUT.getName()), VerificationTimes.once());
    }

    @Test
    void send_verifyJSONContentParsed(RemoteAPI remoteAPI) throws Exception {
        PutRequest request = new PutRequest("/putRequestContent");
        request.setRemoteAPI(remoteAPI);
        JsonNode response = request.send();
        assertThat(response.toString()).isEqualTo(JSON_SUCCESS);
    }
}

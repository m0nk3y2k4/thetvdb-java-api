package com.github.m0nk3y2k4.thetvdb.internal.connection;

import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.JSON_DATA;
import static com.github.m0nk3y2k4.thetvdb.testutils.MockServerUtil.JSON_SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockserver.model.HttpRequest.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.m0nk3y2k4.thetvdb.internal.exception.APIPreconditionException;
import com.github.m0nk3y2k4.thetvdb.internal.util.http.HttpRequestMethod;
import com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter.WithHttpsMockServer;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.verify.VerificationTimes;

@SuppressWarnings("ConstantConditions")
@WithHttpsMockServer
class PostRequestTest {

    @Test
    void send_verifyHttpMethodIsSet(MockServerClient client, RemoteAPI remoteAPI) throws Exception {
        final String resource = "/postRequestMethod";
        PostRequest request = new PostRequest(resource, JSON_DATA);
        request.setRemoteAPI(remoteAPI);
        request.send();
        client.verify(request(resource).withMethod(HttpRequestMethod.POST.getName()), VerificationTimes.once());
    }

    @Test
    void send_withoutData_verifyPreparationPreconditionCheck(RemoteAPI remoteAPI) {
        final String resource = "/postRequestNoData";
        PostRequest request = new PostRequest(resource, null);
        request.setRemoteAPI(remoteAPI);
        APIPreconditionException exception = catchThrowableOfType(request::send, APIPreconditionException.class);
        assertThat(exception).hasMessageContaining("data is not set");
    }

    @Test
    void send_withData_verifyOutputIsSet(MockServerClient client, RemoteAPI remoteAPI) throws Exception {
        final String resource = "/postRequestOutput";
        final String json = "{\"Value\":\"Some content of the POST-request\"}";
        PostRequest request = new PostRequest(resource, json);
        request.setRemoteAPI(remoteAPI);
        request.send();
        client.verify(request(resource).withBody(json), VerificationTimes.once());
    }

    @Test
    void send_verifyJSONContentParsed(RemoteAPI remoteAPI) throws Exception {
        PostRequest request = new PostRequest("/postRequestContent", JSON_DATA);
        request.setRemoteAPI(remoteAPI);
        JsonNode response = request.send();
        assertThat(response).hasToString(JSON_SUCCESS);
    }
}

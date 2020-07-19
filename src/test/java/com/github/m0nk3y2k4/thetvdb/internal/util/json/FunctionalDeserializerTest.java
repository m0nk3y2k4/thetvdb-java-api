package com.github.m0nk3y2k4.thetvdb.internal.util.json;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.testutils.json.Data;
import com.github.m0nk3y2k4.thetvdb.testutils.json.JSONTestUtil.JsonResource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class FunctionalDeserializerTest {


    @ParameterizedTest(name = "[{index}] {0} is deserialized properly")
    @EnumSource(value = JsonResource.class, names = {"EMPTY", "DATA"})
    void deserialize_withFullJSON_verifyJsonIsParsedProperly(JsonResource resource) throws Exception {
        APIResponse<Data> response = new FunctionalDeserializer<>(dataNode -> new ObjectMapper().readValue(dataNode.toString(), Data.class))
                .deserialize(new JsonFactory().createParser(resource.getUrl()), null);

        assertThat(response).isNotNull().isEqualTo(resource.getDTO());
    }
}
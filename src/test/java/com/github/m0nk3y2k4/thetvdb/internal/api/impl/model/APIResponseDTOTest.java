package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.m0nk3y2k4.thetvdb.testutils.json.Data;
import org.junit.jupiter.api.Test;

class APIResponseDTOTest {

    @Test
    void toString_withNullDTOs_verifyDefaultValuesInStringRepresentation() {
        assertThat(new APIResponseDTO.Builder<Data>().build()).asString().isEqualTo("Data: [], Errors: [], Links: []");
    }

    @Test
    void toString_withEmptyDTOs_verifyDefaultValuesInStringRepresentation() {
        assertThat(new APIResponseDTO.Builder<Data>().data(new Data())
                .errors(new APIResponseDTO.JSONErrorsDTO.Builder().build())
                .links(new APIResponseDTO.LinksDTO.Builder().build())
                .build()).asString().isEqualTo(
                        "Data: [], Errors: [Filters: [], Language: , QueryParams: []], Links: [First: , Last: , Next: , Previous: ]");
    }

    @Test
    void toString_withFilledDTOs_verifyStringRepresentation() {
        assertThat(new APIResponseDTO.Builder<Data>().data(Data.with("Content"))
                .errors(new APIResponseDTO.JSONErrorsDTO.Builder()
                        .addInvalidFilters("Fx", "fulter").invalidLanguage("cg").addInvalidQueryParams("itemKey", "q").build())
                .links(new APIResponseDTO.LinksDTO.Builder()
                        .first(1).last(12).next(5).previous(3).build())
                .build()).asString().isEqualTo(
                        "Data: [Content], " +
                                "Errors: [Filters: [Fx, fulter], Language: cg, QueryParams: [itemKey, q]], " +
                                "Links: [First: 1, Last: 12, Next: 5, Previous: 3]");
    }
}
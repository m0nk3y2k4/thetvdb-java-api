/*
 * Copyright (C) 2019 - 2022 thetvdb-java-api Authors and Contributors
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

package com.github.m0nk3y2k4.thetvdb.internal.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.QueryParametersImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class QueryResourceTest {

    private static final Object[] NO_PARAMS = new Object[0];

    //@DisableFormatting
    private static Stream<Arguments> withValidParameters() {
        return Stream.of(
                Arguments.of("/query", null, NO_PARAMS, "/query"),
                Arguments.of("/query/{language}", null, new Object[] {"eng"}, "/query/eng"),
                Arguments.of("/single", queryParameters("date", "today"), NO_PARAMS, "/single?date=today"),
                Arguments.of("/single/{id}/filter", queryParameters("date", "tomorrow"),
                        new Object[] {22L}, "/single/22/filter?date=tomorrow"),
                Arguments.of("/multi", queryParameters("rating", "2", "published", "true"),
                        NO_PARAMS, "/multi?rating=2&published=true"),
                Arguments.of("/multi/{id}/filter", queryParameters("rating", "2", "published", "true"),
                        new Object[] {25L}, "/multi/25/filter?rating=2&published=true"),
                Arguments.of("/invalid", queryParameters("rating", null, "published", "true"),
                        NO_PARAMS, "/invalid?published=true"),
                Arguments.of("/invalid", queryParameters("rating", "2", null, "true"),
                        NO_PARAMS, "/invalid?rating=2"),
                Arguments.of("/invalid/{language}", queryParameters(null, "2"), new Object[] {"eng"}, "/invalid/eng"),
                Arguments.of("/encoded", queryParameters("selection", "{id=47}", "status", "new"),
                        NO_PARAMS, "/encoded?selection=%7Bid%3D47%7D&status=new"),
                Arguments.of("/encoded/{id}", queryParameters("value", "&gt;"), new Object[] {5L}, "/encoded/5?value=%26gt%3B")
        );
    }
    //@EnableFormatting

    private static QueryParameters queryParameters(String key, String value) {
        return new NullableQueryParameters().addParameter(key, value);
    }

    private static QueryParameters queryParameters(String key1, String value1, String key2, String value2) {
        return new NullableQueryParameters().addParameter(key1, value1).addParameter(key2, value2);
    }

    @ParameterizedTest(name = "[{index}] Resource String for path <{0}> with wildcard params {2} and query params {1} is: {3}")
    @MethodSource("withValidParameters")
    void createQueryResource_verifyResourceString(String path, QueryParameters queryParams, Object[] wildcardParams,
            String expected) {
        assertThat(QueryResource.createQueryResource(path, queryParams, wildcardParams)).isEqualTo(expected);
    }

    @Test
    void createNewQueryResource_happyDay() {
        assertThatCode(TestQueryResource::new).doesNotThrowAnyException();
    }

    private static final class NullableQueryParameters extends QueryParametersImpl {

        @Override
        public QueryParameters addParameter(@Nonnull String key, @Nonnull String value) {
            params.put(key, value);
            return this;
        }
    }

    private static final class TestQueryResource extends QueryResource {}
}

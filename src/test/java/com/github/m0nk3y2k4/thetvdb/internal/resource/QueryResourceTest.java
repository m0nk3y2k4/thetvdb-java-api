package com.github.m0nk3y2k4.thetvdb.internal.resource;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.QueryParametersImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class QueryResourceTest {

    private final QueryResource resource = new QueryResource() {};

    private static Stream<Arguments> createQueryResource_verifyResourceString() {
        return Stream.of(
                Arguments.of("/query", null, null, "/query"),
                Arguments.of("/query", "/pending", null, "/query/pending"),
                Arguments.of("/single", null, queryParameters("date", "today"), "/single?date=today"),
                Arguments.of("/single", "/filter", queryParameters("date", "tomorrow"), "/single/filter?date=tomorrow"),
                Arguments.of("/multi", null, queryParameters("rating", "2", "published", "true"), "/multi?rating=2&published=true"),
                Arguments.of("/multi", "/filter", queryParameters("rating", "2", "published", "true"), "/multi/filter?rating=2&published=true"),
                Arguments.of("/invalid", null, queryParameters("rating", null, "published", "true"), "/invalid?published=true"),
                Arguments.of("/invalid", null, queryParameters("rating", "2", null, "true"), "/invalid?rating=2"),
                Arguments.of("/invalid", "/filter", queryParameters(null, "2"), "/invalid/filter"),
                Arguments.of("/encoded", null, queryParameters("selection", "{id=47}", "status", "new"), "/encoded?selection=%7Bid%3D47%7D&status=new"),
                Arguments.of("/encoded", "/filter", queryParameters("value", "&gt;"), "/encoded/filter?value=%26gt%3B")
        );
    }

    @ParameterizedTest(name = "[{index}] Resource String for base \"{0}\", specific \"{1}\" and query parameters {2} is: \"{3}\"")
    @MethodSource
    void createQueryResource_verifyResourceString(String base, String specific, QueryParameters queryParameters, String expected) {
        assertThat(resource.createQueryResource(base, specific, queryParameters)).isEqualTo(expected);
    }

    private static QueryParameters queryParameters(String key, String value) {
        return new NullableQueryParameters().addParameter(key, value);
    }

    private static QueryParameters queryParameters(String key1, String value1, String key2, String value2) {
        return new NullableQueryParameters().addParameter(key1, value1).addParameter(key2, value2);
    }

    private static final class NullableQueryParameters extends QueryParametersImpl {

        @Override
        public QueryParameters addParameter(@Nonnull String key, @Nonnull String value) {
            params.put(key, value);
            return this;
        }
    }
}
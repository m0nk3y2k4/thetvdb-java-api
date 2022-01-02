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

package com.github.m0nk3y2k4.thetvdb.internal.api.impl;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Internal implementation of the {@link QueryParameters} interface
 */
public class QueryParametersImpl implements QueryParameters {

    /** Simple map holding the individual query parameters as key/value pairs */
    protected final Map<String, String> params = new LinkedHashMap<>();

    /**
     * Creates a new empty set of query parameters
     */
    public QueryParametersImpl() {}

    /**
     * Creates an object of this class with a pre-defined set of individual query parameters based on the given
     * key/value pairs. For each entry in this map an appropriate query parameter will be added to this instance.
     *
     * @param parameters Map containing key/value pairs of query parameters to be initially added to this instance.
     *                   Might be empty but not <em>{@code null}</em>.
     */
    public QueryParametersImpl(@Nonnull Map<String, String> parameters) {
        Parameters.validateNotNull(parameters, "Parameters map must not be NULL");

        parameters.forEach(this::addParameter);
    }

    @Override
    public QueryParameters addParameter(@Nonnull String key, @Nonnull String value) {
        Parameters.validateNotNull(key, "Parameter key must not be NULL");
        Parameters.validateNotNull(value, "Parameter value must not be NULL");

        params.put(key, value);
        return this;
    }

    @Override
    public Optional<String> getParameterValue(@Nonnull String key) {
        return Optional.ofNullable(params.get(key));
    }

    @Override
    public boolean containsParameter(@Nonnull String key) {
        return params.containsKey(key);
    }

    @Override
    public int size() {
        return params.size();
    }

    @Override
    public Stream<Parameter> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED), false);
    }

    @Override
    @Nonnull
    public Iterator<Parameter> iterator() {
        return new QueryParameterIterator(params.entrySet().iterator());
    }

    @Override
    public String toString() {
        return params.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(", ", "[", "]"));
    }

    /**
     * Wrapper class used to map query parameter key/value pairs into distinct {@link Parameter} objects
     */
    private static final class QueryParameterIterator implements Iterator<Parameter> {

        /** Iterator of the query parameters hold by this object */
        private final Iterator<Map.Entry<String, String>> parameters;

        private QueryParameterIterator(Iterator<Map.Entry<String, String>> parameters) {
            this.parameters = parameters;
        }

        @Override
        public boolean hasNext() {
            return parameters.hasNext();
        }

        @Override
        public Parameter next() {
            return new QueryParameter(parameters.next());
        }
    }

    /**
     * Wrapper class for single query parameter key/value map entries
     */
    private static final class QueryParameter implements Parameter {

        /** The next key/value pair from the parameters map */
        private final Map.Entry<String, String> parameter;

        private QueryParameter(Map.Entry<String, String> parameter) {
            this.parameter = parameter;
        }

        @Override
        public String getKey() {
            return parameter.getKey();
        }

        @Override
        public String getValue() {
            return parameter.getValue();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Parameter other = (Parameter)obj;
            return Objects.equals(parameter.getKey(), other.getKey()) && Objects
                    .equals(parameter.getValue(), other.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hash(parameter.getKey(), parameter.getValue());
        }
    }
}

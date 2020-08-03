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
    public QueryParametersImpl() {
        super();
    }

    /**
     * Creates an object of this class with a pre-defined set of individual query parameters
     * based on the given key/value pairs. For each entry in this map an appropriate query parameter
     * will be added to this instance.
     *
     * @param parameters Map containing key/value pairs of query parameters to be initially added to this instance. Might
     *                   be empty but not <em>{@code null}</em>.
     */
    public QueryParametersImpl(@Nonnull Map<String, String> parameters) {
        super();

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
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this.iterator(), Spliterator.ORDERED), false);
    }

    @Override
    @Nonnull public Iterator<Parameter> iterator() {
        return new Iterator<>() {

            /** Iterator of the query parameters hold by this object */
            private final Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Parameter next() {
                return new Parameter() {

                    /** The next key/value pair from the parameters map */
                    private final Map.Entry<String, String> entry = iterator.next();

                    @Override
                    public String getKey() {
                        return entry.getKey();
                    }

                    @Override
                    public String getValue() {
                        return entry.getValue();
                    }

                    @Override
                    public boolean equals(Object o) {
                        if (o == null || getClass() != o.getClass()) {
                            return false;
                        }
                        Parameter that = (Parameter) o;
                        return Objects.equals(entry.getKey(), that.getKey()) && Objects.equals(entry.getValue(), that.getValue());
                    }

                    @Override
                    public int hashCode() {
                        return Objects.hash(entry.getKey(), entry.getValue());
                    }
                };
            }
        };
    }

    @Override
    public String toString() {
        return params.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(", ", "[","]"));
    }
}

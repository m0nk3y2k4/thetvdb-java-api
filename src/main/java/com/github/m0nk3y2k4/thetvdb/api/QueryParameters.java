package com.github.m0nk3y2k4.thetvdb.api;

import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory;
import com.github.m0nk3y2k4.thetvdb.api.constants.Query;

/**
 * Interface representing a set of query parameters. These parameters can be used for some of the API
 * calls in order to narrow the results being returned by the remote service. Instances of this interface
 * might be created by using the {@link TheTVDBApiFactory} class.
 * <p><br>
 * Individual parameters hold by this object can be accessed as Iterator or Stream.
 */
public interface QueryParameters extends Iterable<QueryParameters.Parameter> {

    /**
     * Adds a new parameter to this object. An individual query parameter consists of a key/value pair. Adding the same parameter key
     * twice will not result in two different parameters. Instead the parameter value from the first addition will be replaced by the
     * new given value. For a predefined list of query parameters see {@link Query}.
     *
     * @param key The new parameters key. The class {@link Query} prvides a basic set of parameter constants
     *            for the various API routes, which can be used at this point.
     * @param value The new parameters value. Should not be <em>{@code null}</em> or empty. These values will be encoded before being added to the URI.
     *
     * @return Reference to this very instance. Can be used to add multiple parameters in a fluent notation.
     */
    QueryParameters addParameter(@Nonnull String key, @Nonnull String value);

    /**
     * Returns an Optional representing the parameter value for the given key. The Optional might be empty if this object currently holds no
     * individual query parameter with this key.
     *
     * @see #containsParameter(String) containsParameter(key)
     *
     * @param key The parameter key for which the current value should be returned
     *
     * @return Optional containing the current value for the given key or {@link Optional#empty()} if no parameter with this key exists
     */
    Optional<String> getParameterValue(@Nonnull String key);

    /**
     * Returns <em>{@code true}</em> if an individual parameter with the given key exists in this very object or <em>{@code false}</em> if no such parameter has been added yet.
     *
     * @param key The parameter key to check for
     *
     * @return <em>{@code True}</em> if a parameter with the given key has already been added to this object or <em>{@code false}</em> if not
     */
    boolean containsParameter(@Nonnull String key);

    /**
     * Returns the count of single parameter elements held by this container
     *
     * @return Number of single query parameters contained in this parameter set
     */
    int size();

    /**
     * Returns a Stream of individual query parameters held by this class.
     *
     * @return Stream of all query parameters that have been added to this object
     */
    Stream<Parameter> stream();

    /**
     * Interface representing a single, individual key/value parameter
     */
    interface Parameter {

        /**
         * Returns the key of the parameter
         *
         * @return Parameter key
         */
        String getKey();

        /**
         * Returns the value of the parameter
         *
         * @return Parameter value
         */
        String getValue();
    }
}

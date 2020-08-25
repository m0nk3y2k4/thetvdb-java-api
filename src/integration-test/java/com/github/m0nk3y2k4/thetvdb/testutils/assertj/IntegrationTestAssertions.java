package com.github.m0nk3y2k4.thetvdb.testutils.assertj;

import static com.github.m0nk3y2k4.thetvdb.internal.util.functional.ThrowableFunctionalInterfaces.Procedure;
import static com.github.m0nk3y2k4.thetvdb.internal.util.functional.ThrowableFunctionalInterfaces.Supplier;

import java.util.Optional;

import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;

/**
 * Entry point for integration test assertion methods for different types
 * <p><br>
 * Each method in this class is a static factory for a type-specific assertion object, based on either a void remote API
 * route or a route returning some actual response value.
 */
public class IntegrationTestAssertions {

    /**
     * Create assertion for a {@link Supplier Supplier&lt;T,APIException&gt;} object representing a non-void API route that returns
     * an object of type <em>{@code T}</em>
     *
     * @param actual The non-void remote API route supplier
     *
     * @param <T> the type of object returned by the remote API route when invoked
     *
     * @return The created assertion object
     */
    public static <T> AbstractIntegrationTestAssert<Supplier<T, APIException>> assertThat(Supplier<T, APIException> actual) {
        return new AbstractIntegrationTestAssert<>(actual) {
            @Override
            Optional<?> execute() throws APIException {
                return Optional.of(actual.get());
            }
        };
    }

    /**
     * Create assertion for a {@link Procedure Procedure&lt;APIException&gt;} object representing a void API route that returns
     * no value
     *
     * @param actual The void remote API route procedure
     *
     * @return The created assertion object
     */
    public static AbstractIntegrationTestAssert<Procedure<APIException>> assertThat(Procedure<APIException> actual) {
        return new AbstractIntegrationTestAssert<>(actual) {
            @Override
            Optional<?> execute() throws APIException {
                actual.invoke();
                return Optional.empty();
            }
        };
    }
}


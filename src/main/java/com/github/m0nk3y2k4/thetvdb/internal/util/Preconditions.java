package com.github.m0nk3y2k4.thetvdb.internal.util;

import java.util.function.Predicate;

/**
 * Simple set of preconditions to be used for method parameter validation.
 */
public final class Preconditions {

    private Preconditions() {}     // Hidden constructor. Only static methods

    /**
     * Checks the condition and throws the given runtime exception in case the condition is not met.
     *
     * @param condition The condition to check for
     * @param value The value to check against the codition
     * @param exception The exception to be thrown in case the condition is not met
     *
     * @param <T> the type of the value to check
     * @param <X> type of the runtime exception to be thrown
     */
    public static <T, X extends RuntimeException> void requires(Predicate<T> condition, T value, X exception) {
        if (!condition.test(value)) {
            throw exception;
        }
    }
}

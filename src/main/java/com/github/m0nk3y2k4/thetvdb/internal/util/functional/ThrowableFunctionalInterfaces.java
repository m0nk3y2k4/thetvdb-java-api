package com.github.m0nk3y2k4.thetvdb.internal.util.functional;

/**
 * Collection of {@link FunctionalInterface FunctionalInterfaces} which allow simple exception handling.
 * <p>
 * Provides a set of commonly used functional interfaces that enables the actual implementation to throw
 * a single exception. Simplyfies the usage of lambda-expressions by not being forced to include exception
 * handling into the expression itself.
 */
public interface ThrowableFunctionalInterfaces {

    /**
     * Extended {@link java.util.function.Supplier Supplier&lt;T&gt;} functional interface which allows the <code>get</code> method
     * to throw an exception of type X.
     *
     * @param <T> the type of results supplied by this supplier
     * @param <X> the type of exception to be thrown by the supplier
     */
    @FunctionalInterface
    interface Supplier<T, X extends Exception> {

        /**
         * Wraps the given supplier into a throwable supplier
         *
         * @param supplier The supplier to wrap
         *
         * @param <T> the type of results supplied by this supplier
         * @param <X> the type of exception to be thrown by the supplier
         *
         * @return The given supplier wrapped as a throwable supplier object
         */
        static <T, X extends Exception> Supplier<T, X> of(java.util.function.Supplier<T> supplier) {
            return supplier::get;
        }

        /**
         * Gets a result.
         *
         * @return a result
         *
         * @throws X When an exception occurred during invocation
         */
        T get() throws X;
    }

    /**
     * Extended {@link java.util.function.Function Function&lt;T, R&gt;} functional interface which allows the <code>apply</code> method
     * to throw an exception of type X.
     *
     * @param <T> the type of the input to the function
     * @param <R> the type of the result of the function
     * @param <X> the type of exception to be thrown by the function
     */
    @FunctionalInterface
    interface Function<T, R, X extends Exception> {

        /**
         * Wraps the given function into a throwable function
         *
         * @param function The function to wrap
         *
         * @param <T> the type of the input to the function
         * @param <R> the type of the result of the function
         * @param <X> the type of exception to be thrown by the function
         *
         * @return The given function wrapped as a throwable function object
         */
        static <T, R, X extends Exception> Function<T, R, X> of(java.util.function.Function<T, R> function) {
            return function::apply;
        }

        /**
         * Applies this function to the given argument.
         *
         * @param t the function argument
         *
         * @return the function result
         *
         * @throws X When an exception occurred during invocation
         */
        R apply(T t) throws X;
    }
}

/*
 * Copyright (C) 2019 - 2021 thetvdb-java-api Authors and Contributors
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

package com.github.m0nk3y2k4.thetvdb.internal.util.functional;

/**
 * Collection of {@link FunctionalInterface FunctionalInterfaces} which allow simple exception handling.
 * <p><br>
 * Provides a set of commonly used functional interfaces that enables the actual implementation to throw a single
 * exception. Simplifies the usage of lambda-expressions by not being forced to include exception handling into the
 * expression itself.
 */
public interface ThrowableFunctionalInterfaces {

    /**
     * Extended {@link java.util.function.Supplier Supplier&lt;T&gt;} functional interface which allows the <em>{@code
     * get}</em> method to throw an exception of type X.
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
         * @param <T>      the type of results supplied by this supplier
         * @param <X>      the type of exception to be thrown by the supplier
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
     * Extended {@link java.util.function.Function Function&lt;T, R&gt;} functional interface which allows the
     * <em>{@code apply}</em> method to throw an exception of type X.
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
         * @param <T>      the type of the input to the function
         * @param <R>      the type of the result of the function
         * @param <X>      the type of exception to be thrown by the function
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

    /**
     * Procedure functional interface which allows the <em>{@code invoke}</em> method to throw an exception of type X.
     * Can be used for lambda-representations of a void method that may throw an exception.
     *
     * @param <X> the type of exception to be thrown by the procedure
     */
    @FunctionalInterface
    interface Procedure<X extends Exception> {

        /**
         * Invokes the procedure.
         *
         * @throws X When an exception occurred during invocation
         */
        void invoke() throws X;
    }
}

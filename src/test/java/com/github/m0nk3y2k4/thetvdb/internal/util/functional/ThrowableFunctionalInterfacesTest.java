package com.github.m0nk3y2k4.thetvdb.internal.util.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

class ThrowableFunctionalInterfacesTest {

    @Test
    void createThrowableSupplier_throwingException_verifyExceptionIsThrown() {
        ThrowableFunctionalInterfaces.Supplier<String, IOException> throwableSupplier = () -> {throw new IOException();};
        assertThatExceptionOfType(IOException.class).isThrownBy(throwableSupplier::get);
    }

    @Test
    void createThrowableSupplier_fromSupplier_verifySuppliedValue() throws Exception {
        final String stringValue = "Some supplier value";
        Supplier<String> supplier = () -> stringValue;
        ThrowableFunctionalInterfaces.Supplier<String, Exception> throwableSupplier = ThrowableFunctionalInterfaces.Supplier.of(supplier);
        assertThat(throwableSupplier.get()).isEqualTo(stringValue);
    }

    @Test
    void createThrowableFunction_throwingException_verifyExceptionIsThrown() {
        ThrowableFunctionalInterfaces.Function<Integer, Double, IOException> throwableFunction = i -> {throw new IOException();};
        assertThatExceptionOfType(IOException.class).isThrownBy(() -> throwableFunction.apply(2));
    }

    @Test
    void createThrowableFunction_fromFunction_verifyFunctionResult() throws Exception {
        Function<Integer, Double> function = Integer::doubleValue;
        ThrowableFunctionalInterfaces.Function<Integer, Double, Exception> throwableFunction = ThrowableFunctionalInterfaces.Function.of(function);
        assertThat(throwableFunction.apply(2)).isEqualTo(2.0D);
    }
}
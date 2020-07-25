package com.github.m0nk3y2k4.thetvdb.internal.resource;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ResourceTest {

    private final Resource resource = new Resource() {};

    private static Stream<Arguments> createBaseResource_verifyResourceString() {
        return Stream.of(
                Arguments.of("/users", new Object[0], "/users"),
                Arguments.of("/orders", new Object[] {1, 2, 3}, "/orders/1/2/3"),
                Arguments.of("/shipment", new Object[] {"pending", "today"}, "/shipment/pending/today")
        );
    }

    private static Stream<Arguments> createSpecificResource_verifyResourceString() {
        return Stream.of(
                Arguments.of("/invoices", null, new Object[0], "/invoices"),
                Arguments.of("/invoices", null, new Object[] {1,2}, "/invoices/1/2"),
                Arguments.of("/invoices", "/pending", new Object[0], "/invoices/pending"),
                Arguments.of("/invoices", "/sent", new Object[] {"customer", "atari"}, "/invoices/sent/customer/atari")
        );
    }

    @ParameterizedTest(name = "[{index}] Resource String for base \"{0}\" and path parameters {1} is: \"{2}\"")
    @MethodSource
    void createBaseResource_verifyResourceString(String base, Object[] pathParams, String expected) {
        assertThat(resource.createResource(base, pathParams)).isEqualTo(expected);
    }

    @ParameterizedTest(name = "[{index}] Resource String for base \"{0}\", specific \"{1}\" and path parameters {2} is: \"{3}\"")
    @MethodSource
    void createSpecificResource_verifyResourceString(String base, String specific, Object[] pathParams, String expected) {
        assertThat(resource.createResource(base, specific, pathParams)).isEqualTo(expected);
    }
}
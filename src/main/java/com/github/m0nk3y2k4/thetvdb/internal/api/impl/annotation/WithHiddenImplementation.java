package com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation;

import org.immutables.value.Value;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Forces <code>Immutables</code> to restrict the visibility of auto-generated DTO implementations in order to "hide" them.
 * <p>
 * This is a composed annotation providing some commonly used preconfiguration for DTO's with regards to the <code>Immutables</code> API.
 * It forces the actual immutable implementation to be private and the corresponding builder to be package-private.
 */
@Documented
@Value.Style(
        visibility = Value.Style.ImplementationVisibility.PRIVATE,      // Immutable implementation
        builderVisibility = Value.Style.BuilderVisibility.PACKAGE)      // Builder implementation
@Target({ElementType.TYPE})
public @interface WithHiddenImplementation {}

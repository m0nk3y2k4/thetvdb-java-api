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

package com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import org.immutables.value.Value;

/**
 * Forces <em>{@code Immutables}</em> to restrict the visibility of auto-generated DTO implementations in order to
 * "hide" them.
 * <p><br>
 * This is a composed annotation providing some commonly used preconfiguration for DTO's with regards to the <em>{@code
 * Immutables}</em> API. It forces the actual immutable implementation to be private and the corresponding builder to be
 * package-private.
 */
@Documented
@Value.Style(
        visibility = Value.Style.ImplementationVisibility.PRIVATE,      // Immutable implementation
        builderVisibility = Value.Style.BuilderVisibility.PACKAGE)      // Builder implementation
@Target(ElementType.TYPE)
public @interface WithHiddenImplementation {}

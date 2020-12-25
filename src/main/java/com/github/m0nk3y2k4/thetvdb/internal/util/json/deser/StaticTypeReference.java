/*
 * Copyright (C) 2019 - 2020 thetvdb-java-api Authors and Contributors
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

package com.github.m0nk3y2k4.thetvdb.internal.util.json.deser;

import java.lang.reflect.Type;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * A specific type reference representing some static value type.
 * <p><br>
 * Jackson's type references use type inference and reflection in order to automatically determine the correct type used
 * for JSON deserialization. However, this is bound to the limits of the Java inference algorithm what means that it can
 * only be used as long as the compiler can infer the type arguments from the context. This may not always be the case,
 * especially when using generic type arguments in nested methods for example.
 * <p><br>
 * For such cases objects of this class can be used in order to cover the automatic type inference but always return a
 * fix, preconfigured type instead. Just like the actual type reference it will instantiate a reference to a generic
 * type when creating a new instance. However, this reference will be retained by the object and won't be returned when
 * {@link #getType()} is invoked. Instead the static type value provided via the constructor will be returned. For
 * example, the following code cannot properly infer the type reference from the context and will probably end up in a
 * NullPointerException:
 * <pre>{@code
 *     public T getArtwork(JsonNode artwork) throws APIException {
 *         return APIJsonMapper.readValue(artwork, new TypeReference<>() {});       // What concrete type is "T"?
 *     }
 * }</pre>
 * This can be addressed by using some static type reference returning the correct type.
 * <pre>{@code
 *     public T getArtwork(JsonNode artwork) throws APIException {
 *         return APIJsonMapper.readValue(artwork, new StaticTypeReference<>(Artwork.class) {});
 *     }
 * }</pre>
 * Admittedly, this might be some constructed example but there are other, complex use cases where setting a fix type
 * reference, to overcome the limits of automatic type inference, comes in quite handy.
 *
 * @param <T> not actually used by this class
 */
public final class StaticTypeReference<T> extends TypeReference<T> {

    /** The static type to be returned */
    private final Type type;

    /**
     * Creates a new static type reference for the given type
     *
     * @param type The static type to be returned by the {@link #getType()} method
     */
    public StaticTypeReference(Type type) {
        this.type = type;
    }

    /**
     * Returns this objects static type value
     *
     * @return Type value set during instantiation
     */
    @Override
    public Type getType() {
        return type;
    }
}

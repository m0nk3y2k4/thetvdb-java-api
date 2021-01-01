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

package com.github.m0nk3y2k4.thetvdb.internal.util.json.deser;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.APIResponseDTO;
import com.github.m0nk3y2k4.thetvdb.internal.util.functional.ThrowableFunctionalInterfaces;
import com.github.m0nk3y2k4.thetvdb.internal.util.json.APIJsonMapper;

/**
 * Specific JSON deserializer for parsing an API response providing some extended JSON <em>{@code data}</em> node
 * processing capabilities.
 * <p><br>
 * This class enables the {@link APIJsonMapper} to provide some specific handling of the top-level JSON <em>{@code
 * data}</em> node. For this a mapping function has to be provided when creating a new instance of this class. This
 * function will then be invoked in order to deserialize the JSON's <em>{@code data}</em> node and the result of this
 * invocation will be set to the returned API response. Technically it means that the object mapper will simply leave it
 * up to the function to deserialize the <em>{@code data}</em> node rather than using it's native deserialization
 * implementation.
 *
 * @param <T> The type of object that should be the outcome of the deserialization
 * @param <X> The type of exception which the given mapping function is permitted to throw
 */
public final class APIResponseDeserializer<T, X extends IOException> extends JsonDeserializer<APIResponse<T>> {

    /** Mapper used for parsing the API response JSON */
    private final ObjectMapper mapper = new ObjectMapper();

    /** The mapping function to be invoked in order to parse the <em>{@code data}</em> node */
    private final ThrowableFunctionalInterfaces.Function<JsonNode, T, X> dataFunction;

    /**
     * Creates a new functional deserializer which is backed by the given data mapping function.
     *
     * @param dataFunction Mapping function to be invoked in order to deserialize the JSON's <em>{@code data}</em> node
     */
    public APIResponseDeserializer(@Nonnull ThrowableFunctionalInterfaces.Function<JsonNode, T, X> dataFunction) {
        this.dataFunction = dataFunction;
    }

    /**
     * Checks if the specified field exists in the given JSON. If so, the node will be applied to the given mapping
     * function and it's result will be returned. If the JSON does not contain a node with the specified name or the
     * node exists but is a "null node", an {@link IllegalArgumentException} will be thrown.
     *
     * @param json      Base JSON object used for parsing
     * @param fieldName Name of the top-level node to be deserialized
     * @param mapping   Mapping function returning the deserialized object of type <b>U</b>
     * @param <U>       The type of object that the JSON node should be mapped to
     *
     * @return The result of deserializing the referenced top-level node of this JSON object.
     *
     * @throws IOException              If an IO error occurred during the deserialization of the given JSON object
     * @throws IllegalArgumentException If either no node with the given name exists on top-level of this JSON object or
     *                                  the node exists but is a "null node"
     */
    private static <U> U parseNode(JsonNode json, String fieldName,
            ThrowableFunctionalInterfaces.Function<JsonNode, U, IOException> mapping) throws IOException {
        return mapping.apply(json.path(fieldName).requireNonNull());
    }

    @Override
    public APIResponse<T> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode json = mapper.readTree(jsonParser);

        T data = parseNode(json, "data", dataFunction::apply);
        String status = parseNode(json, "status", node -> mapper.readValue(node.toString(), String.class));

        return new APIResponseDTO.Builder<T>().data(data).status(status).build();
    }
}

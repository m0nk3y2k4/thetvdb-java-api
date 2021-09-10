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
import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse.Links;
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
 * up to the function to deserialize the <em>{@code data}</em> node rather than using its native deserialization
 * implementation.
 *
 * @param <T> The type of object that should be the outcome of the deserialization
 * @param <X> The type of exception which the given mapping function is permitted to throw
 */
public final class APIResponseDeserializer<T, X extends IOException> extends JsonDeserializer<APIResponse<T>> {

    /** Module used to extend the object mappers functionality in terms of mapping the responses DTO interfaces */
    private static final Module RESPONSE_MODULE = createResponseModule();

    /** Module used to extend the object mappers functionality in terms of mapping JDK8 Optionals */
    private static final Module JDK8_MODULE = new Jdk8Module();

    /** Mapper used for parsing the API response JSON */
    private final ObjectMapper mapper = new ObjectMapper().registerModules(JDK8_MODULE, RESPONSE_MODULE);

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
     * Creates a JSON module with enhanced functionality regarding the deserialization of <i>TheTVDB.com</i> API JSON
     * responses. This includes the usage of Java {@code Optional} data types as well as optional, non-mandatory
     * top-level properties being mapped into empty DTOs (rather than NULL).
     *
     * @return JSON module with enhanced functionality for parsing optional, non-mandatory top-level properties from the
     *         received JSON response
     */
    private static Module createResponseModule() {
        return new SimpleModule().setDeserializerModifier(new ObjectDeserializerModifier())
                .addAbstractTypeMapping(Links.class, APIResponseDTO.LinksDTO.class);
    }

    /**
     * Checks if the specified field exists in the given JSON. If so, the node will be applied to the given mapping
     * function, and its result will be returned. If the JSON does not contain a node with the specified name or the
     * node exists but is a {@link NullNode}, an {@link IllegalArgumentException} will be thrown.
     *
     * @param json      Base JSON object used for parsing
     * @param fieldName Name of the top-level node to be deserialized
     * @param mapping   Mapping function returning the deserialized object of type <b>U</b>
     * @param <U>       The type of object that the JSON node should be mapped to
     *
     * @return The result of deserializing the referenced top-level node of this JSON object
     *
     * @throws IOException              If an IO error occurred during the deserialization of the given JSON object
     * @throws IllegalArgumentException If either no node with the given name exists on top-level of this JSON object or
     *                                  the node exists but is a "null node"
     */
    private static <U> U parseMandatoryNode(JsonNode json, String fieldName, Mapping<U> mapping) throws IOException {
        return mapping.apply(json.path(fieldName).requireNonNull());
    }

    /**
     * Applies the specified field to the given mapping function and returns its result. If the JSON does not contain a
     * node with the specified name the mapping function will be invoked with a {@link NullNode} object instead. In this
     * case it depends on the actual deserializer implementation whether the mapping (and so this method) returns {@code
     * NULL} or a new, plain instance of type <b>U</b>.
     *
     * @param json      Base JSON object used for parsing
     * @param fieldName Name of the top-level node to be deserialized
     * @param mapping   Mapping function returning the deserialized object of type <b>U</b>
     * @param <U>       The type of object that the JSON node should be mapped to
     *
     * @return The result of deserializing the referenced top-level node of this JSON object
     *
     * @throws IOException If an IO error occurred during the deserialization of the given JSON object
     */
    @SuppressWarnings("SameParameterValue")
    private static <U> U parseOptionalNode(JsonNode json, String fieldName, Mapping<U> mapping) throws IOException {
        return mapping.apply(Optional.ofNullable(json.get(fieldName)).orElse(NullNode.getInstance()));
    }

    @Override
    public APIResponse<T> deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        JsonNode json = mapper.readTree(jsonParser);

        T data = parseMandatoryNode(json, "data", dataFunction::apply);
        String status = parseMandatoryNode(json, "status", node -> mapper.readValue(node.toString(), String.class));
        Links links = parseOptionalNode(json, "links", node -> mapper.treeToValue(node, Links.class));

        return new APIResponseDTO.Builder<T>().data(data).status(status).links(links).build();
    }

    /**
     * Functional interface representing a simple mapping function to convert a given JSON node into a specific
     * Java-type DTO model. The mapping is allowed to throw an {@link IOException} if required.
     *
     * @param <U> The type of object that the JSON node should be mapped to
     */
    @FunctionalInterface
    private interface Mapping<U> extends ThrowableFunctionalInterfaces.Function<JsonNode, U, IOException> {
    }
}

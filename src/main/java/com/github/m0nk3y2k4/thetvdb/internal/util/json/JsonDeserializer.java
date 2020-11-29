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

package com.github.m0nk3y2k4.thetvdb.internal.util.json;

import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_JSON_PARSE_ERROR;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIRuntimeException;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.APIResponseDTO;
import com.github.m0nk3y2k4.thetvdb.internal.util.functional.ThrowableFunctionalInterfaces;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

// ToDo: Check which of the parsing util methods are still needed

/**
 * Utility class for JSON response deserialization.
 * <p><br>
 * Provides functionality to parse the JSON data returned by the remote API and map it into it's corresponding DTO.
 * These DTO's will be wrapped into {@link APIResponse APIResponse&lt;DTO&gt;} objects together with additional
 * information like processing status information.
 */
public final class JsonDeserializer {

    /** Module used to extend the object mappers functionality in terms of mapping the APIs data model interfaces */
    private static final SimpleModule DATA_MODULE = new SimpleModule();

    static {
        // Add Interface <-> Implementation mappings to the module. The object mapper will use these mappings to determine the
        // proper builder to be used to create new instances of a specific interface (via @JsonDeserialize annotation).
        // ToDo: Extend with actual mappings
        DATA_MODULE.setAbstractTypes(new SimpleAbstractTypeResolver());
    }

    private JsonDeserializer() {}     // Private constructor. Only static methods

    /**
     * Returns the <em>{@code data}</em> node of the given JSON. The node must be located on the top-level of the JSON.
     *
     * @param json The JSON containing the <em>{@code data}</em> node on top-level
     *
     * @return The top-level <em>{@code data}</em> of the given JSON
     */
    private static JsonNode getData(@Nonnull JsonNode json) {
        return json.get("data");
    }

    /**
     * Creates a new mapping function for the <em>{@code data}</em> JSON node based on the given type reference.
     *
     * @param baseTypeReference The corresponding type reference for the <em>{@code data}</em> node mapping function
     * @param <T>               The type of object that the node should be mapped to
     *
     * @return New throwable mapping function based on the given type reference. This function may throw exceptions of
     *         the type {@link IOException} when invoking it's <i>apply</i> method.
     */
    private static <T> ThrowableFunctionalInterfaces.Function<JsonNode, T, IOException> createDataFunction(
            @Nonnull TypeReference<T> baseTypeReference) {
        return node -> mapDataObject(node, new DataTypeReference<>(baseTypeReference));
    }

    /**
     * Creates a new JSON object mapper module which uses the given type reference to perform a functional
     * deserialization of the JSON's <em>{@code data}</em> node when parsing a parameterized API response.
     *
     * @param typeReference The type reference used for the functional JSON deserialization
     * @param <T>           The type of object that the <em>{@code data}</em> node should be mapped to
     *
     * @return Simple JSON object mapper module containing a single functional deserializer
     */
    private static <T> Module createFunctionalModule(@Nonnull TypeReference<T> typeReference) {
        return createFunctionalModule(createDataFunction(typeReference));
    }

    /**
     * Creates a new JSON object mapper module which uses the given supplier to perform a functional deserialization of
     * the JSON's <em>{@code data}</em> node when parsing a parameterized API response. As it is not possible to access
     * the actual JSON data from within the supplier, modules created by this method may primarily be used to specify
     * some default object mapping (without parsing the actual JSON).
     *
     * @param supplier Supplier returning some default value without actually parsing the JSON <em>{@code data}</em>
     *                 node
     * @param <T>      The type of object that the <em>{@code data}</em> node should be mapped to
     *
     * @return Simple JSON object mapper module containing a single functional deserializer
     */
    private static <T> Module createFunctionalModule(@Nonnull Supplier<T> supplier) {
        return createFunctionalModule(ThrowableFunctionalInterfaces.Function.of(node -> supplier.get()));
    }

    /**
     * Creates a new JSON object mapper module which uses the given function to perform a functional deserialization of
     * the JSON's <em>{@code data}</em> node when parsing a parameterized API response. The given function will receive
     * the <em>{@code data}</em> node as input parameter and is required to return a new instance of type <b>T</b>. It's
     * up to the function itself whether it is actually interested in parsing the provided JSON. It may also just return
     * some default value while completely ignoring the JSON's content.
     *
     * @param dataFunction The function to be used to parse the JSON's <em>{@code data}</em> node. The object returned
     *                     by this function will be considered to be the valid outcome of parsing this node.
     * @param <T>          The type of object that the <em>{@code data}</em> node should be mapped to
     *
     * @return Simple JSON object mapper module containing a single functional deserializer
     *
     * @see #createFunctionalModule(ThrowableFunctionalInterfaces.Function) createFunctionalModule(throwableFunction)
     */
    private static <T> Module createFunctionalModule(@Nonnull Function<JsonNode, T> dataFunction) {
        return createFunctionalModule(ThrowableFunctionalInterfaces.Function.of(dataFunction));
    }

    /**
     * Creates a new JSON object mapper module which uses the given function to perform a functional deserialization of
     * the JSON's <em>{@code data}</em> node when parsing a parameterized API response. The given function will receive
     * the <em>{@code data}</em> node as input parameter and is required to return a new instance of type <b>T</b>. It's
     * up to the function itself whether it is actually interested in parsing the provided JSON. It may also just return
     * some default value while completely ignoring the JSON's content. The function is permitted to throw an {@link
     * IOException} as this is a common type of exception to be occurring when parsing JSON data.
     *
     * @param dataFunction The function to be used to parse the JSON's <em>{@code data}</em> node. The object returned
     *                     by this function will be considered to be the valid outcome of parsing this node.
     * @param <T>          The type of object that the <em>{@code data}</em> node should be mapped to
     *
     * @return Simple JSON object mapper module containing a single functional deserializer
     *
     * @see #createFunctionalModule(Function) createFunctionalModule(function)
     */
    private static <T> Module createFunctionalModule(
            @Nonnull ThrowableFunctionalInterfaces.Function<JsonNode, T, IOException> dataFunction) {
        return new SimpleModule().addDeserializer(APIResponse.class, new FunctionalDeserializer<>(dataFunction));
    }

    /**
     * Deserializes the given JSON based on the given type reference to a new object of type <b>T</b>. A new functional
     * module which is based on the given type reference will be used in order to map the JSON's top-level <em>{@code
     * data}</em> node.
     *
     * @param json          The JSON object to be parsed
     * @param typeReference The value type reference used for deserialization
     * @param <T>           The type of object that the JSON should be mapped to
     *
     * @return Deserialized JSON object mapped to an instance of type <b>T</b>
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    private static <T> T mapObject(@Nonnull JsonNode json, @Nonnull TypeReference<T> typeReference)
            throws APIException {
        return mapObject(json, typeReference, createFunctionalModule(typeReference));
    }

    /**
     * Deserializes the given <em>{@code dataNode}</em> based on the given type reference to a new object of type
     * <b>T</b> by using the default mapping module.
     *
     * @param dataNode          The node which should be parsed
     * @param dataTypeReference The data type reference used for deserialization
     * @param <T>               The type of object that the node should be mapped to
     *
     * @return Deserialized data node mapped to an instance of type <b>T</b>
     *
     * @throws IOException If an IO error occurred during the deserialization of the given JSON object
     */
    private static <T> T mapDataObject(@Nonnull JsonNode dataNode, @Nonnull TypeReference<T> dataTypeReference)
            throws IOException {
        return new ObjectMapper().registerModule(DATA_MODULE).readValue(dataNode.toString(), dataTypeReference);
    }

    /**
     * Deserializes the given JSON based on the given type reference to a new object of type <b>T</b>. The given mapping
     * module will be registered prior to the actual parsing in order to extend the mapper with additional functionality
     * required for a successful deserialization.
     *
     * @param json          The JSON object to be parsed
     * @param typeReference The value type reference used for deserialization
     * @param module        Module providing additional functionality required for a successful deserialization
     * @param <T>           The type of object that the JSON should be mapped to
     *
     * @return Deserialized JSON object mapped to an instance of type <b>T</b>
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    private static <T> T mapObject(@Nonnull JsonNode json, @Nonnull TypeReference<T> typeReference,
            @Nonnull Module module) throws APIException {
        try {
            return new ObjectMapper().registerModule(module).readValue(json.toString(), typeReference);
        } catch (JsonProcessingException ex) {
            throw new APIException(API_JSON_PARSE_ERROR, ex);
        }
    }

    /**
     * A specific type reference for the JSON's <em>{@code data}</em> node.
     * <p><br>
     * Objects of this class provide access to T's type argument. For example, if T represents a class
     * <em>{@code APIResponseDTO<Episode>}</em>, so an API response whose actual payload/data is an episode,
     * then an object of this class represents an <em>{@code Episode}</em> type argument.
     *
     * @param <T> the type of objects that is representing the base type reference (e.g. APIResponseDTO&lt;?&gt;)
     */
    private static final class DataTypeReference<T> extends TypeReference<T> {
        /** Type supplier for T's type argument */
        private final Supplier<Type> typeSupplier;

        /**
         * Creates a new data type reference based on the given base type reference.
         *
         * @param baseTypeReference The parameterized base type reference
         */
        DataTypeReference(TypeReference<T> baseTypeReference) {
            Parameters.validateCondition(ref -> ParameterizedType.class
                            .isAssignableFrom(ref.getType().getClass()), baseTypeReference,
                    new APIRuntimeException("Base type is required to be parameterized!"));
            this.typeSupplier = () -> ((ParameterizedType)baseTypeReference.getType()).getActualTypeArguments()[0];
        }

        @Override
        public Type getType() {
            return typeSupplier.get();
        }
    }
}

/**
 * Specific JSON deserializer for parsing an API response providing some extended JSON <em>{@code data}</em> node
 * processing capabilities.
 * <p><br>
 * This class extends the default {@link JsonDeserializer} adding the option to provide some specific handling of the
 * top-level JSON <em>{@code data}</em> node. For this a mapping function has to be provided when creating a new
 * instance of this class. This function will then be invoked in order to deserialize the JSON's <em>{@code data}</em>
 * node and the result of this invocation will be set to the returned API response. Technically it means that the object
 * mapper will simply leave it up to the function to deserialize the <em>{@code data}</em> node rather than using it's
 * native deserialization implementation.
 *
 * @param <T> The type of object that should be the outcome of the deserialization
 * @param <X> The type of exception which the given mapping function is permitted to throw
 */
class FunctionalDeserializer<T, X extends IOException> extends com.fasterxml.jackson.databind.JsonDeserializer<APIResponse<T>> {

    /** Module used to extend the object mappers functionality in terms of mapping the API response interfaces */
    private static final SimpleModule APIRESPONSE_MODULE = new SimpleModule();

    static {
        // Add Interface <-> Implementation mappings to the module. The object mapper will use these mappings to determine the
        // proper builder to be used to create new instances of a specific interface (via @JsonDeserialize annotation).
        // ToDo: Check if this is still needed. Might be superfluous due to the lack of complex types (except for
        //  "Data") within the APIResponse JSON.
        APIRESPONSE_MODULE.setAbstractTypes(new SimpleAbstractTypeResolver());
    }

    /** Mapper used to read the "status "JSON node */
    private final ObjectMapper mapper = new ObjectMapper().registerModule(APIRESPONSE_MODULE);

    /** The mapping function to be invoked in order to parse the <em>{@code data}</em> node */
    private final ThrowableFunctionalInterfaces.Function<JsonNode, T, X> dataFunction;

    /**
     * Creates a new functional deserializer which is backed by the given data mapping function.
     *
     * @param dataFunction Mapping function to be invoked in order to deserialize the JSON's <em>{@code data}</em> node
     */
    FunctionalDeserializer(@Nonnull ThrowableFunctionalInterfaces.Function<JsonNode, T, X> dataFunction) {
        this.dataFunction = dataFunction;
    }

    /**
     * Checks if the specified field exists in the given JSON. If so, the node will be applied to the given mapping
     * function and it's result will be wrapped into an Optional. If the JSON does not contain a node with the specified
     * name an empty Optional will be returned.
     *
     * @param json      Base JSON object used for parsing
     * @param fieldName Name of the top-level node to be deserialized
     * @param mapping   Mapping function returning the deserialized object of type <b>U</b>
     * @param <U>       The type of object that the JSON node should be mapped to
     *
     * @return Optional containing the result of deserializing the sub-node element or empty Optional if no node with
     *         the given name exists on top-level of this JSON object.
     *
     * @throws IOException If an IO error occurred during the deserialization of the given JSON object
     */
    private static <U> Optional<U> parseNode(JsonNode json, String fieldName,
            ThrowableFunctionalInterfaces.Function<JsonNode, U, IOException> mapping) throws IOException {
        return json.has(fieldName) ? Optional.of(mapping.apply(json.get(fieldName))) : Optional.empty();
    }

    @Override
    public APIResponse<T> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode json = mapper.readTree(jsonParser);

        T data = parseNode(json, "data", dataFunction::apply).orElse(null);

        Optional<String> status = parseNode(json, "status", String.class);

        return new APIResponseDTO.Builder<T>().data(data).status(status.get()).build();
    }

    /**
     * Checks if the specified field exists in the given JSON. If so, the node will be deserialized based on the given
     * type class. The result of the deserialization will be wrapped into an Optional. If the JSON does not contain a
     * node with the specified name an empty Optional will be returned.
     *
     * @param json      Base JSON object used for parsing
     * @param fieldName Name of the top-level node to be deserialized
     * @param valueType The class of the type of object that the JSON node should be mapped to
     * @param <U>       The type of object that the JSON node should be mapped to
     *
     * @return Optional containing the result of deserializing the sub-node element or empty Optional if no node with
     *         the given name exists on top-level of this JSON object.
     *
     * @throws IOException If an IO error occurred during the deserialization of the given JSON object
     */
    private <U> Optional<U> parseNode(JsonNode json, String fieldName, Class<U> valueType) throws IOException {
        return parseNode(json, fieldName, node -> mapper.readValue(node.toString(), valueType));
    }
}

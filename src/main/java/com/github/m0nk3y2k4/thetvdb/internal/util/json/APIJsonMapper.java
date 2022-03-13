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

package com.github.m0nk3y2k4.thetvdb.internal.util.json;

import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_JSON_PROCESSING_ERROR;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.api.model.data.EntityTranslation;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Translated;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.annotation.APIDataModel;
import com.github.m0nk3y2k4.thetvdb.internal.util.functional.ThrowableFunctionalInterfaces;
import com.github.m0nk3y2k4.thetvdb.internal.util.json.deser.APIResponseDeserializer;
import com.github.m0nk3y2k4.thetvdb.internal.util.json.deser.CollectionDeserializerModifier;
import com.github.m0nk3y2k4.thetvdb.internal.util.json.deser.StaticTypeReference;
import org.atteo.classindex.ClassFilter;
import org.atteo.classindex.ClassIndex;

/**
 * Utility class for JSON response deserialization.
 * <p><br>
 * Provides functionality to parse the JSON data returned by the remote API and map it into the corresponding DTO. These
 * DTO's will be wrapped into {@link APIResponse APIResponse&lt;DTO&gt;} objects together with additional information
 * like processing status information.
 */
public final class APIJsonMapper {

    /** Module used to extend the object mappers functionality in terms of mapping the APIs data model interfaces */
    private static final SimpleModule DATA_MODULE = createDataModule();

    /** Module used to extend the object mappers functionality in terms of mapping JDK8 Optionals */
    private static final Module JDK8_MODULE = new Jdk8Module();

    private APIJsonMapper() {}      // Private constructor. Only static methods

    /**
     * Maps some <i>TheTVDB.com</i> API response JSON into it's Java model representation.
     *
     * @param json          The full JSON as returned by the remote service
     * @param typeReference Type reference representing the Java model structure to which the JSON should be mapped to
     * @param <T>           The DTO type to which the JSON's {@code data} node should be mapped to
     *
     * @return Extended API response containing the requested data parsed from the given JSON as well as additional
     *         status information.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static <T> APIResponse<T> readValue(@Nonnull JsonNode json,
            @Nonnull TypeReference<APIResponse<T>> typeReference) throws APIException {
        try {
            return new ObjectMapper()
                    .registerModule(createAPIResponseModule(((ParameterizedType)typeReference.getType())
                            .getActualTypeArguments()[0]))
                    .readValue(json.toString(), typeReference);
        } catch (JsonProcessingException | IllegalArgumentException ex) {
            throw new APIException(String.format(API_JSON_PROCESSING_ERROR, ex.getMessage()), ex);
        }
    }

    /**
     * Serializes the given object into its JSON string representation.
     *
     * @param object The object to be serialized into a JSON string
     *
     * @return JSON string representation of the given object
     *
     * @throws APIException If problems were encountered while serializing (parsing, generating) the given object
     */
    public static String writeValue(Object object) throws APIException {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            throw new APIException(String.format(API_JSON_PROCESSING_ERROR, ex.getMessage()), ex);
        }
    }

    /**
     * Creates a JSON module with enhanced functionality regarding the deserialization of <i>TheTVDB.com</i> API JSON
     * responses. This includes the creation of a new API response deserializer for the given DTO type which also
     * supports the usage of Java {@code Optional} data types. Unknown properties within the JSON's {@code data} node
     * will be ignored by default.
     *
     * @param dataType DTO type to which the content of the JSON's {@code data} node should be parsed to
     *
     * @return JSON module with enhanced functionality for parsing JSON responses received from the remote service
     */
    private static Module createAPIResponseModule(@Nonnull Type dataType) {
        ThrowableFunctionalInterfaces.Function<JsonNode, ?, IOException> dataFunction = dataNode ->
                new ObjectMapper().registerModules(JDK8_MODULE, DATA_MODULE)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                        .readValue(dataNode.toString(), new StaticTypeReference<>(dataType));
        return new SimpleModule().addDeserializer(APIResponse.class, new APIResponseDeserializer<>(dataFunction));
    }

    /**
     * Creates a JSON module with enhanced functionality regarding the deserialization of the JSON's {@code data} node.
     * This includes proper handling of nullable collections and well as type resolver mappings for DTO model
     * interfaces. The latter will automatically be resolved via the {@link APIDataModel} annotation.
     *
     * @param <T> the API model interface type, used for adding type resolver mappings
     *
     * @return JSON module with enhanced functionality for parsing the actual response DTO models
     */
    @SuppressWarnings("unchecked")  // dto implements dtoInterface
    private static <T> SimpleModule createDataModule() {
        // Add Interface <-> Implementation mappings to the module. The object mapper will use these mappings to determine the
        // proper builder to be used to create new instances of a specific interface (via @JsonDeserialize annotation).
        SimpleAbstractTypeResolver dtoTypeResolver = new SimpleAbstractTypeResolver()
                .addMapping(Translated.class, EntityTranslation.class); // Use EntityTranslation as default implementation
        ClassFilter.only().classes().annotatedWith(JsonDeserialize.class)
                .from(ClassIndex.getAnnotated(APIDataModel.class)).forEach(dto ->
                        Arrays.stream(dto.getInterfaces()).forEach(dtoInterface ->
                                dtoTypeResolver.addMapping((Class<T>)dtoInterface, (Class<? extends T>)dto)));

        SimpleModule dataModule = new SimpleModule();
        dataModule.setDeserializerModifier(new CollectionDeserializerModifier()).setAbstractTypes(dtoTypeResolver);
        return dataModule;
    }
}

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
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIRuntimeException;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Alias;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Artwork;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ArtworkType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Character;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Episode;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Franchise;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Genre;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Movie;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Network;
import com.github.m0nk3y2k4.thetvdb.api.model.data.People;
import com.github.m0nk3y2k4.thetvdb.api.model.data.RemoteId;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Season;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Series;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesAirsDays;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Status;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Trailer;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.APIResponseDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.AliasDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ArtworkDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ArtworkTypeDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.CharacterDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.EpisodeDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.FranchiseDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.GenreDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.NetworkDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.PeopleDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.RemoteIdDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeasonDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesAirsDaysDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesDetailsDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.StatusDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.TrailerDTO;
import com.github.m0nk3y2k4.thetvdb.internal.util.functional.ThrowableFunctionalInterfaces;
import com.github.m0nk3y2k4.thetvdb.internal.util.json.deser.CollectionDeserializerModifier;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

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

    /** Module used to extend the object mappers functionality in terms of mapping JDK8 Optionals */
    private static final Module JDK8_MODULE = new Jdk8Module();

    static {
        // Add Interface <-> Implementation mappings to the module. The object mapper will use these mappings to determine the
        // proper builder to be used to create new instances of a specific interface (via @JsonDeserialize annotation).
        DATA_MODULE.setDeserializerModifier(new CollectionDeserializerModifier())
                .setAbstractTypes(new SimpleAbstractTypeResolver()
                        .addMapping(Alias.class, AliasDTO.class)
                        .addMapping(Artwork.class, ArtworkDTO.class)
                        .addMapping(ArtworkType.class, ArtworkTypeDTO.class)
                        .addMapping(Character.class, CharacterDTO.class)
                        .addMapping(Episode.class, EpisodeDTO.class)
                        .addMapping(Franchise.class, FranchiseDTO.class)
                        .addMapping(Genre.class, GenreDTO.class)
                        .addMapping(Movie.class, MovieDTO.class)
                        .addMapping(Network.class, NetworkDTO.class)
                        .addMapping(People.class, PeopleDTO.class)
                        .addMapping(RemoteId.class, RemoteIdDTO.class)
                        .addMapping(Season.class, SeasonDTO.class)
                        .addMapping(SeriesAirsDays.class, SeriesAirsDaysDTO.class)
                        .addMapping(SeriesDetails.class, SeriesDetailsDTO.class)
                        .addMapping(Series.class, SeriesDTO.class)
                        .addMapping(Status.class, StatusDTO.class)
                        .addMapping(Trailer.class, TrailerDTO.class)
                );
    }

    private JsonDeserializer() {}     // Private constructor. Only static methods

    /**
     * Maps the actual data of a specific artwork base record returned by the artwork route.
     *
     * @param json The full JSON as returned by the remote service, containing the artwork information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing the artwork data parsed from the given JSON as well as additional status
     *         information.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<Artwork> mapArtwork(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual list of artwork types returned by the artwork types overview route.
     *
     * @param json The full JSON as returned by the remote service, containing the artwork type overview information
     *             within the <em>{@code data}</em> node
     *
     * @return Extended API response containing the artwork type data parsed from the given JSON as well as additional
     *         status information.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<List<ArtworkType>> mapArtworkTypesOverview(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual data of a specific character record returned by the characters route.
     *
     * @param json The full JSON as returned by the remote service, containing the character information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing the character data parsed from the given JSON as well as additional
     *         status information.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<Character> mapCharacter(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual data of a specific episode base record returned by the episodes route.
     *
     * @param json The full JSON as returned by the remote service, containing the episode information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing the episode data parsed from the given JSON as well as additional status
     *         information.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<Episode> mapEpisode(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual data of a specific genre base record returned by the genres route.
     *
     * @param json The full JSON as returned by the remote service, containing the genre information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing the genre data parsed from the given JSON as well as additional status
     *         information.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<Genre> mapGenre(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual list of genres returned by the genres overview route.
     *
     * @param json The full JSON as returned by the remote service, containing the genres overview information within
     *             the <em>{@code data}</em> node
     *
     * @return Extended API response containing the genres data parsed from the given JSON as well as additional status
     *         information.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<List<Genre>> mapGenresOverview(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual data of a specific movie base record returned by the movies route.
     *
     * @param json The full JSON as returned by the remote service, containing the movie information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing the movie data parsed from the given JSON as well as additional status
     *         information.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<Movie> mapMovie(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual data of a specific people base record returned by the people route.
     *
     * @param json The full JSON as returned by the remote service, containing the people information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing the people data parsed from the given JSON as well as additional status
     *         information.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<People> mapPeople(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual data of a specific season base record returned by the seasons route.
     *
     * @param json The full JSON as returned by the remote service, containing the season information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing the season data parsed from the given JSON as well as additional status
     *         information.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<Season> mapSeason(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual data of a specific series base record returned by the series route.
     *
     * @param json The full JSON as returned by the remote service, containing the series information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing the series data parsed from the given JSON as well as additional status
     *         information.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<Series> mapSeries(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual data of a specific series extended record returned by the series route.
     *
     * @param json The full JSON as returned by the remote service, containing the series information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing the series data parsed from the given JSON as well as additional status
     *         information.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<SeriesDetails> mapSeriesDetails(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual list of series returned by the series overview route.
     *
     * @param json The full JSON as returned by the remote service, containing the series overview information within
     *             the <em>{@code data}</em> node
     *
     * @return Extended API response containing the series data parsed from the given JSON as well as additional status
     *         information.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<List<Series>> mapSeriesOverview(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
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
        try {
            return new ObjectMapper().registerModule(createFunctionalModule(typeReference))
                    .readValue(json.toString(), typeReference);
        } catch (JsonProcessingException ex) {
            throw new APIException(String.format(API_JSON_PARSE_ERROR, ex.getMessage()), ex);
        }
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
        return new SimpleModule()
                .addDeserializer(APIResponse.class, new FunctionalDeserializer<>(createDataFunction(typeReference)));
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
        return new ObjectMapper().registerModules(JDK8_MODULE, DATA_MODULE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(dataNode.toString(), dataTypeReference);
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

    /** Mapper used for parsing the API response JSON */
    private final ObjectMapper mapper = new ObjectMapper();

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

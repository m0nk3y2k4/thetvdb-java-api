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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonParser;
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
import com.github.m0nk3y2k4.thetvdb.api.model.data.Actor;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Episode;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Image;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ImageQueryParameter;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ImageSummary;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Language;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Movie;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Rating;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Series;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesSearchResult;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesSummary;
import com.github.m0nk3y2k4.thetvdb.api.model.data.User;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.APIResponseDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ActorDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.EpisodeDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ImageDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ImageQueryParameterDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ImageSummaryDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.LanguageDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.RatingDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesSearchResultDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesSummaryDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.UserDTO;
import com.github.m0nk3y2k4.thetvdb.internal.util.functional.ThrowableFunctionalInterfaces;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

/**
 * Utility class for JSON response deserialization.
 * <p><br>
 * Provides functionality to parse the JSON data returned by the remote API and map it into it's corresponding DTO.
 * These DTO's will be wrapped into {@link APIResponse APIResponse&lt;DTO&gt;} objects together with additional
 * information like advanced error reports or pagination information. Note that the latter depends on the invoked remote
 * route and will not always be available. Simple return types will not be mapped into a dedicated DTO class but will
 * use the corresponding Java native data type. For example, query parameters, which are a simple collection of strings,
 * will be mapped into a list of {@link String} whereas simple key/value paris will be returned as {@link Map
 * Map&lt;String, String&gt;}.
 */
public final class JsonDeserializer {

    /** Module used to extend the object mappers functionality in terms of mapping the APIs data model interfaces */
    private static final SimpleModule DATA_MODULE = new SimpleModule();

    static {
        // Add Interface <-> Implementation mappings to the module. The object mapper will use these mappings to determine the
        // proper builder to be used to create new instances of a specific interface (via @JsonDeserialize annotation).
        DATA_MODULE.setAbstractTypes(new SimpleAbstractTypeResolver()
                .addMapping(SeriesSearchResult.class, SeriesSearchResultDTO.class)
                .addMapping(Series.class, SeriesDTO.class)
                .addMapping(Episode.class, EpisodeDTO.class)
                .addMapping(Language.class, LanguageDTO.class)
                .addMapping(Movie.class, MovieDTO.class)
                .addMapping(Movie.Artwork.class, MovieDTO.ArtworkDTO.class)
                .addMapping(Movie.Genre.class, MovieDTO.GenreDTO.class)
                .addMapping(Movie.ReleaseDate.class, MovieDTO.ReleaseDateDTO.class)
                .addMapping(Movie.RemoteId.class, MovieDTO.RemoteIdDTO.class)
                .addMapping(Movie.Trailer.class, MovieDTO.TrailerDTO.class)
                .addMapping(Movie.Translation.class, MovieDTO.TranslationDTO.class)
                .addMapping(Movie.People.class, MovieDTO.PeopleDTO.class)
                .addMapping(Actor.class, ActorDTO.class)
                .addMapping(SeriesSummary.class, SeriesSummaryDTO.class)
                .addMapping(ImageQueryParameter.class, ImageQueryParameterDTO.class)
                .addMapping(ImageSummary.class, ImageSummaryDTO.class)
                .addMapping(Image.class, ImageDTO.class)
                .addMapping(Rating.class, RatingDTO.class)
                .addMapping(User.class, UserDTO.class)
        );
    }

    private JsonDeserializer() {}     // Private constructor. Only static methods

    /**
     * Maps the actual parameters returned by the various routes responsible for providing a list of available query
     * parameters to be used for certain queryable requests.
     * <p><br>
     * Note: Some routes will return the query parameters directly within the <em>{@code data}</em> node whereas for
     * other routes the parameters may be located in a nested <em>{@code params}</em> sub-node. If such a nested node is
     * present, the parameters will be parsed from this node. Otherwise the parameters will be parsed from the
     * <em>{@code data}</em> node.
     *
     * @param json The full JSON as returned by the remote service, containing a list of query parameters in either the
     *             <em>{@code data}</em> node or a nested <em>{@code params}</em> sub-node
     *
     * @return Extended API response containing a list of query parameters parsed from the given JSON. The returned
     *         object typically doesn't contain any additional error and paging information as such data is usually not
     *         available for remote routes used in conjunction with this method.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<List<String>> mapQueryParameters(@Nonnull JsonNode json) throws APIException {
        if (getData(json).has("params")) {
            // Sometimes the parameters are nested in an sub-node
            Function<JsonNode, List<String>> dataFunction =
                    dataNode -> StreamSupport.stream(dataNode.get("params").spliterator(), false)
                            .map(JsonNode::asText).collect(Collectors.toList());
            return mapObject(json, new TypeReference<>() {}, createFunctionalModule(dataFunction));
        }

        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual favorites returned by the various routes responsible for adding, deleting and obtaining user
     * favorites.
     * <p><br>
     * Note: In case the user has no favorites it is possible that some routes will not return an empty, nested
     * <em>{@code favorites}</em> sub-node but an empty <em>{@code data}</em> node instead. Such responses will be
     * handled fail-safe by this method, resulting in response containing an empty list being returned.
     *
     * @param json The full JSON as returned by the remote service, containing a list of favorites within the <em>{@code
     *             favorites}</em> sub-node
     *
     * @return Extended API response containing a list of favorites parsed from the given JSON as well as optional,
     *         additional error information. The returned object typically doesn't contain any additional paging
     *         information as such data is usually not available for remote routes used in conjunction with this
     *         method.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<List<String>> mapFavorites(@Nonnull JsonNode json) throws APIException {
        if (getData(json).has("favorites")) {
            // If the user has no favorites just an empty data-node might be returned
            Function<JsonNode, List<String>> dataFunction =
                    dataNode -> StreamSupport.stream(dataNode.get("favorites").spliterator(), false)
                            .map(JsonNode::asText).collect(Collectors.toList());
            return mapObject(json, new TypeReference<>() {}, createFunctionalModule(dataFunction));
        }

        return mapObject(json, new TypeReference<>() {}, createFunctionalModule(Collections::emptyList));
    }

    /**
     * Maps the header information for a specific series returned by the remote series HEAD route. The header
     * information properties will be represented by the key/value pairs of the returned API responses data map.
     *
     * @param json The full JSON as returned by the remote service, containing key/value pairs of header information
     *
     * @return Map containing header information key/value pairs
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<Map<String, String>> mapSeriesHeader(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual search results returned by the series search route.
     *
     * @param json The full JSON as returned by the remote service, containing a list of matching search results within
     *             the <em>{@code data}</em> node
     *
     * @return Extended API response containing a list of search results parsed from the given JSON. The returned object
     *         typically doesn't contain any additional error or paging information as such data is usually not
     *         available for remote routes used in conjunction with this method.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<List<SeriesSearchResult>> mapSeriesSearchResult(@Nonnull JsonNode json)
            throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual series data returned by the various routes responsible for fetching or filtering series.
     *
     * @param json The full JSON as returned by the remote service, containing the matching series information within
     *             the <em>{@code data}</em> node
     *
     * @return Extended API response containing the series data parsed from the given JSON as well as optional,
     *         additional error information. The returned object typically doesn't contain any additional paging
     *         information as such data is usually not available for remote routes used in conjunction with this
     *         method.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<Series> mapSeries(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual episode data of a specific episode returned by the episodes route.
     *
     * @param json The full JSON as returned by the remote service, containing the episode information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing the episode data parsed from the given JSON as well as optional,
     *         additional error information. The returned object typically doesn't contain any additional paging
     *         information as such data is usually not available for remote routes used in conjunction with this
     *         method.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<Episode> mapEpisode(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual episode data returned by the various routes responsible for fetching and querying episodes.
     * <p><br>
     * The remote API will only return 100 matching records at most. Additional records may be fetched by using the
     * paging information which may be accessed via the returned API response object (see {@link
     * APIResponse#getLinks()}).
     *
     * @param json The full JSON as returned by the remote service, containing a list of episode information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing a list of episodes parsed from the given JSON as well as optional,
     *         additional error and paging information.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<List<Episode>> mapEpisodes(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual languages returned by the API route responsible for providing a list of available languages.
     *
     * @param json The full JSON as returned by the remote service, containing a list of available languages within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing a list of available languages parsed from the given JSON. The returned
     *         object typically doesn't contain any additional error or paging information as such data is usually not
     *         available for remote routes used in conjunction with this method.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<List<Language>> mapLanguages(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual language data returned by the API route responsible for obtaining advanced information about a
     * specific language.
     *
     * @param json The full JSON as returned by the remote service, containing the language information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing the language data parsed from the given JSON. The returned object
     *         typically doesn't contain any additional error or paging information as such data is usually not
     *         available for remote routes used in conjunction with this method.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<Language> mapLanguage(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual movie data returned by the movies route.
     *
     * @param json The full JSON as returned by the remote service, containing the movie information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing the movie data parsed from the given JSON. The returned object typically
     *         doesn't contain any additional error or paging information as such data is usually not available for
     *         remote routes used in conjunction with this method.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<Movie> mapMovie(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual updated movies ID's returned by the movie updates route.
     * <p><br>
     * Note: This seems to be the only route that doesn't return its content wrapped into a "data" node but into a
     * "movies" node instead. In order to be able to reuse the general parsing logic this method will remap the content
     * of the given JSON "movies" node into a new objects "data" node.
     *
     * @param json The full JSON as returned by the remote service, containing the updated movies ID's within the
     *             <em>{@code movies}</em> node
     *
     * @return Extended API response containing the updated movies ID's parsed from the given JSON. The returned object
     *         typically doesn't contain any additional error or paging information as such data is usually not
     *         available for remote routes used in conjunction with this method.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<List<Long>> mapMovieUpdates(@Nonnull JsonNode json) throws APIException {
        return mapObject(new ObjectMapper().createObjectNode()
                .set("data", json.get("movies")), new TypeReference<>() {});
    }

    /**
     * Maps the actual actor data returned by the series actors route.
     *
     * @param json The full JSON as returned by the remote service, containing a list of actor information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing a list of actors parsed from the given JSON as well as optional,
     *         additional error information. The returned object typically doesn't contain any additional paging
     *         information as such data is usually not available for remote routes used in conjunction with this
     *         method.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<List<Actor>> mapActors(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual summary data returned by the series episode summary route.
     *
     * @param json The full JSON as returned by the remote service, containing the series episode summary information
     *             within the <em>{@code data}</em> node
     *
     * @return Extended API response containing the episode summary data parsed from the given JSON. The returned object
     *         typically doesn't contain any additional error or paging information as such data is usually not
     *         available for remote routes used in conjunction with this method.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<SeriesSummary> mapSeriesSummary(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual parameters returned by the API route responsible for providing a list of available image query
     * parameters.
     *
     * @param json The full JSON as returned by the remote service, containing a list of image query parameters within
     *             the <em>{@code data}</em> node
     *
     * @return Extended API response containing a list of image query parameters parsed from the given JSON. The
     *         returned object typically doesn't contain any additional error or paging information as such data is
     *         usually not available for remote routes used in conjunction with this method.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<List<ImageQueryParameter>> mapImageQueryParameters(@Nonnull JsonNode json)
            throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual summary data returned by the series images route.
     *
     * @param json The full JSON as returned by the remote service, containing the series images information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing the series images data parsed from the given JSON. The returned object
     *         typically doesn't contain any additional error or paging information as such data is usually not
     *         available for remote routes used in conjunction with this method.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<ImageSummary> mapSeriesImageSummary(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual image data returned by the queryable series images route.
     *
     * @param json The full JSON as returned by the remote service, containing a list of image information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing a list of images parsed from the given JSON as well as optional,
     *         additional error information. The returned object typically doesn't contain any additional paging
     *         information as such data is usually not available for remote routes used in conjunction with this
     *         method.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<List<Image>> mapImages(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the recently updated series data returned by the queryable updated route.
     * <p><br>
     * The JSON data will be parsed into a map which keys represent the ID of the updated series while their the
     * corresponding values represent the actual date/time of the last update as epoch time.
     *
     * @param json The full JSON as returned by the remote service, containing key/value pairs of recently updated
     *             series within the <em>{@code data}</em> node
     *
     * @return Extended API response containing a map of last updated series parsed from the given JSON as well as
     *         optional, additional error information. The returned object typically doesn't contain any additional
     *         paging information as such data is usually not available for remote routes used in conjunction with this
     *         method.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<Map<Long, Long>> mapUpdates(@Nonnull JsonNode json) throws APIException {
        Function<JsonNode, Map<Long, Long>> dataFunction =
                dataNode -> StreamSupport.stream(dataNode.spliterator(), false)
                        .collect(Collectors.toMap(x -> x.get("id").asLong(), x -> x.get("lastUpdated").asLong()));
        return mapObject(json, new TypeReference<>() {}, createFunctionalModule(dataFunction));
    }

    /**
     * Maps the actual ratings returned by the various routes responsible for adding, querying and fetching user
     * ratings.
     *
     * @param json The full JSON as returned by the remote service, containing a list of rating information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing a list of ratings parsed from the given JSON as well as optional,
     *         additional paging information. The returned object typically doesn't contain any additional error
     *         information as such data is usually not available for remote routes used in conjunction with this
     *         method.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<List<Rating>> mapRatings(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

    /**
     * Maps the actual user data returned by the user route.
     *
     * @param json The full JSON as returned by the remote service, containing the user information within the
     *             <em>{@code data}</em> node
     *
     * @return Extended API response containing the user data parsed from the given JSON. The returned object typically
     *         doesn't contain any additional error or paging information as such data is usually not available for
     *         remote routes used in conjunction with this method.
     *
     * @throws APIException If an IO error occurred during the deserialization of the given JSON object
     */
    public static APIResponse<User> mapUser(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>() {});
    }

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
    private static <T> T mapDataObject(@Nonnull JsonNode dataNode, @Nonnull DataTypeReference<T> dataTypeReference)
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
        } catch (IOException ex) {
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
        APIRESPONSE_MODULE.setAbstractTypes(new SimpleAbstractTypeResolver()
                .addMapping(APIResponse.Errors.class, APIResponseDTO.ErrorsDTO.class)
                .addMapping(APIResponse.Links.class, APIResponseDTO.LinksDTO.class)
        );
    }

    /** Mapper used to read the "errors" and "links" JSON nodes */
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

    @Override
    public APIResponse<T> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode json = mapper.readTree(p);

        T data = parseNode(json, "data", dataFunction::apply).orElse(null);
        Optional<APIResponse.Errors> errors = parseNode(json, "errors", APIResponse.Errors.class);
        Optional<APIResponse.Links> links = parseNode(json, "links", APIResponse.Links.class);

        return new APIResponseDTO.Builder<T>().data(data).errors(errors).links(links).build();
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
    private <U> Optional<U> parseNode(JsonNode json, String fieldName,
            ThrowableFunctionalInterfaces.Function<JsonNode, U, IOException> mapping) throws IOException {
        return json.has(fieldName) ? Optional.of(mapping.apply(json.get(fieldName))) : Optional.empty();
    }
}

package com.github.m0nk3y2k4.thetvdb.internal.util.json;

import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_JSON_PARSE_ERROR;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.RatingDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesSearchResultDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesSummaryDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.UserDTO;
import com.github.m0nk3y2k4.thetvdb.internal.util.functional.ThrowableFunctionalInterfaces;
import com.github.m0nk3y2k4.thetvdb.internal.util.validation.Parameters;

public final class JsonDeserializer {

    /** Object mapper module used to extend the mappers functionality in terms of properly mapping the APIs interfaces */
    private static final SimpleModule DEFAULT_MODULE = new SimpleModule();

    static {
        // Add Interface <-> Implementation mappings to the module. The object mapper will use these mappings to
        // determine and instantiate the proper implementation class for a specific interface.
        DEFAULT_MODULE.setAbstractTypes(new SimpleAbstractTypeResolver()
                .addMapping(SeriesSearchResult.class, SeriesSearchResultDTO.class)
                .addMapping(Series.class, SeriesDTO.class)
                .addMapping(Episode.class, EpisodeDTO.class)
                .addMapping(Language.class, LanguageDTO.class)
                .addMapping(Actor.class, ActorDTO.class)
                .addMapping(SeriesSummary.class, SeriesSummaryDTO.class)
                .addMapping(ImageQueryParameter.class, ImageQueryParameterDTO.class)
                .addMapping(ImageSummary.class, ImageSummaryDTO.class)
                .addMapping(Image.class, ImageDTO.class)
                .addMapping(Rating.class, RatingDTO.class)
                .addMapping(User.class, UserDTO.class)
                .addMapping(APIResponse.class, APIResponseDTO.class)
                .addMapping(APIResponse.JSONErrors.class, APIResponseDTO.JSONErrorsDTO.class)
                .addMapping(APIResponse.Links.class, APIResponseDTO.LinksDTO.class)
        );
    }

    /**
     * A specific type reference for the JSON's <em>{@code data}</em> node.
     * <p><br>
     * Objects of this class provide access to T's type argument. For example, if T represents a class
     * <em>{@code APIResponseDTO&lt;Episode&gt;}</em>, so an API response whose actual payload/data is an episode,
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
            Parameters.validateCondition(ref -> ParameterizedType.class.isAssignableFrom(ref.getType().getClass()), baseTypeReference,
                    new APIRuntimeException("Base type is required to be parameterized!"));
            this.typeSupplier = () -> ((ParameterizedType)baseTypeReference.getType()).getActualTypeArguments()[0];
        }

        @Override
        public Type getType() {
            return typeSupplier.get();
        }
    }

    private JsonDeserializer() {}     // Private constructor. Only static methods

    public static APIResponse<List<String>> mapQueryParameters(@Nonnull JsonNode json) throws APIException {
        if (getData(json).has("params")) {
            // Sometimes the parameters are nested in an sub-node
            Function<JsonNode, List<String>> dataFunction =
                    node -> StreamSupport.stream(getData(node).get("params").spliterator(), false)
                            .map(JsonNode::asText).collect(Collectors.toList());
            return mapObject(json, new TypeReference<>(){}, createFunctionalModule(dataFunction));
        }

        return mapObject(json, new TypeReference<>(){});
    }

    public static APIResponse<List<String>> mapFavorites(@Nonnull JsonNode json) throws APIException {
        if (getData(json).has("favorites")) {
            // If the user has no favorites just an empty data-node might be returned
            Function<JsonNode, List<String>> dataFunction =
                    node -> StreamSupport.stream(getData(node).get("favorites").spliterator(), false)
                            .map(JsonNode::asText).collect(Collectors.toList());
            return mapObject(json, new TypeReference<>() {}, createFunctionalModule(dataFunction));
        }

        return mapObject(json, new TypeReference<>(){}, createFunctionalModule(Collections::emptyList));
    }

    public static Map<String, String> mapSeriesHeader(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>(){});
    }

    public static APIResponse<List<SeriesSearchResult>> mapSeriesSearchResult(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>(){});
    }

    public static APIResponse<Series> mapSeries(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>(){});
    }

    public static APIResponse<Episode> mapEpisode(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>(){});
    }

    public static APIResponse<List<Episode>> mapEpisodes(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>(){});
    }

    public static APIResponse<List<Language>> mapLanguages(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>(){});
    }

    public static APIResponse<Language> mapLanguage(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>(){});
    }

    public static APIResponse<List<Actor>> mapActors(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>(){});
    }

    public static APIResponse<SeriesSummary> mapSeriesSummary(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>(){});
    }

    public static APIResponse<List<ImageQueryParameter>> mapImageQueryParameters(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>(){});
    }

    public static APIResponse<ImageSummary> mapSeriesImageSummary(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>(){});
    }

    public static APIResponse<List<Image>> mapImages(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>(){});
    }

    public static APIResponse<Map<Long, Long>> mapUpdates(@Nonnull JsonNode json) throws APIException {
        Function<JsonNode, Map<Long, Long>> dataFunction =
                node -> StreamSupport.stream(getData(node).spliterator(), false)
                        .collect(Collectors.toMap(x -> x.get("id").asLong(), x -> x.get("lastUpdated").asLong()));
        return mapObject(json, new TypeReference<>(){}, createFunctionalModule(dataFunction));
    }

    public static APIResponse<List<Rating>> mapRatings(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>(){});
    }

    public static APIResponse<User> mapUser(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>(){});
    }

    private static JsonNode getData(@Nonnull JsonNode json) {
        return json.get("data");
    }

    private static <T> ThrowableFunctionalInterfaces.Function<JsonNode, T, IOException> createDataFunction(@Nonnull TypeReference<T> baseTypeReference) {
        return node -> mapDataObject(node, new DataTypeReference<>(baseTypeReference));
    }

    private static <T> Module createFunctionalModule(@Nonnull TypeReference<T> typeReference) {
        return createFunctionalModule(createDataFunction(typeReference));
    }

    private static <T> Module createFunctionalModule(@Nonnull Supplier<T> supplier) {
        return createFunctionalModule(ThrowableFunctionalInterfaces.Function.of(node -> supplier.get()));
    }

    private static <T> Module createFunctionalModule(@Nonnull Function<JsonNode, T> dataFunction) {
        return createFunctionalModule(ThrowableFunctionalInterfaces.Function.of(dataFunction));
    }

    private static <T> Module createFunctionalModule(@Nonnull ThrowableFunctionalInterfaces.Function<JsonNode, T, IOException> dataFunction) {
        return new SimpleModule().addDeserializer(APIResponse.class, new FunctionalDeserializer<>(dataFunction));
    }

    private static <T> T mapObject(@Nonnull JsonNode json, @Nonnull TypeReference<T> typeReference) throws APIException {
        return mapObject(json, typeReference, createFunctionalModule(typeReference));
    }

    private static <T> T mapDataObject(@Nonnull JsonNode json, @Nonnull DataTypeReference<T> dataTypeReference) throws IOException {
        return new ObjectMapper().registerModule(DEFAULT_MODULE).readValue(getData(json).toString(), dataTypeReference);
    }

    private static <T> T mapObject(@Nonnull JsonNode json, @Nonnull TypeReference<T> typeReference, @Nonnull Module module) throws APIException {
        try {
            return new ObjectMapper().registerModule(module).readValue(json.toString(), typeReference);
        } catch (IOException ex) {
            throw new APIException(API_JSON_PARSE_ERROR, ex);
        }
    }
}

class FunctionalDeserializer<T, X extends IOException> extends com.fasterxml.jackson.databind.JsonDeserializer<APIResponse<T>>{

    private final ThrowableFunctionalInterfaces.Function<JsonNode, T, X> dataFunction;

    FunctionalDeserializer(@Nonnull ThrowableFunctionalInterfaces.Function<JsonNode, T, X> dataFunction) {
        this.dataFunction = dataFunction;
    }

    @Override
    public APIResponse<T> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(p);

        APIResponseDTO.Builder<T> response = new APIResponseDTO.Builder<>();

        if (json.has("data")) {
            response.data(dataFunction.apply(json));
        }
        if (json.has("errors")) {
            response.errors(mapper.readValue(json.get("errors").toString(), APIResponseDTO.JSONErrorsDTO.class));
        }
        if (json.has("links")) {
            response.links(mapper.readValue(json.get("links").toString(), APIResponseDTO.LinksDTO.class));
        }

        return response.build();
    }
}

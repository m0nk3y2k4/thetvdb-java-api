package com.github.m0nk3y2k4.thetvdb.internal.util;

import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_JSON_PARSE_ERROR;

import java.io.IOException;
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
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.APIResponseImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ActorImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.EpisodeImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ImageImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ImageQueryParameterImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ImageSummaryImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.LanguageImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.RatingImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesSearchResultImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesSummaryImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.UserImpl;

public final class JsonDeserializer {

    /** Object mapper module used to extend the mappers functionality in terms of properly mapping the APIs interfaces */
    private static final SimpleModule DFAULT_MODULE = new SimpleModule();

    static {
        // Add Interface <-> Implementation mappings to the module. The object mapper will use these mappings to
        // determine and instantiate the proper implementation class for a specific interface.
        DFAULT_MODULE.setAbstractTypes(new SimpleAbstractTypeResolver()
                .addMapping(SeriesSearchResult.class, SeriesSearchResultImpl.class)
                .addMapping(Series.class, SeriesImpl.class)
                .addMapping(Episode.class, EpisodeImpl.class)
                .addMapping(Language.class, LanguageImpl.class)
                .addMapping(Actor.class, ActorImpl.class)
                .addMapping(SeriesSummary.class, SeriesSummaryImpl.class)
                .addMapping(ImageQueryParameter.class, ImageQueryParameterImpl.class)
                .addMapping(ImageSummary.class, ImageSummaryImpl.class)
                .addMapping(Image.class, ImageImpl.class)
                .addMapping(Rating.class, RatingImpl.class)
                .addMapping(User.class, UserImpl.class)
                .addMapping(APIResponse.class, APIResponseImpl.class)
                .addMapping(APIResponse.JSONErrors.class, APIResponseImpl.JSONErrorsImpl.class)
                .addMapping(APIResponse.Links.class, APIResponseImpl.LinksImpl.class)
        );
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

    private static <T> Module createFunctionalModule(@Nonnull Function<JsonNode, T> dataFunction) {
        return new SimpleModule().addDeserializer(APIResponse.class, new FunctionalDeserializer<>(dataFunction));
    }

    private static <T> Module createFunctionalModule(@Nonnull Supplier<T> supplier) {
        Function<JsonNode, T> dataFunction = node -> supplier.get();
        return new SimpleModule().addDeserializer(APIResponse.class, new FunctionalDeserializer<>(dataFunction));
    }

    private static <T> T mapObject(@Nonnull JsonNode json, @Nonnull TypeReference<T> typeReference) throws APIException {
        return mapObject(json, typeReference, DFAULT_MODULE);
    }

    private static <T> T mapObject(@Nonnull JsonNode json, @Nonnull TypeReference<T> typeReference, @Nonnull Module module) throws APIException {
        try {
            return new ObjectMapper().registerModule(module).readValue(json.toString(), typeReference);
        } catch (IOException ex) {
            throw new APIException(API_JSON_PARSE_ERROR, ex);
        }
    }
}

class FunctionalDeserializer<T> extends com.fasterxml.jackson.databind.JsonDeserializer<APIResponse<T>>{

    private final Function<JsonNode, T> dataFunction;

    FunctionalDeserializer(@Nonnull Function<JsonNode, T> dataFunction) {
        this.dataFunction = dataFunction;
    }

    @Override
    public APIResponse<T> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(p);

        APIResponseImpl<T> response = new APIResponseImpl<>();

        if (json.has("data")) {
            response.setData(dataFunction.apply(json));
        }
        if (json.has("errors")) {
            response.setErrors(mapper.readValue(json.get("errors").toString(), APIResponseImpl.JSONErrorsImpl.class));
        }
        if (json.has("links")) {
            response.setLinks(mapper.readValue(json.get("links").toString(), APIResponseImpl.LinksImpl.class));
        }

        return response;
    }
}

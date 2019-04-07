package com.github.m0nk3y2k4.thetvdb.internal.util;

import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_JSON_PARSE_ERROR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
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
    private static final SimpleModule MODULE = new SimpleModule();

    static {
        // Add Interface <-> Implementation mappings to the module. The object mapper will use these mappings to
        // determine and instantiate the proper implementation class for a specific interface.
        MODULE.setAbstractTypes(new SimpleAbstractTypeResolver()
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
        );
    }

    private JsonDeserializer() {}     // Private constructor. Only static methods

    public static List<String> mapQueryParameters(@Nonnull JsonNode json) throws APIException {
        JsonNode dataNode = getData(json);
        JsonNode params = dataNode.has("params") ? dataNode.get("params") : dataNode;   // Sometimes the parameters are nested in an sub-node
        return mapObject(params, new TypeReference<>(){});
    }

    public static List<String> mapFavorites(@Nonnull JsonNode json) throws APIException {
        return mapObject(getData(json).get("favorites"), new TypeReference<>(){});
    }

    public static Map<String, String> mapSeriesHeader(@Nonnull JsonNode json) throws APIException {
        return mapObject(json, new TypeReference<>(){});
    }

    public static List<SeriesSearchResult> mapSeriesSearchResult(@Nonnull JsonNode json) throws APIException {
        return mapObjects(getData(json), SeriesSearchResult.class);
    }

    public static Series mapSeries(@Nonnull JsonNode json) throws APIException {
        return mapObject(getData(json), Series.class);
    }

    public static Episode mapEpisode(@Nonnull JsonNode json) throws APIException {
        return mapObject(getData(json), Episode.class);
    }

    public static List<Episode> mapEpisodes(@Nonnull JsonNode json) throws APIException {
        return mapObjects(getData(json), Episode.class);
    }

    public static List<Language> mapLanguages(@Nonnull JsonNode json) throws APIException {
        return mapObjects(getData(json), Language.class);
    }

    public static Language mapLanguage(@Nonnull JsonNode json) throws APIException {
        return mapObject(getData(json), Language.class);
    }

    public static List<Actor> mapActors(@Nonnull JsonNode json) throws APIException {
        return mapObjects(getData(json), Actor.class);
    }

    public static SeriesSummary mapSeriesSummary(@Nonnull JsonNode json) throws APIException {
        return mapObject(getData(json), SeriesSummary.class);
    }

    public static List<ImageQueryParameter> mapImageQueryParameters(@Nonnull JsonNode json) throws APIException {
        return mapObjects(getData(json), ImageQueryParameter.class);
    }

    public static ImageSummary mapSeriesImageSummary(@Nonnull JsonNode json) throws APIException {
        return mapObject(getData(json), ImageSummary.class);
    }

    public static List<Image> mapImages(@Nonnull JsonNode json) throws APIException {
        return mapObjects(getData(json), Image.class);
    }

    public static Map<Long, Long> mapUpdates(@Nonnull JsonNode json) {
        return StreamSupport.stream(getData(json).spliterator(), false).collect(Collectors.toMap(x -> x.get("id").asLong(), x -> x.get("lastUpdated").asLong()));
    }

    public static List<Rating> mapRatings(@Nonnull JsonNode json) throws APIException {
        return mapObjects(getData(json), Rating.class);
    }

    public static User mapUser(@Nonnull JsonNode json) throws APIException {
        return mapObject(getData(json), User.class);
    }

    private static JsonNode getData(@Nonnull JsonNode json) {
        return json.get("data");
    }

    private static <I, T extends I> List<I> mapObjects(@Nonnull JsonNode json, @Nonnull Class<T> clazz) throws APIException {
        List<I> objects = new ArrayList<>();

        for (JsonNode child : json) {
            objects.add(mapObject(child, clazz));
        }

        return objects;
    }

    private static <T> T mapObject(@Nonnull JsonNode json, @Nonnull Class<T> clazz) throws APIException {
        try {
            return new ObjectMapper().registerModule(MODULE).readValue(json.toString(), clazz);
        } catch (IOException ex) {
            throw new APIException(API_JSON_PARSE_ERROR, ex);
        }
    }

    private static <T> T mapObject(@Nonnull JsonNode json, @Nonnull TypeReference<T> typeReference) throws APIException {
        try {
            return new ObjectMapper().readValue(json.toString(), typeReference);
        } catch (IOException ex) {
            throw new APIException(API_JSON_PARSE_ERROR, ex);
        }
    }
}

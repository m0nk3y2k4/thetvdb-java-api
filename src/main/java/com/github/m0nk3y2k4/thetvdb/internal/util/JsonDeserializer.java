package com.github.m0nk3y2k4.thetvdb.internal.util;

import static com.github.m0nk3y2k4.thetvdb.api.exception.APIException.API_JSON_PARSE_ERROR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.api.model.Actor;
import com.github.m0nk3y2k4.thetvdb.api.model.Episode;
import com.github.m0nk3y2k4.thetvdb.api.model.EpisodeAbstract;
import com.github.m0nk3y2k4.thetvdb.api.model.Image;
import com.github.m0nk3y2k4.thetvdb.api.model.ImageQueryParameter;
import com.github.m0nk3y2k4.thetvdb.api.model.ImageSummary;
import com.github.m0nk3y2k4.thetvdb.api.model.Language;
import com.github.m0nk3y2k4.thetvdb.api.model.Rating;
import com.github.m0nk3y2k4.thetvdb.api.model.Series;
import com.github.m0nk3y2k4.thetvdb.api.model.SeriesAbstract;
import com.github.m0nk3y2k4.thetvdb.api.model.SeriesSummary;
import com.github.m0nk3y2k4.thetvdb.api.model.User;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.ActorImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.EpisodeImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.EpisodeAbstractImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.ImageImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.ImageQueryParameterImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.ImageSummaryImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.LanguageImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.RatingImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.SeriesImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.SeriesAbstractImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.SeriesSummaryImpl;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.UserImpl;

public final class JsonDeserializer {

    private JsonDeserializer() {}     // Private constructor. Only static methods

    public static List<String> createQueryParameters(@Nonnull JsonNode json) throws APIException {
        JsonNode dataNode = json.get("data");
        JsonNode params = dataNode.has("params") ? dataNode.get("params") : dataNode;   // Sometimes the parameters are nested in an sub-node
        return readValue(params, new TypeReference<List<String>>(){});
    }

    public static List<String> createFavorites(@Nonnull JsonNode json) throws APIException {
        return readValue(json.get("data").get("favorites"), new TypeReference<List<String>>(){});
    }

    public static Map<String, String> createSeriesHeader(@Nonnull JsonNode json) throws APIException {
        return readValue(json, new TypeReference<Map<String, String>>(){});
    }

    public static List<SeriesAbstract> createSeriesAbstract(@Nonnull JsonNode json) throws APIException {
        JsonNode dataNode = json.get("data");
        List<SeriesAbstract> seriesList = new ArrayList<>();

        for (JsonNode series : dataNode) {
            seriesList.add(readValue(series, SeriesAbstractImpl.class));
        }

        return seriesList;
    }

    public static Series createSeries(@Nonnull JsonNode json) throws APIException {
        return readValue(json.get("data"), SeriesImpl.class);
    }

    public static Episode createEpisode(@Nonnull JsonNode json) throws APIException {
        return readValue(json.get("data"), EpisodeImpl.class);
    }

    public static List<EpisodeAbstract> createEpisodeAbstract(@Nonnull JsonNode json) throws APIException {
        JsonNode dataNode = json.get("data");
        List<EpisodeAbstract> episodeList = new ArrayList<>();

        for (JsonNode episode : dataNode) {
            episodeList.add(readValue(episode, EpisodeAbstractImpl.class));
        }

        return episodeList;
    }

    public static List<Language> createLanguageList(@Nonnull JsonNode json) throws APIException {
        JsonNode dataNode = json.get("data");
        List<Language> languageList = new ArrayList<>();

        for (JsonNode language : dataNode) {
            languageList.add(readValue(language, LanguageImpl.class));
        }

        return languageList;
    }

    public static Language createLanguage(@Nonnull JsonNode json) throws APIException {
        return readValue(json.get("data"), LanguageImpl.class);
    }

    public static List<Actor> createActors(@Nonnull JsonNode json) throws APIException {
        JsonNode dataNode = json.get("data");
        List<Actor> actorList = new ArrayList<>();

        for (JsonNode actor : dataNode) {
            actorList.add(readValue(actor, ActorImpl.class));
        }

        return actorList;
    }

    public static SeriesSummary createSeriesSummary(@Nonnull JsonNode json) throws APIException {
        return readValue(json.get("data"), SeriesSummaryImpl.class);
    }

    public static List<ImageQueryParameter> createQueryParameterList(@Nonnull JsonNode json) throws APIException {
        JsonNode dataNode = json.get("data");
        List<ImageQueryParameter> queryParamList = new ArrayList<>();

        for (JsonNode queryParam : dataNode) {
            queryParamList.add(readValue(queryParam, ImageQueryParameterImpl.class));
        }

        return queryParamList;
    }

    public static ImageSummary createSeriesImageSummary(@Nonnull JsonNode json) throws APIException {
        return readValue(json.get("data"), ImageSummaryImpl.class);
    }

    public static List<Image> createImages(@Nonnull JsonNode json) throws APIException {
        JsonNode dataNode = json.get("data");
        List<Image> imageList = new ArrayList<>();

        for (JsonNode image : dataNode) {
            imageList.add(readValue(image, ImageImpl.class));
        }

        return imageList;
    }

    public static Map<Long, Long> createUpdated(@Nonnull JsonNode json) {
        JsonNode dataNode = json.get("data");
        Map<Long, Long> updates = new HashMap<>();

        for (JsonNode update : dataNode) {
            updates.put(update.get("id").asLong(), update.get("lastUpdated").asLong());
        }

        return updates;
    }

    public static List<Rating> createRatings(@Nonnull JsonNode json) throws APIException {
        JsonNode dataNode = json.get("data");
        List<Rating> ratingList = new ArrayList<>();

        for (JsonNode rating : dataNode) {
            ratingList.add(readValue(rating, RatingImpl.class));
        }

        return ratingList;
    }

    public static User createUser(@Nonnull JsonNode json) throws APIException {
        return readValue(json.get("data"), UserImpl.class);
    }

    private static <T> T readValue(@Nonnull JsonNode json, Class<T> clazz) throws APIException {
        try {
            return new ObjectMapper().readValue(json.toString(), clazz);
        } catch (IOException ex) {
            throw new APIException(API_JSON_PARSE_ERROR, ex);
        }
    }

    private static <T> T readValue(@Nonnull JsonNode json, TypeReference<T> typeReference) throws APIException {
        try {
            return new ObjectMapper().readValue(json.toString(), typeReference);
        } catch (IOException ex) {
            throw new APIException(API_JSON_PARSE_ERROR, ex);
        }
    }
}

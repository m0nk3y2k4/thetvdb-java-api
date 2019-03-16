package com.github.m0nk3y2k4.thetvdb.shared.util;

import static com.github.m0nk3y2k4.thetvdb.exception.APIException.API_JSON_PARSE_ERROR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.m0nk3y2k4.thetvdb.exception.APIException;
import com.github.m0nk3y2k4.thetvdb.shared.model.Actor;
import com.github.m0nk3y2k4.thetvdb.shared.model.Episode;
import com.github.m0nk3y2k4.thetvdb.shared.model.EpisodeAbstract;
import com.github.m0nk3y2k4.thetvdb.shared.model.Image;
import com.github.m0nk3y2k4.thetvdb.shared.model.ImageQueryParameter;
import com.github.m0nk3y2k4.thetvdb.shared.model.ImageSummary;
import com.github.m0nk3y2k4.thetvdb.shared.model.Language;
import com.github.m0nk3y2k4.thetvdb.shared.model.Rating;
import com.github.m0nk3y2k4.thetvdb.shared.model.Series;
import com.github.m0nk3y2k4.thetvdb.shared.model.SeriesAbstract;
import com.github.m0nk3y2k4.thetvdb.shared.model.SeriesSummary;
import com.github.m0nk3y2k4.thetvdb.shared.model.User;

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
            seriesList.add(readValue(series, SeriesAbstract.class));
        }

        return seriesList;
    }

    public static Series createSeries(@Nonnull JsonNode json) throws APIException {
        return readValue(json.get("data"), Series.class);
    }

    public static Episode createEpisode(@Nonnull JsonNode json) throws APIException {
        return readValue(json.get("data"), Episode.class);
    }

    public static List<EpisodeAbstract> createEpisodeAbstract(@Nonnull JsonNode json) throws APIException {
        JsonNode dataNode = json.get("data");
        List<EpisodeAbstract> episodeList = new ArrayList<>();

        for (JsonNode episode : dataNode) {
            episodeList.add(readValue(episode, EpisodeAbstract.class));
        }

        return episodeList;
    }

    public static List<Language> createLanguageList(@Nonnull JsonNode json) throws APIException {
        JsonNode dataNode = json.get("data");
        List<Language> languageList = new ArrayList<>();

        for (JsonNode language : dataNode) {
            languageList.add(readValue(language, Language.class));
        }

        return languageList;
    }

    public static Language createLanguage(@Nonnull JsonNode json) throws APIException {
        return readValue(json.get("data"), Language.class);
    }

    public static List<Actor> createActors(@Nonnull JsonNode json) throws APIException {
        JsonNode dataNode = json.get("data");
        List<Actor> actorList = new ArrayList<>();

        for (JsonNode actor : dataNode) {
            actorList.add(readValue(actor, Actor.class));
        }

        return actorList;
    }

    public static SeriesSummary createSeriesSummary(@Nonnull JsonNode json) throws APIException {
        return readValue(json.get("data"), SeriesSummary.class);
    }

    public static List<ImageQueryParameter> createQueryParameterList(@Nonnull JsonNode json) throws APIException {
        JsonNode dataNode = json.get("data");
        List<ImageQueryParameter> queryParamList = new ArrayList<>();

        for (JsonNode queryParam : dataNode) {
            queryParamList.add(readValue(queryParam, ImageQueryParameter.class));
        }

        return queryParamList;
    }

    public static ImageSummary createSeriesImageSummary(@Nonnull JsonNode json) throws APIException {
        return readValue(json.get("data"), ImageSummary.class);
    }

    public static List<Image> createImages(@Nonnull JsonNode json) throws APIException {
        JsonNode dataNode = json.get("data");
        List<Image> imageList = new ArrayList<>();

        for (JsonNode image : dataNode) {
            imageList.add(readValue(image, Image.class));
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
            ratingList.add(readValue(rating, Rating.class));
        }

        return ratingList;
    }

    public static User createUser(@Nonnull JsonNode json) throws APIException {
        return readValue(json.get("data"), User.class);
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

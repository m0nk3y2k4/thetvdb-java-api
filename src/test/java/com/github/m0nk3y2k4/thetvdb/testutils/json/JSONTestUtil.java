package com.github.m0nk3y2k4.thetvdb.testutils.json;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

/**
 * Provides access to prefabbed JSON data used for unit testing
 * <p><br>
 * Test utility class offering easy access to predefined JSON files and associated Java DTO's. Resource files and
 * DTO's are aligned in terms of content. In other words, the DTO is the Java data type representation of some JSON
 * resource file, for example:
 * <pre>{@code
 *     JsonParser errorsJSON = new JsonFactory().createParser(JSONTestUtil.getResource(ERRORS));    // JSON resource file
 *     JSONErrors errorsDTO = parse(errorsJSON);
 *
 *     assertThat(errorsDTO).isEqualTo(JSONTestUtil.errors());      // Resource file as Java object
 * }</pre>
 */
public final class JSONTestUtil {

    public enum JsonResource {
        EMPTY("empty", JSONTestUtil::empty, "Empty JSON response"),
        DATA("data", JSONTestUtil::data, "Full JSON response with data, errors and links node"),
        QUERYPARAMETERS("queryParameters", JSONTestUtil::queryParameters, "Query parameters JSON response"),
        QUERYPARAMETERS_NESTED("queryParameters_nested", JSONTestUtil::queryParameters, "Nested query parameters JSON response"),
        EPISODE("episode", JSONTestUtil::episode, "Episode JSON response"),
        LANGUAGE("language", JSONTestUtil::langauge, "Language JSON response"),
        LANGUAGES("languages", JSONTestUtil::langauges, "Languages JSON response"),
        SERIESSEARCH("seriesSearch", JSONTestUtil::seriesSearch, "Series search JSON response"),
        SERIES("series", JSONTestUtil::series, "Series JSON response"),
        SERIESHEADER("seriesHeader", JSONTestUtil::seriesHeader, "Series header JSON response"),
        ACTORS("actors", JSONTestUtil::actors, "Actors JSON response"),
        EPISODES("episodes", JSONTestUtil::episodes, "Episodes JSON response"),
        SERIESSUMMARY("seriesSummary", JSONTestUtil::seriesSummary, "Series summary JSON response"),
        IMAGESUMMARY("imageSummary", JSONTestUtil::imageSummary, "Image summary JSON respone"),
        IMAGES("images", JSONTestUtil::images, "Images JSON response"),
        IMAGEQUERYPARAMETERS("imageQueryParameters", JSONTestUtil::imageQueryParameters, "Image query parameters JSON response"),
        UPDATES("updates", JSONTestUtil::updates, "Updates JSON response"),
        USER("user", JSONTestUtil::user, "User JSON response"),
        FAVORITES("favorites", JSONTestUtil::favorites, "Favorites JSON response"),
        FAVORITES_EMPTY("favorites_empty", () -> new APIResponseDTO.Builder<List<String>>()
                .from(JSONTestUtil.favorites()).data(Collections.emptyList()).build(), "Empty favorites JSON response"),
        RATINGS("ratings", JSONTestUtil::ratings, "Ratings JSON response");

        private final String fileName;
        private final Supplier<?> dtoSupplier;
        private final String description;

        JsonResource(String fileName, Supplier<?> dtoSupplier, String description) {
            this.fileName = fileName;
            this.dtoSupplier = dtoSupplier;
            this.description = description;
        }

        /**
         * Creates a new JsonNode representation of this JSON resource
         *
         * @return This resource mapped as JsonNode object
         *
         * @throws IOException If a low-level I/O problem (unexpected end-of-input, network error) occurs
         */
        public JsonNode getJson() throws IOException {
            return new ObjectMapper().readTree(getUrl());
        }

        /**
         * Returns the uniform resource locator (URL) for this JSON resource
         *
         * @return URL referencing this JSON resource
         */
        public URL getUrl() {
            return ClassLoader.getSystemResource("json/example/" + fileName + ".json");
        }

        /**
         * Returns a DTO representation of this JSON resource
         *
         * @param <T> The actual type of the DTO
         *
         * @return DTO representation of this JSON resource
         */
        @SuppressWarnings("unchecked")
        public <T> T getDTO() {
            return (T)dtoSupplier.get();
        }

        @Override
        public String toString() {
            return description;
        }
    }

    /**
     * Creates a new JSONErrors DTO with default values set
     *
     * @return New JSONErrors DTO prefilled with default values
     */
    public static APIResponse.JSONErrors errors() {
        return new APIResponseDTO.JSONErrorsDTO.Builder()
                .addInvalidFilters("InvalidFilter1", "InvalidFilter2")
                .invalidLanguage("elvish")
                .addInvalidQueryParams("InvalidQueryParam1", "InvalidQueryParam2")
                .build();
    }

    /**
     * Creates a new Links DTO with default values set
     *
     * @return New Links DTO prefilled with default values
     */
    public static APIResponse.Links links() {
        return new APIResponseDTO.LinksDTO.Builder()
                .first(1)
                .last(7)
                .next(5)
                .previous(3)
                .build();
    }

    /**
     * Creates a new empty APIResponse DTO containing no data at all
     *
     * @return New empty APIResponse DTO
     */
    public static APIResponse<Data> empty() {
        return new APIResponseDTO.Builder<Data>().build();
    }

    /**
     * Creates a new full APIResponse DTO containing data, errors and links, all prefilled with default values
     *
     * @return New APIResponse DTO prefilled with default values
     */
    public static APIResponse<Data> data() {
        return new APIResponseDTO.Builder<Data>()
                .data(Data.with("Some content"))
                .errors(errors())
                .links(links())
                .build();
    }

    /**
     * Creates a new query parameters APIResponse DTO with default values set
     *
     * @return New query parameters APIResponse DTO prefilled with default values
     */
    public static APIResponse<List<String>> queryParameters() {
        return new APIResponseDTO.Builder<List<String>>()
                .data(List.of("QueryParameter1", "QueryParameter2"))
                .build();
    }

    /**
     * Creates a new favorites APIResponse DTO with default values set
     *
     * @return New favorites APIResponse DTO prefilled with default values
     */
    public static APIResponse<List<String>> favorites() {
        return new APIResponseDTO.Builder<List<String>>()
                .data(List.of("Favorite1", "Favorite2"))
                .errors(errors())
                .build();
    }

    /**
     * Creates a new series header map with default values set
     *
     * @return New series header map prefilled with default values
     */
    public static Map<String, String> seriesHeader() {
        return Map.of("Header1", "Value1", "Header2", "Value2");
    }

    /**
     * Creates a new series search result APIResponse DTO with default values set
     *
     * @return New series search result APIResponse DTO prefilled with default values
     */
    public static APIResponse<List<SeriesSearchResult>> seriesSearch() {
        return new APIResponseDTO.Builder<List<SeriesSearchResult>>()
                .data(List.of(
                        new SeriesSearchResultDTO.Builder()
                                .addAliases("Alias1", "Alias2")
                                .banner("Banner1")
                                .firstAired("FirstAired1")
                                .id(5478L)
                                .image("Image1")
                                .network("Network1")
                                .overview("Overview1")
                                .poster("Poster1")
                                .seriesName("SeriesName1")
                                .slug("Slug1")
                                .status("Status1")
                                .build(),
                        new SeriesSearchResultDTO.Builder()
                                .addAliases("Alias3", "Alias4")
                                .banner("Banner2")
                                .firstAired("FirstAired2")
                                .id(5479L)
                                .image("Image2")
                                .network("Network2")
                                .overview("Overview2")
                                .poster("Poster2")
                                .seriesName("SeriesName2")
                                .slug("Slug2")
                                .status("Status2")
                                .build()))
                .build();
    }

    /**
     * Creates a new series APIResponse DTO with default values set
     *
     * @return New series APIResponse DTO prefilled with default values
     */
    public static APIResponse<Series> series() {
        return new APIResponseDTO.Builder<Series>()
                .data(new SeriesDTO.Builder()
                        .added("Added")
                        .airsDayOfWeek("AirsDayOfWeek")
                        .airsTime("AirsTime")
                        .addAliases("Alias1", "Alias2")
                        .banner("Banner")
                        .firstAired("FirstAired")
                        .addGenre("Genre1", "Genre2")
                        .id(6421L)
                        .imdbId("ImdbId")
                        .lastUpdated(63254L)
                        .network("Network")
                        .networkId("NetworkId")
                        .overview("Overview")
                        .rating("Rating")
                        .runtime("Runtime")
                        .seriesId("SeriesId")
                        .seriesName("SeriesName")
                        .siteRating(5.0)
                        .siteRatingCount(654L)
                        .slug("Slug")
                        .status("Status")
                        .zap2itId("Zap2ItId")
                        .build())
                .errors(errors())
                .build();
    }

    /**
     * Creates a new episode APIResponse DTO with default values set
     *
     * @return New episode APIResponse DTO prefilled with default values
     */
    public static APIResponse<Episode> episode() {
        return new APIResponseDTO.Builder<Episode>()
                .data(new EpisodeDTO.Builder()
                        .absoluteNumber(69L)
                        .airedEpisodeNumber(12L)
                        .airedSeason(3L)
                        .airsAfterSeason(2L)
                        .airsBeforeEpisode(13L)
                        .airsBeforeSeason(4L)
                        .director("Director")
                        .addDirectors("Director1", "Director2")
                        .dvdChapter(9L)
                        .dvdDiscid("DvdDiscId")
                        .dvdEpisodeNumber(6L)
                        .dvdSeason(5L)
                        .episodeName("EpisodeName")
                        .filename("FileName")
                        .firstAired("FirstAired")
                        .addGuestStars("GuestStar1", "GuestStar2")
                        .id(6954L)
                        .imdbId("ImdbId")
                        .lastUpdated(451474L)
                        .lastUpdatedBy("LastUpdatedBy")
                        .overview("Overview")
                        .productionCode("ProductionCode")
                        .seriesId("SeriesId")
                        .showUrl("ShowUrl")
                        .siteRating(8.0)
                        .siteRatingCount(125L)
                        .thumbAdded("ThumbAdded")
                        .thumbAuthor(56874L)
                        .thumbHeight("ThumbHeight")
                        .thumbWidth("ThumbWidth")
                        .addWriters("Writer1", "Writer2")
                        .build())
                .errors(errors())
                .build();
    }

    /**
     * Creates a new episodes APIResponse DTO with default values set
     *
     * @return New episodes APIResponse DTO prefilled with default values
     */
    public static APIResponse<List<Episode>> episodes() {
        return new APIResponseDTO.Builder<List<Episode>>()
                .data(List.of(
                        new EpisodeDTO.Builder()
                                .absoluteNumber(70L)
                                .airedEpisodeNumber(13L)
                                .airedSeason(4L)
                                .airsAfterSeason(3L)
                                .airsBeforeEpisode(14L)
                                .airsBeforeSeason(5L)
                                .director("Director1")
                                .addDirectors("Director2", "Director3")
                                .dvdChapter(10L)
                                .dvdDiscid("DvdDiscId1")
                                .dvdEpisodeNumber(7L)
                                .dvdSeason(6L)
                                .episodeName("EpisodeName1")
                                .filename("FileName1")
                                .firstAired("FirstAired1")
                                .addGuestStars("GuestStar1", "GuestStar2")
                                .id(6955L)
                                .imdbId("ImdbId1")
                                .lastUpdated(451475L)
                                .lastUpdatedBy("LastUpdatedBy1")
                                .overview("Overview1")
                                .productionCode("ProductionCode1")
                                .seriesId("SeriesId1")
                                .showUrl("ShowUrl1")
                                .siteRating(9.0)
                                .siteRatingCount(126L)
                                .thumbAdded("ThumbAdded1")
                                .thumbAuthor(56875L)
                                .thumbHeight("ThumbHeight1")
                                .thumbWidth("ThumbWidth1")
                                .addWriters("Writer1", "Writer2")
                                .build(),
                        new EpisodeDTO.Builder()
                                .absoluteNumber(71L)
                                .airedEpisodeNumber(14L)
                                .airedSeason(5L)
                                .airsAfterSeason(4L)
                                .airsBeforeEpisode(15L)
                                .airsBeforeSeason(6L)
                                .director("Director4")
                                .addDirectors("Director5", "Director6")
                                .dvdChapter(11L)
                                .dvdDiscid("DvdDiscId2")
                                .dvdEpisodeNumber(8L)
                                .dvdSeason(7L)
                                .episodeName("EpisodeName2")
                                .filename("FileName2")
                                .firstAired("FirstAired2")
                                .addGuestStars("GuestStar3", "GuestStar4")
                                .id(6956L)
                                .imdbId("ImdbId2")
                                .lastUpdated(451476L)
                                .lastUpdatedBy("LastUpdatedBy2")
                                .overview("Overview2")
                                .productionCode("ProductionCode2")
                                .seriesId("SeriesId2")
                                .showUrl("ShowUrl2")
                                .siteRating(10.0)
                                .siteRatingCount(127L)
                                .thumbAdded("ThumbAdded2")
                                .thumbAuthor(56876L)
                                .thumbHeight("ThumbHeight2")
                                .thumbWidth("ThumbWidth2")
                                .addWriters("Writer3", "Writer4")
                                .build()))
                .errors(errors())
                .links(links())
                .build();
    }

    /**
     * Creates a new language APIResponse DTO with default values set
     *
     * @return New language APIResponse DTO prefilled with default values
     */
    public static APIResponse<Language> langauge() {
        return new APIResponseDTO.Builder<Language>()
                .data(new LanguageDTO.Builder()
                        .abbreviation("Abbrevation1")
                        .englishName("EnglishName1")
                        .id(5487L)
                        .name("Name1")
                        .build())
                .build();
    }

    /**
     * Creates a new languages APIResponse DTO with default values set
     *
     * @return New languages APIResponse DTO prefilled with default values
     */
    public static APIResponse<List<Language>> langauges() {
        return new APIResponseDTO.Builder<List<Language>>()
                .data(List.of(
                        new LanguageDTO.Builder()
                                .abbreviation("Abbrevation1")
                                .englishName("EnglishName1")
                                .id(4587L)
                                .name("Name1")
                                .build(),
                        new LanguageDTO.Builder()
                                .abbreviation("Abbrevation2")
                                .englishName("EnglishName2")
                                .id(4588L)
                                .name("Name2")
                                .build()))
                .build();
    }

    /**
     * Creates a new actors APIResponse DTO with default values set
     *
     * @return New actors APIResponse DTO prefilled with default values
     */
    public static APIResponse<List<Actor>> actors() {
        return new APIResponseDTO.Builder<List<Actor>>()
                .data(List.of(
                        new ActorDTO.Builder()
                                .id(3651L)
                                .image("Image1")
                                .imageAdded("ImageAdded1")
                                .imageAuthor(547L)
                                .lastUpdated("LastUpdated1")
                                .name("Name1")
                                .role("Role1")
                                .seriesId(5412L)
                                .sortOrder(1L)
                                .build(),
                        new ActorDTO.Builder()
                                .id(3652L)
                                .image("Image2")
                                .imageAdded("ImageAdded2")
                                .imageAuthor(548L)
                                .lastUpdated("LastUpdated2")
                                .name("Name2")
                                .role("Role2")
                                .seriesId(5413L)
                                .sortOrder(2L)
                                .build()))
                .errors(errors())
                .build();
    }

    /**
     * Creates a new series summary APIResponse DTO with default values set
     *
     * @return New series summary APIResponse DTO prefilled with default values
     */
    public static APIResponse<SeriesSummary> seriesSummary() {
        return new APIResponseDTO.Builder<SeriesSummary>()
                .data(new SeriesSummaryDTO.Builder()
                        .airedEpisodes("AiredEpisodes")
                        .addAiredSeasons("AiredSeasons1", "AiredSeasons2")
                        .dvdEpisodes("DvdEpisodes")
                        .addDvdSeasons("DvdSeasons1", "DvdSeasons2")
                        .build())
                .build();
    }

    /**
     * Creates a new image query parameters APIResponse DTO with default values set
     *
     * @return New image query parameters APIResponse DTO prefilled with default values
     */
    public static APIResponse<List<ImageQueryParameter>> imageQueryParameters() {
        return new APIResponseDTO.Builder<List<ImageQueryParameter>>()
                .data(List.of(
                        new ImageQueryParameterDTO.Builder()
                                .keyType("KeyType1")
                                .languageId("LanguageId1")
                                .addResolution("Resolution1", "Resolution2")
                                .addSubKey("SubKey1", "SubKey2")
                                .build(),
                        new ImageQueryParameterDTO.Builder()
                                .keyType("KeyType2")
                                .languageId("LanguageId2")
                                .addResolution("Resolution3", "Resolution4")
                                .addSubKey("SubKey3", "SubKey4")
                                .build()))
                .build();
    }

    /**
     * Creates a new image summary APIResponse DTO with default values set
     *
     * @return New image summary APIResponse DTO prefilled with default values
     */
    public static APIResponse<ImageSummary> imageSummary() {
        return new APIResponseDTO.Builder<ImageSummary>()
                .data(new ImageSummaryDTO.Builder()
                        .fanartCount(541L)
                        .posterCount(65L)
                        .seasonCount(12L)
                        .seasonwideCount(41L)
                        .seriesCount(2L)
                        .build())
                .build();
    }

    /**
     * Creates a new images APIResponse DTO with default values set
     *
     * @return New images APIResponse DTO prefilled with default values
     */
    public static APIResponse<List<Image>> images() {
        return new APIResponseDTO.Builder<List<Image>>()
                .data(List.of(
                        new ImageDTO.Builder()
                                .fileName("FileName1")
                                .id(9965L)
                                .keyType("KeyType1")
                                .languageId(365L)
                                .putRatingsInfo("average", 6)
                                .putRatingsInfo("count", 674)
                                .resolution("Resolution1")
                                .subKey("SubKey1")
                                .thumbnail("Thumbnail1")
                                .build(),
                        new ImageDTO.Builder()
                                .fileName("FileName2")
                                .id(9966L)
                                .keyType("KeyType2")
                                .languageId(366L)
                                .putRatingsInfo("average", 7)
                                .putRatingsInfo("count", 675)
                                .resolution("Resolution2")
                                .subKey("SubKey2")
                                .thumbnail("Thumbnail2")
                                .build()))
                .errors(errors())
                .build();
    }

    /**
     * Creates a new updates APIResponse DTO with default values set
     *
     * @return New updates APIResponse DTO prefilled with default values
     */
    public static APIResponse<Map<Long, Long>> updates() {
        return new APIResponseDTO.Builder<Map<Long, Long>>()
                .data(Map.of(
                        5487L, 6354874L,
                        5488L, 6354875L))
                .errors(errors())
                .build();
    }

    /**
     * Creates a new ratings APIResponse DTO with default values set
     *
     * @return New ratings APIResponse DTO prefilled with default values
     */
    public static APIResponse<List<Rating>> ratings() {
        return new APIResponseDTO.Builder<List<Rating>>()
                .data(List.of(
                        new RatingDTO.Builder()
                                .rating(5.0)
                                .ratingItemId(6584L)
                                .ratingType("RatingType1")
                                .build(),
                        new RatingDTO.Builder()
                                .rating(6.0)
                                .ratingItemId(6585L)
                                .ratingType("RatingType2")
                                .build()
                ))
                .errors(errors())
                .links(links())
                .build();
    }

    /**
     * Creates a new user APIResponse DTO with default values set
     *
     * @return New user APIResponse DTO prefilled with default values
     */
    public static APIResponse<User> user() {
        return new APIResponseDTO.Builder<User>()
                .data(new UserDTO.Builder()
                        .favoritesDisplaymode("FavoritesDisplaymode")
                        .language("Language")
                        .userName("UserName")
                        .build())
                .build();
    }
}

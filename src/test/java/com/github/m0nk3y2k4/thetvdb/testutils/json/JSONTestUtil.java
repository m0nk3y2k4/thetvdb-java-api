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

package com.github.m0nk3y2k4.thetvdb.testutils.json;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Artwork;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ArtworkType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Character;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Episode;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Genre;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Movie;
import com.github.m0nk3y2k4.thetvdb.api.model.data.People;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Season;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Series;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesDetails;
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

/**
 * Provides access to prefabbed JSON data used for unit testing
 * <p><br>
 * Test utility class offering easy access to predefined JSON files and associated Java DTO's. Resource files and DTO's
 * are aligned in terms of content. In other words, the DTO is the Java data type representation of some JSON resource
 * file. For an easier usage corresponding DTO's and resource files are consolidated into enumerations. For example:
 * <pre>{@code
 *     JsonParser seriesJSON = new JsonFactory().createParser(JsonResource.SERIES.getUrl());    // JSON resource file via URI
 *     APIResponse<Series> seriesDTO = parse(seriesJSON);
 *
 *     assertThat(seriesDTO).isEqualTo(JsonResource.SERIES.getDTO());      // Resource file as Java object
 * }</pre>
 */
public final class JSONTestUtil {

    public enum JsonResource {
        //@DisableFormatting
        ARTWORK("artwork", JSONTestUtil::artwork, "Single artwork JSON response"),
        ARTWORK_MIN("artwork_min", JSONTestUtil::artwork_min, "Single artwork JSON response with only mandatory fields"),
        ARTWORKTYPE_LIST("artworktype_list", JSONTestUtil::artworkType, "List of artwork types JSON response"),
        CHARACTER("character", JSONTestUtil::character, "Single character JSON response"),
        CHARACTER_MIN("character_min", JSONTestUtil::character_min, "Single character JSON response with only mandatory fields"),
        DATA("data", JSONTestUtil::data, "Full JSON response with data and status node"),
        EPISODE("episode", JSONTestUtil::episode, "Single episode JSON response"),
        EPISODE_MIN("episode_min", JSONTestUtil::episode_min, "Single episode JSON response with only mandatory fields"),
        GENRE("genre", JSONTestUtil::genre, "Single genre JSON response"),
        GENRE_LIST("genre_list", JSONTestUtil::genreList, "List of genres JSON response"),
        MOVIE("movie", JSONTestUtil::movie, "Single movie JSON response"),
        MOVIE_MIN("movie_min", JSONTestUtil::movie_min, "Single movie JSON response with only mandatory fields"),
        PEOPLE("people", JSONTestUtil::people, "Single people JSON response"),
        PEOPLE_MIN("people_min", JSONTestUtil::people_min, "Single people JSON response with only mandatory fields"),
        SEASON("season", JSONTestUtil::season, "Single season JSON response"),
        SEASON_MIN("season_min", JSONTestUtil::season_min, "Single season JSON response with only mandatory fields"),
        SERIES("series", JSONTestUtil::series, "Single series JSON response"),
        SERIES_MIN("series_min", JSONTestUtil::series_min, "Single series JSON response with only mandatory fields"),
        SERIES_DETAILS("series_extended", JSONTestUtil::seriesDetails, "Single extended series JSON response"),
        SERIES_DETAILS_MIN("series_extended_min", JSONTestUtil::seriesDetails_min, "Single extended series JSON response with only mandatory fields"),
        SERIES_LIST("series_list", JSONTestUtil::seriesList, "List of series JSON response");
        //@EnableFormatting

        private final String fileName;
        private final Supplier<APIResponse<?>> dtoSupplier;
        private final String description;

        JsonResource(String fileName, Supplier<APIResponse<?>> dtoSupplier, String description) {
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
         * Returns this resources JSON content as String
         *
         * @return This resource as JSON String
         *
         * @throws IOException If an I/O exception occurs
         */
        public String getJsonString() throws IOException {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(getUrl().openStream(), UTF_8))) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
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
         * @return DTO representation of this JSON resource
         */
        public APIResponse<?> getDTO() {
            return dtoSupplier.get();
        }

        @Override
        public String toString() {
            return description;
        }
    }

    private JSONTestUtil() {}

    /**
     * Creates a new basic artwork APIResponse DTO with default values set
     *
     * @return New basic artwork APIResponse DTO prefilled with default values
     */
    public static APIResponse<Artwork> artwork() {
        return createAPIResponse(new ArtworkDTO.Builder()
                .id(4635L)
                .image("Image")
                .thumbnail("Thumbnail")
                .language("Language")
                .type(54L)
                .score(2.0)
                .build());
    }

    /**
     * Creates a new basic artwork APIResponse DTO with only mandatory default values set
     *
     * @return New basic artwork APIResponse DTO prefilled with only mandatory default values
     */
    public static APIResponse<Artwork> artwork_min() {
        return createAPIResponse(new ArtworkDTO.Builder()
                .id(2487L)
                .image("Image")
                .thumbnail("Thumbnail")
                .type(12L)
                .build());
    }

    /**
     * Creates a new artwork type APIResponse DTO with default values set
     *
     * @return New artwork type APIResponse DTO prefilled with default values
     */
    public static APIResponse<List<ArtworkType>> artworkType() {
        return createAPIResponse(List.of(
                new ArtworkTypeDTO.Builder()
                        .id(4575L)
                        .name("Name1")
                        .recordType("RecordType1")
                        .slug("Slug1")
                        .imageFormat("ImageFormat1")
                        .width(758L)
                        .height(140L)
                        .thumbWidth(894L)
                        .thumbHeight(195L)
                        .build(),
                new ArtworkTypeDTO.Builder()
                        .id(4576L)
                        .name("Name2")
                        .recordType("RecordType2")
                        .slug("Slug2")
                        .imageFormat("ImageFormat2")
                        .width(759L)
                        .height(141L)
                        .thumbWidth(895L)
                        .thumbHeight(196L)
                        .build()
        ));
    }

    /**
     * Creates a new character APIResponse DTO with default values set
     *
     * @return New character APIResponse DTO prefilled with default values
     */
    public static APIResponse<Character> character() {
        return createAPIResponse(new CharacterDTO.Builder()
                .id(36487L)
                .name("Name")
                .peopleId(569L)
                .seriesId(45L)
                .movieId(364L)
                .episodeId(975L)
                .type(12L)
                .image("Image")
                .sort(3L)
                .isFeatured(TRUE)
                .url("Url")
                .addNameTranslations("NameTranslation1", "NameTranslation2")
                .addOverviewTranslations("OverviewTranslation1", "OverviewTranslation2")
                .addAliases(
                        new AliasDTO.Builder()
                                .language("Language1")
                                .name("Name1")
                                .build(),
                        new AliasDTO.Builder()
                                .language("Language2")
                                .name("Name2")
                                .build())
                .build());
    }

    /**
     * Creates a new character APIResponse DTO with only mandatory default values set
     *
     * @return New character APIResponse DTO prefilled with only mandatory default values
     */
    public static APIResponse<Character> character_min() {
        return createAPIResponse(new CharacterDTO.Builder()
                .id(50107L)
                .type(6L)
                .sort(9L)
                .isFeatured(TRUE)
                .url("Url")
                .build());
    }

    /**
     * Creates a new full APIResponse DTO containing data and status, both prefilled with default values
     *
     * @return New APIResponse DTO prefilled with default values
     */
    public static APIResponse<Data> data() {
        return createAPIResponse(Data.with("Some content"));
    }

    /**
     * Creates a new episode APIResponse DTO with default values set
     *
     * @return New episode APIResponse DTO prefilled with default values
     */
    public static APIResponse<Episode> episode() {
        return createAPIResponse(new EpisodeDTO.Builder()
                .id(548745L)
                .seriesId(69554L)
                .name("Name")
                .aired("Aired")
                .runtime(62L)
                .addNameTranslations("NameTranslation1", "NameTranslation2")
                .addOverviewTranslations("OverviewTranslation1", "OverviewTranslation2")
                .image("Image")
                .imageType(5L)
                .isMovie(true)
                .addSeasons(
                        new SeasonDTO.Builder()
                                .id(425874L)
                                .seriesId(43647L)
                                .type(3L)
                                .name("Name1")
                                .number(9L)
                                .addNameTranslations("NameTranslation1", "NameTranslation2")
                                .addOverviewTranslations("OverviewTranslation1", "OverviewTranslation2")
                                .image("Image1")
                                .imageType(19L)
                                .network(new NetworkDTO.Builder()
                                        .id(578L)
                                        .name("Name1")
                                        .slug("Slug1")
                                        .abbreviation("Abbreviation1")
                                        .country("Country1")
                                        .build())
                                .build(),
                        new SeasonDTO.Builder()
                                .id(425875L)
                                .seriesId(43648L)
                                .type(4L)
                                .name("Name2")
                                .number(10L)
                                .addNameTranslations("NameTranslation3", "NameTranslation4")
                                .addOverviewTranslations("OverviewTranslation3", "OverviewTranslation4")
                                .image("Image2")
                                .imageType(20L)
                                .network(new NetworkDTO.Builder()
                                        .id(579L)
                                        .name("Name2")
                                        .slug("Slug2")
                                        .abbreviation("Abbreviation2")
                                        .country("Country2")
                                        .build())
                                .build())
                .number(43L)
                .seasonNumber(9L)
                .build());
    }

    /**
     * Creates a new episode APIResponse DTO with only mandatory default values set
     *
     * @return New episode APIResponse DTO prefilled with only mandatory default values
     */
    public static APIResponse<Episode> episode_min() {
        return createAPIResponse(new EpisodeDTO.Builder()
                .id(770348L)
                .seriesId(99001L)
                .isMovie(true)
                .build());
    }

    /**
     * Creates a new genre APIResponse DTO with default values set
     *
     * @return New genre APIResponse DTO prefilled with default values
     */
    public static APIResponse<Genre> genre() {
        return createAPIResponse(new GenreDTO.Builder()
                .id(3L)
                .name("Name")
                .slug("Slug")
                .build());
    }

    /**
     * Creates a new genre overview APIResponse DTO with default values set
     *
     * @return New genre overview APIResponse DTO prefilled with default values
     */
    public static APIResponse<List<Genre>> genreList() {
        return createAPIResponse(List.of(
                new GenreDTO.Builder()
                        .id(6L)
                        .name("Name1")
                        .slug("Slug1")
                        .build(),
                new GenreDTO.Builder()
                        .id(7L)
                        .name("Name2")
                        .slug("Slug2")
                        .build()));
    }

    /**
     * Creates a new movie APIResponse DTO with default values set
     *
     * @return New movie APIResponse DTO prefilled with default values
     */
    public static APIResponse<Movie> movie() {
        return createAPIResponse(new MovieDTO.Builder()
                .id(84756L)
                .name("Name")
                .slug("Slug")
                .image("Image")
                .addNameTranslations("NameTranslation1", "NameTranslation2")
                .addOverviewTranslations("OverviewTranslation1", "OverviewTranslation2")
                .addAliases(
                        new AliasDTO.Builder()
                                .language("Language1")
                                .name("Name1")
                                .build(),
                        new AliasDTO.Builder()
                                .language("Language2")
                                .name("Name2")
                                .build())
                .score(1080D)
                .status(new StatusDTO.Builder()
                        .id(547L)
                        .keepUpdated(TRUE)
                        .name("Name")
                        .recordType("RecordType")
                        .build())
                .build());
    }

    /**
     * Creates a new movie APIResponse DTO with only mandatory default values set
     *
     * @return New movie APIResponse DTO prefilled with only mandatory default values
     */
    public static APIResponse<Movie> movie_min() {
        return createAPIResponse(new MovieDTO.Builder()
                .id(34870L)
                .name("Name")
                .slug("Slug")
                .image("Image")
                .score(599D)
                .status(new StatusDTO.Builder()
                        .id(33L)
                        .keepUpdated(TRUE)
                        .name("Name")
                        .recordType("RecordType")
                        .build())
                .build());
    }

    /**
     * Creates a new people APIResponse DTO with default values set
     *
     * @return New people APIResponse DTO prefilled with default values
     */
    public static APIResponse<People> people() {
        return createAPIResponse(new PeopleDTO.Builder()
                .id(11354L)
                .name("Name")
                .image("Image")
                .addAliases(
                        new AliasDTO.Builder()
                                .language("Language1")
                                .name("Name1")
                                .build(),
                        new AliasDTO.Builder()
                                .language("Language2")
                                .name("Name2")
                                .build())
                .score(487L)
                .build());
    }

    /**
     * Creates a new people APIResponse DTO with only mandatory default values set
     *
     * @return New people APIResponse DTO prefilled with only mandatory default values
     */
    public static APIResponse<People> people_min() {
        return createAPIResponse(new PeopleDTO.Builder()
                .id(71604L)
                .score(156L)
                .build());
    }

    /**
     * Creates a new season APIResponse DTO with default values set
     *
     * @return New season APIResponse DTO prefilled with default values
     */
    public static APIResponse<Season> season() {
        return createAPIResponse(new SeasonDTO.Builder()
                .id(47748L)
                .seriesId(95874L)
                .type(6954L)
                .name("Name")
                .number(6L)
                .addNameTranslations("NameTranslation1", "NameTranslation2")
                .addOverviewTranslations("OverviewTranslation1", "OverviewTranslation2")
                .image("Image")
                .imageType(487L)
                .network(new NetworkDTO.Builder()
                        .id(7841L)
                        .name("Name")
                        .slug("Slug")
                        .abbreviation("Abbreviation")
                        .country("Country")
                        .build())
                .build());
    }

    /**
     * Creates a new season APIResponse DTO with only mandatory default values set
     *
     * @return New season APIResponse DTO prefilled with only mandatory default values
     */
    public static APIResponse<Season> season_min() {
        return createAPIResponse(new SeasonDTO.Builder()
                .seriesId(64410L)
                .type(67L)
                .number(8L)
                .network(new NetworkDTO.Builder().build())
                .build());
    }

    /**
     * Creates a new series APIResponse DTO with default values set
     *
     * @return New series APIResponse DTO prefilled with default values
     */
    public static APIResponse<Series> series() {
        return createAPIResponse(new SeriesDTO.Builder()
                .id(34875L)
                .name("Name")
                .slug("Slug")
                .image("Image")
                .addNameTranslations("NameTranslation1", "NameTranslation2")
                .addOverviewTranslations("OverviewTranslation1", "OverviewTranslation2")
                .addAliases(
                        new AliasDTO.Builder()
                                .language("Language1")
                                .name("Name1")
                                .build(),
                        new AliasDTO.Builder()
                                .language("Language2")
                                .name("Name2")
                                .build())
                .firstAired("FirstAired")
                .lastAired("LastAired")
                .nextAired("NextAired")
                .score(68D)
                .status(new StatusDTO.Builder()
                        .id(44L)
                        .name("Name")
                        .recordType("RecordType")
                        .keepUpdated(TRUE)
                        .build())
                .originalCountry("OriginalCountry")
                .originalLanguage("OriginalLanguage")
                .originalNetwork(new NetworkDTO.Builder()
                        .id(31L)
                        .name("Name")
                        .slug("Slug")
                        .abbreviation("Abbreviation")
                        .country("Country")
                        .build())
                .defaultSeasonType(469L)
                .isOrderRandomized(TRUE)
                .build());
    }

    /**
     * Creates a new series APIResponse DTO with only mandatory default values set
     *
     * @return New series APIResponse DTO prefilled with only mandatory default values
     */
    public static APIResponse<Series> series_min() {
        return createAPIResponse(new SeriesDTO.Builder()
                .nextAired("NextAired")
                .score(100D)
                .status(new StatusDTO.Builder()
                        .id(81L)
                        .name("Name")
                        .recordType("RecordType")
                        .keepUpdated(TRUE)
                        .build())
                .originalNetwork(new NetworkDTO.Builder().build())
                .defaultSeasonType(4L)
                .isOrderRandomized(TRUE)
                .build());
    }

    /**
     * Creates a new series details APIResponse DTO with default values set
     *
     * @return New series details APIResponse DTO prefilled with default values
     */
    public static APIResponse<SeriesDetails> seriesDetails() {
        return createAPIResponse(new SeriesDetailsDTO.Builder()
                .id(924L)
                .name("Name")
                .slug("Slug")
                .image("Image")
                .addNameTranslations("NameTranslation1", "NameTranslation2")
                .addOverviewTranslations("OverviewTranslation1", "OverviewTranslation2")
                .addAliases(
                        new AliasDTO.Builder()
                                .language("Language1")
                                .name("Name1")
                                .build(),
                        new AliasDTO.Builder()
                                .language("Language2")
                                .name("Name2")
                                .build())
                .firstAired("FirstAired")
                .lastAired("LastAired")
                .nextAired("NextAired")
                .score(5D)
                .status(new StatusDTO.Builder()
                        .id(99L)
                        .name("Name")
                        .recordType("RecordType")
                        .keepUpdated(TRUE)
                        .build())
                .originalCountry("OriginalCountry")
                .originalLanguage("OriginalLanguage")
                .originalNetwork(new NetworkDTO.Builder()
                        .id(634L)
                        .name("Name")
                        .slug("Slug")
                        .abbreviation("Abbreviation")
                        .country("Country")
                        .build())
                .defaultSeasonType(57L)
                .isOrderRandomized(TRUE)
                .addArtworks(
                        new ArtworkDTO.Builder()
                                .id(557L)
                                .image("Image1")
                                .thumbnail("Thumbnail1")
                                .language("Language1")
                                .type(6547L)
                                .score(34D)
                                .build(),
                        new ArtworkDTO.Builder()
                                .id(558L)
                                .image("Image2")
                                .thumbnail("Thumbnail2")
                                .language("Language2")
                                .type(6548L)
                                .score(35D)
                                .build())
                .addNetworks(
                        new NetworkDTO.Builder()
                                .id(3487L)
                                .name("Name1")
                                .slug("Slug1")
                                .abbreviation("Abbreviation1")
                                .country("Country1")
                                .build(),
                        new NetworkDTO.Builder()
                                .id(3488L)
                                .name("Name2")
                                .slug("Slug2")
                                .abbreviation("Abbreviation2")
                                .country("Country2")
                                .build())
                .addGenres(
                        new GenreDTO.Builder()
                                .id(8301L)
                                .name("Name1")
                                .slug("Slug1")
                                .build(),
                        new GenreDTO.Builder()
                                .id(8302L)
                                .name("Name2")
                                .slug("Slug2")
                                .build())
                .addTrailers(
                        new TrailerDTO.Builder()
                                .id(6034L)
                                .name("Name1")
                                .language("Language1")
                                .url("Url1")
                                .build(),
                        new TrailerDTO.Builder()
                                .id(6035L)
                                .name("Name2")
                                .language("Language2")
                                .url("Url2")
                                .build())
                .addFranchises(
                        new FranchiseDTO.Builder()
                                .id(87L)
                                .name("Name1")
                                .addNameTranslations("NameTranslation1", "NameTranslation2")
                                .addOverviewTranslations("OverviewTranslation1", "OverviewTranslation2")
                                .addAliases("Alias1", "Alias2")
                                .build(),
                        new FranchiseDTO.Builder()
                                .id(88L)
                                .name("Name2")
                                .addNameTranslations("NameTranslation3", "NameTranslation4")
                                .addOverviewTranslations("OverviewTranslation3", "OverviewTranslation4")
                                .addAliases("Alias3", "Alias4")
                                .build())
                .addRemoteIds(
                        new RemoteIdDTO.Builder()
                                .id("Id1")
                                .type(3070L)
                                .build(),
                        new RemoteIdDTO.Builder()
                                .id("Id2")
                                .type(3071L)
                                .build())
                .addCharacters(
                        new CharacterDTO.Builder()
                                .id(6784L)
                                .name("Name1")
                                .peopleId(7740L)
                                .seriesId(360L)
                                .movieId(7846L)
                                .episodeId(97614L)
                                .type(6L)
                                .image("Image1")
                                .sort(2L)
                                .isFeatured(TRUE)
                                .url("Url1")
                                .addNameTranslations("NameTranslation1", "NameTranslation2")
                                .addOverviewTranslations("OverviewTranslation1", "OverviewTranslation2")
                                .addAliases(
                                        new AliasDTO.Builder()
                                                .language("Language1")
                                                .name("Name1")
                                                .build(),
                                        new AliasDTO.Builder()
                                                .language("Language2")
                                                .name("Name2")
                                                .build())
                                .build(),
                        new CharacterDTO.Builder()
                                .id(6785L)
                                .name("Name2")
                                .peopleId(7741L)
                                .seriesId(361L)
                                .movieId(7847L)
                                .episodeId(97615L)
                                .type(7L)
                                .image("Image2")
                                .sort(3L)
                                .isFeatured(FALSE)
                                .url("Url2")
                                .addNameTranslations("NameTranslation3", "NameTranslation4")
                                .addOverviewTranslations("OverviewTranslation3", "OverviewTranslation4")
                                .addAliases(
                                        new AliasDTO.Builder()
                                                .language("Language3")
                                                .name("Name3")
                                                .build(),
                                        new AliasDTO.Builder()
                                                .language("Language4")
                                                .name("Name4")
                                                .build())
                                .build())
                .airsDays(new SeriesAirsDaysDTO.Builder()
                        .onFriday(TRUE)
                        .onMonday(TRUE)
                        .onSaturday(TRUE)
                        .onSunday(TRUE)
                        .onThursday(TRUE)
                        .onTuesday(TRUE)
                        .onWednesday(TRUE)
                        .build())
                .airsTime("AirsTime")
                .addSeasons(
                        new SeasonDTO.Builder()
                                .id(60014L)
                                .seriesId(48765L)
                                .type(17L)
                                .name("Name1")
                                .number(67L)
                                .addNameTranslations("NameTranslation1", "NameTranslation2")
                                .addOverviewTranslations("OverviewTranslation1", "OverviewTranslation2")
                                .image("Image1")
                                .imageType(47L)
                                .network(new NetworkDTO.Builder()
                                        .id(6654L)
                                        .name("Name1")
                                        .slug("Slug1")
                                        .abbreviation("Abbreviation1")
                                        .country("Country1")
                                        .build())
                                .build(),
                        new SeasonDTO.Builder()
                                .id(60015L)
                                .seriesId(48766L)
                                .type(18L)
                                .name("Name2")
                                .number(68L)
                                .addNameTranslations("NameTranslation3", "NameTranslation4")
                                .addOverviewTranslations("OverviewTranslation3", "OverviewTranslation4")
                                .image("Image2")
                                .imageType(48L)
                                .network(new NetworkDTO.Builder()
                                        .id(6655L)
                                        .name("Name2")
                                        .slug("Slug2")
                                        .abbreviation("Abbreviation2")
                                        .country("Country2")
                                        .build())
                                .build())
                .build());
    }

    /**
     * Creates a new series details APIResponse DTO with only mandatory default values set
     *
     * @return New series details APIResponse DTO prefilled with only mandatory default values
     */
    public static APIResponse<SeriesDetails> seriesDetails_min() {
        return createAPIResponse(new SeriesDetailsDTO.Builder()
                .nextAired("NextAired")
                .score(9D)
                .status(new StatusDTO.Builder()
                        .id(1004L)
                        .name("Name")
                        .recordType("RecordType")
                        .keepUpdated(TRUE)
                        .build())
                .originalNetwork(new NetworkDTO.Builder().build())
                .defaultSeasonType(11L)
                .isOrderRandomized(TRUE)
                .addTrailers(
                        new TrailerDTO.Builder()
                                .id(3274L)
                                .build())
                .addFranchises(
                        new FranchiseDTO.Builder()
                                .id(55743L)
                                .name("Name1")
                                .build())
                .airsDays(new SeriesAirsDaysDTO.Builder()
                        .onFriday(TRUE)
                        .onMonday(TRUE)
                        .onSaturday(TRUE)
                        .onSunday(TRUE)
                        .onThursday(TRUE)
                        .onTuesday(TRUE)
                        .onWednesday(TRUE)
                        .build())
                .build());
    }

    /**
     * Creates a new series overview APIResponse DTO with default values set
     *
     * @return New series overview APIResponse DTO prefilled with default values
     */
    public static APIResponse<List<Series>> seriesList() {
        return createAPIResponse(List.of(
                new SeriesDTO.Builder()
                        .id(69314L)
                        .name("Name1")
                        .slug("Slug1")
                        .image("Image1")
                        .addNameTranslations("NameTranslation1", "NameTranslation2")
                        .addOverviewTranslations("OverviewTranslation1", "OverviewTranslation2")
                        .addAliases(
                                new AliasDTO.Builder()
                                        .language("Language1")
                                        .name("Name1")
                                        .build(),
                                new AliasDTO.Builder()
                                        .language("Language2")
                                        .name("Name2")
                                        .build())
                        .firstAired("FirstAired1")
                        .lastAired("LastAired1")
                        .nextAired("NextAired1")
                        .score(47D)
                        .status(new StatusDTO.Builder()
                                .id(56L)
                                .name("Name1")
                                .recordType("RecordType1")
                                .keepUpdated(TRUE)
                                .build())
                        .originalCountry("OriginalCountry1")
                        .originalLanguage("OriginalLanguage1")
                        .originalNetwork(new NetworkDTO.Builder()
                                .id(478L)
                                .name("Name1")
                                .slug("Slug1")
                                .abbreviation("Abbreviation1")
                                .country("Country1")
                                .build())
                        .defaultSeasonType(78457L)
                        .isOrderRandomized(TRUE)
                        .build(),
                new SeriesDTO.Builder()
                        .id(69315L)
                        .name("Name2")
                        .slug("Slug2")
                        .image("Image2")
                        .addNameTranslations("NameTranslation3", "NameTranslation4")
                        .addOverviewTranslations("OverviewTranslation3", "OverviewTranslation4")
                        .addAliases(
                                new AliasDTO.Builder()
                                        .language("Language3")
                                        .name("Name3")
                                        .build(),
                                new AliasDTO.Builder()
                                        .language("Language4")
                                        .name("Name4")
                                        .build())
                        .firstAired("FirstAired2")
                        .lastAired("LastAired2")
                        .nextAired("NextAired2")
                        .score(48D)
                        .status(new StatusDTO.Builder()
                                .id(57L)
                                .name("Name2")
                                .recordType("RecordType2")
                                .keepUpdated(FALSE)
                                .build())
                        .originalCountry("OriginalCountry2")
                        .originalLanguage("OriginalLanguage2")
                        .originalNetwork(new NetworkDTO.Builder()
                                .id(479L)
                                .name("Name2")
                                .slug("Slug2")
                                .abbreviation("Abbreviation2")
                                .country("Country2")
                                .build())
                        .defaultSeasonType(78458L)
                        .isOrderRandomized(FALSE)
                        .build()));
    }

    /**
     * Creates a new APIResponse DTO with the given <em>{@code data}</em> and some additional default values
     *
     * @param data The actual payload DTO of the response
     * @param <T>  The API response payload type
     *
     * @return New APIResponse DTO prefilled with the given object and some additional default values
     */
    public static <T> APIResponse<T> createAPIResponse(T data) {
        return new APIResponseDTO.Builder<T>()
                .data(data)
                .status("success")
                .build();
    }
}

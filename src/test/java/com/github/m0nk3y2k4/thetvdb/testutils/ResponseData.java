/*
 * Copyright (C) 2019 - 2021 thetvdb-java-api Authors and Contributors
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

package com.github.m0nk3y2k4.thetvdb.testutils;

import static com.github.m0nk3y2k4.thetvdb.testutils.ResponseData.Shape.FULL;
import static java.lang.Boolean.TRUE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Alias;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Artwork;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ArtworkDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ArtworkType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Award;
import com.github.m0nk3y2k4.thetvdb.api.model.data.AwardCategory;
import com.github.m0nk3y2k4.thetvdb.api.model.data.AwardCategoryDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.AwardNominee;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Character;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Company;
import com.github.m0nk3y2k4.thetvdb.api.model.data.CompanyType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ContentRating;
import com.github.m0nk3y2k4.thetvdb.api.model.data.EntityType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Episode;
import com.github.m0nk3y2k4.thetvdb.api.model.data.EpisodeDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.FCList;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Genre;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Movie;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Network;
import com.github.m0nk3y2k4.thetvdb.api.model.data.People;
import com.github.m0nk3y2k4.thetvdb.api.model.data.RemoteId;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Season;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeasonType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Series;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesAirsDays;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Status;
import com.github.m0nk3y2k4.thetvdb.api.model.data.TagOption;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Trailer;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Translation;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.APIResponseDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.AliasDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ArtworkDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ArtworkDetailsDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ArtworkTypeDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.AwardCategoryDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.AwardCategoryDetailsDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.AwardDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.AwardNomineeDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.CharacterDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.CompanyDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.CompanyTypeDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ContentRatingDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.EntityTypeDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.EpisodeDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.EpisodeDetailsDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.FCListDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.GenreDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.NetworkDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.PeopleDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.RemoteIdDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeasonDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeasonTypeDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesAirsDaysDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesDetailsDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.StatusDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.TagOptionDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.TrailerDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.TranslationDTO;
import com.github.m0nk3y2k4.thetvdb.internal.util.json.deser.StaticTypeReference;
import com.github.m0nk3y2k4.thetvdb.testutils.json.Data;

@SuppressWarnings({"ClassWithTooManyFields", "unused", "OverlyLongLambda", "OverlyComplexClass", "ConstantValueVariableUse"})
// Test class providing prefabbed test objects via reflection
public abstract class ResponseData<T> {

    //@DisableFormatting
    //************************ artwork **********************
    public static final ResponseData<APIResponse<List<ArtworkType>>> ARTWORKTYPE_LIST = new ResponseData<>(
            "artworktype_list", artworkTypeList(), "List of artwork types JSON response") {};
    public static final ResponseData<APIResponse<Artwork>> ARTWORK = new ResponseData<>(
            "artwork", artwork(FULL), "Single artwork JSON response") {};
    public static final ResponseData<APIResponse<ArtworkDetails>> ARTWORK_DETAILS = new ResponseData<>(
            "artwork_extended", artworkDetails(FULL), "Single extended artwork JSON response") {};

    //************************ awards ***********************
    public static final ResponseData<APIResponse<AwardCategory>> AWARDCATEGORY = new ResponseData<>(
            "awardcategory", awardCategory(FULL), "Single award category JSON response") {};
    public static final ResponseData<APIResponse<AwardCategoryDetails>> AWARDCATEGORY_DETAILS = new ResponseData<>(
            "awardcategory_extended", awardCategoryDetails(FULL), "Single extended award category JSON response") {};

    //*********************** characters ********************
    public static final ResponseData<APIResponse<Character>> CHARACTER = new ResponseData<>(
            "character", character(FULL), "Single character JSON response") {};

    //*********************** companies *********************
    public static final ResponseData<APIResponse<Company>> COMPANY = new ResponseData<>(
            "company", company(FULL), "Single company JSON response") {};
    public static final ResponseData<APIResponse<List<Company>>> COMPANY_LIST = new ResponseData<>(
            "company_list", companyList(), "List of company JSON response") {};

    //********************* company-types *******************
    public static final ResponseData<APIResponse<List<CompanyType>>> COMPANYTYPE_LIST = new ResponseData<>(
            "companytype_list", companyTypeList(), "List of company types JSON response") {};

    //************************* DUMMY ***********************
    public static final ResponseData<APIResponse<Data>> DATA = new ResponseData<>(
            "data", data(), "Full JSON response with data and status node") {};

    //********************* entity-types ********************
    public static final ResponseData<APIResponse<List<EntityType>>> ENTITYTYPE_LIST = new ResponseData<>(
            "entitytype_list", entityTypeList(), "List of entity types JSON response") {};

    //************************ episodes *********************
    public static final ResponseData<APIResponse<Episode>> EPISODE = new ResponseData<>(
            "episode", episode(FULL), "Single episode JSON response") {};
    public static final ResponseData<APIResponse<EpisodeDetails>> EPISODE_DETAILS = new ResponseData<>(
            "episode_extended", episodeDetails(FULL), "Single extended episode JSON response") {};

    //************************* genres **********************
    public static final ResponseData<APIResponse<Genre>> GENRE = new ResponseData<>(
            "genre", genre(), "Single genre JSON response") {};
    public static final ResponseData<APIResponse<List<Genre>>> GENRE_LIST = new ResponseData<>(
            "genre_list", genreList(), "List of genres JSON response") {};

    //************************* movies **********************
    public static final ResponseData<APIResponse<Movie>> MOVIE = new ResponseData<>(
            "movie", movie(FULL), "Single movie JSON response") {};

    //************************* people **********************
    public static final ResponseData<APIResponse<People>> PEOPLE = new ResponseData<>(
            "people", people(FULL), "Single people JSON response") {};

    //************************ seasons **********************
    public static final ResponseData<APIResponse<Season>> SEASON = new ResponseData<>(
            "season", season(FULL), "Single season JSON response") {};

    //************************* series **********************
    public static final ResponseData<APIResponse<Series>> SERIES = new ResponseData<>(
            "series", series(FULL), "Single series JSON response") {};
    public static final ResponseData<APIResponse<SeriesDetails>> SERIES_DETAILS = new ResponseData<>(
            "series_extended", seriesDetails(FULL), "Single extended series JSON response") {};
    public static final ResponseData<APIResponse<List<Series>>> SERIES_LIST = new ResponseData<>(
            "series_list", seriesList(), "List of series JSON response") {};

    //********************** translations *******************
    public static final ResponseData<APIResponse<Translation>> TRANSLATION = new ResponseData<>(
            "translation", translation(FULL), "Single translated entity JSON response") {};
    //@EnableFormatting

    /**
     * Used to specify the actual content of a test data object.
     * <p><br>
     * <b>NOTE:</b> since all JSON fields have been declared being optional the MIN shape for testing mandatory
     * fields is no longer needed. And although only FULL message tests are performed for the time being, I decided to
     * not completely remove the "shaping" as there might be other use cases for which this can be used in the future.
     */
    enum Shape {
        /** With all data */
        FULL
    }

    /** File name of the JSON template to be used by this response */
    private final String jsonFileName;
    /** DTO representation of the JSON template */
    private final T dto;
    /** JSON type reference representing the actual generic type arguments of this instance */
    private final TypeReference<T> typeReference;
    /** Textual description about what this response represents */
    private final String description;

    /**
     * Creates a new response test data instance based on the given values
     *
     * @param jsonFileName File name of the JSON template to be used by this response
     * @param dto          DTO representation of the JSON template
     * @param description  Textual description about what this response represents
     */
    private ResponseData(String jsonFileName, T dto, String description) {
        this.jsonFileName = jsonFileName;
        this.dto = dto;
        this.description = description;
        this.typeReference = new StaticTypeReference<>(
                ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }


    // *************************************************************************
    // **********                APIResponse creation                 **********
    // *************************************************************************

    private static APIResponse<Artwork> artwork(Shape shape) {
        return createAPIResponse(create(artworkModel(), shape));
    }

    private static APIResponse<ArtworkDetails> artworkDetails(Shape shape) {
        return createAPIResponse(create(artworkDetailsModel(), shape));
    }

    private static APIResponse<List<ArtworkType>> artworkTypeList() {
        return createAPIResponse(createTwo(artworkTypeModel()));
    }

    private static APIResponse<AwardCategory> awardCategory(Shape shape) {
        return createAPIResponse(create(awardCategoryModel(), shape));
    }

    private static APIResponse<AwardCategoryDetails> awardCategoryDetails(Shape shape) {
        return createAPIResponse(create(awardCategoryDetailsModel(), shape));
    }

    private static APIResponse<Character> character(Shape shape) {
        return createAPIResponse(create(characterModel(), shape));
    }

    private static APIResponse<Company> company(Shape shape) {
        return createAPIResponse(create(companyModel(), shape));
    }

    private static APIResponse<List<Company>> companyList() {
        return createAPIResponse(createTwo(companyModel()));
    }

    private static APIResponse<List<CompanyType>> companyTypeList() {
        return createAPIResponse(createTwo(companyTypeModel()));
    }

    private static APIResponse<Data> data() {
        return createAPIResponse(Data.with("Some content"));
    }

    private static APIResponse<List<EntityType>> entityTypeList() {
        return createAPIResponse(createTwo(entityTypeModel()));
    }

    private static APIResponse<Episode> episode(Shape shape) {
        return createAPIResponse(create(episodeModel(), shape));
    }

    private static APIResponse<EpisodeDetails> episodeDetails(Shape shape) {
        return createAPIResponse(create(episodeDetailsModel(), shape));
    }

    private static APIResponse<Genre> genre() {
        return createAPIResponse(create(genreModel()));
    }

    private static APIResponse<List<Genre>> genreList() {
        return createAPIResponse(createTwo(genreModel()));
    }

    private static APIResponse<Movie> movie(Shape shape) {
        return createAPIResponse(create(movieModel(), shape));
    }

    private static APIResponse<People> people(Shape shape) {
        return createAPIResponse(create(peopleModel(), shape));
    }

    private static APIResponse<Season> season(Shape shape) {
        return createAPIResponse(create(seasonModel(), shape));
    }

    private static APIResponse<Series> series(Shape shape) {
        return createAPIResponse(create(seriesModel(), shape));
    }

    private static APIResponse<SeriesDetails> seriesDetails(Shape shape) {
        return createAPIResponse(create(seriesDetailsModel(), shape));
    }

    private static APIResponse<List<Series>> seriesList() {
        return createAPIResponse(createTwo(seriesModel()));
    }

    private static APIResponse<Translation> translation(Shape shape) {
        return createAPIResponse(create(translationModel(), shape));
    }

    private static <T> APIResponse<T> createAPIResponse(T data) {
        return new APIResponseDTO.Builder<T>().data(data).status("success").build();
    }


    // *************************************************************************
    // **********               Utils for DTO creation                **********
    // *************************************************************************

    private static <T> T create(BiFunction<Integer, Shape, T> producer) {
        return create(producer, FULL);
    }

    private static <T> T create(BiFunction<Integer, Shape, T> producer, Shape shape) {
        return create(producer, 1, shape);
    }

    private static <T> T create(BiFunction<Integer, Shape, T> producer, int startIndex) {
        return create(producer, startIndex, FULL);
    }

    private static <T> T create(BiFunction<Integer, Shape, T> producer, int startIndex, Shape shape) {
        return create(1, producer, startIndex, shape).get(0);
    }

    private static <T> List<T> createTwo(BiFunction<Integer, Shape, T> producer) {
        return createTwo(producer, 1);
    }

    private static <T> List<T> createTwo(BiFunction<Integer, Shape, T> producer, int startIndex) {
        return create(2, producer, startIndex, FULL);
    }

    private static <T> List<T> create(int amount, BiFunction<Integer, Shape, T> producer, int startIndex, Shape shape) {
        return IntStream.range(startIndex, startIndex + amount)
                .mapToObj(idx -> producer.apply(idx, shape)).collect(Collectors.toList());
    }


    // *************************************************************************
    // **********              Data model DTO creation                **********
    // *************************************************************************

    private static BiFunction<Integer, Shape, Alias> aliasModel() {
        return (Integer idx, Shape shape) -> new AliasDTO.Builder().language("Language" + idx).name("Name" + idx)
                .build();
    }

    private static BiFunction<Integer, Shape, Artwork> artworkModel() {
        return (Integer idx, Shape shape) -> {
            ArtworkDTO.Builder builder = new ArtworkDTO.Builder();
            if (shape == FULL) {
                builder.id(4634L + idx).image("Image" + idx).thumbnail("Thumbnail" + idx).type(53L + idx)
                        .language("Language" + idx).score(1.0 + idx);
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, ArtworkDetails> artworkDetailsModel() {
        return (Integer idx, Shape shape) -> {
            ArtworkDetailsDTO.Builder builder = new ArtworkDetailsDTO.Builder();
            if (shape == FULL) {
                builder.height(1079L + idx).id(694400L + idx).image("Image" + idx).thumbnail("Thumbnail" + idx)
                        .thumbnailHeight(399L + idx).thumbnailWidth(599L + idx).type(4L + idx)
                        .updatedAt(16015470L + idx).width(1919L + idx).episodeId(39009L + idx)
                        .language("Language" + idx).movieId(573L + idx).networkId(66340L + idx)
                        .peopleId(97511L + idx).score(81D + idx).seasonId(574603L + idx).seriesId(669843L + idx)
                        .seriesPeopleId(646L + idx);
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, ArtworkType> artworkTypeModel() {
        return (Integer idx, Shape shape) -> new ArtworkTypeDTO.Builder().id(4574L + idx).name("Name" + idx)
                .recordType("RecordType" + idx).slug("Slug" + idx).imageFormat("ImageFormat" + idx).width(757L + idx)
                .height(139L + idx).thumbWidth(893L + idx).thumbHeight(194L + idx).build();
    }

    private static BiFunction<Integer, Shape, Award> awardModel() {
        return (Integer idx, Shape shape) -> {
            AwardDTO.Builder builder = new AwardDTO.Builder();
            if (shape == FULL) {
                builder.id(46L + idx).name("Name" + idx);
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, AwardCategory> awardCategoryModel() {
        return (Integer idx, Shape shape) -> {
            AwardCategoryDTO.Builder builder = new AwardCategoryDTO.Builder();
            if (shape == FULL) {
                builder.allowCoNominees(true).award(create(awardModel(), idx, shape)).forMovies(true).forSeries(true)
                        .id(8753L + idx).name("Name" + idx);
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, AwardCategoryDetails> awardCategoryDetailsModel() {
        return (Integer idx, Shape shape) -> {
            AwardCategoryDetailsDTO.Builder builder = new AwardCategoryDetailsDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.allowCoNominees(true).award(create(awardModel(), idx, shape)).forMovies(true).forSeries(true)
                        .id(5449L + idx).name("Name" + idx).nominees(createTwo(awardNomineeModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, AwardNominee> awardNomineeModel() {
        return (Integer idx, Shape shape) -> {
            AwardNomineeDTO.Builder builder = new AwardNomineeDTO.Builder();
            if (shape == FULL) {
                builder.character(create(characterModel(), idx, shape)).episode(create(episodeModel(), idx, shape))
                        .movie(create(movieModel(), idx, shape)).series(create(seriesModel(), idx, shape))
                        .id(64119L + idx).isWinner(true).details("Details" + idx).year("Year" + idx);
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, Character> characterModel() {
        return (Integer idx, Shape shape) -> {
            CharacterDTO.Builder builder = new CharacterDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(36486L + idx).type(11L + idx).sort(2L + idx).isFeatured(TRUE).url("Url" + idx)
                        .name("Name" + idx).peopleId(568L + idx).seriesId(44L + idx).movieId(363L + idx)
                        .episodeId(974L + idx).image("Image" + idx)
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .aliases(createTwo(aliasModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, Company> companyModel() {
        return (Integer idx, Shape shape) -> {
            CompanyDTO.Builder builder = new CompanyDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(64713L + idx).slug("Slug" + idx).primaryCompanyType(513L + idx)
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .aliases(createTwo(aliasModel(), listOffset)).country("Country" + idx)
                        .activeDate("ActiveDate" + idx).inactiveDate("InactiveDate" + idx).name("Name" + idx);
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, CompanyType> companyTypeModel() {
        return (Integer idx, Shape shape) -> new CompanyTypeDTO.Builder().id(7363L + idx).name("Name" + idx).build();
    }

    private static BiFunction<Integer, Shape, ContentRating> contentRatingModel() {
        return (Integer idx, Shape shape) -> new ContentRatingDTO.Builder().id(246L + idx).name("Name" + idx).build();
    }

    private static BiFunction<Integer, Shape, EntityType> entityTypeModel() {
        return (Integer idx, Shape shape) -> new EntityTypeDTO.Builder().id(603L + idx).name("Name" + idx)
                .hasSpecials(true).build();
    }

    private static BiFunction<Integer, Shape, Episode> episodeModel() {
        return (Integer idx, Shape shape) -> {
            EpisodeDTO.Builder builder = new EpisodeDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(548744L + idx).seriesId(69553L + idx).isMovie(true).name("Name" + idx).aired("Aired" + idx)
                        .runtime(61L + idx)
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .image("Image" + idx).imageType(4L + idx).seasons(createTwo(seasonModel(), listOffset))
                        .number(42L + idx).seasonNumber(8L + idx);
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, EpisodeDetails> episodeDetailsModel() {
        return (Integer idx, Shape shape) -> {
            EpisodeDetailsDTO.Builder builder = new EpisodeDetailsDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(647513L + idx).seriesId(634043L + idx).isMovie(true).name("Name" + idx).aired("Aired" + idx)
                        .runtime(73L + idx).image("Image" + idx).imageType(573L + idx).seasonNumber(2L + idx)
                        .productionCode("ProductionCode" + idx).airsAfterSeason(1L + idx).airsBeforeSeason(3L + idx)
                        .airsBeforeEpisode(11L + idx)
                        .network(create(networkModel(), idx, shape))
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .seasons(createTwo(seasonModel(), listOffset)).number(10L + idx)
                        .awards(createTwo(awardModel(), listOffset)).characters(createTwo(characterModel(), listOffset))
                        .contentRatings(createTwo(contentRatingModel(), listOffset))
                        .remoteIds(createTwo(remoteIdModel(), listOffset))
                        .tagOptions(createTwo(tagOptionModel(), listOffset))
                        .trailers(createTwo(trailerModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, FCList> listModel() {
        return (Integer idx, Shape shape) -> {
            FCListDTO.Builder builder = new FCListDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(94L + idx).name("Name" + idx).overview("Overview" + idx).url("Url" + idx).isOfficial(true)
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .aliases(createTwo(aliasModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, Genre> genreModel() {
        return (Integer idx, Shape shape) -> new GenreDTO.Builder().id(2L + idx).name("Name" + idx).slug("Slug" + idx)
                .build();
    }

    private static BiFunction<Integer, Shape, String> nameTranslationModel() {
        return (Integer idx, Shape shape) -> "NameTranslation" + idx;
    }

    private static BiFunction<Integer, Shape, Network> networkModel() {
        return (Integer idx, Shape shape) -> {
            NetworkDTO.Builder builder = new NetworkDTO.Builder();
            if (shape == FULL) {
                builder.id(477L + idx).name("Name" + idx).slug("Slug" + idx).abbreviation("Abbreviation" + idx)
                        .country("Country" + idx);
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, Movie> movieModel() {
        return (Integer idx, Shape shape) -> {
            MovieDTO.Builder builder = new MovieDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(84755L + idx).name("Name" + idx).slug("Slug" + idx).image("Image" + idx).score(1079D + idx)
                        .status(create(statusModel(), idx))
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .aliases(createTwo(aliasModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, String> overviewTranslationModel() {
        return (Integer idx, Shape shape) -> "OverviewTranslation" + idx;
    }

    private static BiFunction<Integer, Shape, People> peopleModel() {
        return (Integer idx, Shape shape) -> {
            PeopleDTO.Builder builder = new PeopleDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(11353L + idx).score(486L + idx).name("Name" + idx).image("Image" + idx)
                        .aliases(createTwo(aliasModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, RemoteId> remoteIdModel() {
        return (Integer idx, Shape shape) -> new RemoteIdDTO.Builder().id("Id" + idx).type(3069L + idx).build();
    }

    private static BiFunction<Integer, Shape, Season> seasonModel() {
        return (Integer idx, Shape shape) -> {
            SeasonDTO.Builder builder = new SeasonDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.seriesId(95873L + idx).number(5L + idx).id(47747L + idx).name("Name" + idx)
                        .type(create(seasonTypeModel(), idx))
                        .network(create(networkModel(), idx, shape))
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .image("Image" + idx).imageType(486L + idx);
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, SeasonType> seasonTypeModel() {
        return (Integer idx, Shape shape) -> new SeasonTypeDTO.Builder().id(6953L + idx).name("Name" + idx)
                .type("Type" + idx).build();
    }

    private static BiFunction<Integer, Shape, Series> seriesModel() {
        return (Integer idx, Shape shape) -> {
            SeriesDTO.Builder builder = new SeriesDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.nextAired("NextAired" + idx).score(67D + idx).lastAired("LastAired" + idx)
                        .originalCountry("OriginalCountry" + idx).defaultSeasonType(468L + idx)
                        .originalLanguage("OriginalLanguage" + idx).isOrderRandomized(TRUE)
                        .id(34874L + idx).name("Name" + idx).slug("Slug" + idx).image("Image" + idx)
                        .status(create(statusModel(), idx))
                        .originalNetwork(create(networkModel(), idx, shape))
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .aliases(createTwo(aliasModel(), listOffset)).firstAired("FirstAired" + idx)
                ;
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, SeriesAirsDays> seriesAirsDaysModel() {
        return (Integer idx, Shape shape) -> new SeriesAirsDaysDTO.Builder().onFriday(TRUE).onMonday(TRUE)
                .onSaturday(TRUE).onSunday(TRUE).onThursday(TRUE).onTuesday(TRUE).onWednesday(TRUE).build();
    }

    private static BiFunction<Integer, Shape, SeriesDetails> seriesDetailsModel() {
        return (Integer idx, Shape shape) -> {
            SeriesDetailsDTO.Builder builder = new SeriesDetailsDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.nextAired("NextAired" + idx).score(4D + idx).defaultSeasonType(56L + idx)
                        .isOrderRandomized(TRUE).firstAired("FirstAired" + idx).lastAired("LastAired" + idx)
                        .id(923L + idx).name("Name" + idx).slug("Slug" + idx).image("Image" + idx)
                        .originalCountry("OriginalCountry" + idx).originalLanguage("OriginalLanguage" + idx)
                        .isOrderRandomized(TRUE).airsTime("AirsTime" + idx)
                        .status(create(statusModel(), idx))
                        .airsDays(create(seriesAirsDaysModel(), idx))
                        .originalNetwork(create(networkModel(), idx, shape))
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .aliases(createTwo(aliasModel()))
                        .artworks(createTwo(artworkModel(), listOffset))
                        .networks(createTwo(networkModel(), listOffset)).genres(createTwo(genreModel(), listOffset))
                        .trailers(createTwo(trailerModel(), listOffset))
                        .lists(createTwo(listModel(), listOffset))
                        .remoteIds(createTwo(remoteIdModel(), listOffset))
                        .characters(createTwo(characterModel(), listOffset))
                        .seasons(createTwo(seasonModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, Status> statusModel() {
        return (Integer idx, Shape shape) -> new StatusDTO.Builder().id(546L + idx).keepUpdated(TRUE).name("Name" + idx)
                .recordType("RecordType" + idx).build();
    }

    private static BiFunction<Integer, Shape, TagOption> tagOptionModel() {
        return (Integer idx, Shape shape) -> {
            TagOptionDTO.Builder builder = new TagOptionDTO.Builder();
            if (shape == FULL) {
                builder.id(5796L + idx).name("Name" + idx).tag(42L + idx).tagName("TagName" + idx)
                        .helpText("HelpText" + idx);
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, Trailer> trailerModel() {
        return (Integer idx, Shape shape) -> {
            TrailerDTO.Builder builder = new TrailerDTO.Builder();
            if (shape == FULL) {
                builder.id(6033L + idx).name("Name" + idx).language("Language" + idx).url("Url" + idx);
            }
            return builder.build();
        };
    }

    private static BiFunction<Integer, Shape, Translation> translationModel() {
        return (Integer idx, Shape shape) -> {
            TranslationDTO.Builder builder = new TranslationDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.language("Language" + idx).isPrimary(true).name("Name" + idx).isAlias(true)
                        .overview("Overview" + idx).addAliases("Alias" + listOffset, "Alias" + (listOffset + 1));
            }
            return builder.build();
        };
    }


    // *************************************************************************
    // **********              Regular object methods                 **********
    // *************************************************************************

    /**
     * Creates a matching JsonNode representation for this response data object
     *
     * @return This response mapped as JsonNode object
     *
     * @throws IOException If a low-level I/O problem (unexpected end-of-input, network error) occurs
     */
    public JsonNode getJson() throws IOException {
        return new ObjectMapper().readTree(getUrl());
    }

    /**
     * Returns this responses JSON content as String
     *
     * @return This response as JSON String
     *
     * @throws IOException If an I/O exception occurs
     */
    public String getJsonString() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getUrl().openStream(), UTF_8))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    /**
     * Returns the uniform resource locator (URL) of the corresponding JSON template used by this response
     *
     * @return URL referencing the JSON template
     */
    public URL getUrl() {
        return ClassLoader.getSystemResource("json/example/" + jsonFileName + ".json");
    }

    /**
     * Returns a DTO representation of this response data object
     *
     * @return DTO representation of this response
     */
    public T getDTO() {
        return dto;
    }

    /**
     * Returns the corresponding JSON type reference for this response
     *
     * @return JSON type reference matching the actual type parameters of this response data object
     */
    public TypeReference<T> getType() {
        return typeReference;
    }

    @Override
    public String toString() {
        return description;
    }
}

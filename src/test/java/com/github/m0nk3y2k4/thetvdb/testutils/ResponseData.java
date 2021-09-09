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
import static java.util.Collections.singletonList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Alias;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Artwork;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ArtworkDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ArtworkStatus;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ArtworkType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Award;
import com.github.m0nk3y2k4.thetvdb.api.model.data.AwardCategory;
import com.github.m0nk3y2k4.thetvdb.api.model.data.AwardCategoryDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.AwardDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.AwardNominee;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Biography;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Character;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Companies;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Company;
import com.github.m0nk3y2k4.thetvdb.api.model.data.CompanyType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ContentRating;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Entity;
import com.github.m0nk3y2k4.thetvdb.api.model.data.EntityType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.EntityUpdate;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Episode;
import com.github.m0nk3y2k4.thetvdb.api.model.data.EpisodeDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.FCList;
import com.github.m0nk3y2k4.thetvdb.api.model.data.FCListDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Gender;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Genre;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Inspiration;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Movie;
import com.github.m0nk3y2k4.thetvdb.api.model.data.MovieDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Network;
import com.github.m0nk3y2k4.thetvdb.api.model.data.People;
import com.github.m0nk3y2k4.thetvdb.api.model.data.PeopleDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.PeopleType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ProductionCountry;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Race;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Release;
import com.github.m0nk3y2k4.thetvdb.api.model.data.RemoteId;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Season;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeasonDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeasonType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Series;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesAirsDays;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesDetails;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesEpisodes;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SourceType;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Status;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Studio;
import com.github.m0nk3y2k4.thetvdb.api.model.data.TagOption;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Trailer;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Translation;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Translations;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.APIResponseDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.AliasDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ArtworkDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ArtworkDetailsDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ArtworkStatusDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ArtworkTypeDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.AwardCategoryDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.AwardCategoryDetailsDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.AwardDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.AwardDetailsDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.AwardNomineeDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.BiographyDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.CharacterDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.CompaniesDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.CompanyDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.CompanyTypeDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ContentRatingDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.EntityDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.EntityTypeDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.EntityUpdateDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.EpisodeDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.EpisodeDetailsDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.FCListDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.FCListDetailsDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.GenderDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.GenreDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.InspirationDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.MovieDetailsDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.NetworkDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.PeopleDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.PeopleDetailsDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.PeopleTypeDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ProductionCountryDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.RaceDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.ReleaseDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.RemoteIdDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeasonDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeasonDetailsDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeasonTypeDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesAirsDaysDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesDetailsDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SeriesEpisodesDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.SourceTypeDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.StatusDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.StudioDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.TagOptionDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.TrailerDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.TranslationDTO;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.TranslationsDTO;
import com.github.m0nk3y2k4.thetvdb.internal.util.json.deser.StaticTypeReference;
import com.github.m0nk3y2k4.thetvdb.testutils.json.Data;

@SuppressWarnings({"ClassWithTooManyFields", "unused", "OverlyLongLambda", "OverlyComplexClass", "ConstantValueVariableUse"})
// Test class providing prefabbed test objects via reflection
public abstract class ResponseData<T> {

    //@DisableFormatting
    //************************ artwork **********************
    public static final ResponseData<APIResponse<Collection<ArtworkStatus>>> ARTWORKSTATUS_OVERVIEW = new ResponseData<>(
            "artworkstatus_overview", artworkStatusOverview(), "Overview of artwork statuses JSON response") {};
    public static final ResponseData<APIResponse<Collection<ArtworkType>>> ARTWORKTYPE_OVERVIEW = new ResponseData<>(
            "artworktype_overview", artworkTypeOverview(), "Overview of artwork types JSON response") {};
    public static final ResponseData<APIResponse<Artwork>> ARTWORK = new ResponseData<>(
            "artwork", artwork(FULL), "Single artwork JSON response") {};
    public static final ResponseData<APIResponse<ArtworkDetails>> ARTWORK_DETAILS = new ResponseData<>(
            "artwork_extended", artworkDetails(FULL), "Single extended artwork JSON response") {};

    //************************ awards ***********************
    public static final ResponseData<APIResponse<AwardCategory>> AWARDCATEGORY = new ResponseData<>(
            "awardcategory", awardCategory(FULL), "Single award category JSON response") {};
    public static final ResponseData<APIResponse<AwardCategoryDetails>> AWARDCATEGORY_DETAILS = new ResponseData<>(
            "awardcategory_extended", awardCategoryDetails(FULL), "Single extended award category JSON response") {};
    public static final ResponseData<APIResponse<Collection<Award>>> AWARD_OVERVIEW = new ResponseData<>(
            "award_overview", awardOverview(), "Overview of awards JSON response") {};
    public static final ResponseData<APIResponse<Award>> AWARD = new ResponseData<>(
            "award", award(), "Single award JSON response") {};
    public static final ResponseData<APIResponse<AwardDetails>> AWARD_DETAILS = new ResponseData<>(
            "award_extended", awardDetails(FULL), "Single extended award JSON response") {};

    //*********************** characters ********************
    public static final ResponseData<APIResponse<Character>> CHARACTER = new ResponseData<>(
            "character", character(FULL), "Single character JSON response") {};

    //*********************** companies *********************
    public static final ResponseData<APIResponse<Company>> COMPANY = new ResponseData<>(
            "company", company(FULL), "Single company JSON response") {};
    public static final ResponseData<APIResponse<Collection<CompanyType>>> COMPANYTYPE_OVERVIEW = new ResponseData<>(
            "companytype_overview", companyTypeOverview(), "Overview of company types JSON response") {};
    public static final ResponseData<APIResponse<Collection<Company>>> COMPANY_OVERVIEW = new ResponseData<>(
            "company_overview", companyOverview(), "Overview of companies JSON response") {};

    //******************** content-ratings ******************
    public static final ResponseData<APIResponse<Collection<ContentRating>>> CONTENTRATING_OVERVIEW = new ResponseData<>(
            "contentrating_overview", contentRatingOverview(), "Overview of content ratings JSON response") {};

    //************************* DUMMY ***********************
    public static final ResponseData<APIResponse<Data>> DATA = new ResponseData<>(
            "data", data(), "Full JSON response with data and status node") {};

    //********************* entity-types ********************
    public static final ResponseData<APIResponse<Collection<EntityType>>> ENTITYTYPE_OVERVIEW = new ResponseData<>(
            "entitytype_overview", entityTypeOverview(), "Overview of entity types JSON response") {};

    //************************ episodes *********************
    public static final ResponseData<APIResponse<Episode>> EPISODE = new ResponseData<>(
            "episode", episode(FULL), "Single episode JSON response") {};
    public static final ResponseData<APIResponse<EpisodeDetails>> EPISODE_DETAILS = new ResponseData<>(
            "episode_extended", episodeDetails(FULL), "Single extended episode JSON response") {};

    //************************* lists ***********************
    public static final ResponseData<APIResponse<Collection<FCList>>> LIST_OVERVIEW = new ResponseData<>(
            "list_overview", listOverview(), "Overview of lists JSON response") {};
    public static final ResponseData<APIResponse<FCList>> LIST = new ResponseData<>(
            "list", list(FULL), "Single list JSON response") {};
    public static final ResponseData<APIResponse<FCListDetails>> LIST_DETAILS = new ResponseData<>(
            "list_extended", listDetails(FULL), "Single extended list JSON response") {};

    //************************ genders **********************
    public static final ResponseData<APIResponse<Collection<Gender>>> GENDER_OVERVIEW = new ResponseData<>(
            "gender_overview", genderOverview(), "Overview of genders JSON response") {};

    //************************* genres **********************
    public static final ResponseData<APIResponse<Genre>> GENRE = new ResponseData<>(
            "genre", genre(), "Single genre JSON response") {};
    public static final ResponseData<APIResponse<Collection<Genre>>> GENRE_OVERVIEW = new ResponseData<>(
            "genre_overview", genreOverview(), "Overview of genres JSON response") {};

    //************************* movies **********************
    public static final ResponseData<APIResponse<Movie>> MOVIE = new ResponseData<>(
            "movie", movie(FULL), "Single movie JSON response") {};
    public static final ResponseData<APIResponse<MovieDetails>> MOVIE_DETAILS = new ResponseData<>(
            "movie_extended", movieDetails(FULL), "Single extended movie JSON response") {};
    public static final ResponseData<APIResponse<Collection<Movie>>> MOVIE_OVERVIEW = new ResponseData<>(
            "movie_overview", movieOverview(), "Overview of movies JSON response") {};

    //************************* people **********************
    public static final ResponseData<APIResponse<Collection<PeopleType>>> PEOPLETYPE_OVERVIEW = new ResponseData<>(
            "peopletype_overview", peopleTypeOverview(), "Overview of people types JSON response") {};
    public static final ResponseData<APIResponse<People>> PEOPLE = new ResponseData<>(
            "people", people(FULL), "Single people JSON response") {};
    public static final ResponseData<APIResponse<PeopleDetails>> PEOPLE_DETAILS = new ResponseData<>(
            "people_extended", peopleDetails(FULL), "Single extended people JSON response") {};

    //************************ seasons **********************
    public static final ResponseData<APIResponse<Season>> SEASON = new ResponseData<>(
            "season", season(FULL), "Single season JSON response") {};
    public static final ResponseData<APIResponse<SeasonDetails>> SEASON_DETAILS = new ResponseData<>(
            "season_extended", seasonDetails(FULL), "Single extended season JSON response") {};
    public static final ResponseData<APIResponse<Collection<SeasonType>>> SEASONTYPE_OVERVIEW = new ResponseData<>(
            "seasontype_overview", seasonTypeOverview(), "Overview of season types JSON response") {};

    //************************* series **********************
    public static final ResponseData<APIResponse<Series>> SERIES = new ResponseData<>(
            "series", series(FULL), "Single series JSON response") {};
    public static final ResponseData<APIResponse<SeriesDetails>> SERIES_DETAILS = new ResponseData<>(
            "series_extended", seriesDetails(FULL), "Single extended series JSON response") {};
    public static final ResponseData<APIResponse<SeriesEpisodes>> SERIESEPISODES = new ResponseData<>(
            "seriesepisodes", seriesEpisodes(FULL), "Single series episodes JSON response") {};
    public static final ResponseData<APIResponse<Collection<Series>>> SERIES_OVERVIEW = new ResponseData<>(
            "series_overview", seriesOverview(), "Overview of series JSON response") {};

    //********************** source-types *******************
    public static final ResponseData<APIResponse<Collection<SourceType>>> SOURCETYPE_OVERVIEW = new ResponseData<>(
            "sourcetype_overview", sourceTypeOverview(), "Overview of source types JSON response") {};

    //************************* status **********************
    public static final ResponseData<APIResponse<Collection<Status>>> STATUS_OVERVIEW = new ResponseData<>(
            "status_overview", statusOverview(), "Overview of statuses JSON response") {};

    //********************** translations *******************
    public static final ResponseData<APIResponse<Translation>> TRANSLATION = new ResponseData<>(
            "translation", translation(FULL), "Single translated entity JSON response") {};
    // ToDo: Remove this and switch to single translation object after remote API has been fixed
    public static final ResponseData<APIResponse<Collection<Translation>>> TRANSLATIONS = new ResponseData<>(
            "translations", createAPIResponse(singletonList(create(translationModel(), FULL))), "List of translated entities JSON response") {};

    //************************ updates **********************
    public static final ResponseData<APIResponse<Collection<EntityUpdate>>> UPDATE_OVERVIEW = new ResponseData<>(
            "update_overview", updateOverview(), "Overview of updates JSON response") {};
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

    private static APIResponse<Collection<ArtworkType>> artworkTypeOverview() {
        return createAPIResponse(createTwo(artworkTypeModel()));
    }

    private static APIResponse<Collection<ArtworkStatus>> artworkStatusOverview() {
        return createAPIResponse(createTwo(artworkStatusModel()));
    }

    private static APIResponse<AwardCategory> awardCategory(Shape shape) {
        return createAPIResponse(create(awardCategoryModel(), shape));
    }

    private static APIResponse<AwardCategoryDetails> awardCategoryDetails(Shape shape) {
        return createAPIResponse(create(awardCategoryDetailsModel(), shape));
    }

    private static APIResponse<Collection<Award>> awardOverview() {
        return createAPIResponse(createTwo(awardModel()));
    }

    private static APIResponse<Award> award() {
        return createAPIResponse(create(awardModel()));
    }

    private static APIResponse<AwardDetails> awardDetails(Shape shape) {
        return createAPIResponse(create(awardDetailsModel(), shape));
    }

    private static APIResponse<Character> character(Shape shape) {
        return createAPIResponse(create(characterModel(), shape));
    }

    private static APIResponse<Company> company(Shape shape) {
        return createAPIResponse(create(companyModel(), shape));
    }

    private static APIResponse<Collection<Company>> companyOverview() {
        return createAPIResponse(createTwo(companyModel()));
    }

    private static APIResponse<Collection<CompanyType>> companyTypeOverview() {
        return createAPIResponse(createTwo(companyTypeModel()));
    }

    private static APIResponse<Collection<ContentRating>> contentRatingOverview() {
        return createAPIResponse(createTwo(contentRatingModel()));
    }

    private static APIResponse<Data> data() {
        return createAPIResponse(Data.with("Some content"));
    }

    private static APIResponse<Collection<EntityType>> entityTypeOverview() {
        return createAPIResponse(createTwo(entityTypeModel()));
    }

    private static APIResponse<Episode> episode(Shape shape) {
        return createAPIResponse(create(episodeModel(), shape));
    }

    private static APIResponse<EpisodeDetails> episodeDetails(Shape shape) {
        return createAPIResponse(create(episodeDetailsModel(), shape));
    }

    private static APIResponse<Collection<FCList>> listOverview() {
        return createAPIResponse(createTwo(listModel()));
    }

    private static APIResponse<FCList> list(Shape shape) {
        return createAPIResponse(create(listModel(), shape));
    }

    private static APIResponse<FCListDetails> listDetails(Shape shape) {
        return createAPIResponse(create(listDetailsModel(), shape));
    }

    private static APIResponse<Genre> genre() {
        return createAPIResponse(create(genreModel()));
    }

    private static APIResponse<Collection<Gender>> genderOverview() {
        return createAPIResponse(createTwo(genderModel()));
    }

    private static APIResponse<Collection<Genre>> genreOverview() {
        return createAPIResponse(createTwo(genreModel()));
    }

    private static APIResponse<Movie> movie(Shape shape) {
        return createAPIResponse(create(movieModel(), shape));
    }

    private static APIResponse<MovieDetails> movieDetails(Shape shape) {
        return createAPIResponse(create(movieDetailsModel(), shape));
    }

    private static APIResponse<Collection<Movie>> movieOverview() {
        return createAPIResponse(createTwo(movieModel()));
    }

    private static APIResponse<People> people(Shape shape) {
        return createAPIResponse(create(peopleModel(), shape));
    }

    private static APIResponse<PeopleDetails> peopleDetails(Shape shape) {
        return createAPIResponse(create(peopleDetailsModel(), shape));
    }

    private static APIResponse<Collection<PeopleType>> peopleTypeOverview() {
        return createAPIResponse(createTwo(peopleTypeModel()));
    }

    private static APIResponse<Season> season(Shape shape) {
        return createAPIResponse(create(seasonModel(), shape));
    }

    private static APIResponse<SeasonDetails> seasonDetails(Shape shape) {
        return createAPIResponse(create(seasonDetailsModel(), shape));
    }

    private static APIResponse<Collection<SeasonType>> seasonTypeOverview() {
        return createAPIResponse(createTwo(seasonTypeModel()));
    }

    private static APIResponse<Series> series(Shape shape) {
        return createAPIResponse(create(seriesModel(), shape));
    }

    private static APIResponse<SeriesDetails> seriesDetails(Shape shape) {
        return createAPIResponse(create(seriesDetailsModel(), shape));
    }

    private static APIResponse<SeriesEpisodes> seriesEpisodes(Shape shape) {
        return createAPIResponse(create(seriesEpisodesModel(), shape));
    }

    private static APIResponse<Collection<Series>> seriesOverview() {
        return createAPIResponse(createTwo(seriesModel()));
    }

    private static APIResponse<Collection<SourceType>> sourceTypeOverview() {
        return createAPIResponse(createTwo(sourceTypeModel()));
    }

    private static APIResponse<Collection<Status>> statusOverview() {
        return createAPIResponse(createTwo(statusModel()));
    }

    private static APIResponse<Translation> translation(Shape shape) {
        return createAPIResponse(create(translationModel(), shape));
    }

    private static <T> APIResponse<T> createAPIResponse(T data) {
        return new APIResponseDTO.Builder<T>().data(data).status("success").build();
    }

    private static APIResponse<Collection<EntityUpdate>> updateOverview() {
        return createAPIResponse(createTwo(entityUpdateModel()));
    }


    // *************************************************************************
    // **********               Utils for DTO creation                **********
    // *************************************************************************

    private static <T> T create(DtoSupplier<T> supplier) {
        return create(supplier, FULL);
    }

    private static <T> T create(DtoSupplier<T> supplier, Shape shape) {
        return create(supplier, 1, shape);
    }

    private static <T> T create(DtoSupplier<T> supplier, int startIndex) {
        return create(supplier, startIndex, FULL);
    }

    private static <T> T create(DtoSupplier<T> supplier, int startIndex, Shape shape) {
        return create(1, supplier, startIndex, shape).stream().findFirst().orElseThrow();
    }

    private static <T> Collection<T> createTwo(DtoSupplier<T> supplier) {
        return createTwo(supplier, 1);
    }

    private static <T> Collection<T> createTwo(DtoSupplier<T> supplier, int startIndex) {
        return create(2, supplier, startIndex, FULL);
    }

    private static <T> Collection<T> create(int amount, DtoSupplier<T> supplier, int startIndex, Shape shape) {
        return IntStream.range(startIndex, startIndex + amount)
                .mapToObj(idx -> supplier.get(idx, shape)).collect(Collectors.toList());
    }


    // *************************************************************************
    // **********              Data model DTO creation                **********
    // *************************************************************************

    private static SimpleDtoSupplier<Alias> aliasModel() {
        return idx -> new AliasDTO.Builder().language("Language" + idx).name("Name" + idx).build();
    }

    private static DtoSupplier<Artwork> artworkModel() {
        return (idx, shape) -> {
            ArtworkDTO.Builder builder = new ArtworkDTO.Builder();
            if (shape == FULL) {
                builder.id(4634L + idx).image("Image" + idx).thumbnail("Thumbnail" + idx).type(53L + idx)
                        .language("Language" + idx).score(1.0 + idx);
            }
            return builder.build();
        };
    }

    private static DtoSupplier<ArtworkDetails> artworkDetailsModel() {
        return (idx, shape) -> {
            ArtworkDetailsDTO.Builder builder = new ArtworkDetailsDTO.Builder();
            if (shape == FULL) {
                builder.height(1079L + idx).id(694400L + idx).image("Image" + idx).thumbnail("Thumbnail" + idx)
                        .thumbnailHeight(399L + idx).thumbnailWidth(599L + idx).type(4L + idx).width(1919L + idx)
                        .updatedAt(16015470L + idx).episodeId(39009L + idx).language("Language" + idx)
                        .movieId(573L + idx).networkId(66340L + idx).seriesPeopleId(646L + idx)
                        .peopleId(97511L + idx).score(81D + idx).seasonId(574603L + idx).seriesId(669843L + idx)
                        .status(create(artworkStatusModel(), idx))
                        .tagOptions(create(tagOptionModel(), idx));
            }
            return builder.build();
        };
    }

    private static SimpleDtoSupplier<ArtworkType> artworkTypeModel() {
        return idx -> new ArtworkTypeDTO.Builder().id(4574L + idx).name("Name" + idx).recordType("RecordType" + idx)
                .slug("Slug" + idx).imageFormat("ImageFormat" + idx).width(757L + idx).height(139L + idx)
                .thumbWidth(893L + idx).thumbHeight(194L + idx).build();
    }

    private static SimpleDtoSupplier<ArtworkStatus> artworkStatusModel() {
        return idx -> new ArtworkStatusDTO.Builder().id(72L + idx).name("Name" + idx).build();
    }

    private static DtoSupplier<Award> awardModel() {
        return (idx, shape) -> {
            AwardDTO.Builder builder = new AwardDTO.Builder();
            if (shape == FULL) {
                builder.id(46L + idx).name("Name" + idx);
            }
            return builder.build();
        };
    }

    private static DtoSupplier<AwardDetails> awardDetailsModel() {
        return (idx, shape) -> {
            AwardDetailsDTO.Builder builder = new AwardDetailsDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(61L + idx).name("Name" + idx).score(5L + idx).
                        categories(createTwo(awardCategoryModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static DtoSupplier<AwardCategory> awardCategoryModel() {
        return (idx, shape) -> {
            AwardCategoryDTO.Builder builder = new AwardCategoryDTO.Builder();
            if (shape == FULL) {
                builder.allowCoNominees(true).award(create(awardModel(), idx, shape)).forMovies(true).forSeries(true)
                        .id(8753L + idx).name("Name" + idx);
            }
            return builder.build();
        };
    }

    private static DtoSupplier<AwardCategoryDetails> awardCategoryDetailsModel() {
        return (idx, shape) -> {
            AwardCategoryDetailsDTO.Builder builder = new AwardCategoryDetailsDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.allowCoNominees(true).award(create(awardModel(), idx, shape)).forMovies(true).forSeries(true)
                        .id(5449L + idx).name("Name" + idx).nominees(createTwo(awardNomineeModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static DtoSupplier<AwardNominee> awardNomineeModel() {
        return (idx, shape) -> {
            AwardNomineeDTO.Builder builder = new AwardNomineeDTO.Builder();
            if (shape == FULL) {
                builder.character(create(characterModel(), idx, shape)).episode(create(episodeModel(), idx, shape))
                        .movie(create(movieModel(), idx, shape)).series(create(seriesModel(), idx, shape))
                        .id(64119L + idx).isWinner(true).details("Details" + idx).year("Year" + idx)
                        .category("Category" + idx).name("Name" + idx);
            }
            return builder.build();
        };
    }

    private static SimpleDtoSupplier<Biography> biographyModel() {
        return idx -> new BiographyDTO.Builder().biography("Biography" + idx).language("Language" + idx).build();
    }

    private static DtoSupplier<Character> characterModel() {
        return (idx, shape) -> {
            CharacterDTO.Builder builder = new CharacterDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(36486L + idx).type(11L + idx).sort(2L + idx).isFeatured(TRUE).url("Url" + idx)
                        .name("Name" + idx).peopleId(568L + idx).seriesId(44L + idx).movieId(363L + idx)
                        .episodeId(974L + idx).image("Image" + idx).peopleType("PeopleType" + idx)
                        .personName("PersonName" + idx)
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .aliases(createTwo(aliasModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static DtoSupplier<Companies> companiesModel() {
        return (idx, shape) -> {
            CompaniesDTO.Builder builder = new CompaniesDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.studio(createTwo(companyModel(), listOffset))
                        .network(createTwo(companyModel(), listOffset))
                        .production(createTwo(companyModel(), listOffset))
                        .distributor(createTwo(companyModel(), listOffset))
                        .specialEffects(createTwo(companyModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static DtoSupplier<Company> companyModel() {
        return (idx, shape) -> {
            CompanyDTO.Builder builder = new CompanyDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(64713L + idx).slug("Slug" + idx).primaryCompanyType(513L + idx).country("Country" + idx)
                        .activeDate("ActiveDate" + idx).inactiveDate("InactiveDate" + idx).name("Name" + idx)
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .aliases(createTwo(aliasModel(), listOffset))
                        .companyType(create(companyTypeModel(), idx));
            }
            return builder.build();
        };
    }

    private static SimpleDtoSupplier<CompanyType> companyTypeModel() {
        return idx -> new CompanyTypeDTO.Builder().companyTypeId(355L + idx).companyTypeName("CompanyTypeName" + idx)
                .build();
    }

    private static SimpleDtoSupplier<ContentRating> contentRatingModel() {
        return idx -> new ContentRatingDTO.Builder().id(246L + idx).name("Name" + idx).country("Country" + idx)
                .description("Description" + idx).contentType("ContentType" + idx).order(25L + idx)
                .fullname("Fullname" + idx).build();
    }

    private static SimpleDtoSupplier<Entity> entityModel() {
        return idx -> new EntityDTO.Builder().order(6L + idx).seriesId(2835L + idx).movieId(1264L + idx).build();
    }

    private static SimpleDtoSupplier<EntityType> entityTypeModel() {
        return idx -> new EntityTypeDTO.Builder().id(603L + idx).name("Name" + idx).hasSpecials(true).build();
    }

    private static DtoSupplier<Episode> episodeModel() {
        return (idx, shape) -> {
            EpisodeDTO.Builder builder = new EpisodeDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(548744L + idx).seriesId(69553L + idx).isMovie(true).name("Name" + idx).aired("Aired" + idx)
                        .runtime(61L + idx)
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .image("Image" + idx).imageType(4L + idx).seasons(createTwo(seasonModel(), listOffset))
                        .number(42L + idx).seasonNumber(8L + idx).lastUpdated("LastUpdated" + idx)
                        .finaleType("FinaleType" + idx);
            }
            return builder.build();
        };
    }

    private static DtoSupplier<EpisodeDetails> episodeDetailsModel() {
        return (idx, shape) -> {
            EpisodeDetailsDTO.Builder builder = new EpisodeDetailsDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(647513L + idx).seriesId(634043L + idx).isMovie(true).name("Name" + idx).aired("Aired" + idx)
                        .runtime(73L + idx).image("Image" + idx).imageType(573L + idx).seasonNumber(2L + idx)
                        .productionCode("ProductionCode" + idx).airsAfterSeason(1L + idx).airsBeforeSeason(3L + idx)
                        .airsBeforeEpisode(11L + idx).lastUpdated("LastUpdated" + idx).finaleType("FinaleType" + idx)
                        .networks(createTwo(networkModel(), listOffset))
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .seasons(createTwo(seasonModel(), listOffset)).number(10L + idx)
                        .awards(createTwo(awardModel(), listOffset)).characters(createTwo(characterModel(), listOffset))
                        .contentRatings(createTwo(contentRatingModel(), listOffset))
                        .remoteIds(createTwo(remoteIdModel(), listOffset))
                        .tagOptions(createTwo(tagOptionModel(), listOffset))
                        .trailers(createTwo(trailerModel(), listOffset))
                        .nominations(Collections.emptyList())
                        .studios(createTwo(studioModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static DtoSupplier<FCList> listModel() {
        return (idx, shape) -> {
            FCListDTO.Builder builder = new FCListDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(94L + idx).name("Name" + idx).overview("Overview" + idx).url("Url" + idx).isOfficial(true)
                        .score(6L + idx)
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .aliases(createTwo(aliasModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static DtoSupplier<FCListDetails> listDetailsModel() {
        return (idx, shape) -> {
            FCListDetailsDTO.Builder builder = new FCListDetailsDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(66L + idx).name("Name" + idx).overview("Overview" + idx).url("Url" + idx).isOfficial(true)
                        .score(8L + idx)
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .aliases(createTwo(aliasModel(), listOffset))
                        .entities(createTwo(entityModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static SimpleDtoSupplier<Gender> genderModel() {
        return idx -> new GenderDTO.Builder().id(656L + idx).name("Name" + idx).build();
    }

    private static SimpleDtoSupplier<Genre> genreModel() {
        return idx -> new GenreDTO.Builder().id(2L + idx).name("Name" + idx).slug("Slug" + idx).build();
    }

    private static SimpleDtoSupplier<Inspiration> inspirationModel() {
        return idx -> new InspirationDTO.Builder().id(97L + idx).type("Type" + idx).typeName("TypeName" + idx)
                .url("Url" + idx).build();
    }

    private static SimpleDtoSupplier<String> nameTranslationModel() {
        return idx -> "NameTranslation" + idx;
    }

    private static DtoSupplier<Network> networkModel() {
        return (idx, shape) -> {
            NetworkDTO.Builder builder = new NetworkDTO.Builder();
            if (shape == FULL) {
                builder.id(477L + idx).name("Name" + idx).slug("Slug" + idx).abbreviation("Abbreviation" + idx)
                        .country("Country" + idx);
            }
            return builder.build();
        };
    }

    private static DtoSupplier<Movie> movieModel() {
        return (idx, shape) -> {
            MovieDTO.Builder builder = new MovieDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(84755L + idx).name("Name" + idx).slug("Slug" + idx).image("Image" + idx).score(1079D + idx)
                        .runtime(46L + idx).lastUpdated("LastUpdated" + idx)
                        .status(create(statusModel(), idx))
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .aliases(createTwo(aliasModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static DtoSupplier<MovieDetails> movieDetailsModel() {
        return (idx, shape) -> {
            MovieDetailsDTO.Builder builder = new MovieDetailsDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(33247L + idx).image("Image" + idx).name("Name" + idx).slug("Slug" + idx).score(32D + idx)
                        .boxOffice("BoxOffice" + idx).budget("Budget" + idx).originalCountry("OriginalCountry" + idx)
                        .originalLanguage("OriginalLanguage" + idx).runtime(160L + idx).lastUpdated("LastUpdated" + idx)
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .aliases(createTwo(aliasModel(), listOffset))
                        .artworks(createTwo(artworkModel(), listOffset))
                        .addAudioLanguages("AudioLanguage" + listOffset, "AudioLanguage" + (listOffset + 1))
                        .awards(createTwo(awardModel(), listOffset))
                        .characters(createTwo(characterModel(), listOffset))
                        .companies(create(companiesModel(), idx))
                        .contentRatings(createTwo(contentRatingModel(), listOffset))
                        .lists(createTwo(listModel(), listOffset))
                        .genres(createTwo(genreModel(), listOffset))
                        .releases(createTwo(releaseModel(), listOffset))
                        .remoteIds(createTwo(remoteIdModel(), listOffset))
                        .status(create(statusModel(), idx))
                        .studios(createTwo(studioModel(), listOffset))
                        .addSubtitleLanguages("SubtitleLanguage" + listOffset, "SubtitleLanguage" + (listOffset + 1))
                        .tagOptions(createTwo(tagOptionModel(), listOffset))
                        .trailers(createTwo(trailerModel(), listOffset))
                        .inspirations(createTwo(inspirationModel(), listOffset))
                        .productionCountries(createTwo(productionCountryModel(), listOffset))
                        .addSpokenLanguages("SpokenLanguage" + listOffset, "SpokenLanguage" + (listOffset + 1))
                        .firstRelease(create(releaseModel(), idx));
            }
            return builder.build();
        };
    }

    private static SimpleDtoSupplier<String> overviewTranslationModel() {
        return idx -> "OverviewTranslation" + idx;
    }

    private static DtoSupplier<People> peopleModel() {
        return (idx, shape) -> {
            PeopleDTO.Builder builder = new PeopleDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(11353L + idx).score(486L + idx).name("Name" + idx).image("Image" + idx)
                        .aliases(createTwo(aliasModel(), listOffset))
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static DtoSupplier<PeopleDetails> peopleDetailsModel() {
        return (idx, shape) -> {
            PeopleDetailsDTO.Builder builder = new PeopleDetailsDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.id(5874L + idx).name("Name" + idx).image("Image" + idx).score(23L + idx).birth("Birth" + idx)
                        .birthPlace("BirthPlace" + idx).death("Death" + idx).gender(2L + idx)
                        .aliases(createTwo(aliasModel(), listOffset))
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .awards(createTwo(awardModel(), listOffset))
                        .biographies(createTwo(biographyModel(), listOffset))
                        .characters(createTwo(characterModel(), listOffset))
                        .races(create(1, raceModel(), idx, shape))
                        .remoteIds(createTwo(remoteIdModel(), listOffset))
                        .tagOptions(createTwo(tagOptionModel(), listOffset))
                        .translations(create(translationsModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static SimpleDtoSupplier<PeopleType> peopleTypeModel() {
        return idx -> new PeopleTypeDTO.Builder().id(21L + idx).name("Name" + idx).build();
    }

    private static SimpleDtoSupplier<ProductionCountry> productionCountryModel() {
        return idx -> new ProductionCountryDTO.Builder().id(43L + idx).country("Country" + idx).name("Name" + idx)
                .build();
    }

    private static SimpleDtoSupplier<Race> raceModel() {
        return idx -> new RaceDTO.Builder().build();
    }

    private static SimpleDtoSupplier<Release> releaseModel() {
        return idx -> new ReleaseDTO.Builder().country("Country" + idx).date("Date" + idx).detail("Detail" + idx)
                .build();
    }

    private static SimpleDtoSupplier<RemoteId> remoteIdModel() {
        return idx -> new RemoteIdDTO.Builder().id("Id" + idx).type(3069L + idx).sourceName("SourceName" + idx).build();
    }

    private static DtoSupplier<Season> seasonModel() {
        return (idx, shape) -> {
            SeasonDTO.Builder builder = new SeasonDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.seriesId(95873L + idx).number(5L + idx).id(47747L + idx).name("Name" + idx).slug("Slug" + idx)
                        .abbreviation("Abbreviation" + idx).country("Country" + idx).image("Image" + idx)
                        .imageType(486L + idx)
                        .type(create(seasonTypeModel(), idx))
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .companies(create(companiesModel(), idx));
            }
            return builder.build();
        };
    }

    private static DtoSupplier<SeasonDetails> seasonDetailsModel() {
        return (idx, shape) -> {
            SeasonDetailsDTO.Builder builder = new SeasonDetailsDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.seriesId(30013L + idx).number(11L + idx).id(67409L + idx).name("Name" + idx).slug("Slug" + idx)
                        .abbreviation("Abbreviation" + idx).country("Country" + idx).image("Image" + idx)
                        .imageType(654L + idx)
                        .type(create(seasonTypeModel(), idx))
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .companies(create(companiesModel(), idx))
                        .artwork(createTwo(artworkModel(), listOffset))
                        .episodes(createTwo(episodeModel(), listOffset))
                        .trailers(createTwo(trailerModel(), listOffset))
                        .tagOptions(createTwo(tagOptionModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static SimpleDtoSupplier<SeasonType> seasonTypeModel() {
        return idx -> new SeasonTypeDTO.Builder().id(6953L + idx).name("Name" + idx).type("Type" + idx).build();
    }

    private static DtoSupplier<Series> seriesModel() {
        return (idx, shape) -> {
            SeriesDTO.Builder builder = new SeriesDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.nextAired("NextAired" + idx).score(67D + idx).lastAired("LastAired" + idx)
                        .originalCountry("OriginalCountry" + idx).defaultSeasonType(468L + idx).country("Country" + idx)
                        .originalLanguage("OriginalLanguage" + idx).isOrderRandomized(TRUE).id(34874L + idx)
                        .name("Name" + idx).slug("Slug" + idx).image("Image" + idx).abbreviation("Abbreviation" + idx)
                        .lastUpdated("LastUpdated" + idx).averageRuntime(41L + idx)
                        .status(create(statusModel(), idx))
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .aliases(createTwo(aliasModel(), listOffset)).firstAired("FirstAired" + idx)
                ;
            }
            return builder.build();
        };
    }

    private static SimpleDtoSupplier<SeriesAirsDays> seriesAirsDaysModel() {
        return idx -> new SeriesAirsDaysDTO.Builder().onFriday(TRUE).onMonday(TRUE).onSaturday(TRUE).onSunday(TRUE)
                .onThursday(TRUE).onTuesday(TRUE).onWednesday(TRUE).build();
    }

    private static DtoSupplier<SeriesDetails> seriesDetailsModel() {
        return (idx, shape) -> {
            SeriesDetailsDTO.Builder builder = new SeriesDetailsDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.nextAired("NextAired" + idx).score(4D + idx).defaultSeasonType(56L + idx)
                        .isOrderRandomized(TRUE).firstAired("FirstAired" + idx).lastAired("LastAired" + idx)
                        .id(923L + idx).name("Name" + idx).slug("Slug" + idx).image("Image" + idx)
                        .originalCountry("OriginalCountry" + idx).originalLanguage("OriginalLanguage" + idx)
                        .isOrderRandomized(TRUE).airsTime("AirsTime" + idx).abbreviation("Abbreviation" + idx)
                        .country("Country" + idx).lastUpdated("LastUpdated" + idx).airsTimeUTC("AirsTimeUTC" + idx)
                        .averageRuntime(50L + idx)
                        .status(create(statusModel(), idx))
                        .airsDays(create(seriesAirsDaysModel(), idx))
                        .companies(createTwo(companyModel(), listOffset))
                        .nameTranslations(createTwo(nameTranslationModel(), listOffset))
                        .overviewTranslations(createTwo(overviewTranslationModel(), listOffset))
                        .aliases(createTwo(aliasModel()))
                        .artworks(createTwo(artworkDetailsModel(), listOffset))
                        .genres(createTwo(genreModel(), listOffset))
                        .trailers(createTwo(trailerModel(), listOffset))
                        .lists(createTwo(listModel(), listOffset))
                        .remoteIds(createTwo(remoteIdModel(), listOffset))
                        .characters(createTwo(characterModel(), listOffset))
                        .seasons(createTwo(seasonModel(), listOffset))
                        .translations(create(translationsModel(), listOffset))
                        .episodes(createTwo(episodeModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static DtoSupplier<SeriesEpisodes> seriesEpisodesModel() {
        return (idx, shape) -> {
            SeriesEpisodesDTO.Builder builder = new SeriesEpisodesDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.series(create(seriesModel(), idx))
                        .episodes(createTwo(episodeModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static SimpleDtoSupplier<SourceType> sourceTypeModel() {
        return idx -> new SourceTypeDTO.Builder().id(629L + idx).name("Name" + idx).slug("Slug" + idx)
                .prefix("Prefix" + idx).postfix("Postfix" + idx).sort(16L + idx).build();
    }

    private static SimpleDtoSupplier<Status> statusModel() {
        return idx -> new StatusDTO.Builder().id(546L + idx).keepUpdated(TRUE).name("Name" + idx)
                .recordType("RecordType" + idx).build();
    }

    private static SimpleDtoSupplier<Studio> studioModel() {
        return idx -> new StudioDTO.Builder().id(86L + idx).name("Name" + idx).parentStudio(15L + idx).build();
    }

    private static DtoSupplier<TagOption> tagOptionModel() {
        return (idx, shape) -> {
            TagOptionDTO.Builder builder = new TagOptionDTO.Builder();
            if (shape == FULL) {
                builder.id(5796L + idx).name("Name" + idx).tag(42L + idx).tagName("TagName" + idx)
                        .helpText("HelpText" + idx);
            }
            return builder.build();
        };
    }

    private static DtoSupplier<Trailer> trailerModel() {
        return (idx, shape) -> {
            TrailerDTO.Builder builder = new TrailerDTO.Builder();
            if (shape == FULL) {
                builder.id(6033L + idx).name("Name" + idx).language("Language" + idx).url("Url" + idx);
            }
            return builder.build();
        };
    }

    private static DtoSupplier<Translations> translationsModel() {
        return (idx, shape) -> {
            TranslationsDTO.Builder builder = new TranslationsDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.addAliases("Alias" + listOffset, "Alias" + (listOffset + 1))
                        .nameTranslations(createTwo(translationModel(), listOffset))
                        .overviewTranslations(createTwo(translationModel(), listOffset));
            }
            return builder.build();
        };
    }

    private static DtoSupplier<Translation> translationModel() {
        return (idx, shape) -> {
            TranslationDTO.Builder builder = new TranslationDTO.Builder();
            if (shape == FULL) {
                int listOffset = (idx << 1) - 1;
                builder.language("Language" + idx).isPrimary(true).name("Name" + idx).isAlias(true)
                        .overview("Overview" + idx).tagline("Tagline" + idx)
                        .addAliases("Alias" + listOffset, "Alias" + (listOffset + 1));
            }
            return builder.build();
        };
    }

    private static SimpleDtoSupplier<EntityUpdate> entityUpdateModel() {
        return idx -> new EntityUpdateDTO.Builder().recordId(39003L + idx).method("Method" + idx)
                .timeStamp(16245743L + idx).entityType("EntityType" + idx).build();
    }

    /**
     * Functional interface for the creation of shaped test object DTOs. Invokes the object creation method with an
     * index and a {@link Shape} parameter.
     *
     * @param <T> The DTOs interface type
     */
    @FunctionalInterface
    private interface DtoSupplier<T> {
        T get(Integer idx, Shape shape);
    }

    /**
     * Functional interface for the creation of simple, unshaped test object DTOs. Invokes the object creation method
     * with an index parameter only.
     *
     * @param <T> The DTOs interface type
     */
    @FunctionalInterface
    private interface SimpleDtoSupplier<T> extends DtoSupplier<T> {
        T get(Integer idx);

        default T get(Integer idx, Shape shape) {
            return get(idx);
        }
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
     * Returns the responses JSON content as String
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

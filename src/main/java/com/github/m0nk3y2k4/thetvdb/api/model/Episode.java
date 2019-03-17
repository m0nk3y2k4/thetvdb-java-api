package com.github.m0nk3y2k4.thetvdb.api.model;

import java.util.List;

public interface Episode extends EpisodeAbstract {

    /**
     * Get the airedSeasonID

     * @return the airedSeasonID
     */
    Long getAiredSeasonID();

    /**
     * Get the airsAfterSeason

     * @return the airsAfterSeason
     */
    Long getAirsAfterSeason();

    /**
     * Get the airsBeforeEpisode

     * @return the airsBeforeEpisode
     */
    Long getAirsBeforeEpisode();

    /**
     * Get the airsBeforeSeason

     * @return the airsBeforeSeason
     */
    Long getAirsBeforeSeason();

    /**
     * Get the director
     * @deprecated  Will be removed in future API release
     *
     * @return the director
     */
    @Deprecated
    String getDirector();

    /**
     * Get the directors

     * @return the directors
     */
    List<String> getDirectors();

    /**
     * Get the dvdChapter

     * @return the dvdChapter
     */
    Long getDvdChapter();

    /**
     * Get the dvdDiscid

     * @return the dvdDiscid
     */
    String getDvdDiscid();

    /**
     * Get the filename

     * @return the filename
     */
    String getFilename();

    /**
     * Get the guestStars

     * @return the guestStars
     */
    List<String> getGuestStars();

    /**
     * Get the imdbId

     * @return the imdbId
     */
    String getImdbId();

    /**
     * Get the lastUpdatedBy

     * @return the lastUpdatedBy
     */
    String getLastUpdatedBy();

    /**
     * Get the productionCode

     * @return the productionCode
     */
    String getProductionCode();

    /**
     * Get the seriesId

     * @return the seriesId
     */
    String getSeriesId();

    /**
     * Get the showUrl

     * @return the showUrl
     */
    String getShowUrl();

    /**
     * Get the siteRating

     * @return the siteRating
     */
    Double getSiteRating();

    /**
     * Get the siteRatingCount

     * @return the siteRatingCount
     */
    Long getSiteRatingCount();

    /**
     * Get the thumbAdded

     * @return the thumbAdded
     */
    String getThumbAdded();

    /**
     * Get the thumbAuthor

     * @return the thumbAuthor
     */
    Long getThumbAuthor();

    /**
     * Get the thumbHeight

     * @return the thumbHeight
     */
    String getThumbHeight();

    /**
     * Get the thumbWidth

     * @return the thumbWidth
     */
    String getThumbWidth();

    /**
     * Get the writers

     * @return the writers
     */
    List<String> getWriters();
}

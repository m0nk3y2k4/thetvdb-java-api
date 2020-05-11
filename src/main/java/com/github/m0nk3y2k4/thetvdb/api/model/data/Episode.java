package com.github.m0nk3y2k4.thetvdb.api.model.data;

import java.util.List;

/**
 * Interface representing an <i>Episode</i> data transfert object.
 * <p/>
 * The methods of this class provide easy access to all episode related data which was returned by the remote service in JSON format.
 * <p/>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
 * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
 * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
 * relieve the API user of this task.
 */
public interface Episode {

    /**
     * Get the absoluteNumber
     *
     * @return the absoluteNumber
     */
    Long getAbsoluteNumber();

    /**
     * Get the airedEpisodeNumber
     *
     * @return the airedEpisodeNumber
     */
    Long getAiredEpisodeNumber();

    /**
     * Get the airedSeason
     *
     * @return the airedSeason
     */
    Long getAiredSeason();

    /**
     * Get the airsAfterSeason
     *
     * @return the airsAfterSeason
     */
    Long getAirsAfterSeason();

    /**
     * Get the airsBeforeEpisode
     *
     * @return the airsBeforeEpisode
     */
    Long getAirsBeforeEpisode();

    /**
     * Get the airsBeforeSeason
     *
     * @return the airsBeforeSeason
     */
    Long getAirsBeforeSeason();

    /**
     * Get the director
     * @deprecated  Will be removed in future API release
     *
     * @return the director
     */
    @Deprecated(since = "0.0.1", forRemoval = true)
    String getDirector();

    /**
     * Get the directors
     *
     * @return the directors
     */
    List<String> getDirectors();

    /**
     * Get the dvdChapter
     *
     * @return the dvdChapter
     */
    Long getDvdChapter();

    /**
     * Get the dvdDiscid
     *
     * @return the dvdDiscid
     */
    String getDvdDiscid();

    /**
     * Get the dvdEpisodeNumber
     *
     * @return the dvdEpisodeNumber
     */
    Long getDvdEpisodeNumber();

    /**
     * Get the dvdSeason
     *
     * @return the dvdSeason
     */
    Long getDvdSeason();

    /**
     * Get the episodeName
     *
     * @return the episodeName
     */
    String getEpisodeName();

    /**
     * Get the filename
     *
     * @return the filename
     */
    String getFilename();

    /**
     * Get the firstAired
     *
     * @return the firstAired
     */
    String getFirstAired();

    /**
     * Get the guestStars
     *
     * @return the guestStars
     */
    List<String> getGuestStars();

    /**
     * Get the id
     *
     * @return the id
     */
    Long getId();

    /**
     * Get the imdbId
     *
     * @return the imdbId
     */
    String getImdbId();

    /**
     * Get the lastUpdated
     *
     * @return the lastUpdated
     */
    Long getLastUpdated();

    /**
     * Get the lastUpdatedBy
     *
     * @return the lastUpdatedBy
     */
    String getLastUpdatedBy();

    /**
     * Get the overview
     *
     * @return the overview
     */
    String getOverview();

    /**
     * Get the productionCode
     *
     * @return the productionCode
     */
    String getProductionCode();

    /**
     * Get the seriesId
     *
     * @return the seriesId
     */
    String getSeriesId();

    /**
     * Get the showUrl
     *
     * @return the showUrl
     */
    String getShowUrl();

    /**
     * Get the siteRating
     *
     * @return the siteRating
     */
    Double getSiteRating();

    /**
     * Get the siteRatingCount
     *
     * @return the siteRatingCount
     */
    Long getSiteRatingCount();

    /**
     * Get the thumbAdded
     *
     * @return the thumbAdded
     */
    String getThumbAdded();

    /**
     * Get the thumbAuthor
     *
     * @return the thumbAuthor
     */
    Long getThumbAuthor();

    /**
     * Get the thumbHeight
     *
     * @return the thumbHeight
     */
    String getThumbHeight();

    /**
     * Get the thumbWidth
     *
     * @return the thumbWidth
     */
    String getThumbWidth();

    /**
     * Get the writers
     *
     * @return the writers
     */
    List<String> getWriters();
}

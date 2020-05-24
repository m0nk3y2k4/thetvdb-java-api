package com.github.m0nk3y2k4.thetvdb.api.model.data;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Interface representing an <i>Episode</i> data transfert object.
 * <p>
 * The methods of this class provide easy access to all episode related data which was returned by the remote service in JSON format.
 * Please note that, as the remote service declares all of the properties to be optional, most of the methods are marked
 * as {@link Nullable}. Methods returning collection-based values however will return an empty collection in case no corresponding
 * data was receieved.
 * <p>
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
    @Nullable Long getAbsoluteNumber();

    /**
     * Get the airedEpisodeNumber
     *
     * @return the airedEpisodeNumber
     */
    @Nullable Long getAiredEpisodeNumber();

    /**
     * Get the airedSeason
     *
     * @return the airedSeason
     */
    @Nullable Long getAiredSeason();

    /**
     * Get the airsAfterSeason
     *
     * @return the airsAfterSeason
     */
    @Nullable Long getAirsAfterSeason();

    /**
     * Get the airsBeforeEpisode
     *
     * @return the airsBeforeEpisode
     */
    @Nullable Long getAirsBeforeEpisode();

    /**
     * Get the airsBeforeSeason
     *
     * @return the airsBeforeSeason
     */
    @Nullable Long getAirsBeforeSeason();

    /**
     * Get the director
     * @deprecated  Will be removed in future API release
     *
     * @return the director
     */
    @Deprecated(since = "0.0.1", forRemoval = true)
    @Nullable String getDirector();

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
    @Nullable Long getDvdChapter();

    /**
     * Get the dvdDiscid
     *
     * @return the dvdDiscid
     */
    @Nullable String getDvdDiscid();

    /**
     * Get the dvdEpisodeNumber
     *
     * @return the dvdEpisodeNumber
     */
    @Nullable Long getDvdEpisodeNumber();

    /**
     * Get the dvdSeason
     *
     * @return the dvdSeason
     */
    @Nullable Long getDvdSeason();

    /**
     * Get the episodeName
     *
     * @return the episodeName
     */
    @Nullable String getEpisodeName();

    /**
     * Get the filename
     *
     * @return the filename
     */
    @Nullable String getFilename();

    /**
     * Get the firstAired
     *
     * @return the firstAired
     */
    @Nullable String getFirstAired();

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
    @Nullable Long getId();

    /**
     * Get the imdbId
     *
     * @return the imdbId
     */
    @Nullable String getImdbId();

    /**
     * Get the lastUpdated
     *
     * @return the lastUpdated
     */
    @Nullable Long getLastUpdated();

    /**
     * Get the lastUpdatedBy
     *
     * @return the lastUpdatedBy
     */
    @Nullable String getLastUpdatedBy();

    /**
     * Get the overview
     *
     * @return the overview
     */
    @Nullable String getOverview();

    /**
     * Get the productionCode
     *
     * @return the productionCode
     */
    @Nullable String getProductionCode();

    /**
     * Get the seriesId
     *
     * @return the seriesId
     */
    @Nullable String getSeriesId();

    /**
     * Get the showUrl
     *
     * @return the showUrl
     */
    @Nullable String getShowUrl();

    /**
     * Get the siteRating
     *
     * @return the siteRating
     */
    @Nullable Double getSiteRating();

    /**
     * Get the siteRatingCount
     *
     * @return the siteRatingCount
     */
    @Nullable Long getSiteRatingCount();

    /**
     * Get the thumbAdded
     *
     * @return the thumbAdded
     */
    @Nullable String getThumbAdded();

    /**
     * Get the thumbAuthor
     *
     * @return the thumbAuthor
     */
    @Nullable Long getThumbAuthor();

    /**
     * Get the thumbHeight
     *
     * @return the thumbHeight
     */
    @Nullable String getThumbHeight();

    /**
     * Get the thumbWidth
     *
     * @return the thumbWidth
     */
    @Nullable String getThumbWidth();

    /**
     * Get the writers
     *
     * @return the writers
     */
    List<String> getWriters();
}

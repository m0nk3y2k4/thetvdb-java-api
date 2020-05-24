package com.github.m0nk3y2k4.thetvdb.api.model.data;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Interface representing a <i>Series</i> data transfert object.
 * <p>
 * The methods of this class provide easy access to all series related data which was returned by the remote service in JSON format.
 * Please note that, as the remote service declares all of the properties to be optional, most of the methods are marked
 * as {@link Nullable}. Methods returning collection-based values however will return an empty collection in case no corresponding
 * data was receieved.
 * <p>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
 * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
 * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
 * relieve the API user of this task.
 */
public interface Series extends SeriesSearchResult {

    /**
     * Get the added
     *
     * @return the added
     */
    @Nullable String getAdded();

    /**
     * Get the airsDayOfWeek
     *
     * @return the airsDayOfWeek
     */
    @Nullable String getAirsDayOfWeek();

    /**
     * Get the arisTime
     *
     * @return the arisTime
     */
    @Nullable String getArisTime();

    /**
     * Get the genre
     *
     * @return the genre
     */
    List<String> getGenre();

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
     * Get the networkId
     *
     * @return the networkId
     */
    @Nullable String getNetworkId();

    /**
     * Get the rating
     *
     * @return the rating
     */
    @Nullable String getRating();

    /**
     * Get the runtime
     *
     * @return the runtime
     */
    @Nullable String getRuntime();

    /**
     * Get the seriesId
     *
     * @return the seriesId
     */
    @Nullable String getSeriesId();

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
     * Get the zap2itId
     *
     * @return the zap2itId
     */
    @Nullable String getZap2itId();
}

package com.github.m0nk3y2k4.thetvdb.api.model;

import java.util.List;

public interface Series extends SeriesAbstract {

    /**
     * Get the added

     * @return the added
     */
    String getAdded();

    /**
     * Get the airsDayOfWeek

     * @return the airsDayOfWeek
     */
    String getAirsDayOfWeek();

    /**
     * Get the arisTime

     * @return the arisTime
     */
    String getArisTime();

    /**
     * Get the genre

     * @return the genre
     */
    List<String> getGenre();

    /**
     * Get the imdbId

     * @return the imdbId
     */
    String getImdbId();

    /**
     * Get the lastUpdated

     * @return the lastUpdated
     */
    Long getLastUpdated();

    /**
     * Get the networkId

     * @return the networkId
     */
    String getNetworkId();

    /**
     * Get the rating

     * @return the rating
     */
    String getRating();

    /**
     * Get the runtime

     * @return the runtime
     */
    String getRuntime();

    /**
     * Get the seriesId

     * @return the seriesId
     */
    String getSeriesId();

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
     * Get the zap2itId

     * @return the zap2itId
     */
    String getZap2itId();
}

package com.github.m0nk3y2k4.thetvdb.api.model.data;

import java.util.List;

/**
 * Interface representing a <i>Series search result</i> data transfert object.
 * <p/>
 * The methods of this class provide easy access to all series search result related data which was returned by the remote service in JSON format.
 * <p/>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
 * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
 * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
 * relieve the API user of this task.
 */
public interface SeriesSearchResult {

    /**
     * Get the aliases
     *
     * @return the aliases
     */
    List<String> getAliases();

    /**
     * Get the banner
     *
     * @return the banner
     */
    String getBanner();

    /**
     * Get the firstAired
     *
     * @return the firstAired
     */
    String getFirstAired();

    /**
     * Get the id
     *
     * @return the id
     */
    Long getId();

    /**
     * Get the network
     *
     * @return the network
     */
    String getNetwork();

    /**
     * Get the overview
     *
     * @return the overview
     */
    String getOverview();

    /**
     * Get the seriesName
     *
     * @return the seriesName
     */
    String getSeriesName();

    /**
     * Get the slug
     *
     * @return the slug
     */
    String getSlug();
    /**
     * Get the status
     *
     * @return the status
     */
    String getStatus();
}

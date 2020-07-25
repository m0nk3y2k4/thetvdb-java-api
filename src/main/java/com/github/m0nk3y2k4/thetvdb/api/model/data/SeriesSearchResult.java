package com.github.m0nk3y2k4.thetvdb.api.model.data;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Interface representing a <i>Series search result</i> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all series search result related data which was returned by the remote service in JSON format.
 * Please note that, as the remote service declares all of the properties to be optional, most of the methods are marked
 * as {@link Nullable}. Methods returning collection-based values however will return an empty collection in case no corresponding
 * data was received.
 * <p><br>
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
    @Nullable String getBanner();

    /**
     * Get the firstAired
     *
     * @return the firstAired
     */
    @Nullable String getFirstAired();

    /**
     * Get the id
     *
     * @return the id
     */
    @Nullable Long getId();

    /**
     * Get the image
     *
     * @return the image
     */
    @Nullable String getImage();

    /**
     * Get the network
     *
     * @return the network
     */
    @Nullable String getNetwork();

    /**
     * Get the overview
     *
     * @return the overview
     */
    @Nullable String getOverview();

    /**
     * Get the poster
     *
     * @return the poster
     */
    @Nullable String getPoster();

    /**
     * Get the seriesName
     *
     * @return the seriesName
     */
    @Nullable String getSeriesName();

    /**
     * Get the slug
     *
     * @return the slug
     */
    @Nullable String getSlug();

    /**
     * Get the status
     *
     * @return the status
     */
    @Nullable String getStatus();
}

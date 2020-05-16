package com.github.m0nk3y2k4.thetvdb.api.model.data;

import javax.annotation.Nullable;

/**
 * Interface representing an <i>Image summary</i> data transfert object.
 * <p/>
 * The methods of this class provide easy access to all image summary related data which was returned by the remote service in JSON format.
 * Please note that, as the remote service declares all of the properties to be optional, most of the methods are marked
 * as {@link Nullable}. Methods returning collection-based values however will return an empty collection in case no corresponding
 * data was receieved.
 * <p/>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
 * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
 * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
 * relieve the API user of this task.
 */
public interface ImageSummary {

    /**
     * Get the fanartCount
     *
     * @return the fanartCount
     */
    @Nullable Long getFanartCount();

    /**
     * Get the posterCount
     *
     * @return the posterCount
     */
    @Nullable Long getPosterCount();

    /**
     * Get the seasonCount
     *
     * @return the seasonCount
     */
    @Nullable Long getSeasonCount();

    /**
     * Get the seasonwideCount
     *
     * @return the seasonwideCount
     */
    @Nullable Long getSeasonwideCount();

    /**
     * Get the seriesCount
     *
     * @return the seriesCount
     */
    @Nullable Long getSeriesCount();
}

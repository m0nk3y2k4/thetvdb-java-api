package com.github.m0nk3y2k4.thetvdb.api.model.data;

import javax.annotation.Nullable;

/**
 * Interface representing an <i>Image summary</i> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all image summary related data which was returned by the remote service in JSON format.
 * Please note that, as the remote service declares all of the properties to be optional, most of the methods are marked
 * as {@link Nullable}. Methods returning collection-based values however will return an empty collection in case no corresponding
 * data was received.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
 * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
 * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
 * relieve the API user of this task.
 */
public interface ImageSummary {

    /**
     * Get the value of the {<em>{@code data.fanart}</em>} JSON property
     *
     * @return The <em>{@code fanart}</em> property from the received JSON
     */
    @Nullable Long getFanartCount();

    /**
     * Get the value of the {<em>{@code data.poster}</em>} JSON property
     *
     * @return The <em>{@code poster}</em> property from the received JSON
     */
    @Nullable Long getPosterCount();

    /**
     * Get the value of the {<em>{@code data.season}</em>} JSON property
     *
     * @return The <em>{@code season}</em> property from the received JSON
     */
    @Nullable Long getSeasonCount();

    /**
     * Get the value of the {<em>{@code data.seasonwide}</em>} JSON property
     *
     * @return The <em>{@code seasonwide}</em> property from the received JSON
     */
    @Nullable Long getSeasonwideCount();

    /**
     * Get the value of the {<em>{@code data.series}</em>} JSON property
     *
     * @return The <em>{@code series}</em> property from the received JSON
     */
    @Nullable Long getSeriesCount();
}

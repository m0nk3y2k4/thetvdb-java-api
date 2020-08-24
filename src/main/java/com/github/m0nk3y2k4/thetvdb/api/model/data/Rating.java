package com.github.m0nk3y2k4.thetvdb.api.model.data;

import javax.annotation.Nullable;

/**
 * Interface representing a <i>Rating</i> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all rating related data which was returned by the remote service in JSON format.
 * Please note that, as the remote service declares all of the properties to be optional, most of the methods are marked
 * as {@link Nullable}. Methods returning collection-based values however will return an empty collection in case no corresponding
 * data was received.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
 * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
 * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
 * relieve the API user of this task.
 */
public interface Rating {

    /**
     * Get the value of the {<em>{@code data.rating}</em>} JSON property
     *
     * @return The <em>{@code rating}</em> property from the received JSON
     */
    @Nullable Double getRating();

    /**
     * Get the value of the {<em>{@code data.ratingItemId}</em>} JSON property
     *
     * @return The <em>{@code ratingItemId}</em> property from the received JSON
     */
    @Nullable Long getRatingItemId();

    /**
     * Get the value of the {<em>{@code data.ratingType}</em>} JSON property
     *
     * @return The <em>{@code ratingType}</em> property from the received JSON
     */
    @Nullable String getRatingType();
}

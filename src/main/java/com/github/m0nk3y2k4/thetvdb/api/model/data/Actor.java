package com.github.m0nk3y2k4.thetvdb.api.model.data;

import javax.annotation.Nullable;

/**
 * Interface representing an <i>Actor</i> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all actor related data which was returned by the remote service in JSON format.
 * Please note that, as the remote service declares all of the properties to be optional, most of the methods are marked
 * as {@link Nullable}. Methods returning collection-based values however will return an empty collection in case no corresponding
 * data was received.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
 * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
 * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
 * relieve the API user of this task.
 */
public interface Actor {

    /**
     * Get the value of the {<em>{@code data.id}</em>} JSON property
     *
     * @return The <em>{@code id}</em> property from the received JSON
     */
    @Nullable Long getId();

    /**
     * Get the value of the {<em>{@code data.image}</em>} JSON property
     *
     * @return The <em>{@code image}</em> property from the received JSON
     */
    @Nullable String getImage();

    /**
     * Get the value of the {<em>{@code data.imageAdded}</em>} JSON property
     *
     * @return The <em>{@code imageAdded}</em> property from the received JSON
     */
    @Nullable String getImageAdded();

    /**
     * Get the value of the {<em>{@code data.imageAuthor}</em>} JSON property
     *
     * @return The <em>{@code imageAuthor}</em> property from the received JSON
     */
    @Nullable Long getImageAuthor();

    /**
     * Get the value of the {<em>{@code data.lastUpdated}</em>} JSON property
     *
     * @return The <em>{@code lastUpdated}</em> property from the received JSON
     */
    @Nullable String getLastUpdated();

    /**
     * Get the value of the {<em>{@code data.name}</em>} JSON property
     *
     * @return The <em>{@code name}</em> property from the received JSON
     */
    @Nullable String getName();

    /**
     * Get the value of the {<em>{@code data.role}</em>} JSON property
     *
     * @return The <em>{@code role}</em> property from the received JSON
     */
    @Nullable String getRole();

    /**
     * Get the value of the {<em>{@code data.seriesId}</em>} JSON property
     *
     * @return The <em>{@code seriesId}</em> property from the received JSON
     */
    @Nullable Long getSeriesId();

    /**
     * Get the value of the {<em>{@code data.sortOrder}</em>} JSON property
     *
     * @return The <em>{@code sortOrder}</em> property from the received JSON
     */
    @Nullable Long getSortOrder();
}

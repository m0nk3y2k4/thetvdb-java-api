package com.github.m0nk3y2k4.thetvdb.api.model.data;


/**
 * Interface representing an
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/ArtworkType">ArtworkType</a>
 * data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all artwork type related data which was returned by the remote
 * service in JSON format.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface ArtworkType {

    /**
     * Get the value of the {<em>{@code data.id}</em>} JSON property
     *
     * @return The <em>{@code id}</em> property from the received JSON
     */
    Long getId();

    /**
     * Get the value of the {<em>{@code data.name}</em>} JSON property
     *
     * @return The <em>{@code name}</em> property from the received JSON
     */
    String getName();

    /**
     * Get the value of the {<em>{@code data.recordType}</em>} JSON property
     *
     * @return The <em>{@code recordType}</em> property from the received JSON
     */
    String getRecordType();

    /**
     * Get the value of the {<em>{@code data.slug}</em>} JSON property
     *
     * @return The <em>{@code slug}</em> property from the received JSON
     */
    String getSlug();

    /**
     * Get the value of the {<em>{@code data.imageFormat}</em>} JSON property
     *
     * @return The <em>{@code imageFormat}</em> property from the received JSON
     */
    String getImageFormat();

    /**
     * Get the value of the {<em>{@code data.width}</em>} JSON property
     *
     * @return The <em>{@code width}</em> property from the received JSON
     */
    Long getWidth();

    /**
     * Get the value of the {<em>{@code data.height}</em>} JSON property
     *
     * @return The <em>{@code height}</em> property from the received JSON
     */
    Long getHeight();

    /**
     * Get the value of the {<em>{@code data.thumbWidth}</em>} JSON property
     *
     * @return The <em>{@code thumbWidth}</em> property from the received JSON
     */
    Long getThumbWidth();

    /**
     * Get the value of the {<em>{@code data.thumbHeight}</em>} JSON property
     *
     * @return The <em>{@code thumbHeight}</em> property from the received JSON
     */
    Long getThumbHeight();
}

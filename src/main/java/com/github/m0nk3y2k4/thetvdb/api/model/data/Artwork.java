package com.github.m0nk3y2k4.thetvdb.api.model.data;


/**
 * Interface representing an
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/ArtworkBaseRecord">ArtworkBaseRecord</a>
 * data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all artwork related data which was returned by the remote service in
 * JSON format.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface Artwork {

    /**
     * Get the value of the {<em>{@code data.id}</em>} JSON property
     *
     * @return The <em>{@code id}</em> property from the received JSON
     */
    Long getId();

    /**
     * Get the value of the {<em>{@code data.image}</em>} JSON property
     *
     * @return The <em>{@code image}</em> property from the received JSON
     */
    String getImage();

    /**
     * Get the value of the {<em>{@code data.thumbnail}</em>} JSON property
     *
     * @return The <em>{@code thumbnail}</em> property from the received JSON
     */
    String getThumbnail();

    /**
     * Get the value of the {<em>{@code data.language}</em>} JSON property
     *
     * @return The <em>{@code language}</em> property from the received JSON
     */
    String getLanguage();

    /**
     * Get the value of the {<em>{@code data.type}</em>} JSON property
     *
     * @return The <em>{@code type}</em> property from the received JSON
     */
    Long getType();

    /**
     * Get the value of the {<em>{@code data.score}</em>} JSON property
     *
     * @return The <em>{@code score}</em> property from the received JSON
     */
    Double getScore();
}

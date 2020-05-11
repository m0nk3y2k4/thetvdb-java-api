package com.github.m0nk3y2k4.thetvdb.api.model.data;

/**
 * Interface representing an <i>Image</i> data transfert object.
 * <p/>
 * The methods of this class provide easy access to all image related data which was returned by the remote service in JSON format.
 * <p/>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
 * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
 * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
 * relieve the API user of this task.
 */
public interface Image {

    /**
     * Get the fileName
     *
     * @return the fileName
     */
    String getFileName();

    /**
     * Get the id
     *
     * @return the id
     */
    Long getId();

    /**
     * Get the keyType
     *
     * @return the keyType
     */
    String getKeyType();

    /**
     * Get the languageId
     *
     * @return the languageId
     */
    Long getLanguageId();

    /**
     * Get the ratingAverage
     *
     * @return the ratingAverage
     */
    Double getRatingAverage();

    /**
     * Get the ratingCount
     *
     * @return the ratingCount
     */
    Integer getRatingCount();

    /**
     * Get the resolution
     *
     * @return the resolution
     */
    String getResolution();

    /**
     * Get the subKey
     *
     * @return the subKey
     */
    String getSubKey();

    /**
     * Get the thumbnail
     *
     * @return the thumbnail
     */
    String getThumbnail();
}

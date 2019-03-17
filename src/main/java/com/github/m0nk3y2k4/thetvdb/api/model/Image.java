package com.github.m0nk3y2k4.thetvdb.api.model;

public interface Image {

    /**
     * Get the fileName

     * @return the fileName
     */
    String getFileName();

    /**
     * Get the id

     * @return the id
     */
    Long getId();

    /**
     * Get the keyType

     * @return the keyType
     */
    String getKeyType();

    /**
     * Get the languageId

     * @return the languageId
     */
    Long getLanguageId();

    /**
     * Get the ratingAverage

     * @return the ratingAverage
     */
    Double getRatingAverage();

    /**
     * Get the ratingCount

     * @return the ratingCount
     */
    Integer getRatingCount();

    /**
     * Get the resolution

     * @return the resolution
     */
    String getResolution();

    /**
     * Get the subKey

     * @return the subKey
     */
    String getSubKey();

    /**
     * Get the thumbnail

     * @return the thumbnail
     */
    String getThumbnail();
}

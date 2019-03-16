package com.github.m0nk3y2k4.thetvdb.shared.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {

    private String fileName;
    private Long id;
    private String keyType;
    private Long languageId;
    private Double ratingAverage;
    private Integer ratingCount;
    private String resolution;
    private String subKey;
    private String thumbnail;

    /**
     * Get the fileName

     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set the fileName
     *
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Get the id

     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id
     *
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the keyType

     * @return the keyType
     */
    public String getKeyType() {
        return keyType;
    }

    /**
     * Set the keyType
     *
     * @param keyType the keyType to set
     */
    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    /**
     * Get the languageId

     * @return the languageId
     */
    public Long getLanguageId() {
        return languageId;
    }

    /**
     * Set the languageId
     *
     * @param languageId the languageId to set
     */
    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    /**
     * Get the ratingAverage

     * @return the ratingAverage
     */
    public Double getRatingAverage() {
        return ratingAverage;
    }

    /**
     * Set the ratingAverage
     *
     * @param ratingAverage the ratingAverage to set
     */
    public void setRatingAverage(Double ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    /**
     * Get the ratingCount

     * @return the ratingCount
     */
    public Integer getRatingCount() {
        return ratingCount;
    }

    /**
     * Set the ratingCount
     *
     * @param ratingCount the ratingCount to set
     */
    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    /**
     * Get the resolution

     * @return the resolution
     */
    public String getResolution() {
        return resolution;
    }

    /**
     * Set the resolution
     *
     * @param resolution the resolution to set
     */
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    /**
     * Get the subKey

     * @return the subKey
     */
    public String getSubKey() {
        return subKey;
    }

    /**
     * Set the subKey
     *
     * @param subKey the subKey to set
     */
    public void setSubKey(String subKey) {
        this.subKey = subKey;
    }

    /**
     * Get the thumbnail

     * @return the thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * Set the thumbnail
     *
     * @param thumbnail the thumbnail to set
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @JsonProperty("ratingsInfo")
    private void mapLanguage(Map<String,Object> ratingInfo) {
        this.ratingAverage = Double.valueOf(ratingInfo.get("average").toString());
        this.ratingCount = (Integer)ratingInfo.get("count");
    }
}

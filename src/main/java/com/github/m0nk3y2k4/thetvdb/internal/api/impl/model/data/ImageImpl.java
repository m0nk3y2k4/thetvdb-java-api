package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Image;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageImpl implements Image {

    private String fileName;
    private Long id;
    private String keyType;
    private Long languageId;
    private Double ratingAverage;
    private Integer ratingCount;
    private String resolution;
    private String subKey;
    private String thumbnail;

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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
    public void mapLanguage(Map<String,Object> ratingInfo) {
        this.ratingAverage = Double.valueOf(ratingInfo.get("average").toString());
        this.ratingCount = (Integer)ratingInfo.get("count");
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.keyType, this.fileName);
    }
}

package com.github.m0nk3y2k4.thetvdb.shared.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageQueryParameter {

    private String keyType;
    private String languageId;
    private List<String> resolution;
    private List<String> subKey;

    public ImageQueryParameter() {
        this.resolution = new ArrayList<>();
        this.subKey = new ArrayList<>();
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
    public String getLanguageId() {
        return languageId;
    }

    /**
     * Set the languageId
     *
     * @param languageId the languageId to set
     */
    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    /**
     * Get the resolution

     * @return the resolution
     */
    public List<String> getResolution() {
        return resolution;
    }

    /**
     * Set the resolution
     *
     * @param resolution the resolution to set
     */
    public void setResolution(List<String> resolution) {
        this.resolution = resolution;
    }

    /**
     * Get the subKey

     * @return the subKey
     */
    public List<String> getSubKey() {
        return subKey;
    }

    /**
     * Set the subKey
     *
     * @param subKey the subKey to set
     */
    public void setSubKey(List<String> subKey) {
        this.subKey = subKey;
    }
}

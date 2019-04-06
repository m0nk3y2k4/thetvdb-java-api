package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ImageQueryParameter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageQueryParameterImpl implements ImageQueryParameter {

    private String keyType;
    private String languageId;
    private List<String> resolution;
    private List<String> subKey;

    public ImageQueryParameterImpl() {
        this.resolution = new ArrayList<>();
        this.subKey = new ArrayList<>();
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

    @Override
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

    @Override
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

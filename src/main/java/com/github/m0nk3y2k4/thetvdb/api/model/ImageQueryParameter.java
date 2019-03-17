package com.github.m0nk3y2k4.thetvdb.api.model;

import java.util.List;

public interface ImageQueryParameter {

    /**
     * Get the keyType

     * @return the keyType
     */
    String getKeyType();

    /**
     * Get the languageId

     * @return the languageId
     */
    String getLanguageId();

    /**
     * Get the resolution

     * @return the resolution
     */
    List<String> getResolution();

    /**
     * Get the subKey

     * @return the subKey
     */
    List<String> getSubKey();
}

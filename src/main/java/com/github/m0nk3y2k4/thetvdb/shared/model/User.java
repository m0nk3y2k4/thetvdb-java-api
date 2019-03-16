package com.github.m0nk3y2k4.thetvdb.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String favoritesDisplaymode;
    private String language;
    private String userName;

    /**
     * Get the favoritesDisplaymode

     * @return the favoritesDisplaymode
     */
    public String getFavoritesDisplaymode() {
        return favoritesDisplaymode;
    }

    /**
     * Set the favoritesDisplaymode
     *
     * @param favoritesDisplaymode the favoritesDisplaymode to set
     */
    public void setFavoritesDisplaymode(String favoritesDisplaymode) {
        this.favoritesDisplaymode = favoritesDisplaymode;
    }

    /**
     * Get the language

     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Set the language
     *
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Get the userName

     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the userName
     *
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}

package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.m0nk3y2k4.thetvdb.api.model.data.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserImpl implements User {

    private String favoritesDisplaymode;
    private String language;
    private String userName;

    @Override
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

    @Override
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

    @Override
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

    @Override
    public String toString() {
        return this.userName;
    }
}

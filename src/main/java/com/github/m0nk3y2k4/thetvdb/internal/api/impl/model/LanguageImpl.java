package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.m0nk3y2k4.thetvdb.api.model.Language;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LanguageImpl implements Language {

    private String abbreviation;
    private String englishName;
    private long id;
    private String name;

    @Override
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Set the abbreviation
     *
     * @param abbreviation the abbreviation to set
     */
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public String getEnglishName() {
        return englishName;
    }

    /**
     * Set the englishName
     *
     * @param englishName the englishName to set
     */
    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    @Override
    public long getId() {
        return id;
    }

    /**
     * Set the id
     *
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the name
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.name;
    }
}

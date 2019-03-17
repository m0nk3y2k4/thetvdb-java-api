package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.m0nk3y2k4.thetvdb.api.model.EpisodeAbstract;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EpisodeAbstractImpl implements EpisodeAbstract {

    private Long absoluteNumber;
    private Long airedEpisodeNumber;
    private Long airedSeason;
    private Long dvdEpisodeNumber;
    private Long dvdSeason;
    private String episodeName;
    private String firstAired;
    private Long id;
    private String episodeNameLanguage;
    private String overviewLanguage;
    private Long lastUpdated;
    private String overview;

    @Override
    public Long getAbsoluteNumber() {
        return absoluteNumber;
    }

    /**
     * Set the absoluteNumber
     *
     * @param absoluteNumber the absoluteNumber to set
     */
    public void setAbsoluteNumber(Long absoluteNumber) {
        this.absoluteNumber = absoluteNumber;
    }

    @Override
    public Long getAiredEpisodeNumber() {
        return airedEpisodeNumber;
    }

    /**
     * Set the airedEpisodeNumber
     *
     * @param airedEpisodeNumber the airedEpisodeNumber to set
     */
    public void setAiredEpisodeNumber(Long airedEpisodeNumber) {
        this.airedEpisodeNumber = airedEpisodeNumber;
    }

    @Override
    public Long getAiredSeason() {
        return airedSeason;
    }

    /**
     * Set the airedSeason
     *
     * @param airedSeason the airedSeason to set
     */
    public void setAiredSeason(Long airedSeason) {
        this.airedSeason = airedSeason;
    }

    @Override
    public Long getDvdEpisodeNumber() {
        return dvdEpisodeNumber;
    }

    /**
     * Set the dvdEpisodeNumber
     *
     * @param dvdEpisodeNumber the dvdEpisodeNumber to set
     */
    public void setDvdEpisodeNumber(Long dvdEpisodeNumber) {
        this.dvdEpisodeNumber = dvdEpisodeNumber;
    }

    @Override
    public Long getDvdSeason() {
        return dvdSeason;
    }

    /**
     * Set the dvdSeason
     *
     * @param dvdSeason the dvdSeason to set
     */
    public void setDvdSeason(Long dvdSeason) {
        this.dvdSeason = dvdSeason;
    }

    @Override
    public String getEpisodeName() {
        return episodeName;
    }

    /**
     * Set the episodeName
     *
     * @param episodeName the episodeName to set
     */
    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    @Override
    public String getFirstAired() {
        return firstAired;
    }

    /**
     * Set the firstAired
     *
     * @param firstAired the firstAired to set
     */
    public void setFirstAired(String firstAired) {
        this.firstAired = firstAired;
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
    public String getEpisodeNameLanguage() {
        return episodeNameLanguage;
    }

    /**
     * Set the episodeNameLanguage
     *
     * @param episodeNameLanguage the episodeNameLanguage to set
     */
    public void setEpisodeNameLanguage(String episodeNameLanguage) {
        this.episodeNameLanguage = episodeNameLanguage;
    }

    @Override
    public String getOverviewLanguage() {
        return overviewLanguage;
    }

    /**
     * Set the overviewLanguage
     *
     * @param overviewLanguage the overviewLanguage to set
     */
    public void setOverviewLanguage(String overviewLanguage) {
        this.overviewLanguage = overviewLanguage;
    }

    @Override
    public Long getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Set the lastUpdated
     *
     * @param lastUpdated the lastUpdated to set
     */
    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String getOverview() {
        return overview;
    }

    /**
     * Set the overview
     *
     * @param overview the overview to set
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }

    @JsonProperty("language")
    public void mapLanguage(Map<String,Object> language) {
        this.episodeNameLanguage = (String)language.get("episodeName");
        this.overviewLanguage = (String)language.get("overview");
    }
}

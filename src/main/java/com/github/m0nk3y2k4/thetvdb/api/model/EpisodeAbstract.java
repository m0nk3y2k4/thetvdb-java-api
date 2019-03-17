package com.github.m0nk3y2k4.thetvdb.api.model;

public interface EpisodeAbstract {

    /**
     * Get the absoluteNumber

     * @return the absoluteNumber
     */
    Long getAbsoluteNumber();

    /**
     * Get the airedEpisodeNumber

     * @return the airedEpisodeNumber
     */
    Long getAiredEpisodeNumber();

    /**
     * Get the airedSeason

     * @return the airedSeason
     */
    Long getAiredSeason();

    /**
     * Get the dvdEpisodeNumber

     * @return the dvdEpisodeNumber
     */
    Long getDvdEpisodeNumber();

    /**
     * Get the dvdSeason

     * @return the dvdSeason
     */
    Long getDvdSeason();

    /**
     * Get the episodeName

     * @return the episodeName
     */
    String getEpisodeName();

    /**
     * Get the firstAired

     * @return the firstAired
     */
    String getFirstAired();

    /**
     * Get the id

     * @return the id
     */
    Long getId();

    /**
     * Get the episodeNameLanguage

     * @return the episodeNameLanguage
     */
    String getEpisodeNameLanguage();

    /**
     * Get the overviewLanguage

     * @return the overviewLanguage
     */
    String getOverviewLanguage();

    /**
     * Get the lastUpdated

     * @return the lastUpdated
     */
    Long getLastUpdated();

    /**
     * Get the overview

     * @return the overview
     */
    String getOverview();
}

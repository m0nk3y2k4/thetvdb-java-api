package com.github.m0nk3y2k4.thetvdb.api.model;

import java.util.List;

public interface SeriesAbstract {

    /**
     * Get the aliases

     * @return the aliases
     */
    List<String> getAliases();

    /**
     * Get the banner

     * @return the banner
     */
    String getBanner();

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
     * Get the network

     * @return the network
     */
    String getNetwork();

    /**
     * Get the overview

     * @return the overview
     */
    String getOverview();

    /**
     * Get the seriesName

     * @return the seriesName
     */
    String getSeriesName();

    /**
     * Get the status

     * @return the status
     */
    String getStatus();
}

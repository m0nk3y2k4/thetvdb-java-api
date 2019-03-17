package com.github.m0nk3y2k4.thetvdb.api.model;

import java.util.List;

public interface SeriesSummary {

    /**
     * Get the airedEpisodes

     * @return the airedEpisodes
     */
    String getAiredEpisodes();

    /**
     * Get the airedSeasons

     * @return the airedSeasons
     */
    List<String> getAiredSeasons();

    /**
     * Get the dvdEpisodes

     * @return the dvdEpisodes
     */
    String getDvdEpisodes();

    /**
     * Get the dvdSeasons

     * @return the dvdSeasons
     */
    List<String> getDvdSeasons();
}

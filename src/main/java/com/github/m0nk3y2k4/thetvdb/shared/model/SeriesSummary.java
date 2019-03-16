package com.github.m0nk3y2k4.thetvdb.shared.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SeriesSummary {

    public String airedEpisodes;
    public List<String> airedSeasons;
    public String dvdEpisodes;
    public List<String> dvdSeasons;

    public SeriesSummary() {
        this.airedSeasons = new ArrayList<>();
        this.dvdSeasons = new ArrayList<>();
    }

    /**
     * Get the airedEpisodes

     * @return the airedEpisodes
     */
    public String getAiredEpisodes() {
        return airedEpisodes;
    }

    /**
     * Set the airedEpisodes
     *
     * @param airedEpisodes the airedEpisodes to set
     */
    public void setAiredEpisodes(String airedEpisodes) {
        this.airedEpisodes = airedEpisodes;
    }

    /**
     * Get the airedSeasons

     * @return the airedSeasons
     */
    public List<String> getAiredSeasons() {
        return airedSeasons;
    }

    /**
     * Set the airedSeasons
     *
     * @param airedSeasons the airedSeasons to set
     */
    public void setAiredSeasons(List<String> airedSeasons) {
        this.airedSeasons = airedSeasons;
    }

    /**
     * Get the dvdEpisodes

     * @return the dvdEpisodes
     */
    public String getDvdEpisodes() {
        return dvdEpisodes;
    }

    /**
     * Set the dvdEpisodes
     *
     * @param dvdEpisodes the dvdEpisodes to set
     */
    public void setDvdEpisodes(String dvdEpisodes) {
        this.dvdEpisodes = dvdEpisodes;
    }

    /**
     * Get the dvdSeasons

     * @return the dvdSeasons
     */
    public List<String> getDvdSeasons() {
        return dvdSeasons;
    }

    /**
     * Set the dvdSeasons
     *
     * @param dvdSeasons the dvdSeasons to set
     */
    public void setDvdSeasons(List<String> dvdSeasons) {
        this.dvdSeasons = dvdSeasons;
    }
}

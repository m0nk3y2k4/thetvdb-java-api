package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.m0nk3y2k4.thetvdb.api.model.SeriesSummary;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SeriesSummaryImpl implements SeriesSummary {

    public String airedEpisodes;
    public List<String> airedSeasons;
    public String dvdEpisodes;
    public List<String> dvdSeasons;

    public SeriesSummaryImpl() {
        this.airedSeasons = new ArrayList<>();
        this.dvdSeasons = new ArrayList<>();
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

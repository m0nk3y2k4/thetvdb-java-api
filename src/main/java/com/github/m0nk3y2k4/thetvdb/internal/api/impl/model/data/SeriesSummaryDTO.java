package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.m0nk3y2k4.thetvdb.api.model.data.SeriesSummary;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SeriesSummaryDTO implements SeriesSummary {

    private String airedEpisodes;
    private List<String> airedSeasons;
    private String dvdEpisodes;
    private List<String> dvdSeasons;

    public SeriesSummaryDTO() {
        this.airedSeasons = Collections.emptyList();
        this.dvdSeasons = Collections.emptyList();
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

    @Override
    public String toString() {
        return String.format("[Seasons/Episodes] Aired: %s/%s, DVD: %s/%s", this.airedSeasons, this.airedEpisodes, this.dvdSeasons, this.dvdEpisodes);
    }
}

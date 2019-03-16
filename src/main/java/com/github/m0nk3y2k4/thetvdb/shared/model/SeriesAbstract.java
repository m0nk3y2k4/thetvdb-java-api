package com.github.m0nk3y2k4.thetvdb.shared.model;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SeriesAbstract {

    private List<String> aliases;
    private String banner;
    private String firstAired;
    private Long id;
    private String network;
    private String overview;
    private String seriesName;
    private String status;

    public SeriesAbstract() {
        this.aliases = Collections.<String>emptyList();
    }

    /**
     * Get the aliases

     * @return the aliases
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * Set the aliases
     *
     * @param aliases the aliases to set
     */
    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    /**
     * Get the banner

     * @return the banner
     */
    public String getBanner() {
        return banner;
    }

    /**
     * Set the banner
     *
     * @param banner the banner to set
     */
    public void setBanner(String banner) {
        this.banner = banner;
    }

    /**
     * Get the firstAired

     * @return the firstAired
     */
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

    /**
     * Get the id

     * @return the id
     */
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

    /**
     * Get the network

     * @return the network
     */
    public String getNetwork() {
        return network;
    }

    /**
     * Set the network
     *
     * @param network the network to set
     */
    public void setNetwork(String network) {
        this.network = network;
    }

    /**
     * Get the overview

     * @return the overview
     */
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

    /**
     * Get the seriesName

     * @return the seriesName
     */
    public String getSeriesName() {
        return seriesName;
    }

    /**
     * Set the seriesName
     *
     * @param seriesName the seriesName to set
     */
    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    /**
     * Get the status

     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the status
     *
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.seriesName;
    }
}

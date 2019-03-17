package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.m0nk3y2k4.thetvdb.api.model.SeriesAbstract;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SeriesAbstractImpl implements SeriesAbstract {

    private List<String> aliases;
    private String banner;
    private String firstAired;
    private Long id;
    private String network;
    private String overview;
    private String seriesName;
    private String status;

    public SeriesAbstractImpl() {
        this.aliases = Collections.<String>emptyList();
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

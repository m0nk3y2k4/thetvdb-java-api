package com.github.m0nk3y2k4.thetvdb.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageSummary {

    @JsonProperty("fanart")
    private Long fanartCount;
    @JsonProperty("poster")
    private Long posterCount;
    @JsonProperty("season")
    private Long seasonCount;
    @JsonProperty("seasonwide")
    private Long seasonwideCount;
    @JsonProperty("series")
    private Long seriesCount;

    /**
     * Get the fanartCount

     * @return the fanartCount
     */
    public Long getFanartCount() {
        return fanartCount;
    }

    /**
     * Set the fanartCount
     *
     * @param fanartCount the fanartCount to set
     */
    public void setFanartCount(Long fanartCount) {
        this.fanartCount = fanartCount;
    }

    /**
     * Get the posterCount

     * @return the posterCount
     */
    public Long getPosterCount() {
        return posterCount;
    }

    /**
     * Set the posterCount
     *
     * @param posterCount the posterCount to set
     */
    public void setPosterCount(Long posterCount) {
        this.posterCount = posterCount;
    }

    /**
     * Get the seasonCount

     * @return the seasonCount
     */
    public Long getSeasonCount() {
        return seasonCount;
    }

    /**
     * Set the seasonCount
     *
     * @param seasonCount the seasonCount to set
     */
    public void setSeasonCount(Long seasonCount) {
        this.seasonCount = seasonCount;
    }

    /**
     * Get the seasonwideCount

     * @return the seasonwideCount
     */
    public Long getSeasonwideCount() {
        return seasonwideCount;
    }

    /**
     * Set the seasonwideCount
     *
     * @param seasonwideCount the seasonwideCount to set
     */
    public void setSeasonwideCount(Long seasonwideCount) {
        this.seasonwideCount = seasonwideCount;
    }

    /**
     * Get the seriesCount

     * @return the seriesCount
     */
    public Long getSeriesCount() {
        return seriesCount;
    }

    /**
     * Set the seriesCount
     *
     * @param seriesCount the seriesCount to set
     */
    public void setSeriesCount(Long seriesCount) {
        this.seriesCount = seriesCount;
    }
}

package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.m0nk3y2k4.thetvdb.api.model.data.ImageSummary;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageSummaryImpl implements ImageSummary {

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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

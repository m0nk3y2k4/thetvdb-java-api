package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Series;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SeriesImpl extends SeriesSearchResultImpl implements Series {

    private String added;
    private String airsDayOfWeek;
    private String arisTime;
    private List<String> genre;
    private String imdbId;
    private Long lastUpdated;
    private String networkId;
    private String rating;
    private String runtime;
    private String seriesId;
    private Double siteRating;
    private Long siteRatingCount;
    private String zap2itId;

    public SeriesImpl() {
        super();
        this.genre = Collections.emptyList();
    }

    @Override
    public String getAdded() {
        return added;
    }

    /**
     * Set the added
     *
     * @param added the added to set
     */
    public void setAdded(String added) {
        this.added = added;
    }

    @Override
    public String getAirsDayOfWeek() {
        return airsDayOfWeek;
    }

    /**
     * Set the airsDayOfWeek
     *
     * @param airsDayOfWeek the airsDayOfWeek to set
     */
    public void setAirsDayOfWeek(String airsDayOfWeek) {
        this.airsDayOfWeek = airsDayOfWeek;
    }

    @Override
    public String getArisTime() {
        return arisTime;
    }

    /**
     * Set the arisTime
     *
     * @param arisTime the arisTime to set
     */
    public void setArisTime(String arisTime) {
        this.arisTime = arisTime;
    }

    @Override
    public List<String> getGenre() {
        return genre;
    }

    /**
     * Set the genre
     *
     * @param genre the genre to set
     */
    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    @Override
    public String getImdbId() {
        return imdbId;
    }

    /**
     * Set the imdbId
     *
     * @param imdbId the imdbId to set
     */
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    @Override
    public Long getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Set the lastUpdated
     *
     * @param lastUpdated the lastUpdated to set
     */
    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String getNetworkId() {
        return networkId;
    }

    /**
     * Set the networkId
     *
     * @param networkId the networkId to set
     */
    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    @Override
    public String getRating() {
        return rating;
    }

    /**
     * Set the rating
     *
     * @param rating the rating to set
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String getRuntime() {
        return runtime;
    }

    /**
     * Set the runtime
     *
     * @param runtime the runtime to set
     */
    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    @Override
    public String getSeriesId() {
        return seriesId;
    }

    /**
     * Set the seriesId
     *
     * @param seriesId the seriesId to set
     */
    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    @Override
    public Double getSiteRating() {
        return siteRating;
    }

    /**
     * Set the siteRating
     *
     * @param siteRating the siteRating to set
     */
    public void setSiteRating(Double siteRating) {
        this.siteRating = siteRating;
    }

    @Override
    public Long getSiteRatingCount() {
        return siteRatingCount;
    }

    /**
     * Set the siteRatingCount
     *
     * @param siteRatingCount the siteRatingCount to set
     */
    public void setSiteRatingCount(Long siteRatingCount) {
        this.siteRatingCount = siteRatingCount;
    }

    @Override
    public String getZap2itId() {
        return zap2itId;
    }

    /**
     * Set the zap2itId
     *
     * @param zap2itId the zap2itId to set
     */
    public void setZap2itId(String zap2itId) {
        this.zap2itId = zap2itId;
    }

    @Override
    public String toString() {
        return this.getSeriesName();
    }
}

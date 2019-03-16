package com.github.m0nk3y2k4.thetvdb.shared.model;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Episode extends EpisodeAbstract {

    private Long airedSeasonID;
    private Long airsAfterSeason;
    private Long airsBeforeEpisode;
    private Long airsBeforeSeason;
    private String director;
    private List<String> directors;
    private Long dvdChapter;
    private String dvdDiscid;
    private String filename;
    private List<String> guestStars;
    private String imdbId;
    private String lastUpdatedBy;
    private String productionCode;
    private String seriesId;
    private String showUrl;
    private Double siteRating;
    private Long siteRatingCount;
    private String thumbAdded;
    private Long thumbAuthor;
    private String thumbHeight;
    private String thumbWidth;
    private List<String> writers;

    public Episode() {
        super();
        this.directors = Collections.<String>emptyList();
        this.guestStars = Collections.<String>emptyList();
        this.writers = Collections.<String>emptyList();
    }

    /**
     * Get the airedSeasonID

     * @return the airedSeasonID
     */
    public Long getAiredSeasonID() {
        return airedSeasonID;
    }

    /**
     * Set the airedSeasonID
     *
     * @param airedSeasonID the airedSeasonID to set
     */
    public void setAiredSeasonID(Long airedSeasonID) {
        this.airedSeasonID = airedSeasonID;
    }

    /**
     * Get the airsAfterSeason

     * @return the airsAfterSeason
     */
    public Long getAirsAfterSeason() {
        return airsAfterSeason;
    }

    /**
     * Set the airsAfterSeason
     *
     * @param airsAfterSeason the airsAfterSeason to set
     */
    public void setAirsAfterSeason(Long airsAfterSeason) {
        this.airsAfterSeason = airsAfterSeason;
    }

    /**
     * Get the airsBeforeEpisode

     * @return the airsBeforeEpisode
     */
    public Long getAirsBeforeEpisode() {
        return airsBeforeEpisode;
    }

    /**
     * Set the airsBeforeEpisode
     *
     * @param airsBeforeEpisode the airsBeforeEpisode to set
     */
    public void setAirsBeforeEpisode(Long airsBeforeEpisode) {
        this.airsBeforeEpisode = airsBeforeEpisode;
    }

    /**
     * Get the airsBeforeSeason

     * @return the airsBeforeSeason
     */
    public Long getAirsBeforeSeason() {
        return airsBeforeSeason;
    }

    /**
     * Set the airsBeforeSeason
     *
     * @param airsBeforeSeason the airsBeforeSeason to set
     */
    public void setAirsBeforeSeason(Long airsBeforeSeason) {
        this.airsBeforeSeason = airsBeforeSeason;
    }

    /**
     * Get the director
     * @deprecated  Will be removed in future API release
     *
     * @return the director
     */
    @Deprecated
    public String getDirector() {
        return director;
    }

    /**
     * Set the director
     * @deprecated  Will be removed in future API release
     *
     * @param director the director to set
     */
    @Deprecated
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * Get the directors

     * @return the directors
     */
    public List<String> getDirectors() {
        return directors;
    }

    /**
     * Set the directors
     *
     * @param directors the directors to set
     */
    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    /**
     * Get the dvdChapter

     * @return the dvdChapter
     */
    public Long getDvdChapter() {
        return dvdChapter;
    }

    /**
     * Set the dvdChapter
     *
     * @param dvdChapter the dvdChapter to set
     */
    public void setDvdChapter(Long dvdChapter) {
        this.dvdChapter = dvdChapter;
    }

    /**
     * Get the dvdDiscid

     * @return the dvdDiscid
     */
    public String getDvdDiscid() {
        return dvdDiscid;
    }

    /**
     * Set the dvdDiscid
     *
     * @param dvdDiscid the dvdDiscid to set
     */
    public void setDvdDiscid(String dvdDiscid) {
        this.dvdDiscid = dvdDiscid;
    }

    /**
     * Get the filename

     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Set the filename
     *
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Get the guestStars

     * @return the guestStars
     */
    public List<String> getGuestStars() {
        return guestStars;
    }

    /**
     * Set the guestStars
     *
     * @param guestStars the guestStars to set
     */
    public void setGuestStars(List<String> guestStars) {
        this.guestStars = guestStars;
    }

    /**
     * Get the imdbId

     * @return the imdbId
     */
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

    /**
     * Get the lastUpdatedBy

     * @return the lastUpdatedBy
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Set the lastUpdatedBy
     *
     * @param lastUpdatedBy the lastUpdatedBy to set
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Get the productionCode

     * @return the productionCode
     */
    public String getProductionCode() {
        return productionCode;
    }

    /**
     * Set the productionCode
     *
     * @param productionCode the productionCode to set
     */
    public void setProductionCode(String productionCode) {
        this.productionCode = productionCode;
    }

    /**
     * Get the seriesId

     * @return the seriesId
     */
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

    /**
     * Get the showUrl

     * @return the showUrl
     */
    public String getShowUrl() {
        return showUrl;
    }

    /**
     * Set the showUrl
     *
     * @param showUrl the showUrl to set
     */
    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

    /**
     * Get the siteRating

     * @return the siteRating
     */
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

    /**
     * Get the siteRatingCount

     * @return the siteRatingCount
     */
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

    /**
     * Get the thumbAdded

     * @return the thumbAdded
     */
    public String getThumbAdded() {
        return thumbAdded;
    }

    /**
     * Set the thumbAdded
     *
     * @param thumbAdded the thumbAdded to set
     */
    public void setThumbAdded(String thumbAdded) {
        this.thumbAdded = thumbAdded;
    }

    /**
     * Get the thumbAuthor

     * @return the thumbAuthor
     */
    public Long getThumbAuthor() {
        return thumbAuthor;
    }

    /**
     * Set the thumbAuthor
     *
     * @param thumbAuthor the thumbAuthor to set
     */
    public void setThumbAuthor(Long thumbAuthor) {
        this.thumbAuthor = thumbAuthor;
    }

    /**
     * Get the thumbHeight

     * @return the thumbHeight
     */
    public String getThumbHeight() {
        return thumbHeight;
    }

    /**
     * Set the thumbHeight
     *
     * @param thumbHeight the thumbHeight to set
     */
    public void setThumbHeight(String thumbHeight) {
        this.thumbHeight = thumbHeight;
    }

    /**
     * Get the thumbWidth

     * @return the thumbWidth
     */
    public String getThumbWidth() {
        return thumbWidth;
    }

    /**
     * Set the thumbWidth
     *
     * @param thumbWidth the thumbWidth to set
     */
    public void setThumbWidth(String thumbWidth) {
        this.thumbWidth = thumbWidth;
    }

    /**
     * Get the writers

     * @return the writers
     */
    public List<String> getWriters() {
        return writers;
    }

    /**
     * Set the writers
     *
     * @param writers the writers to set
     */
    public void setWriters(List<String> writers) {
        this.writers = writers;
    }

}

package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.m0nk3y2k4.thetvdb.api.model.Episode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EpisodeImpl extends EpisodeAbstractImpl implements Episode {

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

    public EpisodeImpl() {
        super();
        this.directors = Collections.emptyList();
        this.guestStars = Collections.emptyList();
        this.writers = Collections.emptyList();
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

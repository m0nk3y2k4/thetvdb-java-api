package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.m0nk3y2k4.thetvdb.api.model.Actor;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ActorImpl implements Actor {

    private Long id;
    private String image;
    private String imageAdded;
    private Long imageAuthor;
    private String lastUpdated;
    private String name;
    private String role;
    private Long seriesId;
    private Long sortOrder;

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
    public String getImage() {
        return image;
    }

    /**
     * Set the image
     *
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String getImageAdded() {
        return imageAdded;
    }

    /**
     * Set the imageAdded
     *
     * @param imageAdded the imageAdded to set
     */
    public void setImageAdded(String imageAdded) {
        this.imageAdded = imageAdded;
    }

    @Override
    public Long getImageAuthor() {
        return imageAuthor;
    }

    /**
     * Set the imageAuthor
     *
     * @param imageAuthor the imageAuthor to set
     */
    public void setImageAuthor(Long imageAuthor) {
        this.imageAuthor = imageAuthor;
    }

    @Override
    public String getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Set the lastUpdated
     *
     * @param lastUpdated the lastUpdated to set
     */
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the name
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getRole() {
        return role;
    }

    /**
     * Set the role
     *
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Long getSeriesId() {
        return seriesId;
    }

    /**
     * Set the seriesId
     *
     * @param seriesId the seriesId to set
     */
    public void setSeriesId(Long seriesId) {
        this.seriesId = seriesId;
    }

    @Override
    public Long getSortOrder() {
        return sortOrder;
    }

    /**
     * Set the sortOrder
     *
     * @param sortOrder the sortOrder to set
     */
    public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }
}

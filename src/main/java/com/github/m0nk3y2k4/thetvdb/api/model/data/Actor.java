package com.github.m0nk3y2k4.thetvdb.api.model.data;

public interface Actor {

    /**
     * Get the id
     *
     * @return the id
     */
    Long getId();

    /**
     * Get the image
     *
     * @return the image
     */
    String getImage();

    /**
     * Get the imageAdded
     *
     * @return the imageAdded
     */
    String getImageAdded();

    /**
     * Get the imageAuthor
     *
     * @return the imageAuthor
     */
    Long getImageAuthor();

    /**
     * Get the lastUpdated
     *
     * @return the lastUpdated
     */
    String getLastUpdated();

    /**
     * Get the name
     *
     * @return the name
     */
    String getName();

    /**
     * Get the role
     *
     * @return the role
     */
    String getRole();

    /**
     * Get the seriesId
     *
     * @return the seriesId
     */
    Long getSeriesId();

    /**
     * Get the sortOrder
     *
     * @return the sortOrder
     */
    Long getSortOrder();
}

package com.github.m0nk3y2k4.thetvdb.api.model;

public interface Rating {

    /**
     * Get the rating

     * @return the rating
     */
    Double getRating();

    /**
     * Get the ratingItemId

     * @return the ratingItemId
     */
    Long getRatingItemId();

    /**
     * Get the ratingType

     * @return the ratingType
     */
    String getRatingType();
}

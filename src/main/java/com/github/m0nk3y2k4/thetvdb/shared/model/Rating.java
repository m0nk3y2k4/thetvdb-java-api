package com.github.m0nk3y2k4.thetvdb.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Rating {

    private Double rating;
    private Long ratingItemId;
    private String ratingType;

    /**
     * Get the rating

     * @return the rating
     */
    public Double getRating() {
        return rating;
    }

    /**
     * Set the rating
     *
     * @param rating the rating to set
     */
    public void setRating(Double rating) {
        this.rating = rating;
    }

    /**
     * Get the ratingItemId

     * @return the ratingItemId
     */
    public Long getRatingItemId() {
        return ratingItemId;
    }

    /**
     * Set the ratingItemId
     *
     * @param ratingItemId the ratingItemId to set
     */
    public void setRatingItemId(Long ratingItemId) {
        this.ratingItemId = ratingItemId;
    }

    /**
     * Get the ratingType

     * @return the ratingType
     */
    public String getRatingType() {
        return ratingType;
    }

    /**
     * Set the ratingType
     *
     * @param ratingType the ratingType to set
     */
    public void setRatingType(String ratingType) {
        this.ratingType = ratingType;
    }
}

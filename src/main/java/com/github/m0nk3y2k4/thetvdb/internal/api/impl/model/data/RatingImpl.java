package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Rating;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RatingImpl implements Rating {

    private Double rating;
    private Long ratingItemId;
    private String ratingType;

    @Override
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

    @Override
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

    @Override
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

    @Override
    public String toString() {
        return String.format("[%s] %s: %s", this.ratingType, this.ratingItemId, this.rating);
    }
}

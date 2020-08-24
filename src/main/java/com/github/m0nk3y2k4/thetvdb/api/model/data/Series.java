package com.github.m0nk3y2k4.thetvdb.api.model.data;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Interface representing a <i>Series</i> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all series related data which was returned by the remote service in JSON format.
 * Please note that, as the remote service declares all of the properties to be optional, most of the methods are marked
 * as {@link Nullable}. Methods returning collection-based values however will return an empty collection in case no corresponding
 * data was received.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
 * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
 * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
 * relieve the API user of this task.
 */
public interface Series extends SeriesSearchResult {

    /**
     * Get the value of the {<em>{@code data.added}</em>} JSON property
     *
     * @return The <em>{@code added}</em> property from the received JSON
     */
    @Nullable String getAdded();

    /**
     * Get the value of the {<em>{@code data.airsDayOfWeek}</em>} JSON property
     *
     * @return The <em>{@code airsDayOfWeek}</em> property from the received JSON
     */
    @Nullable String getAirsDayOfWeek();

    /**
     * Get the value of the {<em>{@code data.airsTime}</em>} JSON property
     *
     * @return The <em>{@code airsTime}</em> property from the received JSON
     */
    @Nullable String getAirsTime();

    /**
     * Get all values of the {<em>{@code data.genre}</em>} JSON property in a list
     *
     * @return The <em>{@code genre}</em> property from the received JSON as list
     */
    List<String> getGenre();

    /**
     * Get the value of the {<em>{@code data.imdbId}</em>} JSON property
     *
     * @return The <em>{@code imdbId}</em> property from the received JSON
     */
    @Nullable String getImdbId();

    /**
     * Get the value of the {<em>{@code data.lastUpdated}</em>} JSON property
     *
     * @return The <em>{@code lastUpdated}</em> property from the received JSON
     */
    @Nullable Long getLastUpdated();

    /**
     * Get the value of the {<em>{@code data.networkId}</em>} JSON property
     *
     * @return The <em>{@code networkId}</em> property from the received JSON
     */
    @Nullable String getNetworkId();

    /**
     * Get the value of the {<em>{@code data.rating}</em>} JSON property
     *
     * @return The <em>{@code rating}</em> property from the received JSON
     */
    @Nullable String getRating();

    /**
     * Get the value of the {<em>{@code data.runtime}</em>} JSON property
     *
     * @return The <em>{@code runtime}</em> property from the received JSON
     */
    @Nullable String getRuntime();

    /**
     * Get the value of the {<em>{@code data.seriesId}</em>} JSON property
     *
     * @return The <em>{@code seriesId}</em> property from the received JSON
     */
    @Nullable String getSeriesId();

    /**
     * Get the value of the {<em>{@code data.siteRating}</em>} JSON property
     *
     * @return The <em>{@code siteRating}</em> property from the received JSON
     */
    @Nullable Double getSiteRating();

    /**
     * Get the value of the {<em>{@code data.siteRatingCount}</em>} JSON property
     *
     * @return The <em>{@code siteRatingCount}</em> property from the received JSON
     */
    @Nullable Long getSiteRatingCount();

    /**
     * Get the value of the {<em>{@code data.zap2itId}</em>} JSON property
     *
     * @return The <em>{@code zap2itId}</em> property from the received JSON
     */
    @Nullable String getZap2itId();
}

package com.github.m0nk3y2k4.thetvdb.api.model.data;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Interface representing a <i>Series summary</i> data transfert object.
 * <p><br>
 * The methods of this class provide easy access to all series summary related data which was returned by the remote service in JSON format.
 * Please note that, as the remote service declares all of the properties to be optional, most of the methods are marked
 * as {@link Nullable}. Methods returning collection-based values however will return an empty collection in case no corresponding
 * data was receieved.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
 * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
 * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
 * relieve the API user of this task.
 */
public interface SeriesSummary {

    /**
     * Get the number of all aired episodes for this series
     *
     * @return the airedEpisodes
     */
    @Nullable String getAiredEpisodes();

    /**
     * Get the airedSeasons
     *
     * @return the airedSeasons
     */
    List<String> getAiredSeasons();

    /**
     * Get the number of all dvd episodes for this series
     *
     * @return the dvdEpisodes
     */
    @Nullable String getDvdEpisodes();

    /**
     * Get the dvdSeasons
     *
     * @return the dvdSeasons
     */
    List<String> getDvdSeasons();
}

package com.github.m0nk3y2k4.thetvdb.api.model.data;

import java.util.List;


/**
 * Interface representing a
 * <a target="_blank" href="https://app.swaggerhub.com/apis-docs/tvdb/tvdb-api-v4/4.0.1#/SeasonBaseRecord">SeasonBaseRecord</a>
 * data transfer object.
 * <p><br>
 * The methods of this class provide easy access to basic season related data which was returned by the remote service
 * in JSON format. Methods returning collection-based values will return an empty collection in case no corresponding
 * data was received.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service
 * in order to facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although
 * there will be no intense post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of
 * them to improve the usability and relieve the API user of this task.
 */
public interface Season {

    /**
     * Get the value of the {<em>{@code data.id}</em>} JSON property
     *
     * @return The <em>{@code id}</em> property from the received JSON
     */
    Long getId();

    /**
     * Get the value of the {<em>{@code data.seriesId}</em>} JSON property
     *
     * @return The <em>{@code seriesId}</em> property from the received JSON
     */
    Long getSeriesId();

    /**
     * Get the value of the {<em>{@code data.type}</em>} JSON property
     *
     * @return The <em>{@code type}</em> property from the received JSON
     */
    Long getType();

    /**
     * Get the value of the {<em>{@code data.name}</em>} JSON property
     *
     * @return The <em>{@code name}</em> property from the received JSON
     */
    String getName();

    /**
     * Get the value of the {<em>{@code data.number}</em>} JSON property
     *
     * @return The <em>{@code number}</em> property from the received JSON
     */
    Long getNumber();

    /**
     * Get the value of the {<em>{@code data.nameTranslations}</em>} JSON property
     *
     * @return The <em>{@code nameTranslations}</em> property from the received JSON
     */
    List<String> getNameTranslations();

    /**
     * Get the value of the {<em>{@code data.overviewTranslations}</em>} JSON property
     *
     * @return The <em>{@code overviewTranslations}</em> property from the received JSON
     */
    List<String> getOverviewTranslations();

    /**
     * Get the value of the {<em>{@code data.image}</em>} JSON property
     *
     * @return The <em>{@code image}</em> property from the received JSON
     */
    String getImage();

    /**
     * Get the value of the {<em>{@code data.imageType}</em>} JSON property
     *
     * @return The <em>{@code imageType}</em> property from the received JSON
     */
    Double getImageType();

    /**
     * Get the value of the {<em>{@code data.network}</em>} JSON property
     *
     * @return The <em>{@code network}</em> property from the received JSON
     */
    // ToDo: SeasonBaseRecord does currently not reference NetworkBaseRecord. Check this again after the
    //  next API update.
    Network getNetwork();
}

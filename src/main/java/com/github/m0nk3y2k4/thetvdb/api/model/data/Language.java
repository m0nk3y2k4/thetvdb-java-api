package com.github.m0nk3y2k4.thetvdb.api.model.data;

/**
 * Interface representing a <i>Language</i> data transfert object.
 * <p/>
 * The methods of this class provide easy access to all language related data which was returned by the remote service in JSON format.
 * <p/>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
 * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
 * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
 * relieve the API user of this task.
 */
public interface Language {

    /**
     * Get the abbreviation
     *
     * @return the abbreviation
     */
    String getAbbreviation();

    /**
     * Get the englishName
     *
     * @return the englishName
     */
    String getEnglishName();

    /**
     * Get the id
     *
     * @return the id
     */
    Long getId();

    /**
     * Get the name
     *
     * @return the name
     */
    String getName();
}

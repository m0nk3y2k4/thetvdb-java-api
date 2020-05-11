package com.github.m0nk3y2k4.thetvdb.api.model.data;

/**
 * Interface representing an <i>User</i> data transfert object.
 * <p/>
 * The methods of this class provide easy access to all user related data which was returned by the remote service in JSON format.
 * <p/>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
 * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
 * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
 * relieve the API user of this task.
 */
public interface User {

    /**
     * Get the favoritesDisplaymode
     *
     * @return the favoritesDisplaymode
     */
    String getFavoritesDisplaymode();

    /**
     * Get the language
     *
     * @return the language
     */
    String getLanguage();

    /**
     * Get the userName
     *
     * @return the userName
     */
    String getUserName();
}

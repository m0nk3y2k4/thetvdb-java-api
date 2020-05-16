package com.github.m0nk3y2k4.thetvdb.api.model.data;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Interface representing an <i>Image query parameter</i> data transfert object.
 * <p/>
 * The methods of this class provide easy access to all image query parameter related data which was returned by the remote service in JSON format.
 * Please note that, as the remote service declares all of the properties to be optional, all methods of this interface are marked
 * as {@link Nullable}.
 * <p/>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
 * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
 * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
 * relieve the API user of this task.
 */
public interface ImageQueryParameter {

    /**
     * Get the keyType
     *
     * @return the keyType
     */
    @Nullable String getKeyType();

    /**
     * Get the languageId
     *
     * @return the languageId
     */
    @Nullable String getLanguageId();

    /**
     * Get the resolution
     *
     * @return the resolution
     */
    @Nullable List<String> getResolution();

    /**
     * Get the subKey
     *
     * @return the subKey
     */
    @Nullable List<String> getSubKey();
}

package com.github.m0nk3y2k4.thetvdb.api.model;


import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Interface for extended remote API responses. Some API routes will not only return the actually requested data but will also report additional
 * information with regards to error and pagination handling.
 * <p/>
 * <b>Note:</b><br/>
 * The type and amount of additional information returned by the remote service depends on the actual API route. Some will only return the requested
 * records, other will also report additional error information in case invalid filter parameters have been used by the client. API calls that
 * support pagination might also add some additional information which can be used to navigate through the "pages".
 * <p/>
 *
 * @param <T> The actual payload DTO based on the data returned by the remote service
 */
public interface APIResponse<T> {

    /**
     * Returns the actually requested records mapped as Java DTO.
     *
     * @return Data returned by the API call
     */
    @Nullable T getData();

    /**
     * Returns an Optional representing the additional error information returned by the remote service. If the request was fully compliant or if the
     * requested resource does not support additional error reporting the returned Optional might be empty.
     *
     * @return Optional containing additional error details or empty Optional if no errors occured or error reporting is not supported by the requested resource
     */
    Optional<JSONErrors> getErrors();

    /**
     * Returns an Optional representing the additional paging information returned by the remote service. If the requested resource does not support pagination
     * the returned Optional might be empty.
     *
     * @return Optional containing additional paging information or empty Optional if pagination is not supported by the requested resource
     */
    Optional<Links> getLinks();

    /**
     * Interface representing optional soft errors that might occur while requesting data from the remote service
     */
    interface JSONErrors {

        /**
         * Returns invalid filters passed to the route
         *
         * @return the invalidFilters
         */
        @Nullable List<String> getInvalidFilters();

        /**
         * Returns invalid language or translation missing
         *
         * @return the invalidLanguage
         */
        @Nullable String getInvalidLanguage();

        /**
         * Returns invalid query params passed to the route
         *
         * @return the invalidQueryParams
         */
        @Nullable List<String> getInvalidQueryParams();
    }

    /**
     * Interface representing optional paging information for remote service requests supporting pagination
     */
    interface Links {

        /**
         * Get the number of the first available page
         *
         * @return the number of the first available page
         */
        @Nullable Integer getFirst();

        /**
         * Get the number of the last available page
         *
         * @return the number of the last available page
         */
        @Nullable Integer getLast();

        /**
         * Get the number of the next page (if available)
         *
         * @return the number of the next page
         */
        @Nullable Integer getNext();

        /**
         * Get the number of the previous page (if available)
         *
         * @return the number of the previous page
         */
        @Nullable Integer getPrevious();
    }
}

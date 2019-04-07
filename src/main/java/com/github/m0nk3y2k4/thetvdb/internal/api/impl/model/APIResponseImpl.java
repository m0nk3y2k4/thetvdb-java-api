package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.m0nk3y2k4.thetvdb.api.model.APIResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class APIResponseImpl<T> implements APIResponse<T> {

    /** Requested record data */
    private T data;
    /** Additional error information */
    private JSONErrors errors;
    /** Additional paging information */
    private Links links;

    @Override
    public T getData() {
        return data;
    }

    /**
     * Set the data
     *
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public Optional<JSONErrors> getErrors() {
        return Optional.ofNullable(errors);
    }

    /**
     * Set the errors
     *
     * @param errors the errors to set
     */
    public void setErrors(JSONErrors errors) {
        this.errors = errors;
    }

    @Override
    public Optional<Links> getLinks() {
        return Optional.ofNullable(links);
    }

    /**
     * Set the links
     *
     * @param links the links to set
     */
    public void setLinks(Links links) {
        this.links = links;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JSONErrorsImpl implements JSONErrors {

        /** JSON properties mapped as Java types */
        private List<String> invalidFilters;
        private String invalidLanguage;
        private List<String> invalidQueryParams;

        public JSONErrorsImpl() {
            super();
            this.invalidFilters = Collections.emptyList();
            this.invalidQueryParams = Collections.emptyList();
        }

        @Override
        public List<String> getInvalidFilters() {
            return invalidFilters;
        }

        /**
         * Set the invalidFilters
         *
         * @param invalidFilters the invalidFilters to set
         */
        public void setInvalidFilters(List<String> invalidFilters) {
            this.invalidFilters = invalidFilters;
        }

        @Override
        public String getInvalidLanguage() {
            return invalidLanguage;
        }

        /**
         * Set the invalidLanguage
         *
         * @param invalidLanguage the invalidLanguage to set
         */
        public void setInvalidLanguage(String invalidLanguage) {
            this.invalidLanguage = invalidLanguage;
        }

        @Override
        public List<String> getInvalidQueryParams() {
            return invalidQueryParams;
        }

        /**
         * Set the invalidQueryParams
         *
         * @param invalidQueryParams the invalidQueryParams to set
         */
        public void setInvalidQueryParams(List<String> invalidQueryParams) {
            this.invalidQueryParams = invalidQueryParams;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LinksImpl implements Links {

        /** JSON properties mapped as Java types */
        private int first;
        private int last;
        private int next;
        private int previous;

        @Override
        public int getFirst() {
            return first;
        }

        /**
         * Set the first
         *
         * @param first the first to set
         */
        public void setFirst(int first) {
            this.first = first;
        }

        @Override
        public int getLast() {
            return last;
        }

        /**
         * Set the last
         *
         * @param last the last to set
         */
        public void setLast(int last) {
            this.last = last;
        }

        @Override
        public int getNext() {
            return next;
        }

        /**
         * Set the next
         *
         * @param next the next to set
         */
        public void setNext(int next) {
            this.next = next;
        }

        @Override
        public int getPrevious() {
            return previous;
        }

        /**
         * Set the previous
         *
         * @param previous the previous to set
         */
        public void setPrevious(int previous) {
            this.previous = previous;
        }
    }
}

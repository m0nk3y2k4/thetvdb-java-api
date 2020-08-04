package com.github.m0nk3y2k4.thetvdb.testutils.parameterized;

/**
 * Small helper class used to wrap calls to some remote or API routes for easier parameterized JUnit testing
 * <p><br>
 * Accepted routes depend on the actual implementation that is used. Please check out the documentation of the implementing classes
 * for further details and examples. An additional textual description can be added which will be displayed as the default String
 * representation of this object. Provides a single method to invoke the underlying remote API route and to return it's response.
 */
public abstract class TestAPICall<T> {

    /** The actual API route represented by this object */
    protected final T route;

    /** Textual description of this remote API call */
    private final String description;

    /**
     * Creates a new API call referencing a given route with a given description
     *
     * @param route The route represented by this API call
     * @param description Textual description of this API call
     */
    protected TestAPICall(T route, String description) {
        this.route = route;
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}

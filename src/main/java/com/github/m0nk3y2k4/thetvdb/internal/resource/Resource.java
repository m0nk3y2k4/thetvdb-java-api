package com.github.m0nk3y2k4.thetvdb.internal.resource;

import java.util.function.Function;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public abstract class Resource {

    protected static final Function<Long, Boolean> ID_VALIDATOR = value -> value > 0;

    protected static final String PATH_ID = "id";

    protected static String createResource(@Nonnull String base, Object... pathParams) {
        return createResource(base, null, pathParams);
    }

    protected static String createResource(@Nonnull String base, @CheckForNull String specific, Object... pathParams) {
        StringBuilder resourceBuilder = new StringBuilder(base);

        if (specific != null) {
            resourceBuilder.append(specific);
        }

        for (Object param : pathParams) {
            resourceBuilder.append("/").append(param);
        }

        return resourceBuilder.toString();
    }
}

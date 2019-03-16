package com.github.m0nk3y2k4.thetvdb.resource;

import java.util.function.Function;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public abstract class Resource {

    protected static final Function<Object, Boolean> ID_VALIDATOR = value -> Long.compare((long)value, 0) > 0;

    public static final String PATH_ID = "id";

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

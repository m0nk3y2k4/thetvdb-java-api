package com.github.m0nk3y2k4.thetvdb.internal.resource;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory;
import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import com.github.m0nk3y2k4.thetvdb.api.exception.APIRuntimeException;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;

public abstract class QueryResource extends Resource {

    private static String createQuery(@CheckForNull QueryParameters queryParams) {
        List<QueryParameters.Parameter> validParams = Optional.ofNullable(queryParams).orElse(TheTVDBApiFactory.createQueryParameters())
                .stream().filter(QueryResource::isValidQueryParameter).collect(Collectors.toList());

        if (!validParams.isEmpty()) {
            // .../resource?param1=value1&param2=value2&...
            return validParams.stream().map(e -> e.getKey() + "=" + encode(e.getValue())).collect(Collectors.joining("&", "?", ""));
        }

        return "";
    }

    protected static String createQueryResource(@Nonnull String base, @CheckForNull QueryParameters queryParams) {
        return base + createQuery(queryParams);
    }

    protected static String createQueryResource(@Nonnull String base, @Nonnull String specific, @CheckForNull QueryParameters queryParams) {
        return createResource(base, specific) + createQuery(queryParams);
    }

    private static Boolean isValidQueryParameter(QueryParameters.Parameter param) {
        return APIUtil.hasValue(param.getKey(), param.getValue());
    }

    private static String encode(String value) throws APIRuntimeException {
        try {
            return URLEncoder.encode(value, Charset.defaultCharset().name());
        } catch (UnsupportedEncodingException ex) {
            throw new APIRuntimeException("Encoding [" + Charset.defaultCharset().name() + "] is not supported" , ex);
        }
    }
}

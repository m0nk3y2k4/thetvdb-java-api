package com.github.m0nk3y2k4.thetvdb.resource;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.m0nk3y2k4.thetvdb.exception.APIRuntimeException;
import com.github.m0nk3y2k4.thetvdb.shared.util.APIUtil;

public abstract class QueryResource extends Resource {

    protected static String createQuery(@CheckForNull Map<String, String> queryParams) {
        Map<String, String> validParams = Optional.ofNullable(queryParams).orElse(Collections.emptyMap()).entrySet()
                .stream().filter(QueryResource::isValidQueryParameter).collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        if (!validParams.isEmpty()) {
            // .../resource?param1=value1&param2=value2&...
            return validParams.entrySet().stream().map(e -> e.getKey() + "=" + encode(e.getValue())).collect(Collectors.joining("&", "?", ""));
        }

        return "";
    }

    protected static String createQueryResource(@Nonnull String base, @CheckForNull Map<String, String> queryParams) {
        return base + createQuery(queryParams);
    }

    protected static String createQueryResource(@Nonnull String base, @Nonnull String specific, @CheckForNull Map<String, String> queryParams) {
        return createResource(base, specific) + createQuery(queryParams);
    }

    protected static String createQueryResource(@Nonnull String base, @Nonnull String specific, @CheckForNull Map<String, String> queryParams, Object... pathParams) {
        return createResource(base, specific, pathParams) + createQuery(queryParams);
    }

    private static Boolean isValidQueryParameter(Entry<String, String> param) {
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

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

/**
 * Specialized implementation for resources containing additional URL query parameters.
 * <p><br>
 * Provides general functionality for the creation of query resource strings. Only non-empty parameters will be considered. Parameter
 * values will be encoded automatically.
 */
public abstract class QueryResource extends Resource {

    protected QueryResource() {}

    /**
     * Creates a new query resource string consisting of the given <em>{@code base}</em> and <em>{@code specific}</em> URL path parameters
     * prepended by the given query parameters in the following format: <b><code>/BASE/specific?query1=value1&amp;query2=value2&amp;...</code></b>
     *
     * @param base Base URL path parameter which identifies a particular endpoint
     * @param specific Specific URL path parameter representing the actual route to be invoked
     * @param queryParams Set of query parameters to be added to the very end of the resource String
     *
     * @return Composed query resource String based on the given parameters
     */
    protected static String createQueryResource(@Nonnull String base, @Nonnull String specific, @CheckForNull QueryParameters queryParams) {
        return createResource(base, specific) + createQuery(queryParams);
    }

    /**
     * Creates a URL query snippet based on the given parameters. Only non-empty key/value pairs will be considered and the values will
     * be encoded automatically. The returned snippet will start with a <i>'?'</i> character followed by the actual parameter mappings. If
     * the given object contains no valid query parameters an empty String will be returned.
     *
     * @param queryParams Set of query parameters based on which the URL snippet should be created
     *
     * @return URL query snippet or empty String if the given object didn't contain any valid parameters
     */
    private static String createQuery(@CheckForNull QueryParameters queryParams) {
        List<QueryParameters.Parameter> validParams = Optional.ofNullable(queryParams).orElse(TheTVDBApiFactory.createQueryParameters())
                .stream().filter(QueryResource::isValidQueryParameter).collect(Collectors.toList());

        if (!validParams.isEmpty()) {
            // .../resource?param1=value1&param2=value2&...
            return validParams.stream().map(e -> e.getKey() + "=" + encode(e.getValue())).collect(Collectors.joining("&", "?", ""));
        }

        return "";
    }

    /**
     * Checks if both, the key and the value of the given query parameter are non-empty Strings
     *
     * @param param The query parameter to check
     *
     * @return True if both, the key and the value are not empty. False if at least one of them is empty.
     */
    private static Boolean isValidQueryParameter(@Nonnull QueryParameters.Parameter param) {
        return APIUtil.hasValue(param.getKey(), param.getValue());
    }

    /**
     * URL encodes the given <em>{@code value}</em> if necessary so that it may be used inside a resource String
     *
     * @param value The value to be encoded
     *
     * @return The encoded value
     */
    private static String encode(@Nonnull String value) {
        try {
            return URLEncoder.encode(value, Charset.defaultCharset().name());
        } catch (UnsupportedEncodingException ex) {
            throw new APIRuntimeException("Encoding [" + Charset.defaultCharset().name() + "] is not supported" , ex);
        }
    }
}

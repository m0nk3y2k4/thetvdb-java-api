package com.github.m0nk3y2k4.thetvdb.api.model.data;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Interface representing a <i>Movie</i> data transfer object.
 * <p><br>
 * The methods of this class provide easy access to all movie related data which was returned by the remote service in JSON format.
 * Please note that, as the remote service declares all of the properties to be optional, most of the methods are marked
 * as {@link Nullable}. Methods returning collection-based values however will return an empty collection in case no corresponding
 * data was received.
 * <p><br>
 * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
 * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
 * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
 * relieve the API user of this task.
 */
public interface Movie {

    /**
     * Get the artworks
     *
     * @return the artworks
     */
    List<Artwork> getArtworks();

    /**
     * Get the genres
     *
     * @return the genres
     */
    List<Genre> getGenres();

    /**
     * Get the id
     *
     * @return the id
     */
    @Nullable Long getId();

    /**
     * Get the actors
     *
     * @return the actors
     */
    List<People> getActors();

    /**
     * Get the directors
     *
     * @return the directors
     */
    List<People> getDirectors();

    /**
     * Get the producers
     *
     * @return the producers
     */
    List<People> getProducers();

    /**
     * Get the writers
     *
     * @return the writers
     */
    List<People> getWriters();

    /**
     * Get the release dates
     *
     * @return the release dates
     */
    List<ReleaseDate> getReleaseDates();

    /**
     * Get the remote IDs
     *
     * @return the remote IDs
     */
    List<RemoteId> getRemoteIds();

    /**
     * Get the runtime
     *
     * @return the runtime
     */
    @Nullable Long getRuntime();

    /**
     * Get the trailers
     *
     * @return the trailers
     */
    List<Trailer> getTrailers();

    /**
     * Get the translations
     *
     * @return the translations
     */
    List<Translation> getTranslations();

    /**
     * Get the url
     *
     * @return the url
     */
    @Nullable String getUrl();

    /**
     * Interface representing a movies <i>Artwork</i> data transfer object.
     * <p><br>
     * The methods of this class provide easy access to all artwork related data which was returned by the remote service in JSON format.
     * Please note that, as the remote service declares all of the properties to be optional, most of the methods are marked
     * as {@link Nullable}. Methods returning collection-based values however will return an empty collection in case no corresponding
     * data was received.
     * <p><br>
     * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
     * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
     * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
     * relieve the API user of this task.
     */
    interface Artwork {

        /**
         * Get the artwork type
         *
         * @return the artwork type
         */
        @Nullable String getArtworkType();

        /**
         * Get the height
         *
         * @return the height
         */
        @Nullable Long getHeight();

        /**
         * Get the id
         *
         * @return the id
         */
        @Nullable String getId();

        /**
         * Get the primary flag
         *
         * @return the primary flag
         */
        @Nullable Boolean isPrimary();

        /**
         * Get the tags
         *
         * @return the tags
         */
        @Nullable String getTags();

        /**
         * Get the thumb url
         *
         * @return the thumb url
         */
        @Nullable String getThumbUrl();

        /**
         * Get the url
         *
         * @return the url
         */
        @Nullable String getUrl();

        /**
         * Get the width
         *
         * @return the width
         */
        @Nullable Long getWidth();
    }

    /**
     * Interface representing a movies <i>Genre</i> data transfer object.
     * <p><br>
     * The methods of this class provide easy access to all genre related data which was returned by the remote service in JSON format.
     * Please note that, as the remote service declares all of the properties to be optional, most of the methods are marked
     * as {@link Nullable}. Methods returning collection-based values however will return an empty collection in case no corresponding
     * data was received.
     * <p><br>
     * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
     * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
     * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
     * relieve the API user of this task.
     */
    interface Genre {

        /**
         * Get the id
         *
         * @return the id
         */
        @Nullable Long getId();

        /**
         * Get the name
         *
         * @return the name
         */
        @Nullable String getName();

        /**
         * Get the url
         *
         * @return the url
         */
        @Nullable String getUrl();
    }

    /**
     * Interface representing a movies <i>ReleaseDate</i> data transfer object.
     * <p><br>
     * The methods of this class provide easy access to all release date related data which was returned by the remote service in JSON format.
     * Please note that, as the remote service declares all of the properties to be optional, most of the methods are marked
     * as {@link Nullable}. Methods returning collection-based values however will return an empty collection in case no corresponding
     * data was received.
     * <p><br>
     * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
     * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
     * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
     * relieve the API user of this task.
     */
    interface ReleaseDate {

        /**
         * Get the country
         *
         * @return the country
         */
        @Nullable String getCountry();

        /**
         * Get the date
         *
         * @return the date
         */
        @Nullable String getDate();

        /**
         * Get the type
         *
         * @return the type
         */
        @Nullable String getType();
    }

    /**
     * Interface representing a movies <i>RemoteId</i> data transfer object.
     * <p><br>
     * The methods of this class provide easy access to all remote ID related data which was returned by the remote service in JSON format.
     * Please note that, as the remote service declares all of the properties to be optional, most of the methods are marked
     * as {@link Nullable}. Methods returning collection-based values however will return an empty collection in case no corresponding
     * data was received.
     * <p><br>
     * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
     * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
     * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
     * relieve the API user of this task.
     */
    interface  RemoteId {

        /**
         * Get the id
         *
         * @return the id
         */
        @Nullable String getId();

        /**
         * Get the source id
         *
         * @return the source id
         */
        @Nullable Long getSourceId();

        /**
         * Get the source name
         *
         * @return the source name
         */
        @Nullable String getSourceName();

        /**
         * Get the source url
         *
         * @return the source url
         */
        @Nullable String getSourceUrl();

        /**
         * Get the url
         *
         * @return the url
         */
        @Nullable String getUrl();
    }

    /**
     * Interface representing a movies <i>Trailer</i> data transfer object.
     * <p><br>
     * The methods of this class provide easy access to all trailer related data which was returned by the remote service in JSON format.
     * Please note that, as the remote service declares all of the properties to be optional, most of the methods are marked
     * as {@link Nullable}. Methods returning collection-based values however will return an empty collection in case no corresponding
     * data was received.
     * <p><br>
     * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
     * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
     * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
     * relieve the API user of this task.
     */
    interface Trailer {

        /**
         * Get the name
         *
         * @return the name
         */
        @Nullable String getName();

        /**
         * Get the url
         *
         * @return the url
         */
        @Nullable String getUrl();
    }

    /**
     * Interface representing a movies <i>Translation</i> data transfer object.
     * <p><br>
     * The methods of this class provide easy access to all translation related data which was returned by the remote service in JSON format.
     * Please note that, as the remote service declares all of the properties to be optional, most of the methods are marked
     * as {@link Nullable}. Methods returning collection-based values however will return an empty collection in case no corresponding
     * data was received.
     * <p><br>
     * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
     * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
     * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
     * relieve the API user of this task.
     */
    interface Translation {

        /**
         * Get the primary flag
         *
         * @return the primary flag
         */
        @Nullable Boolean isPrimary();

        /**
         * Get the language code
         *
         * @return the language code
         */
        @Nullable String getLanguageCode();

        /**
         * Get the name
         *
         * @return the name
         */
        @Nullable String getName();

        /**
         * Get the overview
         *
         * @return the overview
         */
        @Nullable String getOverview();

        /**
         * Get the tagline
         *
         * @return the tagline
         */
        @Nullable String getTagline();
    }

    /**
     * Interface representing a movies <i>People</i> data transfer object.
     * <p><br>
     * The methods of this class provide easy access to all people related data which was returned by the remote service in JSON format.
     * Please note that, as the remote service declares all of the properties to be optional, most of the methods are marked
     * as {@link Nullable}. Methods returning collection-based values however will return an empty collection in case no corresponding
     * data was received.
     * <p><br>
     * The sole purpose of these DTO objects is to encapsulate the exact raw JSON data as received from the remote service in order to
     * facilitate API integration by working with simple Java POJO's instead of nested JSON nodes. Although there will be no intense
     * post-processing of the actual JSON values a type-casting may be applied to <u>some</u> of them to improve the usability and
     * relieve the API user of this task.
     */
    interface People {

        /**
         * Get the id
         *
         * @return the id
         */
        @Nullable String getId();

        /**
         * Get the IMDB id
         *
         * @return the IMDB id
         */
        @Nullable String getImdbId();

        /**
         * Get the featured flag
         *
         * @return the featured flag
         */
        @Nullable Boolean isFeatured();

        /**
         * Get the name
         *
         * @return the name
         */
        @Nullable String getName();

        /**
         * Get the people facebook
         *
         * @return the people facebook
         */
        @Nullable String getPeopleFacebook();

        /**
         * Get the people id
         *
         * @return the people id
         */
        @Nullable String getPeopleId();

        /**
         * Get the people image
         *
         * @return the people image
         */
        @Nullable String getPeopleImage();

        /**
         * Get the people instagram
         *
         * @return the people instagram
         */
        @Nullable String getPeopleInstagram();

        /**
         * Get the people twitter
         *
         * @return the people twitter
         */
        @Nullable String getPeopleTwitter();

        /**
         * Get the role
         *
         * @return the role
         */
        @Nullable String getRole();

        /**
         * Get the role image
         *
         * @return the role image
         */
        @Nullable String getRoleImage();
    }
}

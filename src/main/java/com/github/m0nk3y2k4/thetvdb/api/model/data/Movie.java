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
     * Get all objects of the {<em>{@code data.artworks}</em>} JSON property in a list
     *
     * @return The <em>{@code artworks}</em> property from the received JSON as list
     */
    List<Artwork> getArtworks();

    /**
     * Get all objects of the {<em>{@code data.genres}</em>} JSON property in a list
     *
     * @return The <em>{@code genres}</em> property from the received JSON as list
     */
    List<Genre> getGenres();

    /**
     * Get the value of the {<em>{@code data.id}</em>} JSON property
     *
     * @return The <em>{@code id}</em> property from the received JSON
     */
    @Nullable Long getId();

    /**
     * Get all objects of the {<em>{@code data.people.actors}</em>} JSON property in a list
     *
     * @return The <em>{@code actors}</em> property from the received JSONs people-node as list
     */
    List<People> getActors();

    /**
     * Get all objects of the {<em>{@code data.people.directors}</em>} JSON property in a list
     *
     * @return The <em>{@code directors}</em> property from the received JSONs people-node as list
     */
    List<People> getDirectors();

    /**
     * Get all objects of the {<em>{@code data.people.producers}</em>} JSON property in a list
     *
     * @return The <em>{@code producers}</em> property from the received JSONs people-node as list
     */
    List<People> getProducers();

    /**
     * Get all objects of the {<em>{@code data.people.writers}</em>} JSON property in a list
     *
     * @return The <em>{@code writers}</em> property from the received JSONs people-node as list
     */
    List<People> getWriters();

    /**
     * Get all objects of the {<em>{@code data.release_dates}</em>} JSON property in a list
     *
     * @return The <em>{@code release_dates}</em> property from the received JSON as list
     */
    List<ReleaseDate> getReleaseDates();

    /**
     * Get all objects of the {<em>{@code data.remoteids}</em>} JSON property in a list
     *
     * @return The <em>{@code remoteids}</em> property from the received JSON as list
     */
    List<RemoteId> getRemoteIds();

    /**
     * Get the value of the {<em>{@code data.runtime}</em>} JSON property
     *
     * @return The <em>{@code runtime}</em> property from the received JSON
     */
    @Nullable Long getRuntime();

    /**
     * Get all objects of the {<em>{@code data.trailers}</em>} JSON property in a list
     *
     * @return The <em>{@code trailers}</em> property from the received JSON as list
     */
    List<Trailer> getTrailers();

    /**
     * Get all objects of the {<em>{@code data.translations}</em>} JSON property in a list
     *
     * @return The <em>{@code translations}</em> property from the received JSON as list
     */
    List<Translation> getTranslations();

    /**
     * Get the value of the {<em>{@code data.url}</em>} JSON property
     *
     * @return The <em>{@code url}</em> property from the received JSON
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
         * Get the value of the {<em>{@code data.artworks.artwork_type}</em>} JSON property
         *
         * @return The <em>{@code artwork_type}</em> property from the received JSON
         */
        @Nullable String getArtworkType();

        /**
         * Get the value of the {<em>{@code data.artworks.height}</em>} JSON property
         *
         * @return The <em>{@code height}</em> property from the received JSON
         */
        @Nullable Long getHeight();

        /**
         * Get the value of the {<em>{@code data.artworks.id}</em>} JSON property
         *
         * @return The <em>{@code id}</em> property from the received JSON
         */
        @Nullable String getId();

        /**
         * Get the value of the {<em>{@code data.artworks.is_primary}</em>} JSON property
         *
         * @return The <em>{@code is_primary}</em> property from the received JSON
         */
        @Nullable Boolean isPrimary();

        /**
         * Get the value of the {<em>{@code data.artworks.tags}</em>} JSON property
         *
         * @return The <em>{@code tags}</em> property from the received JSON
         */
        @Nullable String getTags();

        /**
         * Get the value of the {<em>{@code data.artworks.thumb_url}</em>} JSON property
         *
         * @return The <em>{@code thumb_url}</em> property from the received JSON
         */
        @Nullable String getThumbUrl();

        /**
         * Get the value of the {<em>{@code data.artworks.url}</em>} JSON property
         *
         * @return The <em>{@code url}</em> property from the received JSON
         */
        @Nullable String getUrl();

        /**
         * Get the value of the {<em>{@code data.artworks.width}</em>} JSON property
         *
         * @return The <em>{@code width}</em> property from the received JSON
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
         * Get the value of the {<em>{@code data.genres.id}</em>} JSON property
         *
         * @return The <em>{@code id}</em> property from the received JSON
         */
        @Nullable Long getId();

        /**
         * Get the value of the {<em>{@code data.genres.name}</em>} JSON property
         *
         * @return The <em>{@code name}</em> property from the received JSON
         */
        @Nullable String getName();

        /**
         * Get the value of the {<em>{@code data.genres.url}</em>} JSON property
         *
         * @return The <em>{@code url}</em> property from the received JSON
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
         * Get the value of the {<em>{@code data.release_dates.country}</em>} JSON property
         *
         * @return The <em>{@code country}</em> property from the received JSON
         */
        @Nullable String getCountry();

        /**
         * Get the value of the {<em>{@code data.release_dates.date}</em>} JSON property
         *
         * @return The <em>{@code date}</em> property from the received JSON
         */
        @Nullable String getDate();

        /**
         * Get the value of the {<em>{@code data.release_dates.type}</em>} JSON property
         *
         * @return The <em>{@code type}</em> property from the received JSON
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
         * Get the value of the {<em>{@code data.remoteids.id}</em>} JSON property
         *
         * @return The <em>{@code id}</em> property from the received JSON
         */
        @Nullable String getId();

        /**
         * Get the value of the {<em>{@code data.remoteids.source_id}</em>} JSON property
         *
         * @return The <em>{@code source_id}</em> property from the received JSON
         */
        @Nullable Long getSourceId();

        /**
         * Get the value of the {<em>{@code data.remoteids.source_name}</em>} JSON property
         *
         * @return The <em>{@code source_name}</em> property from the received JSON
         */
        @Nullable String getSourceName();

        /**
         * Get the value of the {<em>{@code data.remoteids.source_url}</em>} JSON property
         *
         * @return The <em>{@code source_url}</em> property from the received JSON
         */
        @Nullable String getSourceUrl();

        /**
         * Get the value of the {<em>{@code data.remoteids.url}</em>} JSON property
         *
         * @return The <em>{@code url}</em> property from the received JSON
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
         * Get the value of the {<em>{@code data.trailers.name}</em>} JSON property
         *
         * @return The <em>{@code name}</em> property from the received JSON
         */
        @Nullable String getName();

        /**
         * Get the value of the {<em>{@code data.trailers.url}</em>} JSON property
         *
         * @return The <em>{@code url}</em> property from the received JSON
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
         * Get the value of the {<em>{@code data.translations.is_primary}</em>} JSON property
         *
         * @return The <em>{@code is_primary}</em> property from the received JSON
         */
        @Nullable Boolean isPrimary();

        /**
         * Get the value of the {<em>{@code data.translations.language_code}</em>} JSON property
         *
         * @return The <em>{@code language_code}</em> property from the received JSON
         */
        @Nullable String getLanguageCode();

        /**
         * Get the value of the {<em>{@code data.translations.name}</em>} JSON property
         *
         * @return The <em>{@code name}</em> property from the received JSON
         */
        @Nullable String getName();

        /**
         * Get the value of the {<em>{@code data.translations.overview}</em>} JSON property
         *
         * @return The <em>{@code overview}</em> property from the received JSON
         */
        @Nullable String getOverview();

        /**
         * Get the value of the {<em>{@code data.translations.tagline}</em>} JSON property
         *
         * @return The <em>{@code tagline}</em> property from the received JSON
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
         * Get the value of the {<em>{@code data.people.<actors|directors|producers|writers>.id}</em>} JSON property
         *
         * @return The <em>{@code id}</em> property from the received JSON
         */
        @Nullable String getId();

        /**
         * Get the value of the {<em>{@code data.people.<actors|directors|producers|writers>.imdb_id}</em>} JSON property
         *
         * @return The <em>{@code imdb_id}</em> property from the received JSON
         */
        @Nullable String getImdbId();

        /**
         * Get the value of the {<em>{@code data.people.<actors|directors|producers|writers>.is_featured}</em>} JSON property
         *
         * @return The <em>{@code is_featured}</em> property from the received JSON
         */
        @Nullable Boolean isFeatured();

        /**
         * Get the value of the {<em>{@code data.people.<actors|directors|producers|writers>.name}</em>} JSON property
         *
         * @return The <em>{@code name}</em> property from the received JSON
         */
        @Nullable String getName();

        /**
         * Get the value of the {<em>{@code data.people.<actors|directors|producers|writers>.people_facebook}</em>} JSON property
         *
         * @return The <em>{@code people_facebook}</em> property from the received JSON
         */
        @Nullable String getPeopleFacebook();

        /**
         * Get the value of the {<em>{@code data.people.<actors|directors|producers|writers>.people_id}</em>} JSON property
         *
         * @return The <em>{@code people_id}</em> property from the received JSON
         */
        @Nullable String getPeopleId();

        /**
         * Get the value of the {<em>{@code data.people.<actors|directors|producers|writers>.people_image}</em>} JSON property
         *
         * @return The <em>{@code people_image}</em> property from the received JSON
         */
        @Nullable String getPeopleImage();

        /**
         * Get the value of the {<em>{@code data.people.<actors|directors|producers|writers>.people_instagram}</em>} JSON property
         *
         * @return The <em>{@code people_instagram}</em> property from the received JSON
         */
        @Nullable String getPeopleInstagram();

        /**
         * Get the value of the {<em>{@code data.people.<actors|directors|producers|writers>.people_twitter}</em>} JSON property
         *
         * @return The <em>{@code people_twitter}</em> property from the received JSON
         */
        @Nullable String getPeopleTwitter();

        /**
         * Get the value of the {<em>{@code data.people.<actors|directors|producers|writers>.role}</em>} JSON property
         *
         * @return The <em>{@code role}</em> property from the received JSON
         */
        @Nullable String getRole();

        /**
         * Get the value of the {<em>{@code data.people.<actors|directors|producers|writers>.role_image}</em>} JSON property
         *
         * @return The <em>{@code role_image}</em> property from the received JSON
         */
        @Nullable String getRoleImage();
    }
}

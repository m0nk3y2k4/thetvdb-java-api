# thetvdb-java-api
![Java CI with Maven](https://github.com/m0nk3y2k4/thetvdb-java-api/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master&event=push) &nbsp;
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) &nbsp;
[![GitHub release](https://img.shields.io/github/release/m0nk3y2k4/thetvdb-java-api.svg)](https://github.com/m0nk3y2k4/thetvdb-java-api/releases) &nbsp;
![Java version](https://img.shields.io/badge/Java-10-informational) &nbsp;

A simple connector for an easy integration of the [TheTVDB.com](https://thetvdb.com/) RESTful API in Java projects.

## Table of contents
- [Introduction](#introduction)
- [Technologies](#technologies)
- [Features](#features)
- [Setup](#setup)
- [Code Examples](#code-examples)
- [Development](#development)
- [Status](#status)
- [License](#license)

## Introduction
With more than [240,000+](https://www.thetvdb.com/about) TV Series and movies _TheTVDB.com_ is probably one of the largest
digital media metadata databases in the world. Moreover, they offer the opportunity to access this vast amount of data
on M2M-level by providing a RESTful API, which I think is pretty cool as it enables developers to virtually include
all this data into their own applications. However, the majority of these applications will probably intend to actually
make some good use of this information in order to create something beneficial for the end user. Talking about Java
applications, that's where this connector might come in handy.

One of the main objectives of this little project was to keep the code of these aforementioned applications clean.
Communicating with the _TheTVDB.com_ API to gather information is a necessity, but most likely not the end of whatever
you're aiming to create. So in order to not bloat the code with opening connections, preparing requests, process and
evaluate responses and finally parse its JSON content, all of this may be handled by the `thetvdb-java-api` connector,
leaving you with only a few (but unavoidable) lines of code describing what you actually want to achieve (e.g. query
a TV Series by name).

However, where there is light there is also shadow, they say. Simplifying things often means to also restrict their
functionality to only some basics. And although this might be quite an acceptable trade-off for many people, there will
always be someone who is ruled out from using certain tools due to the need of some more advanced functionality. So another
important goal was to promote this simplified usage while maintaining the usability for more complex use-cases at the
same time. So it's actually up to the developer whether to query by some common parameters in just a one-liner and to
work with the prefabbed Java DTOs or to run more complex queries by providing a set of parameters and maybe even parse
the raw JSON content returned by the _TheTVDB.com_ API all by yourself. Your decision!

## Technologies
* [Java](https://jdk.java.net/) (10) - Class-based, object-oriented programming language
* [Apache Maven](https://maven.apache.org/download.cgi) (3.6.0) - Software project management and comprehension tool

## Features
In general, this connector is fully compatible with all available _TheTVDB.com_ API endpoints. However, as new versions
of the RESTful API might not always be backwards compatible, each major `thetvdb-java-api` release is invariantly linked
to a specific version of the _TheTVDB.com_ remote API. The API version a specific connector release is using can be derived
from its version number. For example, `v3.x` uses the _TheTVDB.com_ APIv3 whereas the (not yet existent) `v4.x` would use
the upcoming APIv4.

<details>
<summary>Supported TheTVDB.com APIv3 routes (<code>thetvdb-java-api v3.x</code>)</summary>

- Authentication
    - [x] [/login](https://api.thetvdb.com/swagger#!/Authentication/post_login)
    - [x] [/refresh_token](https://api.thetvdb.com/swagger#!/Authentication/get_refresh_token)
- Episodes
    - [x] [/episodes/{id}](https://api.thetvdb.com/swagger#!/Episodes/get_episodes_id)
- Languages
    - [x] [/languages](https://api.thetvdb.com/swagger#!/Languages/get_languages)
    - [x] [/languages/{id}](https://api.thetvdb.com/swagger#!/Languages/get_languages_id)
- Movies
    - [x] [/movies/{id}](https://api.thetvdb.com/swagger#!/Movies/get_movies_id)
    - [x] [/movieupdates](https://api.thetvdb.com/swagger#!/Movies/get_movieupdates)
- Search
    - [x] [/search/series](https://api.thetvdb.com/swagger#!/Search/get_search_series)
    - [x] [/search/series/params](https://api.thetvdb.com/swagger#!/Search/get_search_series_params)
- Series
    - [x] [/series/{id}](https://api.thetvdb.com/swagger#!/Series/get_series_id)
    - [x] [/series/{id}](https://api.thetvdb.com/swagger#!/Series/head_series_id) (HEAD)
    - [x] [/series/{id}/actors](https://api.thetvdb.com/swagger#!/Series/get_series_id_actors)
    - [x] [/series/{id}/episodes](https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes)
    - [x] [/series/{id}/episodes/query](https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query)
    - [x] [/series/{id}/episodes/query/params](https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_query_params)
    - [x] [/series/{id}/episodes/summary](https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes_summary)
    - [x] [/series/{id}/filter](https://api.thetvdb.com/swagger#!/Series/get_series_id_filter)
    - [x] [/series/{id}/filter/params](https://api.thetvdb.com/swagger#!/Series/get_series_id_filter_params)
    - [x] [/series/{id}/images](https://api.thetvdb.com/swagger#!/Series/get_series_id_images)
    - [x] [/series/{id}/images/query](https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query)
    - [x] [/series/{id}/images/query/params](https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query_params)
- Updates
    - [x] [/updated/query](https://api.thetvdb.com/swagger#!/Updates/get_updated_query)
    - [x] [/updated/query/params](https://api.thetvdb.com/swagger#!/Updates/get_updated_query_params)
- Users
    - [x] [/user](https://api.thetvdb.com/swagger#!/Users/get_user)
    - [x] [/user/favorites](https://api.thetvdb.com/swagger#!/Users/get_user_favorites)
    - [x] [/user/favorites/{id}](https://api.thetvdb.com/swagger#!/Users/delete_user_favorites_id) (DELETE)
    - [x] [/user/favorites/{id}](https://api.thetvdb.com/swagger#!/Users/put_user_favorites_id) (PUT)
    - [x] [/user/ratings](https://api.thetvdb.com/swagger#!/Users/get_user_ratings)
    - [x] [/user/ratings/query](https://api.thetvdb.com/swagger#!/Users/get_user_ratings_query)
    - [x] [/user/ratings/query/params](https://api.thetvdb.com/swagger#!/Users/get_user_ratings_query_params)
    - [x] [/user/ratings/{itemType}/{itemId}](https://api.thetvdb.com/swagger#!/Users/delete_user_ratings_itemType_itemId)
    - [x] [/user/ratings/{itemType}/{itemId}/{itemRating}](https://api.thetvdb.com/swagger#!/Users/put_user_ratings_itemType_itemId_itemRating)
</details>

<details>
<summary>WIP: Supported TheTVDB.com APIv4 routes (<code>thetvdb-java-api v4.x</code>)</summary>

- Artwork Statuses
    - [x] [/artwork/statuses](https://thetvdb.github.io/v4-api/#/Artwork%20Statuses/getAllArtworkStatuses)
- Artwork Types
    - [x] [/artwork/types](https://thetvdb.github.io/v4-api/#/Artwork%20Types/getAllArtworkTypes)
- Artwork
    - [x] [/artwork/{id}](https://thetvdb.github.io/v4-api/#/Artwork/getArtworkBase)
    - [x] [/artwork/{id}/extended](https://thetvdb.github.io/v4-api/#/Artwork/getArtworkExtended)
- Award Categories
    - [x] [/awards/categories/{id}](https://thetvdb.github.io/v4-api/#/Award%20Categories/getAwardCategory)
    - [x] [/awards/categories/{id}/extended](https://thetvdb.github.io/v4-api/#/Award%20Categories/getAwardCategoryExtended)
- Awards
    - [x] [/awards](https://thetvdb.github.io/v4-api/#/Awards/getAllAwards)
    - [x] [/awards/{id}](https://thetvdb.github.io/v4-api/#/Awards/getAward)
    - [x] [/awards/{id}/extended](https://thetvdb.github.io/v4-api/#/Awards/getAwardExtended)
- Characters
    - [x] [/characters/{id}](https://thetvdb.github.io/v4-api/#/Characters/getCharacterBase)
- Companies
    - [x] [/companies](https://thetvdb.github.io/v4-api/#/Companies/getAllCompanies)
    - [x] [/companies/types](https://thetvdb.github.io/v4-api/#/Companies/getCompanyTypes)
    - [x] [/companies/{id}](https://thetvdb.github.io/v4-api/#/Companies/getCompany)
    - [ ] [/companies/{id}/movies]()
    - [ ] [/companies/{id}/episodes]()
    - [ ] [/companies/{id}/series]()
    - [ ] [/companies/{id}/seriesRollup]()
- Content Ratings
    - [x] [/content/ratings](https://thetvdb.github.io/v4-api/#/Content%20Ratings/getAllContentRatings)
- Countries
    - [ ] [~~/countries~~](https://thetvdb.github.io/v4-api/#/Countries/getAllCountries)
    - [ ] [/countries/{id}/series]()
    - [ ] [/countries/{id}/movies]()
    - [ ] [/countries/{id}/networks]()
- Entity Types
    - [x] [/entities/types](https://thetvdb.github.io/v4-api/#/Entity%20Types/getEntityTypes)
- Episodes
    - [x] [/episodes/{id}](https://thetvdb.github.io/v4-api/#/Episodes/getEpisodeBase)
    - [x] [/episodes/{id}/extended](https://thetvdb.github.io/v4-api/#/Episodes/getEpisodeExtended)
    - [x] [/episodes/{id}/translations/{language}](https://thetvdb.github.io/v4-api/#/Episodes/getEpisodeTranslation)
- Genders
    - [x] [/genders](https://thetvdb.github.io/v4-api/#/Genders/getAllGenders)
- Genres
    - [x] [/genres](https://thetvdb.github.io/v4-api/#/Genres/getAllGenres)
    - [x] [/genres/{id}](https://thetvdb.github.io/v4-api/#/Genres/getGenreBase)
    - [ ] [/genres/{id}/movies]()
    - [ ] [/genres/{id}/series]()
- Inspiration Types
    - [x] [/inspiration/types](https://thetvdb.github.io/v4-api/#/InspirationTypes/getAllInspirationTypes)
- ~~Languages~~
    - [ ] [~~/languages~~](https://thetvdb.github.io/v4-api/#/Languages/getAllLanguages)
- Lists
    - [x] [/lists/{id}/translations/{language}](https://thetvdb.github.io/v4-api/#/Lists/getListTranslation)
    - [x] [/lists](https://thetvdb.github.io/v4-api/#/Lists/getAllLists)
    - [x] [/lists/{id}](https://thetvdb.github.io/v4-api/#/Lists/getList)
    - [x] [/lists/{id}/extended](https://thetvdb.github.io/v4-api/#/Lists/getListExtended)
- Login
    - [x] [/login](https://thetvdb.github.io/v4-api/#/Login/post_login)
- Movie Statuses
    - [x] [/movies/statuses](https://thetvdb.github.io/v4-api/#/Movie%20Statuses/getAllMovieStatuses)
- Movies
    - [x] [/movies](https://thetvdb.github.io/v4-api/#/Movies/getAllMovie)
    - [x] [/movies/{id}](https://thetvdb.github.io/v4-api/#/Movies/getMovieBase)
    - [x] [/movies/{id}/extended](https://thetvdb.github.io/v4-api/#/Movies/getMovieExtended)
    - [x] [/movies/{id}/translations/{language}](https://thetvdb.github.io/v4-api/#/Movies/getMovieTranslation)
- People Types
    - [x] [/people/types](https://thetvdb.github.io/v4-api/#/People%20Types/getAllPeopleTypes)
- People
    - [x] [/people/{id}](https://thetvdb.github.io/v4-api/#/People/getPeopleBase)
    - [x] [/people/{id}/extended](https://thetvdb.github.io/v4-api/#/People/getPeopleExtended)
    - [x] [/people/{id}/translations/{language}](https://thetvdb.github.io/v4-api/#/People/getPeopleTranslation)
- Search
    - [x] [/search](https://thetvdb.github.io/v4-api/#/Search/getSearchResults)
- Seasons
    - [x] [/seasons](https://thetvdb.github.io/v4-api/#/Seasons/getAllSeasons)
    - [x] [/seasons/{id}](https://thetvdb.github.io/v4-api/#/Seasons/getSeasonBase)
    - [x] [/seasons/{id}/extended](https://thetvdb.github.io/v4-api/#/Seasons/getSeasonExtended)
    - [x] [/seasons/types](https://thetvdb.github.io/v4-api/#/Seasons/getSeasonTypes)
    - [x] [/seasons/{id}/translations/{language}](https://thetvdb.github.io/v4-api/#/Seasons/getSeasonTranslation)
- Series
    - [x] [/series](https://thetvdb.github.io/v4-api/#/Series/getAllSeries)
    - [x] [/series/{id}](https://thetvdb.github.io/v4-api/#/Series/getSeriesBase)
    - [x] [/series/{id}/extended](https://thetvdb.github.io/v4-api/#/Series/getSeriesExtended)
    - [x] [/series/{id}/episodes/{season-type}](https://thetvdb.github.io/v4-api/#/Series/getSeriesEpisodes)
    - [x] [/series/{id}/translations/{language}](https://thetvdb.github.io/v4-api/#/Series/getSeriesTranslation)
- Series Statuses
    - [x] [/series/statuses](https://thetvdb.github.io/v4-api/#/Series%20Statuses/getAllSeriesStatuses)
- Source Types
    - [x] [/source/types](https://thetvdb.github.io/v4-api/#/Source%20Types/getAllSourceTypes)
- Updates
    - [x] [/updates](https://thetvdb.github.io/v4-api/#/Updates/updates)
</details>

The latest documentation can be found here: [TheTVDB Java 3.x API](http://thetvdb-java-api.m0nk3y.info/v3/index.html?overview-summary.html)

## Setup
#### Maven
The artifact is *not* available through the central maven repository, but you may still include the connector into your
maven project via the [jitpack.io](https://www.jitpack.io/#m0nk3y2k4/thetvdb-java-api) repository.

1. Add the JitPack repository to your build file 
    ```xml
        <repositories>
            <repository>
                <id>jitpack.io</id>
                <url>https://jitpack.io</url>
            </repository>
        </repositories>
    ```
2. Add the dependency
    ```xml
        <dependency>
            <groupId>com.github.m0nk3y2k4</groupId>
            <artifactId>thetvdb-java-api</artifactId>
            <version>3.0.3</version>
        </dependency>
    ```

#### Other build automation tools
For other tools like e.g. _gradle_ please check out the documentation at https://jitpack.io/.

#### Manually include artifact
In case your build tool isn't supported, or you're not using any build framework at all you can still include the artifact
manually as _.jar_ file. The files (with and without dependencies) can either be obtained from the assets section of the
corresponding [release](https://github.com/m0nk3y2k4/thetvdb-java-api/releases) or by cloning the repository and building
the artifacts locally:
```shell
mvn clean install
```

## Code Examples
#### Basics
When it comes to using the connector, there's only one entry point where all your activities start:
```
com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory
```
This class provides factory methods to create all the objects needed for working with the connector, including new API
instances, free configurable query parameters and proxies.

So let's create a new API instance using the factory methods.
```java
// "Simple" authentication with API-Key only
TheTVDBApi api = TheTVDBApiFactory.createApi("API_KEY");

// or...

// "Advanced" authentication with API-Key, user key and user name
TheTVDBApi usersApi = TheTVDBApiFactory.createApi("API_KEY", "Userkey", "Username");
```
>Please keep in mind that the _username_ and _key_ are **ONLY** required for the [/user](https://api.thetvdb.com/swagger#/Users)
>routes.

Now we're going to use the simple API object to actually request data from the _TheTVDB.com_ REST endpoints. First and
foremost we have to invoke the _/login_ route once. This will induce the remote API to issue a brand new
[JSON Web Token](https://en.wikipedia.org/wiki/JSON_Web_Token) which will be needed for further authentication. After we
have successfully requested a JWT, we can now go on to actually retrieve some data from the API.
```java
api.login();    // We could also use "api.init()" which does exactly the same

// Set the preferred language. If possible the remote API will return the requested data in this language
// If not set, "en"' is the default
api.setLanguage("it");

// Alternatively: invoking the "/languages" endpoint will get us a list of all supported languages
List<Language> allLanguages = api.getAvailableLanguages();
Language italian = allLanguages.stream()
        .filter(l -> l.getName().equals("italiano")).findFirst().get();
api.setLanguage(italian.getAbbreviation());
```
>The connector comes with some integrated auto-login functionality, which will automatically try to request a new JWT
>in case the authorization failed. Although this might come in handy at times, it is **strongly recommended** to manually
>call "/login" route before requesting any data from the _TheTVDB.com_ API.

As you can see the JSON content received from the remote API has already been mapped into a `Language`
[DTO](https://en.wikipedia.org/wiki/Data_transfer_object). These objects are available for all routes returning complex
values like TV Series information, movies or actors. The actual metadata can be accessed through various getters in the
usual object-oriented way.

Finally, let's see how to query for specific information. Let's imagine we would like to get all episodes of season 3
for a specific TV Series. We can do that by using a corresponding set of `QueryParameters`.
```java
long seriesID = 296762;
QueryParameters query = TheTVDBApiFactory.createQueryParameters();
query.addParameter(Query.Series.AIREDSEASON, "3");

// Use your own custom set of query parameters
List<Episode> seasonThree = api.queryEpisodes(seriesID, query);

System.out.println("Here come all the episodes of season 3:");
seasonThree.stream().forEach(e -> System.out.println(
        e.getAiredSeason() + "." + e.getAiredEpisodeNumber() + ": " + e.getEpisodeName()));
``` 
```
Here come all the episodes of season 3:
3.1: Parce Domine
3.2: The Winter Line
3.3: The Absence of Field
...
```

For certain use cases the connector provides additional shortcut methods. Querying for a specific season is one of them,
so we could do the exact same example with even less code.
```java
long seriesID = 296762;

List<Episode> seasonThree = api.queryEpisodesByAiredSeason(seriesID, 3);

System.out.println("And again, all the episodes of season 3:");
seasonThree.stream().forEach(e -> System.out.println(
            e.getAiredSeason() + "." + e.getAiredEpisodeNumber() + ": " + e.getEpisodeName()));
```
```
And again, all the episodes of season 3:
3.1: Parce Domine
3.2: The Winter Line
3.3: The Absence of Field
...
```

And that's basically it! Go and check out the documentation of the [main API interface](http://thetvdb-java-api.m0nk3y.info/v3/index.html?com/github/m0nk3y2k4/thetvdb/api/TheTVDBApi.html)
to find more details on the various endpoint and shortcut methods.

#### Advanced
We could already see that the connector automatically converts the JSON content into a matching DTO. This is the default
behavior as it should be quite suitable for most cases, the so called default _layout_. However, two additional _layouts_
are available to support a wide variety of operational areas: `JSON` and `Extended`

##### JSON layout
If the prefabbed DTO's do not meet your requirements or if you prefer to take care of the JSON parsing yourself, this
is the layout to go with. It supports all basic routes (without shortcut methods though) but instead of mapping the metadata
into a DTO it will simply return the raw JSON as it was received from the remote API.
```java
// Create a new API instance the usual way
TheTVDBApi api = TheTVDBApiFactory.createApi("API_KEY");

// Create a JSON layout from the existing API by invoking the "json()" method
TheTVDBApi.JSON jsonApi = api.json();

long seriesID = 296762;
QueryParameters query = TheTVDBApiFactory.createQueryParameters(Map.of(Query.Series.AIREDSEASON, "3"));

JsonNode seasonThreeJSON = jsonApi.queryEpisodes(seriesID, query);
// jsonApi.queryEpisodesByAiredSeason(seriesID, 3);   --> This wont work! Shortcut methods are only available in the default layout
```

##### Extended layout
This layout is basically just an extension of the default layout and provides access to additional (though optional)
information received from the remote API, namely the "errors" and the "links" JSON nodes. Just like the `JSON` layout,
it supports all basic routes (without shortcut methods). All methods of this layout will return an `APIResponse<T>`
object which wraps the actual metadata DTO together with the error and pagination information.
```java
// Again, create the layout from any existing API
TheTVDBApi.Extended extendedApi = api.extended();

// Get the 2nd page of all episodes for this TV Series (max. 100 per page)
final long page = 2;
QueryParameters query = TheTVDBApiFactory.createQueryParameters()
        .addParameter(Query.Series.PAGE, String.valueOf(page));

APIResponse<List<Episode>> response = extendedApi.queryEpisodes(75760, query);

// Get the metadata. This is actually what the default layout does.
List<Episode> episodesSecondPage = response.getData();

// Errors and Links will not always be available and therefore will be returned as Optionals
response.getErrors().map(Errors::getInvalidQueryParams).ifPresent(System.err::println);
boolean morePages = response.getLinks().map(Links::getLast).map(lastPage -> lastPage > page).orElse(false);
```
>Please note that _Errors_ and _Links_ are not always available but only for certain endpoints. See the _TheTVDB.com_ API
>documentation for detailed information.

##### Proxy
The connector will send all requests directly towards the _TheTVDB.com_ [RESTful API](https://api.thetvdb.com/). In case
your runtime environment is not able to access this resource directly, you can instruct the connector to send its requests
to a different host which will forward them to the remote API.
```java
Proxy localProxy = TheTVDBApiFactory.createProxy("https", "my.local.proxy", 10000);
TheTVDBApi proxiedApi = TheTVDBApiFactory.createApi("API_KEY", localProxy);

// Data will be requested from "https://my.local.proxy:10000/movies/2559"
Movie excellent = proxiedApi.getMovie(2559);
```

## Development
#### Build
After cloning the repository the connector can be build via Apache Maven:
```shell script
mvn clean install
```
The build process will generate the following content, all stored inside the `/target` folder:
- Artifacts
    - `thetvdb-java-api-{VERSION}.jar` contains the actual connector artifact without dependencies
    - `thetvdb-java-api-{VERSION}-jar-with-dependencies.jar` contains the actual connector artifact will all runtime
    dependencies included
    - `thetvdb-java-api-{VERSION}-javadoc.jar` contains the packed JavaDoc documentation
    - `thetvdb-java-api-{VERSION}-sources.jar` contains the packed source code of the connector
- Documentation
    - Available through the `-javadoc.jar` artifact or directly at `/apidocs/index.html`
- Tests
    - jacoco code coverage report available at `/site/jacoco/index.html`

#### Integration Tests
Integration tests are not part of the default build process as they are executed against the actual _TheTVDB.com_ remote
API and thus require some additional configuration as well as an active Internet connection. Integration tests can be
setup and executed with the following steps:

1. Create a new file `/src/integration-tests/resources/thetvdbapi.properties` and set the _TheTVDB.com_ API-Key, user
key and user name you'd like to be used for the tests. You can use the available `thetvdbapi.properties.sample` file as
a template.

2. Build the project with the `integration-tests` maven profile:
    ```shell script
    mvn verify -P integration-tests
    ```

3. _OPTIONAL_: Instead of providing the authentication settings via the `thetvdbapi.properties` file you may also set
them directly as command line arguments:
    ```shell script
    mvn verify -P integration-tests -Dintegration.thetvdb.com.v3.apikey=APIKEY -Dintegration.thetvdb.com.v3.userkey=USERKEY -Dintegration.thetvdb.com.v3.username=USERNAME
    ```

## Status
Project is: _in progress_

Ongoing development of a connector compliant to the upcoming _TheTVDB.com_ APIv4 interface.

## License
This project is licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
>Bear in mind that you might have to negotiate a distinct [license model](https://thetvdb.com/api-information) with
>_TheTVDB.com, LLC._, e.g. when using their API service as part of a commercial product.

# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- New maven plugins
  - _org.apache.maven.plugins:maven-dependency-plugin_: `3.8.0`
### Changed
- Bumped multiple maven dependencies and plugins to latest version:
  - _com.fasterxml.jackson.core:jackson-databind_: `2.13.0` -> `2.18.0`
  - _org.apache.maven.plugins:maven-compiler-plugin_: `3.8.1` -> `3.13.0`
  - _org.assertj:assertj-core_: `3.21.0` -> `3.26.3`
  - _org.codehaus.mojo:build-helper-maven-plugin_: `3.2.0` -> `3.6.0`
  - _org.immutables:value_: `2.8.8` -> `2.10.1`
  - _org.junit.jupiter:*_: `5.8.1` -> `5.11.2`
  - _org.mockito:mockito-core_: `4.0.0` -> `5.14.2`
  - _org.mock-server:*_: `5.11.2` -> `5.15.0`
  - _org.slf4j:*_: `1.7.32` -> `2.0.16`
  - _org.jacoco:jacoco-maven-plugin_: `0.8.7` -> `0.8.12`
  - _org.apache.maven.plugins:maven-javadoc-plugin_: `3.3.1` -> `3.10.1`
  - _org.apache.maven.plugins:maven-failsafe-plugin_: `3.0.0-M5` -> `3.5.1`
  - _org.apache.maven.plugins:maven-surefire-plugin_: `3.0.0-M5` -> `3.5.1`
  - _org.apache.maven.plugins:maven-source-plugin_: `3.2.1` -> `3.3.1`
  - _org.apache.maven.plugins:maven-assembly-plugin_: `3.3.0` -> `3.7.1`
  - _org.apache.maven.plugins:maven-release-plugin_: `2.5.3` -> `3.1.1`

## [3.0.4] - 2021-11-03
### Changed
- Replaced the maven _NOPLogger_ dependency with _SimpleLogger_ implementation: `org.slf4j:slf4j-nop` -> `org.slf4j:slf4j-simple`.
- Updated multiple maven dependencies and plugins to latest version:
    - _org.junit.jupiter:*_: `5.7.0` -> `5.8.1`
    - _org.mockito:mockito-core_: `3.6.28` -> `4.0.0`
    - _com.fasterxml.jackson.core:jackson-databind_: `2.12.0` -> `2.13.0`
    - _org.assertj:assertj-core_: `3.18.1` -> `3.21.0`
    - _org.slf4j:*_: `1.7.30` -> `1.7.32`
    - _org.jacoco:jacoco-maven-plugin_: `0.8.6` -> `0.8.7`
    - _org.apache.maven.plugins:maven-javadoc-plugin_: `3.2.0` -> `3.3.1`

## [3.0.3] - 2021-01-01
### Added
- Util method `Parameters.isPositiveInteger` returning Predicate<String> used to check for positive numeric integers.
- `Proxy` objects now support an additional path component.

### Changed
- Method `APIUtil.prettyPrint(JsonNode)` now wraps lines with a fix Unix-style (LF) line separator.
- Reduced visibility of some JUnit test util classes and constructors.
- Reduced visibility of some public methods which were only used internally.
- Declared several util and factory classes as being final.
- The body of POST requests is now fix UTF-8 encoded.
- Made several methods `static` that did not reference any non-static content are were not overwritten in a sub class.
- Enhanced some test classes to use static inner classes instead of anonymous classes to avoid possible memory leaks.
- The methods `APISession.isInitialized`, `APISession.userAuthentication` and `QueryResource.isValidQueryParameter` now return primitive types.
- Method `JSONDeserializer.mapDataObject(...)` now accepts general `TypeReference<T>` objects.
- `JsonResource.getJsonString` now uses fix UTF-8 encoding when loading JSON content from resource files.
- Test util `MockServerUtil.contentLength` now accepts a more general CharSequence.
- Method `usingExtendedLayout` in TestTheTVDBAPICallAssert.java has been renamed to `isUsingExtendedLayout`.
- Method `usingJsonLayout` in TestTheTVDBAPICallAssert.java has been renamed to `isUsingJsonLayout`.
- Replace usage of anonymous classes in `QueryParametersImpl` to avoid the risk of memory leaks.
- Changed names of system properties used for running integration tests.
- Updated multiple maven dependencies to latest version:
    - _org.mockito:mockito-core_: `3.6.0` -> `3.6.28`
    - _com.fasterxml.jackson.core:jackson-databind_: `2.11.3` -> `2.12.0`

### Removed
- Method `MockServerUtil.defaultAPIHttpHeaders(boolean)` has been replaced by two more convenient ones.
    - `MockServerUtil.defaultAPIHttpHeaders()` includes matchers to check for absence of auth fields.
    - `MockServerUtil.defaultAPIHttpHeadersWithAuthorization()` includes matchers to check for presence of auth fields.
- Constructor from `ParametersTest.QueryParametersWithDisabledValueChecks` inner class.

## [3.0.2] - 2020-11-21
### Changed
- Changed project compile version to Java SE 10.
- Updated multiple maven dependencies to latest version:
    - _org.junit.jupiter:*_: `5.6.2` -> `5.7.0`
    - _org.mockito:mockito-core_: `3.4.6` -> `3.6.0`
    - _org.assertj:assertj-core_: `3.16.1` -> `3.18.1`
    - _com.fasterxml.jackson.core:jackson-databind_: `2.11.2` -> `2.11.3`
    - _org.mock-server:*_: `5.11.1` -> `5.11.2`

## [3.0.1] - 2020-08-29
### Changed
- New releases will now be tagged without the "tvdb-japi-" prefix but only with the version number.

### Fixed
- Replaced hardcoded Windows line ending in JUnit test with dynamic OS setting.

## [3.0.0] - 2020-08-28
First stable release which is fully compatible with _TheTVDB.com_ RESTful remote APIv3.

### Added
- README.md providing useful information for getting familiar with the project.
- LICENSE.txt and license file headers for [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt).
- Added integration tests which will be executed against the real _TheTVDB.com_ remote API.

### Changed
- Fixed some typos and outdated package names in JavaDoc.
- Enhanced JavaDoc in API data model interfaces.
- Renamed `APIResponse.JSONErros` interface to just `APIResponse.Errors`.

## [0.0.8] - 2020-08-22
### Added
- Added support for all routes available under the [Movies](https://api.thetvdb.com/swagger#/Movies) API endpoint.
- Added `CHANGELOG.md` file.
- Included _org.jacoco:jacoco-maven-plugin_ `0.8.5` in order to generate a test coverage report at the end of the build process.

### Changed
- Changed the data types of `TheTVDBApi.queryLastUpdated()` shortcut methods parameters from _String_ to _long_.
- The remote API version used by this connector is now publicly available via the `TheTVDBApi.Version.API_VERSION` constant.
- Specific handling of HTTP-405 responses. Enhance error message with additional information.
- All code now has to have a JUnit test line coverage of at least 80% and no missed class counts.
- Turned off non-error and non-warning messages for JavaDoc generation during maven build.
- HEAD requests now overwrite the new `getData()` method for processing the response header values.

### Fixed
- Make response handling of HEAD request more fail-safe by checking for an existing input stream first.

### Removed
- Removed some unnecessary _@JsonProperty_ annotations from API data models.

## [0.0.7] - 2020-08-16
### Added
- Added execution to _maven-javadoc-plugin_ with the goal to generate a _*javadoc.jar_ file at the end of the build process.
- Added _maven-source-plugin_ `3.2.1` in order to generate a _*sources.jar_ file at the end of the build process.
- Introduced new _@FunctionalInterface_ `Procedure` that allows the invocation of void methods that may throw an exception.
- Included the _getSeriesHeaderInformation_ route also into the `TheTVDBApi.Extended` layout.
- Mapping for the [/search/series](https://api.thetvdb.com/swagger#!/Search/get_search_series) routes JSONs _data.poster_ property.
- Added sjf4j NOP logger binding maven dependency.
- The amount of actual key/value pairs held by a `QueryParameters` object can now be retrieved via the new `size()` method.
- Added a bunch of JUnit5 tests. Note: tests will be executed against a build-in netty server and thus do not require an active Internet connection.
- Added maven dependencies for _org.mock-server:*_ `5.11.1` and _org.assertj:assertj-core_ `3.16.1` for JUnit testing.
- New class `HttpHeaders` holding constants of commonly used HTTP header names.
- Included surefire plugin in version `3.0.0-M5`.
- Added option to dynamically define the remote endpoint to which the api-connector will send its requests to. Can be used for proxying requests. See new `Proxy` class.

### Changed
- The connector now throws an `APIPreconditionException` when trying to invoke any [/user](https://api.thetvdb.com/swagger#/Users) route with APIKey-only authentication.
- JSON deserialization of series header requests now returns a corresponding `APIResponse` object.
- `HeadRequest.getResponse()` now returns the mapped header values in a nested _"data"_ JSON node.
- Consider two `QueryParameters` objects to be equal when they have the same key/value pair. Invoking _toString()_ will now return an overview of the actual key/value pairs.
- Reduced the visibility of some internal public constructors and String constants.
- Updated several maven plugins and dependencies to latest version:
    - _org.apache.maven.plugins:maven-compiler-plugin_: `3.8.0` -> `3.8.1`
    - _org.apache.maven.plugins:maven-javadoc-plugin_: `3.1.0` -> `3.2.0`
    - _com.fasterxml.jackson.core:jackson-databind_: `2.9.5` -> `2.11.2`
    - _org.immutables:value_: `2.8.2` -> `2.8.8`
    - _org.slf4j:slf4j-api_: `1.7.25` -> `1.7.30`
- Replaced JUnit4 with JUnit5 maven dependency.
- Switched to Mockito v.3.
- Enhanced some JavaDoc documentation. Fixed several typos.

### Fixed
- Fixed a typo in the`APIResponse` String representation.
- Fixed a typo in `Series.getArisTime()` method name.
- `Parameters.validatePathParam()` now properly validates the actual parameter value instead of the (only descriptive) parameter name.
- Fixed issue with "Accept" HTTP headers being overwritten in API requests.

## [0.0.6] - 2020-06-16
### Added
- Remote API requests now include the API version to be used.
- Enum for the different HTTP request methods.
- Mapping for the [/search/series](https://api.thetvdb.com/swagger#!/Search/get_search_series) routes JSONs _data.image_ property.
- Introduced new wrapper for some common _@FunctionalInterface_ interfaces which allow a specific exception to be thrown during their execution.
- New util class `Preconditions` that can be used to check certain requirements before executing some specific code.
- Util methods in `APIUtil` for creating a more readable String-representation for _Lists_ and _Optionals_.
- Some more jsr305 annotations.
- New constant for _slug_ query parameter in `Query` parameters class.
- Some additional JavaDoc.

### Changed
- Method `APIUtil.prettyPrint(JsonNode)` now throws a more specific _JsonProcessingException_.
- Small refactoring of API response JSON deserialization.
- `APINotAuthorizedException` is now a sub-type of `APICommunicationException`.
- Re-enabled doclint setting in the JavaDoc maven plugin and added some configuration on how the output should look like.
- Full refactoring of method parameter and precondition checks. Removed many of the specific validator classes and consolidated parts of their functionality into two classes: `Parameters` and `Preconditions`.
- In order to reduce boilerplate code, the hardcoded API data model implementations have been replaced by some auto-generated code based on the [Immutables](https://immutables.github.io/) library.
- Methods in `APIUtil` now return primitive boolean values.
- Converted some validators to _java.util.function.Predicate_. Changed corresponding method signatures.
- Changed class name suffix for API data model implementations.

### Fixed
- Fixed some unpleasant JavaDoc formatting.

### Removed
- Removed `QueryResource.createQueryResource(String, QueryParameters)` method.
- Removed `APIValidationException` class. Replaced by the new `APIPreconditionException` and _java.lang.IllegalArgumentException_.
- `ConnectionValidator` has been removed. Its functionality has been relocated into `Preconditions` class.
- Functionality of `PathValidator`, `QueryValidator` and `ObjectValidator` has been consolidated into the new `Parameters` class.

## [0.0.5] - 2020-05-09
### Added
- Some additional JavaDoc.
- Additional parameter and precondition validations.
- Extended jsr305 annotations.
- `#deleteFromRatings()` is now available in all API layouts.
- Option to provide and use an existing JWT (without requesting one via the [/login](https://api.thetvdb.com/swagger#!/Authentication/post_login) route).
- Proper `toString()` implementations for API data models.

### Changed
- `APIRequest#openConnection` now returns the newly created connection again.
- Refactoring of API requests. Consolidated common functionality in an abstract superclass.
- Slightly changed exception handling.
- Use non-primitive data types in API data model interfaces.
- Reduced the visibility of some properties and methods to default or private.

### Fixed
- Issue with JSON deserialization of favorites API response in case the user has no favorites.
- Session status can no longer be set to `null`.
- Default language can no longer be set to `null`.

### Removed
- `ParamValidator` class has been replaced by `PathValidator` and `QueryValidator`.

## [0.0.4] - 2019-04-08
### Added
- New nested interface `TheTVDBApi.Extended` holding all API methods returning an `APIResponse<DTO>` object.
- Included new wrapper class for remote API responses containing extended error and pagination data.
- Added type resolver with general _Interface<->Implementation_ mappings to `JSONDeserializer`.
- New nested interface `TheTVDBApi.JSON` holding all API methods returning a _JsonNode_ object.
- Mapping for the [/search/series](https://api.thetvdb.com/swagger#!/Search/get_search_series) routes JSONs _data.slug_ property.
- Return more detailed information in case of errors or failed validations.
- Some additional JavaDoc.

### Changed
- Moved all API methods returning a _JsonNode_ object into the new `TheTVDBApi.JSON` layout.
- Complete refactoring of `JsonDeserializer` class.
- Renamed class `SeriesAbstract` to `SeriesSearchResult`.
- Moved API response data models into a distinct sub-package.
- The APIs `#getAvailableFilterParameters` method has been renamed to `#getAvailableSeriesFilterParameters`.
- Calls to the APIs [/series/{id}/episodes\[/query\]](https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes) route will now return an `Episode` instead of an `EpisodeAbstract` object.
- Calls to the APIs [/search/series](https://api.thetvdb.com/swagger#!/Search/get_search_series) route will now return a `SeriesSearchResult` instead of a `SeriesAbstract` object.
- Introduced parameterized types in validations of `ParamValidator` class.
- `APIRequest#openConnection` does no longer return the newly created connection.
- Reduced the visibility of some properties and methods to default or private.

### Fixed
- Issue with auto-authentication when invoking the [/refresh_token](https://api.thetvdb.com/swagger#!/Authentication/get_refresh_token) route.

### Removed
- `EpisodeAbstract` data model and included its content into the existing `Episode` model.

## [0.0.3] - 2019-03-24
### Added
- Validation and enhanced error handling for JWT tokens used for API authentication.

### Changed
- Added validation for `apiKey`, `userKey` and `userName` parameter.
- Reordered parameters in API constructors.
- Handle the API's userKey and userName properties as Optionals.

## [0.0.2] - 2019-03-20
### Added
- New class `QueryParameters` representing a set of query parameters (replaces the old java.util.Map key/value pairs).
- New class `Query` containing static constants for all available API query parameters.
- New class `TheTVDBApiFactory` to be used to create instances of this API.
- Some additional JavaDoc.

### Changed
- Replaced java.util.Map query parameters by the new `QueryParameters` object in API interface method signature.
- Changed packaging structure to encapsulate the actual API implementation. Extracted interfaces from data models.
- Fetch response content from error stream in case the API did not return HTTP-200.
- Disabled JavaDoc creation during maven release.

### Fixed
- Infinite loop in auto-authentication mechanism.

## [0.0.1] - 2019-03-16
### Added
- First basic implementation draft of TheTVDB.com RESTful API Java connector.

[Unreleased]: https://github.com/m0nk3y2k4/thetvdb-java-api/compare/3.0.4...HEAD
[3.0.4]: https://github.com/m0nk3y2k4/thetvdb-java-api/compare/3.0.3...3.0.4
[3.0.3]: https://github.com/m0nk3y2k4/thetvdb-java-api/compare/3.0.2...3.0.3
[3.0.2]: https://github.com/m0nk3y2k4/thetvdb-java-api/compare/3.0.1...3.0.2
[3.0.1]: https://github.com/m0nk3y2k4/thetvdb-java-api/compare/tvdb-japi-3.0.0...3.0.1
[3.0.0]: https://github.com/m0nk3y2k4/thetvdb-java-api/compare/tvdb-japi-0.0.8...tvdb-japi-3.0.0
[0.0.8]: https://github.com/m0nk3y2k4/thetvdb-java-api/compare/tvdb-japi-0.0.7...tvdb-japi-0.0.8
[0.0.7]: https://github.com/m0nk3y2k4/thetvdb-java-api/compare/tvdb-japi-0.0.6...tvdb-japi-0.0.7
[0.0.6]: https://github.com/m0nk3y2k4/thetvdb-java-api/compare/tvdb-japi-0.0.5...tvdb-japi-0.0.6
[0.0.5]: https://github.com/m0nk3y2k4/thetvdb-java-api/compare/tvdb-japi-0.0.4...tvdb-japi-0.0.5
[0.0.4]: https://github.com/m0nk3y2k4/thetvdb-java-api/compare/tvdb-japi-0.0.3...tvdb-japi-0.0.4
[0.0.3]: https://github.com/m0nk3y2k4/thetvdb-java-api/compare/tvdb-japi-0.0.2...tvdb-japi-0.0.3
[0.0.2]: https://github.com/m0nk3y2k4/thetvdb-java-api/compare/tvdb-japi-0.0.1...tvdb-japi-0.0.2
[0.0.1]: https://github.com/m0nk3y2k4/thetvdb-java-api/releases/tag/tvdb-japi-0.0.1

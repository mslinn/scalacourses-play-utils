# scalacourses-play-utils

[![Build Status](https://travis-ci.org/mslinn/scalacourses-play-utils.svg?branch=master)](https://travis-ci.org/mslinn/scalacourses-play-utils)
[ ![Download](https://api.bintray.com/packages/micronautics/play/scalacourses-play-utils/images/download.svg) ](https://bintray.com/micronautics/play/scalacourses-play-utils/_latestVersion)

Utilities for Play Referenced from ScalaCourses.com

See the unit tests for usage examples.

## Installing ##

Add two lines to `build.sbt`.

 * Add the `scalacourses-play-utils` dependency:
````
"com.micronautics" %% "scalacourses-play-utils" % "0.1.12" withSources()
````

 * Add this to the `resolvers`:
````
"micronautics/play on bintray" at "http://dl.bintray.com/micronautics/play"
````

This library has been built against Scala 2.10.6 / Play 2.2.6 and Scala 2.11.8 / Play 2.5.12.

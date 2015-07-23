# scalacourses-play-utils
Utilities for Play Referenced from ScalaCourses.com

## Installing ##

Add two lines to `build.sbt`.

 * Add the `pfview` dependency:
````
"com.micronautics" %% "scalacourses-play-utils" % "0.1.0" withSources()
````

 * Add this to the `resolvers`:
````
"micronautics/play on bintray" at "http://dl.bintray.com/micronautics/play"
````

This library has been built against Scala 2.10.5 / Play 2.2.6 and Scala 2.11.7 / Play 2.4.2.

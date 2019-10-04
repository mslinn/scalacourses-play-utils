/* Copyright 2012-2019 Micronautics Research Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. */

version := "0.2.1"
name := "scalacourses-play-utils"
organization := "com.micronautics"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

scalaVersion := "2.13.1"    // comment this line to use Scala 2.12
//scalaVersion := "2.12.10" // uncomment this line to use Scala 2.13
crossScalaVersions := Seq("2.12.10", "2.13.1")

scalacOptions ++= (
  scalaVersion {
    case sv if sv.startsWith("2.10") => List(
      "-target:jvm-1.7"
    )
    case _ => List(
      "-target:jvm-1.8",
      "-Ywarn-unused"
    )
  }.value ++ Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked"
  )
)

scalacOptions in Test ++= Seq("-Yrangepos")

scalacOptions in (Compile, doc) ++= baseDirectory.map {
  bd: File => Seq[String](
     "-sourcepath", bd.getAbsolutePath,
     "-doc-source-url", s"https://github.com/mslinn/scalacourses-play-utils/tree/master€{FILE_PATH}.scala"
  )
}.value

libraryDependencies ++= scalaVersion {
  case sv if sv.startsWith("2.13") =>
    Seq(
      "javax.inject"           %  "javax.inject"       % "1"      withSources(),
      "com.typesafe.play"      %% "play"               % "2.7.3"  % Provided withSources(),
      "com.typesafe.play"      %% "play-json"          % "2.7.4"  % Provided withSources(),
      "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3"  % Test,
      "ch.qos.logback"         %  "logback-classic"    % "1.2.3"
    )

  case sv if sv.startsWith("2.12") =>
    Seq(
      "com.typesafe.play"      %% "play"               % "2.6.23" % Provided withSources(),
      "com.typesafe.play"      %% "play-json"          % "2.7.4"  % Provided withSources(),
      "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2"  % Test,
      "ch.qos.logback"         %  "logback-classic"    % "1.2.3"
    )
}.value

javacOptions ++= Seq(
  "-Xlint:deprecation",
  "-Xlint:unchecked",
  "-source", "1.8",
  "-target", "1.8",
  "-g:vars"
)

resolvers ++= Seq(
)

logLevel := Level.Warn

// Only show warnings and errors on the screen for compilations.
// This applies to both test:compile and compile and is Info by default
logLevel in compile := Level.Warn

// Level.INFO is needed to see detailed output when running tests
logLevel in test := Level.Info

// define the statements initially evaluated when entering 'console', 'console-quick', but not 'console-project'
initialCommands in console := """
                                |""".stripMargin

cancelable := true

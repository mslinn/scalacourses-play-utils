version := "0.2.1"
name := "scalacourses-play-utils"
organization := "com.micronautics"
licenses += ("MIT", url("https://opensource.org/licenses/MIT"))

//scalaVersion := "2.13.1"
scalaVersion := "2.12.10"
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
      "com.typesafe.play"      %% "play"               % "2.7.3"  % Provided,
      "com.typesafe.play"      %% "play-json"          % "2.7.4"  % Provided,
      "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3"  % Test,
      "ch.qos.logback"         %  "logback-classic"    % "1.2.3"
    )
    Nil

  case sv if sv.startsWith("2.12") =>
    Seq(
      "com.typesafe.play"      %% "play"               % "2.6.23" % Provided,
      "com.typesafe.play"      %% "play-json"          % "2.7.4"  % Provided,
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

version := "0.1.12"
name := "scalacourses-play-utils"
organization := "com.micronautics"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

scalaVersion := "2.11.11"
crossScalaVersions := Seq("2.10.6", "2.11.11", "2.12.2")

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
    "-unchecked",
    "-Ywarn-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture",
    "-Xlint"
  )
)

scalacOptions in Test ++= Seq("-Yrangepos")

scalacOptions in (Compile, doc) ++= baseDirectory.map {
  (bd: File) => Seq[String](
     "-sourcepath", bd.getAbsolutePath,
     "-doc-source-url", s"https://github.com/mslinn/scalacourses-play-utils/tree/master€{FILE_PATH}.scala"
  )
}.value

libraryDependencies ++= scalaVersion {
  case sv if sv.startsWith("2.12") =>
    val playVer = "2.6.2"
    Seq(
      "com.typesafe.play"      %% "play"               % playVer     % "provided",
      "com.typesafe.play"      %% "play-json"          % playVer     % "provided",
      "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0"     % "test",
      "ch.qos.logback"         %  "logback-classic"    % "1.2.1"
    )

  case sv if sv.startsWith("2.11") =>
    val playVer = "2.5.16"
    Seq(
      "com.typesafe.play"      %% "play"               % playVer % "provided",
      "com.typesafe.play"      %% "play-json"          % playVer % "provided",
      "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % "test",
      "ch.qos.logback"         %  "logback-classic"    % "1.2.1"
    )

  case sv if sv.startsWith("2.10") =>
    Seq(
      "com.typesafe.play" %% "play" % "2.2.6" % "provided",
      "org.scalatestplus" %% "play" % "1.0.0" % "test"
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
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases"
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

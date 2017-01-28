version := "0.1.12"
name := "scalacourses-play-utils"
organization := "com.micronautics"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

crossScalaVersions := Seq("2.10.6", "2.11.8")
scalaVersion := "2.11.8"
scalacOptions ++= scalaVersion {
  case sv if sv.startsWith("2.11") =>
    Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-target:jvm-1.8", "-unchecked",
    "-Ywarn-adapted-args", "-Ywarn-value-discard", "-Xlint")

  case sv if sv.startsWith("2.10") =>
    Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-target:jvm-1.7", "-unchecked",
        "-Ywarn-adapted-args", "-Ywarn-value-discard", "-Xlint")
}.value

scalacOptions in Test ++= Seq("-Yrangepos")

scalacOptions in (Compile, doc) <++= baseDirectory.map {
  (bd: File) => Seq[String](
     "-sourcepath", bd.getAbsolutePath,
     "-doc-source-url", "https://github.com/mslinn/scalacourses-play-utils/tree/master€{FILE_PATH}.scala"
  )
}

libraryDependencies ++= scalaVersion {
  case sv if sv.startsWith("2.11") =>
    val playVer = "2.5.12"
    Seq(
      "com.typesafe.play"      %% "play"               % playVer % "provided",
      "com.typesafe.play"      %% "play-json"          % playVer % "provided",
      "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % "test",
      "ch.qos.logback"         %  "logback-classic"    % "1.1.9"
    )

  case sv if sv.startsWith("2.10") =>
    Seq(
      "com.typesafe.play" %% "play" % "2.2.6" % "provided",
      "org.scalatestplus" %% "play" % "1.0.0" % "test"
    )
}.value

javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.8", "-target", "1.8", "-g:vars")

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

bintrayOrganization := Some("micronautics")
bintrayRepository := "play"

publishArtifact in Test := false

cancelable := true

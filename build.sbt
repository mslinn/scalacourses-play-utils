import bintray.Keys._

version := "0.1.1"
name := "scalacourses-play-utils"
organization := "com.micronautics"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

crossScalaVersions := Seq("2.10.5", "2.11.7")
scalaVersion := "2.11.7"
scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-target:jvm-1.7", "-unchecked",
    "-Ywarn-adapted-args", "-Ywarn-value-discard", "-Xlint")
scalacOptions in (Compile, doc) <++= baseDirectory.map {
  (bd: File) => Seq[String](
     "-sourcepath", bd.getAbsolutePath,
     "-doc-source-url", "https://github.com/mslinn/scalacourses-play-utils/tree/master€{FILE_PATH}.scala"
  )
}
scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies <++= scalaVersion {
  case sv if sv.startsWith("2.11") =>
    Seq(
      "com.typesafe.play" %% "play-json" % "2.4.2"    % "provided",
      "org.scalatestplus" %% "play"      % "1.4.0-M3" % "test"
    )

  case sv if sv.startsWith("2.10") =>
    Seq(
      "com.typesafe.play" %% "play"      % "2.2.6" % "provided",
      "org.scalatestplus" %% "play"      % "1.0.0" % "test"
    )
}

javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.7", "-target", "1.7", "-g:vars")

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

bintrayPublishSettings
bintrayOrganization in bintray := Some("micronautics")
repository in bintray := "play"
publishArtifact in Test := false

cancelable := true

sublimeTransitive := true


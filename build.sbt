name := """DAART"""

lazy val commonSettings = Seq(
  organization := "ch.epfl",
  version := "0.1",
  scalaVersion := "2.11.6",
  libraryDependencies ++= commonLibs
)

lazy val commonLibs = Seq(
  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.typesafe.akka" %% "akka-actor" % "2.3.11"
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    /* Project specific settings */
  ).dependsOn(util)

lazy val util = (project in file("util"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
      "org.json4s" %% "json4s-jackson" % "3.2.11"
    )
  )


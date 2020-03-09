import sbt.project

name := "relayr-challenge"

version := "0.1"

scalaVersion := "2.13.1"

lazy val common = (project in file("common"))
  .settings(
    libraryDependencies ++= Dependencies.common ++ Dependencies.test
  )

lazy val simple = (project in file("simple"))
  .dependsOn(common)
  .settings(
    libraryDependencies ++= Dependencies.common ++ Dependencies.test
  )

lazy val actor = (project in file("actor"))
  .dependsOn(common)
  .settings(
    libraryDependencies ++= Dependencies.common ++ Dependencies.actor ++ Dependencies.test
  )

import sbt.project

name := "relayr-challenge"

version := "0.1"

scalaVersion in ThisBuild := "2.13.1"

scalacOptions in ThisBuild += "-Ymacro-annotations"

lazy val common = (project in file("common"))
  .settings(
    libraryDependencies ++= Dependencies.common ++ Dependencies.test
  )

lazy val simple = (project in file("simple"))
  .dependsOn(common)
  .settings(
    libraryDependencies ++= Dependencies.common ++ Dependencies.test
  )

lazy val zio = (project in file("zio"))
  .dependsOn(common)
  .settings(
    libraryDependencies ++= Dependencies.common ++ Dependencies.zio ++ Dependencies.testZio,
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
    scalacOptions += "-Ymacro-annotations"
  )

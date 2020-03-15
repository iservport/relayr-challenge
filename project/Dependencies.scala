import sbt._

object Dependencies {

  val common = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "ch.qos.logback"             %  "logback-classic"   % "1.2.3"
  )

  val actor = Seq(
    "com.typesafe.akka"          %% "akka-actor-typed" % "2.6.3"
  )

  val zio = Seq(
    "dev.zio"                    %% "zio"              % "1.0.0-RC18-2"
  )

  val test = Seq(
    "org.scalatest"              %% "scalatest"        % "3.1.1"      % "test",
    "org.mockito"                %  "mockito-core"     % "3.3.0"      % "test"
    )

  val testZio = Seq(
    "dev.zio"                   %% "zio-test"          % "1.0.0-RC18-2" % "test",
    "dev.zio"                   %% "zio-test-sbt"      % "1.0.0-RC18-2" % "test"
  )
}

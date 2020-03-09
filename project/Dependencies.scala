import sbt._

object Dependencies {

  val common = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  )

  val actor = Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % "2.6.3"
  )

  val test = Seq(
    "org.scalatest"           %% "scalatest"                % "3.1.1"                 % "test",
    "org.mockito"             %  "mockito-core"             % "3.3.0"                 % "test"
    )
}

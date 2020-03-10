package relayr.challenge.zio

import zio.console.Console
import zio.{App, ZEnv, ZIO, console}

object Application extends App {

  val program: ZIO[Console, Nothing, Unit] =
    console.putStrLn("Elevator Control System")

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    program.flatMap(
      _ => ZIO.succeed(0)
    )
  }
}

package relayr.challenge.effectful

import relayr.challenge.effectful.module.EventParser
import relayr.challenge.effectful.module.EventParser.EventParser
import relayr.challenge.model.{Elevator, ElevatorError}
import zio._
import zio.console.Console

object Application extends App {

  val elevators: IO[ElevatorError, Ref[Vector[Elevator]]] = Ref.make((1 to 3).map(eid => Elevator(id = eid)).toVector)
  final val elevatorSystem: IO[ElevatorError, FunctionalElevatorControlSystem] = elevators.map(new FunctionalElevatorControlSystem(_))

  type AppEnv = EventParser with Console
  object AppEnv {
    val live = EventParser.live ++ Console.live
  }

  val program: ZIO[EventParser with Console, Object, FunctionalElevatorControlSystem] = {

    def loop(state: FunctionalElevatorControlSystem): ZIO[Console with EventParser, Object, FunctionalElevatorControlSystem] =
      (for {
      _ <- console.putStrLn("Elevator Control System")
      _ <- console.putStrLn("Enter command: pickup  | status | tick")
      _ <- console.putStr(">> ")

      input   <- console.getStrLn
      command <- EventParser.parse(input)
      out     <- state.receive(command)
      status  <- out.statusAsString()
      _       <- console.putStrLn(s">>>> Status:\n$status\n")
    } yield out).flatMap(loop).foldM(
      _           => elevatorSystem
      , nextState => loop(nextState)
    )

    elevatorSystem.flatMap(loop)
  }

  override def run(args: List[String]): ZIO[ZEnv, Nothing, Int] = {
    program.provideCustomLayer(AppEnv.live).foldM(
      _ => ZIO.succeed(1),
      r => console.putStrLn(r.toString).as(0)
    )
  }

}

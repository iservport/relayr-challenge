package relayr.challenge.effectful

import relayr.challenge.model.{Elevator, ElevatorError, Pickup}
import zio._
import zio.test.Assertion._
import zio.test._

object FunctionalElevatorControlSystemSpec extends DefaultRunnableSpec {

  override def spec: ZSpec[_root_.zio.test.environment.TestEnvironment, Any] = {

    val elevators: IO[ElevatorError, Ref[Vector[Elevator]]] = Ref.make((1 to 3).map(eid => Elevator(id = eid)).toVector)
    val elevatorSystem: IO[ElevatorError, FunctionalElevatorControlSystem] = elevators.map(new FunctionalElevatorControlSystem(_))

    suite("ElevatorControlSystem") (

      testM("has 3 elevators") {
        for {
          ecs       <- elevatorSystem
          elevators <- ecs.status()
        } yield {
          assert(elevators.length)(equalTo(3))
        }
      },
      testM("can update an elevator state") {
        for {
          ecs       <- elevatorSystem
          updated   <- ecs.update(Elevator(1, 1))
          status    <- ecs.status()
        } yield {
          assert(status)(equalTo(updated))
        }
      },
      testM("can pickup a trip to floor 1") {
        val stops: ZIO[Any, ElevatorError, Vector[Int]] = for {
          ecs      <- elevatorSystem
          out      <- ecs.pickup(Pickup("0", "1"))
        } yield out.flatMap(_.stops)
        assertM(stops)(contains(1))

      },
      testM("can pickup a trip from floor 2 to floor 3") {
        val stops: ZIO[Any, ElevatorError, Vector[Int]] = for {
          ecs      <- elevatorSystem
          out      <- ecs.pickup(Pickup("2", "3"))
        } yield out.flatMap(_.stops)
        assertM(stops)(contains(2)) *> assertM(stops)(contains(3))

      },
      testM("can move elevators to next step") {
        val floors: ZIO[Any, ElevatorError, Vector[Int]] = for {
          ecs       <- elevatorSystem
          _         <- ecs.pickup(Pickup("0", "1"))
          moved     <- ecs.tick("1")
          elevators <- moved.elevators.get
        } yield elevators.map(_.floor)
        assertM(floors)(contains(1))
      }
    )//ElevatorControlSystem
  }
}

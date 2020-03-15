package relayr.challenge.simple

import org.slf4j.{Logger, LoggerFactory}
import relayr.challenge.model.{Elevator, ElevatorError, ElevatorErrorData, Trip}

import scala.annotation.tailrec
import scala.collection.immutable.Queue
import scala.collection.mutable
import scala.collection.mutable.Iterable

class SimpleElevatorControlSystem {

  val logger: Logger = LoggerFactory.getLogger(classOf[SimpleElevatorControlSystem])

  var elevators: mutable.Set[Elevator] = mutable.Set(Elevator())
  var requests: Queue[Trip] = Queue()

  def get(id: Int): Option[Elevator] = elevators.find(_.id == id)

  def status(): Iterable[Elevator] = elevators

  def update(elevator: Elevator): Iterable[Elevator] = {
    val updated = elevators.find(_.id == elevator.id)
    elevators.+=(elevator)
  }

  def step(): Unit = elevators.foreach{ elevator =>
    elevator.move
  }

  def move(runnableElevator: Either[ElevatorError, Elevator]): Either[ElevatorError, Elevator] =
    runnableElevator.map(elevator => elevator.move)

  def pickup(trip: Trip): Unit =
    recurse(trip, elevators.toList.sortBy(elevator => Math.abs(elevator.floor - trip.source))) match {
      case Some(closest) => elevators = elevators + closest
      case None => logger.debug(s"All elevators refused trip ${trip}")
    }

  @tailrec
  private def recurse(trip: Trip, closest: List[Elevator]): Option[Elevator] =
    closest.headOption.map(_.pickup(trip)) match {
      case None => None
      case Some(Right(elevator)) => Some(elevator)
      case error =>
        error.foreach {
          case Left(e: ElevatorErrorData) => logger.debug(s"Elevator id = ${e.elevator.id} refused trip ${e.trip} with error ${e.toString}")
          case e => logger.debug(s"Recursion error $e")
        }
        recurse(trip, closest.tail)
    }

}

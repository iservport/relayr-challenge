package relayr.challenge.simple

import org.slf4j.{Logger, LoggerFactory}
import relayr.challenge.model.{AbstractElevatorSystem, Elevator, ElevatorError, Trip}

import scala.annotation.tailrec
import scala.collection.mutable

class SimpleElevatorControlSystem extends AbstractElevatorSystem {

  val logger: Logger = LoggerFactory.getLogger(classOf[SimpleElevatorControlSystem])

  override var elevators: mutable.Set[Elevator] = mutable.Set(Elevator())

  override def step(): Unit = elevators.foreach{ elevator =>
    elevator.move
  }

  def move(runnableElevator: Either[ElevatorError, Elevator]): Either[ElevatorError, Elevator] =
    runnableElevator.map(elevator => elevator.move)

  override def pickup(trip: Trip): Unit =
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
          case Left(e) => logger.debug(s"Elevator id = ${e.elevator.id} refused trip ${e.trip} with error ${e.toString}")
        }
        recurse(trip, closest.tail)
    }

}

package relayr.challenge.model

import relayr.challenge.ElevatorControlSystem

import scala.collection.immutable.Queue
import scala.collection.mutable

abstract class AbstractElevatorSystem extends ElevatorControlSystem {

  var elevators: mutable.Set[Elevator]
  var requests: Queue[Trip] = Queue()

  def get(id: Int): Option[Elevator] = elevators.find(_.id == id)

  def update(elevator: Elevator): Unit = {
    val updated = elevators.find(_.id == elevator.id)
    elevators.+=(elevator)
  }

}

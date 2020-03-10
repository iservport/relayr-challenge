package relayr.challenge

import relayr.challenge.model.{Elevator, Trip}

trait ElevatorControlSystem {

  def get(id: Int): Option[Elevator]

  def update(elevator: Elevator): Unit

  def step(): Unit

  def pickup(trip: Trip): Unit

}

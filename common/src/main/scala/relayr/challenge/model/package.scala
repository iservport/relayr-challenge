package relayr.challenge

package object model {

  sealed trait ElevatorError
  case class ElevatorSystemUnavailable() extends ElevatorError
  case class ElevatorNotFound() extends ElevatorError
  case class ElevatorIdNotValid(id: String) extends ElevatorError
  case class ElevatorFloorNotValid(floor: String) extends ElevatorError
  case class ElevatorTickNotValid(times: String) extends ElevatorError
  trait ElevatorErrorData extends ElevatorError {
    val elevator: Elevator
    val trip: Trip
  }
  case class MaximumWeightExceeded(elevator: Elevator, trip: Trip) extends ElevatorErrorData
  case class MaximumCapacityExceeded(elevator: Elevator, trip: Trip) extends ElevatorErrorData


}

package relayr.challenge

package object model {

  sealed trait ElevatorError
  case class ElevatorNotFound() extends ElevatorError
  trait ElevatorErrorData extends ElevatorError {
    val elevator: Elevator
    val trip: Trip
  }
  case class MaximumWeightExceeded(elevator: Elevator, trip: Trip) extends ElevatorErrorData
  case class MaximumCapacityExceeded(elevator: Elevator, trip: Trip) extends ElevatorErrorData

  sealed trait ElevatorType {
    val capacity: Int = 8
    val maxLoad: Int = 560
  }
  case object StandardType extends ElevatorType
  case object LoadType extends ElevatorType {
    override val capacity: Int = 12
    override val maxLoad: Int = 1000
  }

}

package relayr.challenge

import relayr.challenge.model.Elevator

package object actor {

  sealed trait ElevatorControlMessages
  case class Update(elevator: Elevator) extends ElevatorControlMessages
  case class Step() extends ElevatorControlMessages

}

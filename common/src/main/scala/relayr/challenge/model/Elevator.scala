package relayr.challenge.model

import scala.collection.immutable.Queue

case class Elevator(
                     floor: Int = 0,
                     direction: Int = 0,
                     id: Int = 1,
                     tripLoad: Queue[Trip] = Queue(),
                     elevatorType: ElevatorType = StandardType) {

  def pickup(trip: Trip): Either[ElevatorError, Elevator] =
    pickup(tripLoad.enqueue(trip))

  def pickup(nextTripLoad: Queue[Trip]): Either[ElevatorError, Elevator] =
    validateSafetyLimits(nextTripLoad)

  val stops: Seq[Int] = tripLoad.groupBy(_.destination).keys.toList

  val nextStop: Int = tripLoad.map(_.destination).headOption.getOrElse(floor)

  def move: Elevator = {
    val nextDirection: Int = (nextStop - floor).signum
    val nextFloor: Int = floor + nextDirection
    copy(
      floor = nextFloor,
      direction = nextDirection,
      tripLoad = tripLoad.filterNot(_.destination == nextFloor)
    )
  }

  private def validateSafetyLimits(nextTripLoad: Queue[Trip]): Either[ElevatorError, Elevator] =
    if (nextTripLoad.map(_.weight).sum > elevatorType.maxLoad) Left(MaximumWeightExceeded(this, nextTripLoad.last))
    else if (nextTripLoad.length > elevatorType.capacity) Left(MaximumCapacityExceeded(this, nextTripLoad.last))
    else Right(copy(tripLoad = nextTripLoad))

  override def equals(o:Any): Boolean = o match {
    case Elevator(_, _, oid, _, _) if oid == id=> true
    case _ => false
  }

  override def hashCode: Int = (id).##

}

package relayr.challenge.model

import scala.collection.immutable.Queue

case class Elevator(
                     position: Float = 0.0f,
                     direction: Int = 0,
                     id: Int = 1,
                     content: Queue[Trip] = Queue(),
                     elevatorType: ElevatorType = StandardType) {

  val floor: Int = Math.floor(position).toInt

  def pickup(trip: Trip): Either[ElevatorError, Elevator] =
    pickup(content.enqueue(trip))

  def pickup(nextContent: Queue[Trip]): Either[ElevatorError, Elevator] =
    validateSafetyLimits(nextContent)

  def distance(trip: Trip): Int = {
    val relativeDistance = trip.source - floor
    Math.abs {
      if (relativeDistance.sign == direction) relativeDistance else relativeDistance+1000
    }
  }

  val stops: Seq[Int] = content.groupBy(_.source).keys.toList ++ content.groupBy(_.destination).keys.toList

  val nextStop: Int = content.map(_.destination).headOption.getOrElse(floor)

  def move: Elevator = {
    val nextDirection: Int = (nextStop - floor).sign
    val willStop  = stops.nonEmpty && stops.contains(nextStop) // FIXME this is not guaranteed
    val willStart = stops.nonEmpty && stops.contains(floor)
    val accelerationMode = (willStart, willStop) match {
      case (true, true)  => AccelerationStartAndStop
      case (true, false) => AccelerationStart
      case (false, true) => AccelerationStop
      case _             => AccelerationTravel
    }
    val nextPosition = position + elevatorType.moveBy(1 * nextDirection, accelerationMode)
    val nextFloor: Int = Math.ceil(nextPosition).toInt
    copy(
      position = nextFloor,
      direction = nextDirection,
      content = content.filterNot(_.destination == nextFloor)
    )
  }

  private def validateSafetyLimits(nextContent: Queue[Trip]): Either[ElevatorError, Elevator] =
    if (nextContent.map(_.weight).sum > elevatorType.maxLoad) Left(MaximumWeightExceeded(this, nextContent.last))
    else if (nextContent.length > elevatorType.capacity) Left(MaximumCapacityExceeded(this, nextContent.last))
    else Right(copy(content = nextContent))

  val directionAsString: String = direction match {
    case 1  => "moving upwards to"
    case -1 => "moving downwards to"
    case _  => "and stopped"
  }

  override def toString = s"Elevator $id is at floor $floor $directionAsString ${stops.mkString("["," ","]")}"

}

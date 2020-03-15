package relayr.challenge.model

sealed trait ElevatorType {
  val capacity: Int = 8
  val maxLoad: Int = 560
  val speed = 1.0f
  val accelerationFactor = 1.0f   // should be a factor between 0.5 and 1.0 to represent an acceleration delay
  val decelerationFactor = 1.0f   // should be a factor between 0.5 and 1.0 to represent an deceleration delay
  val loadingFactor = 1.0f        // should be a factor between 0.5 and 1.0 to represent door opening and elevator loading delay
  val unloadingFactor = 1.0f      // should be a factor between 0.5 and 1.0 to represent elevator unloading and door closing delay

  def moveBy(distance: Int, accelerationMode: AccelerationMode = AccelerationTravel): Float = speed*distance*delay(accelerationMode)

  // TODO consider acceleration and/or deceleration as well as loading and unloading delays
  private def delay(accelerationMode: AccelerationMode): Float = 1.0f
}
case object StandardType extends ElevatorType
case object LoadType extends ElevatorType {
  override val capacity: Int = 12
  override val maxLoad: Int = 1000
}


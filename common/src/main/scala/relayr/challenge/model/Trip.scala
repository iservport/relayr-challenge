package relayr.challenge.model

case class Trip(source: Int, destination: Int, weight: Int = 0) {

  val direction: Int = (destination - source).signum

}

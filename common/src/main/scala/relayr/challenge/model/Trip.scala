package relayr.challenge.model

case class Trip(source: Int, destination: Int, weight: Int = 0) {

  val direction: Int = (destination - source).sign

  override def toString: String = s"${if (weight>0) s"$weight kg"} from floor $source to $destination."

}

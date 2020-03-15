package relayr.challenge.model

sealed trait SystemEvent
case object Status extends SystemEvent
case class  Get(id: String) extends SystemEvent
case class  Update(id: String) extends SystemEvent
case class  Pickup(from: String, to: String, weight: String = "0") extends SystemEvent
case class  Tick(times: String = "1")  extends SystemEvent
case object Quit  extends SystemEvent

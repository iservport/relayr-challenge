package relayr.challenge.model

sealed trait AccelerationMode
case object  AccelerationStart        extends AccelerationMode
case object  AccelerationStop         extends AccelerationMode
case object  AccelerationStartAndStop extends AccelerationMode
case object  AccelerationTravel       extends AccelerationMode

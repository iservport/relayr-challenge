package relayr.challenge.actor

import akka.actor.{Actor, ActorLogging}
import org.slf4j.{Logger, LoggerFactory}
import relayr.challenge.model.{AbstractElevatorSystem, Elevator, ElevatorError, Trip}

import scala.annotation.tailrec
import scala.collection.mutable

class ElevatorControlSystemActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case _ => ()
  }
}

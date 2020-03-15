package relayr.challenge.effectful.module

import relayr.challenge.model._
import zio._

import scala.util.matching.Regex

object EventParser {

  type EventParser = Has[EventParser.Service]

  trait Service {
    def parse(input: String): UIO[SystemEvent]
  }

  val getPattern:     Regex = "\\s*((?i)get|g).*(\\d{1,2}).*".r
  val updatePattern:  Regex = "\\s*((?i)update|u).*(\\d{1,2}).*".r
  val statusPattern:  Regex = "\\s*((?i)status|s).*".r
  val pickupPattern:  Regex = "\\s*((?i)pickup|p)\\s*(\\d{1,2})\\s*(\\d{1,2})\\s*".r
  val pickupWPattern: Regex = "\\s*((?i)pickup|p)\\s*(\\d{1,2})\\s*(\\d{1,2})\\s*(\\d{1,2})\\s*".r
  val tickPattern:    Regex = "\\s*((?i)tick|t)\\s*".r
  val tickTPattern:   Regex = "\\s*((?i)tick|t)\\s*(\\d{1,3})\\s*".r

  val live: ZLayer.NoDeps[Nothing, EventParser] = ZLayer.succeed(
    new Service {
      override def parse(input: String): UIO[SystemEvent] =
        UIO.succeed(input) map {
          case getPattern(_, id)                   => Get(id)
          case updatePattern(_, id)                => Update(id)
          case statusPattern(_)                    => Status
          case pickupPattern(_, from, to)          => Pickup(from, to, "0")
          case pickupWPattern(_, from, to, weight) => Pickup(from, to, weight)
          case tickPattern(_)                      => Tick("0")
          case tickTPattern(_, times)              => Tick(times)
          case _                                   => Quit
        }
    }
  )

  def parse(input: String): RIO[EventParser, SystemEvent] = ZIO.accessM(_.get.parse(input))

}

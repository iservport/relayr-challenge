package relayr.challenge.effectful

import relayr.challenge.model._
import zio.{IO, Ref, Schedule, ZIO}

final class FunctionalElevatorControlSystem(val elevators: Ref[Vector[Elevator]]) {

  def receive(command: SystemEvent): IO[ElevatorError, FunctionalElevatorControlSystem] =
    (command match {
      case Get(id)       => get(id)
      case Update(id)    => get(id).map(update)
      case Status        => status()
      case p:Pickup      => pickup(p)
      case Tick(times)   => tick(times)
      case invalid       => ZIO.succeed(s"Invalid command $invalid")
    }).map(r => this)

  def get(id: String): IO[ElevatorError, Elevator] =
    for {
      maybeId  <- IO(id.toInt).mapError(_ => ElevatorIdNotValid(id))
      elevator <- elevators.get.map(_.find(_.id == maybeId).toRight(ElevatorNotFound())).absolve
    } yield elevator

  def status(): IO[ElevatorError, Vector[Elevator]] =
    elevators.get

  def statusAsString(): IO[ElevatorError, String] =
    status().map(_.mkString("\n"))

  def pickup(p: Pickup): IO[ElevatorError, Vector[Elevator]] =
    for {
      trip        <- toTrip(p)
      available   <- findClosest(trip)
      accepted    <- IO.fromEither(available.pickup(trip))
      status      <- update(accepted)
    } yield status

  def toTrip(pickup: Pickup): IO[ElevatorError, Trip] =
    for {
      maybeFrom   <- IO(pickup.from.toInt).mapError(_ => ElevatorFloorNotValid(pickup.from))
      maybeTo     <- IO(pickup.to.toInt).mapError(_ => ElevatorFloorNotValid(pickup.to))
      maybeWeight <- IO(pickup.weight.toInt).orElseSucceed(0)
    } yield Trip(maybeFrom, maybeTo, maybeWeight)

  def update(elevator: Elevator): IO[ElevatorError, Vector[Elevator]] =
    elevators.updateAndGet { ec: Vector[Elevator] =>
      ec.filterNot(_.id == elevator.id):+(elevator)
    }

  def tick(times: String): IO[ElevatorError, FunctionalElevatorControlSystem] =
    for {
      maybeTimes <- IO(times.toInt).mapError(_ => ElevatorTickNotValid(times))
      _          <- elevators.update(_.map(_.move)).repeat(Schedule.recurs(maybeTimes))
    } yield this

  private def findClosest(trip: Trip): IO[ElevatorError, Elevator] = {
    (elevators.get.map { all =>
      all
        .map { elevator => (elevator, elevator.distance(trip)) }
        .sortBy(_._2) // TODO can we optimize considering load factor? use float?
        .headOption.map(_._1)
        .toRight(ElevatorSystemUnavailable())
    }).absolve
  }

}
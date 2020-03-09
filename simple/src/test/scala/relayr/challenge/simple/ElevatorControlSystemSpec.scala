package relayr.challenge.simple

import org.scalatest._
import org.scalatest.matchers.should.Matchers
import relayr.challenge.model.{Elevator, Trip}

import scala.collection.immutable.Queue

class ElevatorControlSystemSpec extends WordSpec with Matchers with EitherValues {

  "In a Simple Elevator Control System" when {

    val ecs = new SimpleElevatorControlSystem()

    "an elevator is called for a single trip" should {

      val singleTrip = Trip(0,3)
      val elevator1AtGround = ecs.get(1).map(_.pickup(singleTrip)).get

      "go to the destination floor step by step and stop" in {

        val elevator1At1 = ecs.move(elevator1AtGround)
        elevator1At1.right.value.floor should be (1)
        elevator1At1.right.value.tripLoad should contain (singleTrip)

        val elevator1At2 = ecs.move(elevator1At1)
        elevator1At2.right.value.floor should be (2)
        elevator1At2.right.value.tripLoad should contain (singleTrip)

        val elevator1At3 = ecs.move(elevator1At2)
        elevator1At3.right.value.floor should be (3)
        elevator1At3.right.value.tripLoad shouldBe empty

        val elevator1Stopped = ecs.move(elevator1At3)
        elevator1Stopped.right.value.floor should be (3)
        elevator1Stopped.right.value.direction should be (0)
      }
    }

    "an elevator is called for multiple trips" should {

      val tripOne = Trip(0,3)
      val tripTwo = Trip(0,2)
      val elevator1AtGround = ecs.get(1).map(_.pickup(Queue(tripOne, tripTwo))).get

      "go to the destination floor step by step and stop" in {

        val elevator1At1 = ecs.move(elevator1AtGround)
        elevator1At1.right.value.floor should be (1)
        elevator1At1.right.value.tripLoad should contain (tripOne)
        elevator1At1.right.value.tripLoad should contain (tripTwo)

        val elevator1At2 = ecs.move(elevator1At1)
        elevator1At2.right.value.floor should be (2)
        elevator1At2.right.value.tripLoad should contain (tripOne)
        elevator1At2.right.value.tripLoad should not contain (tripTwo)

        val elevator1At3 = ecs.move(elevator1At2)
        elevator1At3.right.value.floor should be (3)
        elevator1At3.right.value.tripLoad shouldBe empty

        val elevator1Stopped = ecs.move(elevator1At3)
        elevator1Stopped.right.value.floor should be (3)
        elevator1Stopped.right.value.direction should be (0)
      }
    }

    "an elevator has intermediate calls" should {

      val tripOne = Trip(0,3)
      val tripTwo = Trip(2,3)
      val elevator1AtGround = ecs.get(1).map(_.pickup(Queue(tripOne))).get

      "go to the destination floor step by step and stop" in {

        val elevator1At1 = ecs.move(elevator1AtGround)
        elevator1At1.right.value.floor should be (1)
        elevator1At1.right.value.tripLoad should contain (tripOne)

        val elevator1At2 = ecs.move(elevator1At1)
        elevator1At2.right.value.floor should be (2)
        elevator1At2.right.value.tripLoad should contain (tripOne)

        val elevator1At2WithNewCall = elevator1At2.flatMap(_.pickup(tripTwo))

        val elevator1At3 = ecs.move(elevator1At2WithNewCall)
        elevator1At3.right.value.floor should be (3)
        elevator1At3.right.value.tripLoad shouldBe empty

        val elevator1Stopped = ecs.move(elevator1At3)
        elevator1Stopped.right.value.floor should be (3)
        elevator1Stopped.right.value.direction should be (0)
      }
    }

    "an elevator has intermediate calls reversing direction" should {

      val tripOne = Trip(0,3)
      val tripTwo = Trip(2,1)
      val elevator1AtGround = ecs.get(1).map(_.pickup(Queue(tripOne))).get

      "go to the destination floor step by step and stop" in {

        val elevator1At1 = ecs.move(elevator1AtGround)
        elevator1At1.right.value.floor should be (1)
        elevator1At1.right.value.tripLoad should contain (tripOne)

        val elevator1At2 = ecs.move(elevator1At1)
        elevator1At2.right.value.floor should be (2)
        elevator1At2.right.value.tripLoad should contain (tripOne)

        val elevator1At2WithNewCall = elevator1At2.flatMap(_.pickup(tripTwo))

        val elevator1At3 = ecs.move(elevator1At2WithNewCall)
        elevator1At3.right.value.floor should be (3)
        elevator1At3.right.value.tripLoad should contain (tripTwo)

        val elevator1At2Desc = ecs.move(elevator1At3)
        elevator1At2Desc.right.value.floor should be (2)
        elevator1At2Desc.right.value.tripLoad should contain (tripTwo)

        val elevator1At1Desc = ecs.move(elevator1At2Desc)
        elevator1At1Desc.right.value.floor should be (1)
        elevator1At1Desc.right.value.tripLoad shouldBe empty

        val elevator1Stopped = ecs.move(elevator1At1Desc)
        elevator1Stopped.right.value.floor should be (1)
        elevator1Stopped.right.value.direction should be (0)
      }
    }
  }
}

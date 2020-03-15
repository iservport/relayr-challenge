package relayr.challenge.effectful.module


import relayr.challenge.effectful.module.EventParserUtils._
import relayr.challenge.model.{Pickup, Status, SystemEvent, Tick}
import zio.test.Assertion.equalTo
import zio.test.{DefaultRunnableSpec, assertM, suite, testM}

object EventParserSpec extends DefaultRunnableSpec {

    def spec =
      suite("EventParser")(
        suite("parse")(
          testM("`status | s` returns Status command") {
            checkParse("status", Status)
            checkParse("s", Status)
          },
          testM("`pickup | p` returns Pickup command") {
            checkParse("pickup 1 2", Pickup("1", "2"))
            checkParse("p 1 2", Pickup("1", "2"))
            checkParse("p 1 2 3", Pickup("1", "2", "3"))
          },
          testM("`tick   | t` returns Tick   command") {
            checkParse("tick", Tick("1"))
            checkParse("t 2", Tick("2"))
          }
        )
    )
}

object EventParserUtils {

  def checkParse(input: String, command: SystemEvent) = {

    val app    = EventParser.parse(input)
    val result = app.provideSomeLayer(EventParser.live)
    assertM(result)(equalTo(command))
  }
}
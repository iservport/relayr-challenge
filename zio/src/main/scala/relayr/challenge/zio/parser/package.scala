package relayr.challenge.zio

import relayr.challenge.zio.MenuCommand.{Quit, Step, Update}
import zio.{Has, UIO, ZLayer}

package object parser {

  type MenuCommandParser = Has[MenuCommandParser.Service]

  object MenuCommandParser {

    trait Service {
      def parse(input: String): UIO[MenuCommand]
    }

    val consoleParser: ZLayer.NoDeps[Nothing, MenuCommandParser] = ZLayer.succeed(
      (input: String) => UIO(input) map {
        case "update" => Update
        case "step"   => Step
        case _        => Quit
      }
    )
  }
}

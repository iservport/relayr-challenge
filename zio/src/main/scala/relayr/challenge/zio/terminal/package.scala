package relayr.challenge.zio

import zio.console.Console
import zio.{Has, UIO, ZLayer}

package object terminal {

  type Terminal = Has[Terminal.Service]

  object Terminal {

    trait Service {
      val getUserInput: UIO[String]
      def display(frame: String): UIO[Unit]
    }

    val liveTerminal: ZLayer[Console, Nothing, Terminal] = ZLayer.fromFunction( console =>
      new Service {

        override val getUserInput: UIO[String] =
          console.get.getStrLn.orDie

        override def display(frame: String): UIO[Unit] =
          console.get.putStrLn(frame)
      }
    )
  }
}

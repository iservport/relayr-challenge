package relayr.challenge

package object zio {

  sealed trait MenuCommand
  object MenuCommand {
    case object Update extends MenuCommand
    case object Step  extends MenuCommand
    case object Quit  extends MenuCommand
  }
}

package ru.catlerc

import cats.effect._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.util.EffectCatsEffect.io
// Cats Effect example code
// ========================

final case class Logger(log: String => SyncIO[Unit]) {
  def apply[A](name: String, effect: IO[A]): IO[A] =
    for {
      _ <- log(s"[$name] :kekw: IO делает быр быр").to[IO]
      a <- effect
      _ <- log(s"[$name] IO всё, закончился").to[IO]
    } yield a

  val a = implicitly[Sync[SyncIO]]
}

// Hooks example
// =============

final case class CounterProps(initialCount: Int, logger: Logger)

object Counter {
  def apply() = ScalaFnComponent.withHooks[CounterProps]
    .useStateBy(_.initialCount)
    .render { (props, state) =>

      val inc: SyncIO[Unit] = state.modState(_ + 1)

      val incAndLog: IO[Unit] =
        props.logger("counter", inc.to[IO])

      <.div(
        <.div("Counter: ", state.value),
        <.button("Increment", ^.onClick --> incAndLog),
        // Here we supply an IO[Unit] directly ^^^^^^
      )
    }
}

// Class Component example
// =======================

final class CounterAndLog($: BackendScope[Unit, String]) {

  private val logger =
  // As mentioned above, `.withEffect[SyncIO]` isn't needed when you've chosen Cats Effect as your default effect type
    Logger(str => $.modState(_ + "\n" + str))

  private val counter =
    Counter()(CounterProps(0, logger))

  def render(state: String): VdomNode = {
    <.div(
      counter,
      <.div(
        ^.id := "rete",
        ^.width := 10.cm,
        ^.height := 10.cm
      ),
      <.pre(
        ^.marginTop := 0.5.em,
        ^.width := 40.ex,
        ^.height := 20.em,
        ^.border := "1px solid",
        state,
      )
    )
  }


}
object CounterAndLog{
  def apply() = ScalaComponent.builder[Unit]
    .initialState("Ready.")
    .renderBackend[CounterAndLog]
    .build
}

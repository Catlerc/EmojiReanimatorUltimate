package ru.catlerc

import japgolly.scalajs.react.ScalaComponent
import org.scalajs.dom.document
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom

object App {
  def main(args: Array[String]): Unit = {

    val NoArgs =
      ScalaComponent.builder[Unit]
        .renderStatic(<.div("Hello!"))
        .build

    // Usage:
    CounterAndLog()().renderIntoDOM(dom.document.getElementById("root"))
  }
}
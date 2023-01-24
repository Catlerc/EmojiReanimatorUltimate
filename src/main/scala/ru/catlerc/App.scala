package ru.catlerc

import japgolly.scalajs.react.ScalaComponent
import org.scalajs.dom.{Element, document}
import japgolly.scalajs.react.*
import japgolly.scalajs.react.component.Scala
import japgolly.scalajs.react.vdom.html_<^.*
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.Object.getOwnPropertyNames
import scala.scalajs.js.annotation.{JSExportTopLevel, JSGlobal, JSImport}


//@js.native
//@JSImport("./editor", JSImport.Default)
//object init extends js.Object
//{
//  def apply(e: Element): Element = js.native
//}

//object App {
//  def main(args: Array[String]): Unit = {
//
//    val NoArgs =
//      ScalaComponent.builder[Unit]
//        .renderStatic(<.div("Hello!"))
//        .build
//
//    // Usage:
////    CounterAndLog()().renderIntoDOM()
//      init(dom.document.getElementById("root"))
//  }
//}


object Sockets {
  @JSExportTopLevel("numSocket")
  val numSocket = new Rete.Socket("Number value");
}


@JSImport("./NumControl")
@js.native
class NumControl(editor: Rete.NodeEditor, key: String, node: Rete.Node) extends Rete.Control[Int](key)


@JSGlobal
@js.native
class OutputDTO(var num: Int) extends js.Object

@JSExportTopLevel("KekNode")
class KekNode extends Rete.Component[js.Object, OutputDTO]("Kek!") {



  def builder(node: Rete.Node): Rete.Node = {
    val ctrl = new NumControl(this.editor, "num", node);
    node.addOutput(new Rete.Output("num", "Number", Sockets.numSocket)).addControl(ctrl)

  }

  def worker(node: Rete.Node, inputs: js.Object, outputs: OutputDTO): Unit = {
    println("kekoin!" + node.data.num)

    outputs.num = node.data.num.asInstanceOf[Int]
  }

}
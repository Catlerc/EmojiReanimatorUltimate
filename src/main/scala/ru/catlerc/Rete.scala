package ru.catlerc

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("rete", JSImport.Namespace)
object Rete extends js.Object {

  @js.native
  abstract class Node(val name: String) extends js.Object {
    val busy: Boolean
    val id: Int

    val data: js.Dynamic

    def addControl(control: Control[_]): Node

    var controls: List[Control[_]]

    def addOutput(output: Output): Node

    def addInput(input: Input): Node
  }

  @js.native
  abstract class Component[In <: js.Object, Out <: js.Object](val name: String) extends js.Object {


    val editor: NodeEditor = js.native

    def builder(node: Node): Node

    def worker(node: Node, inputs: In, outputs: Out): Unit
  }

  @js.native
  class Socket(val name: String) extends js.Object

  @js.native
  abstract class NodeEditor(val name: String) extends js.Object {
    val nodes: Array[Node]
  }

  @js.native
  class Control[Data](val key: String) extends js.Object

  @js.native
  class Input(val key: String, val title: String, val socket: Socket) extends js.Object

  @js.native
  class Output(val key: String, val title: String, val socket: Socket) extends js.Object


}

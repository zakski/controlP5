/**
 * ControlP5 Button
 * this example shows how to create buttons with controlP5.
 *
 * find a list of public methods available for the Button Controller
 * at the bottom of this sketch's source code
 *
 * by Andreas Schlegel, 2012
 * www.sojamo.de/libraries/controlp5
 *
 */
package com.szadowsz.controlP5

import processing.core.PApplet

object CButtonExample {


  // Run this project as Java application and this
  // method will launch the sketch
  def main(args: Array[String]): Unit = {
    val a = Array("")
    PApplet.runSketch(a, new CButtonExample())
  }

}

/**
 * @author Zakski : 06/09/2015.
 */
class CButtonExample extends PApplet {

  var cp5: ControlP5 = null

  var colour = color(255)

  var c1 = 0
  var c2 = 0

  var n = 0.0f


  override def settings(): Unit = {
    size(400, 600)
  }

  override def setup(): Unit = {
    surface.setResizable(false)
    cp5 = new ControlP5(this)
    val layer = cp5.getWindowContext("CButtonExample")

    // create a new button with name 'buttonA'
    val colA = layer.addButton("colour A", 100, 100, 200, 19)
    colA.plugTo((b: Boolean) => {
      println("A button event from colour A")
      n = 0.0f
      c1 = c2
      c2 = color(0, 160, 100)
    })

    val colB = layer.addButton("colour B", 100, 120, 200, 19)
    colB.plugTo((b: Boolean) => {
      println("A button event from colour B")
      n = 0.0f
      c1 = c2
      c2 = color(150, 0, 0)
    })

    val colC = layer.addButton("colour C", 100, 140, 200, 19)
    colC.plugTo((b: Boolean) => {
      println("A button event from colour C")
      n = 0.0f
      c1 = c2
      c2 = color(255, 255, 0)
    })

    val play = layer.addButton("play",140,280,200,19)
    play.plugTo((b: Boolean) => {
      println("A button event from Play")
      n = 0.0f
      c1 = c2
      c2 = color(0,0,0)
    })

    val playAg = layer.addButton("Play Again",210,300,200,19)
    playAg.plugTo((b: Boolean) => n = 0.0f)
  }

  override def draw(): Unit = {
    background(colour)
    colour = lerpColor(c1, c2, n)
    n += (1 - n) * 0.02f
  }

  override def keyPressed(): Unit = {
  }
}

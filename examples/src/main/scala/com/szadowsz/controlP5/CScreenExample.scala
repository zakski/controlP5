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

object CScreenExample {

  val cp5 = new ControlP5()

  var index = 1

  var colour = 0


  // Run this project as Java application and this
  // method will launch the sketch
  def main(args: Array[String]): Unit = {
    val a = Array("")
    PApplet.runSketch(a, new CScreenExample())
    PApplet.runSketch(a, new CScreenExample())
  }

}

/**
 * @author Zakski : 06/09/2015.
 */
class CScreenExample extends PApplet {

  val cp5 = CScreenExample.cp5
  val index = {var x = CScreenExample.index; CScreenExample.index += 1; x}

  override def settings(): Unit = {
    size(400, 600)
  }

  override def setup(): Unit = {
    surface.setResizable(false)
    cp5.addPApplet(this, "Example " + index)
    val layer = cp5.getWindowContext("Example " + index)

    // create a new button with name 'buttonA'
    val colA = layer.addButton("colour " + index, 100, 100, 200, 19)
    colA.plugTo((b: Boolean) => {
      println("A button event from colour "  + index)
      CScreenExample.colour = color(0, 160, 100)
    })


  }

  override def draw(): Unit = {
    background(CScreenExample.colour)
   }

  override def keyPressed(): Unit = {
  }
}

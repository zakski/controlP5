package com.szadowsz.controlP5

import com.szadowsz.controlP5.drawable.widget.controller.text.CTextField
import processing.core.PApplet

object CTextFieldExample {

  // Run this project as Java application and this
  // method will launch the sketch
  def main(args: Array[String]): Unit = {
    val a = Array("")
    PApplet.runSketch(a, new CTextFieldExample())
  }

}

/**
 * @author Zakski : 14/10/2015.
 */
class CTextFieldExample extends PApplet {

  var cp5: ControlP5 = null


  override def settings(): Unit = {
    size(700, 400)
  }

  override def setup(): Unit = {
    surface.setResizable(false)
    cp5 = new ControlP5(this)
    val layer = cp5.getWindowContext("CTextFieldExample")

    val label = layer.addTextField("label", width/2, 50, 50, 20)
    label.setView(CTextField.getLabelView)
    label.setValue("Example Title Label")

    val input = layer.addTextField("input",300,100,200,20)
    input.setAutoClear(true)

    val value = layer.addTextField("textValue",300,170,200,20)
    input.plugTo("setValue",value)

  }


  override def draw(): Unit = {
    background(0)
  }
}

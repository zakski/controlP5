package com.szadowsz.controlP5.drawable.widget.group

import com.szadowsz.controlP5.drawable.colour.CColour
import com.szadowsz.controlP5.drawable.layer.CLayer
import com.szadowsz.controlP5.drawable.widget.CBase
import com.szadowsz.processing.SVector
import processing.core.PGraphics

/**
 * @author Zakski : 03/10/2015.
 */
abstract class CGroup[T <: CGroup[T,V], V <: CBase[V]] (name : String, layer: CLayer, parent: CGroup[_,_], v : SVector, width: Int, height: Int)
    extends CBase[T](name,layer,parent,v, width, height){

  protected var _controllers : List[CBase[V]]

  /**
   * Method to set the object's colour.
   *
   * @param colour the object's new colour.
   * @return the updated object.
   */
  override def setColour(colour: CColour): T = {
    _controllers.foreach(_.setColour(colour))
    colour.copyTo(_colour)
    this.asInstanceOf[T]
  }

  /**
   * Method to set the object's foreground colour.
   *
   * @param foreground - the new foreground colour.
   * @return the updated object.
   */
  override def setColourForeground(foreground: Int): T = {
    _colour.setForeground(foreground)
    _controllers.foreach(_.setColourForeground(foreground))
    this.asInstanceOf[T]
  }

  /**
   * Method to set the object's background colour.
   *
   * @param background - the new background colour.
   * @return the updated object.
   */
  override def setColourBackground(background: Int): T = {
    _colour.setBackground(background)
    _controllers.foreach(_.setColourBackground(background))
    this.asInstanceOf[T]
  }

  /**
   * Method to set the object's caption label colour.
   *
   * @param caption - the new caption label colour.
   * @return the updated object.
   */
  override def setColourCaption(caption: Int): T = {
    _colour.setCaption(caption)
    _controllers.foreach(_.setColourCaption(caption))
    this.asInstanceOf[T]
  }

  /**
   * Method to set the object's value label colour.
   *
   * @param value - the new value label colour.
   * @return the updated object.
   */
  override def setColourValue(value: Int): T = {
    _colour.setValue(value)
    _controllers.foreach(_.setColourValue(value))
    this.asInstanceOf[T]
  }

  /**
   * Method to set the object's Active Colour
   *
   * @param active - the active colour
   * @return the object
   */
  override def setColourActive(active: Int): T = {
    _colour.setActive(active)
    _controllers.foreach(_.setColourActive(active))
    this.asInstanceOf[T]
  }

  /**
   * Method to draw the object in the given PApplet.
   *
   * @param graphics Processing context.
   */
  override def draw(graphics: PGraphics): Unit = {}
}

package com.szadowsz.controlP5.drawable.widget.group

import com.szadowsz.controlP5.drawable.layer.CLayer
import com.szadowsz.controlP5.drawable.widget.CBase
import com.szadowsz.processing.SVector
import processing.core.PGraphics

/**
 * @author Zakski : 03/10/2015.
 */
abstract class CGroup[T <: CGroup[T,V], V <: CBase[V]] (name : String, layer: CLayer, parent: CGroup[_,_], v : SVector, width: Int, height: Int)
    extends CBase[T](name,layer,parent,v, width, height){

  /**
   * Method to draw the object in the given PApplet.
   *
   * @param graphics Processing context.
   */
  override def draw(graphics: PGraphics): Unit = {}
}

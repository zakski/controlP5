/**
 * controlP5 is a processing gui library.
 *
 * 2006-2012 by Andreas Schlegel
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version. This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * @author Andreas Schlegel (http://www.sojamo.de)
 *
 */
package com.szadowsz.controlP5.drawable.widget


import com.szadowsz.controlP5.ControlP5
import com.szadowsz.controlP5.drawable.colour.CColourable
import com.szadowsz.controlP5.drawable.label.CLabel
import com.szadowsz.controlP5.drawable.layer.CLayer
import com.szadowsz.controlP5.drawable.widget.group.CGroup
import com.szadowsz.controlP5.drawable.{CDrawable, CIdentification}
import com.szadowsz.processing.SVector

/**
 *
 * The ControllerInterface is inherited by all ControllerGroup and Controller classes.
 *
 */
abstract class CBase[+T](name : String, layer: CLayer, group: CGroup[_,_], v : SVector, width: Int, height: Int)
  extends CColourable[T] with CDrawable with CIdentification {

  protected val _layer : CLayer = layer

  protected val _parent : CGroup[_,_] = group

  protected val _name : String = name

  protected var _position : SVector = SVector(v)

  protected val _caption : CLabel = new CLabel(_layer,name,0,0,_colour.getCaption)

  protected var _width = width

  protected var _height = height

  /**
   * Method to get the identifying name of the object.
   *
   * @return the name.
   */
  def getName: String = _name

  /**
   * Method to get the relative position of the object
   *
   * @return
   */
  def getPosition: SVector = _position

  def getAbsolutePosition: SVector = if(_parent != null)_position + _parent.getAbsolutePosition else _position

  def getParent: CGroup[_,_] = _parent

  def getCP5 : ControlP5 = _layer.getCP5

  def getHeight = _height

  def getWidth = _width

  def isCaptionVisible : Boolean = _caption.isVisible

  def hasParent : Boolean = _parent != null

}
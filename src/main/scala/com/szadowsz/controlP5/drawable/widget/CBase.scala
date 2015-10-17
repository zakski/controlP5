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
import com.szadowsz.controlP5.drawable.colour.{CColour, CColourable}
import com.szadowsz.controlP5.drawable.label.CLabel
import com.szadowsz.controlP5.drawable.layer.CLayer
import com.szadowsz.controlP5.drawable.widget.group.CGroup
import com.szadowsz.controlP5.drawable.{CDrawable, CIdentification}
import com.szadowsz.processing.SVector

/**
 * This class is inherited by all CGroup and CController classes. It provides a basic level of functionality expected
 * by all controller and group objects. As part of this the class inherits the Identification, Colourable and Drawable
 * Traits.
 *
 * @param name The Unique String that Identifies the Widget.
 * @param layer The UI layer that the Widget is displayed upon.
 * @param group The Parent Group Widget that the Widget belongs to.
 * @param v The relative position of the Widget.
 * @param width The Width of the Widget.
 * @param height The Height of the Widget.
 * @tparam Self a trait/class that extends/implements this class.
 */
abstract class CBase[Self <: CBase[Self]](name: String, layer: CLayer, group: CGroup[_, _], v: SVector, width: Int, height: Int)
  extends CColourable[Self] with CDrawable with CIdentification {
  self: Self =>

  /**
   * The UI layer that the Widget is displayed upon.
   */
  protected val _layer: CLayer = layer

  /**
   * The Parent Group Widget that the Widget is stored in.
   */
  protected val _parent: CGroup[_, _] = group

  /**
   * The Unique String that Identifies the Widget, registered in the specified UI Layer.
   */
  protected val _name: String = name

  /**
   * The relative position of the Widget, its offset from its parent / corner of the screen.
   */
  protected var _position: SVector = SVector(v)

  /**
   * The Widget's colour scheme.
   */
  protected override val _colour: CColour = new CColour(if (hasParent) _parent.getColour else _layer.getColour)

  /**
   * The Caption label; the label to display the title of the Widget.
   */
  protected val _caption: CLabel = new CLabel(_layer, name, 0, 0, _colour.getCaption)

  /**
   * The Width of the Widget, if it is Fixed.
   */
  protected var _width = width

  /**
   * The Height of the Widget, if it is Fixed.
   */
  protected var _height = height

  /**
   * The Visibility of the Widget.
   */
  protected var _isVisible = true

  /**
   * Method to get the root ControlP5 Instance, if it is required.
   *
   * @return cp5 instance that controls this widget.
   */
  protected def getCP5: ControlP5 = _layer.getCP5

  /**
   * Method to get the identifying name of the object.
   *
   * @return the name.
   */
  def getName: String = _name

  /**
   * Method to get the Parent of This Widget.
   *
   * @return
   */
  def getParent: CGroup[_, _] = _parent

  /**
   * Method to get the relative position of the object.
   *
   * @return the relative x/y coordinates as an SVector Object.
   */
  def getPosition: SVector = _position

  /**
   * Method to get the absolute position of the object.
   *
   * @return the actual x/y coordinates as an SVector Object.
   */
  def getAbsolutePosition: SVector = if (_parent != null) _position + _parent.getAbsolutePosition else _position

  /**
   * Method to get the height of the object.
   *
   * @return height as an int.
   */
  def getHeight: Int = _height

  /**
   * Method to get the width of the object.
   *
   * @return width as an int.
   */
  def getWidth: Int = _width

  /**
   * Method to get the Title text of the object.
   *
   * @return the Caption of the object as a String.
   */
  def getCaptionText: String = _caption.getText

  /**
   *
   * @return
   */
  def isVisible: Boolean = _isVisible

  /**
   *
   * @return
   */
  def isCaptionVisible: Boolean = _isVisible && _caption.isVisible

  /**
   *
   * @return
   */
  def hasParent: Boolean = _parent != null

  /**
   *
   * @param text
   */
  def setCaptionText(text: String): Unit = {
    _caption.setText(text)
  }

  /**
   *
   * @param width
   * @param height
   */
  def setSize(width: Int, height: Int): Unit = {
    _width = width
    _height = height
  }
}
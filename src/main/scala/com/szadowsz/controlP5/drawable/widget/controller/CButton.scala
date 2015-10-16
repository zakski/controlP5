/**
 * controlP5 is a processing gui library.
 *
 * 2006-2014 by Andreas Schlegel
 *
 * This library is free software; you can redistribute it
 * and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at
 * your option) any later version. This library is
 * distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA
 *
 * @author Andreas Schlegel (http://www.sojamo.de)
 *
 **/
package com.szadowsz.controlP5.drawable.widget.controller

import com.szadowsz.controlP5.behaviour.synch.{CPlug, CPluggable}
import com.szadowsz.controlP5.drawable.layer.CLayer
import com.szadowsz.controlP5.drawable.widget.group.CGroup
import com.szadowsz.controlP5.drawable.widget.view.CView
import com.szadowsz.controlP5.input.mouse.controller.CActionable
import com.szadowsz.processing.SVector
import processing.core.PGraphics


object CButton {
  def getDefaultView: CView[CButton] = new ButtonView()

  private class ButtonView extends CView[CButton] {

    override def display(graphics: PGraphics, button: CButton) {
      graphics.noStroke()
      if (button._internal && button.isSwitch) {
        graphics.fill(button._colour.getActive)
      } else {
        if (button._isMouseInside) {
          if (button._isPressed) {
            graphics.fill(button._colour.getActive)
          } else {
            graphics.fill(button._colour.getForeground)
          }
        } else {
          graphics.fill(button._colour.getBackground)
        }
      }
      graphics.rect(0, 0, button._width, button._height)
      if (button.isCaptionVisible) {
        button._caption.draw(graphics, 0, 0, button)
      }
    }
  }

}


/**
 * <p>
 * A button triggers an event after it has been release.
 * Events can be linked to functions and fields inside your
 * program/sketch. for a full documentation of this
 * controller see the {@link Controller} class.
 *
 * @example controllers/ControlP5button
 */
class CButton(layer: CLayer, parent: CGroup[_, _], name: String, v: SVector, width: Int, height: Int)
  extends CController[CButton](name, layer, parent, v, width, height) with CActionable with CPluggable[Boolean, CButton] {

  /**
   * How the CButton should be displayed.
   */
  protected var _view: CView[CButton] = CButton.getDefaultView

  protected var _internal: Boolean = false

  protected var _isSwitch = false

  override protected val _plugClass: Class[Boolean] = classOf[Boolean]

  override protected var _plugs: List[CPlug[Boolean]] = List()

  /**
   * The action that the Actionable object will perform.
   */
  override protected def activation(): Unit = {
    _internal = !_internal
    val state = if (_isSwitch) _internal else _isPressed
    _plugs.foreach(_.invokeTarget(state))
  }

  def setSwitch(flag: Boolean): CButton = {
    _isSwitch = flag
    this
  }


  def isSwitch: Boolean = _isSwitch

  /**
   * Method to check if the controller has a specified point (usually the mouse pointer) within its bounds.
   *
   * @param position the point to check against
   * @return true if its inside, false otherwise
   */
  override def isInside(position: SVector): Boolean = {
    val start = getAbsolutePosition
    start.x < position.x && start.x + _width > position.x && start.y < position.y && start.y + _height > position.y
  }
}
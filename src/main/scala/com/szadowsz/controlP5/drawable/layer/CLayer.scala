package com.szadowsz.controlP5.drawable.layer

import com.szadowsz.controlP5.ControlP5
import com.szadowsz.controlP5.drawable.label.CFont
import com.szadowsz.controlP5.drawable.widget.CBase
import com.szadowsz.controlP5.drawable.widget.controller.CButton
import com.szadowsz.controlP5.drawable.widget.group.CGroup
import com.szadowsz.controlP5.drawable.{CDrawable, CWindow}
import com.szadowsz.controlP5.input.keys.CKeyListener
import com.szadowsz.controlP5.input.mouse.CMouseInteractable
import com.szadowsz.processing.SVector
import processing.core.{PApplet, PGraphics}
import processing.event.KeyEvent

import java.security.InvalidParameterException

/**
 * @author Zakski : 01/10/2015.
 */
abstract class CLayer(window: CWindow) extends CDrawable {

  protected var _registry: Map[String, CBase[_]] = Map()

  protected var _controllers: List[CBase[_]] = List()

  protected var _window: CWindow = window

  def getPApplet: PApplet = _window.getPApplet

  def getCP5: ControlP5 = _window.getCP5

  def getFont: CFont =  _window.getFont

  def setWindow(window : CWindow): Unit = {
    _window = window
  }

  def addButton(name: String, x: Int, y: Int, width: Int, height: Int): CButton = {
    val button: CButton = new CButton(this, null, name, SVector(x, y), width, height)
    register(button)
    button
  }

  def addButton(name: String, parent: CGroup[_, _], x: Int, y: Int, width: Int, height: Int): CButton = {
    val button: CButton = new CButton(this, parent, name, SVector(x, y), width, height)
    register(button)
    button
  }

  /**
   * Method registers a Controller with the layer, a Controller should/must be registered with a unique name. If
   * not, accessing Controllers by name is not guaranteed. the rule here is last come last serve, existing
   * Controllers with the same name will be overridden.
   *
   * @param controller ControllerInterface
   */
  protected def register(controller: CBase[_]): Unit = {
    if (_registry.contains(controller.getName)) {
      throw new InvalidParameterException("Controllers must have a unique name")
    }
    _registry = _registry + (controller.getName -> controller)

    // check if controller gets added to the top level
    if (!controller.hasParent) {
      _controllers = controller +: _controllers
    }
  }

  def pre(): Unit = {

  }

  /**
   * Method to draw the object in the given PApplet
   *
   * @param graphics Processing applet context
   */
  def draw(graphics: PGraphics): Unit = {
    _controllers.foreach(_.draw(graphics))
  }

  def dispose(): Unit = {
    // TODO
  }

  def keyEvent(event: KeyEvent): Unit = {
    _controllers.foreach(c =>
      if (c.isInstanceOf[CKeyListener])
        c.asInstanceOf[CKeyListener].keyEvent(event)
    )
  }

  def mouseMovedEvent(curr: SVector, prev: SVector): Unit = {
    _controllers.foreach(c =>
      if (c.isInstanceOf[CMouseInteractable[_]])
        c.asInstanceOf[CMouseInteractable[_]].mouseMovedEvent(curr, prev)
    )
  }


  def mouseWheelEvent(movement: Int): Unit = {
    _controllers.foreach(c =>
      if (c.isInstanceOf[CMouseInteractable[_]])
        c.asInstanceOf[CMouseInteractable[_]].mouseWheelRolled(movement)
    )
  }

  def mousePressedEvent(): Unit = {
     _controllers.foreach(c =>
      if (c.isInstanceOf[CMouseInteractable[_]])
        c.asInstanceOf[CMouseInteractable[_]].mousePressed()
    )
  }

  def mouseReleasedEvent(): Unit = {
    _controllers.foreach(c =>
      if (c.isInstanceOf[CMouseInteractable[_]])
        c.asInstanceOf[CMouseInteractable[_]].mouseReleased()
    )
  }
}

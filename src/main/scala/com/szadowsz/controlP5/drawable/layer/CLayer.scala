package com.szadowsz.controlP5.drawable.layer

import com.szadowsz.controlP5.ControlP5
import com.szadowsz.controlP5.drawable.colour.{CColour, CColourable}
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
abstract class CLayer(window: CWindow) extends CDrawable with CColourable[CLayer] {

  protected var _registry: Map[String, CBase[_]] = Map()

  protected var _controllers: List[CBase[_]] = List()

  protected var _window: CWindow = window

  def getPApplet: PApplet = _window.getPApplet

  def getCP5: ControlP5 = _window.getCP5

  def getFont: CFont =  _window.getFont

  def setWindow(window : CWindow): Unit = {
    _window = window
  }

  /**
   * Method to set the object's colour.
   *
   * @param colour the object's new colour.
   * @return the updated object.
   */
  override def setColour(colour: CColour): CLayer = {
    _controllers.foreach(_.setColour(colour))
    colour.copyTo(_colour)
    this
  }

  /**
   * Method to set the object's foreground colour.
   *
   * @param foreground - the new foreground colour.
   * @return the updated object.
   */
  override def setColourForeground(foreground: Int): CLayer = {
    _colour.setForeground(foreground)
    _controllers.foreach(_.setColourForeground(foreground))
    this
  }

  /**
   * Method to set the object's background colour.
   *
   * @param background - the new background colour.
   * @return the updated object.
   */
  override def setColourBackground(background: Int): CLayer = {
    _colour.setBackground(background)
    _controllers.foreach(_.setColourBackground(background))
    this
  }

  /**
   * Method to set the object's caption label colour.
   *
   * @param caption - the new caption label colour.
   * @return the updated object.
   */
  override def setColourCaption(caption: Int): CLayer = {
    _colour.setCaption(caption)
    _controllers.foreach(_.setColourCaption(caption))
    this
  }

  /**
   * Method to set the object's value label colour.
   *
   * @param value - the new value label colour.
   * @return the updated object.
   */
  override def setColourValue(value: Int): CLayer = {
    _colour.setValue(value)
    _controllers.foreach(_.setColourValue(value))
    this
  }

  /**
   * Method to set the object's Active Colour
   *
   * @param active - the active colour
   * @return the object
   */
  override def setColourActive(active: Int): CLayer = {
    _colour.setActive(active)
    _controllers.foreach(_.setColourActive(active))
    this
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

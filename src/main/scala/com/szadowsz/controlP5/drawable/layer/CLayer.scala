package com.szadowsz.controlP5.drawable.layer

import com.szadowsz.controlP5.ControlP5
import com.szadowsz.controlP5.drawable.colour.{CColour, CColourable}
import com.szadowsz.controlP5.drawable.label.CFont
import com.szadowsz.controlP5.drawable.widget.CBase
import com.szadowsz.controlP5.drawable.widget.controller.CButton
import com.szadowsz.controlP5.drawable.widget.controller.text.CTextField
import com.szadowsz.controlP5.drawable.widget.group.CGroup
import com.szadowsz.controlP5.drawable.{CDrawable, CWindow}
import com.szadowsz.controlP5.input.keys.CKeyListener
import com.szadowsz.controlP5.input.mouse.CMouseListener
import com.szadowsz.controlP5.input.mouse.controller.CMouseInteractable
import com.szadowsz.processing.SVector
import processing.core.{PApplet, PGraphics}
import processing.event.KeyEvent

import java.security.InvalidParameterException

/**
 * Base Class for UI Window layers inherited by both CScreen and CPopup. Provides all the functionality to add and
 * remove widgets from the layer, as well as the ability to make changes to all widgets stored in this layer. Offers the
 * option of initialising immediately with a window, or initialising once a window has been assigned.
 *
 * @author Zakski : 01/10/2015.
 */
abstract class CLayer() extends CDrawable with CColourable[CLayer] with CMouseListener with CKeyListener {

  protected var _registry: Map[String, CBase[_]] = Map()

  protected var _controllers: List[CBase[_]] = List()

  protected var _window: CWindow = null

  final def getPApplet: PApplet = _window.getPApplet

  final def getCP5: ControlP5 = _window.getCP5

  def getFont: CFont = _window.getFont

  protected def init() = {}


  def this(window : CWindow){
    this()
    setWindow(window)
  }

  final def setWindow(window: CWindow): Unit = {
    _window = window
    _registry = Map()
    _controllers = List()
    init()
  }

  /**
   * Method to set the object's colour.
   *
   * @param colour the object's new colour.
   * @return the updated object.
   */
  final override def setColour(colour: CColour): CLayer = {
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
  final override def setColourForeground(foreground: Int): CLayer = {
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
  final override def setColourBackground(background: Int): CLayer = {
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
  final override def setColourValue(value: Int): CLayer = {
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
  final override def setColourActive(active: Int): CLayer = {
    _colour.setActive(active)
    _controllers.foreach(_.setColourActive(active))
    this
  }


  def addButton(name: String, x: Int, y: Int, width: Int, height: Int): CButton = {
    addButton(name,null, x, y, width, height)
  }

  def addButton(name: String, parent: CGroup[_, _], x: Int, y: Int, width: Int, height: Int): CButton = {
    val button: CButton = new CButton(this, parent, name, SVector(x, y), width, height)
    register(button)
    button
  }

  def addTextField(name: String, x: Int, y: Int, width: Int, height: Int): CTextField = {
    addTextField(name,null, x, y, width, height)
  }

  def addTextField(name: String, parent: CGroup[_, _], x: Int, y: Int, width: Int, height: Int): CTextField = {
    val text: CTextField = new CTextField(name, this, parent, SVector(x, y), width, height)
    register(text)
    text
  }

  /**
   * Method registers a Controller with the layer, a Controller should/must be registered with a unique name. If
   * not, accessing Controllers by name is not guaranteed. the rule here is last come last serve, existing
   * Controllers with the same name will be overridden.
   *
   * @param controller ControllerInterface
   */
  protected def register[V <: CBase[V]](controller: V): Unit = {
    if (_registry.contains(controller.getName)) {
      throw new InvalidParameterException("Controllers must have a unique name")
    }
    _registry = _registry + (controller.getName -> controller)

    // check if controller gets added to the top level
    if (!controller.hasParent) {
      _controllers = controller +: _controllers
    }
  }

  def go(screen: CScreen): Unit = {
    _window.go(screen)
  }

  def push(layer: CLayer): Unit = {
    _window.push(layer)
  }

  def pop(): Unit = {
    _window.pop()
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

  /**
   * Method to handle a Key Event in a responsible manner.
   *
   * @param event the Processing Key Event that has occurred.
   */
  final override def keyEvent(event: KeyEvent): Unit = {
    _controllers.foreach(c =>
      if (c.isInstanceOf[CKeyListener])
        c.asInstanceOf[CKeyListener].keyEvent(event)
    )
  }

  /**
   * Method to handle object's reaction to the mouse being moved.
   *
   * @param curr the current location of the mouse.
   * @param prev the previous location of the mouse.
   */
  final override def mouseMovedEvent(curr: SVector, prev: SVector): Unit = {
    _controllers.foreach(c =>
      if (c.isInstanceOf[CMouseInteractable])
        c.asInstanceOf[CMouseInteractable].mouseMovedEvent(curr, prev)
    )
  }

  /**
   * Method to process mouse wheel movement while the object is in focus.
   *
   * @note movement will be negative if the mouse wheel was rotated up or away from the user, and positive in the other
   *       direction. On Mac OS X, this will be reversed when "natural" scrolling is enabled in System Preferences.
   *
   * @param movement the change made by the mouse wheel.
   */
  final override def mouseWheelEvent(movement: Int): Unit = {
    _controllers.foreach(c =>
      if (c.isInstanceOf[CMouseInteractable])
        c.asInstanceOf[CMouseInteractable].mouseWheelEvent(movement)
    )
  }

  /**
   * Method to handle object's reaction to the mouse being pressed.
   */
  final override def mousePressedEvent(): Unit = {
    _controllers.foreach(c =>
      if (c.isInstanceOf[CMouseInteractable])
        c.asInstanceOf[CMouseInteractable].mousePressedEvent()
    )
  }

  /**
   * Method to handle object's reaction to the mouse being released.
   */
  final override def mouseReleasedEvent(): Unit = {
    _controllers.foreach(c =>
      if (c.isInstanceOf[CMouseInteractable])
        c.asInstanceOf[CMouseInteractable].mouseReleasedEvent()
    )
  }
}

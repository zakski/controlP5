/**
 * controlP5 is a processing gui library.
 * <p>
 * 2006-2012 by Andreas Schlegel
 * <p>
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version. This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * @author Andreas Schlegel (http://www.sojamo.de)
 */
package com.szadowsz.controlP5.drawable

import com.szadowsz.controlP5.ControlP5
import com.szadowsz.controlP5.drawable.label.CFont
import com.szadowsz.controlP5.drawable.layer.{CLayer, CScreen}
import com.szadowsz.controlP5.drawable.widget.controller.CButton
import com.szadowsz.controlP5.input.keys.{CKeyListener, KeyWrangler}
import com.szadowsz.controlP5.input.mouse.{CMouse, CMouseListener}
import com.szadowsz.processing.SVector
import org.slf4j.{Logger, LoggerFactory}
import processing.core.{PApplet, PGraphics}
import processing.event.KeyEvent

object CWindow {
  private val DEFAULT_KEY_HOLD_TIME: Long = 5000
}

private[controlP5] final class CWindow(cp5: ControlP5, applet: PApplet, baseLayer: CScreen) extends CIdentification with CKeyListener with CMouseListener {
  /**
   * SLF4J logger resource.
   */
  protected val _logger: Logger = LoggerFactory.getLogger(this.getClass)

  /**
   * Parent GUI manager instance.
   */
  protected val _parent: ControlP5 = cp5

  /**
   * Attached Processing instance.
   */
  protected val _applet: PApplet = initApplet(applet)

  /**
   * Attached PGraphics instance.
   */
  protected val _graphics: PGraphics = _applet.g

  /**
   * Stack for All Attached UI layers, head is most recent active layer.
   */
  protected var _layers: List[CLayer] = initLayer(baseLayer)

  /**
   * Stack for Drawable UI layers, head is the last active layer.
   */
  protected var _layerCache: List[CLayer] = List()

  /**
   * Keyboard handling class for the connected PApplet instance.
   */
  protected val _keys: KeyWrangler = initKeyHandler(CWindow.DEFAULT_KEY_HOLD_TIME)

  /**
   * Mouse handling class for the connected PApplet instance.
   */
  protected val _mouse: CMouse = initMouseHandler()

  protected var _font: CFont = new CFont(getPApplet.createFont("Ariel", 12))

  /**
   * Window Name.
   */
  protected var _name: String = "main"

  /**
   * Whether the Window is visible.
   */
  protected var _isVisible: Boolean = true

  /**
   * Whether the Window should be automatically drawn by the Processing instance.
   */
  protected var _isAutoDraw: Boolean = true


  /**
   * Method to initial the method registration required by the PApplet.
   *
   * @param applet the PApplet instance to attach to this window.
   * @return the updated PApplet.
   */
  protected def initApplet(applet: PApplet): PApplet = {
    applet.registerMethod("pre", this)
    applet.registerMethod("draw", this)
    applet.registerMethod("dispose", this)
    applet
  }

  /**
   * Method to initial the method registration required by the PApplet.
   *
   * @param base the PApplet instance to attach to this window.
   * @return the updated PApplet.
   */
  protected def initLayer(base: CScreen): List[CLayer] = {
    base.setWindow(this)
    List[CLayer](base)
  }

  /**
   * Method to activate a handler to process PApplet key events.
   *
   * @param holdTime the time to elapse before a key is considered held.
   * @return a KeyWrangler bound to the PApplet.
   */
  protected def initKeyHandler(holdTime: Long): KeyWrangler = {
    _logger.info("Initialising Key Handling for PApplet")
    val keys = new KeyWrangler(holdTime)
    _applet.registerMethod("keyEvent", keys)
    keys.attachHandler(this)
    _logger.info("Initialisation of Key Handling successful")
    keys
  }

  /**
   * Method to activate a handler to process PApplet mouse events.
   *
   * @return a MousePointer bound to the PApplet.
   */
  protected def initMouseHandler(): CMouse = {
    _logger.info("Initialising Mouse Handling for PApplet")
    val mouse = new CMouse(_applet.mouseX, _applet.mouseY)
    _applet.registerMethod("mouseEvent", mouse)
    mouse.attachHandler(this)
    _logger.info("Initialisation of Mouse Handling successful")
    mouse
  }

  /**
   * Method to get the identifying name of the CWindow.
   *
   * @return the name.
   */
  override def getName: String = _name

  /**
   * Method to get the PApplet instance connected to the CWindow.
   *
   * @return the attached to PApplet instance.
   */
  def getPApplet: PApplet = _applet

  def getFont: CFont =_font

  /**
   * Method to get the parent ControlP5 instance connected to the CWindow.
   *
   * @return the attached to ControlP5 instance.
   */
  def getCP5: ControlP5 = _parent

  /**
   * Method to check the visibility of the CWindow.
   *
   * @return true if visible, false otherwise.
   */
  def isVisible: Boolean = _isVisible

  /**
   * Method to check if the CWindow is automatically drawn.
   *
   * @return true if automatic, false otherwise.
   */
  def isAutoDraw: Boolean = _isAutoDraw

  /**
   * Method to set if the CWindow should be automatically drawn.
   *
   * @param flag true if automatic, false otherwise.
   * @return the Update CWindow.
   */
  def setAutoDraw(flag: Boolean): CWindow = {
    if (_isAutoDraw != flag) {
      if (!flag) {
        _applet.unregisterMethod("draw", this)
      } else {
        _applet.registerMethod("draw", this)
      }
      _isAutoDraw = flag
    }
    this
  }

  def addButton(name: String, x: Int, y: Int, width: Int, height: Int): CButton = {
    _layers.head.addButton(name, x, y, width, height)
  }

  override def keyEvent(event: KeyEvent): Unit = {
    if (_isVisible && _layers.nonEmpty) {
      _layers.head.keyEvent(event)
    }
  }

  override def mouseWheelEvent(movement: Int): Unit = {
    if (_isVisible && _layers.nonEmpty) {
      _layers.head.mouseWheelEvent(movement)
    }
  }

  override def mousePressedEvent(): Unit = {
    if (_isVisible && _layers.nonEmpty) {
      _layers.head.mousePressedEvent()
    }
  }

  override def mouseReleasedEvent(): Unit = {
    if (_isVisible && _layers.nonEmpty) {
      _layers.head.mouseReleasedEvent()
    }
  }

  override def mouseMovedEvent(curr: SVector, prev: SVector): Unit = {
    _layers.head.mouseMovedEvent(curr, prev)
  }

  def pre(): Unit = {
    _layerCache = _layers.take(_layers.indexWhere(_.isInstanceOf[CScreen]) + 1).reverse
    _layers.head.pre()
  }

  def draw(): Unit = {
    _layerCache.foreach(layer => layer.draw(_graphics))
  }

  def dispose(): Unit = {
    _layers match {
      case Nil =>
        _applet.unregisterMethod("keyEvent", _keys)
        _applet.unregisterMethod("mouseEvent", _mouse)
        _applet.unregisterMethod("pre", this)
        _applet.unregisterMethod("dispose", this)
        if (isAutoDraw) _applet.unregisterMethod("draw", this)
      case head :: tail =>
        head.dispose()
        _layers = tail
        dispose()
    }
  }
}
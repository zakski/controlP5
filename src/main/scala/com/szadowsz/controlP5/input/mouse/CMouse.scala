package com.szadowsz.controlP5.input.mouse

import com.szadowsz.processing.SVector
import org.slf4j.LoggerFactory
import processing.event.MouseEvent

/**
 * A mouse pointer that stores the x and y position as well as the pressed status.
 *
 * @param x
 * @param y
 */
class CMouse(x: Int, y: Int) {
  protected lazy val _logger = LoggerFactory.getLogger(this.getClass)

  protected var _current: SVector = SVector(x, y)
  protected var _previous: SVector = SVector(x, y)

  protected var _isPressed: Boolean = false
  protected var _handlers = List[CMouseListener]()

  def getX: Int = _current.x.toInt

  def getY: Int = _current.y.toInt

  def getDX: Int = getX - getPX

  def getDY: Int = getY - getPY

  def getPX: Int = _previous.x.toInt

  def getPY: Int = _previous.y.toInt

  def isPressed: Boolean = _isPressed

  protected def setPreviousX(x: Float): Unit = {
    _previous.x = x
  }

  protected def setPreviousY(y: Float): Unit = {
    _previous.y = y
  }


  def setPosition(x: Int, y: Int): CMouse = {
    setX(x)
    setY(y)
    this
  }

  def setX(x: Int): CMouse = {
    setPreviousX(_current.x)
    _current.x = x
    this
  }

  def setY(y: Int): CMouse = {
    setPreviousY(_current.y)
    _current.y = y
    this
  }

  def setPressed(flag : Boolean): Unit ={
    _isPressed = false
  }

  def mouseEvent(event: MouseEvent): Unit = {
    setPosition(event.getX, event.getY)
    if (event.getAction == MouseEvent.MOVE || event.getAction == MouseEvent.DRAG) {
      _isPressed = true
      _handlers.foreach(_.mouseMovedEvent(_current,_previous))

    }else if (event.getAction == MouseEvent.PRESS) {
      _isPressed = true
      _handlers.foreach(_.mousePressedEvent())

    } else if (event.getAction == MouseEvent.RELEASE) {
      _isPressed = false
      _handlers.foreach(_.mouseReleasedEvent())

    } else if (event.getAction == MouseEvent.WHEEL) {
      _handlers.foreach(_.mouseWheelEvent(event.getCount))
    }
  }

  def attachHandler(handle: CMouseListener): Unit = _handlers = handle :: _handlers

  def detachHandler(handle: CMouseListener): Unit = _handlers = _handlers.filterNot(_ == handle)
}
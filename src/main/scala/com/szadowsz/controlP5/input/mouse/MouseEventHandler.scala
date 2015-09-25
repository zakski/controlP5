package com.szadowsz.controlP5.input.mouse

trait MouseEventHandler {

  def mousePressedEvent(): Unit

  def mouseReleasedEvent():Unit

  def mouseWheelEvent(movement: Int)
}
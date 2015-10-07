package com.szadowsz.controlP5.input.mouse

import com.szadowsz.processing.SVector

trait CMouseListener {

  def mouseMovedEvent(curr: SVector, prev: SVector): Unit

  def mousePressedEvent(): Unit

  def mouseReleasedEvent():Unit

  def mouseWheelEvent(movement: Int)
}
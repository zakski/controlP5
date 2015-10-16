package com.szadowsz.controlP5.input.mouse

import com.szadowsz.processing.SVector

/**
 * Trait to base reactions to mouse input off.
 *
 * @author Zakski : 25/09/2015.
 */
trait CMouseListener {

  /**
   * Method to handle object's reaction to the mouse being moved.
   *
   * @param curr the current location of the mouse.
   * @param prev the previous location of the mouse.
   */
  def mouseMovedEvent(curr: SVector, prev: SVector): Unit

  /**
   * Method to handle object's reaction to the mouse being pressed.
   */
  def mousePressedEvent(): Unit

  /**
   * Method to handle object's reaction to the mouse being released.
   */
  def mouseReleasedEvent():Unit

  /**
   * Method to process mouse wheel movement while the object is in focus.
   *
   * @note movement will be negative if the mouse wheel was rotated up or away from the user, and positive in the other
   *       direction. On Mac OS X, this will be reversed when "natural" scrolling is enabled in System Preferences.
   *
   * @param movement the change made by the mouse wheel.
   */
  def mouseWheelEvent(movement: Int)
}
package com.szadowsz.controlP5.input.mouse.controller

import com.szadowsz.controlP5.input.mouse.CMouseListener
import com.szadowsz.processing.SVector


/**
 *  Adapted CMouseListener Trait for use with Controller objects.
 */
trait CMouseInteractable extends CMouseListener {

  /**
   * If the controller has the mouse inside its bounds.
   */
  protected var _isMouseInside: Boolean = false

  /**
   * If the controller has the mouse pressing down on it.
   */
  protected var _isPressed: Boolean = false

  /**
   * Method to check whether the Mouse is registered as over the interactable controller.
   *
   * @return true if the mouse is inside, false otherwise.s
   */
  def isMouseOver: Boolean = _isMouseInside

  /**
   * Method to check if the controller has a specified point (usually the mouse pointer) within its bounds.
   *
   * @param position the point to check against
   * @return true if its inside, false otherwise
   */
  def isInside(position : SVector): Boolean

  /**
   * Method to set whether the Mouse is over the interactable controller.
   *
   * @param isInside true if the mouse is inside, false otherwise.
   */
  final def setMouseOver(isInside: Boolean): Unit = {
    if (_isMouseInside != isInside) {
      _isMouseInside = isInside

      if (_isMouseInside) {
        onMouseEnter()
      } else {
        onMouseLeave()
      }
    }
  }

  /**
   * Method to handle controller's reaction to the mouse being moved.
   *
   * @param curr the current location of the mouse.
   * @param prev the previous location of the mouse.
   */
  override def mouseMovedEvent(curr: SVector, prev: SVector):Unit = {
    setMouseOver(isInside(curr))
  }


  /**
   * Method to process interactions that happen when the mouse enters the controller.
   */
  def onMouseEnter(): Unit = {
    _isMouseInside = true
  }

  /**
   * Method to process interactions that happen when the mouse leaves the controller.
   */
  def onMouseLeave(): Unit = {
    _isMouseInside = false
  }

  /**
   * Method to handle the controller's reaction to the mouse being pressed.
   */
  override def mousePressedEvent(): Unit = {
    if (_isMouseInside) {
      _isPressed = true
    }
  }

  /**
   * Method to handle the controller's reaction to the mouse being released.
   */
  override def mouseReleasedEvent(): Unit = {
    _isPressed = false
  }

  /**
   * Method to process mouse wheel moving while controller is in focus.
   *
   * @note movement will be negative if the mouse wheel was rotated up or away from the user, and positive in the other
   *       direction. On Mac OS X, this will be reversed when "natural" scrolling is enabled in System Preferences.
   *
   * @param movement the change made by the mouse wheel.
   */
  override def mouseWheelEvent(movement: Int): Unit = {
    // Most Controllers won't want to use with this one, so we leave it empty by default.
  }
}
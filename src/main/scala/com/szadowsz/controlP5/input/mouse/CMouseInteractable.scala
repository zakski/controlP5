package com.szadowsz.controlP5.input.mouse

import com.szadowsz.processing.SVector

trait CMouseInteractable[+T] {

  /**
   * If the object has the mouse inside its bounds.
   */
  protected var _isMouseInside: Boolean = false

  /**
   * If the object has the mouse pressing down on it.
   */
  protected var _isPressed: Boolean = false

  /**
   * Method to check whether the Mouse is registered as over the interactable object.
   *
   * @return true if the mouse is inside, false otherwise.s
   */
  def isMouseOver: Boolean = _isMouseInside

  def isInside(position : SVector): Boolean

  /**
   * Method to set whether the Mouse is over the interactable object.
   *
   * @param isInside true if the mouse is inside, false otherwise.
   * @return the updated interactable object.
   */
  final def setMouseOver(isInside: Boolean): T = {
    if (_isMouseInside != isInside) {
      _isMouseInside = isInside

      if (_isMouseInside) {
        // TODO Add registration code
        onMouseEnter()
      } else {
        // TODO Add deregistration code
        onMouseLeave()
      }
    }
    this.asInstanceOf[T]
  }

  def mouseMovedEvent(curr: SVector, prev: SVector):Unit = {
    setMouseOver(isInside(curr))
  }


  /**
   * Method to process interactions that happen when the mouse enters the object.
   */
  def onMouseEnter(): Unit = {
    _isMouseInside = true
  }

  /**
   * Method to process interactions that happen when the mouse leaves the object.
   */
  def onMouseLeave(): Unit = {
    _isMouseInside = false
  }

  /**
   * Method to process mouse presses while controller is in focus.
   */
  def mousePressed(): Unit = {
    if (_isMouseInside) {
      _isPressed = true
    }
  }

  /**
   * Method to process mouse being released while controller is in focus.
   */
  def mouseReleased(): Unit = {
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
  def mouseWheelRolled(movement: Int): Unit = {
    // Most Controllers won't want to use with this one, so we leave it empty by default.
  }
}
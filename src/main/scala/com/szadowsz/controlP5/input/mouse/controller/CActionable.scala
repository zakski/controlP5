package com.szadowsz.controlP5.input.mouse.controller

/**
 * Expanded from the other Mouse Input Traits to provide an outlet to activate the controller if pressed / released.
 *
 * @author Zakski : 09/09/2015.
 */
trait CActionable extends CMouseInteractable {

  /**
   * When we should call the activation function.
   */
  protected var _activateBy: CActivateByType = CActivateByType.RELEASE

  /**
   * Method to identify how the Actionable object is activated.
   *
   * @return PRESS if activated by a mouse PRESS, otherwise returns RELEASE.
   */
  def getActivateBy: CActivateByType = _activateBy

  /**
   * An Actionable object can be activated by a mouse PRESS or mouse RELEASE. Default value is RELEASE.
   *
   * @param activation use PRESS or RELEASE as the parameter.
   * @return the update Actionable object.
   */
  def setActivateBy(activation: CActivateByType): Unit = {
    _activateBy = activation
  }

  /**
   * Method to handle the controller's reaction to the mouse being pressed.
   */
  override def mousePressedEvent(): Unit = {
    if (_isMouseInside) {
      _isPressed = true
      if (_activateBy == CActivateByType.PRESS) {
        activation()
      }
    }
  }

  /**
   * Method to handle the controller's reaction to the mouse being released.
   */
  override def mouseReleasedEvent(): Unit = {
    if (_activateBy == CActivateByType.RELEASE && _isMouseInside) {
      activation()
    }
    _isPressed = false
  }


  /**
   * The action that the Actionable object will perform.
   */
  protected def activation(): Unit
}

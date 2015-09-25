package com.szadowsz.controlP5.input.mouse.action

import com.szadowsz.controlP5.input.mouse.CMouseInteractable

/**
 * @author Zakski : 09/09/2015.
 */
trait CActionable[T <: CActionable[T]] extends CMouseInteractable[T] {

  /**
   * When we should call the activation function.
   */
  protected var _activateBy: ActivateByType = ActivateByType.RELEASE

  /**
   * Method to identify how the Actionable object is activated.
   *
   * @return PRESS if activated by a mouse PRESS, otherwise returns RELEASE.
   */
  def getActivateBy: ActivateByType = _activateBy

  /**
   * An Actionable object can be activated by a mouse PRESS or mouse RELEASE. Default value is RELEASE.
   *
   * @param activation use PRESS or RELEASE as the parameter.
   * @return the update Actionable object.
   */
  def setActivateBy(activation: ActivateByType): T = {
    _activateBy = activation
    this.asInstanceOf[T]
  }

  /**
   * Method to process mouse presses while controller is in focus.
   */
  override def mousePressed(): Unit = {
    if (_isMouseInside) {
      _isPressed = true
      if (_activateBy == ActivateByType.PRESS) {
        activation()
      }
    }
  }

  /**
   * Method to process mouse being released while controller is in focus.
   */
  override def mouseReleased(): Unit = {
    _isPressed = false
    if (_activateBy == ActivateByType.RELEASE && _isMouseInside) {
      activation()
    }
  }


  /**
   * The action that the Actionable object will perform.
   */
  protected def activation(): Unit
}

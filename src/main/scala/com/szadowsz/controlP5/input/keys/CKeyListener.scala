package com.szadowsz.controlP5.input.keys

import processing.event.KeyEvent

/**
 * Listener Trait compatible with the CKeyboard and capable of enhancing controller that need to respond to key input.
 */
trait CKeyListener {

  /**
   * Method to handle a Key Event in a responsible manner.
   *
   * @param event the Processing Key Event that has occurred.
   */
  def keyEvent(event: KeyEvent) : Unit
}
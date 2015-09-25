package com.szadowsz.controlP5.input.keys

/**
 * Trait to represent what should happen when the chord is activated.
 * 
 * @author Zakski : 06/09/2015.
 */
trait ChordFunction {

  /**
   * Method called when a chord is activated.
   */
  def chordShortcut(): Unit
}

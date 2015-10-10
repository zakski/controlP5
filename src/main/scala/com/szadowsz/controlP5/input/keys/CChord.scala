package com.szadowsz.controlP5.input.keys

import java.awt.event.KeyEvent

/**
 * Class to represent a key combination used in the UI for some function.
 *
 * @author Zakski : 06/09/2015.
 */
class CChord(keys: Array[Int]) {

  /**
   * List of keys that make up this shortcut
   */
  private val _keys = keys.sorted

  /**
   * Method to check whether the key is in the chord.
   *
   * @param nkey the key to check
   * @return true if the key is in this chord, false otherwise
   */
  def contains(nkey: Int): Boolean = {
    _keys.contains(nkey)
  }

  /**
   * Method to get the hashcode of the keys.
   *
   * @return the hashcode.
   */
  override def hashCode: Int = {
    _keys.foldLeft((0.0, 1))((bun, key) => (bun._1 + Math.pow(key, bun._2), bun._2 + 1))._1.toInt
  }

  /**
   * Compares one chord key set to another.
   *
   * @param that the object to cmpare this one to.
   * @return true if they match, false otherwise.
   */
  override def equals(that: Any): Boolean = {
    that match {
      case other: CChord => _keys.sameElements(other._keys)
      case _ => false
    }
  }

  /**
   * Method to provide a string representation.
   *
   * @return string representation of all the keys.
   */
  override def toString: String = {
    _keys.map(code => KeyEvent.getKeyText(code) + "(" + code + ") ").mkString("[ ",","," ]")
  }
}

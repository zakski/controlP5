package com.szadowsz.controlP5.input.keys

import org.slf4j.LoggerFactory
import processing.{event => p}

import java.awt.{event => j}
import scala.collection.{mutable => m}

/**
 * General Key handling
 *
 * @author Zakski : 06/09/2015.
 */
private[controlP5] class CKeyboard(timeBeforeConsideredHeld: Long) {
  protected lazy val _logger = LoggerFactory.getLogger(this.getClass)

  /**
   * List to store keys as they are pressed and released, as well as how long they have been held for.
   */
  protected val _keys = m.HashMap[Int, Long]()

  /**
   * Time interval constant, anything pressed for less than this is not considered held.
   */
  protected var _holdTime: Long = timeBeforeConsideredHeld

  /**
   * List to store current keyboard shortcuts.
   */
  protected var _chords = Map[CChord, ChordFunction]()

  /**
   * Controller with text editing that is in focus.
   */
  protected var _inFocus: Option[CKeyListener] = None

  /**
   * Method to check for a key press
   *
   * @param code - code of the key to be checked
   * @return true if the key has been pressed, false otherwise
   */
  def isKeyPressed(code: Int): Boolean = _keys.contains(code)

  /**
   * Method to check if a key is held
   *
   * @param code - code of the key to be checked
   * @return true if the key is held, false otherwise
   */
  def isKeyHeld(code: Int): Boolean = {
    _keys.exists(key => key._1 == code && (System.currentTimeMillis() - key._2 >= _holdTime))
  }

  /**
   * Method to get the hold time of a key
   *
   * @param code - code of the key to be checked
   * @return time the key has been held for, -1 if the key is currently not held
   */
  def getHeldTime(code: Int): Long = {
    _keys.getOrElse(code, -1L)
  }

  /**
   * Method to get the pressed keys.
   *
   * @return a list of keys currently pressed.
   */
  def getPressedKeys: Iterable[Int] = _keys.filter(key => System.currentTimeMillis() - key._2 < _holdTime).keys

  /**
   * Method to get all held keys.
   *
   * @return a list of keys currently held down.
   */
  def getHeldKeys: Iterable[Int] = _keys.filter(key => System.currentTimeMillis() - key._2 >= _holdTime).keys

  /**
   * Method to get all touched keys.
   *
   * @return a list of all keys currently down.
   */
  def getDownedKeys: Iterable[Int] = _keys.keys

  def attachHandler(handle : CKeyListener): Unit = {
    _inFocus = Some(handle)
  }

  /**
   *  Method to add a new shortcut to the layout
   *
   *
   * @param chord keys of the shortcut
   * @param function what the shortcut does
   */
  def mapShortcut(chord : CChord, function: ChordFunction): Unit = {
    _chords = _chords + (chord -> function)
    _logger.info("Added a new keyboard shortcut")
  }

  /**
   *  Method to apply a new set of shortcuts
   *
   * @param shortcuts the new shortcut layout to apply
   */
  def mapShortcuts(shortcuts: Map[CChord, ChordFunction]): Unit = {
    clearKeys() // clear them so we don't inadvertently trigger a new shortcut.
    _chords = shortcuts
    _logger.info("Applied new keyboard shortcuts")
  }


  /**
   * Method to register a key press
   *
   * @param code - code of the key to be added
   * @return true if the key has been added, false otherwise
   */
  def pressKey(code: Int): Boolean = {
    if (!_keys.contains(code)) {
      _logger.info("Pressed key: {}", j.KeyEvent.getKeyText(code))
      _keys.put(code, System.currentTimeMillis)
      true
    } else {
      false
    }
  }

  /**
   * Method to release a key
   *
   * @param code - code of the key to be added
   * @return true if the key has been removed, false otherwise
   */
  def releaseKey(code: Int): Boolean = {
    if (_keys.contains(code)) {
      val time = _keys.remove(code)
      _logger.info("Released key: {}, Held for {}", j.KeyEvent.getKeyText(code),time.get)
      true
    } else {
      _logger.warn("Couldn't Release key: {}", j.KeyEvent.getKeyText(code))
      false
    }
  }

  /**
   * Clears all currently held keys.
   */
  def clearKeys(): Unit = _keys.clear()

  /**
   * Method to process KeyEvents as they happen
   *
   * @param event - the latest event to process
   */
  def keyEvent(event: p.KeyEvent): Unit = {
    if (event.getAction == p.KeyEvent.PRESS) {
      pressKey(event.getKeyCode)
      if (_inFocus.isEmpty) {
        val chordFunc = _chords.get(new CChord(getDownedKeys.toArray))
        if (chordFunc.isDefined) {
          chordFunc.get.chordShortcut()
        }
      }
    } else if (event.getAction == p.KeyEvent.RELEASE) {
      releaseKey(event.getKeyCode)
    }
    if (_inFocus.isDefined) {
      _inFocus.get.keyEvent(event)
    }
  }
}

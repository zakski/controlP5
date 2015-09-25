package com.szadowsz.controlP5.input.keys

import processing.event.KeyEvent

trait KeyEventHandler {
  def handleKeyEvent(event: KeyEvent)
}
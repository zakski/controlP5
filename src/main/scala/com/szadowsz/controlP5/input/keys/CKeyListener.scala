package com.szadowsz.controlP5.input.keys

import processing.event.KeyEvent

trait CKeyListener {
  def keyEvent(event: KeyEvent)
}
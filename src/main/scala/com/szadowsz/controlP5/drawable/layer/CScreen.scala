package com.szadowsz.controlP5.drawable.layer

import com.szadowsz.controlP5.drawable.CWindow

object CScreen {

  def apply(): CScreen = new CScreen
}

/**
 * @author Zakski : 01/10/2015.
 */
class CScreen(window: CWindow) extends CLayer(window) {

  def this() {
    this(null)
  }
}

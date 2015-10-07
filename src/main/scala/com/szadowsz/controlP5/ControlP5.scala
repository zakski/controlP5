package com.szadowsz.controlP5

import com.szadowsz.controlP5.drawable.CWindow
import com.szadowsz.controlP5.drawable.layer.CScreen
import com.szadowsz.controlP5.drawable.widget.controller.CButton
import processing.core.PApplet

/**
 * Overall control class for the GUI.
 *
 * @author Zakski : 01/10/2015.
 */
class ControlP5(applet: PApplet, baseLayer : CScreen) {

  def this(applet: PApplet){
    this(applet, CScreen())
  }

  /**
   * The window list controlled by the GUI.
   */
  protected var _windows: List[CWindow] = List(new CWindow(this,applet,baseLayer))

  /**
   * Method to check if all CWindows are automatically drawn.
   *
   * @return true if all are automatic, false otherwise.
   */
  def isAutoDraw: Boolean = _windows.forall(_.isAutoDraw)

  /**
   * Method to set if all CWindows should be automatically drawn.
   *
   * @param flag true if automatic, false otherwise.
   * @return the Updated ControlP5 instance.
   */
  def setAutoDraw(flag: Boolean): ControlP5 = {
    _windows.foreach(_.setAutoDraw(flag))
    this
  }

  def addPApplet(name : String, applet : PApplet): Boolean = ???

  def addButton(name: String, x: Int, y: Int, width: Int, height: Int): CButton = {
    _windows.head.addButton(name, x, y, width, height)
  }
  def removePApplet(applet : PApplet): Boolean = ???

  def removePApplet(name : String): Boolean = ???
}

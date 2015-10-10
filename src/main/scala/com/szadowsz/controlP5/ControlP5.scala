package com.szadowsz.controlP5

import com.szadowsz.controlP5.drawable.CWindow
import com.szadowsz.controlP5.drawable.layer.{CLayer, CScreen}
import org.slf4j.{Logger, LoggerFactory}
import processing.core.PApplet

import java.security.InvalidParameterException

/**
 * Overall control class for the GUI.
 *
 * @author Zakski : 01/10/2015.
 */
class ControlP5(applet: PApplet, name: String, baseLayer: CScreen) {
  /**
   * SLF4J logger resource.
   */
  protected val _logger: Logger = LoggerFactory.getLogger(this.getClass)

  /**
   * The windows currently managed by the UI.
   */
  protected var _windows: Map[String, CWindow] = Map(name -> new CWindow(this, applet, name, baseLayer))

  def this(applet: PApplet, baseLayer: CScreen) = {
    this(applet, applet.getClass.getSimpleName, baseLayer)
  }

  def this(applet: PApplet) {
    this(applet, CScreen())
  }

  /**
   * Method to get the current UI layer of a window.
   *
   * @param name the name of the window
   * @return the Active CLayer instance.
   */
  def getWindowContext(name: String): CLayer = _windows.get(name).get.getCurrentLayer

  /**
   * Method to check if all CWindows are automatically drawn.
   *
   * @return true if all are automatic, false otherwise.
   */
  def isAutoDraw: Boolean = _windows.values.forall(_.isAutoDraw)

  /**
   * Method to set if all CWindows should be automatically drawn.
   *
   * @param flag true if automatic, false otherwise.
   * @return the Updated ControlP5 instance.
   */
  def setAutoDraw(flag: Boolean): ControlP5 = {
    _windows.values.foreach(_.setAutoDraw(flag))
    this
  }

  /**
   * Method to register a new PApplet instance with the UI.
   *
    * @param applet the applet to register.
   */
  def addPApplet(applet: PApplet): Unit = {
    addPApplet(applet.getClass.getSimpleName, applet)
  }


  /**
   * Method to register a new PApplet instance with the UI.
   *
   * @param name the registration name of the new instance.
   * @param applet the applet to register.
   */
  def addPApplet(name: String, applet: PApplet): Unit = {
    addPApplet(name, applet, CScreen())
  }

  /**
   * Method to register a new PApplet instance with the UI.
   *
   * @param name the registration name of the new instance.
   * @param applet the applet to register.
   * @param baseLayer the first UI screen to display.
   */
  def addPApplet(name: String, applet: PApplet, baseLayer: CScreen): Unit = {
    _logger.info("Registering PApplet with name \"{}\"", name)
    if (_windows.contains(name)) {
      throw new InvalidParameterException()
    }
    _windows = _windows + (name -> new CWindow(this, applet, name, baseLayer))
    _logger.info("PApplet Registered Successfully")
  }

  def removePApplet(applet: PApplet): Unit = ???

  def removePApplet(name: String): Unit = ???
}

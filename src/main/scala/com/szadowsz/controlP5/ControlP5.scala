package com.szadowsz.controlP5

import com.szadowsz.controlP5.drawable.CWindow
import com.szadowsz.controlP5.drawable.layer.{CLayer, CScreen}
import org.slf4j.{Logger, LoggerFactory}
import processing.core.PApplet

import java.security.InvalidParameterException

/**
 * Overall control class for the GUI, responsible for managing the window components.
 *
 * @author Zakski : 01/10/2015.
 */
final class ControlP5 {

  /**
   * SLF4J logger resource.
   */
  protected val _logger: Logger = LoggerFactory.getLogger(this.getClass)

  /**
   * The registered windows currently managed by the UI.
   */
  protected var _windows: Map[String, CWindow] = Map()

  /**
   * Constructor assigns a custom CScreen for the PApplet passed as a parameter, registering it with the given name.
   *
   * @param applet the initial PApplet to register.
   * @param name the title of the screen and the registration name.
   * @param screen the custom screen to attach.
   */
  def this(applet: PApplet, name: String, screen: CScreen){
    this()
    addPApplet(applet,name,screen)
  }

  /**
   * Constructor assigns a custom CScreen for the PApplet passed as a parameter.
   *
   * @param applet the initial PApplet to register.
   * @param screen the custom screen to attach.
   */
  def this(applet: PApplet, screen: CScreen) = {
    this(applet, applet.getClass.getSimpleName, screen)
  }

  /**
   * Constructor provides a blank CScreen for the PApplet passed as a parameter.
   *
   * @param applet the initial PApplet to register.
   */
  def this(applet: PApplet) {
    this(applet, CScreen())
  }

  /**
   * Method to get the current UI layer of a window.
   *
   * @param name the name of the window.
   * @return the Active CLayer instance.
   */
  def getWindowContext(name: String): CLayer = _windows.get(name).get.getCurrentLayer

  /**
   * Method to register a new PApplet instance with the UI with a bank screen and default name.
   *
   * @param applet the PApplet to register
   */
  def addPApplet(applet: PApplet): Unit = {
    addPApplet(applet,applet.getClass.getSimpleName)
  }


  /**
   * Method to register a new PApplet instance with the UI and provide it a blank screen, given a unique window name.
   *
   * @param applet the PApplet to register.
   * @param name the title of the screen and the registration name.
   */
  def addPApplet(applet: PApplet,name: String): Unit = {
    addPApplet(applet, name, CScreen())
  }

  /**
   * Method to register a new PApplet instance with the UI manager, with a custom screen and name.
   *
   * @param applet the PApplet to register.
   * @param name the title of the screen and the registration name.
   * @param screen the custom screen to attach.
   */
  def addPApplet(applet: PApplet, name: String, screen: CScreen): Unit = {
    _logger.info("Registering PApplet with name \"{}\"", name)
    if (_windows.contains(name)) {
      throw new InvalidParameterException("PApplets must be registered with a unique name, \"" + name + "\" is taken")
    }
    _windows = _windows + (name -> new CWindow(this, applet, name, screen))
    _logger.info("PApplet Registered Successfully")
  }

  /**
   * Method to register a new PApplet instance with the UI manager, with a custom screen and name.
   *
   * @param applet the PApplet to register.
   * @param name the title of the screen and the registration name.
   * @param screenClassName the name of the custom screen class to attach.
   */
  def addPApplet(applet: PApplet, name: String, screenClassName: String): Unit = {
    _logger.info("Registering PApplet with name \"{}\"", name)
    if (_windows.contains(name)) {
      throw new InvalidParameterException("PApplets must be registered with a unique name, \"" + name + "\" is taken")
    }
    val screen = Class.forName(screenClassName).asInstanceOf[Class[CScreen]]

    _windows = _windows + (name -> new CWindow(this, applet, name, screen))
    _logger.info("PApplet Registered Successfully")
  }
}

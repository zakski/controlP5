///**
// * controlP5 is a processing gui library.
// *
// * 2006-2012 by Andreas Schlegel
// *
// * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
// * General Public License as published by the Free Software Foundation; either version 2.1 of the License, or
// * (at your option) any later version. This library is distributed in the hope that it will be useful, but
// * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
// * PURPOSE. See the GNU Lesser General Public License for more details.
// *
// * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
// * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
// *
// * @author Andreas Schlegel (http://www.sojamo.de)
// *
// */
//package com.szadowsz.controlP5
//
//
//import com.szadowsz.controlP5.input.keys.{ChordFunction, ChordKey, KeyWrangler}
//import com.szadowsz.controlP5.input.mouse.MousePointer
//import controlP5._
//import org.slf4j.{Logger, LoggerFactory}
//import processing.core.{PApplet, PFont, PGraphics}
//
//import java.lang.annotation.{Retention, RetentionPolicy}
//import java.util.TreeMap
//
//object ControlP5 extends ControlP5Constants {
//
//  private val DEFAULT_KEY_HOLD_TIME: Long = 5000
//
//  private val DEFAULT_FONT: ControlFont = new ControlFont(new BitFont(CP.decodeBase64(BitFont.standard56base64)))
//
//  @Retention(RetentionPolicy.RUNTIME)
//  trait Invisible {
//  }
//
//  @Retention(RetentionPolicy.RUNTIME)
//  trait Layout {
//  }
//}
//
//
//class ControlP5(applet: PApplet, font: ControlFont) extends ControlP5Base {
//  /**
//   * SLF4J logger resource
//   */
//  protected val _logger: Logger = LoggerFactory.getLogger("ControlP5")
//
//  /**
//   * The PApplet we are rendering on
//   */
//  protected val _parent = initPApplet(applet)
//
//  protected var _graphics: PGraphics = _parent.g
//
//  var pgx: Int = 0
//  var pgy: Int = 0
//  var pgw: Int = 0
//  var pgh: Int = 0
//  var ox: Int = 0
//  var oy: Int = 0
//
//  protected val _window = new ControlWindow(this, _parent)
//
//  protected val _myControllerMap = new TreeMap[String, ControllerInterface[_]]()
//
//  /**
//   * Keyboard handling class
//   */
//  protected val _keys: KeyWrangler = initKeyHandler(ControlP5.DEFAULT_KEY_HOLD_TIME)
//
//  /**
//   * Mouse handling class
//   */
//  protected val _mouse: MousePointer = initMouseHandler()
//
//  /**
//   * Text font for the PApplet
//   */
//  protected var _controlFont: ControlFont = font
//
//  /**
//   * Flag to control whether the PApplet automatically draws the ui.
//   */
//  protected var _isAutoDraw: Boolean = false
//
//  /**
//   * Create a new instance of controlP5.
//   *
//   * @param applet The PApplet we are rendering on
//   */
//  def this(applet: PApplet) {
//    this(applet, ControlP5.DEFAULT_FONT)
//  }
//
//  /**
//   * Create a new instance of controlP5.
//   *
//   * @param applet The PApplet we are rendering on
//   * @param font the font to render text with.
//   */
//  def this(applet: PApplet, font: PFont) {
//    this(applet, new ControlFont(font))
//  }
//
//
//  protected def initPApplet(applet : PApplet):PApplet = {
//    applet.registerMethod("pre", this)
//    applet.registerMethod("dispose", this)
//    super.init(this)
//    applet
//  }
//
//  /**
//   * Method to activate a handler to process PApplet key events.
//   *
//   * @param holdTime the time to elapse before a key is considered held
//   * @return a KeyWrangler bound to the PApplet
//   */
//  protected def initKeyHandler(holdTime: Long): KeyWrangler = {
//    _logger.info("Initialising Key Handling for PApplet")
//    val keys = new KeyWrangler(holdTime)
//    _parent.registerMethod("keyEvent", keys)
//    _logger.info("Initialisation of Key Handling successful")
//    keys
//  }
//
//  /**
//   * Method to activate a handler to process PApplet mouse events.
//   *
//   * @return a MousePointer bound to the PApplet
//   */
//  protected def initMouseHandler(): MousePointer = {
//    _logger.info("Initialising Mouse Handling for PApplet")
//    val mouse = new MousePointer(_parent.mouseX, _parent.mouseY)
//    _parent.registerMethod("mouseEvent", mouse)
//    _logger.info("Initialisation of Mouse Handling successful")
//    mouse
//  }
//
//  /**
//   * Method to check if autoDraw is enabled or disabled.
//   *
//   * @return true if automatically drawn, false otherwise
//   */
//  def isAutoDraw: Boolean = _isAutoDraw
//
//  /**
//   * Method to control whether controlP5 draws any controller on top of any drawing done in the draw() function. If set
//   * to false call controlP5.draw() any time whenever controllers should be drawn into the sketch.
//   *
//   * @param flag true if it should be automatically drawn, false otherwise
//   */
//  def setAutoDraw(flag: Boolean) {
//    if (_isAutoDraw != flag) {
//      if (!flag) {
//        _parent.unregisterMethod("draw", _window)
//      } else {
//        _parent.registerMethod("draw", _window)
//      }
//      _isAutoDraw = flag
//    }
//  }
//
//  /**
//   * Method to manually add a new keyboard shortcut
//   *
//   * @param keyShortcut the function to add
//   * @param nkeys the keys to call the function
//   */
//  def mapShortcut(keyShortcut: ChordFunction, nkeys: Int*): Unit = {
//    _keys.mapShortcut(new ChordKey(nkeys.toArray), keyShortcut)
//  }
//
//  /**
//   * Method to clear the key hold records
//   */
//  def clearKeys(): Unit = {
//    _keys.clearKeys()
//  }
//
//  /**
//   * Method to draw the GUI when autoDraw is disabled.
//   */
//  def draw(): Unit = {
//    if (!_isAutoDraw) {
//      _window.draw()
//    }
//  }
//
//  /**
//   * Method gracefully disposes and clears all controlP5 elements. When running in applet mode, opening new tabs or
//   * switching to another tab causes the applet to call dispose(). therefore dispose() is disabled when running ing
//   * applet mode. TODO implement better dispose handling for applets.
//   */
//  def dispose(): Unit = {
//    _window.clear()
//    _myControllerMap.clear()
//  }
//
//
////  private[controlP5] var isGraphics: Boolean = false
////  protected var _myControlBroadcaster: ControlBroadcaster = null
////  protected var window: ControlWindow = null
////  protected var isMoveable: Boolean = false
////  protected var isAutoInitialization: Boolean = false
////  protected var isGlobalControllersAlwaysVisible: Boolean = true
////  protected var isTabEventsActive: Boolean = false
////  protected var isUpdate: Boolean = false
////
////  /**
////   * from version 0.7.2 onwards shortcuts are disabled by
////   * default. shortcuts can be enabled using
////   * controlP5.enableShortcuts();
////   *
////   * @see #enableShortcuts()
////   */
////  protected var isShortcuts: Boolean = false
////  @deprecated var blockDraw: Boolean = false
////  protected var _myTooltip: Tooltip = null
////  protected var isAnnotation: Boolean = false
////  private[controlP5] var isAndroid: Boolean = false
////
////
////  def setPosition(theX: Int, theY: Int): ControlP5 = {
////    ox = theX
////    oy = theY
////    return this
////  }
////
////  /**
////   * By default event originating from tabs are disabled,
////   * use setTabEventsActive(true) to receive controlEvents
////   * when tabs are clicked.
////   *
////   * @param theFlag
////   */
////  def setTabEventsActive(theFlag: Boolean) {
////    isTabEventsActive = theFlag
////  }
////
////  /**
////   * autoInitialization can be very handy when it comes to
////   * initializing values, e.g. you load a set of
////   * controllers, then the values that are attached to the
////   * controllers will be reset to its saved state. to turn
////   * of auto intialization, call
////   * setAutoInitialization(false) right after initializing
////   * controlP5 and before creating any controller.
////   *
////   * @param theFlag boolean
////   */
////  def setAutoInitialization(theFlag: Boolean) {
////    isAutoInitialization = theFlag
////  }
////
////  /**
////   *
////   * @see controlP5.ControlBroadcaster
////   */
////  def getControlBroadcaster: ControlBroadcaster = {
////    return _myControlBroadcaster
////  }
////
////  /**
////   * @see controlP5.ControlListener
////   */
////  def addListener(theListeners: ControlListener*): ControlP5 = {
////    getControlBroadcaster.addListener(theListeners)
////    return this
////  }
////
////  /**
////   * @see controlP5.ControlListener
////   */
////  def removeListener(theListeners: ControlListener*): ControlP5 = {
////    getControlBroadcaster.removeListener(theListeners)
////    return this
////  }
////
////  /**
////   * @see controlP5.ControlListener
////   */
////  def removeListener(theListener: ControlListener): ControlP5 = {
////    getControlBroadcaster.removeListener(theListener)
////    return this
////  }
////
////  /**
////   * @see controlP5.ControlListener
////   */
////  def getListener(theIndex: Int): ControlListener = {
////    return getControlBroadcaster.getListener(theIndex)
////  }
////
////  /**
////   * @see controlP5.CallbackEvent
////   * @see controlP5.CallbackListener
////   */
////  def addCallback(theListeners: CallbackListener*): ControlP5 = {
////    getControlBroadcaster.addCallback(theListeners)
////    return this
////  }
////
////  /**
////   * @see controlP5.CallbackEvent
////   * @see controlP5.CallbackListener
////   */
////  def addCallback(theListener: CallbackListener): ControlP5 = {
////    getControlBroadcaster.addCallback(theListener)
////    return this
////  }
////
////  /**
////   * @see controlP5.CallbackEvent
////   * @see controlP5.CallbackListener
////   */
////  def addCallback(theListener: CallbackListener, theControllers: Controller[_]*): ControlP5 = {
////    getControlBroadcaster.addCallback(theListener, theControllers)
////    return this
////  }
////
////  /**
////   * @see controlP5.CallbackEvent
////   * @see controlP5.CallbackListener
////   */
////  def removeCallback(theListeners: CallbackListener*): ControlP5 = {
////    getControlBroadcaster.removeCallback(theListeners)
////    return this
////  }
////
////  /**
////   * @see controlP5.CallbackEvent
////   * @see controlP5.CallbackListener
////   */
////  def removeCallback(theControllers: Controller[_]*): ControlP5 = {
////    getControlBroadcaster.removeCallback(theControllers)
////    return this
////  }
////
////  /**
////   * @see controlP5.CallbackEvent
////   * @see controlP5.CallbackListener
////   */
////  def removeCallback(theController: Controller[_]): ControlP5 = {
////    getControlBroadcaster.removeCallback(theController)
////    return this
////  }
////
////  /**
////   * TODO
////   *
////   * @exclude
////   */
////  def addControlsFor(theObject: AnyRef) {
////  }
////
////  def getTab(theName: String): Tab = {
////    {
////      var i: Int = 0
////      while (i < _window.getTabs.size) {
////        {
////          if ((_window.getTabs.get(i).asInstanceOf[Tab]).getName == theName) {
////            return _window.getTabs.get(i).asInstanceOf[Tab]
////          }
////        }
////        ({
////          i += 1;
////          i - 1
////        })
////      }
////    }
////    val myTab: Tab = addTab(theName)
////    return myTab
////  }
////
////  def getTab(theWindow: ControlWindow, theName: String): Tab = {
////    {
////      var i: Int = 0
////      while (i < theWindow.getTabs.size) {
////        {
////          if ((theWindow.getTabs.get(i).asInstanceOf[Tab]).getName == theName) {
////            return theWindow.getTabs.get(i).asInstanceOf[Tab]
////          }
////        }
////        ({
////          i += 1;
////          i - 1
////        })
////      }
////    }
////    val myTab: Tab = theWindow.add(new Tab(this, theWindow, theName))
////    return myTab
////  }
////
////  /**
////   * registers a Controller with ControlP5, a Controller
////   * should/must be registered with a unique name. If not,
////   * accessing Controllers by name is not guaranteed. the
////   * rule here is last come last serve, existing
////   * Controllers with the same name will be overridden.
////   *
////   * @param theController ControllerInterface
////   * @return ControlP5
////   */
////  def register(theObject: AnyRef, theIndex: String, theController: ControllerInterface[_]): ControlP5 = {
////    var address: String = ""
////    if (theObject eq papplet) {
////      address = if ((theController.getName.startsWith("/"))) "" else "/"
////      address += theController.getName
////    }
////    else {
////      address = (if (((theIndex.length == 0) || theIndex.startsWith("/"))) "" else "/")
////      address += theIndex
////      address += (if (theController.getName.startsWith("/")) "" else "/")
////      address += theController.getName
////    }
////    theController.setAddress(address)
////    if (checkName(theController.getAddress)) {
////      remove(theController.getAddress)
////    }
////    _myControllerMap.put(theController.getAddress, theController)
////    val ps: List[ControllerProperty] = getProperties.get(theController)
////    if (ps != null) {
////      for (p <- ps) {
////        p.setAddress(theController.getAddress)
////      }
////    }
////    theController.init
////    if (theObject == null) {
////      theObject = papplet
////    }
////    if (theController.isInstanceOf[Controller[_]]) {
////      if (!(theObject == papplet)) {
////        ((theController.asInstanceOf[Controller[_]]).unplugFrom(papplet).asInstanceOf[Controller[_]]).plugTo(theObject)
////      }
////    }
////    if (!_myObjectToControllerMap.containsKey(theObject)) {
////      _myObjectToControllerMap.put(theObject, new ArrayList[ControllerInterface[_]])
////    }
////    _myObjectToControllerMap.get(theObject).add(theController)
////    return this
////  }
////
////  def register(theController: ControllerInterface[_]): ControlP5 = {
////    return register(papplet, "", theController)
////  }
////
////  /**
////   * Returns a List of all controllers currently
////   * registered.
////   *
////   * @return List<ControllerInterface<?>>
////   */
////  def getAll: List[ControllerInterface[_]] = {
////    return new ArrayList[ControllerInterface[_]](_myControllerMap.values)
////  }
////
////  /**
////   * Returns a list of controllers or groups of a
////   * particular type. The following example will return a
////   * list of registered Bangs only:<br />
////   * <code><pre>
////   * List<Bang> list = controlP5.getAll(Bang.class);
////   * println(list);
////   * for(Bang b:list) {
////   * b.setColorForeground(color(255,255,0));
////   * }
////   * </pre></code> Here the foreground color of all Bangs
////   * is changed to yellow.
////   *
////   * @param <T>
////   * @param theClass A class that extends
////   *                 ControllerInterface, which applies to all
////   *                 Controllers and ControllerGroups
////   * @return List<T>
////   */
////  @SuppressWarnings(Array("unchecked")) def getAll[T](theClass: Class[T]): List[T] = {
////    val l: ArrayList[T] = new ArrayList[T]
////    for (ci <- _myControllerMap.values) {
////      if (ci.getClass eq theClass || ci.getClass.getSuperclass eq theClass) {
////        l.add(ci.asInstanceOf[T])
////      }
////    }
////    return l
////  }
////
////  protected def deactivateControllers {
////    for (t <- getAll(classOf[Textfield])) {
////      t.setFocus(false)
////    }
////  }
////
////  private def checkAddress(theName: String): String = {
////    if (!theName.startsWith("/")) {
////      return "/" + theName
////    }
////    return theName
////  }
////
////  /**
////   * @excude
////   */
////  def printControllerMap {
////    val strs: List[String] = new ArrayList[String]
////    System.out.println("============================================") {
////      val it: Iterator[_] = _myControllerMap.entrySet.iterator
////      while (it.hasNext) {
////        val entry: Map.Entry[_, _] = it.next.asInstanceOf[Map.Entry[_, _]]
////        val key: AnyRef = entry.getKey
////        val value: AnyRef = entry.getValue
////        strs.add(key + " = " + value)
////      }
////    }
////    Collections.sort(strs)
////    for (s <- strs) {
////      System.out.println(s)
////    }
////    System.out.println("============================================")
////  }
////
////  /**
////   * removes a controller by instance.
////   *
////   * TODO Fix this. this only removes the reference to a
////   * controller from the controller map but not its
////   * children, fatal for controller groups!
////   *
////   * @param theController ControllerInterface
////   */
////  protected def remove(theController: ControllerInterface[_]) {
////    _myControllerMap.remove(theController.getAddress)
////  }
////
////  /**
////   * removes a controlP5 element such as a controller,
////   * group, or tab by name.
////   *
////   * @param theString String
////   */
////  def remove(theName: String) {
////    val address: String = checkAddress(theName)
////    if (getController(address) != null) {
////      getController(address).remove
////    }
////    if (getGroup(address) != null) {
////      getGroup(address).remove
////    }
////    {
////      var i: Int = 0
////      while (i < _window.getTabs.size) {
////        {
////          if (_window.getTabs.get(i).getAddress == address) {
////            _window.getTabs.get(i).remove
////          }
////        }
////        ({
////          i += 1;
////          i - 1
////        })
////      }
////    }
////    _myControllerMap.remove(address)
////  }
////
////  def get(theName: String): ControllerInterface[_] = {
////    val address: String = checkAddress(theName)
////    if (_myControllerMap.containsKey(address)) {
////      return _myControllerMap.get(address)
////    }
////    return null
////  }
////
////  def get(theObject: AnyRef, theName: String): ControllerInterface[_] = {
////    return getController(theName, theObject)
////  }
////
////  def get[C](theClass: Class[C], theName: String): C = {
////    for (ci <- _myControllerMap.values) {
////      if (ci.getClass eq theClass || ci.getClass.getSuperclass eq theClass) {
////        return get(theName).asInstanceOf[C]
////      }
////    }
////    return null
////  }
////
////  /**
////   * @exclude
////   * @see ControlP5#getAll(Class)
////   * @return List<ControllerInterface>
////   */
////  //@Invisible
////  def getList: List[ControllerInterface[_]] = {
////    val l: LinkedList[ControllerInterface[_]] = new LinkedList[ControllerInterface[_]]
////    l.addAll(_window.getTabs.get)
////    l.addAll(getAll)
////    return l
////  }
////
////  def getValue(theIndex: String): Float = {
////    val c: Controller[_] = getController(theIndex)
////    if (c != null) {
////      return c.getValue
////    }
////    return Float.NaN
////  }
////
////  def getController(theName: String): Controller[_] = {
////    val address: String = checkAddress(theName)
////    if (_myControllerMap.containsKey(address)) {
////      if (_myControllerMap.get(address).isInstanceOf[Controller[_]]) {
////        return _myControllerMap.get(address).asInstanceOf[Controller[_]]
////      }
////    }
////    return null
////  }
////
////  def getGroup(theGroupName: String): ControllerGroup[_] = {
////    val address: String = checkAddress(theGroupName)
////    if (_myControllerMap.containsKey(address)) {
////      if (_myControllerMap.get(address).isInstanceOf[ControllerGroup[_]]) {
////        return _myControllerMap.get(address).asInstanceOf[ControllerGroup[_]]
////      }
////    }
////    return null
////  }
////
////  private def checkName(theName: String): Boolean = {
////    if (_myControllerMap.containsKey(checkAddress(theName))) {
////      ControlP5.logger.warning("Controller with name \"" + theName + "\" already exists. overwriting reference of existing controller.")
////      return true
////    }
////    return false
////  }
////
////  def moveControllersForObject(theObject: AnyRef, theGroup: ControllerGroup[_]) {
////    if (_myObjectToControllerMap.containsKey(theObject)) {
////      val cs: ArrayList[ControllerInterface[_]] = _myObjectToControllerMap.get(theObject)
////      for (c <- cs) {
////        (c.asInstanceOf[Controller[_]]).moveTo(theGroup)
////      }
////    }
////  }
////
////  def move(theObject: AnyRef, theGroup: ControllerGroup[_]) {
////    moveControllersForObject(theObject, theGroup)
////  }
////
////  /**
////   * @exclude
////   */
////  //@Invisible
////  def pre {
////    val itr: Iterator[FieldChangedListener] = _myFieldChangedListenerMap.values.iterator
////    while (itr.hasNext) {
////      itr.next.update
////    }
////  }
////
////  /**
////   * convenience method to access the main window
////   * (ControlWindow class).
////   */
////  def getWindow: ControlWindow = {
////    return getWindow(papplet)
////  }
////
////  def mouseEvent(theMouseEvent: MouseEvent) {
////    getWindow.mouseEvent(theMouseEvent)
////  }
////
////  def keyEvent(theKeyEvent: KeyEvent) {
////    getWindow.keyEvent(theKeyEvent)
////  }
////
////  /**
////   * convenience method to access the pointer of the main
////   * control window.
////   */
////  def getPointer: ControlWindow#Pointer = {
////    return getWindow(papplet).getPointer
////  }
////
////  /**
////   * convenience method to check if the mouse (or pointer)
////   * is hovering over any controller. only applies to the
////   * main window. To receive the mouseover information for
////   * a ControlWindow use
////   * getWindow(nameOfWindow).isMouseOver();
////   */
////  def isMouseOver: Boolean = {
////    return getWindow(papplet).isMouseOver
////  }
////
////  /**
////   * convenience method to check if the mouse (or pointer)
////   * is hovering over a specific controller. only applies
////   * to the main window. To receive the mouseover
////   * information for a ControlWindow use
////   * getWindow(nameOfWindow
////   * ).isMouseOver(ControllerInterface<?>);
////   */
////  def isMouseOver(theController: ControllerInterface[_]): Boolean = {
////    return getWindow(papplet).isMouseOver(theController)
////  }
////
////  /**
////   * convenience method to check if the mouse (or pointer)
////   * is hovering over a specific controller. only applies
////   * to the main window. To receive the mouseover
////   * information for a ControlWindow use
////   * getWindow(nameOfWindow).getMouseOverList();
////   */
////  def getMouseOverList: List[ControllerInterface[_]] = {
////    return getWindow(papplet).getMouseOverList
////  }
////
////  def getWindow(theApplet: PApplet): ControlWindow = {
////    if (theApplet == papplet) {
////      return _window
////    }
////    return _window
////  }
////
////  /**
////   * adds a Canvas to the default sketch window.
////   *
////   * @see Canvas
////   */
////  def addCanvas(theCanvas: Canvas): ControlP5 = {
////    getWindow.addCanvas(theCanvas)
////    return this
////  }
////
////  def removeCanvas(theCanvas: Canvas): ControlP5 = {
////    getWindow.removeCanvas(theCanvas)
////    return this
////  }
////
////  def setColor(theColor: CColor): ControlP5 = {
////    setColorBackground(theColor.getBackground)
////    setColorForeground(theColor.getForeground)
////    setColorActive(theColor.getActive)
////    setColorCaptionLabel(theColor.getCaptionLabel)
////    setColorValueLabel(theColor.getValueLabel)
////    return this
////  }
////
////  /**
////   * sets the active state color of tabs and controllers,
////   * this cascades down to all known controllers.
////   */
////  def setColorActive(theColor: Int): ControlP5 = {
////    ControlP5.color.setActive(theColor)
////    _window.setColorActive(theColor)
////    return this
////  }
////
////  /**
////   * sets the foreground color of tabs and controllers,
////   * this cascades down to all known controllers.
////   */
////  def setColorForeground(theColor: Int): ControlP5 = {
////    ControlP5.color.setForeground(theColor)
////    _window.setColorForeground(theColor)
////    return this
////  }
////
////  /**
////   * sets the background color of tabs and controllers,
////   * this cascades down to all known controllers.
////   */
////  def setColorBackground(theColor: Int): ControlP5 = {
////    ControlP5.color.setBackground(theColor)
////    _window.setColorBackground(theColor)
////    return this
////  }
////
////  /**
////   * sets the label color of tabs and controllers, this
////   * cascades down to all known controllers.
////   */
////  def setColorCaptionLabel(theColor: Int): ControlP5 = {
////    ControlP5.color.setCaptionLabel(theColor)
////    _window.setColorLabel(theColor)
////    return this
////  }
////
////  /**
////   * sets the value color of controllers, this cascades
////   * down to all known controllers.
////   */
////  def setColorValueLabel(theColor: Int): ControlP5 = {
////    ControlP5.color.setValueLabel(theColor)
////    _window.setColorValue(theColor)
////    return this
////  }
////
////  def setBackground(theColor: Int): ControlP5 = {
////    _window.setBackground(theColor)
////    return this
////  }
////
////  /**
////   * Enables/disables Controllers to be moved around when
////   * ALT-key is down and mouse is dragged. Other key
////   * events are still available like ALT-h to hide and
////   * show the controllers To disable all key events, use
////   * disableKeys()
////   */
////  def setMoveable(theFlag: Boolean): ControlP5 = {
////    isMoveable = theFlag
////    return this
////  }
////
////  /**
////   * Checks if controllers are generally moveable
////   *
////   */
////  def isMoveable: Boolean = {
////    return isMoveable
////  }
////
////  /**
////   * Saves the current values of controllers into a
////   * default properties file
////   *
////   * @see controlP5.ControllerProperties
////   */
////  def saveProperties: Boolean = {
////    return _myProperties.save
////  }
////
////  /**
////   * Saves the current values of controllers into a file,
////   * the filepath is given by parameter theFilePath.
////   *
////   * @see controlP5.ControllerProperties
////   */
////  def saveProperties(theFilePath: String): Boolean = {
////    return _myProperties.saveAs(theFilePath)
////  }
////
////  def saveProperties(theFilePath: String, theSets: String*): Boolean = {
////    return _myProperties.saveAs(theFilePath, theSets)
////  }
////
////  /**
////   * Loads properties from a default properties file and
////   * changes values of controllers accordingly.
////   *
////   * @see controlP5.ControllerProperties
////   * @return
////   */
////  def loadProperties: Boolean = {
////    return _myProperties.load
////  }
////
////  /**
////   * Loads properties from a properties file and changes
////   * the values of controllers accordingly, the filepath
////   * is given by parameter theFilePath.
////   *
////   * @param theFilePath
////   * @return
////   */
////  def loadProperties(theFilePath: String): Boolean = {
////    var path: String = if (theFilePath.endsWith(_myProperties.format.getExtension)) theFilePath else theFilePath + "." + _myProperties.format.getExtension
////    path = checkPropertiesPath(path)
////    val f: File = new File(path)
////    if (f.exists) {
////      return _myProperties.load(path)
////    }
////    ControlP5.logger.info("Properties File " + path + " does not exist.")
////    return false
////  }
////
////  private[controlP5] def checkPropertiesPath(theFilePath: String): String = {
////    theFilePath = if ((theFilePath.startsWith("/") || theFilePath.startsWith("."))) theFilePath else papplet.sketchPath(theFilePath)
////    return theFilePath
////  }
////
////  /**
////   * @exclude
////   * @param theFilePath
////   * @return
////   */
////  //@Invisible
////  def loadLayout(theFilePath: String): Boolean = {
////    theFilePath = checkPropertiesPath(theFilePath)
////    val f: File = new File(theFilePath)
////    if (f.exists) {
////      getLayout.load(theFilePath)
////      return true
////    }
////    ControlP5.logger.info("Layout File " + theFilePath + " does not exist.")
////    return false
////  }
////
////  /**
////   * @exclude
////   * @param theFilePath
////   */
////  def saveLayout(theFilePath: String) {
////    getLayout.save(theFilePath)
////  }
////
////  /**
////   * Returns the current version of controlP5
////   *
////   * @return String
////   */
////  def version: String = {
////    return ControlP5.VERSION
////  }
////
////  /**
////   * shows all controllers and tabs in your sketch.
////   *
////   * @see ControlP5#isVisible()
////   * @see ControlP5#hide()
////   */
////  def show {
////    _window.show
////  }
////
////  def setBroadcast(theValue: Boolean): ControlP5 = {
////    _myControlBroadcaster.broadcast = theValue
////    return this
////  }
////
////  /**
////   * returns true or false according to the current
////   * visibility flag.
////   *
////   * @see ControlP5#show()
////   * @see ControlP5#hide()
////   */
////  def isVisible: Boolean = {
////    return _window.isVisible
////  }
////
////  def setVisible(b: Boolean): ControlP5 = {
////    if (b) {
////      show
////    }
////    else {
////      hide
////    }
////    return this
////  }
////
////  /**
////   * hide all controllers and tabs inside your sketch
////   * window.
////   *
////   * @see ControlP5#show()
////   * @see ControlP5#isVisible()
////   */
////  def hide {
////    _window.hide
////  }
////
////  /**
////   * forces all controllers to update.
////   *
////   * @see ControlP5#isUpdate()
////   * @see ControlP5#setUpdate()
////   */
////  def update {
////    _window.update
////  }
////
////  /**
////   * checks if automatic updates are enabled. By default
////   * this is true.
////   *
////   * @see ControlP5#update()
////   * @see ControlP5#setUpdate(boolean)
////   * @return
////   */
////  def isUpdate: Boolean = {
////    return isUpdate
////  }
////
////  /**
////   * changes the update behavior according to parameter
////   * theFlag
////   *
////   * @see ControlP5#update()
////   * @see ControlP5#isUpdate()
////   * @param theFlag
////   */
////  def setUpdate(theFlag: Boolean) {
////    isUpdate = theFlag
////    _window.setUpdate(theFlag)
////  }
////
////  def setFont(theControlFont: ControlFont): Boolean = {
////    _controlFont = theControlFont
////    isControlFont = true
////    updateFont(_controlFont)
////    return isControlFont
////  }
////
////  def setFont(thePFont: PFont, theFontSize: Int): Boolean = {
////    _controlFont = new ControlFont(thePFont, theFontSize)
////    isControlFont = true
////    updateFont(_controlFont)
////    return isControlFont
////  }
////
////  def setFont(thePFont: PFont): Boolean = {
////    _controlFont = new ControlFont(thePFont)
////    isControlFont = true
////    updateFont(_controlFont)
////    return isControlFont
////  }
////
////  protected def updateFont(theControlFont: ControlFont) {
////    _window.updateFont(theControlFont)
////  }
////
////  def getFont: ControlFont = {
////    return _controlFont
////  }
////
////  /**
////   * disables shortcuts such as alt-h for hiding/showing
////   * controllers
////   *
////   */
////  def disableShortcuts {
////    isShortcuts = false
////  }
////
////  def isShortcuts: Boolean = {
////    return isShortcuts
////  }
////
////  /**
////   * enables shortcuts.
////   */
////  def enableShortcuts {
////    isShortcuts = true
////  }
////
////  def getTooltip: Tooltip = {
////    return _myTooltip
////  }
////
////  def setTooltip(theTooltip: Tooltip) {
////    _myTooltip = theTooltip
////  }
////
////  def setMouseWheelRotation(theRotation: Int) {
////    getWindow.setMouseWheelRotation(theRotation)
////  }
////
////  /**
////   * cp5.begin() and cp5.end() are mechanisms to
////   * auto-layout controllers, see the ControlP5beginEnd
////   * example.
////   */
////  def begin: ControllerGroup[_] = {
////    return begin(_window.getTab("default"))
////  }
////
////  def begin(theGroup: ControllerGroup[_]): ControllerGroup[_] = {
////    setCurrentPointer(theGroup)
////    return theGroup
////  }
////
////  def begin(theX: Int, theY: Int): ControllerGroup[_] = {
////    return begin(_window.getTab("default"), theX, theY)
////  }
////
////  def begin(theGroup: ControllerGroup[_], theX: Int, theY: Int): ControllerGroup[_] = {
////    setCurrentPointer(theGroup)
////    ControllerGroup.set(theGroup.autoPosition, theX, theY)
////    theGroup.autoPositionOffsetX = theX
////    return theGroup
////  }
////
////  def begin(theWindow: ControlWindow): ControllerGroup[_] = {
////    return begin(theWindow.getTab("default"))
////  }
////
////  def begin(theWindow: ControlWindow, theX: Int, theY: Int): ControllerGroup[_] = {
////    return begin(theWindow.getTab("default"), theX, theY)
////  }
////
////  def end(theGroup: ControllerGroup[_]): ControllerGroup[_] = {
////    releaseCurrentPointer(theGroup)
////    return theGroup
////  }
////
////  /**
////   * cp5.begin() and cp5.end() are mechanisms to
////   * auto-layout controllers, see the ControlP5beginEnd
////   * example.
////   */
////  def end: ControllerGroup[_] = {
////    return end(_window.getTab("default"))
////  }
////
////  def addPositionTo(theX: Int, theY: Int, theControllers: List[ControllerInterface[_]]) {
////    val v: Array[Float] = Array[Float](theX, theY)
////    for (c <- theControllers) {
////      val v1: Array[Float] = new Array[Float](2)
////      Controller.set(v1, Controller.x(c.getPosition), Controller.y(c.getPosition))
////      c.setPosition(Controller.x(v) + Controller.x(v1), Controller.y(v) + Controller.y(v1))
////    }
////  }
////
////  def addPositionTo(theX: Int, theY: Int, theControllers: ControllerInterface[_]*) {
////    addPositionTo(theX, theY, Arrays.asList(theControllers))
////  }
//}
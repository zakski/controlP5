package com.hipposretribution.controlP5;

/**
 * controlP5 is a processing gui library.
 * 
 * 2006-2012 by Andreas Schlegel
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version. This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * @author Andreas Schlegel (http://www.sojamo.de)
 * @modified 12/23/2012
 * @version 2.0.4
 * 
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.event.Event;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import com.hipposretribution.controlP5.cast.CBroadcaster;
import com.hipposretribution.controlP5.cast.FieldChangedListener;
import com.hipposretribution.controlP5.cast.callback.CListener;
import com.hipposretribution.controlP5.colour.CColor;
import com.hipposretribution.controlP5.colour.CColourInterface;
import com.hipposretribution.controlP5.drawable.Canvas;
import com.hipposretribution.controlP5.drawable.Tooltip;
import com.hipposretribution.controlP5.drawable.controller.CElement;
import com.hipposretribution.controlP5.drawable.controller.CGroup;
import com.hipposretribution.controlP5.drawable.controller.CInterface;
import com.hipposretribution.controlP5.drawable.controller.group.CTab;
import com.hipposretribution.controlP5.drawable.controller.lone.text.TextField;
import com.hipposretribution.controlP5.drawable.label.ControlFont;
import com.hipposretribution.controlP5.input.keys.KeyShortcut;
import com.hipposretribution.controlP5.input.keys.KeyStorage;
import com.hipposretribution.controlP5.input.mouse.MousePointer;

/**
 * <p>
 * controlP5 is a processing and java library for creating simple control GUIs. The ControlP5 class, the core
 * of controlP5.
 * </p>
 * <p>
 * All addController-Methods are located inside the ControlP5Base class.
 * </p>
 * 
 * @see com.hipposretribution.controlP5.ControlP5Base
 * @example use/ControlP5basics
 */
public class ControlP5 extends ControlP5Base implements CColourInterface<ControlP5> {

	public static final Logger logger = Logger.getLogger(ControlP5.class.getName()); // TODO Consider Log4J
	protected static CColor colourGlobal = new CColor(CP5BLUE); // global default colour

	public PApplet papplet; // processing applet to draw the gui on
	protected ControlWindow window; // window to control the applets with
	protected CBroadcaster caster; // broadcaster to handle events

	protected ControlFont font; // global font

	protected HashMap<Object, ArrayList<CInterface<?>>> objectToControllers;
	protected TreeMap<String, CInterface<?>> controllers; // registry of controllers

	protected KeyStorage keys; // key handling, separate so can be used by other libs/classes
	protected MousePointer mouse; // mouse handling, separate so can be used by other libs/classes

	protected boolean isAndroid; // true if running on Android device, false otherwise
	protected boolean isMoveable;
	public boolean isAutoInitialization = false;
	protected boolean isGlobalControllersAlwaysVisible = true;
	protected boolean isUpdate;
	protected boolean isControlFont;
	protected boolean isAnnotation;

	public boolean isTabEventsActive; // whether tab events should be created

	protected Tooltip _myTooltip;

	/**
	 * Create a new instance of controlP5.
	 * 
	 * @param theParent PApplet
	 */
	public ControlP5(final PApplet theParent, MousePointer mousePointer, KeyStorage keyStorage) {
		papplet = theParent;
		mouse = mousePointer;
		keys = keyStorage;
		init();
	}

	public ControlP5(final PApplet theParent, MousePointer mousePointer, KeyStorage keyStorage, PFont thePFont) {
		this(theParent, mousePointer, keyStorage);
		setFont(thePFont);
	}

	public ControlP5(final PApplet theParent, MousePointer mousePointer, KeyStorage keyStorage,
			ControlFont theControlFont) {
		this(theParent, mousePointer, keyStorage);
		setFont(theControlFont);
	}

	/**
	 * Create a new instance of controlP5.
	 * 
	 * @param theParent PApplet
	 */
	public ControlP5(final PApplet theParent) {
		this(theParent, new MousePointer(), new KeyStorage(5000));
	}

	public ControlP5(final PApplet theParent, PFont thePFont) {
		this(theParent);
		setFont(thePFont);
	}

	public ControlP5(final PApplet theParent, ControlFont theControlFont) {
		this(theParent);
		setFont(theControlFont);
	}

	/**
	 * Method to get the Global Colour
	 * 
	 * @return Global Colour
	 */
	public static CColor getGlobalColour() {
		return colourGlobal;
	}

	/**
	 * Method to set the Global Colour. Cascades down to all other controllers.
	 * 
	 * @return this
	 */
	@Override
	public ControlP5 setColour(CColor theColor) {
		colourGlobal.set(theColor);
		window.setColour(theColor); // Cascade down to all other controllers
		return this;
	}

	/**
	 * Method to set the Global Foreground Colour. Cascades down to all other controllers.
	 * 
	 * @return this
	 */
	@Override
	public ControlP5 setColourForeground(int colorForeground) {
		colourGlobal.setForeground(colorForeground);
		window.setColourForeground(colorForeground); // Cascade down to all other controllers
		return this;
	}

	/**
	 * Method to set the Global Background Colour. Cascades down to all other controllers.
	 * 
	 * @return this
	 */
	@Override
	public ControlP5 setColourBackground(int colorBackground) {
		colourGlobal.setBackground(colorBackground);
		window.setColourBackground(colorBackground);// Cascade down to all other controllers
		return this;
	}

	/**
	 * Method to set the Global Caption Label Colour. Cascades down to all other controllers.
	 * 
	 * @return this
	 */
	@Override
	public ControlP5 setColourCaption(int colorCaption) {
		colourGlobal.setCaption(colorCaption);
		window.setColourCaption(colorCaption);// Cascade down to all other controllers
		return this;
	}

	/**
	 * Method to set the Global Value Colour. Cascades down to all other controllers.
	 * 
	 * @return this
	 */
	@Override
	public ControlP5 setColourValue(int colorValue) {
		colourGlobal.setValue(colorValue);
		window.setColourValue(colorValue); // Cascade down to all other controllers
		return this;
	}

	/**
	 * Method to set the Global Active Colour. Cascades down to all other controllers.
	 * 
	 * @return this
	 */
	@Override
	public ControlP5 setColourActive(int colorActive) {
		colourGlobal.setActive(colorActive);
		window.setColourActive(colorActive); // Cascade down to all other controllers
		return this;
	}

	/**
	 * Method same as {@link #getGlobalColour() getGlobalColour()}
	 * 
	 * @return Global Colour
	 */
	@Override
	public CColor getColour() {
		return getGlobalColour();
	}

	protected void init() {
		// renderer = (papplet.g.getClass().getCanonicalName().indexOf("Java2D") > -1) ? J2D : P3D;
		Class<?> check = papplet.getClass();
		while (check != null) {
			check = check.getSuperclass();
			if (check != null) {
				if (check.toString().toLowerCase().indexOf("android.app.") > -1) {
					isAndroid = true;
					break;
				}
			}
		}

		if (!isAndroid) {
			papplet.registerMethod("keyEvent", this);
			papplet.registerMethod("mouseEvent", this);
		}

		isTabEventsActive = false;

		caster = new CBroadcaster(this);

		font = new ControlFont(papplet.createFont("Ariel", 8));
		;

		window = new ControlWindow(this, papplet);
		keys.attachHandler(window);
		mouse.attachHandler(window);

		papplet.registerMethod("pre", this);
		papplet.registerMethod("dispose", this);

		controllers = new TreeMap<String, CInterface<?>>();

		setFont(font);

		_myTooltip = new Tooltip(this);

		super.init(this);

		disableShortcuts();

		setFont(font);

	}

	/**
	 * By default event originating from tabs are disabled, use setTabEventsActive(true) to receive
	 * controlEvents when tabs are clicked.
	 * 
	 * @param theFlag
	 */
	public void setTabEventsActive(boolean theFlag) {
		isTabEventsActive = theFlag;
	}

	/**
	 * autoInitialization can be very handy when it comes to initializing values, e.g. you load a set of
	 * controllers, then the values that are attached to the controllers will be reset to its saved state. to
	 * turn of auto intialization, call setAutoInitialization(false) right after initializing controlP5 and
	 * before creating any controller.
	 * 
	 * @param theFlag boolean
	 */
	public void setAutoInitialization(boolean theFlag) {
		isAutoInitialization = theFlag;
	}

	/**
	 * by default controlP5 draws any controller on top of any drawing done in the draw() function (this
	 * doesnt apply to P3D where controlP5.draw() has to be called manually in the sketch's draw() function ).
	 * to turn off the auto drawing of controlP5, use controlP5.setAutoDraw(false). now you can call
	 * controlP5.draw() any time whenever controllers should be drawn into the sketch.
	 * 
	 * @param theFlag boolean
	 */
	public void setAutoDraw(boolean theFlag) {
		if (isAutoDraw() && theFlag == false) {
			window.papplet().unregisterMethod("draw", window);
		}
		if (isAutoDraw() == false && theFlag == true) {
			window.papplet().registerMethod("draw", window);
		}
		window.isAutoDraw = theFlag;
	}

	/**
	 * check if the autoDraw function for the main window is enabled(true) or disabled(false).
	 * 
	 * @return boolean
	 */
	public boolean isAutoDraw() {
		return window.isAutoDraw;
	}

	/**
	 * 
	 * @see com.hipposretribution.controlP5.ControlBroadcaster
	 */
	public CBroadcaster getBroadcaster() {
		return caster;
	}

	/**
	 * @see com.hipposretribution.controlP5.events.control.ControlListener
	 */
	public ControlP5 addCallback(CListener... theListeners) {
		getBroadcaster().addListener(theListeners);
		return this;
	}

	/**
	 * @see com.hipposretribution.controlP5.events.control.ControlListener
	 */
	public ControlP5 removeCallback(CListener... theListeners) {
		getBroadcaster().removeListener(theListeners);
		return this;
	}

	/**
	 * @see com.hipposretribution.controlP5.events.control.ControlListener
	 */
	public CListener getListener(int theIndex) {
		return getBroadcaster().getListener(theIndex);
	}


	/**
	 * TODO
	 * 
	 * @exclude
	 * @param theObject
	 */
	public void addControlsFor(Object theObject) {

	}

	public CTab getTab(String theName) {
		for (int i = 0; i < window.getTabs().size(); i++) {
			if (((CTab) window.getTabs().get(i)).getName().equals(theName)) {
				return (CTab) window.getTabs().get(i);
			}
		}
		CTab myTab = addTab(theName);
		return myTab;
	}

	public CTab getTab(ControlWindow theWindow, String theName) {
		for (int i = 0; i < theWindow.getTabs().size(); i++) {
			if (((CTab) theWindow.getTabs().get(i)).getName().equals(theName)) {
				return (CTab) theWindow.getTabs().get(i);
			}
		}
		CTab myTab = theWindow.add(new CTab(this, theWindow, theName));
		return myTab;
	}

	/**
	 * registers a Controller with ControlP5, a Controller should/must be registered with a unique name. If
	 * not, accessing Controllers by name is not guaranteed. the rule here is last come last serve, existing
	 * Controllers with the same name will be overridden.
	 * 
	 * @param theController ControllerInterface
	 * @return ControlP5
	 */
	public ControlP5 register(Object theObject, String theIndex, CInterface<?> theController) {
		String address = "";
		if (theObject == papplet) {
			address = (theController.getName().startsWith("/")) ? "" : "/";
			address += theController.getName();
		} else {
			address = (((theIndex.length() == 0) || theIndex.startsWith("/")) ? "" : "/");
			address += theIndex;
			address += (theController.getName().startsWith("/") ? "" : "/");
			address += theController.getName();
		}
		theController.setAddress(address);
		if (checkName(theController.getAddress())) {
			/*
			 * in case a controller with the same name already exists, will be deleted
			 */
			remove(theController.getAddress());
		}
		/* add the controller to the controller map */
		controllers.put(theController.getAddress(), theController);

		/* initialize the controller */
		theController.init();

		/*
		 * handle controller plugs and map controllers to its reference objects if applicable.
		 */
		if (theController instanceof CElement<?>) {
			if (theObject == null) {
				theObject = papplet;
			}
			if (!theObject.equals(papplet)) {
				((CElement<?>) ((CElement<?>) theController).unplugFrom(papplet)).plugTo(theObject);
			}

			if (!_myObjectToControllerMap.containsKey(theObject)) {
				_myObjectToControllerMap.put(theObject, new ArrayList<CInterface<?>>());
			}
			_myObjectToControllerMap.get(theObject).add(theController);
		} else {
			/*
			 * if theController is of type ControllerGroup, map accordingly here.
			 */
		}
		return this;
	}

	/**
	 * Returns a List of all controllers currently registered.
	 * 
	 * @return List<ControllerInterface<?>>
	 */
	public List<CInterface<?>> getAll() {
		return new ArrayList<CInterface<?>>(controllers.values());
	}

	/**
	 * Returns a list of controllers or groups of a particular type. The following example will return a list
	 * of registered Bangs only:<br />
	 * <code><pre>
	 * List<Bang> list = controlP5.getAll(Bang.class);
	 * println(list);
	 * for(Bang b:list) {
	 *   b.setColorForeground(color(255,255,0));
	 * }
	 * </pre></code> Here the foreground color of all Bangs is changed to yellow.
	 * 
	 * @param <T>
	 * @param theClass A class that extends ControllerInterface, which applies to all Controllers and
	 *            ControllerGroups
	 * @return List<T>
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getAll(Class<T> theClass) {
		ArrayList<T> l = new ArrayList<T>();
		for (CInterface<?> ci : controllers.values()) {
			if (ci.getClass() == theClass || ci.getClass().getSuperclass() == theClass) {
				l.add((T) ci);
			}
		}
		return l;
	}

	protected void deactivateControllers() {
		for (TextField t : getAll(TextField.class)) {
			t.setFocus(false);
		}
	}

	private String checkAddress(String theName) {
		if (!theName.startsWith("/")) {
			return "/" + theName;
		}
		return theName;
	}

	/**
	 * @excude
	 */
	public void printControllerMap() {
		List<String> strs = new ArrayList<String>();
		System.out.println("============================================");
		for (Iterator<Entry<String, CInterface<?>>> it = controllers.entrySet().iterator(); it.hasNext();) {
			Entry<String, CInterface<?>> entry = it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			strs.add(key + " = " + value);
		}
		Collections.sort(strs);
		for (String s : strs) {
			System.out.println(s);
		}
		System.out.println("============================================");
	}

	/**
	 * removes a controller by instance.
	 * 
	 * TODO Fix this. this only removes the reference to a controller from the controller map but not its
	 * children, fatal for controller groups!
	 * 
	 * @param theController ControllerInterface
	 */
	protected void remove(CInterface<?> theController) {
		controllers.remove(theController.getAddress());
	}

	/**
	 * removes a controlP5 element such as a controller, group, or tab by name.
	 * 
	 * @param theString String
	 */
	public void remove(String theName) {
		String address = checkAddress(theName);

		if (getController(address) != null) {
			getController(address).remove();
		}

		if (getGroup(address) != null) {
			getGroup(address).remove();
		}

		for (int i = 0; i < window.getTabs().size(); i++) {
			if (window.getTabs().get(i).getAddress().equals(address)) {
				window.getTabs().get(i).remove();
			}
		}
		controllers.remove(address);
	}

	public CInterface<?> get(String theName) {
		String address = checkAddress(theName);
		if (controllers.containsKey(address)) {
			return controllers.get(address);
		}
		return null;
	}

	public <C> C get(Class<C> theClass, String theName) {
		for (CInterface<?> ci : controllers.values()) {
			if (ci.getClass() == theClass || ci.getClass().getSuperclass() == theClass) {
				return (C) get(theName);
			}
		}
		return null;
	}

	/**
	 * @exclude
	 * @see com.hipposretribution.controlP5.ControlP5#getAll(Class)
	 * @return List<ControllerInterface>
	 */
	@Invisible
	public List<CInterface<?>> getList() {
		LinkedList<CInterface<?>> l = new LinkedList<CInterface<?>>();
		l.addAll(window.getTabs().get());
		l.addAll(getAll());
		return l;
	}

	public float getValue(String theIndex) {
		CElement<?> c = getController(theIndex);
		if (c != null) {
			return c.getValue();
		}
		return Float.NaN;
	}

	public CElement<?> getController(String theName) {
		String address = checkAddress(theName);
		if (controllers.containsKey(address)) {
			if (controllers.get(address) instanceof CElement<?>) {
				return (CElement<?>) controllers.get(address);
			}
		}
		return null;
	}

	public CGroup<?> getGroup(String theGroupName) {
		String address = checkAddress(theGroupName);
		if (controllers.containsKey(address)) {
			if (controllers.get(address) instanceof CGroup<?>) {
				return (CGroup<?>) controllers.get(address);
			}
		}
		return null;
	}

	private boolean checkName(String theName) {
		if (controllers.containsKey(checkAddress(theName))) {
			ControlP5.logger().warning(
				"Controller with name \"" + theName
						+ "\" already exists. overwriting reference of existing controller.");
			return true;
		}
		return false;
	}

	public void moveControllersForObject(Object theObject, CGroup<?> theGroup) {
		if (_myObjectToControllerMap.containsKey(theObject)) {
			ArrayList<CInterface<?>> cs = _myObjectToControllerMap.get(theObject);
			for (CInterface<?> c : cs) {
				((CElement<?>) c).moveTo(theGroup);
			}
		}
	}

	public void move(Object theObject, CGroup<?> theGroup) {
		moveControllersForObject(theObject, theGroup);
	}

	protected void clear() {
		window.clear();
		controllers.clear();
	}

	/**
	 * @exclude
	 */
	@Invisible
	public void pre() {
		Iterator<FieldChangedListener> itr = _myFieldChangedListenerMap.values().iterator();
		while (itr.hasNext()) {
			itr.next().update();
		}
	}

	/**
	 * call draw() from your program when autoDraw is disabled.
	 * 
	 * @exclude
	 */
	@Invisible
	public void draw() {
		if (!isAutoDraw()) {
			window.draw();
		}
	}

	/**
	 * convenience method to access the main window (ControlWindow class).
	 */
	public ControlWindow getWindow() {
		return getWindow(papplet);
	}

	public void mouseEvent(MouseEvent theMouseEvent) {
		mouse.mouseEvent(theMouseEvent);
	}

	public void keyEvent(KeyEvent theKeyEvent) {
		keys.keyEvent(theKeyEvent);
	}

	/**
	 * convenience method to access the pointer of the main control window.
	 */
	public MousePointer getPointer() {
		return mouse;
	}

	/**
	 * convenience method to check if the mouse (or pointer) is hovering over any controller. only applies to
	 * the main window. To receive the mouseover information for a ControlWindow use
	 * getWindow(nameOfWindow).isMouseOver();
	 */
	public boolean isMouseOver() {
		return getWindow(papplet).isMouseOver();
	}

	/**
	 * convenience method to check if the mouse (or pointer) is hovering over a specific controller. only
	 * applies to the main window. To receive the mouseover information for a ControlWindow use
	 * getWindow(nameOfWindow).isMouseOver(ControllerInterface<?>);
	 */
	public boolean isMouseOver(CInterface<?> theController) {
		return getWindow(papplet).isMouseOver(theController);
	}

	/**
	 * convenience method to check if the mouse (or pointer) is hovering over a specific controller. only
	 * applies to the main window. To receive the mouseover information for a ControlWindow use
	 * getWindow(nameOfWindow).getMouseOverList();
	 */
	public List<CInterface<?>> getMouseOverList() {
		return getWindow(papplet).getMouseOverList();
	}

	public ControlWindow getWindow(PApplet theApplet) {
		if (theApplet.equals(papplet)) {
			return window;
		}
		// TODO !!! check for another window in case theApplet is of type
		// PAppletWindow.
		return window;
	}

	/**
	 * adds a Canvas to the default sketch window.
	 * 
	 * @see com.hipposretribution.controlP5.drawable.Canvas
	 */
	public ControlP5 addCanvas(Canvas theCanvas) {
		getWindow().addCanvas(theCanvas);
		return this;
	}

	public ControlP5 removeCanvas(Canvas theCanvas) {
		getWindow().removeCanvas(theCanvas);
		return this;
	}

	/**
	 * Enables/disables Controllers to be moved around when ALT-key is down and mouse is dragged. Other key
	 * events are still available like ALT-h to hide and show the controllers To disable all key events, use
	 * disableKeys()
	 */
	public ControlP5 setMoveable(boolean theFlag) {
		isMoveable = theFlag;
		return this;
	}

	/**
	 * Checks if controllers are generally moveable
	 * 
	 */
	public boolean isMoveable() {
		return isMoveable;
	}

	/**
	 * shows all controllers and tabs in your sketch.
	 * 
	 * @see com.hipposretribution.controlP5.ControlP5#isVisible()
	 * @see com.hipposretribution.controlP5.ControlP5#hide()
	 */

	public void show() {
		window.show();
	}

	public ControlP5 setBroadcast(boolean theBroadcastFlag) {
		caster.setBroadcast(theBroadcastFlag);
		window.setBroadcast(theBroadcastFlag);
		return this;
	}

	/**
	 * returns true or false according to the current visibility flag.
	 * 
	 * @see com.hipposretribution.controlP5.ControlP5#show()
	 * @see com.hipposretribution.controlP5.ControlP5#hide()
	 */
	public boolean isVisible() {
		return window.isVisible();
	}

	/**
	 * hide all controllers and tabs inside your sketch window.
	 * 
	 * @see com.hipposretribution.controlP5.ControlP5#show()
	 * @see com.hipposretribution.controlP5.ControlP5#isVisible()
	 */
	public void hide() {
		window.hide();
	}

	/**
	 * forces all controllers to update.
	 * 
	 * @see com.hipposretribution.controlP5.ControlP5#isUpdate()
	 * @see com.hipposretribution.controlP5.ControlP5#setUpdate()
	 */
	public void update() {
		window.update();
	}

	/**
	 * checks if automatic updates are enabled. By default this is true.
	 * 
	 * @see com.hipposretribution.controlP5.ControlP5#update()
	 * @see com.hipposretribution.controlP5.ControlP5#setUpdate(boolean)
	 * @return
	 */
	public boolean isUpdate() {
		return isUpdate;
	}

	/**
	 * changes the update behavior according to parameter theFlag
	 * 
	 * @see com.hipposretribution.controlP5.ControlP5#update()
	 * @see com.hipposretribution.controlP5.ControlP5#isUpdate()
	 * @param theFlag
	 */
	public void setUpdate(boolean theFlag) {
		isUpdate = theFlag;
		window.setUpdate(theFlag);
	}

	@Deprecated
	public boolean setFont(int theBitFontIndex) {
		logger.warning("BitFont is now of type PFont, use setFont(PFont) instead.");
		return false;
	}

	public boolean setFont(ControlFont theControlFont) {
		font = theControlFont;
		isControlFont = true;
		updateFont(font);
		return isControlFont;
	}

	public boolean setFont(PFont thePFont, int theFontSize) {
		font = new ControlFont(thePFont, theFontSize);
		isControlFont = true;
		updateFont(font);
		return isControlFont;
	}

	public boolean setFont(PFont thePFont) {
		font = new ControlFont(thePFont);
		isControlFont = true;
		updateFont(font);
		return isControlFont;
	}

	protected void updateFont(ControlFont theControlFont) {
		window.updateFont(theControlFont);
	}

	public ControlFont getFont() {
		return font;
	}

	/**
	 * disables shortcuts such as alt-h for hiding/showing controllers
	 * 
	 */
	public void disableShortcuts() {
		keys.disableShortcuts();
	}

	public boolean isShortcuts() {
		return keys.shortcutsOn();
	}

	/**
	 * enables shortcuts.
	 */
	public void enableShortcuts() {
		keys.enableShortcuts();
	}

	public Tooltip getTooltip() {
		return _myTooltip;
	}

	public void setTooltip(Tooltip theTooltip) {
		_myTooltip = theTooltip;
	}

	/**
	 * cp5.begin() and cp5.end() are mechanisms to auto-layout controllers, see the ControlP5beginEnd example.
	 */
	public CGroup<?> begin() {
		// TODO replace controlWindow.tab("default") with
		// controlWindow.tabs().get(1);
		return begin(window.getTab("default"));
	}

	public CGroup<?> begin(CGroup<?> theGroup) {
		setCurrentPointer(theGroup);
		return theGroup;
	}

	public CGroup<?> begin(int theX, int theY) {
		// TODO replace controlWindow.tab("default") with
		// controlWindow.tabs().get(1);
		return begin(window.getTab("default"), theX, theY);
	}

	public CGroup<?> begin(CGroup<?> theGroup, int theX, int theY) {
		setCurrentPointer(theGroup);
		theGroup.autoPosition.x = theX;
		theGroup.autoPosition.y = theY;
		theGroup.autoPositionOffsetX = theX;
		return theGroup;
	}

	public CGroup<?> begin(ControlWindow theWindow) {
		return begin(theWindow.getTab("default"));
	}

	public CGroup<?> begin(ControlWindow theWindow, int theX, int theY) {
		return begin(theWindow.getTab("default"), theX, theY);
	}

	public CGroup<?> end(CGroup<?> theGroup) {
		releaseCurrentPointer(theGroup);
		return theGroup;
	}

	/**
	 * cp5.begin() and cp5.end() are mechanisms to auto-layout controllers, see the ControlP5beginEnd example.
	 */
	public CGroup<?> end() {
		return end(window.getTab("default"));
	}

	public void addPositionTo(int theX, int theY, List<CInterface<?>> theControllers) {
		PVector v = new PVector(theX, theY);
		for (CInterface<?> c : theControllers) {
			c.setPosition(PVector.add(c.getPosition(), v));
		}
	}

	public void addPositionTo(int theX, int theY, CInterface<?>... theControllers) {
		addPositionTo(theX, theY, Arrays.asList(theControllers));
	}

	/**
	 * disposes and clears all controlP5 elements. When running in applet mode, opening new tabs or switching
	 * to another tab causes the applet to call dispose(). therefore dispose() is disabled when running ing
	 * applet mode. TODO implement better dispose handling for applets.
	 * 
	 * @exclude
	 */
	public void dispose() {
		// if (!isApplet) {
		// clear();
		// }
	}

	/* add Objects with Annotation */

	public static Logger logger() {
		return logger;
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Invisible {
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Layout {
	}

	public void clearKeys() {
		keys.clear();
	}

	public char getKey() {
		return keys.lastPressedEvent().getKey();
	}

	public boolean isShiftDown() {
		return keys.isKeyPressed(SHIFT);
	}

	public boolean isAltDown() {
		return keys.isKeyPressed(ALT);
	}

	public boolean isMetaDown() {
		return keys.isKeyPressed(Event.META);
	}

	public void androidMouseEvent() {
		mouse.mouseEvent(papplet.mouseX, papplet.mouseY, papplet.mousePressed);
	}

	public void clearMouse() {
		mouse.setPressed(false);

	}

	public int mouseX() {
		return mouse.getX();
	}

	public int mouseY() {
		return mouse.getY();
	}

	public int mousePX() {
		return mouse.getPreviousX();
	}

	public int mousePY() {
		return mouse.getPreviousY();
	}

	public int mouseDX() {
		return mouse.getX() - mouse.getPreviousX();
	}

	public int mouseDY() {
		return mouse.getY() - mouse.getPreviousY();
	}

	public boolean isMouseLock() {
		return mouse.isMouseLock();
	}

	public void mapShortcut(KeyShortcut keyShortcut, int... nkeys) {
		keys.mapShortcut(keyShortcut, nkeys);
		
	}
}

// new controllers
// http://www.cambridgeincolour.com/tutorials/photoshop-curves.htm
// http://images.google.com/images?q=synthmaker
// http://en.wikipedia.org/wiki/Pie_menu
//
// inspiration
// http://www.explodingart.com/arb/Andrew_R._Brown/Code/Code.html

// TODO
// (1) file dialog:
// http://java.sun.com/j2se/1.5.0/docs/api/java/awt/FileDialog.html
// (2) add ControlIcon for custom icons with PImage

// gui addons inspiration.
// http://www.futureaudioworkshop.com/
//
// new color schemes http://ethanschoonover.com/solarized


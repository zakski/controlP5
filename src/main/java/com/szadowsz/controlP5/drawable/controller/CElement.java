package com.szadowsz.controlP5.drawable.controller;

/**
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

import com.szadowsz.controlP5.CP;
import com.szadowsz.controlP5.ControlP5;
import com.szadowsz.controlP5.ControlWindow;
import com.szadowsz.controlP5.behaviour.ControlBehavior;
import com.szadowsz.controlP5.cast.CBroadcaster;
import com.szadowsz.controlP5.cast.callback.CEvent;
import com.szadowsz.controlP5.cast.callback.CListener;
import com.szadowsz.controlP5.cast.plug.CPlug;
import com.szadowsz.controlP5.cast.plug.CPlugToInterface;
import com.szadowsz.controlP5.colour.CColor;
import com.szadowsz.controlP5.drawable.CDrawableInterface;
import com.szadowsz.controlP5.drawable.controller.group.CTab;
import com.szadowsz.controlP5.drawable.controller.group.ControlGroup;
import com.szadowsz.controlP5.drawable.controller.group.TextArea;
import com.szadowsz.controlP5.drawable.controller.group.listbox.ListBox;
import com.szadowsz.controlP5.drawable.controller.group.multilist.MultiList;
import com.szadowsz.controlP5.drawable.controller.lone.press.Button;
import com.szadowsz.controlP5.drawable.controller.lone.press.Matrix;
import com.szadowsz.controlP5.drawable.controller.lone.press.Toggle;
import com.szadowsz.controlP5.drawable.controller.lone.slider.Knob;
import com.szadowsz.controlP5.drawable.controller.lone.slider.Slider;
import com.szadowsz.controlP5.drawable.controller.lone.text.NumberBox;
import com.szadowsz.controlP5.drawable.controller.lone.text.TextField;
import com.szadowsz.controlP5.drawable.controller.lone.text.TextLabel;
import com.szadowsz.controlP5.drawable.controller.view.ControllerView;
import com.szadowsz.controlP5.drawable.controller.view.ControllerViewType;
import com.szadowsz.controlP5.drawable.label.ControlFont;
import com.szadowsz.controlP5.drawable.label.Label;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.KeyEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * Controller is an abstract class that is extended by any available controller within controlP5. this is the full documentation list for all methods
 * available for a controller. An event triggered by a controller will be forwarded to the main program. If a void controlEvent(ControlEvent theEvent)
 * {} method is available, this method will be called.
 * </p>
 * <p>
 * A Controller can notify the main program in 2 different ways:
 * </p>
 * <ul>
 * <li>(1) add method controlEvent(ControlEvent theEvent) to your sketch. ControlP5 will automatically detect this method and will used it to forward
 * any controlEvent triggered by a controller - you can disable forwarding by using setBroadcast(false)
 * {@link CElement#setBroadcast(boolean)}</li>
 * <li>(2) each controller requires a unique name when being create. In case an existing name is used for a newly created Controller, the existing one
 * will be overwritten. each unique name can be used to automatically link a controller to either a method or a field within your program.</li>
 * </ul>
 * 
 * @see com.hipposretribution.controlP5.elements.drawable.controllers.press.Bang
 * @see Button
 * @see Knob
 * @see Matrix
 * @see MultiList
 * @see NumberBox
 * @see com.hipposretribution.controlP5.drawable.controller.group.toggle.RadioButton
 * @see ListBox
 * @see Slider
 * @see TextArea
 * @see TextField
 * @see TextLabel
 * @see Toggle
 * @see ControlGroup
 * @see com.hipposretribution.controlP5.ControlBehavior
 * @see com.hipposretribution.controlP5.events.control.ControlEvent
 * 
 * @example use/ControlP5basics
 */
public abstract class CElement<T> implements CInterface<T>, CPlugToInterface<T>, CDrawableInterface<T> {

	protected ControlP5 cp5; // main ControlP5 instance
	protected ControlWindow window;
	protected CGroup<?> parent; // parent controller group instance
	protected ArrayList<CPlug> plugList; // the plugs for objects the controller is attached to
	protected ArrayList<CListener> listeners; // the control listeners attached to this device TODO:
												// cleanup
	private T me; // sub class cast of this object

	protected final String name; // name of the controller instance
	private String address;
	protected int id = -1; // TODO: Automate the assignment of this

	protected Label captionLabel; // description label for the controller

	protected ControllerView<T> controllerView;
	protected ControllerViewType displayMode = ControllerViewType.DEFAULT;
	protected CColor color = new CColor(); // stores all required colours of the controller

	protected PVector position; // position of the controller
	protected PVector positionBuffer = new PVector(); // used for dragging controllers
	protected PVector absolutePosition = new PVector(); // absolute position of the controller

	protected int width; // width of the controller
	protected int height; // height of the controller

	protected int decimalScale = 2; // the number of decimal places float values should have

	protected boolean isInit;
	protected boolean isActive = false; // whether the controller is currently in use
	protected boolean isDragged;
	protected boolean isLocked; // stops the controller from being interacted with
	protected boolean isBroadcast;
	protected boolean isInside;
	protected boolean isMouseOver;
	protected boolean isMousePressed;
	protected boolean isMoveable = true;
	protected boolean isVisible = true; // visibility of the controller
	protected boolean isUserInteraction = true;
	protected boolean isBehavior;
	protected boolean isUpdate = false;
	protected boolean tooltipEnabled;
	protected boolean listening;

	public float _myValue = Float.NaN;

	protected float _myDefaultValue = Float.NaN;

	protected String _myStringValue = "";

	protected float[] _myArrayValue;

	protected String target;

	protected Object targetObject;

	protected List<CElement<?>> subElements;

	protected int _myId = -1;

	protected Label _myValueLabel;

	protected float _myMin;
	protected float _myMax;
	protected float _myUnit;

	protected ControlBehavior _myBehavior;

	protected int _myDecimalPoints = 2;

	public static int autoWidth = 49;
	public static int autoHeight = 19;
	public static PVector autoSpacing = new PVector(10, 10, 0);

	protected boolean[] availableImages = new boolean[4];

	protected PImage[] images = new PImage[4];

	protected int _myPickingColor = 0xffffff00;

	/**
	 * TODO add distribution options for MOVE, RELEASE, and PRESSED. setDecimalPoints: setDcimalPoints(6) does
	 * only show 2 digits after the point
	 */

	/**
	 * Convenience constructor to extend Controller.
	 * 
	 * @example use/ControlP5extendController
	 * @param theControlP5
	 * @param theName
	 */
	public CElement(ControlP5 theControlP5, String theName) {
		this(theControlP5, theControlP5.getDefaultTab(), theName, 0, 0, autoWidth, autoHeight);
		theControlP5.register(theControlP5.papplet, theName, this);
	}

	protected CElement(final ControlP5 theControlP5, final CGroup<?> theParent, final String theName, final float theX, final float theY,
			final int theWidth, final int theHeight) {
		cp5 = theControlP5;
		me = (T) this;
		isBroadcast = (cp5 == null) ? false : cp5.getBroadcaster().isBroadcast();
		name = theName;

		position = new PVector(theX, theY, 0);
		positionBuffer = new PVector(theX, theY, 0);

		setParent(theParent);
		if (theParent != null) {
			color.set(theParent.color);
		} else {
			color.set(ControlP5.getGlobalColour());
		}
		width = theWidth;
		height = theHeight;

		captionLabel = new Label(cp5, theName);
		captionLabel.setColor(color.getCaption());
		_myValueLabel = new Label(cp5, "-");
		_myValueLabel.setColor(color.getCaption());

		plugList = new ArrayList<CPlug>();
		listeners = new ArrayList<CListener>();
		subElements = new ArrayList<CElement<?>>();
		_myArrayValue = new float[0];
	}

	@Override
	public Class<?>[] getAcceptedClassList() {
		return defaultAcceptedClassList;
	}

	/**
	 * Method to get the current number of listeners attached to this element
	 */
	@Override
	public int getListenerSize() {
		return listeners.size();
	}

	/**
	 * Method to add 1 to n listeners to this element
	 *
	 * @return the updated element
	 */
	@Override
	public final T addListener(CListener... theListeners) {
		for (CListener theListener : theListeners) {
			listeners.add(theListener);
		}
		return me;

	}

	/**
	 * Method to remove 1 to n listeners from this element
	 *
	 * @return the updated element
	 */
	@Override
	public final T removeListener(CListener... theListeners) {
		for (CListener theListener : theListeners) {
			listeners.remove(theListener);
		}

		return me;
	}

	/**
	 * Method to remove all listeners from this element
	 *
	 * @return the updated element
	 */
	@Override
	public final T removeAllListeners() {
		listeners.clear();
		return me;
	}

	/**
	 * Method to set the Controller's Colours
	 * 
	 * @return the Controller
	 */
	@Override
	public T setColour(CColor theColor) {
		color.set(theColor);
		setColourCaption(theColor.getCaption());
		setColourValue(theColor.getValue());
		return me;
	}

	/**
	 * Method to set the Controller's Foreground Colour
	 * 
	 * @return the Controller
	 */
	@ControlP5.Layout
	@Override
	public T setColourForeground(final int colorForeground) {
		color.setForeground(colorForeground);
		return me;
	}

	/**
	 * Method to set the Controller's Background Colour
	 * 
	 * @return the Controller
	 */
	@ControlP5.Layout
	@Override
	public T setColourBackground(final int theColor) {
		color.setBackground(theColor);
		return me;
	}

	/**
	 * Method to set the Controller's Caption Label Colour
	 * 
	 * @return the Controller
	 */
	@ControlP5.Layout
	@Override
	public T setColourCaption(final int theColor) {
		color.setCaption(theColor);
		captionLabel.setColor(color.getCaption());
		return me;
	}

	/**
	 * Method to set the Controller's Value Label Colour
	 * 
	 * @return the Controller
	 */
	@ControlP5.Layout
	@Override
	public T setColourValue(final int theColor) {
		color.setValue(theColor);
		if (_myValueLabel != null) {
			_myValueLabel.setColor(color.getValue());
		}
		return me;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hipposretribution.controlP5.elements.colour.CColourInterface#setColourActive(int)
	 */
	@Override
	@ControlP5.Layout
	public T setColourActive(final int theColor) {
		color.setActive(theColor);
		return me;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hipposretribution.controlP5.elements.colour.CColourInterface#getColour()
	 */
	@Override
	@ControlP5.Layout
	public CColor getColour() {
		return color;
	}

	public List<CElement<?>> getSubelements() {
		return subElements;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@ControlP5.Layout
	public String getAddress() {
		return address;
	}

	@Override
	@ControlP5.Invisible
	@ControlP5.Layout
	public T setAddress(String theAddress) {
		if (address == null || address.length() == 0) {
			address = theAddress;
		}
		return me;
	}

	protected CElement(final ControlP5 theControlP5, final String theName, final float theX, final float theY, final int theWidth, final int theHeight) {
		this(theControlP5, theControlP5.getTab("default"), theName, theX, theY, theWidth, theHeight);
	}

	protected CElement(final String theName, final int theX, final int theY) {
		name = theName;
		position = new PVector(theX, theY, 0);
	}

	@Override
	@ControlP5.Invisible
	public void init() {

		_myDefaultValue = _myValue;

		// plug to a method or field inside the main papplet.
		// forwarding a ControlEvent to the controlEvent() method inside
		// the main papplet is done by the controlbroadcaster.
		cp5.getBroadcaster().plug(cp5.papplet, this, name);
		isInit = cp5.isAutoInitialization;
		setValue(_myDefaultValue);
		isInit = true;
		updateDisplayMode(ControllerViewType.DEFAULT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hipposretribution.controlP5.elements.drawable.controllers.ControllerInterface#setFont(com.
	 * hipposretribution.controlP5.elements.drawable.label.ControlFont)
	 */
	@Override
	public T setFont(ControlFont theControlFont) {
		captionLabel.setFont(theControlFont);
		return me;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hipposretribution.controlP5.elements.drawable.controllers.ControllerInterface#setFont(processing
	 * .core.PFont)
	 */
	@Override
	public T setFont(PFont thePFont) {
		captionLabel.setFont(thePFont);
		return me;
	}

	/**
	 * with setBehavior you can add a ControlBehavior to a controller. A ControlBehavior can be used to e.g.
	 * automatically change state, function, position, etc.
	 * 
	 * @example ControlP5behavior
	 * @param theBehavior ControlBehavior
	 * @return Controller
	 */
	public T setBehavior(final ControlBehavior theBehavior) {
		isBehavior = true;
		_myBehavior = theBehavior;
		_myBehavior.init(this);
		return me;
	}

	/**
	 * @return Controller
	 */
	public T removeBehavior() {
		isBehavior = false;
		_myBehavior = null;
		return me;
	}

	/**
	 * @return ControlBehavior
	 */
	public ControlBehavior getBehavior() {
		return _myBehavior;
	}

	/**
	 * @return float
	 */
	@ControlP5.Layout
	public float getDefaultValue() {
		return _myDefaultValue;
	}

	/**
	 * set the default value.
	 * 
	 * @param theValue float
	 * @return Controller
	 */
	@ControlP5.Layout
	public T setDefaultValue(final float theValue) {
		_myDefaultValue = theValue;
		return me;
	}

	/**
	 * enable or prevent the controller to be moveable. By default a controller is moveable.
	 * 
	 * @param theValue boolean
	 * @return Controller
	 */
	@ControlP5.Layout
	public T setMoveable(final boolean theValue) {
		isMoveable = theValue;
		return me;
	}

	/**
	 * checks if a controller is moveable.
	 * 
	 * @return boolean
	 */
	@ControlP5.Layout
	public boolean isMoveable() {
		return isMoveable;
	}

	/**
	 * show or hide the labels of a controller.
	 * 
	 * @param theValue boolean
	 * @return Controller
	 */
	@ControlP5.Layout
	public T setLabelVisible(final boolean theValue) {
		captionLabel.setVisible(theValue);
		return me;
	}

	/**
	 * @return boolean
	 */
	@ControlP5.Layout
	public boolean isLabelVisible() {
		return captionLabel.isVisible();
	}

	/**
	 * Use setBroadcast to enable and disable the broadcasting of changes in a controller's value. By default
	 * any value changes are forwarded to function controlEvent inside your program. use setBroadcast(false)
	 * to disable forwarding.
	 * 
	 * @param theFlag boolean
	 * @return Controller
	 */
	@ControlP5.Layout
	public T setBroadcast(final boolean theFlag) {
		isBroadcast = theFlag;
		return me;
	}

	/**
	 * check if broadcasting is enabled or disabled for a controller. Every event relevant for a value change
	 * will be broadcasted to any of the value-listeners. By default broadcasting for a controller is enabled.
	 * 
	 * @return boolean
	 */
	@ControlP5.Layout
	public boolean isBroadcast() {
		return isBroadcast;
	}

	/**
	 * get the position of a controller. e.g. Controller.getPosition().x; {@inheritDoc}
	 */
	@Override
	@ControlP5.Layout
	public PVector getPosition() {
		return position;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@ControlP5.Layout
	public T setPosition(PVector thePVector) {
		position.x = thePVector.x;
		position.y = thePVector.y;
		position.z = thePVector.z;
		positionBuffer.x = thePVector.x;
		positionBuffer.y = thePVector.y;
		positionBuffer.z = thePVector.z;
		return me;
	}

	/**
	 * set the position of a controller. The position of a controller is relative.
	 * 
	 * @param theX float
	 * @param theY float
	 * @return Controller
	 */
	@Override
	public T setPosition(final float theX, final float theY) {
		position.x = theX;
		position.y = theY;
		positionBuffer.x = position.x;
		positionBuffer.y = position.y;
		return me;
	}

	/**
	 * @return {@link PVector}
	 */
	@Override
	public PVector getAbsolutePosition() {
		// should return a mutable object of absolutePostion in a new PVector
		// object to prevent absolutePosition from being modified by changing
		// its field values. PVector should have getter and setters for x,y,z
		return absolutePosition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@ControlP5.Invisible
	public T setAbsolutePosition(PVector thePVector) {
		// TODO
		// doesnt work properly yet.
		// absolute position should not be changed from the outside anyway.
		absolutePosition.x = thePVector.x;
		absolutePosition.y = thePVector.y;
		absolutePosition.z = thePVector.z;
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@ControlP5.Invisible
	public T updateAbsolutePosition() {
		return me;
	}

	/**
	 * continuousUpdateEvents is used for internal updates of a controller. this method is final and can't be
	 * overridden.
	 * 
	 * @exclude
	 */
	@Override
	@ControlP5.Invisible
	public final void continuousUpdateEvents() {
		if (isBehavior) {
			if (_myBehavior.isActive() && !isMousePressed) {
				_myBehavior.update();
			}
		}
	}

	/**
	 * updateEvents is used for internal updates of a controller. this method is final and can't be
	 * overwritten.
	 */
	@Override
	@ControlP5.Invisible
	public final T updateEvents() {
		if (isInside) {
			boolean moved = ((cp5.mouseDX()) != 0 || (cp5.mouseDY()) != 0);
			if (isMousePressed) {
				if (moved) {
					onDrag();
					isDragged = true;
				}
			} else {
				if (moved && this.equals(window.getFirstFromMouseOverList())) {
					onMove();
				}
			}
		}

		if (isVisible && (isMousePressed == cp5.isMousePressed())) {
			if (isMousePressed && cp5.isAltDown() && isMoveable) {
				if (!cp5.isMoveable()) {
					positionBuffer.x += cp5.mouseDX();
					positionBuffer.y += cp5.mouseDY();
					if (cp5.isShiftDown()) {
						position.x = ((int) (positionBuffer.x) / 10) * 10;
						position.y = ((int) (positionBuffer.y) / 10) * 10;
					} else {
						position.set(positionBuffer);
					}
				}
			} else {
				if (!isLocked) {
					if (isInside) {
						setMouseOver(true);
					}
					if (inside()) {
						if (!isInside) {
							onEnter();
							setIsInside(true);
						}
						setIsInside(true);
					} else {
						if (isInside && !isMousePressed) {
							onLeave();
							setMouseOver(false);
							setIsInside(false);
						}
						if (!isInside && isMouseOver) {

							setMouseOver(false);

							// here the mouseOver is set to false when the mouse is released
							// outside a controller. the mouseoverlist is not updated when
							// the mouse is still pressed but has left the controller - updating the
							// list here currently conflicts with callbacks called from inside
							// setMouseOver()
							//
							// Note: the mouseoverlist is only updated for ControllerGroups when
							// the mouse is pressed but is hovering other controllers while mouse is
							// dragged.
						}
					}
				}
			}
		}
		return me;
	}

	/**
	 * @param theStatus boolean
	 * @return boolean
	 */
	@Override
	@ControlP5.Invisible
	public final boolean setMousePressed(final boolean theStatus) {
		if (!isVisible && !isUserInteraction) {
			return false;
		}
		if (theStatus == true) {
			if (isInside) {
				isMousePressed = true;
				if (!cp5.isAltDown()) {
					mousePressed();
					onPress();
					cp5.getBroadcaster().broadcast(new CEvent(this, CActionType.PRESSED));
				}
				return true;
			}
		} else {
			if (isMousePressed == true && inside()) {
				isMousePressed = false;
				if (!cp5.isAltDown()) {
					if (!isDragged) {
						onClick();
					}
					mouseReleased();
					onRelease();
					isDragged = false;
					cp5.getBroadcaster().broadcast(new CEvent(this, CActionType.RELEASED));
				}

			}
			if (!inside()) {
				setIsInside(false);
				if (isMousePressed) {
					isMousePressed = false;
					mouseReleasedOutside();
					onReleaseOutside();
					cp5.getBroadcaster().broadcast(new CEvent(this, CActionType.RELEASED_OUTSIDE));
				}
				if (this instanceof TextField) {
					mouseReleasedOutside();
					onReleaseOutside();
				}
			}
		}
		return false;
	}

	/**
	 * enables a controller to listen to changes made to the variable linked to the controller. Use true to
	 * enable and false to disable a controller from listening to changes.
	 * 
	 * @param theFlag
	 * @return Controller
	 */
	public T listen(boolean theValue) {
		if (theValue == listening) {
			return me;
		}
		listening = theValue;
		if (listening) {
			cp5.listenTo(name, cp5.getObjectForController(this));
		} else {
			cp5.stopListeningTo(name, cp5.getObjectForController(this));
		}
		return me;
	}

	/**
	 * returns true or false for the current listening status. by default it is set to false
	 * 
	 * @see CElement#listen(boolean)
	 * 
	 * @return boolean
	 */
	public boolean isListening() {
		return listening;
	}

	/**
	 * check if the mouse is within this particular controller.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean isMouseOver() {
		return isMouseOver;
	}

	@Override
	public T setMouseOver(boolean theFlag) {
		if (isMouseOver == theFlag) {
			return me;
		}
		isMouseOver = theFlag;
		if (isMouseOver) {
			window.setMouseOverController(this);
			cp5.getBroadcaster().broadcast(new CEvent(this, CActionType.ENTER));
			cp5.getTooltip().activate(this);
		} else {
			cp5.getBroadcaster().broadcast(new CEvent(this, CActionType.LEAVE));
			window.removeMouseOverFor(this);
			cp5.getTooltip().deactivate();
		}
		return me;
	}

	/**
	 * @see CInterface.updateInternalEvents
	 */
	@Override
	@ControlP5.Invisible
	public T updateInternalEvents(final PApplet theApplet) {
		return me;
	}

	/**
	 * the default draw function for each controller extending superclass Controller. This draw function will
	 * take care of default matrix operations and will call the display function of the current ControllerView
	 * object active for this particular controller.
	 * 
	 * @exclude
	 * @see com.hipposretribution.controlP5.elements.drawable.controllers.ControllerView
	 * @param theApplet PApplet
	 */
	@Override
	@ControlP5.Invisible
	public void draw(final PApplet theApplet) {

		theApplet.pushMatrix();

		theApplet.translate(position.x, position.y);

		if (controllerView != null) {
			controllerView.display(theApplet, me);
		}

		theApplet.popMatrix();

		// theApplet.pushMatrix();
		// _myDebugDisplay.display(theApplet, this);
		// theApplet.popMatrix();
	}

	/**
	 * @param theElement ControllerInterface
	 * @return Controller
	 */
	@Override
	@ControlP5.Invisible
	public T add(final CInterface<?> theElement) {
		ControlP5.logger().warning(" add() not supported.");
		return me;
	}

	/**
	 * @param theElement ControllerInterface
	 * @return Controller
	 */
	@Override
	@ControlP5.Invisible
	public T remove(final CInterface<?> theElement) {
		ControlP5.logger().warning("remove() not supported.");
		return me;
	}

	/**
	 * removes a controller from controlP5.
	 */
	@Override
	public void remove() {
		window.removeMouseOverFor(this);
		if (parent != null) {
			parent.remove(this);
		}
		if (cp5 != null) {
			cp5.remove(this);
		}
	}

	@Override
	public T bringToFront() {
		return bringToFront(this);
	}

	@Override
	public T bringToFront(CInterface<?> theController) {
		if (parent instanceof CTab) {
			moveTo((CTab) parent);
		} else {
			parent.bringToFront(theController);
		}
		if (theController != this) {
			if (getSubelements().contains(theController)) {
				if (theController instanceof CElement<?>) {
					getSubelements().remove(theController);
					getSubelements().add((CElement<?>) theController);
				}
			}
		}
		return me;
	}

	/**
	 * returns the index name of the controller.
	 * 
	 * @return String
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * moves the controller to another tab. The tab is defined by parameter theTabName. if controlP5 can't
	 * find a tab with given name, controlP5 will create this tab and add it to the main window.
	 * 
	 * @param theTabName String
	 * @return Controller
	 */
	public final T moveTo(final String theTabName) {
		setTab(theTabName);
		for (CElement<?> c : getSubelements()) {
			c.moveTo(theTabName);
		}
		return me;
	}

	/**
	 * moves the controller to another tab.
	 * 
	 * @param theTab
	 * @return Controller
	 */
	public final T moveTo(final CTab theTab) {
		setTab(theTab.getWindow(), theTab.getName());
		for (CElement<?> c : getSubelements()) {
			c.moveTo(theTab);
		}
		return me;
	}

	/**
	 * moves the controller to the default tab inside the main window.
	 * 
	 * @param theApplet
	 * @return Controller
	 */
	public final T moveTo(final PApplet theApplet) {
		setTab("default");
		for (CElement<?> c : getSubelements()) {
			c.moveTo(theApplet);
		}
		return me;
	}

	/**
	 * moves the controller to a tab inside the main window.
	 * 
	 * @param theApplet
	 * @param theTabName
	 */
	public final T moveTo(final PApplet theApplet, final String theTabName) {
		setTab(theTabName);
		for (CElement<?> c : getSubelements()) {
			c.moveTo(theApplet, theTabName);
		}
		return me;
	}

	/**
	 * moves the controller to the default tab of a control window - other than the main window.
	 * 
	 * @param theControlWindow
	 */
	public final T moveTo(final ControlWindow theControlWindow) {
		setTab(theControlWindow, "default");
		for (CElement<?> c : getSubelements()) {
			c.moveTo(theControlWindow);
		}
		return me;
	}

	/**
	 * 
	 * @param theControlWindow
	 * @param theTabName
	 * @return Controller
	 */
	public final T moveTo(final ControlWindow theControlWindow, final String theTabName) {
		setTab(theControlWindow, theTabName);
		for (CElement<?> c : getSubelements()) {
			c.moveTo(theControlWindow, theTabName);
		}
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final T moveTo(final CGroup<?> theGroup, final CTab theTab, ControlWindow theControlWindow) {
		for (CElement<?> c : getSubelements()) {
			c.moveTo(theGroup, theTab, theControlWindow);
		}

		if (theGroup != null) {
			setGroup(theGroup);
			return me;
		}

		if (theControlWindow == null) {
			theControlWindow = cp5.getWindow();
		}

		setTab(theControlWindow, theTab.getName());
		return me;
	}

	/**
	 * 
	 * @param theGroup
	 * @return Controller
	 */
	public final T moveTo(final ControlGroup<?> theGroup) {
		if (theGroup != null) {
			setGroup(theGroup);
		}
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final T moveTo(final CGroup<?> theGroup) {
		if (theGroup != null) {
			setGroup(theGroup);
		}
		return me;
	}

	/**
	 * sets the tab of the controller.
	 * 
	 * @param theName String
	 * @return Controller
	 */
	public final T setTab(final String theName) {
		setParent(cp5.getTab(theName));
		for (CElement<?> c : getSubelements()) {
			c.setTab(theName);
		}
		return me;
	}

	/**
	 * 
	 */
	public final T setTab(final ControlWindow theWindow, final String theName) {
		setParent(cp5.getTab(theWindow, theName));
		for (CElement<?> c : getSubelements()) {
			c.setTab(theWindow, theName);
		}
		return me;
	}

	/**
	 * sets the group of the controller.
	 * 
	 * @param theName String
	 * @return Controller
	 */
	public final T setGroup(final String theName) {
		setParent(cp5.getGroup(theName));
		for (CElement<?> c : getSubelements()) {
			c.setGroup(theName);
		}
		return me;
	}

	public final T setGroup(final CGroup<?> theGroup) {
		setParent(theGroup);
		for (CElement<?> c : getSubelements()) {
			c.setGroup(theGroup);
		}
		return me;
	}

	/**
	 * get the instance of the tab the controller belongs to.
	 * 
	 * @return Tab
	 */
	@Override
	public CTab getTab() {
		if (parent instanceof CTab) {
			return (CTab) parent;
		}
		return parent.getTab();
	}

	/**
	 * set the parent of a parent of a controller. this method is only meant for internal use. this method is
	 * final and can't be overwritten.
	 * 
	 * @param theParent ControllerInterface
	 * @return Controller
	 */
	@ControlP5.Invisible
	public final T setParent(final CGroup<?> theParent) {
		if (parent != null) {
			parent.remove(this);
		}
		absolutePosition = new PVector(position.x, position.y);
		if (theParent != null) {
			parent = theParent;
			parent.add(this);
			absolutePosition.add(parent.getPosition());
			window = parent.getWindow();
		}
		setMouseOver(false);
		return me;
	}

	/**
	 * returns the parent of a controller.
	 * 
	 * @return ControllerInterface
	 */

	@ControlP5.Invisible
	@Override
	@ControlP5.Layout
	public CInterface<?> getParent() {
		return parent;
	}

	/**
	 * returns the control window of the controller
	 * 
	 * @return ControlWindow
	 */
	@Override
	public ControlWindow getWindow() {
		return window;
	}

	/**
	 * checks if the mouse is within the area of a controller.
	 * 
	 * @return boolean
	 */
	protected boolean inside() {
		return (cp5.mouseX() > position.x + parent.getAbsolutePosition().x && cp5.mouseX() < position.x + parent.getAbsolutePosition().x + width
				&& cp5.mouseY() > position.y + parent.getAbsolutePosition().y && cp5.mouseY() < position.y + parent.getAbsolutePosition().y + height);
	}

	/**
	 * returns true or false and indicates if the mouse is inside the area of a controller.
	 * 
	 * @return boolean
	 */
	public boolean isInside() {
		return isInside;
	}

	/**
	 * checks if a controller is active.
	 * 
	 * @return boolean
	 */
	@ControlP5.Layout
	public boolean isActive() {
		return isActive;
	}

	/**
	 * returns true or false if the mouse has is pressed.
	 * 
	 * @return boolean
	 */
	public boolean isMousePressed() {
		return isMousePressed;
	}

	protected void onEnter() {
	}

	public void onLeave() {
	}

	protected void onDrag() {
	}

	protected void onMove() {
	}

	protected void onClick() {
	}

	protected void onPress() {
	}

	public void onRelease() {
	}

	public void onScroll(int theAmount) {
	}

	protected void onReleaseOutside() {
	}

	protected void mousePressed() {
	}

	protected void mouseReleased() {
	}

	protected void mouseReleasedOutside() {
	}

	public void setIsInside(boolean theFlag) {
		isInside = theFlag;
	}

	protected boolean getIsInside() {
		return isInside;
	}

	/**
	 * 
	 * @param KeyEvent theEvent
	 */
	@Override
	@ControlP5.Invisible
	public void keyEvent(final KeyEvent theEvent) {
	}

	/**
	 * set the id of a controller.
	 * 
	 * @param int theId
	 * @return Controller
	 */
	@Override
	@ControlP5.Layout
	public T setId(final int theId) {
		_myId = theId;
		return me;
	}

	/**
	 * returns the id of a controller, by default the id is -1. Any int can be given to a controller as its
	 * ID, controlP5 does not recognize duplicates, this has to be managed on the user site.
	 * 
	 * @return int
	 */
	@Override
	@ControlP5.Layout
	public int getId() {
		return _myId;
	}

	protected CPlug getPlug(int theIndex) {
		return plugList.get(theIndex);
	}

	/**
	 * 
	 * @return List<Plug>
	 */
	public ArrayList<CPlug> getPlugs() {
		return plugList;
	}

	public boolean hasPlug(CPlug thePlug) {
		for (CPlug cp : plugList) {
			if (cp.getObject().equals(thePlug.getObject()) && cp.getTargetName().equals(thePlug.getTargetName())) {
				return true;
			}
		}
		return false;
	}

	public T addPlug(final CPlug thePlug) {
		if (!hasPlug(thePlug))
			plugList.add(thePlug);
		return me;
	}

	public T removePlug(final CPlug thePlug) {
		plugList.remove(thePlug);
		return me;
	}

	/**
	 * plugs the controller to a list of objects
	 * 
	 * @param theObject
	 * @return Controller
	 */
	public T plugTo(final Object[] theObjects) {
		for (Object o : theObjects) {
			plugTo(o, name);
		}
		return me;
	}

	/**
	 * 
	 * @param theObjects
	 * @param theName
	 * @return Controller
	 */
	public T plugTo(final Object[] theObjects, String theName) {
		for (Object o : theObjects) {
			plugTo(o, theName);
		}
		return me;
	}

	/**
	 * @param theObject
	 * @return Controller
	 */
	public T plugTo(Object theObject) {
		return plugTo(theObject, name);
	}

	public T plugTo(Object theObject, String theName) {
		if ((theObject instanceof PApplet)) {
			unplugFrom(theObject);
		}
		cp5.getBroadcaster().plug(theObject, this, theName);
		cp5.getBroadcaster().plug(theObject, this, CBroadcaster.EVENT_CALLBACK_METHOD);
		return me;
	}

	/**
	 * unplugs the controller from a list of objects
	 * 
	 * @param theObjects
	 * @return
	 */
	public T unplugFrom(final Object[] theObjects) {
		for (Object o : theObjects) {
			unplugFrom(o);
		}
		return me;
	}

	/**
	 * unplugs the Controller for a single object
	 * 
	 * @param theObject
	 * @return Controller
	 */
	public T unplugFrom(final Object theObject) {
		for (Iterator<CPlug> i = plugList.iterator(); i.hasNext();) {
			if (i.next().getObject().equals(theObject)) {
				i.remove();
			}
		}
		return me;
	}

	/**
	 * @param theValue float
	 */
	@Override
	@ControlP5.Layout
	public T setValue(float theValue) {
		_myValue = theValue;
		broadcast();
		return me;
	}

	/**
	 * @see CElement#getStringValue()
	 * @see CElement#getArrayValue()
	 * @return float
	 */
	@Override
	@ControlP5.Layout
	public float getValue() {
		return _myValue;
	}

	/**
	 * @param theValue
	 * @return Controller
	 */
	@Override
	@ControlP5.Layout
	public T setStringValue(String theValue) {
		_myStringValue = theValue;
		return me;
	}

	/**
	 * @see CElement#getValue()
	 * @see CElement#getArrayValue()
	 * 
	 * @return String
	 */
	@Override
	@ControlP5.Layout
	public String getStringValue() {
		return _myStringValue;
	}

	/**
	 * returns the current float array value of a controller.
	 * 
	 * @see CElement#getValue()
	 * @see CElement#getStringValue()
	 * 
	 * @return float[]
	 */
	@Override
	@ControlP5.Layout
	public float[] getArrayValue() {
		return _myArrayValue;
	}

	/**
	 * @param theIndex
	 * @return float
	 */
	@Override
	public float getArrayValue(int theIndex) {
		if (theIndex >= 0 && theIndex < _myArrayValue.length) {
			return _myArrayValue[theIndex];
		} else {
			return Float.NaN;
		}
	}

	/**
	 * 
	 * @param theArray
	 * @return Controller
	 */
	@Override
	@ControlP5.Layout
	public T setArrayValue(float[] theArray) {
		_myArrayValue = theArray;
		return me;
	}

	/**
	 * 
	 * @param theIndex
	 * @param theValue
	 * @return Controller
	 */
	@Override
	public T setArrayValue(int theIndex, float theValue) {
		if (theIndex >= 0 && theIndex < _myArrayValue.length) {
			_myArrayValue[theIndex] = theValue;
		}
		return me;
	}

	/**
	 * sets the value of the controller without sending the broadcast event. this function is final.
	 * 
	 * @param theValue float
	 * @return Controller
	 */
	public final T changeValue(float theValue) {
		boolean br = this.isBroadcast;
		this.isBroadcast = false;
		setValue(theValue);
		this.isBroadcast = br;
		return me;
	}

	/**
	 * updates the value of the controller without having to set the value explicitly. update does not
	 * visually update the controller. the updating status can be set with setUpdate(true/false) and checked
	 * with isUpdate().
	 * 
	 * @see CElement#setUpdate(boolean)
	 * @see CElement#isUpdate()
	 * @return Controller
	 */
	@Override
	public T update() {
		return me;
	}

	/**
	 * disables the update function for a controller.
	 * 
	 * @see CElement#update()
	 * @see CElement#isUpdate()
	 * 
	 * @param theFlag boolean
	 * @return Controller
	 */
	@Override
	public T setUpdate(final boolean theFlag) {
		isUpdate = theFlag;
		return me;
	}

	/**
	 * enables the update function for a controller.
	 * 
	 * @see CElement#update()
	 * @see CElement#setUpdate(boolean)
	 * 
	 * @return boolean
	 */
	@Override
	public boolean isUpdate() {
		return isUpdate;
	}

	@Override
	@ControlP5.Invisible
	public int getPickingColor() {
		return _myPickingColor;
	}

	/**
	 * sets the content of the caption label of a controller.
	 * 
	 * @param theLabel
	 * @return Controller
	 */
	@Override
	@ControlP5.Layout
	public T setCaptionLabel(final String theLabel) {
		captionLabel.setText(theLabel);
		return me;
	}

	/**
	 * set or change the value of the value label of a controller. (this is cheating, but maybe useful for
	 * some cases.)
	 * 
	 * @param theLabel
	 * @return Controller
	 */
	@ControlP5.Layout
	public T setValueLabel(final String theLabel) {
		_myValueLabel.setText(theLabel);
		return me;
	}

	/**
	 * returns the controller's caption label text.
	 * 
	 * @return String
	 */
	@ControlP5.Invisible
	public String getLabel() {
		return captionLabel.getText();
	}

	/**
	 * @return int
	 */
	public int listenerSize() {
		return listeners.size();
	}

	protected void broadcast() {
		final CEvent myEvent = new CEvent(this, CActionType.BROADCAST);
		for (CListener cl : listeners) {
			cl.controlEvent(myEvent);
		}
		if (isBroadcast && isInit) {
			cp5.getBroadcaster().broadcast(myEvent);
		}
		isInit = true;
	}

	/**
	 * @return boolean
	 */
	@ControlP5.Layout
	@Override
	public boolean isVisible() {
		if (getParent() != null) {
			if (getParent().isVisible() == false) {
				return false;
			}
		}
		return isVisible;
	}

	/**
	 * @param theFlag boolean
	 * @return Controller
	 */
	@ControlP5.Layout
	public T setVisible(final boolean theFlag) {
		isVisible = theFlag;
		if (theFlag == false) {
			isActive = false;
		}
		return me;
	}

	/**
	 * by default controllers use simple shapes, to replace these shapes with images, use setImages(). This
	 * can be handy for buttons, toggles, bangs, for more complex controllers such as sliders, range,
	 * dropdownlist this is not advisable.
	 * 
	 * @param theImageDefault
	 * @param theImageOver
	 * @param theImageActive
	 * @return Controller
	 */
	public T setImages(PImage theImageDefault, PImage theImageOver, PImage theImageActive) {
		setImage(theImageDefault, ControlP5.DEFAULT);
		setImage(theImageOver, ControlP5.OVER);
		setImage(theImageActive, ControlP5.ACTIVE);
		setImage(theImageActive, ControlP5.HIGHLIGHT);
		return me;
	}

	public T setImages(PImage theImageDefault, PImage theImageOver, PImage theImageActive, PImage theImageHighlight) {
		setImage(theImageDefault, ControlP5.DEFAULT);
		setImage(theImageOver, ControlP5.OVER);
		setImage(theImageActive, ControlP5.ACTIVE);
		setImage(theImageHighlight, ControlP5.HIGHLIGHT);
		return me;
	}

	public T setImages(PImage... imgs) {
		if (imgs.length < 3 || imgs.length > 4) {
			return me;
		}
		setImage(imgs[0], ControlP5.DEFAULT);
		setImage(imgs[1], ControlP5.OVER);
		setImage(imgs[2], ControlP5.ACTIVE);
		setImage(imgs.length == 3 ? imgs[2] : imgs[3], ControlP5.HIGHLIGHT);
		return me;
	}

	public T setImage(PImage theImage) {
		return setImage(theImage, ControlP5.DEFAULT);
	}

	/**
	 * @param theImage
	 * @param theState use Controller.DEFAULT (background) Controller.OVER (foreground) Controller.ACTIVE
	 *            (active)
	 */
	public T setImage(PImage theImage, int theState) {
		if (theImage != null) {
			images[theState] = theImage;
			availableImages[theState] = true;
			updateDisplayMode(ControllerViewType.IMAGE);
		}
		return me;
	}

	public T updateSize() {
		if (images[ControlP5.DEFAULT] != null) {
			setSize(images[ControlP5.DEFAULT]);
		}
		return me;
	}

	/**
	 * auto-updates the size of a controller according to the dimensions of the PImage.
	 * 
	 * @param theImage
	 * @return Controller
	 */
	public T setSize(PImage theImage) {
		if (theImage != null) {
			setSize(theImage.width, theImage.height);
		}
		return me;
	}

	/**
	 * @param theWidth
	 * @param theHeight
	 * @return Controller
	 */
	public T setSize(int theWidth, int theHeight) {
		setWidth(theWidth);
		setHeight(theHeight);
		return me;
	}

	protected T updateDisplayMode(ControllerViewType theMode) {
		if (theMode != ControllerViewType.DEFAULT) {
			ControlP5.logger().warning(
				"Image-based or custom displays are not yet implemented for this type of controller. (" + this.getClass().getName() + ")");
		}
		return me;
	}

	/**
	 * use setDisplay to customize your controller look. A new controller-display class required to implement
	 * interface ControllerView. By default the display mode will be set to CUSTOM when setting a new display.
	 * 
	 * @see com.hipposretribution.controlP5.elements.drawable.controllers.ControllerView
	 * @param theDisplay
	 * @return Controller
	 */
	public T setView(ControllerView<T> theDisplay) {
		setView(theDisplay, ControllerViewType.CUSTOM);
		return me;
	}

	public void setView(ControllerView<T> theDisplay, ControllerViewType theMode) {
		displayMode = theMode;
		controllerView = theDisplay;
	}

	/**
	 * @see com.hipposretribution.controlP5.elements.drawable.label.Label
	 * @return Label
	 */
	@ControlP5.Layout
	public Label getCaptionLabel() {
		return captionLabel;
	}

	/**
	 * @return Label
	 */
	@ControlP5.Layout
	public Label getValueLabel() {
		return _myValueLabel;
	}

	/**
	 * returns the maximum value of the controller.
	 * 
	 * @return float
	 */
	@ControlP5.Layout
	public float getMax() {
		return _myMax;
	}

	/**
	 * returns the minimum value of the controller.
	 * 
	 * @return float
	 */
	@ControlP5.Layout
	public float getMin() {
		return _myMin;
	}

	/**
	 * sets the minimum value of the Controller.
	 * 
	 * @param theValue float
	 * @return Controller
	 */
	@ControlP5.Layout
	public T setMin(float theValue) {
		_myMin = theValue;
		changeValue(getValue());
		return me;
	}

	/**
	 * sets the maximum value of the Controller.
	 * 
	 * @param theValue float
	 * @return Controller
	 */
	@ControlP5.Layout
	public T setMax(float theValue) {
		_myMax = theValue;
		changeValue(getValue());
		return me;
	}

	/**
	 * @param theWidth
	 * @return Controller
	 */
	@ControlP5.Layout
	public T setWidth(int theWidth) {
		width = theWidth;
		return me;
	}

	/**
	 * @param theHeight
	 * @return Controller
	 */
	@ControlP5.Layout
	public T setHeight(int theHeight) {
		height = theHeight;
		return me;
	}

	/**
	 * @return int
	 */
	@Override
	@ControlP5.Layout
	public int getWidth() {
		return width;
	}

	/**
	 * @return int
	 */
	@Override
	@ControlP5.Layout
	public int getHeight() {
		return height;
	}

	/**
	 * sets the decimal precision of a controller's float value displayed. the precision does not apply to the
	 * returned float value.
	 * 
	 * @param theValue
	 * @return Controller
	 */
	@ControlP5.Layout
	public T setDecimalPrecision(int theValue) {
		_myDecimalPoints = theValue;
		_myValueLabel.setText("" + adjustValue(getValue()));
		return me;
	}

	/**
	 * @return int
	 */
	@ControlP5.Layout
	public int getDecimalPrecision() {
		return _myDecimalPoints;
	}

	/**
	 * @param theValue float
	 * @return String
	 */
	protected String adjustValue(final float theValue) {
		return adjustValue(theValue, _myDecimalPoints);
	}

	/**
	 * @param theValue
	 * @param theFloatPrecision
	 * @return String
	 */
	protected String adjustValue(final float theValue, final int theFloatPrecision) {

		int myFloatNumberLength = theFloatPrecision + 1;
		if (plugList.size() > 0) {
			for (CPlug cp : plugList) {
				if (cp.getParameterClass() == int.class) {
					myFloatNumberLength = 0;
				}
			}
		}
		String myLabelValue = "" + theValue;
		int myIndex = myLabelValue.indexOf('.');
		if (myIndex > 0) {

			if (theFloatPrecision == 0) {
				myIndex--;
			}
			myLabelValue = myLabelValue.substring(0, Math.min(myLabelValue.length(), myIndex + myFloatNumberLength));

			final int n = (myLabelValue.length() - myIndex);
			if (n < myFloatNumberLength) {
				for (int i = 0; i < myFloatNumberLength - n; i++) {
					myLabelValue += "0";
				}
			}
		} else {
			myLabelValue += ".";
			for (int i = 0; i < myFloatNumberLength; i++) {
				myLabelValue += "0";
			}
		}

		return myLabelValue;
	}

	/**
	 * @return ControlWindow
	 */
	public ControlWindow getControlWindow() {
		return window;
	}

	/**
	 * disables the controller to be moved, or changed or controlled by the user.
	 * 
	 * @return Controller
	 */
	public T lock() {
		isLocked = true;
		return me;
	}

	/**
	 * enables the controller to be moved, changed and controlled by the user.
	 * 
	 * @return Controller
	 */
	public T unlock() {
		isLocked = false;
		return me;
	}

	/**
	 * @return boolean
	 */
	@ControlP5.Layout
	public boolean isLock() {
		return isLocked;
	}

	/**
	 * sets the lock status of the controller
	 * 
	 * @param theValue
	 * @return Controller
	 */
	@ControlP5.Layout
	public T setLock(boolean theValue) {
		isLocked = theValue;
		return me;
	}

	/**
	 * @exclude TODO
	 */
	@ControlP5.Invisible
	public T setUserInteraction(boolean theValue) {
		isUserInteraction = theValue;
		return me;
	}

	/**
	 * @exclude
	 * @return boolean
	 */
	@ControlP5.Invisible
	public boolean isUserInteraction() {
		return isUserInteraction;
	}

	/**
	 * adds a tooltip to a controller, by default the tooltip is disabled. A Tooltip is made visible when
	 * entering a controller with the mouse, when the mouse is moved inside the controller, the tooltip will
	 * hide.
	 * 
	 * @param theText
	 * @return Controller
	 */
	public T registerTooltip(String theText) {
		cp5.getTooltip().register(this, theText);
		return me;
	}

	/**
	 * @see CElement#registerTooltip(String)
	 * @return Controller
	 */
	public T unregisterTooltip() {
		cp5.getTooltip().unregister(this);
		return me;
	}

	protected T setTooltipEnabled(boolean theValue) {
		tooltipEnabled = theValue;
		return me;
	}

	protected boolean isTooltipEnabled() {
		return tooltipEnabled;
	}

	/**
	 * @return Controller
	 */
	public T linebreak() {
		cp5.linebreak(this, true, autoWidth, autoHeight, autoSpacing);
		return me;
	}

	class DebugView implements ControllerView<T> {

		@Override
		public void display(PApplet theApplet, T theController) {
			if (inside()) {
				theApplet.fill(255, 0, 0, 50);
				theApplet.stroke(255, 0, 0);
			} else {
				theApplet.fill(255, 50);
				theApplet.stroke(255);
			}

			theApplet.pushMatrix();
			// theApplet.translate(position.x, position.y);
			theApplet.rect(0, 0, width, height);
			theApplet.popMatrix();
		}
	}

	/**
	 * @exclude {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getName() + " [" + getClass().getSimpleName() + "]";
	}

	/**
	 * @exclude
	 * @return String
	 */
	public String getInfo() {
		return "[ type:\tController" + "\nname:\t" + name + "\n" + "label:\t" + captionLabel.getText() + "\n" + "id:\t" + _myId + "\n" + "value:\t"
				+ getValue() + "\n" + "arrayvalue:\t" + CP.arrayToString(_myArrayValue) + "\n" + "position:\t" + position + "\n" + "absolute:\t"
				+ absolutePosition + "\n" + "width:\t" + getWidth() + "\n" + "height:\t" + getHeight() + "\n" + "color:\t" + getColour() + "\n"
				+ "visible:\t" + isVisible + "\n" + "moveable:\t" + isMoveable + " ]";
	}
}

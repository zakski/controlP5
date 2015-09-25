package com.szadowsz.controlP5.drawable.controller;

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

import com.szadowsz.controlP5.CP;
import com.szadowsz.controlP5.ControlP5;
import com.szadowsz.controlP5.ControlWindow;
import com.szadowsz.controlP5.ControllerList;
import com.szadowsz.controlP5.cast.callback.CEvent;
import com.szadowsz.controlP5.cast.callback.CListener;
import com.szadowsz.controlP5.colour.CColor;
import com.szadowsz.controlP5.drawable.CDrawableInterface;
import com.szadowsz.controlP5.drawable.Canvas;
import com.szadowsz.controlP5.drawable.controller.group.CTab;
import com.szadowsz.controlP5.drawable.controller.lone.press.Button;
import com.szadowsz.controlP5.drawable.label.ControlFont;
import com.szadowsz.controlP5.drawable.label.Label;
import com.szadowsz.controlP5.drawable.label.style.AlignmentX;
import com.szadowsz.controlP5.drawable.label.style.AlignmentY;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.event.KeyEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * ControllerGroup is an abstract class and is extended by class ControlGroup, Tab, or the ListBox.
 * 
 */
public abstract class CGroup<T> implements CInterface<T>, CListener {

	protected PVector position;

	protected PVector positionBuffer;

	public PVector absolutePosition;

	protected ControllerList controllers;

	protected List<CListener> listeners;

	// protected ControlWindow _myControlWindow;

	protected ControlP5 cp5;

	protected CGroup<?> _myParent;

	protected String name;

	protected int _myId = -1;

	public CColor color = new CColor();

	protected boolean isMousePressed = false;

	// only applies to the area of the title bar of a group
	protected boolean isInside = false;

	// applies to the area including controllers, currently only supported for listbox
	protected boolean isInsideGroup = false;

	protected boolean isVisible = true;

	protected boolean isOpen = true;

	protected boolean isBarVisible = true;

	protected boolean isArrowVisible = true;

	protected Button _myCloseButton;

	protected boolean isMoveable = true;

	protected Label _myLabel;

	protected Label _myValueLabel;

	protected int _myWidth = 99;

	protected int _myHeight = 9;

	protected boolean isUpdate;

	protected List<Canvas> _myCanvas;

	protected float _myValue;

	protected String _myStringValue;

	protected float[] _myArrayValue;

	protected boolean isCollapse = true;

	protected int _myPickingColor = 0x6600ffff;

	public final PVector autoPosition = new PVector(10, 30, 0);

	public float tempAutoPositionHeight = 0;

	public float autoPositionOffsetX = 10;

	private String _myAddress = "";

	private boolean mouseover;

	protected final T me;

	/**
	 * Convenience constructor to extend ControllerGroup.
	 * 
	 * @example use/ControlP5extendController
	 * @param theControlP5
	 * @param theName
	 */
	public CGroup(ControlP5 theControlP5, String theName) {
		this(theControlP5, theControlP5.getDefaultTab(), theName, 0, 0);
		theControlP5.register(theControlP5.papplet, theName, this);
	}

	/**
	 * 
	 * @param theControlP5 ControlP5
	 * @param theParent ControllerGroup
	 * @param theName String
	 * @param theX float
	 * @param theY float
	 * @exclude
	 */
	public CGroup(ControlP5 theControlP5, CGroup<?> theParent, String theName, float theX, float theY) {
		position = new PVector(theX, theY, 0);
		cp5 = theControlP5;
		me = (T) this;
		color.set((theParent == null) ? ControlP5.getGlobalColour() : theParent.color);
		name = theName;
		controllers = new ControllerList();
		_myCanvas = new ArrayList<Canvas>();
		listeners = new ArrayList<CListener>();
		_myLabel = new Label(cp5, name);
		_myLabel.setText(name);
		_myLabel.setColor(color.getCaption());
		_myLabel.setAlignment(AlignmentX.LEFT, AlignmentY.TOP);
		setParent((theParent == null) ? this : theParent);
	}

	protected CGroup(int theX, int theY) {
		position = new PVector(theX, theY, 0);
		me = (T) this;
		controllers = new ControllerList();
		_myCanvas = new ArrayList<Canvas>();
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
	 * Method to get the current number of listeners attached to this element
	 */
	@Override
	public int getListenerSize() {
		return listeners.size();
	}

	/**
	 * Method to set the Controller Group's Colours. Cascades down to grouped controllers.
	 * 
	 * @return the Controller Group
	 */
	@Override
	public T setColour(CColor theColor) {
		color.set(theColor);
		for (CInterface<?> ci : controllers.get()) {
			ci.setColour(theColor);
		}
		return me;
	}

	/**
	 * Method to set the Controller Group's Foreground Colour. Cascades down to grouped controllers.
	 * 
	 * @return the Controller Group
	 */
	@Override
	public T setColourForeground(int theColor) {
		color.setForeground(theColor);
		for (CInterface<?> ci : controllers.get()) {
			ci.setColourForeground(theColor);
		}
		return me;
	}

	/**
	 * Method to set the Controller Group's Background Colour. Cascades down to grouped controllers.
	 * 
	 * @return the Controller Group
	 */
	@Override
	public T setColourBackground(int theColor) {
		color.setBackground(theColor);
		for (CInterface<?> ci : controllers.get()) {
			ci.setColourBackground(theColor);
		}
		return me;
	}

	/**
	 * Method to set the Controller Group's Caption Label Colour. Cascades down to grouped controllers.
	 * 
	 * @return the Controller Group
	 */
	@Override
	public T setColourCaption(int theColor) {
		color.setCaption(theColor);
		if (_myLabel != null) {
			_myLabel.setColor(color.getCaption());
		}
		for (CInterface<?> ci : controllers.get()) {
			ci.setColourCaption(theColor);
		}
		return me;
	}

	/**
	 * Method to set the Controller Group's Caption Label Colour. Cascades down to grouped controllers.
	 * 
	 * @return the Controller Group
	 */
	@Override
	public T setBroadcast(boolean theFlag) {
		for (CInterface<?> ci : controllers.get()) {
			ci.setBroadcast(theFlag);
		}
		return me;
	}

	/**
	 * Method to set the Controller Group's Value Label Colour. Cascades down to grouped controllers.
	 * 
	 * @return the Controller Group
	 */
	@Override
	public T setColourValue(int theColor) {
		color.setValue(theColor);
		if (_myValueLabel != null) {
			_myValueLabel.setColor(color.getValue());
		}
		for (CInterface<?> ci : controllers.get()) {
			ci.setColourValue(theColor);
		}
		return me;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hipposretribution.controlP5.elements.colour.CColourInterface#setColourActive(int)
	 */
	@Override
	public T setColourActive(int theColor) {
		color.setActive(theColor);
		for (CInterface<?> ci : controllers.get()) {
			ci.setColourActive(theColor);
		}
		return me;
	}

	/**
	 * @exclude {@inheritDoc}
	 */
	@Override
	@ControlP5.Invisible
	public void init() {
	}

	/**
	 * @exclude {@inheritDoc}
	 */
	@ControlP5.Invisible
	@Override
	public CInterface<?> getParent() {
		return _myParent;
	}

	void setParent(CGroup<?> theParent) {

		if (_myParent != null && _myParent != this) {
			_myParent.remove(this);
		}

		_myParent = theParent;

		if (_myParent != this) {
			_myParent.add(this);
		}

		absolutePosition = new PVector(position.x, position.y);

		absolutePosition.add(_myParent.absolutePosition);

		positionBuffer = new PVector(position.x, position.y);

		if (cp5.getWindow() != null) {
			setMouseOver(false);
		}
	}

	/**
	 * @param theGroup ControllerGroup
	 * @return ControllerGroup
	 */
	public final T setGroup(CGroup<?> theGroup) {
		setParent(theGroup);
		return me;
	}

	/**
	 * @param theName String
	 * @return ControllerGroup
	 */
	public final T setGroup(String theName) {
		setParent(cp5.getGroup(theName));
		return me;
	}

	/**
	 * @param theGroup ControlGroup
	 * @param theTab Tab
	 * @param theControlWindow ControlWindow
	 * @return ControllerGroup
	 */
	@Override
	public final T moveTo(CGroup<?> theGroup, CTab theTab, ControlWindow theControlWindow) {
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

	@Override
	public final T moveTo(CGroup<?> theGroup) {
		moveTo(theGroup, null, null);
		return me;
	}

	public final T moveTo(CTab theTab) {
		moveTo(null, theTab, theTab.getWindow());
		return me;
	}

	public T moveTo(PApplet thePApplet) {
		moveTo(cp5.getWindow());
		return me;
	}

	public T moveTo(ControlWindow theControlWindow) {
		moveTo(null, theControlWindow.getTab("default"), theControlWindow);
		return me;
	}

	public final T moveTo(String theTabName) {
		moveTo(null, cp5.getWindow().getTab(theTabName), cp5.getWindow());
		return me;
	}

	public final T moveTo(String theTabName, ControlWindow theControlWindow) {
		moveTo(null, theControlWindow.getTab(theTabName), theControlWindow);
		return me;
	}

	public final T moveTo(ControlWindow theControlWindow, String theTabName) {
		moveTo(null, theControlWindow.getTab(theTabName), theControlWindow);
		return me;
	}

	public final T moveTo(CTab theTab, ControlWindow theControlWindow) {
		moveTo(null, theTab, theControlWindow);
		return me;
	}

	/**
	 * @param theName String
	 * @return ControllerGroup
	 */
	public final T setTab(String theName) {
		setParent(cp5.getTab(theName));
		return me;
	}

	public final T setTab(ControlWindow theWindow, String theName) {
		setParent(cp5.getTab(theWindow, theName));
		return me;
	}

	/**
	 * @param theTab Tab
	 * @return ControllerGroup
	 */
	public final T setTab(CTab theTab) {
		setParent(theTab);
		return me;
	}

	/**
	 * @return Tab
	 */
	@Override
	public CTab getTab() {
		if (this instanceof CTab) {
			return (CTab) this;
		}
		if (_myParent instanceof CTab) {
			return (CTab) _myParent;
		}
		return _myParent.getTab();
	}

	@Override
	public T setFont(PFont thePFont) {
		return setFont(new ControlFont(thePFont));
	}

	@Override
	public T setFont(ControlFont theControlFont) {
		_myLabel.setFont(theControlFont);
		if (_myValueLabel != null) {
			_myValueLabel.setFont(theControlFont);
		}
		for (int i = 0; i < controllers.size(); i++) {
			if (controllers.get(i) instanceof CElement<?>) {
				((CElement<?>) controllers.get(i)).setFont(theControlFont);
			} else {
				((CGroup<?>) controllers.get(i)).setFont(theControlFont);
			}
		}
		return me;
	}

	@Override
	@ControlP5.Invisible
	public PVector getAbsolutePosition() {
		return new PVector(absolutePosition.x, absolutePosition.y);
	}

	/**
	 * @exclude {@inheritDoc}
	 */
	@Override
	@ControlP5.Invisible
	public T setAbsolutePosition(PVector thePVector) {
		absolutePosition.set(thePVector.x, thePVector.y, thePVector.z);
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PVector getPosition() {
		return new PVector(position.x, position.y);
	}

	/**
	 * set the position of this controller.
	 * 
	 * @param theX float
	 * @param theY float
	 */
	@Override
	public T setPosition(float theX, float theY) {
		position.set((int) theX, (int) theY, 0);
		positionBuffer.set(position);
		updateAbsolutePosition();
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T setPosition(PVector thePVector) {
		setPosition(thePVector.x, thePVector.y);
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T updateAbsolutePosition() {
		absolutePosition.set(position);
		absolutePosition.add(_myParent.getAbsolutePosition());
		for (int i = 0; i < controllers.size(); i++) {
			controllers.get(i).updateAbsolutePosition();
		}
		return me;
	}

	/**
	 * @exclude {@inheritDoc}
	 */
	@Override
	@ControlP5.Invisible
	public void continuousUpdateEvents() {
		if (controllers.size() <= 0) {
			return;
		}
		for (int i = controllers.size() - 1; i >= 0; i--) {
			((CInterface<?>) controllers.get(i)).continuousUpdateEvents();
		}
	}

	/**
	 * @exclude
	 * @return ControllerGroup
	 */
	@Override
	public T update() {
		if (controllers.size() <= 0) {
			return me;
		}
		for (int i = controllers.size() - 1; i >= 0; i--) {
			if (((CInterface<?>) controllers.get(i)).isUpdate()) {
				((CInterface<?>) controllers.get(i)).update();
			}
		}
		return me;
	}

	/**
	 * enables or disables the update function of a controller.
	 * 
	 * @param theFlag boolean
	 * @return ControllerGroup
	 */
	@Override
	public T setUpdate(boolean theFlag) {
		isUpdate = theFlag;
		for (int i = 0; i < controllers.size(); i++) {
			((CInterface<?>) controllers.get(i)).setUpdate(theFlag);
		}
		return me;
	}

	/**
	 * checks the update status of a controller.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean isUpdate() {
		return isUpdate;
	}

	/**
	 * @exclude {@inheritDoc}
	 */
	@Override
	@ControlP5.Invisible
	public T updateEvents() {
		if (isOpen) {
			for (int i = controllers.size() - 1; i >= 0; i--) {
				((CInterface<?>) controllers.get(i)).updateEvents();
			}
		}
		if (isVisible) {
			if ((isMousePressed == cp5.isMousePressed())) {
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
						updateAbsolutePosition();
					}
				} else {
					if (isInside) {
						setMouseOver(true);
					}
					if (inside()) {
						if (!isInside) {
							isInside = true;
							onEnter();
							setMouseOver(true);
						}
					} else {
						if (isInside && !isMousePressed) {
							onLeave();
							isInside = false;
							setMouseOver(false);
						}
					}
				}
			}
		}
		return me;
	}

	/**
	 * @exclude {@inheritDoc}
	 */
	@Override
	@ControlP5.Invisible
	public T updateInternalEvents(PApplet theApplet) {
		return me;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return boolean
	 */
	@Override
	public boolean isMouseOver() {
		mouseover = isInside || isInsideGroup || !isBarVisible;
		return mouseover;
	}

	@Override
	public T setMouseOver(boolean theFlag) {

		mouseover = (!isBarVisible) ? false : theFlag;

		if (!mouseover) {
			isInside = false;
			isInsideGroup = false;
			cp5.getWindow().removeMouseOverFor(this);
			for (int i = controllers.size() - 1; i >= 0; i--) {
				controllers.get(i).setMouseOver(false);
			}
		} else {
			// TODO since inside can be either isInside or isInsideGroup, there are 2 options here,
			// which i am not sure how to handle them yet.
			cp5.getWindow().setMouseOverController(this);
		}
		return me;
	}

	/**
	 * @exclude
	 * @param theApplet PApplet
	 */
	@Override
	@ControlP5.Invisible
	public final void draw(PApplet theApplet) {
		if (isVisible) {
			theApplet.pushMatrix();
			theApplet.translate(position.x, position.y);
			preDraw(theApplet);
			drawControllers(theApplet);
			postDraw(theApplet);
			if (_myValueLabel != null) {
				_myValueLabel.draw(theApplet, 2, 2, this);
			}
			theApplet.popMatrix();
		}
	}

	protected void drawControllers(PApplet theApplet) {
		if (isOpen) {

			for (Canvas cc : _myCanvas) {
				if (cc.mode() == Canvas.PRE) {
					cc.draw(theApplet);
				}
			}
			for (CInterface<?> ci : controllers.get()) {
				if (ci.isVisible()) {
					ci.updateInternalEvents(theApplet);
					ci.draw(theApplet);
				}
			}

			for (CDrawableInterface cd : controllers.getDrawables()) {
				cd.draw(theApplet);
			}

			for (Canvas cc : _myCanvas) {
				if (cc.mode() == Canvas.POST) {
					cc.draw(theApplet);
				}
			}
		}
	}

	protected void preDraw(PApplet theApplet) {
	}

	protected void postDraw(PApplet theApplet) {
	}

	/**
	 * Adds a canvas to a controllerGroup such as a tab or group. Use processing's draw methods to add visual
	 * content.
	 * 
	 * @param theCanvas
	 * @return Canvas
	 */
	public Canvas addCanvas(Canvas theCanvas) {
		_myCanvas.add(theCanvas);
		theCanvas.setup(cp5.papplet);
		return theCanvas;
	}

	/**
	 * Removes a canvas from a controller group.
	 * 
	 * @param theCanvas
	 * @return ControllerGroup
	 */
	public T removeCanvas(Canvas theCanvas) {
		_myCanvas.remove(theCanvas);
		return me;
	}

	/**
	 * Adds a controller to the group, but use Controller.setGroup() instead.
	 * 
	 * @param theElement ControllerInterface
	 * @return ControllerGroup
	 */
	@Override
	public T add(CInterface<?> theElement) {
		controllers.add(theElement);
		return me;
	}

	@Override
	public T bringToFront() {
		return bringToFront(this);
	}

	@Override
	public T bringToFront(CInterface<?> theController) {
		if (_myParent instanceof CTab) {
			moveTo((CTab) _myParent);
		} else {
			_myParent.bringToFront(theController);
		}
		if (theController != this) {
			if (controllers.get().contains(theController)) {
				controllers.remove(theController);
				controllers.add(theController);
			}
		}
		return me;
	}

	/**
	 * Removes a controller from the group, but use Controller.setGroup() instead.
	 * 
	 * @param theElement ControllerInterface
	 * @return ControllerGroup
	 */

	@Override
	public T remove(CInterface<?> theElement) {
		if (theElement != null) {
			theElement.setMouseOver(false);
		}
		controllers.remove(theElement);
		return me;
	}

	/**
	 * @param theElement CDrawable
	 * @return ControllerGroup
	 */
	@ControlP5.Invisible
	public T addDrawable(CDrawableInterface theElement) {
		controllers.addDrawable(theElement);
		return me;
	}

	/**
	 * @param theElement CDrawable
	 * @return ControllerGroup
	 */
	public T remove(CDrawableInterface theElement) {
		controllers.removeDrawable(theElement);
		return me;
	}

	/**
	 * removes the group from controlP5.
	 */
	@Override
	public void remove() {
		cp5.getWindow().removeMouseOverFor(this);
		if (_myParent != null) {
			_myParent.remove(this);
		}
		if (cp5 != null) {
			cp5.remove(this);
		}

		for (int i = controllers.size() - 1; i >= 0; i--) {
			controllers.get(i).remove();
		}
		controllers.clear();
		controllers.clearDrawable();
		controllers = new ControllerList();
		if (this instanceof CTab) {
			cp5.getWindow().removeTab((CTab) this);
		}
	}

	/**
	 * @return String
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAddress() {
		return _myAddress;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T setAddress(String theAddress) {
		if (_myAddress.length() == 0) {
			_myAddress = theAddress;
		}
		return me;
	}

	/**
	 * @return ControlWindow
	 */
	@Override
	public ControlWindow getWindow() {
		return cp5.getWindow();
	}

	/**
	 * @exclude
	 * @param theEvent KeyEvent
	 */
	@Override
	@ControlP5.Invisible
	public void keyEvent(KeyEvent theEvent) {
		for (int i = 0; i < controllers.size(); i++) {
			((CInterface<?>) controllers.get(i)).keyEvent(theEvent);
		}
	}

	/**
	 * @exclude
	 * @param theStatus boolean
	 * @return boolean
	 */
	@Override
	public boolean setMousePressed(boolean theStatus) {
		if (!isVisible) {
			return false;
		}

		if (name == "fire_sequence_menu" && !theStatus) {
			System.out.println("test");
		}

		if (isOpen == true) {
			for (int i = controllers.size() - 1; i >= 0; i--) {
				if (((CInterface<?>) controllers.get(i)).setMousePressed(theStatus)) {
					return true;
				}

			}
		}

		if (theStatus == true) {
			if (isInside) {
				isMousePressed = true;
				mousePressed();
				return true;
			}
		} else {
			if (isMousePressed == true) {
				isMousePressed = false;
				mouseReleased();
			}
		}
		return false;
	}

	protected void mousePressed() {
	}

	public void mouseReleased() {
	}

	protected void onEnter() {
	}

	protected void onLeave() {
	}

	public void onScroll(int theAmount) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T setId(int theId) {
		_myId = theId;
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getId() {
		return _myId;
	}

	/**
	 * @param theLabel String
	 * @return ControllerGroup
	 */
	public T setLabel(String theLabel) {
		_myLabel.setText(theLabel);
		return me;
	}

	/**
	 * @return boolean
	 */
	@Override
	public boolean isVisible() {
		if (_myParent != null && _myParent != this) {
			if (getParent().isVisible() == false) {
				return false;
			}
		}
		return isVisible;
	}

	/**
	 * @param theFlag boolean
	 * @return ControllerGroup
	 */
	public T setVisible(boolean theFlag) {
		isVisible = theFlag;
		return me;
	}

	/**
	 * set the moveable status of the group, when false, the group can't be moved.
	 * 
	 * @param theFlag boolean
	 * @return ControllerGroup
	 */
	public T setMoveable(boolean theFlag) {
		isMoveable = theFlag;
		return me;
	}

	public boolean isMoveable() {
		return isMoveable;
	}

	public T setOpen(boolean theFlag) {
		isOpen = theFlag;
		return me;
	}

	/**
	 * @return boolean
	 */
	public boolean isOpen() {
		return isOpen;
	}

	public T open() {
		setOpen(true);
		return me;
	}

	public T close() {
		setOpen(false);
		return me;
	}

	/**
	 * TODO redesign or deprecate remove the close button.
	 */
	@ControlP5.Invisible
	public T removeCloseButton() {
		if (_myCloseButton == null) {
			_myCloseButton.remove();
		}
		_myCloseButton = null;
		return me;
	}

	public T setTitle(String theTitle) {
		getCaptionLabel().setText(theTitle);
		return me;
	}

	public T hideBar() {
		isBarVisible = false;
		return me;
	}

	public T showBar() {
		isBarVisible = true;
		return me;
	}

	/**
	 * @return boolean
	 */
	public boolean isBarVisible() {
		return isBarVisible;
	}

	/**
	 * @return ControlGroup
	 */
	public T hideArrow() {
		isArrowVisible = false;
		return me;
	}

	/**
	 * @return ControlGroup
	 */
	public T showArrow() {
		isArrowVisible = true;
		return me;
	}

	/**
	 * TODO redesign or deprecate add a close button to the controlbar of this controlGroup.
	 */
	@ControlP5.Invisible
	public T addCloseButton() {
		if (_myCloseButton == null) {
			_myCloseButton = new Button(cp5, this, getName() + "close", 1, _myWidth + 1, -10, 12, 9);
			_myCloseButton.setCaptionLabel("X");
			_myCloseButton.addListener(this);
		}
		return me;
	}

	/**
	 * @exclude {@inheritDoc}
	 */
	@Override
	@ControlP5.Invisible
	public int getPickingColor() {
		return _myPickingColor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CColor getColour() {
		return color;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T setValue(float theValue) {
		_myValue = theValue;
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getValue() {
		return _myValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStringValue() {
		return _myStringValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T setStringValue(String theValue) {
		_myStringValue = theValue;
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
	 * @param theArray
	 * @return ControllerGroup
	 */
	@Override
	public T setArrayValue(float[] theArray) {
		_myArrayValue = theArray;
		return me;
	}

	public CElement<?> getController(String theController) {
		return cp5.getController(theController);
	}

	@Override
	public T setCaptionLabel(String theValue) {
		getCaptionLabel().setText(theValue);
		return me;
	}

	public Label getCaptionLabel() {
		return _myLabel;
	}

	public Label getValueLabel() {
		return _myValueLabel;
	}

	/**
	 * @return ControllerGroup
	 */
	public T enableCollapse() {
		isCollapse = true;
		return me;
	}

	/**
	 * @return ControllerGroup
	 */
	public T disableCollapse() {
		isCollapse = false;
		return me;
	}

	public boolean isCollapse() {
		return isCollapse;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getWidth() {
		return _myWidth;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHeight() {
		return _myHeight;
	}

	/**
	 * @param theWidth
	 * @return ControllerGroup
	 */
	public T setWidth(int theWidth) {
		_myWidth = theWidth;
		return me;
	}

	/**
	 * @param theHeight
	 * @return ControllerGroup
	 */
	public T setHeight(int theHeight) {
		_myHeight = theHeight;
		return me;
	}

	public T setSize(int theWidth, int theHeight) {
		setWidth(theWidth);
		// setHeight(theHeight) will set the Height of the bar therefore will not be used here.
		return me;
	}

	protected boolean inside() {
		return (cp5.mouseX() > position.x + _myParent.absolutePosition.x && cp5.mouseX() < position.x + _myParent.absolutePosition.x + _myWidth
				&& cp5.mouseY() > position.y + _myParent.absolutePosition.y - _myHeight && cp5.mouseY() < position.y + _myParent.absolutePosition.y);
	}

	@Override
	public void controlEvent(CEvent theEvent) {
	}

	@Override
	public String toString() {
		return getName() + " [" + getClass().getSimpleName() + "]";
	}

	public String getInfo() {
		return "type:\tControllerGroup" + "\nname:\t" + name + "\n" + "label:\t" + _myLabel.getText() + "\n" + "id:\t" + _myId + "\n" + "value:\t"
				+ _myValue + "\n" + "arrayvalue:\t" + CP.arrayToString(_myArrayValue) + "\n" + "position:\t" + position + "\n" + "absolute:\t"
				+ absolutePosition + "\n" + "width:\t" + getWidth() + "\n" + "height:\t" + getHeight() + "\n" + "color:\t" + getColour() + "\n"
				+ "visible:\t" + isVisible + "\n" + "moveable:\t" + isMoveable + "\n";
	}
}

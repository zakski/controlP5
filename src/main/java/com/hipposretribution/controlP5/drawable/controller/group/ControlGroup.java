package com.hipposretribution.controlP5.drawable.controller.group;

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

import processing.core.PApplet;

import com.hipposretribution.controlP5.ControlP5;
import com.hipposretribution.controlP5.cast.callback.CEvent;
import com.hipposretribution.controlP5.cast.callback.CListener;
import com.hipposretribution.controlP5.drawable.controller.CGroup;
import com.hipposretribution.controlP5.drawable.label.Label;

/**
 * <p>
 * In previous versions you would use the ControlGroup class to bundle controllers in a group. Now please use the Group class to do so.
 * </p>
 * <p>
 * ControlGroup extends ControllerGroup, for a list and documentation of available methods see the {@link CGroup} documentation.
 * </p>
 * 
 * @see com.hipposretribution.controlP5.drawable.controller.group.Group
 * @example controllers/ControlP5group
 */
public class ControlGroup<T> extends CGroup<T> {

	protected int _myBackgroundHeight = 0;

	protected int _myBackgroundColor = 0x00ffffff;

	protected boolean isEventActive = false;

	/**
	 * Convenience constructor to extend ControlGroup.
	 * 
	 * @example use/ControlP5extendController
	 * @param theControlP5
	 * @param theName
	 */
	public ControlGroup(ControlP5 theControlP5, String theName) {
		this(theControlP5, theControlP5.getDefaultTab(), theName, 0, 0, 100, 9);
		theControlP5.register(theControlP5.papplet, theName, this);
	}

	public ControlGroup(ControlP5 theControlP5, CGroup<?> theParent, String theName, int theX, int theY, int theW, int theH) {
		super(theControlP5, theParent, theName, theX, theY);
		_myValueLabel = new Label(cp5, "");
		_myWidth = theW;
		_myHeight = theH;
	}

	@Override
	@ControlP5.Invisible
	public void mousePressed() {
		if (isBarVisible && isCollapse) {
			if (!cp5.isAltDown()) {
				isOpen = !isOpen;
				if (isEventActive) {
					final CEvent myEvent = new CEvent(this, CActionType.BROADCAST);
					cp5.getBroadcaster().broadcast(myEvent);
					for (CListener cl : listeners) {
						cl.controlEvent(myEvent);
					}
				}
			}
		}
	}

	/**
	 * activates or deactivates the Event status of a ControlGroup.
	 * 
	 * @see CTab
	 * @param theFlag boolean
	 */
	public T activateEvent(boolean theFlag) {
		isEventActive = theFlag;
		return me;
	}

	@Override
	public T setSize(int theWidth, int theHeight) {
		super.setSize(theWidth, theHeight);
		setBackgroundHeight(theHeight);
		return me;
	}

	/**
	 * get the height of the controlGroup's background.
	 * 
	 * @return
	 */
	public int getBackgroundHeight() {
		return _myBackgroundHeight;
	}

	/**
	 * set the height of the controlGroup's background.
	 * 
	 * @param theHeight
	 * @return ControlGroup
	 */
	public T setBackgroundHeight(int theHeight) {
		_myBackgroundHeight = theHeight;
		return me;
	}

	/**
	 * set the background color of a controlGroup.
	 * 
	 * @param theColor
	 * @return ControlGroup
	 */
	public T setBackgroundColor(int theColor) {
		_myBackgroundColor = theColor;
		return me;
	}

	/**
	 * set the height of the top bar (used to open/close and move a controlGroup).
	 * 
	 * @param theHeight
	 * @return ControlGroup
	 */
	public T setBarHeight(int theHeight) {
		_myHeight = theHeight;
		return me;
	}

	/**
	 * @return int
	 */
	public int getBarHeight() {
		return _myHeight;
	}

	@Override
	public T updateInternalEvents(PApplet theApplet) {
		if (isInside && isBarVisible) {
			cp5.getWindow().setMouseOverController(this);
		}
		return me;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see controlP5.ControllerGroup#preDraw(processing.core.PApplet)
	 */
	@Override
	protected void preDraw(PApplet theApplet) {
		if (isOpen) {
			theApplet.fill(_myBackgroundColor);
			theApplet.rect(0, 0, _myWidth, _myBackgroundHeight - 1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see controlP5.ControllerGroup#postDraw(processing.core.PApplet)
	 */
	@Override
	protected void postDraw(PApplet theApplet) {
		if (isBarVisible) {
			theApplet.fill(isInside ? color.getForeground() : color.getBackground());
			theApplet.rect(0, -1, _myWidth, -_myHeight);
			_myLabel.draw(theApplet, 0, -_myHeight - 1, this);
			if (isCollapse && isArrowVisible) {
				theApplet.fill(_myLabel.getColor());
				theApplet.pushMatrix();
				theApplet.translate(2, 0);
				if (isOpen) {
					theApplet.triangle(_myWidth - 10, -_myHeight / 2 - 3, _myWidth - 4, -_myHeight / 2 - 3, _myWidth - 7, -_myHeight / 2);
				} else {
					theApplet.triangle(_myWidth - 10, -_myHeight / 2, _myWidth - 4, -_myHeight / 2, _myWidth - 7, -_myHeight / 2 - 3);
				}
				theApplet.popMatrix();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see controlP5.ControlListener#controlEvent(controlP5.ControlEvent)
	 */
	@Override
	@ControlP5.Invisible
	public void controlEvent(CEvent theEvent) {
		if (theEvent.getController().getName().equals(getName() + "close")) {
			setVisible(false);
		}
	}

	/**
	 * !!! experimental, see ControllerGroup.value()
	 * 
	 * 
	 * @return String
	 */
	@ControlP5.Invisible
	public String stringValue() {
		return Float.toString(_myValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getInfo() {
		return "type:\tControlGroup\n" + super.getInfo();
	}
}

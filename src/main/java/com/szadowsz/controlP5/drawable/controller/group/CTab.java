package com.szadowsz.controlP5.drawable.controller.group;

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

import com.szadowsz.controlP5.ControlP5;
import com.szadowsz.controlP5.ControlWindow;
import com.szadowsz.controlP5.cast.callback.CEvent;
import com.szadowsz.controlP5.drawable.controller.CGroup;
import com.szadowsz.controlP5.drawable.label.style.AlignmentX;
import com.szadowsz.controlP5.drawable.label.style.AlignmentY;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Tabs are used to organize controllers. Tabs are arranged horizontally from the top-left corner by default,
 * Tab extends ControllerGroup, for more available methods see the ControllerGroup documentation. Reposition
 * tabs with {@link ControlWindow#setPositionOfTabs(int, int)}
 * 
 * @example controllers/ControlP5tab
 * @nosuperclasses ControllerGroup ControllerGroup
 */
public class CTab extends CGroup<CTab> {

	protected int _myOffsetX = -1000;

	protected int _myOffsetY = -1000;

	public boolean isActive = false;

	private boolean isAlwaysActive = false;

	protected boolean isEventActive = false;

	protected float _myValue = 0;

	protected String _myStringValue = "";

	public static int padding = 4;

	public boolean autoWidth = true;

	/**
	 * 
	 * @param theControlP5 ControlP5
	 * @param theControlWindow ControlWindow
	 * @param theName String
	 */
	public CTab(ControlP5 theControlP5, ControlWindow theControlWindow, String theName) {
		super(theControlP5, null, theName, 0, 0);
		position = new PVector();
		absolutePosition = new PVector();
		isMoveable = false;
		isEventActive = theControlP5.isTabEventsActive;
		_myHeight = 16;
		_myWidth = _myLabel.getWidth() + padding * 2;
		_myLabel.setAlignment(AlignmentX.LEFT, AlignmentY.CENTER).setPadding(0, 0);
	}

	public void setOffset(int theValueX, int theValueY) {
		_myOffsetX = theValueX;
		_myOffsetY = theValueY;
	}

	public int height() {
		return _myHeight;
	}

	public boolean updateLabel() {
		isInside = inside();
		return cp5.getWindow().getTabs().size() > 2;
	}

	public void drawLabel(PApplet theApplet) {
		if (autoWidth) {
			_myWidth = _myLabel.getWidth() + padding * 2;
		}
		theApplet.pushMatrix();
		theApplet.fill(isInside ? color.getForeground() : color.getBackground());
		if (isActive) {
			theApplet.fill(color.getActive());
		}
		theApplet.translate(_myOffsetX, _myOffsetY);
		theApplet.rect(0, 0, _myWidth - 1, _myHeight);
		_myLabel.draw(theApplet, padding, 0, this);
		theApplet.popMatrix();
	}

	/**
	 * set the label of the group. TODO overwriting COntrollerGroup.setLabel to set the Width of a tab after
	 * renaming. this should be temporary and fixed in the future.
	 * 
	 * @param theLabel String
	 * @return Tab
	 */
	@Override
	public CTab setLabel(String theLabel) {
		_myLabel.setText(theLabel);
		return this;
	}

	public int width() {
		return _myWidth;
	}

	/**
	 * @param theWidth
	 * @return
	 */
	@Override
	public CTab setWidth(int theWidth) {
		_myWidth = theWidth + padding;
		autoWidth = false;
		return this;
	}

	@Override
	public CTab setHeight(int theHeight) {
		_myHeight = theHeight;
		return this;
	}

	@Override
	protected boolean inside() {
		return (cp5.mouseX()> _myOffsetX && cp5.mouseX() < _myOffsetX + _myWidth
				&& cp5.mouseY() > _myOffsetY && cp5.mouseY() < _myOffsetY + _myHeight);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@ControlP5.Invisible
	public void mousePressed() {
		cp5.getWindow().activateTab(this);
		if (isEventActive) {
			cp5.getBroadcaster().broadcast(new CEvent(this, CActionType.BROADCAST));
		}
	}

	/**
	 * Activates a tab.
	 * 
	 * @param theFlag boolean
	 */
	public CTab setActive(boolean theFlag) {
		isActive = theFlag;
		setOpen(theFlag);
		return this;
	}

	public CTab setAlwaysActive(boolean theFlag) {
		isAlwaysActive = theFlag;
		return this;
	}

	/**
	 * checks if a tab is active.
	 * 
	 * @return boolean
	 */
	public boolean isActive() {
		return isAlwaysActive ? true : isActive;
	}

	public boolean isAlwaysActive() {
		return isAlwaysActive;
	}

	@Override
	public CTab bringToFront() {
		cp5.getWindow().activateTab(this);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CTab moveTo(ControlWindow theWindow) {
		cp5.getWindow().removeTab(this);
		setTab(theWindow, getName());
		return this;
	}

	/**
	 * activates or deactivates the Event status of a tab, When activated a tab will send a controlEvent to
	 * the main application. By default this is disabled.
	 * 
	 * @param theFlag boolean
	 * @return Tab
	 */
	public CTab activateEvent(boolean theFlag) {
		isEventActive = theFlag;
		return this;
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
	public float getValue() {
		return _myValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CTab setValue(float theValue) {
		_myValue = theValue;
		return this;
	}
}

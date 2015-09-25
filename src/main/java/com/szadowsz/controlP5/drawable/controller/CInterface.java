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

import com.szadowsz.controlP5.ControlP5;
import com.szadowsz.controlP5.ControlWindow;
import com.szadowsz.controlP5.cast.callback.CListenToInterface;
import com.szadowsz.controlP5.colour.CColourInterface;
import com.szadowsz.controlP5.drawable.CDrawableInterface;
import com.szadowsz.controlP5.drawable.controller.group.CTab;
import com.szadowsz.controlP5.drawable.label.ControlFont;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.event.KeyEvent;

/**
 * 
 * The ControllerInterface is inherited by all ControllerGroup and Controller classes.
 * 
 */
public interface CInterface<T> extends CDrawableInterface<T>, CColourInterface<T>, CListenToInterface<T> {

	@ControlP5.Invisible
	public void init();

	public T setValue(float theValue);

	public float getValue();

	public T setStringValue(String theValue);

	public String getStringValue();

	public float[] getArrayValue();

	public float getArrayValue(int theIndex);

	public T setArrayValue(int theIndex, float theValue);

	public T setArrayValue(float[] theArray);

	public int getId();

	public PVector getPosition();

	@ControlP5.Invisible
	public T setPosition(float theX, float theY);

	@ControlP5.Invisible
	public T setPosition(PVector thePVector);

	public PVector getAbsolutePosition();

	public T setAbsolutePosition(PVector thePVector);

	public T updateAbsolutePosition();

	public CInterface<?> getParent();

	public T update();

	public T setUpdate(boolean theFlag);

	public T bringToFront();

	public T bringToFront(CInterface<?> theController);

	public boolean isUpdate();

	@ControlP5.Invisible
	public T updateEvents();

	@ControlP5.Invisible
	public void continuousUpdateEvents();

	/**
	 * a method for putting input events like e.g. mouse or keyboard events and queries. this has been taken
	 * out of the draw method for better overwriting capability.
	 * 
	 * 
	 */
	@ControlP5.Invisible
	public T updateInternalEvents(PApplet theApplet);

	public T setFont(ControlFont theControlFont);

	public T setFont(PFont thePFont);

	@ControlP5.Invisible
	public void draw(PApplet theApplet);

	public T add(CInterface<?> theElement);

	public T remove(CInterface<?> theElement);

	public void remove();

	public String getName();

	public String getAddress();

	public ControlWindow getWindow();

	public CTab getTab();

	public boolean setMousePressed(boolean theStatus);

	@ControlP5.Invisible
	public void keyEvent(KeyEvent theEvent);

	@ControlP5.Invisible
	public T setAddress(String theAddress);

	public T setId(int theValue);

	public T moveTo(CGroup<?> theGroup, CTab theTab, ControlWindow theWindow);

	public T moveTo(CGroup<?> theGroup);

	@ControlP5.Invisible
	public int getPickingColor();

	public boolean isMouseOver();

	public T setMouseOver(boolean theFlag);

	public T setCaptionLabel(String theValue);
	
	public T setBroadcast(final boolean theFlag);

}

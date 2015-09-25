package com.szadowsz.controlP5.drawable.controller.lone.press;

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
import com.szadowsz.controlP5.drawable.controller.CElement;
import com.szadowsz.controlP5.drawable.controller.CGroup;
import com.szadowsz.controlP5.drawable.controller.group.CTab;
import com.szadowsz.controlP5.drawable.controller.view.ControllerView;
import com.szadowsz.controlP5.drawable.controller.view.ControllerViewType;
import com.szadowsz.controlP5.drawable.label.style.AlignmentX;
import com.szadowsz.controlP5.drawable.label.style.AlignmentY;
import processing.core.PApplet;

/**
 * <p>
 * A button triggers an event after it has been release. Events can be linked to functions and fields inside
 * your program/sketch. for a full documentation of this controller see the {@link CElement} class.
 * </p>
 * 
 * @example controllers/ControlP5button
 */

public class Button extends CElement<Button> {

	protected boolean isPressed;

	protected boolean isOn = false;

	public static int autoWidth = 69;

	public static int autoHeight = 19;

	protected int activateBy = ControlP5.RELEASE;

	protected boolean isSwitch = false;

	/**
	 * Convenience constructor to extend Button.
	 * 
	 * @example use/ControlP5extendController
	 * @param theControlP5
	 * @param theName
	 */
	public Button(ControlP5 theControlP5, String theName) {
		this(theControlP5, theControlP5.getDefaultTab(), theName, 0, 0, 0, autoWidth, autoHeight);
		theControlP5.register(theControlP5.papplet, theName, this);
	}

	public Button(ControlP5 theControlP5, CGroup<?> theParent, String theName, float theDefaultValue,
			int theX, int theY, int theWidth, int theHeight) {
		super(theControlP5, theParent, theName, theX, theY, theWidth, theHeight);
		_myValue = theDefaultValue;
		captionLabel.setAlignment(AlignmentX.LEFT, AlignmentY.CENTER);
	}

	/**
	 * @exclude
	 */
	public Button() {
		super(null, null, null, 0, 0, 1, 1);
	}

	@Override
	protected void onEnter() {
		isActive = true;
	}

	@Override
	public void onLeave() {
		isActive = false;
	}

	/**
	 * @exclude
	 */
	@Override
	@ControlP5.Invisible
	public void mousePressed() {
		isActive = getIsInside();
		isPressed = true;
		if (activateBy == ControlP5.PRESSED) {
			activate();
		}
	}

	/**
	 * @exclude
	 */
	@Override
	@ControlP5.Invisible
	public void mouseReleased() {
		isPressed = false;
		if (activateBy == ControlP5.RELEASE) {
			activate();
		}
		isActive = false;
	}

	/**
	 * A button can be activated by a mouse PRESSED or mouse RELEASE. Default value is RELEASE.
	 * 
	 * @param theValue use ControlP5.PRESSED or ControlP5.RELEASE as parameter
	 * @return Button
	 */
	public Button activateBy(int theValue) {
		if (theValue == ControlP5.PRESSED) {
			activateBy = ControlP5.PRESSED;
		} else {
			activateBy = ControlP5.RELEASE;
		}
		return this;
	}

	protected void activate() {
		if (isActive) {
			isActive = false;
			if (getParent() instanceof CTab) {
				setIsInside(false);
			}
			isOn = !isOn;
			setValue(_myValue);

		}
	}

	/**
	 * @exclude
	 */
	@Override
	@ControlP5.Invisible
	public void mouseReleasedOutside() {
		mouseReleased();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Button setValue(float theValue) {
		_myValue = theValue;
		broadcast();
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Button update() {
		return setValue(_myValue);
	}

	/**
	 * Turns a button into a switch, or use a Toggle instead.
	 * 
	 * @see Toggle
	 * @param theFlag turns the button into a switch when true
	 * @return Button
	 */
	public Button setSwitch(boolean theFlag) {
		isSwitch = theFlag;
		return this;
	}

	/**
	 * If the button acts as a switch, setOn will turn on the switch. Use
	 * {@link Button#setSwitch(boolean) setSwitch}
	 * to turn a Button into a Switch.
	 * 
	 * @return Button
	 */
	public Button setOn() {
		if (isSwitch) {
			isOn = false;
			isActive = true;
			activate();
		}
		return this;
	}

	/**
	 * If the button acts as a switch, setOff will turn off the switch. Use
	 * {@link Button#setSwitch(boolean) setSwitch}
	 * to turn a Button into a Switch.
	 * 
	 * @return Button
	 */
	public Button setOff() {
		if (isSwitch) {
			isOn = true;
			isActive = true;
			activate();
		}
		return this;
	}

	/**
	 * @return boolean
	 */
	public boolean isOn() {
		return isOn;
	}

	/**
	 * @return boolean
	 */
	public boolean isPressed() {
		return isPressed;
	}

	/**
	 * Returns true or false and indicates the switch state of the button. {@link setSwitch(boolean)
	 * setSwitch} should have been set before.
	 * 
	 * @see Button#setSwitch(boolean)
	 * @return boolean
	 */
	public boolean getBooleanValue() {
		return isOn;
	}

	/**
	 * @exclude
	 */
	@ControlP5.Invisible
	public Button updateDisplayMode(ControllerViewType theMode) {
		displayMode = theMode;
		switch (theMode) {
		case DEFAULT:
			controllerView = new ButtonView();
			break;
		case IMAGE:
			controllerView = new ButtonImageView();
			break;
		case CUSTOM:
		default:
			break;

		}
		return this;
	}

	private class ButtonView implements ControllerView<Button> {

		@Override
		public void display(PApplet theApplet, Button theController) {
			theApplet.noStroke();
			if (isOn && isSwitch) {
				theApplet.fill(color.getActive());
			} else {
				if (getIsInside()) {
					if (isPressed) {
						theApplet.fill(color.getActive());
					} else {
						theApplet.fill(color.getForeground());
					}
				} else {
					theApplet.fill(color.getBackground());
				}
			}
			theApplet.rect(0, 0, width, height);
			if (isLabelVisible()) {
				captionLabel.draw(theApplet, 0, 0, theController);
			}
		}
	}

	private class ButtonImageView implements ControllerView<Button> {

		@Override
		public void display(PApplet theApplet, Button theController) {
			if (isOn && isSwitch) {
				theApplet.image((availableImages[ControlP5.HIGHLIGHT] == true) ? images[ControlP5.HIGHLIGHT]
						: images[ControlP5.DEFAULT], 0, 0);
				return;
			}
			if (getIsInside()) {
				if (isPressed) {
					theApplet.image((availableImages[ControlP5.ACTIVE] == true) ? images[ControlP5.ACTIVE]
							: images[ControlP5.DEFAULT], 0, 0);
				} else {
					theApplet.image((availableImages[ControlP5.OVER] == true) ? images[ControlP5.OVER]
							: images[ControlP5.DEFAULT], 0, 0);
				}
			} else {
				theApplet.image(images[ControlP5.DEFAULT], 0, 0);
			}
		}
	}

	/**
	 * @exclude
	 */
	@Override
	public String getInfo() {
		return "type:\tButton\n" + super.getInfo();
	}

	/**
	 * @exclude
	 */
	@Override
	public String toString() {
		return super.toString() + " [ " + getValue() + " ] " + "Button" + " (" + this.getClass().getSuperclass() + ")";
	}
}

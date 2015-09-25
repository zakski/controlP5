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
import com.szadowsz.controlP5.drawable.controller.group.CTab;
import com.szadowsz.controlP5.drawable.controller.view.ControllerView;
import com.szadowsz.controlP5.drawable.controller.view.ControllerViewType;
import com.szadowsz.controlP5.drawable.label.style.AlignmentX;
import com.szadowsz.controlP5.drawable.label.style.AlignmentY;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * a toggle can have two states, true and false, where true has the value 1 and false is 0.
 * 
 * @example controllers/ControlP5toggle
 * @nosuperclasses Controller Controller
 */
public class Toggle extends CElement<Toggle> {

	protected int cnt;

	protected boolean isOn = false;

	protected float internalValue = -1;

	public static int autoWidth = 39;

	public static int autoHeight = 19;

	public final PVector autoSpacing = new PVector(10, 20, 0);

	/**
	 * Convenience constructor to extend Toggle.
	 * 
	 * @example use/ControlP5extendController
	 * @param theControlP5
	 * @param theName
	 */
	public Toggle(ControlP5 theControlP5, String theName) {
		this(theControlP5, theControlP5.getDefaultTab(), theName, 0, 0, 0, autoWidth, autoHeight);
		theControlP5.register(theControlP5.papplet, theName, this);
	}

	/**
	 * 
	 * @param theControlP5 ControlP5
	 * @param theParent Tab
	 * @param theName String
	 * @param theValue float
	 * @param theX float
	 * @param theY float
	 * @param theWidth int
	 * @param theHeight int
	 */
	public Toggle(ControlP5 theControlP5, CTab theParent, String theName, float theValue, float theX, float theY,
			int theWidth, int theHeight) {
		super(theControlP5, theParent, theName, theX, theY, theWidth, theHeight);
		_myValue = theValue;
		captionLabel.setAlignment(AlignmentX.LEFT, AlignmentY.BOTTOM_OUTSIDE).setPaddingX(0);
	}

	/**
	 * 
	 * @param theApplet PApplet
	 */
	@Override
	@ControlP5.Invisible
	public void draw(PApplet theApplet) {
		theApplet.pushMatrix();
		theApplet.translate(position.x, position.y);
		controllerView.display(theApplet, this);
		theApplet.popMatrix();
	}

	@Override
	protected void onEnter() {
		isActive = true;
	}

	@Override
	public void onLeave() {
		isActive = false;
	}
	
	public boolean isToggled(){
		return isOn;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@ControlP5.Invisible
	public void mousePressed() {
		setState(!isOn);
		isActive = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Toggle setValue(float theValue) {
		if (theValue == 0) {
			setState(false);
		} else {
			setState(true);
		}
		return this;
	}

	/**
	 * @param theValue
	 */
	public Toggle setValue(boolean theValue) {
		setValue((theValue == true) ? 1 : 0);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Toggle update() {
		return setValue(_myValue);
	}

	/**
	 * sets the state of the toggle, this can be true or false.
	 * 
	 * @param theFlag boolean
	 */
	public Toggle setState(boolean theFlag) {
		isOn = theFlag;
		_myValue = (isOn == false) ? 0 : 1;
		broadcast();
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public boolean getState() {
		return isOn;
	}

	/**
	 * switch the state of a toggle.
	 */
	public Toggle toggle() {
		if (isOn) {
			setState(false);
		} else {
			setState(true);
		}
		return this;
	}

	/**
	 * by default a toggle returns 0 (for off) and 1 (for on). the internal value variable can be used to
	 * store an additional value for a toggle event.
	 * 
	 * @param theInternalValue
	 */
	@ControlP5.Invisible
	public void setInternalValue(float theInternalValue) {
		internalValue = theInternalValue;
	}

	@ControlP5.Invisible
	public float internalValue() {
		return internalValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Toggle linebreak() {
		cp5.linebreak(this, true, autoWidth, autoHeight, autoSpacing);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@ControlP5.Invisible
	public Toggle updateDisplayMode(ControllerViewType theState) {
		displayMode = theState;
		switch (theState) {
		case DEFAULT:
			controllerView = new ToggleView();
			break;
		case IMAGE:
			controllerView = new ToggleImageView();
			break;
		case SWITCH:
			controllerView = new ToggleSwitchView();
			break;
		case CUSTOM:
		default:
			break;
		}
		return this;
	}

	class ToggleView implements ControllerView<Toggle> {

		@Override
		public void display(PApplet theApplet, Toggle theController) {
			if (isActive) {
				theApplet.fill(isOn ? color.getActive() : color.getForeground());
			} else {
				theApplet.fill(isOn ? color.getActive() : color.getBackground());
			}
			theApplet.rect(0, 0, width, height);
			if (isLabelVisible()) {
				captionLabel.draw(theApplet, 0, 0, theController);
			}
		}
	}

	class ToggleImageView implements ControllerView<Toggle> {

		@Override
		public void display(PApplet theApplet, Toggle theController) {
			if (isActive) {
				theApplet.image((availableImages[ControlP5.ACTIVE] == true) ? images[ControlP5.ACTIVE]
						: images[ControlP5.DEFAULT], 0, 0);
			} else {
				if (isOn) {
					theApplet.image((availableImages[ControlP5.ACTIVE] == true) ? images[ControlP5.ACTIVE]
							: images[ControlP5.DEFAULT], 0, 0);
				} else {
					theApplet.image(images[ControlP5.DEFAULT], 0, 0);
				}
			}
			theApplet.rect(0, 0, width, height);
		}
	}

	class ToggleSwitchView implements ControllerView<Toggle> {

		@Override
		public void display(PApplet theApplet, Toggle theController) {
			theApplet.fill(color.getBackground());
			theApplet.rect(0, 0, width, height);
			theApplet.fill(color.getActive());
			if (isOn) {
				theApplet.rect(0, 0, width / 2, height);
			} else {
				theApplet.rect((width % 2 == 0 ? 0 : 1) + width / 2, 0, width / 2, height);
			}
			if (isLabelVisible()) {
				captionLabel.draw(theApplet, 0, 0, theController);
			}
		}
	}
}

package com.hipposretribution.controlP5.drawable.controller.lone.slider;

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
import com.hipposretribution.controlP5.drawable.CDrawableBaseInterface;
import com.hipposretribution.controlP5.drawable.controller.CElement;
import com.hipposretribution.controlP5.drawable.label.Label;

/**
 * Tickmarks are used by the Slider and Knob controller.
 */
class TickMark implements CDrawableBaseInterface {

	protected CElement<?> parent;

	protected int length = 4;

	protected Label label;

	protected boolean isLabel;

	private ControlP5 cp5;

	public TickMark(CElement<?> theController, ControlP5 controlP5) {
		parent = theController;
		cp5 = controlP5;
	}

	@Override
	public void draw(PApplet theApplet) {
		draw(theApplet, ControlP5.HORIZONTAL);
	}

	public void draw(PApplet theApplet, int theDirection) {
		theApplet.pushMatrix();
		switch (theDirection) {
		case (ControlP5.HORIZONTAL):
			theApplet.translate(0, length);
			theApplet.rect(0, 0, 1, length);
			if (isLabel) {
				label.draw(theApplet, 0, length + 4, parent);
			}
			break;
		case (ControlP5.VERTICAL):
			theApplet.translate(-length, 0);
			theApplet.rect(0, 0, length, 1);
			if (isLabel) {
				label.draw(theApplet, -label.getWidth(), 0, parent);
			}
			break;
		}

		theApplet.popMatrix();
	}

	public void setLength(int theLength) {
		length = theLength;
	}

	public Label setLabel(String theLabeltext) {
		if (label == null) {
			label = new Label(cp5, theLabeltext);
			isLabel = true;
		} else {
			label.setText(theLabeltext);
		}
		return label;
	}

	public Label getLabel() {
		if (label == null) {
			setLabel("?");
		}
		return label;
	}

}

package com.hipposretribution.controlP5.drawable.controller.lone.text;

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
import processing.core.PFont;

import com.hipposretribution.controlP5.ControlP5;
import com.hipposretribution.controlP5.drawable.controller.CElement;
import com.hipposretribution.controlP5.drawable.controller.group.CTab;
import com.hipposretribution.controlP5.drawable.label.ControlFont;
import com.hipposretribution.controlP5.drawable.label.Label;

public class TextLabel extends CElement<TextLabel> {
	/*
	 * NOTE: Rather than have two labels, one of which we will never see exactly like the original ControlP5
	 * library, we will instead use the caption label.
	 */

	/**
	 * 
	 * @param theControlP5 ControlP5
	 * @param theParent Tab
	 * @param theName String
	 * @param theValue String
	 * @param theX int
	 * @param theY int
	 */
	public TextLabel(final ControlP5 theControlP5, final CTab theParent, final String theName, final String theValue,
			final int theX, final int theY) {
		super(theControlP5, theParent, theName, theX, theY, 200, 20);

		captionLabel = new Label(cp5, theValue, 10, 10, color.getCaption());
		captionLabel.setFont(cp5.getFont());
		captionLabel.setMultiline(false);
		captionLabel.setUpperCase(false);
	}

	/**
	 * @param theControlP5
	 * @param theValue
	 * @param theX
	 * @param theY
	 */
	public TextLabel(ControlP5 theControlP5, final String theValue, final int theX, final int theY) {
		super("", theX, theY);

		cp5 = theControlP5;

		captionLabel = new Label(cp5, theValue, 10, 10, color.getCaption());
		captionLabel.setFont(cp5.getFont());
		captionLabel.setMultiline(false);
		captionLabel.setUpperCase(false);
	}

	/**
	 * @param theControlP5
	 * @param theValue
	 * @param theX
	 * @param theY
	 * @param theW
	 * @param theH
	 */
	public TextLabel(ControlP5 theControlP5, final String theValue, final int theX, final int theY, final int theW,
			final int theH) {
		super("", theX, theY);
		cp5 = theControlP5;
		captionLabel = new Label(cp5, theValue, theW, theH, color.getCaption());
		captionLabel.setFont(cp5.getFont());
		captionLabel.setMultiline(false);
		captionLabel.setUpperCase(false);
	}

	/**
	 * Method to draw the Text Label.
	 * 
	 */
	@Override
	public void draw(final PApplet theApplet) {
		if (isVisible) {
			theApplet.pushMatrix();
			theApplet.translate(position.x, position.y);
			captionLabel.draw(theApplet, 0, 0, this);
			theApplet.popMatrix();
		}
	}

	/**
	 * Method to draw the Text Label.
	 * 
	 * @param theX - the x-coordinate
	 * @param theY
	 */
	public void draw(int theX, int theY) {
		cp5.papplet.pushMatrix();
		cp5.papplet.translate(theX, theY);
		draw(cp5.papplet);
		cp5.papplet.popMatrix();
	}

	/**
	 * Method to draw the Text Label.
	 * 
	 */
	public void draw() {
		draw(cp5.papplet);
	}

	/**
	 * Method to enable/disable the label's background
	 * 
	 * @param theBackgroundFlag - flag to enable/disbale background
	 * @return this
	 */
	public TextLabel setBackground(boolean theBackgroundFlag) {
		captionLabel.setBackground(theBackgroundFlag);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hipposretribution.controlP5.elements.colour.CColourInterface#setColourBackground(int)
	 */
	@Override
	public TextLabel setColourBackground(int colorBackground) {
		color.setBackground(colorBackground);
		captionLabel.setColorBackground(colorBackground);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hipposretribution.controlP5.elements.colour.CColourInterface#setColourValue(int)
	 */
	@Override
	public TextLabel setColourValue(int colorValue) {
		color.setValue(colorValue);
		captionLabel.setColor(colorValue);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hipposretribution.controlP5.elements.drawable.controllers.ControllerInterface#setFont(com.
	 * hipposretribution.controlP5.elements.drawable.label.ControlFont)
	 */
	@Override
	public TextLabel setFont(ControlFont theControlFont) {
		captionLabel.setFont(theControlFont);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hipposretribution.controlP5.elements.drawable.controllers.ControllerInterface#setFont(processing
	 * .core.PFont)
	 */
	@Override
	public TextLabel setFont(PFont thePFont) {
		captionLabel.setFont(thePFont);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hipposretribution.controlP5.elements.drawable.CDrawable#setHeight(int)
	 */
	@Override
	public TextLabel setHeight(int theValue) {
		captionLabel.setHeight(theValue);
		return this;
	}

	/**
	 * Method to set the label's multiline flag
	 * 
	 * @param theMultilineFlag - the value of the flag to set
	 * @return this text label
	 */
	public TextLabel setMultiline(final boolean theMultilineFlag) {
		captionLabel.setMultiline(true);
		return this;
	}

	/**
	 * set the text of the textlabel.
	 * 
	 * @param theText - the text to set as the label
	 */
	public TextLabel setText(final String theText) {
		captionLabel.setText(theText);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hipposretribution.controlP5.elements.drawable.CDrawable#setWidth(int)
	 */
	@Override
	public TextLabel setWidth(int theWidth) {
		captionLabel.setWidth(theWidth);
		return this;
	}

	public TextLabel setLineHeight(int theValue) {
		captionLabel.setLineHeight(theValue);
		return this;
	}

	public int getLineHeight() {
		return captionLabel.getLineHeight();
	}

	/**
	 * 
	 * @param theValue float
	 */
	@Override
	public TextLabel setValue(float theValue) {
		return this;
	}



	@Override
	public TextLabel setStringValue(String theValue) {
		return setValue(theValue);
	}

/**
	 * set the text of the textlabel.
	 * 
	 * @param theText String
	 */
	public TextLabel setValue(final String theText) {
		_myStringValue = theText;
		_myValueLabel.setText(theText);
		width = _myValueLabel.getWidth();
		height = _myValueLabel.getHeight();
		return this;
	}
}

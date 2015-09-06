package com.hipposretribution.controlP5.colour; // TODO work out this class' final resting place.

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
 *          Refactored for clarity and better UK English sensibilities
 * 
 * @author zakski (https://github.com/zakski/animated-nemesis-gui)
 * @modified 29/05/2014
 * @version 3.0.0
 * 
 */

import java.io.Serializable;

import com.hipposretribution.controlP5.ControlP5;

/**
 * A wrapper class to contains all the colours of a controller: foreground, background, active, caption label
 * and value label colours.
 */

@SuppressWarnings("serial")
public final class CColor implements Serializable {

	/*
	 * NOTE: all CColor fields/variables should have colour specified in front of them to better group them
	 * together, similar to how we are storing the individual values here.
	 */

	private int colorForeground = 0xff00698c; // controller foreground colour
	private int colorBackground = 0xff003652; // controller background colour
	private int colorCaption = 0xffffffff; // controller caption colour (i.e. the colour of the display name)
	private int colorValue = 0xffffffff; // controller value colour (e.g. the entered text of a TextField)
	private int colorActive = 0xff08a2cf; // controller active colour (e.g. when pressed, when in focus)

	private static final int maskAlpha = 0xff000000; // mask to get the alpha from the rest of the colour

	/**
	 * Convenience Constructor to set the CColor to the current Global Values
	 * 
	 */
	public CColor() {
		this(ControlP5.getGlobalColour());
	}

	/**
	 * Convenience Constructor to set the CColor to an Existing CColor's Values
	 * 
	 */
	public CColor(CColor theColor) {
		set(theColor);
	}

	/**
	 * Constructor to create a CColor from Scratch
	 * 
	 * @param fore - the foreground colour
	 * @param back - the background colour
	 * @param capt - the caption colour
	 * @param val - the value colour
	 * @param act - the active colour
	 */
	public CColor(int fore, int back, int capt, int val, int act) {
		setForeground(fore);
		setBackground(back);
		setCaption(capt);
		setValue(val);
		setActive(act);
	}

	/**
	 * Protected Method to force all colours to be at least a tiny bit opaque. If you want something to be
	 * invisible, disable it properly.
	 * 
	 * @param theColor - the colour to look at
	 * @return processed colour value
	 */
	protected int ensureOpacity(int theColor) {
		if ((theColor & maskAlpha) == 0) { // if the colour is completely transparent
			return maskAlpha; // set colour to black
		} else { // otherwise it is ok
			return theColor;
		}
	}

	/**
	 * @param theColor
	 * @return
	 */
	public CColor set(CColor theColor) {
		colorForeground = theColor.colorForeground;
		colorBackground = theColor.colorBackground;
		colorActive = theColor.colorActive;
		colorCaption = theColor.colorCaption;
		colorValue = theColor.colorValue;
		return this;
	}

	/**
	 * Method to copy this colour to another object
	 * 
	 * @param theControl - the object that we want to copy this colour to
	 * @return this colour
	 */
	public CColor copyTo(CColourInterface<?> theControl) {
		theControl.setColourForeground(colorForeground);
		theControl.setColourBackground(colorBackground);
		theControl.setColourCaption(colorCaption);
		theControl.setColourValue(colorValue);
		theControl.setColourActive(colorActive);
		return this;
	}

	/**
	 * Method to set the foreground colour
	 * 
	 * @param theColor - the colour to set
	 * @return the updated CColor
	 */
	public CColor setForeground(int theColor) {
		colorForeground = ensureOpacity(theColor);
		return this;
	}

	/**
	 * Method to set the background colour
	 * 
	 * @param theColor - the colour to set
	 * @return the updated CColor
	 */
	public CColor setBackground(int theColor) {
		colorBackground = ensureOpacity(theColor);
		return this;
	}

	/**
	 * Method to set the caption colour
	 * 
	 * @param theColor - the colour to set
	 * @return the updated CColor
	 */
	public CColor setCaption(int theColor) {
		colorCaption = ensureOpacity(theColor);
		return this;
	}

	/**
	 * Method to set the value colour
	 * 
	 * @param theColor - the colour to set
	 * @return the updated CColor
	 */
	public CColor setValue(int theColor) {
		colorValue = ensureOpacity(theColor);
		return this;
	}

	/**
	 * Method to set the active colour
	 * 
	 * @param theColor - the colour to set
	 * @return the updated CColor
	 */
	public CColor setActive(int theColor) {
		colorActive = ensureOpacity(theColor);
		return this;
	}

	/**
	 * Method to get the foreground colour
	 * 
	 * @return foreground colour
	 */
	public int getForeground() {
		return colorForeground;
	}

	/**
	 * Method to get the background colour
	 * 
	 * @return background colour
	 */
	public int getBackground() {
		return colorBackground;
	}

	/**
	 * Method to get the caption colour
	 * 
	 * @return caption colour
	 */
	public int getCaption() {
		return colorCaption;
	}

	/**
	 * Method to get the value colour
	 * 
	 * @return value colour
	 */
	public int getValue() {
		return colorValue;
	}

	/**
	 * Method to get the active colour
	 * 
	 * @return active colour
	 */
	public int getActive() {
		return colorActive;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		CColor cc = (CColor) o;
		if (colorBackground != cc.colorBackground || colorForeground != cc.colorForeground
				|| colorActive != cc.colorActive || colorCaption != cc.colorCaption || colorValue != cc.colorValue) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 23;
		result = 37 * result + colorBackground;
		result = 37 * result + colorForeground;
		result = 37 * result + colorActive;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return ("bg (" + (colorBackground >> 16 & 0xff) + "," + (colorBackground >> 8 & 0xff) + ","
				+ (colorBackground >> 0 & 0xff) + "), " + "fg (" + (colorForeground >> 16 & 0xff) + ","
				+ (colorForeground >> 8 & 0xff) + "," + (colorForeground >> 0 & 0xff) + "), " + "active ("
				+ (colorActive >> 16 & 0xff) + "," + (colorActive >> 8 & 0xff) + "," + (colorActive >> 0 & 0xff)
				+ "), " + "captionlabel (" + (colorCaption >> 16 & 0xff) + "," + (colorCaption >> 8 & 0xff) + ","
				+ (colorCaption >> 0 & 0xff) + "), " + "valuelabel (" + (colorValue >> 16 & 0xff) + ","
				+ (colorValue >> 8 & 0xff) + "," + (colorValue >> 0 & 0xff) + ")");
	}

}

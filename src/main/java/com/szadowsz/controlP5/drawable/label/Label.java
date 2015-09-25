package com.szadowsz.controlP5.drawable.label;

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
import com.szadowsz.controlP5.drawable.CDrawableInterface;
import com.szadowsz.controlP5.drawable.controller.CInterface;
import com.szadowsz.controlP5.drawable.label.style.AlignmentX;
import com.szadowsz.controlP5.drawable.label.style.AlignmentY;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * A custom label using controlP5's BitFonts or PFont based ControlFonts.
 * 
 * 
 * @see controlP5.ControlFont
 * @example controllers/ControlP5Textlabel
 * 
 */
public class Label implements CDrawableInterface<Label> {

	protected ControlP5 cp5;
	protected ControlFont font;
	protected String text = "";
	protected LabelType type;
	protected LabelStyle style = new LabelStyle();

	protected boolean hasChanged;
	protected boolean hasBackground;

	protected boolean isFixedSize;
	protected boolean isMultiline;
	protected boolean isUpperCase = true;
	protected boolean isVisible = true;

	protected int height = -1;
	protected int width = -1;

	protected int color = 0xffffffff;
	protected int colorBackground = 0xffffffff;

	protected int lineHeight = 0;
	protected int textHeight = 1;

	protected AlignmentX alignX = AlignmentX.LEFT;
	protected AlignmentY alignY = AlignmentY.TOP;
	protected AlignmentX textAlign = AlignmentX.LEFT;

	protected int paddingX = 4;
	protected int paddingY = 4;

	protected float offsetYratio = 0;

	public Label(ControlP5 theControlP5, String theValue) {
		init(theControlP5, theValue, 0, 0, color);
	}

	public Label(ControlP5 theControlP5, String theValue, int theWidth, int theHeight, int theColor) {
		init(theControlP5, theValue, theWidth, theHeight, theColor);
	}

	private void init(ControlP5 theControlP5, String theValue, int theWidth, int theHeight, int theColor) {
		cp5 = theControlP5;
		width = theWidth;
		height = theHeight;
		text = theValue;
		color = theColor;
		type = new SinglelineLabel();
		font = cp5.getFont();
		style = new LabelStyle();
		hasChanged = true;
	}

	void adjusted() {
		hasChanged = false;
	}

	@Override
	public void draw(PApplet theApplet) {
		if (isVisible) {
			font.adjust(theApplet, this);
			draw(theApplet, 0, 0);
		}
	}

	public void draw(PApplet theApplet, int theX, int theY) {
		if (isVisible) {
			theApplet.pushMatrix();
			theApplet.translate(style.marginLeft, style.marginTop);
			theApplet.translate(theX, theY);

			if (hasBackground) {

				float ww = style.paddingRight + style.paddingLeft;
				if (style.backgroundWidth > -1) {
					ww += style.backgroundWidth;
				} else {
					ww += font.getWidth();
				}
				float hh = style.paddingBottom + style.paddingTop;
				if (style.backgroundHeight > -1) {
					hh += style.backgroundHeight;
				} else {
					hh += font.getHeight();
				}
				theApplet.fill(colorBackground);
				theApplet.rect(0, 1, ww, hh);
			}
			theApplet.translate(style.paddingLeft, style.paddingTop);
			font.draw(cp5, this);
			theApplet.popMatrix();
		}
	}

	public void draw(PApplet theApplet, int theX, int theY, CInterface<?> theController) {
		if (isVisible) {
			type.draw(theApplet, theX, theY, theController);
		}
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	public boolean isFixedSize() {
		return isMultiline ? false : isFixedSize;
	}

	public boolean isMultiline() {
		return isMultiline;
	}

	public boolean isUpperCase() {
		return isUpperCase;
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}

	public int getColor() {
		return color;
	}

	public ControlFont getFont() {
		return font;
	}

	public String getFormattedText() {
		return type.getTextFormatted();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hipposretribution.controlP5.drawable.CDrawableInterface#getHeight()
	 */
	@Override
	public int getHeight() {
		return type.getHeight();
	}

	public int getOverflow() {
		return type.getOverflow();
	}

	public String getText() {
		return text;
	}

	public LabelStyle getStyle() {
		return style;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hipposretribution.controlP5.drawable.CDrawableInterface#getWidth()
	 */
	@Override
	public int getWidth() {
		return type.getWidth();
	}

	public int getTextHeight() {
		return font.getTextHeight();
	}

	public int getLineHeight() {
		return lineHeight;
	}

	public Label setAlignment(AlignmentX theX, AlignmentY theY) {
		alignX = theX;
		alignY = theY;
		return this;
	}

	public Label setAlignmentX(AlignmentX theX) {
		alignX = theX;
		return this;
	}

	public Label setAlignmentY(AlignmentY theY) {
		alignY = theY;
		return this;
	}

	public Label setBackground(boolean theBackgroundFlag) {
		hasBackground = theBackgroundFlag;
		return this;
	}

	public Label setColor(int theColor) {
		color = theColor;
		return this;
	}

	public Label setColorBackground(int theColor) {
		colorBackground = theColor;
		return this;
	}

	public Label setFixedSize(boolean theValue) {
		isFixedSize = theValue;
		return this;
	}

	public Label setFont(PFont thePFont) {
		return setFont(new ControlFont(thePFont));
	}

	public Label setFont(ControlFont theFont) {
		setLineHeight(theFont.getSize());
		font = new ControlFont(theFont.getFont(), theFont.getSize());
		hasChanged = true;
		return this;
	}

	public Label setHeight(int theHeight) {
		height = theHeight;
		hasChanged = true;
		return this;
	}

	public Label setMultiline(boolean theValue) {
		if (isMultiline != theValue) {
			isMultiline = theValue;
			type = (isMultiline) ? new MultiLineLabel() : new SinglelineLabel();
			hasChanged = true;
		}
		return this;
	}

	public Label setText(String theValue) {
		text = theValue;
		hasChanged = true;
		return this;
	}

	public Label setUpperCase(boolean theValue) {
		isUpperCase = theValue;
		hasChanged = true;
		return this;
	}

	@Override
	public Label setVisible(boolean theValue) {
		isVisible = theValue;
		return this;
	}

	public Label setWidth(int theWidth) {
		width = theWidth;
		hasChanged = true;
		return this;
	}

	public Label setPadding(int thePaddingX, int thePaddingY) {
		paddingX = thePaddingX;
		paddingY = thePaddingY;
		return this;
	}

	public Label setPaddingX(int thePaddingX) {
		paddingX = thePaddingX;
		return this;
	}

	public Label setPaddingY(int thePaddingY) {
		paddingY = thePaddingY;
		return this;
	}

	public Label setSize(int theSize) {
		font.setSize(theSize);
		return this;
	}

	Label setTextHeight(int theHeight) {
		textHeight = theHeight;
		return this;
	}

	public Label setOffsetYratio(float theValue) {
		offsetYratio = theValue;
		hasChanged = true;
		return this;
	}

	public float getOffsetYratio() {
		return offsetYratio;
	}

	public Label setLineHeight(int theValue) {
		lineHeight = theValue;
		hasChanged = true;
		return this;
	}

	/**
	 * Internal Interface used to differ between single and multiple line labels without requiring us to check
	 * which the label currently is each time we draw
	 * 
	 */
	interface LabelType {

		/**
		 * Method to draw the label according to its type
		 * 
		 * @param theApplet - the applet to draw the label on
		 * @param theX - initial x-coordinate
		 * @param theY - initial y-coordinate
		 * @param theController - the controller the label is contained within
		 */
		public void draw(PApplet theApplet, int theX, int theY, CInterface<?> theController);

		/**
		 * Method to format the label's width according to the label's specifications
		 * 
		 * @return the label width
		 */
		public int getWidth();

		/**
		 * Method to get the label's height according to the label's specifications
		 * 
		 * @return the label's height
		 */
		public int getHeight();

		/**
		 * Method to format the label's text height overflow according to the label's specifications. Used for
		 * Multiline only.
		 * 
		 * @return the label's height overflow
		 */
		public int getOverflow();

		/**
		 * Method to format the label's text according to the label's specifications
		 * 
		 * @return the label display text
		 */
		public String getTextFormatted();
	}

	/**
	 * Internal Class to represent the operations needed for drawing the label on a single line
	 * 
	 */
	class SinglelineLabel implements LabelType {

		/**
		 * Method to translate the label according to its alignment settings
		 * 
		 * @param theApplet - the applet to draw the label on
		 * @param theController - the controller the label is contained within
		 * @param theAlignX - the X Alignment setting
		 * @param theAlignY - the Y Alignment setting
		 */
		private void align(PApplet theApplet, CInterface<?> theController, AlignmentX theAlignX,
				AlignmentY theAlignY) {
			int x = 0;
			int y = 0;
			switch (theAlignX) {
			case CENTER:
				x = (theController.getWidth() - font.getWidth()) / 2;
				break;
			case LEFT:
				x = paddingX;
				break;
			case RIGHT:
				x = theController.getWidth() - font.getWidth() - paddingX;
				break;
			case LEFT_OUTSIDE:
				x = -font.getWidth() - paddingX;
				break;
			case RIGHT_OUTSIDE:
				x = theController.getWidth() + paddingX;
				break;
			}
			switch (theAlignY) {
			case CENTER:
				y = theController.getHeight() / 2 + font.getTop() - font.getCenter();
				break;
			case TOP:
				y = 0;
				break;
			case BOTTOM:
				y = theController.getHeight() - font.getHeight() - 1;
				break;
			case BASELINE:
				y = theController.getHeight() + font.getTop() - 1;
				break;
			case BOTTOM_OUTSIDE:
				y = theController.getHeight() + paddingY;
				break;
			case TOP_OUTSIDE:
				y = -font.getHeight() - paddingY;
				break;
			}
			theApplet.translate(x, y);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.hipposretribution.controlP5.elements.drawable.label.Label.LabelType#draw(com.hipposretribution
		 * .controlP5.elements.drawable.label.Label, processing.core.PApplet, int, int,
		 * com.hipposretribution.controlP5.elements.ControllerInterface)
		 */
		@Override
		public void draw(PApplet theApplet, int theX, int theY, CInterface<?> theController) {
			font.adjust(theApplet, Label.this);
			theApplet.pushMatrix();
			align(theApplet, theController, alignX, alignY);
			Label.this.draw(theApplet, theX, theY);
			theApplet.popMatrix();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.hipposretribution.controlP5.elements.drawable.label.Label.LabelType#getWidth()
		 */
		@Override
		public int getWidth() {
			return isFixedSize ? width : font.getWidth();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.hipposretribution.controlP5.elements.drawable.label.Label.LabelType#getHeight()
		 */
		@Override
		public int getHeight() {
			return font.getHeight();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.hipposretribution.controlP5.elements.drawable.label.Label.LabelType#getOverflow()
		 */
		@Override
		public int getOverflow() {
			return -1;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.hipposretribution.controlP5.elements.drawable.label.Label.LabelType#getTextFormatted()
		 */
		@Override
		public String getTextFormatted() {
			return (isUpperCase ? text.toUpperCase() : text);
		}
	}

	/**
	 * Internal Class to represent the operations needed for drawing the label on multiple lines
	 * 
	 */
	class MultiLineLabel implements LabelType {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.hipposretribution.controlP5.elements.drawable.label.Label.LabelType#draw(com.hipposretribution
		 * .controlP5.elements.drawable.label.Label, processing.core.PApplet, int, int,
		 * com.hipposretribution.controlP5.elements.ControllerInterface)
		 */
		@Override
		public void draw(PApplet theApplet, int theX, int theY, CInterface<?> theController) {
			font.adjust(theApplet, Label.this);
			Label.this.draw(theApplet, theX, theY);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.hipposretribution.controlP5.elements.drawable.label.Label.LabelType#getWidth()
		 */
		@Override
		public int getWidth() {
			return width;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.hipposretribution.controlP5.elements.drawable.label.Label.LabelType#getHeight()
		 */
		@Override
		public int getHeight() {
			return height;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.hipposretribution.controlP5.elements.drawable.label.Label.LabelType#getOverflow()
		 */
		@Override
		public int getOverflow() {
			return font.getOverflow();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.hipposretribution.controlP5.elements.drawable.label.Label.LabelType#getTextFormatted()
		 */
		@Override
		public String getTextFormatted() {
			return (isUpperCase ? text.toUpperCase() : text);
		}

	}

	public Label setTextAlignment(AlignmentX theAlignment) {
		textAlign = theAlignment;
		return this;
	}
}

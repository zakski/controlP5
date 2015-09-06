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

import java.util.ArrayList;
import java.util.logging.Level;

import processing.core.PApplet;
import processing.core.PVector;

import com.hipposretribution.controlP5.ControlP5;
import com.hipposretribution.controlP5.ControlP5.Invisible;
import com.hipposretribution.controlP5.drawable.controller.CElement;
import com.hipposretribution.controlP5.drawable.controller.CGroup;
import com.hipposretribution.controlP5.drawable.controller.view.ControllerView;
import com.hipposretribution.controlP5.drawable.controller.view.ControllerViewType;
import com.hipposretribution.controlP5.drawable.label.AlignmentX;
import com.hipposretribution.controlP5.drawable.label.AlignmentY;
import com.hipposretribution.controlP5.drawable.label.Label;

/**
 * A range slider works just like a slider but can be adjusted on both ends.
 * 
 * @see Slider
 * @example controllers/ControlP5range
 * @nosuperclasses Controller Controller
 */
public class Range extends CElement<Range> {

	/*
	 * TODO if range value is int, value labels do initialize as floats. first click makes them display as
	 * ints without decimal point
	 */
	protected static final int HORIZONTAL = 0;

	protected static final int VERTICAL = 1;

	protected int _myDirection;

	protected float _myValuePosition;

	protected boolean isDragging;

	protected boolean isDraggable = true;

	protected boolean isFirstClick;

	protected Label _myHighValueLabel;

	protected float _myValueRange;

	protected boolean isMinHandle;

	protected boolean isMaxHandle;

	protected boolean isMoveHandle;

	protected float distanceHandle;

	protected int handleSize = 10;

	protected int minHandle = 0;

	protected int maxHandle = 0;

	protected int mr = 0;

	protected final ArrayList<TickMark> _myTickMarks = new ArrayList<TickMark>();

	protected boolean isShowTickMarks;

	protected boolean isSnapToTickMarks;

	public static int autoWidth = 99;

	public static int autoHeight = 9;

	public static PVector autoSpacing = new PVector(0, 5, 0);

	public int alignValueLabel = ControlP5.CENTER;

	protected int _myColorTickMark = 0xffffffff;

	private int mode = -1;

	/**
	 * Convenience constructor to extend Range.
	 * 
	 * @example use/ControlP5extendController
	 * @param theControlP5
	 * @param theName
	 */
	public Range(ControlP5 theControlP5, String theName) {
		this(theControlP5, theControlP5.getDefaultTab(), theName, 0, 100, 0, 100, 0, 0, 99, 9);
		theControlP5.register(theControlP5.papplet, theName, this);
	}

	/**
	 * 
	 * @param theControlP5 ControlP5
	 * @param theParent ControllerGroup
	 * @param theName String
	 * @param theMin float
	 * @param theMax float
	 * @param theDefaultValue float
	 * @param theX int
	 * @param theY int
	 * @param theWidth int
	 * @param theHeight int
	 */
	@ControlP5.Invisible
	public Range(ControlP5 theControlP5, CGroup<?> theParent, String theName, float theMin, float theMax,
			float theDefaultMinValue, float theDefaultMaxValue, int theX, int theY, int theWidth, int theHeight) {
		super(theControlP5, theParent, theName, theX, theY, theWidth, theHeight);

		_myArrayValue = new float[] { theDefaultMinValue, theDefaultMaxValue };

		_myMin = theMin;
		_myMax = theMax;
		_myValueRange = _myMax - _myMin;

		minHandle = (int) PApplet.map(theDefaultMinValue, _myMin, _myMax, handleSize, getWidth() - handleSize);
		maxHandle = (int) PApplet.map(theDefaultMaxValue, _myMin, _myMax, handleSize, getWidth() - handleSize);
		mr = maxHandle - minHandle;

		captionLabel = new Label(cp5, theName).setColor(color.getCaption()).setAlignment(AlignmentX.RIGHT_OUTSIDE,
			AlignmentY.CENTER);
		_myValueLabel = new Label(cp5, "" + adjustValue(_myMin)).setColor(color.getValue())
				.setText("" + adjustValue(theDefaultMinValue)).setAlignment(AlignmentX.LEFT, AlignmentY.CENTER);
		_myHighValueLabel = new Label(cp5, adjustValue(_myMax)).setColor(color.getValue())
				.setText("" + adjustValue(theDefaultMaxValue)).setAlignment(AlignmentX.RIGHT, AlignmentY.CENTER);

		_myValue = theDefaultMinValue;

		_myDirection = HORIZONTAL;

		update();

	}

	@Override
	public Range setColourValue(int theColor) {
		_myValueLabel.setColor(theColor);
		_myHighValueLabel.setColor(theColor);
		return this;
	}

	@Override
	public Range setColourCaption(int theColor) {
		captionLabel.setColor(theColor);
		return this;
	}

	public Range setHighValueLabel(final String theLabel) {
		_myHighValueLabel.setText(theLabel);
		return this;
	}

	public Range setLowValueLabel(final String theLabel) {
		_myValueLabel.setText(theLabel);
		return this;
	}

	@ControlP5.Invisible
	public Range setSliderMode(int theMode) {
		return this;
	}

	public Range setHandleSize(int theSize) {
		handleSize = theSize;
		setLowValue(_myArrayValue[0], false);
		setHighValue(_myArrayValue[1], false);
		mr = maxHandle - minHandle;
		return this;
	}

	@Override
	@ControlP5.Invisible
	public Range updateInternalEvents(PApplet theApplet) {
		if (isVisible) {
			int c = cp5.mouseDX();
			if (c == 0) {
				return this;
			}
			if (isMousePressed && !cp5.isAltDown()) {
				switch (mode) {
				case (ControlP5.LEFT):
					minHandle = PApplet.max(handleSize, PApplet.min(maxHandle, minHandle + c));
					break;
				case (ControlP5.RIGHT):
					maxHandle = PApplet.max(minHandle, PApplet.min(getWidth() - handleSize, maxHandle + c));
					break;
				case (ControlP5.CENTER):
					minHandle = PApplet.max(handleSize, PApplet.min(getWidth() - mr - handleSize, minHandle + c));
					maxHandle = PApplet.max(minHandle, PApplet.min(getWidth() - handleSize, minHandle + mr));
					break;
				}
				update();
			}
		}
		return this;
	}

	@Override
	@Invisible
	public void mousePressed() {

		final float posX = parent.getAbsolutePosition().x + position.x;
		final float posY = parent.getAbsolutePosition().y + position.y;

		if (cp5.mouseY() < posY || cp5.mouseY() > posY + getHeight()) {
			mode = -1;
			isMinHandle = isMaxHandle = false;
			return;
		}

		int x0 = (int) (posX + minHandle);
		int x1 = (int) (posX + maxHandle);

		if (cp5.mouseX() >= x0 - handleSize && cp5.mouseX() < x0) {
			mode = ControlP5.LEFT;
			isMinHandle = true;
		} else if (cp5.mouseX() >= x1 && cp5.mouseX() < x1 + handleSize) {
			mode = ControlP5.RIGHT;
			isMaxHandle = true;
		} else if (cp5.mouseX() > x0 && cp5.mouseX() < x1 && isDraggable) {
			mode = ControlP5.CENTER;
		}
	}

	/**
	 * set the value of the range-slider. to set the low and high value, use setLowValue and setHighValue or
	 * setRangeValues
	 * 
	 * @see #setLowValue(float)
	 * @see #setHighValue(float)
	 * @see #setRangeValues(float, float)
	 * 
	 * @param theValue float
	 * @return Range
	 */
	@Override
	@ControlP5.Invisible
	public Range setValue(float theValue) {
		_myValue = theValue;
		broadcast();
		return this;
	}

	/**
	 * @exclude
	 */
	@Override
	@ControlP5.Invisible
	public Range update() {
		_myArrayValue[0] = PApplet.map(minHandle, handleSize, getWidth() - handleSize, _myMin, _myMax);
		_myArrayValue[1] = PApplet.map(maxHandle, handleSize, getWidth() - handleSize, _myMin, _myMax);
		mr = maxHandle - minHandle;
		_myHighValueLabel.setText(adjustValue(_myArrayValue[1]));
		_myValueLabel.setText(adjustValue(_myArrayValue[0]));
		return setValue(_myValue);
	}

	@ControlP5.Invisible
	public Range setDraggable(boolean theFlag) {
		isDraggable = theFlag;
		isDragging = (theFlag == false) ? false : isDragging;
		return this;
	}

	@Override
	public float[] getArrayValue() {
		return _myArrayValue;
	}

	@Override
	public Range setArrayValue(float[] theArray) {
		setLowValue(theArray[0], false);
		setHighValue(theArray[1], false);
		return update();
	}

	@Override
	public Range setMin(float theValue) {
		_myMin = theValue;
		_myValueRange = _myMax - _myMin;
		return setLowValue(_myArrayValue[0]);
	}

	@Override
	public Range setMax(float theValue) {
		_myMax = theValue;
		_myValueRange = _myMax - _myMin;
		return setHighValue(_myArrayValue[1]);
	}

	public float getLowValue() {
		return _myArrayValue[0];
	}

	public float getHighValue() {
		return _myArrayValue[1];
	}

	@Override
	public Range setWidth(int theValue) {
		width = theValue;
		return this;
	}

	@Override
	public Range setHeight(int theValue) {
		height = theValue;
		return this;
	}

	@Override
	@ControlP5.Invisible
	public void mouseReleased() {
		isDragging = isMinHandle = isMaxHandle = isMoveHandle = false;
		mode = -1;
	}

	@Override
	@ControlP5.Invisible
	public void mouseReleasedOutside() {
		mouseReleased();
	}

	@Override
	@ControlP5.Invisible
	public void onLeave() {
		isMinHandle = false;
		isMaxHandle = false;
	}

	protected void setTickMarks() {
		System.out.println("Range Tickmarks not yet supported");
	}

	public Range setColorTickMark(int theColor) {
		_myColorTickMark = theColor;
		return this;
	}

	public Range showTickMarks(boolean theFlag) {
		isShowTickMarks = theFlag;
		return this;
	}

	public Range snapToTickMarks(boolean theFlag) {
		isSnapToTickMarks = theFlag;
		System.out.println("Range Tickmarks not yet supported");
		return this;
	}

	@ControlP5.Invisible
	public TickMark getTickMark() {
		System.out.println("Range Tickmarks not yet supported");
		return null;
	}

	public ArrayList<TickMark> getTickMarks() {
		return _myTickMarks;
	}

	public Range setNumberOfTickMarks(int theNumber) {
		System.out.println("Range Tickmarks not yet supported");
		_myTickMarks.clear();
		if (theNumber > 0) {
			for (int i = 0; i < theNumber; i++) {
				_myTickMarks.add(new TickMark(this, cp5));
			}
			showTickMarks(true);
			snapToTickMarks(true);
		} else {
			showTickMarks(false);
			snapToTickMarks(false);
		}
		_myUnit = (_myMax - _myMin) / ((width > height) ? width - 1 : height - 1);
		setLowValue(_myArrayValue[0], false);
		setHighValue(_myArrayValue[1], false);
		return update();
	}

	public Range setRange(float theMinValue, float theMaxValue) {
		setMin(theMinValue);
		setMax(theMaxValue);
		return this;
	}

	public Range setRangeValues(float theLowValue, float theHighValue) {
		return setArrayValue(new float[] { theLowValue, theHighValue });
	}

	private Range setLowValue(float theValue, boolean isUpdate) {
		_myArrayValue[0] = PApplet.max(_myMin, snapValue(theValue));
		minHandle = (int) PApplet.map(_myArrayValue[0], _myMin, _myMax, handleSize, getWidth() - handleSize);
		return (isUpdate) ? update() : this;
	}

	public Range setLowValue(float theValue) {
		return setLowValue(theValue, true);
	}

	private Range setHighValue(float theValue, boolean isUpdate) {
		_myArrayValue[1] = PApplet.min(_myMax, snapValue(theValue));
		maxHandle = (int) PApplet.map(_myArrayValue[1], _myMin, _myMax, handleSize, getWidth() - handleSize);
		return (isUpdate) ? update() : this;
	}

	public Range setHighValue(float theValue) {
		return setHighValue(theValue, true);
	}

	protected float snapValue(float theValue) {
		if (isMousePressed) {
			return theValue;
		}
		if (isSnapToTickMarks) {
			_myValuePosition = ((theValue - _myMin) / _myUnit);
			float n = PApplet.round(PApplet.map(_myValuePosition, 0, (_myDirection == HORIZONTAL) ? getWidth()
					: getHeight(), 0, _myTickMarks.size() - 1));
			theValue = PApplet.map(n, 0, _myTickMarks.size() - 1, _myMin, _myMax);
		}
		return theValue;
	}

	@Override
	@ControlP5.Invisible
	public Range updateDisplayMode(ControllerViewType theMode) {
		displayMode = theMode;
		switch (theMode) {
		case DEFAULT:
			controllerView = new RangeView();
			break;
		case SPRITE:
			controllerView = new RangeSpriteView();
			break;
		case IMAGE:
			controllerView = new RangeImageView();
			break;
		case CUSTOM:
		default:
			break;
		}
		return this;
	}

	class RangeSpriteView implements ControllerView<Range> {
		@Override
		public void display(PApplet theApplet, Range theController) {
			ControlP5.logger().log(Level.INFO, "RangeSpriteDisplay not available.");
		}
	}

	class RangeView implements ControllerView<Range> {

		@Override
		public void display(PApplet theApplet, Range theController) {

			int high = mode;

			final float posX = parent.getAbsolutePosition().x + position.x;
			int x0 = (int) (posX + minHandle);
			int x1 = (int) (posX + maxHandle);

			if (isInside() && high < 0) {
				if (cp5.mouseX() >= x0 - handleSize && cp5.mouseX() < x0) {
					high = ControlP5.LEFT;
				} else if (cp5.mouseX() >= x1 && cp5.mouseX() < x1 + handleSize) {
					high = ControlP5.RIGHT;
				} else if (cp5.mouseX() > x0 && cp5.mouseX() < x1 && isDraggable) {
					high = ControlP5.CENTER;
				}
			}

			theApplet.pushMatrix();

			theApplet.fill(color.getBackground());

			theApplet.noStroke();

			theApplet.rect(0, 0, width, height);

			theApplet.fill(high == ControlP5.CENTER ? color.getActive() : color.getForeground());

			if (isShowTickMarks) {
				int n = handleSize / 2;
				theApplet.rect(minHandle - n, 0, mr + handleSize, height);
				theApplet.fill((isMinHandle || high == ControlP5.LEFT) ? color.getActive() : color.getForeground());
				theApplet.triangle(minHandle - handleSize, 0, minHandle, 0, minHandle - n, height);
				theApplet.fill((isMaxHandle || high == ControlP5.RIGHT) ? color.getActive() : color.getForeground());
				theApplet.triangle(maxHandle, 0, maxHandle + handleSize, 0, maxHandle + n, height);
			} else {
				theApplet.rect(minHandle, 0, mr, height);
				theApplet.fill((isMinHandle || high == ControlP5.LEFT) ? color.getActive() : color.getForeground());
				theApplet.rect((minHandle - handleSize), 0, handleSize, height);
				theApplet.fill((isMaxHandle || high == ControlP5.RIGHT) ? color.getActive() : color.getForeground());
				theApplet.rect(maxHandle, 0, handleSize, height);

			}

			if (isLabelVisible()) {
				captionLabel.draw(theApplet, 0, 0, theController);
				_myValueLabel.draw(theApplet, 0, 0, theController);
				_myHighValueLabel.draw(theApplet, 0, 0, theController);
			}

			theApplet.popMatrix();

			if (isShowTickMarks) {
				theApplet.pushMatrix();
				float x = (getWidth() - handleSize) / (getTickMarks().size() - 1);
				theApplet.translate(handleSize / 2, getHeight());
				theApplet.fill(_myColorTickMark);
				for (TickMark tm : getTickMarks()) {
					tm.draw(theApplet);
					theApplet.translate(x, 0);
				}
				theApplet.popMatrix();
			}
		}
	}

	class RangeImageView implements ControllerView<Range> {
		@Override
		public void display(PApplet theApplet, Range theController) {
			ControlP5.logger().log(Level.INFO, "RangeImageDisplay not implemented.");
		}
	}

	@Override
	@ControlP5.Invisible
	public String toString() {
		return "type:\tRange\n" + super.toString();
	}
}

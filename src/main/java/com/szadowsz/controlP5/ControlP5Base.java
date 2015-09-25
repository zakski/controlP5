package com.szadowsz.controlP5;

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

import com.szadowsz.controlP5.cast.FieldChangedListener;
import com.szadowsz.controlP5.colour.CColor;
import com.szadowsz.controlP5.drawable.controller.CElement;
import com.szadowsz.controlP5.drawable.controller.CGroup;
import com.szadowsz.controlP5.drawable.controller.CInterface;
import com.szadowsz.controlP5.drawable.controller.group.*;
import com.szadowsz.controlP5.drawable.controller.group.listbox.DropdownList;
import com.szadowsz.controlP5.drawable.controller.group.listbox.ListBox;
import com.szadowsz.controlP5.drawable.controller.group.multilist.MultiList;
import com.szadowsz.controlP5.drawable.controller.group.toggle.ToggleGroup;
import com.szadowsz.controlP5.drawable.controller.lone.chart.Chart;
import com.szadowsz.controlP5.drawable.controller.lone.press.Button;
import com.szadowsz.controlP5.drawable.controller.lone.press.Matrix;
import com.szadowsz.controlP5.drawable.controller.lone.press.Toggle;
import com.szadowsz.controlP5.drawable.controller.lone.slider.Knob;
import com.szadowsz.controlP5.drawable.controller.lone.slider.Range;
import com.szadowsz.controlP5.drawable.controller.lone.slider.Slider;
import com.szadowsz.controlP5.drawable.controller.lone.slider.Slider2D;
import com.szadowsz.controlP5.drawable.controller.lone.text.NumberBox;
import com.szadowsz.controlP5.drawable.controller.lone.text.TextField;
import com.szadowsz.controlP5.drawable.controller.lone.text.TextLabel;
import processing.core.PVector;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map.Entry;

/**
 * The ControlP5Base supports the ControlP5 class and implements all adder methods to add controllers to
 * controlP5.
 */
class ControlP5Base implements ControlP5Constants {

	private ControlP5 cp5;

	protected Map<Object, ArrayList<CInterface<?>>> _myObjectToControllerMap = new HashMap<Object, ArrayList<CInterface<?>>>();

	protected Map<String, FieldChangedListener> _myFieldChangedListenerMap = new HashMap<String, FieldChangedListener>();

	protected CGroup<?> currentGroupPointer;

	protected boolean isCurrentGroupPointerClosed = true;

	protected int autoDirection = HORIZONTAL;

	public CTab getDefaultTab() {
		return (CTab) cp5.getWindow().getTabs().get(1);
	}

	protected void init(ControlP5 theControlP5) {
		cp5 = theControlP5;
		currentGroupPointer = cp5.getWindow().getTab("default");
	}

	public CTab addTab(String theName) {
		for (int i = 0; i < cp5.getWindow().getTabs().size(); i++) {
			if (cp5.getWindow().getTabs().get(i).getName().equals(theName)) {
				return (CTab) cp5.getWindow().getTabs().get(i);
			}
		}
		CTab myTab = new CTab(cp5, cp5.getWindow(), theName);
		cp5.getWindow().getTabs().add(myTab);
		return myTab;
	}

	public Button addButton(final Object theObject, String theIndex, final String theName, final float theValue,
			final int theX, final int theY, final int theW, final int theH) {
		Button myController = new Button(cp5, (CGroup<?>) cp5.getWindow().getTabs().get(1), theName,
				theValue, theX, theY, theW, theH);
		cp5.register(theObject, theIndex, myController);
		return myController;
	}

	public Toggle addToggle(final Object theObject, String theIndex, final String theName,
			final boolean theDefaultValue, final float theX, final float theY, final int theWidth, final int theHeight) {
		Toggle myController = new Toggle(cp5, (CTab) cp5.getWindow().getTabs().get(1), theName,
				(theDefaultValue == true) ? 1f : 0f, theX, theY, theWidth, theHeight);
		cp5.register(theObject, theIndex, myController);
		return myController;
	}

	/**
	 * Matrix is a 2-D matrix controller using toggle controllers in a rows and a columns setup. useful for
	 * software drum machines.
	 */

	public Matrix addMatrix(final Object theObject, final String theIndex, final String theName, final int theCellX,
			final int theCellY, final int theX, final int theY, final int theWidth, final int theHeight) {
		Matrix myController = new Matrix(cp5, (CTab) cp5.getWindow().getTabs().get(1), theName, theCellX, theCellY,
				theX, theY, theWidth, theHeight);
		cp5.register(theObject, theIndex, myController);
		return myController;
	}

	public Matrix addMatrix(final String theName) {
		return addMatrix(theName, 10, 10, 0, 0, 100, 100);
	}

	public Matrix addMatrix(final String theName, final int theCellX, final int theCellY, final int theX,
			final int theY, final int theWidth, final int theHeight) {
		return addMatrix(null, "", theName, theCellX, theCellY, theX, theY, theWidth, theHeight);
	}

	public Slider2D addSlider2D(final String theName) {
		return addSlider2D(null, "", theName, 0, 99, 0, 99, 0, 0, 0, 0, 99, 99);
	}

	/**
	 * Adds a 2D slider to controlP5. A 2D slider is a 2D area with 1 cursor returning its xy coordinates.
	 */
	public Slider2D addSlider2D(Object theObject, final String theIndex, final String theName, float theMinX,
			float theMaxX, float theMinY, float theMaxY, float theDefaultValueX, float theDefaultValueY, int theX,
			int theY, int theW, int theH) {
		Slider2D myController = new Slider2D(cp5, (CGroup<?>) cp5.getWindow().getTabs().get(1), theName,
				theX, theY, theW, theH);
		cp5.register(theObject, theIndex, myController);
		myController.setMinX(theMinX);
		myController.setMaxX(theMaxX);
		myController.setMinY(theMinY);
		myController.setMaxY(theMaxY);
		myController.setArrayValue(new float[] { theDefaultValueX, theDefaultValueY });
		myController.updateValue();
		return myController;
	}

	/**
	 * Adds a slider to controlP5. by default it will be added to the default tab of the main window. Sliders
	 * can be arranged vertically and horizontally depending on their width and height. The look of a sliders
	 * control can either be a bar or a handle. you can add tickmarks to a slider or use the default
	 * free-control setting. A slider can be controller by mouse click, drag or mouse-wheel.
	 */
	public Slider addSlider(Object theObject, final String theIndex, final String theName, float theMin, float theMax,
			float theDefaultValue, int theX, int theY, int theW, int theH) {
		Slider myController = new Slider(cp5, (CGroup<?>) cp5.getWindow().getTabs().get(1), theName, theMin,
				theMax, theDefaultValue, theX, theY, theW, theH);
		cp5.register(theObject, theIndex, myController);
		return myController;
	}

	public Slider addSlider(String theName, float theMin, float theMax, float theDefaultValue, int theX, int theY,
			int theW, int theH) {
		return addSlider(null, "", theName, theMin, theMax, theDefaultValue, theX, theY, theW, theH);
	}

	public Slider addSlider(final String theName, final float theMin, final float theMax, final int theX,
			final int theY, final int theWidth, final int theHeight) {
		return addSlider(null, "", theName, theMin, theMax, theMin, theX, theY, theWidth, theHeight);
	}

	public Slider addSlider(Object theObject, final String theIndex, final String theName, float theMin, float theMax,
			int theX, int theY, int theW, int theH) {
		return addSlider(theObject, theIndex, theName, theMin, theMax, theMin, theX, theY, theW, theH);
	}

	public Range addRange(final String theName) {
		return addRange(theName, 0, 100, 0, 100, 0, 0, 99, 9);
	}

	/**
	 * A range controller, a slider that allows control on both ends of the slider.
	 */
	public Range addRange(Object theObject, final String theIndex, String theName, float theMin, float theMax,
			float theDefaultMinValue, float theDefaultMaxValue, int theX, int theY, int theW, int theH) {
		Range myController = new Range(cp5, (CGroup<?>) cp5.getWindow().getTabs().get(1), theName, theMin,
				theMax, theDefaultMinValue, theDefaultMaxValue, theX, theY, theW, theH);
		cp5.register(theObject, theIndex, myController);
		return myController;
	}

	public Range addRange(String theName, float theMin, float theMax, float theDefaultMinValue,
			float theDefaultMaxValue, int theX, int theY, int theW, int theH) {
		return addRange(null, "", theName, theMin, theMax, theDefaultMinValue, theDefaultMaxValue, theX, theY, theW,
			theH);
	}

	public Range addRange(final String theName, final float theMin, final float theMax, final int theX, final int theY,
			final int theWidth, final int theHeight) {
		return addRange(null, "", theName, theMin, theMax, theMin, theMax, theX, theY, theWidth, theHeight);
	}

	public Range addRange(final Object theObject, final String theIndex, final String theName, final float theMin,
			final float theMax, final int theX, final int theY, final int theWidth, final int theHeight) {
		return addRange(theObject, theIndex, theName, theMin, theMax, theMin, theMax, theX, theY, theWidth, theHeight);
	}

	public NumberBox addNumberbox(final Object theObject, final String theIndex, final String theName,
			final float theDefaultValue, final int theX, final int theY, final int theWidth, final int theHeight) {
		NumberBox myController = new NumberBox(cp5, (CTab) cp5.getWindow().getTabs().get(1), theName, theDefaultValue,
				theX, theY, theWidth, theHeight);
		cp5.register(theObject, theIndex, myController);
		return myController;
	}

	public NumberBox addNumberbox(final String theName, final int theX, final int theY, final int theWidth,
			final int theHeight) {
		return addNumberbox(null, "", theName, Float.NaN, theX, theY, theWidth, theHeight);
	}

	public NumberBox addNumberbox(final Object theObject, final String theIndex, final String theName, final int theX,
			final int theY, final int theWidth, final int theHeight) {
		return addNumberbox(theObject, theIndex, theName, Float.NaN, theX, theY, theWidth, theHeight);
	}

	public NumberBox addNumberbox(final String theName, final float theDefaultValue, final int theX, final int theY,
			final int theWidth, final int theHeight) {
		return addNumberbox(null, "", theName, theDefaultValue, theX, theY, theWidth, theHeight);
	}

	/**
	 * Knobs can use limited and endless revolutions, custom angles and starting points. There are 2 control
	 * areas for a knob, an area closer to the edge allows 'click-and-adjust' control, a click and drag action
	 * at the inside allows to gradually change the value of a know when dragged. A knob can be controller by
	 * mouse click, drag or mouse-wheel.
	 */
	public Knob addKnob(final Object theObject, final String theIndex, final String theName, final float theMin,
			final float theMax, final float theDefaultValue, final int theX, final int theY, final int theDiameter) {
		Knob myController = new Knob(cp5, (CTab) cp5.getWindow().getTabs().get(1), theName, theMin, theMax,
				theDefaultValue, theX, theY, theDiameter);
		cp5.register(theObject, theIndex, myController);
		return myController;
	}

	public Knob addKnob(final String theName, final float theMin, final float theMax, final int theX, final int theY,
			final int theDiameter) {
		return addKnob(null, "", theName, theMin, theMax, theMin, theX, theY, theDiameter);
	}

	public Knob addKnob(final Object theObject, final String theIndex, final String theName, final float theMin,
			final float theMax, final int theX, final int theY, final int theDiameter) {
		return addKnob(theObject, theIndex, theName, theMin, theMax, theX, theY, theDiameter);
	}

	public Knob addKnob(final String theName, final float theMin, final float theMax, final float theDefaultValue,
			final int theX, final int theY, final int theDiameter) {
		return addKnob(null, "", theName, theMin, theMax, theDefaultValue, theX, theY, theDiameter);
	}

	/**
	 * Multilist is a tree like menu.
	 */
	public MultiList addMultiList(final Object theObject, final String theIndex, final String theName, final int theX,
			final int theY, final int theWidth, final int theHeight) {
		MultiList myController = new MultiList(cp5, (CTab) cp5.getWindow().getTabs().get(1), theName, theX, theY,
				theWidth, theHeight);
		cp5.register(theObject, theIndex, myController);
		return myController;
	}

	public MultiList addMultiList(final String theName, final int theX, final int theY, final int theWidth,
			final int theHeight) {
		return addMultiList(null, "", theName, theX, theY, theWidth, theHeight);
	}

	public TextLabel addTextlabel(final String theName) {
		return addTextlabel(theName, "", 0, 0);
	}

	public TextLabel addTextlabel(final Object theObject, final String theIndex, final String theName,
			final String theText, final int theX, final int theY) {
		TextLabel myController = new TextLabel(cp5, (CTab) cp5.getWindow().getTabs().get(1), theName, theText, theX,
				theY);
		cp5.register(theObject, theIndex, myController);
		return myController;
	}

	public TextLabel addTextlabel(final String theName, final String theText, final int theX, final int theY) {
		return addTextlabel(null, "", theName, theText, theX, theY);
	}

	public TextLabel addTextlabel(final Object theObject, final String theIndex, final String theName,
			final String theText) {
		return addTextlabel(theObject, theIndex, theName, theText, 0, 0);
	}

	public TextLabel addTextlabel(final String theName, final String theText) {
		return addTextlabel(null, "", theName, theText, 0, 0);
	}

	public TextArea addTextarea(final String theName) {
		return addTextarea(theName, "", 0, 0, 199, 99);
	}

	/**
	 * A Textarea is a label without any controller functionality and can be used to leave notes, headlines,
	 * etc when extending the dedicated area of the Textrea, a scrollbar is added on the right.
	 */
	public TextArea addTextarea(final String theName, final String theText, final int theX, final int theY,
			final int theW, final int theH) {
		TextArea myController = new TextArea(cp5, (CTab) cp5.getWindow().getTabs().get(1), theName, theText, theX,
				theY, theW, theH);
		cp5.register(null, "", myController);
		return myController;
	}

	public TextField addTextfield(final String theIndex) {
		return addTextfield(theIndex, 0, 0, 199, 19);
	}

	/**
	 * A Textfield allows single line text input. If text goes beyond the edges of a Textfield box, the text
	 * will automatically scroll. Use Arrow keys to navigate back and forth.
	 */
	public TextField addTextfield(final Object theObject, final String theIndex, final String theName, final int theX,
			final int theY, final int theW, final int theH) {
		TextField myController = new TextField(cp5, (CTab) cp5.getWindow().getTabs().get(1), theName, "", theX, theY,
				theW, theH);
		cp5.register(theObject, theIndex, myController);
		return myController;
	}

	public TextField addTextfield(final String theName, final int theX, final int theY, final int theW, final int theH) {
		return addTextfield(null, "", theName, theX, theY, theW, theH);
	}

	public TextField addTextfield(final Object theObject, final String theIndex, final String theName) {
		return addTextfield(theObject, theIndex, theName, 0, 0, 99, 19);
	}

	// TODO
	// addTextarea theObject

	public Accordion addAccordion(String theName) {
		Accordion myController = new Accordion(cp5, (CTab) cp5.getWindow().getTabs().get(1), theName, 0, 0, 200);
		cp5.register(null, "", myController);
		return myController;
	}

	public Accordion addAccordion(String theName, int theX, int theY, int theWidth) {
		Accordion myController = new Accordion(cp5, (CTab) cp5.getWindow().getTabs().get(1), theName, theX, theY,
				theWidth);
		cp5.register(null, "", myController);
		return myController;
	}

	/**
	 * Use radio buttons for multiple choice options.
	 */
	public ToggleGroup  addToggleGroup (final String theName) {
		return addToggleGroup (theName, 0, 0);
	}

	public ToggleGroup  addToggleGroup (final String theName, final int theX, final int theY) {
		ToggleGroup  myController = new ToggleGroup (cp5, (CTab) cp5.getWindow().getTabs().get(1), theName, theX, theY);
		cp5.register(null, "", myController);
		return myController;
	}

	/**
	 * Use radio buttons for multiple choice options.
	 */
	public ToggleGroup  addRadio(final String theName) {
		return addToggleGroup (theName, 0, 0);
	}

	public ToggleGroup  addRadio(final String theName, final int theX, final int theY) {
		ToggleGroup  myController = new ToggleGroup (cp5, (CTab) cp5.getWindow().getTabs().get(1), theName, theX, theY);
		cp5.register(null, "", myController);
		return myController;
	}

	// TODO
	// addCheckBox theObject

	public ListBox addListBox(final String theName) {
		return addListBox(theName, 0, 0, 99, 199);
	}

	/**
	 * A list box is a list of items a user can choose from. When items exceed the dedicated area of a list
	 * box, a scrollbar is added to the right of the box. the Box can be navigated using mouse click, drag and
	 * the mouse-wheel.
	 */
	public ListBox addListBox(final String theName, final int theX, final int theY, final int theW, final int theH) {
		ListBox myController = new ListBox(cp5, (CTab) cp5.getWindow().getTabs().get(1), theName, theX, theY, theW,
				theH);
		cp5.register(null, "", myController);
		return myController;
	}

	// TODO
	// addListBox theObject

	public DropdownList addDropdownList(final String theName) {
		return addDropdownList(theName, 0, 0, 99, 99);
	}

	public DropdownList addDropdownList(final String theName, final int theX, final int theY, final int theW,
			final int theH) {
		DropdownList myController = new DropdownList(cp5, (CTab) cp5.getWindow().getTabs().get(1), theName, theX,
				theY, theW, theH);
		cp5.register(null, "", myController);
		return myController;
	}

	// TODO
	// addDropdownList theObject

	public ColorPicker addColorPicker(final String theName) {
		return addColorPicker(theName, 0, 0, 255, 10);
	}

	/**
	 * adds a simple RGBA colorpicker.
	 */
	public ColorPicker addColorPicker(final String theName, final int theX, final int theY, final int theW,
			final int theH) {
		ColorPicker myController = new ColorPicker(cp5, (CTab) cp5.getWindow().getTabs().get(1), theName, theX, theY,
				theW, theH);
		cp5.register(null, "", myController);
		return myController;
	}

	// TODO
	// addColorPicker theObject

	public Chart addChart(String theName) {
		return addChart(theName, 0, 0, 200, 100);
	}

	/**
	 * adds chart support to display float array based data.
	 */
	public Chart addChart(String theName, int theX, int theY, int theW, int theH) {
		Chart myController = new Chart(cp5, (CTab) cp5.getWindow().getTabs().get(1), theName, theX, theY, theW, theH);
		cp5.register(null, "", myController);
		return myController;
	}

	/**
	 * A controller group can be used to group controllers for a better organization of single controllers.
	 */
	public Group addGroup(Object theObject, final String theIndex, String theName, int theX, int theY, int theW) {
		Group myController = new Group(cp5, (CGroup<?>) cp5.getWindow().getTabs().get(1), theName, theX,
				theY, theW, 9);
		cp5.register(theObject, theIndex, myController);
		return myController;
	}

	public Group addGroup(String theName, int theX, int theY, int theW) {
		return addGroup(null, "", theName, theX, theY, theW);
	}

	public Group addGroup(String theName) {
		return addGroup(theName, 0, 0);
	}

	public Group addGroup(String theName, int theX, int theY) {
		return addGroup(null, "", theName, theX, theY, 99);
	}

	public TextLabel getTextlabel(String theText, int theX, int theY) {
		return new TextLabel(cp5, theText, theX, theY);
	}

	public TextLabel getTextlabel() {
		return getTextlabel("", 0, 0);
	}

	protected void setCurrentPointer(CGroup<?> theGroup) {
		currentGroupPointer = theGroup;
		isCurrentGroupPointerClosed = false;
	}

	protected void releaseCurrentPointer(CGroup<?> theGroup) {
		if (isCurrentGroupPointerClosed == false) {
			currentGroupPointer = theGroup;
			isCurrentGroupPointerClosed = true;
		} else {
			ControlP5.logger().warning("use .end() first before using .begin() again.");
		}
	}

	public void setAutoAddDirection(int theDirection) {
		if (theDirection == HORIZONTAL) {
			autoDirection = HORIZONTAL;
			return;
		}
		autoDirection = VERTICAL;
	}

	public void setAutoSpacing() {
		CElement.autoSpacing.x = 10;
		CElement.autoSpacing.y = 10;
		CElement.autoSpacing.z = 0;
	}

	public void setAutoSpacing(float theX) {
		CElement.autoSpacing.x = theX;
	}

	public void setAutoSpacing(float theX, float theY) {
		CElement.autoSpacing.x = theX;
		CElement.autoSpacing.y = theY;
	}

	public void setAutoSpacing(float theX, float theY, float theZ) {
		CElement.autoSpacing.x = theX;
		CElement.autoSpacing.y = theY;
		CElement.autoSpacing.z = theZ;
	}

	public void linebreak(CElement<?> theController, boolean theFlag, int theW, int theH, PVector theSpacing) {
		if (currentGroupPointer.autoPosition.x + CElement.autoSpacing.x + theW > cp5.papplet.width) {
			currentGroupPointer.autoPosition.y += currentGroupPointer.tempAutoPositionHeight;
			currentGroupPointer.autoPosition.x = currentGroupPointer.autoPositionOffsetX;
			currentGroupPointer.tempAutoPositionHeight = 0;
			theController.getPosition().x = currentGroupPointer.autoPosition.x;
			theController.getPosition().y = currentGroupPointer.autoPosition.y;
			theFlag = false;
		}

		if (theFlag == true) {
			currentGroupPointer.autoPosition.y += currentGroupPointer.tempAutoPositionHeight;
			currentGroupPointer.autoPosition.x = currentGroupPointer.autoPositionOffsetX;
			currentGroupPointer.tempAutoPositionHeight = 0;
		} else {
			if (theController instanceof Slider) {
				currentGroupPointer.autoPosition.x += theController.getCaptionLabel().getWidth();
			}
			currentGroupPointer.autoPosition.x += CElement.autoSpacing.x + theW;
			if ((theH + theSpacing.y) > currentGroupPointer.tempAutoPositionHeight) {
				currentGroupPointer.tempAutoPositionHeight = theH + theSpacing.y;
			}
		}
	}

	/**
	 * Adds a default slider with a default width of 100 and height of 10. the default value range is from
	 * 0-100.
	 */
	public Slider addSlider(String theName) {
		return addSlider(theName, 0, 100);
	}

	public Slider addSlider(Object theObject, final String theIndex, String theName) {
		return addSlider(theObject, theIndex, theName, 0, 100);
	}

	public Slider addSlider(String theName, float theMin, float theMax) {
		return addSlider(null, "", theName, theMin, theMax);
	}

	public Slider addSlider(Object theObject, final String theIndex, String theName, float theMin, float theMax) {
		int x = (int) currentGroupPointer.autoPosition.x;
		int y = (int) currentGroupPointer.autoPosition.y;
		Slider s = addSlider(theObject, theIndex, theName, theMin, theMax, theMin, x, y, Slider.autoWidth,
			Slider.autoHeight);
		linebreak(s, false, Slider.autoWidth, Slider.autoHeight, CElement.autoSpacing);
		s.moveTo(currentGroupPointer);
		if (autoDirection == VERTICAL) {
			s.linebreak();
		}
		return s;
	}

	/**
	 * Adds a default Button, the default value is 1, width and height are set to the default values of
	 * Button.autoWidth and Button.autoHeight
	 */
	public Button addButton(String theName) {
		return addButton(null, "", theName, 1);
	}

	public Button addButton(Object theObject, final String theIndex, String theName) {
		return addButton(theObject, theIndex, theName, 1);
	}

	public Button addButton(String theName, float theValue) {
		return addButton(null, "", theName, theValue);
	}

	public Button addButton(Object theObject, final String theIndex, String theName, float theValue) {
		int x = (int) currentGroupPointer.autoPosition.x;
		int y = (int) currentGroupPointer.autoPosition.y;
		Button b = addButton(theObject, theIndex, theName, theValue, x, y, Button.autoWidth, Button.autoHeight);
		linebreak(b, false, Button.autoWidth, Button.autoHeight, CElement.autoSpacing);
		b.moveTo(currentGroupPointer);
		return b;
	}

	/**
	 * Adds a default Toggle, the default value is false, width and height are set to the default values of
	 * Toggle.autoWidth and Toggle.autoHeight
	 */
	public Toggle addToggle(String theName) {
		return addToggle(null, "", theName);
	}

	public Toggle addToggle(String theName, boolean theValue) {
		return addToggle(null, "", theName, theValue);
	}

	public Toggle addToggle(Object theObject, final String theIndex, String theName) {
		return addToggle(theObject, theIndex, theName, false);
	}

	public Toggle addToggle(Object theObject, final String theIndex, String theName, boolean theValue) {
		Toggle t = addToggle(theObject, theIndex, theName, theValue, currentGroupPointer.autoPosition.x,
			currentGroupPointer.autoPosition.y, Toggle.autoWidth, Toggle.autoHeight);
		linebreak(t, false, Toggle.autoWidth, Toggle.autoHeight, t.autoSpacing);
		t.moveTo(currentGroupPointer);
		return t;
	}

	public NumberBox addNumberbox(Object theObject, final String theIndex, String theName) {
		int x = (int) currentGroupPointer.autoPosition.x;
		int y = (int) currentGroupPointer.autoPosition.y;
		NumberBox n = addNumberbox(theObject, theIndex, theName, x, y, NumberBox.autoWidth, NumberBox.autoHeight);
		linebreak(n, false, NumberBox.autoWidth, NumberBox.autoHeight, n.autoSpacing);
		n.moveTo(currentGroupPointer);
		return n;
	}

	public NumberBox addNumberbox(String theName) {
		return addNumberbox(null, "", theName);
	}

	public Knob addKnob(Object theObject, final String theIndex, String theName, int theMin, int theMax) {
		Knob n = addKnob(theObject, theIndex, theName, theMin, theMax, theMin,
			(int) currentGroupPointer.autoPosition.x, (int) currentGroupPointer.autoPosition.y, Knob.autoWidth);
		linebreak(n, false, Knob.autoWidth, Knob.autoHeight, n.autoSpacing);
		n.moveTo(currentGroupPointer);
		return n;
	}

	public Knob addKnob(String theName) {
		return addKnob(theName, 0, 100);

	}

	public Knob addKnob(Object theObject, final String theIndex, String theName) {
		return addKnob(theObject, theIndex, theName, 0, 100);
	}

	public Knob addKnob(String theName, int theMin, int theMax) {
		return addKnob(null, "", theName, theMin, theMax);
	}

	/**
	 * 
	 */
	public ControlWindow addControlWindow(String theName) {
		// TODO re-implement ControlWindow
		ControlP5
				.logger()
				.warning(
					"ControlWindow has been disabled currently, please have a look at the changlog.txt file inside the src folder.");
		return null;
		// return addControlWindow(theName, 20, 20, 300, 400);
	}

	public CElement<?> getController(String theName, Object theObject) {
		if (_myObjectToControllerMap.containsKey(theObject)) {
			ArrayList<CInterface<?>> cs = _myObjectToControllerMap.get(theObject);
			for (CInterface<?> c : cs) {
				if (c.getName().equals(theName)) {
					return (CElement<?>) c;
				}
			}
		}
		return null;
	}

	public Object getObjectForController(CInterface<?> theController) {
		for (Iterator<Entry<Object, ArrayList<CInterface<?>>>> it = _myObjectToControllerMap.entrySet()
				.iterator(); it.hasNext();) {

			Entry<Object, ArrayList<CInterface<?>>> entry = it.next();
			Object key = entry.getKey();

			ArrayList<CInterface<?>> value = entry.getValue();
			for (CInterface<?> c : value) {
				if (c.equals(theController)) {
					return key;
				}
			}
		}
		return null;
	}

	public ControlP5Base setPosition(int theX, int theY, Object o) {
		if (o != null && _myObjectToControllerMap.containsKey(o)) {
			ArrayList<CInterface<?>> cs = _myObjectToControllerMap.get(o);
			for (CInterface<?> c : cs) {
				int x = (int) c.getPosition().x + theX;
				int y = (int) c.getPosition().y + theY;
				c.setPosition(x, y);
			}
		}
		return this;
	}

	public ControlP5Base remove(Object theObject) {
		if (theObject != null && _myObjectToControllerMap.containsKey(theObject)) {
			ArrayList<CInterface<?>> cs = _myObjectToControllerMap.get(theObject);
			for (CInterface<?> c : cs) {
				c.remove();
			}
		}
		return this;
	}

	public ControlP5Base setColor(CColor theColor, Object theObject) {
		if (_myObjectToControllerMap.containsKey(theObject)) {
			ArrayList<CInterface<?>> cs = _myObjectToControllerMap.get(theObject);
			for (CInterface<?> c : cs) {
				c.setColour(theColor);
			}
		}
		return this;
	}

	public ControlP5Base listenTo(String theFieldName, Object theObject) {
		String key = theObject.hashCode() + "" + theFieldName.hashCode();
		FieldChangedListener value = new FieldChangedListener(cp5);
		value.listenTo(theObject, theFieldName);
		_myFieldChangedListenerMap.put(key, value);
		return this;
	}

	public ControlP5Base stopListeningTo(String theFieldName, Object theObject) {
		String key = theObject.hashCode() + "" + theFieldName.hashCode();
		_myFieldChangedListenerMap.remove(key);
		return this;
	}

	public ControlP5Base moveTo(CGroup<?> theController, Object theObject) {
		if (_myObjectToControllerMap.containsKey(theObject)) {
			ArrayList<CInterface<?>> cs = _myObjectToControllerMap.get(theObject);
			for (CInterface<?> c : cs) {
				c.moveTo(theController);
			}
		}
		return this;
	}

	/**
	 * prints a list of public methods of requested class into the console. You can specify patterns that will
	 * print methods found with only these particular patterns in their name.
	 * <p>
	 * printed Format: returnType methodName(parameter type)
	 */
	public static void printPublicMethodsFor(Class<?> theClass, String... thePattern) {
		Set<String> set = getPublicMethodsFor(theClass, true, thePattern);
		String str = "";

		str += "/**\n";
		str += "* ControlP5 " + theClass.getSimpleName() + "\n";
		str += "*\n";
		str += "*\n";
		str += "* find a list of public methods available for the " + theClass.getSimpleName() + " Controller\n";
		str += "* at the bottom of this sketch.\n";
		str += "*\n";
		str += "* by Andreas Schlegel, 2012\n";
		str += "* www.sojamo.de/libraries/controlp5\n";
		str += "*\n";
		str += "*/\n\n";
		str += "/*\n";
		str += "a list of all methods available for the " + theClass.getSimpleName() + " Controller\n";
		str += "use ControlP5.printPublicMethodsFor(" + theClass.getSimpleName() + ".class);\n";
		str += "to print the following list into the console.\n\n";
		str += "You can find further details about class " + theClass.getSimpleName() + " in the javadoc.\n\n";
		str += "Format:\n";
		str += "ClassName : returnType methodName(parameter type)\n\n\n";
		for (String s : set) {
			str += s + "\n";
		}
		str += "\n\n*/\n\n";
		System.out.println(str);
	}

	public static void printPublicMethodsFor(Class<?> theClass) {
		printPublicMethodsFor(theClass, "");
	}

	public static Set<String> getPublicMethodsFor(Class<?> theClass) {
		return getPublicMethodsFor(theClass, true, "");
	}

	public static Set<String> getPublicMethodsFor(Class<?> theClass, String... thePattern) {
		return getPublicMethodsFor(theClass, true, thePattern);
	}

	public static Set<String> getPublicMethodsFor(Class<?> theClass, boolean theFlag) {
		return getPublicMethodsFor(theClass, true, "");
	}

	public static Set<String> getPublicMethodsFor(Class<?> theClass, boolean isSuperclass, String... thePattern) {
		Set<String> s = new TreeSet<String>();

		Class<?> c = theClass;
		while (c != null) {
			for (Method method : c.getDeclaredMethods()) {
				if (!method.isAnnotationPresent(Deprecated.class)
						&& !method.isAnnotationPresent(ControlP5.Invisible.class)
						&& method.getModifiers() == Modifier.PUBLIC) {
					for (String p : thePattern) {
						if (p.length() > 0) {
							if (!method.getName().toLowerCase().contains(p.toLowerCase())) {
								continue;
							}
						}
						String params = "";
						for (Class<?> t : method.getParameterTypes()) {
							params += t.getSimpleName() + ", ";
						}
						if (params.length() > 0) {
							params = params.substring(0, params.length() - 2);
						}
						s.add(c.getCanonicalName() + " : "
								+ method.getReturnType().getSimpleName().replace("Object", theClass.getSimpleName())
								+ " " + method.getName() + "(" + params + ") ");
					}
				}
			}

			if (isSuperclass) {
				c = c.getSuperclass();
			} else {
				c = null;
			}
		}
		return s;
	}

	public Toggle addToggle(final String theName, final float theX, final float theY, final int theWidth,
			final int theHeight) {
		return addToggle(null, "", theName, false, theX, theY, theWidth, theHeight);
	}
}

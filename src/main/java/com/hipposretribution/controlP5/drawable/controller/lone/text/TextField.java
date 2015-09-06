package com.hipposretribution.controlP5.drawable.controller.lone.text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PFont;
import processing.event.KeyEvent;

import com.hipposretribution.controlP5.ControlP5;
import com.hipposretribution.controlP5.drawable.controller.CElement;
import com.hipposretribution.controlP5.drawable.controller.CGroup;
import com.hipposretribution.controlP5.drawable.label.AlignmentX;
import com.hipposretribution.controlP5.drawable.label.AlignmentY;
import com.hipposretribution.controlP5.drawable.label.ControlFont;
import com.hipposretribution.controlP5.drawable.label.Label;

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

/**
 * A singleline input textfield, use arrow keys to go back and forth, use backspace to delete characters.
 * Using the up and down arrows lets you cycle through the history of the textfield.
 * 
 * This is the best you can get. Font handling, font switching, measuring, left align, right align, etc. was
 * giving me a big headache. not perfect, i think this is a good compromise.
 * 
 * @example controllers/ControlP5textfield
 * @nosuperclasses Controller Controller
 */
public class TextField extends CElement<TextField> {

	/*
	 * TODO textspacing does not work properly for bitfonts sometimes first row of pixels in a bitfont texture
	 * gets cut off
	 */

	protected boolean isTexfieldActive;

	protected boolean isKeepFocus;

	protected StringBuffer _myTextBuffer = new StringBuffer();

	protected int _myTextBufferIndex = 0;

	protected int _myTextBufferOverflow = 0;

	protected int _myTextBufferIndexPosition = 0;

	public static int cursorWidth = 1;

	protected Map<Integer, TextfieldCommand> keyMapping;

	protected InputFilter _myInputFilter = InputFilter.BITFONT;

	protected List<Integer> ignorelist;

	protected LinkedList<String> _myHistory;

	protected int _myHistoryIndex;

	protected boolean changed;

	protected int len = 0;

	protected int offset = 2;

	protected int margin = 2;

	protected boolean isPasswordMode;

	protected boolean autoclear = true;

	protected int _myColorCursor = 0x88ffffff;

	// TODO Replace this with class
	public enum InputFilter {
		INTEGER(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')), FLOAT(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', '.')), BITFONT(Arrays.asList('\n', '\r', ' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1',
			'2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~')), DEFAULT(
				new LinkedList<Character>());

		final List<Character> allowed;

		InputFilter(List<Character> theList) {
			allowed = theList;
		}

		protected boolean apply(char theCharater) {
			if (allowed.isEmpty()) {
				return true;
			} else {
				return allowed.contains(theCharater);
			}
		}

	}

	/**
	 * Convenience constructor to extend Textfield.
	 * 
	 * @example use/ControlP5extendController
	 * @param theControlP5
	 * @param theName
	 */
	public TextField(ControlP5 theControlP5, String theName) {
		this(theControlP5, theControlP5.getDefaultTab(), theName, "", 0, 0, 199, 19);
		theControlP5.register(theControlP5.papplet, theName, this);
	}

	public TextField(ControlP5 theControlP5, CGroup<?> theParent, String theName, String theDefaultValue, int theX, int theY, int theWidth,
			int theHeight) {
		super(theControlP5, theParent, theName, theX, theY, theWidth, theHeight);
		captionLabel = new Label(cp5, theName.toUpperCase(), 0, 0, color.getCaption());

		_myValueLabel.setFont(cp5.getFont());

		captionLabel.setAlignment(AlignmentX.LEFT, AlignmentY.BOTTOM_OUTSIDE);
		captionLabel.setPaddingX(0);

		_myValueLabel.setFixedSize(true);
		_myValueLabel.setText("");
		_myValueLabel.setWidth(width - margin * 2);
		_myValueLabel.setPadding(0, 0);
		_myValueLabel.setAlignment(AlignmentX.LEFT, AlignmentY.CENTER);
		_myValueLabel.setColor(color.getValue());
		_myValueLabel.setUpperCase(false);

		_myHistory = new LinkedList<String>();
		_myHistory.addFirst("");

		keyMapping = new HashMap<Integer, TextfieldCommand>();
		keyMapping.put(ControlP5.ENTER, new Enter());
		keyMapping.put(ControlP5.DEFAULT, new InsertCharacter());
		keyMapping.put(ControlP5.DELETE, new DeleteCharacter());
		keyMapping.put(ControlP5.BACKSPACE, new DeleteCharacter());
		keyMapping.put(ControlP5.LEFT, new MoveLeft());
		keyMapping.put(ControlP5.RIGHT, new MoveRight());
		keyMapping.put(ControlP5.UP, new MoveUp());
		keyMapping.put(ControlP5.DOWN, new MoveDown());

		ignorelist = new LinkedList<Integer>();
		ignorelist.add(ControlP5.SHIFT);
		ignorelist.add(ControlP5.ALT);
		ignorelist.add(ControlP5.CONTROL);
		ignorelist.add(ControlP5.TAB);
		ignorelist.add(ControlP5.COMMANDKEY);

		setInputFilter(ControlP5.DEFAULT);
		changed = true;

	}

	@Override
	public TextField setWidth(int theWidth) {
		_myValueLabel.setWidth(theWidth);
		return super.setWidth(theWidth);
	}

	@Override
	public TextField setHeight(int theHeight) {
		_myValueLabel.setHeight(theHeight);
		return super.setHeight(theHeight);
	}

	public TextField setFocus(boolean theValue) {
		isTexfieldActive = isActive = theValue;
		changed = true;
		return this;
	}

	/**
	 * check if the textfield is active and in focus.
	 * 
	 * @return boolean
	 */
	public boolean isFocus() {
		return isTexfieldActive;
	}

	public TextField keepFocus(boolean theValue) {
		isKeepFocus = theValue;
		if (isKeepFocus) {
			setFocus(true);
		}
		return this;
	}

	@Override
	public TextField setFont(PFont thePFont) {
		super.setFont(thePFont);
		changed = true;
		return this;
	}

	@Override
	public TextField setFont(ControlFont theControlFont) {
		super.setFont(theControlFont);
		changed = true;
		return this;
	}

	public TextField setPasswordMode(boolean theFlag) {
		isPasswordMode = theFlag;
		return this;
	}

	public TextField setInputFilter(int theInputType) {
		switch (theInputType) {
		case (ControlP5.INTEGER):
			_myInputFilter = InputFilter.INTEGER;
			break;
		case (ControlP5.FLOAT):
			_myInputFilter = InputFilter.FLOAT;
			break;
		case (ControlP5.BITFONT):
			_myInputFilter = InputFilter.BITFONT;
			break;
		default:
			_myInputFilter = InputFilter.DEFAULT;
			break;
		}
		return this;
	}

	@Override
	public TextField setValue(float theValue) {
		// use setText(String) instead
		return this;
	}

	public TextField setValue(String theText) {
		_myTextBuffer = new StringBuffer(theText);
		setIndex(_myTextBuffer.length());
		changed = true;
		return this;
	}

	public TextField setText(String theText) {
		return setValue(theText);
	}

	public TextField clear() {
		// create a new text buffer
		_myTextBuffer = new StringBuffer();
		// reset the buffer index
		setIndex(0);
		return this;
	}

	public TextField setAutoClear(boolean theValue) {
		autoclear = theValue;
		return this;
	}

	public boolean isAutoClear() {
		return autoclear;
	}

	@Override
	protected void mousePressed() {
		if (isActive) {
			// TODO System.out.println("adjust cursor");
		}

		// TODO System.out.println(x + ":" + y);
		setFocus(true);
	}

	@Override
	protected void mouseReleasedOutside() {
		if (isKeepFocus == false) {
			isTexfieldActive = isActive = false;
		}
	}

	public int getIndex() {
		return _myTextBufferIndex;
	}

	public String getText() {
		return _myTextBuffer.toString();
	}

	public TextField setColor(int theColor) {
		getValueLabel().setColor(theColor);
		return this;
	}

	public TextField setColorCursor(int theColor) {
		_myColorCursor = theColor;
		return this;
	}

	@Override
	public void draw(PApplet theApplet) {
		if (changed) {
			updateLabel(theApplet);
			changed = false;
		}
		theApplet.pushStyle();
		theApplet.fill(color.getBackground());
		theApplet.pushMatrix();
		theApplet.translate(position.x, position.y);
		theApplet.rect(0, 0, width, height);
		theApplet.noStroke();

		theApplet.fill(_myColorCursor);
		theApplet.pushMatrix();
		theApplet.pushStyle();

		if (_myTextBufferIndexPosition > len - offset) {
			_myValueLabel.setTextAlignment(AlignmentX.RIGHT);
			theApplet.translate(getWidth() - margin, 0);
			if (isTexfieldActive) {
				theApplet.rect(0, 0, cursorWidth, height);
			}
		} else {
			_myValueLabel.setTextAlignment(AlignmentX.LEFT);
			theApplet.translate(margin, 0);
			if (isTexfieldActive) {
				theApplet.rect(PApplet.max(0, PApplet.min(_myTextBufferIndexPosition, getWidth() - margin)), 0, cursorWidth, height);
			}
		}

		_myValueLabel.draw(theApplet, 0, 0, this);
		theApplet.popStyle();
		theApplet.popMatrix();

		theApplet.fill(isTexfieldActive ? color.getActive() : color.getForeground());
		theApplet.rect(0, 0, width, 1);
		theApplet.rect(0, height - 1, width, 1);
		theApplet.rect(-1, 0, 1, height);
		theApplet.rect(width, 0, 1, height);
		if (isLabelVisible()) {
			captionLabel.draw(theApplet, 0, 0, this);
		}
		theApplet.popMatrix();
		theApplet.popStyle();
	}

	private void updateLabel(PApplet theApplet) {
		if (_myInputFilter == InputFilter.BITFONT) {
			setInputFilter(ControlP5.DEFAULT);
		}
		String str = passCheck(getText());
		String t1 = str;
		int off = margin * 2;
		int ww = ControlFont.getWidthFor(str, _myValueLabel, theApplet);
		if ((ww < getWidth() - off)) {
			_myTextBufferIndexPosition = ControlFont.getWidthFor(t1.substring(0, _myTextBufferIndex), _myValueLabel, theApplet);
			len = getWidth();
		} else {
			char[] c = str.toCharArray();
			int mx = 0;
			int n = 0;
			for (int i = 0; i < c.length; i++) {
				n += theApplet.textWidth(c[i]);
				if (n > _myValueLabel.getWidth() - off) {
					break;
				}
				len = n;
				mx++;
			}
			t1 = "";
			n = 0;
			// this is messed up an occasionally throws ArrayIndexOutOfBounds Exceptions
			// sometimes the beginning of the text is not in order when text length exceeds
			// length of textfield.
			// needs fixing. TODO
			for (int i = PApplet.max(mx, _myTextBufferIndex - 1); i >= 0; i--) {
				try {
					n += theApplet.textWidth(c[i]);
				} catch (Exception e) {
				}
				t1 = c[i] + t1;
				if (n >= _myValueLabel.getWidth() - off - 4) {
					_myTextBufferOverflow = str.indexOf(t1);
					break;
				}
			}
			int strn = PApplet.max(0, PApplet.min(t1.length(), _myTextBufferIndex - _myTextBufferOverflow));
			_myTextBufferIndexPosition = ControlFont.getWidthFor(t1.substring(0, strn), _myValueLabel, theApplet);
		}
		_myValueLabel.setText(t1);
		changed = false;
	}

	private String passCheck(String label) {
		if (!isPasswordMode) {
			return label;
		}
		String newlabel = "";
		for (int i = 0; i < label.length(); i++) {
			newlabel += "*";
		}
		return newlabel;
	}

	@Override
	public void keyEvent(KeyEvent event) {
		if (isUserInteraction && isTexfieldActive && isActive && event.getAction() == KeyEvent.PRESS) {
			if (ignorelist.contains(event.getKeyCode())) {
				return;
			}
			if (keyMapping.containsKey(event.getKeyCode())) {
				keyMapping.get(event.getKeyCode()).execute();
			} else {
				keyMapping.get(ControlP5.DEFAULT).execute();
			}
		}
	}

	/**
	 * make the controller execute a return event. submit the current content of the texfield.
	 * 
	 */
	public TextField submit() {
		keyMapping.get(ControlP5.ENTER).execute();
		return this;
	}

	public String[] getTextList() {
		String[] s = new String[_myHistory.size()];
		_myHistory.toArray(s);
		return s;
	}

	private TextField setIndex(int theIndex) {
		_myTextBufferIndex = theIndex;
		changed = true;
		return this;
	}

	interface TextfieldCommand {

		void execute();
	}

	class InsertCharacter implements TextfieldCommand {

		@Override
		public void execute() {
			if ((cp5.getKey()) == 65535) {
				return;
			}

			if (_myInputFilter.apply(cp5.getKey())) {
				_myTextBuffer.insert(_myTextBufferIndex, cp5.getKey());
				setIndex(_myTextBufferIndex + 1);
			}
		}
	}

	class Enter implements TextfieldCommand {

		@Override
		public void execute() {
			setStringValue(_myTextBuffer.toString());
			broadcast();
			// update current buffer with the last item inside the input history
			_myHistory.set(_myHistory.size() - 1, _myTextBuffer.toString());
			// set the history index to our last item
			_myHistoryIndex = _myHistory.size();
			// add a new and empty buffer to the history
			_myHistory.add("");
			if (autoclear) {
				clear();
			}
		}
	}

	class DeleteCharacter implements TextfieldCommand {

		@Override
		public void execute() {
			if (_myTextBuffer.length() > 0 && _myTextBufferIndex > 0) {
				_myTextBuffer.deleteCharAt(_myTextBufferIndex - 1);
				setIndex(_myTextBufferIndex - 1);
			}
		}
	}

	class MoveLeft implements TextfieldCommand {

		@Override
		public void execute() {
			setIndex((cp5.isMetaDown()) ? 0 : PApplet.max(0, _myTextBufferIndex - 1));
		}
	}

	class MoveRight implements TextfieldCommand {

		@Override
		public void execute() {
			setIndex((cp5.isMetaDown()) ? _myTextBuffer.length() : PApplet.min(_myTextBuffer.length(), _myTextBufferIndex + 1));
		}
	}

	class MoveUp implements TextfieldCommand {

		@Override
		public void execute() {
			if (_myHistoryIndex == 0) {
				return;
			}
			_myHistoryIndex = PApplet.max(0, --_myHistoryIndex);
			_myTextBuffer = new StringBuffer(_myHistory.get(_myHistoryIndex));
			setIndex(_myTextBuffer.length());
		}
	}

	class MoveDown implements TextfieldCommand {

		@Override
		public void execute() {
			if (_myHistoryIndex >= _myHistory.size() - 1) {
				return;
			}

			_myHistoryIndex = PApplet.min(_myHistory.size() - 1, ++_myHistoryIndex);
			_myTextBuffer = new StringBuffer(_myHistory.get(_myHistoryIndex));
			setIndex(_myTextBuffer.length());
		}
	}

}

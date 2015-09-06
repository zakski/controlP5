package com.hipposretribution.controlP5.drawable.controller.group.toggle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

import com.hipposretribution.controlP5.ControlP5;
import com.hipposretribution.controlP5.cast.CBroadcaster;
import com.hipposretribution.controlP5.cast.callback.CEvent;
import com.hipposretribution.controlP5.cast.plug.CPlug;
import com.hipposretribution.controlP5.cast.plug.CPlugToInterface;
import com.hipposretribution.controlP5.drawable.controller.CGroup;
import com.hipposretribution.controlP5.drawable.controller.group.ControlGroup;
import com.hipposretribution.controlP5.drawable.controller.lone.press.Toggle;
import com.hipposretribution.controlP5.drawable.controller.view.ControllerViewType;
import com.hipposretribution.controlP5.drawable.label.AlignmentX;
import com.hipposretribution.controlP5.drawable.label.AlignmentY;

public class ToggleGroup extends ControlGroup<ToggleGroup> implements CPlugToInterface<ToggleGroup> {

	protected ArrayList<CPlug> plugList; // the plugs for objects the controller is attached to

	protected List<Toggle> _myRadioToggles;

	protected boolean[] availableImages = new boolean[3];
	protected PImage[] images = new PImage[3];

	protected int spacingRow = 1;
	protected int spacingColumn = 1;
	protected int itemsPerRow = -1;

	protected int itemHeight = 9;
	protected int itemWidth = 9;

	protected boolean isMultipleChoice;

	protected boolean hasOneAlwaysSelected = true;

	protected AlignmentX alignX = AlignmentX.RIGHT_OUTSIDE;
	protected AlignmentY alignY = AlignmentY.CENTER;

	protected int _myPaddingX = 4;
	protected int _myPaddingY = 0;

	/**
	 * Convenience constructor to extend ToggleGroup.
	 * 
	 * @example use/ControlP5extendController
	 * @param theControlP5
	 * @param theName
	 */
	public ToggleGroup(ControlP5 theControlP5, String theName) {
		this(theControlP5, theControlP5.getDefaultTab(), theName, 0, 0);
		theControlP5.register(theControlP5.papplet, theName, this);
	}

	/**
	 * A CheckBox should only be added to controlP5 by using controlP5.addCheckBox()
	 * 
	 * @exclude
	 * @param theControlP5
	 * @param theParent
	 * @param theName
	 * @param theX
	 * @param theY
	 */
	public ToggleGroup(final ControlP5 theControlP5, final CGroup<?> theParent, final String theName, final int theX, final int theY) {
		super(theControlP5, theParent, theName, theX, theY, 99, 9);
		isBarVisible = false;
		isCollapse = false;
		_myRadioToggles = new ArrayList<Toggle>();
		plugList = new ArrayList<>();
		setItemsPerRow(1);
		cp5.getBroadcaster().plug(cp5.papplet, this, name, new Class[] { int.class });
	}

	protected void updateAlign() {
		for (Toggle t : _myRadioToggles) {
			t.getCaptionLabel().setAlignment(alignX, alignY);
		}
	}

	protected void updateLayout() {
		int nn = 0;
		int xx = 0;
		int yy = 0;
		int n = _myRadioToggles.size();
		for (int i = 0; i < n; i++) {
			Toggle t = _myRadioToggles.get(i);
			t.getPosition().y = yy;
			t.getPosition().x = xx;

			xx += t.getWidth() + spacingColumn;
			nn++;
			if (nn == itemsPerRow) {
				nn = 0;
				_myWidth = xx;
				yy += t.getHeight() + spacingRow;
				xx = 0;
			} else {
				_myWidth = xx;
			}
		}
	}

	protected final void updateValues() {
		_myValue = -1;
		updateValues(true);
	}

	protected void updateValues(boolean theBroadcastFlag) {
		_myArrayValue = new float[_myRadioToggles.size()];

		for (int i = 0; i < _myRadioToggles.size(); i++) {
			Toggle t = (_myRadioToggles.get(i));
			_myArrayValue[i] = t.getValue();
		}

		if (theBroadcastFlag) {
			CEvent myEvent = new CEvent(this, CActionType.BROADCAST);
			cp5.getBroadcaster().broadcast(myEvent);
		}
	}

	protected boolean hasActiveToggles() {
		for (Toggle tog : _myRadioToggles) {
			if (tog.getState()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @exclude
	 */
	@ControlP5.Invisible
	@Override
	public void controlEvent(CEvent theEvent) {
		if (hasOneAlwaysSelected && !hasActiveToggles() && theEvent.getController().getValue() < 1) {
			if (theEvent.getController() instanceof Toggle) {
				Toggle t = ((Toggle) theEvent.getController());
				boolean b = t.isBroadcast();
				t.setBroadcast(false);
				t.setState(true);
				t.setBroadcast(b);
				return;
			}
		}

		if (!isMultipleChoice && theEvent.getController().getValue() >= 1) {
			_myValue = -1;
			int n = _myRadioToggles.size();
			for (int i = 0; i < n; i++) {
				Toggle t = _myRadioToggles.get(i);
				if (t.getState() && !t.equals(theEvent.getController())) {
					t.setState(false);
				} else {
					if (t.isToggled()) {
						_myValue = t.internalValue();
					}
				}
			}
		}
		updateValues(true);
	}

	public final ToggleGroup activateAll() {
		if (!isMultipleChoice) {
			return this;
		}

		for (int i = 0; i < _myRadioToggles.size(); i++) {
			_myRadioToggles.get(i).setState(true);
		}
		updateValues();
		return this;
	}

	public ToggleGroup deactivateAll() {
		if (hasOneAlwaysSelected) {
			return this;
		}

		for (int i = 0; i < _myRadioToggles.size(); i++) {
			_myRadioToggles.get(i).setState(false);
		}

		_myValue = -1;

		updateValues(true);
		return this;
	}

	public ToggleGroup activate(int theIndex) {
		if (!isMultipleChoice) {
			for (int i = 0; i < _myRadioToggles.size(); i++) {
				if (i != theIndex) {
					_myRadioToggles.get(i).setState(false);
				}
			}
		}

		_myRadioToggles.get(theIndex).setState(true);
		_myValue = _myRadioToggles.get(theIndex).internalValue();
		updateValues(true);
		return this;
	}

	/**
	 * @param theIndex
	 */
	public ToggleGroup deactivate(int theIndex) {
		if (!isMultipleChoice && hasOneAlwaysSelected) {
			return this;
		}

		Toggle t = _myRadioToggles.get(theIndex);
		if (t.isActive()) {
			t.setState(false);
			_myValue = -1;
			updateValues(true);
		}
		return this;
	}

	public ToggleGroup activate(String theName) {
		for (int i = 0; i < _myRadioToggles.size(); i++) {
			if (theName.equals(_myRadioToggles.get(i).getName())) {
				return activate(i);
			}
		}
		return this;
	}

	public ToggleGroup deactivate(String theName) {
		if (!isMultipleChoice && hasOneAlwaysSelected) {
			return this;
		}

		for (int i = 0; i < _myRadioToggles.size(); i++) {

			Toggle t = _myRadioToggles.get(i);
			if (theName.equals(t.getName())) {
				t.setState(false);
				_myValue = -1;
				updateValues(true);
				return this;
			}
		}
		return this;
	}

	public final ToggleGroup toggle(int theIndex) {
		if (getState(theIndex)) {
			return deactivate(theIndex);
		} else {
			return activate(theIndex);
		}
	}

	public final ToggleGroup toggle(String theName) {
		if (getState(theName)) {
			return deactivate(theName);
		} else {
			return activate(theName);
		}
	}

	/**
	 * plugs the controller to a list of objects
	 * 
	 * @param theObject
	 * @return Controller
	 */
	public ToggleGroup plugTo(final Object[] theObjects) {
		for (Object o : theObjects) {
			plugTo(o, name);
		}
		return this;
	}

	/**
	 * 
	 * @param theObjects
	 * @param theName
	 * @return Controller
	 */
	public ToggleGroup plugTo(final Object[] theObjects, String theName) {
		for (Object o : theObjects) {
			plugTo(o, theName);
		}
		return this;
	}

	/**
	 * @param theObject
	 * @return Controller
	 */
	public ToggleGroup plugTo(Object theObject) {
		return plugTo(theObject, name);
	}

	public ToggleGroup plugTo(Object theObject, String theName) {
		if ((theObject instanceof PApplet)) {
			unplugFrom(theObject);
		}
		cp5.getBroadcaster().plug(theObject, this, theName);
		cp5.getBroadcaster().plug(theObject, this, CBroadcaster.EVENT_CALLBACK_METHOD);
		return this;
	}

	/**
	 * unplugs the controller from a list of objects
	 * 
	 * @param theObjects
	 * @return
	 */
	public ToggleGroup unplugFrom(final Object[] theObjects) {
		for (Object o : theObjects) {
			unplugFrom(o);
		}
		return this;
	}

	/**
	 * unplugs the Controller for a single object
	 * 
	 * @param theObject
	 * @return Controller
	 */
	public ToggleGroup unplugFrom(final Object theObject) {
		for (Iterator<CPlug> i = plugList.iterator(); i.hasNext();) {
			if (i.next().getObject().equals(theObject)) {
				i.remove();
			}
		}
		return this;
	}

	public boolean isCheckBoxGroup() {
		return isMultipleChoice;
	}

	public boolean isRadioGroup() {
		return !isMultipleChoice;
	}

	public boolean hasPlug(CPlug thePlug) {
		for (CPlug cp : plugList) {
			if (cp.getObject().equals(thePlug.getObject()) && cp.getTargetName().equals(thePlug.getTargetName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to get all Toggles
	 * 
	 * @return a list of all toggles in the group
	 */
	public List<Toggle> getItems() {
		return _myRadioToggles;
	}

	/**
	 * Method to get a Toggle by index
	 * 
	 * @param theIndex - the index of the requested Toggle
	 * @return the Toggle at that index
	 */
	public Toggle getItem(int theIndex) {
		return _myRadioToggles.get(theIndex);
	}

	/**
	 * Method to get a Toggle by name
	 * 
	 * @param theName - the name of the Toggle
	 * @return the first Toggle with that name or null if none exists
	 */
	public Toggle getItem(String theName) {
		for (Toggle t : _myRadioToggles) {
			if (theName.equals(t.getName())) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Gets the state of an item - this can be true (for on) or false (for off) - by index.
	 * 
	 * @param theIndex
	 * @return boolean
	 */
	public boolean getState(int theIndex) {
		if (theIndex < _myRadioToggles.size() && theIndex >= 0) {
			return _myRadioToggles.get(theIndex).getState();
		}
		return false;
	}

	/**
	 * Gets the state of an item - this can be true (for on) or false (for off) - by name.
	 * 
	 * @param theName
	 * @return
	 */
	public boolean getState(String theName) {
		int n = _myRadioToggles.size();
		for (int i = 0; i < n; i++) {
			Toggle t = _myRadioToggles.get(i);
			if (theName.equals(t.getName())) {
				return t.getState();
			}
		}
		return false;
	}

	protected CPlug getPlug(int theIndex) {
		return plugList.get(theIndex);
	}

	/**
	 * 
	 * @return List<ControllerPlug>
	 */
	public ArrayList<CPlug> getPlugs() {
		return plugList;
	}

	public ToggleGroup align(AlignmentX theX, AlignmentY theY) {
		alignX = theX;
		alignY = theY;
		updateAlign();
		return this;
	}

	public ToggleGroup alignX(AlignmentX theX) {
		return align(theX, alignY);
	}

	public ToggleGroup alignY(AlignmentY theY) {
		return align(alignX, theY);
	}

	public ToggleGroup setOneAlwaysSelected(boolean theValue) {
		hasOneAlwaysSelected = theValue;
		return this;
	}

	public ToggleGroup setMultipleChoice(boolean isMultipleChoice) {
		this.isMultipleChoice = isMultipleChoice;
		return this;
	}

	public ToggleGroup setLabelPadding(int thePaddingX, int thePaddingY) {
		_myPaddingX = thePaddingX;
		_myPaddingY = thePaddingY;
		for (Toggle t : _myRadioToggles) {
			t.getCaptionLabel().setPadding(thePaddingX, thePaddingY);
		}
		return this;
	}

	@Override
	public Class<?>[] getAcceptedClassList() {
		return defaultAcceptedClassList;
	}

	/**
	 * 
	 * @param theDefaultImage
	 * @param theOverImage
	 * @param theActiveImage
	 * @return RadioButton
	 */
	public ToggleGroup setImages(PImage theDefaultImage, PImage theOverImage, PImage theActiveImage) {
		setImage(theDefaultImage, ControlP5.DEFAULT);
		setImage(theOverImage, ControlP5.OVER);
		setImage(theActiveImage, ControlP5.ACTIVE);
		return this;
	}

	/**
	 * @param theImage
	 */
	public ToggleGroup setImage(PImage theImage) {
		return setImage(theImage, ControlP5.DEFAULT);
	}

	/**
	 * @param theImage
	 * @param theState use Controller.DEFAULT (background), or Controller.OVER (foreground), or
	 *            Controller.ACTIVE (active)
	 * @return
	 */
	public ToggleGroup setImage(PImage theImage, int theState) {
		if (theImage != null) {
			images[theState] = theImage;
			availableImages[theState] = true;
			for (int i = 0; i < _myRadioToggles.size(); i++) {
				_myRadioToggles.get(i).setImage(theImage, theState);
			}
		}
		return this;
	}

	public ToggleGroup setSize(PImage theImage) {
		return setSize(theImage.width, theImage.height);
	}

	/**
	 * set the height of a radioButton/checkBox item. by default the height is 11px. in order to recognize a
	 * custom height, the itemHeight has to be set before adding items to a radioButton/checkBox.
	 * 
	 * @param theItemHeight
	 */
	public ToggleGroup setItemHeight(int theItemHeight) {
		itemHeight = theItemHeight;
		for (Toggle t : _myRadioToggles) {
			t.setHeight(theItemHeight);
		}
		updateLayout();
		return this;
	}

	/**
	 * set the width of a radioButton/checkBox item. by default the width is 11px. in order to recognize a
	 * custom width, the itemWidth has to be set before adding items to a radioButton/checkBox.
	 * 
	 * @param theItemWidth
	 */
	public ToggleGroup setItemWidth(int theItemWidth) {
		itemWidth = theItemWidth;
		for (Toggle t : _myRadioToggles) {
			t.setWidth(theItemWidth);
		}
		updateLayout();
		return this;
	}

	@Override
	public ToggleGroup setSize(int theWidth, int theHeight) {
		setItemWidth(theWidth);
		setItemHeight(theHeight);
		return this;
	}

	/**
	 * Items of a radioButton or a checkBox are organized in columns and rows. SetItemsPerRow sets the limit
	 * of items per row. items exceeding the limit will be pushed to the next row.
	 * 
	 * @param theValue
	 */
	public ToggleGroup setItemsPerRow(final int theValue) {
		itemsPerRow = theValue;
		updateLayout();
		return this;
	}

	/**
	 * Sets the spacing in pixels between columns.
	 * 
	 * @param theSpacing
	 */
	public ToggleGroup setSpacingColumn(final int theSpacing) {
		spacingColumn = theSpacing;
		updateLayout();
		return this;
	}

	/**
	 * Sets the spacing in pixels between rows.
	 * 
	 * @param theSpacing
	 */
	public ToggleGroup setSpacingRow(final int theSpacing) {
		spacingRow = theSpacing;
		updateLayout();
		return this;
	}

	/**
	 * Sets the value for all RadioButton items according to the values of the array passed on. 0 will turn
	 * off an item, any other value will turn it on.
	 */
	@Override
	public ToggleGroup setArrayValue(float[] theArray) {
		for (int i = 0; i < theArray.length; i++) {
			if (_myArrayValue[i] != theArray[i]) {
				if (theArray[i] == 0) {
					deactivate(i);
				} else {
					activate(i);
				}
			}
		}
		super.setArrayValue(theArray);
		return this;
	}

	/**
	 * @param theName
	 * @param theValue
	 * @return
	 */
	public ToggleGroup addItem(final String theName, final float theValue) {
		Toggle t = cp5.addToggle(theName, 0, 0, itemWidth, itemHeight);
		t.getCaptionLabel().setAlignment(AlignmentX.RIGHT_OUTSIDE, AlignmentY.CENTER).setPaddingY(0);
		t.updateDisplayMode(ControllerViewType.DEFAULT);
		t.setImages(images[0], images[1], images[2]);
		t.setSize(images[0]);
		addItem(t, theValue);
		return this;
	}

	/**
	 * @param theToggle
	 * @param theValue
	 * @return
	 */
	public ToggleGroup addItem(final Toggle theToggle, final float theValue) {
		theToggle.setGroup(this);
		theToggle.setMoveable(false);
		theToggle.setInternalValue(theValue);
		theToggle.setBroadcast(false);
		_myRadioToggles.add(theToggle);
		if (_myRadioToggles.size() == 1 && hasOneAlwaysSelected) {
			theToggle.setState(true);
		}
		updateLayout();
		getColour().copyTo(theToggle);
		theToggle.addListener(this);
		updateValues(false);
		return this;
	}

	public ToggleGroup addPlug(final CPlug thePlug) {
		if (!hasPlug(thePlug))
			plugList.add(thePlug);
		return me;
	}

	/**
	 * @param theName
	 */
	public ToggleGroup removeItem(final String theName) {
		int n = _myRadioToggles.size();
		for (int i = 0; i < n; i++) {
			if ((_myRadioToggles.get(i)).getName().equals(theName)) {
				(_myRadioToggles.get(i)).removeListener(this);
				_myRadioToggles.remove(i);
			}
		}
		updateValues(false);
		return this;
	}

	public ToggleGroup removePlug(final CPlug thePlug) {
		plugList.remove(thePlug);
		return me;
	}

	/**
	 * @exclude {@inheritDoc}
	 */
	@Override
	public String getInfo() {
		return (isCheckBoxGroup()) ? "type:\tCheckBox\n" : "type:\tRadioButton\n" + super.getInfo();
	}
}

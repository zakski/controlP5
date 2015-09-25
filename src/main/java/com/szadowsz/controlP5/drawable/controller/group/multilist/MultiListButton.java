package com.szadowsz.controlP5.drawable.controller.group.multilist;

import com.szadowsz.controlP5.CP;
import com.szadowsz.controlP5.ControlP5;
import com.szadowsz.controlP5.drawable.controller.CElement;
import com.szadowsz.controlP5.drawable.controller.CGroup;
import com.szadowsz.controlP5.drawable.controller.lone.press.Button;
import com.szadowsz.controlP5.drawable.label.style.AlignmentX;
import com.szadowsz.controlP5.drawable.label.style.AlignmentY;

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
 * Used by MultiList.
 * 
 * @example controllers/ControlP5multiList
 * @nosuperclasses Controller Controller
 */
public class MultiListButton extends Button implements MultiListInterface {

	// private MultiListInterface parent;

	private CElement<?> parent;

	private MultiList root;

	private int[] _myRect = new int[4];

	protected int _myDirection = ControlP5.RIGHT;

	private boolean isUpperCase = true;

	/**
	 * 
	 * @param theProperties ControllerProperties
	 * @param theParent MultiListInterface
	 * @param theRoot MultiList
	 */
	protected MultiListButton(ControlP5 theControlP5, String theName, float theValue, int theX, int theY, int theWidth,
			int theHeight, CElement<?> theParent, MultiList theRoot) {
		super(theControlP5, (CGroup<?>) theRoot.getParent(), theName, theValue, theX, theY, theWidth,
				theHeight);
		parent = theParent;
		root = theRoot;
		updateRect(position.x, position.y, width, height);
		captionLabel.setAlignment(AlignmentX.LEFT, AlignmentY.CENTER);
	}

	@Override
	public MultiListButton toUpperCase(boolean theValue) {
		isUpperCase = theValue;
		for (CElement<?> c : getSubelements()) {
			c.getCaptionLabel().setUpperCase(isUpperCase);
		}
		captionLabel.setUpperCase(isUpperCase);
		return this;
	}

	@Override
	public void remove() {
		int myYoffset = 0;
		for (int i = 0; i < parent.getSubelements().size(); i++) {
			if (parent.getSubelements().get(i) == this) {
				myYoffset = height + 1;
			}
			((MultiListButton) parent.getSubelements().get(i)).updateLocation(0, -myYoffset);
		}

		if (parent != null) {
			removeListener(root);
			parent.remove(this);
		}
		if (cp5 != null) {
			removeListener(root);
			cp5.remove(this);
		}
		for (int i = 0; i < getSubelements().size(); i++) {
			((MultiListButton) getSubelements().get(i)).remove();
		}
	}

	@Override
	public int getDirection() {
		return _myDirection;
	}

	protected void setDirection(int theDirection) {
		_myDirection = theDirection;
	}

	public void updateRect(float theX, float theY, float theW, float theH) {
		_myRect = new int[] { (int) theX, (int) theY, (int) theW, (int) theH };
	}

	@Override
	public void updateLocation(float theX, float theY) {
		position.x += theX;
		position.y += theY;
		updateRect(position.x, position.y, width, height);
		for (int i = 0; i < getSubelements().size(); i++) {
			((MultiListInterface) getSubelements().get(i)).updateLocation(theX, theY);
		}
	}

	/**
	 * set the width of a multlist button.
	 * 
	 * @param theWidth int
	 */
	@Override
	public MultiListButton setWidth(int theWidth) {
		// negative direction
		int dif = (_myDirection == ControlP5.LEFT) ? theWidth - width : 0;
		width = theWidth;
		updateLocation(-dif, 0);
		return this;
	}

	/**
	 * set the height of a multlist button.
	 * 
	 * @param theHeight int
	 */
	@Override
	public MultiListButton setHeight(int theHeight) {
		int difHeight = height;
		height = theHeight;
		difHeight = height - difHeight;
		int myYoffset = 0;
		for (int i = 0; i < parent.getSubelements().size(); i++) {
			if (parent.getSubelements().get(i) instanceof MultiListInterface) {
				((MultiListInterface) parent.getSubelements().get(i)).updateLocation(0, myYoffset);
				if ((parent.getSubelements().get(i)) == this) {
					myYoffset = difHeight;
				}
			}
		}
		updateLocation(0, 0);
		return this;
	}

	/**
	 * add a new button to the sublist of this multilist button.
	 * 
	 * @param theName String
	 * @param theValue int
	 * @return MultiListButton
	 */
	public MultiListButton add(String theName, float theValue) {
		int myHeight = -(height + 1);
		for (int i = 0; i < getSubelements().size(); i++) {
			myHeight += (getSubelements().get(i)).getHeight() + 1;
		}
		// negative direction, this is static now, make it dynamic depending on
		// the
		// location of the list.
		int xx = ((int) position.x + (width + 1));
		MultiListButton b = new MultiListButton(cp5, theName, theValue, xx, (int) position.y + (height + 1) + myHeight,
				width, height, this, root);
		b.isMoveable = false;
		b.toUpperCase(isUpperCase);
		b.setVisible(false);
		cp5.register(null, "", b);
		b.addListener(root);
		getSubelements().add(b);
		updateRect(xx, position.y, width, (height + 1) + myHeight);
		return b;
	}

	@Override
	protected void onEnter() {
		if (!root.isUpdateLocation) {
			isActive = true;
			root.occupied(true);
			root.mostRecent = this;
			if (parent instanceof MultiListInterface) {
				((MultiListInterface) parent).close(this);
			}
			open();
		}
	}

	@Override
	public void onLeave() {
		if (parent instanceof MultiListInterface) {
			if (!((MultiListInterface) parent).observe() && !root.isUpdateLocation && root.mostRecent == this) {
				isActive = false;
				root.occupied(false);
			}
		}
	}

	@Override
	public void mouseReleasedOutside() {
		// !!! other than in the Button class, calling mouseReleased here
		// conflicts with mouseReleased();
	}

	@Override
	public boolean observe() {
		return CP.inside(_myRect, cp5.mouseX(), cp5.mouseY());
	}

	@Override
	public void close(MultiListInterface theInterface) {
		for (int i = 0; i < getSubelements().size(); i++) {
			if (theInterface != (MultiListInterface) getSubelements().get(i)) {
				((MultiListInterface) getSubelements().get(i)).close();
			}
		}

	}

	@Override
	public void close() {
		for (int i = 0; i < getSubelements().size(); i++) {
			((MultiListButton) getSubelements().get(i)).close();
			((MultiListButton) getSubelements().get(i)).setVisible(false);
		}
	}

	@Override
	public void open() {
		for (int i = 0; i < getSubelements().size(); i++) {
			((MultiListButton) getSubelements().get(i)).setVisible(true);
		}
	}
}

package com.hipposretribution.controlP5.drawable.controller.group;

import com.hipposretribution.controlP5.ControlP5;
import com.hipposretribution.controlP5.cast.callback.CEvent;
import com.hipposretribution.controlP5.drawable.controller.CInterface;

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
 * <p>
 * The Accordion is a list of ControlGroups which can be expanded and collapsed. Only one item can be open at
 * a time.
 * </p>
 * 
 * @see com.hipposretribution.controlP5.drawable.controller.CGroup
 * @see com.hipposretribution.controlP5.drawable.controller.group.ControlGroup
 * @example controllers/ControlP5accordion
 */
public class Accordion extends ControlGroup<Accordion> {

	public enum AccordionMode {
		SINGLE, MULTI;
	}

	protected int spacing = 1;
	protected int minHeight = 100;
	protected int itemheight;
	protected AccordionMode mode = AccordionMode.SINGLE;

	/**
	 * Convenience constructor to extend Accordion.
	 * 
	 * @example use/ControlP5extendController
	 * @param theControlP5
	 * @param theName
	 */
	public Accordion(ControlP5 theControlP5, String theName) {
		this(theControlP5, theControlP5.getDefaultTab(), theName, 0, 0, 200);
		theControlP5.register(theControlP5.papplet, theName, this);
	}

	public Accordion(ControlP5 theControlP5, CTab theTab, String theName, int theX, int theY, int theW) {
		super(theControlP5, theTab, theName, theX, theY, theW, 9);
		hideBar();
	}

	/**
	 * Adds items of type ControlGroup to the Accordion, only ControlGroups can be added.
	 * 
	 * @exclude
	 * @param theGroup
	 * @return Accordion
	 */
	public Accordion addItem(ControlGroup<?> theGroup) {
		theGroup.close();
		theGroup.moveTo(this);
		theGroup.activateEvent(true);
		theGroup.addListener(this);
		theGroup.setMoveable(false);
		if (theGroup.getBackgroundHeight() < minHeight) {
			theGroup.setBackgroundHeight(minHeight);
		}
		controllers.add(theGroup);
		updateItems();
		return this;
	}

	/**
	 * Removes a ControlGroup from the accordion AND from controlP5 remove(ControllerInterface theGroup)
	 * overwrites it's super method. if you want to remove a ControlGroup only from the accordion, use
	 * removeItem(ControlGroup).
	 * 
	 * @see com.hipposretribution.controlP5.drawable.controller.group.Accordion#removeItem(ControlGroup)
	 * @return ControllerInterface
	 */
	@Override
	public Accordion remove(CInterface<?> theGroup) {
		if (theGroup instanceof ControlGroup<?>) {
			controllers.remove(theGroup);
			((ControlGroup<?>) theGroup).removeListener(this);
			updateItems();
		}
		super.remove(theGroup);
		return this;
	}

	/**
	 * Removes a ControlGroup from the accordion and puts it back into the default tab of controlP5. if you
	 * dont have access to a ControlGroup via a variable, use controlP5.group("theNameOfTheGroup") which will
	 * return a
	 * 
	 * @return Accordion
	 */
	public Accordion removeItem(ControlGroup<?> theGroup) {
		if (theGroup == null) {
			return this;
		}
		controllers.remove(theGroup);
		theGroup.removeListener(this);
		theGroup.moveTo(cp5.getWindow());
		updateItems();
		return this;
	}

	/**
	 * UpdateItems is called when changes such as remove, change of height is performed on an accordion.
	 * updateItems() is called automatically for such cases, but by calling updateItems manually an update
	 * will be forced.
	 * 
	 * @return Accordion
	 */
	public Accordion updateItems() {
		int n = 0;
		setWidth(_myWidth);

		for (CInterface<?> cg : controllers.get()) {
			if (cg instanceof ControlGroup) {
				n += ((ControlGroup<?>) cg).getBarHeight() + spacing;
				cg.setPosition(0, n);
				if (((ControlGroup<?>) cg).isOpen()) {
					n += ((ControlGroup<?>) cg).getBackgroundHeight();
				}
			}
		}
		return this;
	}

	/**
	 * Sets the minimum height of a collapsed item, default value is 100.
	 * 
	 * @param theHeight
	 * @return Accordion
	 */
	public Accordion setMinItemHeight(int theHeight) {
		minHeight = theHeight;
		for (CInterface<?> cg : controllers.get()) {
			if (cg instanceof ControlGroup<?>) {
				if (((ControlGroup<?>) cg).getBackgroundHeight() < minHeight) {
					((ControlGroup<?>) cg).setBackgroundHeight(minHeight);
				}
			}
		}
		updateItems();
		return this;
	}

	public int getMinItemHeight() {
		return minHeight;
	}

	public Accordion setItemHeight(int theHeight) {
		itemheight = theHeight;
		for (CInterface<?> cg : controllers.get()) {
			if (cg instanceof ControlGroup) {
				((ControlGroup<?>) cg).setBackgroundHeight(itemheight);
			}
		}
		updateItems();
		return this;
	}

	public int getItemHeight() {
		return itemheight;
	}

	@Override
	public Accordion setWidth(int theWidth) {
		super.setWidth(theWidth);
		for (CInterface<?> cg : controllers.get()) {
			if (cg instanceof ControlGroup) {
				((ControlGroup<?>) cg).setWidth(theWidth);
			}
		}
		return this;
	}

	/**
	 * @exclude {@inheritDoc}
	 */
	@Override
	@ControlP5.Invisible
	public void controlEvent(CEvent theEvent) {
		if (theEvent.isGroup()) {
			int n = 0;
			for (CInterface<?> cg : controllers.get()) {
				if (cg instanceof ControlGroup) {
					n += ((ControlGroup<?>) cg).getBarHeight() + spacing;
					cg.setPosition(0, n);
					if (mode == AccordionMode.SINGLE) {
						if (cg == theEvent.getGroup() && ((ControlGroup<?>) cg).isOpen()) {
							n += ((ControlGroup<?>) cg).getBackgroundHeight();
						} else {
							((ControlGroup<?>) cg).close();
						}
					} else {
						if (((ControlGroup<?>) cg).isOpen()) {
							n += ((ControlGroup<?>) cg).getBackgroundHeight();
						}
					}
				}
			}
		}
	}

	@Override
	public Accordion open() {
		int[] n = new int[controllers.size()];
		for (int i = 0; i < controllers.size(); i++) {
			n[i] = i;
		}
		return open(n);
	}

	@Override
	public Accordion close() {
		int[] n = new int[controllers.size()];
		for (int i = 0; i < controllers.size(); i++) {
			n[i] = i;
		}
		return close(n);
	}

	public Accordion open(int... theId) {
		if (theId[0] == -1) {
			return open();
		}
		int n = 0, i = 0;
		for (CInterface<?> cg : controllers.get()) {
			if (cg instanceof ControlGroup) {
				boolean a = false;
				for (int j = 0; j < theId.length; j++) {
					if (theId[j] == i) {
						a = true;
					}
				}
				boolean b = ((ControlGroup<?>) cg).isOpen() || a ? true : false;
				i++;
				n += ((ControlGroup<?>) cg).getBarHeight() + spacing;
				cg.setPosition(0, n);
				if (b) {
					n += ((ControlGroup<?>) cg).getBackgroundHeight();
					((ControlGroup<?>) cg).open();
				}
			}
		}
		return this;
	}

	public Accordion close(int... theId) {
		if (theId[0] == -1) {
			return close();
		}
		int n = 0, i = 0;
		for (CInterface<?> cg : controllers.get()) {
			if (cg instanceof ControlGroup) {
				boolean a = false;
				for (int j = 0; j < theId.length; j++) {
					if (theId[j] == i) {
						a = true;
					}
				}
				boolean b = !((ControlGroup<?>) cg).isOpen() || a ? true : false;
				i++;
				n += ((ControlGroup<?>) cg).getBarHeight() + spacing;
				((ControlGroup<?>) cg).setPosition(0, n);
				if (b) {
					((ControlGroup<?>) cg).close();
				} else {
					n += ((ControlGroup<?>) cg).getBackgroundHeight();
				}
			}
		}
		return this;
	}

	public Accordion setCollapseMode(AccordionMode theMode) {
		mode = theMode;
		if (mode == AccordionMode.SINGLE) {
			int n = 0;
			for (int i = 0; i < controllers.size(); i++) {
				CInterface<?> cg = controllers.get(i);
				if (cg instanceof ControlGroup) {

					n += ((ControlGroup<?>) cg).getBarHeight() + spacing;
					cg.setPosition(0, n);

					if (i == 0) {
						n += ((ControlGroup<?>) cg).getBackgroundHeight();
					} else {
						((ControlGroup<?>) cg).close();
					}

				}
			}
		}
		return this;
	}
}

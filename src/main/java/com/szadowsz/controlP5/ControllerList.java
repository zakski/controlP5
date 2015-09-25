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

import com.szadowsz.controlP5.drawable.CDrawableInterface;
import com.szadowsz.controlP5.drawable.controller.CInterface;

import java.util.List;
import java.util.Vector;

/**
 * Stores objects of type ControllerInterface and CDrawable, mainly for internal use.
 */
public class ControllerList {

	protected List<CInterface<?>> controllers;

	protected List<CDrawableInterface> drawables;

	public ControllerList() {
		controllers = new Vector<CInterface<?>>();
		drawables = new Vector<CDrawableInterface>();
	}

	public void add(CInterface<?> theController) {
		if (controllers.indexOf(theController) < 0) {
			controllers.add(theController);
		}
	}

	public void remove(CInterface<?> theController) {
		controllers.remove(theController);
	}

	public void addDrawable(CDrawableInterface theController) {
		if (drawables.indexOf(theController) < 0) {
			drawables.add(theController);
		}
	}

	public void removeDrawable(CDrawableInterface theController) {
		drawables.remove(theController);
	}

	public CInterface<?> get(int theIndex) {
		return controllers.get(theIndex);
	}

	public List<CInterface<?>> get() {
		return controllers;
	}

	public CDrawableInterface getDrawable(int theIndex) {
		return drawables.get(theIndex);
	}

	public List<CDrawableInterface> getDrawables() {
		return drawables;
	}

	public int sizeDrawable() {
		return drawables.size();
	}

	public int size() {
		return controllers.size();
	}

	public void clear() {
		controllers.clear();
	}

	public void clearDrawable() {
		drawables.clear();
	}

}

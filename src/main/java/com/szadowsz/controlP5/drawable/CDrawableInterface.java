package com.szadowsz.controlP5.drawable;

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
 * The CDrawable interface is used to draw controllers and labels, primarily for internal use.
 */
public interface CDrawableInterface<T> extends CDrawableBaseInterface {
	/*
	 * NOTE: all controller/label drawing-specific functions should be declared originally in here to ensure
	 * consistency.
	 */

	/**
	 * Method to get the object's drawing visibility
	 * 
	 * @return true if should be visible, false otherwise
	 */
	public boolean isVisible();

	/**
	 * Method to set the object's drawing visibility
	 * 
	 * @param theVisibleFlag - the value to set the flag to
	 * @return the object
	 */
	public T setVisible(final boolean theVisibleFlag);

	/**
	 * Method to set the object's width
	 * 
	 * @param theWidth - the width to set
	 * @return the object
	 */
	public T setWidth(int theWidth);

	/**
	 * Method to set the object's height
	 * 
	 * @param theHeight - the height to set
	 * @return the object
	 */
	public T setHeight(int theHeight);

	/**
	 * Method to get the object's width
	 * 
	 * @return the object's width
	 */
	public int getWidth();

	/**
	 * Method to get the object's height
	 * 
	 * @return the object's height
	 */
	public int getHeight();

}

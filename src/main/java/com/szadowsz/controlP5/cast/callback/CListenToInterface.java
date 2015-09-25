/**
 * controlP5 is a processing GUI library.
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
 * 
 * @author Zakski
 * @since 10/07/2014
 * @version Z-0.0.1
 */
package com.szadowsz.controlP5.cast.callback;

/**
 * Interface for Controllers to manage Listeners
 * 
 * @author zakski
 *
 * @param <T> the class that is implementing the interface
 */
public interface CListenToInterface<T> extends CCallbackBase {

	/**
	 * Method to get the size of the CallbackListener list
	 * 
	 * @return the attached listener count
	 */
	public int getListenerSize();

	/**
	 * Method to add 1 to n CCallbackListeners to this object
	 *
	 * @param theListeners - the listeners to attach
	 *
	 * @return the updated object
	 */
	public T addListener(CListener... theListeners);

	/**
	 * Method to remove 1 to n CCallbackListeners from this object
	 *
	 * @param theListeners - the listeners to remove
	 *
	 * @return the updated object
	 */
	public T removeListener(CListener... theListeners);

	/**
	 * Method to remove all listeners to this object
	 *
	 *
	 * @return the updated object
	 */
	public T removeAllListeners();

}

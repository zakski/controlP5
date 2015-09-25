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
package com.szadowsz.controlP5.cast.plug;

public interface CPlugBase {

	public final static Class<?>[] defaultAcceptedClassList = {boolean.class, int.class, float.class, String.class };
	
	
	/**
	 * The Type of Plug we are using
	 * 
	 * INVALID - basically non-functional
	 * 
	 * METHOD - plug will invoke a method
	 * 
	 * FIELD - plug will change a field value
	 * 
	 * EVENT - special plug to invoke Control/Callback Event methods
	 */
	public enum CPlugCallType {
		INVALID, METHOD, FIELD, EVENT; // TODO consider removing Event
	}
}

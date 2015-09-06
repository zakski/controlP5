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
package com.hipposretribution.controlP5.cast.plug;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The ControllerPlug is used to do all the reflection procedures to link a controller to a variable or
 * function inside your main application. For Internal Usage alongside ControlBroadcaster.
 * 
 * @see com.hipposretribution.controlP5.behaviour.ControlBroadcaster
 * @example use/ControlP5plugTo
 */
public class CPlug implements CPlugBase {

	private Object boundObject; // the object that we have bound the controller to
	private Method boundMethod; // the object's method that we intend to invoke
	private Field boundField; // the object's field that we intend to invoke
	private CPlugCallType boundType = CPlugCallType.INVALID; // What we will invoke, i.e. method or field

	private Class<?> parameterClass; // the field/method parameter type
	private String targetName; // the name of the field / method that we have targeted

	private static final Set<Class<?>> WRAPPERS = Collections.unmodifiableSet(new HashSet<Class<?>>(Arrays.asList(Boolean.class, Character.class,
		Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class)));

	public CPlug(Object theObject, Field theField, CPlugCallType thePlugType) {
		this(theObject, thePlugType);

		boundField = theField;
		targetName = theField.getName();
		parameterClass = theField.getType();
	}

	public CPlug(Object theObject, Method theMethod, CPlugCallType thePlugType) {
		this(theObject, thePlugType);

		boundMethod = theMethod;
		targetName = theMethod.getName();
		parameterClass = (theMethod.getParameterTypes().length > 0) ? theMethod.getParameterTypes()[0] : null;
	}

	protected CPlug(Object theObject, CPlugCallType thePlugType) {
		boundObject = theObject;
		boundType = thePlugType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boundObject == null) ? 0 : boundObject.hashCode());
		result = prime * result + ((boundType == null) ? 0 : boundType.hashCode());
		result = prime * result + ((targetName == null) ? 0 : targetName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CPlug other = (CPlug) obj;
		if (boundObject == null) {
			if (other.boundObject != null)
				return false;
		} else if (!boundObject.equals(other.boundObject))
			return false;
		if (boundType != other.boundType)
			return false;
		if (targetName == null) {
			if (other.targetName != null)
				return false;
		} else if (!targetName.equals(other.targetName))
			return false;
		return true;
	}

	/**
	 * Method to convert the passed in Object to the field/method parameter that we are expecting
	 * 
	 * @param theValue - the Object to convert
	 * @return an Object the same type as the field/method parameter that we are expecting
	 */
	private Object convertToType(Object theValue) {
		// if they are the same we don't do anything - best case
		if (theValue.getClass() == parameterClass) {
			return theValue;

		} else if (WRAPPERS.contains(theValue.getClass())) {
			return convertToPrimitive(convertToDouble(theValue));

		} else {
			return null;
		}
	}

	/**
	 * Method to convert the passed in Object primitive to the primitive field/method parameter that we are expecting
	 * 
	 * @param theValue - the Object to convert
	 * @return an Object the same type as the field/method parameter that we are expecting
	 */
	private double convertToDouble(Object theValue) {

		if (theValue instanceof Number) {
			return ((Number) theValue).doubleValue();

		} else if (theValue instanceof Character) {
			return ((Character) theValue).charValue();

		} else if (theValue instanceof Boolean) {
			return ((Boolean) theValue).booleanValue() ? 1 : 0;

		} else {
			return 0;
		}
	}

	/**
	 * Method to convert the passed in Object primitive to the primitive field/method parameter that we are expecting
	 * 
	 * @param theValue - the Object to convert
	 * @return an Object the same type as the field/method parameter that we are expecting
	 */
	private Object convertToPrimitive(double theValue) {
		if (parameterClass == double.class) {
			return theValue;

		} else if (parameterClass == float.class) {
			return (float) theValue;

		} else if (parameterClass == long.class) {
			return (long) theValue;

		} else if (parameterClass == int.class) {
			return (int) theValue;

		} else if (parameterClass == short.class) {
			return (short) theValue;

		} else if (parameterClass == char.class) {
			return (char) theValue;

		} else if (parameterClass == byte.class) {
			return (byte) theValue;

		} else if (parameterClass == boolean.class) {
			return (theValue > 0.5) ? true : false;
		} else {
			return null;
		}
	}

	/**
	 * Method to check if the type of plug matches
	 * 
	 * @param theType - the type to check the plug against
	 * @return true if they are of the same type, false otherwise
	 */
	public boolean checkType(CPlugCallType theType) {
		return boundType == theType;
	}

	/**
	 * Method to get the object that we have connected the controller to
	 * 
	 * @return the object
	 */
	public Object getObject() {
		return boundObject;
	}

	/**
	 * Method to get the object's method that we have connected the controller to
	 * 
	 * @return the method
	 */
	public Method getMethod() {
		return boundMethod;
	}

	/**
	 * Method to get the object's field that we have connected the controller to
	 * 
	 * @return the field
	 */
	public Field getField() {
		return boundField;
	}

	/**
	 * Method to convert the parameter to the correct method parameter array type
	 * 
	 * @param theValue - the value we are passing in
	 * @return an array of parameter that are used by the method we invoke
	 */
	public Object[] getMethodParameter(Object theValue) {
		return new Object[] { getFieldParameter(theValue) };
	}

	/**
	 * Method to convert the parameter to the correct field type
	 * 
	 * @param theValue - the value we are passing in
	 * @return an object the same type as the field that we invoke
	 */
	public Object getFieldParameter(Object theValue) {
		return convertToType(theValue);
	}

	public String getTargetName() {
		return targetName;
	}

	public Class<?> getParameterClass() {
		return parameterClass;
	}
}

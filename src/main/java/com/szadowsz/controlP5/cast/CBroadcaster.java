package com.szadowsz.controlP5.cast;

import com.szadowsz.controlP5.ControlP5;
import com.szadowsz.controlP5.cast.callback.CEvent;
import com.szadowsz.controlP5.cast.callback.CListenToInterface;
import com.szadowsz.controlP5.cast.callback.CListener;
import com.szadowsz.controlP5.cast.plug.CPlug;
import com.szadowsz.controlP5.cast.plug.CPlugBase;
import com.szadowsz.controlP5.cast.plug.CPlugToInterface;
import com.szadowsz.controlP5.drawable.controller.CGroup;
import processing.core.PApplet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class CBroadcaster implements CListenToInterface<CBroadcaster>, CPlugBase {

	// PApplet method name
	public static final String EVENT_CALLBACK_METHOD = "controlEvent"; // name of the method used for value change events

	// Field and Method Caches used for Reflection
	private static Map<Class<?>, Field[]> fieldcache = new HashMap<Class<?>, Field[]>();
	private static Map<Class<?>, Method[]> methodcache = new HashMap<Class<?>, Method[]>();

	private ControlP5 cRoot; // the cRoot interface that this broadcaster is used by

	private ArrayList<CListener> listeners = new ArrayList<>(); // event listeners
	private HashMap<PApplet, CPlug> eventPlugs = new HashMap<>(); // global event reflection caller for each active PApplet

	private boolean isBroadcast; // controls whether global broadcasts are made

	/**
	 * Method to find all visible fields for a Particular Class. Used to help update a field after a
	 * controller value has changed.
	 * 
	 * @param theClass - the class to find the fields for
	 * @return a list of the class's fields
	 */
	private static Field[] getFieldsFor(Class<?> theClass) {
		if (!fieldcache.containsKey(theClass)) {
			fieldcache.put(theClass, theClass.getDeclaredFields());
		}
		return fieldcache.get(theClass);
	}

	/**
	 * Method to find all visible methods for a Particular Class. Used to help call a method after a
	 * controller value has changed.
	 * 
	 * @param theClass - the class to find the methods for
	 * @return a list of the class's methods
	 */
	private static Method[] getMethodFor(Class<?> theClass) {
		if (!methodcache.containsKey(theClass)) {
			methodcache.put(theClass, theClass.getDeclaredMethods());
		}
		return methodcache.get(theClass);
	}

	/**
	 * Broadcaster Constructor to initialise all fields required for the callbacks
	 * 
	 * @param theControlP5 - GUI Interface
	 */
	public CBroadcaster(ControlP5 theRoot) {
		cRoot = theRoot;

		// Initialise the first global event plug
		addApplet(cRoot.papplet);
	}

	/**
	 * Convenience Method to print/log encountered errors while using reflection to call a class's method
	 * 
	 * @param theMethod - the method that we tried
	 * @param theException - the exception that we received
	 */
	private void printMethodError(Method theMethod, Exception theException) {
		ControlP5.logger.severe("An error occured while forwarding a Controller event, please check your code at " + theMethod.getName() + " "
				+ "exception:  " + theException);
		theException.printStackTrace();
	}

	/**
	 * Method to create a ControllerPlug object that maps a controller to a field or method allowing us to
	 * make reflective calls against it
	 * 
	 * @param theObject - the object we wish to map to the controller
	 * @param theTargetName - the method/field target name
	 * @param theAcceptClassList - the field/method parameter types we are looking for
	 * @return the Plug to link the controller to the object
	 */
	private CPlug checkObject(final Object theObject, final String theTargetName, final Class<?>[] theAcceptClassList) {
		Class<?> theClass = theObject.getClass(); // first we get the object's class

		// Next we look to see if we can find a method that matches
		CPlug plug = checkObjectForMethod(theClass, theObject, theTargetName, theAcceptClassList);

		if (plug != null) {
			return plug;

		} else { // if we have not found a match, move on to looking at fields

			Field[] fields = getFieldsFor(theClass); // get all declared fields

			main: for (int i = 0; i < fields.length; i++) { // check if they match our target name
				if ((fields[i].getName()).equals(theTargetName)) {

					// check if they match our desired type
					for (int j = 0; j < theAcceptClassList.length; j++) {
						if (fields[i].getType() == theAcceptClassList[j]) {
							return new CPlug(theObject, fields[i], CPlugCallType.FIELD);
						}
					}
					break main; // if it is not of our type break as field names are unique
				}
			}
		}
		return null;
	}

	/**
	 * Method to create a ControllerPlug object that maps a controller to a method allowing us to make
	 * reflective calls against it
	 * 
	 * @param theClass - the object's class for convenience
	 * @param theObject - the object we wish to map to the controller
	 * @param theTargetName - the method target name
	 * @param theAcceptClassList - the method parameter types we are looking for
	 * @return the Plug to link the controller to the object
	 */
	private CPlug checkObjectForMethod(Class<?> theClass, final Object theObject, final String theTargetName, final Class<?>[] theAcceptClassList) {

		Method[] methods = getMethodFor(theClass); // get declared methods for that class

		main: for (int i = 0; i < methods.length; i++) { // go through all methods
			if ((methods[i].getName()).equals(theTargetName)) {

				if (methods[i].getParameterTypes().length == 1) { // CallbackEvent method

					if (theAcceptClassList.length == 1 && theAcceptClassList[0] == CEvent.class) {
						if (methods[i].getParameterTypes()[0] == theAcceptClassList[0]) {
							return new CPlug(theObject, methods[i], CPlugCallType.EVENT);
						}
					}

					for (int j = 0; j < theAcceptClassList.length; j++) { // primitive-ish parameter method
						if (methods[i].getParameterTypes()[0] == theAcceptClassList[j]) {
							return new CPlug(theObject, methods[i], CPlugCallType.METHOD);
						}
					}
				} else if (methods[i].getParameterTypes().length == 0) { // No Parameter Method
					return new CPlug(theObject, methods[i], CPlugCallType.METHOD);
				}
				break main; // if it is not of our type break, TODO investigate if this is always necessary
			}
		}
		return null;
	}

	/**
	 * Private Reflection method to invoke a method
	 * 
	 * @param theObject - the object whose method we are invoking
	 * @param theMethod - the method we are invoking
	 * @param theParam - the method parameters we are supplying
	 */
	private void invokeMethod(final Object theObject, final Method theMethod, final Object[] theParam) {
		try {
			if (theParam[0] == null) {
				theMethod.invoke(theObject, new Object[0]);
			} else {
				theMethod.invoke(theObject, theParam);
			}
		} catch (IllegalArgumentException e) {
			ControlP5.logger.warning(e.toString());
			/**
			 * TODO thrown when plugging a String method/field.
			 */
		} catch (IllegalAccessException e) {
			printMethodError(theMethod, e);
		} catch (InvocationTargetException e) {
			printMethodError(theMethod, e);
		} catch (NullPointerException e) {
			printMethodError(theMethod, e);
		}

	}

	/**
	 * Private Reflection method to set a field value
	 * 
	 * @param theObject - the object that we are setting the field for
	 * @param theField - the field we are setting
	 * @param theParam - the value we are setting
	 */
	private void invokeField(final Object theObject, final Field theField, final Object theParam) {
		try {
			theField.set(theObject, theParam);
		} catch (IllegalAccessException e) {
			ControlP5.logger.warning(e.toString());
		}
	}

	/**
	 * Method to update a field / invoke method based on a Plug
	 * 
	 * @param thePlug - the plug that holds the binding data
	 * @param theValue - the object value / parameter we are supplying
	 */
	private void invokeTarget(final CPlug thePlug, Object theValue) {
		if (thePlug.checkType(CPlugCallType.METHOD)) { // convert the object into a method parameter
			invokeMethod(thePlug.getObject(), thePlug.getMethod(), thePlug.getMethodParameter(theValue));

		} else if (thePlug.checkType(CPlugCallType.FIELD)) { // convert the object into a field parameter
			invokeField(thePlug.getObject(), thePlug.getField(), thePlug.getFieldParameter(theValue));

		}
	}

	/**
	 * Method to broadcast that the controller value has changed to all plugs attached to the specified
	 * controller
	 * 
	 * @param theEvent - the event record
	 * @return this Broadcaster
	 */
	private void broadcastToPlug(CEvent theEvent) {
		if (theEvent.isController()) { // then if it is a controller

			// Attempt to invoke connected method/field plugs
			for (CPlug cp : theEvent.getController().getPlugs()) {
				if (cp.checkType(CPlugCallType.EVENT)) {
					invokeMethod(cp.getObject(), cp.getMethod(), new Object[] { theEvent });
				} else {
					invokeTarget(cp, theEvent.getValue());
				}
			}

		} else if (theEvent.isGroup()) { // if it is a group
			CGroup<?> group = theEvent.getGroup();

			if (group instanceof CPlugToInterface<?>) { // see if it implements the plug interface
				CPlugToInterface<?> plugGroup = (CPlugToInterface<?>) group;

				// if it does run through its plugs
				for (CPlug cp : plugGroup.getPlugs()) {

					if (cp.checkType(CPlugCallType.EVENT)) { // if it is an event pass on the event
						invokeMethod(cp.getObject(), cp.getMethod(), new Object[] { theEvent });

					} else { // otherwise do a normal target call
						invokeTarget(cp, theEvent.getValue());
					}
				}
			}
		}
	}

	/**
	 * Method to broadcast that an action has occurred to all Callback Listeners and plugs attached to the specified
	 * controller
	 * 
	 * @param theEvent - the event record
	 * @return this Broadcaster
	 */
	public CBroadcaster broadcast(CEvent theEvent) {
		if (isBroadcast()) {
			for (CListener listener : listeners) { // TODO handle event type filter here
				listener.controlEvent(theEvent); // first notify all listeners
			}

			// then process all plugs in case of a value change
			if (theEvent.getAction() == CActionType.BROADCAST) {
				broadcastToPlug(theEvent);
			}

			// finally call the correct PApplet method, if one exists
			PApplet applet = cRoot.papplet; // theEvent.getInterface().getApplet();
			if (eventPlugs.containsKey(applet)) {
				invokeMethod(applet, eventPlugs.get(applet).getMethod(), new Object[] { theEvent });
			}
		}
		return this;
	}

	/**
	 * Method to create a binding between an Object and a Controller
	 * 
	 * @param theObject - the object we are binding to the controller
	 * @param theControllerName - the name of the controller we are binding
	 * @param theTargetName - the target method/field
	 * @return this Broadcaster
	 */
	public boolean plug(Object theObject, final String theControllerName, final String theTargetName) {
		return plug(theObject, cRoot.getController(theControllerName), theTargetName);
	}

	/**
	 * Method to create a binding between an Object and a Controller
	 * 
	 * @param theObject - the object we are binding to the controller
	 * @param theController - the controller we are binding
	 * @param theTargetName - the target method/field
	 * @return this Broadcaster
	 */
	public boolean plug(Object theObject, final CPlugToInterface<?> theController, final String theTargetName) {
		return plug(theObject, theController, theTargetName, theController.getAcceptedClassList());
	}

	/**
	 * Method to create a binding between an Object and a Controller
	 * 
	 * @param theObject - the object we are binding to the controller
	 * @param theController - the controller we are binding
	 * @param theTargetName - the target method/field
	 * @param acceptclasslist - the accepted classed to plug to, defaults to list in ControllerPlugBase if removed
	 * @return true if connected, false otherwise
	 */
	public boolean plug(Object theObject, final CPlugToInterface<?> theController, final String theTargetName, Class<?>[] acceptclasslist) {

		if (theController != null) { // try to create the plug
			CPlug theControllerPlug = checkObject(theObject, theTargetName, acceptclasslist);

			if (theControllerPlug == null) { // if we have failed, exit
				return false;

			} else { // otherwise store the binding on the controller
				if (!theController.hasPlug(theControllerPlug)) {
					theController.addPlug(theControllerPlug);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to set the Broadcaster's status
	 * 
	 * @param theBroadcastFlag - the status to set it to: true if active, false otherwise
	 * @return this Broadcaster
	 */
	public CBroadcaster setBroadcast(boolean theBroadcastFlag) {
		isBroadcast = theBroadcastFlag;
		return this;
	}

	/**
	 * Method to identify the Broadcaster's status
	 * 
	 * @return true if the broadcaster is active, false otherwise
	 */
	public boolean isBroadcast() {
		return isBroadcast;
	}

	/**
	 * Method to get a CCallbackListener at a specified index
	 * 
	 * @param theIndex - the specified index of the listener
	 * @return the listener located at the specified index
	 * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())
	 */
	public CListener getListener(int theIndex) {
		return listeners.get(theIndex);
	}

	/**
	 * @return the number of listeners currently attached to the Broadcaster
	 */
	@Override
	public int getListenerSize() {
		return listeners.size();
	}

	/**
	 * Method to add 1 to n CCallbackListeners to the Broadcaster
	 * 
	 * @return the updated Broadcaster
	 */
	@Override
	public CBroadcaster addListener(CListener... theListeners) {
		for (CListener listener : theListeners) {
			listeners.add(listener);
		}
		return this;
	}

	/**
	 * Method to register an applet with the broadcaster
	 * 
	 * @param theApplet - the applet to register
	 * @return the update broadcaster
	 */
	public CBroadcaster addApplet(PApplet theApplet) {
		CPlug eventPlug = checkObject(theApplet, EVENT_CALLBACK_METHOD, new Class[] { CEvent.class });
		if (eventPlug != null) {
			eventPlugs.put(theApplet, eventPlug);
		}

		return this;
	}

	/**
	 * Method to remove 1 to n CCallbackListeners from the Broadcaster
	 * 
	 * @return the updated Broadcaster
	 */
	@Override
	public CBroadcaster removeListener(CListener... theListeners) {
		for (CListener listener : theListeners) {
			listeners.remove(listener);
		}
		return this;
	}

	/**
	 * Method to remove all CCallbackListeners from the Broadcaster
	 * 
	 * @return the updated Broadcaster
	 */
	@Override
	public CBroadcaster removeAllListeners() {
		listeners.clear();
		return this;
	}

	/**
	 * Method to unregister an applet with the broadcaster
	 * 
	 * @param theApplet - the applet to unregister
	 * @return the update broadcaster
	 */
	public CBroadcaster removeApplet(PApplet theApplet) {
		eventPlugs.remove(theApplet);
		return this;
	}
}

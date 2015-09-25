package com.szadowsz.controlP5.cast.plug;

import java.util.ArrayList;

/**
 * Interface to contain/separate out the plug method used by controllers
 * 
 * @author zakski
 * 
 * @param <T> - The Controller
 */
public interface CPlugToInterface<T> extends CPlugBase {

	/**
	 * Method to plug the controller to a list of objects
	 * 
	 * @param theObjects - the objects being targeted
	 * @return the controller
	 */
	public T plugTo(final Object[] theObjects);

	/**
	 * Method to plug the controller to a list of objects
	 * 
	 * @param theObjects - the objects being targeted
	 * @param theTargetName - the field/method name
	 * @return the controller
	 */
	public T plugTo(final Object[] theObjects, String theTargetName);

	/**
	 * Method to plug the controller to an object
	 * 
	 * @param theObject - the object being targeted
	 * @return the controller
	 */
	public T plugTo(Object theObject);

	/**
	 * Method to plug the controller to an object
	 * 
	 * @param theObject - the object being targeted
	 * @param theTargetName - the field/method name
	 * @return the controller
	 */
	public T plugTo(Object theObject, String theTargetName);

	/**
	 * Method to unplug the controller from a list of objects
	 * 
	 * @param theObjects - the objects being removed
	 * @return the controller
	 */
	public T unplugFrom(final Object[] theObjects);

	/**
	 * Method to unplug the controller from an object
	 * 
	 * @param theObject - the object being removed
	 * @return the controller
	 */
	public T unplugFrom(final Object theObject);

	/**
	 * Method to check if a Controller Plug is already attached
	 * 
	 * @param thePlug - the plug to check
	 * @return true if attached, false otherwise
	 */
	public boolean hasPlug(CPlug thePlug);

	/**
	 * Method to get the list of attached Controller Plugs
	 * 
	 * @return the list of plugs
	 */
	public ArrayList<CPlug> getPlugs();

	/**
	 * Method to attach a Controller Plug to the Controller
	 * 
	 * @param thePlug - the plug to attach
	 * @return the controller
	 */
	public T addPlug(CPlug thePlug);

	/**
	 * Method to remove a Controller Plug from the Controller
	 * 
	 * @param thePlug - the plug to detach
	 * @return the controller
	 */
	public T removePlug(CPlug thePlug);

	/**
	 * Method to get the default accepted class list of a Controller
	 * 
	 * @return the class list
	 */
	public Class<?>[] getAcceptedClassList();
}

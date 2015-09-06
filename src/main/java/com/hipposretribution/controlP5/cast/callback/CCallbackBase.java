package com.hipposretribution.controlP5.cast.callback;

/**
 * CCallbackBase is an internal interface that contains all the common constants that the Listener and the Event need
 * 
 * @author Zakski
 *
 */
interface CCallbackBase {

	/**
	 * The type of Action that the Event has been caused by
	 * 
	 * <p>
	 * ENTER - The Mouse has entered of the Controller
	 * </p>
	 * 
	 * <p>
	 * LEAVE - The Mouse has moved outside of the Controller
	 * </p>
	 * 
	 * <p>
	 * PRESSED - The Mouse has been clicked inside of the Controller
	 * </p>
	 * 
	 * <p>
	 * RELEASED - The Mouse has been released inside of the Controller
	 * </p>
	 * 
	 * <p>
	 * RELEASED_OUTSIDE - The Mouse has been released outside of the Controller
	 * </p>
	 * 
	 * <p>
	 * BROADCAST - The Controller's value has Changed
	 * </p>
	 * 
	 * @author Zakski
	 *
	 */
	public enum CActionType {
		ENTER, LEAVE, PRESSED, RELEASED, RELEASED_OUTSIDE, BROADCAST;
	}

	/**
	 * The type of Controller that the Event has occurred for
	 * 
	 * <p>
	 * UNDEFINED - unknown type
	 * </p>
	 * 
	 * <p>
	 * CONTROLLER - it has been created by a normal controller
	 * </p>
	 * 
	 * <p>
	 * GROUP - it has been created by a general controller group
	 * </p>
	 * 
	 * <p>
	 * TAB - it has been created by a tab
	 * </p>
	 * 
	 * @author Zakski
	 *
	 */
	public enum CEventType {
		UNDEFINED, CONTROLLER, TAB, GROUP;
	}
}

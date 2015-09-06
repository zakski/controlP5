package com.hipposretribution.controlP5.cast.callback;

import com.hipposretribution.controlP5.drawable.controller.CElement;
import com.hipposretribution.controlP5.drawable.controller.CGroup;
import com.hipposretribution.controlP5.drawable.controller.CInterface;
import com.hipposretribution.controlP5.drawable.controller.group.CTab;

// import com.hipposretribution.p5g.draw.controller.CControllerInterface;

/**
 * A CCallbackEvent is sent when a controller action such as enter, leave, press, etc has occurred or the
 * Controller's value has otherwise changed.
 */
public class CEvent implements CCallbackBase {

	protected final CInterface<?> controller; // the controller the even has happened for
	protected final CEventType type; // the type of controller the event has happened for
	protected final CActionType action; // the action that the event triggered for

	/**
	 * Controller Constructor for Control Events
	 * 
	 * @param theController - the Controller the event is triggering for
	 */
	public CEvent(CElement<?> theController, CActionType theAction) {
		controller = theController;
		action = theAction;
		type = CEventType.CONTROLLER;
	}

	/**
	 * Controller Group Constructor for Control Events
	 * 
	 * @param theTab - the Tab the event is triggering for
	 */
	public CEvent(CTab theTab, CActionType theAction) {
		controller = theTab;
		action = theAction;
		type = CEventType.TAB;
	}

	/**
	 * Controller Group Constructor for Control Events
	 * 
	 * @param theCGroup - the Controller Group the event is triggering for
	 */
	public CEvent(CGroup<?> theCGroup, CActionType theAction) {
		controller = theCGroup;
		action = theAction;
		type = CEventType.GROUP;
	}

	/**
	 * Method checks if the ControlEvent was triggered by a controller
	 * 
	 * @see com.hippsoretribution.controlP5.drawable.control.CPluggableInterface
	 * @return boolean true if it comes from a controller, false otherwise
	 */
	public boolean isController() {
		return type == CEventType.CONTROLLER;
	}

	/**
	 * Method checks if the ControlEvent was triggered by a CGroup
	 * 
	 * @see com.hippsoretribution.controlP5.drawable.control.CGroup
	 * @return true if it comes from a controller group, false otherwise
	 */
	public boolean isGroup() {
		return type == CEventType.GROUP;
	}

	/**
	 * Method checks if the ControlEvent was triggered by a tab
	 * 
	 * @see com.hipposretribution.controlP5.drawable.controller.group.CTab.hippsoretribution.controlP5.drawable.control.Tab
	 * @return boolean true if it comes from a tab, false otherwise
	 */
	public boolean isTab() {
		return type == CEventType.TAB;
	}

	/**
	 * Checks if the ControlEvent originates from a specific Controller or CGroup.
	 * 
	 * @param theController - the specified controller
	 * @return true if it comes from the controller, false otherwise
	 */
	public boolean isFrom(CInterface<?> theController) {
		return controller.equals(theController);
	}

	public String getName() {
		return controller.getName();
	}

	/**
	 * Method to get the Action that has occurred
	 * 
	 * @return Action Type of ENTER, LEAVE, PRESSED, RELEASED, RELEASED_OUTSIDE or BROADCAST
	 */
	public CActionType getAction() {
		return action;
	}

	/**
	 * Method to get the Controller Type
	 * 
	 * @return Event Type of CONTROLLER, TAB, or GROUP
	 */
	public CEventType getType() {
		return type;
	}

	/**
	 * Method returns the interface sending the ControlEvent.
	 * 
	 * @return the raw interface
	 */
	public CInterface<?> getInterface() {
		return controller;
	}
	
	/**
	 * Method returns the instance of the controller sending the ControlEvent.
	 * 
	 * @return a Controller if the event type is a controller, else null
	 */
	public CElement<?> getController() {
		return (isController()) ? ((CElement<?>) controller) : null;
	}

	/**
	 * Method returns the instance of the tab that evoked the ControlEvent
	 * 
	 * @return a CGroup if the event type is group, else null
	 */
	public CTab getTab() {
		return (isTab()) ? ((CTab) controller) : null;
	}

	/**
	 * Method returns the instance of the group that evoked the ControlEvent
	 * 
	 * @return a CGroup if the event type is group, else null
	 */
	public CGroup<?> getGroup() {
		return (isGroup()) ? ((CGroup<?>) controller) : null;
	}

	public Object getValue() {
		return controller.getValue();
	}
}

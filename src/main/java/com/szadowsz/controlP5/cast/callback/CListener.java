package com.szadowsz.controlP5.cast.callback;


/**
 * Use a CCallbackListener to listen for controller actions such as enter, leave, press, etc has occurred or for if a
 * controller's value has otherwise changed.
 */
public interface CListener extends CCallbackBase {

	public void controlEvent(CEvent theEvent);

}

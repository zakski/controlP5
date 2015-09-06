package com.hipposretribution.controlP5.input.mouse;


public interface MouseEventHandler {

	public void mousePressedEvent();
	public void mouseAndroidPressedEvent();
	
	public void mouseReleasedEvent();
	public void mouseAndroidReleasedEvent();
	
	public void mouseWheelEvent(int movement);
}

package com.hipposretribution.controlP5.input.mouse;

import java.util.ArrayList;

import processing.event.MouseEvent;

import com.hipposretribution.controlP5.ControlWindow;

/**
 * A pointer by default is linked to the mouse and stores the x and y position as well as the pressed and
 * released state. The pointer can be accessed by its getter method {@link ControlWindow#getPointer()}. Then
 * use {@link com.hippsoretribution.controlP5.ControlWindow#set(int, int)} to alter its position or invoke {
 * {@link com.hippsoretribution.controlP5.ControlWindow#pressed()} or
 * {@link com.hippsoretribution.controlP5.ControlWindow#released()} to change its state. To disable the mouse
 * and enable the Pointer use {@link com.hippsoretribution.controlP5.ControlWindow#enable()} and
 * {@link com.hippsoretribution.controlP5.ControlWindow#disable()} to default back to the mouse as input
 * parameter.
 */

public class MousePointer {

	protected int mouseX, mouseY;
	protected int pmouseX, pmouseY;

	protected boolean mouseOver;
	protected boolean mouseLock;
	protected boolean mousePressed;

	protected boolean pmouseReleased; // Android
	protected boolean pmousePressed; // Android

	protected boolean enabled = true;

	protected ArrayList<MouseEventHandler> handlers; // the handlers to inform of key events

	public MousePointer(){
		handlers = new ArrayList<>();
	}
	
	public MousePointer setX(int theX) {
		mouseX = theX;
		return this;
	}

	public MousePointer setY(int theY) {
		mouseY = theY;
		return this;
	}

	public int getY() {
		return mouseY;
	}

	public int getX() {
		return mouseX;
	}

	public int getPreviousX() {
		return pmouseX;
	}

	public int getPreviousY() {
		return pmouseY;
	}

	public MousePointer set(int theX, int theY) {
		setX(theX);
		setY(theY);
		return this;
	}

	/**
	 * when in Android mode, call mouseEvent(int, int, boolean).
	 * 
	 * @param theX
	 * @param theY
	 * @param pressed
	 */
	public void mouseEvent(int theX, int theY, boolean pressed) {

		mouseX = theX;
		mouseY = theY;

		if (pressed && !pmousePressed) {
			for (MouseEventHandler handler : handlers) {
				handler.mouseAndroidPressedEvent();
			}
			pmousePressed = true;
			pmouseReleased = false;
		} else if (!pressed && !pmouseReleased) {
			mouseLock = false;
			for (MouseEventHandler handler : handlers) {
				handler.mouseAndroidReleasedEvent();
			}
			pmousePressed = false;
			pmouseReleased = true;
		}
	}

	/**
	 * @exclude
	 * @param event MouseEvent
	 */
	public void mouseEvent(MouseEvent event) {
		if (enabled) {
			pmouseX = mouseX;
			pmouseY = mouseY;

			mouseX = event.getX();
			mouseY = event.getY();

			if (event.getAction() == MouseEvent.PRESS) {
				mousePressed = true;
				for (MouseEventHandler handler : handlers) {
					handler.mousePressedEvent();
				}
			} else if (event.getAction() == MouseEvent.RELEASE) {
				mousePressed = false;
				mouseLock = false;
				for (MouseEventHandler handler : handlers) {
					handler.mouseReleasedEvent();
				}
			} else if (event.getAction() == MouseEvent.WHEEL) {
				for (MouseEventHandler handler : handlers) {
					handler.mouseWheelEvent(event.getCount());
				}
			}
		}
	}

	public boolean attachHandler(MouseEventHandler handle) {
		return handlers.add(handle);
	}

	public boolean detachHandler(MouseEventHandler handle) {
		return handlers.remove(handle);
	}

	public void enable() {
		enabled = false;
	}

	public void disable() {
		enabled = true;
	}

	public boolean isEnabled() {
		return !enabled;
	}

	public void setMouseOver(boolean isOver) {
		mouseOver = isOver;
	}

	public void setPressed(boolean isPressed) {
		mousePressed = isPressed;

	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setLock() {
		mouseLock = true;
	}

	public boolean isMouseLock() {
		return mouseLock;
	}
}

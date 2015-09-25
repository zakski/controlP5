package com.szadowsz.controlP5.drawable;

import com.szadowsz.controlP5.ControlWindow;
import processing.core.PApplet;

/**
 * Use a Canvas to draw custom graphics into a control window or the default sketch window.
 * 
 * The Canvas is an abstract class and must be extended by your custom Canvas class, see the ControlP5canvas
 * example for details.
 * 
 * @example controllers/ControlP5canvas
 * 
 */

public abstract class Canvas implements CDrawableBaseInterface {

	protected ControlWindow window;

	public final static int PRE = 0;

	public final static int POST = 1;

	protected int mode = PRE;

	public void setup(PApplet theApplet) {
	}

	/**
	 * controlWindowCanvas is an abstract class and therefore needs to be extended by your class. draw(PApplet
	 * theApplet) is the only method that needs to be overwritten.
	 */
	@Override
	public abstract void draw(PApplet theApplet);

	/**
	 * move a canvas to another controlWindow
	 * 
	 * @param theControlWindow
	 */
	public void moveTo(ControlWindow theControlWindow) {
		if (window != null) {
			window.removeCanvas(this);
		}
		theControlWindow.addCanvas(this);
	}

	/**
	 * get the drawing mode of a Canvas. this can be PRE or POST.
	 * 
	 * @return
	 */
	public final int mode() {
		return mode;
	}

	/**
	 * set the drawing mode to PRE. PRE is the default.
	 */
	public final void pre() {
		setMode(PRE);
	}

	/**
	 * set the drawing mode to POST.
	 */
	public final void post() {
		setMode(POST);
	}

	/**
	 * 
	 * @param theMode
	 */
	public final void setMode(int theMode) {
		if (theMode == PRE) {
			mode = PRE;
		} else {
			mode = POST;
		}
	}

	public final void setControlWindow(ControlWindow theControlWindow) {
		window = theControlWindow;
	}

	public final ControlWindow window() {
		return window;
	}
}

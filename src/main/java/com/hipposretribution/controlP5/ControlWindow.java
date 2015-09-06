package com.hipposretribution.controlP5;

/**
 * controlP5 is a processing gui library.
 * <p>
 * 2006-2012 by Andreas Schlegel
 * <p>
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version. This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * @author Andreas Schlegel (http://www.sojamo.de)
 * @modified 12/23/2012
 * @version 2.0.4
 */

import com.hipposretribution.controlP5.colour.CColor;
import com.hipposretribution.controlP5.colour.CColourInterface;
import com.hipposretribution.controlP5.drawable.CDrawableBaseInterface;
import com.hipposretribution.controlP5.drawable.CDrawableInterface;
import com.hipposretribution.controlP5.drawable.Canvas;
import com.hipposretribution.controlP5.drawable.controller.CElement;
import com.hipposretribution.controlP5.drawable.controller.CGroup;
import com.hipposretribution.controlP5.drawable.controller.CInterface;
import com.hipposretribution.controlP5.drawable.controller.group.CTab;
import com.hipposretribution.controlP5.drawable.controller.group.TextArea;
import com.hipposretribution.controlP5.drawable.controller.group.listbox.DropdownList;
import com.hipposretribution.controlP5.drawable.controller.group.listbox.ListBox;
import com.hipposretribution.controlP5.drawable.controller.lone.slider.Knob;
import com.hipposretribution.controlP5.drawable.controller.lone.slider.Slider;
import com.hipposretribution.controlP5.drawable.controller.lone.text.NumberBox;
import com.hipposretribution.controlP5.drawable.label.ControlFont;
import com.hipposretribution.controlP5.input.keys.KeyEventHandler;
import com.hipposretribution.controlP5.input.mouse.MouseEventHandler;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.event.KeyEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * the purpose of a control window is to shift controllers from the main window into a separate window. to
 * save cpu, a control window is not updated when not active - in focus. for the same reason the framerate is
 * set to 15. To constantly update the control window, use {@link ControlWindow#setUpdateMode(int)}
 *
 * @example controllers/ControlP5window
 */
public final class ControlWindow implements CColourInterface<ControlWindow>, KeyEventHandler, MouseEventHandler {

    protected ControlP5 cp5;

    protected CElement<?> isControllerActive;

    protected CColor color = new CColor();

    private String _myName = "main";

    protected PApplet _myApplet;

    protected ControllerList _myTabs;

    protected boolean isVisible = true;
    protected boolean isInit = false;
    protected boolean isRemove = false;
    protected boolean isAutoDraw;
    protected boolean isUpdate;
    protected boolean isDrawBackground = true;
    protected boolean isUndecorated = false;

    protected CDrawableBaseInterface _myDrawable;

    protected List<Canvas> _myCanvas;

    protected PVector autoPosition = new PVector(10, 30, 0);

    protected float tempAutoPositionHeight = 0;

    protected boolean rendererNotification = false;

    protected PVector positionOfTabs = new PVector(0, 0, 0);

    private int _myFrameCount = 0;

    private List<CInterface<?>> mouseoverlist;

    private boolean focused = true;

    /**
     * @exclude
     */
    public ControlWindow(final ControlP5 theControlP5, final PApplet theApplet) {
        mouseoverlist = new ArrayList<CInterface<?>>();
        cp5 = theControlP5;
        _myApplet = theApplet;
        isAutoDraw = true;
        init();
    }

    protected void init() {
        _myCanvas = new ArrayList<Canvas>();

        _myTabs = new ControllerList();

        _myTabs.add(new CTab(cp5, this, "global"));

        _myTabs.add(new CTab(cp5, this, "default"));

        activateTab((CTab) _myTabs.get(1));

		/*
         * register a post event that will be called by processing after the draw method has been finished.
		 */

        // processing pre 2.0 will not draw automatically if in P3D mode. in earlier versions of controlP5
        // this had been checked here and the user had been informed to draw controlP5 manually by adding
        // cp5.draw() to the sketch's draw function. with processing 2.0 and this version of controlP5
        // this notification does no longer exist.

        if (isInit == false) {
            _myApplet.registerMethod("pre", this);
            _myApplet.registerMethod("draw", this);
        }
        isInit = true;
    }

    /**
     * Method to set the Control Window's Colours. This will cascade down to all Controllers below it.
     *
     * @return the Control Window
     */
    @Override
    public ControlWindow setColour(CColor theColor) {
        color.set(theColor);
        for (int i = 0; i < getTabs().size(); i++) {
            ((CTab) getTabs().get(i)).setColour(theColor);
        }
        return this;
    }

    /**
     * Method to set the Control Window's Foreground Colour. This will cascade down to all Controllers below
     * it.
     *
     * @return the Control Window
     */
    @Override
    public ControlWindow setColourForeground(int colorForeground) {
        color.setForeground(colorForeground);
        for (int i = 0; i < getTabs().size(); i++) {
            ((CTab) getTabs().get(i)).setColourForeground(colorForeground);
        }
        return this;
    }

    /**
     * Method to set the Control Window's Background Colour. This will cascade down to all Controllers below
     * it.
     *
     * @return the Control Window
     */
    @Override
    public ControlWindow setColourBackground(int theColor) {
        color.setBackground(theColor);
        for (int i = 0; i < getTabs().size(); i++) {
            ((CTab) getTabs().get(i)).setColourBackground(theColor);
        }
        return this;
    }

    /**
     * Method to set the Control Window's Caption Label Colour. This will cascade down to all Controllers
     * below it.
     *
     * @return the Control Window
     */
    @Override
    public ControlWindow setColourCaption(int theColor) {
        color.setCaption(theColor);
        for (int i = 0; i < getTabs().size(); i++) {
            ((CTab) getTabs().get(i)).setColourCaption(theColor);
        }
        return this;
    }

    /**
     * Method to set the Control Window's Value Label Colour. This will cascade down to all Controllers below
     * it.
     *
     * @return the Control Window
     */
    @Override
    public ControlWindow setColourValue(int theColor) {
        color.setValue(theColor);
        for (int i = 0; i < getTabs().size(); i++) {
            ((CTab) getTabs().get(i)).setColourValue(theColor);
        }
        return this;
    }

    /**
     * Method to set the Control Window's Active Colour. This will cascade down to all Controllers below it.
     *
     * @return the Control Window
     */
    @Override
    public ControlWindow setColourActive(int theColor) {
        color.setActive(theColor);
        for (int i = 0; i < getTabs().size(); i++) {
            ((CTab) getTabs().get(i)).setColourActive(theColor);
        }
        return this;
    }

    /**
     * Method to get the Control Window's CColour
     *
     * @return the Control Window's Colours
     */
    @Override
    public CColor getColour() {
        return color;
    }

    public CTab getCurrentTab() {
        for (int i = 1; i < _myTabs.size(); i++) {
            if (((CTab) _myTabs.get(i)).isActive()) {
                return (CTab) _myTabs.get(i);
            }
        }
        return null;
    }

    public ControlWindow activateTab(String theTab) {

        for (int i = 1; i < _myTabs.size(); i++) {
            if (((CTab) _myTabs.get(i)).getName().equals(theTab)) {
                if (!((CTab) _myTabs.get(i)).isActive) {
                    resetMouseOver();
                }
                activateTab((CTab) _myTabs.get(i));
            }
        }
        return this;
    }

    public ControlWindow removeTab(CTab theTab) {
        _myTabs.remove(theTab);
        return this;
    }

    public CTab add(CTab theTab) {
        _myTabs.add(theTab);
        return theTab;
    }

    public CTab addTab(String theTab) {
        return getTab(theTab);
    }

    public ControlWindow activateTab(CTab theTab) {
        for (int i = 1; i < _myTabs.size(); i++) {
            if (_myTabs.get(i) == theTab) {
                if (!((CTab) _myTabs.get(i)).isActive) {
                    resetMouseOver();
                }
                ((CTab) _myTabs.get(i)).setActive(true);
            } else {
                ((CTab) _myTabs.get(i)).setActive(false);
            }
        }
        return this;
    }

    public ControllerList getTabs() {
        return _myTabs;
    }

    public CTab getTab(String theTabName) {
        return cp5.getTab(this, theTabName);
    }

    /**
     * Sets the position of the tab bar which is set to 0,0 by default. to move the tabs to y-position 100,
     * use cp5.window().setPositionOfTabs(new PVector(0,100,0));
     *
     * @param thePVector
     */
    public ControlWindow setPositionOfTabs(PVector thePVector) {
        positionOfTabs.set(thePVector);
        return this;
    }

    public ControlWindow setPositionOfTabs(int theX, int theY) {
        positionOfTabs.set(theX, theY, positionOfTabs.z);
        return this;
    }

    /**
     * Returns the position of the tab bar as PVector. to move the tabs to y-position 100, use
     * cp5.window().getPositionOfTabs().y = 100; or cp5.window().setPositionOfTabs(new PVector(0,100,0));
     *
     * @return PVector
     */
    public PVector getPositionOfTabs() {
        return positionOfTabs;
    }

    void setAllignmentOfTabs(int theValue, int theWidth) {
        // TODO
    }

    void setAllignmentOfTabs(int theValue, int theWidth, int theHeight) {
        // TODO
    }

    void setAllignmentOfTabs(int theValue) {
        // TODO
    }

    public void remove() {
        for (int i = _myTabs.size() - 1; i >= 0; i--) {
            ((CTab) _myTabs.get(i)).remove();
        }
        _myTabs.clear();
        _myTabs.clearDrawable();
    }

    /**
     * clear the control window, delete all controllers from a control window.
     */
    public ControlWindow clear() {
        remove();
        return this;
    }

    protected void updateFont(ControlFont theControlFont) {
        for (int i = 0; i < _myTabs.size(); i++) {
            ((CTab) _myTabs.get(i)).setFont(theControlFont);
        }
    }

    /**
     * @exclude
     */
    @ControlP5.Invisible
    public void updateEvents() {
        handleMouseOver();
        // handleMouseWheelMoved(); TODO work out if still necessary
        if (_myTabs.size() <= 0) {
            return;
        }
        ((CInterface<?>) _myTabs.get(0)).updateEvents();
        for (int i = 1; i < _myTabs.size(); i++) {
            ((CTab) _myTabs.get(i)).continuousUpdateEvents();
            if (((CTab) _myTabs.get(i)).isActive() && ((CTab) _myTabs.get(i)).isVisible()) {
                ((CInterface<?>) _myTabs.get(i)).updateEvents();
            }
        }
    }

    /**
     * returns true if the mouse is inside a controller. !!! doesnt work for groups yet.
     */
    public boolean isMouseOver() {
        // TODO doesnt work for all groups yet, only ListBox and DropdownList.
        if (_myFrameCount + 1 < _myApplet.frameCount) {
            resetMouseOver();
        }
        return isVisible ? cp5.getPointer().isMouseOver() : false;
    }

    public boolean isMouseOver(CInterface<?> theController) {
        return mouseoverlist.contains(theController);
    }

    public void resetMouseOver() {
        cp5.getPointer().setMouseOver(false);
        for (int i = mouseoverlist.size() - 1; i >= 0; i--) {
            mouseoverlist.get(i).setMouseOver(false);
        }
        mouseoverlist.clear();
    }

    public CInterface<?> getFirstFromMouseOverList() {
        if (getMouseOverList().isEmpty()) {
            return null;
        } else {
            return getMouseOverList().get(0);
        }
    }

    /**
     * A list of controllers that are registered with a mouseover.
     */
    public List<CInterface<?>> getMouseOverList() {
        return mouseoverlist;
    }

    private ControlWindow handleMouseOver() {
        for (int i = mouseoverlist.size() - 1; i >= 0; i--) {
            if (!mouseoverlist.get(i).isMouseOver() || !isVisible) {
                mouseoverlist.remove(i);
            }
        }
        cp5.getPointer().setMouseOver(mouseoverlist.size() > 0);
        return this;
    }

    public ControlWindow removeMouseOverFor(CInterface<?> theController) {
        mouseoverlist.remove(theController);
        return this;
    }

    public ControlWindow setMouseOverController(CInterface<?> theController) {
        if (!mouseoverlist.contains(theController) && isVisible && theController.isVisible()) {
            mouseoverlist.add(theController);
        }
        cp5.getPointer().setMouseOver(true);
        return this;
    }

    /**
     * updates all controllers inside the control window if update is enabled.
     *
     * @exclude
     */
    public void update() {
        ((CInterface<?>) _myTabs.get(0)).update();
        for (int i = 1; i < _myTabs.size(); i++) {
            ((CTab) _myTabs.get(i)).update();
        }
    }

    /**
     * enable or disable the update function of a control window.
     */
    public void setUpdate(boolean theFlag) {
        isUpdate = theFlag;
        for (int i = 0; i < _myTabs.size(); i++) {
            ((CInterface<?>) _myTabs.get(i)).setUpdate(theFlag);
        }
    }

    /**
     * check the update status of a control window.
     */
    public boolean isUpdate() {
        return isUpdate;
    }

    public ControlWindow addCanvas(Canvas theCanvas) {
        _myCanvas.add(theCanvas);
        theCanvas.setControlWindow(this);
        theCanvas.setup(_myApplet);
        return this;
    }

    public ControlWindow removeCanvas(Canvas theCanvas) {
        _myCanvas.remove(theCanvas);
        return this;
    }

    private boolean isReset = false;

    public ControlWindow pre() {

        if (_myFrameCount + 1 < _myApplet.frameCount) {
            if (isReset) {
                resetMouseOver();
                isReset = false;
            }
        } else {
            isReset = true;
        }

        if (papplet().focused != focused) {
            cp5.clearKeys();
            cp5.clearMouse();
            focused = papplet().focused;
        }

        return this;
    }

    /**
     * when in Android mode, call mouseEvent(int, int, boolean).
     *
     * @param theX
     * @param theY
     * @param pressed
     */
    @Override
    public void mouseAndroidPressedEvent() {
        updateEvents();
        mousePressedEvent();
    }

    @Override
    public void mouseAndroidReleasedEvent() {
        updateEvents();
        mouseReleasedEvent();
        for (CInterface<?> c : mouseoverlist) {
            if (c instanceof CElement) {
                ((CElement<?>) c).onLeave();
                ((CElement<?>) c).onRelease();
            } else if (c instanceof CGroup) {
                ((CGroup<?>) c).mouseReleased();
            }
        }
        resetMouseOver();

    }

    /**
     * @exclude draw content.
     */
    public void draw() {

        _myFrameCount = _myApplet.frameCount;

        if (cp5.isAndroid) {
            cp5.androidMouseEvent();
        } else {
            updateEvents();
        }
        if (isVisible) {

            // TODO save stroke, noStroke, fill, noFill, strokeWeight
            // parameters and restore after drawing controlP5 elements.

            int myRectMode = _myApplet.g.rectMode;

            int myEllipseMode = _myApplet.g.ellipseMode;

            int myImageMode = _myApplet.g.imageMode;

            _myApplet.pushStyle();
            _myApplet.rectMode(PConstants.CORNER);
            _myApplet.ellipseMode(PConstants.CORNER);
            _myApplet.imageMode(PConstants.CORNER);
            _myApplet.noStroke();

            if (_myDrawable != null) {
                _myDrawable.draw(_myApplet);
            }

            for (int i = 0; i < _myCanvas.size(); i++) {
                if ((_myCanvas.get(i)).mode() == Canvas.PRE) {
                    (_myCanvas.get(i)).draw(_myApplet);
                }
            }

            _myApplet.noStroke();
            _myApplet.noFill();
            int myOffsetX = (int) getPositionOfTabs().x;
            int myOffsetY = (int) getPositionOfTabs().y;
            int myHeight = 0;
            if (_myTabs.size() > 0) {
                for (int i = 1; i < _myTabs.size(); i++) {
                    if (((CTab) _myTabs.get(i)).isVisible()) {
                        if (myHeight < ((CTab) _myTabs.get(i)).height()) {
                            myHeight = ((CTab) _myTabs.get(i)).height();
                        }

                        // conflicts with Android, getWidth not found TODO

                        // if (myOffsetX > (papplet().getWidth()) - ((Tab) _myTabs.get(i)).width()) {
                        // myOffsetY += myHeight + 1;
                        // myOffsetX = (int) getPositionOfTabs().x;
                        // myHeight = 0;
                        // }

                        ((CTab) _myTabs.get(i)).setOffset(myOffsetX, myOffsetY);

                        if (((CTab) _myTabs.get(i)).isActive()) {
                            ((CTab) _myTabs.get(i)).draw(_myApplet);
                        }

                        if (((CTab) _myTabs.get(i)).updateLabel()) {
                            ((CTab) _myTabs.get(i)).drawLabel(_myApplet);
                        }
                        myOffsetX += ((CTab) _myTabs.get(i)).width();
                    }
                }
                ((CInterface<?>) _myTabs.get(0)).draw(_myApplet);
            }
            for (int i = 0; i < _myCanvas.size(); i++) {
                if ((_myCanvas.get(i)).mode() == Canvas.POST) {
                    (_myCanvas.get(i)).draw(_myApplet);
                }
            }

            // draw Tooltip here.
            cp5.getTooltip().draw(this);
            _myApplet.rectMode(myRectMode);
            _myApplet.ellipseMode(myEllipseMode);
            _myApplet.imageMode(myImageMode);
            _myApplet.popStyle();
        }
    }

    /**
     * Adds a custom context to a ControlWindow. Use a custom class which implements the CDrawable interface
     *
     * @see com.hipposretribution.controlP5.drawable.CDrawableInterface
     * @param theDrawable CDrawable
     */
    public ControlWindow setContext(CDrawableInterface theDrawable) {
        _myDrawable = theDrawable;
        return this;
    }

    /**
     * returns the name of the control window.
     */
    public String name() {
        return _myName;
    }

    @Override
    public void mousePressedEvent() {
        if (isVisible) {
            for (int i = 0; i < _myTabs.size(); i++) {
                if (((CInterface<?>) _myTabs.get(i)).setMousePressed(true)) {
                    cp5.getPointer().setLock();
                    return;
                }
            }
        }
    }

    @Override
    public void mouseReleasedEvent() {
        if (isVisible) {
            for (int i = 0; i < _myTabs.size(); i++) {
                ((CInterface<?>) _myTabs.get(i)).setMousePressed(false);
            }
        }
    }

    @Override
    public void mouseWheelEvent(int mouseWheelMoved) {
        if (mouseWheelMoved != 0) {
            CopyOnWriteArrayList<CInterface<?>> mouselist = new CopyOnWriteArrayList<CInterface<?>>(
                    mouseoverlist);
            for (CInterface<?> c : mouselist) {
                if (c.isVisible()) {
                    if (c instanceof CElement) {
                        ((CElement<?>) c).onScroll(mouseWheelMoved);
                    }
                    if (c instanceof CGroup) {
                        ((CGroup<?>) c).onScroll(mouseWheelMoved);
                    }
                    if (c instanceof Slider) {
                        ((Slider) c).scrolled(mouseWheelMoved);
                    } else if (c instanceof Knob) {
                        ((Knob) c).scrolled(mouseWheelMoved);
                    } else if (c instanceof NumberBox) {
                        ((NumberBox) c).scrolled(mouseWheelMoved);
                    } else if (c instanceof ListBox) {
                        ((ListBox) c).scrolled(mouseWheelMoved);
                    } else if (c instanceof DropdownList) {
                        ((DropdownList) c).scrolled(mouseWheelMoved);

                    } else if (c instanceof TextArea) {
                        ((TextArea) c).scrolled(mouseWheelMoved);
                    }
                    break;
                }
            }
        }
        mouseWheelMoved = 0;
    }

    /**
     * @exclude
     * @param theKeyEvent KeyEvent
     */
    @Override
    public void handleKeyEvent(KeyEvent theKeyEvent) {
        for (int i = 0; i < _myTabs.size(); i++) {
            ((CInterface<?>) _myTabs.get(i)).keyEvent(theKeyEvent);
        }
    }

    public void setBroadcast(boolean theBroadcastFlag) {
        for (int i = 0; i < _myTabs.size(); i++) {
            ((CInterface<?>) _myTabs.get(i)).setBroadcast(theBroadcastFlag);
        }
    }

    /**
     * get the papplet instance of the ControlWindow.
     */
    public PApplet papplet() {
        return _myApplet;
    }

    /**
     * sets the frame rate of the control window.
     *
     * @param theFrameRate
     * @return ControlWindow
     */
    public ControlWindow frameRate(int theFrameRate) {
        _myApplet.frameRate(theFrameRate);
        return this;
    }

    public ControlWindow show() {
        isVisible = true;
        return this;
    }

    /**
     * by default the background of a controlWindow is filled with a background color every frame. to enable
     * or disable the background from drawing, use setDrawBackgorund(true/false).
     *
     * @param theFlag
     * @return ControlWindow
     */
    public ControlWindow setDrawBackground(boolean theFlag) {
        isDrawBackground = theFlag;
        return this;
    }

    public boolean isDrawBackground() {
        return isDrawBackground;
    }

    public boolean isVisible() {
        return isVisible;
    }

    protected boolean isControllerActive(CElement<?> theController) {
        if (isControllerActive == null) {
            return false;
        }
        return isControllerActive.equals(theController);
    }

    protected ControlWindow setControllerActive(CElement<?> theController) {
        isControllerActive = theController;
        return this;
    }

    public ControlWindow toggleUndecorated() {
        setUndecorated(!isUndecorated());
        return this;
    }

    public ControlWindow setUndecorated(boolean theFlag) {
        if (theFlag != isUndecorated()) {
            isUndecorated = theFlag;
            _myApplet.frame.removeNotify();
            _myApplet.frame.setUndecorated(isUndecorated);
            _myApplet.setSize(_myApplet.width, _myApplet.height);
//			_myApplet.setBounds(0, 0, _myApplet.width, _myApplet.height);
            _myApplet.frame.setSize(_myApplet.width, _myApplet.height);
            _myApplet.frame.addNotify();
        }
        return this;
    }

    public boolean isUndecorated() {
        return isUndecorated;
    }

    public ControlWindow setPosition(int theX, int theY) {
        return setLocation(theX, theY);
    }

    public ControlWindow setLocation(int theX, int theY) {
        _myApplet.frame.setLocation(theX, theY);
        return this;
    }

    /**
     * hide the controllers and tabs of the ControlWindow.
     */
    public ControlWindow hide() {
        isVisible = false;
        cp5.getPointer().setMouseOver(false);
        return this;
    }
}

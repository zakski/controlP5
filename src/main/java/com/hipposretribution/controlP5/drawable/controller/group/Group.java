package com.hipposretribution.controlP5.drawable.controller.group;

import com.hipposretribution.controlP5.ControlP5;
import com.hipposretribution.controlP5.drawable.controller.CGroup;

public class Group extends ControlGroup<Group> {

	/**
	 * Convenience constructor to extend Group.
	 * 
	 * @example use/ControlP5extendController
	 * @param theControlP5
	 * @param theName
	 */
	public Group(ControlP5 theControlP5, String theName) {
		this(theControlP5, theControlP5.getDefaultTab(), theName, 0, 0, 99, 9);
		theControlP5.register(theControlP5.papplet, theName, this);
	}

	public Group(ControlP5 theControlP5, CGroup<?> theParent, String theName, int theX, int theY, int theW,
			int theH) {
		super(theControlP5, theParent, theName, theX, theY, theW, theH);
	}

}

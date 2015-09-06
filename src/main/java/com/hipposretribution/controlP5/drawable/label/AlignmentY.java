package com.hipposretribution.controlP5.drawable.label;

import processing.core.PApplet;

public enum AlignmentY {

	CENTER(PApplet.CENTER), BASELINE(PApplet.BASELINE), TOP(PApplet.TOP), BOTTOM(PApplet.BOTTOM), TOP_OUTSIDE(12), BOTTOM_OUTSIDE(
			13);

	public final int code;

	private AlignmentY(int ncode) {
		code = ncode;
	}
}

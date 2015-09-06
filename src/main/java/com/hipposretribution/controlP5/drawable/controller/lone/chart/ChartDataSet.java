package com.hipposretribution.controlP5.drawable.controller.lone.chart;

import java.util.ArrayList;
import java.util.ListIterator;

import processing.core.PConstants;
import processing.core.PGraphics;

import com.hipposretribution.controlP5.colour.CColor;

/**
 * Used by Chart, a chart data set is a container to store chart data.
 */
@SuppressWarnings("serial")
class ChartDataSet extends ArrayList<ChartData> {

	protected CColor colour;
	protected float strokeWeight = 1;
	protected int[] colors = new int[0];

	protected final String _myName;

	public ChartDataSet(String theName) {
		_myName = theName;
		colour = new CColor();
	}

	public CColor getColor() {
		return colour;
	}

	public ChartDataSet setColors(int... theColors) {
		colors = theColors;
		return this;
	}

	public int[] getColors() {
		return colors;
	}

	public int getColor(int theIndex) {
		if (colors.length == 0) {
			return getColor().getForeground();
		}
		if (colors.length == 2) {
			return PGraphics.lerpColor(colors[0], colors[1], theIndex / (float) size(), PConstants.RGB);
		}
		if (theIndex >= 0 && theIndex < colors.length) {
			return colors[theIndex];
		}
		return getColor(0);
	}

	public ChartDataSet setStrokeWeight(float theStrokeWeight) {
		strokeWeight = theStrokeWeight;
		return this;
	}

	public float getStrokeWeight() {
		return strokeWeight;
	}

	public float[] getValues() {
		float[] v = new float[size()];
		int n = 0;
		ListIterator<ChartData> litr = listIterator();
		while (litr.hasNext()) {
			v[n++] = litr.next().getValue();
		}
		return v;
	}

}

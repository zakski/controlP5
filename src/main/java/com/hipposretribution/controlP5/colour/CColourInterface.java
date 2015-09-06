package com.hipposretribution.controlP5.colour; // TODO work out this class' final resting place.

/**
 * Interface to enforce all Default Colour Methods
 * 
 * @author zakski
 * 
 * @param <T> an interface/class that extends/implements this interface
 */
public interface CColourInterface<T> {

	// Default Colour Schemes
	public final static CColor RETRO = new CColor(0xff00698c, 0xff003652, 0xff08a2cf, 0xffffffff, 0xffffffff);
	public final static CColor CP5BLUE = new CColor(0xff016c9e, 0xff02344d, 0xff00b4ea, 0xffffffff, 0xffffffff);
	public final static CColor RED = new CColor(0xffaa0000, 0xff660000, 0xffff0000, 0xffffffff, 0xffffffff);
	public final static CColor WHITE = new CColor(0x99ffffff, 0x55ffffff, 0xffffffff, 0xffffffff, 0xffffffff);
	
	
	/**
	 * Method to get the object's Colour
	 * 
	 * @return the object's Colour
	 */
	public T setColour(CColor theCcolour);
	
	/**
	 * Method to set the object's Foreground Colour
	 * 
	 * @param colorForeground - the foreground colour
	 * @return the object
	 */
	public T setColourForeground(int colorForeground);

	/**
	 * Method to set the object's Background Colour
	 * 
	 * @param colorBackground - the background colour
	 * @return the object
	 */
	public T setColourBackground(int colorBackground);

	/**
	 * Method to set the object's Caption Colour
	 * 
	 * @param colorCaption - the caption colour
	 * @return the object
	 */
	public T setColourCaption(int colorCaption);

	/**
	 * Method to set the object's Value Colour
	 * 
	 * @param colorValue - the value colour
	 * @return the object
	 */
	public T setColourValue(int colorValue);

	/**
	 * Method to set the object's Active Colour
	 * 
	 * @param colorActive - the active colour
	 * @return the object
	 */
	public T setColourActive(int colorActive);

	/**
	 * Method to get the object's Colour
	 * 
	 * @return the object's Colour
	 */
	public CColor getColour();

}

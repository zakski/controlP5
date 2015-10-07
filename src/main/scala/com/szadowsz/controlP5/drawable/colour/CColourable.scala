package com.szadowsz.controlP5.drawable.colour

/**
 * Trait to enforce all Default Colour Methods.
 *
 * @author Zakski : 02/10/2015.
 *
 * @tparam T a trait/class that extends/implements this trait.
 */
trait CColourable[+T] {

  protected val _colour: CColour = CColour.getGlobal

  /**
   * Method to get the object's Colour.
   *
   * @return the object's Colour.
   */
  def getColour: CColour = _colour

  /**
   * Method to set the object's colour.
   *
   * @param colour the object's new colour.
   * @return the updated object.
   */
  final def setColour(colour: CColour): T = {
    colour.copyTo(this)
    this.asInstanceOf[T]
  }

  /**
   * Method to set the object's foreground colour.
   *
   * @param foreground - the new foreground colour.
   * @return the updated object.
   */
  def setColourForeground(foreground: Int): T = {
    _colour.setForeground(foreground)
    this.asInstanceOf[T]
  }

  /**
   * Method to set the object's background colour.
   *
   * @param background - the new background colour.
   * @return the updated object.
   */
  def setColourBackground(background: Int): T = {
    _colour.setBackground(background)
    this.asInstanceOf[T]
  }

  /**
   * Method to set the object's caption label colour.
   *
   * @param caption - the new caption label colour.
   * @return the updated object.
   */
  def setColourCaption(caption: Int): T = {
    _colour.setCaption(caption)
    this.asInstanceOf[T]
  }

  /**
   * Method to set the object's value label colour.
   *
   * @param value - the new value label colour.
   * @return the updated object.
   */
  def setColourValue(value: Int): T = {
    _colour.setValue(value)
    this.asInstanceOf[T]
  }

  /**
   * Method to set the object's Active Colour
   *
   * @param colourActive - the active colour
   * @return the object
   */
  def setColourActive(colourActive: Int): T = {
    _colour.setForeground(colourActive)
    this.asInstanceOf[T]
  }
}
package com.szadowsz.controlP5.drawable.colour

/**
 * Trait to enforce all Default Colour Methods.
 *
 * @author Zakski : 02/10/2015.
 *
 * @tparam Self a trait/class that extends/implements this trait.
 */
trait CColourable[Self <: CColourable[Self]] {
  self: Self =>

  /**
   * The object's colour scheme.
   */
  protected val _colour: CColour = CColour.THEME_CP5BLUE

  /**
   * Method to get the object's Colour.
   *
   * @return the object's Colour.
   */
  final def getColour: CColour = _colour

  /**
   * Method to set the object's foreground colour.
   *
   * @return the foreground colour.
   */
  def getColourForeground: Int = {
    _colour.getForeground
  }

  /**
   * Method to set the object's background colour.
   *
   * @return the background colour.
   */
  final def getColourBackground: Int = {
    _colour.getBackground
  }

  /**
   * Method to set the object's caption label colour.
   *
   * @return the caption label colour.
   */
  final def getColourCaption: Int = {
    _colour.getCaption
  }

  /**
   * Method to set the object's value label colour.
   *
   * @return the value label colour.
   */
  final def getColourValue: Int = {
    _colour.getValue
  }

  /**
   * Method to set the object's Active Colour.
   *
   * @return the active colour.
   */
  final def getColourActive: Int = {
    _colour.getBackground
  }

  /**
   * Method to set the object's colour.
   *
   * @param colour the object's new colour.
   * @return the updated object.
   */
  def setColour(colour: CColour): Self = {
    colour.copyTo(this)
    this.asInstanceOf[Self]
  }

  /**
   * Method to set the object's foreground colour.
   *
   * @param foreground - the new foreground colour.
   * @return the updated object.
   */
  def setColourForeground(foreground: Int): Self = {
    _colour.setForeground(foreground)
    this.asInstanceOf[Self]
  }

  /**
   * Method to set the object's background colour.
   *
   * @param background - the new background colour.
   * @return the updated object.
   */
  def setColourBackground(background: Int): Self = {
    _colour.setBackground(background)
    this.asInstanceOf[Self]
  }

  /**
   * Method to set the object's caption label colour.
   *
   * @param caption - the new caption label colour.
   * @return the updated object.
   */
  def setColourCaption(caption: Int): Self = {
    _colour.setCaption(caption)
    this.asInstanceOf[Self]
  }

  /**
   * Method to set the object's value label colour.
   *
   * @param value - the new value label colour.
   * @return the updated object.
   */
  def setColourValue(value: Int): Self = {
    _colour.setValue(value)
    this.asInstanceOf[Self]
  }

  /**
   * Method to set the object's active colour.
   *
   * @param colourActive - the active colour.
   * @return the updated object.
   */
  def setColourActive(colourActive: Int): Self = {
    _colour.setForeground(colourActive)
    this.asInstanceOf[Self]
  }
}
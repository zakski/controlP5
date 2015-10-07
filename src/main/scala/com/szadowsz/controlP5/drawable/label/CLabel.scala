/**
 * controlP5 is a processing gui library.
 *
 * 2006-2012 by Andreas Schlegel
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version. This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * @author Andreas Schlegel (http://www.sojamo.de)
 */
package com.szadowsz.controlP5.drawable.label


import com.szadowsz.controlP5.drawable.CDrawable
import com.szadowsz.controlP5.drawable.label.CLabel.LabelType
import com.szadowsz.controlP5.drawable.label.style.{AlignmentX, AlignmentY, CLabelStyle}
import com.szadowsz.controlP5.drawable.layer.CLayer
import com.szadowsz.controlP5.drawable.widget.CBase
import processing.core.{PFont, PGraphics}

/**
 * A custom label using controlP5's BitFonts or PFont based ControlFonts.
 *
 *
 * @see controlP5.ControlFont
 * @example controllers/ControlP5Textlabel
 *
 */
object CLabel {

  /**
   * Internal Interface used to differ between single and multiple line labels without requiring us to check
   * which the label currently is each time we draw
   *
   */
  private[label] trait LabelType {
    /**
     * Method to draw the label according to its type
     *
     * @param graphics - the applet to draw the label on
     * @param theX - initial x-coordinate
     * @param theY - initial y-coordinate
     * @param theController - the controller the label is contained within
     */
    def draw(graphics : PGraphics, theX: Int, theY: Int, theController: CBase[_])

    /**
     * Method to format the label's width according to the label's specifications
     *
     * @return the label width
     */
    def getWidth: Int

    /**
     * Method to get the label's height according to the label's specifications
     *
     * @return the label's height
     */
    def getHeight: Int

    /**
     * Method to format the label's text height overflow according to the label's specifications. Used for
     * Multiline only.
     *
     * @return the label's height overflow
     */
    def getOverflow: Int

    /**
     * Method to format the label's text according to the label's specifications
     *
     * @return the label display text
     */
    def getTextFormatted: String
  }

}

class CLabel(layer: CLayer, theValue: String, theWidth: Int, theHeight: Int, theColor: Int) extends CDrawable {
  protected var _layer: CLayer = layer
  protected var _font: CFont = _layer.getFont
  protected var _text: String = theValue
  protected var _type: LabelType = new SinglelineLabel
  protected var _style: CLabelStyle = new CLabelStyle
  protected var _hasChanged: Boolean = true
  protected var _hasBackground: Boolean = false
  protected var _isFixedSize: Boolean = false
  protected var _isMultiline: Boolean = false
  protected var _isUpperCase: Boolean = true
  protected var _isVisible: Boolean = true
  protected var _height: Int = theWidth
  protected var _width: Int = theHeight
  protected var _colour: Int = theColor
  protected var _colourBackground: Int = 0xffffffff
  protected var _lineHeight: Int = 0
  protected var _textHeight: Int = 1
  protected var _alignX: AlignmentX = AlignmentX.LEFT
  protected var _alignY: AlignmentY = AlignmentY.TOP
  protected var _textAlign: AlignmentX = AlignmentX.LEFT
  protected var _paddingX: Int = 4
  protected var _paddingY: Int = 4
  protected var _offsetYratio: Float = 0

  def this(layer: CLayer, theValue: String) {
    this(layer, theValue, 0, 0, 0xffffffff)
  }

  private[label] def adjusted() {
    _hasChanged = false
  }

  def draw(graphics: PGraphics) {
    if (isVisible) {
      _font.adjust(graphics, this)
      draw(graphics, 0, 0)
    }
  }

  def draw(graphics: PGraphics, theX: Int, theY: Int) {
    if (isVisible) {
      graphics.pushMatrix()
      graphics.translate(_style.marginLeft, _style.marginTop)
      graphics.translate(theX, theY)
      if (_hasBackground) {
        var ww: Float = _style.paddingRight + _style.paddingLeft
        if (_style.backgroundWidth > -1) {
          ww += _style.backgroundWidth
        }
        else {
          ww += _font.getWidth
        }
        var hh: Float = _style.paddingBottom + _style.paddingTop
        if (_style.backgroundHeight > -1) {
          hh += _style.backgroundHeight
        }
        else {
          hh += _font.getHeight
        }
        graphics.fill(_colourBackground)
        graphics.rect(0, 1, ww, hh)
      }
      graphics.translate(_style.paddingLeft, _style.paddingTop)
      _font.draw(_layer, this)
      graphics.popMatrix()
    }
  }

  def draw(graphics: PGraphics, theX: Int, theY: Int, controller: CBase[_]) {
    if (isVisible) {
      _type.draw(graphics, theX, theY, controller)
    }
  }

  def hasChanged: Boolean = _hasChanged

  def isFixedSize: Boolean = if (isMultiline) false else isFixedSize

  def isMultiline: Boolean = _isMultiline

  def isUpperCase: Boolean = _isUpperCase

  def isVisible: Boolean = _isVisible

  def getColor: Int = _colour

  def getFont: CFont = _font

  def getFormattedText: String = _type.getTextFormatted

  def getHeight: Int = _type.getHeight

  def getOverflow: Int = _type.getOverflow

  def getText: String = _text

  def getStyle: CLabelStyle = _style

  def getWidth: Int = _type.getWidth

  def getTextHeight: Int = _font.getTextHeight

  def getLineHeight: Int = _lineHeight

  def setAlignment(theX: AlignmentX, theY: AlignmentY): CLabel = {
    _alignX = theX
    _alignY = theY
    this
  }

  def setAlignmentX(theX: AlignmentX): CLabel = {
    _alignX = theX
    this
  }

  def setAlignmentY(theY: AlignmentY): CLabel = {
    _alignY = theY
    this
  }

  def setBackground(theBackgroundFlag: Boolean): CLabel = {
    _hasBackground = theBackgroundFlag
    this
  }

  def setColor(theColor: Int): CLabel = {
    _colour = theColor
    this
  }

  def setColorBackground(theColor: Int): CLabel = {
    _colourBackground = theColor
    this
  }

  def setFixedSize(theValue: Boolean): CLabel = {
    _isFixedSize = theValue
    this
  }

  def setFont(thePFont: PFont): CLabel = {
    setFont(new CFont(thePFont))
  }

  def setFont(theFont: CFont): CLabel = {
    setLineHeight(theFont.getSize)
    _font = new CFont(theFont.getFont, theFont.getSize)
    _hasChanged = true
    this
  }

  def setHeight(theHeight: Int): CLabel = {
    _height = theHeight
    _hasChanged = true
    this
  }

  def setMultiline(theValue: Boolean): CLabel = {
    if (isMultiline != theValue) {
      _isMultiline = theValue
      _type = if (isMultiline) new MultiLineLabel else new SinglelineLabel
      _hasChanged = true
    }
    this
  }

  def setText(theValue: String): CLabel = {
    _text = theValue
    _hasChanged = true
    this
  }

  def setUpperCase(theValue: Boolean): CLabel = {
    _isUpperCase = theValue
    _hasChanged = true
    this
  }

  def setVisible(theValue: Boolean): CLabel = {
    _isVisible = theValue
    this
  }

  def setWidth(theWidth: Int): CLabel = {
    _width = theWidth
    _hasChanged = true
    this
  }

  def setPadding(thePaddingX: Int, thePaddingY: Int): CLabel = {
    _paddingX = thePaddingX
    _paddingY = thePaddingY
    this
  }

  def setPaddingX(thePaddingX: Int): CLabel = {
    _paddingX = thePaddingX
    this
  }

  def setPaddingY(thePaddingY: Int): CLabel = {
    _paddingY = thePaddingY
    this
  }

  def setSize(theSize: Int): CLabel = {
    _font.setSize(theSize)
    this
  }

  private[label] def setTextHeight(theHeight: Int): CLabel = {
    _textHeight = theHeight
    this
  }

  def setOffsetYratio(theValue: Float): CLabel = {
    _offsetYratio = theValue
    _hasChanged = true
    this
  }

  def getOffsetYratio: Float = _offsetYratio

  def setLineHeight(theValue: Int): CLabel = {
    _lineHeight = theValue
    _hasChanged = true
    this
  }

  /**
   * Internal Class to represent the operations needed for drawing the label on a single line
   *
   */
  private[label] class SinglelineLabel extends LabelType {
    /**
     * Method to translate the label according to its alignment settings
     *
     * @param graphics - the applet to draw the label on
     * @param control - the controller the label is contained within
     * @param alignX - the X Alignment setting
     * @param alignY - the Y Alignment setting
     */
    private def align(graphics: PGraphics, control: CBase[_], alignX: AlignmentX, alignY: AlignmentY) {
      var x: Int = 0
      var y: Int = 0
      alignX match {
        case AlignmentX.CENTER =>
          x = (control.getWidth - _font.getWidth) / 2
        case AlignmentX.LEFT =>
          x = _paddingX
        case AlignmentX.RIGHT =>
          x = control.getWidth - _font.getWidth - _paddingX
        case AlignmentX.LEFT_OUTSIDE =>
          x = -_font.getWidth - _paddingX
        case AlignmentX.RIGHT_OUTSIDE =>
          x = control.getWidth + _paddingX
       }
      alignY match {
        case AlignmentY.CENTER =>
          y = control.getHeight / 2 + _font.getTop - _font.getCenter
         case AlignmentY.TOP =>
          y = 0
         case AlignmentY.BOTTOM =>
          y = control.getHeight - _font.getHeight - 1
        case AlignmentY.BASELINE =>
          y = control.getHeight + _font.getTop - 1
        case AlignmentY.BOTTOM_OUTSIDE =>
          y = control.getHeight + _paddingY
        case AlignmentY.TOP_OUTSIDE =>
          y = -_font.getHeight - _paddingY
      }
      graphics.translate(x, y)
    }

    def draw(graphics : PGraphics, theX: Int, theY: Int, controller: CBase[_]) {
      _font.adjust(graphics, CLabel.this)
      graphics.pushMatrix()
      align(graphics, controller, _alignX, _alignY)
      CLabel.this.draw(graphics, theX, theY)
      graphics.popMatrix()
    }

    def getWidth: Int = if (isFixedSize) _width else _font.getWidth

    def getHeight: Int = _font.getHeight

    def getOverflow: Int =  -1

    def getTextFormatted: String = if (isUpperCase) _text.toUpperCase else _text
  }

  /**
   * Internal Class to represent the operations needed for drawing the label on multiple lines
   *
   */
  private[label] class MultiLineLabel extends LabelType {

    def draw(graphics : PGraphics, theX: Int, theY: Int, controller: CBase[_]) {
      _font.adjust(graphics, CLabel.this)
      CLabel.this.draw(graphics, theX, theY)
    }

    def getWidth: Int = _width

    def getHeight: Int = _height

    def getOverflow: Int = _font.getOverflow

    def getTextFormatted: String = if (isUpperCase) _text.toUpperCase else _text
  }

  def setTextAlignment(theAlignment: AlignmentX): CLabel = {
    _textAlign = theAlignment
    this
  }
}
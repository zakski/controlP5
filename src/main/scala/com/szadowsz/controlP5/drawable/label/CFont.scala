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
 *
 */
package com.szadowsz.controlP5.drawable.label

import com.szadowsz.controlP5.drawable.layer.CLayer
import processing.core.{PApplet, PFont, PGraphics}

import java.util.{ArrayList, List}

/**
 * A ControlFont is a container for a PFont that can be used to customize the font of a label. (Designing the
 * Font handling gave me a big headache, especially when it comes to calculating the dimensions of a font
 * which are not available at all times but only at certain times. The current status I suppose is a good
 * compromise and works for standard font handling cases. For any special cases it will be difficult to
 * convince me to make any changes.)
 *
 */
object CFont {

  private def checkFontSize(theFont: PFont): Int = {
    try {
      return theFont.getSize
    }
    catch {
      case e: NullPointerException => {
        System.out.println("ControlP5: could not find font-size details for font " + theFont.getName + ", use constructor ControlFont(PFont theFont, int theFontSize) to specify the font size.")
        return 10
      }
    }
  }

  def getWidthFor(theText: String, theLabel: CLabel, theApplet: PApplet): Int = {
    theApplet.textFont(theLabel.getFont.pfont, theLabel.getFont.size)
    theApplet.textWidth(theText).toInt
  }
}

class CFont(theFont: PFont, theFontSize: Int, theLineHeight: Int) {
  private[label] var pfont: PFont = theFont
  private[label] var txt: List[String]  = new ArrayList[String]
  private[label] var s: String = ""
  private var top: Int = 0
  private var bottom: Int = 0
  private var center: Int = 0
  private var height: Int = 0
  private var width: Int = 0
  private var baseline: Int = 0
  private var textHeight: Int = 1
  private var offset: Array[Int] = new Array[Int](2)
  private var size: Int = theFontSize

  def this(theFont: PFont, theFontSize: Int) {
   this(theFont, theFontSize, theFontSize + 2)
  }

  def this(theFont: PFont) {
    this(theFont, CFont.checkFontSize(theFont))
  }

  def setSize(theSize: Int) {
    size = theSize
  }

  def getSize: Int = size

  def getOffset(theIndex: Int): Int = offset(theIndex)

  def getTextHeight: Int = textHeight

  def getWidth: Int = width

  def getHeight: Int = height

  def getCenter: Int = center

  def getTop: Int = top

  def getBottom: Int = bottom

  def getBaseline: Int = baseline

  def getFont: PFont = pfont

  def adjust(graphics : PGraphics, theLabel: CLabel) {
    if (theLabel.hasChanged) {
      graphics.textFont(pfont, size)
      top = -graphics.textAscent.toInt
      bottom = graphics.textDescent.toInt
      center = -((-top - bottom) / 2)
      height = if (theLabel.isMultiline) theLabel.getHeight else (graphics.textAscent + graphics.textDescent).toInt
      width = if (theLabel.isMultiline) theLabel.getWidth else graphics.textWidth(theLabel.getFormattedText).toInt
      if (theLabel.isMultiline) {
        calculateHeight(graphics, theLabel)
      }
      theLabel.adjusted()
    }
  }

  private def calculateHeight(graphics: PGraphics, theLabel: CLabel) {
    txt.clear()
    var myString: String = theLabel.getFormattedText
    val paragraphs = myString.split("\n")
    myString = ""
    for (p <- paragraphs) {
      val words = p.split("\\s")
      for (w <- words) {
        if (graphics.textWidth(myString + w) < width) {
          myString += w + " "
        }
        else {
          txt.add(myString.substring(0, PApplet.max(0, myString.length - 1)))
          myString = w + " "
        }
      }
      txt.add(myString.substring(0, myString.length - 1))
      myString = ""
    }
    if (theLabel.getHeight % theLabel.getLineHeight != 0) {
      txt.add("")
    }
    textHeight = PApplet.round(txt.size * theLabel.getLineHeight)
    val maxLineNum: Int = PApplet.round(theLabel.getHeight / theLabel.getLineHeight)
    val offset: Int = (PApplet.max(0, txt.size - maxLineNum) * PApplet.abs(theLabel.getOffsetYratio)).toInt
    val lim: Int = PApplet.min(txt.size, maxLineNum)
    s = ""
      for(i <- 0 until lim){
          s += txt.get(i + offset) + "\n"
      }
  }

  def getOverflow: Int = textHeight - height

  def draw(layer: CLayer, label: CLabel) {
    val papplet: PApplet = layer.getPApplet
    var loadedFont: PFont = papplet.g.textFont
    val loadedSize: Float = papplet.g.textSize
    if (loadedFont == null) {
      papplet.textSize(loadedSize)
      loadedFont = papplet.g.textFont
    }
    val loadedAlign: Int = papplet.g.textAlign
    papplet.textFont(pfont, size)
   // papplet.textAlign(label.textAlign.code)
    papplet.fill(label.getColor)
    if (label.isMultiline) {
      papplet.fill(label.getColor)
      papplet.textLeading(label.getLineHeight)
      papplet.text(s, 0, 0, label.getWidth, label.getHeight)
    }
    else {
      papplet.translate(0, -top + 1)
      papplet.fill(label.getColor)
      papplet.textLeading(label.getLineHeight)
      papplet.text(label.getFormattedText, 0, 0)
    }
    papplet.textFont(loadedFont, loadedSize)
    papplet.textAlign(loadedAlign)
  }
}
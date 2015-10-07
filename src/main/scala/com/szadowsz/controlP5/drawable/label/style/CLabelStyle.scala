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
package com.szadowsz.controlP5.drawable.label.style

import java.io.Serializable


/**
 * Labels use the ControllerStyle class to store margin and padding information.
 *
 * @see com.hippsoretribution.controlP5.drawable.label.Label#getStyle()
 *
 * @example extra/ControlP5style
 */
@SerialVersionUID(3250201688970310633L)
class CLabelStyle extends Serializable {
  var paddingTop: Int = 0
  var paddingRight: Int = 0
  var paddingBottom: Int = 0
  var paddingLeft: Int = 0
  var marginTop: Int = 0
  var marginRight: Int = 0
  var marginBottom: Int = 0
  var marginLeft: Int = 0
  var background: Int = 0
  var backgroundWidth: Int = -1
  var backgroundHeight: Int = -1
  var color: Int = 0

  def margin(theValue: Int): CLabelStyle = {
    marginTop = theValue
    marginRight = theValue
    marginBottom = theValue
    marginLeft = theValue
     this
  }

  def padding(theValue: Int): CLabelStyle = {
    paddingTop = theValue
    paddingRight = theValue
    paddingBottom = theValue
    paddingLeft = theValue
     this
  }

  def setPadding(theTop: Int, theRight: Int, theBottom: Int, theLeft: Int): CLabelStyle = {
    padding(theTop, theRight, theBottom, theLeft)
     this
  }

  def setPaddingTop(theValue: Int): CLabelStyle = {
    paddingTop = theValue
     this
  }

  def setPaddingBottom(theValue: Int): CLabelStyle = {
    paddingBottom = theValue
     this
  }

  def setPaddingRight(theValue: Int): CLabelStyle = {
    paddingRight = theValue
     this
  }

  def setPaddingLeft(theValue: Int): CLabelStyle = {
    paddingLeft = theValue
     this
  }

  def margin(theTop: Int, theRight: Int, theBottom: Int, theLeft: Int): CLabelStyle = {
    marginTop = theTop
    marginRight = theRight
    marginBottom = theBottom
    marginLeft = theLeft
     this
  }

  def setMargin(theTop: Int, theRight: Int, theBottom: Int, theLeft: Int): CLabelStyle = {
    margin(theTop, theRight, theBottom, theLeft)
     this
  }

  def setMarginTop(theValue: Int): CLabelStyle = {
    marginTop = theValue
     this
  }

  def setMarginBottom(theValue: Int): CLabelStyle = {
    marginBottom = theValue
     this
  }

  def setMarginRight(theValue: Int): CLabelStyle = {
    marginRight = theValue
     this
  }

  def setMarginLeft(theValue: Int): CLabelStyle = {
    marginLeft = theValue
     this
  }

  def padding(theTop: Int, theRight: Int, theBottom: Int, theLeft: Int): CLabelStyle = {
    paddingTop = theTop
    paddingRight = theRight
    paddingBottom = theBottom
    paddingLeft = theLeft
     this
  }

  def moveMargin(theTop: Int, theRight: Int, theBottom: Int, theLeft: Int): CLabelStyle = {
    marginTop += theTop
    marginRight += theRight
    marginBottom += theBottom
    marginLeft += theLeft
     this
  }

  def movePadding(theTop: Int, theRight: Int, theBottom: Int, theLeft: Int): CLabelStyle = {
    paddingTop += theTop
    paddingRight += theRight
    paddingBottom += theBottom
    paddingLeft += theLeft
     this
  }
}
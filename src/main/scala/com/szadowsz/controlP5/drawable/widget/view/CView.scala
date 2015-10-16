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
package com.szadowsz.controlP5.drawable.widget.view

import com.szadowsz.controlP5.drawable.widget.controller.CController
import processing.core.PGraphics

/**
 * The trait CView can be used to define custom displays for controllers.
 *
 */
trait CView[T <: CController[T]] {

  /**
   * Any settings changes that are needed go here
   */
  def init(controller : T): Unit = {}

  /**
   * draws your custom controllers. display() will be called by a controller's draw() function and will pass
   * a reference of PApplet as well as the Controller itself to your custom display class.
   *
   * @param graphics
   * @param controller
   */
  def display(graphics : PGraphics, controller: T)
}
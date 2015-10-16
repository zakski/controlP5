/**
 * controlP5 is a processing gui library.
 *
 * 2006-2012 by Andreas Schlegel
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307 USA
 *
 * @author Andreas Schlegel (http://www.sojamo.de)
 */
package com.szadowsz.controlP5.drawable.widget.controller.text

import com.szadowsz.controlP5.behaviour.synch.{CPlug, CPluggable}
import com.szadowsz.controlP5.drawable.label.style.{AlignmentX, AlignmentY}
import com.szadowsz.controlP5.drawable.label.{CFont, CLabel}
import com.szadowsz.controlP5.drawable.layer.CLayer
import com.szadowsz.controlP5.drawable.widget.controller.CController
import com.szadowsz.controlP5.drawable.widget.group.CGroup
import com.szadowsz.controlP5.drawable.widget.view.CView
import com.szadowsz.controlP5.input.keys.CKeyListener
import com.szadowsz.controlP5.input.mouse.controller.CMouseInteractable
import com.szadowsz.processing.SVector
import processing.core.{PFont, PGraphics}
import processing.event.KeyEvent

import java.awt.{event => j}

object CTextField {
  def getDefaultView: CView[CTextField] = new FieldView()

  def getFieldView: CView[CTextField] = new FieldView()

  def getLabelView: CView[CTextField] = new LabelView()

  def getDefaultFilter: CTextFilter = new CDefaultFilter()

  private sealed class CDefaultFilter extends CTextFilter {
    override def isValid(code: Int): Boolean = true
  }

  private class LabelView extends CView[CTextField] {
    override def init(field: CTextField): Unit = {
      field._value.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER)
      field._caption.setPaddingX(5)
    }

    override def display(graphics: PGraphics, textField: CTextField) {
      textField._value.draw(graphics, 0, 0, textField)
    }
  }

  private class FieldView extends CView[CTextField] {

    override def init(field: CTextField): Unit = {
      field.initValueLabel(field._layer)
      field._caption.setAlignment(AlignmentX.LEFT_OUTSIDE, AlignmentY.CENTER)
      field._caption.setPaddingX(5)
    }

    override def display(graphics: PGraphics, textField: CTextField) {
      graphics.pushStyle()

      graphics.fill(textField.getColour.getBackground)
      graphics.rect(0, 0, textField.getWidth, textField.getHeight)
      graphics.noStroke()

      graphics.pushMatrix()
      graphics.pushStyle()

      graphics.fill(textField._colourCursor)
      textField._buffer.beginDraw()
      textField._buffer.background(0, 0)
      val text = textField._txtBuffer.toString
      val indexPos = CFont.getWidthFor(text.substring(0, textField._index), textField._value, textField._buffer)
      val dif = Math.max(indexPos - textField._value.getWidth, 0).toInt
      textField._value.draw(textField._buffer, -dif, 0, textField)
      textField._buffer.noStroke()
      if (textField._isFocus) {
        if (!textField._layer.getPApplet.keyPressed) {
          textField._buffer.fill(textField._colourCursor, (Math.abs(Math.sin(textField._layer.getPApplet.frameCount * 0.05f)) * 255).toFloat)
        } else {
          textField._buffer.fill(textField._colourCursor)
        }
        textField._buffer.rect(Math.max(1, Math.min(indexPos, textField._value.getWidth - 3)), 0, 1, textField.getHeight)
      }
      textField._buffer.endDraw()
      graphics.image(textField._buffer, 0, 0)
      //
      graphics.popStyle()
      graphics.popMatrix()

      graphics.fill(if (textField.isFocus) textField._colour.getActive else textField._colour.getForeground)
      graphics.rect(0, 0, textField.getWidth, 1)
      graphics.rect(0, textField.getHeight - 1, textField.getWidth, 1)
      graphics.rect(-1, 0, 1, textField.getHeight)
      graphics.rect(textField.getWidth, 0, 1, textField.getHeight)
      textField._caption.draw(graphics, 0, 0, textField)

      graphics.popStyle()
    }
  }

}

/**
 * @author Zakski : 13/10/2015.
 */
class CTextField(name: String, layer: CLayer, parent: CGroup[_, _], v: SVector, width: Int, height: Int)
  extends CController[CTextField](name, layer, parent, v, width, height)
  with CKeyListener with CPluggable[String, CTextField] with CMouseInteractable {

  /**
   * How the CTextField should be displayed.
   */
  override protected var _view: CView[CTextField] = initView()

  protected var _buffer: PGraphics = _layer.getPApplet.createGraphics(getWidth, getHeight)

  /**
   * The ignored character set. These characters are not processed.
   */
  protected var _ignored: Set[Int] = initIgnored()

  /**
   * The Field Value Label.
   */
  protected val _value: CLabel = initValueLabel(_layer)

  protected var _txtBuffer: StringBuffer = new StringBuffer()

  protected var _filter: CTextFilter = CTextField.getDefaultFilter

  protected var _index: Int = 0

  override protected val _plugClass: Class[String] = classOf[String]

  override protected var _plugs: List[CPlug[String]] = List()


  protected var _isFocus: Boolean = false

  protected var _isAutoClear: Boolean = false

  protected var _colourCursor: Int = _layer.getPApplet.color(0)

  def this(name: String, text: String, layer: CLayer, parent: CGroup[_, _], v: SVector, width: Int, height: Int) {
    this(name, layer, parent, v, width, height)
    setValue(text)
  }

  protected def initView(): CView[CTextField] = {
    val view = CTextField.getDefaultView
    view.init(this)
    view
  }

  protected def initValueLabel(layer: CLayer): CLabel = {
    val label = new CLabel(_layer, "", 0, 0, _colour.getValue)
    label.setUpperCase(false)
    label.setFixedSize(true)
    label.setText("")
    label.setColor(_colour.getValue)
    label.setAlignment(AlignmentX.LEFT,AlignmentY.CENTER)
    label
  }

  protected def initIgnored(): Set[Int] = {
    Set(
      j.KeyEvent.VK_SHIFT,
      j.KeyEvent.VK_ALT,
      j.KeyEvent.VK_CONTROL,
      j.KeyEvent.VK_TAB,
      j.KeyEvent.VK_META
    )
  }

  def getValue: String = _value.getText

  def setValue(text: String): Unit = {
    _value.setText(text)
  }

  /**
   * check if the textfield is active and in focus.
   *
   * @return boolean
   */
  def isFocus: Boolean = _isFocus

  def isAutoClear: Boolean = _isAutoClear


  def setFocus(flag: Boolean): Unit = {
    _isFocus = flag
  }

  def setColorCursor(colour: Int): Unit = {
    _colourCursor = colour
  }

  def setFont(font: PFont): Unit = {
    setFont(new CFont(font))
  }

  def setFont(font: CFont): Unit = {
    _caption.setFont(font)
    _value.setFont(font)
  }

  def clear(): Unit = {
    _txtBuffer = new StringBuffer()
    _value.setText("")
    _index = 0
  }

  def setAutoClear(flag: Boolean): Unit = {
    _isAutoClear = flag
  }

  protected def isPrintable(key : Int): Boolean ={
    val block = Character.UnicodeBlock.of(key)
    !Character.isISOControl(key) && key != j.KeyEvent.CHAR_UNDEFINED &&
      block != null && block != Character.UnicodeBlock.SPECIALS
  }

  def insertChar(index: Int, key: Int): Unit = {
     if (isPrintable(key)) {
      if (_filter.isValid(key)) {
        _txtBuffer.insert(index, key.toChar)
        _value.setText(_txtBuffer.toString)
        if (index <= _index)
          _index = Math.min(_txtBuffer.length(), _index + 1)
      }
    }
  }

  def deleteChar(index: Int): Unit = {
    if (_txtBuffer.length() > 0 && index > 0) {
      _txtBuffer.deleteCharAt(index - 1)
      _value.setText(_txtBuffer.toString)
      _index = Math.max(_index - 1, 0)
    }
  }

  /**
   * Method to handle a Key Event in a responsible manner.
   *
   * @param event the Processing Key Event that has occurred.
   */
  override def keyEvent(event: KeyEvent): Unit = {
    if (_isFocus && event.getAction == KeyEvent.PRESS) {
      if (!_ignored.contains(event.getKeyCode)) {
        event.getKeyCode match {
          case j.KeyEvent.VK_ENTER =>
            _plugs.foreach(_.invokeTarget(_txtBuffer.toString))
            _isFocus = false
            if (_isAutoClear) {
              clear()
            }

          case j.KeyEvent.VK_BACK_SPACE => deleteChar(_index)

          case j.KeyEvent.VK_RIGHT => _index = Math.min(_txtBuffer.length(), _index + 1)

          case j.KeyEvent.VK_LEFT => _index = Math.max(_index - 1, 0)

          case key => insertChar(_index, event.getKey)
        }
      }
    }
  }

  /**
   * Method to handle the controller's reaction to the mouse being released.
   */
  override def mouseReleasedEvent(): Unit = {
    _isFocus = _isMouseInside
    _isPressed = false
  }


  override def setSize(width: Int, height: Int): Unit = {
    super.setSize(width, height)
    _buffer = _layer.getPApplet.createGraphics(getWidth, getHeight)
  }


  /**
   * Method to check if the controller has a specified point (usually the mouse pointer) within its bounds.
   *
   * @param position the point to check against
   * @return true if its inside, false otherwise
   */
  override def isInside(position: SVector): Boolean = {
    val start = getAbsolutePosition
    start.x < position.x && start.x + _width > position.x && start.y < position.y && start.y + _height > position.y
  }
}

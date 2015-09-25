package com.szadowsz.processing

import processing.core.PVector

object SVector {

  def apply(): SVector = new SVector()

  def apply(x : Float, y : Float): SVector = new SVector(x,y)

  def apply(x : Float, y : Float, z : Float): SVector = new SVector(x,y,z)

  def apply(vector : PVector): SVector = new SVector(vector)
}

/**
 * PVector class enhanced for Scala.
 *
 * @author Zakski : 07/09/2015.
 */
class SVector(x: Float, y: Float, z: Float) extends PVector(x, y, z) {

  def this(that: PVector) {
    this(that.x, that.y, that.z)
  }

  def this(x: Float, y: Float) {
    this(x, y, 0)
  }

  def this() {
    this(0, 0, 0)
  }

  def +(that: PVector) = new SVector(this.x + that.x, this.y + that.y, this.z + that.z)

  def -(that: PVector) = new SVector(this.x - that.x, this.y - that.y, this.z - that.z)

  def /(scalar: Float) = new SVector(this.x / scalar, this.y / scalar, this.z / scalar)

  def *(scalar: Float) = new SVector(this.x * scalar, this.y * scalar, this.z * scalar)

}

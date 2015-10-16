package com.szadowsz.controlP5.drawable.widget

/**
 * @author Zakski : 16/10/2015.
 */
trait StrictSelf[T <: StrictSelf[T]] { self: T =>
  type Self >: self.type <: T
}
package com.szadowsz.controlP5.behaviour.synch

import com.szadowsz.controlP5.drawable.CIdentification

import scala.annotation.varargs

/**
 * Interface to contain/separate out the plug method used by controllers
 *
 * @tparam T the type of plug we are expecting.
 * @tparam C the controller class
 *
 * @author Zakski : 29/09/2015.
 */
trait CPluggable[T, C <: CPluggable[T, C]] {
  self: CIdentification =>

  /**
   * The expected plug class, for convenience.
   */
  protected val _plugClass: Class[T]

  /**
   * The list of attached plugs
   */
  protected var _plugs: List[CPlug[T]]

  /**
   * Convenience Method to get only the plugs that invoke a function.
   *
   * @return the filtered plug list.
   */
  def getFunctionPlugs: List[CPlug[T]] = _plugs.filter(_.getCallType == CPlugType.FUNCTION)

  /**
   * Convenience Method to get only the plugs that invoke a method.
   *
   * @return the filtered plug list.
   */
  def getMethodPlugs: List[CPlug[T]] = _plugs.filter(_.getCallType == CPlugType.METHOD)

  /**
   * Convenience Method to get only the plugs that invoke a function or a method.
   *
   * @return the filtered plug list.
   */
  def getProcedurePlugs: List[CPlug[T]] = {
    _plugs.filter(p => p.getCallType == CPlugType.FUNCTION || p.getCallType == CPlugType.METHOD)
  }

  /**
   * Convenience Method to get only the plugs that invoke a field.
   *
   * @return the filtered plug list.
   */
  def getFieldPlugs: List[CPlug[T]] = _plugs.filter(_.getCallType == CPlugType.FIELD)

  /**
   * Convenience Method to get only the plugs that invoke accessors.
   *
   * @return the filtered plug list.
   */
  def getAccessorPlugs: List[CPlug[T]] = _plugs.filter(_.getCallType == CPlugType.ACCESSOR)

  /**
   * Convenience Method to get only the plugs that invoke a field or accessors.
   *
   * @return the filtered plug list.
   */
  def getValuePlugs: List[CPlug[T]] = {
    _plugs.filter(p => p.getCallType == CPlugType.FIELD || p.getCallType == CPlugType.ACCESSOR)
  }

  /**
   * Convenience Method to get only the plugs that invoke a field or accessors.
   *
   * @return the filtered plug list.
   */
  def getPlugs: List[CPlug[T]] = _plugs

  /**
   * Method to remove all existing plugs from the object.
   *
   * @return the updated pluggable object.
   */
  def clearPlugs(): C = {
    _plugs = List()
    this.asInstanceOf[C]
  }

  /**
   * Method to plug in a variety of objects, using the default name as a target.
   *
   * @param objects the objects to connect to.
   * @return the updated pluggable object.
   */
  @varargs
  def plugTo(objects: AnyRef*): C = {
    plugTo(getName, objects: _*)
  }

  /**
   * Method to plug in a variety of objects, using the default name as a target.
   *
   * @param objects the objects to connect to.
   * @param targetName - the field/method name
   * @return the updated pluggable object.
   */
  @varargs
  def plugTo(targetName: String, objects: AnyRef*): C = {
    _plugs = objects.foldLeft(_plugs) { (plugs, obj) =>
      obj match {
        case function: Function1[T, Unit] => plugs :+ CPlug(function)
        case _ => plugs :+ CPlug[T](_plugClass, obj, targetName)
      }
    }

    this.asInstanceOf[C]
  }

  /**
   * Method to unplug a list of objects.
   *
   * @param objects - the objects being removed
   * @return the updated pluggable object.
   */
  @varargs
  def unplugFrom(objects: AnyRef*): C = {
    _plugs = _plugs.filterNot(p => objects.contains(p.getBoundObject))
    this.asInstanceOf[C]
  }
}

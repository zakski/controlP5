package com.szadowsz.controlP5.behaviour.synch

import java.beans.PropertyDescriptor
import java.lang.reflect.{Field, Method, Modifier}
import java.security.InvalidParameterException
import scala.util.Try

/**
 * Companion Object with a variety of utility methods to aid in the specification of the variety of expected ways that
 * the GUI can interface with the backend via reflection.
 *
 * @author Zakski : 26/09/2015.
 */
object CPlug {

  /**
   * Private Method to look for both a Java and Scala style getter of a field.
   *
   * @param obj the object to investigate
   * @param name name of the field to find the getter for
   * @return the get method.
   */
  private def getGetter(obj: AnyRef, name: String) = {
    val c = obj.getClass
    Try(new PropertyDescriptor(name, c).getReadMethod).getOrElse(c.getDeclaredMethod(name))
  }

  /**
   * Private Method to look for both a Java and Scala style setter of a field.
   *
   * @param obj the object to investigate
   * @param name name of the field to find the setter for
   * @return the set method.
   */
  private def getSetter(obj: AnyRef, name: String) = {
    val c = obj.getClass
    val f = c.getDeclaredField(name)
    Try(new PropertyDescriptor(name, c).getWriteMethod).getOrElse(c.getDeclaredMethod(name + "_$eq", f.getType))
  }

  /**
   * Method apply method.
   *
   * @note not directly available due to the need to sus out the class type in the parent apply method.
   *
   * @param obj the instance that the method will be invoked upon.
   * @param method the method to invoke.
   * @tparam T the class type param for hinting / consistency around the method's parameter type.
   * @return the constructed CPlug instance.
   */
  protected def apply[T](obj: AnyRef, method: Method): CPlug[T] = {
    if (Modifier.isPublic(method.getModifiers)) {
      new CPlug[T](obj, method)
    } else {
      throw new InvalidParameterException("Method needs to be public to be accessed via reflection")
    }
  }

  /**
   * Field/Accessor Plug apply method.
   *
   * @note not directly available due to the need to sus out the class type in the parent apply method.
   *
   * @param obj the instance that the field/accessor will be invoked upon.
   * @param field the field to use or pull accessors from.
   * @tparam T the class type param for hinting / consistency around the field's type.
   * @return the constructed CPlug instance.
   */
  protected def apply[T](obj: AnyRef, field: Field): CPlug[T] = {
    if (Modifier.isPublic(field.getModifiers)) {
      new CPlug[T](obj, field)
    } else {
      val getter = getGetter(obj, field.getName)
      val setter = getSetter(obj, field.getName)
      new CPlug[T](obj, (getter, setter))
    }
  }

  /**
   * Function Plug apply method.
   *
   * @param function the provided scala function to invoke.
   * @tparam T the class type param for hinting / consistency around the function's parameter.
   * @return the constructed CPlug instance.
   */
  def apply[T](function: (T) => Unit): CPlug[T] = {
    new CPlug(function)
  }

  /**
   * General Plug apply method. Deconstructs to Field/Accessor or Method Plug apply method depending on initial
   * response.
   *
   * @todo investigate edge cases where this could potentially take the wrong path due to unexpected naming conventions
   *       or only desiring the use of once accessor.
   *
   * @note We check for a field, then a method.
   *
   * @param pClass class object to verify field and/or method parameter type is correct.
   * @param obj the instance that the field/method/accessor will be invoked upon.
   * @param name the name of the field / method.
   * @tparam T the class type param used in conjunction with the class instance to keep things honest.
   * @return the constructed CPlug instance.
   */
  def apply[T](pClass: Class[T], obj: AnyRef, name: String): CPlug[T] = {
    val field: Field = obj.getClass.getDeclaredFields.find(f => f.getName == name && f.getType == pClass).orNull
    if (field != null) {
      apply[T](obj, field)
    } else {
      val method: Method = obj.getClass.getDeclaredMethod(name, pClass)
      apply[T](obj, method)
    }
  }
}

/**
 * Class to specify a variety of expected ways that the GUI can interface with the backend via reflection.
 *
 * @constructor Base constructor to handle all possible reflection inputs.
 *
 * @param function the provided scala function to invoke.
 * @param obj the instance to invoke the binding upon if not a function plug.
 * @param field the field to manipulate.
 * @param accessors the get/set method pair to invoke.
 * @param method the method to invoke.
 * @param callType the assumption we are make as to which of the invocation bindings are available.
 * @tparam T the class of the field/function param/method param/accessor param & return type.
 *
 * @author Zakski : 26/09/2015.
 */
class CPlug[T] protected(function: (T) => Unit, obj: AnyRef, field: Field, accessors: (Method, Method), method: Method, callType: CPlugType) {

  /**
   * Scala Function to call if available.
   */
  protected val _boundFunction: (T) => Unit = function

  /**
   * Scala/Java object instance that the field/method/accessors are invoked on.
   */
  protected val _boundObject: AnyRef = obj

  /**
   * Scala/Java Method to call if available.
   */
  protected val _boundMethod: Method = method

  /**
   * Scala/Java Method to call if available.
   */
  protected val _boundAccessors: (Method, Method) = accessors

  /**
   * Java Field to call if available.
   *
   * @note Scala fields tend to be private when using reflection and are accessed via accessors instead.
   */
  protected val _boundField: Field = field

  /**
   * Enum to categorise the type of binding that has been performed.
   */
  protected val _boundType: CPlugType = callType

  /**
   * Function Plug constructor.
   *
   * @param function the provided scala function to invoke.
   */
  protected def this(function: (T) => Unit) {
    this(function, null, null, null, null, CPlugType.FUNCTION)
  }

  /**
   * Field Plug constructor.
   *
   * @param obj the instance to alter the field on.
   * @param field the field to manipulate.
   */
  protected def this(obj: AnyRef, field: Field) {
    this(null, obj, field, null, null, CPlugType.FIELD)
  }

  /**
   * Accessor Plug constructor.
   *
   * @param obj the instance to invoke the accessor on.
   * @param accessors the get/set method pair to invoke.
   */
  protected def this(obj: AnyRef, accessors: (Method, Method)) {
    this(null, obj, null, accessors, null, CPlugType.ACCESSOR)
  }

  /**
   * Method Plug constructor.
   *
   * @param obj the instance to invoke the method on.
   * @param method the method to invoke.
   */
  protected def this(obj: AnyRef, method: Method) {
    this(null, obj, null, null, method, CPlugType.METHOD)
  }

  /**
   * Method to get the invocation binding type.
   *
   * @return the type of the plug.
   */
  def getCallType: CPlugType = _boundType

  /**
   * Method to get the bound invocation object / function.
   *
   * @return the bound object / function.
   */
  def getBoundObject: AnyRef = if(_boundObject != null) _boundObject else _boundFunction


  /**
   * Method to invoke the targeted Field/Method/Function/Setter.
   *
   * @param value the parameter / new field value.
   */
  def invokeTarget(value: T): Unit = {
    _boundType match {
      case CPlugType.ACCESSOR => _boundAccessors._2.invoke(obj, value.asInstanceOf[AnyRef])
      case CPlugType.FUNCTION => _boundFunction(value)
      case CPlugType.FIELD => _boundField.set(_boundObject, value)
      case CPlugType.METHOD => _boundMethod.invoke(_boundObject, value.asInstanceOf[AnyRef])
    }
  }

  /**
   * Method to invoke the targeted Field/Getter.
   *
   * @return a value if plug is of the type field or accessor, otherwise none.
   */
  def invokeTarget(): Option[T] = {
    _boundType match {
      case CPlugType.ACCESSOR => Some(_boundAccessors._1.invoke(obj).asInstanceOf[T])
      case CPlugType.FUNCTION => None
      case CPlugType.FIELD => Some(_boundField.get(_boundObject).asInstanceOf[T])
      case CPlugType.METHOD => None
    }
  }
}

package com.szadowsz.controlP5.behaviour.synch

import com.szadowsz.controlP5.drawable.CIdentification
import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

/**
 * Test Suite for the CPluggable trait.
 *
 * @author Zakski : 29/09/2015.
 */
@RunWith(classOf[JUnitRunner])
class CPluggableSpec extends FunSpec {

  describe("The CPluggable Trait") {

    /**
     * test class to test the trait with
     */
    class TestPluggable(private val name : String) extends CPluggable[Boolean, TestPluggable] with CIdentification {
      override protected val _plugClass: Class[Boolean] = classOf[Boolean]
      override protected var _plugs: List[CPlug[Boolean]] = List()

      /**
       * Method to get the identifying name of the object.
       *
       * @return the name.
       */
      override def getName: String = name
    }

    it("should successfully bind a function") {
      val bind = (bool: Boolean) => println(bool)
      val pluggable = new TestPluggable("testBool")

      pluggable.plugTo(bind)

      assertResult(1)(pluggable.getFunctionPlugs.length)
    }

    it("should successfully bind to a case class with a default field name using accessors") {
      case class testBind(var testBool: Boolean)


      val bind = testBind(true)
      val pluggable = new TestPluggable("testBool")

      pluggable.plugTo(bind)

      assertResult(1)(pluggable.getAccessorPlugs.length)
      assertResult(true)(pluggable.getPlugs.head.invokeTarget().getOrElse(false))
    }

    it("should successfully bind to a case class with a specified field name using accessors") {
      case class testBind(var boolean: Boolean)


      val bind = testBind(true)
      val pluggable = new TestPluggable("testBool")

      pluggable.plugTo("boolean", bind)

      assertResult(1)(pluggable.getAccessorPlugs.length)
      assertResult(true)(pluggable.getPlugs.head.invokeTarget().getOrElse(false))
    }

    it("should successfully bind to a java class with a default field name") {
      val bind = new TestJavaObject()
      val pluggable = new TestPluggable("testBool")

      pluggable.plugTo(bind)

      assertResult(1)(pluggable.getFieldPlugs.length)
      assertResult(false)(pluggable.getPlugs.head.invokeTarget().getOrElse(true))
    }

    it("should successfully bind to a java class with a specified field name") {
      val bind = new TestJavaObject()
      val pluggable = new TestPluggable("testBool")

      pluggable.plugTo("testBool2", bind)

      assertResult(1)(pluggable.getFieldPlugs.length)
      assertResult(false)(pluggable.getPlugs.head.invokeTarget().getOrElse(true))
    }

    it("should successfully bind to a java class with a default method name") {
      val bind = new TestJavaObject()
      val pluggable = new TestPluggable("testMethod")

      pluggable.plugTo(bind)

      assertResult(1)(pluggable.getMethodPlugs.length)
    }

    it("should successfully bind to a java class with a specified method name") {
      val bind = new TestJavaObject()
      val pluggable = new TestPluggable("testBool")

      pluggable.plugTo("testMethod", bind)

      assertResult(1)(pluggable.getMethodPlugs.length)
    }

    it("should successfully unbind an object/anyref") {
      val bind = new TestJavaObject()
      val pluggable = new TestPluggable("testBool")

      pluggable.plugTo("testMethod", bind)
      assertResult(1)(pluggable.getMethodPlugs.length)

      pluggable.unplugFrom(bind)
      assertResult(0)(pluggable.getMethodPlugs.length)
    }
  }
}
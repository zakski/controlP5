package com.szadowsz.controlP5.behaviour.synch

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

import java.security.InvalidParameterException

/**
 * Test Suite for the CPlug class.
 *
 * @author Zakski : 29/09/2015.
 */
@RunWith(classOf[JUnitRunner])
class CPlugSpec extends FunSpec {

  describe("The CPlug Class") {

    it("should successfully invoke a Boolean function") {
      var testBool = false
      val testPlug = CPlug((b: Boolean) => testBool = b)

      testPlug.invokeTarget(true)

      assertResult(CPlugType.FUNCTION)(testPlug.getCallType)
      assertResult(true)(testBool)
    }

    it("should successfully invoke an Int function") {
      var testInt = 0
      val testPlug = CPlug((i: Int) => testInt = i)

      testPlug.invokeTarget(1)
      assertResult(CPlugType.FUNCTION)(testPlug.getCallType)
      assertResult(1)(testInt)
    }


    it("should successfully invoke a String function") {

      var testChar = 'a'
      val testPlug = CPlug((s: String) => testChar = s.last)

      testPlug.invokeTarget("xyz")

      assertResult(CPlugType.FUNCTION)(testPlug.getCallType)
      assertResult('z')(testChar)
    }

    it("should successfully set a Boolean via a Field Accessor") {
      class TestObj(var testBool: Boolean) {}

      val testObject = new TestObj(false)
      val testPlug = CPlug(java.lang.Boolean.TYPE, testObject, "testBool")

      testPlug.invokeTarget(true)

      assertResult(CPlugType.ACCESSOR)(testPlug.getCallType)
      assertResult(true)(testObject.testBool)
    }

    it("should successfully set an Int via a Field Accessor") {
      class TestObj(var testInt: Int) {}

      val testObject = new TestObj(0)
      val testPlug = CPlug(java.lang.Integer.TYPE, testObject, "testInt")

      testPlug.invokeTarget(1)

      assertResult(CPlugType.ACCESSOR)(testPlug.getCallType)
      assertResult(1)(testObject.testInt)
    }

    it("should successfully set a String via a Field Accessor") {
      class TestObj(var testString: String) {}

      val testObject = new TestObj("abc")
      val testPlug = CPlug(classOf[String], testObject, "testString")

      testPlug.invokeTarget("xyz")

      assertResult(CPlugType.ACCESSOR)(testPlug.getCallType)
      assertResult("xyz")(testObject.testString)
    }


    it("should successfully get a Boolean via a Field Accessor") {
      class TestObj(var testBool: Boolean) {}

      val testObject = new TestObj(false)
      val testPlug = CPlug(java.lang.Boolean.TYPE, testObject, "testBool")

      val testBool = testPlug.invokeTarget().getOrElse(true)

      assertResult(CPlugType.ACCESSOR)(testPlug.getCallType)
      assertResult(false)(testBool)
    }

    it("should successfully get an Int via a Field Accessor") {
      class TestObj(var testInt: Int) {}

      val testObject = new TestObj(0)
      val testPlug = CPlug(java.lang.Integer.TYPE, testObject, "testInt")

      val testInt = testPlug.invokeTarget().getOrElse(1)

      assertResult(CPlugType.ACCESSOR)(testPlug.getCallType)
      assertResult(0)(testInt)
    }

    it("should successfully get a String via a Field Accessor") {
      class TestObj(var testString: String) {}

      val testObject = new TestObj("abc")
      val testPlug = CPlug(classOf[String], testObject, "testString")

      val testString = testPlug.invokeTarget().getOrElse("xyz")

      assertResult(CPlugType.ACCESSOR)(testPlug.getCallType)
      assertResult("abc")(testString)
    }

    it("should successfully set a Boolean Field") {
      val testObject = new TestJavaObject()
      val testPlug = CPlug(java.lang.Boolean.TYPE, testObject, "testBool")

      testPlug.invokeTarget(true)

      assertResult(CPlugType.FIELD)(testPlug.getCallType)
      assertResult(true)(testObject.testBool)
    }

    it("should successfully set an Int Field") {
      val testObject = new TestJavaObject()
      val testPlug = CPlug(java.lang.Integer.TYPE, testObject, "testInt")

      testPlug.invokeTarget(1)

      assertResult(CPlugType.FIELD)(testPlug.getCallType)
      assertResult(1)(testObject.testInt)
    }

    it("should successfully set a String Field") {
      val testObject = new TestJavaObject()
      val testPlug = CPlug(classOf[String], testObject, "testString")

      testPlug.invokeTarget("xyz")

      assertResult(CPlugType.FIELD)(testPlug.getCallType)
      assertResult("xyz")(testObject.testString)
    }


    it("should successfully get a Boolean Field") {
      val testObject = new TestJavaObject()
      val testPlug = CPlug(java.lang.Boolean.TYPE, testObject, "testBool")

      val testBool = testPlug.invokeTarget().getOrElse(true)

      assertResult(CPlugType.FIELD)(testPlug.getCallType)
      assertResult(false)(testBool)
    }

    it("should successfully get an Int Field") {
      val testObject = new TestJavaObject()
      val testPlug = CPlug(java.lang.Integer.TYPE, testObject, "testInt")

      val testInt = testPlug.invokeTarget().getOrElse(1)

      assertResult(CPlugType.FIELD)(testPlug.getCallType)
      assertResult(0)(testInt)
    }

    it("should successfully get a String Field") {
      val testObject = new TestJavaObject()
      val testPlug = CPlug(classOf[String], testObject, "testString")

      val testString = testPlug.invokeTarget().getOrElse("xyz")

      assertResult(CPlugType.FIELD)(testPlug.getCallType)
      assertResult("abc")(testString)
    }

    it("should successfully invoke a Boolean method") {
      val testObject = new TestJavaObject()
      val testPlug = CPlug(java.lang.Boolean.TYPE, testObject, "testMethod")

      testPlug.invokeTarget(true)

      assertResult(CPlugType.METHOD)(testPlug.getCallType)
      assertResult(true)(testObject.testBool)
    }

    it("should successfully invoke an Int method") {
      val testObject = new TestJavaObject()
      val testPlug = CPlug(java.lang.Integer.TYPE, testObject, "testMethod")

      testPlug.invokeTarget(1)

      assertResult(CPlugType.METHOD)(testPlug.getCallType)
      assertResult(1)(testObject.testInt)
    }


    it("should successfully invoke a String method") {
      val testObject = new TestJavaObject()

      val testPlug = CPlug(classOf[String], testObject, "testMethod")

      testPlug.invokeTarget("xyz")

      assertResult(CPlugType.METHOD)(testPlug.getCallType)
      assertResult("xyz")(testObject.testString)
    }

    it("should unsuccessfully build with a nonexistent member") {
       intercept[NoSuchMethodException] {
        val testObject = new TestJavaObject()
        CPlug(classOf[String], testObject, "testFakeMember")
      }
    }

    it("should unsuccessfully build with a private method") {
      intercept[InvalidParameterException] {
        val testObject = new TestJavaObject()
        CPlug(classOf[String], testObject, "testPrivateMethod")
      }
    }

    it("should unsuccessfully build with a private field and no accessors") {
      intercept[NoSuchMethodException] {
        val testObject = new TestJavaObject()
        CPlug(classOf[String], testObject, "testInaccessibleField")
      }
    }

    it("should unsuccessfully build with a private accessor") {
      intercept[NoSuchMethodException] {
        val testObject = new TestJavaObject()
        CPlug(classOf[String], testObject, "testInaccessibleAccessor")
      }
    }
  }
}